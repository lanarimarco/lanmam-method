# Phase 3: Conversion - Program: CUST001

# Conversion Agent

## Purpose
Convert RPGLE programs to Java services following established patterns and best practices.

## Inputs Required
1. RPGLE source file from `/source-rpgle/programs/{PROGRAM}.rpgle`
2. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
3. Database entities from `/work-in-progress/{PROGRAM}/02-database/`
4. Display file specifications from `/source-rpgle/dds/display-files/`
5. All common patterns from `/common-patterns/`
6. Templates from this agent folder

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/03-conversion/`:
1. Service class (e.g., `CustomerInquiryService.java`) within `services` folder
2. Controller class (e.g., `CustomerInquiryController.java`) within `controllers` folder
3. DTO classes (e.g., `CustomerInquiryDTO.java`) within `dtos` folder
4. Exception classes if needed
5. `conversion-notes.md` - Explain all decisions



## Process
1. Review analysis document thoroughly
2. Study the RPGLE logic flow
3. Map RPG operations to Java/Spring patterns (use `/common-patterns/rpg-operations-mapping.md`)
4. Implement business logic in service layer
5. Create REST API in controller layer
6. Define DTOs for data transfer
7. Add proper error handling
8. Add logging at key points
9. Document any deviations or special handling
10. **Document any issues encountered** - If you face significant challenges, unclear requirements, or make important decisions, document them clearly in `conversion-notes.md` for the project manager to review

## IMPORTANT: Java Package Structure
**Java packages MUST match the folder structure.**

When you declare a package in Java:
```java
package com.example.myapp.services;
```

The file MUST be placed in the corresponding folder path:
```
services/com/example/myapp/services/YourClass.java
```

**Examples:**
- Package: `com.smeup.erp.services` → File: `services/com/smeup/erp/services/CustomerInquiryService.java`
- Package: `com.smeup.erp.controllers` → File: `controllers/com/smeup/erp/controllers/CustomerInquiryController.java`
- Package: `com.smeup.erp.dtos` → File: `dtos/com/smeup/erp/dtos/CustomerInquiryDTO.java`

**Always:**
1. Determine the full package name (e.g., `com.smeup.erp.services`)
2. Create the corresponding folder structure under `services/`, `controllers/`, or `dtos/`
3. Place the `.java` file in the deepest package folder
4. The folder path must exactly mirror the package declaration

## Output Format
Follow Spring Boot best practices and use provided templates

## Quality Checklist
- [ ] All RPGLE business logic converted
- [ ] Service layer properly implements logic
- [ ] Controller exposes appropriate REST endpoints
- [ ] DTOs properly structure data
- [ ] Error handling comprehensive
- [ ] Logging added appropriately
- [ ] Code follows Spring conventions
- [ ] Dependencies properly injected
- [ ] Conversion notes document complete
