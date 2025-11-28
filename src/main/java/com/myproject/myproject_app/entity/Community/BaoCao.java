package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "bao_cao")
@Data
@NoArgsConstructor
public class BaoCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBaoCao;

    // Người báo cáo (Whistleblower)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_bao_cao", nullable = false)
    private NguoiDung nguoiBaoCao;

    // Lý do (VD: "Spam", "Xúc phạm", "Ảnh nóng")
    private String lyDo;

    // Trạng thái xử lý của Admin
    private String trangThaiXuLy = "PENDING"; // PENDING, PROCESSED, IGNORED

    private LocalDateTime ngayBaoCao = LocalDateTime.now();

    // Liên kết trực tiếp (Một báo cáo chỉ thuộc về 1 trong 2 loại này)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anh_cong_dong")
    private AnhCongDong anhCongDong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_binh_luan")
    private BinhLuan binhLuan;
}