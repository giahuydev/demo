package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.internal.TrackAsiaResponse;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.utils.RoutingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService {

    private final RestTemplate restTemplate;
    private final RoutingUtil routingUtil;

    @Value("${track-asia.api-key}")
    private String trackAsiaKey;

    @Value("${track-asia.base-url}")
    private String trackAsiaBaseUrl;

    public RouteProcessResult getRouteAndSamplePoints(double latDi, double lonDi, double latDen, double lonDen, String phuongTien) {
        // 1. Map phương tiện
        String mode = switch (phuongTien != null ? phuongTien.toUpperCase() : "DRIVING") {
            case "XE_DAP" -> "cycling";
            case "DI_BO" -> "walking";
            default -> "driving"; // MOTO, OTO
        };

        // 2. Build URL
        String url = UriComponentsBuilder.fromHttpUrl(trackAsiaBaseUrl)
                .queryParam("origin", latDi + "," + lonDi)
                .queryParam("destination", latDen + "," + lonDen)
                .queryParam("mode", mode)
                .queryParam("key", trackAsiaKey)
                .toUriString();

        // 3. Gọi API
        TrackAsiaResponse response;
        try {
            response = restTemplate.getForObject(url, TrackAsiaResponse.class);
        } catch (Exception e) {
            log.error("TrackAsia API Error", e);
            throw new AppException(ErrorCode.EXTERNAL_API_ERROR);
        }

        if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()) {
            throw new AppException(ErrorCode.ROUTE_NOT_FOUND);
        }

        // 4. Xử lý dữ liệu
        TrackAsiaResponse.Route route = response.getRoutes().get(0);
        String polyline = route.getOverviewPolyline().getPoints();

        // TrackAsia trả về value là giây và mét
        double totalSeconds = route.getLegs().get(0).getDuration().getValue();
        double totalMeters = route.getLegs().get(0).getDistance().getValue();

        // 5. Giải mã & Lấy mẫu (Sampling)
        List<RoutingUtil.GeoPoint> fullPath = routingUtil.decodePolyline(polyline);
        List<TimePoint> timePoints = new ArrayList<>();

        // Lấy mẫu mỗi 15 phút (900 giây)
        int stepSeconds = 15 * 60;
        for (int t = 0; t <= totalSeconds; t += stepSeconds) {
            double ratio = (double) t / totalSeconds;
            double targetDistance = totalMeters * ratio;

            RoutingUtil.GeoPoint point = routingUtil.findPointAtDistanceFromStart(fullPath, targetDistance);
            if (point != null) {
                timePoints.add(new TimePoint(t / 60, point.lat(), point.lon()));
            }
        }

        // Luôn thêm điểm cuối cùng nếu chưa có
        if (totalSeconds % stepSeconds != 0) {
            timePoints.add(new TimePoint((long) totalSeconds / 60, latDen, lonDen));
        }

        return new RouteProcessResult(polyline, (long) totalSeconds / 60, timePoints);
    }

    public record RouteProcessResult(String overviewPolyline, Long durationMinutes, List<TimePoint> waypoints) {}
    public record TimePoint(long minuteOffset, double lat, double lon) {}
}