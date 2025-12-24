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
    LICH_HEN_NOT_FOUND(3001, "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),
    FAVORITE_LIMIT_EXCEEDED(3002, "Danh sách yêu thích đã đầy", HttpStatus.BAD_REQUEST),
    FAVORITE_ALREADY_EXISTS(3003, "Địa điểm này đã có trong danh sách yêu thích", HttpStatus.CONFLICT),
    CRON_INVALID(3003, "Định dạng thời gian (Cron) không hợp lệ", HttpStatus.BAD_REQUEST),
    REMINDER_NOT_FOUND(3004, "Không tìm thấy cấu hình nhắc nhở", HttpStatus.NOT_FOUND),

    // --- 4xxx: Lỗi Dữ Liệu Đầu Vào (Request) ---
    INVALID_REQUEST(4000, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    LOCATION_REQUIRED(4001, "Vui lòng cung cấp tên địa điểm (location)", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(4002, "Không tìm thấy địa chỉ từ tọa độ hoặc tên địa điểm", HttpStatus.NOT_FOUND),
    INVALID_FORECAST_DAYS(4003, "Số ngày dự báo phải từ 1 đến 16 ngày", HttpStatus.BAD_REQUEST),
    INVALID_COORDINATES(4004, "Tọa độ không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- 5xxx: Lỗi Hệ Thống Bên Ngoài (External API) ---
    EXTERNAL_API_ERROR(5001, "Lỗi khi gọi API bên ngoài (OpenMeteo/Nominatim...)", HttpStatus.BAD_GATEWAY),

    ROUTE_NOT_FOUND(6001, "Không tìm thấy đường đi giữa hai địa điểm này (TrackAsia không trả về kết quả)", HttpStatus.BAD_REQUEST),
    ROUTING_SERVICE_ERROR(6002, "Lỗi kết nối đến dịch vụ bản đồ", HttpStatus.BAD_GATEWAY),
    AI_ANALYSIS_FAILED(6003, "Không thể phân tích rủi ro lúc này (Lỗi AI)", HttpStatus.INTERNAL_SERVER_ERROR),
    TRIP_NOT_FOUND(6004, "Hành trình không tồn tại hoặc link chia sẻ không đúng", HttpStatus.NOT_FOUND),

    POST_NOT_FOUND(1005, "Bài viết không tồn tại", HttpStatus.NOT_FOUND),
    ALREADY_REPORTED(1006, "Bạn đã báo cáo bài viết này rồi", HttpStatus.CONFLICT),

    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
