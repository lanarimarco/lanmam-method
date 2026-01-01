# Story 4.2: Create Backend Integration Tests with Testcontainers

Status: ready-for-dev

## Story

As a **QA engineer**,
I want **integration tests using Testcontainers with real PostgreSQL**,
So that **I can validate the full backend stack works with actual database**.

## Acceptance Criteria

1. **Given** backend application and database migration exist
   **When** I create integration tests
   **Then** `CustomerIntegrationTest.java` is created

2. **And** tests use Testcontainers to spin up PostgreSQL

3. **And** tests verify Customer entity persists correctly to CUSTMAST table

4. **And** tests verify API endpoint returns correct data

5. **And** tests validate SQL uses DDS column names (CUSTID, CUSTNM)

6. **And** tests clean up data after execution

## Tasks / Subtasks

- [ ] Task 1: Create CustomerIntegrationTest.java full-stack integration test (AC: #1, #2, #4)
  - [ ] Extend AbstractIntegrationTest for Testcontainers PostgreSQL
  - [ ] Use @SpringBootTest(webEnvironment = RANDOM_PORT) for full context
  - [ ] Inject TestRestTemplate for API endpoint testing
  - [ ] Configure test to run Flyway migrations automatically

- [ ] Task 2: Implement end-to-end customer creation and retrieval test (AC: #3, #4, #5)
  - [ ] Test: POST customer via API → verify persisted to CUSTMAST → GET customer → verify data
  - [ ] Use JdbcTemplate to verify SQL column names (CUSTNO, CUSTNAME, etc.)
  - [ ] Test full stack: Controller → Service → Repository → PostgreSQL → Response
  - [ ] Verify JSON response format matches ApiResponse wrapper

- [ ] Task 3: Implement customer not found integration test (AC: #4)
  - [ ] Test: GET non-existent customer → verify 404 status
  - [ ] Verify error response format matches RFC 7807 Problem Details
  - [ ] Verify exception handling through full stack

- [ ] Task 4: Implement data cleanup and isolation (AC: #6)
  - [ ] Add @BeforeEach method to truncate CUSTMAST table
  - [ ] Use @Transactional with @Rollback for test isolation (optional)
  - [ ] Verify each test starts with clean database state

- [ ] Task 5: Run tests and verify coverage (AC: all)
  - [ ] Run `mvn verify` - all integration tests pass
  - [ ] Verify Testcontainers starts PostgreSQL container
  - [ ] Verify Flyway migrations execute successfully
  - [ ] Verify all 3+ integration test scenarios pass

## Dev Notes

### 🔥 CRITICAL DISCOVERY: Integration Test Infrastructure Already Exists

**Story 2.3 created repository-level integration tests** (`CustomerRepositoryIT.java`) that already cover:
- ✅ Testcontainers PostgreSQL setup (AbstractIntegrationTest base class)
- ✅ DDS table/column name validation
- ✅ Data persistence verification
- ✅ Data cleanup (@BeforeEach truncation)

**Story 4.2 is NOT a duplicate** - it focuses on **FULL-STACK integration testing**:
- Testing the complete request/response cycle through REST API
- Verifying Controller → Service → Repository → Database → Response flow
- Testing error handling and exception mapping through the full stack
- Validating API response formats (ApiResponse wrapper, RFC 7807 errors)

### What Already Exists (From Previous Stories)

**AbstractIntegrationTest.java** (Story 1.4 - Testing Infrastructure):
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {
  @ServiceConnection
  static PostgreSQLContainer<?> postgres;

  static {
    postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    postgres.start();
  }
}
```

**Pattern:** Singleton Container Pattern - one PostgreSQL container shared across all test classes

**CustomerRepositoryIT.java** (Story 2.3 - Repository Layer):
- 4 integration tests for repository layer only
- Tests persistence, DDS column mapping, findByCustomerId, BigDecimal precision
- Uses JdbcTemplate for SQL verification
- Cleans up via `@BeforeEach` DELETE FROM CUSTMAST

### What Story 4.2 MUST Create

**CustomerIntegrationTest.java** - Full-Stack Integration Tests:
1. **End-to-End Customer Flow Test:**
   - POST /api/v1/customers → create customer
   - Verify via JdbcTemplate that data is in CUSTMAST with DDS columns
   - GET /api/v1/customers/{id} → retrieve customer
   - Verify ApiResponse wrapper format: `{ data: {...}, meta: {...} }`
   - Verify all Customer fields match (CUSTNO → customerId, CUSTNAME → customerName, etc.)

2. **Customer Not Found Test:**
   - GET /api/v1/customers/99999 (non-existent)
   - Verify 404 HTTP status
   - Verify RFC 7807 Problem Details format
   - Verify error message contains customer ID

3. **Invalid Customer ID Test:**
   - GET /api/v1/customers/0 (invalid - Story 4.1 added validation)
   - Verify 400 HTTP status
   - Verify validation error response

### Testing Stack and Tools

**From project-context.md:**
- **Integration Tests:** Testcontainers with real PostgreSQL
- **Framework:** JUnit 5
- **Assertions:** AssertJ
- **HTTP Client:** TestRestTemplate (Spring Boot)
- **SQL Verification:** JdbcTemplate
- **Database:** PostgreSQL 16 (via Testcontainers)

**Maven Failsafe Plugin:** Already configured in pom.xml
- Integration tests run during `verify` phase
- Test class pattern: `**/*IT.java`
- Uses suffix `IT` (Integration Test) not `Test` (Unit Test)

### Key Technical Requirements from Previous Stories

**Story 4.1 Learnings (Code Review Findings):**
1. **DDS Field Size Validation:** CustomerService now validates customerId <= 99999 (5P 0 field constraint)
2. **Expected Behavior:** GET with customerId > 99999 should return 400 Bad Request
3. **JaCoCo Coverage:** Integration tests contribute to overall coverage metrics

**Story 2.5 (CustomerController) Implementation:**
- Endpoint: `GET /api/v1/customers/{id}`
- Success Response: `ApiResponse<CustomerDTO>` with 200 OK
- Not Found: `CustomerNotFoundException` → 404 with RFC 7807 format
- Invalid ID: `InvalidCustomerIdException` → 400 with RFC 7807 format
- Uses `@ControllerAdvice` GlobalExceptionHandler for error mapping

**Story 2.6 (DTO/Mapper) Implementation:**
- CustomerDTO maps all DDS fields from Customer entity
- CustomerMapper converts Entity ↔ DTO
- All field mappings already tested in unit tests

### File Structure and Naming

**Test Location:** `backend/src/test/java/com/smeup/backend/integration/CustomerIntegrationTest.java`

**Why `integration` package?**
- Separates full-stack tests from repository-only tests
- Follows Maven Failsafe convention: integration tests in dedicated package
- Alternative: `com.smeup.backend.api.CustomerIntegrationTest` (API-focused)

**Naming Convention:**
- Class: `CustomerIntegrationTest` (matches Story 4.2 AC)
- Suffix: `IT` not `Test` (triggers Failsafe plugin)
- Tests: `shouldReturnCustomerWhenValidId()`, `shouldReturn404WhenCustomerNotFound()`, etc.

### Test Template Structure

```java
package com.smeup.backend.integration;

import com.smeup.backend.AbstractIntegrationTest;
import com.smeup.backend.dto.ApiResponse;
import com.smeup.backend.dto.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Full-stack integration tests for Customer API endpoints.
 *
 * <p>Tests the complete request/response cycle:
 * REST API → Controller → Service → Repository → PostgreSQL (Testcontainers)
 *
 * <p>Validates:
 * - API endpoints return correct data
 * - Data persists to CUSTMAST table with DDS column names
 * - Error handling through full stack (404, 400)
 * - Response format (ApiResponse wrapper, RFC 7807 errors)
 *
 * <p>Uses AbstractIntegrationTest for Testcontainers PostgreSQL setup.
 * Flyway migrations run automatically before tests.
 */
class CustomerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    // Clean CUSTMAST table before each test
    jdbcTemplate.update("DELETE FROM CUSTMAST");
  }

  @Test
  void shouldReturnCustomerWhenValidId() {
    // Given - insert customer directly via JDBC (simulating existing data)
    jdbcTemplate.update(
      "INSERT INTO CUSTMAST (CUSTNO, CUSTNAME, ADDR1, CITY, STATE, ZIP, PHONE) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?)",
      12345L, "ACME Corp", "123 Main St", "Springfield", "IL", 62701, "555-1234"
    );

    // When - call API endpoint
    ResponseEntity<ApiResponse<CustomerDTO>> response = restTemplate.exchange(
      "/api/v1/customers/12345",
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {}
    );

    // Then - verify response
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getData()).isNotNull();
    assertThat(response.getBody().getData().getCustomerId()).isEqualTo(12345L);
    assertThat(response.getBody().getData().getCustomerName()).isEqualTo("ACME Corp");
  }

  @Test
  void shouldReturn404WhenCustomerNotFound() {
    // When - GET non-existent customer
    ResponseEntity<String> response = restTemplate.getForEntity(
      "/api/v1/customers/99999",
      String.class
    );

    // Then - verify 404 with RFC 7807 error format
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    // Verify error response contains expected fields (title, detail, etc.)
  }
}
```

### RPGLE CUST001 Integration Test Mapping

**Original RPGLE Integration Flow:**
```rpgle
* CUST001 - Customer Inquiry Program
* Full program execution flow:
*
* 1. Display PROMPT screen (customer number entry)
* 2. Read customer number from screen
* 3. CHAIN to CUSTMAST physical file
* 4. IF %FOUND(CUSTMAST)
*      Display DETAIL screen with customer data
*    ELSE
*      Display error message on PROMPT
*    ENDIF
* 5. Loop back for next inquiry
```

**Integration Test Coverage Mapping:**
| RPGLE Operation | Java Full-Stack Flow | Integration Test |
|-----------------|----------------------|------------------|
| Display PROMPT → Read CUSTNO | HTTP GET /api/v1/customers/{id} | Request sent via TestRestTemplate |
| CHAIN CUSTMAST | Controller → Service → Repository.findByCustomerId() | Full stack invocation |
| Access PostgreSQL | JPA query to PostgreSQL (Testcontainers) | Real database query |
| %FOUND = *ON | Optional.isPresent() → return CustomerDTO | shouldReturnCustomerWhenValidId() |
| %FOUND = *OFF | Optional.empty() → throw CustomerNotFoundException | shouldReturn404WhenCustomerNotFound() |
| Display DETAIL | Return ApiResponse<CustomerDTO> as JSON | Verify response.getBody().getData() |
| Error indicator *IN90 | HTTP 404 status + RFC 7807 error | Verify response.getStatusCode() |

**Integration Test Validates:**
1. ✅ Database connectivity (Testcontainers PostgreSQL)
2. ✅ Flyway migrations executed (CUSTMAST table exists)
3. ✅ JPA entity mapping (Customer ↔ CUSTMAST with DDS columns)
4. ✅ Service layer business logic (validation, not found handling)
5. ✅ Controller layer (request mapping, response formatting)
6. ✅ Exception handling (@ControllerAdvice → RFC 7807)
7. ✅ DTO serialization (CustomerDTO → JSON)
8. ✅ API contract (ApiResponse wrapper format)

### Difference from Unit Tests (Story 4.1)

**Unit Tests (CustomerServiceTest):**
- Mock repository with Mockito
- Test service logic in isolation
- Fast execution (~0.5 seconds)
- No database, no Spring context
- Coverage: Service layer only

**Integration Tests (CustomerIntegrationTest):**
- Real PostgreSQL database (Testcontainers)
- Full Spring Boot application context
- Slower execution (~5 seconds)
- Tests entire request/response flow
- Coverage: Controller + Service + Repository + Database

**Both are required for Epic 4 goal:** "QA engineers can validate 100% functional equivalence"
- Unit tests validate logic correctness
- Integration tests validate end-to-end behavior matches RPGLE

### Testing Anti-Patterns to Avoid

**❌ DON'T create duplicate repository tests:**
- CustomerRepositoryIT already validates repository layer
- Don't test repository methods again in CustomerIntegrationTest
- Focus on API endpoint behavior

**❌ DON'T test unit test scenarios again:**
- CustomerServiceTest already validates service logic
- Don't re-test validation rules, exception handling in integration tests
- Focus on full-stack integration

**✅ DO test end-to-end flows:**
- API request → full stack → database → API response
- Error propagation through layers
- Response format compliance
- Data persistence across layers

**✅ DO verify integration points:**
- Spring Boot auto-configuration works
- JPA mappings work with real PostgreSQL
- Exception translation from service → controller
- JSON serialization of complex objects

### Project Context Alignment

**From project-context.md (Lines 83-89):**
```
Testing Rules:
- Unit tests: JUnit 5 + Mockito (Java), Vitest + RTL (React)
- Integration tests: Testcontainers with real PostgreSQL
- E2E tests: Playwright for full user flows
- Test files co-located with source in __tests__/ folders
- 80%+ code coverage required
```

**Test Coverage Contribution:**
- Integration tests counted by JaCoCo
- Help achieve 80%+ coverage requirement
- Cover paths not tested by unit tests (Spring configuration, JPA, etc.)

**RPGLE Traceability (Lines 97-116):**
- Every test must reference original RPGLE operation
- Use @DisplayName with RPGLE context
- Inline comments explaining RPGLE → Java mapping

### Previous Story Intelligence (Story 4.1)

**Key Learnings from Code Review:**
1. **DDS Boundary Validation:** customerId must be <= 99999 (added in review fix)
2. **Test Quality:** Use explicit `times(1)` in Mockito verifications
3. **Future-Proofing:** Recursive patterns (`service.**`) for scalability
4. **Exclusions:** Exclude entities, DTOs from coverage (low value)

**How These Apply to Story 4.2:**
- ✅ Integration test should verify 400 response for customerId > 99999
- ✅ Use real database, no Mockito needed
- ✅ Tests automatically contribute to service layer coverage
- ✅ Focus on valuable integration scenarios, not POJO testing

### Dependencies and Story Order

**Depends On (All Complete):**
- ✅ Story 1.4: Testing Infrastructure (AbstractIntegrationTest created)
- ✅ Story 2.1: Customer Entity (JPA mappings)
- ✅ Story 2.2: Flyway Migration (CUSTMAST table schema)
- ✅ Story 2.3: Customer Repository (database access)
- ✅ Story 2.4: Customer Service (business logic)
- ✅ Story 2.5: Customer Controller (REST endpoints)
- ✅ Story 2.6: Customer DTO/Mapper (data transfer)
- ✅ Story 4.1: Unit Tests (service layer validation)

**Blocks:**
- 🔒 Story 4.3: Controller Tests (MockMvc isolated tests - different approach)
- 🔒 Story 4.6: Functional Equivalence Validation (needs all test types complete)

**No blockers - Story 4.2 is ready for implementation!**

### Git Intelligence

**Recent Commits Pattern (from Story 4.1):**
```
✨ feat: Add JaCoCo Maven plugin for code coverage
✨ feat: Add DDS boundary validation (max 99999)
🐛 fix: Add explicit Mockito times(1) verifications
```

**Test File Pattern:**
- Created: `CustomerServiceTest.java` (unit tests)
- Modified: `CustomerService.java` (added validation)
- Modified: `pom.xml` (JaCoCo plugin)

**Commit Message Convention:**
- Prefix: `✨ feat:` for new features
- Prefix: `🐛 fix:` for bug fixes
- Include story reference in commit body

### Success Criteria

**Story marked "done" when:**
1. ✅ CustomerIntegrationTest.java created in integration package
2. ✅ Tests extend AbstractIntegrationTest
3. ✅ TestRestTemplate injected and used for API calls
4. ✅ End-to-end customer flow test passes (insert → GET → verify)
5. ✅ Customer not found test passes (404 response)
6. ✅ Invalid customer ID test passes (400 response)
7. ✅ Tests verify DDS column names via JdbcTemplate
8. ✅ Tests clean up data in @BeforeEach
9. ✅ All integration tests pass: `mvn verify`
10. ✅ Testcontainers PostgreSQL starts successfully
11. ✅ Flyway migrations execute without errors
12. ✅ Code style passes: `mvn checkstyle:check`

## Dev Agent Record

### Agent Model Used

_To be completed by Dev Agent implementing this story._

### Debug Log References

_To be completed during implementation._

### Completion Notes List

_To be completed when story moves to "done" status._

### File List

**Files to Create:**
- `backend/src/test/java/com/smeup/backend/integration/CustomerIntegrationTest.java` - Full-stack integration tests

**Files to Update:**
- `_bmad-output/implementation-artifacts/sprint-status.yaml` - Story status: "backlog" → "ready-for-dev" → "in-progress" → "review" → "done"
- This story file - Add completion notes

**Files to Verify (No Changes Expected):**
- `backend/src/test/java/com/smeup/backend/AbstractIntegrationTest.java` - Testcontainers base class
- `backend/pom.xml` - Maven Failsafe plugin configuration
- `backend/src/main/resources/db/migration/V1__create_custmast_table.sql` - Flyway migration

## References

**Epic Source:**
- [Source: _bmad-output/planning-artifacts/epics.md - Epic 4, Story 4.2, Lines 522-537]

**Previous Story Intelligence:**
- [Source: _bmad-output/implementation-artifacts/4-1-create-backend-unit-tests-for-customer-service.md - Unit test patterns and learnings]

**Existing Test Infrastructure:**
- [Source: backend/src/test/java/com/smeup/backend/AbstractIntegrationTest.java - Testcontainers setup]
- [Source: backend/src/test/java/com/smeup/backend/repository/CustomerRepositoryIT.java - Repository integration test pattern]

**Implementation Dependencies:**
- [Source: backend/src/main/java/com/smeup/backend/controller/CustomerController.java - REST endpoints to test]
- [Source: backend/src/main/java/com/smeup/backend/service/CustomerService.java - Service layer with validation]
- [Source: backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java - Response DTO]
- [Source: backend/src/main/java/com/smeup/backend/dto/ApiResponse.java - Response wrapper]

**Architecture Standards:**
- [Source: _bmad-output/planning-artifacts/architecture.md - Testcontainers decision]
- [Source: _bmad-output/project-context.md - Lines 83-89: Testing rules, Lines 97-116: RPGLE traceability]
