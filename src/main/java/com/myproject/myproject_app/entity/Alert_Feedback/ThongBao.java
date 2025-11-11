package com.myproject.myproject_app.entity.Alert_Feedback;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
@Data
@NoArgsConstructor
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThongBao;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // FK: id_canh_bao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_canh_bao")
    private CanhBaoThoiTiet canhBao;

    private String loaiThongBao;
    private String tieuDe;
    @Lob
    private String noiDung;
    private boolean daDoc;
    private LocalDateTime thoiGianTao;
    private LocalDateTime thoiGianDoc;
}
