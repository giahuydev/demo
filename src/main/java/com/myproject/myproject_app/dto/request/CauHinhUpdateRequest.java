package com.myproject.myproject_app.dto.request;

import com.myproject.myproject_app.entity.Search_Schedule.DonViThoiGianEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class CauHinhUpdateRequest {
    private Integer thoiGianBaoTruocMacDinh;
    @Enumerated(EnumType.STRING)
    private DonViThoiGianEnum donViThoiGian;
    private boolean nhanEmail;
    private boolean nhanThongBaoDay;
}