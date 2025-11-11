package com.myproject.myproject_app.entity.Search_Schedule;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hanh_trinh")
@Data
@NoArgsConstructor
public class HanhTrinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHanhTrinh;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_nguon (phân tích)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String tenHanhTrinh;
    private String diemDi;
    private Float viDoDi;
    private Float kinhDoDi;
    private String diemDen;
    private Float viDoDen;
    private Float kinhDoDen;
    private LocalDateTime thoiGianKhoiHanh;
    private String phuongTien;
    @Lob
    private String danhGiaNguyHiem;
    @Lob
    private String deXuat;
    private String linkChiaSe;
    private LocalDateTime ngayTao;
}
