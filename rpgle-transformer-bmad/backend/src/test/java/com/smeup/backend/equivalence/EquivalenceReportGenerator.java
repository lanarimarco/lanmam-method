package com.smeup.backend.equivalence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Generates markdown validation reports for functional equivalence testing.
 *
 * <p>Creates stakeholder-readable reports showing test results, pass/fail rates,
 * and detailed failure analysis for RPGLE transformation validation (NFR1 compliance).
 *
 * <p>Validates RPGLE CUST001 equivalence.
 * Original Program: source-rpgle/programs/CUST001.rpgle
 */
public class EquivalenceReportGenerator {

  private static final String REPORT_FILE = "backend/target/equivalence-validation-report.md";

  /**
   * Generates markdown validation report from test results.
   *
   * <p>Validates 100% functional equivalence requirement (NFR1).
   * Original RPGLE Program: CUST001 - Customer Inquiry
   *
   * @param results List of EquivalenceResult objects from test execution
   * @return Path to generated report file
   */
  public static Path generateReport(List<EquivalenceResult> results) {
    try {
      String reportContent = buildReportContent(results);
      Path reportPath = Paths.get(REPORT_FILE);

      // Create target directory if it doesn't exist
      Files.createDirectories(reportPath.getParent());

      Files.writeString(reportPath, reportContent);

      System.out.println("=".repeat(80));
      System.out.println("Equivalence Validation Report Generated:");
      System.out.println(reportPath.toAbsolutePath());
      System.out.println("=".repeat(80));

      return reportPath;

    } catch (IOException e) {
      throw new RuntimeException(
          "Failed to generate equivalence validation report: " + e.getMessage(), e);
    }
  }

  private static String buildReportContent(List<EquivalenceResult> results) {
    StringBuilder report = new StringBuilder();

    // Header
    report.append("# Functional Equivalence Validation Report\n\n");
    report.append("**Program:** CUST001 - Customer Inquiry\n\n");
    report.append("**Original:** source-rpgle/programs/CUST001.rpgle\n\n");
    report.append("**Date:** ").append(LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");

    report.append("---\n\n");

    // Summary Statistics
    long totalTests = results.size();
    long passedTests = results.stream().filter(EquivalenceResult::isPassed).count();
    long failedTests = totalTests - passedTests;
    double passPercentage = totalTests > 0
        ? (double) passedTests / totalTests * 100
        : 0.0;

    report.append("## Summary\n\n");
    report.append(String.format("- **Total Test Cases:** %d\n", totalTests));
    report.append(String.format("- **Passed:** %d\n", passedTests));
    report.append(String.format("- **Failed:** %d\n", failedTests));
    report.append(String.format("- **Pass Rate:** %.1f%%\n\n", passPercentage));

    // Overall Result
    String validationStatus = (failedTests == 0) ? "✅ PASS" : "❌ FAIL";
    report.append(String.format("**Validation Status:** %s\n\n", validationStatus));

    if (failedTests == 0) {
      report.append("**Conclusion:** 100% functional equivalence validated. ");
      report.append("The transformation preserves all RPGLE business logic (NFR1 compliant).\n\n");
    } else {
      report.append("**Conclusion:** Functional equivalence validation FAILED. ");
      report.append(String.format("%d test case(s) have discrepancies. ", failedTests));
      report.append("Review detailed failure analysis below.\n\n");
    }

    report.append("---\n\n");

    // Detailed Results
    if (failedTests > 0) {
      report.append("## Detailed Failure Analysis\n\n");

      for (EquivalenceResult result : results) {
        if (!result.isPassed()) {
          report.append(String.format("### Test Case: %s\n\n", result.getTestCaseId()));
          report.append("**Differences:**\n\n");

          for (String difference : result.getDifferences()) {
            report.append(String.format("- %s\n", difference));
          }

          report.append("\n");
        }
      }

      report.append("---\n\n");
    }

    // All Test Cases Summary
    report.append("## All Test Cases\n\n");
    report.append("| Test Case ID | Status |\n");
    report.append("|--------------|--------|\n");

    for (EquivalenceResult result : results) {
      String status = result.isPassed() ? "✅ PASS" : "❌ FAIL";
      report.append(String.format("| %s | %s |\n", result.getTestCaseId(), status));
    }

    report.append("\n---\n\n");

    // Footer
    report.append("## Validation Criteria\n\n");
    report.append("- **NFR1 Requirement:** 100% Functional Equivalence (ZERO TOLERANCE)\n");
    report.append("- **Validation Method:** Field-by-field comparison against CSV test data\n");
    report.append("- **Test Framework:** JUnit 5 + Testcontainers + Jackson CSV\n");
    report.append("- **Success Criteria:** All test cases must pass (100% pass rate)\n\n");

    report.append("**End of Report**\n");

    return report.toString();
  }
}
