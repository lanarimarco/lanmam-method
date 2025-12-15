package com.company.modernization.exceptions;

/**
 * Exception thrown when an external service call fails
 * Used for:
 * - External API failures
 * - Third-party service errors
 * - Integration failures
 */
public class ExternalServiceException extends ServiceException {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
