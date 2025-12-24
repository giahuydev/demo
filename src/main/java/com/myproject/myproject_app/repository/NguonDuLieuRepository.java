package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguonDuLieuRepository extends JpaRepository<NguonDuLieu,Integer> {

    @Query("SELECT n FROM NguonDuLieu n JOIN n.danhSachModel m WHERE m.tenModelHienThi = :tenModel")
    Optional<NguonDuLieu> findByModelName(@Param("tenModel") String tenModel);

    @Query("SELECT n FROM NguonDuLieu n JOIN n.danhSachModel m WHERE m.maModelApi = :maApi")
    Optional<NguonDuLieu> findByMaModelApi(@Param("maApi") String maApi);

    Optional<NguonDuLieu> findByTenChucNang(String tenChucNang);

    // ✅ THÊM METHOD MỚI: Tìm NguonDuLieu thông qua Model
    @Query("SELECT m.nguonDuLieu FROM MoHinhDuBao m WHERE m.maModelApi = :maModelApi")
    Optional<NguonDuLieu> findNguonByModelApi(@Param("maModelApi") String maModelApi);

    // ✅ THÊM METHOD PHỤ (backup): Tìm theo tenModelHienThi
    @Query("SELECT m.nguonDuLieu FROM MoHinhDuBao m WHERE m.tenModelHienThi = :tenModel")
    Optional<NguonDuLieu> findNguonByModelTen(@Param("tenModel") String tenModel);
}