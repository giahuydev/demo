package com.myproject.myproject_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Kích hoạt cấu hình CORS (được định nghĩa ở bean dưới)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Tắt CSRF (Do dùng API stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Phân quyền truy cập (Authorize Requests)
                .authorizeHttpRequests(request -> request
                        // Cho phép truy cập công khai vào endpoint weather (dựa trên URL bạn gửi)
                        .requestMatchers("/weather/**", "/identity/weather/**").permitAll()
                        // Cho phép các file static nếu cần (ảnh, css, js)
                        .requestMatchers("/public/**").permitAll()
                        // Tất cả các request còn lại bắt buộc phải có Token/Đăng nhập
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // Bean cấu hình CORS chi tiết
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // CHỈ ĐỊNH RÕ: Cho phép Frontend ở port 5173
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Cho phép các method HTTP
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cho phép các headers (Authorization, Content-Type...)
        configuration.setAllowedHeaders(List.of("*"));

        // Cho phép gửi credentials (cookies, authorization headers) nếu cần
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}