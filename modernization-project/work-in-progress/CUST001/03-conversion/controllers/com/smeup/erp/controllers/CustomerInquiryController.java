package com.smeup.erp.controllers;

import com.smeup.erp.dtos.CustomerInquiryDTO;
import com.smeup.erp.services.CustomerInquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller for CUST001 - Customer Inquiry
 * Replaces RPGLE display file: CUSTDSP
 *
 * Display file screens mapped to REST endpoints:
 * - PROMPT screen (customer number entry) -> POST /api/customers/inquire
 * - DETAIL screen (customer information) -> Response payload
 *
 * Function keys:
 * - F3 (Exit): Handled by client/UI
 * - F12 (Return): Handled by client/UI navigation
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerInquiryController {

    private static final Logger log = LoggerFactory.getLogger(CustomerInquiryController.class);

    @Autowired
    private CustomerInquiryService customerInquiryService;

    /**
     * Customer inquiry endpoint
     * Equivalent to EXFMT PROMPT + CHAIN + EXFMT DETAIL in RPGLE
     *
     * RPGLE flow (lines 34-72):
     * 1. EXFMT PROMPT - display prompt screen, get customer number
     * 2. Validate input
     * 3. CHAIN to database
     * 4. EXFMT DETAIL - display customer details or error
     *
     * REST flow:
     * 1. Receive POST request with customer number
     * 2. Service validates and queries database
     * 3. Return customer details or error in response
     *
     * @param request CustomerInquiryDTO containing customer number
     * @return ResponseEntity with customer details or error message
     */
    @PostMapping("/inquire")
    public ResponseEntity<CustomerInquiryDTO> inquireCustomer(@RequestBody CustomerInquiryDTO request) {
        log.info("Customer inquiry request received: {}", request);

        // Process inquiry through service
        CustomerInquiryDTO response = customerInquiryService.inquireCustomer(request);

        // Return appropriate HTTP status based on result
        if (response.isSuccess()) {
            log.info("Customer inquiry successful");
            return ResponseEntity.ok(response);
        } else {
            // Error occurred (validation or not found)
            log.warn("Customer inquiry failed: {}", response.getErrorMessage());

            // Return 400 for validation errors, 404 for not found
            if ("Customer number required".equals(response.getErrorMessage())) {
                return ResponseEntity.badRequest().body(response);
            } else if ("Customer not found".equals(response.getErrorMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                // System error
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    }

    /**
     * Initialize endpoint for initial screen load
     * Returns empty DTO for new inquiry session
     * Equivalent to initial EXFMT PROMPT in RPGLE (line 34)
     *
     * @return ResponseEntity with empty CustomerInquiryDTO
     */
    @GetMapping("/inquire/init")
    public ResponseEntity<CustomerInquiryDTO> initializeInquiry() {
        log.info("Initialize customer inquiry screen");
        CustomerInquiryDTO dto = new CustomerInquiryDTO();
        return ResponseEntity.ok(dto);
    }

    /**
     * Health check endpoint
     *
     * @return Simple status message
     */
    @GetMapping("/inquire/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Customer Inquiry Service is running");
    }
}
