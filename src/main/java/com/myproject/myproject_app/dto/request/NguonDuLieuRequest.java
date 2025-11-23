package com.myproject.myproject_app.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NguonDuLieuRequest {
    private String tenNguon; // UK
    private String quocGia;
    private String moTa;
    private String urlApi;
}
