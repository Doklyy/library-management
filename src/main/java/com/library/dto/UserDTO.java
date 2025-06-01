package com.library.dto;

import com.library.entity.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

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
    Timestamp dob;
    String identityNumber;
    Timestamp createdAt;
    Timestamp updatedAt;
    Boolean active;
    Boolean isDeleted;
    UserRole role;
} 