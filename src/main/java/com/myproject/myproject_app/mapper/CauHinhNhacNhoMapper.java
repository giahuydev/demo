package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.request.CauHinhNhacNhoRequest;
import com.myproject.myproject_app.dto.response.CauHinhNhacNhoResponse;
import com.myproject.myproject_app.entity.Search_Schedule.CauHinhNhacNho;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CauHinhNhacNhoMapper {

    // 1. Entity -> Response
    CauHinhNhacNhoResponse toResponse(CauHinhNhacNho entity);

    // 2. Request -> Entity (Create)
    // Lưu ý: Chúng ta thường tạo CauHinh thông qua LichHen, nhưng hàm này vẫn hữu ích
    @Mapping(target = "idCauHinh", ignore = true)
    @Mapping(target = "lichHen", ignore = true)
    @Mapping(target = "daGui", ignore = true)
    @Mapping(target = "thoiGianGuiThucTe", ignore = true)
    @Mapping(target = "thongBaos", ignore = true)
    CauHinhNhacNho toEntity(CauHinhNhacNhoRequest request);

    // 3. Update Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCauHinh", ignore = true)
    @Mapping(target = "lichHen", ignore = true)
    @Mapping(target = "daGui", ignore = true)
    @Mapping(target = "thoiGianGuiThucTe", ignore = true)
    @Mapping(target = "thongBaos", ignore = true)
    void updateCauHinh(@MappingTarget CauHinhNhacNho entity, CauHinhNhacNhoRequest request);
}