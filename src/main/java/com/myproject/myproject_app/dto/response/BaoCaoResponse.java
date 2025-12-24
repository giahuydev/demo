package com.myproject.myproject_app.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BaoCaoResponse {
    private Integer idBaoCao;
    private String tenNguoiBaoCao;
    private String lyDo;
    private LocalDateTime ngayBaoCao;

    private Integer idAnhBiBaoCao;
    private String urlAnhBiBaoCao;
}