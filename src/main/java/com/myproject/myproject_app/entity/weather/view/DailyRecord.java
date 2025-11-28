package com.myproject.myproject_app.entity.weather.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyRecord {
    private String time;
    private Integer weatherCode;
    private Double temperature2mMax;
    private Double temperature2mMin;
    private Double apparentTemperatureMax;
    private Double apparentTemperatureMin;
    private String sunrise;
    private String sunset;
    private Double sunshineDuration;
    private Double uvIndexMax;
    private Double precipitationSum;
    private Double precipitationHours;
    private Integer precipitationProbabilityMax;
    private Double windSpeed10mMax;
    private Double windGusts10mMax;
    private Integer windDirection10mDominant;
}