package com.library.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    // Success codes
    SUCCESS(200, "success"),
    CREATED(201, "Tạo mới thành công"),
    NO_CONTENT(204, "Không có dữ liệu"),

    // Client error codes
    BAD_REQUEST(400, "error.bad.request"),
    UNAUTHORIZED(401, "error.unauthorized"),
    FORBIDDEN(403, "error.forbidden"),
    NOT_FOUND(404, "error.not.found"),
    METHOD_NOT_ALLOWED(405, "Phương thức không được phép"),
    CONFLICT(409, "Xung đột dữ liệu"),

    // Server error codes
    INTERNAL_SERVER_ERROR(500, "error.internal.server"),
    SERVICE_UNAVAILABLE(503, "Dịch vụ không khả dụng"),

    // User-related error codes
    VALIDATION_ERROR(400, "Lỗi xác thực dữ liệu"),
    DUPLICATE_USERNAME(409, "Tên đăng nhập đã tồn tại"),
    DUPLICATE_EMAIL(409, "Email đã tồn tại"),
    INVALID_CREDENTIALS(401, "Tên đăng nhập hoặc mật khẩu không đúng"),
    ACCOUNT_LOCKED(403, "Tài khoản đã bị khóa"),
    ACCOUNT_DISABLED(403, "Tài khoản đã bị vô hiệu hóa"),
    TOKEN_EXPIRED(401, "Token đã hết hạn"),
    INVALID_TOKEN(401, "Token không hợp lệ");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 