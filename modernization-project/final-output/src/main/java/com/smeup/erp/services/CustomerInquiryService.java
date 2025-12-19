package com.smeup.erp.services;

import com.smeup.erp.constants.ErrorMessages;
import com.smeup.erp.dtos.CustomerInquiryDTO;
import com.smeup.erp.entities.Customer;
import com.smeup.erp.exceptions.NotFoundException;
import com.smeup.erp.exceptions.ServiceException;
import com.smeup.erp.exceptions.ValidationException;
import com.smeup.erp.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementing business logic from RPGLE program: CUST001
 * Original purpose: Customer inquiry - display customer information by customer number
 *
 * Conversion notes:
 * - Main loop (DoW *IN03 = *Off) is handled by UI/client-side
 * - CHAIN operation converted to repository.findByCustomerNumber()
 * - %Found indicator converted to Optional.orElseThrow()
 * - *IN90 error indicator mapped to custom exceptions
 *
 * REFACTORING CHANGES (2025-12-18):
 * - Implemented constructor injection instead of field injection
 * - Replaced string-based error handling with custom exceptions
 * - Reduced logging of sensitive customer data
 * - Extracted error messages to constants
 */
@Service
public class CustomerInquiryService {

    private static final Logger log = LoggerFactory.getLogger(CustomerInquiryService.class);

    private final CustomerRepository customerRepository;

    // Constructor injection (Spring 4.3+ doesn't require @Autowired)
    public CustomerInquiryService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

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
     * Exception handling:
     * - ValidationException: Invalid/missing customer number -> HTTP 400
     * - NotFoundException: Customer not found -> HTTP 404
     * - ServiceException: System error -> HTTP 500
     *
     * @param request CustomerInquiryDTO with customer number
     * @return CustomerInquiryDTO with customer details
     * @throws ValidationException if customer number is invalid
     * @throws NotFoundException if customer not found
     * @throws ServiceException if system error occurs
     */
    @Transactional(readOnly = true, timeout = 5)
    public CustomerInquiryDTO inquireCustomer(CustomerInquiryDTO request) {
        log.info("Processing customer inquiry for customer number: {}", request.getCustomerNumber());

        try {
            // Validate customer number (RPGLE lines 43-48)
            // IF PCUSTNO = 0
            validateCustomerNumber(request.getCustomerNumber());

            // CHAIN operation (RPGLE line 51)
            // C     PCUSTNO       Chain     CUSTMAST
            Customer customer = customerRepository
                .findByCustomerNumber(request.getCustomerNumber())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND));

            log.info("Customer found with number: {}", customer.getCustomerNumber());

            // Populate response with customer data (RPGLE lines 55-62)
            CustomerInquiryDTO response = new CustomerInquiryDTO();
            populateCustomerDetails(response, customer);
            response.setSuccess(true);
            response.setErrorIndicator(false);

            return response;

        } catch (ValidationException | NotFoundException e) {
            // Re-throw validation and not found exceptions
            // These will be caught by GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            // Catch unexpected errors and wrap in ServiceException
            log.error("Error processing customer inquiry for customer number: {}",
                request.getCustomerNumber(), e);
            throw new ServiceException(ErrorMessages.SYSTEM_ERROR, e);
        }
    }

    /**
     * Validate customer number
     * Equivalent to RPGLE validation logic (lines 43-48)
     *
     * @param customerNumber the customer number to validate
     * @throws ValidationException if customer number is null or zero
     */
    private void validateCustomerNumber(Integer customerNumber) {
        // RPGLE: IF PCUSTNO = 0
        if (customerNumber == null || customerNumber == 0) {
            log.warn("Validation failed: Customer number is null or zero");
            // RPGLE: EVAL *IN90 = *On
            // RPGLE: EVAL wMessage = 'Customer number required'
            throw new ValidationException(ErrorMessages.CUSTOMER_NUMBER_REQUIRED);
        }

        // Additional validation for range
        if (customerNumber < 1 || customerNumber > 99999) {
            log.warn("Validation failed: Customer number out of range: {}", customerNumber);
            throw new ValidationException(ErrorMessages.CUSTOMER_NUMBER_INVALID);
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
