package com.example.sprintdb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ApiError err = new ApiError(404, "NOT_FOUND", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        ApiError err = new ApiError(400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        ApiError err = new ApiError(400, "VALIDATION_ERROR",
                ex.getBindingResult().getAllErrors().isEmpty() ? "Validation failed" : ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // UI fallback
    @ExceptionHandler(Exception.class)
    public String handleUiErrors(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
