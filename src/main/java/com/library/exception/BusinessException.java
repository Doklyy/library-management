package com.library.exception;

import com.library.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final HttpStatus status;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.status = HttpStatus.valueOf(responseCode.getCode() / 100);
    }

    public BusinessException(ResponseCode responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
        this.status = HttpStatus.valueOf(responseCode.getCode() / 100);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.status = HttpStatus.valueOf(code / 100);
    }
} 