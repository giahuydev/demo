package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.LichHenCreationRequest;
import com.myproject.myproject_app.dto.request.LichHenUpdateRequest;
import com.myproject.myproject_app.dto.response.LichHenResponse;
import com.myproject.myproject_app.entity.Search_Schedule.LichHen;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LichHenMapper {

    // 1. Entity -> Response
    @Mapping(source = "nguoiDung.idNguoiDung", target = "idNguoiDung")
    LichHenResponse toResponse(LichHen entity);

    // 2. Request -> Entity (Create)
    @Mapping(target = "idLichHen", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "cauHinhNhacNho", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    LichHen toEntity(LichHenCreationRequest request);

    // 3. Update Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idLichHen", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "cauHinhNhacNho", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    void updateLichHen(@MappingTarget LichHen entity, LichHenUpdateRequest request);
}