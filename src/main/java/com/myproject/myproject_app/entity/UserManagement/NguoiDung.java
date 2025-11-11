package com.myproject.myproject_app.entity.UserManagement;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "nguoi_dung", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
@Data
@NoArgsConstructor
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNguoiDung;

    private String email; // UK
    private String matKhau;
    private String hoTen;
    private String soDienThoai;
    private String avatarUrl;
    private LocalDateTime ngayDangKy;
    private LocalDateTime lanDangNhapCuoi;
    private String vaiTro;
    private boolean kichHoat;
    private Integer diemDongGop;
    private String cheDoGiaoDien;
    private boolean nguoiDungTinCay;

    // FK: id_nguon_mac_dinh
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon_mac_dinh")
    private NguonDuLieu nguonMacDinh;
}