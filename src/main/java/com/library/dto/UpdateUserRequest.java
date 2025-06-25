package com.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDateTime dob;
    private String identityNumber;
} 