# Story 5.3: Document DDS to Modern Stack Mapping

Status: done

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **documentation showing DDS file to JPA/React mapping**,
So that **I can understand how legacy data structures map to modern code**.

## Acceptance Criteria

1. **Given** transformation is complete **When** I create the data mapping document **Then** `docs/transformations/CUST001/data-mapping.md` is created
2. CUSTMAST.dds fields are mapped to Customer entity fields
3. CUSTDSP.dds fields are mapped to React component props
4. Table shows: DDS Field -> SQL Column -> Java Field -> TypeScript Field
5. Any data type transformations are explained

## Tasks / Subtasks

- [x] Task 1: Set up data mapping document (AC: 1)
  - [x] Create `data-mapping.md` in `docs/transformations/CUST001/`
  - [x] Set up document template with sections
  - [x] Create table of contents

- [x] Task 2: Map CUSTMAST.dds to database and JPA (AC: 2, 4, 5)
  - [x] Analyze CUSTMAST.dds physical file definition
  - [x] Document each field with: DDS name, type, length, description
  - [x] Map to SQL column in custmast table
  - [x] Map to JPA entity field in Customer.java
  - [x] Document data type transformations (RPGLE -> SQL -> Java)
  - [x] Create comprehensive mapping table
  - [x] Explain any type conversions or constraints

- [x] Task 3: Map CUSTDSP.dds to React components (AC: 3, 4, 5)
  - [x] Analyze CUSTDSP.dds display file definition
  - [x] Document each display field with: DDS name, usage, attributes
  - [x] Map to React component props in TypeScript
  - [x] Document field usage (input/output/both)
  - [x] Create DDS -> TypeScript type mapping table
  - [x] Show how screen fields map to CustomerDetail component props
  - [x] Show how search parameters map to CustomerSearch component

- [x] Task 4: Create full-stack data flow diagram (AC: 4, 5)
  - [x] Create comprehensive table showing end-to-end mapping
  - [x] Columns: DDS Field | SQL Column | Java Field | TypeScript Type | Notes
  - [x] Include all fields from CUSTMAST.dds
  - [x] Include all fields from CUSTDSP.dds
  - [x] Highlight any fields that don't have 1:1 mapping
  - [x] Document any calculated or derived fields

- [x] Task 5: Document data type transformation rules (AC: 5)
  - [x] Create data type conversion reference section
  - [x] Map RPGLE numeric types (ZONED, PACKED) -> SQL -> Java
  - [x] Map RPGLE character types -> SQL -> Java -> TypeScript
  - [x] Map RPGLE date/timestamp -> SQL -> Java -> TypeScript
  - [x] Explain any precision or format changes
  - [x] Document validation rules that affect data types
  - [x] Include examples for complex transformations

- [x] Task 6: Add field-level notes and gotchas (AC: 2, 3, 4, 5)
  - [x] Document any fields with special handling
  - [x] Note any fields added in modern stack (not in DDS)
  - [x] Note any DDS fields not implemented in modern stack
  - [x] Document default values and null handling differences
  - [x] Include performance considerations (indexes, etc.)
  - [x] Cross-reference business logic mapping for validation rules

## Dev Notes

- **Documentation Focus**: Complete data structure mapping from legacy to modern stack
- **Target Audience**: Developers need to understand data flow from DDS through all layers
- **Format**: Tables with clear column mappings and explanatory notes
- **Validation**: Supports FR24-FR31 (transformation documentation requirements)

### Project Structure Notes

- Document builds on `analysis.md` and `business-logic-mapping.md` from Stories 5.1 and 5.2
- Should reference specific DDS files and modern code files
- Use tables for structured mappings
- Include code snippets to show actual field definitions
- Focus on "how data flows through the stack"

### References

- [Source: _bmad-output/planning-artifacts/epics.md - Epic 5, Story 5.3]
- [Source: _bmad-output/planning-artifacts/prd.md - FR24-FR31]
- [Source: source-rpgle/dds/CUSTMAST.dds - Physical file definition]
- [Source: source-rpgle/dds/CUSTDSP.dds - Display file definition]
- [Source: backend/src/main/resources/db/migration/V1__create_custmast_table.sql - Database schema]
- [Source: backend/src/main/java/com/smeup/backend/entity/Customer.java - JPA entity]
- [Source: backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java - Data transfer object]
- [Source: frontend/src/types/customer.ts - TypeScript types]
- [Source: frontend/src/components/CustomerDetail.tsx - React component props]
- [Source: frontend/src/components/CustomerSearch.tsx - React search component]
- [Source: docs/transformations/CUST001/analysis.md - Program overview]
- [Source: docs/transformations/CUST001/business-logic-mapping.md - Business logic context]

### Technical Constraints

- **Completeness**: All DDS fields must be documented
- **Accuracy**: Mappings must match actual implementation
- **Clarity**: Show full data flow from DDS through all technology layers
- **Traceability**: Clear references to source definitions
- **Educational**: Help developers with zero RPGLE knowledge understand DDS concepts

### Documentation Standards

- Use Markdown tables for structured field mappings
- Include code blocks showing actual DDS definitions
- Include code blocks showing JPA entity annotations
- Include code blocks showing TypeScript type definitions
- Use clear section headers for different file types
- Include "DDS Concepts" explanations for RPGLE-naive developers
- Cross-reference database migrations and schema
- Link to relevant source files

### Data Mapping Categories to Cover

1. **Physical File (CUSTMAST.dds)**
   - Field definitions from DDS
   - SQL table schema mapping
   - JPA entity field mapping
   - Data type transformations
   - Constraints and indexes

2. **Display File (CUSTDSP.dds)**
   - Input/output field definitions
   - React component prop mapping
   - TypeScript type definitions
   - Field usage patterns
   - UI element mapping

3. **Data Type Conversions**
   - RPGLE numeric -> SQL -> Java -> TypeScript
   - RPGLE character -> SQL -> Java -> TypeScript
   - Date/timestamp handling
   - Null handling across layers
   - Precision and format changes

4. **Full-Stack Flow**
   - End-to-end data flow for each field
   - API request/response mapping
   - State management in React
   - Database constraints propagation
   - Validation across layers

5. **Special Cases**
   - Fields not in DDS (added in modern stack)
   - DDS fields not implemented
   - Calculated or derived fields
   - Fields with special transformations
   - Backward compatibility considerations

### Key Learning Objectives

- Understand DDS physical file → SQL schema mapping
- Understand DDS display file → React UI mapping
- See how data types evolve through the stack
- Learn how legacy constraints map to modern validation
- Trace any field from DDS source to UI display

## Dev Agent Record

### Implementation Plan

**Approach:** Create comprehensive data mapping documentation showing field-by-field transformation from DDS through all technology layers.

**Strategy:**
1. Load and analyze DDS source files (CUSTMAST.dds, CUSTDSP.dds)
2. Reference actual implementation files (SQL schema, JPA entity, TypeScript types)
3. Create detailed mapping tables for each transformation layer
4. Document DDS concepts for developers with zero RPGLE knowledge
5. Show complete data flow from AS/400 to React UI

**Files Referenced:**
- `source-rpgle/dds/physical-files/CUSTMAST.dds` - Physical file definition
- `source-rpgle/dds/display-files/CUSTDSP.dds` - Display file definition
- `backend/src/main/resources/db/migration/V1__create_custmast.sql` - SQL schema
- `backend/src/main/java/com/smeup/backend/entity/Customer.java` - JPA entity
- `backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java` - Data transfer object
- `frontend/src/features/customers/customer.types.ts` - TypeScript types
- `frontend/src/features/customers/CustomerDetail.tsx` - React component
- `frontend/src/features/customers/CustomerSearch.tsx` - React search component

### Completion Notes

✅ **All Tasks Completed:**

**Task 1:** Created data-mapping.md with complete structure:
- Document created at `docs/transformations/CUST001/data-mapping.md`
- Comprehensive table of contents with 7 major sections
- Professional formatting with code examples and tables

**Task 2:** Documented CUSTMAST.dds → Database → JPA mapping:
- Analyzed all 10 fields from CUSTMAST physical file
- Created field-by-field mapping table (DDS → SQL → Java → TypeScript)
- Documented SQL schema with DDS field name preservation
- Explained JPA entity annotations and validation constraints
- Documented packed decimal transformations (5P 0, 9P 2)

**Task 3:** Documented CUSTDSP.dds → React components mapping:
- Analyzed PROMPT and DETAIL record formats
- Mapped display fields to React component props
- Created DDS → TypeScript interface mappings
- Documented field usage patterns (Input B vs Output O)
- Showed CustomerSearch and CustomerDetail prop mappings
- Explained display attributes (EDTCDE, COLOR, DSPATR)

**Task 4:** Created full-stack data flow table:
- End-to-end mapping: DDS → SQL → Java → DTO → JSON → TypeScript
- Documented API request/response flow with examples
- Included JSON response example with actual field values
- Highlighted data flow through all architectural layers
- No fields with non-1:1 mapping (full parity maintained)

**Task 5:** Documented comprehensive data type transformation rules:
- RPGLE Packed Decimal → SQL → Java → TypeScript transformations
- Separate sections for integer types (5P 0, 8P 0) and decimal types (9P 2)
- Character type transformations (30A, 2A, 12A)
- Date handling: YYYYMMDD integer format with UI formatting examples
- Null handling differences across all layers
- Precision and format changes (BigDecimal, CHAR vs VARCHAR)
- Included transformation chain examples for clarity

**Task 6:** Added field-level notes and special cases:
- Customer ID: No auto-generation, preserves AS/400 numbers
- State Code: 2-character uppercase validation with regex
- Account Balance: BigDecimal precision for currency
- Last Order Date: YYYYMMDD integer format explanation
- Default values and null handling table
- Performance considerations: indexed fields
- Cross-references to business-logic-mapping.md and analysis.md
- Backward compatibility notes

**Key Achievements:**
- Complete DDS concept explanations for non-RPGLE developers
- Visual data flow path from AS/400 to modern React UI
- Field-by-field traceability across all technology layers
- Code examples from actual implementation files
- Educational content about DDS data types and attributes
- Comprehensive reference for future transformations

**Quality Assurance:**
- All 10 CUSTMAST fields documented
- All CUSTDSP display fields mapped to React components
- Every transformation layer covered
- Cross-references to related documentation
- Professional formatting with tables and code blocks
- Clear examples for complex transformations

### File List

- `docs/transformations/CUST001/data-mapping.md` (created)
- `_bmad-output/implementation-artifacts/5-3-document-dds-to-modern-stack-mapping.md` (modified)
- `_bmad-output/implementation-artifacts/sprint-status.yaml` (modified)

### Change Log

- Created comprehensive DDS to Modern Stack data mapping documentation (Date: 2026-01-02)
- Documented all CUSTMAST.dds fields across SQL, JPA, DTO, and TypeScript layers
- Documented all CUSTDSP.dds display fields to React component mappings
- Added DDS concepts section for developers with zero RPGLE knowledge
- Created full-stack data flow tables and transformation rules
- Added field-level notes, special cases, and performance considerations

### Code Review Fixes Applied (Date: 2026-01-02)

**Fixed by AI Code Reviewer - 6 issues resolved:**

1. **HIGH:** Added detailed explanation for why CREDITLIM and LASTORDER are not displayed in CUSTDSP DETAIL format - this is an intentional design decision from the original RPGLE program, not an implementation gap
2. **MEDIUM:** Updated File List to include story file and sprint-status.yaml
3. **MEDIUM:** Validated CustomerDTO.java mapping claims - all 10 fields correctly mapped with proper DDS field references
4. **MEDIUM:** Added note referencing V1__create_custmast.sql for full SQL migration with DDS comments
5. **MEDIUM:** Enhanced documentation explaining design decision to omit credit limit and last order date from UI display (functional equivalence with AS/400 green screen)
6. **MEDIUM:** Added cross-reference note for SQL migration file with detailed DDS traceability comments

**Quality improvements:**
- Clarified that displaying only 8 fields (not all 10 database fields) matches original green screen behavior
- Added business context for field visibility decisions
- Improved traceability documentation
