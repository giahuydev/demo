package com.myproject.myproject_app.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LichHenResponse {
    private Integer idLichHen;
    private Integer idNguoiDung;
    private String tenSuKien;
    
    private LocalDateTime thoiGianBatDau;  // ✅ EXPLICIT: Start time
    private LocalDateTime thoiGianKetThuc;  // ✅ EXPLICIT: End time
    
    private String diaDiem;
    private Double viDo;
    private Double kinhDo;
    private String ghiChu;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    private Set<CauHinhNhacNhoResponse> cauHinhNhacNhos;
}