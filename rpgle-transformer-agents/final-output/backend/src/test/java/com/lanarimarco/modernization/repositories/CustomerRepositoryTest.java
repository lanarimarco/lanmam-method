package com.lanarimarco.modernization.repositories;

import com.lanarimarco.modernization.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Repository tests for CustomerRepository
 *
 * Tests JPA repository methods against an in-memory H2 database
 * Uses @DataJpaTest for Spring Data JPA testing support
 */
@DataJpaTest
@DisplayName("Customer Repository Tests")
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;
    private static final Integer TEST_CUSTOMER_NUMBER = 12345;

    @BeforeEach
    void setUp() {
        // Create and persist test customer
        testCustomer = Customer.builder()
                .customerNumber(TEST_CUSTOMER_NUMBER)
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

        entityManager.persist(testCustomer);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should find customer by customer number")
    void testFindByCustomerNumber_Found() {
        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(TEST_CUSTOMER_NUMBER);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerNumber()).isEqualTo(TEST_CUSTOMER_NUMBER);
        assertThat(result.get().getCustomerName()).isEqualTo("John Doe");
        assertThat(result.get().getAddress1()).isEqualTo("123 Main Street");
        assertThat(result.get().getCity()).isEqualTo("Springfield");
        assertThat(result.get().getState()).isEqualTo("IL");
        assertThat(result.get().getZipCode()).isEqualTo(62701);
        assertThat(result.get().getPhoneNumber()).isEqualTo("555-1234");
        assertThat(result.get().getBalance()).isEqualByComparingTo(new BigDecimal("1250.75"));
    }

    @Test
    @DisplayName("Should return empty when customer not found")
    void testFindByCustomerNumber_NotFound() {
        // Given
        Integer nonexistentCustomerNumber = 99999;

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(nonexistentCustomerNumber);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return true when customer exists")
    void testExistsByCustomerNumber_Exists() {
        // When
        boolean exists = customerRepository.existsByCustomerNumber(TEST_CUSTOMER_NUMBER);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when customer does not exist")
    void testExistsByCustomerNumber_NotExists() {
        // Given
        Integer nonexistentCustomerNumber = 99999;

        // When
        boolean exists = customerRepository.existsByCustomerNumber(nonexistentCustomerNumber);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find customer by ID using inherited method")
    void testFindById_Found() {
        // When
        Optional<Customer> result = customerRepository.findById(TEST_CUSTOMER_NUMBER);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerNumber()).isEqualTo(TEST_CUSTOMER_NUMBER);
    }

    @Test
    @DisplayName("Should handle customer with null optional fields")
    void testFindByCustomerNumber_NullFields() {
        // Given
        Customer customerWithNulls = Customer.builder()
                .customerNumber(54321)
                .customerName("Jane Doe")
                .address1(null)
                .city(null)
                .state(null)
                .zipCode(null)
                .phoneNumber(null)
                .balance(BigDecimal.ZERO)
                .build();

        entityManager.persist(customerWithNulls);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(54321);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerName()).isEqualTo("Jane Doe");
        assertThat(result.get().getAddress1()).isNull();
        assertThat(result.get().getCity()).isNull();
        assertThat(result.get().getState()).isNull();
        assertThat(result.get().getZipCode()).isNull();
        assertThat(result.get().getPhoneNumber()).isNull();
    }

    @Test
    @DisplayName("Should persist and retrieve customer with BigDecimal balance")
    void testBigDecimalPrecision() {
        // Given
        BigDecimal preciseBalance = new BigDecimal("12345.67");
        Customer customerWithPreciseBalance = Customer.builder()
                .customerNumber(11111)
                .customerName("Test Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(12345)
                .phoneNumber("123-4567")
                .balance(preciseBalance)
                .build();

        entityManager.persist(customerWithPreciseBalance);
        entityManager.flush();
        entityManager.clear(); // Clear persistence context to ensure fresh read

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(11111);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getBalance()).isEqualByComparingTo(preciseBalance);
        assertThat(result.get().getBalance().scale()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should handle minimum customer number")
    void testFindByCustomerNumber_MinimumNumber() {
        // Given
        Customer minCustomer = Customer.builder()
                .customerNumber(1)
                .customerName("Min Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(12345)
                .phoneNumber("000-0000")
                .balance(BigDecimal.ZERO)
                .build();

        entityManager.persist(minCustomer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle maximum customer number")
    void testFindByCustomerNumber_MaximumNumber() {
        // Given
        Customer maxCustomer = Customer.builder()
                .customerNumber(99999)
                .customerName("Max Customer")
                .address1("Address")
                .city("City")
                .state("ST")
                .zipCode(99999)
                .phoneNumber("999-9999")
                .balance(new BigDecimal("9999999.99"))
                .build();

        entityManager.persist(maxCustomer);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(99999);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerNumber()).isEqualTo(99999);
    }

    @Test
    @DisplayName("Should count all customers")
    void testCount() {
        // When
        long count = customerRepository.count();

        // Then
        assertThat(count).isGreaterThanOrEqualTo(1); // At least the test customer
    }

    @Test
    @DisplayName("Should handle special characters in customer data")
    void testFindByCustomerNumber_SpecialCharacters() {
        // Given
        Customer customerWithSpecialChars = Customer.builder()
                .customerNumber(77777)
                .customerName("O'Brien & Sons, Inc.")
                .address1("123 Main St., Apt #5")
                .city("Springfield")
                .state("IL")
                .zipCode(62701)
                .phoneNumber("(555) 123-4567")
                .balance(new BigDecimal("1000.00"))
                .build();

        entityManager.persist(customerWithSpecialChars);
        entityManager.flush();

        // When
        Optional<Customer> result = customerRepository.findByCustomerNumber(77777);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerName()).isEqualTo("O'Brien & Sons, Inc.");
        assertThat(result.get().getAddress1()).isEqualTo("123 Main St., Apt #5");
        assertThat(result.get().getPhoneNumber()).isEqualTo("(555) 123-4567");
    }
}
