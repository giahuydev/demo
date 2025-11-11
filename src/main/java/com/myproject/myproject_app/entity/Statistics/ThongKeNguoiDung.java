package com.myproject.myproject_app.entity.Statistics;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "thong_ke_nguoi_dung")
@Data
@NoArgsConstructor
public class ThongKeNguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThongKe;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    private LocalDate ngay;
    private Integer soLanTimKiem;
    private Integer soLanXemDuBao;
    private Integer soLanXemBanDo;
    private Integer soLichHenTao;
    private Integer soAnhDang;
    private Integer thoiGianTruyCapPhut;
    private String chucNangSuDungNhieuNhat;

    // FK: id_nguon_dung_nhieu_nhat
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon_dung_nhieu_nhat")
    private NguonDuLieu nguonDungNhieuNhat;
}