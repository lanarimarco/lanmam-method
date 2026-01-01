# Story 4.6: Create Functional Equivalence Validation Test

Status: review

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **QA engineer**,
I want **a test that validates 100% functional equivalence to RPGLE behavior**,
So that **I can confirm the transformation preserves all business logic**.

## Acceptance Criteria

1. **Given** test data is available (input -> expected output) **When** I run the equivalence validation test **Then** test loads customer test data from test resources
2. Test executes customer inquiry for each test case
3. Test compares actual output to expected output
4. Test reports any functional differences
5. 100% of test cases must pass for validation success
6. Test results are documented in validation report

## Tasks / Subtasks

- [x] Task 1: Create test data structure and fixtures (AC: 1)
  - [x] Create `backend/src/test/resources/equivalence-test-data/` directory
  - [x] Create `customer-inquiry-test-cases.csv` with input/expected output pairs
  - [x] Define CSV schema: test_case_id, customer_id, expected_name, expected_address, expected_city, expected_state, expected_zip, expected_status_code
  - [x] Include positive test cases (valid customer IDs from V2__insert_test_data.sql)
  - [x] Include negative test cases (invalid/non-existent customer IDs)
  - [x] Include edge cases (boundary values, special characters if applicable)

- [x] Task 2: Create CSV parser and test data loader (AC: 1)
  - [x] Add `jackson-dataformat-csv` dependency to pom.xml (version: 2.18.x - latest as of Dec 2025)
  - [x] Create `CustomerTestCase.java` record in `backend/src/test/java/com/smeup/backend/equivalence/`
  - [x] Create `TestDataLoader.java` utility to parse CSV into List<CustomerTestCase>
  - [x] Use Jackson CSV to map CSV rows to CustomerTestCase records
  - [x] Handle CSV parsing errors gracefully with clear error messages

- [x] Task 3: Create equivalence validation test harness (AC: 2, 3, 4)
  - [x] Create `CustomerEquivalenceTest.java` in `backend/src/test/java/com/smeup/backend/equivalence/`
  - [x] Use `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)` for full integration testing
  - [x] Use `@Testcontainers` with PostgreSQL for isolated test database
  - [x] Load test data using TestDataLoader in @BeforeAll
  - [x] Use JUnit 5 `@ParameterizedTest` with `@MethodSource` to iterate over all test cases
  - [x] Each test case: call `GET /api/v1/customers/{customerId}` via RestTemplate
  - [x] Compare actual response fields against expected values from CSV
  - [x] Use `assertAll()` to report all field mismatches in a single test execution

- [x] Task 4: Implement field-by-field comparison logic (AC: 3, 4)
  - [x] Create `EquivalenceValidator.java` utility class
  - [x] Implement `compareCustomerResponse(CustomerDTO actual, CustomerTestCase expected)` method
  - [x] Return `EquivalenceResult` with: testCaseId, passed (boolean), differences (List<String>)
  - [x] Handle null/missing values appropriately
  - [x] Handle HTTP error responses (404, 400) and compare to expected status codes
  - [x] Log detailed differences for failed test cases

- [x] Task 5: Generate validation report (AC: 6)
  - [x] Create `EquivalenceReportGenerator.java` utility
  - [x] Collect all EquivalenceResult objects during test execution
  - [x] Generate markdown report: `backend/target/equivalence-validation-report.md`
  - [x] Report includes: total test cases, passed count, failed count, pass percentage
  - [x] Report includes detailed failure analysis for each failed test case
  - [x] Report includes summary section: "PASS" if 100%, "FAIL" otherwise
  - [x] Output report path to console after test execution

- [x] Task 6: Validate and document equivalence testing (AC: 5, 6)
  - [x] Run `mvn test -Dtest=CustomerEquivalenceTest` - all test cases must pass
  - [x] Verify equivalence-validation-report.md is generated with 100% pass rate
  - [x] Copy final report to `docs/transformations/CUST001/equivalence-validation.md`
  - [x] Update `backend/README.md` with equivalence testing documentation
  - [x] Document how to add new test cases to customer-inquiry-test-cases.csv
  - [x] Document how to interpret validation report results

## Dev Notes

### Critical Context from Previous Stories

**Story 4.5 (E2E Tests with Playwright) - Blocked Status:**
- Story 4.5 is currently **blocked** due to frontend environment coupling issues
- Playwright E2E tests cannot verify UI because frontend fails to render under test conditions
- **Impact on 4.6:** This story focuses on **backend API functional equivalence only**, bypassing the frontend layer entirely
- This approach validates business logic preservation without dependency on UI E2E tests

**Testing Stack Established:**
- JUnit 5 + Mockito for unit tests (Story 4.1)
- Testcontainers for integration tests (Story 4.2)
- MockMvc for controller tests (Story 4.3)
- Vitest + React Testing Library for frontend components (Story 4.4)
- Playwright for E2E tests (Story 4.5 - blocked)

**Database Schema & Test Data:**
- PostgreSQL database with CUSTMAST table (from Story 2.2)
- Test data seeded via `V2__insert_test_data.sql` (from Story 4.5)
- Known test customers available (e.g., customer ID 1001)

**API Endpoint Under Test:**
- `GET /api/v1/customers/{customerId}` (from Story 2.5)
- Returns CustomerDTO with fields: customerId, customerName, address, city, state, zip
- Returns 404 for non-existent customers
- Returns 400 for invalid customer ID format

### Functional Equivalence Testing Strategy

**NFR1 Compliance - 100% Functional Equivalence (ZERO TOLERANCE):**
This story directly addresses the most critical non-functional requirement. The RPGLE transformation must achieve 100% functional equivalence - business logic, data validation, and error handling must match the original RPGLE behavior exactly.

**Test Data Approach:**
- Use CSV files to define input/expected output test cases
- CSV format enables business stakeholders to review test cases without code knowledge
- Test cases derived from original RPGLE program behavior documentation
- Parameterized testing ensures comprehensive coverage with minimal test code

**Validation Methodology:**
1. Load test cases from CSV (input: customer ID, expected: all response fields)
2. Execute API call for each test case against real database (Testcontainers)
3. Compare actual API response to expected values field-by-field
4. Report any discrepancies with detailed difference analysis
5. Require 100% pass rate - even one mismatch fails the validation

**Reporting Requirements:**
- Machine-readable results (JUnit test output)
- Human-readable markdown report for stakeholders
- Detailed failure analysis to guide corrections
- Final report stored in `docs/transformations/CUST001/` for traceability

### Project Structure Notes

**Test Data Location:**
```
backend/src/test/resources/equivalence-test-data/
└── customer-inquiry-test-cases.csv
```

**Test Code Location:**
```
backend/src/test/java/com/smeup/equivalence/
├── CustomerEquivalenceTest.java
├── CustomerTestCase.java (record)
├── TestDataLoader.java
├── EquivalenceValidator.java
└── EquivalenceReportGenerator.java
```

**Report Output:**
```
backend/target/equivalence-validation-report.md (generated)
docs/transformations/CUST001/equivalence-validation.md (final copy)
```

### References

- [Source: _bmad-output/planning-artifacts/epics.md - Epic 4, Story 4.6]
- [Source: _bmad-output/planning-artifacts/architecture.md - Testing Strategy, NFR1]
- [Source: _bmad-output/planning-artifacts/prd.md - FR20-FR23 Automated Test Generation, NFR1]
- [Source: 4-5-create-e2e-tests-with-playwright.md - Previous Story Context]

### Technical Constraints & Standards

**Testing Framework:**
- JUnit 5 with `@ParameterizedTest` for data-driven testing
- `@SpringBootTest` for full integration with Spring context
- Testcontainers PostgreSQL for isolated database per test run
- Jackson CSV (`jackson-dataformat-csv`) for CSV parsing

**Code Quality Standards:**
- Follow Google Java Style (Checkstyle enforced)
- Cyclomatic complexity ≤ 10 per method
- All test methods must have descriptive names (e.g., `testCustomerEquivalence_CustomerID_{id}_ExpectsMatch`)
- Use JUnit 5 `assertAll()` for multiple assertions to report all failures at once

**Test Data Best Practices:**
- CSV schema must be versioned (include header row with field names)
- Test case IDs must be unique and descriptive
- Include at least 5 positive test cases and 3 negative test cases
- Edge cases must include boundary values (min/max customer IDs, special characters if supported)
- CSV files must be UTF-8 encoded

**Equivalence Validation Rules:**
- String fields: exact match (case-sensitive unless specified otherwise)
- Numeric fields: exact value match
- Null handling: null in expected = null in actual (or absent field)
- HTTP status codes: must match exactly (200 for success, 404 for not found, 400 for validation error)
- Error messages: validate presence and general structure (exact wording may vary)

### Latest Technical Information (December 2025)

**JUnit 5 Best Practices for Functional Equivalence Testing:**

*Parameterized Testing:*
- Use `@ParameterizedTest` with `@MethodSource` or `@CsvFileSource` for data-driven tests
- JUnit 5's parameterized tests reduce duplication and ensure broader test coverage
- Each test case runs independently with clear failure isolation

*Equivalence Partitioning:*
- Test data should be segregated into equivalence classes (valid IDs, invalid formats, non-existent IDs)
- Test one representative from each partition to optimize coverage
- [Source: https://www.browserstack.com/guide/functional-testing]

*Test Structure:*
- Each `@Test` method should verify a single behavior or use case
- Use `assertAll()` to group multiple related assertions and report all failures together
- [Source: https://www.code-intelligence.com/junit-testing]

*Automation & Integration:*
- Test automation is critical to reduce human error and increase efficiency
- Incorporate functional testing into CI/CD pipeline from the outset
- [Source: https://www.code-intelligence.com/blog/tips-for-functional-testing-in-java]

**Spring Boot CSV Testing Approaches:**

*Jackson CSV Library (Recommended):*
- Add dependency: `jackson-dataformat-csv` version 2.18.x (latest stable as of Dec 2025)
- Provides robust CSV parsing with schema validation
- Maps CSV rows directly to Java records or POJOs
- [Source: https://www.baeldung.com/spring-app-setup-with-csv-files]

*JUnit 5 @CsvSource:*
- Built-in annotation for inline CSV test data
- Suitable for small datasets (< 10 rows)
- For larger datasets (Story 4.6 use case), use `@CsvFileSource` or Jackson CSV
- [Source: https://www.baeldung.com/parameterized-tests-junit-5]

*Test Harness Patterns:*
- Spring Boot Test supports full integration testing with `@SpringBootTest`
- Testcontainers ensures database isolation and repeatability
- Combine with JUnit 5 lifecycle hooks (`@BeforeAll`, `@AfterAll`) for setup/teardown
- [Source: https://reflectoring.io/spring-boot-data-jpa-test/]

**Dependency Version:**
```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-csv</artifactId>
    <version>2.18.2</version>
    <scope>test</scope>
</dependency>
```

### Architecture Compliance

**Testing Architecture Alignment:**
This story follows the testing architecture defined in architecture.md:
- **Layer:** Functional Equivalence Validation (custom test harness)
- **Tool:** JUnit 5 + Testcontainers + Jackson CSV
- **Purpose:** Compare against RPGLE test data - 100% functional equivalence validation

**API Response Format Validation:**
Tests must validate that responses conform to the standard format:
```json
{
  "data": { "customerId": "1001", "customerName": "...", ... },
  "meta": { "timestamp": "...", "requestId": "..." }
}
```

**Error Response Format Validation (RFC 7807):**
Tests must validate error responses match the RFC 7807 Problem Details format:
```json
{
  "type": "https://api.smeup.com/errors/not-found",
  "title": "Customer Not Found",
  "status": 404,
  "detail": "Customer with ID 9999 does not exist",
  "instance": "/api/v1/customers/9999"
}
```

### Implementation Guidance for Dev Agent

**Step-by-Step Implementation Plan:**

1. **Start with Test Data Creation:**
   - Create CSV file with at least 8 test cases (5 positive, 3 negative)
   - Use existing test data from `V2__insert_test_data.sql` for positive cases
   - Define clear expected outcomes for each test case

2. **Build Data Loading Infrastructure:**
   - Create Java record for test case structure
   - Implement CSV parser using Jackson
   - Validate CSV schema and data integrity

3. **Implement Test Harness:**
   - Use `@SpringBootTest` with Testcontainers for real database
   - Use `@ParameterizedTest` to iterate over test cases
   - Call API endpoints using `TestRestTemplate`

4. **Add Comparison Logic:**
   - Implement field-by-field comparison
   - Handle both success and error responses
   - Collect detailed difference reports

5. **Generate Validation Report:**
   - Create markdown report generator
   - Include summary statistics and detailed failures
   - Output to standard location for documentation

6. **Verify 100% Pass Rate:**
   - All test cases must pass
   - Report must show 100% functional equivalence
   - Copy final report to docs/transformations/CUST001/

**Critical Success Factors:**
- ✅ Test data must cover all RPGLE business logic paths
- ✅ Comparison logic must be precise (no false positives/negatives)
- ✅ Report must be stakeholder-readable (non-technical audience)
- ✅ Tests must be repeatable and isolated (Testcontainers)
- ✅ 100% pass rate required - no tolerance for partial equivalence

**Common Pitfalls to Avoid:**
- ❌ Don't hardcode test data in Java code - use CSV for flexibility
- ❌ Don't skip negative test cases - validate error handling equivalence
- ❌ Don't use brittle string matching - parse JSON responses properly
- ❌ Don't ignore HTTP status codes - they're part of functional behavior
- ❌ Don't generate reports that developers can't easily share with stakeholders

### Traceability to RPGLE Original

**CUST001.rpgle Business Logic Coverage:**
The test cases must validate the following RPGLE business logic:
1. Customer lookup by ID (CHAIN operation on CUSTMAST)
2. Customer ID validation (numeric, valid range)
3. Customer not found handling
4. Data retrieval and field mapping (all CUSTMAST fields)
5. Success response structure
6. Error response structure

**Inline Comments Requirement:**
All test code must include comments referencing original RPGLE behavior:
```java
// Validates RPGLE CHAIN operation on CUSTMAST file
// Original: C CUSTID CHAIN CUSTMAST
```

### Definition of Done

**Test Implementation Complete:**
- [ ] CSV test data file created with comprehensive test cases
- [ ] Test data loader implemented with Jackson CSV
- [ ] Equivalence test harness created with @ParameterizedTest
- [ ] Field comparison logic implemented with detailed reporting
- [ ] Validation report generator created

**Validation Complete:**
- [ ] `mvn test -Dtest=CustomerEquivalenceTest` passes with 100% success rate
- [ ] Equivalence validation report generated in `backend/target/`
- [ ] Final report copied to `docs/transformations/CUST001/equivalence-validation.md`
- [ ] Code passes Checkstyle and PMD with zero violations
- [ ] All code includes RPGLE reference comments

**Documentation Complete:**
- [ ] README updated with equivalence testing instructions
- [ ] Test case CSV schema documented
- [ ] Report interpretation guide included
- [ ] Instructions for adding new test cases provided

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5

### Debug Log References

No significant blockers encountered. Test execution was smooth with one CSV formatting correction needed.

### Completion Notes List

- ✅ Created comprehensive CSV test data with 8 test cases (5 positive, 3 negative)
- ✅ Implemented Jackson CSV data loading with @JsonProperty mapping for snake_case headers
- ✅ Built complete equivalence validation test harness using JUnit 5 + Testcontainers
- ✅ Implemented field-by-field comparison with BigDecimal precision handling
- ✅ Generated stakeholder-readable markdown reports with 100% pass rate
- ✅ All 61 tests pass (8 equivalence tests + 53 existing tests)
- ✅ Achieved 100% functional equivalence validation (NFR1 compliant)
- ✅ Created comprehensive backend/README.md documentation
- ✅ Archived final validation report in docs/transformations/CUST001/

### File List

**Test Infrastructure:**
- backend/src/test/java/com/smeup/backend/equivalence/CustomerTestCase.java
- backend/src/test/java/com/smeup/backend/equivalence/TestDataLoader.java
- backend/src/test/java/com/smeup/backend/equivalence/EquivalenceResult.java
- backend/src/test/java/com/smeup/backend/equivalence/EquivalenceValidator.java
- backend/src/test/java/com/smeup/backend/equivalence/EquivalenceReportGenerator.java
- backend/src/test/java/com/smeup/backend/equivalence/CustomerEquivalenceTest.java

**Test Data:**
- backend/src/test/resources/equivalence-test-data/customer-inquiry-test-cases.csv

**Reports:**
- backend/target/equivalence-validation-report.md
- docs/transformations/CUST001/equivalence-validation.md

**Documentation:**
- backend/README.md

**Dependencies:**
- backend/pom.xml (added jackson-dataformat-csv 2.18.2)
