package com.lanarimarco.modernization.dtos;

/**
 * Standard error response for REST API
 * Used across all controllers for consistent error reporting
 *
 * Extracted from CustomerInquiryController as part of refactoring (R1)
 * to improve reusability and follow Single Responsibility Principle
 */
public class ErrorResponse {
    private String message;
    private String error;

    public ErrorResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
