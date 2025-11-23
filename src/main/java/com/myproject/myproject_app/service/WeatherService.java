package com.myproject.myproject_app.service;

import com.google.gson.Gson;
import com.myproject.myproject_app.dto.response.GeocodingResponse;
import com.myproject.myproject_app.dto.response.OpenMeteoResponse;
import com.myproject.myproject_app.dto.response.TrackAsiaSearchResponse;
import com.myproject.myproject_app.dto.response.WeatherResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final NguonDuLieuRepository nguonDuLieuRepository;
    private final StringRedisTemplate redisTemplate;
    private final RestTemplate restTemplate;
    private final Gson gson;

    // Lấy Key từ file application.yaml
    @Value("${trackasia.key}")
    private String trackAsiaKey;

    // URL API TrackAsia Search V2 (Text Search)
    private static final String TRACK_ASIA_SEARCH_API = "https://maps.track-asia.com/api/v2/place/textsearch/json?query={query}&key={key}";

    private static final double SEARCH_RADIUS_KM = 3.0;
    private static final long CACHE_TTL_HOURS = 1;

    public WeatherResponse getWeather(String locationName, String sourceName) {

        // 1. Validate Nguồn
        NguonDuLieu config = nguonDuLieuRepository.findBytenNguon(sourceName)
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        if (!config.isKichHoat()) throw new AppException(ErrorCode.SOURCE_INACTIVE);

        // 2. Geocoding (Dùng TrackAsia)
        GeocodingResponse.Result coords = getCoordinates(locationName);
        double lat = coords.getLatitude();
        double lon = coords.getLongitude();

        // 3. Check Redis
        String geoIndexKey = "geo_index:" + sourceName.toLowerCase();
        String existingCacheKey = findNearestCacheKey(geoIndexKey, lat, lon);

        if (existingCacheKey != null) {
            String cachedJson = redisTemplate.opsForValue().get(existingCacheKey);
            if (cachedJson != null) {
                log.info("CACHE HIT: {}", existingCacheKey);
                WeatherResponse response = gson.fromJson(cachedJson, WeatherResponse.class);
                response.setLocationName(coords.getName());
                return response;
            } else {
                redisTemplate.opsForGeo().remove(geoIndexKey, existingCacheKey);
            }
        }

        // 4. Call API Thời tiết (OpenMeteo)
        log.info("CACHE MISS: Gọi API {} tại [{}, {}]", sourceName, lat, lon);

        String finalUrl = config.getUrlApi()
                .replace("{lat}", String.valueOf(lat))
                .replace("{lon}", String.valueOf(lon));

        try {
            String jsonResponse = restTemplate.getForObject(finalUrl, String.class);
            OpenMeteoResponse rawData = gson.fromJson(jsonResponse, OpenMeteoResponse.class);

            // Map dữ liệu (Có xử lý null an toàn)
            WeatherResponse finalResponse = mapToWeatherResponse(rawData, coords, sourceName);

            // 5. Lưu Cache
            String newCacheKey = String.format("weather:%s:%.4f:%.4f", sourceName.toLowerCase(), lat, lon);
            redisTemplate.opsForValue().set(newCacheKey, gson.toJson(finalResponse), CACHE_TTL_HOURS, TimeUnit.HOURS);
            redisTemplate.opsForGeo().add(geoIndexKey, new Point(lon, lat), newCacheKey);

            return finalResponse;

        } catch (Exception e) {
            log.error("Lỗi gọi API Weather: ", e);
            if (e instanceof AppException) throw e;
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    // --- HELPER: Geocoding dùng TrackAsia ---
    private GeocodingResponse.Result getCoordinates(String locationName) {
        try {
            // Encode URL tự động bởi RestTemplate, chỉ cần replace placeholder
            String url = TRACK_ASIA_SEARCH_API
                    .replace("{query}", locationName)
                    .replace("{key}", trackAsiaKey);

            String jsonResponse = restTemplate.getForObject(url, String.class);
            TrackAsiaSearchResponse searchResponse = gson.fromJson(jsonResponse, TrackAsiaSearchResponse.class);

            if (searchResponse != null && "OK".equals(searchResponse.getStatus())
                    && searchResponse.getResults() != null && !searchResponse.getResults().isEmpty()) {

                // Lấy kết quả tốt nhất
                TrackAsiaSearchResponse.Result bestMatch = searchResponse.getResults().get(0);

                double lat = bestMatch.getGeometry().getLocation().getLat();
                double lng = bestMatch.getGeometry().getLocation().getLng();
                String fullName = bestMatch.getFormattedAddress();

                // Map về Result chung
                GeocodingResponse.Result result = new GeocodingResponse.Result();
                result.setLatitude(lat);
                result.setLongitude(lng);
                result.setName(fullName);
                result.setCountry("Vietnam");

                return result;
            }
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);

        } catch (Exception e) {
            log.error("Lỗi TrackAsia: ", e);
            if (e instanceof AppException) throw e;
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }
    }

    // --- HELPER: Redis ---
    private String findNearestCacheKey(String geoIndexKey, double lat, double lon) {
        try {
            Circle circle = new Circle(new Point(lon, lat), new Distance(SEARCH_RADIUS_KM, Metrics.KILOMETERS));
            GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(geoIndexKey, circle);
            if (results != null && !results.getContent().isEmpty()) {
                return results.getContent().get(0).getContent().getName();
            }
        } catch (Exception e) {
            log.warn("Lỗi Redis Geo (Có thể do chưa bật Redis): {}", e.getMessage());
        }
        return null;
    }

    // --- HELPER: Mapping (An toàn với Null) ---
    private WeatherResponse mapToWeatherResponse(OpenMeteoResponse raw,
                                                 GeocodingResponse.Result coords,
                                                 String source) {
        OpenMeteoResponse.Current c = raw.getCurrent();

        // An toàn khi lấy UV từ Hourly
        double currentUV = 0.0;
        if (raw.getHourly() != null && raw.getHourly().getUvIndex() != null && !raw.getHourly().getUvIndex().isEmpty()) {
            currentUV = raw.getHourly().getUvIndex().get(0);
        }

        WeatherResponse.CurrentWeather currentDTO = WeatherResponse.CurrentWeather.builder()
                .time(c.getTime())
                .temperature(c.getTemperature())
                .feelsLike(c.getFeelsLike())
                .humidity(c.getHumidity())
                .windSpeed(c.getWindSpeed())
                .windDirection(c.getWindDirection())
                .cloudCover(c.getCloudCover())
                .condition(c.getIsDay() == 1 ? "Day" : "Night")
                .uvIndex(currentUV)
                .latitude(coords.getLatitude())
                .longitude(coords.getLongitude())
                .build();

        List<WeatherResponse.HourlyForecast> hourlyList = new ArrayList<>();
        if (raw.getHourly() != null && raw.getHourly().getTime() != null) {
            OpenMeteoResponse.Hourly h = raw.getHourly();
            int limit = Math.min(h.getTime().size(), 24);

            for (int i = 0; i < limit; i++) {
                // Check null từng trường để tránh NullPointerException
                double temp = (h.getTemperature() != null && h.getTemperature().size() > i) ? h.getTemperature().get(i) : 0.0;
                int humid = (h.getHumidity() != null && h.getHumidity().size() > i) ? h.getHumidity().get(i) : 0;
                int rain = (h.getPrecipProb() != null && h.getPrecipProb().size() > i && h.getPrecipProb().get(i) != null) ? h.getPrecipProb().get(i) : 0;
                double uv = (h.getUvIndex() != null && h.getUvIndex().size() > i && h.getUvIndex().get(i) != null) ? h.getUvIndex().get(i) : 0.0;
                double soil = (h.getSoilMoisture() != null && h.getSoilMoisture().size() > i && h.getSoilMoisture().get(i) != null) ? h.getSoilMoisture().get(i) : 0.0;
                int isDay = (h.getIsDay() != null && h.getIsDay().size() > i && h.getIsDay().get(i) != null) ? h.getIsDay().get(i) : 0;

                hourlyList.add(WeatherResponse.HourlyForecast.builder()
                        .time(h.getTime().get(i))
                        .temperature(temp)
                        .humidity(humid)
                        .rainChance(rain)
                        .uvIndex(uv)
                        .soilMoisture(soil)
                        .isDay(isDay == 1 ? "Day" : "Night")
                        .build());
            }
        }

        // Mapping Daily (Tương tự, bạn có thể thêm check null nếu cần)
        List<WeatherResponse.DailyForecast> dailyList = new ArrayList<>();
        if (raw.getDaily() != null && raw.getDaily().getTime() != null) {
            OpenMeteoResponse.Daily d = raw.getDaily();
            int size = Math.min(d.getTime().size(), 7);
            for(int i=0; i<size; i++){
                dailyList.add(WeatherResponse.DailyForecast.builder()
                        .date(d.getTime().get(i))
                        .maxUV(d.getUvIndexMax().get(i))
                        .totalRain(d.getRainSum().get(i))
                        .sunrise(d.getSunrise().get(i))
                        .sunset(d.getSunset().get(i))
                        .build());
            }
        }

        return WeatherResponse.builder()
                .locationName(coords.getName())
                .sourceUsed(source)
                .lastUpdated(java.time.LocalDateTime.now().toString())
                .current(currentDTO)
                .hourly(hourlyList)
                .daily(dailyList)
                .build();
    }
}