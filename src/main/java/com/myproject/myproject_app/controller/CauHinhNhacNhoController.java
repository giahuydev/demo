package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.CauHinhNhacNhoRequest;
import com.myproject.myproject_app.dto.response.CauHinhNhacNhoResponse;
import com.myproject.myproject_app.service.CauHinhNhacNhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reminders")
@RequiredArgsConstructor
public class CauHinhNhacNhoController {

    private final CauHinhNhacNhoService cauHinhNhacNhoService;

    // 1. Lấy chi tiết 1 cấu hình nhắc nhở
    @GetMapping("/{id}")
    public ApiResponse<CauHinhNhacNhoResponse> getDetail(@PathVariable Integer id) {
        return ApiResponse.<CauHinhNhacNhoResponse>builder()
                .result(cauHinhNhacNhoService.getById(id))
                .build();
    }

    // 2. Cập nhật nhắc nhở lẻ
    @PutMapping("/{id}")
    public ApiResponse<CauHinhNhacNhoResponse> update(@PathVariable Integer id,
                                                      @RequestBody @Valid CauHinhNhacNhoRequest request) {
        return ApiResponse.<CauHinhNhacNhoResponse>builder()
                .result(cauHinhNhacNhoService.update(id, request))
                .message("Cập nhật nhắc nhở thành công")
                .build();
    }

    // 3. Xóa một nhắc nhở lẻ
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        cauHinhNhacNhoService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Đã xóa nhắc nhở")
                .build();
    }
}