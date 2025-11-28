package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.NguonDuLieuRequest;
import com.myproject.myproject_app.dto.response.NguonDuLieuResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MoHinhDuBaoMapper.class})
public interface NguonDuLieuMapper {

    NguonDuLieu toEntity(NguonDuLieuRequest request);

    NguonDuLieuResponse toResponse(NguonDuLieu entity);

    void updateEntityFromRequest(@MappingTarget NguonDuLieu entity, NguonDuLieuRequest request);


    @AfterMapping
    default void linkParent(@MappingTarget NguonDuLieu entity) {
        if (entity.getDanhSachModel() != null) {
            entity.getDanhSachModel().forEach(child -> child.setNguonDuLieu(entity));
        }
    }
}