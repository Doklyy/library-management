package com.library.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.library.common.ResponseCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ResponseCode responseCode;
    private final HttpStatus status;
    private final Map<String, Object> args;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.status = HttpStatus.valueOf(responseCode.getCode());
        this.args = null;
    }

    public BusinessException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
        this.status = HttpStatus.valueOf(responseCode.getCode());
        this.args = null;
    }

    public BusinessException(ResponseCode responseCode, String message, Map<String, Object> args) {
        super(message);
        this.responseCode = responseCode;
        this.status = HttpStatus.valueOf(responseCode.getCode());
        this.args = args;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.responseCode = ResponseCode.INTERNAL_SERVER_ERROR;
        this.status = HttpStatus.valueOf(code / 100);
        this.args = null;
    }
} 