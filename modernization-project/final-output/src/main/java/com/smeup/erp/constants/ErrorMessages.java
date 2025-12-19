package com.smeup.erp.constants;

/**
 * Centralized error message constants.
 * Ensures consistency across the application and simplifies maintenance.
 */
public final class ErrorMessages {

    private ErrorMessages() {
        // Utility class - prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Validation Errors
    public static final String CUSTOMER_NUMBER_REQUIRED = "Customer number is required";
    public static final String CUSTOMER_NUMBER_INVALID = "Customer number must be between 1 and 99999";

    // Not Found Errors
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";

    // System Errors
    public static final String SYSTEM_ERROR = "A system error occurred. Please try again later";
    public static final String DATABASE_ERROR = "Database error occurred";
}
