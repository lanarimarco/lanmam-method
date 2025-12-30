---
stepsCompleted: [1, 2, 3, 4, 5, 6, 7, 8]
inputDocuments:
  - '_bmad-output/planning-artifacts/product-brief-rpgle-transformer-bmad-2025-12-29.md'
  - '_bmad-output/planning-artifacts/prd.md'
workflowType: 'architecture'
project_name: 'rpgle-transformer-bmad'
user_name: 'Lana'
date: '2025-12-29'
status: 'complete'
completedAt: '2025-12-30'
---

# Architecture Decision Document

_This document builds collaboratively through step-by-step discovery. Sections are appended as we work through each architectural decision together._

## Project Context Analysis

### Requirements Overview

**Functional Requirements: 50 FRs across 8 Capability Areas**

The rpgle-transformer-bmad requires a sophisticated multi-component architecture organized around these core capabilities:

**1. RPGLE Input Processing (FR1-4)**
- Accept RPGLE source code, DDS display files, DDS physical files
- Validate presence of all required input files
- **Architectural Implication:** Needs robust file parsing and validation layer

**2. AI Semantic Analysis (FR5-10)**
- Analyze RPGLE business logic for semantic meaning (not just syntax)
- Generate plain English explanations of program functionality
- Identify business rules, extract data flow patterns
- Map DDS structures to modern UI/data patterns
- **Architectural Implication:** Core AI pipeline using Claude/LLMs - centerpiece of entire system

**3. Code Generation - Spring Boot Backend (FR11-15)**
- Generate REST APIs, JPA entities, service layers, controllers
- Include inline RPGLE reference comments
- **Architectural Implication:** Template-based code generation with semantic context injection

**4. Code Generation - React Frontend (FR16-19)**
- Generate UI components from DDS display files
- Map green-screen formats (PROMPT/DETAIL) to modern React patterns
- Include inline DDS reference comments
- **Architectural Implication:** Coordinated with backend generation to ensure API alignment

**5. Automated Test Generation (FR20-23)**
- Generate test suites covering business logic scenarios, edge cases, validation rules
- Validate functional equivalence to original RPGLE
- **Architectural Implication:** Test generation must run alongside code generation, informed by semantic analysis

**6. Documentation & Analysis (FR24-31)**
- Generate transformation analysis documents for each program
- Include program overview, business logic summary, data flow, DDS mappings, transformation decisions, edge cases
- **Architectural Implication:** Documentation generation pipeline parallel to code generation

**7. Developer Workflow Support (FR32-37)**
- Enable transformation execution without RPGLE knowledge
- Provide access to AI explanations, generated code, analysis docs, test suites
- **Architectural Implication:** Developer-facing interface/CLI with clear workflow orchestration

**8. Validation & Quality Assurance (FR38-43)**
- Support test execution, UI comparison, business logic validation
- Verify code follows Spring Boot and React best practices
- Validate code quality meets maintainability standards
- **Architectural Implication:** Quality gates and validation pipeline integrated into transformation workflow

**9. Progress Tracking & Visibility (FR44-47)**
- Track transformation progress across multiple programs
- Display velocity metrics, quality indicators, timeline predictions
- **Architectural Implication:** Dashboard/reporting system with metrics collection

**10. Knowledge & Pattern Library (FR48-50)**
- Provide reference implementations, pattern library, code examples
- **Architectural Implication:** Growing knowledge base that improves with each transformation

---

### Non-Functional Requirements

**Critical NFRs That Drive Architecture:**

**NFR1: 100% Functional Equivalence (ZERO TOLERANCE)**
- This is the primary architectural constraint
- Every architectural decision must support validation of functional equivalence
- Drives: Automated test generation, validation pipeline, quality gates
- **Business Impact:** 1,000,000€ at risk if equivalence fails

**NFR3-5: Code Quality & Maintainability**
- Generated code must pass linters with zero critical violations
- Cyclomatic complexity ≤ 10 for service methods
- Production-ready code without requiring cleanup
- **Drives:** Code generation templates, quality validation, static analysis integration

**NFR6: Transformation Speed (4-hour target per program)**
- Must support 2-3 programs/week velocity
- Typical transformation completes in under 4 hours
- **Drives:** Performance optimization of AI pipeline, parallel processing where possible

**NFR11: Zero RPGLE Knowledge Requirement**
- Developers with only Java/Spring/React skills must succeed
- AI handles ALL semantic understanding
- **Drives:** Plain English explanations, comprehensive documentation, inline comments architecture

**NFR14-15: Automated & Manual Validation**
- Automated test execution and functional equivalence validation
- Side-by-side UI comparison support
- **Drives:** Test generation, validation framework, comparison tooling

---

### Scale & Complexity Assessment

**Project Complexity: MEDIUM-HIGH**

**High Complexity Factors:**
- AI/ML semantic code understanding (cutting-edge, novel approach)
- Multi-language code generation (RPGLE → Spring Boot + React)
- 100% functional equivalence requirement (zero tolerance quality bar)
- Mission-critical business impact (1M€ at risk)

**Medium Complexity Factors:**
- Automated test generation
- Documentation generation
- Progress tracking dashboard
- Developer workflow orchestration

**Primary Technical Domain:** AI-Powered Code Transformation & Migration Tooling

**Estimated Architectural Components:**
- AI Semantic Analysis Engine (core)
- Multi-Target Code Generator (Spring Boot + React)
- Test Suite Generator
- Documentation Generator
- Validation Pipeline
- Progress Tracking System
- Knowledge Base / Pattern Library
- Developer Workflow Orchestrator

---

### Technical Constraints & Dependencies

**AI/ML Dependencies:**
- **Claude (Anthropic)** or equivalent LLM with semantic code understanding capabilities
- AI must handle RPGLE business logic analysis without human RPGLE expertise
- Prompts and templates provided by user for AI consistency

**Input/Output Constraints:**
- **Input:** RPGLE source code + DDS display files + DDS physical files
- **Output:** Spring Boot (Java 17+, Spring Boot 3.x) + React (18+, TypeScript) + JUnit tests + Jest/React Testing Library tests

**Technology Stack Requirements (from PRD NFRs):**
- **Backend:** Java 17+, Spring Boot 3.x, Spring Data JPA, PostgreSQL/MySQL
- **Frontend:** React 18+, TypeScript, modern component patterns
- **Testing:** JUnit (Java), Jest/React Testing Library (React)

**Quality Gates:**
- Code must pass CheckStyle, PMD (Java) with zero critical violations
- Code must pass ESLint (React) with zero critical violations
- Automated tests must achieve 80%+ code coverage
- All tests must pass before transformation considered complete

**External Inputs Provided:**
- **Templates:** User provides AI transformation templates for consistency
- **Test Data:** User provides test data for validation pipeline

---

### Architectural Challenges & Solutions

**Challenge 1: AI Consistency Across 50 Programs**
- **Risk:** AI generates inconsistent patterns across different transformations
- **Solution:** User-provided templates ensure consistent transformation approach
- **Architecture:** Template-driven code generation with semantic context injection

**Challenge 2: Validation Pipeline (100% Functional Equivalence)**
- **Risk:** How to validate equivalence without manual RPGLE expertise?
- **Solution:** User-provided test data enables automated validation
- **Architecture:** Automated test execution framework comparing outputs against known-good test data

**Challenge 3: Multi-Language Coordination (Spring Boot ↔ React)**
- **Risk:** Backend and frontend generated independently could become misaligned
- **Recommended Solution:** **Hybrid Contract-First + Shared Data Model Approach**
  - **Step 1:** AI generates OpenAPI contract from RPGLE semantic analysis (API endpoints, operations)
  - **Step 2:** AI generates shared data model from DDS physical files (entities, fields, relationships)
  - **Step 3:** Generate Spring Boot from API contract + data model (controllers, DTOs, JPA entities)
  - **Step 4:** Generate React from API contract + data model (API client, TypeScript types, components)
  - **Benefit:** API contract and data model are single source of truth - automatic synchronization
  - **Validation:** Contract testing ensures both sides honor the agreement

**Architecture:**
```
RPGLE + DDS Files
    ↓
AI Semantic Analysis
    ↓
    ├─→ OpenAPI Contract (API coordination)
    └─→ Shared Data Model (data consistency)
         ↓
    ├─→ Spring Boot Generation (from contract + model)
    └─→ React Generation (from contract + model)
```

**Challenge 4: Progressive Enhancement (Learning from Transformations)**
- **Risk:** Each transformation starts from scratch without learning from previous successes
- **Recommended Solution:** **Multi-Layered Learning System**

  **Layer 1: Evolving Pattern Library**
  - Extract and catalog successful transformation patterns after each program
  - Build library of proven RPGLE→Spring/React mappings
  - Later transformations reference similar patterns from library
  - Example: "Customer CRUD pattern" becomes reusable after CUST001 success

  **Layer 2: Validation-Driven Improvement**
  - Capture all validation results (successes, failures, edge cases discovered)
  - Feed insights back into AI transformation prompts
  - Refine semantic analysis based on real outcomes
  - Continuous improvement loop

  **Layer 3: Transformation Knowledge Base**
  - Store each transformation as case study (input → output → validation results)
  - AI queries knowledge base: "Have we seen similar RPGLE patterns?"
  - Reuse successful approaches, avoid known pitfalls
  - Institutional knowledge accumulation over 50-program migration

**Architecture:**
```
Transformation N
    ↓
Validation Results
    ↓
Pattern Extraction
    ↓
    ├─→ Pattern Library Update
    ├─→ AI Prompt Refinement
    └─→ Knowledge Base Case Study
         ↓
Transformation N+1 (improved by learning)
```

---

### Cross-Cutting Concerns

**1. Code Quality Assurance**
- Spans all generated output (Spring Boot, React, Tests)
- Requires: Linting integration, static analysis, code metrics validation
- Quality gates before transformation marked complete

**2. Semantic Understanding (Core Competency)**
- Foundational to every transformation
- AI must understand business intent, not just syntax
- Plain English explanations required throughout

**3. Documentation & Traceability**
- Every generated file needs inline RPGLE/DDS references
- Transformation analysis document for each program
- Developers must be able to trace modern code back to legacy sources

**4. Validation & Functional Equivalence**
- Automated test execution against user-provided test data
- Side-by-side UI comparison capability
- 100% equivalence validation required before completion

**5. Developer Experience**
- Zero RPGLE knowledge requirement throughout workflow
- Clear error messages, actionable feedback
- Intuitive transformation workflow
- Progress visibility and confidence building

---

## Starter Template Evaluation

### Primary Technology Domain

**AI-Assisted Code Generation Monorepo** - Claude Code/GitHub Copilot transforms RPGLE → Spring Boot + React with testable artifacts in a single repository.

### Starter Options Considered

| Option | Description | Verdict |
|--------|-------------|---------|
| **Spring Initializr** | Official Spring Boot generator | ✅ Best for backend |
| **Vite React Template** | Official Vite + React + TypeScript | ✅ Best for frontend |
| **Nx Monorepo** | Full monorepo orchestration | ❌ Overkill - AI generates directly |
| **react-springboot-cli** | Combined CLI | ❌ Less flexible |

### Selected Approach: Simple Monorepo with Official Starters

**Rationale:** Since Claude Code/Copilot generates code directly, we don't need complex build orchestration. Simple folder structure with official starters gives maximum flexibility and current best practices.

### Current Versions (December 2025)

| Technology | Version | Notes |
|------------|---------|-------|
| **Spring Boot** | 4.0.1 | Latest stable (Dec 18, 2025) |
| **Java** | 21 (LTS) | Required for Spring Boot 4.x |
| **React** | 19.2.1 | Latest stable |
| **Vite** | 6.x | Fast build tooling |
| **Vitest** | 3.x | React unit testing |

### Initialization Commands

**Backend (Spring Boot):**
```bash
# Via start.spring.io or Spring CLI
# Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Validation
curl https://start.spring.io/starter.zip \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=4.0.1 \
  -d javaVersion=21 \
  -d groupId=com.smeup \
  -d artifactId=backend \
  -d name=backend \
  -d dependencies=web,data-jpa,postgresql,validation \
  -o backend.zip && unzip backend.zip -d backend
```

**Frontend (React + Vite + TypeScript):**
```bash
npm create vite@latest frontend -- --template react-ts
cd frontend && npm install
```

### Project Structure

```
rpgle-transformer-bmad/
├── backend/                    # Spring Boot 4.0 + Java 21
│   ├── src/main/java/
│   ├── src/test/java/          # JUnit 5 tests
│   └── pom.xml
├── frontend/                   # React 19 + TypeScript + Vite 6
│   ├── src/
│   ├── tests/                  # Vitest + React Testing Library
│   └── package.json
├── source-rpgle/               # Original RPGLE source files
├── _bmad-output/               # BMAD workflow artifacts
└── docs/                       # Transformation documentation
```

### Architectural Decisions Provided by Starters

**Spring Boot 4.0 Provides:**
- Java 21 with virtual threads support
- Spring Web for REST APIs
- Spring Data JPA for database access
- Bean Validation (Jakarta)
- JUnit 5 + Mockito for testing
- Maven build with dependency management

**Vite + React 19 Provides:**
- TypeScript configuration
- ES Modules + fast HMR development
- Production build optimization
- Vitest-ready testing infrastructure

**Note:** Project initialization using these commands should be the first implementation story.

---

## Core Architectural Decisions

### Decision Priority Analysis

**Critical Decisions (Block Implementation):**
- Database: PostgreSQL 16 + H2 for testing
- API: REST with OpenAPI 3.0 contract-first
- State Management: React Query + Zustand
- Testing: JUnit 5 + Vitest + Playwright

**Important Decisions (Shape Architecture):**
- Form handling: React Hook Form + Zod
- Styling: Tailwind CSS
- Code quality: Checkstyle/PMD + ESLint/Prettier

**Deferred Decisions (Post-MVP):**
- CI/CD pipeline configuration
- Production deployment strategy
- Monitoring and logging

### Data Architecture

| Decision | Choice | Version | Rationale |
|----------|--------|---------|-----------|
| **Production DB** | PostgreSQL | 16.x | Industry standard, excellent Spring Data support |
| **Test DB** | H2 (embedded) | Latest | Zero-config for unit tests |
| **ORM** | Spring Data JPA | 4.0.x | Included in starter, JPA entities from DDS |
| **Migrations** | Flyway | 10.x | Schema versioning, repeatable migrations |

### API & Communication Patterns

| Decision | Choice | Rationale |
|----------|--------|-----------|
| **API Style** | REST | Simple, well-understood, matches RPGLE CRUD patterns |
| **Contract** | OpenAPI 3.0 | Single source of truth for Spring Boot + React |
| **Documentation** | SpringDoc OpenAPI | Auto-generated from code annotations |
| **Error Format** | RFC 7807 Problem Details | Standard error responses |
| **Validation** | Jakarta Bean Validation | Declarative, annotation-based |

### Frontend Architecture

| Decision | Choice | Version | Rationale |
|----------|--------|---------|-----------|
| **Server State** | React Query | 5.x | Caching, loading states, error handling |
| **Client State** | Zustand | 5.x | Lightweight, simple API |
| **Forms** | React Hook Form | 7.x | Performance, validation integration |
| **Schema Validation** | Zod | 3.x | TypeScript-first, runtime validation |
| **Styling** | Tailwind CSS | 3.x | Utility-first, consistent, no CSS conflicts |
| **HTTP Client** | Generated from OpenAPI | - | Type-safe API calls from contract |

### Testing & Validation Architecture

| Layer | Tool | Purpose |
|-------|------|---------|
| **Unit (Java)** | JUnit 5 + Mockito | Service layer testing |
| **Integration (Java)** | Testcontainers | Real PostgreSQL in tests |
| **Unit (React)** | Vitest + RTL | Component testing |
| **E2E** | Playwright | Full user flow testing |
| **Equivalence** | Custom test harness | Compare against RPGLE test data |

**Functional Equivalence Validation Strategy:**
1. User provides test data (input → expected output)
2. Generated tests execute with test data
3. Compare actual vs expected results
4. 100% match required for transformation approval

### Code Quality Standards

**Backend (Java):**
- Checkstyle: Google Java Style
- PMD: Standard ruleset
- Cyclomatic complexity: ≤ 10 per method

**Frontend (React/TypeScript):**
- ESLint: React + TypeScript recommended
- Prettier: Consistent formatting
- No `any` types allowed

### Infrastructure Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| **Local Dev** | Docker Compose | PostgreSQL + optional services |
| **Backend Build** | Maven | Spring Boot standard |
| **Frontend Build** | Vite | Fast builds, HMR |
| **Monorepo** | Simple folders | No orchestration needed - AI generates directly |

---

## Implementation Patterns & Consistency Rules

### Critical Conflict Points

For RPGLE → Spring Boot + React transformation, AI agents must follow consistent patterns to avoid conflicts across 50 program transformations.

### Naming Patterns

**Database ↔ Java Entity Mapping (KEY PATTERN):**

| Element | Convention | Example |
|---------|------------|---------|
| **SQL Table** | DDS Physical/Logical File name | `CUSTMAST`, `ORDERHDR` |
| **Java Entity** | Readable PascalCase | `Customer`, `OrderHeader` |
| **SQL Column** | DDS Field name | `CUSTID`, `CUSTNM` |
| **Java Field** | Readable camelCase | `customerId`, `customerName` |

**Entity Example:**
```java
@Entity
@Table(name = "CUSTMAST")  // DDS Physical File name
public class Customer {    // Readable class name

    @Id
    @Column(name = "CUSTID")  // DDS field name
    private Long customerId;  // Readable field name

    @Column(name = "CUSTNM")
    private String customerName;
}
```

**Principle:** Code = Readable (for Java/React developers), SQL = DDS names (for traceability)

**API Naming (REST):**

| Element | Convention | Example |
|---------|------------|---------|
| Resources | plural, kebab-case | `/customers`, `/order-items` |
| Path params | camelCase | `/customers/{customerId}` |
| Query params | camelCase | `?pageSize=10&sortBy=name` |
| Actions | verb prefix | `/customers/{id}/activate` |

**Java Naming (Spring Boot):**

| Element | Convention | Example |
|---------|------------|---------|
| Packages | singular, lowercase | `com.smeup.customer` |
| Classes | PascalCase | `CustomerService`, `CustomerRepository` |
| Methods | camelCase, verb prefix | `findByEmail()`, `createCustomer()` |
| Constants | SCREAMING_SNAKE | `MAX_PAGE_SIZE` |

**React Naming (TypeScript):**

| Element | Convention | Example |
|---------|------------|---------|
| Components | PascalCase | `CustomerList`, `CustomerForm` |
| Files | PascalCase.tsx | `CustomerList.tsx` |
| Hooks | use prefix | `useCustomers`, `useCustomerForm` |
| Types | PascalCase + suffix | `CustomerDTO`, `CustomerFormProps` |

### Structure Patterns

**Spring Boot Package Structure:**
```
com.smeup.backend/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/              # Data Transfer Objects
├── entity/           # JPA entities
├── repository/       # Spring Data repositories
├── service/          # Business logic
├── exception/        # Custom exceptions
└── util/             # Utilities
```

**React Folder Structure:**
```
src/
├── components/       # Shared UI components
├── features/         # Feature modules
│   └── customers/
│       ├── CustomerList.tsx
│       ├── CustomerForm.tsx
│       ├── useCustomers.ts
│       └── customer.types.ts
├── hooks/            # Shared hooks
├── api/              # Generated API client
├── types/            # Shared types
└── utils/            # Utilities
```

### Format Patterns

**API Response Format:**
```json
{
  "data": { ... },
  "meta": {
    "timestamp": "2025-12-30T10:00:00Z",
    "requestId": "uuid"
  }
}
```

**Error Response Format (RFC 7807):**
```json
{
  "type": "https://api.smeup.com/errors/validation",
  "title": "Validation Error",
  "status": 400,
  "detail": "Customer email is required",
  "instance": "/customers"
}
```

**Date/Time Format:**
- API: ISO 8601 (`2025-12-30T10:00:00Z`)
- Database: `TIMESTAMP WITH TIME ZONE`
- Display: Localized via `dayjs`

### Communication Patterns

**React Query Keys:**
```typescript
// Pattern: [resource, scope, params]
['customers', 'list', { page: 1 }]
['customers', 'detail', customerId]
```

**Zustand Store Pattern:**
```typescript
// One store per feature
useCustomerStore.getState().setSelectedCustomer(id)
```

### Process Patterns

**Error Handling:**
- Backend: `@ControllerAdvice` global exception handler
- Frontend: React Query `onError` + toast notifications
- Logging: Structured JSON logs with correlation IDs

**Loading States:**
- Use React Query's `isLoading`, `isFetching`
- Skeleton components for initial load
- Inline spinners for mutations

### Enforcement Guidelines

**All AI Agents MUST:**
1. Use `@Table(name = "DDS_FILE")` for entity mapping
2. Use `@Column(name = "DDS_FIELD")` for field mapping
3. Follow naming conventions exactly (no exceptions)
4. Include RPGLE/DDS reference comments in generated code
5. Generate tests alongside code
6. Use RFC 7807 for all error responses

**Pattern Verification:**
- ESLint rules enforce React patterns
- Checkstyle enforces Java patterns
- PR review checklist includes pattern compliance

---

## Project Structure & Boundaries

### Requirements to Structure Mapping

| FR Category | Backend Location | Frontend Location |
|-------------|------------------|-------------------|
| RPGLE Input Processing (FR1-4) | `source-rpgle/` | N/A |
| AI Semantic Analysis (FR5-10) | Claude Code prompts | N/A |
| Spring Boot Generation (FR11-15) | `backend/` | N/A |
| React Generation (FR16-19) | N/A | `frontend/` |
| Test Generation (FR20-23) | `backend/src/test/` | `frontend/src/__tests__/` |
| Documentation (FR24-31) | `docs/transformations/` | N/A |
| Validation (FR38-43) | `backend/src/test/` | `frontend/e2e/` |

### Complete Project Directory Structure

```
rpgle-transformer-bmad/
│
├── README.md                           # Project overview
├── docker-compose.yml                  # Local dev (PostgreSQL)
├── .gitignore
├── .editorconfig
│
├── source-rpgle/                       # INPUT: Original RPGLE sources
│   ├── programs/                       # RPGLE program sources
│   │   ├── CUST001.rpgle              # Customer maintenance
│   │   ├── CUST001.dspf               # Display file
│   │   └── ...
│   ├── physical-files/                 # DDS Physical Files
│   │   ├── CUSTMAST.pf                # Customer master
│   │   └── ...
│   └── logical-files/                  # DDS Logical Files
│       └── ...
│
├── backend/                            # OUTPUT: Spring Boot 4.0 + Java 21
│   ├── pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/smeup/
│   │   │   │   ├── Application.java
│   │   │   │   ├── config/
│   │   │   │   │   ├── OpenApiConfig.java
│   │   │   │   │   ├── JpaConfig.java
│   │   │   │   │   └── WebConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── CustomerController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── CustomerDTO.java
│   │   │   │   │   └── CustomerCreateRequest.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── Customer.java        # @Table(name="CUSTMAST")
│   │   │   │   ├── repository/
│   │   │   │   │   └── CustomerRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   └── CustomerService.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   └── ResourceNotFoundException.java
│   │   │   │   └── mapper/
│   │   │   │       └── CustomerMapper.java
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-test.yml
│   │   │       └── db/migration/           # Flyway
│   │   │           └── V1__create_custmast.sql
│   │   └── test/
│   │       └── java/com/smeup/
│   │           ├── controller/
│   │           │   └── CustomerControllerTest.java
│   │           ├── service/
│   │           │   └── CustomerServiceTest.java
│   │           └── integration/
│   │               └── CustomerIntegrationTest.java
│   └── Dockerfile
│
├── frontend/                           # OUTPUT: React 19 + TypeScript + Vite
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── tailwind.config.js
│   ├── index.html
│   ├── src/
│   │   ├── main.tsx
│   │   ├── App.tsx
│   │   ├── api/                        # Generated from OpenAPI
│   │   │   ├── client.ts
│   │   │   └── customers.ts
│   │   ├── components/
│   │   │   ├── ui/                     # Shared UI components
│   │   │   │   ├── Button.tsx
│   │   │   │   ├── Input.tsx
│   │   │   │   └── Table.tsx
│   │   │   └── layout/
│   │   │       ├── Header.tsx
│   │   │       └── Sidebar.tsx
│   │   ├── features/
│   │   │   └── customers/              # Feature module
│   │   │       ├── CustomerList.tsx
│   │   │       ├── CustomerForm.tsx
│   │   │       ├── CustomerDetail.tsx
│   │   │       ├── useCustomers.ts     # React Query hook
│   │   │       └── customer.types.ts
│   │   ├── hooks/
│   │   │   └── useApi.ts
│   │   ├── stores/
│   │   │   └── appStore.ts             # Zustand
│   │   ├── types/
│   │   │   └── common.ts
│   │   └── utils/
│   │       ├── formatters.ts
│   │       └── validators.ts
│   ├── src/__tests__/                  # Vitest unit tests
│   │   └── features/
│   │       └── customers/
│   │           └── CustomerList.test.tsx
│   ├── e2e/                            # Playwright E2E tests
│   │   └── customers.spec.ts
│   └── Dockerfile
│
├── docs/                               # Documentation
│   ├── transformations/                # Per-program analysis
│   │   └── CUST001/
│   │       ├── analysis.md             # AI semantic analysis
│   │       ├── business-logic.md       # Business rules extracted
│   │       ├── data-mapping.md         # DDS → JPA mapping
│   │       └── validation-results.md   # Test results
│   ├── api/
│   │   └── openapi.yaml                # Generated OpenAPI spec
│   └── architecture/
│       └── decisions.md                # This document
│
├── _bmad-output/                       # BMAD workflow artifacts
│   ├── planning-artifacts/
│   │   ├── product-brief-*.md
│   │   ├── prd.md
│   │   └── architecture.md
│   └── bmm-workflow-status.yaml
│
├── templates/                          # AI transformation templates
│   ├── prompts/
│   │   ├── semantic-analysis.md
│   │   ├── spring-boot-generation.md
│   │   └── react-generation.md
│   └── examples/
│       ├── entity-example.java
│       ├── controller-example.java
│       └── component-example.tsx
│
└── scripts/                            # Automation scripts
    ├── validate-transformation.sh
    └── run-all-tests.sh
```

### Architectural Boundaries

**API Boundaries:**
- REST API at `/api/v1/*`
- OpenAPI contract in `docs/api/openapi.yaml`
- All controllers in `backend/src/main/java/com/smeup/controller/`

**Data Boundaries:**
- JPA entities map to DDS Physical Files
- SQL operations target original file names (`CUSTMAST`, not `customers`)
- Flyway migrations in `backend/src/main/resources/db/migration/`

**Frontend Boundaries:**
- Feature modules in `frontend/src/features/`
- Shared components in `frontend/src/components/ui/`
- API client generated from OpenAPI in `frontend/src/api/`

### Integration Points

**RPGLE → Backend Flow:**
```
source-rpgle/CUST001.rpgle
    → Claude Code analysis
    → docs/transformations/CUST001/analysis.md
    → backend/src/main/java/.../Customer*.java
    → backend/src/test/java/.../Customer*Test.java
```

**Backend → Frontend Flow:**
```
backend/controller → OpenAPI → frontend/api/client.ts
backend/dto → OpenAPI → frontend/types/
```

**Test Data Flow:**
```
User-provided test data
    → backend/src/test/resources/test-data/
    → Integration tests compare output
    → Validation results in docs/transformations/
```

---

## Architecture Validation Results

### Coherence Validation ✅

**Decision Compatibility:** All technology choices (Spring Boot 4.0, React 19, PostgreSQL 16, Vite 6) are fully compatible and represent current best practices as of December 2025.

**Pattern Consistency:** Naming conventions are consistent:
- SQL targets DDS file names (`CUSTMAST`)
- Java uses readable names (`Customer`)
- React uses PascalCase components
- All patterns align with chosen technologies

**Structure Alignment:** Project structure fully supports all architectural decisions with clear separation of concerns.

### Requirements Coverage ✅

**50 Functional Requirements:** All covered by architectural decisions

**15 Non-Functional Requirements:** All critical NFRs addressed

**Key Coverage:**
- 100% Functional Equivalence → Test data validation pipeline
- Zero RPGLE Knowledge → Readable code with inline DDS comments
- Code Quality → Checkstyle/PMD + ESLint/Prettier

### Implementation Readiness ✅

**AI Agents Can Implement Because:**
1. All naming patterns are explicit with examples
2. Entity mapping pattern clearly shows DDS → JPA translation
3. Project structure is complete with all files/directories
4. Integration flows are documented

### Architecture Completeness Checklist

**✅ Requirements Analysis**
- [x] Project context thoroughly analyzed
- [x] Scale and complexity assessed (Medium-High)
- [x] Technical constraints identified (AI/Claude, DDS input, etc.)
- [x] Cross-cutting concerns mapped

**✅ Architectural Decisions**
- [x] Critical decisions documented with versions
- [x] Technology stack fully specified
- [x] Integration patterns defined (Contract-First, Shared Data Model)
- [x] Performance considerations addressed

**✅ Implementation Patterns**
- [x] Naming conventions established
- [x] Structure patterns defined
- [x] Communication patterns specified (React Query, Zustand)
- [x] Process patterns documented (error handling, loading states)

**✅ Project Structure**
- [x] Complete directory structure defined
- [x] Component boundaries established
- [x] Integration points mapped
- [x] Requirements to structure mapping complete

### Architecture Readiness Assessment

**Overall Status:** ✅ READY FOR IMPLEMENTATION

**Confidence Level:** HIGH

**Key Strengths:**
- Clear DDS → JPA → React transformation pipeline
- Contract-first API design ensures backend/frontend sync
- Comprehensive naming patterns prevent AI agent conflicts
- Full traceability from RPGLE to modern code

**First Implementation Priority:**
1. Initialize Spring Boot project with starter command
2. Initialize React project with Vite
3. Transform first program (CUST001)

---

## Architecture Completion Summary

### Workflow Completion

**Architecture Decision Workflow:** COMPLETED ✅
**Total Steps Completed:** 8
**Date Completed:** 2025-12-30
**Document Location:** _bmad-output/planning-artifacts/architecture.md

### Final Architecture Deliverables

**Complete Architecture Document**
- All architectural decisions documented with specific versions
- Implementation patterns ensuring AI agent consistency
- Complete project structure with all files and directories
- Requirements to architecture mapping
- Validation confirming coherence and completeness

**Implementation Ready Foundation**
- 20+ architectural decisions made
- 15+ implementation patterns defined
- 8 architectural components specified
- 50 FRs + 15 NFRs fully supported

**AI Agent Implementation Guide**
- Technology stack with verified versions (Spring Boot 4.0.1, React 19.2.1, etc.)
- Consistency rules that prevent implementation conflicts
- Project structure with clear boundaries
- Integration patterns and communication standards

### Implementation Handoff

**For AI Agents (Claude Code / GitHub Copilot):**
This architecture document is your complete guide for implementing rpgle-transformer-bmad. Follow all decisions, patterns, and structures exactly as documented.

**First Implementation Priority:**
```bash
# Backend initialization
curl https://start.spring.io/starter.zip \
  -d type=maven-project -d language=java \
  -d bootVersion=4.0.1 -d javaVersion=21 \
  -d groupId=com.smeup -d artifactId=backend \
  -d dependencies=web,data-jpa,postgresql,validation \
  -o backend.zip && unzip backend.zip -d backend

# Frontend initialization
npm create vite@latest frontend -- --template react-ts
```

**Development Sequence:**
1. Initialize project using documented starter commands
2. Set up development environment (Docker Compose for PostgreSQL)
3. Transform first RPGLE program (CUST001) following patterns
4. Build features following established patterns
5. Validate functional equivalence with test data

### Quality Assurance Checklist

**✅ Architecture Coherence**
- [x] All decisions work together without conflicts
- [x] Technology choices are compatible
- [x] Patterns support the architectural decisions
- [x] Structure aligns with all choices

**✅ Requirements Coverage**
- [x] All 50 functional requirements are supported
- [x] All 15 non-functional requirements are addressed
- [x] Cross-cutting concerns are handled
- [x] Integration points are defined

**✅ Implementation Readiness**
- [x] Decisions are specific and actionable
- [x] Patterns prevent agent conflicts
- [x] Structure is complete and unambiguous
- [x] Examples are provided for clarity

---

**Architecture Status:** ✅ READY FOR IMPLEMENTATION

**Next Phase:** Create Epics and Stories using `/bmad:bmm:workflows:create-epics-and-stories`
