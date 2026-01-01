# Story 4.3: Create Backend Controller Tests

Status: ready-for-dev

## Story

As a **QA engineer**,
I want **MockMvc tests for CustomerController REST endpoints with @WebMvcTest**,
So that **I can validate API responses match expected format in isolation from service layer**.

## Acceptance Criteria

1. **Given** CustomerController exists
   **When** I create controller tests
   **Then** `CustomerControllerTest.java` is created (or enhanced if exists)

2. **And** tests verify `GET /api/v1/customers/{id}` returns 200 with valid data

3. **And** tests verify 404 response for non-existent customer

4. **And** tests verify response format matches `{ data: {...}, meta: {...} }`

5. **And** tests verify error format matches RFC 7807

6. **And** tests use @WebMvcTest for isolated controller testing

## Tasks / Subtasks

- [ ] Task 1: Assess existing CustomerControllerTest.java (AC: #1, #6)
  - [ ] Read current test file (integration test with AbstractIntegrationTest)
  - [ ] Understand difference: existing uses full Spring context, story wants @WebMvcTest isolation
  - [ ] Decision: Create CustomerControllerMockMvcTest.java or refactor existing
  - [ ] Document that existing test is integration-level, new test is unit-level

- [ ] Task 2: Create isolated controller test with @WebMvcTest (AC: #2, #4, #6)
  - [ ] Use @WebMvcTest(CustomerController.class) to load only web layer
  - [ ] Mock CustomerService with @MockBean
  - [ ] Test GET /api/v1/customers/{id} returns ApiResponse<CustomerDTO> with 200 OK
  - [ ] Verify JSON response structure: $.data.customerId, $.data.customerName, $.meta

- [ ] Task 3: Implement error scenario tests (AC: #3, #5)
  - [ ] Test 404 response when service throws CustomerNotFoundException
  - [ ] Test 400 response when service throws InvalidCustomerIdException
  - [ ] Verify RFC 7807 ProblemDetail format: $.status, $.title, $.detail

- [ ] Task 4: Add RPGLE traceability and documentation (AC: all)
  - [ ] Add Javadoc referencing RPGLE CUST001 operations
  - [ ] Add @DisplayName annotations with RPGLE context
  - [ ] Document test scope (controller layer isolation vs integration tests)

- [ ] Task 5: Run tests and verify coverage (AC: all)
  - [ ] Run `mvn test` - all unit tests pass
  - [ ] Verify controller layer has 100% coverage (isolated tests)
  - [ ] Verify tests run faster than integration tests (no database/Testcontainers)

## Dev Notes

### 🔥 CRITICAL DISCOVERY: CustomerControllerTest Already Exists But Wrong Type!

**Current State Analysis:**
- ✅ File EXISTS: `backend/src/test/java/com/smeup/backend/controller/CustomerControllerTest.java`
- ❌ **BUT**: It's an **integration test**, not an isolated controller test!

**Current Test Characteristics:**
```java
class CustomerControllerTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository; // Direct repository access!

    private RestTemplate restTemplate; // Real HTTP calls!
}
```

**What makes it an integration test:**
1. Extends `AbstractIntegrationTest` (Testcontainers PostgreSQL)
2. Uses `@LocalServerPort` (real embedded server)
3. Injects `CustomerRepository` directly
4. Uses `RestTemplate` for real HTTP calls
5. Tests full stack: Controller → Service → Repository → Database

**Story 4.3 Requirements (Isolated Controller Test):**
```java
@WebMvcTest(CustomerController.class)  // Load ONLY controller
class CustomerControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;  // Mock HTTP layer, no real server

    @MockBean
    private CustomerService customerService;  // Mock service, no repository/DB
}
```

**What makes it an isolated test:**
1. `@WebMvcTest` loads only web layer (no service, no repository, no DB)
2. `MockMvc` mocks HTTP without starting real server
3. `@MockBean` for CustomerService - test focuses on controller logic
4. Fast execution (no Testcontainers, no database)
5. Tests only: HTTP → Controller → Response (service is mocked)

### Story 4.3 Options

**Option 1: Rename existing, create new isolated test** (RECOMMENDED)
- Rename `CustomerControllerTest.java` → `CustomerControllerIT.java`
- Create new `CustomerControllerTest.java` with @WebMvcTest
- **Rationale:** Current test is integration-level (belongs with *IT.java pattern)
- **Benefits:** Clear separation, follows Maven naming convention

**Option 2: Keep existing, create additional test**
- Keep `CustomerControllerTest.java` as-is (integration test)
- Create `CustomerControllerMockMvcTest.java` with @WebMvcTest
- **Rationale:** Both test types have value
- **Benefits:** No breaking changes, additive approach

**Option 3: Convert existing test to @WebMvcTest**
- Replace AbstractIntegrationTest with @WebMvcTest
- Remove repository injection, mock service instead
- **Rationale:** Story says "create controller tests"
- **Drawbacks:** Loses existing integration test coverage

**Recommended Approach: Option 1**
1. Rename current test to match integration test pattern (*IT.java)
2. Create proper isolated controller test for Story 4.3
3. Document both test types in project (unit vs integration)

### What Story 4.2 Already Provides

**Story 4.2 (CustomerIntegrationIT.java):**
- Full-stack integration tests with Testcontainers
- Tests complete flow: HTTP → Controller → Service → Repository → PostgreSQL
- MockMvc-based (but with full Spring context via webAppContextSetup)
- 5 comprehensive tests covering all endpoints and error scenarios

**Story 4.3 (This story - CustomerControllerTest.java - @WebMvcTest):**
- **Unit-level** controller tests with mocked service
- Tests controller layer ONLY: HTTP request → Controller logic → Response
- MockMvc-based but **isolated** (no service/repository loaded)
- Fast execution, no database required

**Existing CustomerControllerTest.java (Integration test):**
- Full-stack with real server on random port
- RestTemplate for real HTTP calls
- Direct repository access
- Belongs with integration test naming (*IT.java)

**All Three Test Types Have Value:**
| Test Type | Focus | Dependencies | Speed | Coverage |
|-----------|-------|--------------|-------|----------|
| Unit (Story 4.3) | Controller isolation | Service mocked | Fastest | Controller logic only |
| Integration IT (existing) | Full HTTP stack | Real service/repo/DB | Slow | End-to-end HTTP flow |
| Integration IT (Story 4.2) | API contract | Real service/repo/DB | Slow | API + persistence |

### Testing Anti-Patterns to Avoid

**❌ DON'T create duplicate integration tests:**
- Story 4.2 already has full-stack tests (CustomerIntegrationIT.java)
- Story 4.3 should focus on **isolated** controller tests with @WebMvcTest
- Existing CustomerControllerTest.java should be renamed to *IT.java pattern

**❌ DON'T test service logic in controller tests:**
- CustomerServiceTest.java (Story 4.1) already validates service layer
- Controller tests should mock service and test only:
  - HTTP request mapping
  - Response formatting
  - Exception translation to HTTP status codes

**✅ DO test controller-specific concerns:**
- Path variable extraction (`/customers/{id}` → `Long id`)
- Response wrapper structure (`ApiResponse<CustomerDTO>`)
- HTTP status code mapping (200, 404, 400)
- Global exception handler integration (`@ControllerAdvice`)

**✅ DO use @WebMvcTest for fast feedback:**
- No Testcontainers (no Docker overhead)
- No database (no Flyway migrations)
- No service layer (mocked with Mockito)
- Tests run in milliseconds

### @WebMvcTest vs Full Integration Test

**@WebMvcTest (Story 4.3 - Unit Test):**
```java
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;  // Mocked!

    @Test
    void shouldReturn200() throws Exception {
        // Given - mock service behavior
        when(customerService.findCustomerById(1L))
            .thenReturn(new Customer(...));

        // When/Then - test only controller
        mockMvc.perform(get("/api/v1/customers/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.customerId").value(1));

        // Verify controller called service correctly
        verify(customerService).findCustomerById(1L);
    }
}
```

**Integration Test (Story 4.2/existing - Integration Test):**
```java
class CustomerIntegrationIT extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;  // Full Spring context!

    @Autowired
    private JdbcTemplate jdbcTemplate;  // Real database!

    @Test
    void shouldReturn200() throws Exception {
        // Given - insert real data via JDBC
        jdbcTemplate.update("INSERT INTO CUSTMAST...");

        // When/Then - test full stack
        mockMvc.perform(get("/api/v1/customers/12345"))
            .andExpect(status().isOk());

        // Verify data persisted in database
        var dbResult = jdbcTemplate.queryForMap("SELECT...");
    }
}
```

**Key Differences:**
| Aspect | @WebMvcTest (Unit) | Integration Test |
|--------|-------------------|------------------|
| Loads | Controller only | Full Spring context |
| Service | Mocked | Real implementation |
| Database | None | PostgreSQL (Testcontainers) |
| Speed | ~50ms | ~3000ms |
| Purpose | Controller logic | End-to-end validation |

### Project Context Alignment

**From project-context.md (Lines 82-89):**
```
Testing Rules:
- Unit tests: JUnit 5 + Mockito (Java), Vitest + RTL (React)
- Integration tests: Testcontainers with real PostgreSQL
- E2E tests: Playwright for full user flows
- Test files co-located with source in __tests__/ folders
- 80%+ code coverage required
```

**Story 4.3 is a UNIT TEST:**
- Uses JUnit 5 + Mockito ✅
- Mocks CustomerService ✅
- No Testcontainers (that's for integration tests) ✅
- Fast execution for rapid feedback ✅

**Test Coverage Strategy:**
```
Story 4.1 (Unit): CustomerService logic          → 98% service coverage
Story 4.2 (Integration): Full-stack API flow     → End-to-end validation
Story 4.3 (Unit): Controller HTTP mapping        → 100% controller coverage
Existing (Integration): Alternative full-stack   → Redundant with Story 4.2
```

### RPGLE CUST001 Controller Test Mapping

**Original RPGLE Controller Logic:**
```rpgle
* CUST001 - Customer Inquiry Program
* Controller-equivalent logic in RPGLE:
*
* 1. Accept CUSTNO from PROMPT screen (parameter input)
* 2. Call CHAIN subroutine (service layer)
* 3. IF %FOUND - Display DETAIL screen (200 OK response)
* 4. IF NOT %FOUND - Display error message (404 response)
* 5. IF CUSTNO invalid - Display error (400 response)
```

**Controller Test Validates:**
| RPGLE Operation | Controller Responsibility | Test Validates |
|-----------------|--------------------------|----------------|
| Accept CUSTNO parameter | `@PathVariable Long id` | Path param extraction |
| Call CHAIN subroutine | `customerService.findCustomerById()` | Service method called |
| Display DETAIL screen | Return `ApiResponse<CustomerDTO>` | Response wrapper format |
| Display error message (not found) | Catch `CustomerNotFoundException` | 404 status code |
| Display error (invalid) | Catch `InvalidCustomerIdException` | 400 status code |

**Service Layer is MOCKED** because:
- Service logic already tested in CustomerServiceTest (Story 4.1)
- Controller test focuses on HTTP concerns (request → response mapping)
- Faster feedback loop without database dependencies

### Dependencies and Story Order

**Depends On (All Complete):**
- ✅ Story 2.5: CustomerController (REST endpoint implementation)
- ✅ Story 2.4: CustomerService (business logic, will be mocked)
- ✅ Story 2.6: CustomerDTO/ApiResponse (response format)
- ✅ Story 4.1: Unit Tests (established Mockito testing pattern)

**Blocks:**
- 🔒 Story 4.6: Functional Equivalence Validation (needs all test types complete)

**No blockers - Story 4.3 is ready for implementation!**

### Git Intelligence (From Previous Stories)

**Recent Test Patterns:**
```
Story 4.1: CustomerServiceTest.java (unit test with Mockito)
Story 4.2: CustomerIntegrationIT.java (full-stack with Testcontainers)
```

**File Naming Convention:**
- Unit tests: `*Test.java` (runs in `test` phase via Surefire)
- Integration tests: `*IT.java` (runs in `verify` phase via Failsafe)
- Current CustomerControllerTest.java violates convention (is integration, should be *IT)

**Commit Message Pattern (From Story 4.2):**
```
✨ feat: Implement full-stack integration tests for Customer API using Testcontainers
✨ feat: Update sprint status for Epic 4 and Story 4.2
```

### Success Criteria

**Story marked "done" when:**
1. ✅ Decision documented: Rename existing test or create new
2. ✅ CustomerControllerTest.java uses @WebMvcTest(CustomerController.class)
3. ✅ CustomerService is mocked with @MockBean
4. ✅ Test validates GET /customers/{id} returns 200 with ApiResponse format
5. ✅ Test validates 404 for non-existent customer (service returns empty)
6. ✅ Test validates 400 for invalid customer ID (service throws exception)
7. ✅ Tests verify RFC 7807 ProblemDetail format for errors
8. ✅ RPGLE traceability in Javadoc and @DisplayName
9. ✅ All unit tests pass: `mvn test`
10. ✅ Controller layer achieves 100% coverage (isolated from service)
11. ✅ Tests execute in under 1 second (no Testcontainers overhead)
12. ✅ Code style passes: `mvn checkstyle:check`

## Dev Agent Record

### Agent Model Used

_To be completed by Dev Agent implementing this story._

### Debug Log References

_To be completed during implementation._

### Completion Notes List

_To be completed when story moves to "done" status._

### File List

**Files to Create/Update:**
- `backend/src/test/java/com/smeup/backend/controller/CustomerControllerTest.java` - Isolated controller unit tests with @WebMvcTest

**Decision Required:**
- Option A: Rename existing CustomerControllerTest.java to CustomerControllerIT.java, create new unit test
- Option B: Keep existing, create CustomerControllerMockMvcTest.java
- Option C: Convert existing to @WebMvcTest (loses integration test)

**Files to Update:**
- `_bmad-output/implementation-artifacts/sprint-status.yaml` - Story status: "backlog" → "ready-for-dev" → "in-progress" → "review" → "done"
- This story file - Add completion notes

**Files to Verify (No Changes Expected):**
- `backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java` - Service unit tests
- `backend/src/test/java/com/smeup/backend/integration/CustomerIntegrationIT.java` - Full-stack integration tests
- `backend/src/main/java/com/smeup/backend/controller/CustomerController.java` - Controller implementation

## References

**Epic Source:**
- [Source: _bmad-output/planning-artifacts/epics.md - Epic 4, Story 4.3, Lines 539-555]

**Previous Story Intelligence:**
- [Source: _bmad-output/implementation-artifacts/4-1-create-backend-unit-tests-for-customer-service.md - Mockito unit test patterns]
- [Source: _bmad-output/implementation-artifacts/4-2-create-backend-integration-tests-with-testcontainers.md - MockMvc usage patterns]

**Existing Test Files:**
- [Source: backend/src/test/java/com/smeup/backend/controller/CustomerControllerTest.java - Current integration test]
- [Source: backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java - Service unit test pattern]
- [Source: backend/src/test/java/com/smeup/backend/integration/CustomerIntegrationIT.java - Integration test pattern]

**Implementation Dependencies:**
- [Source: backend/src/main/java/com/smeup/backend/controller/CustomerController.java - REST controller to test]
- [Source: backend/src/main/java/com/smeup/backend/service/CustomerService.java - Service to mock]
- [Source: backend/src/main/java/com/smeup/backend/exception/GlobalExceptionHandler.java - Exception translation]

**Architecture Standards:**
- [Source: _bmad-output/planning-artifacts/architecture.md - Testing strategy, @WebMvcTest pattern]
- [Source: _bmad-output/project-context.md - Lines 82-89: Testing rules, Lines 65-71: Java/Spring rules]
