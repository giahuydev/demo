package com.myproject.myproject_app.repository;


import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, String> {
    boolean existsByhoTen(String hoTen);
}
