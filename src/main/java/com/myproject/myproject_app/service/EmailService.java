package com.myproject.myproject_app.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.base-url}") // Cấu hình trong application.yaml (VD: http://localhost:8080)
    private String baseUrl;

    @Async
    public void sendEmailWithTracking(String to, String subject, String htmlBody, Integer notificationId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // --- LOGIC TRACKING ---
            // Tạo URL tracking pixel: /api/tracking/open/{id}
            String trackingUrl = baseUrl + "/api/tracking/open/" + notificationId;
            String pixelTag = String.format(
                    "<br><img src='%s' width='1' height='1' style='display:none;' alt='' />",
                    trackingUrl
            );

            // Gắn pixel vào cuối email
            helper.setText(htmlBody + pixelTag, true);

            mailSender.send(message);
            log.info("Đã gửi mail (kèm tracking) đến: {}", to);

        } catch (MessagingException e) {
            log.error("Lỗi gửi mail đến {}: {}", to, e.getMessage());
            // Không throw exception ở @Async để tránh crash thread pool
        }
    }
}