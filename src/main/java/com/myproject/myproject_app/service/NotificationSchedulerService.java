package com.myproject.myproject_app.service;

import com.myproject.myproject_app.entity.Alert_Feedback.CaiDatThongBao;
import com.myproject.myproject_app.entity.Alert_Feedback.ThongBao;
import com.myproject.myproject_app.entity.Alert_Feedback.TrangThaiGuiEnum;
import com.myproject.myproject_app.entity.Search_Schedule.CauHinhNhacNho;
import com.myproject.myproject_app.entity.Search_Schedule.LichHen;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.repository.CaiDatThongBaoRepository;
import com.myproject.myproject_app.repository.CauHinhNhacNhoRepository;
import com.myproject.myproject_app.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSchedulerService {

    private final CauHinhNhacNhoRepository cauHinhRepo;
    private final CaiDatThongBaoRepository caiDatRepo;
    private final ThongBaoRepository thongBaoRepo;

    private final EmailService emailService;
    private final AiService aiService;
    private final WeatherForecastService weatherService;

    // =========================================================================
    // TASK 1: QUÉT LỊCH HẸN (Chạy mỗi 1 phút)
    // =========================================================================
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void scanForEventReminders() {
        LocalDateTime now = LocalDateTime.now();

        // Lấy danh sách nhắc nhở chưa gửi
        List<CauHinhNhacNho> candidates = cauHinhRepo.findPendingReminders();

        for (CauHinhNhacNho config : candidates) {
            try {
                LichHen event = config.getLichHen();
                LocalDateTime triggerTime = calculateTriggerTime(event.getThoiGianBatDau(), config);

                // Nếu đã đến giờ gửi (hoặc quá giờ một chút nhưng chưa gửi)
                if (!now.isBefore(triggerTime)) {
                    processEventNotification(config, event);
                }
            } catch (Exception e) {
                log.error("Lỗi xử lý nhắc nhở Config ID {}: {}", config.getIdCauHinh(), e.getMessage());
            }
        }
    }

    private void processEventNotification(CauHinhNhacNho config, LichHen event) {
        // 1. Lấy dữ liệu thời tiết (Đã dùng hàm Adapter mới trả về String)
        String weatherText = weatherService.getEventWeatherString(
                event.getViDo(),
                event.getKinhDo(),
                event.getThoiGianBatDau()
        );

        // 2. AI tạo nội dung Email
        String noiDungAi = aiService.generateEventReminder(
                event.getNguoiDung().getHoTen(),
                event.getTenSuKien(),
                weatherText,
                event.getGhiChu()
        );

        String tieuDe = "Nhắc nhở sự kiện: " + event.getTenSuKien();

        // 3. Quy trình Tạo DB -> Gửi Mail (Tracking) -> Cập nhật DB
        createAndSendNotification(
                event.getNguoiDung(),
                config,
                null, // Không phải báo cáo hàng ngày
                tieuDe,
                noiDungAi
        );

        // 4. Đánh dấu Config đã xử lý xong để không gửi lại
        config.setDaGui(true);
        config.setThoiGianGuiThucTe(LocalDateTime.now());
        cauHinhRepo.save(config);
    }

    // =========================================================================
    // TASK 2: BÁO CÁO HẰNG NGÀY (Chạy mỗi 1 phút)
    // =========================================================================
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void scanForDailyReports() {
        LocalTime now = LocalTime.now();
        String currentHm = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        // Tìm tất cả user cài đặt giờ nhận tin trùng với giờ hiện tại
        List<CaiDatThongBao> settings = caiDatRepo.findAllByCronExpression(currentHm);

        for (CaiDatThongBao setting : settings) {
            // Anti-Spam: Kiểm tra xem hôm nay đã gửi cho setting này chưa
            boolean daGuiHomNay = thongBaoRepo.existsByCaiDatAndDate(
                    setting.getIdCaiDat(),
                    LocalDate.now().atStartOfDay(),
                    LocalDate.now().atTime(LocalTime.MAX)
            );

            if (!daGuiHomNay) {
                processDailyNotification(setting);
            }
        }
    }

    private void processDailyNotification(CaiDatThongBao setting) {
        try {
            // 1. Lấy thời tiết hiện tại (Dùng hàm Adapter mới)
            String weatherText = weatherService.getCurrentWeatherString(
                    setting.getViDo(),
                    setting.getKinhDo()
            );

            // 2. AI viết báo cáo
            String noiDungAi = aiService.generateDailyWeatherReport(
                    setting.getNguoiDung().getHoTen(),
                    weatherText
            );

            // 3. Quy trình Tạo DB -> Gửi Mail -> Cập nhật DB
            createAndSendNotification(
                    setting.getNguoiDung(),
                    null, // Không phải sự kiện
                    setting,
                    "Dự báo thời tiết hôm nay",
                    noiDungAi
            );
        } catch (Exception e) {
            log.error("Lỗi gửi daily report Setting ID {}: {}", setting.getIdCaiDat(), e.getMessage());
        }
    }

    // =========================================================================
    // HÀM CHUNG: TẠO DB -> GỬI MAIL -> UPDATE DB (QUAN TRỌNG CHO TRACKING)
    // =========================================================================
    private void createAndSendNotification(
            NguoiDung user,
            CauHinhNhacNho cauHinh,
            CaiDatThongBao caiDat,
            String tieuDe,
            String noiDung
    ) {
        // BƯỚC 1: Lưu vào DB trước với trạng thái DANG_GUI để lấy ID
        if (caiDat != null && !caiDat.isTrangThai()) {
                log.info("Cài đặt ID {} bị tắt, bỏ qua gửi thông báo", caiDat.getIdCaiDat());
                return;
            }

        ThongBao tb = new ThongBao();
        tb.setNguoiDung(user);
        tb.setCauHinhNhacNho(cauHinh);
        tb.setCaiDatThongBao(caiDat);
        tb.setTieuDe(tieuDe);
        tb.setNoiDung(noiDung);
        tb.setTrangThaiGui(TrangThaiGuiEnum.DANG_GUI);
        tb.setThoiGianTao(LocalDateTime.now());
        tb.setDaMo(false); // Mặc định chưa mở mail

        // Lưu ngay lập tức
        tb = thongBaoRepo.save(tb);

        try {
            // BƯỚC 2: Gửi mail kèm ID vừa tạo (để Tracking Pixel hoạt động)
            emailService.sendEmailWithTracking(
                    user.getEmail(),
                    tieuDe,
                    noiDung,
                    tb.getIdThongBao() // <--- Truyền ID vào đây để tạo link tracking
            );

            // BƯỚC 3: Update trạng thái thành công
            tb.setTrangThaiGui(TrangThaiGuiEnum.THANH_CONG);
            tb.setThoiGianGui(LocalDateTime.now());
            thongBaoRepo.save(tb);

            log.info("Đã gửi thông báo ID {} thành công tới {}", tb.getIdThongBao(), user.getEmail());

        } catch (Exception e) {
            // Nếu gửi mail lỗi, update trạng thái thất bại
            log.error("Gửi mail thất bại cho thông báo ID {}: {}", tb.getIdThongBao(), e.getMessage());
            tb.setTrangThaiGui(TrangThaiGuiEnum.THAT_BAI);
            tb.setLoi(e.getMessage());
            thongBaoRepo.save(tb);
        }
    }

    // Helper tính thời gian kích hoạt nhắc nhở
    private LocalDateTime calculateTriggerTime(LocalDateTime start, CauHinhNhacNho config) {
        long amount = config.getThoiGianCanhBao();
        return switch (config.getDonViThoiGian()) {
            case PHUT -> start.minusMinutes(amount);
            case GIO -> start.minusHours(amount);
            case NGAY -> start.minusDays(amount);
            default -> start.minusMinutes(amount);
        };
    }
}