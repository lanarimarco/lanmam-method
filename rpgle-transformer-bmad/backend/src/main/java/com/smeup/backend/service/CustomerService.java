package com.smeup.backend.service;

import com.smeup.backend.entity.Customer;
import com.smeup.backend.exception.CustomerNotFoundException;
import com.smeup.backend.exception.InvalidCustomerIdException;
import com.smeup.backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementing CUST001 customer inquiry business logic.
 *
 * <p>
 * Original RPGLE Program: CUST001 - Customer Inquiry
 *
 * <p>
 * Source: source-rpgle/programs/CUST001.rpgle
 *
 * <p>
 * This service maps RPGLE operations to Spring Boot:
 *
 * <ul>
 * <li>CHAIN CUSTMAST → findCustomerById()
 * <li>%FOUND → Optional.isPresent()
 * <li>Input validation → validateCustomerId()
 * </ul>
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Find customer by ID - equivalent to RPGLE CHAIN CUSTMAST.
     *
     * <p>
     * RPGLE Equivalent:
     *
     * <pre>
     * C     CUSTNO    CHAIN     CUSTMAST
     * C               IF        %FOUND(CUSTMAST)
     * </pre>
     *
     * @param customerId customer number (CUSTNO field)
     * @return customer if found
     * @throws InvalidCustomerIdException if customerId is null or not positive
     * @throws CustomerNotFoundException  if customer is not found
     */
    @Transactional(readOnly = true)
    public Customer findCustomerById(Long customerId) {
        validateCustomerId(customerId);
        return customerRepository
                .findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    /**
     * Validate customer ID - equivalent to RPGLE CUSTNO IFEQ *ZEROS check.
     *
     * <p>
     * RPGLE Equivalent:
     *
     * <pre>
     * C     CUSTNO    IFEQ      *ZEROS
     * C               MOVEL     'ERR001'      MSGID
     * </pre>
     */
    private void validateCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new InvalidCustomerIdException("Customer ID must be a positive number");
        }
    }
}
