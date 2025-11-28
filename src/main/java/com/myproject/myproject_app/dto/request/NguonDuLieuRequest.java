package com.myproject.myproject_app.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class NguonDuLieuRequest {
    // Thông tin định danh API
    private String tenChucNang;
    private String baseDomain;

    // Các bộ tham số cố định
    private String dailyParams;
    private String hourlyParams;
    private String currentParams;
    private String minutely15Params;

    // Danh sách model con
    private List<MoHinhDuBaoDTO> danhSachModel;
}