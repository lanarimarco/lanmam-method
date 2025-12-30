# Story 2.1: Create Customer Entity from DDS Physical File

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **a JPA Customer entity generated from CUSTMAST.dds DDS physical file**,
so that **I have a database model that maps to the original AS/400 data structure**.

## Acceptance Criteria

1. **Given** CUSTMAST.dds DDS physical file exists in source-rpgle/dds/physical-files/
   **When** I analyze the DDS file and generate the JPA entity
   **Then** a `Customer.java` entity class is created in backend/src/main/java/com/smeup/backend/entity/

2. **And** `@Table(name = "CUSTMAST")` annotation uses the DDS physical file name

3. **And** `@Column(name = "CUSTID")` annotations use original DDS field names

4. **And** Java field names use readable camelCase (e.g., `customerId`, `customerName`)

5. **And** inline comments reference the original DDS file and field definitions

## Tasks / Subtasks

- [x] Task 1: Create entity package structure (AC: #1)
  - [x] Create `backend/src/main/java/com/smeup/backend/entity/` directory
  - [x] Verify package structure follows established patterns from Epic 1

- [x] Task 2: Analyze CUSTMAST.dds DDS file (AC: #1, #2, #3, #4, #5)
  - [x] Locate and read `source-rpgle/dds/physical-files/CUSTMAST.dds`
  - [x] Extract field definitions (names, types, lengths, constraints)
  - [x] Map DDS field types to Java/JPA types
  - [x] Identify primary key field(s)

- [x] Task 3: Create Customer.java entity (AC: #1, #2, #3, #4, #5)
  - [x] Create `Customer.java` in entity package
  - [x] Add `@Entity` annotation
  - [x] Add `@Table(name = "CUSTMAST")` annotation
  - [x] Define fields with `@Column(name = "DDS_FIELD_NAME")` annotations
  - [x] Use readable camelCase for Java field names
  - [x] Add `@Id` annotation for primary key
  - [x] Add inline comments referencing DDS file and fields
  - [x] Add proper JavaDoc header comment
  - [x] Generate getters/setters or use Lombok if appropriate

- [x] Task 4: Verify code quality (AC: all)
  - [x] Run Checkstyle validation (`mvn checkstyle:check`)
  - [x] Verify Google Java Style compliance
  - [x] Ensure no Checkstyle violations

- [x] Task 5: Create basic entity test (AC: all)
  - [x] Create `CustomerTest.java` in `backend/src/test/java/com/smeup/backend/entity/`
  - [x] Test entity instantiation
  - [x] Test getters/setters
  - [x] Verify field mappings
  - [x] Run tests with `mvn test` (uses H2, not Docker)

## Dev Notes

### CRITICAL: Database ↔ Java Entity Mapping Pattern

This is the **KEY ARCHITECTURAL PATTERN** that must be followed for ALL entity mappings in this project:

| Element | Convention | Example |
|---------|------------|---------|
| **SQL Table** | DDS Physical File name (UPPERCASE) | `CUSTMAST` |
| **Java Entity** | Readable PascalCase | `Customer` |
| **SQL Column** | DDS Field name (UPPERCASE) | `CUSTID`, `CUSTNM` |
| **Java Field** | Readable camelCase | `customerId`, `customerName` |

**Principle:** Code = Readable (for Java/React developers), SQL = DDS names (for traceability)

### DDS Physical File Location

The CUSTMAST.dds file should be located at:
```
source-rpgle/dds/physical-files/CUSTMAST.dds
```

**Note:** If this file doesn't exist yet, you'll need to create a sample DDS structure for the MVP. A typical customer master file would include fields like:
- CUSTID (Customer ID - Primary Key)
- CUSTNM (Customer Name)
- CUSTAD (Customer Address)
- CUSTCT (Customer City)
- CUSTST (Customer State)
- CUSTZI (Customer ZIP)

### Entity Example Pattern

```java
package com.smeup.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Customer entity mapped from DDS Physical File CUSTMAST.dds
 * 
 * Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 * Table: CUSTMAST
 * 
 * This entity preserves the original AS/400 data structure while providing
 * a modern Java interface for the application.
 */
@Entity
@Table(name = "CUSTMAST")  // DDS Physical File name
public class Customer implements Serializable {

    @Id
    @Column(name = "CUSTID")  // DDS field: CUSTID
    private Long customerId;  // Readable field name

    @Column(name = "CUSTNM", length = 50)  // DDS field: CUSTNM
    private String customerName;

    @Column(name = "CUSTAD", length = 100)  // DDS field: CUSTAD
    private String customerAddress;

    // Getters and setters...
}
```

### DDS to JPA Type Mapping

| DDS Type | Java Type | JPA Annotation |
|----------|-----------|----------------|
| Numeric (packed decimal) | `Long` or `BigDecimal` | `@Column` |
| Character | `String` | `@Column(length=X)` |
| Date | `LocalDate` | `@Column` |
| Timestamp | `LocalDateTime` | `@Column` |

### Package Structure

Following the structure established in Epic 1:

```
backend/src/main/java/com/smeup/backend/
├── BackendApplication.java  (✅ exists from Epic 1)
└── entity/                   (🆕 create in this story)
    └── Customer.java         (🆕 create in this story)
```

### Testing Strategy

**Unit Tests (this story):**
- Test entity instantiation and field access
- Uses H2 in-memory database (no Docker required)
- Run with: `mvn test`

**Integration Tests (future story):**
- Will test with real PostgreSQL via Testcontainers
- Will be created in Story 2-2 or 2-3 after Flyway migration exists

### Learning from Epic 1

**From Story 1-1 (Spring Boot Init):**
- ✅ Follow exact package structure: `com.smeup.backend`
- ✅ Use Google Java Style (Checkstyle configured)
- ⚠️ Ensure all dependencies are in pom.xml (JPA already included)

**From Story 1-3 (Dev Environment):**
- ✅ PostgreSQL for development (Docker Compose)
- ✅ H2 for unit tests (no Docker dependency)
- ⚠️ CRITICAL: Tests in this story use H2, NOT PostgreSQL

**From Story 1-4 (Testing Infrastructure):**
- ✅ Unit tests: `*Test.java` → H2
- ✅ Integration tests: `*IT.java` → Testcontainers + PostgreSQL
- ✅ `AbstractIntegrationTest` available for future integration tests

**From Story 1-5 (Code Quality):**
- ✅ Checkstyle (Google Java Style) will validate code
- ✅ Run `mvn checkstyle:check` before marking done

### Critical Architecture Compliance

From [Source: _bmad-output/planning-artifacts/architecture.md#Naming-Patterns]:

**MUST follow these naming conventions:**
1. `@Table(name = "CUSTMAST")` - DDS Physical File name (UPPERCASE)
2. `@Column(name = "CUSTID")` - DDS field names (UPPERCASE)
3. Class name: `Customer` - Readable PascalCase
4. Field names: `customerId` - Readable camelCase

**MUST include:**
- JavaDoc header comment referencing DDS file
- Inline comments for each field referencing DDS field name
- Proper JPA annotations (`@Entity`, `@Table`, `@Id`, `@Column`)

### References

- [Source: _bmad-output/planning-artifacts/epics.md#Epic-2-Story-2.1]
- [Source: _bmad-output/planning-artifacts/architecture.md#Naming-Patterns]
- [Source: _bmad-output/planning-artifacts/architecture.md#Database-Architecture]
- [Source: _bmad-output/implementation-artifacts/1-1-initialize-spring-boot-backend-project.md]
- [Source: _bmad-output/implementation-artifacts/1-3-configure-development-environment.md]
- [Source: _bmad-output/implementation-artifacts/1-4-configure-testing-infrastructure.md]

## Dev Agent Guardrails

### CRITICAL: Naming Convention Enforcement

**YOU MUST:**
- Use `@Table(name = "CUSTMAST")` exactly (DDS file name, UPPERCASE)
- Use `@Column(name = "CUSTID")` exactly (DDS field names, UPPERCASE)
- Use `Customer` for class name (readable, PascalCase)
- Use `customerId` for field names (readable, camelCase)

**YOU MUST NOT:**
- Use lowercase table names (e.g., `@Table(name = "custmast")`) ❌
- Use readable names in SQL (e.g., `@Column(name = "customer_id")`) ❌
- Use DDS names in Java (e.g., `private Long CUSTID`) ❌

### CRITICAL: Testing Requirements

**YOU MUST:**
- Create unit tests using H2 (in-memory)
- Run tests with `mvn test` (no Docker required)
- Verify tests pass before marking story done

**YOU MUST NOT:**
- Create integration tests in this story (wait for Story 2-2)
- Depend on Docker/PostgreSQL for unit tests ❌
- Skip testing ❌

### CRITICAL: Code Quality Requirements

**YOU MUST:**
- Follow Google Java Style (Checkstyle will enforce)
- Run `mvn checkstyle:check` and fix all violations
- Include proper JavaDoc comments
- Reference DDS file in comments

**YOU MUST NOT:**
- Ignore Checkstyle violations ❌
- Skip inline DDS reference comments ❌
- Use generic or placeholder comments ❌

### CRITICAL: Package Structure

**YOU MUST:**
- Create entity in `backend/src/main/java/com/smeup/backend/entity/`
- Follow package structure from Epic 1
- Use singular package names (`entity`, not `entities`)

**YOU MUST NOT:**
- Create entity in wrong package ❌
- Use plural package names ❌
- Deviate from established structure ❌

## Senior Developer Review (AI)

**Review Date:** 2025-12-30  
**Reviewer:** Senior Developer (Claude Sonnet 4.5 - Adversarial Mode)  
**Outcome:** ✅ **APPROVED** (after fixes applied)

### Review Summary

**Total Issues Found:** 10 (5 High, 3 Medium, 2 Low)  
**Issues Fixed:** 8 (all High and Medium issues)  
**Remaining:** 2 Low-priority issues (acceptable for merge)

### Action Items

- [x] **[HIGH]** Add `equals()` and `hashCode()` methods based on `customerId` (Customer.java:220-236)
- [x] **[HIGH]** Document manual ID assignment strategy (Customer.java:38-40)
- [x] **[HIGH]** Add Jakarta Bean Validation annotations (@NotNull, @NotBlank, @Size, @Pattern, @Min, @Max) (Customer.java:multiple)
- [x] **[HIGH]** Add JPA integration tests for equals/hashCode and BigDecimal precision (CustomerJpaTest.java:created)
- [x] **[HIGH]** Fix constructor indentation to 2 spaces (Customer.java:107-109)
- [x] **[MEDIUM]** Add nullable constraints to @Column annotations (Customer.java:48)
- [x] **[MEDIUM]** Add tests for BigDecimal precision/scale validation (CustomerJpaTest.java:92-115)
- [x] **[MEDIUM]** Enhance toString() to include all fields for better debugging (Customer.java:193-206)
- [ ] **[LOW]** Add Javadoc to getters/setters (deferred - not blocking)
- [ ] **[LOW]** Consider BDD-style test naming (deferred - current names acceptable)

### Files Modified During Review

1. **Customer.java**
   - Added: `equals()` and `hashCode()` methods
   - Added: Jakarta validation annotations (@NotNull, @NotBlank, @Size, @Pattern, @Min, @Max)
   - Added: Documentation explaining manual ID assignment
   - Added: `nullable = false` constraint on customerName
   - Enhanced: `toString()` to include all 10 fields
   - Fixed: Constructor indentation
   - Added: `Objects` import for equals/hashCode

2. **CustomerJpaTest.java** (NEW)
   - Created comprehensive test suite (9 tests)
   - Tests: equals/hashCode behavior
   - Tests: BigDecimal precision and scale preservation
   - Tests: toString completeness
   - Tests: Serializable interface compliance

### Test Results After Fixes

```
Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
- BackendApplicationTests: 1 test
- CustomerJpaTest: 9 tests  
- CustomerTest: 14 tests
```

**Code Quality:**
- ✅ Checkstyle: 0 violations
- ✅ All tests passing
- ✅ All acceptance criteria met
- ✅ All HIGH and MEDIUM issues resolved

### Acceptance Criteria Validation

- ✅ AC#1: Customer.java created in `backend/src/main/java/com/smeup/backend/entity/`
- ✅ AC#2: `@Table(name = "CUSTMAST")` uses DDS physical file name
- ✅ AC#3: `@Column(name = "CUSTNO")` annotations use original DDS field names
- ✅ AC#4: Java field names use readable camelCase (customerId, customerName)
- ✅ AC#5: Inline comments reference original DDS file and field definitions

**All acceptance criteria fully implemented and validated.**

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5 (claude-sonnet-4-20250514)

### Debug Log References

- DDS File analyzed: source-rpgle/dds/physical-files/CUSTMAST.dds
- Maven test execution: `mvn test -Dtest=CustomerTest` - 14 tests passed
- Checkstyle validation: `mvn checkstyle:check` - 0 violations
- Test framework: JUnit 5 with H2 in-memory database

### Completion Notes List

- ✅ Successfully created Customer JPA entity following critical naming pattern
- ✅ @Table(name = "CUSTMAST") uses DDS physical file name (UPPERCASE)
- ✅ @Column annotations use original DDS field names (CUSTNO, CUSTNAME, etc.)
- ✅ Java field names use readable camelCase (customerId, customerName, etc.)
- ✅ Comprehensive JavaDoc header referencing original DDS file
- ✅ All 10 DDS fields mapped to appropriate Java types:
  - CUSTNO (5P 0) → Long customerId
  - CUSTNAME (30A) → String customerName  
  - ADDR1 (30A) → String addressLine1
  - CITY (20A) → String city
  - STATE (2A) → String state
  - ZIP (5P 0) → Integer zipCode
  - PHONE (12A) → String phoneNumber
  - BALANCE (9P 2) → BigDecimal accountBalance
  - CREDITLIM (9P 2) → BigDecimal creditLimit
  - LASTORDER (8P 0) → Integer lastOrderDate
- ✅ Created comprehensive unit tests (14 test cases)
- ✅ All tests pass using H2 in-memory database (no Docker dependency)
- ✅ Code quality verified: 0 Checkstyle violations
- ✅ Follows Google Java Style guide
- ✅ Package structure follows Epic 1 patterns (com.smeup.backend.entity)

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created with comprehensive context | SM Agent (Claude Sonnet 4.5) |
| 2025-12-30 | Implemented Customer entity with DDS mapping | Dev Agent (Claude Sonnet 4.5) |
| 2025-12-30 | Created comprehensive unit tests (14 tests, all passing) | Dev Agent (Claude Sonnet 4.5) |
| 2025-12-30 | Fixed Checkstyle indentation violations | Dev Agent (Claude Sonnet 4.5) |
| 2025-12-30 | Story marked ready for review | Dev Agent (Claude Sonnet 4.5) |

### File List

**New Files Created:**
- backend/src/main/java/com/smeup/backend/entity/Customer.java
- backend/src/test/java/com/smeup/backend/entity/CustomerTest.java
- backend/src/test/java/com/smeup/backend/entity/CustomerJpaTest.java
- source-rpgle/dds/physical-files/CUSTMAST.dds (already existed)

**Files Modified During Code Review:**
- backend/src/main/java/com/smeup/backend/entity/Customer.java (added validation, equals/hashCode, enhanced toString)
