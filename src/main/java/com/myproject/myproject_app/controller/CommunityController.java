package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.AnhCongDongCreationRequest;
import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.BaoCaoRequest;
import com.myproject.myproject_app.dto.request.KiemDuyetRequest;
import com.myproject.myproject_app.entity.Community.AnhCongDong;
import com.myproject.myproject_app.service.AnhCongDongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final AnhCongDongService anhService;
    @PostMapping("/report")
    public ApiResponse<String> baoCaoViPham(@RequestBody BaoCaoRequest request) {
        anhService.baoCaoAnh(request);
        return ApiResponse.<String>builder().message("Đã gửi báo cáo").build();
    }

    @PostMapping("/admin/approve")
    public ApiResponse<String> duyetBaiViet(@RequestBody KiemDuyetRequest request) {
        anhService.adminDuyetBai(request);
        return ApiResponse.<String>builder().message("Đã xử lý xong").build();
    }

    @PostMapping("/create")
    public ApiResponse<AnhCongDong> taoBaiViet(@RequestBody AnhCongDongCreationRequest request) {
        return ApiResponse.<AnhCongDong>builder()
                .result(anhService.dangAnhMoi(request))
                .message("Đăng bài thành công!")
                .build();
    }

    // 2. Thả tim (Truyền ID ảnh vào URL, User ID lấy từ Param hoặc Token)
    @PostMapping("/{idAnh}/like")
    public ApiResponse<String> thichBaiViet(@PathVariable Integer idAnh, @RequestParam String userId) {
        anhService.thichAnh(idAnh, userId);
        return ApiResponse.<String>builder()
                .message("Thao tác thành công")
                .build();
    }
}