package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Search_Schedule.CauHinhNhacNho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// SỬA 1: Đổi Long -> Integer (vì ID cấu hình của bạn là Integer)
@Repository
public interface CauHinhNhacNhoRepository extends JpaRepository<CauHinhNhacNho, Integer> {
    Optional<CauHinhNhacNho> findByLichHen_NguoiDung_IdNguoiDung(String idNguoiDung);

    List<CauHinhNhacNho> findByLichHen_IdLichHen(Integer idLichHen);


    List<CauHinhNhacNho> findByDaGuiFalseAndKichHoatTrue();

    @Query("SELECT c FROM CauHinhNhacNho c " +
            "JOIN FETCH c.lichHen lh " +
            "JOIN FETCH lh.nguoiDung " +
            "WHERE c.daGui = false " +
            "AND c.kichHoat = true " +
            "AND lh.thoiGianBatDau > CURRENT_TIMESTAMP")
    List<CauHinhNhacNho> findPendingReminders();
}