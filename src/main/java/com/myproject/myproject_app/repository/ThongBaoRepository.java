package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Alert_Feedback.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {

    List<ThongBao> findByNguoiDung_IdNguoiDung(String idNguoiDung);

    @Query("SELECT COUNT(t) > 0 FROM ThongBao t " +
            "WHERE t.caiDatThongBao.idCaiDat = :idCaiDat " +
            "AND t.thoiGianTao BETWEEN :start AND :end")
    boolean existsByCaiDatAndDate(@Param("idCaiDat") Integer idCaiDat,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);
}