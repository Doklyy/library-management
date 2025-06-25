package com.library.service;

import com.library.dto.LoginRequest;
import com.library.dto.LoginResponse;
import com.library.dto.RegisterRequest;
import com.library.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    User register(RegisterRequest request);
} 