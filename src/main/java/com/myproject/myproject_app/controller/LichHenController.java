package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.LichHenCreationRequest;
import com.myproject.myproject_app.dto.request.LichHenUpdateRequest;
import com.myproject.myproject_app.dto.response.LichHenResponse;
import com.myproject.myproject_app.service.LichHenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lich-hen")
@RequiredArgsConstructor
public class LichHenController {

    private final LichHenService lichHenService;

    // 1. Tạo lịch hẹn (Kèm danh sách nhắc nhở trong body request)
    @PostMapping("/user/{userId}")
    public ApiResponse<LichHenResponse> create(@PathVariable Integer userId,
                                               @RequestBody @Valid LichHenCreationRequest request) {
        return ApiResponse.<LichHenResponse>builder()
                .result(lichHenService.createLichHen(userId, request))
                .message("Tạo lịch hẹn thành công")
                .build();
    }

    // 2. Lấy danh sách lịch hẹn của User
    @GetMapping("/user/{userId}")
    public ApiResponse<List<LichHenResponse>> getAllByUser(@PathVariable String userId) {
        return ApiResponse.<List<LichHenResponse>>builder()
                .result(lichHenService.getAllByUserId(userId))
                .build();
    }

    // 3. Xem chi tiết 1 lịch hẹn
    @GetMapping("/{id}")
    public ApiResponse<LichHenResponse> getDetail(@PathVariable Integer id) {
        return ApiResponse.<LichHenResponse>builder()
                .result(lichHenService.getById(id))
                .build();
    }

    // 4. Cập nhật lịch hẹn (Có thể sửa/thay thế danh sách nhắc nhở)
    @PutMapping("/{id}")
    public ApiResponse<LichHenResponse> update(@PathVariable Integer id,
                                               @RequestBody LichHenUpdateRequest request) {
        return ApiResponse.<LichHenResponse>builder()
                .result(lichHenService.updateLichHen(id, request))
                .message("Cập nhật lịch hẹn thành công")
                .build();
    }

    // 5. Xóa lịch hẹn (Xóa luôn cả các nhắc nhở con)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        lichHenService.deleteLichHen(id);
        return ApiResponse.<Void>builder()
                .message("Đã xóa lịch hẹn")
                .build();
    }
}