package com.myproject.myproject_app.entity.MultiSourceData;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NguonDuLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNguon;
    @Column(unique = true, nullable = false)
    private String tenChucNang;

    @Column(nullable = false)
    private String baseDomain;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String dailyParams;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String hourlyParams;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String currentParams;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String minutely15Params;

    @OneToMany(mappedBy = "nguonDuLieu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MoHinhDuBao> danhSachModel = new ArrayList<>();

    public void addModel(MoHinhDuBao model) {
        danhSachModel.add(model);
        model.setNguonDuLieu(this);
    }
}