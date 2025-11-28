package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.response.WeatherFormattedResponse;
import com.myproject.myproject_app.service.WeatherForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherForecastController {

    // Đổi từ WeatherService -> WeatherForecastService
    private final WeatherForecastService weatherForecastService;

    // API lấy dữ liệu thời tiết
    @GetMapping
    public ApiResponse<WeatherFormattedResponse> getWeather(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "Dự báo thời tiết") String chucNang,
            @RequestParam(defaultValue = "best_match") String model,
            @RequestParam(defaultValue = "7") Integer days
    ) {
        // Gọi method từ service mới
        WeatherFormattedResponse result = weatherForecastService.getWeatherFormatted(chucNang, model, lat, lon, days);

        return ApiResponse.<WeatherFormattedResponse>builder()
                .result(result)
                .build();
    }
}