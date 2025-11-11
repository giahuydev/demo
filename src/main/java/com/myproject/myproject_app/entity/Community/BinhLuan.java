package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "binh_luan")
@Data
@NoArgsConstructor
public class BinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBinhLuan;

    // FK: id_anh
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anh")
    private AnhCongDong anh;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @Lob
    private String noiDung;
    private LocalDateTime thoiGian;
    private Integer luotThich;
}
