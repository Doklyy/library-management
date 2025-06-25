package com.library.service.impl;

import com.library.dto.PageResponse;
import com.library.dto.UserDTO;
import com.library.dto.CreateUserRequest;
import com.library.dto.UpdateUserRequest;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest;
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

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ResponseCode.CONFLICT,
                    String.format("Tên đăng nhập '%s' đã tồn tại", request.getUsername()));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.CONFLICT,
                    String.format("Email '%s' đã tồn tại", request.getEmail()));
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByRoleName("USER")
            .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Role không tồn tại: USER"));
        user.setRole(role);
        user.setActive(true);

        return userRepository.save(user);
    }

    @Override
    public UserDTO findDetailUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND,
                        String.format("Không tìm thấy người dùng với ID: %d", id)));
        return convertToDTO(user);
    }

    @Override
    public Page<UserDTO> findUserFilter(LoginRequest request, Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(ResponseCode.CONFLICT,
                    String.format("Tên đăng nhập '%s' đã tồn tại", request.getUsername()));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.CONFLICT,
                    String.format("Email '%s' đã tồn tại", request.getEmail()));
        }

        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String roleName = request.getRole() != null ? request.getRole().toUpperCase() : "USER";
        Role role = roleRepository.findByRoleName(roleName)
            .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "Role không tồn tại: " + roleName));
        user.setRole(role);
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND,
                        String.format("Không tìm thấy người dùng với ID: %d", id)));

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ResponseCode.CONFLICT,
                    String.format("Email '%s' đã tồn tại", request.getEmail()));
        }

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        return convertToDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUserById(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND,
                        String.format("Không tìm thấy người dùng với tên đăng nhập: %s", username)));
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND,
                        String.format("Không tìm thấy người dùng với email: %s", email)));
        return convertToDTO(user);
    }

    @Override
    public PageResponse<UserDTO> searchUsers(String keyword, Boolean isActive, String role, Pageable pageable) {
        UserRole userRole = role != null ? UserRole.valueOf(role.toUpperCase()) : null;
        Page<User> userPage = userRepository.searchUsers(keyword, isActive, userRole, pageable);
        List<UserDTO> users = userPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                users,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .dob(user.getDob())
                .identityNumber(user.getIdentityNumber())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isActive(user.isActive())
                .isDeleted(user.isDeleted())
                .role(user.getRole() != null ? user.getRole().getRoleName() : null)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}