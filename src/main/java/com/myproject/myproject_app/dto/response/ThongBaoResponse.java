package com.myproject.myproject_app.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThongBaoResponse {
    private Integer idThongBao;
    private Integer idNguoiDung;

    private String loaiNguon;
    private Integer idNguon;

    private String tieuDe;
    private String noiDung;


    private String trangThaiGui;
    private LocalDateTime thoiGianGui;
    private String loi;
   // private boolean daMo;

    private LocalDateTime thoiGianTao;
}