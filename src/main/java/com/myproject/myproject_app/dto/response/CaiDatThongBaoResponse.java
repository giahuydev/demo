package com.myproject.myproject_app.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaiDatThongBaoResponse {
    private Integer idCaiDat;
    private Integer idNguoiDung;
    private String loaiThongBao;
    private String khuVuc;
    private String cronExpression;
    private boolean trangThai;
    private LocalDateTime ngayDangKy;
}