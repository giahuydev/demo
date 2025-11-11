package com.myproject.myproject_app.entity.Alert_Feedback;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "canh_bao_thoi_tiet")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"thongBaos"}) // Loại bỏ quan hệ 1-N để tránh StackOverflow khi dùng Lombok @Data
public class CanhBaoThoiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCanhBao;

    // FK: id_nguon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String loaiCanhBao;
    private String mucDo;
    private String khuVuc;
    private Float viDo;
    private Float kinhDo;
    @Lob
    private String noiDung;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
    private LocalDateTime ngayTao;
    private boolean conHieuLuc;

    // Quan hệ 1-N tới THONG_BAO (để JPA biết)
    @OneToMany(mappedBy = "canhBao")
    private java.util.Set<ThongBao> thongBaos;
}