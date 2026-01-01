package com.smeup.backend.equivalence;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading test data from CSV files.
 *
 * <p>Uses Jackson CSV to parse customer-inquiry-test-cases.csv into
 * CustomerTestCase records for parameterized equivalence testing.
 *
 * <p>Validates RPGLE test data mapping for functional equivalence validation.
 * Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 */
public class TestDataLoader {

  private static final String TEST_DATA_FILE =
      "/equivalence-test-data/customer-inquiry-test-cases.csv";

  /**
   * Loads all customer test cases from CSV file.
   *
   * @return List of CustomerTestCase records parsed from CSV
   * @throws RuntimeException if CSV file cannot be loaded or parsed
   */
  public static List<CustomerTestCase> loadCustomerTestCases() {
    try (InputStream inputStream =
        TestDataLoader.class.getResourceAsStream(TEST_DATA_FILE)) {

      if (inputStream == null) {
        throw new RuntimeException(
            "Test data file not found: " + TEST_DATA_FILE
                + ". Ensure customer-inquiry-test-cases.csv exists in test resources.");
      }

      CsvMapper mapper = new CsvMapper();
      CsvSchema schema = CsvSchema.emptySchema().withHeader();

      MappingIterator<CustomerTestCase> iterator =
          mapper.readerFor(CustomerTestCase.class)
              .with(schema)
              .readValues(inputStream);

      List<CustomerTestCase> testCases = new ArrayList<>();
      while (iterator.hasNext()) {
        testCases.add(iterator.next());
      }

      if (testCases.isEmpty()) {
        throw new RuntimeException(
            "No test cases loaded from " + TEST_DATA_FILE
                + ". CSV file may be empty or malformed.");
      }

      return testCases;

    } catch (IOException e) {
      throw new RuntimeException(
          "Failed to load or parse test data from " + TEST_DATA_FILE
              + ". Check CSV format and schema. Error: " + e.getMessage(), e);
    }
  }

  /**
   * Validates CSV file can be loaded successfully.
   * Used for early validation in test setup.
   *
   * @throws RuntimeException if CSV cannot be loaded
   */
  public static void validateTestDataExists() {
    loadCustomerTestCases(); // Will throw if file doesn't exist or is invalid
  }
}
