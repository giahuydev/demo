package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Search_Schedule.LichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LichHenRepository extends JpaRepository<LichHen, Integer> {

    List<LichHen> findByNguoiDung_IdNguoiDung(String idNguoiDung);


    @Query("SELECT l FROM LichHen l WHERE l.nguoiDung.idNguoiDung = :userId " +
            "AND l.thoiGianBatDau BETWEEN :startDate AND :endDate " +
            "ORDER BY l.thoiGianBatDau ASC")
    List<LichHen> findByUserAndDateRange(@Param("userId") String userId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * 3. (Tùy chọn) Kiểm tra trùng lịch* Kiểm tra xem user có lịch nào trùng giờ chính xác không
     */
    boolean existsByNguoiDung_IdNguoiDungAndThoiGianBatDau(String idNguoiDung, LocalDateTime thoiGianBatDau);
}