package com.lanarimarco.modernization.services;

import com.lanarimarco.modernization.dtos.CustomerDTO;
import com.lanarimarco.modernization.entities.Customer;
import com.lanarimarco.modernization.exceptions.CustomerNotFoundException;
import com.lanarimarco.modernization.exceptions.CustomerValidationException;
import com.lanarimarco.modernization.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerInquiryService
 *
 * Tests the business logic for customer inquiry operations
 * Mocks the repository layer for isolation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Inquiry Service Tests")
class CustomerInquiryServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerInquiryService customerInquiryService;

    private Customer testCustomer;
    private static final Integer VALID_CUSTOMER_NUMBER = 12345;
    private static final Integer INVALID_CUSTOMER_NUMBER = 0;
    private static final Integer NEGATIVE_CUSTOMER_NUMBER = -1;
    private static final Integer NONEXISTENT_CUSTOMER_NUMBER = 99999;

    @BeforeEach
    void setUp() {
        // Create test customer data
        testCustomer = Customer.builder()
                .customerNumber(VALID_CUSTOMER_NUMBER)
                .customerName("John Doe")
                .address1("123 Main Street")
                .city("Springfield")
                .state("IL")
                .zipCode(62701)
                .phoneNumber("555-1234")
                .balance(new BigDecimal("1250.75"))
                .creditLimit(new BigDecimal("5000.00"))
                .lastOrderDate(20251215)
                .build();
    }

    @Test
    @DisplayName("Should return customer when valid customer number is provided")
    void testGetCustomerByNumber_ValidCustomer() {
        // Given
        when(customerRepository.findByCustomerNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(Optional.of(testCustomer));

        // When
        CustomerDTO result = customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(VALID_CUSTOMER_NUMBER);
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        assertThat(result.getAddress1()).isEqualTo("123 Main Street");
        assertThat(result.getCity()).isEqualTo("Springfield");
        assertThat(result.getState()).isEqualTo("IL");
        assertThat(result.getZipCode()).isEqualTo(62701);
        assertThat(result.getPhoneNumber()).isEqualTo("555-1234");
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("1250.75"));

        // Verify repository was called
        verify(customerRepository, times(1)).findByCustomerNumber(VALID_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("Should throw CustomerValidationException when customer number is null")
    void testGetCustomerByNumber_NullCustomerNumber() {
        // When & Then
        assertThatThrownBy(() -> customerInquiryService.getCustomerByNumber(null))
                .isInstanceOf(CustomerValidationException.class)
                .hasMessage("Customer number required");

        // Verify repository was not called
        verify(customerRepository, never()).findByCustomerNumber(anyInt());
    }

    @Test
    @DisplayName("Should throw CustomerValidationException when customer number is zero")
    void testGetCustomerByNumber_ZeroCustomerNumber() {
        // When & Then
        assertThatThrownBy(() -> customerInquiryService.getCustomerByNumber(INVALID_CUSTOMER_NUMBER))
                .isInstanceOf(CustomerValidationException.class)
                .hasMessage("Customer number required");

        // Verify repository was not called
        verify(customerRepository, never()).findByCustomerNumber(anyInt());
    }

    @Test
    @DisplayName("Should throw CustomerValidationException when customer number is negative")
    void testGetCustomerByNumber_NegativeCustomerNumber() {
        // When & Then
        assertThatThrownBy(() -> customerInquiryService.getCustomerByNumber(NEGATIVE_CUSTOMER_NUMBER))
                .isInstanceOf(CustomerValidationException.class)
                .hasMessage("Customer number must be positive");

        // Verify repository was not called
        verify(customerRepository, never()).findByCustomerNumber(anyInt());
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer does not exist")
    void testGetCustomerByNumber_CustomerNotFound() {
        // Given
        when(customerRepository.findByCustomerNumber(NONEXISTENT_CUSTOMER_NUMBER))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerInquiryService.getCustomerByNumber(NONEXISTENT_CUSTOMER_NUMBER))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessage("Customer not found: " + NONEXISTENT_CUSTOMER_NUMBER);

        // Verify repository was called
        verify(customerRepository, times(1)).findByCustomerNumber(NONEXISTENT_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("Should not include credit limit and last order date in DTO")
    void testGetCustomerByNumber_DTOMappingExcludesUnusedFields() {
        // Given
        when(customerRepository.findByCustomerNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(Optional.of(testCustomer));

        // When
        CustomerDTO result = customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER);

        // Then
        assertThat(result).isNotNull();
        // DTO should only have the fields used by CUST001 program
        // creditLimit and lastOrderDate should not be in the DTO
        assertThat(result).hasFieldOrProperty("customerNumber");
        assertThat(result).hasFieldOrProperty("customerName");
        assertThat(result).hasFieldOrProperty("address1");
        assertThat(result).hasFieldOrProperty("city");
        assertThat(result).hasFieldOrProperty("state");
        assertThat(result).hasFieldOrProperty("zipCode");
        assertThat(result).hasFieldOrProperty("phoneNumber");
        assertThat(result).hasFieldOrProperty("balance");
    }

    @Test
    @DisplayName("Should handle customer with minimum valid number (1)")
    void testGetCustomerByNumber_MinimumValidNumber() {
        // Given
        Integer minCustomerNumber = 1;
        Customer minCustomer = Customer.builder()
                .customerNumber(minCustomerNumber)
                .customerName("Min Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(12345)
                .phoneNumber("000-0000")
                .balance(BigDecimal.ZERO)
                .build();

        when(customerRepository.findByCustomerNumber(minCustomerNumber))
                .thenReturn(Optional.of(minCustomer));

        // When
        CustomerDTO result = customerInquiryService.getCustomerByNumber(minCustomerNumber);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(minCustomerNumber);
    }

    @Test
    @DisplayName("Should handle customer with maximum valid number (99999)")
    void testGetCustomerByNumber_MaximumValidNumber() {
        // Given
        Integer maxCustomerNumber = 99999;
        Customer maxCustomer = Customer.builder()
                .customerNumber(maxCustomerNumber)
                .customerName("Max Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(12345)
                .phoneNumber("999-9999")
                .balance(new BigDecimal("9999999.99"))
                .build();

        when(customerRepository.findByCustomerNumber(maxCustomerNumber))
                .thenReturn(Optional.of(maxCustomer));

        // When
        CustomerDTO result = customerInquiryService.getCustomerByNumber(maxCustomerNumber);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(maxCustomerNumber);
    }

    @Test
    @DisplayName("Should handle customer with null optional fields")
    void testGetCustomerByNumber_NullOptionalFields() {
        // Given
        Customer customerWithNulls = Customer.builder()
                .customerNumber(VALID_CUSTOMER_NUMBER)
                .customerName("John Doe")
                .address1(null) // Nullable field
                .city(null)     // Nullable field
                .state(null)    // Nullable field
                .zipCode(null)  // Nullable field
                .phoneNumber(null) // Nullable field
                .balance(BigDecimal.ZERO)
                .build();

        when(customerRepository.findByCustomerNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(Optional.of(customerWithNulls));

        // When
        CustomerDTO result = customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(VALID_CUSTOMER_NUMBER);
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        assertThat(result.getAddress1()).isNull();
        assertThat(result.getCity()).isNull();
        assertThat(result.getState()).isNull();
        assertThat(result.getZipCode()).isNull();
        assertThat(result.getPhoneNumber()).isNull();
    }

    @Test
    @DisplayName("Should handle BigDecimal balance with proper precision")
    void testGetCustomerByNumber_BigDecimalPrecision() {
        // Given
        BigDecimal preciseBalance = new BigDecimal("12345.67");
        testCustomer.setBalance(preciseBalance);

        when(customerRepository.findByCustomerNumber(VALID_CUSTOMER_NUMBER))
                .thenReturn(Optional.of(testCustomer));

        // When
        CustomerDTO result = customerInquiryService.getCustomerByNumber(VALID_CUSTOMER_NUMBER);

        // Then
        assertThat(result.getBalance()).isEqualByComparingTo(preciseBalance);
        assertThat(result.getBalance().scale()).isEqualTo(2);
    }
}
