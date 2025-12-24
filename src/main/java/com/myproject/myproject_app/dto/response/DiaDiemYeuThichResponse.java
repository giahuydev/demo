package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DiaDiemYeuThichResponse {
    private Integer idDiaDiem;
    private String tenDiaDiem;
    private String bietDanh;
    private Float viDo;
    private Float kinhDo;

    // ✅ THAY ĐỔI: Trả về tên model thay vì tên nguồn
    private String tenNguon;  // Sẽ chứa model.tenModelHienThi (VD: "ICON Seamless")
    private Integer idModel;  // ✅ MỚI: Trả về ID của model

    private LocalDateTime ngayThem;
}