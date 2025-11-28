package com.myproject.myproject_app.dto.internal;

import java.util.List;

public record KetQuaPhanTichChiTiet(
        String danhGiaTongQuan,
        String deXuatChung,
        List<ChiTietDiemCanhBao> danhSachDiem
) {}