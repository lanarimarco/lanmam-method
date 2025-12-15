package com.company.modernization.exceptions;

/**
 * Exception thrown when a requested entity is not found
 * Used for:
 * - Entity not found by ID
 * - Record doesn't exist (RPGLE CHAIN not found)
 */
public class NotFoundException extends ServiceException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
