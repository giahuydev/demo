package com.myproject.myproject_app.entity.MultiSourceData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoHinhDuBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String tenModelHienThi;

    @Column(nullable = false)
    private String maModelApi;

    private String nhaCungCap;

    private String quocGia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idNguon", nullable = false)
    @JsonIgnore
    private NguonDuLieu nguonDuLieu;
}