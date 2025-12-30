# Story 1.1: Initialize Spring Boot Backend Project

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **a Spring Boot 4.0.1 project initialized with Java 21 and essential dependencies**,
so that **I have a ready-to-develop backend foundation following project architecture standards**.

## Acceptance Criteria

1. **Given** the project repository exists
   **When** I run the Spring Boot initialization command from architecture.md
   **Then** a `backend/` directory is created with Spring Boot 4.0.1 project structure

2. **And** pom.xml includes dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Validation

3. **And** Java 21 is configured as the compiler version

4. **And** the project compiles successfully with `mvn compile`

## Tasks / Subtasks

- [x] Task 1: Initialize Spring Boot project (AC: #1, #2, #3)
  - [x] Run Spring Initializr command to generate project
  - [x] Verify backend/ directory structure created
  - [x] Verify pom.xml contains correct dependencies
  - [x] Verify Java 21 is set in pom.xml

- [x] Task 2: Verify project compiles (AC: #4)
  - [x] Run `mvn compile` in backend directory
  - [x] Verify compilation succeeds with no errors

- [x] Task 3: Basic project structure setup
  - [x] Create package structure: `com.smeup.backend`
  - [x] Verify Application.java main class exists
  - [x] Verify application.properties/yml exists

## Dev Notes

### Initialization Command (EXACT - from Architecture)

```bash
curl https://start.spring.io/starter.zip \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=4.0.1 \
  -d javaVersion=21 \
  -d groupId=com.smeup \
  -d artifactId=backend \
  -d name=backend \
  -d dependencies=web,data-jpa,postgresql,validation \
  -o backend.zip && unzip backend.zip -d backend && rm backend.zip
```

### Required Dependencies (pom.xml)

| Dependency | Purpose |
|------------|---------|
| spring-boot-starter-web | REST API support |
| spring-boot-starter-data-jpa | Database ORM layer |
| postgresql | PostgreSQL JDBC driver |
| spring-boot-starter-validation | Bean validation (Jakarta) |

### Expected pom.xml Version Configuration

```xml
<properties>
    <java.version>21</java.version>
</properties>

<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.1</version>
</parent>
```

### Expected Project Structure After Initialization

```
backend/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/smeup/backend/
│   │   │   └── BackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/smeup/backend/
│           └── BackendApplicationTests.java
└── mvnw / mvnw.cmd
```

### Project Structure Notes

- Backend will be located at: `rpgle-transformer-bmad/backend/`
- Package base: `com.smeup.backend`
- Main class: `BackendApplication.java`
- This is the foundation for Epic 2 (CUST001 Backend Transformation)

### Critical Architecture Compliance

From [Source: _bmad-output/planning-artifacts/architecture.md#Starter-Template-Evaluation]:

- **Spring Boot 4.0.1** is required (latest stable as of Dec 2025)
- **Java 21 LTS** is required for Spring Boot 4.x
- **Maven** is the build tool (not Gradle)
- Project initialized from official Spring Initializr

From [Source: _bmad-output/project-context.md#Technology-Stack]:

- Java 21 LTS - Backend language
- Spring Boot 4.0.1 - Backend framework
- Spring Data JPA 4.0.x - ORM layer

### Testing This Story

**Verification Commands:**

```bash
# Verify directory exists
ls -la backend/

# Verify pom.xml dependencies
grep -E "spring-boot-starter-(web|data-jpa|validation)|postgresql" backend/pom.xml

# Verify Java version
grep "java.version" backend/pom.xml

# Verify Spring Boot version
grep "spring-boot-starter-parent" -A 2 backend/pom.xml

# Compile project
cd backend && mvn compile
```

### References

- [Source: _bmad-output/planning-artifacts/architecture.md#Starter-Template-Evaluation]
- [Source: _bmad-output/planning-artifacts/architecture.md#Initialization-Commands]
- [Source: _bmad-output/project-context.md#Technology-Stack]
- [Source: _bmad-output/planning-artifacts/epics.md#Epic-1-Story-1.1]

## Dev Agent Record

### Agent Model Used

Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References

- Spring Initializr URL: https://start.spring.io/starter.zip
- Command used: curl with bootVersion=4.0.1, javaVersion=21, dependencies=web,data-jpa,postgresql,validation

### Completion Notes List

- Successfully initialized Spring Boot 4.0.1 project using Spring Initializr
- All 4 acceptance criteria verified and satisfied
- Project compiles successfully with `mvn compile`
- Package structure follows architecture: `com.smeup.backend`
- All required dependencies present in pom.xml

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created with comprehensive context | SM Agent |
| 2025-12-30 | Implemented story - Spring Boot project initialized | Dev Agent (Claude Opus 4.5) |
| 2025-12-30 | Code review: Fixed 5 issues (H2 dep, pom cleanup, test config) | Code Review Agent |

### File List

**New Files Created:**
- backend/.gitattributes
- backend/.gitignore
- backend/.mvn/wrapper/maven-wrapper.properties
- backend/mvnw
- backend/mvnw.cmd
- backend/pom.xml
- backend/src/main/java/com/smeup/backend/BackendApplication.java
- backend/src/main/resources/application.properties
- backend/src/main/resources/static/ (directory)
- backend/src/main/resources/templates/ (directory)
- backend/src/test/java/com/smeup/backend/BackendApplicationTests.java
- backend/src/test/resources/application.properties (added by review)
- backend/src/test/resources/application-test.properties (added by review)

**Files Modified by Review:**
- backend/pom.xml (added H2 dependency, cleaned up placeholders, updated description)

**Files Removed by Review:**
- backend/HELP.md (unnecessary Spring Initializr reference)

## Senior Developer Review (AI)

**Review Date:** 2025-12-30
**Reviewer:** Claude Opus 4.5 (Adversarial Code Review)
**Outcome:** APPROVED (after fixes)

### Issues Found and Fixed

| # | Severity | Issue | Fix Applied |
|---|----------|-------|-------------|
| 1 | CRITICAL | Tests fail - no datasource configured | Added H2 test dependency |
| 2 | CRITICAL | Missing H2 dependency for tests | Added to pom.xml |
| 3 | HIGH | pom.xml has empty placeholder elements | Removed empty tags |
| 4 | HIGH | Generic project description | Updated to project-specific |
| 5 | MEDIUM | No test configuration file | Created application.properties in test/resources |
| 6 | LOW | Unnecessary HELP.md | Removed |

### Verification
- All tests pass: `mvn test` → BUILD SUCCESS
- Project compiles: `mvn compile` → SUCCESS
