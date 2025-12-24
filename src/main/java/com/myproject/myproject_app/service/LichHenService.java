package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.CauHinhNhacNhoRequest;
import com.myproject.myproject_app.dto.request.LichHenCreationRequest;
import com.myproject.myproject_app.dto.request.LichHenUpdateRequest;
import com.myproject.myproject_app.dto.response.LichHenResponse;
import com.myproject.myproject_app.entity.Search_Schedule.CauHinhNhacNho;
import com.myproject.myproject_app.entity.Search_Schedule.DonViThoiGianEnum;
import com.myproject.myproject_app.entity.Search_Schedule.LichHen;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.LichHenMapper;
import com.myproject.myproject_app.repository.CauHinhNhacNhoRepository;
import com.myproject.myproject_app.repository.LichHenRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LichHenService {

    private final LichHenRepository lichHenRepository;
    private final CauHinhNhacNhoRepository cauHinhNhacNhoRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final LichHenMapper lichHenMapper;

    // --- 1. CREATE ---
    @Transactional
    public LichHenResponse createLichHen(Integer userId, LichHenCreationRequest request) {
        // Kiểm tra User
        NguoiDung user = nguoiDungRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Map Request -> Entity
        LichHen lichHen = lichHenMapper.toEntity(request);
        lichHen.setNguoiDung(user);
        lichHen.setNgayTao(LocalDateTime.now());
        lichHen.setNgayCapNhat(LocalDateTime.now());

        // Lưu Lịch Hẹn (Parent) trước để có ID
        LichHen savedLichHen = lichHenRepository.save(lichHen);

        // Xử lý danh sách Nhắc nhở (Children)
        if (request.getNhacNhos() != null && !request.getNhacNhos().isEmpty()) {
            List<CauHinhNhacNho> reminders = request.getNhacNhos().stream()
                    .map(req -> mapToCauHinhEntity(req, savedLichHen))
                    .collect(Collectors.toList());

            cauHinhNhacNhoRepository.saveAll(reminders);

            // Set lại vào object để trả về Response đầy đủ
            savedLichHen.setCauHinhNhacNho(new HashSet<>(reminders));
        }

        return lichHenMapper.toResponse(savedLichHen);
    }

    // --- 2. UPDATE ---
    @Transactional
    public LichHenResponse updateLichHen(Integer idLichHen, LichHenUpdateRequest request) {
        LichHen lichHen = lichHenRepository.findById(idLichHen)
                .orElseThrow(() -> new AppException(ErrorCode.LICH_HEN_NOT_FOUND));

        // Update thông tin cơ bản (Tên, ngày giờ...)
        lichHenMapper.updateLichHen(lichHen, request);
        lichHen.setNgayCapNhat(LocalDateTime.now());

        // Nếu người dùng gửi danh sách nhắc nhở mới -> XÓA CŨ, THÊM MỚI
        // (Đây là chiến lược đơn giản nhất để đồng bộ)
        if (request.getNhacNhos() != null) {
            // 1. Xóa hết nhắc nhở cũ
            cauHinhNhacNhoRepository.deleteAll(lichHen.getCauHinhNhacNho());
            lichHen.getCauHinhNhacNho().clear();

            // 2. Thêm nhắc nhở mới
            List<CauHinhNhacNho> newReminders = request.getNhacNhos().stream()
                    .map(req -> mapToCauHinhEntity(req, lichHen))
                    .collect(Collectors.toList());

            cauHinhNhacNhoRepository.saveAll(newReminders);
            lichHen.getCauHinhNhacNho().addAll(newReminders);
        }

        return lichHenMapper.toResponse(lichHenRepository.save(lichHen));
    }

    // --- 3. DELETE ---
    @Transactional
    public void deleteLichHen(Integer idLichHen) {
        if (!lichHenRepository.existsById(idLichHen)) {
            throw new AppException(ErrorCode.LICH_HEN_NOT_FOUND);
        }
        // Vì CascadeType.ALL, xóa Lịch Hẹn sẽ tự xóa hết Nhắc Nhở
        lichHenRepository.deleteById(idLichHen);
    }

    // --- 4. GET ONE ---
    public LichHenResponse getById(Integer idLichHen) {
        return lichHenRepository.findById(idLichHen)
                .map(lichHenMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.LICH_HEN_NOT_FOUND));
    }

    // --- 5. GET ALL BY USER ---
    public List<LichHenResponse> getAllByUserId(String userId) {
        // Có thể thêm filter ngày tháng nếu cần
        return lichHenRepository.findByNguoiDung_IdNguoiDung(userId).stream()
                .map(lichHenMapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- Helper Method ---
    private CauHinhNhacNho mapToCauHinhEntity(CauHinhNhacNhoRequest req, LichHen parent) {
        CauHinhNhacNho config = new CauHinhNhacNho();
        config.setLichHen(parent);
        config.setThoiGianCanhBao(req.getThoiGianCanhBao());

        // Map String -> Enum
        try {
            config.setDonViThoiGian(DonViThoiGianEnum.valueOf(req.getDonViThoiGian()));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }

        // Default values cho logic báo thức
        config.setKichHoat(true);
        config.setDaGui(false);

        return config;
    }
}