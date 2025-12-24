package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LichSuTimKiemResponse {
    private Integer id;
    private String tenDiaDiem;

    // ✅ THAY ĐỔI: Trả về tên model thay vì tên nguồn
    private String tenNguon;  // Sẽ chứa model.tenModelHienThi (VD: "Best Match")
    private Integer idModel;  // ✅ MỚI: Trả về ID của model

    private Float viDo;
    private Float kinhDo;
}