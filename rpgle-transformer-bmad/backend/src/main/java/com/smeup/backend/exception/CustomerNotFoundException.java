package com.smeup.backend.exception;

/**
 * Exception thrown when customer is not found.
 *
 * <p>
 * RPGLE Equivalent: ERR002 - Customer not found in CUSTMAST
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with ID: " + customerId);
    }
}
