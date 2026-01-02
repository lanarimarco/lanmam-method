# Story 5.2: Document Business Logic Mapping

Status: ready-for-dev

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **business logic mapping from RPGLE to Spring Boot documented**,
So that **I can verify the transformation preserves all business rules**.

## Acceptance Criteria

1. **Given** CUST001 backend transformation is complete **When** I create the business logic mapping **Then** `docs/transformations/CUST001/business-logic-mapping.md` is created
2. Document maps every RPGLE business rule to Spring Boot implementation
3. Document includes: validation rules, error handling, data transformations
4. Document uses side-by-side comparison format (RPGLE → Spring Boot)
5. Document is understandable by developers reviewing functional equivalence

## Tasks / Subtasks

- [ ] Task 1: Set up business logic mapping document (AC: 1)
  - [ ] Create `business-logic-mapping.md` in `docs/transformations/CUST001/`
  - [ ] Set up document template with sections
  - [ ] Create table of contents

- [ ] Task 2: Map validation rules (AC: 2, 3, 4)
  - [ ] Identify all validation rules in CUST001.rpgle
  - [ ] Document customer number validation (RPGLE → Java)
  - [ ] Document field-level validations (required fields, formats)
  - [ ] Create side-by-side comparison table
  - [ ] Verify functional equivalence for each rule

- [ ] Task 3: Map error handling logic (AC: 2, 3, 4)
  - [ ] Identify error handling in RPGLE (error indicators, messages)
  - [ ] Document how errors are handled in CustomerService.java
  - [ ] Map RPGLE error messages to Spring Boot exceptions
  - [ ] Show error response format transformation
  - [ ] Include examples of error scenarios

- [ ] Task 4: Map data transformations (AC: 2, 3, 4)
  - [ ] Identify any data transformations in RPGLE
  - [ ] Document field formatting logic (dates, numbers, strings)
  - [ ] Map RPGLE data operations to Java operations
  - [ ] Show before/after examples for key transformations
  - [ ] Verify data integrity preserved

- [ ] Task 5: Map database operations (AC: 2, 3, 4)
  - [ ] Identify RPGLE file operations (CHAIN, READ, etc.)
  - [ ] Map to JPA repository methods
  - [ ] Document transaction handling differences
  - [ ] Show query transformations (RPGLE keys → JPA queries)
  - [ ] Include performance considerations

- [ ] Task 6: Create comprehensive mapping reference (AC: 2, 4, 5)
  - [ ] Create master table: RPGLE Operation → Spring Boot Implementation
  - [ ] Include line number references from RPGLE source
  - [ ] Include file/method references for Spring Boot code
  - [ ] Add notes explaining any transformation decisions
  - [ ] Ensure non-RPGLE developers can follow the mapping

## Dev Notes

- **Documentation Focus**: Proving functional equivalence at business logic level
- **Target Audience**: Developers and QA engineers verifying transformations
- **Format**: Side-by-side comparisons with clear traceability
- **Validation**: This document supports FR38-FR40 (functional equivalence validation)

### Project Structure Notes

- Document builds on `analysis.md` created in Story 5.1
- Should reference specific RPGLE lines and Java methods
- Use code snippets to illustrate key mappings
- Focus on "what business logic is preserved" not implementation details

### References

- [Source: _bmad-output/planning-artifacts/epics.md - Epic 5, Story 5.2]
- [Source: _bmad-output/planning-artifacts/prd.md - FR24-FR31, FR38-FR40]
- [Source: source-rpgle/programs/CUST001.rpgle - Original RPGLE program with business logic]
- [Source: backend/src/main/java/com/smeup/rpgletransformer/service/CustomerService.java - Business logic implementation]
- [Source: backend/src/main/java/com/smeup/rpgletransformer/controller/CustomerController.java - API error handling]
- [Source: backend/src/main/java/com/smeup/rpgletransformer/dto/CustomerDTO.java - Data transformation]
- [Source: docs/transformations/CUST001/analysis.md - Program overview from Story 5.1]

### Technical Constraints

- **Completeness**: Every RPGLE business rule must be mapped
- **Traceability**: Clear references to source code line numbers
- **Accuracy**: Mappings must be verified against actual code
- **Clarity**: Use examples and comparisons, not just descriptions
- **Testability**: Should enable QA to create test scenarios

### Documentation Standards

- Use Markdown tables for structured mappings
- Include code blocks with syntax highlighting (RPGLE and Java)
- Use clear section headers for different logic categories
- Include "Why this matters" explanations for complex transformations
- Cross-reference related validation tests
- Link to relevant source files

### Business Logic Categories to Cover

1. **Input Validation**
   - Customer number format validation
   - Required field checks
   - Business rule constraints

2. **Data Retrieval Logic**
   - Customer lookup operations
   - Not found handling
   - Data loading patterns

3. **Business Rules**
   - Any calculations or derived fields
   - Status checks or conditional logic
   - Business constraints

4. **Error Handling**
   - Error detection logic
   - Error message generation
   - User feedback patterns

5. **Response Formatting**
   - How data is prepared for display
   - Field transformations for output
   - Response structure

## Dev Agent Record

### Agent Model Used
- Model: [To be filled by Dev agent]
- Run ID: [To be filled by Dev agent]

### Implementation Summary
[To be filled by Dev agent after completion]

### Files Created/Modified
[To be filled by Dev agent]

### Validation Status
[To be filled by Dev agent]

---

**Story Created:** 2025-01-02
**Epic:** 5 - Transformation Documentation & Developer Workflow
**Dependencies:** Story 5.1 (analysis document must exist as reference)
**Estimated Effort:** 2-3 hours
