package dev.gym.workloadservice.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ExceptionPayload exceptionPayload = new ExceptionPayload(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now().toString());
        return new ResponseEntity<>(exceptionPayload, HttpStatus.NOT_FOUND);
    }
}
