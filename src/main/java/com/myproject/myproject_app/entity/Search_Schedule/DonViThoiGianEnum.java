package com.myproject.myproject_app.entity.Search_Schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public enum DonViThoiGianEnum {
    PHUT(ChronoUnit.MINUTES), // Ánh xạ PHUT -> MINUTES của Java
    GIO(ChronoUnit.HOURS),    // Ánh xạ GIO -> HOURS
    NGAY(ChronoUnit.DAYS);    // Ánh xạ NGAY -> DAYS

    private final ChronoUnit unit;
}
