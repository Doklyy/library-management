package com.library.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ForgotPasswordVerifyRequest {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Verification code is required")
    private String code;
} 