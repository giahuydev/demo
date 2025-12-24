package com.myproject.myproject_app.dto.request;

import lombok.Data;

@Data
public class AnhCongDongCreationRequest {
    // ID người đăng
    private String userId;

    // Ảnh & Nội dung
    private String urlAnh;
    private String moTa;

    // Dữ liệu thời tiết (FE gửi chuỗi đơn giản)
    private String tinhTrangThoiTiet;
    private String camGiac;
    private Float luongMua;

    // Địa điểm
    private String diaDiem;
}