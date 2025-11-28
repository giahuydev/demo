package com.myproject.myproject_app.entity.Search_Schedule;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Sử dụng ChronoUnit để chuẩn hóa đơn vị thời gian
enum DonViThoiGianEnum {
    MINUTES, HOURS, DAYS
}

@Entity
@Table(name = "cau_hinh_nhac_nho")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"lichHen"})
public class CauHinhNhacNho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCauHinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lich_hen")
    private LichHen lichHen;

    // Giá trị thời gian (ví dụ: 3)
    private Integer thoiGianCanhBao;

    // Đơn vị thời gian (ví dụ: DAYS)
    @Enumerated(EnumType.STRING)
    private DonViThoiGianEnum donViThoiGian;

    private boolean kichHoat;
}