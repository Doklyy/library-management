package com.library.service;

import com.library.dto.UserDTO;
import com.library.dto.CreateUserRequest;
import com.library.dto.UpdateUserRequest;
import com.library.dto.LoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO findDetailUserById(Long id);
    Page<UserDTO> findUserFilter(LoginRequest request, Pageable pageable);
    UserDTO createUser(CreateUserRequest request);
    UserDTO updateUser(UpdateUserRequest request);
    void deleteUserById(List<Long> ids);
    UserDTO getUserByEmail(String email);
} 