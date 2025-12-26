package com.lanarimarco.modernization.exceptions;

/**
 * Exception thrown when a requested entity is not found
 * Maps to RPGLE CHAIN operation when %Found returns false
 *
 * Usage scenarios:
 * - Entity not found by ID
 * - Record doesn't exist in database
 * - CHAIN operation fails to find record
 */
public class NotFoundException extends ServiceException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
