package com.myproject.myproject_app.dto.request;

import lombok.Data;

@Data
public class DiaDiemYeuThichUpdateRequest {

    private String bietDanh;
    private String tenNguon;
    private String tenDiaDiem;
    private Float viDo;
    private Float kinhDo;
}