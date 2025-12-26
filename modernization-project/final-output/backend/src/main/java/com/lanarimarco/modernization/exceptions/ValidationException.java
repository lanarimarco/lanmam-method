package com.lanarimarco.modernization.exceptions;

/**
 * Exception thrown when input validation fails
 * Maps to RPGLE validation errors (indicator *IN90)
 *
 * Usage scenarios:
 * - Invalid input data
 * - Required fields missing
 * - Data format errors
 * - Business validation failures
 */
public class ValidationException extends ServiceException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
