package com.myproject.myproject_app.entity.Community;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "like_anh", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_nguoi_dung", "id_anh"})
})
@Data
@NoArgsConstructor
public class LikeAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anh", nullable = false)
    private AnhCongDong anhCongDong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_dung", nullable = false)
    private NguoiDung nguoiDung;

    private LocalDateTime ngayLike = LocalDateTime.now();
}