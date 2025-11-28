package com.myproject.myproject_app.entity.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Minutely15Data {
    private List<String> time;

    @JsonProperty("temperature_2m")
    private List<Double> temperature2m;

    @JsonProperty("relative_humidity_2m")
    private List<Integer> relativeHumidity2m;

    @JsonProperty("apparent_temperature")
    private List<Double> apparentTemperature;

    @JsonProperty("precipitation")
    private List<Double> precipitation;

    @JsonProperty("rain")
    private List<Double> rain;

    @JsonProperty("snowfall")
    private List<Double> snowfall;

    @JsonProperty("sunshine_duration")
    private List<Double> sunshineDuration;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;

    @JsonProperty("wind_speed_10m")
    private List<Double> windSpeed10m;

    @JsonProperty("wind_direction_10m")
    private List<Integer> windDirection10m;

    @JsonProperty("wind_gusts_10m")
    private List<Double> windGusts10m;

    @JsonProperty("visibility")
    private List<Double> visibility;

    @JsonProperty("is_day")
    private List<Integer> isDay;

    @JsonProperty("lightning_potential")
    private List<Double> lightningPotential;
}