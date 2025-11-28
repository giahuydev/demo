package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LichSuTimKiemResponse {
    private Integer id;
    private String tenDiaDiem;
    private String tenNguon;
    private Float viDo;
    private Float kinhDo;
}