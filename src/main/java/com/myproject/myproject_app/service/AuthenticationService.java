package com.myproject.myproject_app.service;

import com.myproject.myproject_app.dto.request.AuthenticationRequest;
import com.myproject.myproject_app.dto.response.AuthenticationResponse;
import com.myproject.myproject_app.entity.UserManagement.NguoiDung;
import com.myproject.myproject_app.exception.AppException;
import com.myproject.myproject_app.exception.ErrorCode;
import com.myproject.myproject_app.repository.NguoiDungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final NguoiDungRepository nguoiDungRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Tìm user theo email
        // Lưu ý: Đảm bảo NguoiDungRepository có method findByEmail
        NguoiDung user = nguoiDungRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // 2. Kiểm tra mật khẩu (So sánh trực tiếp hoặc dùng PasswordEncoder nếu có)
        // Ở đây giả định bạn đang lưu password dạng thô (như trong FE cũ)
        if (!user.getMatKhau().equals(request.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        // 3. Tạo Token (Giả lập token hoặc dùng JWT thật nếu bạn đã config)
        // String token = generateToken(user);
        String token = "mock-jwt-token-" + user.getIdNguoiDung(); // Placeholder

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .idNguoiDung(user.getIdNguoiDung())
                .hoTen(user.getHoTen())
                .email(user.getEmail())
                .build();
    }
}