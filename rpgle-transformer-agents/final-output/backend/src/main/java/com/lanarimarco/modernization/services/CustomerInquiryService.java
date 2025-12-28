package com.lanarimarco.modernization.services;

import com.lanarimarco.modernization.dtos.CustomerDTO;
import com.lanarimarco.modernization.entities.Customer;
import com.lanarimarco.modernization.exceptions.CustomerNotFoundException;
import com.lanarimarco.modernization.exceptions.CustomerValidationException;
import com.lanarimarco.modernization.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for Customer Inquiry Operations
 *
 * Implements the business logic from RPGLE program CUST001
 * Provides customer lookup functionality
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerInquiryService {

    private final CustomerRepository customerRepository;

    /**
     * Get customer by customer number
     *
     * Maps to RPGLE logic:
     * 1. Validate customer number (must not be 0)
     * 2. CHAIN to CUSTMAST by customer number
     * 3. Check %Found(CUSTMAST)
     * 4. If found, populate detail screen
     * 5. If not found, display error message
     *
     * @param customerNumber the customer number to look up
     * @return CustomerDTO containing customer information
     * @throws CustomerValidationException if customer number is invalid (0 or null)
     * @throws CustomerNotFoundException if customer not found in database
     */
    public CustomerDTO getCustomerByNumber(Integer customerNumber) {
        log.info("Processing customer inquiry for customer number: {}", customerNumber);

        // Validate customer number
        // RPGLE: If PCUSTNO = 0
        validateCustomerNumber(customerNumber);

        // Read customer master
        // RPGLE: C     PCUSTNO       Chain     CUSTMAST
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> {
                    log.warn("Customer not found: {}", customerNumber);
                    return new CustomerNotFoundException(customerNumber);
                });

        log.debug("Customer found: {} - {}", customer.getCustomerNumber(), customer.getCustomerName());

        // Map entity to DTO
        return mapToDTO(customer);
    }

    /**
     * Validate customer number
     *
     * RPGLE validation logic:
     * If PCUSTNO = 0
     *    Eval *IN90 = *On
     *    Eval PMSG = 'Customer number required'
     *
     * @param customerNumber the customer number to validate
     * @throws CustomerValidationException if validation fails
     */
    private void validateCustomerNumber(Integer customerNumber) {
        if (customerNumber == null || customerNumber == 0) {
            log.warn("Invalid customer number provided: {}", customerNumber);
            throw new CustomerValidationException("Customer number required");
        }

        // Additional validation: customer number should be positive
        if (customerNumber < 0) {
            log.warn("Negative customer number provided: {}", customerNumber);
            throw new CustomerValidationException("Customer number must be positive");
        }
    }

    /**
     * Map Customer entity to CustomerDTO
     *
     * RPGLE mapping:
     * Eval DCUSTNO = CUSTNO
     * Eval DCUSTNAME = CUSTNAME
     * Eval DADDR1 = ADDR1
     * Eval DCITY = CITY
     * Eval DSTATE = STATE
     * Eval DZIP = ZIP
     * Eval DPHONE = PHONE
     * Eval DBALANCE = BALANCE
     *
     * @param customer the Customer entity
     * @return CustomerDTO
     */
    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerNumber(customer.getCustomerNumber())
                .customerName(customer.getCustomerName())
                .address1(customer.getAddress1())
                .city(customer.getCity())
                .state(customer.getState())
                .zipCode(customer.getZipCode())
                .phoneNumber(customer.getPhoneNumber())
                .balance(customer.getBalance())
                .build();
    }
}
