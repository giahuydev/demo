package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NguonDuLieuRepository extends JpaRepository<NguonDuLieu,Integer> {

    Optional<NguonDuLieu> findBytenNguon(String tenNguon);

    boolean existsBytenNguon(String tenNguon);
}
