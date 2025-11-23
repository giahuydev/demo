package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.NguonDuLieuRequest;
import com.myproject.myproject_app.dto.request.NguonDuLieuUpdateRequest;
import com.myproject.myproject_app.dto.response.NguonDuLieuResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.NguonDuLieuMapper;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NguonDuLieuService {

    NguonDuLieuRepository nguonDuLieuRepository;
    NguonDuLieuMapper nguonDuLieuMapper;

    // 1. Tạo mới (Admin)
    @Transactional
    public NguonDuLieuResponse createNguonDuLieu(NguonDuLieuRequest request) {
        if (nguonDuLieuRepository.existsBytenNguon(request.getTenNguon())) {
            throw new AppException(ErrorCode.SOURCE_EXISTED);
        }

        NguonDuLieu nguonDuLieu = nguonDuLieuMapper.toNguonDuLieu(request);

        nguonDuLieu.setNgayThem(LocalDateTime.now());
        nguonDuLieu.setKichHoat(true);

        return nguonDuLieuMapper.toNguonDuLieuResponse(nguonDuLieuRepository.save(nguonDuLieu));
    }

    // 2. Lấy danh sách (User/Admin)
    public List<NguonDuLieuResponse> getAllNguonDuLieu() {
        return nguonDuLieuRepository.findAll().stream()
                .map(nguonDuLieuMapper::toNguonDuLieuResponse)
                .toList();
    }

    // 3. Lấy chi tiết theo tên
    public NguonDuLieuResponse getNguonDuLieuByName(String name) {
        return nguonDuLieuRepository.findBytenNguon(name)
                .map(nguonDuLieuMapper::toNguonDuLieuResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));
    }

    // 4. Cập nhật (Admin)
    @Transactional
    public NguonDuLieuResponse updateNguonDuLieu(String name, NguonDuLieuUpdateRequest request) {
        NguonDuLieu nguonDuLieu = nguonDuLieuRepository.findBytenNguon(name)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        nguonDuLieuMapper.updateNguonDuLieu(nguonDuLieu, request);

        return nguonDuLieuMapper.toNguonDuLieuResponse(nguonDuLieuRepository.save(nguonDuLieu));
    }

    // 5. Xóa (Bổ sung thêm cho đủ bộ CRUD)
    @Transactional
    public void deleteNguonDuLieu(String name) {
        NguonDuLieu nguonDuLieu = nguonDuLieuRepository.findBytenNguon(name)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        nguonDuLieuRepository.delete(nguonDuLieu);
    }
}