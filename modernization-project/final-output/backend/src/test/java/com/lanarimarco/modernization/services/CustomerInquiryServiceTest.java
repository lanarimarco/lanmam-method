package com.lanarimarco.modernization.services;

import com.lanarimarco.modernization.dtos.CustomerInquiryDTO;
import com.lanarimarco.modernization.entities.Customer;
import com.lanarimarco.modernization.exceptions.NotFoundException;
import com.lanarimarco.modernization.exceptions.ValidationException;
import com.lanarimarco.modernization.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerInquiryService
 * Tests business logic in isolation with mocked dependencies
 *
 * Validates behavior equivalent to RPGLE program CUST001:
 * - Customer number validation (lines 43-48)
 * - Customer lookup via CHAIN operation (lines 51-52)
 * - Found/Not found scenarios (lines 53-70)
 * - Data mapping to display fields (lines 55-62)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerInquiryService Unit Tests")
class CustomerInquiryServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerInquiryService customerInquiryService;

    private Customer testCustomer;
    private BigDecimal validCustomerNumber;

    @BeforeEach
    void setUp() {
        validCustomerNumber = new BigDecimal("12345");

        // Create test customer matching RPGLE data structure (lines 14-22)
        testCustomer = new Customer();
        testCustomer.setCustomerNumber(validCustomerNumber);
        testCustomer.setCustomerName("ACME Corporation");
        testCustomer.setAddress("123 Main Street");
        testCustomer.setCity("New York");
        testCustomer.setState("NY");
        testCustomer.setZipCode(new BigDecimal("10001"));
        testCustomer.setPhone("212-555-1234");
        testCustomer.setBalance(new BigDecimal("1500.75"));
        testCustomer.setCreditLimit(new BigDecimal("5000.00"));
        testCustomer.setLastOrderDate("20251215");
    }

    // ========================================
    // Success Scenarios
    // ========================================

    @Test
    @DisplayName("Should successfully retrieve customer by valid customer number")
    void testInquireCustomer_Success() {
        // Given: Repository returns a customer (RPGLE: %Found = true, lines 53-62)
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called to inquire customer
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Customer data is correctly mapped to DTO
        assertThat(result).isNotNull();
        assertThat(result.getCustomerNumber()).isEqualTo(validCustomerNumber);
        assertThat(result.getCustomerName()).isEqualTo("ACME Corporation");
        assertThat(result.getAddress()).isEqualTo("123 Main Street");
        assertThat(result.getCity()).isEqualTo("New York");
        assertThat(result.getState()).isEqualTo("NY");
        assertThat(result.getZipCode()).isEqualTo(new BigDecimal("10001"));
        assertThat(result.getPhone()).isEqualTo("212-555-1234");
        assertThat(result.getBalance()).isEqualTo(new BigDecimal("1500.75"));
        assertThat(result.getCreditLimit()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(result.getLastOrderDate()).isEqualTo("20251215");

        // Verify repository interaction
        verify(customerRepository, times(1)).findByCustomerNumber(validCustomerNumber);
    }

    @Test
    @DisplayName("Should handle customer with minimum balance")
    void testInquireCustomer_MinimumBalance() {
        // Given: Customer with zero balance
        testCustomer.setBalance(BigDecimal.ZERO);
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Balance is correctly set to zero
        assertThat(result.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should handle customer with large balance")
    void testInquireCustomer_LargeBalance() {
        // Given: Customer with maximum balance (9 digits, 2 decimals - RPGLE line 22)
        BigDecimal largeBalance = new BigDecimal("9999999.99");
        testCustomer.setBalance(largeBalance);
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Large balance is handled correctly
        assertThat(result.getBalance()).isEqualTo(largeBalance);
    }

    // ========================================
    // Validation Error Scenarios
    // Maps to RPGLE lines 43-48
    // ========================================

    @Test
    @DisplayName("Should throw ValidationException when customer number is null")
    void testInquireCustomer_NullCustomerNumber() {
        // When/Then: Null customer number throws ValidationException
        // Maps to RPGLE: If PCUSTNO = 0, *IN90 = *On, wMessage = 'Customer number required'
        assertThatThrownBy(() -> customerInquiryService.inquireCustomer(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Customer number required");

        // Verify repository is never called
        verify(customerRepository, never()).findByCustomerNumber(any());
    }

    @Test
    @DisplayName("Should throw ValidationException when customer number is zero")
    void testInquireCustomer_ZeroCustomerNumber() {
        // When/Then: Zero customer number throws ValidationException
        // Maps to RPGLE: If PCUSTNO = 0 (line 43)
        assertThatThrownBy(() -> customerInquiryService.inquireCustomer(BigDecimal.ZERO))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Customer number required");

        // Verify repository is never called
        verify(customerRepository, never()).findByCustomerNumber(any());
    }

    @Test
    @DisplayName("Should throw ValidationException when customer number is negative")
    void testInquireCustomer_NegativeCustomerNumber() {
        // When/Then: Negative customer number throws ValidationException
        BigDecimal negativeNumber = new BigDecimal("-123");

        assertThatThrownBy(() -> customerInquiryService.inquireCustomer(negativeNumber))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Customer number required");

        // Verify repository is never called
        verify(customerRepository, never()).findByCustomerNumber(any());
    }

    // ========================================
    // Not Found Scenarios
    // Maps to RPGLE lines 66-70
    // ========================================

    @Test
    @DisplayName("Should throw NotFoundException when customer does not exist")
    void testInquireCustomer_CustomerNotFound() {
        // Given: Repository returns empty (RPGLE: %Found = false)
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.empty());

        // When/Then: Service throws NotFoundException
        // Maps to RPGLE: *IN90 = *On, wMessage = 'Customer not found'
        assertThatThrownBy(() -> customerInquiryService.inquireCustomer(validCustomerNumber))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        // Verify repository was called
        verify(customerRepository, times(1)).findByCustomerNumber(validCustomerNumber);
    }

    @Test
    @DisplayName("Should throw NotFoundException for non-existent customer number")
    void testInquireCustomer_NonExistentCustomer() {
        // Given: Search for customer 99999 which doesn't exist
        BigDecimal nonExistentNumber = new BigDecimal("99999");
        when(customerRepository.findByCustomerNumber(nonExistentNumber))
                .thenReturn(Optional.empty());

        // When/Then: NotFoundException is thrown
        assertThatThrownBy(() -> customerInquiryService.inquireCustomer(nonExistentNumber))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerRepository, times(1)).findByCustomerNumber(nonExistentNumber);
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    @Test
    @DisplayName("Should handle customer with minimum customer number (1)")
    void testInquireCustomer_MinimumCustomerNumber() {
        // Given: Customer number 1
        BigDecimal minNumber = BigDecimal.ONE;
        testCustomer.setCustomerNumber(minNumber);
        when(customerRepository.findByCustomerNumber(minNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(minNumber);

        // Then: Customer is retrieved successfully
        assertThat(result.getCustomerNumber()).isEqualTo(minNumber);
    }

    @Test
    @DisplayName("Should handle customer with maximum customer number (99999)")
    void testInquireCustomer_MaximumCustomerNumber() {
        // Given: Customer number 99999 (5 digits max - RPGLE line 15)
        BigDecimal maxNumber = new BigDecimal("99999");
        testCustomer.setCustomerNumber(maxNumber);
        when(customerRepository.findByCustomerNumber(maxNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(maxNumber);

        // Then: Customer is retrieved successfully
        assertThat(result.getCustomerNumber()).isEqualTo(maxNumber);
    }

    @Test
    @DisplayName("Should handle customer with null optional fields")
    void testInquireCustomer_NullOptionalFields() {
        // Given: Customer with null credit limit and last order date
        testCustomer.setCreditLimit(null);
        testCustomer.setLastOrderDate(null);
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Null fields are handled gracefully
        assertThat(result.getCreditLimit()).isNull();
        assertThat(result.getLastOrderDate()).isNull();
        // Required fields should still be populated
        assertThat(result.getCustomerNumber()).isEqualTo(validCustomerNumber);
        assertThat(result.getCustomerName()).isNotNull();
    }

    @Test
    @DisplayName("Should handle customer with empty strings")
    void testInquireCustomer_EmptyStrings() {
        // Given: Customer with empty string fields
        testCustomer.setAddress("");
        testCustomer.setCity("");
        testCustomer.setPhone("");
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Empty strings are preserved
        assertThat(result.getAddress()).isEmpty();
        assertThat(result.getCity()).isEmpty();
        assertThat(result.getPhone()).isEmpty();
    }

    @Test
    @DisplayName("Should handle customer with special characters in name")
    void testInquireCustomer_SpecialCharacters() {
        // Given: Customer name with special characters
        String nameWithSpecialChars = "O'Brien & Associates, Inc.";
        testCustomer.setCustomerName(nameWithSpecialChars);
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Special characters are preserved
        assertThat(result.getCustomerName()).isEqualTo(nameWithSpecialChars);
    }

    // ========================================
    // Data Precision Tests
    // ========================================

    @Test
    @DisplayName("Should preserve decimal precision for balance")
    void testInquireCustomer_DecimalPrecision() {
        // Given: Balance with exact 2 decimal places (RPGLE: 9Y 2 - line 22)
        BigDecimal preciseBalance = new BigDecimal("1234.56");
        testCustomer.setBalance(preciseBalance);
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Decimal precision is maintained
        assertThat(result.getBalance()).isEqualByComparingTo(preciseBalance);
    }

    @Test
    @DisplayName("Should handle zip code as numeric value")
    void testInquireCustomer_ZipCodeNumeric() {
        // Given: 5-digit zip code (RPGLE: 5Y 0 - line 20)
        BigDecimal zipCode = new BigDecimal("12345");
        testCustomer.setZipCode(zipCode);
        when(customerRepository.findByCustomerNumber(validCustomerNumber))
                .thenReturn(Optional.of(testCustomer));

        // When: Service is called
        CustomerInquiryDTO result = customerInquiryService.inquireCustomer(validCustomerNumber);

        // Then: Zip code is correctly set
        assertThat(result.getZipCode()).isEqualByComparingTo(zipCode);
    }
}
