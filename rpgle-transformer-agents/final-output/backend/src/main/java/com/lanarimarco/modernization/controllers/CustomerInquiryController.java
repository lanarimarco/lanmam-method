package com.lanarimarco.modernization.controllers;

import com.lanarimarco.modernization.dtos.CustomerInquiryDTO;
import com.lanarimarco.modernization.services.CustomerInquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST Controller for Customer Inquiry operations
 * Converted from RPGLE program: CUST001
 *
 * Provides REST API endpoints to replace the interactive display file (CUSTDSP)
 *
 * REFACTORING NOTES (R1, R2, R5):
 * - Removed ErrorResponse inner class (now in com.lanarimarco.modernization.dtos)
 * - Removed @ExceptionHandler methods (now in GlobalExceptionHandler)
 * - Controller simplified to focus only on request handling
 * - Exception handling centralized using @RestControllerAdvice pattern
 *
 * For future POST/PUT endpoints with CustomerInquiryDTO in request body,
 * use @Valid annotation to trigger bean validation:
 *
 * Example:
 * @PostMapping
 * public ResponseEntity<CustomerInquiryDTO> createCustomer(
 *     @Valid @RequestBody CustomerInquiryDTO customer) {
 *     // Bean validation automatically triggered
 *     // GlobalExceptionHandler will catch MethodArgumentNotValidException
 * }
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerInquiryController {

    private static final Logger log = LoggerFactory.getLogger(CustomerInquiryController.class);

    private final CustomerInquiryService customerInquiryService;

    public CustomerInquiryController(CustomerInquiryService customerInquiryService) {
        this.customerInquiryService = customerInquiryService;
    }

    /**
     * Get customer by customer number
     * Maps to RPGLE program flow: PROMPT screen input -> CHAIN operation -> DETAIL screen display
     *
     * @param customerNumber The customer number to search for
     * @return CustomerInquiryDTO with customer information
     */
    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerInquiryDTO> getCustomer(@PathVariable BigDecimal customerNumber) {
        log.info("GET /api/customers/{}", customerNumber);

        CustomerInquiryDTO customer = customerInquiryService.inquireCustomer(customerNumber);
        return ResponseEntity.ok(customer);
    }
}
