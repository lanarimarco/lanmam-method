# Code Review Checklist

## General Code Quality
- [ ] Code is readable and well-organized
- [ ] Variable/method names are descriptive
- [ ] No magic numbers (use constants)
- [ ] No commented-out code
- [ ] Consistent formatting
- [ ] Appropriate use of white space

## Spring Boot Best Practices
- [ ] Services are properly annotated with @Service
- [ ] Controllers use appropriate HTTP methods
- [ ] Proper use of @Transactional
- [ ] Dependencies injected via constructor
- [ ] Configuration externalized
- [ ] Appropriate logging levels used

## Error Handling
- [ ] All exceptions properly caught
- [ ] Meaningful error messages
- [ ] Appropriate exception types used
- [ ] No swallowed exceptions
- [ ] Global exception handler implemented

## Security
- [ ] Input validation present
- [ ] No SQL injection vulnerabilities
- [ ] Sensitive data not logged
- [ ] CORS properly configured
- [ ] Authentication/authorization considered

## Performance
- [ ] No N+1 query problems
- [ ] Appropriate use of lazy/eager loading
- [ ] Database queries optimized
- [ ] Caching used where appropriate
- [ ] No memory leaks

## Testing
- [ ] Unit tests cover main paths
- [ ] Edge cases tested
- [ ] Mocks used appropriately
- [ ] Tests are maintainable
- [ ] Test data is realistic

## Documentation
- [ ] Classes have JavaDoc
- [ ] Complex methods documented
- [ ] API endpoints documented
- [ ] README updated if needed

## RPGLE Conversion Specific
- [ ] All RPGLE logic converted
- [ ] Indicators properly handled
- [ ] File operations correctly mapped
- [ ] Display file logic preserved
- [ ] Business rules maintained
