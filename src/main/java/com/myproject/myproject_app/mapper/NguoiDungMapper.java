package com.myproject.myproject_app.mapper;


import com.myproject.myproject_app.dto.request.NguoiDungCreationRequest;
import com.myproject.myproject_app.dto.request.NguoiDungUpdateRequest;
import com.myproject.myproject_app.dto.response.NguoiDungCreationResponse;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NguoiDungMapper {

    NguoiDung toNguoiDung(NguoiDungCreationRequest request);

    NguoiDungCreationResponse toNguoiDungResponse(NguoiDung nguoiDung);

    void updateUser(@MappingTarget NguoiDung nguoiDung, NguoiDungUpdateRequest request);
}
