package com.myproject.myproject_app.entity.UserManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "nguoi_dung", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idNguoiDung;

    private String email; // UK
    private String matKhau;
    private String hoTen;
    private String avatarUrl;
    private LocalDateTime ngayDangKy;
    private LocalDateTime lanDangNhapCuoi;
    private String vaiTro;
    private boolean kichHoat;
    private Integer diemDongGop;
    private String cheDoGiaoDien;
    private boolean nguoiDungTinCay;

}