package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.LichSuTimKiemRequest;
import com.myproject.myproject_app.dto.response.LichSuTimKiemResponse;
import com.myproject.myproject_app.entity.Search_Schedule.LichSuTimKiem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LichSuTimKiemMapper {

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "idLichSu", ignore = true)
    @Mapping(target = "thoiGianTim", ignore = true)
    LichSuTimKiem toLichSuTimKiem(LichSuTimKiemRequest request);

    // ✅ FIX: Map từ model.tenModelHienThi và model.id
    @Mapping(source = "model.tenModelHienThi", target = "tenNguon")
    @Mapping(source = "model.id", target = "idModel")
    LichSuTimKiemResponse toResponse(LichSuTimKiem entity);
}