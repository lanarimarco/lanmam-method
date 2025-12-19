package com.smeup.erp.exceptions;

/**
 * Generic service layer exception.
 * Maps to HTTP 500 Internal Server Error.
 *
 * Used for:
 * - Unexpected errors during processing
 * - Database connectivity issues
 * - External service failures
 * - System-level errors
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
