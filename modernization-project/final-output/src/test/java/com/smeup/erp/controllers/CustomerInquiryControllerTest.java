package com.smeup.erp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smeup.erp.dtos.CustomerInquiryDTO;
import com.smeup.erp.services.CustomerInquiryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for CustomerInquiryController
 * Tests REST API endpoints for CUST001 - Customer Inquiry
 *
 * Endpoints tested:
 * - POST /api/customers/inquire - Customer inquiry
 * - GET /api/customers/inquire/init - Initialize inquiry screen
 * - GET /api/customers/inquire/health - Health check
 *
 * Test Scenarios:
 * 1. Successful customer inquiry (200 OK)
 * 2. Customer not found (404 NOT FOUND)
 * 3. Validation error (400 BAD REQUEST)
 * 4. System error (500 INTERNAL SERVER ERROR)
 * 5. Initialize endpoint
 * 6. Health check endpoint
 */
@WebMvcTest(CustomerInquiryController.class)
class CustomerInquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerInquiryService customerInquiryService;

    private CustomerInquiryDTO validRequest;
    private CustomerInquiryDTO successResponse;

    @BeforeEach
    void setUp() {
        // Setup valid request
        validRequest = new CustomerInquiryDTO();
        validRequest.setCustomerNumber(12345);

        // Setup successful response
        successResponse = new CustomerInquiryDTO();
        successResponse.setCustomerNumber(12345);
        successResponse.setCustomerName("ACME Corporation");
        successResponse.setAddress1("123 Main Street");
        successResponse.setCity("New York");
        successResponse.setState("NY");
        successResponse.setZipCode(10001);
        successResponse.setPhone("212-555-1234");
        successResponse.setBalance(new BigDecimal("1500.50"));
        successResponse.setCreditLimit(new BigDecimal("5000.00"));
        successResponse.setLastOrderDate(LocalDate.of(2025, 12, 15));
        successResponse.setSuccess(true);
        successResponse.setErrorIndicator(false);
    }

    // ========================================
    // POST /api/customers/inquire - SUCCESS TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should return 200 OK with customer details")
    void testInquireCustomer_Success_Returns200() throws Exception {
        // Given - Service returns successful response
        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.errorIndicator").value(false))
            .andExpect(jsonPath("$.customerNumber").value(12345))
            .andExpect(jsonPath("$.customerName").value("ACME Corporation"))
            .andExpect(jsonPath("$.address1").value("123 Main Street"))
            .andExpect(jsonPath("$.city").value("New York"))
            .andExpect(jsonPath("$.state").value("NY"))
            .andExpect(jsonPath("$.zipCode").value(10001))
            .andExpect(jsonPath("$.phone").value("212-555-1234"))
            .andExpect(jsonPath("$.balance").value(1500.50))
            .andExpect(jsonPath("$.creditLimit").value(5000.00))
            .andExpect(jsonPath("$.lastOrderDate").value("2025-12-15"));

        verify(customerInquiryService, times(1)).inquireCustomer(any(CustomerInquiryDTO.class));
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should accept valid JSON request")
    void testInquireCustomer_ValidJSON_Accepted() throws Exception {
        // Given
        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        String jsonRequest = "{\"customerNumber\":12345}";

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    // ========================================
    // POST /api/customers/inquire - NOT FOUND TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should return 404 when customer not found")
    void testInquireCustomer_CustomerNotFound_Returns404() throws Exception {
        // Given - Service returns not found response
        CustomerInquiryDTO notFoundResponse = new CustomerInquiryDTO();
        notFoundResponse.setCustomerNumber(99999);
        notFoundResponse.setSuccess(false);
        notFoundResponse.setErrorIndicator(true);
        notFoundResponse.setErrorMessage("Customer not found");

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(notFoundResponse);

        validRequest.setCustomerNumber(99999);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorIndicator").value(true))
            .andExpect(jsonPath("$.errorMessage").value("Customer not found"))
            .andExpect(jsonPath("$.customerNumber").value(99999));
    }

    // ========================================
    // POST /api/customers/inquire - VALIDATION ERROR TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should return 400 for validation error (zero)")
    void testInquireCustomer_ValidationError_Returns400() throws Exception {
        // Given - Service returns validation error
        CustomerInquiryDTO validationErrorResponse = new CustomerInquiryDTO();
        validationErrorResponse.setCustomerNumber(0);
        validationErrorResponse.setSuccess(false);
        validationErrorResponse.setErrorIndicator(true);
        validationErrorResponse.setErrorMessage("Customer number required");

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(validationErrorResponse);

        validRequest.setCustomerNumber(0);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorIndicator").value(true))
            .andExpect(jsonPath("$.errorMessage").value("Customer number required"))
            .andExpect(jsonPath("$.customerNumber").value(0));
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should return 400 for null customer number")
    void testInquireCustomer_NullCustomerNumber_Returns400() throws Exception {
        // Given
        CustomerInquiryDTO validationErrorResponse = new CustomerInquiryDTO();
        validationErrorResponse.setCustomerNumber(null);
        validationErrorResponse.setSuccess(false);
        validationErrorResponse.setErrorIndicator(true);
        validationErrorResponse.setErrorMessage("Customer number required");

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(validationErrorResponse);

        validRequest.setCustomerNumber(null);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorMessage").value("Customer number required"));
    }

    // ========================================
    // POST /api/customers/inquire - SYSTEM ERROR TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should return 500 for system error")
    void testInquireCustomer_SystemError_Returns500() throws Exception {
        // Given - Service returns system error
        CustomerInquiryDTO systemErrorResponse = new CustomerInquiryDTO();
        systemErrorResponse.setCustomerNumber(12345);
        systemErrorResponse.setSuccess(false);
        systemErrorResponse.setErrorIndicator(true);
        systemErrorResponse.setErrorMessage("System error occurred during inquiry");

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(systemErrorResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.errorIndicator").value(true))
            .andExpect(jsonPath("$.errorMessage").value("System error occurred during inquiry"));
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should handle service throwing exception")
    void testInquireCustomer_ServiceException_Returns500() throws Exception {
        // Given - Service throws runtime exception
        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        // When & Then - Spring should handle the exception
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().is5xxServerError());
    }

    // ========================================
    // POST /api/customers/inquire - CONTENT TYPE TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should require JSON content type")
    void testInquireCustomer_InvalidContentType_Returns415() throws Exception {
        // When & Then - Sending plain text instead of JSON
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.TEXT_PLAIN)
                .content("12345"))
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should handle malformed JSON")
    void testInquireCustomer_MalformedJSON_Returns400() throws Exception {
        // When & Then - Sending invalid JSON
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
            .andExpect(status().isBadRequest());
    }

    // ========================================
    // GET /api/customers/inquire/init - INITIALIZE TESTS
    // ========================================

    @Test
    @DisplayName("GET /api/customers/inquire/init - Should return 200 with empty DTO")
    void testInitializeInquiry_Success_Returns200() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/customers/inquire/init"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.customerNumber").doesNotExist())
            .andExpect(jsonPath("$.customerName").doesNotExist());

        // Service should not be called for init
        verify(customerInquiryService, never()).inquireCustomer(any());
    }

    @Test
    @DisplayName("GET /api/customers/inquire/init - Should not require request body")
    void testInitializeInquiry_NoRequestBody() throws Exception {
        // When & Then - Simple GET request
        mockMvc.perform(get("/api/customers/inquire/init"))
            .andExpect(status().isOk());
    }

    // ========================================
    // GET /api/customers/inquire/health - HEALTH CHECK TESTS
    // ========================================

    @Test
    @DisplayName("GET /api/customers/inquire/health - Should return 200 with status message")
    void testHealthCheck_Returns200() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/customers/inquire/health"))
            .andExpect(status().isOk())
            .andExpect(content().string("Customer Inquiry Service is running"));

        // Service should not be called for health check
        verify(customerInquiryService, never()).inquireCustomer(any());
    }

    @Test
    @DisplayName("GET /api/customers/inquire/health - Should return plain text")
    void testHealthCheck_ReturnsPlainText() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/customers/inquire/health"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    // ========================================
    // CORS TESTS
    // ========================================

    @Test
    @DisplayName("Should handle CORS preflight request")
    void testCorsPreflightRequest() throws Exception {
        // When & Then - OPTIONS request for CORS preflight
        mockMvc.perform(options("/api/customers/inquire")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow cross-origin requests")
    void testCrossOriginRequest() throws Exception {
        // Given
        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then - Request with Origin header
        mockMvc.perform(post("/api/customers/inquire")
                .header("Origin", "http://localhost:3000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    // ========================================
    // BOUNDARY VALUE TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should handle maximum customer number")
    void testInquireCustomer_MaximumCustomerNumber() throws Exception {
        // Given - Maximum 5-digit customer number
        successResponse.setCustomerNumber(99999);
        validRequest.setCustomerNumber(99999);

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerNumber").value(99999));
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should handle minimum customer number")
    void testInquireCustomer_MinimumCustomerNumber() throws Exception {
        // Given - Minimum valid customer number
        successResponse.setCustomerNumber(1);
        validRequest.setCustomerNumber(1);

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerNumber").value(1));
    }

    // ========================================
    // RESPONSE FORMAT TESTS
    // ========================================

    @Test
    @DisplayName("POST /api/customers/inquire - Should return all customer fields in response")
    void testInquireCustomer_AllFieldsPresent() throws Exception {
        // Given
        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerNumber").exists())
            .andExpect(jsonPath("$.customerName").exists())
            .andExpect(jsonPath("$.address1").exists())
            .andExpect(jsonPath("$.city").exists())
            .andExpect(jsonPath("$.state").exists())
            .andExpect(jsonPath("$.zipCode").exists())
            .andExpect(jsonPath("$.phone").exists())
            .andExpect(jsonPath("$.balance").exists())
            .andExpect(jsonPath("$.creditLimit").exists())
            .andExpect(jsonPath("$.lastOrderDate").exists())
            .andExpect(jsonPath("$.success").exists())
            .andExpect(jsonPath("$.errorIndicator").exists());
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should format decimal values correctly")
    void testInquireCustomer_DecimalFormatting() throws Exception {
        // Given - Test decimal precision
        successResponse.setBalance(new BigDecimal("1234.56"));
        successResponse.setCreditLimit(new BigDecimal("5000.00"));

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance").value(1234.56))
            .andExpect(jsonPath("$.creditLimit").value(5000.00));
    }

    @Test
    @DisplayName("POST /api/customers/inquire - Should format date correctly")
    void testInquireCustomer_DateFormatting() throws Exception {
        // Given
        successResponse.setLastOrderDate(LocalDate.of(2025, 12, 15));

        when(customerInquiryService.inquireCustomer(any(CustomerInquiryDTO.class)))
            .thenReturn(successResponse);

        // When & Then
        mockMvc.perform(post("/api/customers/inquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastOrderDate").value("2025-12-15"));
    }
}
