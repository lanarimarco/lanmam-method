package com.smeup.backend.equivalence;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a functional equivalence validation.
 *
 * <p>Contains the test case ID, pass/fail status, and detailed list of differences
 * between expected and actual values.
 *
 * <p>Used for reporting and validation to ensure 100% functional equivalence
 * to original RPGLE behavior (NFR1 compliance).
 */
public class EquivalenceResult {

  private final String testCaseId;
  private final boolean passed;
  private final List<String> differences;

  public EquivalenceResult(String testCaseId, boolean passed, List<String> differences) {
    this.testCaseId = testCaseId;
    this.passed = passed;
    this.differences = differences != null ? differences : new ArrayList<>();
  }

  public String getTestCaseId() {
    return testCaseId;
  }

  public boolean isPassed() {
    return passed;
  }

  public List<String> getDifferences() {
    return differences;
  }

  /**
   * Creates a passing result with no differences.
   */
  public static EquivalenceResult pass(String testCaseId) {
    return new EquivalenceResult(testCaseId, true, List.of());
  }

  /**
   * Creates a failing result with specified differences.
   */
  public static EquivalenceResult fail(String testCaseId, List<String> differences) {
    return new EquivalenceResult(testCaseId, false, differences);
  }
}
