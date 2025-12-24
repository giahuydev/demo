package com.myproject.myproject_app.mapper;

import com.myproject.myproject_app.dto.response.ThongBaoResponse;
import com.myproject.myproject_app.entity.Alert_Feedback.ThongBao;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ThongBaoMapper {

    @Mapping(source = "nguoiDung.idNguoiDung", target = "idNguoiDung")
    // Map Enum sang String
    @Mapping(target = "trangThaiGui", expression = "java(entity.getTrangThaiGui() != null ? entity.getTrangThaiGui().name() : null)")
    // Logic xác định nguồn
    @Mapping(target = "loaiNguon", expression = "java(getLoaiNguon(entity))")
    @Mapping(target = "idNguon", expression = "java(getIdNguon(entity))")
    ThongBaoResponse toResponse(ThongBao entity);

    // Logic xác định loại nguồn
    default String getLoaiNguon(ThongBao entity) {
        if (entity.getCauHinhNhacNho() != null) return "NHAC_NHO";
        if (entity.getCaiDatThongBao() != null) return "BAO_CAO";
        return "KHAC";
    }

    // Logic lấy ID nguồn
    default Integer getIdNguon(ThongBao entity) {
        if (entity.getCauHinhNhacNho() != null) return entity.getCauHinhNhacNho().getIdCauHinh();
        if (entity.getCaiDatThongBao() != null) return entity.getCaiDatThongBao().getIdCaiDat();
        return null;
    }
}