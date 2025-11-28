package com.myproject.myproject_app.entity.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyData {
    private List<String> time;

    @JsonProperty("temperature_2m")
    private List<Double> temperature2m;

    @JsonProperty("relative_humidity_2m")
    private List<Integer> relativeHumidity2m;

    @JsonProperty("apparent_temperature")
    private List<Double> apparentTemperature;

    @JsonProperty("precipitation_probability")
    private List<Integer> precipitationProbability;

    @JsonProperty("precipitation")
    private List<Double> precipitation;

    @JsonProperty("rain")
    private List<Double> rain;

    @JsonProperty("showers")
    private List<Double> showers;

    @JsonProperty("snowfall")
    private List<Double> snowfall;

    @JsonProperty("snow_depth")
    private List<Double> snowDepth;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;

    @JsonProperty("pressure_msl")
    private List<Double> pressureMsl;

    @JsonProperty("cloud_cover")
    private List<Integer> cloudCover;

    @JsonProperty("surface_pressure")
    private List<Double> surfacePressure;

    @JsonProperty("visibility")
    private List<Double> visibility;

    @JsonProperty("wind_speed_10m")
    private List<Double> windSpeed10m;

    @JsonProperty("is_day")
    private List<Integer> isDay;

    @JsonProperty("uv_index")
    private List<Double> uvIndex;

    @JsonProperty("wind_gusts_10m")
    private List<Double> windGusts10m;

    @JsonProperty("wind_direction_10m")
    private List<Integer> windDirection10m;
}