package com.myproject.myproject_app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myproject_app.dto.internal.KetQuaPhanTichChiTiet;
import com.myproject.myproject_app.dto.request.HanhTrinhRequest;
import com.myproject.myproject_app.dto.response.HanhTrinhResponse;
import com.myproject.myproject_app.dto.response.WeatherFormattedResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.Search_Schedule.HanhTrinh;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.entity.weather.view.Minutely15Record;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.HanhTrinhMapper;
import com.myproject.myproject_app.repository.HanhTrinhRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HanhTrinhService {

    private final HanhTrinhRepository hanhTrinhRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final NguonDuLieuRepository nguonRepo;

    private final RoutingService routingService;
    private final WeatherForecastService weatherService;

    private final ChatModel chatModel;
    private final HanhTrinhMapper hanhTrinhMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public HanhTrinhResponse phanTichHanhTrinh(HanhTrinhRequest req) {
        // 1. Validate User & Nguồn
        NguoiDung user = nguoiDungRepo.findById(req.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        NguonDuLieu nguon = nguonRepo.findByTenChucNang(req.getTenNguon())
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));

        // 2. Gọi Routing (TrackAsia) -> Lấy Polyline & Waypoints
        var routeResult = routingService.getRouteAndSamplePoints(
                req.getViDoDi(), req.getKinhDoDi(),
                req.getViDoDen(), req.getKinhDoDen(),
                req.getPhuongTien()
        );

        // 3. Tổng hợp dữ liệu thời tiết từ Waypoints
        StringBuilder weatherSummary = new StringBuilder();
        weatherSummary.append(String.format("Tổng thời gian dự kiến: %d phút. Chi tiết thời tiết:\n", routeResult.durationMinutes()));

        for (RoutingService.TimePoint p : routeResult.waypoints()) {
            LocalDateTime checkTime = req.getThoiGianKhoiHanh().plusMinutes(p.minuteOffset());

            // Gọi Weather Service (Tối ưu Minutely15)
            WeatherFormattedResponse weather = weatherService.getJourneyWeather(
                    req.getTenNguon(), p.lat(), p.lon(),
                    req.getThoiGianKhoiHanh(), routeResult.durationMinutes()
            );

            // Lọc bản ghi 15 phút khớp nhất
            Minutely15Record record = findClosestRecord(weather.getMinutely15(), checkTime);

            if (record != null) {
                weatherSummary.append(String.format("- Phút thứ %d (Tại %.4f, %.4f, lúc %s): %s, Mưa %.1fmm, Gió %.1fkm/h%s\n",
                        p.minuteOffset(), p.lat(), p.lon(), checkTime.toLocalTime(),
                        decodeWeatherCode(record.getWeatherCode()),
                        record.getPrecipitation(), record.getWindSpeed10m(),
                        (record.getPrecipitation() > 0.5) ? " [CÓ MƯA]" : ""));
            }
        }

        // 4. Gọi AI Phân tích
        KetQuaPhanTichChiTiet aiResult = goiAiPhanTich(req, weatherSummary.toString());

        // 5. Lưu Entity
        HanhTrinh ht = new HanhTrinh();
        ht.setNguoiDung(user);
        ht.setNguon(nguon);
        ht.setTenHanhTrinh(req.getTenHanhTrinh());
        ht.setDiemDi(req.getDiemDi()); ht.setViDoDi(req.getViDoDi()); ht.setKinhDoDi(req.getKinhDoDi());
        ht.setDiemDen(req.getDiemDen()); ht.setViDoDen(req.getViDoDen()); ht.setKinhDoDen(req.getKinhDoDen());
        ht.setThoiGianKhoiHanh(req.getThoiGianKhoiHanh());
        ht.setPhuongTien(req.getPhuongTien());
        ht.setRouteGeometry(routeResult.overviewPolyline()); // Lưu chuỗi mã hóa đường đi
        ht.setLinkChiaSe(UUID.randomUUID().toString());
        ht.setNgayTao(LocalDateTime.now());

        try {
            ht.setDanhGiaNguyHiem(objectMapper.writeValueAsString(aiResult)); // Lưu JSON kết quả AI
            ht.setDeXuat(aiResult.deXuatChung());
        } catch (Exception e) {
            log.error("Lỗi parse JSON AI", e);
        }

        return hanhTrinhMapper.toResponse(hanhTrinhRepo.save(ht));
    }

    // --- AI Logic ---
    private KetQuaPhanTichChiTiet goiAiPhanTich(HanhTrinhRequest req, String data) {
        BeanOutputConverter<KetQuaPhanTichChiTiet> converter = new BeanOutputConverter<>(KetQuaPhanTichChiTiet.class);
        String promptText = """
            Bạn là trợ lý giao thông. Phân tích rủi ro hành trình sau:
            - Phương tiện: {xe}
            - Lộ trình thời tiết chi tiết:
            {weather}
            
            Yêu cầu:
            1. Đánh giá mức độ rủi ro (CAO/TRUNG BINH/THAP) cho từng điểm mốc thời gian.
            2. Trả về danh sách các điểm cảnh báo (chỉ những điểm có mưa/gió/nguy hiểm).
            3. Đưa ra lời khuyên chung.
            
            Trả về đúng định dạng JSON:
            {format}
            """;

        Prompt prompt = new PromptTemplate(promptText).create(Map.of(
                "xe", req.getPhuongTien(),
                "weather", data,
                "format", converter.getFormat()
        ));

        return converter.convert(chatModel.call(prompt)
                                            .getResult()
                                            .getOutput().
                                            getContent());
    }

    // Helper: Tìm record gần nhất trong list (chênh lệch không quá 15 phút)
    private Minutely15Record findClosestRecord(List<Minutely15Record> list, LocalDateTime time) {
        if (list == null) return null;
        Minutely15Record best = null;
        long minDiff = Long.MAX_VALUE;

        for (Minutely15Record r : list) {
            // Parse chuỗi thời gian ISO từ API
            LocalDateTime recordTime = LocalDateTime.parse(r.getTime(), DateTimeFormatter.ISO_DATE_TIME);
            long diff = Math.abs(java.time.Duration.between(recordTime, time).getSeconds());

            if (diff < minDiff) {
                minDiff = diff;
                best = r;
            }
        }
        return minDiff <= 900 ? best : null; // 900s = 15p
    }

    private String decodeWeatherCode(Integer code) {
        if (code == null) return "N/A";
        if (code == 0) return "Trời quang";
        if (code < 50) return "Có mây";
        if (code < 80) return "Mưa rào";
        return "Giông bão";
    }
}