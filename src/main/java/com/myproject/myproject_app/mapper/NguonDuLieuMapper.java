package com.myproject.myproject_app.mapper;


import com.myproject.myproject_app.dto.request.NguonDuLieuRequest;
import com.myproject.myproject_app.dto.request.NguonDuLieuUpdateRequest;
import com.myproject.myproject_app.dto.response.NguonDuLieuResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface NguonDuLieuMapper {

    NguonDuLieu toNguonDuLieu(NguonDuLieuRequest request);

        NguonDuLieuResponse toNguonDuLieuResponse(NguonDuLieu nguonDuLieu);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNguonDuLieu(@MappingTarget NguonDuLieu nguonDuLieu, NguonDuLieuUpdateRequest request);
}
