package com.library.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Tên đăng nhập là bắt buộc")
    private String username;
    @NotBlank(message = "Số điện thoại là bắt buộc")
    private String phone;
    private String fullName;
    @NotBlank(message = "Email là bắt buộc")
    private String email;
}