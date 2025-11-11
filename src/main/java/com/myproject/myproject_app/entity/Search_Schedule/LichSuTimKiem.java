package com.myproject.myproject_app.entity.Search_Schedule;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_tim_kiem")
@Data
@NoArgsConstructor
public class LichSuTimKiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLichSu;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_nguon
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String tenDiaDiem;
    private Float viDo;
    private Float kinhDo;
    private LocalDateTime thoiGianTim;
}