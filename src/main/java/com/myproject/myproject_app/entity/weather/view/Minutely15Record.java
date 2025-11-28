package com.myproject.myproject_app.entity.weather.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Minutely15Record {
    private String time;
    private Double temperature2m;
    private Integer relativeHumidity2m;
    private Double apparentTemperature;
    private Double precipitation;
    private Double rain;
    private Double snowfall;
    private Double sunshineDuration;
    private Integer weatherCode;
    private Double windSpeed10m;
    private Integer windDirection10m;
    private Double windGusts10m;
    private Double visibility;
    private Integer isDay;
    private Double lightningPotential;
}