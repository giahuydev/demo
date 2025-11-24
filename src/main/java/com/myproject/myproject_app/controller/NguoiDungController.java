package com.myproject.myproject_app.controller;


import com.myproject.myproject_app.dto.request.ApiResponse;
import com.myproject.myproject_app.dto.request.NguoiDungCreationRequest;
import com.myproject.myproject_app.dto.request.NguoiDungUpdateRequest;
import com.myproject.myproject_app.dto.response.NguoiDungCreationResponse;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.service.NguoiDungService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/User")
@RequiredArgsConstructor
public class NguoiDungController {
    private final NguoiDungService nguoiDungService;

    @PostMapping
    ApiResponse<NguoiDung> creationNguoiDung(@RequestBody @Valid NguoiDungCreationRequest request) {
        return ApiResponse.<NguoiDung>builder()
                .result(nguoiDungService.createRequest(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<NguoiDungCreationResponse>> getNguoiDung() {
        return ApiResponse.<List<NguoiDungCreationResponse>>builder()
                .result(nguoiDungService.getDSNguoiDung())
                .build();
    }

    @GetMapping("/{IdNguoiDung}")
    ApiResponse<NguoiDungCreationResponse> getNguoiDungById
            (@PathVariable("IdNguoiDung") String id) {
        return ApiResponse.<NguoiDungCreationResponse>builder()
                .result(nguoiDungService.getNguoiDungById(id))
                .build();
    }

    @PutMapping("/{IdNguoiDung}")
    ApiResponse<NguoiDungCreationResponse> updateNguoiDung
            (@PathVariable String id, @RequestBody NguoiDungUpdateRequest request) {
        return ApiResponse.<NguoiDungCreationResponse>builder()
                .result(nguoiDungService.updateNguoiDung(id, request))
                .build();
    }

    @DeleteMapping("/{IdNguoiDung}")
    ApiResponse<String> deleteNguoiDung
            (@PathVariable String id) {
        nguoiDungService.deleteNguoiDung(id);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }
}
