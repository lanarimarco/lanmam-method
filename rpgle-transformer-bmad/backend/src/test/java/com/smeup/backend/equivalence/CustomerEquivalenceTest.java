package com.smeup.backend.equivalence;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.smeup.backend.dto.ApiResponse;
import com.smeup.backend.dto.CustomerDTO;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Functional Equivalence Validation Test for Customer Inquiry.
 *
 * <p>Validates 100% functional equivalence to RPGLE CUST001 behavior (NFR1 compliance).
 * Uses parameterized testing with CSV test data to ensure all business logic,
 * data validation, and error handling matches the original RPGLE program.
 *
 * <p>Original RPGLE Program: source-rpgle/programs/CUST001.rpgle
 * Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 *
 * <p>Validates RPGLE CHAIN operation on CUSTMAST file.
 * Original: C CUSTID CHAIN CUSTMAST
 *
 * <p>Test Data: backend/src/test/resources/equivalence-test-data/
 *   customer-inquiry-test-cases.csv
 *
 * <p>Test Framework: JUnit 5 + Testcontainers + Jackson CSV
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("Customer Inquiry Functional Equivalence Tests")
public class CustomerEquivalenceTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @LocalServerPort
  private int port;

  private RestTemplate restTemplate = new RestTemplate();

  private static List<CustomerTestCase> testCases;
  private static final List<EquivalenceResult> testResults = new ArrayList<>();

  /**
   * Load all test cases from CSV before running tests.
   * Validates test data file exists and is parseable.
   */
  @BeforeAll
  static void loadTestData() {
    testCases = TestDataLoader.loadCustomerTestCases();
    assertNotNull(testCases, "Test cases should be loaded");
    assertTrue(testCases.size() > 0, "At least one test case should exist");
    System.out.println("Loaded " + testCases.size() + " test cases for equivalence validation");
  }

  /**
   * Generate validation report after all tests complete.
   * Report shows overall pass/fail and detailed failure analysis.
   */
  @AfterAll
  static void generateValidationReport() {
    Path reportPath = EquivalenceReportGenerator.generateReport(testResults);
    System.out.println("Equivalence validation report: " + reportPath.toAbsolutePath());
  }

  /**
   * Provides test cases as stream for parameterized testing.
   */
  static Stream<CustomerTestCase> provideTestCases() {
    return testCases.stream();
  }

  /**
   * Parameterized test executing customer inquiry for each test case.
   *
   * <p>Validates RPGLE CUSTMAST field mapping equivalence.
   * For positive cases (200 OK), validates all CustomerDTO fields.
   * For negative cases (404/400), validates HTTP status code.
   *
   * <p>Uses assertAll() to report all field mismatches in a single test execution.
   *
   * @param testCase The test case containing input and expected output
   */
  @ParameterizedTest(name = "{0}")
  @MethodSource("provideTestCases")
  @DisplayName("Customer Equivalence Test")
  void testCustomerEquivalence(CustomerTestCase testCase) {
    // Call GET /api/v1/customers/{customerId}
    String url = "http://localhost:" + port + "/api/v1/customers/" + testCase.customerId();

    EquivalenceResult result = executeTestCase(url, testCase);

    // Collect result for report generation
    testResults.add(result);
  }

  private EquivalenceResult executeTestCase(String url, CustomerTestCase testCase) {
    try {
      ResponseEntity<ApiResponse<CustomerDTO>> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {});

      HttpStatus actualStatus = (HttpStatus) response.getStatusCode();
      int actualStatusCode = actualStatus.value();

      if (testCase.isPositiveCase()) {
        // Positive test case: validate 200 OK and CustomerDTO fields
        assertEquals(HttpStatus.OK.value(), actualStatusCode,
            "Expected HTTP 200 for test case: " + testCase.testCaseId());

        ApiResponse<CustomerDTO> apiResponse = response.getBody();
        assertNotNull(apiResponse, "API response should not be null");

        CustomerDTO actualCustomer = apiResponse.getData();
        assertNotNull(actualCustomer, "Customer data should not be null");

        // Field-by-field comparison
        EquivalenceResult result = EquivalenceValidator.compareCustomerResponse(
            actualCustomer, testCase);

        // Use assertAll to report all field mismatches
        assertAll("Customer field validation for " + testCase.testCaseId(),
            () -> assertTrue(result.isPassed(),
                () -> "Equivalence validation failed. Differences: "
                    + String.join(", ", result.getDifferences()))
        );

        return result;

      } else {
        throw new IllegalStateException(
            "Expected error response but got success: " + testCase.testCaseId());
      }

    } catch (HttpClientErrorException e) {
      // Handle HTTP error responses (404, 400, etc.)
      int actualStatusCode = e.getStatusCode().value();

      if (testCase.isNegativeCase()) {
        // Negative test case: validate error status code
        EquivalenceResult result = EquivalenceValidator.validateStatusCode(
            actualStatusCode, testCase);

        assertEquals(testCase.expectedStatusCode(), actualStatusCode,
            "Expected HTTP " + testCase.expectedStatusCode()
                + " for test case: " + testCase.testCaseId());

        return result;

      } else {
        throw new IllegalStateException(
            "Expected success response but got error: " + testCase.testCaseId(), e);
      }
    }
  }
}
