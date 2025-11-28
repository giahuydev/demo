package com.myproject.myproject_app.entity.Email;

import com.myproject.myproject_app.entity.Alert_Feedback.ThongBao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_gui_email")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"thongBao"})
public class LichSuGuiEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLichSuGui;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_thong_bao")
    private ThongBao thongBao; // Liên kết với nội dung đã được tạo

    private String emailNhan;
    private LocalDateTime thoiGianGui;

    private boolean thanhCong; // Giao dịch gửi thành công?
    private boolean daMo; // Đã mở email (thông qua tracking pixel)

    @Lob
    private String loi; // Chi tiết lỗi nếu gửi thất bại
}