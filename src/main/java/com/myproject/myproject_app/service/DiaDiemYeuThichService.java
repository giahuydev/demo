package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.DiaDiemYeuThichRequest;
import com.myproject.myproject_app.dto.request.DiaDiemYeuThichUpdateRequest;
import com.myproject.myproject_app.dto.response.DiaDiemYeuThichResponse;
import com.myproject.myproject_app.entity.MultiSourceData.MoHinhDuBao;
import com.myproject.myproject_app.entity.UserManagement.DiaDiemYeuThich;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.DiaDiemYeuThichMapper;
import com.myproject.myproject_app.repository.DiaDiemYeuThichRepository;
import com.myproject.myproject_app.repository.MoHinhDuBaoRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaDiemYeuThichService {

    private final DiaDiemYeuThichRepository diaDiemRepo;
    private final MoHinhDuBaoRepository modelRepo;  // ✅ THAY ĐỔI: Dùng ModelRepo
    private final NguoiDungRepository nguoiDungRepo;
    private final DiaDiemYeuThichMapper diaDiemMapper;

    // ✅ 1. CREATE: Thêm mới - FIXED LOGIC
    @Transactional
    public DiaDiemYeuThichResponse createFavorite(DiaDiemYeuThichRequest request) {
        log.info("Creating favorite with tenNguon (maModelApi): {}", request.getTenNguon());

        DiaDiemYeuThich favorite = new DiaDiemYeuThich();

        favorite.setTenDiaDiem(request.getTenDiaDiem());
        favorite.setBietDanh(request.getTenDiaDiem());
        favorite.setViDo(request.getViDo());
        favorite.setKinhDo(request.getKinhDo());
        favorite.setNgayThem(LocalDateTime.now());

        // Validate User
        NguoiDung user = nguoiDungRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        favorite.setNguoiDung(user);

        // ✅ FIX: Tìm Model trực tiếp theo maModelApi
        MoHinhDuBao model = modelRepo.findByMaModelApi(request.getTenNguon())
                .orElseGet(() -> {
                    // Backup: Thử tìm theo tenModelHienThi
                    log.warn("Không tìm thấy model theo maModelApi: {}, thử tìm theo tenModelHienThi",
                            request.getTenNguon());
                    return modelRepo.findByTenModelHienThi(request.getTenNguon())
                            .orElseThrow(() -> {
                                log.error("KHÔNG TÌM THẤY MODEL với tên: {}", request.getTenNguon());
                                return new AppException(ErrorCode.SOURCE_NOT_FOUND);
                            });
                });

        log.info("Found Model: {} (ID: {}, Nguồn: {})",
                model.getTenModelHienThi(),
                model.getId(),
                model.getNguonDuLieu().getTenChucNang());

        favorite.setModel(model);

        DiaDiemYeuThich saved = diaDiemRepo.save(favorite);
        return diaDiemMapper.toResponse(saved);
    }

    // 2. READ LIST: Lấy danh sách theo UserID
    public List<DiaDiemYeuThichResponse> getAllFavoritesByUserId(String userId) {
        List<DiaDiemYeuThich> list = diaDiemRepo.findByNguoiDung_IdNguoiDung(userId);
        return list.stream()
                .map(diaDiemMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 3. READ ONE: Lấy chi tiết 1 địa điểm
    public DiaDiemYeuThichResponse getFavoriteById(Integer id) {
        DiaDiemYeuThich favorite = diaDiemRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FAVORITE_NOT_FOUND));
        return diaDiemMapper.toResponse(favorite);
    }

    // ✅ 4. UPDATE: Cập nhật - FIXED LOGIC
    @Transactional
    public DiaDiemYeuThichResponse updateFavorite(Integer id, DiaDiemYeuThichUpdateRequest request) {
        DiaDiemYeuThich existingFavorite = diaDiemRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FAVORITE_NOT_FOUND));

        boolean isUpdatingLocation = request.getTenDiaDiem() != null
                || request.getViDo() != null
                || request.getKinhDo() != null;

        if (isUpdatingLocation) {
            if (request.getTenDiaDiem() == null || request.getViDo() == null || request.getKinhDo() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        }

        diaDiemMapper.updateEntity(existingFavorite, request);

        // ✅ FIX: Tìm Model trực tiếp khi update
        if (request.getTenNguon() != null && !request.getTenNguon().isEmpty()) {
            log.info("Updating model to: {}", request.getTenNguon());

            MoHinhDuBao newModel = modelRepo.findByMaModelApi(request.getTenNguon())
                    .orElseGet(() -> {
                        log.warn("Không tìm thấy model theo maModelApi: {}, thử tìm theo tenModelHienThi",
                                request.getTenNguon());
                        return modelRepo.findByTenModelHienThi(request.getTenNguon())
                                .orElseThrow(() -> {
                                    log.error("KHÔNG TÌM THẤY MODEL với tên: {}", request.getTenNguon());
                                    return new AppException(ErrorCode.SOURCE_NOT_FOUND);
                                });
                    });

            log.info("Updated to Model: {} (ID: {})", newModel.getTenModelHienThi(), newModel.getId());
            existingFavorite.setModel(newModel);
        }
        return diaDiemMapper.toResponse(diaDiemRepo.save(existingFavorite));
    }

    // 5. DELETE: Xóa
    @Transactional
    public void deleteFavorite(Integer id) {
        if (!diaDiemRepo.existsById(id)) {
            throw new AppException(ErrorCode.FAVORITE_NOT_FOUND);
        }
        diaDiemRepo.deleteById(id);
    }
}