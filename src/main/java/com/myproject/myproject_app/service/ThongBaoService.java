package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.response.ThongBaoResponse;
import com.myproject.myproject_app.entity.Alert_Feedback.ThongBao;
import com.myproject.myproject_app.entity.Alert_Feedback.TrangThaiGuiEnum;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.ThongBaoMapper;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import com.myproject.myproject_app.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThongBaoService {

    private final ThongBaoRepository thongBaoRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final ThongBaoMapper thongBaoMapper;

    // ================= DÀNH CHO USER (APP/WEB) =================

    // 1. Xem danh sách (Inbox)
    public List<ThongBaoResponse> getAllByUserId(String userId) {
        if (!nguoiDungRepository.existsById(String.valueOf(userId))) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return thongBaoRepository.findByNguoiDung_IdNguoiDung(userId)
                .stream()
                .map(thongBaoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 2. Xem chi tiết (Khi User bấm vào trên App)
    // Logic: Nếu User chủ động bấm vào xem trên App, ta cũng tính là "Đã đọc" (daMo = true)
    @Transactional
    public ThongBaoResponse getById(Integer id) {
        ThongBao entity = thongBaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tồn tại"));

        // Cập nhật trạng thái đã mở
        markAsReadLogic(entity);

        return thongBaoMapper.toResponse(entity);
    }

    // 3. Xóa thông báo (User xóa khỏi hộp thư)
    @Transactional
    public void delete(Integer id) {
        if (!thongBaoRepository.existsById(id)) {
            throw new RuntimeException("Thông báo không tồn tại");
        }
        thongBaoRepository.deleteById(id);
    }

    // 4. Xóa tất cả thông báo của User (Dọn dẹp)
    @Transactional
    public void deleteAllByUser(String userId) {
        if (!nguoiDungRepository.existsById(String.valueOf(userId))) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        List<ThongBao> list = thongBaoRepository.findByNguoiDung_IdNguoiDung(userId);
        thongBaoRepository.deleteAll(list);
    }

    // ================= DÀNH CHO TRACKING & HỆ THỐNG =================

    // 5. Hàm này dùng cho TrackingController (Pixel) gọi
    @Transactional
    public void markAsRead(Integer id) {
        thongBaoRepository.findById(id).ifPresent(this::markAsReadLogic);
    }

    // Logic chung để set daMo = true
    private void markAsReadLogic(ThongBao entity) {
        if (!entity.isDaMo()) {
            entity.setDaMo(true);
            thongBaoRepository.save(entity);
        }
    }

    // 6. Hàm này dùng cho Scheduler/AI gọi để lưu thông báo mới
    @Transactional
    public ThongBao saveNotification(ThongBao thongBao) {
        return thongBaoRepository.save(thongBao);
    }

    public void guiThongBaoHeThong(NguoiDung nguoiNhan, String tieuDe, String noiDung) {
        ThongBao thongBao = ThongBao.builder()
                .nguoiDung(nguoiNhan)
                .tieuDe(tieuDe)
                .noiDung(noiDung)
                .thoiGianTao(LocalDateTime.now())
                .daMo(false)
                .trangThaiGui(TrangThaiGuiEnum.THANH_CONG)
                .build();

        thongBaoRepository.save(thongBao);
    }
}