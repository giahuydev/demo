package com.myproject.myproject_app.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống chưa được định nghĩa"),
    INVALID_KEY(1001, "Key xác thực không hợp lệ"),

    // --- User Errors ---
    USER_EXISTED(1002, "Người dùng đã tồn tại"),
    USERNAME_INVALID(1003, "Tên đăng nhập phải có ít nhất 3 ký tự"),
    INVALID_PASSWORD(1004, "Mật khẩu phải có ít nhất 8 ký tự"),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại"),

    // --- WEATHER SERVICE ERRORS ---
    LOCATION_REQUIRED(2001, "Vui lòng nhập tên địa điểm"),
    ADDRESS_NOT_FOUND(2002, "Không tìm thấy địa chỉ này trên bản đồ"),

    // Error Code cho Logic Check Nguồn (Mới)
    SOURCE_NOT_FOUND(2003, "Nguồn dữ liệu không tồn tại trong hệ thống"),
    SOURCE_INACTIVE(2004, "Nguồn dữ liệu này đang tạm khóa hoặc ngừng hoạt động"),

    EXTERNAL_API_ERROR(2005, "Lỗi kết nối đến nhà cung cấp dữ liệu thời tiết"),
    SOURCE_EXISTED(2006, "Tên nguồn dữ liệu đã tồn tại, vui lòng chọn tên khác"),
    ;

    private int code;
    private String message;
}
