package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.DiaDiemYeuThichRequest;
import com.myproject.myproject_app.dto.request.DiaDiemYeuThichUpdateRequest;
import com.myproject.myproject_app.dto.response.DiaDiemYeuThichResponse;
import com.myproject.myproject_app.entity.UserManagement.DiaDiemYeuThich;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DiaDiemYeuThichMapper {

    // ✅ FIX: Map từ model.tenModelHienThi (thay vì nguon.tenChucNang)
    @Mapping(source = "model.tenModelHienThi", target = "tenNguon")
    @Mapping(source = "model.id", target = "idModel")
    DiaDiemYeuThichResponse toResponse(DiaDiemYeuThich entity);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "idDiaDiem", ignore = true)
    @Mapping(target = "ngayThem", ignore = true)
    DiaDiemYeuThich toEntity(DiaDiemYeuThichRequest request);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "idDiaDiem", ignore = true)
    @Mapping(target = "ngayThem", ignore = true)
    void updateEntity(@MappingTarget DiaDiemYeuThich entity, DiaDiemYeuThichUpdateRequest request);
}