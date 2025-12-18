package com.smeup.erp.services;

import com.smeup.erp.dtos.CustomerInquiryDTO;
import com.smeup.erp.entities.Customer;
import com.smeup.erp.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service implementing business logic from RPGLE program: CUST001
 * Original purpose: Customer inquiry - display customer information by customer number
 *
 * Conversion notes:
 * - Main loop (DoW *IN03 = *Off) is handled by UI/client-side
 * - CHAIN operation converted to repository.findByCustomerNumber()
 * - %Found indicator converted to Optional.isPresent()
 * - *IN90 error indicator mapped to DTO errorIndicator field
 */
@Service
public class CustomerInquiryService {

    private static final Logger log = LoggerFactory.getLogger(CustomerInquiryService.class);

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Process customer inquiry request
     * Equivalent to RPGLE main logic (lines 34-74)
     *
     * RPGLE flow:
     * 1. Validate customer number (line 43-48)
     * 2. CHAIN to CUSTMAST (line 51)
     * 3. If found, populate display fields (line 53-64)
     * 4. If not found, set error (line 66-70)
     *
     * @param request CustomerInquiryDTO with customer number
     * @return CustomerInquiryDTO with customer details or error message
     */
    @Transactional(readOnly = true)
    public CustomerInquiryDTO inquireCustomer(CustomerInquiryDTO request) {
        log.info("Processing customer inquiry for customer number: {}", request.getCustomerNumber());

        CustomerInquiryDTO response = new CustomerInquiryDTO();
        response.setCustomerNumber(request.getCustomerNumber());

        try {
            // Validate customer number (RPGLE lines 43-48)
            // IF PCUSTNO = 0
            validateCustomerNumber(request.getCustomerNumber(), response);

            if (response.isErrorIndicator()) {
                log.warn("Validation failed: {}", response.getErrorMessage());
                return response;
            }

            // CHAIN operation (RPGLE line 51)
            // C     PCUSTNO       Chain     CUSTMAST
            Optional<Customer> customerOpt = customerRepository.findByCustomerNumber(request.getCustomerNumber());

            // IF %Found(CUSTMAST) (RPGLE line 53)
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                log.info("Customer found: {}", customer.getCustomerNumber());

                // Populate response with customer data (RPGLE lines 55-62)
                populateCustomerDetails(response, customer);
                response.setSuccess(true);
                response.setErrorIndicator(false);

            } else {
                // Customer not found (RPGLE lines 66-70)
                // EVAL *IN90 = *On
                // EVAL wMessage = 'Customer not found'
                log.warn("Customer not found: {}", request.getCustomerNumber());
                response.setSuccess(false);
                response.setErrorIndicator(true);
                response.setErrorMessage("Customer not found");
            }

            return response;

        } catch (Exception e) {
            log.error("Error processing customer inquiry", e);
            response.setSuccess(false);
            response.setErrorIndicator(true);
            response.setErrorMessage("System error occurred during inquiry");
            return response;
        }
    }

    /**
     * Validate customer number
     * Equivalent to RPGLE validation logic (lines 43-48)
     *
     * @param customerNumber the customer number to validate
     * @param response the response object to populate with error if validation fails
     */
    private void validateCustomerNumber(Integer customerNumber, CustomerInquiryDTO response) {
        // RPGLE: IF PCUSTNO = 0
        if (customerNumber == null || customerNumber == 0) {
            // RPGLE: EVAL *IN90 = *On
            response.setErrorIndicator(true);
            // RPGLE: EVAL wMessage = 'Customer number required'
            response.setErrorMessage("Customer number required");
            response.setSuccess(false);
            log.debug("Validation error: Customer number is required");
        }
    }

    /**
     * Populate customer details into response DTO
     * Equivalent to RPGLE display field population (lines 55-62)
     *
     * RPGLE assignments:
     * - DCUSTNO = CUSTNO
     * - DCUSTNAME = CUSTNAME
     * - DADDR1 = ADDR1
     * - DCITY = CITY
     * - DSTATE = STATE
     * - DZIP = ZIP
     * - DPHONE = PHONE
     * - DBALANCE = BALANCE
     *
     * @param response the DTO to populate
     * @param customer the Customer entity from database
     */
    private void populateCustomerDetails(CustomerInquiryDTO response, Customer customer) {
        response.setCustomerNumber(customer.getCustomerNumber());
        response.setCustomerName(customer.getCustomerName());
        response.setAddress1(customer.getAddress1());
        response.setCity(customer.getCity());
        response.setState(customer.getState());
        response.setZipCode(customer.getZipCode());
        response.setPhone(customer.getPhone());
        response.setBalance(customer.getBalance());
        response.setCreditLimit(customer.getCreditLimit());
        response.setLastOrderDate(customer.getLastOrderDate());
    }
}
