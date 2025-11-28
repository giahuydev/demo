package com.myproject.myproject_app.entity.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentData {
    private String time;
    private int interval;

    @JsonProperty("temperature_2m")
    private Double temperature2m;

    @JsonProperty("relative_humidity_2m")
    private Integer relativeHumidity2m;

    @JsonProperty("apparent_temperature")
    private Double apparentTemperature;

    @JsonProperty("is_day")
    private Integer isDay;

    @JsonProperty("precipitation")
    private Double precipitation;

    @JsonProperty("rain")
    private Double rain;

    @JsonProperty("showers")
    private Double showers;

    @JsonProperty("snowfall")
    private Double snowfall;

    @JsonProperty("weather_code")
    private Integer weatherCode;

    @JsonProperty("cloud_cover")
    private Integer cloudCover;

    @JsonProperty("pressure_msl")
    private Double pressureMsl;

    @JsonProperty("surface_pressure")
    private Double surfacePressure;

    @JsonProperty("wind_speed_10m")
    private Double windSpeed10m;

    @JsonProperty("wind_direction_10m")
    private Integer windDirection10m;

    @JsonProperty("wind_gusts_10m")
    private Double windGusts10m;
}