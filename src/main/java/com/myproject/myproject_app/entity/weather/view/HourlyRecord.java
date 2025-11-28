package com.myproject.myproject_app.entity.weather.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HourlyRecord {
    private String time;
    private Double temperature2m;
    private Integer relativeHumidity2m;
    private Double apparentTemperature;
    private Integer precipitationProbability;
    private Double precipitation;
    private Double rain;
    private Double showers;
    private Double snowfall;
    private Double snowDepth;
    private Integer weatherCode;
    private Double pressureMsl;
    private Integer cloudCover;
    private Double surfacePressure;
    private Double visibility;
    private Double windSpeed10m;
    private Integer isDay;
    private Double uvIndex;
    private Double windGusts10m;
    private Integer windDirection10m;
}