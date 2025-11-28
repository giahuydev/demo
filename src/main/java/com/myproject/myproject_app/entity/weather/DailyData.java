package com.myproject.myproject_app.entity.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyData {
    private List<String> time;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;

    @JsonProperty("temperature_2m_max")
    private List<Double> temperature2mMax;

    @JsonProperty("temperature_2m_min")
    private List<Double> temperature2mMin;

    @JsonProperty("apparent_temperature_max")
    private List<Double> apparentTemperatureMax;

    @JsonProperty("apparent_temperature_min")
    private List<Double> apparentTemperatureMin;

    @JsonProperty("sunrise")
    private List<String> sunrise;

    @JsonProperty("sunset")
    private List<String> sunset;

    @JsonProperty("sunshine_duration")
    private List<Double> sunshineDuration;

    @JsonProperty("uv_index_max")
    private List<Double> uvIndexMax;

    @JsonProperty("precipitation_sum")
    private List<Double> precipitationSum;

    @JsonProperty("precipitation_hours")
    private List<Double> precipitationHours;

    @JsonProperty("precipitation_probability_max")
    private List<Integer> precipitationProbabilityMax;

    @JsonProperty("wind_speed_10m_max")
    private List<Double> windSpeed10mMax;

    @JsonProperty("wind_gusts_10m_max")
    private List<Double> windGusts10mMax;

    @JsonProperty("wind_direction_10m_dominant")
    private List<Integer> windDirection10mDominant;
}