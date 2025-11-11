package com.myproject.myproject_app.entity.Email;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dang_ky_email")
@Data
@NoArgsConstructor
public class DangKyEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDangKy;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_nguon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String email;
    private String thoiGianGui;
    @Lob // text -> LOB
    private String diaDiemNhan;
    private boolean kichHoat;
    private LocalDateTime ngayDangKy;
}