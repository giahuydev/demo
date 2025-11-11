package com.myproject.myproject_app.entity.Email;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_gui_email")
@Data
@NoArgsConstructor
public class LichSuGuiEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmail;

    // FK: id_dang_ky
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dang_ky")
    private DangKyEmail dangKyEmail;

    private String loaiEmail;
    private String emailNhan;
    private String tieuDe;
    private LocalDateTime thoiGianGui;
    private boolean thanhCong;
    private boolean daMo;
    private boolean daClick;
    @Lob
    private String loi;
}