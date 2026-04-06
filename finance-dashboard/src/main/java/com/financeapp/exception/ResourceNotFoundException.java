package com.financeapp.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(HttpStatus.NOT_FOUND, String.format("%s with id %d not found", resource, id));
    }
}
