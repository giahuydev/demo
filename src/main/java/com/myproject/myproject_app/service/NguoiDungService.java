package com.myproject.myproject_app.service;


import com.myproject.myproject_app.dto.request.NguoiDungCreationRequest;
import com.myproject.myproject_app.dto.request.NguoiDungUpdateRequest;
import com.myproject.myproject_app.dto.response.NguoiDungCreationResponse;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.mapper.NguoiDungMapper;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NguoiDungService {

    NguoiDungRepository nguoiDungRepository;
    NguoiDungMapper nguoiDungMapper;

    public NguoiDung createRequest(NguoiDungCreationRequest request)
    {
        if(nguoiDungRepository.existsByhoTen(request.getClass().getName()))
            throw new RuntimeException("Người Dùng Đã Tồn Tại");

        NguoiDung nguoiDung = nguoiDungMapper.toNguoiDung(request);

        nguoiDung.setNgayDangKy(LocalDateTime.now());
        nguoiDung.setKichHoat(true);
        nguoiDung.setNguoiDungTinCay(true);

        return nguoiDungRepository.save(nguoiDung);
    }

    public List<NguoiDungCreationResponse> getDSNguoiDung()
    {
        return nguoiDungRepository.findAll().stream()
                .map(nguoiDungMapper::toNguoiDungResponse).toList();
    }

    public void deleteNguoiDung(String id){
        nguoiDungRepository.deleteById(id);
    }

    public NguoiDungCreationResponse getNguoiDungById(String id){
        return nguoiDungMapper.toNguoiDungResponse(nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public  NguoiDungCreationResponse updateNguoiDung(String id, NguoiDungUpdateRequest request)
    {
         NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("User not found"));
        nguoiDungMapper.updateUser(nguoiDung,request);
        return  nguoiDungMapper.toNguoiDungResponse(nguoiDungRepository.save(nguoiDung));
    }
}
