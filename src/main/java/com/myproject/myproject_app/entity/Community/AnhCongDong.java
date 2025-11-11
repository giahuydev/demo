package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "anh_cong_dong")
@Data
@NoArgsConstructor
public class AnhCongDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAnh;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    private String urlAnh;
    private String urlThumbnail;
    @Lob
    private String moTa;
    private String tinhTrangTroi;
    private Float viDo;
    private Float kinhDo;
    private String diaDiem;
    private Float nhietDo;
    private Integer doAm;
    private Float luongMua;
    private LocalDateTime thoiGianChup;
    private LocalDateTime ngayDang;
    private String trangThaiKiemDuyet;
    private Integer luotThich;
    private Integer luotBinhLuan;
    private boolean xacNhanBanQuyen;
    @Lob
    private String lyDoTuChoi;
}