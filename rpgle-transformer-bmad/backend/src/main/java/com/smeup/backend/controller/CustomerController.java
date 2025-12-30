package com.smeup.backend.controller;

import com.smeup.backend.dto.ApiResponse;
import com.smeup.backend.entity.Customer;
import com.smeup.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Customer operations.
 * Exposes endpoints for customer inquiry.
 *
 * <p>
 * RPGLE Equivalent: CUST001 - Customer Inquiry
 */
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves customer details by ID.
     *
     * <p>
     * RPGLE: CHAIN (CUSTID) CUSTMAST
     *
     * @param customerId The customer ID.
     * @return The customer details wrapped in ApiResponse.
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        return ResponseEntity.ok(new ApiResponse<>(customer));
    }
}
