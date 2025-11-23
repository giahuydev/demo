package com.myproject.myproject_app.entity.UserManagement;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Table(name = "token_phien")
@Data
@NoArgsConstructor
public class TokenPhien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idToken;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @Lob // text -> LOB
    private String refreshToken;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayHetHan;
    private boolean conHieuLuc;
    private String thietBi;
    private String ipAddress;
}
