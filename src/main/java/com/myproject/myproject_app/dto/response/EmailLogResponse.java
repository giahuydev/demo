package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailLogResponse {
    private String tieuDe;
    private String noiDungRutGon;
    private LocalDateTime thoiGianGui;
    private String trangThai; // "SUCCESS", "FAILED"
    private String nguoiNhan;
}