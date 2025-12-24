package com.myproject.myproject_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestClient restClient;

    public AiService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://generativelanguage.googleapis.com/v1beta/models").build();
    }

    // =========================================================================
    // TASK 1: NHẮC NHỞ SỰ KIỆN (Dùng Prompt bạn vừa gửi)
    // =========================================================================
    public String generateEventReminder(String userName, String eventName, String weatherText, String note) {
        String prompt = String.format(
                "Bạn là trợ lý thời tiết cá nhân. Dưới đây là dữ liệu thời tiết chi tiết cho sự kiện '%s' của %s:\n" +
                        "---------------------\n" +
                        "%s\n" + // <-- Đây là chỗ weatherText (bảng dữ liệu Full Specs) được chèn vào
                        "---------------------\n" +
                        "LƯU Ý QUAN TRỌNG VỀ DỮ LIỆU:\n" +
                        "- Dữ liệu chứa 'WMO Code' (Chuẩn Tổ chức Khí tượng Thế giới). Ví dụ: 0 là Trời quang, 61-65 là Mưa, 95-99 là Giông bão.\n" +
                        "- Các chỉ số khác: UV (Tia cực tím), Precip (Lượng mưa), Wind Gust (Gió giật), Lightning (Sấm sét).\n" +
                        "- Hãy tự diễn giải các chỉ số này sang tiếng Việt tự nhiên để cảnh báo người dùng.\n" +
                        "\n" +
                        "Yêu cầu nội dung email:\n" +
                        "1. Cảnh báo ngay nếu WMO Code hoặc chỉ số Lightning/Wind Gust chỉ ra thời tiết xấu.\n" +
                        "2. Nhắc nhở người dùng mang vật dụng cần thiết (ô, áo mưa, kính râm...) dựa trên data.\n" +
                        "3. Viết ngắn gọn, thân thiện (dưới 80 từ).\n" +
                        "4. Ghi chú của user về sự kiện: %s",
                eventName, userName, weatherText, (note == null ? "Không có" : note)
        );
        return callGeminiApi(prompt);
    }

    // =========================================================================
    // TASK 2: BÁO CÁO HẰNG NGÀY (Prompt điều chỉnh cho phù hợp ngữ cảnh ngày)
    // =========================================================================
    public String generateDailyWeatherReport(String userName, String weatherText) {
        String prompt = String.format(
                "Chào %s. Bạn là trợ lý thời tiết. Dưới đây là dự báo chi tiết cho cả ngày hôm nay:\n" +
                        "---------------------\n" +
                        "%s\n" +
                        "---------------------\n" +
                        "LƯU Ý QUAN TRỌNG:\n" +
                        "- Dữ liệu chứa 'WMO Code' (Chuẩn WMO). Hãy chú ý các mã báo hiệu mưa hoặc giông bão.\n" +
                        "- Chú ý chỉ số UV (nếu cao cần nhắc kem chống nắng) và nhiệt độ RealFeel (Cảm giác thực).\n" +
                        "\n" +
                        "Yêu cầu nội dung email:\n" +
                        "1. Tóm tắt diễn biến thời tiết trong ngày (Sáng/Chiều/Tối thế nào?).\n" +
                        "2. Đưa ra 1 lời khuyên hữu ích về trang phục (mặc ấm/mát) hoặc sức khỏe.\n" +
                        "3. Giọng văn vui vẻ, tràn đầy năng lượng cho ngày mới. Giới hạn dưới 100 từ.",
                userName, weatherText
        );
        return callGeminiApi(prompt);
    }
    // =========================================================================
    // TASK 3: KIỂM DUYỆT VĂN BẢN (Mới - Dùng Gemini Thật)
    // =========================================================================
    public String kiemDuyetVanBan(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "APPROVED";
        }

        String prompt = String.format(
                "Bạn là hệ thống kiểm duyệt nội dung (Content Moderator) nghiêm ngặt.\n" +
                        "Nhiệm vụ: Kiểm tra đoạn văn bản sau xem có vi phạm tiêu chuẩn cộng đồng không:\n" +
                        "- Vi phạm: Khiêu dâm, bạo lực, ngôn từ thù ghét, chửi thề, lừa đảo, chính trị nhạy cảm.\n" +
                        "\n" +
                        "Văn bản cần kiểm tra: \"%s\"\n" +
                        "\n" +
                        "Yêu cầu trả về:\n" +
                        "- Chỉ trả về duy nhất 1 từ kết quả: \"APPROVED\" (An toàn) hoặc \"REJECTED\" (Vi phạm) hoặc \"PENDING\" (Nghi ngờ).\n" +
                        "- Không giải thích gì thêm.",
                text
        );

        String result = callGeminiApi(prompt).trim().toUpperCase();

        // Xử lý kết quả trả về từ AI để đảm bảo đúng format hệ thống cần
        if (result.contains("REJECTED")) return "REJECTED";
        if (result.contains("PENDING")) return "PENDING";
        if (result.contains("APPROVED")) return "APPROVED";

        // Fallback nếu AI trả lời dài dòng
        return "PENDING";
    }

    // =========================================================================
    // TASK 4: KIỂM DUYỆT HÌNH ẢNH (Mới - Mock logic để chạy được ngay)
    // =========================================================================
    public String kiemDuyetHinhAnh(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "APPROVED";
        }

        // Hiện tại: Giả lập kiểm duyệt (Để gửi ảnh thật cần convert Base64 phức tạp hơn)
        // Cách test: Nếu tên ảnh chứa chữ "vi_pham" -> Chặn. Còn lại cho qua.
        if (imageUrl.toLowerCase().contains("vi_pham") || imageUrl.toLowerCase().contains("banned")) {
            return "REJECTED";
        }

        return "APPROVED";
    }

    // --- Hàm gọi API chung (Giữ nguyên) ---
    private String callGeminiApi(String prompt) {
        try {
            GeminiRequest requestBody = new GeminiRequest(List.of(new Content(List.of(new Part(prompt)))));

            GeminiResponse response = restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gemini-1.5-flash:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response != null && !response.candidates().isEmpty()) {
                return response.candidates().get(0).content().parts().get(0).text();
            }
        } catch (Exception e) {
            log.error("Lỗi gọi Gemini AI: {}", e.getMessage());
            return "Dự báo thời tiết: Vui lòng xem chi tiết trong ứng dụng để có thông tin chính xác nhất.";
        }
        return "Không có nội dung.";
    }

    // --- DTO nội bộ cho Gemini ---
    public record GeminiRequest(List<Content> contents) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
    public record GeminiResponse(List<Candidate> candidates) {}
    public record Candidate(Content content) {}
}