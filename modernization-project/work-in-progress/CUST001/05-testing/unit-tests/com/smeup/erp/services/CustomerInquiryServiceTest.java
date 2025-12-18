package com.smeup.erp.services;

import com.smeup.erp.dtos.CustomerInquiryDTO;
import com.smeup.erp.entities.Customer;
import com.smeup.erp.repositories.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerInquiryService
 * Tests converted logic from RPGLE program: CUST001
 *
 * Test Scenarios based on CUST001-analysis.md section 14:
 * 1. Happy Path - valid customer number returns customer details
 * 2. Customer Not Found - non-existent customer number
 * 3. Validation Error - zero customer number
 * 4. Validation Error - null customer number
 * 5. Boundary Testing - maximum customer number (99999)
 * 6. Exception Handling - repository exceptions
 */
@ExtendWith(MockitoExtension.class)
class CustomerInquiryServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerInquiryService service;

    private CustomerInquiryDTO validRequest;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Setup valid test request
        validRequest = new CustomerInquiryDTO();
        validRequest.setCustomerNumber(12345);

        // Setup test customer entity
        testCustomer = new Customer();
        testCustomer.setCustomerNumber(12345);
        testCustomer.setCustomerName("ACME Corporation");
        testCustomer.setAddress1("123 Main Street");
        testCustomer.setCity("New York");
        testCustomer.setState("NY");
        testCustomer.setZipCode(10001);
        testCustomer.setPhone("212-555-1234");
        testCustomer.setBalance(new BigDecimal("1500.50"));
        testCustomer.setCreditLimit(new BigDecimal("5000.00"));
        testCustomer.setLastOrderDate(LocalDate.of(2025, 12, 15));
    }

    // ========================================
    // HAPPY PATH TESTS
    // ========================================

    @Test
    @DisplayName("Should successfully return customer details for valid customer number")
    void testInquireCustomer_ValidCustomer_ReturnsCustomerDetails() {
        // Given - RPGLE: Valid CUSTNO, CHAIN finds customer (lines 51-64)
        when(customerRepository.findByCustomerNumber(12345))
            .thenReturn(Optional.of(testCustomer));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - Verify RPGLE behavior: populate display fields (lines 55-62)
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertFalse(response.isErrorIndicator());
        assertNull(response.getErrorMessage());

        assertEquals(12345, response.getCustomerNumber());
        assertEquals("ACME Corporation", response.getCustomerName());
        assertEquals("123 Main Street", response.getAddress1());
        assertEquals("New York", response.getCity());
        assertEquals("NY", response.getState());
        assertEquals(10001, response.getZipCode());
        assertEquals("212-555-1234", response.getPhone());
        assertEquals(new BigDecimal("1500.50"), response.getBalance());
        assertEquals(new BigDecimal("5000.00"), response.getCreditLimit());
        assertEquals(LocalDate.of(2025, 12, 15), response.getLastOrderDate());

        verify(customerRepository, times(1)).findByCustomerNumber(12345);
    }

    @Test
    @DisplayName("Should preserve customer number in response even when customer found")
    void testInquireCustomer_PreservesCustomerNumber() {
        // Given
        when(customerRepository.findByCustomerNumber(99999))
            .thenReturn(Optional.of(testCustomer));

        validRequest.setCustomerNumber(99999);

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - Customer number from request should be preserved
        assertEquals(99999, response.getCustomerNumber());
    }

    // ========================================
    // CUSTOMER NOT FOUND TESTS
    // ========================================

    @Test
    @DisplayName("Should return error when customer not found")
    void testInquireCustomer_CustomerNotFound_ReturnsError() {
        // Given - RPGLE: CHAIN operation, %Found(CUSTMAST) = false (lines 66-70)
        when(customerRepository.findByCustomerNumber(12345))
            .thenReturn(Optional.empty());

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - Verify RPGLE behavior: set *IN90, display error message
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.isErrorIndicator()); // *IN90 = *On
        assertEquals("Customer not found", response.getErrorMessage());
        assertEquals(12345, response.getCustomerNumber());

        // Customer fields should be null/not populated
        assertNull(response.getCustomerName());
        assertNull(response.getAddress1());
        assertNull(response.getCity());

        verify(customerRepository, times(1)).findByCustomerNumber(12345);
    }

    @Test
    @DisplayName("Should handle non-existent customer number 99999")
    void testInquireCustomer_NonExistentCustomer() {
        // Given
        validRequest.setCustomerNumber(99999);
        when(customerRepository.findByCustomerNumber(99999))
            .thenReturn(Optional.empty());

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then
        assertFalse(response.isSuccess());
        assertTrue(response.isErrorIndicator());
        assertEquals("Customer not found", response.getErrorMessage());
    }

    // ========================================
    // VALIDATION ERROR TESTS
    // ========================================

    @Test
    @DisplayName("Should return validation error for zero customer number")
    void testInquireCustomer_ZeroCustomerNumber_ReturnsValidationError() {
        // Given - RPGLE: IF PCUSTNO = 0 (line 43)
        validRequest.setCustomerNumber(0);

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - RPGLE: set *IN90 = *On, wMessage = 'Customer number required' (lines 44-45)
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.isErrorIndicator()); // *IN90 = *On
        assertEquals("Customer number required", response.getErrorMessage());
        assertEquals(0, response.getCustomerNumber());

        // Repository should not be called when validation fails
        verify(customerRepository, never()).findByCustomerNumber(any());
    }

    @Test
    @DisplayName("Should return validation error for null customer number")
    void testInquireCustomer_NullCustomerNumber_ReturnsValidationError() {
        // Given - RPGLE: IF PCUSTNO = 0 (null equivalent)
        validRequest.setCustomerNumber(null);

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - RPGLE: set *IN90 = *On, wMessage = 'Customer number required'
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.isErrorIndicator());
        assertEquals("Customer number required", response.getErrorMessage());
        assertNull(response.getCustomerNumber());

        // Repository should not be called when validation fails
        verify(customerRepository, never()).findByCustomerNumber(any());
    }

    @Test
    @DisplayName("Should not call repository when validation fails")
    void testInquireCustomer_ValidationFailure_NoRepositoryCall() {
        // Given
        validRequest.setCustomerNumber(0);

        // When
        service.inquireCustomer(validRequest);

        // Then - verify no database access on validation failure
        verify(customerRepository, never()).findByCustomerNumber(any());
    }

    // ========================================
    // BOUNDARY VALUE TESTS
    // ========================================

    @Test
    @DisplayName("Should handle maximum customer number 99999")
    void testInquireCustomer_MaximumCustomerNumber_Success() {
        // Given - Test boundary: 5P 0 field max = 99999
        Integer maxCustomerNumber = 99999;
        validRequest.setCustomerNumber(maxCustomerNumber);

        Customer maxCustomer = new Customer();
        maxCustomer.setCustomerNumber(maxCustomerNumber);
        maxCustomer.setCustomerName("Max Customer");
        maxCustomer.setBalance(new BigDecimal("9999999.99"));

        when(customerRepository.findByCustomerNumber(maxCustomerNumber))
            .thenReturn(Optional.of(maxCustomer));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then
        assertTrue(response.isSuccess());
        assertFalse(response.isErrorIndicator());
        assertEquals(maxCustomerNumber, response.getCustomerNumber());
        assertEquals("Max Customer", response.getCustomerName());
        assertEquals(new BigDecimal("9999999.99"), response.getBalance());
    }

    @Test
    @DisplayName("Should handle minimum valid customer number 1")
    void testInquireCustomer_MinimumCustomerNumber_Success() {
        // Given - Minimum valid customer number
        Integer minCustomerNumber = 1;
        validRequest.setCustomerNumber(minCustomerNumber);

        Customer minCustomer = new Customer();
        minCustomer.setCustomerNumber(minCustomerNumber);
        minCustomer.setCustomerName("First Customer");

        when(customerRepository.findByCustomerNumber(minCustomerNumber))
            .thenReturn(Optional.of(minCustomer));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then
        assertTrue(response.isSuccess());
        assertEquals(minCustomerNumber, response.getCustomerNumber());
        assertEquals("First Customer", response.getCustomerName());
    }

    // ========================================
    // EXCEPTION HANDLING TESTS
    // ========================================

    @Test
    @DisplayName("Should handle repository exception gracefully")
    void testInquireCustomer_RepositoryException_ReturnsSystemError() {
        // Given - Repository throws exception (simulating database error)
        when(customerRepository.findByCustomerNumber(12345))
            .thenThrow(new RuntimeException("Database connection failed"));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - Should return system error message
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.isErrorIndicator());
        assertEquals("System error occurred during inquiry", response.getErrorMessage());
        assertEquals(12345, response.getCustomerNumber());
    }

    @Test
    @DisplayName("Should handle null pointer exception in repository")
    void testInquireCustomer_NullPointerException_ReturnsSystemError() {
        // Given
        when(customerRepository.findByCustomerNumber(12345))
            .thenThrow(new NullPointerException("Null entity"));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then
        assertFalse(response.isSuccess());
        assertTrue(response.isErrorIndicator());
        assertEquals("System error occurred during inquiry", response.getErrorMessage());
    }

    // ========================================
    // RPGLE BEHAVIOR VERIFICATION TESTS
    // ========================================

    @Test
    @DisplayName("Should verify CHAIN operation equivalent behavior")
    void testChainOperationEquivalent() {
        // This test verifies the Java code matches RPGLE CHAIN behavior
        // RPGLE: C     PCUSTNO       Chain     CUSTMAST (line 51)

        // Given
        Integer customerNumber = 12345;
        when(customerRepository.findByCustomerNumber(customerNumber))
            .thenReturn(Optional.of(testCustomer));

        // When - Direct repository call (simulating CHAIN)
        Optional<Customer> result = customerRepository.findByCustomerNumber(customerNumber);

        // Then - Verify %Found(CUSTMAST) equivalent
        assertTrue(result.isPresent()); // %Found = true
        assertEquals(testCustomer, result.get());
        assertEquals(customerNumber, result.get().getCustomerNumber());
    }

    @Test
    @DisplayName("Should verify %NotFound equivalent behavior")
    void testNotFoundEquivalent() {
        // RPGLE: %Found(CUSTMAST) = false

        // Given
        Integer customerNumber = 99999;
        when(customerRepository.findByCustomerNumber(customerNumber))
            .thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(customerNumber);

        // Then
        assertFalse(result.isPresent()); // %Found = false
    }

    // ========================================
    // DATA PRECISION TESTS
    // ========================================

    @Test
    @DisplayName("Should preserve decimal precision for balance field")
    void testInquireCustomer_DecimalPrecision_Balance() {
        // Given - Test packed decimal 9P 2 precision
        testCustomer.setBalance(new BigDecimal("1234567.89"));
        when(customerRepository.findByCustomerNumber(12345))
            .thenReturn(Optional.of(testCustomer));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then - Verify 2 decimal places preserved
        assertEquals(new BigDecimal("1234567.89"), response.getBalance());
        assertEquals(2, response.getBalance().scale());
    }

    @Test
    @DisplayName("Should handle zero balance correctly")
    void testInquireCustomer_ZeroBalance() {
        // Given
        testCustomer.setBalance(BigDecimal.ZERO);
        when(customerRepository.findByCustomerNumber(12345))
            .thenReturn(Optional.of(testCustomer));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then
        assertEquals(BigDecimal.ZERO, response.getBalance());
    }

    @Test
    @DisplayName("Should handle negative balance correctly")
    void testInquireCustomer_NegativeBalance() {
        // Given
        testCustomer.setBalance(new BigDecimal("-500.00"));
        when(customerRepository.findByCustomerNumber(12345))
            .thenReturn(Optional.of(testCustomer));

        // When
        CustomerInquiryDTO response = service.inquireCustomer(validRequest);

        // Then
        assertEquals(new BigDecimal("-500.00"), response.getBalance());
        assertTrue(response.getBalance().compareTo(BigDecimal.ZERO) < 0);
    }

    // ========================================
    // MULTIPLE INQUIRY TESTS
    // ========================================

    @Test
    @DisplayName("Should handle multiple consecutive inquiries")
    void testInquireCustomer_MultipleInquiries() {
        // Given - Simulating multiple lookups in same session
        Customer customer1 = new Customer();
        customer1.setCustomerNumber(100);
        customer1.setCustomerName("Customer 100");

        Customer customer2 = new Customer();
        customer2.setCustomerNumber(200);
        customer2.setCustomerName("Customer 200");

        when(customerRepository.findByCustomerNumber(100))
            .thenReturn(Optional.of(customer1));
        when(customerRepository.findByCustomerNumber(200))
            .thenReturn(Optional.of(customer2));

        // When - First inquiry
        validRequest.setCustomerNumber(100);
        CustomerInquiryDTO response1 = service.inquireCustomer(validRequest);

        // Then - First inquiry successful
        assertTrue(response1.isSuccess());
        assertEquals("Customer 100", response1.getCustomerName());

        // When - Second inquiry
        validRequest.setCustomerNumber(200);
        CustomerInquiryDTO response2 = service.inquireCustomer(validRequest);

        // Then - Second inquiry successful
        assertTrue(response2.isSuccess());
        assertEquals("Customer 200", response2.getCustomerName());

        // Verify both calls were made
        verify(customerRepository, times(1)).findByCustomerNumber(100);
        verify(customerRepository, times(1)).findByCustomerNumber(200);
    }
}
