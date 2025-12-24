package com.myproject.myproject_app.service;


import com.myproject.myproject_app.dto.request.AnhCongDongCreationRequest;
import com.myproject.myproject_app.dto.request.BaoCaoRequest;
import com.myproject.myproject_app.dto.request.KiemDuyetRequest;
import com.myproject.myproject_app.entity.Community.AnhCongDong;
import com.myproject.myproject_app.entity.Community.BaoCao;
import com.myproject.myproject_app.entity.Community.LikeAnh;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.repository.AnhCongDongRepository;
import com.myproject.myproject_app.repository.BaoCaoRepository;
import com.myproject.myproject_app.repository.LikeAnhRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnhCongDongService {

    private final AnhCongDongRepository anhRepo;
    private final BaoCaoRepository baoCaoRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final LikeAnhRepository likeRepo;
    private final ThongBaoService thongBaoService;

    // ✅ Inject AI Service để kiểm duyệt
    private final AiService aiService;

    // =================================================================
    // 1. ĐĂNG ẢNH MỚI (CÓ TÍCH HỢP AI CHECK)
    // =================================================================
    @Transactional
    public AnhCongDong dangAnhMoi(AnhCongDongCreationRequest request) {
        NguoiDung user = nguoiDungRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        AnhCongDong anh = new AnhCongDong();
        anh.setNguoiDung(user);
        anh.setUrlAnh(request.getUrlAnh());
        anh.setMoTa(request.getMoTa());

        // Map dữ liệu thời tiết
        anh.setTinhTrangThoiTiet(request.getTinhTrangThoiTiet());
        anh.setCamGiac(request.getCamGiac());
        anh.setLuongMua(request.getLuongMua());
        anh.setDiaDiem(request.getDiaDiem());

        anh.setNgayDang(LocalDateTime.now());
        anh.setLuotThich(0);
        anh.setLuotBinhLuan(0);
        anh.setSoLuotBaoCao(0);

        // --- 🤖 AI KIỂM DUYỆT BẮT ĐẦU ---
        String ketQuaText = "APPROVED"; // Mặc định OK nếu không có mô tả
        if (request.getMoTa() != null && !request.getMoTa().isEmpty()) {
            try {
        ketQuaText = aiService.kiemDuyetVanBan(request.getMoTa());
            } catch (Exception e) {
                log.warn("AI Service lỗi, để PENDING để Admin duyệt: ", e.getMessage());
                ketQuaText = "PENDING"; 
            }
        }

        String ketQuaAnh = "APPROVED";
        if (request.getUrlAnh() != null) {
            ketQuaAnh = aiService.kiemDuyetHinhAnh(request.getUrlAnh());
        }

        // Logic tổng hợp kết quả (Decision Matrix)
        String trangThaiCuoiCung = "APPROVED";
        String lyDoViPham = "";

        if ("REJECTED".equals(ketQuaText) || "REJECTED".equals(ketQuaAnh)) {
            trangThaiCuoiCung = "REJECTED";
            lyDoViPham = "Nội dung hoặc hình ảnh vi phạm tiêu chuẩn cộng đồng (AI phát hiện).";
        } else if ("PENDING".equals(ketQuaText) || "PENDING".equals(ketQuaAnh)) {
            trangThaiCuoiCung = "PENDING";
            // Không cần lý do cụ thể, chỉ là nghi ngờ cần Admin duyệt
        }

        anh.setTrangThaiKiemDuyet(trangThaiCuoiCung);
        // --- 🤖 AI KẾT THÚC ---

        AnhCongDong savedAnh = anhRepo.save(anh);

        // Nếu bị AI từ chối ngay lập tức -> Gửi thông báo cho User biết luôn
        if ("REJECTED".equals(trangThaiCuoiCung)) {
            thongBaoService.guiThongBaoHeThong(
                    user,
                    "Bài viết bị chặn tự động 🚫",
                    lyDoViPham
            );
        }

        return savedAnh;
    }

    // =================================================================
    // CÁC HÀM KHÁC (LIKE, BÁO CÁO, DUYỆT) - GIỮ NGUYÊN
    // =================================================================

    @Transactional
    public void thichAnh(Integer idAnh, String userId) {
        AnhCongDong anh = anhRepo.findById(idAnh)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        NguoiDung user = nguoiDungRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Optional<LikeAnh> existingLike = likeRepo.findByAnhCongDongAndNguoiDung(anh, user);

        if (existingLike.isPresent()) {
            likeRepo.delete(existingLike.get());
            anh.setLuotThich(Math.max(0, anh.getLuotThich() - 1));
        } else {
            LikeAnh newLike = new LikeAnh();
            newLike.setAnhCongDong(anh);
            newLike.setNguoiDung(user);
            likeRepo.save(newLike);
            anh.setLuotThich(anh.getLuotThich() + 1);
        }
        anhRepo.save(anh);
    }

    @Transactional
    public void baoCaoAnh(BaoCaoRequest request) {
        AnhCongDong anh = anhRepo.findById(request.getIdAnh())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        NguoiDung nguoiBaoCao = nguoiDungRepo.findById(request.getIdNguoiBaoCao())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (baoCaoRepo.existsByAnhCongDongAndNguoiBaoCao(anh, nguoiBaoCao)) {
            throw new AppException(ErrorCode.ALREADY_REPORTED);
        }

        BaoCao baoCao = new BaoCao();
        baoCao.setNguoiBaoCao(nguoiBaoCao);
        baoCao.setAnhCongDong(anh);
        baoCao.setLyDo(request.getLyDo());
        baoCaoRepo.save(baoCao);

        int soLuotMoi = anh.getSoLuotBaoCao() + 1;
        anh.setSoLuotBaoCao(soLuotMoi);

        if (soLuotMoi >= 20 && "APPROVED".equals(anh.getTrangThaiKiemDuyet())) {
            anh.setTrangThaiKiemDuyet("PENDING");
           
            thongBaoService.guiThongBaoHeThong(
                anh.getNguoiDung(),
                "Thông báo vi phạm quy tắc cộng đồng",
                "Bài viết của bạn đã bị tạm ẩn do nhận nhiều báo cáo từ người dùng khác." 
            );
        }
        anhRepo.save(anh);
    }

    @Transactional
    public void adminDuyetBai(KiemDuyetRequest request) {
        AnhCongDong anh = anhRepo.findById(request.getIdAnh())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        String trangThaiMoi = request.getTrangThai();
        anh.setTrangThaiKiemDuyet(trangThaiMoi);

        if ("REJECTED".equals(trangThaiMoi)) {
            String noiDung = "Bài viết bị gỡ.";
            if (request.getLyDoTuChoi() != null) noiDung += "\nLý do: " + request.getLyDoTuChoi();
            thongBaoService.guiThongBaoHeThong(anh.getNguoiDung(), "Bài viết bị gỡ bỏ ⚠️", noiDung);
        } else if ("APPROVED".equals(trangThaiMoi)) {
            thongBaoService.guiThongBaoHeThong(anh.getNguoiDung(), "Bài viết được duyệt ✅", "Đã hiển thị công khai.");
        }
        anhRepo.save(anh);
    }
}