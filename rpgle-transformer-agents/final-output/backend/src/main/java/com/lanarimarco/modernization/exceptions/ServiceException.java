package com.lanarimarco.modernization.exceptions;

/**
 * Base exception for all service layer exceptions
 * Following error handling strategy from common-patterns/error-handling-strategy.md
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
