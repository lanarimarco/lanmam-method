package com.lanarimarco.modernization.exceptions;

/**
 * Exception thrown when customer input validation fails
 *
 * Maps to RPGLE validation errors:
 * - Customer number = 0: "Customer number required"
 */
public class CustomerValidationException extends RuntimeException {

    public CustomerValidationException(String message) {
        super(message);
    }

    public CustomerValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
