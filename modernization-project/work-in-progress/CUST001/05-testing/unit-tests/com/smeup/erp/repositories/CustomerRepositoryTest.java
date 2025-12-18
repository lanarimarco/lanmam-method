package com.smeup.erp.repositories;

import com.smeup.erp.entities.Customer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository tests for CustomerRepository
 * Tests JPA repository operations for CUSTMAST table
 *
 * These tests use an in-memory H2 database to verify:
 * - findByCustomerNumber() custom query method
 * - existsByCustomerNumber() custom query method
 * - Standard JPA operations (findById, save, etc.)
 *
 * RPGLE operations tested:
 * - CHAIN CUSTMAST (mapped to findByCustomerNumber)
 * - %Found indicator (mapped to Optional.isPresent())
 */
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Setup and persist test customer
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

        entityManager.persist(testCustomer);
        entityManager.flush();
    }

    // ========================================
    // findByCustomerNumber() TESTS - CHAIN EQUIVALENT
    // ========================================

    @Test
    @DisplayName("findByCustomerNumber() - Should find existing customer")
    void testFindByCustomerNumber_ExistingCustomer_ReturnsCustomer() {
        // When - RPGLE: C     PCUSTNO       Chain     CUSTMAST
        Optional<Customer> result = customerRepository.findByCustomerNumber(12345);

        // Then - RPGLE: IF %Found(CUSTMAST)
        assertTrue(result.isPresent());
        assertEquals(12345, result.get().getCustomerNumber());
        assertEquals("ACME Corporation", result.get().getCustomerName());
        assertEquals("123 Main Street", result.get().getAddress1());
        assertEquals("New York", result.get().getCity());
        assertEquals("NY", result.get().getState());
        assertEquals(10001, result.get().getZipCode());
        assertEquals("212-555-1234", result.get().getPhone());
        assertEquals(new BigDecimal("1500.50"), result.get().getBalance());
        assertEquals(new BigDecimal("5000.00"), result.get().getCreditLimit());
        assertEquals(LocalDate.of(2025, 12, 15), result.get().getLastOrderDate());
    }

    @Test
    @DisplayName("findByCustomerNumber() - Should return empty for non-existent customer")
    void testFindByCustomerNumber_NonExistentCustomer_ReturnsEmpty() {
        // When - RPGLE: C     PCUSTNO       Chain     CUSTMAST
        Optional<Customer> result = customerRepository.findByCustomerNumber(99999);

        // Then - RPGLE: IF NOT %Found(CUSTMAST)
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findByCustomerNumber() - Should return empty for zero customer number")
    void testFindByCustomerNumber_ZeroCustomerNumber_ReturnsEmpty() {
        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(0);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("findByCustomerNumber() - Should handle null customer number")
    void testFindByCustomerNumber_NullCustomerNumber_ReturnsEmpty() {
        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(null);

        // Then
        assertFalse(result.isPresent());
    }

    // ========================================
    // existsByCustomerNumber() TESTS
    // ========================================

    @Test
    @DisplayName("existsByCustomerNumber() - Should return true for existing customer")
    void testExistsByCustomerNumber_ExistingCustomer_ReturnsTrue() {
        // When
        boolean exists = customerRepository.existsByCustomerNumber(12345);

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByCustomerNumber() - Should return false for non-existent customer")
    void testExistsByCustomerNumber_NonExistentCustomer_ReturnsFalse() {
        // When
        boolean exists = customerRepository.existsByCustomerNumber(99999);

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("existsByCustomerNumber() - Should return false for zero customer number")
    void testExistsByCustomerNumber_ZeroCustomerNumber_ReturnsFalse() {
        // When
        boolean exists = customerRepository.existsByCustomerNumber(0);

        // Then
        assertFalse(exists);
    }

    @Test
    @DisplayName("existsByCustomerNumber() - Should handle null customer number")
    void testExistsByCustomerNumber_NullCustomerNumber_ReturnsFalse() {
        // When
        boolean exists = customerRepository.existsByCustomerNumber(null);

        // Then
        assertFalse(exists);
    }

    // ========================================
    // STANDARD JPA TESTS
    // ========================================

    @Test
    @DisplayName("findById() - Should find customer by ID")
    void testFindById_ExistingCustomer_ReturnsCustomer() {
        // When - Using primary key
        Optional<Customer> result = customerRepository.findById(12345);

        // Then
        assertTrue(result.isPresent());
        assertEquals(12345, result.get().getCustomerNumber());
    }

    @Test
    @DisplayName("save() - Should persist new customer")
    void testSave_NewCustomer_Persisted() {
        // Given - Create new customer
        Customer newCustomer = new Customer();
        newCustomer.setCustomerNumber(54321);
        newCustomer.setCustomerName("Test Company");
        newCustomer.setBalance(new BigDecimal("1000.00"));

        // When - Save customer
        Customer savedCustomer = customerRepository.save(newCustomer);
        entityManager.flush();

        // Then - Verify it was saved
        assertNotNull(savedCustomer);
        assertEquals(54321, savedCustomer.getCustomerNumber());

        // Verify it can be retrieved
        Optional<Customer> retrieved = customerRepository.findByCustomerNumber(54321);
        assertTrue(retrieved.isPresent());
        assertEquals("Test Company", retrieved.get().getCustomerName());
    }

    // ========================================
    // BOUNDARY VALUE TESTS
    // ========================================

    @Test
    @DisplayName("Should handle maximum customer number 99999")
    void testFindByCustomerNumber_MaximumValue() {
        // Given - Create customer with max number
        Customer maxCustomer = new Customer();
        maxCustomer.setCustomerNumber(99999);
        maxCustomer.setCustomerName("Max Customer");
        entityManager.persist(maxCustomer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(99999);

        // Then
        assertTrue(result.isPresent());
        assertEquals(99999, result.get().getCustomerNumber());
        assertEquals("Max Customer", result.get().getCustomerName());
    }

    @Test
    @DisplayName("Should handle minimum customer number 1")
    void testFindByCustomerNumber_MinimumValue() {
        // Given - Create customer with min number
        Customer minCustomer = new Customer();
        minCustomer.setCustomerNumber(1);
        minCustomer.setCustomerName("First Customer");
        entityManager.persist(minCustomer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(1);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getCustomerNumber());
        assertEquals("First Customer", result.get().getCustomerName());
    }

    // ========================================
    // DATA INTEGRITY TESTS
    // ========================================

    @Test
    @DisplayName("Should preserve decimal precision for balance")
    void testDecimalPrecision_Balance() {
        // Given - Customer with specific balance
        Customer customer = new Customer();
        customer.setCustomerNumber(11111);
        customer.setCustomerName("Decimal Test");
        customer.setBalance(new BigDecimal("1234567.89"));
        entityManager.persist(customer);
        entityManager.flush();

        // When - Retrieve customer
        Optional<Customer> result = customerRepository.findByCustomerNumber(11111);

        // Then - Verify decimal precision preserved (9P 2)
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("1234567.89"), result.get().getBalance());
        assertEquals(2, result.get().getBalance().scale());
    }

    @Test
    @DisplayName("Should preserve decimal precision for credit limit")
    void testDecimalPrecision_CreditLimit() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerNumber(22222);
        customer.setCustomerName("Credit Test");
        customer.setCreditLimit(new BigDecimal("9999999.99"));
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(22222);

        // Then - Verify decimal precision preserved (9P 2)
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("9999999.99"), result.get().getCreditLimit());
        assertEquals(2, result.get().getCreditLimit().scale());
    }

    @Test
    @DisplayName("Should handle zero balance")
    void testZeroBalance() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerNumber(33333);
        customer.setCustomerName("Zero Balance");
        customer.setBalance(BigDecimal.ZERO);
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(33333);

        // Then
        assertTrue(result.isPresent());
        assertEquals(BigDecimal.ZERO, result.get().getBalance());
    }

    @Test
    @DisplayName("Should handle negative balance")
    void testNegativeBalance() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerNumber(44444);
        customer.setCustomerName("Negative Balance");
        customer.setBalance(new BigDecimal("-500.00"));
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(44444);

        // Then
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("-500.00"), result.get().getBalance());
        assertTrue(result.get().getBalance().compareTo(BigDecimal.ZERO) < 0);
    }

    // ========================================
    // DATE HANDLING TESTS
    // ========================================

    @Test
    @DisplayName("Should preserve date values correctly")
    void testDateHandling() {
        // Given - Date from RPGLE LASTORDER field (8P 0 YYYYMMDD format)
        Customer customer = new Customer();
        customer.setCustomerNumber(55555);
        customer.setCustomerName("Date Test");
        customer.setLastOrderDate(LocalDate.of(2025, 12, 15));
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(55555);

        // Then
        assertTrue(result.isPresent());
        assertEquals(LocalDate.of(2025, 12, 15), result.get().getLastOrderDate());
    }

    @Test
    @DisplayName("Should handle null date values")
    void testNullDate() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerNumber(66666);
        customer.setCustomerName("No Order Date");
        customer.setLastOrderDate(null);
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(66666);

        // Then
        assertTrue(result.isPresent());
        assertNull(result.get().getLastOrderDate());
    }

    // ========================================
    // STRING FIELD TESTS
    // ========================================

    @Test
    @DisplayName("Should handle long customer names")
    void testLongCustomerName() {
        // Given - Max length is 30 characters
        String longName = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123";
        Customer customer = new Customer();
        customer.setCustomerNumber(77777);
        customer.setCustomerName(longName);
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(77777);

        // Then
        assertTrue(result.isPresent());
        assertEquals(longName, result.get().getCustomerName());
    }

    @Test
    @DisplayName("Should handle 2-character state codes")
    void testStateCode() {
        // Given - State is 2 characters
        Customer customer = new Customer();
        customer.setCustomerNumber(88888);
        customer.setCustomerName("State Test");
        customer.setState("CA");
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(88888);

        // Then
        assertTrue(result.isPresent());
        assertEquals("CA", result.get().getState());
        assertEquals(2, result.get().getState().length());
    }

    @Test
    @DisplayName("Should handle 5-digit zip codes")
    void testZipCode() {
        // Given - ZIP is 5P 0
        Customer customer = new Customer();
        customer.setCustomerNumber(99998);
        customer.setCustomerName("Zip Test");
        customer.setZipCode(90210);
        entityManager.persist(customer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(99998);

        // Then
        assertTrue(result.isPresent());
        assertEquals(90210, result.get().getZipCode());
    }

    // ========================================
    // MULTIPLE CUSTOMER TESTS
    // ========================================

    @Test
    @DisplayName("Should handle multiple customers correctly")
    void testMultipleCustomers() {
        // Given - Multiple customers
        Customer customer2 = new Customer();
        customer2.setCustomerNumber(11111);
        customer2.setCustomerName("Customer Two");
        entityManager.persist(customer2);

        Customer customer3 = new Customer();
        customer3.setCustomerNumber(22222);
        customer3.setCustomerName("Customer Three");
        entityManager.persist(customer3);

        entityManager.flush();

        // When - Find each customer
        Optional<Customer> result1 = customerRepository.findByCustomerNumber(12345);
        Optional<Customer> result2 = customerRepository.findByCustomerNumber(11111);
        Optional<Customer> result3 = customerRepository.findByCustomerNumber(22222);

        // Then - All should be found
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertTrue(result3.isPresent());

        assertEquals("ACME Corporation", result1.get().getCustomerName());
        assertEquals("Customer Two", result2.get().getCustomerName());
        assertEquals("Customer Three", result3.get().getCustomerName());
    }
}
