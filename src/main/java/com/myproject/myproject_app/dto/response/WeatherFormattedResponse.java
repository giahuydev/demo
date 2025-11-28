package com.myproject.myproject_app.dto.response;

import com.myproject.myproject_app.entity.weather.CurrentData;
import com.myproject.myproject_app.entity.weather.view.DailyRecord;
import com.myproject.myproject_app.entity.weather.view.HourlyRecord;
import com.myproject.myproject_app.entity.weather.view.Minutely15Record;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeatherFormattedResponse {
    private double latitude;
    private double longitude;
    private String timezone;
    private double elevation;

    private CurrentData current;
    private List<HourlyRecord> hourly;
    private List<DailyRecord> daily;
    private List<Minutely15Record> minutely15;
}