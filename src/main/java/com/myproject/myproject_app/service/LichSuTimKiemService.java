package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.LichSuTimKiemRequest;
import com.myproject.myproject_app.dto.response.LichSuTimKiemResponse;
import com.myproject.myproject_app.entity.MultiSourceData.MoHinhDuBao;
import com.myproject.myproject_app.entity.Search_Schedule.LichSuTimKiem;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.LichSuTimKiemMapper;
import com.myproject.myproject_app.repository.LichSuTimKiemRepository;
import com.myproject.myproject_app.repository.MoHinhDuBaoRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LichSuTimKiemService {

    private final LichSuTimKiemRepository historyRepo;
    private final NguoiDungRepository userRepo;
    private final MoHinhDuBaoRepository modelRepo;  // ✅ THAY ĐỔI: Dùng ModelRepo
    private final LichSuTimKiemMapper historyMapper;

    // ✅ FIX: Thêm lịch sử tìm kiếm
    @Transactional
    public LichSuTimKiemResponse addSearchHistory(LichSuTimKiemRequest request) {
        log.info("Adding search history with tenNguon (maModelApi): {}", request.getTenNguon());

        // Validate User
        NguoiDung user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // ✅ FIX: Tìm Model trực tiếp theo maModelApi
        MoHinhDuBao model = modelRepo.findByMaModelApi(request.getTenNguon())
                .orElseGet(() -> {
                    // Backup: Thử tìm theo tenModelHienThi
                    log.warn("Không tìm thấy model theo maModelApi: {}, thử tìm theo tenModelHienThi",
                            request.getTenNguon());
                    return modelRepo.findByTenModelHienThi(request.getTenNguon())
                            .orElseThrow(() -> {
                                log.error("KHÔNG TÌM THẤY MODEL với tên: {}", request.getTenNguon());
                                return new AppException(ErrorCode.SOURCE_NOT_FOUND);
                            });
                });

        log.info("Found Model: {} (ID: {}, Nguồn: {})",
                model.getTenModelHienThi(),
                model.getId(),
                model.getNguonDuLieu().getTenChucNang());

        // Map Request -> Entity
        LichSuTimKiem history = historyMapper.toLichSuTimKiem(request);

        // Set relationships
        history.setNguoiDung(user);
        history.setModel(model);
        history.setThoiGianTim(LocalDateTime.now());

        LichSuTimKiem saved = historyRepo.save(history);
        return historyMapper.toResponse(saved);
    }

    // READ ONE: Lấy chi tiết 1 lịch sử
    public LichSuTimKiemResponse getHistoryById(Integer id) {
        LichSuTimKiem history = historyRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST));
        return historyMapper.toResponse(history);
    }

    // READ LIST: Lấy danh sách lịch sử theo User
    public List<LichSuTimKiemResponse> getHistoryByUser(String userId) {
        return historyRepo.findAllByNguoiDung_IdNguoiDungOrderByThoiGianTimDesc(userId)
                .stream()
                .map(historyMapper::toResponse)
                .collect(Collectors.toList());
    }

    // DELETE ONE: Xóa 1 lịch sử
    @Transactional
    public void deleteHistory(Integer historyId) {
        if (!historyRepo.existsById(historyId)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        historyRepo.deleteById(historyId);
    }

    // DELETE ALL: Xóa toàn bộ lịch sử của User
    @Transactional
    public void deleteAllHistory(String userId) {
        historyRepo.deleteAllByNguoiDung_IdNguoiDung(userId);
    }
}