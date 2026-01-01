package com.smeup.backend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smeup.backend.AbstractIntegrationTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Full-stack integration tests for Customer API endpoints.
 *
 * <p>Tests the complete request/response cycle:
 * REST API → Controller → Service → Repository → PostgreSQL (Testcontainers)
 *
 * <p>RPGLE Program: CUST001 - Customer Inquiry
 *
 * <p>Validates:
 * - API endpoints return correct data
 * - Data persists to CUSTMAST table with DDS column names
 * - Error handling through full stack (404, 400)
 * - Response format (ApiResponse wrapper, RFC 7807 errors)
 *
 * <p>Uses AbstractIntegrationTest for Testcontainers PostgreSQL setup.
 * Flyway migrations run automatically before tests.
 *
 * <p>Story: 4.2 - Create Backend Integration Tests with Testcontainers
 */
class CustomerIntegrationIT extends AbstractIntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    // Initialize MockMvc with the full Spring context
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    // Clean CUSTMAST table before each test - ensures test isolation
    // DDS Table: CUSTMAST
    jdbcTemplate.update("DELETE FROM CUSTMAST");
  }

  /**
   * Test end-to-end customer retrieval through full stack.
   *
   * <p>RPGLE Operation: CHAIN (CUSTID) CUSTMAST → %FOUND → Display customer details
   *
   * <p>Full Stack Flow:
   * HTTP GET → CustomerController → CustomerService → CustomerRepository → PostgreSQL →
   * CustomerMapper → ApiResponse<CustomerDTO> → JSON
   *
   * <p>Validates:
   * - AC #1: Integration test exists
   * - AC #2: Testcontainers PostgreSQL used (via AbstractIntegrationTest)
   * - AC #3: Customer entity persists to CUSTMAST
   * - AC #4: API endpoint returns correct data
   * - AC #5: SQL uses DDS column names (CUSTNO, CUSTNAME, etc.)
   */
  @Test
  @DisplayName("should return customer with 200 OK when valid customer ID exists - RPGLE: CHAIN %FOUND")
  void shouldReturnCustomerWhenValidId() throws Exception {
    // Given - insert customer directly via JDBC (simulating existing CUSTMAST data)
    // DDS Columns: CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER
    jdbcTemplate.update(
        "INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        12345L,                         // CUSTNO (5P 0)
        "ACME Corporation",             // CUSTNAME (30A)
        "123 Main Street",              // ADDR1 (30A)
        "Springfield",                  // CITY (20A)
        "IL",                           // STATE (2A)
        62701,                          // ZIP (5P 0)
        "555-123-4567",                 // PHONE (12A)
        new BigDecimal("1500.00"),      // BALANCE (9P 2)
        new BigDecimal("10000.00"),     // CREDITLIM (9P 2)
        20251215                        // LASTORDER (8P 0)
    );

    // When - call API endpoint (RPGLE: User enters CUSTNO on PROMPT screen)
    // Then - verify response (RPGLE: Display DETAIL screen with customer data)
    mockMvc.perform(get("/api/v1/customers/12345"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").exists())
        .andExpect(jsonPath("$.data.customerId").value(12345))
        .andExpect(jsonPath("$.data.customerName").value("ACME Corporation"))
        .andExpect(jsonPath("$.data.addressLine1").value("123 Main Street"))
        .andExpect(jsonPath("$.data.city").value("Springfield"))
        .andExpect(jsonPath("$.data.state").value("IL"))
        .andExpect(jsonPath("$.data.zipCode").value(62701))
        .andExpect(jsonPath("$.data.phoneNumber").value("555-123-4567"))
        .andExpect(jsonPath("$.data.accountBalance").value(1500.00))
        .andExpect(jsonPath("$.data.creditLimit").value(10000.00))
        .andExpect(jsonPath("$.data.lastOrderDate").value(20251215));

    // Verify data still persisted in CUSTMAST with DDS column names (AC #5)
    var dbResult = jdbcTemplate.queryForMap(
        "SELECT CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE "
            + "FROM CUSTMAST WHERE CUSTNO = ?",
        12345L
    );
    assertThat(dbResult.get("custno")).isEqualTo(12345L);
    assertThat(dbResult.get("custname")).isEqualTo("ACME Corporation");
  }

  /**
   * Test customer not found error handling through full stack.
   *
   * <p>RPGLE Operation: CHAIN (CUSTID) CUSTMAST → NOT %FOUND → Error indicator *IN90 → Display error
   *
   * <p>Full Stack Flow:
   * HTTP GET → CustomerController → CustomerService.findCustomerById() →
   * Repository returns Optional.empty() → throw CustomerNotFoundException →
   * GlobalExceptionHandler → RFC 7807 ProblemDetail → HTTP 404
   *
   * <p>Validates:
   * - AC #4: API endpoint error handling works
   * - RFC 7807 Problem Details format
   * - Exception propagation through all layers
   */
  @Test
  @DisplayName("should return 404 NOT FOUND when customer does not exist - RPGLE: CHAIN NOT %FOUND")
  void shouldReturn404WhenCustomerNotFound() throws Exception {
    // Given - CUSTMAST table is empty (no customer 99999)
    // (setUp() already cleaned the table)

    // When - GET non-existent customer (RPGLE: CHAIN to non-existent record)
    // Then - verify 404 with RFC 7807 Problem Details format (RPGLE: Error indicator *IN90)
    mockMvc.perform(get("/api/v1/customers/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.title").value("Customer Not Found"))
        .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("99999")));
  }

  /**
   * Test invalid customer ID validation through full stack.
   *
   * <p>RPGLE Operation: CUSTNO IFEQ *ZEROS → Error indicator *IN90 → Display error
   *
   * <p>Full Stack Flow:
   * HTTP GET → CustomerController → CustomerService.findCustomerById() →
   * validateCustomerId() checks customerId <= 0 → throw InvalidCustomerIdException →
   * GlobalExceptionHandler → RFC 7807 ProblemDetail → HTTP 400
   *
   * <p>Validates:
   * - AC #4: API endpoint validation works
   * - DDS field constraint enforcement (5P 0 cannot be zero or negative)
   * - Exception propagation through all layers
   *
   * <p>Story 4.1 Learning: CustomerService validates customerId > 0 and <= 99999 (5P 0 max)
   */
  @Test
  @DisplayName("should return 400 BAD REQUEST when customer ID is zero - RPGLE: CUSTNO IFEQ *ZEROS")
  void shouldReturn400WhenCustomerIdIsZero() throws Exception {
    // When - GET with invalid customer ID (RPGLE: CUSTNO = 0)
    // Then - verify 400 with RFC 7807 Problem Details format
    mockMvc.perform(get("/api/v1/customers/0"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.title").value("Invalid Customer ID"))
        .andExpect(jsonPath("$.detail").value("Customer ID must be a positive number"));
  }

  /**
   * Test invalid customer ID exceeding DDS field size.
   *
   * <p>RPGLE: DDS field CUSTNO is 5P 0 → max value 99999
   * Java Long can hold much larger values, but we must respect RPGLE constraints
   *
   * <p>Story 4.1 Code Review: Added validation for customerId > 99999
   *
   * <p>Validates:
   * - DDS field size boundary enforcement
   * - Functional equivalence to RPGLE (cannot store values > 99999)
   */
  @Test
  @DisplayName("should return 400 BAD REQUEST when customer ID exceeds DDS field size - RPGLE: 5P 0 max 99999")
  void shouldReturn400WhenCustomerIdExceedsDdsFieldSize() throws Exception {
    // When - GET with customer ID exceeding DDS 5P 0 limit
    // Then - verify 400 with RFC 7807 Problem Details format
    mockMvc.perform(get("/api/v1/customers/100000"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.title").value("Invalid Customer ID"))
        .andExpect(jsonPath("$.detail").value("Customer ID must be a positive number"));
  }

  /**
   * Test minimal customer data retrieval (only required fields).
   *
   * <p>Validates that optional DDS fields can be null/empty and API still works.
   *
   * <p>DDS Required Fields: CUSTNO, CUSTNAME
   * DDS Optional Fields: ADDR1, CITY, STATE, ZIP, PHONE, BALANCE, CREDITLIM, LASTORDER
   *
   * <p>Validates:
   * - AC #3: Entity persists correctly with nullable fields
   * - AC #4: API returns correct data even with minimal fields
   */
  @Test
  @DisplayName("should return customer with 200 OK when only required fields are populated")
  void shouldReturnCustomerWithMinimalData() throws Exception {
    // Given - insert customer with only required fields (CUSTNO, CUSTNAME)
    jdbcTemplate.update(
        "INSERT INTO CUSTMAST (CUSTNO, CUSTNAME) VALUES (?, ?)",
        54321L,
        "Minimal Data Corp"
    );

    // When - call API endpoint
    // Then - verify response with nullable optional fields
    mockMvc.perform(get("/api/v1/customers/54321"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.customerId").value(54321))
        .andExpect(jsonPath("$.data.customerName").value("Minimal Data Corp"))
        .andExpect(jsonPath("$.data.addressLine1").doesNotExist())
        .andExpect(jsonPath("$.data.city").doesNotExist())
        .andExpect(jsonPath("$.data.state").doesNotExist())
        .andExpect(jsonPath("$.data.zipCode").doesNotExist())
        .andExpect(jsonPath("$.data.phoneNumber").doesNotExist())
        .andExpect(jsonPath("$.data.accountBalance").doesNotExist())
        .andExpect(jsonPath("$.data.creditLimit").doesNotExist())
        .andExpect(jsonPath("$.data.lastOrderDate").doesNotExist());
  }
}
