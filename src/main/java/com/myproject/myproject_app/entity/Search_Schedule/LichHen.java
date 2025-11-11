package com.myproject.myproject_app.entity.Search_Schedule;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_hen")
@Data
@NoArgsConstructor
public class LichHen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLichHen;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_nguon (tham khảo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String tenSuKien;
    private LocalDateTime ngayGio;
    private String diaDiem;
    private Float viDo;
    private Float kinhDo;
    @Lob
    private String ghiChu;
    private boolean thongBao3Ngay;
    private boolean thongBao1Ngay;
    private boolean thongBaoSang;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}