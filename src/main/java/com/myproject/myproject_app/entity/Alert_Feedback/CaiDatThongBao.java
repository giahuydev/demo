package com.myproject.myproject_app.entity.Alert_Feedback;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

// Enum cho loại báo cáo/thông báo
@Entity
@Table(name = "cai_dat_thong_bao")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"nguoiDung", "nguon", "thongBaos"})
public class CaiDatThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCaiDat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    private String khuVuc;
    private Double viDo;
    private Double kinhDo;
    private String cronExpression;
    private boolean trangThai;
    private LocalDateTime ngayDangKy;

    // Quan hệ One-to-Many: Một cài đặt có thể sinh ra nhiều thông báo (hàng ngày)
    @OneToMany(mappedBy = "caiDatThongBao", cascade = CascadeType.ALL)
    private Set<ThongBao> thongBaos;
}