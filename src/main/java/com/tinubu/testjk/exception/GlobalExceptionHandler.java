package com.tinubu.testjk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> violation: ex.getConstraintViolations()) {
            sb
            .append("\n Bean: ")
            .append(violation.getRootBeanClass())
            .append("\n Property: ")
            .append(violation.getPropertyPath())
            .append("\n Error: ")
            .append(violation.getMessage())
            .append("\n")
            ;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Data: " + sb.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex.getMessage());
    }
}
