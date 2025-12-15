# Logging Standards

## Log Levels

### ERROR
- System failures
- Unhandled exceptions
- Data corruption issues
- Critical business failures

### WARN
- Validation failures
- Business rule violations
- Recoverable errors
- Deprecated usage

### INFO
- Major business operations
- Service start/stop
- Configuration changes
- Important state changes

### DEBUG
- Detailed business flow
- Database queries
- Method entry/exit
- Variable values

## Logging Pattern

```java
private static final Logger log = LoggerFactory.getLogger(ClassName.class);

// INFO: Business operations
log.info("Processing customer inquiry for: {}", customerNumber);

// WARN: Validation issues
log.warn("Invalid customer number provided: {}", customerNumber);

// ERROR: Exceptions
log.error("Failed to process inquiry", exception);

// DEBUG: Detailed flow
log.debug("Retrieved customer: {}", customer);
```

## What NOT to Log
- Sensitive data (passwords, SSN, credit cards)
- Full entity dumps in production
- Excessive debug info in production
