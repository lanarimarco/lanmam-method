# Java/Spring Boot Documentation Standards

**Version**: 1.0  
**Last Updated**: 2026-01-02  
**Purpose**: Define inline code documentation standards for RPGLE-to-Java transformations

## Table of Contents

1. [Overview](#overview)
2. [What to Document](#what-to-document)
3. [What NOT to Document](#what-not-to-document)
4. [JavaDoc Standards](#javadoc-standards)
5. [Inline Comment Standards](#inline-comment-standards)
6. [DDS-to-Java Mapping Documentation](#dds-to-java-mapping-documentation)
7. [Business Logic Documentation](#business-logic-documentation)
8. [Examples from CUST001](#examples-from-cust001)

---

## Overview

This document defines documentation standards specifically for RPGLE-to-modern transformation projects. The goal is to maintain clarity about:

- **DDS field origins** - Where each Java field came from in the original DDS files
- **Business logic mappings** - How RPGLE logic translates to Java
- **Validation rules** - Why certain constraints exist
- **Edge cases** - Special handling that differs from standard patterns

These standards balance thoroughness with maintainability, avoiding documentation bloat while preserving critical transformation context.

---

## What to Document

### Always Document

1. **DDS Field Mappings**
   - Original DDS field name, type, and length
   - Any data type transformations (e.g., Packed Decimal → BigDecimal)
   - Special formatting or edit codes (EDTCDE, EDTWRD)

2. **Business Logic from RPGLE**
   - Validation rules translated from RPGLE code
   - Calculations and formulas
   - Conditional logic and branching
   - File operations (CHAIN, READ, WRITE, UPDATE, DELETE)

3. **Non-Obvious Design Decisions**
   - Why manual ID assignment instead of @GeneratedValue
   - Why certain fields are nullable or required
   - Deviations from standard Spring Boot patterns

4. **API Contract Information**
   - HTTP method, path, parameters
   - Request/response body structure
   - Status codes and error responses
   - Authentication/authorization requirements

5. **Transformation Rationale**
   - Why a particular approach was chosen
   - Limitations or known differences from RPGLE behavior
   - Future enhancement opportunities

### Consider Documenting

1. **Complex algorithms** - Multi-step calculations or intricate logic
2. **Performance considerations** - Database query optimization, caching strategies
3. **Security constraints** - Data validation, sanitization, access control
4. **Integration points** - External services, message queues, file systems

---

## What NOT to Document

### Avoid Documenting

1. **Self-explanatory code**
   ```java
   // BAD: Obvious getter documentation
   /**
    * Gets the customer ID
    * @return the customer ID
    */
   public Long getCustomerId() {
       return customerId;
   }
   ```

2. **Standard setters without validation**
   ```java
   // BAD: Unnecessary setter documentation
   /**
    * Sets the customer name
    * @param customerName the customer name to set
    */
   public void setCustomerName(String customerName) {
       this.customerName = customerName;
   }
   ```

3. **Standard JPA boilerplate**
   - Default constructors without special initialization
   - Standard equals/hashCode implementations
   - Standard toString methods

4. **Obvious variable names**
   ```java
   // BAD: Comment adds no value
   // Customer repository
   private final CustomerRepository customerRepository;
   ```

5. **Implementation details covered by method names**
   ```java
   // BAD: Method name already explains what it does
   /**
    * Validates that the customer ID is not null
    */
   private void validateCustomerId() { ... }
   ```

---

## JavaDoc Standards

### Class-Level JavaDoc

**Required for**:
- All public classes
- JPA entities
- Service classes
- Controllers
- DTOs

**Format**:
```java
/**
 * Brief one-sentence description of the class.
 *
 * <p>Longer description explaining purpose, context, and usage.
 * Include transformation-specific details here.
 *
 * <p>DDS Source: path/to/original-file.dds
 * <p>RPGLE Program: PROGNAME - Program Purpose
 * <p>Table/File: FILENAME
 *
 * <p>Additional context about business logic, constraints, or design decisions.
 *
 * @author Team/Agent Name (optional)
 * @since Version (optional)
 * @see RelatedClass (optional)
 */
```

**Example**:
```java
/**
 * Customer entity mapped from DDS Physical File CUSTMAST.dds
 *
 * <p>Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 * <p>Table: CUSTMAST
 * <p>Record Format: CUSTREC
 *
 * <p>This entity preserves the original AS/400 data structure while providing
 * a modern Java interface for the application.
 *
 * <p>DDS Field Mapping:
 * <ul>
 *   <li>CUSTNO (5P 0) → customerId (Long)</li>
 *   <li>CUSTNAME (30A) → customerName (String)</li>
 *   <li>BALANCE (9P 2) → accountBalance (BigDecimal)</li>
 * </ul>
 */
@Entity
@Table(name = "CUSTMAST")
public class Customer implements Serializable {
    // ...
}
```

### Method-Level JavaDoc

**Required for**:
- All public and protected methods
- Complex private methods with non-obvious logic
- Methods implementing RPGLE operations (CHAIN, READ, etc.)

**Format**:
```java
/**
 * Brief description of what the method does.
 *
 * <p>Additional context about RPGLE origin, business logic, or edge cases.
 *
 * <p>RPGLE Operation: CHAIN CUSTMAST (or equivalent)
 * <p>Original Subroutine: SUBNAME (if applicable)
 *
 * @param paramName Description of parameter, including valid ranges and constraints
 * @param anotherParam Description of another parameter
 * @return Description of return value, including null conditions
 * @throws ExceptionType Description of when this exception is thrown
 */
```

**Example**:
```java
/**
 * Find customer by customer ID.
 *
 * <p>RPGLE Operation: CHAIN CUSTMAST by CUSTNO
 * <p>Returns empty Optional if customer not found (equivalent to %Found indicator = *Off)
 *
 * @param customerId Customer number to search for (1-99999)
 * @return Optional containing Customer if found, empty otherwise
 */
Optional<Customer> findByCustomerId(Long customerId);
```

### Field-Level JavaDoc

**Required for**:
- All public fields (though prefer private fields with getters/setters)
- JPA entity fields mapped from DDS
- DTOs with DDS field origins

**Format**:
```java
/**
 * Brief field description.
 * <p>DDS Field: FIELDNAME (TYPE LENGTH)
 * <p>Additional constraints or context
 */
@Column(name = "FIELDNAME")
private FieldType fieldName;
```

**Example**:
```java
/**
 * Customer Number - Primary Key
 * <p>DDS Field: CUSTNO (5P 0)
 * <p>Note: No @GeneratedValue - IDs are manually assigned to preserve
 * original AS/400 customer numbers during migration.
 */
@Id
@Column(name = "CUSTNO", nullable = false)
@NotNull(message = "Customer ID is required")
private Long customerId;
```

---

## Inline Comment Standards

### When to Use Inline Comments

1. **Complex business logic**
   ```java
   // RPGLE: Calculate account status based on balance and credit limit
   // Original logic from CUST001.rpgle lines 85-92
   if (accountBalance.compareTo(creditLimit) > 0) {
       // Over credit limit - set warning indicator
       status = AccountStatus.OVER_LIMIT;
   }
   ```

2. **Non-obvious RPGLE equivalences**
   ```java
   // RPGLE: IF *IN90 = *On - Error indicator set
   if (validationError) {
       return ResponseEntity.badRequest().body(errorMessage);
   }
   ```

3. **Temporary workarounds or TODOs**
   ```java
   // TODO: Replace with proper date handling when migrating from YYYYMMDD integer format
   // Current implementation matches RPGLE date storage (LASTORDER field - 8P 0)
   private Integer lastOrderDate;
   ```

### Comment Formatting

- Use `//` for single-line comments
- Use `/* ... */` for multi-line blocks (rarely needed)
- Start comments with capital letter, end with period for complete sentences
- Keep comments close to the code they describe (within 1-2 lines)
- Avoid trailing comments unless very brief

**Good**:
```java
// RPGLE: CHAIN CUSTMAST by customer number
Customer customer = customerRepository.findByCustomerId(customerId)
    .orElseThrow(() -> new CustomerNotFoundException(customerId));
```

**Bad**:
```java
Customer customer = customerRepository.findByCustomerId(customerId)  // Find customer
    .orElseThrow(() -> new CustomerNotFoundException(customerId));  // Throw error if not found
```

---

## DDS-to-Java Mapping Documentation

### Entity Field Mapping Pattern

For JPA entities, document DDS field mappings in field-level JavaDoc:

```java
/**
 * Customer Name
 * <p>DDS Field: CUSTNAME (30A)
 */
@Column(name = "CUSTNAME", length = 30, nullable = false)
@NotBlank(message = "Customer name is required")
@Size(max = 30, message = "Customer name must not exceed 30 characters")
private String customerName;
```

### Data Type Transformation Documentation

Document data type conversions clearly:

```java
/**
 * Account Balance
 * <p>DDS Field: BALANCE (9P 2) - Packed Decimal with 2 decimal places
 * <p>Java Type: BigDecimal - Exact decimal arithmetic required for financial data
 */
@Column(name = "BALANCE", precision = 9, scale = 2)
private BigDecimal accountBalance;
```

```java
/**
 * Last Order Date (YYYYMMDD format)
 * <p>DDS Field: LASTORDER (8P 0) - Numeric date in YYYYMMDD format
 * <p>Java Type: Integer - Preserves original format for compatibility
 * <p>Note: Future enhancement could convert to LocalDate
 */
@Column(name = "LASTORDER")
private Integer lastOrderDate;
```

### Repository RPGLE Operation Mapping

Document RPGLE file operations as Spring Data JPA methods:

```java
/**
 * Repository for Customer entity operations.
 *
 * <p>Original RPGLE Operations mapped to Spring Data JPA:
 * <ul>
 *   <li>CHAIN CUSTMAST - Record lookup by key → findByCustomerId()</li>
 *   <li>READ CUSTMAST - Sequential read → findAll()</li>
 *   <li>WRITE CUSTMAST - Insert record → save()</li>
 *   <li>UPDATE CUSTMAST - Update record → save()</li>
 *   <li>DELETE CUSTMAST - Delete record → deleteById()</li>
 * </ul>
 *
 * <p>DDS Physical File: source-rpgle/dds/physical-files/CUSTMAST.dds
 *
 * @see Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by customer ID.
     *
     * <p>RPGLE Operation: CHAIN CUSTMAST by CUSTNO
     * <p>Indicator: %Found(CUSTMAST) → isPresent()
     *
     * @param customerId Customer number (1-99999)
     * @return Optional containing Customer if found, empty otherwise
     */
    Optional<Customer> findByCustomerId(Long customerId);
}
```

---

## Business Logic Documentation

### Service Method Documentation

Document business logic transformations in service methods:

```java
/**
 * Retrieve customer by customer number.
 *
 * <p>RPGLE Subroutine: GETCUST in CUST001.rpgle
 * <p>Original Logic:
 * <pre>
 *   CHAIN CUSTMAST by PCUSTNO
 *   IF %Found(CUSTMAST)
 *     // Populate display fields
 *   ELSE
 *     // Set error indicator and message
 *   ENDIF
 * </pre>
 *
 * <p>Transformation Notes:
 * <ul>
 *   <li>CHAIN operation → repository.findByCustomerId()</li>
 *   <li>%Found indicator → Optional.isPresent()</li>
 *   <li>Error indicator *IN90 → CustomerNotFoundException</li>
 * </ul>
 *
 * @param customerNumber Customer ID to look up (1-99999)
 * @return CustomerDTO with customer details
 * @throws CustomerNotFoundException if customer not found
 */
@Transactional(readOnly = true)
public CustomerDTO getCustomerByNumber(Long customerNumber) {
    // RPGLE: CHAIN CUSTMAST
    Customer customer = customerRepository.findByCustomerId(customerNumber)
        .orElseThrow(() -> new CustomerNotFoundException(
            "Customer not found: " + customerNumber)); // RPGLE: *IN90 = *On
    
    // RPGLE: Populate display fields (DCUSTNO, DCUSTNAME, etc.)
    return mapToDTO(customer);
}
```

### Validation Logic Documentation

Document validation rules with RPGLE origins:

```java
/**
 * Validate customer number input.
 *
 * <p>RPGLE Validation (CUST001.rpgle lines 20-25):
 * <pre>
 *   IF PCUSTNO = 0
 *     *IN90 = *On
 *     PMSG = 'Customer number required'
 *   ENDIF
 * </pre>
 *
 * @param customerNumber Customer number to validate
 * @throws IllegalArgumentException if validation fails
 */
private void validateCustomerNumber(Long customerNumber) {
    if (customerNumber == null || customerNumber == 0) {
        throw new IllegalArgumentException("Customer number required");
    }
}
```

---

## Examples from CUST001

### Example 1: Entity Class Documentation

**File**: `backend/src/main/java/com/smeup/backend/entity/Customer.java`

```java
package com.smeup.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Customer entity mapped from DDS Physical File CUSTMAST.dds
 * 
 * <p>Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 * <p>Table: CUSTMAST
 * <p>Record Format: CUSTREC
 * 
 * <p>This entity preserves the original AS/400 data structure while providing
 * a modern Java interface for the application.
 * 
 * <p>DDS Field Mapping:
 * <ul>
 *   <li>CUSTNO (5P 0) → customerId (Long)</li>
 *   <li>CUSTNAME (30A) → customerName (String)</li>
 *   <li>ADDR1 (30A) → addressLine1 (String)</li>
 *   <li>CITY (20A) → city (String)</li>
 *   <li>STATE (2A) → state (String)</li>
 *   <li>ZIP (5P 0) → zipCode (Integer)</li>
 *   <li>PHONE (12A) → phoneNumber (String)</li>
 *   <li>BALANCE (9P 2) → accountBalance (BigDecimal)</li>
 *   <li>CREDITLIM (9P 2) → creditLimit (BigDecimal)</li>
 *   <li>LASTORDER (8P 0) → lastOrderDate (Integer, YYYYMMDD format)</li>
 * </ul>
 */
@Entity
@Table(name = "CUSTMAST") // DDS Physical File name
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer Number - Primary Key
     * <p>DDS Field: CUSTNO (5P 0)
     * <p>Note: No @GeneratedValue - IDs are manually assigned to preserve
     * original AS/400 customer numbers during migration.
     */
    @Id
    @Column(name = "CUSTNO", nullable = false)
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    /**
     * Customer Name
     * <p>DDS Field: CUSTNAME (30A)
     */
    @Column(name = "CUSTNAME", length = 30, nullable = false)
    @NotBlank(message = "Customer name is required")
    @Size(max = 30, message = "Customer name must not exceed 30 characters")
    private String customerName;

    /**
     * State Code
     * <p>DDS Field: STATE (2A)
     * <p>Validation: Must be 2 uppercase letters (e.g., "IL", "CA")
     */
    @Column(name = "STATE", length = 2)
    @Size(min = 2, max = 2, message = "State code must be exactly 2 characters")
    @Pattern(regexp = "[A-Z]{2}", message = "State code must be 2 uppercase letters")
    private String state;

    /**
     * Account Balance
     * <p>DDS Field: BALANCE (9P 2) - Packed Decimal with 2 decimal places
     * <p>Java Type: BigDecimal - Exact decimal arithmetic for financial data
     */
    @Column(name = "BALANCE", precision = 9, scale = 2)
    @DecimalMin(value = "0.0", message = "Account balance cannot be negative")
    private BigDecimal accountBalance;

    /**
     * Last Order Date (YYYYMMDD format)
     * <p>DDS Field: LASTORDER (8P 0)
     * <p>Format: Integer in YYYYMMDD format (e.g., 20251228)
     * <p>Note: Preserves original AS/400 date format for compatibility
     */
    @Column(name = "LASTORDER")
    private Integer lastOrderDate;

    // Default constructor required by JPA
    public Customer() {
    }

    // Getters and Setters
    // (Only include JavaDoc for getters/setters with special logic)

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // ... other standard getters/setters omitted for brevity

    @Override
    public String toString() {
        return "Customer{"
                + "customerId=" + customerId
                + ", customerName='" + customerName + '\''
                + ", accountBalance=" + accountBalance
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
```

### Example 2: Repository Documentation

**File**: `backend/src/main/java/com/smeup/backend/repository/CustomerRepository.java`

```java
package com.smeup.backend.repository;

import com.smeup.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Customer entity operations.
 *
 * <p>Original RPGLE Operations mapped to Spring Data JPA:
 * <ul>
 *   <li>CHAIN CUSTMAST - Record lookup by key → findByCustomerId()</li>
 *   <li>READ CUSTMAST - Sequential read → findAll()</li>
 *   <li>WRITE CUSTMAST - Insert record → save()</li>
 *   <li>UPDATE CUSTMAST - Update record → save()</li>
 *   <li>DELETE CUSTMAST - Delete record → deleteById()</li>
 * </ul>
 *
 * <p>Source: RPGLE programs use CHAIN opcode to retrieve customer records
 * from CUSTMAST physical file by customer number (CUSTNO).
 *
 * <p>DDS Physical File: source-rpgle/dds/physical-files/CUSTMAST.dds
 *
 * @see Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by customer ID.
     *
     * <p>RPGLE Operation: CHAIN CUSTMAST by CUSTNO
     * <p>Indicator: %Found(CUSTMAST) → isPresent()
     *
     * @param customerId Customer number (1-99999)
     * @return Optional containing Customer if found, empty otherwise
     */
    Optional<Customer> findByCustomerId(Long customerId);
}
```

### Example 3: DTO Documentation

**File**: `backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java`

```java
package com.smeup.backend.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Customer.
 * <p>Represents the API view of the Customer entity.
 * <p>Maps to DDS Physical File: CUSTMAST.dds
 */
public record CustomerDTO(
        /**
         * Customer Number.
         * <p>DDS Field: CUSTNO (5P 0)
         */
        Long customerId,

        /**
         * Customer Name.
         * <p>DDS Field: CUSTNAME (30A)
         */
        String customerName,

        /**
         * Address Line 1.
         * <p>DDS Field: ADDR1 (30A)
         */
        String addressLine1,

        /**
         * City.
         * <p>DDS Field: CITY (20A)
         */
        String city,

        /**
         * State Code.
         * <p>DDS Field: STATE (2A)
         */
        String state,

        /**
         * Zip Code.
         * <p>DDS Field: ZIP (5P 0)
         */
        Integer zipCode,

        /**
         * Phone Number.
         * <p>DDS Field: PHONE (12A)
         */
        String phoneNumber,

        /**
         * Account Balance.
         * <p>DDS Field: BALANCE (9P 2)
         */
        BigDecimal accountBalance,

        /**
         * Credit Limit.
         * <p>DDS Field: CREDITLIM (9P 2)
         */
        BigDecimal creditLimit,

        /**
         * Last Order Date in YYYYMMDD format.
         * <p>DDS Field: LASTORDER (8P 0)
         */
        Integer lastOrderDate
) {
}
```

### Example 4: Controller Documentation

**File**: `backend/src/main/java/com/smeup/backend/controller/CustomerController.java`

```java
/**
 * REST Controller for Customer Inquiry operations.
 *
 * <p>Exposes HTTP API for customer lookup functionality,
 * replacing the 5250 green-screen interface from CUST001 program.
 *
 * <p>RPGLE Program: CUST001.rpgle - Customer Inquiry
 * <p>DDS Display File: CUSTDSP.dds
 *
 * <p>Endpoints:
 * <ul>
 *   <li>GET /api/v1/customers/{id} - Look up customer by ID (replaces PROMPT screen)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Get customer by customer ID.
     *
     * <p>RPGLE Workflow (CUST001.rpgle):
     * <ol>
     *   <li>User enters customer number in PROMPT screen</li>
     *   <li>Program validates input (non-zero)</li>
     *   <li>CHAIN operation to CUSTMAST file</li>
     *   <li>If found: Display DETAIL screen</li>
     *   <li>If not found: Display error message</li>
     * </ol>
     *
     * <p>REST API Transformation:
     * <ul>
     *   <li>HTTP GET replaces PROMPT screen input</li>
     *   <li>Path parameter replaces interactive entry</li>
     *   <li>JSON response replaces DETAIL screen</li>
     *   <li>404 status replaces "Customer not found" error indicator</li>
     * </ul>
     *
     * @param customerId Customer number to look up (1-99999)
     * @return CustomerDTO with customer details
     * @throws CustomerNotFoundException if customer not found (HTTP 404)
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @PathVariable @Min(1) @Max(99999) Long customerId) {
        
        CustomerDTO customer = customerService.getCustomerByNumber(customerId);
        return ResponseEntity.ok(customer);
    }
}
```

---

## Summary

### Documentation Checklist

- [ ] Class-level JavaDoc includes DDS file reference and field mappings
- [ ] Public methods have JavaDoc with RPGLE operation context
- [ ] Entity fields document DDS field name, type, and length
- [ ] Business logic includes RPGLE source reference and transformation notes
- [ ] Validation rules reference original RPGLE validation logic
- [ ] No unnecessary documentation on obvious getters/setters
- [ ] Comments explain "why", not "what" (code should explain "what")
- [ ] All documentation passes Checkstyle linting
- [ ] Documentation is clear enough for AI agents to understand transformation context

### AI-Friendly Documentation Tips

When writing documentation for AI-assisted transformations:

1. **Be explicit about mappings** - Don't assume the AI knows DDS field types
2. **Reference line numbers** - Helps AI locate original RPGLE logic
3. **Include before/after examples** - Shows transformation pattern clearly
4. **Document edge cases** - AI needs to understand exception scenarios
5. **Use consistent formatting** - Makes parsing documentation easier

---

**Related Documents**:
- [TypeScript Documentation Standards](typescript-documentation-standards.md)
- [Business Logic Mapping Template](business-logic-mapping-template.md)
- [Documentation Checklist](documentation-checklist.md)
