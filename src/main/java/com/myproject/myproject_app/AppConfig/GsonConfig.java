package com.myproject.myproject_app.AppConfig;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GsonConfig {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                // 1. Xử lý hiển thị ngày tháng đẹp
                .setDateFormat(DATE_FORMAT)

                // 2. Adapter để Serialize (Java -> JSON) cho LocalDateTime
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DATE_FORMAT))))

                // 3. Adapter để Deserialize (JSON -> Java) cho LocalDateTime
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                        LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern(DATE_FORMAT)))

                // 4. Bỏ qua lỗi nếu JSON input quá lỏng lẻo
                .setLenient()
                // 5. (Tùy chọn) In JSON đẹp (xuống dòng, thụt lề) khi log/debug
                // .setPrettyPrinting()
                .create();
    }
}