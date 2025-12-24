package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // [QUAN TRỌNG] Cần import Optional

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, String> {

    // Phương thức cũ của bạn
    boolean existsByhoTen(String hoTen);

    // [MỚI] Thêm phương thức này để AuthenticationService gọi được
    // JPA sẽ tự động hiểu và tìm bản ghi có cột 'email' trùng khớp
    Optional<NguoiDung> findByEmail(String email);

    // [MỚI] Thêm phương thức này để kiểm tra trùng email khi đăng ký (nếu cần dùng sau này)
    boolean existsByEmail(String email);
}