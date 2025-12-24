package com.myproject.myproject_app.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHenCreationRequest {
    @NotBlank(message = "Tên sự kiện không được để trống")
    private String tenSuKien;

    @NotNull(message = "Thời gian bắt đầu sự kiện không được để trống")
    @Future(message = "Thời gian bắt đầu phải ở trong tương lai")
    private LocalDateTime ngayGio;  // Map → thoiGianBatDau

    @NotNull(message = "Thời gian kết thúc sự kiện không được để trống")
    @Future(message = "Thời gian kết thúc phải ở trong tương lai")
    private LocalDateTime thoiGianKetThuc;  // ✅ NEW FIELD

    private String diaDiem;
    private Double viDo;
    private Double kinhDo;
    private String ghiChu;

    private List<CauHinhNhacNhoRequest> nhacNhos;
}