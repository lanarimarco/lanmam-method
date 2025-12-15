package com.company.modernization.exceptions;

/**
 * Exception thrown when a business rule is violated
 * Used for:
 * - Business rule violations
 * - State conflicts
 * - Constraint violations
 */
public class BusinessRuleException extends ServiceException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
