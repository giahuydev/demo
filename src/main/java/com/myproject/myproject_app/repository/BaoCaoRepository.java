package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Community.AnhCongDong;
import com.myproject.myproject_app.entity.Community.BaoCao;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaoCaoRepository extends JpaRepository<BaoCao,Integer> {
    boolean existsByAnhCongDongAndNguoiBaoCao(AnhCongDong anhCongDong, NguoiDung nguoiBaoCao);
}
