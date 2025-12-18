# Phase 5: Testing - Program: CUST001

# Testing Agent

## Purpose
Create comprehensive test suites to ensure Java code matches RPGLE behavior.

## Inputs Required
1. Original RPGLE program from `/source-rpgle/programs/`
2. Converted Java code from `/work-in-progress/{PROGRAM}/03-conversion/`
3. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
4. Test templates from this folder

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/05-testing/`:
1. Service unit tests (e.g., `CustomerInquiryServiceTest.java`)
2. Controller integration tests (e.g., `CustomerInquiryControllerTest.java`)
3. Repository tests if needed into `unit-tests`
4. Test data setup scripts into `test-data`
5. `test-results.md` - Document test coverage and results

## IMPORTANT: Java Package Structure
**Java packages MUST match the folder structure.**

When you declare a package in Java:
```java
package com.example.myapp;
```

The file MUST be placed in the corresponding folder path:
```
unit-tests/com/example/myapp/YourTestClass.java
```

**Examples:**
- Package: `com.smeup.erp.service` → File: `unit-tests/com/smeup/erp/service/CustomerServiceTest.java`
- Package: `com.smeup.erp.controller` → File: `unit-tests/com/smeup/erp/controller/CustomerControllerTest.java`

**Always:**
1. Determine the full package name (e.g., `com.smeup.erp.service`)
2. Create the corresponding folder structure under `unit-tests/` or `test-data/` or `integration-tests/`
3. Place the `.java` file in the deepest package folder
4. The folder path must exactly mirror the package declaration

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
