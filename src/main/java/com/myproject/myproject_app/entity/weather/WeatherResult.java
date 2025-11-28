package com.myproject.myproject_app.entity.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResult {

    private Double latitude;
    private Double longitude;

    @JsonProperty("generationtime_ms")
    private Double generationtimeMs;

    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;

    private String timezone;

    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;

    private Double elevation;

    @JsonProperty("current")
    private CurrentData current;

    @JsonProperty("daily")
    private DailyData daily;

    @JsonProperty("hourly")
    private HourlyData hourly;

    @JsonProperty("minutely_15")
    private Minutely15Data minutely15;
}