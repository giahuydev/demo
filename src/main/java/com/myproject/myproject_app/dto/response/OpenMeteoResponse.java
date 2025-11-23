package com.myproject.myproject_app.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class OpenMeteoResponse {

    @SerializedName("current")
    private Current current;

    @SerializedName("hourly")
    private Hourly hourly;

    @SerializedName("daily")
    private Daily daily; // Thêm phần dự báo theo ngày

    // --- 1. Dữ liệu hiện tại ---
    @Data
    public static class Current {
        private String time;

        @SerializedName("temperature_2m") private Double temperature;
        @SerializedName("relative_humidity_2m") private Integer humidity;
        @SerializedName("apparent_temperature") private Double feelsLike;
        @SerializedName("is_day") private Integer isDay;
        @SerializedName("rain") private Double rain;
        @SerializedName("weather_code") private Integer weatherCode;
        @SerializedName("cloud_cover") private Integer cloudCover;
        @SerializedName("wind_speed_10m") private Double windSpeed;
        @SerializedName("wind_direction_10m") private Integer windDirection;
        @SerializedName("wind_gusts_10m") private Double windGusts;
    }

    // --- 2. Dữ liệu theo giờ (Hourly) ---
    @Data
    public static class Hourly {
        private List<String> time;

        @SerializedName("temperature_2m") private List<Double> temperature;
        @SerializedName("relative_humidity_2m") private List<Integer> humidity;
        @SerializedName("precipitation_probability") private List<Integer> precipProb;
        @SerializedName("weather_code") private List<Integer> weatherCode;
        @SerializedName("cloud_cover") private List<Integer> cloudCover;
        @SerializedName("wind_speed_10m") private List<Double> windSpeed;
        @SerializedName("wind_direction_10m") private List<Integer> windDirection;
        @SerializedName("uv_index") private List<Double> uvIndex;

        // Thêm thông tin đất trồng (Soil)
        @SerializedName("soil_temperature_0cm") private List<Double> soilTemp;
        @SerializedName("soil_moisture_0_to_1cm") private List<Double> soilMoisture;

        @SerializedName("is_day") private List<Integer> isDay;
    }

    // --- 3. Dữ liệu theo ngày (Daily) - Mới thêm ---
    @Data
    public static class Daily {
        private List<String> time;

        @SerializedName("weather_code") private List<Integer> weatherCode;
        @SerializedName("sunrise") private List<String> sunrise;
        @SerializedName("sunset") private List<String> sunset;
        @SerializedName("uv_index_max") private List<Double> uvIndexMax;
        @SerializedName("rain_sum") private List<Double> rainSum;
        @SerializedName("precipitation_probability_max") private List<Integer> precipProbMax;

        // Nhiệt độ Min/Max (thường API sẽ có, nếu bạn tick chọn)
        @SerializedName("temperature_2m_max") private List<Double> tempMax;
        @SerializedName("temperature_2m_min") private List<Double> tempMin;
    }
}