# Business Logic Conversion Notes - CUST001

**Date**: 2025-12-28
**Program**: CUST001 - Customer Inquiry
**Source**: CUST001.rpgle

---

## Overview

This document describes the conversion of the CUST001 RPGLE program to Java Spring Boot services and REST controllers. The program implements a simple customer inquiry function that allows users to look up customer information by customer number.

---

## Files Created

### DTOs (Data Transfer Objects)
- `CustomerDTO.java` - Data transfer object for customer information
  - Location: `src/main/java/com/lanarimarco/modernization/dtos/CustomerDTO.java`
  - Package: `com.lanarimarco.modernization.dtos`

### Service Layer
- `CustomerInquiryService.java` - Business logic implementation
  - Location: `src/main/java/com/lanarimarco/modernization/services/CustomerInquiryService.java`
  - Package: `com.lanarimarco.modernization.services`

### Controller Layer
- `CustomerInquiryController.java` - REST API endpoints
  - Location: `src/main/java/com/lanarimarco/modernization/controllers/CustomerInquiryController.java`
  - Package: `com.lanarimarco.modernization.controllers`

### Exception Classes
- `CustomerNotFoundException.java` - Customer not found exception
  - Location: `src/main/java/com/lanarimarco/modernization/exceptions/CustomerNotFoundException.java`
  - Package: `com.lanarimarco.modernization.exceptions`

- `CustomerValidationException.java` - Validation error exception
  - Location: `src/main/java/com/lanarimarco/modernization/exceptions/CustomerValidationException.java`
  - Package: `com.lanarimarco.modernization.exceptions`

---

## RPGLE to Java Mapping

### Program Structure Mapping

| RPGLE Component | Java Component | Notes |
|-----------------|----------------|-------|
| File Declarations (F-specs) | Repository injection | Spring Data JPA handles file access |
| Main Processing (C-specs) | Service methods | Business logic in service layer |
| Display File Interaction | REST API endpoints | Controller exposes HTTP endpoints |
| CHAIN operation | `repository.findByCustomerNumber()` | JPA repository method |
| %Found() check | `Optional.orElseThrow()` | Functional programming approach |
| Indicator *IN90 | Exception throwing | Exceptions replace indicators |
| ExFmt (Display) | REST response | JSON response instead of screen display |

### Business Logic Flow Mapping

**RPGLE Flow:**
```rpgle
C                   ExFmt     PROMPT              // Display prompt screen
C                   DoW       *IN03 = *Off        // Loop until F3
C                   Eval      *IN90 = *Off        // Clear error
C                   Eval      PMSG = *Blanks      // Clear message
C                   If        PCUSTNO = 0         // Validate
C                   Eval      *IN90 = *On         // Set error indicator
C                   Eval      PMSG = 'Customer number required'
C                   ExFmt     PROMPT              // Redisplay
C                   Iter                          // Next iteration
C                   EndIf
C     PCUSTNO       Chain     CUSTMAST            // Read customer
C                   If        %Found(CUSTMAST)    // If found
C                   Eval      DCUSTNO = CUSTNO    // Populate fields
...
C                   ExFmt     DETAIL              // Display detail
C                   Else                          // Not found
C                   Eval      *IN90 = *On         // Set error
C                   Eval      PMSG = 'Customer not found'
C                   EndIf
C                   ExFmt     PROMPT              // Redisplay
C                   EndDo
```

**Java Flow:**
```java
// REST API Request: GET /api/customers/{customerNumber}

// 1. Validation
if (customerNumber == null || customerNumber == 0) {
    throw new CustomerValidationException("Customer number required");
}

// 2. Database lookup
Customer customer = customerRepository.findByCustomerNumber(customerNumber)
    .orElseThrow(() -> new CustomerNotFoundException(customerNumber));

// 3. Map to DTO and return
return mapToDTO(customer);
```

### Key Differences

1. **State Management**:
   - RPGLE: Uses indicators (*IN90, *IN03) to manage state
   - Java: Uses exceptions for error conditions, stateless HTTP for flow control

2. **User Interaction**:
   - RPGLE: Interactive screen-based (ExFmt operations)
   - Java: REST API with JSON request/response

3. **Loop Control**:
   - RPGLE: DoW loop with F3 to exit
   - Java: Each request is independent, no session state

4. **Error Handling**:
   - RPGLE: Indicators and message fields
   - Java: HTTP status codes and exception handling

---

## REST API Design

### Endpoint

**GET `/api/customers/{customerNumber}`**

Retrieves customer information by customer number.

#### Request
```http
GET /api/customers/12345
Accept: application/json
```

#### Successful Response (200 OK)
```json
{
  "customerNumber": 12345,
  "customerName": "John Doe",
  "address1": "123 Main Street",
  "city": "Springfield",
  "state": "IL",
  "zipCode": 62701,
  "phoneNumber": "555-1234",
  "balance": 1250.75
}
```

#### Error Responses

**Customer Not Found (404 Not Found)**
```json
{
  "error": "Customer not found",
  "message": "Customer not found: 12345",
  "customerNumber": 12345
}
```

**Validation Error (400 Bad Request)**
```json
{
  "error": "Validation failed",
  "message": "Customer number required"
}
```

**Server Error (500 Internal Server Error)**
```json
{
  "error": "Internal server error",
  "message": "An unexpected error occurred while processing your request"
}
```

---

## Validation Logic

### RPGLE Validation
```rpgle
If PCUSTNO = 0
   Eval *IN90 = *On
   Eval PMSG = 'Customer number required'
```

### Java Validation
```java
private void validateCustomerNumber(Integer customerNumber) {
    if (customerNumber == null || customerNumber == 0) {
        throw new CustomerValidationException("Customer number required");
    }

    if (customerNumber < 0) {
        throw new CustomerValidationException("Customer number must be positive");
    }
}
```

**Enhanced Validation**:
- Added null check (RPGLE doesn't have null concept)
- Added negative number check (additional safety)

---

## Error Handling Strategy

### Exception Hierarchy

```
RuntimeException
├── CustomerValidationException   (400 Bad Request)
└── CustomerNotFoundException      (404 Not Found)
```

### Controller Exception Handlers

```java
@ExceptionHandler(CustomerNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleCustomerNotFound(...)
    → Returns HTTP 404

@ExceptionHandler(CustomerValidationException.class)
public ResponseEntity<Map<String, Object>> handleValidationError(...)
    → Returns HTTP 400

@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, Object>> handleGenericError(...)
    → Returns HTTP 500
```

---

## Logging Strategy

### Log Levels Used

**INFO Level**:
- Request received: `"REST API: Get customer request for customer number: {}"`
- Request successful: `"REST API: Successfully retrieved customer: {}"`
- Service processing: `"Processing customer inquiry for customer number: {}"`

**WARN Level**:
- Validation failures: `"Invalid customer number provided: {}"`
- Customer not found: `"Customer not found: {}"`
- Exception handlers: `"Customer not found exception: {}"`

**DEBUG Level**:
- Customer details: `"Customer found: {} - {}"`

**ERROR Level**:
- Unexpected errors: `"Unexpected error in customer inquiry"`

### Logging Best Practices Applied
- Use SLF4J with Lombok's `@Slf4j`
- Structured logging with parameters
- No sensitive data logged (customer names OK, but no SSN, credit cards)
- Appropriate log levels for different scenarios

---

## Data Transfer Object (DTO) Design

### Fields Included
Based on analysis, CUST001 only displays these fields:
- customerNumber
- customerName
- address1
- city
- state
- zipCode
- phoneNumber
- balance

### Fields Excluded
Not used by CUST001, so excluded from DTO:
- creditLimit (not displayed)
- lastOrderDate (not displayed)

**Rationale**: Keep DTO minimal, only include what the program uses. This reduces data transfer and improves API clarity.

---

## Service Layer Design

### Transactional Behavior
```java
@Transactional(readOnly = true)
public class CustomerInquiryService
```

**Read-Only Transaction**:
- CUST001 is inquiry-only (no updates)
- Read-only optimization for database
- Matches RPGLE file declaration: `FCUSTMAST  IF   E` (Input only)

### Method Design

**Single Responsibility**:
- `getCustomerByNumber()` - Public API method
- `validateCustomerNumber()` - Private validation method
- `mapToDTO()` - Private mapping method

**Clear Separation of Concerns**:
- Validation logic isolated
- Mapping logic isolated
- Business flow easy to follow

---

## Controller Layer Design

### CORS Configuration
```java
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
```

**Purpose**:
- Allow React frontend (running on different port) to access API
- Configurable via application properties
- Defaults to localhost:3000 for development

### Exception Handling

**Centralized in Controller**:
- `@ExceptionHandler` methods handle exceptions
- Maps exceptions to HTTP status codes
- Returns consistent error response format

**Benefits**:
- Service layer doesn't need to know about HTTP
- Controller handles HTTP-specific concerns
- Consistent error responses across all endpoints

---

## Deviations from Original RPGLE

### Functional Enhancements

1. **Additional Validation**:
   - RPGLE: Only checks if customer number = 0
   - Java: Also checks for null and negative numbers
   - **Rationale**: Defensive programming, better error messages

2. **Structured Error Responses**:
   - RPGLE: Simple text messages on screen
   - Java: JSON error responses with error codes
   - **Rationale**: API best practices, machine-readable errors

3. **Logging**:
   - RPGLE: No built-in logging
   - Java: Comprehensive logging at all levels
   - **Rationale**: Debugging, auditing, monitoring

### Architectural Differences

1. **Stateless vs. Stateful**:
   - RPGLE: Stateful session with DoW loop
   - Java: Stateless REST API, each request independent
   - **Rationale**: REST principles, scalability

2. **Synchronous Display vs. API**:
   - RPGLE: Interactive ExFmt (wait for user input)
   - Java: Request/response cycle
   - **Rationale**: Modern web architecture

---

## Dependencies Required

Update `pom.xml` with these dependencies:

```xml
<dependencies>
    <!-- Spring Boot Starter Web (includes Spring MVC, REST, JSON) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA (already included from Phase 2) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring Boot Starter Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Lombok (already included from Phase 2) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- SLF4J for Logging (included with Spring Boot) -->
</dependencies>
```

---

## Configuration Required

### application.yml

```yaml
# Server Configuration
server:
  port: 8080

# CORS Configuration
cors:
  allowed-origins: http://localhost:3000,http://localhost:5173

# Logging Configuration
logging:
  level:
    com.lanarimarco.modernization: DEBUG
    org.springframework.web: INFO
    org.hibernate.SQL: DEBUG
```

---

## Testing Considerations

### Unit Tests (Phase 5)

**Service Layer Tests**:
- Test `getCustomerByNumber()` with valid customer number
- Test validation exception when customer number is 0
- Test validation exception when customer number is null
- Test validation exception when customer number is negative
- Test `CustomerNotFoundException` when customer not found
- Mock CustomerRepository for isolation

**Controller Layer Tests**:
- Test GET endpoint returns 200 with valid customer
- Test GET endpoint returns 404 when customer not found
- Test GET endpoint returns 400 for validation errors
- Use MockMvc for controller testing

### Integration Tests (Phase 5)

- Test full stack: Controller → Service → Repository → Database
- Test with actual database (H2 in-memory or Docker DB2)
- Verify JSON response format
- Test error scenarios end-to-end

---

## Migration Path from RPGLE

### For Users

**Old Process (5250)**:
1. Call CUST001 program
2. See PROMPT screen
3. Enter customer number
4. Press Enter
5. View DETAIL screen or error message
6. Press F12 to return or F3 to exit

**New Process (Web)**:
1. Open web application
2. Navigate to Customer Inquiry page
3. Enter customer number in form
4. Click Search button
5. View customer details or error message
6. Click Back or navigate to other functions

**Benefits**:
- Accessible from web browser, no terminal needed
- Modern UI/UX
- Can bookmark, share links
- Mobile-friendly (with responsive design)

---

## API Usage Examples

### Using curl

```bash
# Successful lookup
curl http://localhost:8080/api/customers/12345

# Customer not found
curl http://localhost:8080/api/customers/99999

# Validation error
curl http://localhost:8080/api/customers/0
```

### Using JavaScript (React)

```javascript
const getCustomer = async (customerNumber) => {
  try {
    const response = await fetch(`http://localhost:8080/api/customers/${customerNumber}`);

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message);
    }

    const customer = await response.json();
    return customer;
  } catch (error) {
    console.error('Error fetching customer:', error);
    throw error;
  }
};
```

---

## Known Issues and Limitations

### Current Limitations
None identified for this simple inquiry program.

### Potential Future Enhancements

1. **Pagination**: If extending to list all customers, add pagination
2. **Search Capabilities**: Search by name, phone, etc.
3. **Field Validation**: Add more sophisticated validation (phone format, zip format, etc.)
4. **Caching**: Add caching for frequently accessed customers
5. **Audit Trail**: Log all customer lookups for security/compliance
6. **Rate Limiting**: Prevent abuse of API

---

## Quality Checklist

- [x] All RPGLE business logic converted
- [x] Service layer properly implements logic
- [x] Controller exposes appropriate REST endpoints
- [x] DTOs properly structure data
- [x] Error handling comprehensive (validation, not found, server errors)
- [x] Logging added appropriately (INFO, WARN, DEBUG, ERROR)
- [x] Code follows Spring conventions
- [x] Dependencies properly injected (constructor injection with @RequiredArgsConstructor)
- [x] Conversion notes document complete
- [x] No hardcoded values (CORS origin is configurable)
- [x] Follows REST API best practices
- [x] Matches RPGLE behavior for all business scenarios

---

## References

- Source RPGLE: `/source-rpgle/programs/CUST001.rpgle`
- Analysis Document: `/work-in-progress/CUST001/01-analysis/CUST001-analysis.md`
- Database Notes: `/work-in-progress/CUST001/02-database/database-notes.md`
- Error Handling Strategy: `/common-patterns/error-handling-strategy.md`
- Logging Standards: `/common-patterns/logging-standards.md`
- Maven Structure: `/common-patterns/maven-project-structure.md`

---

## UI Agent Notes (Phase 4)

When creating the React UI:

1. **Search Form**:
   - Single input field for customer number (numeric only)
   - "Search" button to submit
   - "Clear" button to reset form
   - Display validation errors in red below input field

2. **Results Display**:
   - Show customer information in a card or panel
   - Format balance as currency ($1,250.75)
   - Format zip code with leading zeros if needed
   - Format phone number (consider (555) 123-4567 format)
   - Provide "New Search" button to go back

3. **Error Handling**:
   - Display API error messages prominently
   - Different styling for validation errors vs. not found errors
   - Allow user to try again without navigating away

4. **API Integration**:
   - Use environment variable for API base URL
   - Handle loading states (show spinner during API call)
   - Handle network errors gracefully

5. **Accessibility**:
   - Proper labels for form fields
   - ARIA attributes for screen readers
   - Keyboard navigation support
   - Focus management

---

**Phase 3 Status**: ✅ COMPLETE
