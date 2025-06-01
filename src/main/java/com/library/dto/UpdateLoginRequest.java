package com.library.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateLoginRequest {
    private Long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private Timestamp dob;
    private String identityNumber;
}
