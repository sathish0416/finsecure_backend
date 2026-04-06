package com.financeapp.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {
    public BusinessException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BusinessException(String message, HttpStatus status) {
        super(status, message);
    }
}
