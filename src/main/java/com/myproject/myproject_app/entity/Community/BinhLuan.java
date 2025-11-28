package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "binh_luan")
@Data
@NoArgsConstructor
public class BinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBinhLuan;

    // Liên kết với Ảnh
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anh", nullable = false)
    private AnhCongDong anhCongDong;

    // Người bình luận
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String noiDung;

    private LocalDateTime ngayBinhLuan = LocalDateTime.now();

    // Hỗ trợ tính năng báo cáo bình luận xấu (vi phạm tiêu chuẩn cộng đồng)
    private Integer soLuotBaoCao = 0;
    private String trangThaiKiemDuyet = "PENDING";

    // Một bình luận cũng có thể bị báo cáo
    @OneToMany(mappedBy = "binhLuan", cascade = CascadeType.ALL)
    private List<BaoCao> danhSachBaoCao = new ArrayList<>();
}