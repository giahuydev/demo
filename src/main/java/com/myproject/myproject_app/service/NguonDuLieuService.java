package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.NguonDuLieuRequest;
import com.myproject.myproject_app.dto.response.NguonDuLieuResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.NguonDuLieuMapper;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NguonDuLieuService {

    private final NguonDuLieuRepository nguonDuLieuRepository;
    private final NguonDuLieuMapper nguonDuLieuMapper;

    @Transactional
    public NguonDuLieuResponse create(NguonDuLieuRequest request) {
        if (nguonDuLieuRepository.findByTenChucNang(request.getTenChucNang()).isPresent()) {
            throw new AppException(ErrorCode.SOURCE_EXISTED);
        }

        NguonDuLieu entity = nguonDuLieuMapper.toEntity(request);
        if (entity.getDanhSachModel() != null) {
            entity.getDanhSachModel().forEach(model -> model.setNguonDuLieu(entity));
        }
        NguonDuLieu savedEntity = nguonDuLieuRepository.save(entity);
        return nguonDuLieuMapper.toResponse(savedEntity);
    }

    @Transactional
    public NguonDuLieuResponse update(Integer id, NguonDuLieuRequest request) {
        NguonDuLieu entity = nguonDuLieuRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        nguonDuLieuRepository.findByTenChucNang(request.getTenChucNang())
                .ifPresent(existing -> {
                    if (!existing.getIdNguon().equals(id)) {
                        throw new AppException(ErrorCode.SOURCE_EXISTED);
                    }
                });

        nguonDuLieuMapper.updateEntityFromRequest(entity, request);

        NguonDuLieu updatedEntity = nguonDuLieuRepository.save(entity);
        return nguonDuLieuMapper.toResponse(updatedEntity);
    }

    @Transactional
    public void delete(Integer id) {
        if (!nguonDuLieuRepository.existsById(id)) {
            throw new AppException(ErrorCode.SOURCE_NOT_FOUND);
        }
        nguonDuLieuRepository.deleteById(id);
    }

    public NguonDuLieuResponse getById(Integer id) {
        NguonDuLieu entity = nguonDuLieuRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));
        return nguonDuLieuMapper.toResponse(entity);
    }

    public List<NguonDuLieuResponse> getAll() {
        return nguonDuLieuRepository.findAll().stream()
                .map(nguonDuLieuMapper::toResponse)
                .collect(Collectors.toList());
    }
}