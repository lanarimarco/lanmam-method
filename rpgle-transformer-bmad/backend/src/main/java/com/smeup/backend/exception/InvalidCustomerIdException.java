package com.smeup.backend.exception;

/**
 * Exception thrown when customer ID is invalid.
 *
 * <p>
 * RPGLE Equivalent: ERR001 - Invalid customer number
 */
public class InvalidCustomerIdException extends RuntimeException {
    public InvalidCustomerIdException(String message) {
        super(message);
    }
}
