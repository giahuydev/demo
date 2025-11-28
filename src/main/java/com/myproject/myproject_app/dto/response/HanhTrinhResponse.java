package com.myproject.myproject_app.dto.response;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class HanhTrinhResponse {
    private Integer idHanhTrinh;
    private String tenHanhTrinh;
    private String phuongTien;
    private LocalDateTime thoiGianKhoiHanh;
    private String linkChiaSe;

    private String routeGeometry;

    @JsonRawValue
    private String danhGiaNguyHiem;

    private String deXuat;
}