package com.myproject.myproject_app.controller;

import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.DiaDiemYeuThichRequest;
import com.myproject.myproject_app.dto.request.DiaDiemYeuThichUpdateRequest;
import com.myproject.myproject_app.dto.response.DiaDiemYeuThichResponse;
import com.myproject.myproject_app.service.DiaDiemYeuThichService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class DiaDiemYeuThichController {

    private final DiaDiemYeuThichService favoriteService;

    // 1. Thêm mới
    @PostMapping
    public ApiResponse<DiaDiemYeuThichResponse> createFavorite(@RequestBody DiaDiemYeuThichRequest request) {
        return ApiResponse.<DiaDiemYeuThichResponse>builder()
                .result(favoriteService.createFavorite(request))
                .message("Đã thêm vào danh sách yêu thích")
                .build();
    }

    // 2. Lấy danh sách (FE truyền userId qua query param: /favorites?userId=1)
    @GetMapping
    public ApiResponse<List<DiaDiemYeuThichResponse>> getAllFavorites(@RequestParam String userId) {
        return ApiResponse.<List<DiaDiemYeuThichResponse>>builder()
                .result(favoriteService.getAllFavoritesByUserId(userId))
                .message("Lấy danh sách thành công")
                .build();
    }

    // 3. Lấy chi tiết 1 địa điểm
    @GetMapping("/{id}")
    public ApiResponse<DiaDiemYeuThichResponse> getFavoriteById(@PathVariable Integer id) {
        return ApiResponse.<DiaDiemYeuThichResponse>builder()
                .result(favoriteService.getFavoriteById(id))
                .build();
    }

    // 4. Cập nhật
    @PutMapping("/{id}")
    public ApiResponse<DiaDiemYeuThichResponse> updateFavorite(@PathVariable Integer id, @RequestBody DiaDiemYeuThichUpdateRequest request) {
        return ApiResponse.<DiaDiemYeuThichResponse>builder()
                .result(favoriteService.updateFavorite(id, request))
                .message("Cập nhật thành công")
                .build();
    }

    // 5. Xóa
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteFavorite(@PathVariable Integer id) {
        favoriteService.deleteFavorite(id);
        return ApiResponse.<String>builder()
                .message("Đã xóa địa điểm khỏi danh sách")
                .build();
    }
}