package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.DiaDiemYeuThichRequest;
import com.myproject.myproject_app.dto.request.DiaDiemYeuThichUpdateRequest;
import com.myproject.myproject_app.dto.response.DiaDiemYeuThichResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.UserManagement.DiaDiemYeuThich;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.DiaDiemYeuThichMapper;
import com.myproject.myproject_app.repository.DiaDiemYeuThichRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaDiemYeuThichService {

    private final DiaDiemYeuThichRepository diaDiemRepo;
    private final NguonDuLieuRepository nguonRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final DiaDiemYeuThichMapper diaDiemMapper;

    // 1. CREATE: Thêm mới
    @Transactional
    public DiaDiemYeuThichResponse createFavorite(DiaDiemYeuThichRequest request) {
        DiaDiemYeuThich favorite = new DiaDiemYeuThich();

        favorite.setTenDiaDiem(request.getTenDiaDiem());
        favorite.setBietDanh(request.getTenDiaDiem());
        favorite.setViDo(request.getViDo());
        favorite.setKinhDo(request.getKinhDo());
        favorite.setNgayThem(LocalDateTime.now());

        NguoiDung user = nguoiDungRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        favorite.setNguoiDung(user);

        NguonDuLieu nguon = nguonRepo.findBytenNguon(request.getTenNguon())
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));
        favorite.setNguon(nguon);

        return diaDiemMapper.toResponse(diaDiemRepo.save(favorite));
    }

    // 2. READ LIST: Lấy danh sách theo UserID
    public List<DiaDiemYeuThichResponse> getAllFavoritesByUserId(String userId) {
        List<DiaDiemYeuThich> list = diaDiemRepo.findAllById(userId);
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

    // 4. UPDATE: Cập nhật
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

        if (request.getTenNguon() != null && !request.getTenNguon().isEmpty()) {
            NguonDuLieu newSource = nguonRepo.findBytenNguon(request.getTenNguon())
                    .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));
            existingFavorite.setNguon(newSource);
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