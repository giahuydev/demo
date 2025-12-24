package com.myproject.myproject_app.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CauHinhNhacNhoResponse {
    private Integer idCauHinh;
    private Integer thoiGianCanhBao;
    private String donViThoiGian;
    private boolean daGui;
}