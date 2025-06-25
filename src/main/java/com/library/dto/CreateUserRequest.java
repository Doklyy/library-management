package com.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;

    @NotEmpty(message = "Phone cannot be empty")
    private String phone;

    private String address;
    private String identityCard;
    private String avatar;
    private String role;
    private LocalDateTime dob;
    private String identityNumber;
}