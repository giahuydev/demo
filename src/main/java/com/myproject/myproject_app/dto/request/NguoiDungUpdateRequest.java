package com.myproject.myproject_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguoiDungUpdateRequest {

    String matKhau;
    String hoTen;
    String avatarUrl;
    String cheDoGiaoDien;
}
