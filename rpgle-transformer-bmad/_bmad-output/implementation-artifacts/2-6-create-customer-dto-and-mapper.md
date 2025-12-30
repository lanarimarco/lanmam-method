---
story_id: 2-6-create-customer-dto-and-mapper
epic_id: epic-2
story_title: Create Customer DTO and Mapper
acceptance_criteria:
  - "**Given** the Customer entity and controller exist"
  - "**When** I create the DTO and mapper"
  - "**Then** `CustomerDTO.java` is created with readable field names"
  - "**And** `CustomerMapper.java` converts between Entity and DTO"
  - "**And** DTO includes all fields needed for customer inquiry display"
  - "**And** API responses use DTO (not entity directly)"
tasks:
  - "Create `CustomerDTO.java` record/class in `dto` package"
  - "Create `CustomerMapper.java` in `mapper` package (using MapStruct or manual mapping)"
  - "Refactor `CustomerController.java` to use `CustomerDTO` in `ApiResponse`"
  - "Refactor `CustomerService` or `CustomerController` to perform mapping"
  - "Update tests to verify DTO usage"
---

# Story 2.6: Create Customer DTO and Mapper

## Background
Currently, the `CustomerController` returns the `Customer` JPA Entity directly. This exposes the database integrity to the API layer and couples the API contract tightly to the database schema. We need to introduce a Data Transfer Object (DTO) to decouple these layers.

## Requirements
1.  **CustomerDTO**: Create a POJO/Record that represents the API view of a customer.
    *   Fields should match the `Customer` entity but be decoupled.
    *   Good place to handle any formatting if necessary (though preferably raw data).
2.  **CustomerMapper**: Implement a mapper to convert `Customer` -> `CustomerDTO`.
    *   Can be a simple utility class or component.
3.  **Refactor Controller**: Update `CustomerController` to return `ApiResponse<CustomerDTO>` instead of `ApiResponse<Customer>`.

## Implementation Plan
1.  Define `CustomerDTO` in `com.smeup.backend.dto`.
2.  Define `CustomerMapper` in `com.smeup.backend.mapper`.
3.  Update `CustomerController` to inject Mapper and use it.
4.  Update `CustomerControllerTest` to expect DTO structure (which should be identical for now, but typed differently).

## Change Log
- 2025-12-30: Implemented Story 2.6. Created `CustomerDTO` record, `CustomerMapper` component, and `CustomerMapperTest`. Refactored `CustomerController` to use DTOs. Verified all tests pass.
