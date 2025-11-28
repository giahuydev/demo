package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.LichSuTimKiemRequest;
import com.myproject.myproject_app.dto.response.LichSuTimKiemResponse;
import com.myproject.myproject_app.service.LichSuTimKiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search-history")
@RequiredArgsConstructor
public class LichSuTimKiemController {

    private final LichSuTimKiemService historyService;

    @PostMapping
    public ApiResponse<LichSuTimKiemResponse> addHistory(@RequestBody LichSuTimKiemRequest request) {
        return ApiResponse.<LichSuTimKiemResponse>builder()
                .result(historyService.addSearchHistory(request))
                .message("Đã lưu lịch sử tìm kiếm")
                .build();
    }


    @GetMapping
    public ApiResponse<List<LichSuTimKiemResponse>> getHistory(@RequestParam String userId) {
        return ApiResponse.<List<LichSuTimKiemResponse>>builder()
                .result(historyService.getHistoryByUser(userId))
                .message("Lấy danh sách thành công")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<LichSuTimKiemResponse> getHistoryDetail(@PathVariable Integer id) {
        return ApiResponse.<LichSuTimKiemResponse>builder()
                .result(historyService.getHistoryById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteHistory(@PathVariable Integer id) {
        historyService.deleteHistory(id);
        return ApiResponse.<String>builder()
                .message("Đã xóa lịch sử")
                .build();
    }

    @DeleteMapping("/all")
    public ApiResponse<String> deleteAllHistory(@RequestParam String userId) {
        historyService.deleteAllHistory(userId);
        return ApiResponse.<String>builder()
                .message("Đã xóa toàn bộ lịch sử tìm kiếm")
                .build();
    }
}