package com.smeup.erp.controllers;

import com.smeup.erp.dtos.CustomerInquiryDTO;
import com.smeup.erp.services.CustomerInquiryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller for CUST001 - Customer Inquiry
 * Replaces RPGLE display file: CUSTDSP
 *
 * Display file screens mapped to REST endpoints:
 * - PROMPT screen (customer number entry) -> POST /api/v1/customers/inquire
 * - DETAIL screen (customer information) -> Response payload
 *
 * Function keys:
 * - F3 (Exit): Handled by client/UI
 * - F12 (Return): Handled by client/UI navigation
 *
 * REFACTORING CHANGES (2025-12-18):
 * - Implemented constructor injection instead of field injection
 * - Fixed CORS to use configuration property instead of wildcard
 * - Added @Valid annotation for request body validation
 * - Simplified error handling (delegated to GlobalExceptionHandler)
 * - Added API versioning (/v1/)
 * - Reduced logging of sensitive data
 */
@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class CustomerInquiryController {

    private static final Logger log = LoggerFactory.getLogger(CustomerInquiryController.class);

    private final CustomerInquiryService customerInquiryService;

    // Constructor injection (Spring 4.3+ doesn't require @Autowired)
    public CustomerInquiryController(CustomerInquiryService customerInquiryService) {
        this.customerInquiryService = customerInquiryService;
    }

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
     * Error handling:
     * - Validation errors (400) -> Handled by GlobalExceptionHandler
     * - Not found (404) -> Handled by GlobalExceptionHandler
     * - System errors (500) -> Handled by GlobalExceptionHandler
     *
     * @param request CustomerInquiryDTO containing customer number (validated by @Valid)
     * @return ResponseEntity with customer details
     */
    @PostMapping("/inquire")
    public ResponseEntity<CustomerInquiryDTO> inquireCustomer(
            @Valid @RequestBody CustomerInquiryDTO request) {

        log.info("Customer inquiry request received for customer number: {}",
            request.getCustomerNumber());

        // Process inquiry through service
        // Exceptions are automatically handled by GlobalExceptionHandler
        CustomerInquiryDTO response = customerInquiryService.inquireCustomer(request);

        log.info("Customer inquiry successful for customer number: {}",
            response.getCustomerNumber());

        return ResponseEntity.ok(response);
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
     * NOTE: For production, consider using Spring Boot Actuator instead
     *
     * @return Simple status message
     */
    @GetMapping("/inquire/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Customer Inquiry Service is running");
    }
}
