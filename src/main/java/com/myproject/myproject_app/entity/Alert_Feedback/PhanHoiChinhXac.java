package com.myproject.myproject_app.entity.Alert_Feedback;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "phan_hoi_chinh_xac")
@Data
@NoArgsConstructor
public class PhanHoiChinhXac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPhanHoi;

    // FK: id_nguoi_dung (Người gửi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_nguon (Nguồn bị báo cáo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String diaDiem;
    private Float viDo;
    private Float kinhDo;
    private LocalDateTime thoiGianPhanHoi;
    private String loaiVanDe;
    private String mucDoSaiLech;
    @Lob
    private String moTa;
    private String urlAnhChungMinh;
    private String trangThaiXuLy;
    @Lob
    private String phanHoiAdmin;
    private LocalDateTime ngayXuLy;

    // FK: id_admin_xu_ly (Admin xử lý)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_xu_ly")
    private NguoiDung adminXuLy;
}