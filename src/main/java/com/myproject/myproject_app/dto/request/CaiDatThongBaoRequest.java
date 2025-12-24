package com.myproject.myproject_app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaiDatThongBaoRequest {

    @NotBlank(message = "Loại thông báo không được để trống")
    private String loaiThongBao;

    @NotBlank(message = "Tên khu vực không được để trống")
    private String khuVuc;

    @Min(value = -90, message = "Vĩ độ không hợp lệ")
    @Max(value = 90, message = "Vĩ độ không hợp lệ")
    private Double viDo;

    @Min(value = -180, message = "Kinh độ không hợp lệ")
    @Max(value = 180, message = "Kinh độ không hợp lệ")
    private Double kinhDo;

    @NotBlank(message = "Thời gian (Cron) không được để trống")
    private String cronExpression;

    private boolean trangThai;
}