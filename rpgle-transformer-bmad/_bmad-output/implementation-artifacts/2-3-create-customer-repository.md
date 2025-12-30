# Story 2.3: Create Customer Repository

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **a Spring Data JPA repository for Customer entity**,
so that **I can perform database operations on customer data**.

## Acceptance Criteria

1. **Given** the Customer entity and database table exist
   **When** I create the repository
   **Then** `CustomerRepository.java` is created extending JpaRepository

2. **And** custom query method `findByCustomerId(Long customerId)` exists
   _(Note: customerId is Long, not String - corrected from epic based on entity)_

3. **And** inline comments reference the original RPGLE CHAIN operation

4. **And** repository integrates correctly with Spring Data JPA

5. **And** repository tests verify CRUD operations work with real database

## Tasks / Subtasks

- [x] Task 1: Create CustomerRepository interface (AC: #1, #3)
  - [x] Create `CustomerRepository.java` in `com.smeup.backend.repository` package
  - [x] Extend `JpaRepository<Customer, Long>`
  - [x] Add header comment referencing RPGLE CHAIN operation
  - [x] Follow project package structure conventions

- [x] Task 2: Add custom query methods (AC: #2, #3)
  - [x] Add `Optional<Customer> findByCustomerId(Long customerId)` method
  - [x] Add inline comment referencing RPGLE CHAIN CUSTMAST operation
  - [x] Added `findByCustomerNameContainingIgnoreCase` for search functionality

- [x] Task 3: Create repository unit tests (AC: #4, #5)
  - [x] Create `CustomerRepositoryTest.java` in test directory
  - [x] Test save and findById operations
  - [x] Test findByCustomerId custom query
  - [x] Use `@DataJpaTest` for repository-only testing
  - [x] Verify Flyway migration runs before tests

- [x] Task 4: Create repository integration tests (AC: #5)
  - [x] Add repository tests to existing IT infrastructure
  - [x] Verify entity persists correctly to CUSTMAST table
  - [x] Test with real PostgreSQL via Testcontainers

- [x] Task 5: Verify all tests pass (AC: all)
  - [x] Run `mvn test` - all 34 unit tests pass
  - [x] Run `mvn verify` - all 11 integration tests pass
  - [x] Checkstyle passes

## Dev Notes

### Spring Data JPA Repository Pattern

Spring Data JPA provides automatic implementation of repository interfaces. The `JpaRepository` interface includes:

- `save(S entity)` - Save/update entity
- `findById(ID id)` - Find by primary key (returns Optional)
- `findAll()` - Get all entities
- `deleteById(ID id)` - Delete by primary key
- `count()` - Count total entities

### Repository Interface Template

```java
package com.smeup.backend.repository;

import com.smeup.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Customer entity operations.
 *
 * Original RPGLE Operations:
 * - CHAIN CUSTMAST (Record lookup by key)
 * - READ CUSTMAST (Sequential read)
 *
 * This repository provides modern Spring Data JPA equivalents:
 * - findByCustomerId() maps to CHAIN operation
 * - findAll() maps to READ operation
 *
 * Source: RPGLE programs use CHAIN opcode to retrieve customer records
 *         from CUSTMAST physical file by customer number.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find customer by customer ID.
     *
     * RPGLE Equivalent: CHAIN CUSTMAST
     * - Retrieves a single record by key (CUSTNO)
     * - Returns empty Optional if not found (like CHAIN with %FOUND check)
     *
     * @param customerId the customer number (CUSTNO field)
     * @return Optional containing the customer if found
     */
    Optional<Customer> findByCustomerId(Long customerId);

}
```

### Package Structure

```
backend/src/main/java/com/smeup/backend/
├── BackendApplication.java     (✅ exists)
├── entity/
│   └── Customer.java           (✅ exists - Story 2-1)
└── repository/                  (🆕 create in this story)
    └── CustomerRepository.java  (🆕 create in this story)

backend/src/test/java/com/smeup/backend/
├── entity/
│   ├── CustomerTest.java        (✅ exists)
│   └── CustomerJpaTest.java     (✅ exists)
└── repository/                   (🆕 create in this story)
    └── CustomerRepositoryTest.java (🆕 create in this story)
```

### Testing Strategy

**Unit Tests with @DataJpaTest:**
```java
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldSaveAndFindCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(12345L);
        customer.setCustomerName("Test Customer");

        // When
        customerRepository.save(customer);
        Optional<Customer> found = customerRepository.findByCustomerId(12345L);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getCustomerName()).isEqualTo("Test Customer");
    }
}
```

**Integration Tests with Testcontainers:**
- Extend `AbstractIntegrationTest` for PostgreSQL container
- Verify entity persists with correct column names

### Previous Story Intelligence (Story 2-2)

**Key Learnings:**
- ✅ Flyway migration V1__create_custmast.sql exists and runs correctly
- ✅ CUSTMAST table created with all DDS columns
- ✅ `spring-boot-starter-flyway` is required for Spring Boot 4
- ⚠️ **Known Issue**: Integration tests fail when run together due to Spring context caching
  - **Workaround**: Run ITs individually or use `@DirtiesContext`
- ✅ Unit tests work with H2 + Flyway
- ✅ Checkstyle validates code style

**From Story 2-1:**
- Customer entity uses `Long customerId` as primary key
- Primary key is NOT auto-generated (preserves AS/400 IDs)
- Entity has proper `@Column(name = "CUSTNO")` mapping

### RPGLE CHAIN Operation Reference

In RPGLE, the CHAIN operation retrieves a record by key:

```rpgle
C     CUSTNO    CHAIN     CUSTMAST
C                   IF        %FOUND(CUSTMAST)
C                   ...       (record found processing)
C                   ELSE
C                   ...       (not found processing)
C                   ENDIF
```

Spring Data JPA equivalent:
- `findByCustomerId(Long id)` returns `Optional<Customer>`
- Empty Optional = record not found (like `%FOUND = *OFF`)
- Present Optional = record found (like `%FOUND = *ON`)

### References

- [Source: backend/src/main/java/com/smeup/backend/entity/Customer.java] - JPA entity with column mappings
- [Source: backend/src/main/resources/db/migration/V1__create_custmast.sql] - Database schema
- [Source: _bmad-output/planning-artifacts/architecture.md] - Repository conventions
- [Source: _bmad-output/project-context.md] - Package structure rules
- [Source: _bmad-output/implementation-artifacts/2-2-create-flyway-migration-for-custmast-table.md] - Previous story learnings

## Dev Agent Guardrails

### CRITICAL: Package Structure

**YOU MUST:**
- Create repository in `com.smeup.backend.repository` package
- Create test in `com.smeup.backend.repository` test package
- Use `@Repository` annotation on the interface

**YOU MUST NOT:**
- Put repository in entity package ❌
- Create repository as a class (it's an interface) ❌
- Skip the `@Repository` annotation ❌

### CRITICAL: Method Signatures

**YOU MUST:**
- Use `Long` for customerId parameter (matches entity)
- Return `Optional<Customer>` for findByCustomerId
- Name method exactly `findByCustomerId` (Spring Data JPA naming convention)

**YOU MUST NOT:**
- Use `String` for customerId (entity uses Long) ❌
- Return bare `Customer` without Optional ❌
- Use incorrect method names that don't follow Spring Data conventions ❌

### CRITICAL: RPGLE Reference Comments

**YOU MUST:**
- Include header comment explaining CHAIN operation mapping
- Add method-level comments referencing RPGLE opcodes
- Explain what the Spring Data equivalent achieves

**YOU MUST NOT:**
- Skip inline RPGLE reference comments ❌
- Write generic comments without RPGLE context ❌

### CRITICAL: Testing Requirements

**YOU MUST:**
- Use `@DataJpaTest` for repository unit tests
- Test both save and find operations
- Test the custom `findByCustomerId` method
- Verify Flyway migrations run in tests

**YOU MUST NOT:**
- Skip repository tests ❌
- Test only inherited JpaRepository methods ❌
- Use `@SpringBootTest` when `@DataJpaTest` suffices ❌

### CRITICAL: Integration Test Workaround

**YOU MUST:**
- If adding integration tests, consider the known context caching issue from Story 2-2
- Either run ITs individually or use `@DirtiesContext(classMode = ClassMode.AFTER_CLASS)`
- Document any workarounds applied

**YOU MUST NOT:**
- Ignore the known IT issue ❌
- Add ITs that will fail when run together ❌

## Dev Agent Record

### Agent Model Used

Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References

N/A

### Completion Notes List

1. **CustomerRepository Created**: Spring Data JPA repository interface with comprehensive RPGLE CHAIN operation documentation in Javadoc.

2. **Custom Query Methods Added**:
   - `findByCustomerId(Long)` - Maps to RPGLE CHAIN CUSTMAST operation
   - `findByCustomerNameContainingIgnoreCase(String)` - Search functionality for customer lookup

3. **Unit Tests (10 tests)**:
   - Nested test classes for each operation type (save, findByCustomerId, findByName, findById, delete, findAll)
   - All tests use `@DataJpaTest` with H2 in-memory database
   - Tests reference RPGLE operation equivalents in `@DisplayName`

4. **Integration Tests (4 tests)**:
   - Tests run against real PostgreSQL via Testcontainers
   - Verify JDBC direct insert/query works with repository
   - Test BigDecimal precision for financial fields

5. **Spring Boot 4 Fix**: Added `spring-boot-data-jpa-test` dependency to pom.xml - required for `@DataJpaTest` annotation which moved to `org.springframework.boot.data.jpa.test.autoconfigure` package.

6. **CRITICAL FIX - Singleton Container Pattern**: Fixed the integration test context caching issue from Story 2-2:
   - **Root Cause**: `@Container` + `@Testcontainers` stops container after each test class, but Spring caches context pointing to dead container
   - **Solution**: Removed `@Testcontainers` and `@Container` annotations, using static block to start container once
   - **Reference**: [Testcontainers Spring Boot Setup](https://maciejwalkowiak.com/blog/testcontainers-spring-boot-setup/)
   - **Result**: All 11 ITs now pass when run together

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created with comprehensive context | SM Agent (Claude Opus 4.5) |
| 2025-12-30 | Story implemented - repository, tests, and IT container fix | Dev Agent (Claude Opus 4.5) |

### File List

**Created:**
- `backend/src/main/java/com/smeup/backend/repository/CustomerRepository.java`
- `backend/src/test/java/com/smeup/backend/repository/CustomerRepositoryTest.java`
- `backend/src/test/java/com/smeup/backend/repository/CustomerRepositoryIT.java`

**Modified:**
- `backend/pom.xml` - Added `spring-boot-data-jpa-test` dependency
- `backend/src/test/java/com/smeup/backend/AbstractIntegrationTest.java` - Singleton container pattern
- `backend/src/test/java/com/smeup/backend/migration/FlywayMigrationIT.java` - Removed @DirtiesContext
