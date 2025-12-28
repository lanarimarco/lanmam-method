# Quality Standards for Code Review

## Code Quality Standards

### 1. Spring Boot Best Practices
- [ ] Use constructor-based dependency injection (preferred over field injection)
- [ ] Services should be annotated with `@Service`
- [ ] Controllers should be annotated with `@RestController`
- [ ] Repositories should extend `JpaRepository` or `CrudRepository`
- [ ] Use `@Transactional` appropriately (service layer, not controller)
- [ ] DTOs should be immutable where possible
- [ ] Use proper HTTP status codes in REST responses

### 2. Code Organization
- [ ] Clear separation of concerns (Controller → Service → Repository)
- [ ] One public class per file
- [ ] Logical package structure maintained
- [ ] No circular dependencies
- [ ] Proper use of interfaces where beneficial

### 3. Naming Conventions
- [ ] Classes: PascalCase (e.g., `CustomerService`)
- [ ] Methods: camelCase (e.g., `findCustomerById`)
- [ ] Constants: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)
- [ ] Variables: camelCase, descriptive names
- [ ] Boolean methods: start with `is`, `has`, `can` (e.g., `isActive`)

### 4. Error Handling
- [ ] Use custom exceptions for business logic errors
- [ ] Global exception handler (`@ControllerAdvice`) implemented
- [ ] Proper exception hierarchy (extend from appropriate base classes)
- [ ] Meaningful error messages for users
- [ ] Technical details logged but not exposed to users
- [ ] Input validation with proper error responses

### 5. Security
- [ ] Input validation on all user inputs
- [ ] SQL injection prevention (use JPA parameterized queries)
- [ ] No sensitive data in logs
- [ ] Passwords/secrets from environment variables
- [ ] Authentication/authorization considered where needed
- [ ] CORS properly configured

### 6. Performance
- [ ] Efficient database queries (no N+1 problems)
- [ ] Use pagination for large result sets
- [ ] Lazy loading configured appropriately
- [ ] Database indexes recommended where needed
- [ ] Caching opportunities identified
- [ ] Connection pooling configured

### 7. Testing
- [ ] Unit tests for service layer (>80% coverage)
- [ ] Integration tests for controllers
- [ ] Tests are isolated and repeatable
- [ ] Edge cases covered
- [ ] Error conditions tested
- [ ] Mock dependencies properly
- [ ] Meaningful test names (should describe what is being tested)

### 8. Documentation
- [ ] JavaDoc for public APIs
- [ ] Complex business logic explained in comments
- [ ] README files present and accurate
- [ ] API endpoints documented
- [ ] Configuration options documented

### 9. Code Maintainability
- [ ] DRY principle followed (Don't Repeat Yourself)
- [ ] Single Responsibility Principle followed
- [ ] Methods are short and focused (<50 lines ideally)
- [ ] Classes have clear, single purpose
- [ ] Magic numbers replaced with named constants
- [ ] No commented-out code
- [ ] No TODO comments without issue tracking

### 10. Logging
- [ ] Appropriate log levels (ERROR, WARN, INFO, DEBUG, TRACE)
- [ ] Structured logging with context
- [ ] No excessive logging (performance impact)
- [ ] Sensitive data not logged
- [ ] Exception stack traces logged at ERROR level

## React/TypeScript Quality Standards

### 1. Component Quality
- [ ] Functional components with hooks (no class components)
- [ ] Props properly typed with TypeScript interfaces
- [ ] Components are focused and reusable
- [ ] No logic in JSX (extract to functions)
- [ ] Proper key props in lists

### 2. TypeScript Usage
- [ ] No `any` types (use proper types or `unknown`)
- [ ] Interfaces for all props and state
- [ ] Type definitions exported and reusable
- [ ] Enums for fixed sets of values
- [ ] Proper null/undefined handling

### 3. State Management
- [ ] useState for local state
- [ ] useEffect dependencies correct
- [ ] No unnecessary re-renders
- [ ] State lifted to appropriate level
- [ ] Custom hooks for reusable logic

### 4. API Integration
- [ ] API calls in service layer (not in components)
- [ ] Proper error handling
- [ ] Loading states handled
- [ ] API base URL from environment variables
- [ ] Request/response types defined

### 5. Accessibility
- [ ] Semantic HTML elements
- [ ] ARIA labels where needed
- [ ] Keyboard navigation support
- [ ] Focus management
- [ ] Color contrast sufficient

### 6. Performance
- [ ] React.memo for expensive components
- [ ] useMemo/useCallback where appropriate
- [ ] Lazy loading for routes
- [ ] Code splitting implemented
- [ ] Images optimized

## Acceptance Criteria

Code is considered ready for integration when:
- [ ] All quality standards met (or deviations documented)
- [ ] All tests passing
- [ ] No critical security issues
- [ ] Performance is acceptable
- [ ] Code is maintainable and readable
- [ ] Documentation is complete and accurate
