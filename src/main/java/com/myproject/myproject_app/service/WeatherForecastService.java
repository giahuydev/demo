package com.myproject.myproject_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myproject_app.dto.response.WeatherFormattedResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.weather.DailyData;
import com.myproject.myproject_app.entity.weather.HourlyData;
import com.myproject.myproject_app.entity.weather.Minutely15Data;
import com.myproject.myproject_app.entity.weather.WeatherResult;
import com.myproject.myproject_app.entity.weather.view.DailyRecord;
import com.myproject.myproject_app.entity.weather.view.HourlyRecord;
import com.myproject.myproject_app.entity.weather.view.Minutely15Record;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastService {

    private final NguonDuLieuRepository nguonDuLieuRepository;
    private final StringRedisTemplate redisTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final long CACHE_TTL_MINUTES = 15;

    /**
     * PUBLIC METHOD: Gọi từ Controller
     * Lấy dữ liệu raw -> Chuyển đổi sang format đẹp -> Trả về
     */
    public WeatherFormattedResponse getWeatherFormatted(String tenChucNang, String maModelApi, Double lat, Double lon, Integer forecastDays) {
        // 1. Lấy dữ liệu thô (Raw Data) từ Cache hoặc API
        WeatherResult rawData = getWeatherDataRaw(tenChucNang, maModelApi, lat, lon, forecastDays);

        // 2. Chuyển đổi cấu trúc dữ liệu (Transform)
        return transformToFormattedResponse(rawData);
    }

    /**
     * PRIVATE METHOD: Logic xử lý Cache và gọi API
     */
    private WeatherResult getWeatherDataRaw(String tenChucNang, String maModelApi, Double lat, Double lon, Integer forecastDays) {

        // 1. Validate Input
        int days = (forecastDays == null) ? 7 : forecastDays;
        if (days < 1 || days > 16) {
            throw new AppException(ErrorCode.INVALID_FORECAST_DAYS);
        }

        // 2. Tạo Cache Key
        String cacheKey = String.format("weather:%s:%s:%.2f:%.2f:d%d",
                tenChucNang.replace(" ", "_").toLowerCase(),
                maModelApi, lat, lon, days);

        // 3. Check Redis
        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            try {
                log.info("CACHE HIT: {}", cacheKey);
                return objectMapper.readValue(cachedJson, WeatherResult.class);
            } catch (JsonProcessingException e) {
                log.warn("Lỗi parse Cache: {}", e.getMessage());
            }
        }

        // 4. Lấy Cấu hình từ DB
        NguonDuLieu config = nguonDuLieuRepository.findByTenChucNang(tenChucNang)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        // 5. Build URL
        String url = buildForecastUrl(config, maModelApi, lat, lon, days);
        log.info("CALL API: {}", url);

        // 6. Call API Open-Meteo
        try {
            WeatherResult result = restTemplate.getForObject(url, WeatherResult.class);

            // 7. Lưu Cache
            if (result != null) {
                String jsonToCache = objectMapper.writeValueAsString(result);
                redisTemplate.opsForValue().set(cacheKey, jsonToCache, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            }
            return result;

        } catch (Exception e) {
            log.error("Lỗi API Open-Meteo: {}", e.getMessage());
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    // --- URL BUILDER ---
    private String buildForecastUrl(NguonDuLieu config, String maModelApi, Double lat, Double lon, int days) {
        StringBuilder builder = new StringBuilder();

        builder.append(config.getBaseDomain());
        builder.append("latitude=").append(lat);
        builder.append("&longitude=").append(lon);

        appendParamIfPresent(builder, "daily", config.getDailyParams());
        appendParamIfPresent(builder, "hourly", config.getHourlyParams());
        appendParamIfPresent(builder, "current", config.getCurrentParams());
        appendParamIfPresent(builder, "minutely_15", config.getMinutely15Params());

        builder.append("&timezone=Asia%2FBangkok");
        builder.append("&models=").append(maModelApi);
        builder.append("&forecast_days=").append(days);

        return builder.toString();
    }

    private void appendParamIfPresent(StringBuilder builder, String key, String value) {
        if (value != null && !value.isEmpty()) {
            builder.append("&").append(key).append("=").append(value);
        }
    }

    /**
     * Lấy thời tiết tối ưu cho Hành trình (Chỉ lấy Minutely15)
     * Không ảnh hưởng đến luồng cũ.
     */
    public WeatherFormattedResponse getJourneyWeather(String tenNguon, Double lat, Double lon, LocalDateTime startTime, long durationMinutes) {

        // 1. Tính toán số ngày cần dự báo
        int forecastDays = calculateForecastDays(startTime, durationMinutes);

        // 2. Tạo Key Cache
        String cacheKey = String.format("weather_journey:%s:%.2f:%.2f:d%d",
                tenNguon.replace(" ", "_").toLowerCase(), lat, lon, forecastDays);

        // 3. Kiểm tra Cache Redis
        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            try {
                log.info("CACHE HIT JOURNEY: {}", cacheKey);
                return objectMapper.readValue(cachedJson, WeatherFormattedResponse.class);
            } catch (Exception e) {
                log.warn("Lỗi đọc Cache Journey", e);
            }
        }

        // 4. Nếu Cache Miss -> Gọi API
        log.info("CACHE MISS JOURNEY: Gọi API trực tiếp (Không lưu cache)");

        NguonDuLieu config = nguonDuLieuRepository.findByTenChucNang(tenNguon)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        // Build URL minutely_15
        String url = buildJourneyUrl(config, lat, lon, forecastDays);

        try {
            WeatherResult rawResult = restTemplate.getForObject(url, WeatherResult.class);

            // 5. Transform và Trả về NGAY
            return transformToFormattedResponse(rawResult);

        } catch (Exception e) {
            log.error("Lỗi gọi API Journey: {}", e.getMessage());
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    // Tính số ngày dựa trên thời gian bắt đầu và thời gian đi
    private int calculateForecastDays(LocalDateTime startTime, long durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
        long days = ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate());
        return (int) Math.max(1, Math.min(16, days + 1));
    }

    // Build URL tối giản cho hành trình
    private String buildJourneyUrl(NguonDuLieu config, Double lat, Double lon, int days) {
        StringBuilder sb = new StringBuilder();
        // Base Domain
        sb.append(config.getBaseDomain());
        sb.append("latitude=").append(lat).append("&longitude=").append(lon);

        // Model: Fix cứng best_match theo yêu cầu
        sb.append("&models=best_match");

        // Chỉ lấy minutely_15
        if (config.getMinutely15Params() != null) {
            sb.append("&minutely_15=").append(config.getMinutely15Params());
        }

        sb.append("&forecast_days=").append(days);
        sb.append("&timezone=Asia%2FBangkok");

        return sb.toString();
    }


    private WeatherFormattedResponse transformToFormattedResponse(WeatherResult raw) {
        if (raw == null) return null;
        return WeatherFormattedResponse.builder()
                .latitude(raw.getLatitude())
                .longitude(raw.getLongitude())
                .timezone(raw.getTimezone())
                .elevation(raw.getElevation())
                .current(raw.getCurrent()) // Current giữ nguyên object
                .hourly(transformHourly(raw.getHourly()))
                .daily(transformDaily(raw.getDaily()))
                .minutely15(transformMinutely15(raw.getMinutely15()))
                .build();
    }

    private List<HourlyRecord> transformHourly(HourlyData h) {
        if (h == null || h.getTime() == null) return null;

        List<HourlyRecord> list = new ArrayList<>();
        int size = h.getTime().size();

        for (int i = 0; i < size; i++) {
            HourlyRecord.HourlyRecordBuilder builder = HourlyRecord.builder();
            builder.time(h.getTime().get(i));

            if (h.getTemperature2m() != null) builder.temperature2m(h.getTemperature2m().get(i));
            if (h.getRelativeHumidity2m() != null) builder.relativeHumidity2m(h.getRelativeHumidity2m().get(i));
            if (h.getApparentTemperature() != null) builder.apparentTemperature(h.getApparentTemperature().get(i));
            if (h.getPrecipitationProbability() != null) builder.precipitationProbability(h.getPrecipitationProbability().get(i));
            if (h.getPrecipitation() != null) builder.precipitation(h.getPrecipitation().get(i));
            if (h.getRain() != null) builder.rain(h.getRain().get(i));
            if (h.getShowers() != null) builder.showers(h.getShowers().get(i));
            if (h.getSnowfall() != null) builder.snowfall(h.getSnowfall().get(i));
            if (h.getSnowDepth() != null) builder.snowDepth(h.getSnowDepth().get(i));
            if (h.getWeatherCode() != null) builder.weatherCode(h.getWeatherCode().get(i));
            if (h.getPressureMsl() != null) builder.pressureMsl(h.getPressureMsl().get(i));
            if (h.getCloudCover() != null) builder.cloudCover(h.getCloudCover().get(i));
            if (h.getSurfacePressure() != null) builder.surfacePressure(h.getSurfacePressure().get(i));
            if (h.getVisibility() != null) builder.visibility(h.getVisibility().get(i));
            if (h.getWindSpeed10m() != null) builder.windSpeed10m(h.getWindSpeed10m().get(i));
            if (h.getIsDay() != null) builder.isDay(h.getIsDay().get(i));
            if (h.getUvIndex() != null) builder.uvIndex(h.getUvIndex().get(i));
            if (h.getWindGusts10m() != null) builder.windGusts10m(h.getWindGusts10m().get(i));
            if (h.getWindDirection10m() != null) builder.windDirection10m(h.getWindDirection10m().get(i));

            list.add(builder.build());
        }
        return list;
    }

    private List<DailyRecord> transformDaily(DailyData d) {
        if (d == null || d.getTime() == null) return null;

        List<DailyRecord> list = new ArrayList<>();
        int size = d.getTime().size();

        for (int i = 0; i < size; i++) {
            DailyRecord.DailyRecordBuilder builder = DailyRecord.builder();
            builder.time(d.getTime().get(i));

            if (d.getWeatherCode() != null) builder.weatherCode(d.getWeatherCode().get(i));
            if (d.getTemperature2mMax() != null) builder.temperature2mMax(d.getTemperature2mMax().get(i));
            if (d.getTemperature2mMin() != null) builder.temperature2mMin(d.getTemperature2mMin().get(i));
            if (d.getApparentTemperatureMax() != null) builder.apparentTemperatureMax(d.getApparentTemperatureMax().get(i));
            if (d.getApparentTemperatureMin() != null) builder.apparentTemperatureMin(d.getApparentTemperatureMin().get(i));
            if (d.getSunrise() != null) builder.sunrise(d.getSunrise().get(i));
            if (d.getSunset() != null) builder.sunset(d.getSunset().get(i));
            if (d.getSunshineDuration() != null) builder.sunshineDuration(d.getSunshineDuration().get(i));
            if (d.getUvIndexMax() != null) builder.uvIndexMax(d.getUvIndexMax().get(i));
            if (d.getPrecipitationSum() != null) builder.precipitationSum(d.getPrecipitationSum().get(i));
            if (d.getPrecipitationHours() != null) builder.precipitationHours(d.getPrecipitationHours().get(i));
            if (d.getPrecipitationProbabilityMax() != null) builder.precipitationProbabilityMax(d.getPrecipitationProbabilityMax().get(i));
            if (d.getWindSpeed10mMax() != null) builder.windSpeed10mMax(d.getWindSpeed10mMax().get(i));
            if (d.getWindGusts10mMax() != null) builder.windGusts10mMax(d.getWindGusts10mMax().get(i));
            if (d.getWindDirection10mDominant() != null) builder.windDirection10mDominant(d.getWindDirection10mDominant().get(i));

            list.add(builder.build());
        }
        return list;
    }

    private List<Minutely15Record> transformMinutely15(Minutely15Data m) {
        if (m == null || m.getTime() == null) return null;

        List<Minutely15Record> list = new ArrayList<>();
        int size = m.getTime().size();

        for (int i = 0; i < size; i++) {
            Minutely15Record.Minutely15RecordBuilder builder = Minutely15Record.builder();
            builder.time(m.getTime().get(i));

            if (m.getTemperature2m() != null) builder.temperature2m(m.getTemperature2m().get(i));
            if (m.getRelativeHumidity2m() != null) builder.relativeHumidity2m(m.getRelativeHumidity2m().get(i));
            if (m.getApparentTemperature() != null) builder.apparentTemperature(m.getApparentTemperature().get(i));
            if (m.getPrecipitation() != null) builder.precipitation(m.getPrecipitation().get(i));
            if (m.getRain() != null) builder.rain(m.getRain().get(i));
            if (m.getSnowfall() != null) builder.snowfall(m.getSnowfall().get(i));
            if (m.getSunshineDuration() != null) builder.sunshineDuration(m.getSunshineDuration().get(i));
            if (m.getWeatherCode() != null) builder.weatherCode(m.getWeatherCode().get(i));
            if (m.getWindSpeed10m() != null) builder.windSpeed10m(m.getWindSpeed10m().get(i));
            if (m.getWindDirection10m() != null) builder.windDirection10m(m.getWindDirection10m().get(i));
            if (m.getWindGusts10m() != null) builder.windGusts10m(m.getWindGusts10m().get(i));
            if (m.getVisibility() != null) builder.visibility(m.getVisibility().get(i));
            if (m.getIsDay() != null) builder.isDay(m.getIsDay().get(i));
            if (m.getLightningPotential() != null) builder.lightningPotential(m.getLightningPotential().get(i));

            list.add(builder.build());
        }
        return list;
    }
}