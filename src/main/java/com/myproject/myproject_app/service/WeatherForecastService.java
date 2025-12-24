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
import org.springframework.web.util.UriComponentsBuilder;

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

        // 5. Build URL (FIXED VERSION)
        String url = buildForecastUrl(config, maModelApi, lat, lon, days);
        log.info("CACHE MISS: Gọi API - {}", url);

        // 6. Call API Open-Meteo
        try {
            WeatherResult result = restTemplate.getForObject(url, WeatherResult.class);

            // 7. Lưu Cache
            if (result != null) {
                String jsonToCache = objectMapper.writeValueAsString(result);
                redisTemplate.opsForValue().set(cacheKey, jsonToCache, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                log.info("Lưu cache thành công: {}", cacheKey);
            }
            return result;

        } catch (Exception e) {
            log.error("Lỗi API Open-Meteo: {}", e.getMessage());
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    /**
     * ✅ FIXED: Build URL - Chỉ thêm hourly params (giống API của bạn)
     * <p>
     * Dữ liệu từ DB:
     * - baseDomain: "https://api.open-meteo.com/v1/forecast?temporal_resolution=native&"
     * - hourlyParams: "temperature_2m,relative_humidity_2m,..."
     * - maModelApi: "best_match"
     */
    // Tìm đến đoạn code này trong file WeatherForecastService.java
    private String buildForecastUrl(NguonDuLieu config, String maModelApi, Double lat, Double lon, int days) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getBaseDomain());

        builder.queryParam("latitude", lat);
        builder.queryParam("longitude", lon);

        // ✅ Thêm DAILY
        if (config.getDailyParams() != null && !config.getDailyParams().isEmpty()) {
            builder.queryParam("daily", config.getDailyParams());
        }

        // ✅ Thêm HOURLY
        if (config.getHourlyParams() != null && !config.getHourlyParams().isEmpty()) {
            builder.queryParam("hourly", config.getHourlyParams());
        }

        // ✅ Thêm CURRENT
        if (config.getCurrentParams() != null && !config.getCurrentParams().isEmpty()) {
            builder.queryParam("current", config.getCurrentParams());
        }

        // ✅ Thêm MINUTELY_15
        if (config.getMinutely15Params() != null && !config.getMinutely15Params().isEmpty()) {
            builder.queryParam("minutely_15", config.getMinutely15Params());
        }

        builder.queryParam("models", maModelApi);
        builder.queryParam("timezone", "Asia/Bangkok");
        builder.queryParam("forecast_days", days);

        String finalUrl = builder.build().toUriString();
        log.info("✅ Final URL Generated: {}", finalUrl);
        return finalUrl;
    }

    /**
     * ✅ UPDATED: Chỉ thêm param nếu có giá trị và không trống
     */
    private void appendParamIfPresent(StringBuilder builder, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            builder.append("&").append(key).append("=").append(value);
            log.debug("Thêm param: {}={}", key, value.substring(0, Math.min(50, value.length())) + "...");
        }
    }

    // ============================================================================
    // CÁC PHƯƠNG THỨC KHÁC (GIỮ NGUYÊN)
    // ============================================================================

    public WeatherFormattedResponse getJourneyWeather(String tenNguon, Double lat, Double lon, LocalDateTime startTime, long durationMinutes) {
        int forecastDays = calculateForecastDays(startTime, durationMinutes);
        String cacheKey = String.format("weather_journey:%s:%.2f:%.2f:d%d",
                tenNguon.replace(" ", "_").toLowerCase(), lat, lon, forecastDays);

        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            try {
                log.info("CACHE HIT JOURNEY: {}", cacheKey);
                return objectMapper.readValue(cachedJson, WeatherFormattedResponse.class);
            } catch (Exception e) {
                log.warn("Lỗi đọc Cache Journey", e);
            }
        }

        log.info("CACHE MISS JOURNEY: Gọi API trực tiếp");
        NguonDuLieu config = nguonDuLieuRepository.findByTenChucNang(tenNguon)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        String url = buildJourneyUrl(config, lat, lon, forecastDays);
        try {
            WeatherResult rawResult = restTemplate.getForObject(url, WeatherResult.class);
            return transformToFormattedResponse(rawResult);
        } catch (Exception e) {
            log.error("Lỗi gọi API Journey: {}", e.getMessage());
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private int calculateForecastDays(LocalDateTime startTime, long durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
        long days = ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate());
        return (int) Math.max(1, Math.min(16, days + 1));
    }

    private String buildJourneyUrl(NguonDuLieu config, Double lat, Double lon, int days) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getBaseDomain());

        builder.queryParam("latitude", lat);
        builder.queryParam("longitude", lon);
        builder.queryParam("models", "best_match");

        if (config.getMinutely15Params() != null && !config.getMinutely15Params().isEmpty()) {
            builder.queryParam("minutely_15", config.getMinutely15Params());
        }

        builder.queryParam("forecast_days", days);
        builder.queryParam("timezone", "Asia/Bangkok");  // ✅ ĐÚNG

        String finalUrl = builder.build().toUriString();
        log.info("✅ Journey URL Generated: {}", finalUrl);
        return finalUrl;
    }

    public String getEventWeatherString(Double lat, Double lon, LocalDateTime startTime) {
        try {
            WeatherFormattedResponse res = getPrecisionEventWeather("Dự báo thời tiết", lat, lon, startTime, 120);
            return formatWeatherForGemini(res, true);
        } catch (Exception e) {
            log.error("Lỗi lấy thời tiết sự kiện: {}", e.getMessage());
            return "Không có dữ liệu chi tiết.";
        }
    }

    public String getCurrentWeatherString(Double lat, Double lon) {
        try {
            WeatherFormattedResponse res = getHourlyWeatherForRecurring("Dự báo thời tiết", lat, lon, LocalDateTime.now());
            return formatWeatherForGemini(res, false);
        } catch (Exception e) {
            log.error("Lỗi lấy thời tiết hàng ngày: {}", e.getMessage());
            return "Không có dữ liệu hiện tại.";
        }
    }

    private String formatWeatherForGemini(WeatherFormattedResponse res, boolean useMinutely) {
        if (res == null) return "Không có dữ liệu.";
        StringBuilder sb = new StringBuilder();
        sb.append("Dữ liệu thời tiết chi tiết (Full Specs):\n");

        if (useMinutely && res.getMinutely15() != null && !res.getMinutely15().isEmpty()) {
            for (Minutely15Record rec : res.getMinutely15()) {
                String timeStr = (rec.getTime() != null && rec.getTime().length() >= 16)
                        ? rec.getTime().substring(11, 16) : rec.getTime();
                sb.append(String.format("- %s | Temp: %.1f°C (Feel: %.1f°C), Hum: %d%% | Precip: %.1fmm | Wind: %.1fkm/h | [WMO: %d]\n",
                        timeStr, rec.getTemperature2m(), rec.getApparentTemperature(), rec.getRelativeHumidity2m(),
                        rec.getPrecipitation(), rec.getWindSpeed10m(), rec.getWeatherCode()));
            }
        } else if (res.getHourly() != null && !res.getHourly().isEmpty()) {
            for (HourlyRecord rec : res.getHourly()) {
                String timeStr = (rec.getTime() != null && rec.getTime().length() >= 16)
                        ? rec.getTime().substring(11, 16) : rec.getTime();
                sb.append(String.format("- %s | Temp: %.1f°C (Feel: %.1f°C), Hum: %d%% | Precip: %.1fmm (Prob: %d%%) | Wind: %.1fkm/h | [WMO: %d]\n",
                        timeStr, rec.getTemperature2m(), rec.getApparentTemperature(), rec.getRelativeHumidity2m(),
                        rec.getPrecipitation(), rec.getPrecipitationProbability(), rec.getWindSpeed10m(), rec.getWeatherCode()));
            }
        }
        return sb.toString();
    }

    public WeatherFormattedResponse getHourlyWeatherForRecurring(String tenNguon, Double lat, Double lon, LocalDateTime runTime) {
        int forecastDays = 2;
        String cacheKey = String.format("weather_hourly_recurring:%s:%.2f:%.2f:d%d",
                tenNguon.replace(" ", "_").toLowerCase(), lat, lon, forecastDays);

        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            try {
                return objectMapper.readValue(cachedJson, WeatherFormattedResponse.class);
            } catch (Exception e) {
                log.warn("Cache error", e);
            }
        }

        NguonDuLieu config = nguonDuLieuRepository.findByTenChucNang(tenNguon)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        String url = buildHourlyUrl(config, lat, lon, forecastDays);

        try {
            WeatherResult rawResult = restTemplate.getForObject(url, WeatherResult.class);
            LocalDateTime endTime = runTime.plusHours(24);
            filterRawResultByTime(rawResult, runTime, endTime, true);
            WeatherFormattedResponse response = transformToFormattedResponse(rawResult);

            if (response != null) {
                redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(response), 30, TimeUnit.MINUTES);
            }
            return response;
        } catch (Exception e) {
            log.error("Lỗi API Recurring: {}", e.getMessage());
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    public WeatherFormattedResponse getPrecisionEventWeather(String tenNguon, Double lat, Double lon, LocalDateTime startTime, long durationMinutes) {
        int forecastDays = calculateForecastDays(startTime, durationMinutes);
        String cacheKey = String.format("weather_event_precision:%s:%.2f:%.2f:d%d",
                tenNguon.replace(" ", "_").toLowerCase(), lat, lon, forecastDays);

        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            try {
                return objectMapper.readValue(cachedJson, WeatherFormattedResponse.class);
            } catch (Exception e) {
                log.warn("Cache error", e);
            }
        }

        NguonDuLieu config = nguonDuLieuRepository.findByTenChucNang(tenNguon)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        String url = buildJourneyUrl(config, lat, lon, forecastDays);

        try {
            WeatherResult rawResult = restTemplate.getForObject(url, WeatherResult.class);
            LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
            filterRawResultByTime(rawResult, startTime, endTime, false);
            WeatherFormattedResponse response = transformToFormattedResponse(rawResult);

            if (response != null) {
                redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(response), 30, TimeUnit.MINUTES);
            }
            return response;
        } catch (Exception e) {
            log.error("Lỗi API Precision Event: {}", e.getMessage());
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private void filterRawResultByTime(WeatherResult raw, LocalDateTime start, LocalDateTime end, boolean isHourly) {
        if (raw == null) return;
        List<String> times = isHourly && raw.getHourly() != null ? raw.getHourly().getTime()
                : !isHourly && raw.getMinutely15() != null ? raw.getMinutely15().getTime() : null;

        if (times == null || times.isEmpty()) return;

        List<Integer> validIndices = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            LocalDateTime apiTime = LocalDateTime.parse(times.get(i));
            if (apiTime.isAfter(start.minusMinutes(1)) && apiTime.isBefore(end.plusMinutes(1))) {
                validIndices.add(i);
            }
        }

        if (isHourly) {
            filterHourlyData(raw.getHourly(), validIndices);
        } else {
            filterMinutely15Data(raw.getMinutely15(), validIndices);
        }
    }

    private void filterHourlyData(HourlyData h, List<Integer> indices) {
        if (h == null) return;
        h.setTime(filterList(h.getTime(), indices));
        h.setTemperature2m(filterList(h.getTemperature2m(), indices));
        h.setRelativeHumidity2m(filterList(h.getRelativeHumidity2m(), indices));
        h.setApparentTemperature(filterList(h.getApparentTemperature(), indices));
        h.setPrecipitationProbability(filterList(h.getPrecipitationProbability(), indices));
        h.setPrecipitation(filterList(h.getPrecipitation(), indices));
        h.setRain(filterList(h.getRain(), indices));
        h.setShowers(filterList(h.getShowers(), indices));
        h.setSnowfall(filterList(h.getSnowfall(), indices));
        h.setSnowDepth(filterList(h.getSnowDepth(), indices));
        h.setWeatherCode(filterList(h.getWeatherCode(), indices));
        h.setPressureMsl(filterList(h.getPressureMsl(), indices));
        h.setCloudCover(filterList(h.getCloudCover(), indices));
        h.setSurfacePressure(filterList(h.getSurfacePressure(), indices));
        h.setVisibility(filterList(h.getVisibility(), indices));
        h.setWindSpeed10m(filterList(h.getWindSpeed10m(), indices));
        h.setIsDay(filterList(h.getIsDay(), indices));
        h.setUvIndex(filterList(h.getUvIndex(), indices));
        h.setWindGusts10m(filterList(h.getWindGusts10m(), indices));
        h.setWindDirection10m(filterList(h.getWindDirection10m(), indices));
    }

    private void filterMinutely15Data(Minutely15Data m, List<Integer> indices) {
        if (m == null) return;
        m.setTime(filterList(m.getTime(), indices));
        m.setTemperature2m(filterList(m.getTemperature2m(), indices));
        m.setRelativeHumidity2m(filterList(m.getRelativeHumidity2m(), indices));
        m.setApparentTemperature(filterList(m.getApparentTemperature(), indices));
        m.setPrecipitation(filterList(m.getPrecipitation(), indices));
        m.setRain(filterList(m.getRain(), indices));
        m.setSnowfall(filterList(m.getSnowfall(), indices));
        m.setSunshineDuration(filterList(m.getSunshineDuration(), indices));
        m.setWeatherCode(filterList(m.getWeatherCode(), indices));
        m.setWindSpeed10m(filterList(m.getWindSpeed10m(), indices));
        m.setWindDirection10m(filterList(m.getWindDirection10m(), indices));
        m.setWindGusts10m(filterList(m.getWindGusts10m(), indices));
        m.setVisibility(filterList(m.getVisibility(), indices));
        m.setIsDay(filterList(m.getIsDay(), indices));
        m.setLightningPotential(filterList(m.getLightningPotential(), indices));
    }

    private <T> List<T> filterList(List<T> original, List<Integer> indices) {
        if (original == null) return null;
        List<T> filtered = new ArrayList<>();
        for (Integer index : indices) {
            if (index < original.size()) {
                filtered.add(original.get(index));
            }
        }
        return filtered;
    }

    private String buildHourlyUrl(NguonDuLieu config, Double lat, Double lon, int days) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getBaseDomain());

        builder.queryParam("latitude", lat);
        builder.queryParam("longitude", lon);
        builder.queryParam("models", "best_match");

        if (config.getHourlyParams() != null && !config.getHourlyParams().isEmpty()) {
            builder.queryParam("hourly", config.getHourlyParams());
        }

        if (config.getCurrentParams() != null && !config.getCurrentParams().isEmpty()) {
            builder.queryParam("current", config.getCurrentParams());
        }

        builder.queryParam("forecast_days", days);
        builder.queryParam("timezone", "Asia/Bangkok");  // ✅ ĐÚNG

        String finalUrl = builder.build().toUriString();
        log.info("✅ Hourly URL Generated: {}", finalUrl);
        return finalUrl;
    }

    private WeatherFormattedResponse transformToFormattedResponse(WeatherResult raw) {
        if (raw == null) return null;
        return WeatherFormattedResponse.builder()
                .latitude(raw.getLatitude())
                .longitude(raw.getLongitude())
                .timezone(raw.getTimezone())
                .elevation(raw.getElevation())
                .current(raw.getCurrent())
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
            HourlyRecord.HourlyRecordBuilder builder = HourlyRecord.builder().time(h.getTime().get(i));
            if (h.getTemperature2m() != null) builder.temperature2m(h.getTemperature2m().get(i));
            if (h.getRelativeHumidity2m() != null) builder.relativeHumidity2m(h.getRelativeHumidity2m().get(i));
            if (h.getApparentTemperature() != null) builder.apparentTemperature(h.getApparentTemperature().get(i));
            if (h.getPrecipitationProbability() != null)
                builder.precipitationProbability(h.getPrecipitationProbability().get(i));
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
            DailyRecord.DailyRecordBuilder builder = DailyRecord.builder().time(d.getTime().get(i));
            if (d.getWeatherCode() != null) builder.weatherCode(d.getWeatherCode().get(i));
            if (d.getTemperature2mMax() != null) builder.temperature2mMax(d.getTemperature2mMax().get(i));
            if (d.getTemperature2mMin() != null) builder.temperature2mMin(d.getTemperature2mMin().get(i));
            if (d.getApparentTemperatureMax() != null)
                builder.apparentTemperatureMax(d.getApparentTemperatureMax().get(i));
            if (d.getApparentTemperatureMin() != null)
                builder.apparentTemperatureMin(d.getApparentTemperatureMin().get(i));
            if (d.getSunrise() != null) builder.sunrise(d.getSunrise().get(i));
            if (d.getSunset() != null) builder.sunset(d.getSunset().get(i));
            if (d.getSunshineDuration() != null) builder.sunshineDuration(d.getSunshineDuration().get(i));
            if (d.getUvIndexMax() != null) builder.uvIndexMax(d.getUvIndexMax().get(i));
            if (d.getPrecipitationSum() != null) builder.precipitationSum(d.getPrecipitationSum().get(i));
            if (d.getPrecipitationHours() != null) builder.precipitationHours(d.getPrecipitationHours().get(i));
            if (d.getPrecipitationProbabilityMax() != null)
                builder.precipitationProbabilityMax(d.getPrecipitationProbabilityMax().get(i));
            if (d.getWindSpeed10mMax() != null) builder.windSpeed10mMax(d.getWindSpeed10mMax().get(i));
            if (d.getWindGusts10mMax() != null) builder.windGusts10mMax(d.getWindGusts10mMax().get(i));
            if (d.getWindDirection10mDominant() != null)
                builder.windDirection10mDominant(d.getWindDirection10mDominant().get(i));
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