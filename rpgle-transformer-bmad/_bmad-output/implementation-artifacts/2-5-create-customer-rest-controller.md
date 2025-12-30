# Story 2.5: Create Customer REST Controller

Status: ready-for-dev

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **a REST controller exposing customer inquiry endpoints**,
so that **the frontend can access customer data via API**.

## Acceptance Criteria

1. **Given** the Customer service exists
   **When** I create the REST controller
   **Then** `CustomerController.java` is created in `com.smeup.backend.controller`

2. **And** endpoint `GET /api/v1/customers/{customerId}` is implemented
   - Returns customer details if found (200 OK)
   - Returns 404 Not Found if customer does not exist (`CustomerNotFoundException`)
   - Returns 400 Bad Request if customer ID is invalid (`InvalidCustomerIdException`)

3. **And** response format follows architecture standard: `{ data: {...}, meta: {...} }`

4. **And** error responses use RFC 7807 Problem Details format
   - Implemented via `@ControllerAdvice`

5. **And** inline comments reference original RPGLE program operations (e.g. CUST001)

6.  **And** `CustomerDTO` is NOT required yet (Story 2.6 is next), so controller can return `Customer` entity wrapped in response for now, OR return raw Entity if DTO story is strictly sequential.
    *Correction*: Architecture says "Use DTO". Story 2.6 title is "Create Customer DTO and Mapper".
    *Current decision*: Since Story 2.6 is specifically for DTOs, this story (2.5) might be intended to use the Entity directly or create a simple inner class/record. However, strict separation suggests waiting for 2.6.
    *Refinement*: To make 2.5 testable and working, it might be better to return the Entity directly (temporary) or create the DTO structure now.
    *Epic 2 AC #372*: just says "endpoint returns customer details".
    *Strategy*: I will implement the controller to return `Customer` entity wrapped in `ApiResponse` for this story to satisfy the immediate requirement, noting that 2.6 will refine it to DTO. Or, I can scaffold a basic DTO now.
    *Better approach*: Story 2.6 is "Create Customer DTO and Mapper". I will follow the strict breakdown. Controller will return `Customer` entity (wrapped in `ApiResponse`) for now.

7. **And** unit tests verify controller endpoints with MockMvc (AC from Epic 4.3 is "Create Backend Controller Tests" but Epic 2.5 implies functional controller requires verification).
   *Note*: Epic 4 has specific testing stories. However, "Dev Agent Guardrails" usually mandate tests.
   *Decision*: I will include basic `WebMvcTest` unit tests to ensure the controller works as expected, satisfying "Definition of Done".

## Tasks / Subtasks

- [ ] Task 1: Create Response Wrapper (AC: #3)
  - [ ] Create `ApiResponse<T>` class in `com.smeup.backend.dto` (or `common`)
  - [ ] Fields: `data` (T), `meta` (Map/Object)

- [ ] Task 2: Create Global Exception Handler (AC: #4)
  - [ ] Create `GlobalExceptionHandler` in `com.smeup.backend.exception`
  - [ ] Handle `CustomerNotFoundException` -> 404 Not Found (ProblemDetail)
  - [ ] Handle `InvalidCustomerIdException` -> 400 Bad Request (ProblemDetail)

- [ ] Task 3: Create CustomerController (AC: #1, #2, #5)
  - [ ] Create `CustomerController` in `com.smeup.backend.controller`
  - [ ] Inject `CustomerService` via constructor
  - [ ] Implement `findCustomerById` endpoint
  - [ ] Add inline RPGLE documentation comments

- [ ] Task 4: Create Controller Unit Tests (AC: #7)
  - [ ] Create `CustomerControllerTest` in `backend/src/test/java/.../controller`
  - [ ] Use `@WebMvcTest(CustomerController.class)`
  - [ ] Mock `CustomerService`
  - [ ] Verify 200, 400, 404 scenarios and JSON structure

## Dev Notes

### Architecture & Patterns
- **API Versioning**: URL path strategy `/api/v1/...`
- **Response Format**:
  ```json
  {
      "data": { ... },
      "meta": { ... }
  }
  ```
- **Error Handling**: Use Spring Boot 3's `ProblemDetail` support or custom `ProblemDetails` object (RFC 7807).
  ```json
  {
      "type": "about:blank",
      "title": "Bad Request",
      "status": 400,
      "detail": "Customer ID must be positive",
      "instance": "/api/v1/customers/-1"
  }
  ```

### Service Integration
- `CustomerService.findCustomerById(Long)` returns `Customer` or throws exception.
- Do NOT use `Optional` checks in controller; use `try-catch` or let `GlobalExceptionHandler` handle runtime exceptions (Preferred).

### Files to Create
- `backend/src/main/java/com/smeup/backend/common/ApiResponse.java` (Generic wrapper)
- `backend/src/main/java/com/smeup/backend/exception/GlobalExceptionHandler.java`
- `backend/src/main/java/com/smeup/backend/controller/CustomerController.java`
- `backend/src/test/java/com/smeup/backend/controller/CustomerControllerTest.java`

### References
- [Source: backend/src/main/java/com/smeup/backend/service/CustomerService.java] - Service contract
- [Source: _bmad-output/planning-artifacts/architecture.md#API-Response-Format] - Response standard
- [Source: _bmad-output/planning-artifacts/epics.md#Story-2.5] - Requirements

## Dev Agent Guardrails

### CRITICAL: Exception Handling
**YOU MUST:**
- Use a `@ControllerAdvice` global handler.
- Map `InvalidCustomerIdException` to HTTP 400.
- Map `CustomerNotFoundException` to HTTP 404.
- Return `ProblemDetail` (Spring 3 standard) or compliant JSON.

**YOU MUST NOT:**
- Catch exceptions manually in the controller methods (let the advice handle it).
- Return plain text error messages.

### CRITICAL: Response Wrapping
**YOU MUST:**
- Wrap the return entity in a `data` field.
- Ensure `meta` field exists (can be null/empty for now).

### CRITICAL: RPGLE Traceability
**YOU MUST:**
- Add comments referencing CUST001/CUSTMAST.

### CRITICAL: Testing
**YOU MUST:**
- Write `@WebMvcTest` unit tests.
- Verify the JSON structure matches requirements.
