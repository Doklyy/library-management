package com.library.service;

// Add these methods to UserService interface
public interface UserService {
    // ...existing methods...
    
    UserDTO getUserByEmail(String email);
    String createPasswordResetToken(Long userId);
    void resetPassword(String token, String newPassword);
}
