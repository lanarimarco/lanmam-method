package com.smeup.erp.exceptions;

/**
 * Exception thrown when input validation fails.
 * Maps to HTTP 400 Bad Request.
 *
 * Used for:
 * - Missing required fields
 * - Invalid data format
 * - Business validation failures
 * - Constraint violations
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
