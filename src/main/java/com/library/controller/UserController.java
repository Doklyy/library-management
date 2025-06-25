package com.library.controller;

import com.library.common.ApiResponse;
import com.library.common.ResponseCode;
import com.library.dto.PageResponse;
import com.library.dto.UserDTO;
import com.library.service.UserService;
import com.library.dto.LoginRequest;
import com.library.dto.CreateUserRequest;
import com.library.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserDTO>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String role,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsers(keyword, isActive, role, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findDetailUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.created(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestBody List<Long> ids) {
        userService.deleteUserById(ids);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/check-role/{username}")
    public ResponseEntity<ApiResponse<String>> checkUserRole(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.error(ResponseCode.NOT_FOUND, "User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("User role: " + user.getRole()));
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkAuth(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok("Authenticated successfully. Auth header: " + authHeader);
    }
}