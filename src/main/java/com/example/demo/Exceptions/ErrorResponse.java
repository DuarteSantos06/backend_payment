package com.example.demo.Exceptions;

public record ErrorResponse(
        String error,
        String message,
        int status
) {}
