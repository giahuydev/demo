package com.myproject.myproject_app.entity.MultiSourceData;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "thong_ke_nguon")
@Data
@NoArgsConstructor
public class ThongKeNguon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThongKe;

    // FK: id_nguon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private LocalDate ngay;
    private Integer soLuotSuDung;
    private Integer soPhanHoiSai;
    private Float tyLeChinhXac;
    private Integer soDanhGia;
    private Float diemTrungBinh;
}