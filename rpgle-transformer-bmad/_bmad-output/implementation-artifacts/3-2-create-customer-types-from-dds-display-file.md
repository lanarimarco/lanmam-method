# Story 3.2: Create Customer Types from DDS Display File

Status: done

## Story

As a **developer**,
I want **TypeScript types generated from CUSTDSP.dds display file structure**,
So that **the frontend has type-safe models matching the original display fields**.

## Acceptance Criteria

1. **Given** CUSTDSP.dds exists in source-rpgle/dds/display-files/
   **When** I analyze the display file and generate types
   **Then** `frontend/src/features/customers/customer.types.ts` is created
   **And** `Customer` interface includes all display fields (readable names)
   **And** inline comments reference original DDS display file fields
   **And** types are exported for use in components

## Tasks / Subtasks

- [x] Create features directory structure (AC: #1)
  - [x] Create `frontend/src/features/` directory
  - [x] Create `frontend/src/features/customers/` directory
  - [x] Follow feature-based folder structure from architecture
- [x] Analyze CUSTDSP.dds display file structure (AC: #1, #2)
  - [x] Read source-rpgle/dds/display-files/CUSTDSP.dds
  - [x] Extract fields from PROMPT record format (customer number entry)
  - [x] Extract fields from DETAIL record format (customer detail display)
  - [x] Map DDS field types to TypeScript types
- [x] Create TypeScript types matching display file (AC: #1, #2, #3)
  - [x] Create `frontend/src/features/customers/customer.types.ts`
  - [x] Define `CustomerPromptFormData` interface for PROMPT format fields
  - [x] Define `CustomerDetailDisplay` interface for DETAIL format fields
  - [x] Use readable camelCase names (pcustno → customerNumber, dcustname → customerName)
  - [x] Add DDS field reference comments for each field
  - [x] Mark optional fields appropriately based on DDS definitions
- [x] Add comprehensive type documentation (AC: #3, #4)
  - [x] Add file header comment referencing CUSTDSP.dds source
  - [x] Document field types and DDS mapping
  - [x] Include usage examples in JSDoc comments
  - [x] Export all types for component use

## Dev Notes

### Critical Context from Previous Stories

**Story 3.1 Learnings:**
- ✅ Use `.env.local` for environment configuration (Vite 6 standard)
- ✅ All fields should have `| null` union types for proper null safety (except required fields)
- ✅ No `any` types allowed - use proper type guards
- ✅ Export all types that components will need
- ✅ Follow architecture naming: camelCase for TypeScript, DDS names in comments

**Backend Patterns Already Established:**
- Backend uses `Customer` entity with readable Java field names
- DDS CUSTMAST fields mapped: CUSTID → customerId, CUSTNM → customerName, etc.
- Frontend types should align with backend CustomerDTO structure for API compatibility

### Architecture Requirements

**From architecture.md - Frontend Architecture:**

**Feature-Based Folder Structure:**
```
frontend/src/
├── features/           # Feature modules (NEW)
│   └── customers/      # Customer feature
│       ├── customer.types.ts       # TypeScript types
│       ├── CustomerSearch.tsx      # (Future story 3.3)
│       ├── CustomerDetail.tsx      # (Future story 3.4)
│       └── CustomerInquiry.tsx     # (Future story 3.5)
├── components/ui/      # Shared UI components
└── api/                # API client (Story 3.1)
```

**TypeScript Naming Conventions:**
- Interfaces: PascalCase (`CustomerPromptFormData`, `CustomerDetailDisplay`)
- Properties: camelCase (`customerNumber`, `customerName`)
- Files: camelCase.ts (`customer.types.ts`)

**Type Safety Requirements:**
- No `any` types allowed anywhere
- Explicit field types for all properties
- Nullable fields use `| null` union type
- Optional fields use `?` when appropriate

### DDS Display File Structure Analysis

**Source:** source-rpgle/dds/display-files/CUSTDSP.dds

**PROMPT Record Format (Lines 13-30):**
- Purpose: Customer number entry screen
- Fields:
  - `PCUSTNO` (5Y 0) - Customer Number input field, numeric 5 digits
  - `PMSG` (50A) - Error message display, alphanumeric 50 chars

**DETAIL Record Format (Lines 33-59):**
- Purpose: Customer detail display screen
- Fields:
  - `DCUSTNO` (5Y 0) - Customer Number display, numeric 5 digits
  - `DCUSTNAME` (30A) - Customer Name, alphanumeric 30 chars
  - `DADDR1` (30A) - Address Line 1, alphanumeric 30 chars
  - `DCITY` (20A) - City, alphanumeric 20 chars
  - `DSTATE` (2A) - State code, alphanumeric 2 chars
  - `DZIP` (5Y 0) - Zip code, numeric 5 digits
  - `DPHONE` (12A) - Phone number, alphanumeric 12 chars
  - `DBALANCE` (9Y 2) - Account Balance, numeric 9 digits with 2 decimals

**DDS to TypeScript Type Mapping:**
- `nY 0` (packed numeric, no decimals) → `number`
- `nY m` (packed numeric, m decimals) → `number`
- `nA` (alphanumeric) → `string`
- Input fields (B = Both) → required in form types
- Output fields (O = Output) → all fields in display types
- Error fields with indicators → optional (use `?`)

### API Integration Considerations

**Alignment with Backend (from Story 3.1):**
- Backend API returns `ApiResponse<Customer>` with CustomerDTO structure
- Frontend already has `Customer` type in `src/types/customer.types.ts` (generic API type)
- This story creates **display-specific types** for UI components
- Display types map to DDS fields, API types map to backend DTO

**Type Relationship:**
```
DDS Display File (CUSTDSP.dds)
    ↓
CustomerPromptFormData (user input)
CustomerDetailDisplay (screen display)
    ↓
API Client transforms to/from
    ↓
Customer (API type from Story 3.1)
```

### Field Mapping Table

| DDS Field | DDS Type | TypeScript Name | TypeScript Type | Notes |
|-----------|----------|-----------------|-----------------|-------|
| PCUSTNO | 5Y 0 | customerNumber | number | Input field for search |
| PMSG | 50A | errorMessage | string \| null | Optional error display |
| DCUSTNO | 5Y 0 | customerNumber | number | Display-only |
| DCUSTNAME | 30A | customerName | string \| null | May be null if no data |
| DADDR1 | 30A | addressLine1 | string \| null | May be null |
| DCITY | 20A | city | string \| null | May be null |
| DSTATE | 2A | state | string \| null | May be null |
| DZIP | 5Y 0 | zipCode | number \| null | May be null |
| DPHONE | 12A | phoneNumber | string \| null | May be null |
| DBALANCE | 9Y 2 | accountBalance | number \| null | May be null |

### Implementation Warnings

**CRITICAL - Avoid These Mistakes:**
1. **DO NOT** create types in wrong location - must be `features/customers/customer.types.ts`
2. **DO NOT** use generic names like `FormData` - use descriptive names like `CustomerPromptFormData`
3. **DO NOT** forget DDS field reference comments for traceability
4. **DO NOT** use `any` types - explicit typing required
5. **DO NOT** skip nullable annotations - most display fields can be null

**Pattern to Follow:**
```typescript
// ✅ CORRECT
/**
 * Customer number entry form data from DDS PROMPT format
 * DDS Source: CUSTDSP.dds - Record Format PROMPT
 */
export interface CustomerPromptFormData {
  /**
   * Customer Number for lookup
   * DDS Field: PCUSTNO (5Y 0) - Line 23
   */
  customerNumber: number;

  /**
   * Error message display
   * DDS Field: PMSG (50A) - Line 27
   */
  errorMessage?: string;
}

// ❌ INCORRECT - Missing comments and wrong nullability
export interface FormData {
  customerNumber: any;  // Never use 'any'!
  errorMessage: string;  // Should be optional
}
```

### Testing Requirements

**From architecture.md - Testing Architecture:**
- Create unit tests for type validation if using Zod schemas
- Test file: `frontend/src/features/customers/__tests__/customer.types.test.ts`
- Verify type exports work correctly
- Test type guards if implemented

### Project Structure Notes

**Feature Directory Creation:**
- This is the FIRST story to create the `features/` directory
- Sets the pattern for future feature modules
- Must follow architecture's feature-based structure

**Dependencies:**
- No new npm packages required for basic TypeScript types
- If adding Zod validation (recommended): `npm install zod` (already in package.json)
- Zod provides runtime validation matching TypeScript types

### References

**DDS Source Files:**
- [Source: source-rpgle/dds/display-files/CUSTDSP.dds]
- PROMPT format: Lines 13-30 (customer number entry)
- DETAIL format: Lines 33-59 (customer detail display)

**Architecture Documentation:**
- [Source: _bmad-output/planning-artifacts/architecture.md#frontend-architecture]
- Feature-based folder structure requirements
- TypeScript naming conventions

**Related Stories:**
- Story 3.1 (done): Created generic API types in `src/types/customer.types.ts`
- Story 3.3 (next): Will use `CustomerPromptFormData` for search form
- Story 3.4 (next): Will use `CustomerDetailDisplay` for detail component

**Epic Context:**
- [Source: _bmad-output/planning-artifacts/epics.md#epic-3-cust001-frontend-transformation]
- Story is part of Epic 3: CUST001 Frontend Transformation
- Second story in epic - builds on API client from 3.1

### Previous Story Review Findings

**Key Learnings from Story 3.1 Code Review:**
1. **Type Safety:** All fields properly typed with `| null` for nullable values
2. **Environment Config:** Use `.env.local` (Vite 6 standard)
3. **Export Pattern:** Re-export types from main module for easier imports
4. **No any Types:** Use proper type guards instead of `as any`
5. **Linting:** Ensure ESLint passes with zero errors

**Apply to This Story:**
- All DDS display fields should be `| null` (except required customerNumber in prompt)
- Add proper JSDoc documentation for each type and field
- Export all types for component consumption
- Follow established TypeScript patterns from Story 3.1

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5 (claude-sonnet-4-5-20250929)

### Debug Log References

- All tests passing: 23/23 customer types tests + 6/6 API tests + 2/2 App tests = 31/31 total
- Linting passed with zero errors
- No regressions detected
- Code review completed with 7 HIGH/MEDIUM issues fixed automatically

### Completion Notes List

**Implementation Summary:**
- Created feature-based directory structure (`frontend/src/features/customers/`)
- Analyzed CUSTDSP.dds display file structure (PROMPT and DETAIL formats)
- Defined two TypeScript interfaces: `CustomerPromptFormData` and `CustomerDetailDisplay`
- Mapped all 10 DDS display fields to TypeScript with proper types and null safety
- Added comprehensive JSDoc documentation with DDS field references
- Created type guard functions for runtime validation (with documented limitations)
- Implemented Zod schemas for robust runtime validation
- Created API-to-Display mapper function for integrating with Story 3.1 API types
- Created barrel export file (index.ts) for convenient imports
- Implemented 23 comprehensive unit tests covering all scenarios
- All acceptance criteria satisfied
- All code review findings addressed

**Technical Decisions:**
1. Created `CustomerPromptFormData` for PROMPT format (2 fields: customerNumber, errorMessage)
2. Created `CustomerDetailDisplay` for DETAIL format (8 fields: all customer display data)
3. Used `| null` union types for all display fields except required customerNumber
4. Added type guard functions `isCustomerPromptFormData` and `isCustomerDetailDisplay` (shallow validation)
5. Implemented Zod schemas `CustomerPromptFormDataSchema` and `CustomerDetailDisplaySchema` for robust runtime validation
6. Created `apiCustomerToDisplay()` mapper to convert API `Customer` types (customerId) to display types (customerNumber)
7. Created barrel export file `index.ts` for convenient feature-based imports
8. Followed naming pattern from Story 3.1: camelCase properties, DDS names in comments
9. Documented intentional field mismatch: Display types have 8 fields, API types have 9 (excludes creditLimit, lastOrderDate)
10. Set foundation for future component stories (3.3, 3.4, 3.5)

**Testing:**
- Created comprehensive test suite with 23 test cases (expanded from 13 during code review)
- Tests cover: type structure validation, type guards, null handling, field validation, Zod schema validation, API-to-Display mapper
- Verified DDS field mapping accuracy (PROMPT: 2 fields, DETAIL: 8 fields)
- Added Zod schema validation tests (8 test cases)
- Added API mapper tests (2 test cases)
- Improved field validation tests to be more robust (not fragile to optional undefined fields)
- All tests passing (31/31 total including existing tests)

**Quality Assurance:**
- ESLint passed with zero errors
- TypeScript strict mode compliance
- No `any` types used
- Proper null safety with `| null` union types
- Complete JSDoc documentation with usage examples
- DDS field references for full traceability

### File List

**Created:**
- frontend/src/features/ - Feature modules directory (new)
- frontend/src/features/customers/ - Customer feature directory (new)
- frontend/src/features/customers/customer.types.ts - TypeScript type definitions with Zod schemas and mapper
- frontend/src/features/customers/index.ts - Barrel export file for convenient imports
- frontend/src/features/customers/__tests__/customer.types.test.ts - Unit tests (23 tests)

**Modified:**
- None (no existing files modified)

### Code Review Record

**Review Date:** 2025-12-31
**Reviewer:** Claude Sonnet 4.5 (Adversarial Code Review Agent)
**Issues Found:** 10 total (3 High, 4 Medium, 3 Low)
**Issues Fixed:** 7 (all High and Medium issues)

**High Priority Fixes Applied:**
1. ✅ Staged all untracked files in git (story file + implementation files)
2. ✅ Added `apiCustomerToDisplay()` mapper function to bridge API and Display types
3. ✅ Created barrel export file `index.ts` for feature module

**Medium Priority Fixes Applied:**
4. ✅ Implemented Zod schemas (`CustomerPromptFormDataSchema`, `CustomerDetailDisplaySchema`) for robust runtime validation
5. ✅ Enhanced type guard JSDoc with warnings about shallow validation
6. ✅ Documented field mismatch between API types (9 fields) and Display types (8 fields) in Zod schema comments
7. ✅ Fixed fragile field count tests - now validate explicit fields instead of counting keys

**Low Priority Issues (Not Fixed):**
8. ℹ️ Missing usage examples in README - deferred to future story
9. ℹ️ DDS line number references could be fragile if file reformatted - acceptable tradeoff for traceability
10. ℹ️ Enhanced JSDoc for type guards - partially addressed with WARNING notes

**Test Coverage After Review:**
- Original: 13 test cases
- After fixes: 23 test cases (+10 new tests for Zod schemas and mapper)
- All tests passing: 31/31 total (23 customer types + 6 API + 2 App)
