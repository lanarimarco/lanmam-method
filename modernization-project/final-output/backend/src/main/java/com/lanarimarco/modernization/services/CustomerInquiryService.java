package com.lanarimarco.modernization.services;

import com.lanarimarco.modernization.dtos.CustomerInquiryDTO;
import com.lanarimarco.modernization.entities.Customer;
import com.lanarimarco.modernization.exceptions.NotFoundException;
import com.lanarimarco.modernization.exceptions.ValidationException;
import com.lanarimarco.modernization.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service for Customer Inquiry operations
 * Converted from RPGLE program: CUST001
 *
 * Business Logic:
 * - Validates customer number input
 * - Retrieves customer information from database
 * - Handles not found scenarios
 *
 * REFACTORING NOTES (R3):
 * - Fixed validation to reject negative numbers (line 76)
 * - Changed comparison from == 0 to <= 0
 */
@Service
@Transactional(readOnly = true)
public class CustomerInquiryService {

    private static final Logger log = LoggerFactory.getLogger(CustomerInquiryService.class);

    private final CustomerRepository customerRepository;

    public CustomerInquiryService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Inquire customer by customer number
     * Maps to RPGLE logic:
     * - Lines 43-48: Validation (customer number must be non-zero and positive)
     * - Lines 51-52: CHAIN to CUSTMAST by CUSTNO
     * - Lines 53-70: Handle found/not found scenarios
     *
     * @param customerNumber The customer number to search for
     * @return CustomerInquiryDTO containing customer information
     * @throws ValidationException if customer number is invalid
     * @throws NotFoundException if customer is not found
     */
    public CustomerInquiryDTO inquireCustomer(BigDecimal customerNumber) {
        log.info("Processing customer inquiry for: {}", customerNumber);

        // Validation - maps to lines 43-48 in RPGLE
        validateCustomerNumber(customerNumber);

        // CHAIN operation equivalent - maps to lines 51-52 in RPGLE
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> {
                    // Maps to lines 66-70 in RPGLE (customer not found)
                    log.warn("Customer not found: {}", customerNumber);
                    return new NotFoundException("Customer not found");
                });

        log.debug("Retrieved customer: {}", customer);

        // Convert entity to DTO - maps to lines 55-62 in RPGLE (populate display fields)
        return convertToDTO(customer);
    }

    /**
     * Validates customer number
     * Maps to RPGLE lines 43-48
     *
     * REFACTORING (R3): Changed from == 0 to <= 0 to reject negative numbers
     *
     * @param customerNumber The customer number to validate
     * @throws ValidationException if customer number is zero, negative, or null
     */
    private void validateCustomerNumber(BigDecimal customerNumber) {
        if (customerNumber == null || customerNumber.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Invalid customer number provided: {}", customerNumber);
            throw new ValidationException("Customer number required");
        }
    }

    /**
     * Converts Customer entity to CustomerInquiryDTO
     * Maps to RPGLE lines 55-62 (populate display fields)
     *
     * @param customer The customer entity
     * @return CustomerInquiryDTO with customer information
     */
    private CustomerInquiryDTO convertToDTO(Customer customer) {
        CustomerInquiryDTO dto = new CustomerInquiryDTO();
        dto.setCustomerNumber(customer.getCustomerNumber());
        dto.setCustomerName(customer.getCustomerName());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setState(customer.getState());
        dto.setZipCode(customer.getZipCode());
        dto.setPhone(customer.getPhone());
        dto.setBalance(customer.getBalance());
        // Credit limit and last order date are in entity but not displayed in RPGLE DETAIL screen
        dto.setCreditLimit(customer.getCreditLimit());
        dto.setLastOrderDate(customer.getLastOrderDate());
        return dto;
    }
}
