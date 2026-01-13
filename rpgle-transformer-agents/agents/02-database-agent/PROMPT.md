# Database Agent

## Workspace Context
**Current workspace**: `/work-in-progress/{PROGRAM}/02-database/`

All relative file references in this prompt are relative to the current workspace directory.

**Path interpretation examples**:
- `./PROMPT.md` → Current workspace's PROMPT file
- `../01-analysis/` → Sibling directory in the {PROGRAM} folder
- `../../source-rpgle/` → Source RPGLE files at the repository root level
- `/work-in-progress/{PROGRAM}/02-database/database-notes.md` → Output file in current workspace

## Purpose
Create JPA entities, repositories, and database access layer based on RPGLE file definitions.

## Inputs Required

### Essential Inputs (Must Have)
1. **Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/analysis.md`** - Context and requirements
   - If missing: Cannot proceed. This is a blocker.
2. **DDS files from `/source-rpgle/dds/physical-files/`** - Core database definitions
   - If missing: Cannot proceed without at least one DDS file. Document this blocker.

### Recommended Inputs (Should Have)
3. **Project settings from `project-settings.md`** - Java package structure, naming
   - If missing: Use reasonable defaults (e.g., `com.example.entities`, `com.example.repositories`)
4. **Type mappings guide: `type-mappings.md`** - DDS to Java type conversions
   - If missing: Use standard DB2/RPGLE to Java mappings (CHAR→String, PACKED→BigDecimal, etc.)

### Optional Inputs (Nice to Have)
5. **DDS files from `/source-rpgle/dds/logical-files/`** - Indexes and views
   - If missing: Skip logical file processing, note in documentation
6. **Naming conventions: `naming-conventions.md`** - Code style guidelines
   - If missing: Use standard Java naming (camelCase for fields, PascalCase for classes)
7. **Common patterns from `/common-patterns/`** - Best practices and examples
   - If missing: Use standard JPA patterns
8. **Templates from this agent folder** - Code templates
   - If missing: Generate code using standard JPA/Spring Data patterns

### Handling Missing Inputs
- **If one or more Essential Inputs (Must Have) are missing**: The only output to be produced must be `database-notes.md` documenting the missing inputs and blocking the conversion
- Always document which inputs were missing in `database-notes.md`
- Document any assumptions made due to missing inputs
- Proceed with available information - do not block on optional inputs
- Use reasonable defaults and standard practices when guidelines are missing

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/02-database/` by following `/common-patterns/maven-project-structure.md`:
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
