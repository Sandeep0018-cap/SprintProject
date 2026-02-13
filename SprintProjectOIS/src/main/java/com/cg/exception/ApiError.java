package com.cg.exception; // Defines the package for custom exception handling components

import java.time.LocalDateTime;

public class ApiError { // Standardized structure for delivering error details via REST or Web layers

    private LocalDateTime timestamp = LocalDateTime.now(); // Automatically records the moment the error occurred
    private int status; // Stores the HTTP response status code (e.g., 404, 500)
    private String error; // Stores the short-form error category or HTTP status name
    private String message; // Provides a human-readable description of what went wrong
    private String path; // Captures the specific URL endpoint where the failure happened

    public ApiError(int status, String error, String message, String path) { // Constructor for generating immediate error payloads
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }

    public int getStatus() { return status; }

    public String getError() { return error; }

    public String getMessage() { return message; }

    public String getPath() { return path; }
}
