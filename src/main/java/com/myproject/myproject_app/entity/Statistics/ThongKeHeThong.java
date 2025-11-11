package com.myproject.myproject_app.entity.Statistics;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table (name = "thong_ke_he_thong")
@Data
@NoArgsConstructor
public class ThongKeHeThong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThongKe;

    private LocalDate ngay;
    private Integer tongNguoiDungMoi;
    private Integer nguoiDungHoatDong;
    private Integer tongLuotTimKiem;
    private Integer tongLuotXemDuBao;
    private Integer tongLuotXemBanDo;
    private Integer tongAnhDang;
    private Integer tongPhanHoi;
    private Integer tongLichHenTao;
    private Integer tongHanhTrinhTao;
    private String diaDiemDuocTimNhieuNhat;
    private String thietBiPhoBienNhat;
}