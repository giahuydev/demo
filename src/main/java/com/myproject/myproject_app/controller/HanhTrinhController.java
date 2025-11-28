package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.HanhTrinhRequest;
import com.myproject.myproject_app.dto.response.HanhTrinhResponse;
import com.myproject.myproject_app.service.HanhTrinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class HanhTrinhController {

    private final HanhTrinhService hanhTrinhService;

    @PostMapping("/analyze")
    public ApiResponse<HanhTrinhResponse> analyzeTrip(@RequestBody HanhTrinhRequest request) {
        return ApiResponse.<HanhTrinhResponse>builder()
                .result(hanhTrinhService.phanTichHanhTrinh(request))
                .message("Phân tích hành trình thành công")
                .build();
    }

    // Bạn có thể thêm API GET theo LinkChiaSe sau này nếu cần
}