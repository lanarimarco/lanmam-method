package com.company.modernization.exceptions;

/**
 * Base exception for all service-level exceptions in the modernization project
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
