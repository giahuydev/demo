package com.myproject.myproject_app.dto.response; // Nhớ sửa package cho đúng

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.util.List;

@Data
public class TrackAsiaSearchResponse {
    private String status;
    private List<Result> results;

    @Data
    public static class Result {
        @SerializedName("formatted_address")
        private String formattedAddress;
        private Geometry geometry;
    }

    @Data
    public static class Geometry {
        private Location location;
    }

    @Data
    public static class Location {
        private double lat;
        private double lng;
    }
}