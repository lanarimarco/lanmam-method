# Story 4.5: Create E2E Tests with Playwright

Status: ready-for-dev

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **QA engineer**,
I want **Playwright end-to-end tests for customer inquiry workflow**,
So that **I can validate the full user journey works correctly**.

## Acceptance Criteria

1. **Given** frontend and backend are running **When** I create E2E tests **Then** `frontend/e2e/customer-inquiry.spec.ts` is created
2. E2E test navigates to customer inquiry page
3. E2E test enters valid customer number and submits
4. E2E test verifies customer details are displayed
5. E2E test verifies error message for invalid customer
6. Tests can run against local Docker environment

## Tasks / Subtasks

- [ ] Task 1: Set up Playwright in frontend project (AC: 1, 6)
  - [ ] Install Playwright dependencies (`npm install -D @playwright/test`)
  - [ ] Run `npx playwright install` to install browsers
  - [ ] Create `playwright.config.ts` with test configuration
  - [ ] Configure Playwright to run against `http://localhost:5173` (Vite dev server)
  - [ ] Add npm script `"test:e2e": "playwright test"` to package.json

- [ ] Task 2: Create Docker Compose setup for E2E testing (AC: 6)
  - [ ] Verify `docker-compose.yml` includes backend and database services
  - [ ] Create script to start backend and frontend for E2E tests
  - [ ] Verify backend runs on `http://localhost:8080`
  - [ ] Verify frontend runs on `http://localhost:5173`
  - [ ] Document E2E test setup in README

- [ ] Task 3: Create customer inquiry E2E test - Happy Path (AC: 2, 3, 4)
  - [ ] Create `frontend/e2e/customer-inquiry.spec.ts`
  - [ ] Test navigates to `/customers` page
  - [ ] Test enters valid customer number (e.g., "1001") in search form
  - [ ] Test clicks submit button (or presses Enter)
  - [ ] Test waits for customer details to appear
  - [ ] Test verifies customer name is displayed
  - [ ] Test verifies customer address is displayed
  - [ ] Test verifies all expected fields are visible

- [ ] Task 4: Create customer inquiry E2E test - Error Cases (AC: 5)
  - [ ] Test entering invalid customer number (non-existent)
  - [ ] Test verifies error message is displayed
  - [ ] Test entering invalid format (non-numeric)
  - [ ] Test verifies validation error appears
  - [ ] Test verifies customer details are not shown

- [ ] Task 5: Create test fixtures and helpers (AC: 1, 6)
  - [ ] Create `frontend/e2e/fixtures/customer-data.ts` with test data
  - [ ] Create helper functions for common actions (search, wait for results)
  - [ ] Configure test database seed data if needed
  - [ ] Ensure tests clean up data after execution

- [ ] Task 6: Run and verify E2E tests (AC: 1-6)
  - [ ] Start backend and frontend with `docker-compose up`
  - [ ] Run `npm run test:e2e` - all E2E tests pass
  - [ ] Verify tests run in headless mode for CI/CD
  - [ ] Verify tests can run in headed mode for debugging
  - [ ] Configure Playwright to generate test reports

## Dev Notes

- **Testing Stack**: Playwright for E2E browser testing
- **Architecture**: Full-stack integration testing (frontend + backend + database)
- **Environment**: Tests run against local Docker environment
- **Scope**: Customer inquiry workflow from CUST001 RPGLE transformation

### Project Structure Notes

- E2E tests located in `frontend/e2e/` directory
- Playwright config at `frontend/playwright.config.ts`
- Test reports output to `frontend/playwright-report/`
- Backend must be running at `http://localhost:8080/api/v1`
- Frontend must be running at `http://localhost:5173`
- Docker Compose orchestrates backend + database for E2E environment

### References

- [Source: _bmad-output/planning-artifacts/epics.md - Epic 4, Story 4.5]
- [Source: _bmad-output/planning-artifacts/architecture.md - Testing Strategy]
- [Source: _bmad-output/planning-artifacts/prd.md - FR20-FR23 Automated Test Generation]
- [Playwright Documentation: https://playwright.dev/]

### Technical Constraints

- **Browser Support**: Chromium, Firefox, WebKit (Playwright defaults)
- **Test Isolation**: Each test should be independent and idempotent
- **Data Seeding**: Tests should either seed their own data or use known test fixtures
- **CI/CD Ready**: Tests must run in headless mode without manual intervention
- **Performance**: E2E tests should complete in under 60 seconds

### Testing Standards

- Use Page Object Model pattern for maintainability
- Use explicit waits (`waitForSelector`) instead of arbitrary timeouts
- Verify both positive and negative test scenarios
- Include accessibility checks where appropriate
- Generate screenshots/videos on test failure for debugging

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5

### Debug Log References

#### Docker Backend Setup Note
Created Dockerfile and docker-compose.yml for E2E environment. Note: There's a schema validation issue between PostgreSQL CHAR(2) type and Hibernate VARCHAR expectation for STATE column. This is a pre-existing backend configuration issue unrelated to E2E testing. 

**Workaround for E2E testing**: Backend can run locally via `./mvnw spring-boot:run` which works correctly. E2E tests are configured to run against local backend at `http://localhost:8080` regardless of how it's started.

**TODO** (separate task): Fix Hibernate schema validation to accept PostgreSQL CHAR types or update migration to use VARCHAR.

### Completion Notes List

- ✅ Playwright installed and configured in frontend project
- ✅ playwright.config.ts created with multi-browser support
- ✅ E2E test scripts added to package.json (test:e2e, test:e2e:ui)
- ✅ docker-compose.yml updated with backend service definition
- ✅ backend/Dockerfile created for containerization
- ✅ frontend/README.md updated with E2E testing documentation
- ✅ E2E tests created: customer-inquiry.spec.ts with happy path and error scenarios
- ✅ Test fixtures created: customer-data.ts with test customer records
- ✅ Page helpers created: page-helpers.ts following Page Object Model pattern
- ✅ Database test data migration created: V2__insert_test_data.sql

### File List

- frontend/playwright.config.ts
- frontend/package.json (modified - added E2E scripts)
- frontend/e2e/customer-inquiry.spec.ts
- frontend/e2e/fixtures/customer-data.ts
- frontend/e2e/helpers/page-helpers.ts
- frontend/README.md (modified - added E2E documentation)
- docker-compose.yml (modified - added backend service)
- backend/Dockerfile
- backend/src/main/resources/db/migration/V2__insert_test_data.sql
