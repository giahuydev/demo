package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "so_sanh_du_bao")
@Data
@NoArgsConstructor
public class SoSanhDuBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSoSanh;

    // FK: id_anh
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anh")
    private AnhCongDong anh;

    // FK: id_nguon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private Float nhietDoDuBao;
    private Integer doAmDuBao;
    private Float luongMuaDuBao;
    private Float doChinhXac;
    @Lob
    private String nhanXet;
}