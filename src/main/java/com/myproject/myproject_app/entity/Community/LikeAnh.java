package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "like_anh", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_anh", "id_nguoi_dung"}) // Đảm bảo 1 user chỉ like 1 ảnh 1 lần
})
@Data
@NoArgsConstructor
public class LikeAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK: id_anh
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anh")
    private AnhCongDong anh;

    // FK: id_nguoi_dung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    private LocalDateTime thoiGian;
}