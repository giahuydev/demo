package com.myproject.myproject_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguoiDungUpdateRequest {

    private String matKhau;
    private String hoTen;
    private String avatarUrl;
    private String cheDoGiaoDien;
}
