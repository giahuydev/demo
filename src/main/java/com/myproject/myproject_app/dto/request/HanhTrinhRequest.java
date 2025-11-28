package com.myproject.myproject_app.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HanhTrinhRequest {
    private String userId;
    private String tenNguon;

    private String tenHanhTrinh;

    private String diemDi;
    private Float viDoDi;
    private Float kinhDoDi;

    private String diemDen;
    private Float viDoDen;
    private Float kinhDoDen;

    private LocalDateTime thoiGianKhoiHanh;
    private String phuongTien;
}