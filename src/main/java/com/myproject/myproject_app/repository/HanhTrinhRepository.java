package com.myproject.myproject_app.repository;

import com.myproject.myproject_app.entity.Search_Schedule.HanhTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HanhTrinhRepository extends JpaRepository<HanhTrinh, Integer> {
    Optional<HanhTrinh> findByLinkChiaSe(String linkChiaSe);
}