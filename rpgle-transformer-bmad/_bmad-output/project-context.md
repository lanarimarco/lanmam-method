---
project_name: 'rpgle-transformer-bmad'
user_name: 'Lana'
date: '2025-12-30'
sections_completed: ['technology_stack', 'language_rules', 'framework_rules', 'testing_rules', 'code_quality', 'workflow_rules', 'critical_rules']
status: 'complete'
---

# Project Context for AI Agents

_This file contains critical rules and patterns that AI agents must follow when implementing code in this project. Focus on unobvious details that agents might otherwise miss._

---

## Technology Stack & Versions

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 LTS | Backend language |
| Spring Boot | 4.0.1 | Backend framework |
| Spring Data JPA | 4.0.x | ORM layer |
| PostgreSQL | 16.x | Production database |
| H2 | Latest | Test database |
| Flyway | 10.x | DB migrations |
| React | 19.2.1 | Frontend framework |
| TypeScript | 5.x | Frontend language |
| Vite | 6.x | Build tool |
| React Query | 5.x | Server state |
| Zustand | 5.x | Client state |
| React Hook Form | 7.x | Form handling |
| Zod | 3.x | Schema validation |
| Tailwind CSS | 3.x | Styling |
| JUnit 5 | Latest | Java testing |
| Mockito | Latest | Java mocking |
| Testcontainers | Latest | Integration tests |
| Vitest | 3.x | React testing |
| Playwright | Latest | E2E testing |

---

## Critical Implementation Rules

### Database to Java Entity Mapping (CRITICAL)

```java
// SQL uses DDS Physical File names, Java uses readable names
@Entity
@Table(name = "CUSTMAST")  // DDS Physical File name
public class Customer {     // Readable class name

    @Column(name = "CUSTID") // DDS field name
    private Long customerId; // Readable field name
}
```

**Rule:** Code = Readable, SQL = DDS names (for traceability)

### API Conventions

- REST endpoints: plural, kebab-case (`/customers`, `/order-items`)
- Path params: camelCase (`{customerId}`)
- Response format: `{ data: {...}, meta: {...} }`
- Errors: RFC 7807 Problem Details format

### Java (Spring Boot) Rules

- Package structure: `com.smeup.{module}/controller|service|repository|entity|dto`
- Use `@ControllerAdvice` for global exception handling
- Always include `@Transactional` on service methods with DB writes
- Use constructor injection, never field injection
- Cyclomatic complexity must be 10 or less per method

### React/TypeScript Rules

- Components: PascalCase files (`CustomerList.tsx`)
- Hooks: `use` prefix (`useCustomers.ts`)
- No `any` types allowed - use proper typing
- Use React Query for all API calls
- Feature-based folder structure (`features/customers/`)
- Use Zod for runtime validation

### Testing Rules

- **Unit tests:** JUnit 5 + Mockito (Java), Vitest + RTL (React)
- **Integration tests:** Testcontainers with real PostgreSQL
- **E2E tests:** Playwright for full user flows
- Test files co-located with source in `__tests__/` folders
- 80%+ code coverage required

### Code Quality

- Java: Checkstyle (Google style), PMD standard ruleset
- React: ESLint + Prettier with strict TypeScript
- All linting must pass with zero critical violations
- No warnings allowed in production builds

### RPGLE Traceability (CRITICAL)

Every generated file MUST include inline comments referencing original RPGLE/DDS:

```java
/**
 * Customer entity mapped from DDS Physical File: CUSTMAST
 * Original fields: CUSTID, CUSTNM, CUSTAD1, CUSTAD2
 * Source: source-rpgle/physical-files/CUSTMAST.pf
 */
```

```typescript
/**
 * Customer components generated from RPGLE program: CUST001
 * Display file: CUST001.dspf
 * Source: source-rpgle/programs/CUST001.rpgle
 */
```

---

## Anti-Patterns to Avoid

- **Never** use generic table names like `customers` - always use DDS file names (`CUSTMAST`)
- **Never** skip the OpenAPI contract - it's the single source of truth
- **Never** commit code without passing all linters
- **Never** transform without generating corresponding tests
- **Never** use field injection in Spring - use constructor injection
- **Never** use `any` type in TypeScript - define proper types
- **Never** skip inline RPGLE/DDS reference comments

---

## Project Structure

```
rpgle-transformer-bmad/
├── backend/                    # Spring Boot 4.0 + Java 21
│   └── src/main/java/com/smeup/
│       ├── controller/         # REST controllers
│       ├── service/            # Business logic
│       ├── repository/         # Data access
│       ├── entity/             # JPA entities (@Table = DDS name)
│       └── dto/                # Data transfer objects
├── frontend/                   # React 19 + TypeScript + Vite
│   └── src/
│       ├── features/           # Feature modules
│       ├── components/ui/      # Shared components
│       └── api/                # Generated from OpenAPI
├── source-rpgle/               # Original RPGLE sources (INPUT)
├── docs/transformations/       # Per-program analysis docs
└── templates/                  # AI transformation templates
```

---

## Validation Requirements

- **100% Functional Equivalence:** Generated code must produce identical results to original RPGLE
- **Test Data Validation:** User provides test data, tests must pass with exact match
- **Zero Tolerance:** Any functional deviation is a blocker

---

**Architecture Reference:** `_bmad-output/planning-artifacts/architecture.md`
