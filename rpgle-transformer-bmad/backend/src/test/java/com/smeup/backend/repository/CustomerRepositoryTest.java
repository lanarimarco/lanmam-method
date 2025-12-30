package com.smeup.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.smeup.backend.entity.Customer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/**
 * Unit tests for CustomerRepository.
 *
 * <p>Tests verify that Spring Data JPA correctly implements the repository
 * interface methods, particularly the custom findByCustomerId() method
 * that maps to RPGLE CHAIN operation.
 *
 * <p>Uses @DataJpaTest for repository-only testing with H2 in-memory database.
 * Flyway migrations run automatically to create the CUSTMAST table.
 */
@DataJpaTest
class CustomerRepositoryTest {

  @Autowired
  private CustomerRepository customerRepository;

  private Customer testCustomer;

  @BeforeEach
  void setUp() {
    // Clean up before each test
    customerRepository.deleteAll();

    // Create a test customer matching DDS field structure
    testCustomer = new Customer();
    testCustomer.setCustomerId(12345L);
    testCustomer.setCustomerName("ACME Corporation");
    testCustomer.setAddressLine1("123 Main Street");
    testCustomer.setCity("Springfield");
    testCustomer.setState("IL");
    testCustomer.setZipCode(62701);
    testCustomer.setPhoneNumber("555-123-4567");
    testCustomer.setAccountBalance(new BigDecimal("1500.00"));
    testCustomer.setCreditLimit(new BigDecimal("10000.00"));
    testCustomer.setLastOrderDate(20251215);
  }

  @Nested
  @DisplayName("save() - RPGLE WRITE/UPDATE equivalent")
  class SaveTests {

    @Test
    @DisplayName("should save new customer to CUSTMAST table")
    void shouldSaveNewCustomer() {
      // When - equivalent to RPGLE WRITE CUSTMAST
      Customer saved = customerRepository.save(testCustomer);

      // Then
      assertThat(saved).isNotNull();
      assertThat(saved.getCustomerId()).isEqualTo(12345L);
      assertThat(customerRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("should update existing customer")
    void shouldUpdateExistingCustomer() {
      // Given - save initial customer
      customerRepository.save(testCustomer);

      // When - update customer name (equivalent to RPGLE UPDATE CUSTMAST)
      testCustomer.setCustomerName("ACME Corp Updated");
      Customer updated = customerRepository.save(testCustomer);

      // Then
      assertThat(updated.getCustomerName()).isEqualTo("ACME Corp Updated");
      assertThat(customerRepository.count()).isEqualTo(1);
    }
  }

  @Nested
  @DisplayName("findByCustomerId() - RPGLE CHAIN equivalent")
  class FindByCustomerIdTests {

    @Test
    @DisplayName("should find customer by ID - equivalent to CHAIN with %FOUND=*ON")
    void shouldFindCustomerById() {
      // Given - customer exists in CUSTMAST
      customerRepository.save(testCustomer);

      // When - equivalent to: CHAIN CUSTMAST using CUSTNO key
      Optional<Customer> found = customerRepository.findByCustomerId(12345L);

      // Then - %FOUND(CUSTMAST) = *ON
      assertThat(found).isPresent();
      assertThat(found.get().getCustomerName()).isEqualTo("ACME Corporation");
      assertThat(found.get().getCity()).isEqualTo("Springfield");
    }

    @Test
    @DisplayName("should return empty when customer not found - equivalent to %FOUND=*OFF")
    void shouldReturnEmptyWhenNotFound() {
      // Given - customer does NOT exist in CUSTMAST

      // When - equivalent to: CHAIN CUSTMAST with non-existent key
      Optional<Customer> found = customerRepository.findByCustomerId(99999L);

      // Then - %FOUND(CUSTMAST) = *OFF
      assertThat(found).isEmpty();
    }
  }

  @Nested
  @DisplayName("findByCustomerNameContainingIgnoreCase() - Search functionality")
  class FindByCustomerNameTests {

    @Test
    @DisplayName("should find customers by partial name match")
    void shouldFindByPartialName() {
      // Given - multiple customers
      customerRepository.save(testCustomer);

      Customer customer2 = new Customer();
      customer2.setCustomerId(12346L);
      customer2.setCustomerName("ACME Industries");
      customerRepository.save(customer2);

      Customer customer3 = new Customer();
      customer3.setCustomerId(12347L);
      customer3.setCustomerName("Beta Company");
      customerRepository.save(customer3);

      // When - search for "ACME"
      List<Customer> found = customerRepository
          .findByCustomerNameContainingIgnoreCase("ACME");

      // Then - should find both ACME customers
      assertThat(found).hasSize(2);
      assertThat(found).extracting(Customer::getCustomerName)
          .containsExactlyInAnyOrder("ACME Corporation", "ACME Industries");
    }

    @Test
    @DisplayName("should be case insensitive")
    void shouldBeCaseInsensitive() {
      // Given
      customerRepository.save(testCustomer);

      // When - search with lowercase
      List<Customer> found = customerRepository
          .findByCustomerNameContainingIgnoreCase("acme");

      // Then
      assertThat(found).hasSize(1);
      assertThat(found.get(0).getCustomerName()).isEqualTo("ACME Corporation");
    }

    @Test
    @DisplayName("should return empty list when no match")
    void shouldReturnEmptyListWhenNoMatch() {
      // Given
      customerRepository.save(testCustomer);

      // When
      List<Customer> found = customerRepository
          .findByCustomerNameContainingIgnoreCase("NonExistent");

      // Then
      assertThat(found).isEmpty();
    }
  }

  @Nested
  @DisplayName("findById() - JpaRepository inherited method")
  class FindByIdTests {

    @Test
    @DisplayName("should find by primary key")
    void shouldFindByPrimaryKey() {
      // Given
      customerRepository.save(testCustomer);

      // When - using inherited JpaRepository method
      Optional<Customer> found = customerRepository.findById(12345L);

      // Then
      assertThat(found).isPresent();
      assertThat(found.get().getCustomerId()).isEqualTo(12345L);
    }
  }

  @Nested
  @DisplayName("deleteById() - RPGLE DELETE equivalent")
  class DeleteTests {

    @Test
    @DisplayName("should delete customer by ID")
    void shouldDeleteById() {
      // Given
      customerRepository.save(testCustomer);
      assertThat(customerRepository.count()).isEqualTo(1);

      // When - equivalent to RPGLE DELETE CUSTMAST
      customerRepository.deleteById(12345L);

      // Then
      assertThat(customerRepository.count()).isZero();
      assertThat(customerRepository.findByCustomerId(12345L)).isEmpty();
    }
  }

  @Nested
  @DisplayName("findAll() - RPGLE READ equivalent")
  class FindAllTests {

    @Test
    @DisplayName("should return all customers")
    void shouldReturnAllCustomers() {
      // Given - multiple customers
      customerRepository.save(testCustomer);

      Customer customer2 = new Customer();
      customer2.setCustomerId(12346L);
      customer2.setCustomerName("Beta Company");
      customerRepository.save(customer2);

      // When - equivalent to RPGLE READ loop
      List<Customer> all = customerRepository.findAll();

      // Then
      assertThat(all).hasSize(2);
    }
  }
}
