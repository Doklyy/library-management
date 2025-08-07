package com.library.entity;

// Add these fields to User entity
@Entity
public class User {
    // ...existing fields...
    
    private String resetPasswordToken;
    
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime resetPasswordTokenExpiry;
    
    // Add getters and setters
}
