package com.smeup.erp.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404 Not Found.
 *
 * Used for:
 * - Entity not found by ID
 * - Record doesn't exist in database
 * - RPGLE CHAIN operation with %NotFound
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
