# Story 2.4: Create Customer Service with Business Logic

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **a service layer implementing the CUST001 business logic**,
so that **the customer inquiry functionality matches the original RPGLE behavior**.

## Acceptance Criteria

1. **Given** the Customer repository exists
   **When** I create the service implementing CUST001 business logic
   **Then** `CustomerService.java` is created with `findCustomerById()` method

2. **And** validation logic matches original RPGLE (customer number validation)
   _(Customer ID must be a positive Long, matching CUSTNO 5P 0 constraint)_

3. **And** error handling matches original RPGLE behavior (invalid customer, not found)

4. **And** inline comments reference original RPGLE lines and logic

5. **And** service uses `@Transactional` for database operations

6. **And** unit tests verify service business logic with mocked repository

## Tasks / Subtasks

- [x] Task 1: Create CustomerService class (AC: #1, #4, #5)
  - [x] Create `CustomerService.java` in `com.smeup.backend.service` package
  - [x] Add header comment referencing RPGLE CUST001 program
  - [x] Use constructor injection for CustomerRepository
  - [x] Add `@Service` annotation

- [x] Task 2: Implement findCustomerById method (AC: #1, #2, #3, #4)
  - [x] Create `findCustomerById(Long customerId)` method returning `Optional<Customer>`
  - [x] Add input validation for customerId (must be positive)
  - [x] Add inline comment referencing RPGLE CHAIN operation
  - [x] Add `@Transactional(readOnly = true)` annotation

- [x] Task 3: Create custom exception classes (AC: #3)
  - [x] Create `CustomerNotFoundException` extends `RuntimeException`
  - [x] Create `InvalidCustomerIdException` extends `RuntimeException`
  - [x] Follow RFC 7807 Problem Details pattern for error messages

- [x] Task 4: Create unit tests for CustomerService (AC: all)
  - [x] Create `CustomerServiceTest.java` in test directory
  - [x] Use Mockito to mock CustomerRepository
  - [x] Test valid customer lookup returns customer
  - [x] Test customer not found returns empty Optional
  - [x] Test invalid customer ID throws exception
  - [x] Test null customer ID throws exception
  - [x] Achieve 80%+ code coverage (Current: ~100% on new logic)

- [x] Task 5: Verify all tests pass (AC: all)
  - [x] Run `mvn test` - all unit tests pass
  - [x] Run `mvn verify` - all integration tests still pass
  - [x] Checkstyle passes

## Dev Notes

### RPGLE CUST001 Business Logic Reference

The original RPGLE CUST001 program implements customer inquiry:

```rpgle
* CUST001 - Customer Inquiry Program
* Reads customer record from CUSTMAST physical file
*
C     *ENTRY    PLIST
C               PARM                    CUSTNO           5 0
C
* Validate customer number is positive
C     CUSTNO    IFEQ      *ZEROS
C               MOVEL     'ERR001'      MSGID            7
C               GOTO      EXIT
C               ENDIF
C
* Chain to customer master file
C     CUSTNO    CHAIN     CUSTMAST
C               IF        %FOUND(CUSTMAST)
*               Process customer record...
C               ELSE
C               MOVEL     'ERR002'      MSGID
C               ENDIF
```

**Key RPGLE Operations to Map:**
- `CUSTNO IFEQ *ZEROS` → Validate customerId is positive
- `CHAIN CUSTMAST` → Repository findByCustomerId()
- `%FOUND(CUSTMAST)` → Optional.isPresent()
- `ERR001` → InvalidCustomerIdException
- `ERR002` → CustomerNotFoundException (or empty Optional)

### Service Class Template

```java
package com.smeup.backend.service;

import com.smeup.backend.entity.Customer;
import com.smeup.backend.repository.CustomerRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementing CUST001 customer inquiry business logic.
 *
 * <p>Original RPGLE Program: CUST001 - Customer Inquiry
 * <p>Source: source-rpgle/programs/CUST001.rpgle
 *
 * <p>This service maps RPGLE operations to Spring Boot:
 * <ul>
 *   <li>CHAIN CUSTMAST → findCustomerById()</li>
 *   <li>%FOUND → Optional.isPresent()</li>
 *   <li>Input validation → validateCustomerId()</li>
 * </ul>
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Find customer by ID - equivalent to RPGLE CHAIN CUSTMAST.
     *
     * <p>RPGLE Equivalent:
     * <pre>
     * C     CUSTNO    CHAIN     CUSTMAST
     * C               IF        %FOUND(CUSTMAST)
     * </pre>
     *
     * @param customerId customer number (CUSTNO field)
     * @return Optional containing customer if found
     * @throws InvalidCustomerIdException if customerId is null or not positive
     */
    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(Long customerId) {
        validateCustomerId(customerId);
        return customerRepository.findByCustomerId(customerId);
    }

    /**
     * Validate customer ID - equivalent to RPGLE CUSTNO IFEQ *ZEROS check.
     *
     * <p>RPGLE Equivalent:
     * <pre>
     * C     CUSTNO    IFEQ      *ZEROS
     * C               MOVEL     'ERR001'      MSGID
     * </pre>
     */
    private void validateCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new InvalidCustomerIdException(
                "Customer ID must be a positive number"
            );
        }
    }
}
```

### Exception Classes Template

```java
package com.smeup.backend.exception;

/**
 * Exception thrown when customer ID is invalid.
 *
 * RPGLE Equivalent: ERR001 - Invalid customer number
 */
public class InvalidCustomerIdException extends RuntimeException {
    public InvalidCustomerIdException(String message) {
        super(message);
    }
}

/**
 * Exception thrown when customer is not found.
 *
 * RPGLE Equivalent: ERR002 - Customer not found in CUSTMAST
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with ID: " + customerId);
    }
}
```

### Package Structure

```
backend/src/main/java/com/smeup/backend/
├── BackendApplication.java     (✅ exists)
├── entity/
│   └── Customer.java           (✅ exists - Story 2-1)
├── repository/
│   └── CustomerRepository.java (✅ exists - Story 2-3)
├── service/                     (🆕 create in this story)
│   └── CustomerService.java     (🆕 create in this story)
└── exception/                   (🆕 create in this story)
    ├── InvalidCustomerIdException.java  (🆕)
    └── CustomerNotFoundException.java   (🆕)

backend/src/test/java/com/smeup/backend/
├── entity/
│   ├── CustomerTest.java        (✅ exists)
│   └── CustomerJpaTest.java     (✅ exists)
├── repository/
│   ├── CustomerRepositoryTest.java   (✅ exists)
│   └── CustomerRepositoryIT.java     (✅ exists)
└── service/                      (🆕 create in this story)
    └── CustomerServiceTest.java  (🆕 create in this story)
```

### Testing Strategy

**Unit Tests with Mockito:**

```java
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("findCustomerById should return customer when found")
    void shouldReturnCustomerWhenFound() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(12345L);
        customer.setCustomerName("Test Customer");
        when(customerRepository.findByCustomerId(12345L))
            .thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.findCustomerById(12345L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerName()).isEqualTo("Test Customer");
        verify(customerRepository).findByCustomerId(12345L);
    }

    @Test
    @DisplayName("findCustomerById should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
        // Given - RPGLE: CHAIN returns %FOUND = *OFF
        when(customerRepository.findByCustomerId(99999L))
            .thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerService.findCustomerById(99999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findCustomerById should throw when ID is null")
    void shouldThrowWhenIdIsNull() {
        // RPGLE: CUSTNO IFEQ *ZEROS check
        assertThatThrownBy(() -> customerService.findCustomerById(null))
            .isInstanceOf(InvalidCustomerIdException.class);
    }

    @Test
    @DisplayName("findCustomerById should throw when ID is zero or negative")
    void shouldThrowWhenIdIsZeroOrNegative() {
        // RPGLE: CUSTNO IFEQ *ZEROS check
        assertThatThrownBy(() -> customerService.findCustomerById(0L))
            .isInstanceOf(InvalidCustomerIdException.class);
        assertThatThrownBy(() -> customerService.findCustomerById(-1L))
            .isInstanceOf(InvalidCustomerIdException.class);
    }
}
```

### Previous Story Intelligence (Story 2-3)

**Key Learnings:**
- ✅ CustomerRepository exists with `findByCustomerId(Long)` method
- ✅ Repository returns `Optional<Customer>` (maps to RPGLE %FOUND)
- ✅ Spring Boot 4 requires `spring-boot-data-jpa-test` for `@DataJpaTest`
- ✅ Singleton Container Pattern fixed IT context caching issue
- ✅ All 34 unit tests + 11 integration tests pass with `mvn verify`
- ✅ Checkstyle validates Google Java Style

**From Story 2-1:**
- Customer entity uses `Long customerId` as primary key
- Entity has `@NotNull` and `@NotBlank` validation annotations
- Entity fields match DDS CUSTMAST physical file

**From Story 2-2:**
- Flyway migration creates CUSTMAST table correctly
- H2 works for unit tests, PostgreSQL via Testcontainers for ITs

### References

- [Source: backend/src/main/java/com/smeup/backend/entity/Customer.java] - JPA entity with column mappings
- [Source: backend/src/main/java/com/smeup/backend/repository/CustomerRepository.java] - Repository with findByCustomerId
- [Source: _bmad-output/planning-artifacts/architecture.md#Java-Naming] - Service layer conventions
- [Source: _bmad-output/project-context.md] - Package structure rules
- [Source: _bmad-output/implementation-artifacts/2-3-create-customer-repository.md] - Previous story learnings

## Dev Agent Guardrails

### CRITICAL: Package Structure

**YOU MUST:**
- Create service in `com.smeup.backend.service` package
- Create exceptions in `com.smeup.backend.exception` package
- Create test in `com.smeup.backend.service` test package
- Use `@Service` annotation on the class

**YOU MUST NOT:**
- Put service in controller or repository package ❌
- Create service as an interface (it's a class) ❌
- Use field injection (use constructor injection) ❌

### CRITICAL: Constructor Injection

**YOU MUST:**
- Use constructor injection for CustomerRepository dependency
- Constructor should be the only way to inject dependencies

**Example:**
```java
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
```

**YOU MUST NOT:**
- Use `@Autowired` on fields ❌
- Use setter injection ❌

### CRITICAL: Transaction Management

**YOU MUST:**
- Add `@Transactional(readOnly = true)` on read-only methods
- Add `@Transactional` on write methods (future stories)

**YOU MUST NOT:**
- Skip `@Transactional` annotations on service methods ❌
- Use `@Transactional` on private methods (doesn't work) ❌

### CRITICAL: RPGLE Reference Comments

**YOU MUST:**
- Include header comment explaining CUST001 program mapping
- Add method-level comments referencing RPGLE opcodes (CHAIN, %FOUND)
- Add validation method comments referencing RPGLE checks (IFEQ *ZEROS)
- Reference error codes (ERR001, ERR002) in exception comments

**YOU MUST NOT:**
- Skip inline RPGLE reference comments ❌
- Write generic comments without RPGLE context ❌

### CRITICAL: Error Handling

**YOU MUST:**
- Create custom exceptions that extend RuntimeException
- Use descriptive exception messages
- Throw InvalidCustomerIdException for null/zero/negative IDs
- Return empty Optional for not found (don't throw CustomerNotFoundException in service)
  - _Note: Controller will decide whether to throw or return 404_

**YOU MUST NOT:**
- Use generic exceptions like `IllegalArgumentException` ❌
- Return null instead of Optional ❌
- Catch and swallow exceptions ❌

### CRITICAL: Testing Requirements

**YOU MUST:**
- Use `@ExtendWith(MockitoExtension.class)` for unit tests
- Mock CustomerRepository with Mockito
- Test all validation scenarios (null, zero, negative)
- Test found and not-found scenarios
- Use `@DisplayName` with RPGLE context
- Achieve 80%+ code coverage

**YOU MUST NOT:**
- Skip service unit tests ❌
- Use `@SpringBootTest` for service unit tests (use Mockito) ❌
- Test with real repository in unit tests ❌

## Dev Agent Record

### Agent Model Used

{{agent_model_name_version}}

### Debug Log References

### Completion Notes List
- Successfully implemented `CustomerService` with business logic mapping RPGLE `CUST001` behavior.
- Implemented input validation matching `IFEQ *ZEROS` check.
- Added custom exceptions `InvalidCustomerIdException` and `CustomerNotFoundException`.
- Verified implementation with ~100% unit test coverage for the service layer.
- Fixed style issues in `BackendApplication.java` and new files to ensure Google Java Style compliance.
- All unit and integration tests passed.

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created with comprehensive context | SM Agent (Claude Opus 4.5) |
| 2025-12-30 | Implemented service logic and unit tests | Antigravity (Dev Agent) |

### File List
- `backend/src/main/java/com/smeup/backend/service/CustomerService.java`
- `backend/src/main/java/com/smeup/backend/exception/InvalidCustomerIdException.java`
- `backend/src/main/java/com/smeup/backend/exception/CustomerNotFoundException.java`
- `backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java`
- `backend/src/main/java/com/smeup/backend/BackendApplication.java` (modified for style)

