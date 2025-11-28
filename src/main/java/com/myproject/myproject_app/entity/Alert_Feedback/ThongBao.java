package com.myproject.myproject_app.entity.Alert_Feedback;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Enum cho trạng thái gửi của thông báo
enum TrangThaiGuiEnum {
    CHUA_GUI, DANG_CHO, THANH_CONG, THAT_BAI
}

@Entity
@Table(name = "thong_bao")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"nguoiDung"})
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThongBao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    // Các trường cho liên kết Đa hình (Nguồn gốc tạo thông báo)
    private String nguonPhatSinh;   // Tên Entity (Ví dụ: "CaiDatThongBao")
    private Integer idEntityNguon;  // ID của Entity đó

    // Nội dung được tạo bởi AI hoặc Template
    private String tieuDe;
    @Lob
    private String noiDung;

    @Enumerated(EnumType.STRING)
    private TrangThaiGuiEnum trangThaiGui;

    private LocalDateTime thoiGianTao;
    private LocalDateTime thoiGianCapNhat;

    @OneToMany(mappedBy = "thongBao", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<LichSuGuiEmail> lichSuGuiEmails;
}