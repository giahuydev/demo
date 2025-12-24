package com.myproject.myproject_app.entity.UserManagement;

import com.myproject.myproject_app.entity.MultiSourceData.MoHinhDuBao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dia_diem_yeu_thich")
@Data
@NoArgsConstructor
public class DiaDiemYeuThich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDiaDiem;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // ✅ FIX: Đổi từ NguonDuLieu -> MoHinhDuBao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_model")
    private MoHinhDuBao model;

    private String tenDiaDiem;
    private String bietDanh;
    private Float viDo;
    private Float kinhDo;
    private Integer thuTuSapXep;
    private LocalDateTime ngayThem;
}