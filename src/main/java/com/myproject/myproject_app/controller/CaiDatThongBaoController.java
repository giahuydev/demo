package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.CaiDatThongBaoRequest;
import com.myproject.myproject_app.dto.response.CaiDatThongBaoResponse;
import com.myproject.myproject_app.service.CaiDatThongBaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification-settings")
@RequiredArgsConstructor
public class CaiDatThongBaoController {

    private final CaiDatThongBaoService caiDatThongBaoService;


     // 1. TẠO CÀI ĐẶT MỚI

    @PostMapping("/user/{userId}")
    public ApiResponse<CaiDatThongBaoResponse> create(@PathVariable Integer userId,
                                                      @RequestBody @Valid CaiDatThongBaoRequest request) {

        CaiDatThongBaoResponse result = caiDatThongBaoService.createCaiDat(userId, request);

        return ApiResponse.<CaiDatThongBaoResponse>builder()
                .code(1000)
                .result(result)
                .message("Tạo cài đặt thông báo thành công")
                .build();
    }


    //2. LẤY DANH SÁCH CÀI ĐẶT CỦA USER

    @GetMapping("/user/{userId}")
    public ApiResponse<List<CaiDatThongBaoResponse>> getAllByUser(@PathVariable String userId) {
        return ApiResponse.<List<CaiDatThongBaoResponse>>builder()
                .result(caiDatThongBaoService.getAllByUserId(userId))
                .build();
    }


     //3. XEM CHI TIẾT 1 CÀI ĐẶT

    @GetMapping("/{id}")
    public ApiResponse<CaiDatThongBaoResponse> getDetail(@PathVariable Integer id) {
        return ApiResponse.<CaiDatThongBaoResponse>builder()
                .result(caiDatThongBaoService.getById(id))
                .build();
    }


     //4. CẬP NHẬT CÀI ĐẶT

    @PutMapping("/{id}")
    public ApiResponse<CaiDatThongBaoResponse> update(@PathVariable Integer id,
                                                      @RequestBody @Valid CaiDatThongBaoRequest request) {
        // @Valid để đảm bảo khi update người dùng không nhập tọa độ sai hoặc xóa mất Cron
        return ApiResponse.<CaiDatThongBaoResponse>builder()
                .result(caiDatThongBaoService.updateCaiDat(id, request))
                .message("Cập nhật cài đặt thành công")
                .build();
    }


     // 5. XÓA CÀI ĐẶT
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Integer id) {
        caiDatThongBaoService.deleteCaiDat(id);
        return ApiResponse.<Void>builder()
                .message("Đã xóa cài đặt thông báo")
                .build();
    }
}