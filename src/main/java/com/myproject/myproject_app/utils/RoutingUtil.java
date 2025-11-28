package com.myproject.myproject_app.utils;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoutingUtil {

    public record GeoPoint(double lat, double lon) {}

    /**
     * Giải mã chuỗi Polyline (Encoded String) thành danh sách tọa độ
     */
    public List<GeoPoint> decodePolyline(String encoded) {
        List<GeoPoint> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new GeoPoint((double) lat / 1E5, (double) lng / 1E5));
        }
        return poly;
    }

    /**
     * Tìm tọa độ tại một khoảng cách cụ thể (mét) tính từ điểm xuất phát
     */
    public GeoPoint findPointAtDistanceFromStart(List<GeoPoint> path, double targetMeters) {
        if (path == null || path.isEmpty()) return null;
        if (targetMeters <= 0) return path.get(0);

        double distanceCovered = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            GeoPoint p1 = path.get(i);
            GeoPoint p2 = path.get(i + 1);

            double distSegment = haversineMeters(p1.lat, p1.lon, p2.lat, p2.lon);

            if (distanceCovered + distSegment >= targetMeters) {
                double remain = targetMeters - distanceCovered;
                double ratio = remain / distSegment;
                return new GeoPoint(
                        p1.lat + (p2.lat - p1.lat) * ratio,
                        p1.lon + (p2.lon - p1.lon) * ratio
                );
            }
            distanceCovered += distSegment;
        }
        return path.get(path.size() - 1);
    }

    private double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Bán kính trái đất (mét)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}