# RPGLE Transformer Backend

Spring Boot REST API for RPGLE-to-modern-stack transformation.

## Testing Strategy

### Functional Equivalence Validation

This project includes comprehensive equivalence testing to ensure **100% functional equivalence** to the original RPGLE programs (NFR1 compliance).

#### Running Equivalence Tests

```bash
./mvnw test -Dtest=CustomerEquivalenceTest
```

This will:
1. Load test cases from CSV files in `src/test/resources/equivalence-test-data/`
2. Execute parameterized tests against the API using Testcontainers
3. Compare actual responses to expected values field-by-field
4. Generate a validation report in `backend/target/equivalence-validation-report.md`

#### Test Data Format

Test cases are defined in CSV files with the following schema:

```csv
test_case_id,customer_id,expected_name,expected_address,expected_city,expected_state,expected_zip,expected_phone,expected_balance,expected_credit_limit,expected_last_order,expected_status_code
POSITIVE_1001,1001,ACME Corporation,123 Main Street,Springfield,IL,62701,217-555-0100,1500.50,10000.00,20251201,200
NEGATIVE_NOT_FOUND,9999,,,,,,,,,,404
```

#### Adding New Test Cases

1. Open `src/test/resources/equivalence-test-data/customer-inquiry-test-cases.csv`
2. Add a new row with your test case data
3. Ensure `test_case_id` is unique
4. Set `expected_status_code` to:
   - `200` for successful responses
   - `404` for "not found" errors
   - `400` for validation errors
5. Run the test suite to verify

#### Interpreting Validation Reports

Reports are generated in markdown format at:
- **Runtime:** `backend/target/equivalence-validation-report.md`
- **Archived:** `docs/transformations/CUST001/equivalence-validation.md`

**Report Structure:**
- **Summary:** Total tests, passed, failed, pass percentage
- **Validation Status:** ✅ PASS (100%) or ❌ FAIL (< 100%)
- **All Test Cases:** Complete list with pass/fail status
- **Detailed Failure Analysis:** Field-by-field differences for failed tests

**Success Criteria:**
- All test cases must pass (100% pass rate)
- Zero tolerance for functional differences
- Report must show "✅ PASS" validation status

## Other Test Suites

### Unit Tests
```bash
./mvnw test -Dtest=CustomerServiceTest
```

### Integration Tests
```bash
./mvnw test -Dtest=CustomerIntegrationTest
```

### Controller Tests
```bash
./mvnw test -Dtest=CustomerControllerTest
```

### All Tests
```bash
./mvnw test
```

## Code Quality

### Checkstyle
```bash
./mvnw checkstyle:check
```

### JaCoCo Coverage Report
```bash
./mvnw jacoco:report
```
View report: `target/site/jacoco/index.html`

## Build

```bash
./mvnw clean package
```

## Run

```bash
./mvnw spring-boot:run
```

API available at: `http://localhost:8080/api/v1`
