package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DiaDiemYeuThichResponse {
    private Integer idDiaDiem;
    private String tenDiaDiem;
    private String bietDanh;
    private Float viDo;
    private Float kinhDo;
    private String tenNguon;
    private LocalDateTime ngayThem;
}