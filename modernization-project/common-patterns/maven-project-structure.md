# Maven Project Structure Convention

## IMPORTANT: All Java Artifacts Must Follow Maven Directory Structure

When creating any Java-related artifacts (classes, interfaces, tests, resources), you **MUST** organize them according to the standard Maven directory layout.

---

## Standard Maven Directory Structure

```
project-root/
├── src/
│   ├── main/
│   │   ├── java/              # All production Java source files
│   │   │   └── com/
│   │   │       └── smeup/
│   │   │           └── erp/
│   │   │               ├── controllers/
│   │   │               ├── services/
│   │   │               ├── repositories/
│   │   │               ├── entities/
│   │   │               ├── dtos/
│   │   │               ├── exceptions/
│   │   │               ├── config/
│   │   │               └── utils/
│   │   └── resources/         # Application properties, configs, static files
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       ├── java/              # All test Java source files
│       │   └── com/
│       │       └── smeup/
│       │           └── erp/
│       │               ├── controllers/
│       │               ├── services/
│       │               ├── repositories/
│       │               └── testdata/
│       └── resources/         # Test-specific resources
│           └── application-test.yml
└── pom.xml
```

---

## Package to Directory Mapping Rules

### Rule 1: Package Declaration Must Match Directory Path

**Java package declaration:**
```java
package com.smeup.erp.services;
```

**MUST be placed in:**
```
src/main/java/com/smeup/erp/services/YourClass.java
```

### Rule 2: Full Path Components

The directory path consists of:
1. **Base directory**: `src/main/java/` (for production) or `src/test/java/` (for tests)
2. **Package path**: Each dot (`.`) in the package name becomes a directory separator (`/`)
3. **File name**: The Java class name with `.java` extension

### Examples

| Package Declaration | File Path |
|-------------------|-----------|
| `com.smeup.erp.controllers` | `src/main/java/com/smeup/erp/controllers/CustomerController.java` |
| `com.smeup.erp.services` | `src/main/java/com/smeup/erp/services/CustomerService.java` |
| `com.smeup.erp.repositories` | `src/main/java/com/smeup/erp/repositories/CustomerRepository.java` |
| `com.smeup.erp.entities` | `src/main/java/com/smeup/erp/entities/Customer.java` |
| `com.smeup.erp.dtos` | `src/main/java/com/smeup/erp/dtos/CustomerDTO.java` |
| `com.smeup.erp.exceptions` | `src/main/java/com/smeup/erp/exceptions/ValidationException.java` |
| `com.smeup.erp.config` | `src/main/java/com/smeup/erp/config/DatabaseConfig.java` |
| `com.smeup.erp.utils` | `src/main/java/com/smeup/erp/utils/DateUtils.java` |

---

## Test Files Structure

**Test classes MUST:**
1. Be placed in `src/test/java/` (NOT `src/main/java/`)
2. Follow the same package structure as the code they test
3. Typically have the same name as the class being tested + `Test` suffix

### Example

**Production class:**
```
src/main/java/com/smeup/erp/services/CustomerService.java
```

**Test class:**
```
src/test/java/com/smeup/erp/services/CustomerServiceTest.java
```

---

## Resources Files Structure

### Production Resources (`src/main/resources/`)

- **Application configuration**: `application.yml`, `application-{profile}.yml`
- **Static files**: `static/` directory
- **Templates**: `templates/` directory
- **Database scripts**: `db/migration/` (for Flyway) or `db/changelog/` (for Liquibase)

### Test Resources (`src/test/resources/`)

- **Test configuration**: `application-test.yml`
- **Test data files**: CSV, JSON, XML files for test fixtures
- **Mock data**: Any static test data

---

## Common Package Organization

Use this standard package structure for the base package `com.smeup.erp`:

| Package | Purpose | Example Classes |
|---------|---------|-----------------|
| `controllers` | REST API endpoints | `CustomerInquiryController.java` |
| `services` | Business logic layer | `CustomerInquiryService.java` |
| `repositories` | Data access layer (JPA) | `CustomerRepository.java` |
| `entities` | JPA entity classes | `Customer.java`, `Order.java` |
| `dtos` | Data Transfer Objects | `CustomerInquiryDTO.java` |
| `exceptions` | Custom exception classes | `ValidationException.java` |
| `config` | Spring configuration classes | `SecurityConfig.java` |
| `utils` | Utility/helper classes | `DateUtils.java`, `StringUtils.java` |
| `constants` | Application constants | `ErrorMessages.java` |

---

## Step-by-Step Process for Creating Java Files

When you need to create a Java class, follow these steps:

1. **Determine the package name** based on the class type
   - Example: For a service class → `com.smeup.erp.services`

2. **Convert package to directory path**
   - Replace dots with slashes: `com/smeup/erp/services`

3. **Add Maven base directory**
   - Production: `src/main/java/com/smeup/erp/services`
   - Test: `src/test/java/com/smeup/erp/services`

4. **Add the file name**
   - `src/main/java/com/smeup/erp/services/CustomerService.java`

5. **Ensure the package declaration in the file matches**
   ```java
   package com.smeup.erp.services;

   public class CustomerService {
       // implementation
   }
   ```

---

## Common Mistakes to Avoid

❌ **WRONG**: Creating files in flat directories
```
services/CustomerService.java
controllers/CustomerController.java
```

✅ **CORRECT**: Following full package path
```
src/main/java/com/smeup/erp/services/CustomerService.java
src/main/java/com/smeup/erp/controllers/CustomerController.java
```

❌ **WRONG**: Package doesn't match directory
```
File: src/main/java/com/smeup/erp/services/CustomerService.java
Package: com.example.services;  // Mismatch!
```

✅ **CORRECT**: Package matches directory
```
File: src/main/java/com/smeup/erp/services/CustomerService.java
Package: com.smeup.erp.services;  // Perfect match!
```

❌ **WRONG**: Putting tests in main directory
```
src/main/java/com/smeup/erp/services/CustomerServiceTest.java
```

✅ **CORRECT**: Tests in test directory
```
src/test/java/com/smeup/erp/services/CustomerServiceTest.java
```

---

## Agent-Specific Instructions

### For ALL Agents Creating Java Code:

1. **ALWAYS** create the full directory path before writing Java files
2. **VERIFY** the package declaration matches the directory structure
3. **ORGANIZE** files by type into appropriate packages (controllers, services, etc.)
4. **SEPARATE** production code (`src/main/java`) from test code (`src/test/java`)
5. **PLACE** configuration files in `src/main/resources` or `src/test/resources`

### When Working in `/work-in-progress/{PROGRAM}/`:

During development phases (analysis, database, conversion, ui, testing), you may create files in phase-specific directories. However, ensure that:

- Java files still follow the package structure within those directories
- When files are moved to `/final-output/`, they MUST follow the standard Maven structure
- Document the directory structure used in your phase notes

---

## Final Output Structure

All final Java artifacts in `/final-output/` **MUST** strictly follow this Maven structure:

```
final-output/
├── src/
│   ├── main/
│   │   ├── java/com/smeup/erp/
│   │   └── resources/
│   └── test/
│       ├── java/com/smeup/erp/
│       └── resources/
├── pom.xml
└── README.md
```

This ensures that:
- Maven can compile the project
- IDEs can properly recognize the project structure
- Tests can be discovered and run automatically
- The application can be packaged and deployed correctly

---

## Quick Reference

**Creating a Controller:**
```
Package: com.smeup.erp.controllers
Path: src/main/java/com/smeup/erp/controllers/YourController.java
```

**Creating a Service:**
```
Package: com.smeup.erp.services
Path: src/main/java/com/smeup/erp/services/YourService.java
```

**Creating a Test:**
```
Package: com.smeup.erp.services
Path: src/test/java/com/smeup/erp/services/YourServiceTest.java
```

**Creating application.yml:**
```
Path: src/main/resources/application.yml
```

---

**Remember**: Maven expects this exact structure. Deviating from it will cause compilation failures and deployment issues.
