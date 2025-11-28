package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.MoHinhDuBaoDTO;
import com.myproject.myproject_app.entity.MultiSourceData.MoHinhDuBao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoHinhDuBaoMapper {

    // DTO -> Entity
    @Mapping(target = "nguonDuLieu", ignore = true)
    MoHinhDuBao toEntity(MoHinhDuBaoDTO dto);

    // Entity -> DTO
    MoHinhDuBaoDTO toDTO(MoHinhDuBao entity);
}