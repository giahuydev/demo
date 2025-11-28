package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "anh_cong_dong")
@Data
@NoArgsConstructor
public class AnhCongDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAnh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    private String urlAnh;
    private String urlThumbnail;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String moTa;

    // --- Dữ liệu Thời tiết (FE đã xử lý từ raw data -> String định tính) ---
    private String tinhTrangThoiTiet; // VD: "SUNNY", "RAINY", "STORM"
    private String camGiac;           // VD: "HOT", "COLD", "COMFORTABLE"


    private Float luongMua;
    private String diaDiem;
    private LocalDateTime ngayDang = LocalDateTime.now();

    // --- Quản lý trạng thái ---
    private String trangThaiKiemDuyet = "PENDING";

    // --- Thống kê ---
    private Integer luotThich = 0;
    private Integer luotBinhLuan = 0;
    private Integer soLuotBaoCao = 0;

    // --- Relationships ---
    @OneToMany(mappedBy = "anhCongDong", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BinhLuan> danhSachBinhLuan = new ArrayList<>();

    @OneToMany(mappedBy = "anhCongDong", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeAnh> danhSachLuotThich = new ArrayList<>();

    @OneToMany(mappedBy = "anhCongDong", cascade = CascadeType.ALL)
    private List<BaoCao> danhSachBaoCao = new ArrayList<>();
}