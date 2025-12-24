package com.myproject.myproject_app.repository;


import com.myproject.myproject_app.entity.Alert_Feedback.CaiDatThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaiDatThongBaoRepository extends JpaRepository<CaiDatThongBao, Integer> {


    List<CaiDatThongBao> findByTrangThaiTrue();

    List<CaiDatThongBao> findByNguoiDung_IdNguoiDung(String idNguoiDung);

    @Query("SELECT c FROM CaiDatThongBao c JOIN FETCH c.nguoiDung WHERE c.cronExpression = :currentTime AND c.trangThai = true")
    List<CaiDatThongBao> findAllByCronExpression(@Param("currentTime") String currentTime);
    /**
     * (Tùy chọn) Kiểm tra xem user này đã cài đặt loại thông báo này cho khu vực này chưa
     * Để tránh spam tạo trùng lặp.
     */
    // boolean existsByNguoiDung_IdNguoiDungAndLoaiThongBaoAndKhuVuc(Integer userId, LoaiBaoCaoEnum loai, String khuVuc);
}