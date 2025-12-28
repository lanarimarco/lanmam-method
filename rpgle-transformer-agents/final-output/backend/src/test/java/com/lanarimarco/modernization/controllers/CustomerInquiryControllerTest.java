package com.lanarimarco.modernization.controllers;

import com.lanarimarco.modernization.dtos.CustomerDTO;
import com.lanarimarco.modernization.exceptions.CustomerNotFoundException;
import com.lanarimarco.modernization.exceptions.CustomerValidationException;
import com.lanarimarco.modernization.services.CustomerInquiryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CustomerInquiryController
 *
 * Tests the REST API endpoints using MockMvc
 * Mocks the service layer
 */
@WebMvcTest(CustomerInquiryController.class)
@DisplayName("Customer Inquiry Controller Integration Tests")
class CustomerInquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerInquiryService customerInquiryService;

    private CustomerDTO testCustomerDTO;
    private static final Integer VALID_CUSTOMER_NUMBER = 12345;
    private static final Integer INVALID_CUSTOMER_NUMBER = 0;
    private static final Integer NONEXISTENT_CUSTOMER_NUMBER = 99999;

    @BeforeEach
    void setUp() {
        testCustomerDTO = CustomerDTO.builder()
                .customerNumber(VALID_CUSTOMER_NUMBER)
                .customerName("John Doe")
                .address1("123 Main Street")
                .city("Springfield")
                .state("IL")
                .zipCode(62701)
                .phoneNumber("555-1234")
                .balance(new BigDecimal("1250.75"))
                .build();
    }

    @Test
    @DisplayName("GET /api/customers/{id} should return customer when found")
    void testGetCustomer_Success() throws Exception {
        // Given
        when(customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", VALID_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerNumber", is(VALID_CUSTOMER_NUMBER)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.address1", is("123 Main Street")))
                .andExpect(jsonPath("$.city", is("Springfield")))
                .andExpect(jsonPath("$.state", is("IL")))
                .andExpect(jsonPath("$.zipCode", is(62701)))
                .andExpect(jsonPath("$.phoneNumber", is("555-1234")))
                .andExpect(jsonPath("$.balance", is(1250.75)));

        verify(customerInquiryService, times(1)).getCustomerByNumber(VALID_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("GET /api/customers/{id} should return 404 when customer not found")
    void testGetCustomer_NotFound() throws Exception {
        // Given
        when(customerInquiryService.getCustomerByNumber(NONEXISTENT_CUSTOMER_NUMBER))
                .thenThrow(new CustomerNotFoundException(NONEXISTENT_CUSTOMER_NUMBER));

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", NONEXISTENT_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Customer not found")))
                .andExpect(jsonPath("$.message", containsString("Customer not found")))
                .andExpect(jsonPath("$.customerNumber", is(NONEXISTENT_CUSTOMER_NUMBER)));

        verify(customerInquiryService, times(1)).getCustomerByNumber(NONEXISTENT_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("GET /api/customers/{id} should return 400 for validation error")
    void testGetCustomer_ValidationError() throws Exception {
        // Given
        when(customerInquiryService.getCustomerByNumber(INVALID_CUSTOMER_NUMBER))
                .thenThrow(new CustomerValidationException("Customer number required"));

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", INVALID_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.message", is("Customer number required")));

        verify(customerInquiryService, times(1)).getCustomerByNumber(INVALID_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("GET /api/customers/{id} should return 500 for unexpected errors")
    void testGetCustomer_UnexpectedError() throws Exception {
        // Given
        when(customerInquiryService.getCustomerByNumber(anyInt()))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", VALID_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Internal server error")))
                .andExpect(jsonPath("$.message", containsString("unexpected error")));

        verify(customerInquiryService, times(1)).getCustomerByNumber(VALID_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("GET /api/customers/{id} should handle minimum customer number")
    void testGetCustomer_MinimumNumber() throws Exception {
        // Given
        Integer minCustomerNumber = 1;
        CustomerDTO minCustomer = CustomerDTO.builder()
                .customerNumber(minCustomerNumber)
                .customerName("Min Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(12345)
                .phoneNumber("000-0000")
                .balance(BigDecimal.ZERO)
                .build();

        when(customerInquiryService.getCustomerByNumber(minCustomerNumber))
                .thenReturn(minCustomer);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", minCustomerNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNumber", is(minCustomerNumber)))
                .andExpect(jsonPath("$.balance", is(0)));
    }

    @Test
    @DisplayName("GET /api/customers/{id} should handle maximum customer number")
    void testGetCustomer_MaximumNumber() throws Exception {
        // Given
        Integer maxCustomerNumber = 99999;
        CustomerDTO maxCustomer = CustomerDTO.builder()
                .customerNumber(maxCustomerNumber)
                .customerName("Max Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(99999)
                .phoneNumber("999-9999")
                .balance(new BigDecimal("9999999.99"))
                .build();

        when(customerInquiryService.getCustomerByNumber(maxCustomerNumber))
                .thenReturn(maxCustomer);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", maxCustomerNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNumber", is(maxCustomerNumber)))
                .andExpect(jsonPath("$.balance", is(9999999.99)));
    }

    @Test
    @DisplayName("GET /api/customers/{id} should handle null optional fields")
    void testGetCustomer_NullFields() throws Exception {
        // Given
        CustomerDTO customerWithNulls = CustomerDTO.builder()
                .customerNumber(VALID_CUSTOMER_NUMBER)
                .customerName("John Doe")
                .address1(null)
                .city(null)
                .state(null)
                .zipCode(null)
                .phoneNumber(null)
                .balance(BigDecimal.ZERO)
                .build();

        when(customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(customerWithNulls);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", VALID_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerNumber", is(VALID_CUSTOMER_NUMBER)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.address1").doesNotExist())
                .andExpect(jsonPath("$.city").doesNotExist())
                .andExpect(jsonPath("$.state").doesNotExist())
                .andExpect(jsonPath("$.zipCode").doesNotExist())
                .andExpect(jsonPath("$.phoneNumber").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/customers/{id} should handle negative customer number")
    void testGetCustomer_NegativeNumber() throws Exception {
        // Given
        Integer negativeNumber = -1;
        when(customerInquiryService.getCustomerByNumber(negativeNumber))
                .thenThrow(new CustomerValidationException("Customer number must be positive"));

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", negativeNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")))
                .andExpect(jsonPath("$.message", is("Customer number must be positive")));
    }

    @Test
    @DisplayName("GET /api/customers/{id} should return correct Content-Type header")
    void testGetCustomer_ContentType() throws Exception {
        // Given
        when(customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", VALID_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    @DisplayName("GET /api/customers/{id} should handle special characters in customer name")
    void testGetCustomer_SpecialCharactersInName() throws Exception {
        // Given
        CustomerDTO customerWithSpecialChars = CustomerDTO.builder()
                .customerNumber(VALID_CUSTOMER_NUMBER)
                .customerName("O'Brien & Sons, Inc.")
                .address1("123 Main St.")
                .city("Springfield")
                .state("IL")
                .zipCode(62701)
                .phoneNumber("555-1234")
                .balance(new BigDecimal("1000.00"))
                .build();

        when(customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(customerWithSpecialChars);

        // When & Then
        mockMvc.perform(get("/api/customers/{customerNumber}", VALID_CUSTOMER_NUMBER)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is("O'Brien & Sons, Inc.")));
    }
}
