package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.response.WeatherFormattedResponse;
import com.myproject.myproject_app.service.WeatherForecastService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    /**
     * ✅ API CHÍNH: Lấy dữ liệu thời tiết theo tọa độ
     * Frontend gọi: GET /identity/weather?lat=10.8231&lon=106.6297&chucNang=Dự báo thời tiết&model=best_match&days=7
     */
    @GetMapping
    public ApiResponse<WeatherFormattedResponse> getWeather(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "Dự báo thời tiết") String chucNang,
            @RequestParam(defaultValue = "best_match") String model,
            @RequestParam(defaultValue = "7") Integer days
    ) {
        log.info("🌐 Received weather request: lat={}, lon={}, model={}, days={}", lat, lon, model, days);

        WeatherFormattedResponse result = weatherForecastService.getWeatherFormatted(
                chucNang,
                model,
                lat,
                lon,
                days
        );

        return ApiResponse.<WeatherFormattedResponse>builder()
                .code(1000)
                .message("Lấy dữ liệu thời tiết thành công")
                .result(result)
                .build();
    }

    /**
     * ✅ API PHỤ: Test kết nối (dùng khi debug)
     */
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Backend đang hoạt động bình thường")
                .result("OK")
                .build();
    }
}