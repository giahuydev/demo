package com.myproject.myproject_app.dto.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackAsiaResponse {
    private String status;
    private List<Route> routes;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        // Mapping trường "overview_polyline" trong JSON sang biến overviewPolyline
        @JsonProperty("overview_polyline")
        private Polyline overviewPolyline;

        private List<Leg> legs;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Polyline {
        private String points; // Chuỗi mã hóa đường đi (Encoded String)
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Leg {
        private ValueText distance; // Khoảng cách
        private ValueText duration; // Thời gian di chuyển
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ValueText {
        private String text;  // khoảng cách hoặc thời gian : km, h
        private Double value; //  khoảng cách hoặc thời gian : m, s
    }
}