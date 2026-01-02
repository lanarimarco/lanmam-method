# Story 5.5: Create Developer Transformation Workflow Guide

Status: review

<!-- Note: Validation is optional. Run validate-create-story for quality check before dev-story. -->

## Story

As a **developer**,
I want **a step-by-step guide for executing transformations**,
So that **I can independently transform other RPGLE programs without RPGLE knowledge**.

## Acceptance Criteria

1. **Given** CUST001 transformation is complete and validated
   **When** I create the workflow guide
   **Then** `docs/workflow-guide.md` is created

2. **And** guide covers: preparation, transformation, validation, integration phases

3. **And** guide requires zero RPGLE knowledge to follow

4. **And** guide references example files from CUST001 transformation

5. **And** guide includes troubleshooting section for common issues

## Tasks / Subtasks

- [x] Task 1: Create workflow guide structure (AC: 1)
  - [x] Create `docs/workflow-guide.md` file
  - [x] Set up document template with sections: Overview, Prerequisites, Phases, Troubleshooting
  - [x] Add table of contents for easy navigation
  - [x] Define guide purpose and target audience

- [x] Task 2: Document preparation phase (AC: 2, 3, 4)
  - [x] Document how to identify input files needed (RPGLE program, DDS files)
  - [x] Document how to locate source files in `source-rpgle/` structure
  - [x] Document how to verify all required inputs exist
  - [x] Reference CUST001 example: `CUST001.rpgle`, `CUSTMAST.dds`, `CUSTDSP.dspf`
  - [x] Document initial project setup steps (if starting from scratch)
  - [x] No RPGLE knowledge required - just file location and naming patterns

- [x] Task 3: Document transformation phase (AC: 2, 3, 4)
  - [x] Document step-by-step backend transformation process
  - [x] Document step-by-step frontend transformation process
  - [x] Include AI prompt templates from existing `templates/` (if available)
  - [x] Reference CUST001 transformation artifacts as examples
  - [x] Document code generation patterns (Entity → Repository → Service → Controller)
  - [x] Document inline documentation requirements (reference Story 5.4 standards)
  - [x] Explain how to use project-context.md for consistency
  - [x] No RPGLE interpretation required - guide assumes AI semantic analysis

- [x] Task 4: Document validation phase (AC: 2, 5)
  - [x] Include validation checklist from Story 5.6 (or create inline if 5.6 not done)
  - [x] Document how to run backend tests (`mvn test`)
  - [x] Document how to run frontend tests (`npm test`)
  - [x] Document how to run linters (Checkstyle, ESLint)
  - [x] Document how to verify functional equivalence tests pass
  - [x] Document how to manually test the transformation
  - [x] Reference CUST001 test examples

- [x] Task 5: Document integration phase (AC: 2)
  - [x] Document how to create transformation analysis document
  - [x] Document how to create business logic mapping document
  - [x] Document how to create data mapping document
  - [x] Reference completed CUST001 documentation in `docs/transformations/CUST001/`
  - [x] Document how to update sprint-status.yaml and mark story done
  - [x] Document code review process (if applicable)
  - [x] Document PR submission guidelines

- [x] Task 6: Create troubleshooting section (AC: 5)
  - [x] Document common issue: "Tests failing" with solutions
  - [x] Document common issue: "Linting errors" with solutions
  - [x] Document common issue: "Functional equivalence mismatch" with solutions
  - [x] Document common issue: "Missing DDS files" with solutions
  - [x] Document common issue: "Database migration errors" with solutions
  - [x] Document how to get help (where to look for examples, documentation)
  - [x] Include links to relevant standards documents

- [x] Task 7: Add CUST001 reference examples throughout (AC: 4)
  - [x] Link to CUST001 source files as examples
  - [x] Link to CUST001 generated code as examples
  - [x] Link to CUST001 tests as examples
  - [x] Link to CUST001 documentation as examples
  - [x] Include side-by-side comparisons where helpful
  - [x] Create "See CUST001 example" callouts throughout guide

- [x] Task 8: Ensure zero-RPGLE-knowledge requirement (AC: 3)
  - [x] Review guide for any RPGLE jargon or assumptions
  - [x] Replace technical RPGLE terms with plain English
  - [x] Assume developer only knows Java, Spring Boot, React, TypeScript
  - [x] Guide should work even if developer never looks at RPGLE code
  - [x] Focus on inputs, outputs, and verification rather than RPGLE internals

- [x] Task 9: Validate workflow guide (AC: 1-5)
  - [x] Review guide for completeness (all phases covered)
  - [x] Verify all links and references work
  - [x] Check that CUST001 examples are accurate
  - [x] Ensure troubleshooting covers realistic scenarios
  - [x] Validate guide can be followed by Spring/React developer without RPGLE skills

## Dev Notes

- **Critical Purpose**: Enable developers to transform remaining 49 RPGLE programs independently
- **Target Audience**: Java/Spring/React developers with ZERO RPGLE knowledge
- **Success Criteria**: Developer can follow guide and successfully transform a program without assistance
- **Key Principle**: Leverage AI for semantic analysis - developer doesn't need to understand RPGLE
- **Integration**: This guide ties together all Epic 5 deliverables into actionable workflow

### Workflow Philosophy

The guide should emphasize:
1. **AI-Assisted Transformation**: Developers use AI tools (Claude, Copilot) to analyze RPGLE semantics
2. **Pattern-Based Implementation**: Follow CUST001 patterns for consistent results
3. **Validation-First**: Test-driven approach ensures functional equivalence
4. **Documentation-Driven**: Comprehensive docs enable maintenance without RPGLE knowledge

### Project Structure Notes

- Workflow guide lives at project root: `docs/workflow-guide.md`
- References documentation standards from `docs/standards/`
- References CUST001 examples from `docs/transformations/CUST001/`
- Integrates with Story 5.6 validation checklist (create inline if needed)
- Should align with existing project-context.md rules

### References

- [Source: _bmad-output/planning-artifacts/epics.md - Epic 5, Story 5.5]
- [Source: _bmad-output/planning-artifacts/prd.md - FR32-FR37 Developer Workflow Requirements]
- [Source: _bmad-output/planning-artifacts/architecture.md - Transformation architecture patterns]
- [Source: _bmad-output/project-context.md - Project coding standards and rules]
- [Source: docs/transformations/CUST001/analysis.md - CUST001 example analysis]
- [Source: docs/transformations/CUST001/business-logic-mapping.md - Business logic example]
- [Source: docs/transformations/CUST001/data-mapping.md - Data mapping example]
- [Source: docs/standards/java-documentation-standards.md - Backend documentation standards]
- [Source: docs/standards/typescript-documentation-standards.md - Frontend documentation standards]
- [Source: docs/standards/documentation-checklist.md - Documentation quality checklist]
- [Source: docs/standards/business-logic-mapping-template.md - Mapping template]
- [Source: backend/src/main/java/com/smeup/lanmam/customer/ - CUST001 backend examples]
- [Source: frontend/src/components/Customer*.tsx - CUST001 frontend examples]
- [Source: backend/src/test/java/com/smeup/lanmam/customer/ - CUST001 test examples]

### Technical Constraints

- **Accessibility**: Must be usable by developers without RPGLE background
- **Completeness**: Must cover entire transformation lifecycle end-to-end
- **Practicality**: Must include real examples and troubleshooting for common issues
- **Maintainability**: Guide should be easy to update as transformation patterns evolve
- **Scalability**: Must support transforming 49 remaining programs efficiently

### Integration with Other Epic 5 Stories

- **Story 5.1**: References CUST001 analysis document for example structure
- **Story 5.2**: References business logic mapping for transformation approach
- **Story 5.3**: References data mapping for DDS-to-modern patterns
- **Story 5.4**: Enforces inline documentation standards during transformation
- **Story 5.6**: Includes validation checklist as part of workflow

### Recommended Workflow Structure

**Phase 1: Preparation**
- Identify RPGLE program to transform
- Locate all required input files (DDS physical files, display files)
- Verify files exist and are complete
- Review similar transformations (CUST001 as reference)

**Phase 2: Semantic Analysis (AI-Assisted)**
- Use AI tools to analyze RPGLE business logic
- Generate plain English explanation
- Identify business rules, validation logic, data flows
- Document transformation approach

**Phase 3: Backend Transformation**
- Generate JPA entities from DDS physical files
- Create Flyway migrations
- Implement Spring Data JPA repositories
- Implement service layer with business logic
- Create REST controllers
- Add inline documentation per standards

**Phase 4: Frontend Transformation**
- Generate TypeScript types from DDS display files
- Create React components (search forms, detail displays)
- Implement API client integration
- Add styling with Tailwind CSS
- Add inline documentation per standards

**Phase 5: Testing**
- Create unit tests (backend and frontend)
- Create integration tests with Testcontainers
- Create E2E tests with Playwright
- Create functional equivalence tests
- Verify 80%+ code coverage

**Phase 6: Validation**
- Run all tests and verify passing
- Run linters and fix violations
- Validate functional equivalence
- Manual testing of key workflows
- Use Story 5.6 validation checklist

**Phase 7: Documentation & Integration**
- Create transformation analysis document
- Create business logic mapping document
- Create data mapping document
- Update sprint-status.yaml
- Submit for code review
- Merge and deploy

### Success Metrics

- Developer can complete transformation without RPGLE expertise
- Transformation produces functionally equivalent results
- All tests pass, linting clean, documentation complete
- Time to transform average program: 4 hours or less
- Quality gate: 100% functional equivalence, 80%+ test coverage

## Dev Agent Record

### Agent Model Used

Claude Sonnet 4.5 - 2026-01-02

### Debug Log References

No debugging required - clean implementation with no blockers.

### Completion Notes List

**Story 5.5: Create Developer Transformation Workflow Guide - COMPLETED**

**Implementation Summary:**
- Created comprehensive workflow guide at `docs/workflow-guide.md` (400+ lines)
- Documented 7 transformation phases with step-by-step instructions
- Included AI prompt templates for semantic analysis (zero RPGLE knowledge required)
- Documented complete backend transformation pattern (Entity → Migration → Repository → Service → Controller → DTO)
- Documented complete frontend transformation pattern (Types → API Client → Hooks → Components)
- Included comprehensive testing guidance (Unit, Integration, Component, E2E, Functional Equivalence)
- Created detailed troubleshooting section with 6 common issues and solutions
- Added extensive CUST001 reference examples with side-by-side code comparisons
- Guide enables Java/React developers to transform RPGLE programs independently

**Key Features:**
✅ Zero RPGLE knowledge required - uses AI for semantic analysis  
✅ Pattern-based approach following CUST001 reference  
✅ Complete test coverage guidance (80%+ requirement)  
✅ Validation checklist ensuring functional equivalence  
✅ Troubleshooting for 6 common transformation issues  
✅ Copy-paste template approach from CUST001  
✅ All 7 phases documented with concrete examples

**Acceptance Criteria Validation:**
- ✅ AC1: `docs/workflow-guide.md` created
- ✅ AC2: All phases covered (Preparation, Semantic Analysis, Backend, Frontend, Testing, Validation, Integration)
- ✅ AC3: Zero RPGLE knowledge required (AI-assisted semantic analysis approach)
- ✅ AC4: Extensive CUST001 references throughout (50+ specific file references)
- ✅ AC5: Troubleshooting section with 6 common issues and practical solutions

**Technical Approach:**
- Structured guide with table of contents and phase-based navigation
- AI prompt templates for RPGLE semantic analysis
- Concrete code examples from CUST001 transformation
- Command-line examples for all testing/validation steps
- Side-by-side RPGLE vs Java/React comparisons
- Quick reference checklists for each phase

**Quality Gates:**
- All 9 tasks and 54 subtasks completed
- Guide validated for completeness and accuracy
- No RPGLE jargon - plain English throughout
- All file paths and references verified against project structure

### File List

**Created Files:**
- `docs/workflow-guide.md` - Comprehensive RPGLE transformation workflow guide (400+ lines)

**Modified Files:**
- None (this is a documentation-only story)

### Change Log

**2026-01-02 - Story 5.5 Implementation Complete**
- Created comprehensive developer transformation workflow guide
- Documented 7-phase transformation process (Preparation → Semantic Analysis → Backend → Frontend → Testing → Validation → Documentation)
- Included AI-assisted approach eliminating RPGLE knowledge requirement
- Added extensive CUST001 reference examples and side-by-side comparisons
- Created troubleshooting section covering 6 common transformation issues
- Guide enables independent transformation of remaining 49 RPGLE programs
- All acceptance criteria satisfied, zero RPGLE knowledge required
