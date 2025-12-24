package com.myproject.myproject_app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhNhacNhoRequest {
    @NotNull(message = "Thời gian cảnh báo không được để trống")
    @Min(value = 1, message = "Thời gian cảnh báo phải lớn hơn 0")
    private Integer thoiGianCanhBao;

    @NotBlank(message = "Đơn vị thời gian không được để trống (MINUTES, HOURS, DAYS)")
    private String donViThoiGian;
}