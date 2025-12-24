package com.myproject.myproject_app.entity.Alert_Feedback;


import com.myproject.myproject_app.entity.Search_Schedule.CauHinhNhacNho;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"nguoiDung", "cauHinhNhacNho", "caiDatThongBao"})
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThongBao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cau_hinh_nhac_nho", nullable = true)
    private CauHinhNhacNho cauHinhNhacNho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cai_dat_thong_bao", nullable = true)
    private CaiDatThongBao caiDatThongBao;

    private String tieuDe;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @Enumerated(EnumType.STRING)
    private TrangThaiGuiEnum trangThaiGui;

    private LocalDateTime thoiGianGui;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String loi;

    private boolean daMo;

    private LocalDateTime thoiGianTao;
}