package com.company.modernization.exceptions;

/**
 * Exception thrown when a database access error occurs
 * Used for:
 * - Database connection failures
 * - Query execution errors
 * - Data integrity violations
 */
public class DataAccessException extends ServiceException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
