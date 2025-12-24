package com.myproject.myproject_app.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private boolean authenticated;
    // Trả về thêm thông tin user để FE lưu vào localStorage
    private String idNguoiDung;
    private String hoTen;
    private String email;
}