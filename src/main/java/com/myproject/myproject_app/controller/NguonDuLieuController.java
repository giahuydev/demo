package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.NguonDuLieuRequest;
import com.myproject.myproject_app.dto.response.NguonDuLieuResponse;
import com.myproject.myproject_app.service.NguonDuLieuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nguon-du-lieu")
@RequiredArgsConstructor
public class NguonDuLieuController {

    private final NguonDuLieuService nguonDuLieuService;

    // 1. Tạo mới
    @PostMapping
    public ApiResponse<NguonDuLieuResponse> create(@RequestBody @Valid NguonDuLieuRequest request) {
        return ApiResponse.<NguonDuLieuResponse>builder()
                .result(nguonDuLieuService.create(request))
                .build();
    }

    // 2. Cập nhật (theo ID)
    @PutMapping("/{id}")
    public ApiResponse<NguonDuLieuResponse> update(@PathVariable Integer id, @RequestBody @Valid NguonDuLieuRequest request) {
        return ApiResponse.<NguonDuLieuResponse>builder()
                .result(nguonDuLieuService.update(id, request))
                .build();
    }

    // 3. Xóa (theo ID)
    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Integer id) {
        nguonDuLieuService.delete(id);
        return ApiResponse.<String>builder()
                .result("Đã xóa nguồn dữ liệu thành công")
                .build();
    }

    // 4. Lấy chi tiết (theo ID)
    @GetMapping("/{id}")
    public ApiResponse<NguonDuLieuResponse> getById(@PathVariable Integer id) {
        return ApiResponse.<NguonDuLieuResponse>builder()
                .result(nguonDuLieuService.getById(id))
                .build();
    }

    // 5. Lấy danh sách tất cả
    @GetMapping
    public ApiResponse<List<NguonDuLieuResponse>> getAll() {
        return ApiResponse.<List<NguonDuLieuResponse>>builder()
                .result(nguonDuLieuService.getAll())
                .build();
    }
}