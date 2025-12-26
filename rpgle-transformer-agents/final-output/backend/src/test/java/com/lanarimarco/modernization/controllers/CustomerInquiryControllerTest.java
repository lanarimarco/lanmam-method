package com.lanarimarco.modernization.controllers;

import com.lanarimarco.modernization.dtos.CustomerInquiryDTO;
import com.lanarimarco.modernization.exceptions.NotFoundException;
import com.lanarimarco.modernization.exceptions.ValidationException;
import com.lanarimarco.modernization.services.CustomerInquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CustomerInquiryController
 * Tests REST API endpoints with Spring MVC test framework
 *
 * Validates REST API behavior equivalent to RPGLE program CUST001:
 * - Input validation and error responses (lines 43-48)
 * - Customer retrieval and response formatting (lines 51-62)
 * - Not found error handling (lines 66-70)
 */
@WebMvcTest(CustomerInquiryController.class)
@DisplayName("CustomerInquiryController Integration Tests")
class CustomerInquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerInquiryService customerInquiryService;

    private CustomerInquiryDTO testCustomerDTO;
    private BigDecimal validCustomerNumber;

    @BeforeEach
    void setUp() {
        validCustomerNumber = new BigDecimal("12345");

        // Create test DTO matching RPGLE display fields
        testCustomerDTO = new CustomerInquiryDTO();
        testCustomerDTO.setCustomerNumber(validCustomerNumber);
        testCustomerDTO.setCustomerName("ACME Corporation");
        testCustomerDTO.setAddress("123 Main Street");
        testCustomerDTO.setCity("New York");
        testCustomerDTO.setState("NY");
        testCustomerDTO.setZipCode(new BigDecimal("10001"));
        testCustomerDTO.setPhone("212-555-1234");
        testCustomerDTO.setBalance(new BigDecimal("1500.75"));
        testCustomerDTO.setCreditLimit(new BigDecimal("5000.00"));
        testCustomerDTO.setLastOrderDate("20251215");
    }

    // ========================================
    // Success Scenarios
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should return customer when found")
    void testGetCustomer_Success() throws Exception {
        // Given: Service returns customer data
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: GET request returns 200 OK with customer data
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerNumber", is(12345)))
                .andExpect(jsonPath("$.customerName", is("ACME Corporation")))
                .andExpect(jsonPath("$.address", is("123 Main Street")))
                .andExpect(jsonPath("$.city", is("New York")))
                .andExpect(jsonPath("$.state", is("NY")))
                .andExpect(jsonPath("$.zipCode", is(10001)))
                .andExpect(jsonPath("$.phone", is("212-555-1234")))
                .andExpect(jsonPath("$.balance", is(1500.75)))
                .andExpect(jsonPath("$.creditLimit", is(5000.00)))
                .andExpect(jsonPath("$.lastOrderDate", is("20251215")));

        verify(customerInquiryService, times(1)).inquireCustomer(validCustomerNumber);
    }

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should return customer with zero balance")
    void testGetCustomer_ZeroBalance() throws Exception {
        // Given: Customer with zero balance
        testCustomerDTO.setBalance(BigDecimal.ZERO);
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Response includes zero balance
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(0)));

        verify(customerInquiryService, times(1)).inquireCustomer(validCustomerNumber);
    }

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should handle large customer numbers")
    void testGetCustomer_LargeCustomerNumber() throws Exception {
        // Given: Maximum 5-digit customer number
        BigDecimal maxNumber = new BigDecimal("99999");
        testCustomerDTO.setCustomerNumber(maxNumber);
        when(customerInquiryService.inquireCustomer(maxNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Request succeeds with large number
        mockMvc.perform(get("/api/customers/{customerNumber}", maxNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNumber", is(99999)));

        verify(customerInquiryService, times(1)).inquireCustomer(maxNumber);
    }

    // ========================================
    // Validation Error Scenarios
    // Maps to RPGLE lines 43-48 - validation errors
    // ========================================

    @Test
    @DisplayName("GET /api/customers/0 - Should return 400 Bad Request for zero customer number")
    void testGetCustomer_ZeroCustomerNumber() throws Exception {
        // Given: Service throws ValidationException for zero
        when(customerInquiryService.inquireCustomer(BigDecimal.ZERO))
                .thenThrow(new ValidationException("Customer number required"));

        // When/Then: Returns 400 Bad Request with error message
        // Maps to RPGLE: *IN90 = *On, wMessage = 'Customer number required'
        mockMvc.perform(get("/api/customers/{customerNumber}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Customer number required")))
                .andExpect(jsonPath("$.error", is("VALIDATION_ERROR")));

        verify(customerInquiryService, times(1)).inquireCustomer(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("GET /api/customers/{invalid} - Should return 400 for invalid customer number format")
    void testGetCustomer_InvalidFormat() throws Exception {
        // Given: Service throws ValidationException for invalid format
        when(customerInquiryService.inquireCustomer(any()))
                .thenThrow(new ValidationException("Customer number required"));

        // When/Then: Returns 400 Bad Request
        mockMvc.perform(get("/api/customers/{customerNumber}", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/customers/-123 - Should return 400 for negative customer number")
    void testGetCustomer_NegativeCustomerNumber() throws Exception {
        // Given: Service throws ValidationException for negative number
        BigDecimal negativeNumber = new BigDecimal("-123");
        when(customerInquiryService.inquireCustomer(negativeNumber))
                .thenThrow(new ValidationException("Customer number required"));

        // When/Then: Returns 400 Bad Request
        mockMvc.perform(get("/api/customers/{customerNumber}", negativeNumber))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Customer number required")))
                .andExpect(jsonPath("$.error", is("VALIDATION_ERROR")));
    }

    // ========================================
    // Not Found Scenarios
    // Maps to RPGLE lines 66-70 - customer not found
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should return 404 when customer not found")
    void testGetCustomer_NotFound() throws Exception {
        // Given: Service throws NotFoundException
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenThrow(new NotFoundException("Customer not found"));

        // When/Then: Returns 404 Not Found with error message
        // Maps to RPGLE: *IN90 = *On, wMessage = 'Customer not found'
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Customer not found")))
                .andExpect(jsonPath("$.error", is("NOT_FOUND")));

        verify(customerInquiryService, times(1)).inquireCustomer(validCustomerNumber);
    }

    @Test
    @DisplayName("GET /api/customers/99999 - Should return 404 for non-existent customer")
    void testGetCustomer_NonExistentCustomer() throws Exception {
        // Given: Service throws NotFoundException for customer 99999
        BigDecimal nonExistentNumber = new BigDecimal("99999");
        when(customerInquiryService.inquireCustomer(nonExistentNumber))
                .thenThrow(new NotFoundException("Customer not found"));

        // When/Then: Returns 404 Not Found
        mockMvc.perform(get("/api/customers/{customerNumber}", nonExistentNumber))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Customer not found")))
                .andExpect(jsonPath("$.error", is("NOT_FOUND")));
    }

    // ========================================
    // Error Handling Tests
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should return 500 for unexpected errors")
    void testGetCustomer_UnexpectedError() throws Exception {
        // Given: Service throws unexpected exception
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenThrow(new RuntimeException("Database connection error"));

        // When/Then: Returns 500 Internal Server Error
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("An unexpected error occurred")))
                .andExpect(jsonPath("$.error", is("INTERNAL_ERROR")));
    }

    // ========================================
    // Content Type Tests
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should return JSON content type")
    void testGetCustomer_ContentType() throws Exception {
        // Given: Service returns customer data
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Response has JSON content type
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should accept any content type in request")
    void testGetCustomer_AcceptsAnyContentType() throws Exception {
        // Given: Service returns customer data
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Request without content type succeeds
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk());
    }

    // ========================================
    // Response Structure Tests
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should include all required fields in response")
    void testGetCustomer_AllFieldsPresent() throws Exception {
        // Given: Service returns complete customer data
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Response includes all fields
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNumber", notNullValue()))
                .andExpect(jsonPath("$.customerName", notNullValue()))
                .andExpect(jsonPath("$.address", notNullValue()))
                .andExpect(jsonPath("$.city", notNullValue()))
                .andExpect(jsonPath("$.state", notNullValue()))
                .andExpect(jsonPath("$.zipCode", notNullValue()))
                .andExpect(jsonPath("$.phone", notNullValue()))
                .andExpect(jsonPath("$.balance", notNullValue()));
    }

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should handle null optional fields")
    void testGetCustomer_NullOptionalFields() throws Exception {
        // Given: Customer with null optional fields
        testCustomerDTO.setCreditLimit(null);
        testCustomerDTO.setLastOrderDate(null);
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Response handles null fields gracefully
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creditLimit").doesNotExist())
                .andExpect(jsonPath("$.lastOrderDate").doesNotExist());
    }

    // ========================================
    // Special Characters Tests
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should handle special characters in data")
    void testGetCustomer_SpecialCharacters() throws Exception {
        // Given: Customer with special characters
        testCustomerDTO.setCustomerName("O'Brien & Associates, Inc.");
        testCustomerDTO.setAddress("123 \"Main\" Street");
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Special characters are properly escaped in JSON
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is("O'Brien & Associates, Inc.")))
                .andExpect(jsonPath("$.address", containsString("\"Main\"")));
    }

    // ========================================
    // Decimal Precision Tests
    // ========================================

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should preserve decimal precision in balance")
    void testGetCustomer_DecimalPrecision() throws Exception {
        // Given: Balance with 2 decimal places
        testCustomerDTO.setBalance(new BigDecimal("1234.56"));
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Decimal precision is preserved
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(1234.56)));
    }

    @Test
    @DisplayName("GET /api/customers/{customerNumber} - Should format large balance correctly")
    void testGetCustomer_LargeBalance() throws Exception {
        // Given: Large balance value
        testCustomerDTO.setBalance(new BigDecimal("9999999.99"));
        when(customerInquiryService.inquireCustomer(validCustomerNumber))
                .thenReturn(testCustomerDTO);

        // When/Then: Large balance is correctly formatted
        mockMvc.perform(get("/api/customers/{customerNumber}", validCustomerNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(9999999.99)));
    }
}
