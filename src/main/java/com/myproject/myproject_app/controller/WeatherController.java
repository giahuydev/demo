package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.response.WeatherResponse;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ApiResponse<WeatherResponse> getWeather(
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "source", defaultValue = "OpenMeteo") String source
    ) {
        // 1. Validate Input cơ bản
        if (location == null || location.trim().isEmpty()) {
            throw new AppException(ErrorCode.LOCATION_REQUIRED);
        }

        // 2. Gọi Service (Logic mới: Check DB -> Geo -> Cache -> API)
        WeatherResponse result = weatherService.getWeather(location.trim(), source.trim());

        // 3. Đóng gói kết quả trả về chuẩn ApiResponse
        ApiResponse<WeatherResponse> response = new ApiResponse<>();
        response.setCode(1000);
        response.setMessage("Lấy dữ liệu thời tiết thành công");
        response.setResult(result);

        return response;
    }
}