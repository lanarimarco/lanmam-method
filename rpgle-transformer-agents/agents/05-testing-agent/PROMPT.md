# Testing Agent

## Purpose
Create comprehensive test suites to ensure Java code matches RPGLE behavior.

## Inputs Required
1. Project settings from `project-settings.md`
2. Original RPGLE program from `/source-rpgle/programs/`
3. Converted Java code from `/work-in-progress/{PROGRAM}/03-conversion/`
4. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
5. All common patterns from `/common-patterns/`
6. Test templates from this folder

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
