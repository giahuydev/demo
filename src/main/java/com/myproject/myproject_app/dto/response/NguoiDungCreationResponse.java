package com.myproject.myproject_app.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NguoiDungCreationResponse {
    private String idNguoiDung;
    private String email; // UK
    private String matKhau;
    private String hoTen;
    private LocalDateTime ngayDangKy;
    private boolean kichHoat;
}
