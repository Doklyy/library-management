package com.library.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private ResponseCode code;
    private String message;
    private T data;

    public ApiResponse(ResponseCode code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(ResponseCode.SUCCESS, "Success", data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<T>(ResponseCode.CREATED, "Created successfully", data);
    }

    public static <T> ApiResponse<T> error(ResponseCode code, String message) {
        return new ApiResponse<T>(code, message, null);
    }

    public static <T> ApiResponse<T> error(ResponseCode responseCode, String message, T data) {
        return new ApiResponse<T>(responseCode, message, data);
    }
} 