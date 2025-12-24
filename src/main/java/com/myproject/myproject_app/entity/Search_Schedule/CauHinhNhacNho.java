package com.myproject.myproject_app.entity.Search_Schedule;

import com.myproject.myproject_app.entity.Alert_Feedback.ThongBao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@Table(name = "cau_hinh_nhac_nho")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"lichHen", "thongBaos"})
public class CauHinhNhacNho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCauHinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lich_hen", nullable = false)
    private LichHen lichHen;


    private Integer thoiGianCanhBao;

    @Enumerated(EnumType.STRING)
    private DonViThoiGianEnum donViThoiGian;

    private boolean kichHoat;

    private boolean daGui;
    private LocalDateTime thoiGianGuiThucTe;

    @OneToMany(mappedBy = "cauHinhNhacNho", cascade = CascadeType.ALL)
    private Set<ThongBao> thongBaos;
}