# Review Agent

## Purpose
Review and refactor converted code to ensure quality, best practices, and maintainability.

## Inputs Required
1. All converted code from `/work-in-progress/{PROGRAM}/03-conversion/`
2. Test results from `/work-in-progress/{PROGRAM}/05-tests/`
3. Quality standards: `quality-standards.md`
4. Refactoring patterns: `refactoring-patterns.md`

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/06-review/`:
1. `review-report.md` - Comprehensive review findings
2. `refactoring-log.md` - Document all refactoring performed
3. Refactored code (if improvements made)
4. **Update project tracking**: Update `/documentation/progress-tracking/conversion-status.md` to mark program as complete

## Review Areas
1. Code Quality
   - Follows Spring Boot best practices
   - Proper use of design patterns
   - Code is DRY (Don't Repeat Yourself)
   - Appropriate abstraction levels

2. Security
   - Input validation
   - SQL injection prevention (JPA handles this)
   - Sensitive data handling
   - Authentication/authorization considerations

3. Performance
   - Efficient database queries
   - Proper indexing recommendations
   - Caching opportunities
   - N+1 query prevention

4. Maintainability
   - Clear, descriptive naming
   - Appropriate comments
   - Logical code organization
   - Proper exception handling

5. Testing
   - Adequate test coverage
   - Tests are meaningful
   - Edge cases covered

## Process
1. Review code against quality standards
2. Run static analysis tools (if available)
3. Identify code smells
4. Suggest or implement improvements
5. Verify tests still pass after refactoring
6. Document all changes and rationale

## Quality Checklist
- [ ] Code follows Java/Spring conventions
- [ ] No security vulnerabilities
- [ ] Performance is acceptable
- [ ] Code is maintainable
- [ ] Test coverage adequate
- [ ] Documentation complete
- [ ] Ready for production
- [ ] **Progress tracking updated** - Program marked complete in `/documentation/progress-tracking/conversion-status.md`
