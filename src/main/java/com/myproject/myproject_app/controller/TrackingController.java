package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private final ThongBaoService thongBaoService;

    // Mã Base64 của 1 bức ảnh GIF trong suốt 1x1 pixel
    private static final byte[] PIXEL_BYTES = java.util.Base64.getDecoder().decode(
            "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7"
    );

    @GetMapping("/open/{idThongBao}")
    public ResponseEntity<byte[]> trackOpenEmail(@PathVariable Integer idThongBao) {
        try {
            log.info("Nhận tín hiệu mở mail cho thông báo ID: {}", idThongBao);

            // 1. Gọi Service để cập nhật trạng thái "Đã mở"
            thongBaoService.markAsRead(idThongBao);

        } catch (Exception e) {
            // Nếu có lỗi (VD: ID không tồn tại), vẫn log lại nhưng không throw lỗi ra ngoài
            // để bức ảnh vẫn tải được, tránh hiện icon "ảnh lỗi" trong mail người dùng
            log.error("Lỗi tracking email ID {}: {}", idThongBao, e.getMessage());
        }

        // 2. Trả về bức ảnh 1x1
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(PIXEL_BYTES);
    }
}