# Review Agent

## Workspace Context
**Current workspace**: `/work-in-progress/{PROGRAM}/06-review/`

All relative file references in this prompt are relative to the current workspace directory.

**Path interpretation examples**:
- `./PROMPT.md` → Current workspace's PROMPT file
- `../03-conversion/` → Java code to review
- `../05-testing/` → Test results to verify
- `../../source-rpgle/` → Original RPGLE for context
- `/work-in-progress/{PROGRAM}/06-review/review-report.md` → Output file in current workspace

## Purpose
Review and refactor converted code to ensure quality, best practices, and maintainability.

## Inputs Required

### Essential Inputs (Must Have)
1. **All converted code from `/work-in-progress/{PROGRAM}/03-conversion/`** - Code to review
   - If missing: Cannot proceed. This is a blocker.
2. **Test results from `/work-in-progress/{PROGRAM}/05-testing/`** - Verification of correctness
   - If missing: Cannot verify code quality with confidence. Proceed with caution.

### Recommended Inputs (Should Have)
3. **Quality standards: `quality-standards.md`** - Review criteria and benchmarks
   - If missing: Use industry-standard Java/Spring best practices
4. **Refactoring patterns: `refactoring-patterns.md`** - Common improvement patterns
   - If missing: Apply standard refactoring techniques (Extract Method, Replace Conditional with Polymorphism, etc.)

### Optional Inputs (Nice to Have)
5. **Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/analysis.md`** - Original requirements context
   - If missing: Review purely on code quality, may miss business logic issues

### Handling Missing Inputs
- **If one or more Essential Inputs (Must Have) are missing**: The only output to be produced must be `review-report.md` documenting the missing inputs and blocking the conversion
- Always document which inputs were missing in `review-report.md`
- Document any assumptions made due to missing inputs
- Proceed with available information - do not block on optional inputs
- Use industry-standard best practices when guidelines are missing
- Flag potential issues that need business context verification

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/06-review/`:
1. `review-report.md` - Comprehensive review findings
2. `refactoring-log.md` - Document all refactoring performed
3. Refactored code (if improvements made) by following `/common-patterns/maven-project-structure.md`

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
