package dev.gym.workloadservice.service.exception;

import org.springframework.http.HttpStatus;

public record ExceptionPayload (
        String message,
        HttpStatus httpStatus,
        String timestamp ) { }
