# Story 3.3: Create Customer Search Form Component

Status: review

## Story

As a **developer**,
I want **a React component for customer number input (PROMPT format)**,
So that **users can enter a customer number to search, matching the green-screen PROMPT**.

## Acceptance Criteria

1. **Given** the API client and types exist
   **When** I create the search form component
   **Then** `frontend/src/features/customers/CustomerSearch.tsx` is created
   **And** form includes customer number input field
   **And** form validates customer number format before submission
   **And** form submits on Enter key (matching green-screen behavior)
   **And** inline comments reference original CUSTDSP PROMPT format
   **And** component uses React Hook Form for form handling

## Tasks / Subtasks

- [x] Create CustomerSearch component file (AC: #1)
  - [x] Create `frontend/src/features/customers/CustomerSearch.tsx`
  - [x] Add file header with DDS CUSTDSP PROMPT reference
  - [x] Set up component structure with TypeScript

- [x] Implement form with React Hook Form (AC: #1, #6)
  - [x] Import and set up `useForm` from react-hook-form
  - [x] Use `CustomerPromptFormDataSchema` for validation (from Story 3.2)
  - [x] Integrate Zod schema with React Hook Form using `@hookform/resolvers/zod`
  - [x] Define form with `customerNumber` input field

- [x] Add customer number input field (AC: #2, #5)
  - [x] Create input for customer number (numeric, 5 digits max)
  - [x] Add DDS PROMPT reference comment: `PCUSTNO (5Y 0) - Line 23`
  - [x] Display field label matching DDS: "Customer Number:"
  - [x] Apply appropriate input type for numeric entry

- [x] Implement form validation (AC: #3)
  - [x] Use Zod schema validation (positive integer)
  - [x] Display validation errors from React Hook Form
  - [x] Show error messages below input field
  - [x] Prevent submission if validation fails

- [x] Add submit behavior (AC: #4, #5)
  - [x] Handle form submission on Enter key (native form behavior)
  - [x] Clear form after successful submission
  - [x] Reference DDS behavior in comments (PROMPT format, F3=Exit)
  - [x] Emit customer number to parent component via callback

- [x] Add error message display (AC: #2, #5)
  - [x] Add optional error message display area
  - [x] Style error message in red (matching DDS PMSG field, indicator 90)
  - [x] Reference DDS PROMPT PMSG field: `PMSG (50A) - Line 27`
  - [x] Handle error message prop from parent component

- [x] Create comprehensive unit tests
  - [x] Test component renders with input field
  - [x] Test validation prevents invalid submissions
  - [x] Test Enter key triggers submission
  - [x] Test error message display
  - [x] Test form clears after submission
  - [x] Achieve 80%+ code coverage

- [x] Update barrel export
  - [x] Export `CustomerSearch` component from `index.ts`
  - [x] Maintain consistent export pattern with types

## Dev Notes

### Critical Context from Previous Stories

**Story 3.2 Learnings (JUST COMPLETED - Code Review Findings):**
- ✅ Use Zod schemas for runtime validation (NOT just TypeScript types)
- ✅ `CustomerPromptFormDataSchema` already exists in `customer.types.ts`
- ✅ Import from barrel export: `import { CustomerPromptFormDataSchema, CustomerPromptFormData } from '../customers';`
- ✅ Zod schema already validates: customerNumber (positive integer), errorMessage (optional string, max 50 chars)
- ✅ API mapper function `apiCustomerToDisplay()` exists for converting API responses
- ✅ All types have `| null` union types for proper null safety
- ✅ Code review emphasized production-readiness: proper validation, comprehensive tests

**Story 3.1 Learnings:**
- ✅ API client `getCustomerById()` exists in `src/api/customers.ts`
- ✅ Use `.env.local` for environment configuration (Vite 6 standard)
- ✅ API returns `ApiResponse<Customer>` with structure: `{ data: Customer, meta: {} }`
- ✅ Error handling returns RFC 7807 Problem Details format
- ✅ No `any` types allowed - use proper TypeScript typing
- ✅ All tests must pass with zero linting errors

**Key Integration Points:**
- Form data type: `CustomerPromptFormData` (from Story 3.2)
- Validation schema: `CustomerPromptFormDataSchema` (Zod schema from Story 3.2)
- API call: `getCustomerById(customerId)` (from Story 3.1)
- Error handling: `ApiError` type (from Story 3.1)

### Architecture Requirements

**From architecture.md - Frontend Architecture:**

**React Hook Form + Zod Integration Pattern:**
```typescript
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { CustomerPromptFormDataSchema, type CustomerPromptFormData } from './customer.types';

const { register, handleSubmit, formState: { errors } } = useForm<CustomerPromptFormData>({
  resolver: zodResolver(CustomerPromptFormDataSchema),
});
```

**Component File Naming:**
- Components: PascalCase files (`CustomerSearch.tsx`)
- Feature-based location: `frontend/src/features/customers/`
- Co-located tests: `frontend/src/features/customers/__tests__/CustomerSearch.test.tsx`

**Testing Requirements:**
- Use Vitest + React Testing Library
- Test files co-located with source in `__tests__/` folders
- 80%+ code coverage required
- Test user interactions, validation, and error states

**Code Quality Standards:**
- ESLint + Prettier with strict TypeScript
- No `any` types allowed anywhere
- All linting must pass with zero critical violations
- No warnings allowed in production builds

### DDS Display File Structure Analysis

**Source:** source-rpgle/dds/display-files/CUSTDSP.dds

**PROMPT Record Format (Lines 13-30):**
- Purpose: Customer number entry screen (green-screen input form)
- Screen layout:
  - Line 1, Col 2: "CUST001" (program ID, highlighted)
  - Line 1, Col 30: "Customer Inquiry" (screen title, highlighted)
  - Line 1, Col 70: TIME display
  - Line 2, Col 70: DATE display
  - Line 4, Col 2: "Customer Number:" label
  - Line 4, Col 20: PCUSTNO input field (5Y 0, Both input/output)
  - Line 5, Col 2: "Error:" label (indicator 90, red, highlighted)
  - Line 5, Col 9: PMSG error message (50A, Output only, red)
  - Line 23, Col 2: "F3=Exit" (blue, function key help)

**Field Mapping:**
| DDS Field | Type | Purpose | TypeScript Equivalent |
|-----------|------|---------|----------------------|
| PCUSTNO | 5Y 0 (numeric, 5 digits) | Customer number input | number |
| PMSG | 50A (alphanumeric, 50 chars) | Error message display | string \| undefined |

**Green-Screen Behavior to Match:**
1. User enters customer number in PCUSTNO field
2. Press Enter key to submit (trigger customer inquiry)
3. If error: Display PMSG in red below input (indicator 90)
4. F3 key exits program (React: handled by parent or navigation)
5. Simple, focused interaction - one input, immediate feedback

### Component Design Patterns

**React Hook Form Pattern (CRITICAL):**
```typescript
// CORRECT PATTERN - Use Zod resolver
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

const {
  register,
  handleSubmit,
  formState: { errors },
  reset
} = useForm<CustomerPromptFormData>({
  resolver: zodResolver(CustomerPromptFormDataSchema),
});
```

**Form Submission Pattern:**
```typescript
const onSubmit = (data: CustomerPromptFormData) => {
  // Call parent callback with validated customer number
  onSearch(data.customerNumber);
  reset(); // Clear form after submission
};
```

**Component Props Interface:**
```typescript
interface CustomerSearchProps {
  /**
   * Callback when user submits valid customer number
   * @param customerNumber - Validated customer number to search
   */
  onSearch: (customerNumber: number) => void;

  /**
   * Optional error message to display (from API call failure)
   * Maps to DDS PMSG field (50A) - Line 27
   */
  errorMessage?: string;

  /**
   * Optional loading state during search
   */
  isLoading?: boolean;
}
```

### Implementation Warnings

**CRITICAL - Avoid These Mistakes:**
1. **DO NOT** use unvalidated form inputs - MUST use Zod schema
2. **DO NOT** forget DDS field reference comments for traceability
3. **DO NOT** skip unit tests - comprehensive testing required
4. **DO NOT** use `any` types - TypeScript strict mode enforced
5. **DO NOT** forget to clear form after successful submission
6. **DO NOT** create custom validation - use `CustomerPromptFormDataSchema` from Story 3.2
7. **DO NOT** forget to install `@hookform/resolvers` if not already in package.json

**Dependencies Check:**
- ✅ `react-hook-form` v7.69.0 (already in package.json)
- ✅ `zod` v3.25.76 (already in package.json)
- ⚠️ `@hookform/resolvers` - CHECK if installed, add if missing

**Pattern to Follow:**
```typescript
/**
 * Customer search form component from DDS PROMPT format.
 *
 * Maps to CUSTDSP.dds - Record Format PROMPT (Lines 13-30)
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * RPGLE Program: CUST001 - Customer Inquiry
 *
 * This component recreates the green-screen customer number entry.
 * User enters customer number, submits on Enter, displays errors if any.
 */
export function CustomerSearch({ onSearch, errorMessage, isLoading }: CustomerSearchProps) {
  // React Hook Form setup with Zod validation
  const { register, handleSubmit, formState: { errors }, reset } = useForm<CustomerPromptFormData>({
    resolver: zodResolver(CustomerPromptFormDataSchema),
  });

  const onSubmit = (data: CustomerPromptFormData) => {
    onSearch(data.customerNumber);
    reset(); // Clear form like green-screen does
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      {/* Customer Number Input - DDS PCUSTNO field */}
      {/* Error Message Display - DDS PMSG field */}
      {/* Submit button or Enter key behavior */}
    </form>
  );
}
```

### Testing Requirements

**From architecture.md - Testing Architecture:**

**Unit Test Requirements:**
- Framework: Vitest + React Testing Library
- Location: `frontend/src/features/customers/__tests__/CustomerSearch.test.tsx`
- Coverage: 80%+ required

**Test Scenarios:**
1. **Component Rendering:**
   - Renders input field with correct label
   - Renders without error message initially
   - Renders with error message when provided

2. **Form Validation:**
   - Prevents submission with empty input
   - Prevents submission with negative number
   - Prevents submission with non-integer
   - Allows submission with valid positive integer

3. **User Interactions:**
   - Typing in input updates form state
   - Enter key triggers form submission
   - Form clears after successful submission
   - Calls onSearch callback with validated number

4. **Error Handling:**
   - Displays validation errors from Zod schema
   - Displays API error message from props
   - Error message styled in red (matching DDS)

5. **Loading State:**
   - Disables input during loading
   - Shows loading indicator if provided

**Example Test Structure:**
```typescript
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CustomerSearch } from '../CustomerSearch';

describe('CustomerSearch', () => {
  it('should render input field with label', () => {
    // Test implementation
  });

  it('should validate customer number is positive integer', async () => {
    // Test Zod schema validation
  });

  it('should submit on Enter key press', async () => {
    // Test form submission
  });

  it('should display error message in red', () => {
    // Test error display matching DDS PMSG
  });
});
```

### Project Structure Notes

**Feature-Based Folder Structure (ESTABLISHED in Story 3.2):**
```
frontend/src/features/customers/
├── customer.types.ts          # TypeScript types (Story 3.2) ✅
├── index.ts                    # Barrel export (Story 3.2) ✅
├── CustomerSearch.tsx          # THIS STORY - customer number input
├── CustomerDetail.tsx          # Story 3.4 (next)
├── CustomerInquiry.tsx         # Story 3.5 (later)
└── __tests__/
    ├── customer.types.test.ts  # Type tests (Story 3.2) ✅
    └── CustomerSearch.test.tsx # THIS STORY - component tests
```

**File Organization:**
- Component files: PascalCase (`CustomerSearch.tsx`)
- Type files: camelCase (`customer.types.ts`)
- Test files: Match component name + `.test.tsx` suffix
- All customer feature files in same directory for cohesion

**Alignment with Unified Project Structure:**
- ✅ Follows architecture.md feature-based folder structure
- ✅ Consistent with Story 3.1 (API client in `src/api/`)
- ✅ Consistent with Story 3.2 (types in `src/features/customers/`)
- ✅ Co-located tests match architecture testing standards

### References

**DDS Source Files:**
- [Source: source-rpgle/dds/display-files/CUSTDSP.dds]
- PROMPT format: Lines 13-30 (customer number entry screen)
- PCUSTNO field: Line 23 (5Y 0, Both input/output)
- PMSG field: Line 27 (50A, Output, conditional indicator 90)

**Architecture Documentation:**
- [Source: _bmad-output/planning-artifacts/architecture.md#frontend-architecture]
- React Hook Form + Zod integration pattern
- Feature-based folder structure requirements
- Testing standards (Vitest + RTL, 80%+ coverage)

**Project Context:**
- [Source: _bmad-output/project-context.md]
- Technology stack versions (React 19.2.1, React Hook Form 7.x, Zod 3.x)
- React/TypeScript rules (no `any` types, PascalCase components)
- Testing rules (co-located tests, 80%+ coverage)

**Related Stories:**
- Story 3.1 (done): Created API client with `getCustomerById()` function
- Story 3.2 (done): Created `CustomerPromptFormData` type and `CustomerPromptFormDataSchema` Zod schema
- Story 3.4 (next): Will use this component to trigger customer detail display
- Story 3.5 (later): Will compose search + detail into full inquiry page

**Epic Context:**
- [Source: _bmad-output/planning-artifacts/epics.md#epic-3-cust001-frontend-transformation]
- Story is part of Epic 3: CUST001 Frontend Transformation
- Third story in epic - builds on API client (3.1) and types (3.2)
- Recreates green-screen PROMPT format as React component

### Previous Story Review Findings

**Key Learnings from Story 3.2 Code Review:**
1. **Zod Schemas Are Essential:** Type guards are shallow - use Zod for runtime validation
2. **Production-Ready Code:** All HIGH/MEDIUM issues must be addressed before marking done
3. **Comprehensive Testing:** Went from 13 → 23 tests, adding validation and mapper tests
4. **API Integration:** Created `apiCustomerToDisplay()` mapper to bridge API and Display types
5. **Barrel Exports:** Created `index.ts` for convenient imports
6. **Documentation:** Enhanced JSDoc with warnings about type guard limitations
7. **Field Mismatches:** Documented intentional differences between API types and Display types

**Apply to This Story:**
- Use `CustomerPromptFormDataSchema` Zod schema (not type guards)
- Write comprehensive tests covering all user interactions
- Document DDS field references in JSDoc comments
- Export component from `index.ts` barrel file
- Ensure ESLint passes with zero errors
- Achieve 80%+ code coverage

### Latest Technical Information

**React Hook Form v7.69.0 (Current Stable):**
- Zod integration via `@hookform/resolvers/zod` package
- `useForm` hook with `resolver` option for schema validation
- `register` function for input binding
- `handleSubmit` for form submission with validation
- `formState.errors` for validation error access
- `reset()` method to clear form after submission

**Zod v3.25.76 (Current Stable):**
- `z.number().int().positive()` for customer number validation
- `z.string().max(50).optional()` for error message
- `safeParse()` method for runtime validation
- Schema already defined in `CustomerPromptFormDataSchema`

**React 19.2.1 + TypeScript 5.x:**
- Strict type checking enforced
- No `any` types allowed
- Component props must be fully typed
- Event handlers must have proper types

**Testing Stack (Vitest 3.x + RTL):**
- `@testing-library/react` v16.3.1 for component testing
- `@testing-library/user-event` v14.6.1 for user interaction simulation
- Vitest v3.2.4 for test runner
- Coverage reporting built-in

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5 (claude-sonnet-4-5-20250929)

### Debug Log References

- All tests passing: 26/26 CustomerSearch tests + 23/23 customer types tests + 6/6 API tests + 2/2 App tests = 57/57 total
- Linting passed with zero errors
- No regressions detected
- Test coverage: 100% on new component

### Completion Notes List

**Implementation Summary:**
- Created CustomerSearch component implementing DDS CUSTDSP PROMPT format
- Integrated React Hook Form v7.69.0 with Zod validation using `@hookform/resolvers`
- Installed missing dependency: `@hookform/resolvers` package
- All 26 comprehensive unit tests pass covering rendering, validation, user interactions, error handling, loading states, and accessibility
- Component uses existing `CustomerPromptFormDataSchema` from Story 3.2
- Implemented green-screen behavior: Enter key submission, form reset after search, error message display
- Updated barrel export to include new component

**Technical Implementation:**
1. Created `CustomerSearch.tsx` with full DDS PROMPT traceability comments
2. Used `useForm` hook with `zodResolver` for schema validation
3. Customer number input field with type="number" and autofocus
4. Zod schema validation prevents invalid submissions (negative, zero, empty)
5. Error message display styled in red (matching DDS PMSG field, indicator 90)
6. Loading state disables input and button, changes button text to "Searching..."
7. Form clears after successful submission (matching green-screen behavior)
8. Props interface: onSearch callback, errorMessage, isLoading
9. Tailwind CSS styling for consistent UI appearance

**Testing:**
- Created 26 comprehensive test cases covering 6 test suites:
  - Component Rendering (6 tests): labels, title, error display, button, help text
  - Form Validation (5 tests): empty, negative, zero, valid positive, Zod error messages
  - User Interactions (4 tests): typing, Enter key, callback, form reset
  - Error Handling (4 tests): API error display, error label, hiding errors, error updates
  - Loading State (5 tests): disabled input, disabled button, button text, enabled states
  - Accessibility (2 tests): label association, autofocus
- All tests use Vitest + React Testing Library + userEvent
- Fixed 3 initially failing tests (label query method, Zod error message text, autofocus attribute)
- Fixed 2 linting errors (unused import beforeEach, unused variable)

**Quality Assurance:**
- ESLint passed with zero errors
- TypeScript strict mode compliance
- No `any` types used
- Proper null safety with optional props
- Complete JSDoc documentation with usage examples
- DDS field references for full traceability (PCUSTNO Line 23, PMSG Line 27)
- Follows architecture patterns: React Hook Form + Zod, feature-based structure
- Component props fully typed with CustomerSearchProps interface

### File List

**Created:**
- `frontend/src/features/customers/CustomerSearch.tsx` - Main component (143 lines)
- `frontend/src/features/customers/__tests__/CustomerSearch.test.tsx` - Unit tests (26 tests, 315 lines)

**Modified:**
- `frontend/src/features/customers/index.ts` - Added CustomerSearch export to barrel file
- `frontend/package.json` - Added @hookform/resolvers dependency (installed via npm)
