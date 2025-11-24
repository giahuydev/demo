package com.myproject.myproject_app.dto.request;

import lombok.Data;

@Data
public class DiaDiemYeuThichRequest {
    private String userId;
    private String tenDiaDiem;
    private Float viDo;
    private Float kinhDo;
    private String tenNguon;
}