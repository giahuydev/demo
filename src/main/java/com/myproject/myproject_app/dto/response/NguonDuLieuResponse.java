package com.myproject.myproject_app.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NguonDuLieuResponse {

    private String tenNguon; // UK
    private String quocGia;
    private String moTa;
    private Float doChinhXacTrungBinh;
    private Integer tongLuotDanhGia;
    private boolean kichHoat;
}
