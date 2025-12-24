package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.UserManagement.DiaDiemYeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaDiemYeuThichRepository extends JpaRepository<DiaDiemYeuThich, Integer> {

    List<DiaDiemYeuThich> findByNguoiDung_IdNguoiDung(String idNguoiDung);
}