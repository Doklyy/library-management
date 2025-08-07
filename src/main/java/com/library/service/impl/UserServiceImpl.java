package com.library.service.impl;

import com.library.dto.PageResponse;
import com.library.dto.UserDTO;
import com.library.dto.CreateUserRequest;
import com.library.dto.UpdateUserRequest;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
import com.library.dto.ForgotPasswordRequest;
import com.library.dto.ResetPasswordRequest;
import com.library.dto.ForgotPasswordVerifyRequest;
import com.library.dto.ForgotPasswordResetRequest;
import com.library.dto.ForgotPasswordByPhoneRequest;
import com.library.entity.User;
import com.library.entity.UserRole;
import com.library.entity.Role;
import com.library.repository.UserRepository;
import com.library.repository.RoleRepository;
import com.library.service.UserService;
import com.library.exception.BusinessException;
import com.library.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService