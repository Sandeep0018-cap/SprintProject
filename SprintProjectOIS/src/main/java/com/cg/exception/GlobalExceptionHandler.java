package com.cg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice // Intercepts exceptions across all controllers to provide uniform responses
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Triggers when a requested DB entity does not exist
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ApiError err = new ApiError(404, "NOT_FOUND", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class) // Triggers for semantic errors or invalid business states
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
        ApiError err = new ApiError(400, "BAD_REQUEST", ex.getMessage(), req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Specifically handles failures from @Valid/@Validated annotations
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        // Extracts the first available validation message or provides a generic fallback
        String msg = ex.getBindingResult().getAllErrors().isEmpty() ? "Validation failed" 
                     : ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        
        ApiError err = new ApiError(400, "VALIDATION_ERROR", msg, req.getRequestURI());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class) // Catch-all handler for unexpected system failures
    public String handleUiErrors(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage()); // Passes the error details to the thymeleaf error page
        return "error"; // Resolves to the error.html view template
    }
}
