package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.LichSuTimKiemRequest;
import com.myproject.myproject_app.dto.response.LichSuTimKiemResponse;
import com.myproject.myproject_app.entity.Search_Schedule.LichSuTimKiem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LichSuTimKiemMapper {

    @Mapping(target = "nguon", ignore = true)
    @Mapping(target = "nguoiDung", ignore = true)
    @Mapping(target = "idLichSu", ignore = true)
    @Mapping(target = "thoiGianTim", ignore = true)
    LichSuTimKiem toLichSuTimKiem(LichSuTimKiemRequest request);

    @Mapping(source = "nguon.tenChucNang", target = "tenNguon")
    LichSuTimKiemResponse toResponse(LichSuTimKiem entity);
}