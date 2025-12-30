---
stepsCompleted: [1, 2, 3]
inputDocuments:
  - '_bmad-output/planning-artifacts/prd.md'
  - '_bmad-output/planning-artifacts/architecture.md'
project_name: 'rpgle-transformer-bmad'
---

# rpgle-transformer-bmad - Epic Breakdown

## Overview

This document provides the complete epic and story breakdown for rpgle-transformer-bmad, decomposing the requirements from the PRD and Architecture into implementable stories.

## Requirements Inventory

### Functional Requirements

**RPGLE Input Processing**
- FR1: System can accept RPGLE source code files as input
- FR2: System can accept DDS display file definitions as input
- FR3: System can accept DDS physical file definitions as input
- FR4: System can validate that all required input files are present for a transformation

**AI Semantic Analysis**
- FR5: System can analyze RPGLE business logic to understand semantic meaning
- FR6: System can generate plain English explanations of RPGLE program functionality
- FR7: System can identify business rules embedded in RPGLE code
- FR8: System can extract data flow patterns from RPGLE programs
- FR9: System can map DDS display file structures to UI interaction patterns
- FR10: System can map DDS physical file structures to data model requirements

**Code Generation - Spring Boot Backend**
- FR11: System can generate Spring Boot REST API code from RPGLE programs
- FR12: System can generate JPA entity classes from DDS physical file definitions
- FR13: System can generate Spring service layer code implementing RPGLE business logic
- FR14: System can generate REST controller endpoints for program operations
- FR15: System can include inline comments in generated Java code referencing original RPGLE sources

**Code Generation - React Frontend**
- FR16: System can generate React UI components from DDS display file definitions
- FR17: System can generate form input components from green-screen PROMPT formats
- FR18: System can generate detail display components from green-screen DETAIL formats
- FR19: System can include inline comments in generated React code referencing original DDS sources

**Automated Test Generation**
- FR20: System can generate automated test suites alongside transformed code
- FR21: System can generate tests covering identified business logic scenarios
- FR22: System can generate tests for edge cases and validation rules
- FR23: System can generate tests validating functional equivalence to original RPGLE behavior

**Documentation & Analysis**
- FR24: System can generate transformation analysis documents for each converted program
- FR25: Transformation analysis includes plain English RPGLE program overview
- FR26: Transformation analysis includes business logic summary
- FR27: Transformation analysis includes data flow analysis
- FR28: Transformation analysis includes DDS file mapping explanations
- FR29: Transformation analysis includes transformation decision rationale
- FR30: Transformation analysis includes edge cases identified
- FR31: Transformation analysis includes test coverage description

**Developer Workflow Support**
- FR32: Developers can execute transformations without RPGLE knowledge
- FR33: Developers can review AI-generated business logic explanations before transformation
- FR34: Developers can access generated Spring Boot code with RPGLE reference comments
- FR35: Developers can access generated React code with DDS reference comments
- FR36: Developers can access transformation analysis documents
- FR37: Developers can access auto-generated test suites

**Validation & Quality Assurance**
- FR38: QA engineers can run auto-generated test suites to validate functional equivalence
- FR39: QA engineers can compare React UI behavior to original green-screen interface
- FR40: QA engineers can validate that business logic is preserved in transformations
- FR41: Developers can verify generated code follows Spring Boot best practices
- FR42: Developers can verify generated code follows React best practices
- FR43: System can validate generated code quality meets maintainability standards

**Progress Tracking & Visibility**
- FR44: Project managers can track transformation progress across multiple programs
- FR45: Project managers can view velocity metrics (programs completed per week)
- FR46: Project managers can view quality gate indicators (test pass rates, code quality)
- FR47: Project managers can view timeline predictions for completion

**Knowledge & Pattern Library**
- FR48: System provides reference implementation examples for common RPGLE patterns
- FR49: System provides pattern library for RPGLE→Spring/React transformations
- FR50: System provides code examples demonstrating transformation approach

### NonFunctional Requirements

**Transformation Accuracy & Correctness**
- NFR1: Generated code must achieve 100% functional equivalence to original RPGLE (ZERO TOLERANCE)
- NFR2: DDS physical file to JPA entity mapping must preserve all field types, lengths, and constraints

**Code Quality & Maintainability**
- NFR3: Generated Java code must pass standard linters (CheckStyle, PMD) with zero critical violations
- NFR4: Generated React code must pass ESLint with zero critical violations
- NFR5: Code must be production-ready without requiring significant cleanup or refactoring

**Performance**
- NFR6: Typical program transformation should complete in under 4 hours
- NFR7: Generated REST APIs must respond within 2 seconds for typical CRUD operations

**Reliability & Robustness**
- NFR8: Re-running transformation must produce functionally equivalent output
- NFR9: System must handle incomplete or malformed input files gracefully
- NFR10: Auto-generated test suites must achieve minimum 80% code coverage

**Usability & Developer Experience**
- NFR11: Developers with only Java/Spring/React skills must succeed (ZERO RPGLE knowledge required)
- NFR12: Transformation analysis must provide complete explanation of RPGLE program functionality
- NFR13: All error messages must be actionable with clear explanation of problem

**Testability & Validation**
- NFR14: System must automatically validate functional equivalence through generated test suites
- NFR15: Generated React UI must support side-by-side visual comparison with original green-screen

### Additional Requirements

**From Architecture Document:**

**Starter Template (CRITICAL - Epic 1 Story 1):**
- Spring Boot 4.0.1 with Java 21 (via start.spring.io)
- React 19.2.1 with TypeScript + Vite 6 (via npm create vite)
- PostgreSQL 16 for production, H2 for testing
- Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Validation

**Contract-First API Design:**
- OpenAPI 3.0 specification as single source of truth
- Generate Spring Boot from API contract
- Generate React TypeScript client from API contract

**Database ↔ Java Entity Mapping Pattern:**
- SQL table names = DDS Physical File names (e.g., CUSTMAST)
- Java entity classes = Readable names (e.g., Customer)
- SQL column names = DDS field names (e.g., CUSTID)
- Java fields = Readable camelCase (e.g., customerId)

**Testing Architecture:**
- JUnit 5 + Mockito for Java unit tests
- Testcontainers for integration tests with real PostgreSQL
- Vitest + React Testing Library for React unit tests
- Playwright for E2E tests

**Code Quality Standards:**
- Checkstyle (Google Java Style) + PMD for Java
- ESLint + Prettier for React/TypeScript
- Cyclomatic complexity ≤ 10 per method
- No `any` types in TypeScript

**RPGLE Traceability (CRITICAL):**
- Every generated file must include inline comments referencing original RPGLE/DDS
- Transformation analysis document for each program

### FR Coverage Map

| FR | Epic | Description |
|----|------|-------------|
| FR1-4 | Epic 2 | RPGLE Input Processing |
| FR5-10 | Epic 2 | AI Semantic Analysis |
| FR11-15 | Epic 2 | Spring Boot Generation |
| FR16-19 | Epic 3 | React Frontend Generation |
| FR20-23 | Epic 4 | Automated Test Generation |
| FR24-31 | Epic 5 | Documentation & Analysis |
| FR32-37 | Epic 5 | Developer Workflow Support |
| FR38-43 | Epic 4 | Validation & QA |
| FR44-47 | Epic 6 | Progress Tracking |
| FR48-50 | Epic 6 | Pattern Library |

## Epic List

### Epic 1: Project Foundation & Development Environment
Developers have a ready-to-develop project structure with Spring Boot 4.0.1 + React 19.2.1 + testing infrastructure configured.

**FRs covered:** Architecture requirements (Starter Template)
**NFRs addressed:** NFR3-5 (Code Quality standards configured)

### Epic 2: CUST001 Backend Transformation
Developers can transform RPGLE + DDS files into a working Spring Boot REST API with customer inquiry functionality.

**FRs covered:** FR1-4, FR5-10, FR11-15
**NFRs addressed:** NFR1-2 (Functional Equivalence), NFR6-7 (Performance)

### Epic 3: CUST001 Frontend Transformation
Developers can transform DDS display files into a working React UI that connects to the backend API.

**FRs covered:** FR16-19
**NFRs addressed:** NFR4 (React Code Quality)

### Epic 4: Automated Testing & Validation
QA engineers can validate 100% functional equivalence using auto-generated test suites.

**FRs covered:** FR20-23, FR38-43
**NFRs addressed:** NFR10 (80% Code Coverage), NFR14-15 (Automated Validation)

### Epic 5: Transformation Documentation & Developer Workflow
Developers with zero RPGLE knowledge can understand and maintain the generated code.

**FRs covered:** FR24-31, FR32-37
**NFRs addressed:** NFR11-13 (Developer Experience)

### Epic 6: Migration Scaling & Pattern Library
Project managers can track progress and developers can apply patterns to remaining 49 programs.

**FRs covered:** FR44-47, FR48-50
**NFRs addressed:** All NFRs at scale

---

## Epic 1: Project Foundation & Development Environment

Developers have a ready-to-develop project structure with Spring Boot 4.0.1 + React 19.2.1 + testing infrastructure configured.

### Story 1.1: Initialize Spring Boot Backend Project

As a **developer**,
I want **a Spring Boot 4.0.1 project initialized with Java 21 and essential dependencies**,
So that **I have a ready-to-develop backend foundation following project architecture standards**.

**Acceptance Criteria:**

**Given** the project repository exists
**When** I run the Spring Boot initialization command from architecture.md
**Then** a `backend/` directory is created with Spring Boot 4.0.1 project structure
**And** pom.xml includes dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Validation
**And** Java 21 is configured as the compiler version
**And** the project compiles successfully with `mvn compile`

### Story 1.2: Initialize React Frontend Project

As a **developer**,
I want **a React 19 + TypeScript + Vite project initialized**,
So that **I have a ready-to-develop frontend foundation following project architecture standards**.

**Acceptance Criteria:**

**Given** the backend project exists
**When** I run the React initialization command from architecture.md
**Then** a `frontend/` directory is created with React 19 + TypeScript + Vite 6 structure
**And** package.json includes React 19.2.1, TypeScript, and Vite 6
**And** the project builds successfully with `npm run build`
**And** the dev server starts with `npm run dev`

### Story 1.3: Configure Development Environment

As a **developer**,
I want **Docker Compose configured for local PostgreSQL and development environment**,
So that **I can run the full stack locally without external dependencies**.

**Acceptance Criteria:**

**Given** both backend and frontend projects exist
**When** I run `docker-compose up`
**Then** PostgreSQL 16 container starts and is accessible on port 5432
**And** backend application.yml is configured for local PostgreSQL connection
**And** backend connects successfully to the database on startup

### Story 1.4: Configure Testing Infrastructure

As a **developer**,
I want **testing frameworks configured for both backend and frontend**,
So that **I can run automated tests as part of the development workflow**.

**Acceptance Criteria:**

**Given** the development environment is configured
**When** I run `mvn test` in backend
**Then** JUnit 5 tests execute successfully (sample test passes)
**And** Testcontainers is available for integration tests

**Given** the frontend project exists
**When** I run `npm test` in frontend
**Then** Vitest tests execute successfully (sample test passes)
**And** React Testing Library is available for component tests

### Story 1.5: Configure Code Quality Tools

As a **developer**,
I want **code quality tools configured per architecture standards**,
So that **generated code meets quality standards automatically**.

**Acceptance Criteria:**

**Given** both projects exist
**When** I run `mvn checkstyle:check` in backend
**Then** Checkstyle (Google Java Style) validates code quality

**Given** frontend project exists
**When** I run `npm run lint` in frontend
**Then** ESLint + Prettier validate code quality
**And** TypeScript strict mode is enabled

---

## Epic 2: CUST001 Backend Transformation

Developers can transform RPGLE + DDS files into a working Spring Boot REST API with customer inquiry functionality.

### Story 2.1: Create Customer Entity from DDS Physical File

As a **developer**,
I want **a JPA Customer entity generated from CUSTMAST.dds DDS physical file**,
So that **I have a database model that maps to the original AS/400 data structure**.

**Acceptance Criteria:**

**Given** CUSTMAST.dds DDS physical file exists in source-rpgle/dds/physical-files/
**When** I analyze the DDS file and generate the JPA entity
**Then** a `Customer.java` entity class is created in backend/src/main/java/com/smeup/entity/
**And** `@Table(name = "CUSTMAST")` annotation uses the DDS physical file name
**And** `@Column(name = "CUSTID")` annotations use original DDS field names
**And** Java field names use readable camelCase (e.g., `customerId`, `customerName`)
**And** inline comments reference the original DDS file and field definitions

### Story 2.2: Create Flyway Migration for CUSTMAST Table

As a **developer**,
I want **a Flyway migration script that creates the CUSTMAST table**,
So that **the database schema matches the original DDS physical file structure**.

**Acceptance Criteria:**

**Given** the Customer entity exists
**When** I create the Flyway migration
**Then** `V1__create_custmast.sql` is created in backend/src/main/resources/db/migration/
**And** the table is named `CUSTMAST` (matching DDS physical file)
**And** column names match DDS field names (CUSTID, CUSTNM, etc.)
**And** column types preserve DDS field types and lengths
**And** migration runs successfully on application startup

### Story 2.3: Create Customer Repository

As a **developer**,
I want **a Spring Data JPA repository for Customer entity**,
So that **I can perform database operations on customer data**.

**Acceptance Criteria:**

**Given** the Customer entity and database table exist
**When** I create the repository
**Then** `CustomerRepository.java` is created extending JpaRepository
**And** custom query method `findByCustomerId(String customerId)` exists
**And** inline comments reference the original RPGLE CHAIN operation
**And** repository integrates correctly with Spring Data JPA

### Story 2.4: Create Customer Service with Business Logic

As a **developer**,
I want **a service layer implementing the CUST001 business logic**,
So that **the customer inquiry functionality matches the original RPGLE behavior**.

**Acceptance Criteria:**

**Given** the Customer repository exists
**When** I create the service implementing CUST001 business logic
**Then** `CustomerService.java` is created with `findCustomerById()` method
**And** validation logic matches original RPGLE (customer number validation)
**And** error handling matches original RPGLE behavior (invalid customer, not found)
**And** inline comments reference original RPGLE lines and logic
**And** service uses `@Transactional` for database operations

### Story 2.5: Create Customer REST Controller

As a **developer**,
I want **a REST controller exposing customer inquiry endpoints**,
So that **the frontend can access customer data via API**.

**Acceptance Criteria:**

**Given** the Customer service exists
**When** I create the REST controller
**Then** `CustomerController.java` is created with `GET /api/v1/customers/{customerId}` endpoint
**And** response format follows architecture standard: `{ data: {...}, meta: {...} }`
**And** error responses use RFC 7807 Problem Details format
**And** inline comments reference original RPGLE program operations
**And** endpoint returns customer details for valid customer ID
**And** endpoint returns 404 for invalid customer ID

### Story 2.6: Create Customer DTO and Mapper

As a **developer**,
I want **a DTO class and mapper for Customer API responses**,
So that **the API returns properly structured data separate from the entity**.

**Acceptance Criteria:**

**Given** the Customer entity and controller exist
**When** I create the DTO and mapper
**Then** `CustomerDTO.java` is created with readable field names
**And** `CustomerMapper.java` converts between Entity and DTO
**And** DTO includes all fields needed for customer inquiry display
**And** API responses use DTO (not entity directly)

---

## Epic 3: CUST001 Frontend Transformation

Developers can transform DDS display files into a working React UI that connects to the backend API.

### Story 3.1: Create API Client for Customer Endpoints

As a **developer**,
I want **a TypeScript API client for customer endpoints**,
So that **React components can communicate with the backend API type-safely**.

**Acceptance Criteria:**

**Given** the backend Customer API is available
**When** I create the API client
**Then** `frontend/src/api/customers.ts` is created with typed API functions
**And** `getCustomerById(customerId: string)` function exists
**And** response types match CustomerDTO structure
**And** error handling returns typed error responses
**And** API base URL is configurable via environment variable

### Story 3.2: Create Customer Types from DDS Display File

As a **developer**,
I want **TypeScript types generated from CUSTDSP.dds display file structure**,
So that **the frontend has type-safe models matching the original display fields**.

**Acceptance Criteria:**

**Given** CUSTDSP.dds exists in source-rpgle/dds/display-files/
**When** I analyze the display file and generate types
**Then** `frontend/src/features/customers/customer.types.ts` is created
**And** `Customer` interface includes all display fields (readable names)
**And** inline comments reference original DDS display file fields
**And** types are exported for use in components

### Story 3.3: Create Customer Search Form Component

As a **developer**,
I want **a React component for customer number input (PROMPT format)**,
So that **users can enter a customer number to search, matching the green-screen PROMPT**.

**Acceptance Criteria:**

**Given** the API client and types exist
**When** I create the search form component
**Then** `frontend/src/features/customers/CustomerSearch.tsx` is created
**And** form includes customer number input field
**And** form validates customer number format before submission
**And** form submits on Enter key (matching green-screen behavior)
**And** inline comments reference original CUSTDSP PROMPT format
**And** component uses React Hook Form for form handling

### Story 3.4: Create Customer Detail Display Component

As a **developer**,
I want **a React component displaying customer details (DETAIL format)**,
So that **users can view customer information, matching the green-screen DETAIL display**.

**Acceptance Criteria:**

**Given** the customer types exist
**When** I create the detail display component
**Then** `frontend/src/features/customers/CustomerDetail.tsx` is created
**And** component displays all customer fields from CUSTDSP DETAIL format
**And** field layout reflects original green-screen arrangement
**And** inline comments reference original DDS display fields
**And** component handles loading and error states

### Story 3.5: Create Customer Inquiry Page with React Query

As a **developer**,
I want **a complete customer inquiry page using React Query for state management**,
So that **the full customer lookup workflow functions in the browser**.

**Acceptance Criteria:**

**Given** CustomerSearch and CustomerDetail components exist
**When** I create the inquiry page
**Then** `frontend/src/features/customers/CustomerInquiry.tsx` is created
**And** page combines search form and detail display
**And** `useCustomer` hook uses React Query for API calls
**And** successful search displays customer details
**And** failed search displays error message
**And** loading state shows while fetching
**And** page is accessible at `/customers` route

### Story 3.6: Style Customer Components with Tailwind CSS

As a **developer**,
I want **customer components styled with Tailwind CSS**,
So that **the UI is modern, responsive, and consistent with architecture standards**.

**Acceptance Criteria:**

**Given** all customer components exist
**When** I apply Tailwind CSS styling
**Then** components have consistent visual styling
**And** layout is responsive (works on desktop and mobile)
**And** form inputs have proper focus states and validation feedback
**And** error messages are visually distinct
**And** styling follows Tailwind best practices (utility classes)

---

## Epic 4: Automated Testing & Validation

QA engineers can validate 100% functional equivalence using auto-generated test suites.

### Story 4.1: Create Backend Unit Tests for Customer Service

As a **QA engineer**,
I want **JUnit 5 unit tests for CustomerService business logic**,
So that **I can validate the service layer implements RPGLE logic correctly**.

**Acceptance Criteria:**

**Given** CustomerService exists with business logic
**When** I create unit tests
**Then** `CustomerServiceTest.java` is created in backend/src/test/java/
**And** tests cover valid customer lookup scenario
**And** tests cover invalid customer number validation
**And** tests cover customer not found scenario
**And** tests use Mockito to mock repository
**And** inline comments reference original RPGLE logic being tested
**And** code coverage for CustomerService is >= 80%

### Story 4.2: Create Backend Integration Tests with Testcontainers

As a **QA engineer**,
I want **integration tests using Testcontainers with real PostgreSQL**,
So that **I can validate the full backend stack works with actual database**.

**Acceptance Criteria:**

**Given** backend application and database migration exist
**When** I create integration tests
**Then** `CustomerIntegrationTest.java` is created
**And** tests use Testcontainers to spin up PostgreSQL
**And** tests verify Customer entity persists correctly to CUSTMAST table
**And** tests verify API endpoint returns correct data
**And** tests validate SQL uses DDS column names (CUSTID, CUSTNM)
**And** tests clean up data after execution

### Story 4.3: Create Backend Controller Tests

As a **QA engineer**,
I want **MockMvc tests for CustomerController REST endpoints**,
So that **I can validate API responses match expected format**.

**Acceptance Criteria:**

**Given** CustomerController exists
**When** I create controller tests
**Then** `CustomerControllerTest.java` is created
**And** tests verify `GET /api/v1/customers/{id}` returns 200 with valid data
**And** tests verify 404 response for non-existent customer
**And** tests verify response format matches `{ data: {...}, meta: {...} }`
**And** tests verify error format matches RFC 7807
**And** tests use @WebMvcTest for isolated controller testing

### Story 4.4: Create Frontend Component Tests with Vitest

As a **QA engineer**,
I want **Vitest + React Testing Library tests for customer components**,
So that **I can validate React components render and behave correctly**.

**Acceptance Criteria:**

**Given** customer React components exist
**When** I create component tests
**Then** `CustomerSearch.test.tsx` tests form input and validation
**And** `CustomerDetail.test.tsx` tests data display rendering
**And** `CustomerInquiry.test.tsx` tests full page workflow
**And** tests mock API calls using MSW or similar
**And** tests verify loading states, error states, and success states
**And** code coverage for customer components is >= 80%

### Story 4.5: Create E2E Tests with Playwright

As a **QA engineer**,
I want **Playwright end-to-end tests for customer inquiry workflow**,
So that **I can validate the full user journey works correctly**.

**Acceptance Criteria:**

**Given** frontend and backend are running
**When** I create E2E tests
**Then** `frontend/e2e/customer-inquiry.spec.ts` is created
**And** test navigates to customer inquiry page
**And** test enters valid customer number and submits
**And** test verifies customer details are displayed
**And** test verifies error message for invalid customer
**And** tests can run against local Docker environment

### Story 4.6: Create Functional Equivalence Validation Test

As a **QA engineer**,
I want **a test that validates 100% functional equivalence to RPGLE behavior**,
So that **I can confirm the transformation preserves all business logic**.

**Acceptance Criteria:**

**Given** test data is available (input -> expected output)
**When** I run the equivalence validation test
**Then** test loads customer test data from test resources
**And** test executes customer inquiry for each test case
**And** test compares actual output to expected output
**And** test reports any functional differences
**And** 100% of test cases must pass for validation success
**And** test results are documented in validation report

---

## Epic 5: Transformation Documentation & Developer Workflow

Developers with zero RPGLE knowledge can understand and maintain the generated code.

### Story 5.1: Create CUST001 Transformation Analysis Document

As a **developer**,
I want **a comprehensive analysis document explaining CUST001 transformation**,
So that **I can understand the business logic without reading RPGLE code**.

**Acceptance Criteria:**

**Given** CUST001 transformation is complete
**When** I create the analysis document
**Then** `docs/transformations/CUST001/analysis.md` is created
**And** document includes plain English program overview
**And** document explains what CUST001 does (customer inquiry workflow)
**And** document describes input/output behavior
**And** document is understandable by developers with zero RPGLE knowledge

### Story 5.2: Document Business Logic Mapping

As a **developer**,
I want **documentation mapping RPGLE business rules to Java implementation**,
So that **I can trace any business logic back to its original source**.

**Acceptance Criteria:**

**Given** the analysis document exists
**When** I document business logic mapping
**Then** `docs/transformations/CUST001/business-logic.md` is created
**And** each business rule is listed with RPGLE source reference
**And** corresponding Java method is identified for each rule
**And** validation rules are documented with examples
**And** error handling behavior is documented

### Story 5.3: Document DDS to Modern Stack Mapping

As a **developer**,
I want **documentation showing DDS file to JPA/React mapping**,
So that **I can understand how legacy data structures map to modern code**.

**Acceptance Criteria:**

**Given** transformation is complete
**When** I create the data mapping document
**Then** `docs/transformations/CUST001/data-mapping.md` is created
**And** CUSTMAST.dds fields are mapped to Customer entity fields
**And** CUSTDSP.dds fields are mapped to React component props
**And** table shows: DDS Field -> SQL Column -> Java Field -> TypeScript Field
**And** any data type transformations are explained

### Story 5.4: Create Inline Code Documentation Standards

As a **developer**,
I want **all generated code to include inline RPGLE reference comments**,
So that **I can understand the original context while reading modern code**.

**Acceptance Criteria:**

**Given** all CUST001 code is generated
**When** I verify inline documentation
**Then** every Java file includes header comment with RPGLE source reference
**And** every React file includes header comment with DDS source reference
**And** complex business logic methods include line-by-line RPGLE references
**And** comments explain intent, not just syntax translation
**And** comments follow JavaDoc/JSDoc format standards

### Story 5.5: Create Developer Transformation Workflow Guide

As a **developer**,
I want **a step-by-step guide for executing transformations**,
So that **I can independently transform other RPGLE programs**.

**Acceptance Criteria:**

**Given** CUST001 transformation is complete and validated
**When** I create the workflow guide
**Then** `docs/workflow-guide.md` is created
**And** guide covers: preparation, transformation, validation, integration phases
**And** guide requires zero RPGLE knowledge to follow
**And** guide references example files from CUST001 transformation
**And** guide includes troubleshooting section for common issues

### Story 5.6: Create Transformation Validation Checklist

As a **developer**,
I want **a checklist for validating each transformation**,
So that **I can ensure quality and completeness before marking done**.

**Acceptance Criteria:**

**Given** workflow guide exists
**When** I create the validation checklist
**Then** checklist is included in workflow-guide.md
**And** checklist covers: code compiles, tests pass, linting passes
**And** checklist covers: inline comments present, documentation created
**And** checklist covers: functional equivalence validated
**And** checklist can be used as PR review template

---

## Epic 6: Migration Scaling & Pattern Library

Project managers can track progress and developers can apply patterns to remaining 49 programs.

### Story 6.1: Create Transformation Progress Tracking Structure

As a **project manager**,
I want **a structured way to track transformation progress across all 50 programs**,
So that **I can monitor migration status and velocity**.

**Acceptance Criteria:**

**Given** CUST001 transformation is complete
**When** I create the progress tracking structure
**Then** `docs/migration-status.md` is created
**And** document lists all 50 RPGLE programs to transform
**And** each program has status: Not Started / In Progress / Complete / Validated
**And** document includes completion date for finished transformations
**And** document can be updated as transformations progress

### Story 6.2: Create Velocity Metrics Tracking

As a **project manager**,
I want **metrics showing programs completed per week**,
So that **I can predict timeline and identify velocity issues early**.

**Acceptance Criteria:**

**Given** progress tracking exists
**When** I add velocity metrics
**Then** migration-status.md includes weekly velocity section
**And** metrics show: programs completed this week, running average
**And** metrics show: projected completion date based on current velocity
**And** target velocity (2-3 programs/week) is clearly indicated
**And** any velocity drops are visible for early intervention

### Story 6.3: Create Quality Gate Tracking

As a **project manager**,
I want **quality gate indicators for each transformation**,
So that **I can ensure all transformations meet quality standards**.

**Acceptance Criteria:**

**Given** velocity metrics exist
**When** I add quality gate tracking
**Then** each program in migration-status.md has quality indicators
**And** indicators include: tests pass, linting pass, documentation complete
**And** indicators include: functional equivalence validated
**And** overall quality summary shows percentage meeting all gates
**And** any quality failures are flagged for attention

### Story 6.4: Create RPGLE Pattern Reference Library

As a **developer**,
I want **a reference library of common RPGLE to Spring/React transformation patterns**,
So that **I can apply proven patterns to future transformations**.

**Acceptance Criteria:**

**Given** CUST001 transformation patterns are documented
**When** I create the pattern library
**Then** `templates/patterns/` directory is created
**And** `customer-inquiry-pattern.md` documents the CUST001 pattern
**And** pattern includes: RPGLE structure to Java structure mapping
**And** pattern includes: DDS display to React component mapping
**And** pattern includes: common variations and edge cases

### Story 6.5: Create AI Prompt Templates for Transformation

As a **developer**,
I want **reusable prompt templates for Claude Code/Copilot transformations**,
So that **I can achieve consistent results across all 50 programs**.

**Acceptance Criteria:**

**Given** pattern library exists
**When** I create prompt templates
**Then** `templates/prompts/semantic-analysis.md` is created for RPGLE analysis
**And** `templates/prompts/spring-boot-generation.md` is created for backend
**And** `templates/prompts/react-generation.md` is created for frontend
**And** templates reference project-context.md for consistency rules
**And** templates include example inputs and expected outputs

### Story 6.6: Create Example Transformation Reference

As a **developer**,
I want **the CUST001 transformation preserved as a reference example**,
So that **I can learn from it when transforming similar programs**.

**Acceptance Criteria:**

**Given** CUST001 transformation is complete and validated
**When** I organize the reference example
**Then** `examples/cust001-customer-inquiry/` directory is organized
**And** directory includes: original RPGLE/DDS files
**And** directory includes: generated Spring Boot code
**And** directory includes: generated React code
**And** directory includes: generated tests
**And** directory includes: transformation documentation
**And** README explains how to use this as a reference
