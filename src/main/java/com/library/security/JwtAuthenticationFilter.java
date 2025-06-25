package com.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.common.ApiResponse;
import com.library.common.ResponseCode;
import com.library.dto.LoginRequest;
import com.library.dto.LoginResponse;
import com.library.dto.UserDTO;
import com.library.dto.ErrorResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("=== Starting authentication attempt ===");
        log.info("Request Method: {}", request.getMethod());
        
        try {
            String requestBody = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            log.info("Request body: {}", requestBody);
            
            LoginRequest loginRequest = objectMapper.readValue(requestBody, LoginRequest.class);
            log.info("Parsed login request - Username: {}, Email: {}", loginRequest.getUsername(), loginRequest.getEmail());

            String username = loginRequest.getUsername();
            if (username == null && loginRequest.getEmail() != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
                username = userDetails.getUsername();
            }

            if (username == null) {
                throw new IllegalArgumentException("Username or email is required");
            }

            log.info("Attempting authentication with username: {}", username);
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            log.error("Error parsing login request", e);
            throw new IllegalArgumentException("Invalid request body");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
            new LoginResponse("SUCCESS", "Authentication successful", token)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
            new ErrorResponse("UNAUTHORIZED", "Authentication failed: " + failed.getMessage())));
    }
}