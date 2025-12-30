# Story 1.3: Configure Development Environment

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **Docker Compose configured for local PostgreSQL and development environment**,
so that **I can run the full stack locally without external dependencies**.

## Acceptance Criteria

1. **Given** both backend and frontend projects exist
   **When** I run `docker-compose up` at the project root
   **Then** a PostgreSQL 16 container starts and is accessible on port 5432

2. **And** the database data persists across restarts (using a volume)

3. **And** the backend `application.yml` is configured to connect to this local PostgreSQL instance

4. **And** the backend connects successfully to the database on startup without errors

## Tasks / Subtasks

- [x] Task 1: Create Docker Compose configuration (AC: #1, #2)
  - [x] Create `docker-compose.yml` at project root
  - [x] Define PostgreSQL 16 service
  - [x] Configure environment variables (DB name, user, password)
  - [x] Configure port mapping (5432:5432)
  - [x] Configure named volume for persistence
  - [x] Add `.env` file support (optional but recommended) or sane defaults

- [x] Task 2: Configure Backend for Local Development (AC: #3)
  - [x] Convert/Rename `backend/src/main/resources/application.properties` to `application.yml`
  - [x] Configure `spring.datasource` to point to localhost:5432
  - [x] Configure JPA settings (hibernate ddl-auto, dialect)
  - [x] Ensure credentials match Docker Compose settings

- [x] Task 3: Verify Testing Configuration is Unaffected
  - [x] Check `backend/src/test/resources/application.properties` (or yml)
  - [x] Ensure tests still use H2 (do not switch tests to Postgres)
  - [x] Run `mvn test` to confirm regressions are avoided

- [x] Task 4: Validate End-to-End Startup (AC: #4)
  - [x] Run `docker-compose up -d`
  - [x] Start Spring Boot backend (`mvn spring-boot:run`)
  - [x] Verify logs show successful database connection
  - [x] Verify no connection refused errors

## Dev Notes

### Docker Compose Configuration Strategy

**File:** `docker-compose.yml` (Root)

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: rpgle_postgres
    environment:
      POSTGRES_DB: smeup
      POSTGRES_USER: smeup
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U smeup"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

### Spring Boot Configuration (application.yml)

**File:** `backend/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: backend
  datasource:
    url: jdbc:postgresql://localhost:5432/smeup
    username: smeup
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Critical Architecture Compliance

From [Source: _bmad-output/planning-artifacts/architecture.md#Infrastructure-Decisions]:
- **PostgreSQL 16.x** is REQUIRED for production/local dev
- **H2** is REQUIRED for testing (do not change test config)
- **Docker Compose** is the chosen orchestration tool

From [Source: _bmad-output/planning-artifacts/epics.md#Story-1.3]:
- Explicit requirement for `application.yml` (implies migration from .properties)

### Learning from Previous Stories (1-1, 1-2)

- **Lesson:** Verify tests explicitly. Story 1-2 code review failed because tests weren't run.
- **Action:** Task 3 explicitly requires running `mvn test` to ensure the config change (properties -> yml) doesn't break the H2 test setup.
- **Lesson:** Explicit versions.
- **Action:** Use `postgres:16-alpine` to pin version.

### Testing This Story

**Verification Commands:**

```bash
# 1. Start Database
docker-compose up -d

# 2. Check Container Status
docker ps | grep postgres

# 3. Check Persistence (optional verify)
docker inspect -f '{{ .Mounts }}' rpgle_postgres

# 4. Start Backend (ensure it connects)
cd backend
mvn spring-boot:run

# 5. Verify Tests still pass (H2 check)
mvn test
```

### References

- [Source: _bmad-output/planning-artifacts/architecture.md#Infrastructure-Decisions]
- [Source: _bmad-output/planning-artifacts/epics.md#Story-1.3]

## Dev Agent Guardrails

### CRITICAL: Testing Isolation

**MUST enforce:**
- `src/main/resources/application.yml` uses **PostgreSQL**
- `src/test/resources/application.properties` (or yml) uses **H2**
- **NEVER** make tests depend on the Docker container. Tests must align with "Unit/Integration" separation, but typically pure Unit/Service tests use H2 or Mocking. (Architecture mentions "Testcontainers for integration tests with real PostgreSQL" - if we implement that, we should be careful. But for *this* story, we just want to ensure we don't Break existing tests).

### CRITICAL: Configuration Format

**MUST follow:**
- Convert `application.properties` to `application.yml` as per AC.
- Ensure structure is valid YAML (indentation).

### CRITICAL: Versioning

**MUST match:**
- PostgreSQL 16 (matches Architecture).

### Anti-Patterns to Avoid

- **Hardcoding credentials without override ability:** (Ideally use ENV vars, but for "Initialize" story, defaults in yml are acceptable for local dev if matched with docker-compose).
- **Using `latest` tag for Docker images:** Always pin version (`16-alpine`).
- **Ignoring `.gitignore`:** Ensure `postgres_data` or local volume folders are ignored if locally mounted. (Named volumes are better/safer).

## Dev Agent Record

### Agent Model Used

Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References

- Docker startup: `docker-compose up -d` (success after retry)
- Backend startup: `Started BackendApplication in 1.082 seconds`
- DB Connection: `HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection`
- Test Verification: `mvn test` passed using H2 (verified via logs)

### Completion Notes List

- ✅ Created `docker-compose.yml` with PostgreSQL 16
- ✅ Configured `application.yml` for local development
- ✅ Verified backend connects to Docker Postgres
- ✅ Verified tests still use H2 (regression testing passed)
- ✅ Validated end-to-end startup

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created | PM Agent (Claude Opus 4.5) |
| 2025-12-30 | Implementation complete - Docker env configured | Dev Agent (Claude Opus 4.5) |
| 2025-12-30 | Fixed code review findings: Removed redundant Hibernate dialect config | Code Review (Automated) |

### File List

**New Files:**
- docker-compose.yml
- backend/src/main/resources/application.yml

**Deleted Files:**
- backend/src/main/resources/application.properties
