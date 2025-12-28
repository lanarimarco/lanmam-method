package com.lanarimarco.modernization.exceptions;

/**
 * Exception thrown when a customer is not found
 *
 * Maps to RPGLE condition: %Found(CUSTMAST) = false after CHAIN operation
 * Error message in RPGLE: "Customer not found"
 */
public class CustomerNotFoundException extends RuntimeException {

    private final Integer customerNumber;

    public CustomerNotFoundException(Integer customerNumber) {
        super(String.format("Customer not found: %d", customerNumber));
        this.customerNumber = customerNumber;
    }

    public Integer getCustomerNumber() {
        return customerNumber;
    }
}
