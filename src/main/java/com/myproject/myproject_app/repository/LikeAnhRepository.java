package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Community.AnhCongDong;
import com.myproject.myproject_app.entity.Community.LikeAnh;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeAnhRepository extends JpaRepository<LikeAnh, Integer> {
    Optional<LikeAnh> findByAnhCongDongAndNguoiDung(AnhCongDong anh, NguoiDung user);
}