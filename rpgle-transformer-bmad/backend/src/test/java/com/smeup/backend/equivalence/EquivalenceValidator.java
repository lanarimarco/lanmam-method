package com.smeup.backend.equivalence;

import com.smeup.backend.dto.CustomerDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for validating functional equivalence between expected and actual values.
 *
 * <p>Performs field-by-field comparison of CustomerDTO against expected test case values.
 * Handles null values, numeric precision, and provides detailed difference reporting.
 *
 * <p>Validates RPGLE CUSTMAST field mapping equivalence.
 * Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 */
public class EquivalenceValidator {

  /**
   * Compares actual CustomerDTO response against expected test case values.
   *
   * <p>Validates RPGLE CHAIN operation equivalence.
   * Original: C CUSTID CHAIN CUSTMAST
   *
   * @param actual The actual CustomerDTO from API response
   * @param expected The expected test case values
   * @return EquivalenceResult indicating pass/fail with detailed differences
   */
  public static EquivalenceResult compareCustomerResponse(
      CustomerDTO actual, CustomerTestCase expected) {

    List<String> differences = new ArrayList<>();

    // Customer ID
    if (!Objects.equals(actual.customerId(), expected.customerId())) {
      differences.add(String.format(
          "customerId: expected=%s, actual=%s",
          expected.customerId(), actual.customerId()));
    }

    // Customer Name
    if (!Objects.equals(actual.customerName(), expected.expectedName())) {
      differences.add(String.format(
          "customerName: expected='%s', actual='%s'",
          expected.expectedName(), actual.customerName()));
    }

    // Address Line 1
    if (!Objects.equals(actual.addressLine1(), expected.expectedAddress())) {
      differences.add(String.format(
          "addressLine1: expected='%s', actual='%s'",
          expected.expectedAddress(), actual.addressLine1()));
    }

    // City
    if (!Objects.equals(actual.city(), expected.expectedCity())) {
      differences.add(String.format(
          "city: expected='%s', actual='%s'",
          expected.expectedCity(), actual.city()));
    }

    // State
    if (!Objects.equals(actual.state(), expected.expectedState())) {
      differences.add(String.format(
          "state: expected='%s', actual='%s'",
          expected.expectedState(), actual.state()));
    }

    // Zip Code
    if (!Objects.equals(actual.zipCode(), expected.expectedZip())) {
      differences.add(String.format(
          "zipCode: expected=%s, actual=%s",
          expected.expectedZip(), actual.zipCode()));
    }

    // Phone Number
    if (!Objects.equals(actual.phoneNumber(), expected.expectedPhone())) {
      differences.add(String.format(
          "phoneNumber: expected='%s', actual='%s'",
          expected.expectedPhone(), actual.phoneNumber()));
    }

    // Account Balance (use BigDecimal.compareTo for numeric comparison)
    if (!compareBigDecimal(actual.accountBalance(), expected.expectedBalance())) {
      differences.add(String.format(
          "accountBalance: expected=%s, actual=%s",
          expected.expectedBalance(), actual.accountBalance()));
    }

    // Credit Limit
    if (!compareBigDecimal(actual.creditLimit(), expected.expectedCreditLimit())) {
      differences.add(String.format(
          "creditLimit: expected=%s, actual=%s",
          expected.expectedCreditLimit(), actual.creditLimit()));
    }

    // Last Order Date
    if (!Objects.equals(actual.lastOrderDate(), expected.expectedLastOrder())) {
      differences.add(String.format(
          "lastOrderDate: expected=%s, actual=%s",
          expected.expectedLastOrder(), actual.lastOrderDate()));
    }

    boolean passed = differences.isEmpty();
    return new EquivalenceResult(expected.testCaseId(), passed, differences);
  }

  /**
   * Compares two BigDecimal values handling nulls.
   * Uses compareTo for numeric equivalence (ignores scale differences).
   */
  private static boolean compareBigDecimal(BigDecimal actual, BigDecimal expected) {
    if (actual == null && expected == null) {
      return true;
    }
    if (actual == null || expected == null) {
      return false;
    }
    return actual.compareTo(expected) == 0;
  }

  /**
   * Validates HTTP status code against expected value.
   *
   * @param actualStatusCode The actual HTTP status code from response
   * @param expectedStatusCode The expected HTTP status code from test case
   * @return EquivalenceResult for status code comparison
   */
  public static EquivalenceResult validateStatusCode(
      int actualStatusCode, CustomerTestCase testCase) {

    if (actualStatusCode == testCase.expectedStatusCode()) {
      return EquivalenceResult.pass(testCase.testCaseId());
    }

    List<String> differences = List.of(String.format(
        "HTTP Status Code: expected=%d, actual=%d",
        testCase.expectedStatusCode(), actualStatusCode));

    return EquivalenceResult.fail(testCase.testCaseId(), differences);
  }
}
