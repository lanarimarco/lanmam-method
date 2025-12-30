# Story 1.4: Configure Testing Infrastructure

Status: review

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **testing frameworks configured for both backend and frontend**,
so that **I can run automated tests as part of the development workflow**.

## Acceptance Criteria

1. **Given** the backend project
   **When** I run `mvn test`
   **Then** Unit tests execute successfully using H2 (fast feedback)

2. **Given** the backend project
   **When** I run `mvn verify`
   **Then** Integration tests execute successfully using Testcontainers and Docker PostgreSQL (real environment validation)

3. **Given** the frontend project
   **When** I run `npm test`
   **Then** Vitest tests execute successfully (component logic)

## Tasks / Subtasks

- [x] Task 1: Normalize Backend Test Dependencies
  - [x] Add `spring-boot-starter-test` (replace ad-hoc `-test` deps if needed)
  - [x] Add `org.springframework.boot:spring-boot-testcontainers`
  - [x] Add `org.testcontainers:postgresql`
  - [x] Add `org.testcontainers:junit-jupiter`

- [x] Task 2: Configure Maven Failsafe Plugin
  - [x] Update `pom.xml` to include `maven-failsafe-plugin`
  - [x] Configure it to run `**/*IT.java` files during `verify` phase

- [x] Task 3: Implement Integration Test Infrastructure
  - [x] Create `backend/src/test/java/com/smeup/backend/AbstractIntegrationTest.java`
  - [x] Configure `@Testcontainers` and `@ServiceConnection` (DynamicPropertySource) for Postgres
  - [x] Ensure it uses the Docker image `postgres:16-alpine`

- [x] Task 4: Create Sample Integration Test
  - [x] Create `backend/src/test/java/com/smeup/backend/BackendApplicationIT.java`
  - [x] Extends `AbstractIntegrationTest`
  - [x] Verifies context loads with real DB
  - [x] Run `mvn verify` to validate

- [x] Task 5: Verify Frontend Test Infrastructure
  - [x] Review `frontend/package.json` scripts
  - [x] Add `test:coverage` script (`vitest run --coverage`) if missing
  - [x] Run `npm test` to ensure it passes

## Dev Notes

### Backend Testing Strategy

**Unit Tests (`*Test.java`):**
- Run with `mvn test`
- Use H2 (`jdbc:h2:mem:testdb`)
- Fast, no Docker required (except H2 emulation)

**Integration Tests (`*IT.java`):**
- Run with `mvn verify` (Failsafe plugin)
- Use Testcontainers (`postgres:16-alpine`)
- Slower, requires Docker
- **Configuration Pattern:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
}
```

### Maven Dependency Updates

Review `pom.xml`. Replace suspicious artifacts `spring-boot-starter-data-jpa-test` etc with:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
```

### Dev Agent Guardrails

- **CRITICAL:** Do NOT break the existing `BackendApplicationTests` (Unit test). It must continues to pass with `mvn test`.
- **CRITICAL:** Integration tests MUST be separated named `*IT.java`.
- **CRITICAL:** Use `@ServiceConnection` (Spring Boot 3.1+) for simplified Testcontainers config if supported by Spring Boot 4.0.1 (it should be).

## Dev Agent Record

### Agent Model Used

Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References

- `mvn test`: `Tests run: 1` (Unit, H2) - Verified no regression.
- `mvn verify`: `Tests run: 1` (Integration, Docker) - Verified Testcontainers functionality. `Container postgres:16-alpine started`.
- `npm test`: `Tests 2 passed` (Frontend Vitest).

### Completion Notes List

- ✅ Normalized Backend POM (Added Testcontainers, Failsafe)
- ✅ Implemented `AbstractIntegrationTest` with `@ServiceConnection`
- ✅ Created `BackendApplicationIT` smoke test
- ✅ Confirmed separation of Unit (H2) and Integration (Docker) tests
- ✅ Verified Frontend testing and added coverage script

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created | PM Agent (Claude Opus 4.5) |
| 2025-12-30 | Implemented Testing Infrastructure | Dev Agent (Claude Opus 4.5) |

### File List

**New Files:**
- backend/src/test/java/com/smeup/backend/AbstractIntegrationTest.java
- backend/src/test/java/com/smeup/backend/BackendApplicationIT.java

**Modified Files:**
- backend/pom.xml
- frontend/package.json
