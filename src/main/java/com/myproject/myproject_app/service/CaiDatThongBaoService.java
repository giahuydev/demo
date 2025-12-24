package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.CaiDatThongBaoRequest;
import com.myproject.myproject_app.dto.response.CaiDatThongBaoResponse;
import com.myproject.myproject_app.entity.Alert_Feedback.CaiDatThongBao;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.CaiDatThongBaoMapper;
import com.myproject.myproject_app.repository.CaiDatThongBaoRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaiDatThongBaoService {

    private final CaiDatThongBaoRepository caiDatThongBaoRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final CaiDatThongBaoMapper caiDatThongBaoMapper; // Inject Mapper

    // --- 1. CREATE ---
    @Transactional
    public CaiDatThongBaoResponse createCaiDat(Integer userId, CaiDatThongBaoRequest request) {
        NguoiDung user = nguoiDungRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!CronExpression.isValidExpression(request.getCronExpression())) {
            throw new AppException(ErrorCode.CRON_INVALID);
        }

        CaiDatThongBao entity = caiDatThongBaoMapper.toEntity(request);

        entity.setNguoiDung(user);
        entity.setNgayDangKy(LocalDateTime.now());
        entity.setTrangThai(true);

        return caiDatThongBaoMapper.toResponse(caiDatThongBaoRepository.save(entity));
    }

    // --- 2. READ ALL ---
    public List<CaiDatThongBaoResponse> getAllByUserId(String userId) {
        if (!nguoiDungRepository.existsById(String.valueOf(userId))) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        return caiDatThongBaoRepository.findByNguoiDung_IdNguoiDung(userId).stream()
                .map(caiDatThongBaoMapper::toResponse) // Method Reference rút gọn
                .collect(Collectors.toList());
    }

    // --- 3. READ ONE ---
    public CaiDatThongBaoResponse getById(Integer id) {
        CaiDatThongBao entity = caiDatThongBaoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LICH_HEN_NOT_FOUND));
        return caiDatThongBaoMapper.toResponse(entity);
    }

    // --- 4. UPDATE ---
    @Transactional
    public CaiDatThongBaoResponse updateCaiDat(Integer id, CaiDatThongBaoRequest request) {
        CaiDatThongBao entity = caiDatThongBaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        // Validate logic nghiệp vụ riêng (Cron)
        if (request.getCronExpression() != null && !CronExpression.isValidExpression(request.getCronExpression())) {
            throw new AppException(ErrorCode.CRON_INVALID);
        }

        // DÙNG MAPSTRUCT: Tự động update các trường khác null từ Request vào Entity
        caiDatThongBaoMapper.updateCaiDat(entity, request);

        CaiDatThongBao updated = caiDatThongBaoRepository.save(entity);
        return caiDatThongBaoMapper.toResponse(updated);
    }

    // --- 5. DELETE ---
    @Transactional
    public void deleteCaiDat(Integer id) {
        if (!caiDatThongBaoRepository.existsById(id)) {
            throw new RuntimeException("Setting not found");
        }
        caiDatThongBaoRepository.deleteById(id);
    }
}