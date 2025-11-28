package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.LichSuTimKiemRequest;
import com.myproject.myproject_app.dto.response.LichSuTimKiemResponse;
import com.myproject.myproject_app.entity.MultiSourceData.NguonDuLieu;
import com.myproject.myproject_app.entity.Search_Schedule.LichSuTimKiem;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.mapper.LichSuTimKiemMapper;
import com.myproject.myproject_app.repository.LichSuTimKiemRepository;
import com.myproject.myproject_app.repository.NguonDuLieuRepository;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LichSuTimKiemService {

    private final LichSuTimKiemRepository historyRepo;
    private final NguoiDungRepository userRepo;
    private final NguonDuLieuRepository nguonRepo;
    private final LichSuTimKiemMapper historyMapper;

    @Transactional
    public LichSuTimKiemResponse addSearchHistory(LichSuTimKiemRequest request) {

        NguoiDung user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        NguonDuLieu nguon = nguonRepo.findBytenNguon(request.getTenNguon())
                .orElseThrow(() -> new AppException(ErrorCode.SOURCE_NOT_FOUND));


        LichSuTimKiem history = historyMapper.toLichSuTimKiem(request);

        history.setNguoiDung(user);
        history.setNguon(nguon);
        history.setThoiGianTim(LocalDateTime.now());

        return historyMapper.toResponse(historyRepo.save(history));
    }

    public LichSuTimKiemResponse getHistoryById(Integer id) {
        LichSuTimKiem history = historyRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQUEST)); // Hoặc dùng code lỗi riêng
        return historyMapper.toResponse(history);
    }

    public List<LichSuTimKiemResponse> getHistoryByUser(String userId) {
        return historyRepo.findAllByNguoiDung_IdOrderByThoiGianTimKiemDesc(Integer.parseInt(userId))
                .stream()
                .map(historyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteHistory(Integer historyId) {
        if (!historyRepo.existsById(historyId)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        historyRepo.deleteById(historyId);
    }

    @Transactional
    public void deleteAllHistory(String userId) {
        historyRepo.deleteAllByNguoiDung_Id(Integer.parseInt(userId));
    }
}