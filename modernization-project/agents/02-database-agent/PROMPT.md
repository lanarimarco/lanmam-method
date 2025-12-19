# Database Agent

## Purpose
Create JPA entities, repositories, and database access layer based on RPGLE file definitions.

## Inputs Required
1. Project settings from `project-settings.md`
2. DDS files from `/source-rpgle/dds/physical-files/`
3. DDS files from `/source-rpgle/dds/logical-files/`
4. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
5. Type mappings guide: `type-mappings.md`
6. Naming conventions: `naming-conventions.md`
7. All common patterns from `/common-patterns/`
8. Templates from this agent folder

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/02-database/` by following `/common-patterns/maven-project-structure.md`
1. JPA Entity classes (e.g., `Customer.java`)
2. Repository interfaces (e.g., `CustomerRepository.java`)
3. Custom query methods if needed
4. `database-notes.md` - Documentation of conversion decisions

## Process
1. Read all DDS definitions referenced in the analysis
2. Convert field types using type-mappings.md
3. Identify primary keys and relationships
4. Create JPA entities with proper annotations within `entities` subpackages
5. Create repository interfaces with custom methods within `repositories` subpackages
6. Document any schema changes or decisions


## Output Format
Use templates in this folder

## Quality Checklist
- [ ] All fields from DDS mapped to entity
- [ ] Correct JPA annotations used
- [ ] Primary keys properly defined
- [ ] Column names match DB2 names
- [ ] Relationships properly mapped
- [ ] Repository methods match RPGLE operations
