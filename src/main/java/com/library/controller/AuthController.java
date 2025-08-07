package com.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.LoginRequest;
import com.library.dto.ForgotPasswordRequest;
import com.library.dto.ResetPasswordRequest;
import com.library.dto.ForgotPasswordVerifyRequest;
import com.library.dto.ForgotPasswordResetRequest;
import com.library.dto.ForgotPasswordByPhoneRequest;
import com.library.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // The actual authentication will be handled by JwtAuthenticationFilter
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String token = userService.verifyUserAndGenerateToken(request);
        return ResponseEntity.ok().body(Map.of("resetToken", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> forgotPasswordReset(@RequestBody ForgotPasswordResetRequest request) {
        userService.resetPasswordWithToken(request);
        return ResponseEntity.ok().body("Đặt lại mật khẩu thành công.");
    }

    @PostMapping("/forgot-password/phone")
    public ResponseEntity<?> forgotPasswordByPhone(@RequestBody ForgotPasswordByPhoneRequest request) {
        String token = userService.verifyUserByPhoneAndGenerateToken(request);
        return ResponseEntity.ok().body(Map.of("resetToken", token));
    }
}