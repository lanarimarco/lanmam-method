# Error Handling Strategy

## Exception Hierarchy

```
ServiceException (base)
├── ValidationException
├── NotFoundException
├── BusinessRuleException
├── DataAccessException
└── ExternalServiceException
```

## When to Use Each Exception

### ValidationException
- Invalid input data
- Required fields missing
- Data format errors
- Business validation failures

### NotFoundException
- Entity not found by ID
- Record doesn't exist (RPGLE CHAIN not found)

### BusinessRuleException
- Business rule violations
- State conflicts
- Constraint violations

## Exception Handling Pattern

```java
try {
    // Business logic
} catch (ValidationException e) {
    log.warn("Validation failed: {}", e.getMessage());
    throw e; // Re-throw for controller to handle
} catch (Exception e) {
    log.error("Unexpected error", e);
    throw new ServiceException("Operation failed", e);
}
```

## Controller Error Handling

```java
@ExceptionHandler(ValidationException.class)
public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
    return ResponseEntity
        .badRequest()
        .body(new ErrorResponse(e.getMessage()));
}
```
