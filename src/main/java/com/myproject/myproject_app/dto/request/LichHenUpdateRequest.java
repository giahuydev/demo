package com.myproject.myproject_app.dto.request;

import jakarta.validation.constraints.Future;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHenUpdateRequest {
    private String tenSuKien;
    
    @Future(message = "Thời gian bắt đầu phải ở trong tương lai")
    private LocalDateTime ngayGio;  // Map → thoiGianBatDau

    @Future(message = "Thời gian kết thúc phải ở trong tương lai")
    private LocalDateTime thoiGianKetThuc;  // ✅ NEW FIELD

    private String diaDiem;
    private Double viDo;
    private Double kinhDo;
    private String ghiChu;

    private List<CauHinhNhacNhoRequest> nhacNhos;
}