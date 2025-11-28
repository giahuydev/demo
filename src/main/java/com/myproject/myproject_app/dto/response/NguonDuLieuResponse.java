package com.myproject.myproject_app.dto.response;


import com.myproject.myproject_app.dto.request.MoHinhDuBaoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NguonDuLieuResponse {
    private Integer id;
    private String tenChucNang;
    private String baseDomain;

    private String dailyParams;
    private String hourlyParams;
    private String currentParams;
    private String minutely15Params;

    private List<MoHinhDuBaoDTO> danhSachModel;
}