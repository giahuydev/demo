package com.myproject.myproject_app.dto.request;

import lombok.Data;

@Data
public class LichSuTimKiemRequest {
    private String userId;
    private String tenDiaDiem;
    private String tenNguon;
    private Float viDo;
    private Float kinhDo;
}