package com.smeup.backend.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smeup.backend.entity.Customer;
import com.smeup.backend.exception.CustomerNotFoundException;
import com.smeup.backend.exception.GlobalExceptionHandler;
import com.smeup.backend.exception.InvalidCustomerIdException;
import com.smeup.backend.mapper.CustomerMapper;
import com.smeup.backend.service.CustomerService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Isolated Controller Test for CustomerController using MockMvc.
 *
 * <p>
 * This test focuses on the controller layer using standalone MockMvc with mocked service dependencies.
 * Unlike CustomerControllerTest (full integration), this test isolates the controller layer.
 *
 * <p>
 * RPGLE Context: Tests REST API endpoints that replace RPGLE CUST001 (Customer Inquiry) operations.
 *
 * <p>
 * Difference from CustomerControllerTest:
 * - CustomerControllerTest: Full integration test with Testcontainers, real database, full Spring Boot context
 * - CustomerControllerMockMvcTest: Controller-focused test with mocked service layer, no database, no Spring context
 *
 * <p>
 * Test Coverage:
 * - GET /api/v1/customers/{id} → 200 OK with ApiResponse wrapper
 * - GET /api/v1/customers/{id} → 404 Not Found (ProblemDetail)
 * - GET /api/v1/customers/{id} → 400 Bad Request (ProblemDetail)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerController - Isolated Controller Tests (MockMvc)")
class CustomerControllerMockMvcTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    private CustomerMapper customerMapper;
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
        customerController = new CustomerController(customerService, customerMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Test: GET /api/v1/customers/{id} returns 200 OK with valid customer data.
     *
     * <p>
     * RPGLE Equivalent:
     * <pre>
     * C     CUSTNO    CHAIN     CUSTMAST
     * C               IF        %FOUND(CUSTMAST)
     * C               EVAL      *Return data to screen*
     * </pre>
     *
     * <p>
     * Verifies:
     * - HTTP 200 OK status
     * - ApiResponse wrapper structure: { data: {...}, meta: {...} }
     * - CustomerDTO fields mapped correctly from entity
     */
    @Test
    @DisplayName("GET /api/v1/customers/{id} - Should return 200 OK with customer data in ApiResponse wrapper")
    void shouldReturnCustomerWhenIdIsValidAndExists() throws Exception {
        // Given - Mock service to return a customer (RPGLE: %FOUND = true)
        Long customerId = 1L;
        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(customerId);
        mockCustomer.setCustomerName("ACME Corporation");
        mockCustomer.setAddressLine1("123 Main St");
        mockCustomer.setCity("New York");
        mockCustomer.setState("NY");
        mockCustomer.setZipCode(10001);
        mockCustomer.setPhoneNumber("555-0100");
        mockCustomer.setAccountBalance(new BigDecimal("1500.00"));
        mockCustomer.setCreditLimit(new BigDecimal("5000.00"));

        given(customerService.findCustomerById(customerId)).willReturn(mockCustomer);

        // When & Then - Perform GET request and verify response structure
        mockMvc.perform(get("/api/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                // Verify ApiResponse wrapper structure
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.meta").exists())
                // Verify CustomerDTO fields in $.data
                .andExpect(jsonPath("$.data.customerId").value(1))
                .andExpect(jsonPath("$.data.customerName").value("ACME Corporation"))
                .andExpect(jsonPath("$.data.addressLine1").value("123 Main St"))
                .andExpect(jsonPath("$.data.city").value("New York"))
                .andExpect(jsonPath("$.data.state").value("NY"))
                .andExpect(jsonPath("$.data.zipCode").value(10001))
                .andExpect(jsonPath("$.data.phoneNumber").value("555-0100"))
                .andExpect(jsonPath("$.data.accountBalance").value(1500.00))
                .andExpect(jsonPath("$.data.creditLimit").value(5000.00));

        // Verify service was called exactly once
        verify(customerService).findCustomerById(customerId);
    }

    /**
     * Test: GET /api/v1/customers/{id} returns 404 Not Found when customer does not exist.
     *
     * <p>
     * RPGLE Equivalent:
     * <pre>
     * C     CUSTNO    CHAIN     CUSTMAST
     * C               IF        NOT %FOUND(CUSTMAST)
     * C               EVAL      *Display error message*
     * </pre>
     *
     * <p>
     * Verifies:
     * - HTTP 404 Not Found status
     * - RFC 7807 ProblemDetail format with title and detail
     */
    @Test
    @DisplayName("GET /api/v1/customers/{id} - Should return 404 Not Found when customer does not exist")
    void shouldReturn404WhenCustomerDoesNotExist() throws Exception {
        // Given - Mock service to throw CustomerNotFoundException (RPGLE: %FOUND = false)
        Long nonExistentId = 999L;
        given(customerService.findCustomerById(nonExistentId))
                .willThrow(new CustomerNotFoundException(nonExistentId));

        // When & Then - Perform GET request and verify 404 response
        mockMvc.perform(get("/api/v1/customers/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                // Verify RFC 7807 ProblemDetail structure
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Customer Not Found"))
                .andExpect(jsonPath("$.detail").value("Customer not found with ID: 999"));

        // Verify service was called
        verify(customerService).findCustomerById(nonExistentId);
    }

    /**
     * Test: GET /api/v1/customers/{id} returns 400 Bad Request for invalid customer ID.
     *
     * <p>
     * RPGLE Equivalent:
     * <pre>
     * C               IF        CUSTNO <= 0
     * C               EVAL      *Display validation error*
     * </pre>
     *
     * <p>
     * Verifies:
     * - HTTP 400 Bad Request status
     * - RFC 7807 ProblemDetail format for validation errors
     */
    @Test
    @DisplayName("GET /api/v1/customers/{id} - Should return 400 Bad Request for invalid customer ID")
    void shouldReturn400WhenCustomerIdIsInvalid() throws Exception {
        // Given - Mock service to throw InvalidCustomerIdException (RPGLE: validation failure)
        Long invalidId = -1L;
        given(customerService.findCustomerById(invalidId))
                .willThrow(new InvalidCustomerIdException("Customer ID must be a positive number"));

        // When & Then - Perform GET request and verify 400 response
        mockMvc.perform(get("/api/v1/customers/{id}", invalidId))
                .andExpect(status().isBadRequest())
                // Verify RFC 7807 ProblemDetail structure
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Invalid Customer ID"))
                .andExpect(jsonPath("$.detail").value("Customer ID must be a positive number"));

        // Verify service was called
        verify(customerService).findCustomerById(invalidId);
    }

    /**
     * Test: GET /api/v1/customers/{id} with zero ID returns 400 Bad Request.
     *
     * <p>
     * Additional edge case validation test.
     */
    @Test
    @DisplayName("GET /api/v1/customers/{id} - Should return 400 Bad Request for zero customer ID")
    void shouldReturn400WhenCustomerIdIsZero() throws Exception {
        // Given - Mock service to throw InvalidCustomerIdException for zero ID
        Long zeroId = 0L;
        given(customerService.findCustomerById(zeroId))
                .willThrow(new InvalidCustomerIdException("Customer ID must be a positive number"));

        // When & Then - Perform GET request and verify 400 response
        mockMvc.perform(get("/api/v1/customers/{id}", zeroId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Invalid Customer ID"));

        // Verify service was called
        verify(customerService).findCustomerById(zeroId);
    }
}
