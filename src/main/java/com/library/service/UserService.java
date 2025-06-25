package com.library.service;

import com.library.dto.PageResponse;
import com.library.dto.UserDTO;
import com.library.dto.CreateUserRequest;
import com.library.dto.UpdateUserRequest;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
import com.library.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO findDetailUserById(Long id);
    Page<UserDTO> findUserFilter(LoginRequest request, Pageable pageable);
    UserDTO createUser(CreateUserRequest request);
    UserDTO updateUser(Long id, UpdateUserRequest request);
    void deleteUserById(List<Long> ids);
    UserDTO getUserByEmail(String email);
    UserDTO getUserByUsername(String username);
    PageResponse<UserDTO> searchUsers(String keyword, Boolean isActive, String role, Pageable pageable);
    User register(RegisterRequest request);
}