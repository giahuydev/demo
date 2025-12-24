package com.myproject.myproject_app.entity.Search_Schedule;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_hen")
@Data
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"nguoiDung", "nguon", "cauHinhNhacNhos"})
public class LichHen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLichHen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String tenSuKien;
    @Column(nullable = false)
    private LocalDateTime thoiGianBatDau;

    @Column(nullable = false)
    private LocalDateTime thoiGianKetThuc;
    private String diaDiem;

    private Double viDo;
    private Double kinhDo;

    @Lob
    private String ghiChu;

    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "lichHen", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<CauHinhNhacNho> cauHinhNhacNho;
}