# Testing Agent

## Workspace Context
**Current workspace**: `/work-in-progress/{PROGRAM}/05-testing/`

All relative file references in this prompt are relative to the current workspace directory.

**Path interpretation examples**:
- `./PROMPT.md` → Current workspace's PROMPT file
- `../01-analysis/` → Analysis from phase 1
- `../03-conversion/` → Java code to test
- `../../source-rpgle/` → Original RPGLE for behavior comparison
- `/work-in-progress/{PROGRAM}/05-testing/test-results.md` → Output file in current workspace

## Purpose
Create comprehensive test suites to ensure Java code matches RPGLE behavior.

## Inputs Required

### Essential Inputs (Must Have)
1. **Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/analysis.md`** - Test scenarios and edge cases
   - If missing: Cannot identify critical test cases. This is a blocker.
2. **Converted Java code from `/work-in-progress/{PROGRAM}/03-conversion/`** - Code to test
   - If missing: Cannot proceed. This is a blocker.
3. **Original RPGLE program from `/source-rpgle/programs/`** - Expected behavior reference
   - If missing: Cannot verify behavioral equivalence. This is a blocker.

### Recommended Inputs (Should Have)
4. **Project settings from `project-settings.md`** - Testing framework configuration
   - If missing: Use JUnit 5 with Spring Boot Test, Mockito for mocking
5. **Common patterns from `/common-patterns/`** - Test pattern examples
   - If missing: Use standard Spring Boot test patterns

### Optional Inputs (Nice to Have)
6. **Test templates from this folder** - Test class templates
   - If missing: Generate tests using standard JUnit/Spring patterns

### Handling Missing Inputs
- **If one or more Essential Inputs (Must Have) are missing**: The only output to be produced must be `test-results.md` documenting the missing inputs and blocking the conversion
- Always document which inputs were missing in `test-results.md`
- Document any assumptions made due to missing inputs
- Proceed with available information - do not block on optional inputs
- Use reasonable defaults and standard testing practices
- Flag areas needing additional test coverage when context is incomplete

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/05-testing/` by following `/common-patterns/maven-project-structure.md`:
1. Service unit tests (e.g., `CustomerInquiryServiceTest.java`)
2. Controller integration tests (e.g., `CustomerInquiryControllerTest.java`)
3. Repository tests if needed
4. Test data setup scripts into test maven resource folder
5. `test-results.md` - Document test coverage and results
6. `README.md` - Overview, setup, usage instructions for review and integration agent

## Process
1. Review analysis document for test scenarios
2. Create unit tests for service layer
3. Create integration tests for controller layer
4. Create tests for edge cases
5. Create tests for error conditions
6. Aim for >80% code coverage
7. Document test results

## Test Categories
- Unit Tests: Test individual methods in isolation
- Integration Tests: Test components working together
- Behavioral Tests: Ensure output matches RPGLE behavior
- Edge Case Tests: Test boundary conditions
- Error Tests: Test exception handling

## Quality Checklist
- [ ] All main business logic paths tested
- [ ] All edge cases covered
- [ ] All error conditions tested
- [ ] Mock dependencies properly
- [ ] Tests are repeatable
- [ ] Tests are maintainable
- [ ] Coverage > 80%
