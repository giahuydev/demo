package com.myproject.myproject_app.dto.request;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NguoiDungCreationRequest {
    private String email; // UK
    private String matKhau;
    private String hoTen;

}
