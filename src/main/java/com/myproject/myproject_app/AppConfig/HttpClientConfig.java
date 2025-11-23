package com.myproject.myproject_app.AppConfig;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(3000)) // Timeout kết nối
                .setReadTimeout(Duration.ofSeconds(5000))    // Timeout đọc dữ liệu
                .build();
    }
}