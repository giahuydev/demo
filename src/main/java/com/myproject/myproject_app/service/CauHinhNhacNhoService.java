package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.CauHinhNhacNhoRequest;
import com.myproject.myproject_app.dto.response.CauHinhNhacNhoResponse;
import com.myproject.myproject_app.entity.Search_Schedule.CauHinhNhacNho;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.CauHinhNhacNhoMapper;
import com.myproject.myproject_app.repository.CauHinhNhacNhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CauHinhNhacNhoService {

    private final CauHinhNhacNhoRepository cauHinhNhacNhoRepository;
    private final CauHinhNhacNhoMapper cauHinhNhacNhoMapper;

    // 1. Xem chi tiết
    public CauHinhNhacNhoResponse getById(Integer id) {
        // SỬA: Bỏ Long.valueOf(), truyền trực tiếp id (Integer)
        CauHinhNhacNho entity = cauHinhNhacNhoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REMINDER_NOT_FOUND));
        return cauHinhNhacNhoMapper.toResponse(entity);
    }

    // --- THÊM MỚI: Hàm này để phục vụ Controller (Sửa lỗi findByUserId lúc nãy) ---
    public CauHinhNhacNhoResponse getByUserId(String userId) {
        // Gọi hàm mới đã sửa tên trong Repository
        CauHinhNhacNho entity = cauHinhNhacNhoRepository.findByLichHen_NguoiDung_IdNguoiDung(userId)
                .orElseThrow(() -> new AppException(ErrorCode.REMINDER_NOT_FOUND));
        return cauHinhNhacNhoMapper.toResponse(entity);
    }

    // 2. Cập nhật (Sửa thời gian hoặc Bật/Tắt)
    @Transactional
    public CauHinhNhacNhoResponse update(Integer id, CauHinhNhacNhoRequest request) {
        // SỬA: Bỏ Long.valueOf()
        CauHinhNhacNho entity = cauHinhNhacNhoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REMINDER_NOT_FOUND));

        // Kiểm tra xem người dùng có thay đổi thời gian nhắc không?
        boolean isTimeChanged = false;
        if (request.getThoiGianCanhBao() != null && !request.getThoiGianCanhBao().equals(entity.getThoiGianCanhBao())) {
            isTimeChanged = true;
        }
        if (request.getDonViThoiGian() != null && !request.getDonViThoiGian().equals(entity.getDonViThoiGian().name())) {
            isTimeChanged = true;
        }

        // Map dữ liệu mới vào Entity
        cauHinhNhacNhoMapper.updateCauHinh(entity, request);

        // LOGIC QUAN TRỌNG: Nếu thời gian thay đổi, phải reset trạng thái "đã gửi" để báo thức lại
        if (isTimeChanged) {
            entity.setDaGui(false);
            entity.setThoiGianGuiThucTe(null);
        }

        return cauHinhNhacNhoMapper.toResponse(cauHinhNhacNhoRepository.save(entity));
    }

    // 3. Xóa nhắc nhở lẻ
    @Transactional
    public void delete(Integer id) {
        // SỬA: Bỏ Long.valueOf()
        if (!cauHinhNhacNhoRepository.existsById(id)) {
            throw new AppException(ErrorCode.REMINDER_NOT_FOUND);
        }
        cauHinhNhacNhoRepository.deleteById(id);
    }
}