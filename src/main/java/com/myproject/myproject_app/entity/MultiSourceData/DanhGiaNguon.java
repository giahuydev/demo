package com.myproject.myproject_app.entity.MultiSourceData;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "danh_gia_nguon")
@Data
@NoArgsConstructor
public class DanhGiaNguon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDanhGia;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_nguon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String diaDiem;
    private Float viDo;
    private Float kinhDo;
    private Integer diemSao;
    @Lob
    private String nhanXet;
    private LocalDateTime thoiGianTao;
}
