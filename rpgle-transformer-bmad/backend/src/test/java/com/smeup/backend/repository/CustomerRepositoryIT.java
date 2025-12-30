package com.smeup.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.smeup.backend.AbstractIntegrationTest;
import com.smeup.backend.entity.Customer;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Integration tests for CustomerRepository with real PostgreSQL database.
 *
 * <p>Uses Testcontainers to run tests against actual PostgreSQL,
 * verifying that the repository correctly persists to CUSTMAST table
 * with DDS column names (CUSTNO, CUSTNAME, etc.).
 */
class CustomerRepositoryIT extends AbstractIntegrationTest {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    // Clean up CUSTMAST table before each test
    jdbcTemplate.update("DELETE FROM CUSTMAST");
  }

  @Test
  @DisplayName("should persist customer to CUSTMAST table with DDS column names")
  void shouldPersistToCorrectTable() {
    // Given
    Customer customer = createTestCustomer(12345L, "ACME Corporation");

    // When
    customerRepository.save(customer);

    // Then - verify data is in CUSTMAST table with correct column names
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM CUSTMAST WHERE CUSTNO = ?",
        Integer.class,
        12345L
    );
    assertThat(count).isEqualTo(1);
  }

  @Test
  @DisplayName("should store all DDS fields correctly")
  void shouldStoreAllDdsFields() {
    // Given
    Customer customer = createTestCustomer(12345L, "ACME Corporation");
    customer.setAddressLine1("123 Main Street");
    customer.setCity("Springfield");
    customer.setState("IL");
    customer.setZipCode(62701);
    customer.setPhoneNumber("555-123-4567");
    customer.setAccountBalance(new BigDecimal("1500.00"));
    customer.setCreditLimit(new BigDecimal("10000.00"));
    customer.setLastOrderDate(20251215);

    // When
    customerRepository.save(customer);

    // Then - verify all DDS columns are populated correctly
    var result = jdbcTemplate.queryForMap(
        "SELECT CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER "
            + "FROM CUSTMAST WHERE CUSTNO = ?",
        12345L
    );

    assertThat(result.get("custno")).isEqualTo(12345L);
    assertThat(result.get("custname")).isEqualTo("ACME Corporation");
    assertThat(result.get("addr1")).isEqualTo("123 Main Street");
    assertThat(result.get("city")).isEqualTo("Springfield");
    assertThat(result.get("state")).isEqualTo("IL");
    assertThat(result.get("zip")).isEqualTo(62701);
    assertThat(result.get("phone")).isEqualTo("555-123-4567");
    assertThat(((BigDecimal) result.get("balance")).compareTo(new BigDecimal("1500.00")))
        .isZero();
    assertThat(((BigDecimal) result.get("creditlim")).compareTo(new BigDecimal("10000.00")))
        .isZero();
    assertThat(result.get("lastorder")).isEqualTo(20251215);
  }

  @Test
  @DisplayName("findByCustomerId should work with PostgreSQL - CHAIN equivalent")
  void findByCustomerIdShouldWork() {
    // Given - insert directly via JDBC to verify repository reads correctly
    jdbcTemplate.update(
        "INSERT INTO CUSTMAST (CUSTNO, CUSTNAME) VALUES (?, ?)",
        99999L,
        "Direct Insert Customer"
    );

    // When - use repository to find (CHAIN CUSTMAST equivalent)
    Optional<Customer> found = customerRepository.findByCustomerId(99999L);

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getCustomerName()).isEqualTo("Direct Insert Customer");
  }

  @Test
  @DisplayName("should handle BigDecimal precision correctly for financial fields")
  void shouldHandleBigDecimalPrecision() {
    // Given - test precise decimal values matching DDS 9P 2 spec
    Customer customer = createTestCustomer(12345L, "Precision Test");
    customer.setAccountBalance(new BigDecimal("9999999.99")); // Max for 9P 2
    customer.setCreditLimit(new BigDecimal("1234567.89"));

    // When
    customerRepository.save(customer);
    Optional<Customer> found = customerRepository.findByCustomerId(12345L);

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getAccountBalance())
        .isEqualByComparingTo(new BigDecimal("9999999.99"));
    assertThat(found.get().getCreditLimit())
        .isEqualByComparingTo(new BigDecimal("1234567.89"));
  }

  private Customer createTestCustomer(Long id, String name) {
    Customer customer = new Customer();
    customer.setCustomerId(id);
    customer.setCustomerName(name);
    return customer;
  }
}
