package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.status = 400; // Default status code
    }
}
