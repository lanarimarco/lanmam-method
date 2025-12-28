package com.lanarimarco.modernization.controllers;

import com.lanarimarco.modernization.dtos.CustomerDTO;
import com.lanarimarco.modernization.exceptions.CustomerNotFoundException;
import com.lanarimarco.modernization.exceptions.CustomerValidationException;
import com.lanarimarco.modernization.services.CustomerInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for Customer Inquiry Operations
 *
 * Provides REST API endpoints for the CUST001 Customer Inquiry program
 * Replaces the 5250 display file interface with modern REST API
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class CustomerInquiryController {

    private final CustomerInquiryService customerInquiryService;

    /**
     * Get customer by customer number
     *
     * Endpoint: GET /api/customers/{customerNumber}
     *
     * RPGLE Program Flow:
     * 1. User enters customer number on PROMPT screen
     * 2. Program validates and reads CUSTMAST
     * 3. If found, displays DETAIL screen
     * 4. If not found, displays error message
     *
     * @param customerNumber the customer number to look up
     * @return ResponseEntity with CustomerDTO if found
     * @throws CustomerNotFoundException if customer not found (404)
     * @throws CustomerValidationException if validation fails (400)
     */
    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer customerNumber) {
        log.info("REST API: Get customer request for customer number: {}", customerNumber);

        CustomerDTO customer = customerInquiryService.getCustomerByNumber(customerNumber);

        log.info("REST API: Successfully retrieved customer: {}", customerNumber);
        return ResponseEntity.ok(customer);
    }

    /**
     * Exception handler for CustomerNotFoundException
     *
     * Maps to RPGLE error message: "Customer not found"
     * Returns HTTP 404 Not Found
     *
     * @param ex the exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
        log.warn("Customer not found exception: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Customer not found");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("customerNumber", ex.getCustomerNumber());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * Exception handler for CustomerValidationException
     *
     * Maps to RPGLE error messages:
     * - "Customer number required" (when PCUSTNO = 0)
     * Returns HTTP 400 Bad Request
     *
     * @param ex the exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(CustomerValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(CustomerValidationException ex) {
        log.warn("Validation exception: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Validation failed");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Generic exception handler for unexpected errors
     *
     * Returns HTTP 500 Internal Server Error
     *
     * @param ex the exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        log.error("Unexpected error in customer inquiry", ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal server error");
        errorResponse.put("message", "An unexpected error occurred while processing your request");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
