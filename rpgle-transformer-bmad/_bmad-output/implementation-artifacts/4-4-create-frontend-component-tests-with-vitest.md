# Story 4.4: Create Frontend Component Tests with Vitest

Status: ready-for-dev

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **QA engineer**,
I want **Vitest + React Testing Library tests for customer components**,
so that **I can validate React components render and behave correctly**.

## Acceptance Criteria

1. **Given** customer React components exist
   **When** I create component tests
   **Then** `CustomerSearch.test.tsx` tests form input and validation

2. **And** `CustomerDetail.test.tsx` tests data display rendering

3. **And** `CustomerInquiry.test.tsx` tests full page workflow

4. **And** tests mock API calls using MSW or similar

5. **And** tests verify loading states, error states, and success states

6. **And** code coverage for customer components is >= 80%

## Tasks / Subtasks

- [ ] Task 1: Set up Vitest and React Testing Library if not configured (AC: 1, 2, 3)
  - [ ] Verify Vitest is installed and configured in package.json
  - [ ] Verify @testing-library/react is installed
  - [ ] Verify @testing-library/user-event is installed
  - [ ] Create vitest.config.ts if needed

- [ ] Task 2: Create CustomerSearch component tests (AC: 1, 4, 5)
  - [ ] Create frontend/src/components/CustomerSearch.test.tsx
  - [ ] Test component renders search form correctly
  - [ ] Test form validation (customer number required, numeric)
  - [ ] Test form submission triggers search handler
  - [ ] Mock API calls for search functionality
  - [ ] Test loading state during search
  - [ ] Test error state for API failures
  - [ ] Test success state with search results

- [ ] Task 3: Create CustomerDetail component tests (AC: 2, 4, 5)
  - [ ] Create frontend/src/components/CustomerDetail.test.tsx
  - [ ] Test component renders with customer data
  - [ ] Test all customer fields are displayed correctly
  - [ ] Test component handles missing/null data gracefully
  - [ ] Mock API calls for fetching customer details
  - [ ] Test loading state during data fetch
  - [ ] Test error state for API failures

- [ ] Task 4: Create CustomerInquiry page tests (AC: 3, 4, 5)
  - [ ] Create frontend/src/pages/CustomerInquiry.test.tsx
  - [ ] Test full page workflow: search → select → display details
  - [ ] Test integration between search and detail components
  - [ ] Mock all API interactions
  - [ ] Test error handling and user feedback
  - [ ] Test loading states across page workflow

- [ ] Task 5: Set up MSW for API mocking (AC: 4)
  - [ ] Install MSW (Mock Service Worker) if not present
  - [ ] Create frontend/src/mocks/handlers.ts for customer API mocks
  - [ ] Create frontend/src/mocks/server.ts for test setup
  - [ ] Configure MSW to intercept customer API calls in tests

- [ ] Task 6: Run tests and verify coverage (AC: 6)
  - [ ] Run `npm test` - all component tests pass
  - [ ] Run `npm run test:coverage` to check coverage
  - [ ] Verify customer components have >= 80% coverage
  - [ ] Add additional tests if coverage is below threshold

## Dev Notes

- Use Vitest as the test runner (faster than Jest for Vite projects)
- Use React Testing Library for component testing
- Use MSW (Mock Service Worker) for API mocking
- Follow testing best practices: test behavior, not implementation
- Focus on user interactions and visible outcomes

### Project Structure Notes

- Tests should be co-located with components: `ComponentName.test.tsx`
- MSW mocks should be in `frontend/src/mocks/`
- Test coverage reports in `frontend/coverage/`
- Align with existing frontend structure

### References

- [Source: frontend/src/components/CustomerSearch.tsx]
- [Source: frontend/src/components/CustomerDetail.tsx]
- [Source: frontend/src/pages/CustomerInquiry.tsx]
- [Source: frontend/src/services/customerApi.ts]
- [Vitest Documentation](https://vitest.dev/)
- [React Testing Library Documentation](https://testing-library.com/react)
- [MSW Documentation](https://mswjs.io/)

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5

### Debug Log References

### Completion Notes List

### File List
