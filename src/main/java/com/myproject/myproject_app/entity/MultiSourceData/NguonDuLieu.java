package com.myproject.myproject_app.entity.MultiSourceData;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nguon_du_lieu", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ten_nguon"})
})
@Data
@NoArgsConstructor
public class NguonDuLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNguon;

    private String tenNguon; // UK
    private String quocGia;
    private String moTa;
    private String urlApi;
    private Float doChinhXacTrungBinh;
    private Integer tongLuotDanhGia;
    private boolean kichHoat;
    private Integer mucDoUuTien;
    private LocalDateTime ngayThem;
}