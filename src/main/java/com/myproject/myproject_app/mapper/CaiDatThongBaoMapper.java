package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.CaiDatThongBaoRequest;
import com.myproject.myproject_app.dto.response.CaiDatThongBaoResponse;
import com.myproject.myproject_app.entity.Alert_Feedback.CaiDatThongBao;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CaiDatThongBaoMapper {

    // 1. Chuyển từ Entity sang Response
    @Mapping(source = "nguoiDung.idNguoiDung", target = "idNguoiDung")
    CaiDatThongBaoResponse toResponse(CaiDatThongBao entity);

    // 2. Chuyển từ Request sang Entity (Dùng cho Create)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "idCaiDat", ignore = true)
    @Mapping(target = "ngayDangKy", ignore = true)
    CaiDatThongBao toEntity(CaiDatThongBaoRequest request);

    // 3. Cập nhật Entity từ Request (Dùng cho Update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCaiDat", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "ngayDangKy", ignore = true)
    void updateCaiDat(@MappingTarget CaiDatThongBao entity, CaiDatThongBaoRequest request);
}