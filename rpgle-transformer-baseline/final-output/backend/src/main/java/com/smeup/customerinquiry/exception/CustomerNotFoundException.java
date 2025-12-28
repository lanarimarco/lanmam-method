package com.smeup.customerinquiry.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Integer customerNumber) {
        super("Customer not found with number: " + customerNumber);
    }
}
