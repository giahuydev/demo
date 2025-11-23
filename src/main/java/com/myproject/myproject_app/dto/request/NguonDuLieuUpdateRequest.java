package com.myproject.myproject_app.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NguonDuLieuUpdateRequest {
    private String tenNguon; // UK
    private String quocGia;
    private String moTa;
    private boolean kichHoat;
    private String urlApi;
    private Float doChinhXacTrungBinh;
    private Integer tongLuotDanhGia;
}
