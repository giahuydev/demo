package com.myproject.myproject_app.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class WeatherResponse {
    private String locationName;
    private String sourceUsed;
    private String lastUpdated;

    private CurrentWeather current;
    private List<HourlyForecast> hourly;
    private List<DailyForecast> daily; // Dữ liệu dự báo theo ngày

    // --- Class con: Thời tiết hiện tại ---
    @Data
    @Builder
    public static class CurrentWeather {
        private String time;
        private double temperature;
        private double feelsLike;
        private int humidity;
        private double windSpeed;
        private int windDirection;
        private int cloudCover;
        private String condition; // "Day" hoặc "Night"
        private double uvIndex;
        private double latitude;
        private double longitude;
    }

    // --- Class con: Dự báo theo giờ (24h tới) ---
    @Data
    @Builder
    public static class HourlyForecast {
        private String time;
        private double temperature;
        private int humidity;
        private int rainChance;
        private double uvIndex;      // Chỉ số UV
        private double soilMoisture; // Độ ẩm đất (0-1cm)
        private String isDay;        // "Day" hoặc "Night"
    }

    // --- Class con: Dự báo theo ngày (7 ngày tới) ---
    @Data
    @Builder
    public static class DailyForecast {
        private String date;
        private int weatherCode;
        private String sunrise;      // Giờ mặt trời mọc
        private String sunset;       // Giờ mặt trời lặn
        private double maxUV;        // UV cao nhất trong ngày
        private double totalRain;    // Tổng lượng mưa
        private int maxRainChance;   // Xác suất mưa cao nhất
    }
}