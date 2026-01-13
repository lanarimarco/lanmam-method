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

### CRITICAL: Pre-Check for Existing Artifacts (MANDATORY)
**This check is MANDATORY before any code generation. Failure to perform this check will result in duplicate entities and repository conflicts.**

#### Step 1: Search for Existing Artifacts
Search `/final-output/backend/src/main/java/` for:
1. **Existing Entities** representing the same database tables
2. **Existing Repositories** for the same entities
3. **Existing relationships** between entities (OneToMany, ManyToOne, etc.)

#### Step 2: Analyze Existing Artifacts
For **each artifact found**, analyze:
- **Entity Classes**: 
  - What database table does it map to (`@Table` annotation)?
  - What fields/columns are already defined?
  - What relationships exist with other entities?
  - What JPA annotations and validation rules are used?
- **Repository Interfaces**:
  - What custom query methods are already defined?
  - What naming conventions are used for query methods?
  - Are there any specifications or query DSL implementations?

#### Step 3: Decision Matrix
Based on your analysis, follow this decision matrix:

| Scenario | Action | Example |
|----------|--------|---------|
| **Entity for the SAME table exists** | **EXTEND the existing entity** - Add missing fields/relationships to existing entity | `Customer` entity exists for `CUSTPF` table → Add missing columns like `creditLimit`, `lastOrderDate` |
| **Repository for the entity exists** | **EXTEND the existing repository** - Add new custom query methods | `CustomerRepository` exists → Add methods like `findByRegionAndStatus()`, `findTopCustomersByRevenue()` |
| **Entity has partial fields** | **COMPLETE the existing entity** - Add all missing fields from DDS while preserving existing ones | Existing `Customer` has 5 fields, DDS defines 12 → Add the 7 missing fields |
| **Relationship already exists** | **VERIFY and document** - Ensure relationship is correctly mapped on both sides | `Customer` → `Order` relationship exists → Verify bidirectional mapping is correct |
| **Table with different name but same structure** | **CONSOLIDATE** - Determine if they represent same entity, merge if appropriate | `CUSTPF` and `CUSTOMER` both exist → Analyze and potentially merge |
| **Completely different table** | **CREATE NEW entity and repository** - Only create new classes for truly distinct tables | Converting `PRODUCT` table when only `CUSTOMER` entities exist → Create new Product entity and ProductRepository |

#### Step 4: Document Your Decision
In `database-notes.md`, **ALWAYS document**:
```markdown
## Artifact Reuse Analysis

### Existing Artifacts Found
- Entity: [Name, table name, and path]
- Repository: [Name and path]
- Related Entities: [List entities with relationships]

### Decision Made
- [ ] EXTENDED existing entity: [Entity name]
  - Added fields: [list new fields]
  - Added relationships: [list new relationships]
- [ ] EXTENDED existing repository: [Repository name]
  - Added query methods: [list new methods with signatures]
- [ ] COMPLETED partial entity: [Entity name]
  - Preserved existing fields: [count]
  - Added missing fields: [count and list]
- [ ] CREATED new artifacts (justify why): [explanation]

### Table Mapping Verification
- DDS table name: [table name from DDS]
- Entity @Table annotation: [verified value]
- Conflicts: NONE / [describe any conflicts and resolution]

### Field Mapping Summary
- Total fields in DDS: [count]
- Fields already in existing entity: [count]
- New fields added: [count]
- Fields with special handling: [list with explanations]
```

### Artifacts to Create in Current Workspace
**IMPORTANT**: All artifacts are created in `/work-in-progress/{PROGRAM}/02-database/` following Maven structure as defined in `/common-patterns/maven-project-structure.md`

#### When EXTENDING Existing Artifacts:
**DO NOT modify files in `/final-output/`**. Instead:
1. **COPY** the existing entity/repository from `/final-output/backend/src/main/java/` to current workspace with same Maven structure
2. **EXTEND** the copied version with new fields, relationships, or query methods
3. Document in `database-notes.md` what was copied and what was added

**Files to create in current workspace**:
```
/work-in-progress/{PROGRAM}/02-database/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── smeup/
│   │               └── erp/
│   │                   ├── entities/
│   │                   │   └── [CopiedAndExtendedEntity].java
│   │                   └── repositories/
│   │                       └── [CopiedAndExtendedRepository].java
│   └── test/
│       └── java/
│           └── com/
│               └── smeup/
│                   └── erp/
│                       └── repositories/
│                           └── [Repository]Test.java
├── database-notes.md
└── README.md
```

#### When CREATING New Artifacts (only when justified):
**Files to create in current workspace following Maven structure**:
```
/work-in-progress/{PROGRAM}/02-database/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── smeup/
│   │               └── erp/
│   │                   ├── entities/
│   │                   │   └── [New]Entity.java
│   │                   └── repositories/
│   │                       └── [New]Repository.java
│   └── test/
│       └── java/
│           └── com/
│               └── smeup/
│                   └── erp/
│                       └── repositories/
│                           └── [New]RepositoryTest.java
├── database-notes.md
└── README.md
```

**Critical Rules**:
- ✅ **DO**: Create all artifacts in current workspace following Maven structure
- ✅ **DO**: Copy existing artifacts from `/final-output/` to current workspace as starting point
- ✅ **DO**: Extend the copied versions with new fields/methods
- ❌ **DO NOT**: Modify files in `/final-output/` directly
- ❌ **DO NOT**: Create flat directory structures
- ✅ **DO**: Follow package-to-directory mapping from `/common-patterns/maven-project-structure.md`

## Process
1. **MANDATORY: Perform artifact pre-check** (see "CRITICAL: Pre-Check for Existing Artifacts" section)
   - Search for existing entities and repositories in `/final-output/backend/src/main/java/`
   - Analyze what tables they map to and what fields/methods they have
   - Make extend vs. create decision using the decision matrix
   - Document your decision in `database-notes.md`
2. **If extending existing artifacts**:
   - **COPY** the existing entity/repository from `/final-output/backend/src/main/java/` to current workspace
   - Maintain the same Maven directory structure in current workspace
   - Use copied files as starting point for extensions
3. Read all DDS definitions referenced in the analysis
4. Convert field types using `type-mappings.md` guide
5. Identify primary keys, foreign keys, and relationships
6. **If extending existing entity**: Add missing fields, relationships, and annotations to the COPIED entity in current workspace
   **If creating new entity**: Create complete JPA entity with proper annotations following Maven structure
7. **If extending existing repository**: Add new custom query methods to the COPIED repository in current workspace
   **If creating new repository**: Create repository interface with necessary methods following Maven structure
8. Ensure column names match DB2 physical column names
9. Add proper validation annotations where appropriate
10. Document all decisions, extensions, and field mappings in `database-notes.md`
11. **Document any issues encountered** - If you face significant challenges, unclear DDS definitions, or make important mapping decisions, document them clearly in `database-notes.md` for the project manager to review


## Output Format
- **MUST follow Maven directory structure** as defined in `/common-patterns/maven-project-structure.md`
- Use provided templates as reference (`entity-template.java`, `repository-template.java`)
- All Java files MUST be in proper package directories (`src/main/java/com/smeup/erp/...`)
- All test files MUST be in test directories (`src/test/java/com/smeup/erp/...`)
- Follow Spring Data JPA best practices
- Use standard JPA annotations (@Entity, @Table, @Column, @Id, etc.)

## Quality Checklist
- [ ] **CRITICAL: Artifact pre-check completed and documented**
- [ ] **CRITICAL: No duplicate entities created for the same table**
- [ ] **CRITICAL: @Table annotations verified against DDS table names**
- [ ] **CRITICAL: All files follow Maven directory structure** (`/common-patterns/maven-project-structure.md`)
- [ ] **CRITICAL: Package declarations match directory paths**
- [ ] **CRITICAL: Existing artifacts copied to current workspace** (not modified in `/final-output/`)
- [ ] Decision to extend vs. create documented in `database-notes.md`
- [ ] All fields from DDS mapped to entity
- [ ] Correct JPA annotations used (@Entity, @Table, @Column, @Id, etc.)
- [ ] Primary keys properly defined with @Id and appropriate generation strategy
- [ ] Foreign keys and relationships properly mapped (@OneToMany, @ManyToOne, @JoinColumn)
- [ ] Column names match DB2 physical names (verified against DDS)
- [ ] Field types correctly mapped using `type-mappings.md`
- [ ] Validation annotations added where appropriate (@NotNull, @Size, etc.)
- [ ] Repository methods match RPGLE data access operations
- [ ] Custom query methods properly defined with @Query if needed
- [ ] Test files in `src/test/java/` with matching package structure
- [ ] Bidirectional relationships correctly mapped on both entities
- [ ] Cascade operations appropriate for relationships
- [ ] Database notes document complete with artifact reuse analysis and field mapping summary
