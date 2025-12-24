package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CauHinhResponse {
    private Integer thoiGianBaoTruocMacDinh;
    private String donViThoiGian; // Trả về tên hiển thị "Phút", "Giờ"
    private boolean nhanEmail;
    private boolean nhanThongBaoDay;
}