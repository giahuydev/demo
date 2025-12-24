package com.myproject.myproject_app.controller;


import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.response.ThongBaoResponse;
import com.myproject.myproject_app.service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class ThongBaoController {

    private final ThongBaoService thongBaoService;

    // 1. Lấy danh sách thông báo (Inbox)
    @GetMapping("/user/{userId}")
    public ApiResponse<List<ThongBaoResponse>> getInbox(@PathVariable String userId) {
        return ApiResponse.<List<ThongBaoResponse>>builder()
                .result(thongBaoService.getAllByUserId(userId))
                .build();
    }

    // 2. Xem chi tiết 1 thông báo
    @GetMapping("/{id}")
    public ApiResponse<ThongBaoResponse> getDetail(@PathVariable Integer id) {
        return ApiResponse.<ThongBaoResponse>builder()
                .result(thongBaoService.getById(id))
                .build();
    }

    // 3. Xóa 1 thông báo
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        thongBaoService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Đã xóa thông báo")
                .build();
    }

    // 4. Xóa toàn bộ thông báo của User (Clear All)
    @DeleteMapping("/user/{userId}")
    public ApiResponse<Void> deleteAll(@PathVariable String userId) {
        thongBaoService.deleteAllByUser(userId);
        return ApiResponse.<Void>builder()
                .message("Đã dọn dẹp hộp thư")
                .build();
    }
}