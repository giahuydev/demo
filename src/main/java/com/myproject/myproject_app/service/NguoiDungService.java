package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.NguoiDungCreationRequest;
import com.myproject.myproject_app.dto.request.NguoiDungUpdateRequest;
import com.myproject.myproject_app.dto.response.NguoiDungCreationResponse;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.NguoiDungMapper;
import com.myproject.myproject_app.repository.NguoiDungRepository;
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
public class NguoiDungService {

    NguoiDungRepository nguoiDungRepository;
    NguonDuLieuRepository nguonDuLieuRepository;
    NguoiDungMapper nguoiDungMapper;

    // --- 1. TẠO MỚI (Đăng Ký) ---
    @Transactional
    public NguoiDung createRequest(NguoiDungCreationRequest request) {
        // Check trùng
        if (nguoiDungRepository.existsByhoTen(request.getHoTen())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        NguoiDung nguoiDung = nguoiDungMapper.toNguoiDung(request);

        nguoiDung.setNgayDangKy(LocalDateTime.now());
        nguoiDung.setKichHoat(true);
        nguoiDung.setNguoiDungTinCay(true);
        nguoiDung.setVaiTro("USER");

        return nguoiDungRepository.save(nguoiDung);
    }


//    @Transactional
//    public void updateDefaultSource(String userId, String tenNguonMoi) {
//        NguoiDung user = nguoiDungRepository.findById(userId)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        NguonDuLieu newSource = nguonDuLieuRepository.findByModelName(tenNguonMoi)
//                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));
//        user.setNguonMacDinh(newSource);
//        nguoiDungRepository.save(user);
//    }

    public List<NguoiDungCreationResponse> getDSNguoiDung() {
        return nguoiDungRepository.findAll().stream()
                .map(nguoiDungMapper::toNguoiDungResponse).toList();
    }

    public void deleteNguoiDung(String id) {
        if (!nguoiDungRepository.existsById(id)) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        nguoiDungRepository.deleteById(id);
    }

    public NguoiDungCreationResponse getNguoiDungById(String id) {
        return nguoiDungMapper.toNguoiDungResponse(nguoiDungRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Transactional
    public NguoiDungCreationResponse updateNguoiDung(String id, NguoiDungUpdateRequest request) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        nguoiDungMapper.updateUser(nguoiDung, request);

//        if (request.getTenNguonMacDinh() != null) {
//            NguonDuLieu nguonMoi = nguonDuLieuRepository.findByMaModelApi(request.getTenNguonMacDinh())
//                    .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));
//            nguoiDung.setNguonMacDinh(nguonMoi);
//        }

        return nguoiDungMapper.toNguoiDungResponse(nguoiDungRepository.save(nguoiDung));
    }
}