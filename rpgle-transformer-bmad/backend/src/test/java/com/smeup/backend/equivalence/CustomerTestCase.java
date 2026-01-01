package com.smeup.backend.equivalence;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * Test case record for functional equivalence validation.
 * Maps to customer-inquiry-test-cases.csv schema.
 *
 * <p>Validates RPGLE CHAIN operation on CUSTMAST file.
 * Original: C CUSTID CHAIN CUSTMAST
 *
 * <p>Each test case represents an input customer ID and expected API response.
 * Used for parameterized equivalence testing to validate 100% functional
 * equivalence to original RPGLE behavior (NFR1 compliance).
 */
public record CustomerTestCase(
    @JsonProperty("test_case_id") String testCaseId,
    @JsonProperty("customer_id") Long customerId,
    @JsonProperty("expected_name") String expectedName,
    @JsonProperty("expected_address") String expectedAddress,
    @JsonProperty("expected_city") String expectedCity,
    @JsonProperty("expected_state") String expectedState,
    @JsonProperty("expected_zip") Integer expectedZip,
    @JsonProperty("expected_phone") String expectedPhone,
    @JsonProperty("expected_balance") BigDecimal expectedBalance,
    @JsonProperty("expected_credit_limit") BigDecimal expectedCreditLimit,
    @JsonProperty("expected_last_order") Integer expectedLastOrder,
    @JsonProperty("expected_status_code") Integer expectedStatusCode) {

  /**
   * Returns true if this test case expects a successful (200 OK) response.
   */
  public boolean isPositiveCase() {
    return expectedStatusCode != null && expectedStatusCode == 200;
  }

  /**
   * Returns true if this test case expects an error response (4xx).
   */
  public boolean isNegativeCase() {
    return expectedStatusCode != null && expectedStatusCode >= 400;
  }
}
