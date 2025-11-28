package com.myproject.myproject_app.entity.Email;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Enum cho loại báo cáo/thông báo
enum LoaiBaoCaoEnum {
    BAO_CAO_HANG_NGAY,
    BAO_CAO_HANG_TUAN
}

@Entity
@Table(name = "cai_dat_thong_bao")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"nguoiDung", "nguon"})
public class CaiDatThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCaiDat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguon")
    private NguonDuLieu nguon;

    @Enumerated(EnumType.STRING)
    private LoaiBaoCaoEnum loaiThongBao;

    // Ví dụ: Hà Nội, Quận 1
    private String khuVuc;

    // Ví dụ: 0 0 8 * * ? (8h sáng hàng ngày)
    private String cronExpression;

    private boolean trangThai; // Kích hoạt/Vô hiệu hóa
    private LocalDateTime ngayDangKy;
}