package com.library.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String identityCard;
    private String identityNumber;
} 