# Story 1.5: Configure Code Quality Tools

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **code quality tools configured per architecture standards**,
so that **generated code meets quality standards automatically**.

## Acceptance Criteria

1. **Given** the backend project
   **When** I run `mvn checkstyle:check`
   **Then** Checkstyle (Google Java Style) runs and validates code quality

2. **Given** the frontend project
   **When** I run `npm run lint`
   **Then** ESLint runs with Prettier integration
   **And** formatting issues are flagged or fixed

3. **Given** the frontend project
   **When** I check TypeScript configuration
   **Then** Strict mode is enabled (already done in 1.2, verify)

## Tasks / Subtasks

- [x] Task 1: Configure Backend Checkstyle
  - [x] Add `maven-checkstyle-plugin` to `backend/pom.xml`
  - [x] Configure `google_checks.xml` as the style
  - [x] Ensure execution in `validate` phase (so it runs before compile)
  - [x] Run `mvn checkstyle:check` to verify

- [x] Task 2: Configure Frontend Prettier
  - [x] Install `prettier`, `eslint-config-prettier` in `frontend`
  - [x] Create `frontend/.prettierrc` with standard rules
  - [x] Add `.prettierignore`

- [x] Task 3: Integrate ESLint and Prettier
  - [x] Update `frontend/eslint.config.js` (or equivalent) to extend Prettier config
  - [x] Update `package.json` script `lint` to include (or separate) check
  - [x] Add `format` script (`prettier --write .`)

- [x] Task 4: Verify Automation
  - [x] Run `mvn clean verify` (Backend) - should include checkstyle
  - [x] Run `npm run lint` (Frontend) - should check style

## Dev Notes

### Backend Checkstyle Config

In `pom.xml` under `<plugins>`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.1</version>
    <configuration>
        <configLocation>google_checks.xml</configLocation>
        <consoleOutput>true</consoleOutput>
        <failsOnError>true</failsOnError>
        <linkXRef>false</linkXRef>
    </configuration>
    <executions>
        <execution>
            <id>validate</id>
            <phase>validate</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Frontend Prettier Config

`.prettierrc`:
```json
{
  "semi": true,
  "singleQuote": true,
  "tabWidth": 2,
  "trailingComma": "es5",
  "printWidth": 100
}
```

### Dev Agent Guardrails

- **CRITICAL:** Do NOT disable TypeScript strict mode.
- **CRITICAL:** Ensure `maven-checkstyle-plugin` does not use an ancient version incompatible with Java 21. Use 3.3.0+.

## Dev Agent Record

### Agent Model Used

Claude Opus 4.5 (claude-opus-4-5-20251101)

### Debug Log References

- Backend Checkstyle: `mvn checkstyle:check` -> BUILD SUCCESS, 0 violations.
- Frontend Lint: `npm run lint` -> Success.
- Frontend Format: `npm run format` -> Formatted files correctly.

### Completion Notes List

- ✅ Configured `maven-checkstyle-plugin` with Google style for backend.
- ✅ Installed and configured Prettier for frontend.
- ✅ Integrated Prettier with ESLint flat config.
- ✅ Added `format` script to frontend.
- ✅ Verified TypeScript strict mode is active.

### Change Log
| Date | Change | By |
|------|--------|-----|
| 2025-12-30 | Story created | PM Agent (Claude Opus 4.5) |
| 2025-12-30 | Implemented Checkstyle, Prettier, and ESLint integration | Dev Agent (Claude Opus 4.5) |
| 2025-12-30 | Verified by Code Review & Minor formatting fixes | Code Review (Automated) |

### File List

- backend/pom.xml
- frontend/package.json
- frontend/.prettierrc
- frontend/.prettierignore
- frontend/eslint.config.js
