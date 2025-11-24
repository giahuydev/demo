package com.myproject.myproject_app.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // --- 9xxx: Lỗi hệ thống chung ---
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Key không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- 1xxx: Lỗi Người Dùng (User) ---
    USER_EXISTED(1002, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Chưa đăng nhập hoặc token không hợp lệ", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền thực hiện thao tác này", HttpStatus.FORBIDDEN),

    // --- 2xxx: Lỗi Nguồn Dữ Liệu (Source) ---
    SOURCE_NOT_FOUND(2001, "Nguồn dữ liệu không tồn tại hoặc chưa được hỗ trợ", HttpStatus.NOT_FOUND),
    SOURCE_INVALID(2002, "Thông tin nguồn dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    SOURCE_EXISTED(2003, "Nguồn dữ liệu đã tồn tại", HttpStatus.BAD_REQUEST),
    SOURCE_INACTIVE(2004, "Nguồn dữ liệu đang bị vô hiệu hóa", HttpStatus.BAD_REQUEST),

    // --- 3xxx: Lỗi Địa Điểm Yêu Thích (Favorite) ---
    FAVORITE_NOT_FOUND(3001, "Địa điểm yêu thích không tìm thấy", HttpStatus.NOT_FOUND),
    FAVORITE_LIMIT_EXCEEDED(3002, "Danh sách yêu thích đã đầy", HttpStatus.BAD_REQUEST),
    FAVORITE_ALREADY_EXISTS(3003, "Địa điểm này đã có trong danh sách yêu thích", HttpStatus.CONFLICT),

    // --- 4xxx: Lỗi Dữ Liệu Đầu Vào (Request) ---
    INVALID_REQUEST(4000, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    LOCATION_REQUIRED(4001, "Vui lòng cung cấp tên địa điểm (location)", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(4002, "Không tìm thấy địa chỉ từ tọa độ hoặc tên địa điểm", HttpStatus.NOT_FOUND),

    // --- 5xxx: Lỗi Hệ Thống Bên Ngoài (External API) ---
    EXTERNAL_API_ERROR(5001, "Lỗi khi gọi API bên ngoài (OpenMeteo/Nominatim...)", HttpStatus.BAD_GATEWAY)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
