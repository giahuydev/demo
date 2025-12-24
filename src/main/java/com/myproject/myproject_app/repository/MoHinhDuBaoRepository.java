package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.MultiSourceData.MoHinhDuBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoHinhDuBaoRepository extends JpaRepository<MoHinhDuBao, Integer> {

    // Tìm model theo mã API (VD: "icon_seamless", "best_match")
    Optional<MoHinhDuBao> findByMaModelApi(String maModelApi);

    // Tìm model theo tên hiển thị (VD: "ICON Seamless")
    Optional<MoHinhDuBao> findByTenModelHienThi(String tenModelHienThi);
}