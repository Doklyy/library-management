package com.library.common;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    // Success codes
    SUCCESS(200, "Success"),
    CREATED(201, "Created successfully"),
    NO_CONTENT(204, "No content"),

    // Client error codes
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    CONFLICT(409, "Resource conflict"),

    // Server error codes
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    SERVICE_UNAVAILABLE(503, "Service unavailable"),

    // User-related error codes
    VALIDATION_ERROR(1001, "Validation error"),
    DUPLICATE_USERNAME(1002, "Username already exists"),
    DUPLICATE_EMAIL(1003, "Email already exists"),
    INVALID_CREDENTIALS(1004, "Invalid credentials"),
    ACCOUNT_LOCKED(1005, "Account is locked"),
    ACCOUNT_DISABLED(1006, "Account is disabled"),
    TOKEN_EXPIRED(1007, "Token has expired"),
    INVALID_TOKEN(1008, "Invalid token");

    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 