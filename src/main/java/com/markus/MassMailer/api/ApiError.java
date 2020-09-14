package com.markus.MassMailer.api;

import org.springframework.http.HttpStatus;

public class ApiError extends ApiResponse{
    private String error;

    public ApiError(HttpStatus status, String message, String error) {
        this.setStatus(status);
        this.setMessage(message);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}