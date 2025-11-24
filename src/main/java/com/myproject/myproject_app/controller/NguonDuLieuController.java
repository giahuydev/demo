package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.NguonDuLieuRequest;
import com.myproject.myproject_app.dto.request.NguonDuLieuUpdateRequest;
import com.myproject.myproject_app.dto.response.NguonDuLieuResponse;
import com.myproject.myproject_app.service.NguonDuLieuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SoureWeather")
@RequiredArgsConstructor
public class NguonDuLieuController {

    private final NguonDuLieuService nguonDuLieuService;

    // 1. Tạo mới nguồn dữ liệu
    @PostMapping
    ApiResponse<NguonDuLieuResponse> createNguonDuLieu(@RequestBody @Valid NguonDuLieuRequest request) {
        return ApiResponse.<NguonDuLieuResponse>builder()
                .result(nguonDuLieuService.createNguonDuLieu(request))
                .build();
    }

    // 2. Lấy danh sách tất cả nguồn
    @GetMapping
    ApiResponse<List<NguonDuLieuResponse>> getAllNguonDuLieu() {
        return ApiResponse.<List<NguonDuLieuResponse>>builder()
                .result(nguonDuLieuService.getAllNguonDuLieu())
                .build();
    }

    // 3. Lấy chi tiết theo tên nguồn (Thay vì ID, logic service của bạn đang dùng Name)
    @GetMapping("/{tenNguon}")
    ApiResponse<NguonDuLieuResponse> getNguonDuLieuByName(@PathVariable("tenNguon") String name) {
        return ApiResponse.<NguonDuLieuResponse>builder()
                .result(nguonDuLieuService.getNguonDuLieuByName(name))
                .build();
    }

    // 4. Cập nhật nguồn dữ liệu
    @PutMapping("/{tenNguon}")
    ApiResponse<NguonDuLieuResponse> updateNguonDuLieu(
            @PathVariable("tenNguon") String name,
            @RequestBody NguonDuLieuUpdateRequest request) {
        return ApiResponse.<NguonDuLieuResponse>builder()
                .result(nguonDuLieuService.updateNguonDuLieu(name, request))
                .build();
    }

    // 5. Xóa nguồn dữ liệu
    @DeleteMapping("/{tenNguon}")
    ApiResponse<String> deleteNguonDuLieu(@PathVariable("tenNguon") String name) {
        nguonDuLieuService.deleteNguonDuLieu(name);
        return ApiResponse.<String>builder()
                .result("Nguồn dữ liệu đã được xóa thành công")
                .build();
    }
}