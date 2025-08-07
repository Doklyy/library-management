package com.library.dto;
import lombok.Data;

@Data
public class ForgotPasswordResetRequest {
    private String resetToken;
    private String newPassword;
} 