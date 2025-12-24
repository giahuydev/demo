package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Search_Schedule.LichSuTimKiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuTimKiemRepository extends JpaRepository<LichSuTimKiem, Integer> {

    List<LichSuTimKiem> findAllByNguoiDung_IdNguoiDungOrderByThoiGianTimDesc(String userId);

    void deleteAllByNguoiDung_IdNguoiDung(String userId);
}