package com.library.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    Long id;
    String username;
    String password;
    String fullName;
    String email;
    String phone;
    String address;
    LocalDateTime dob;
    String identityNumber;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    Boolean isDeleted;
    String role;
} 