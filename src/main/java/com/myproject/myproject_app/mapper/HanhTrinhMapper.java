package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.response.HanhTrinhResponse;
import com.myproject.myproject_app.entity.Search_Schedule.HanhTrinh;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HanhTrinhMapper {

    HanhTrinhResponse toResponse(HanhTrinh entity);
}