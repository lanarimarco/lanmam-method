# Phase 2: Database - Program: CUST001

# Database Agent

## Purpose
Create JPA entities, repositories, and database access layer based on RPGLE file definitions.

## Inputs Required
1. DDS files from `/source-rpgle/dds/physical-files/`
2. DDS files from `/source-rpgle/dds/logical-files/`
3. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
4. Type mappings guide: `type-mappings.md`
5. Naming conventions: `naming-conventions.md`

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/02-database/`:
1. JPA Entity classes (e.g., `Customer.java`)
2. Repository interfaces (e.g., `CustomerRepository.java`)
3. Custom query methods if needed
4. `database-notes.md` - Documentation of conversion decisions

## Process
1. Read all DDS definitions referenced in the analysis
2. Convert field types using type-mappings.md
3. Identify primary keys and relationships
4. Create JPA entities with proper annotations within `entities` folder
5. Create repository interfaces with custom methods within `repositories` folder
6. Document any schema changes or decisions

## IMPORTANT: Java Package Structure
**Java packages MUST match the folder structure.**

When you declare a package in Java:
```java
package com.example.myapp.entities;
```

The file MUST be placed in the corresponding folder path:
```
entities/com/example/myapp/entities/YourClass.java
```

**Examples:**
- Package: `com.smeup.erp.entities` → File: `entities/com/smeup/erp/entities/Customer.java`
- Package: `com.smeup.erp.repositories` → File: `repositories/com/smeup/erp/repositories/CustomerRepository.java`

**Always:**
1. Determine the full package name (e.g., `com.smeup.erp.entities`)
2. Create the corresponding folder structure under `entities/` or `repositories/`
3. Place the `.java` file in the deepest package folder
4. The folder path must exactly mirror the package declaration

## Output Format
Use templates in this folder

## Quality Checklist
- [ ] All fields from DDS mapped to entity
- [ ] Correct JPA annotations used
- [ ] Primary keys properly defined
- [ ] Column names match DB2 names
- [ ] Relationships properly mapped
- [ ] Repository methods match RPGLE operations
