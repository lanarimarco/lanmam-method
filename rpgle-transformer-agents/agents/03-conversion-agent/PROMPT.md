# Conversion Agent

## Workspace Context
**Current workspace**: `/work-in-progress/{PROGRAM}/03-conversion/`

All relative file references in this prompt are relative to the current workspace directory.

**Path interpretation examples**:
- `./PROMPT.md` → Current workspace's PROMPT file
- `../01-analysis/` → Sibling directory in the {PROGRAM} folder
- `../02-database/` → Database layer from previous phase
- `../../source-rpgle/` → Source RPGLE files at the repository root level
- `/work-in-progress/{PROGRAM}/03-conversion/conversion-notes.md` → Output file in current workspace

## Purpose
Convert RPGLE programs to Java services following established patterns and best practices.

## Inputs Required

### Essential Inputs (Must Have)
1. **RPGLE source file from `/source-rpgle/programs/{PROGRAM}.rpgle`** - Source code to convert
   - If missing: Cannot proceed. This is a blocker.
2. **Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/analysis.md`** - Business logic context
   - If missing: Cannot proceed without understanding program purpose and logic flow. This is a blocker.
3. **Database entities from `/work-in-progress/{PROGRAM}/02-database/`** - JPA entities and repositories
   - If missing: Cannot implement data access layer. This is a blocker.

### Recommended Inputs (Should Have)
4. **Project settings from `project-settings.md`** - Package structure, naming conventions
   - If missing: Use reasonable defaults (e.g., `com.example.services`, `com.example.controllers`)
5. **Common patterns from `/common-patterns/`** - Best practices and mapping guides
   - If missing: Use standard Spring Boot patterns, document deviations
6. **Display file specifications from `/source-rpgle/dds/display-files/`** - UI interaction context
   - If missing: Create generic DTOs, note UI integration needed

### Optional Inputs (Nice to Have)
7. **Templates from this agent folder** - Code templates
   - If missing: Generate code using standard Spring Boot patterns

### Handling Missing Inputs
- **If one or more Essential Inputs (Must Have) are missing**: The only output to be produced must be `conversion-notes.md` documenting the missing inputs and blocking the conversion
- Always document which inputs were missing in `conversion-notes.md`
- Document any assumptions made due to missing inputs
- Proceed with available information - do not block on optional inputs
- Use reasonable defaults and standard practices when guidelines are missing
- Flag areas needing review when essential context is missing

## Outputs to Produce

### CRITICAL: Pre-Check for Existing Artifacts (MANDATORY)
**This check is MANDATORY before any code generation. Failure to perform this check will result in duplicate code and conflicts.**

#### Step 1: Search for Existing Artifacts
Search `/final-output/backend/src/main/java/` for:
1. **Existing Services** handling similar business logic or entities
2. **Existing Controllers** with similar REST endpoints
3. **Existing DTOs** for the same entities

#### Step 2: Analyze Existing Artifacts
For **each artifact found**, analyze:
- **Service Classes**: 
  - What business operations do they already implement?
  - Do they handle the same entity/domain as the current program?
  - Can new functionality be added to the existing service?
- **Controller Classes**:
  - What `@RequestMapping` paths are already in use?
  - What endpoints (`@GetMapping`, `@PostMapping`, etc.) exist?
  - Do they serve similar REST operations?
- **DTO Classes**:
  - What data structures are already defined?
  - Can they be reused or extended?

#### Step 3: Decision Matrix
Based on your analysis, follow this decision matrix:

| Scenario | Action | Example |
|----------|--------|---------|
| **Existing service handles the SAME entity** | **EXTEND the existing service** - Add new methods to the existing service class | `CustomerInquiryService` already exists → Add methods like `updateCustomer()`, `deleteCustomer()` |
| **Existing controller has related endpoints** | **EXTEND the existing controller** - Add new endpoints to the existing controller, ensuring unique paths | `CustomerInquiryController` exists with `/api/customers` → Add new endpoints like `/api/customers/{id}/addresses` |
| **Existing DTO represents the SAME entity** | **REUSE or extend the DTO** - Use existing DTO or create a specialized variant if needed | `CustomerDTO` exists → Reuse it, or create `CustomerDetailDTO extends CustomerDTO` |
| **@RequestMapping conflict detected** | **DO NOT CREATE NEW CONTROLLER** - Add endpoints to existing controller with unique sub-paths | Existing `/api/customers` → Add `/api/customers/search`, `/api/customers/validate`, etc. |
| **Similar business logic exists** | **CONSOLIDATE into existing service** - Refactor to combine similar operations | `CustomerSearchService` + new search logic → Merge into single service |
| **Completely different domain/entity** | **CREATE NEW artifacts** - Only create new classes for truly distinct functionality | Converting `PRODUCT` program when only `CUSTOMER` artifacts exist → Create new ProductService, ProductController |

#### Step 4: Document Your Decision
In `conversion-notes.md`, **ALWAYS document**:
```markdown
## Artifact Reuse Analysis

### Existing Artifacts Found
- Service: [Name and path]
- Controller: [Name and path]  
- DTOs: [Names and paths]

### Decision Made
- [ ] EXTENDED existing service: [Service name]
  - Added methods: [list new methods]
- [ ] EXTENDED existing controller: [Controller name]
  - Added endpoints: [list new endpoints with paths]
- [ ] REUSED existing DTOs: [DTO names]
- [ ] CREATED new artifacts (justify why): [explanation]

### @RequestMapping Verification
- Existing mappings checked: [list existing paths]
- New mappings: [list new paths]
- Conflicts: NONE / [describe any conflicts and resolution]
```

### Artifacts to Create in Current Workspace
**IMPORTANT**: All artifacts are created in `/work-in-progress/{PROGRAM}/03-conversion/` following Maven structure as defined in `/common-patterns/maven-project-structure.md`

#### When EXTENDING Existing Artifacts:
**DO NOT modify files in `/final-output/`**. Instead:
1. **COPY** the existing artifact from `/final-output/backend/src/main/java/` to current workspace with same Maven structure
2. **EXTEND** the copied version with new methods/endpoints
3. Document in `conversion-notes.md` what was copied and what was added

**Files to create in current workspace**:
```
/work-in-progress/{PROGRAM}/03-conversion/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── smeup/
│   │               └── erp/
│   │                   ├── controllers/
│   │                   │   └── [CopiedAndExtendedController].java
│   │                   ├── services/
│   │                   │   └── [CopiedAndExtendedService].java
│   │                   ├── dtos/
│   │                   │   └── [NewOrCopiedDTO].java
│   │                   └── exceptions/
│   │                       └── [NewException].java (if needed)
│   └── test/
│       └── java/
│           └── com/
│               └── smeup/
│                   └── erp/
│                       └── services/
│                           └── [Service]Test.java
├── conversion-notes.md
└── README.md
```

#### When CREATING New Artifacts (only when justified):
**Files to create in current workspace following Maven structure**:
```
/work-in-progress/{PROGRAM}/03-conversion/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── smeup/
│   │               └── erp/
│   │                   ├── controllers/
│   │                   │   └── [New]Controller.java
│   │                   ├── services/
│   │                   │   └── [New]Service.java
│   │                   ├── dtos/
│   │                   │   └── [New]DTO.java
│   │                   └── exceptions/
│   │                       └── [New]Exception.java (if needed)
│   └── test/
│       └── java/
│           └── com/
│               └── smeup/
│                   └── erp/
│                       └── services/
│                           └── [New]ServiceTest.java
├── conversion-notes.md
└── README.md
```

**Critical Rules**:
- ✅ **DO**: Create all artifacts in current workspace following Maven structure
- ✅ **DO**: Copy existing artifacts from `/final-output/` to current workspace as starting point
- ✅ **DO**: Extend the copied versions with new functionality
- ❌ **DO NOT**: Modify files in `/final-output/` directly
- ❌ **DO NOT**: Create flat directory structures
- ✅ **DO**: Follow package-to-directory mapping from `/common-patterns/maven-project-structure.md`



## Process
1. **MANDATORY: Perform artifact pre-check** (see "CRITICAL: Pre-Check for Existing Artifacts" section)
   - Search for existing services, controllers, and DTOs in `/final-output/backend/src/main/java/`
   - Analyze what they already implement
   - Make extend vs. create decision using the decision matrix
   - Document your decision in `conversion-notes.md`
2. **If extending existing artifacts**:
   - **COPY** the existing files from `/final-output/backend/src/main/java/` to current workspace
   - Maintain the same Maven directory structure in current workspace
   - Use copied files as starting point for extensions
3. Review analysis document thoroughly
4. Study the RPGLE logic flow
5. Map RPG operations to Java/Spring patterns (use `/common-patterns/rpg-operations-mapping.md`)
6. **If extending existing service**: Add new business logic methods to the COPIED service in current workspace
   **If creating new service**: Implement business logic in new service following Maven structure
7. **If extending existing controller**: Add new REST endpoints to the COPIED controller in current workspace
   **If creating new controller**: Create REST API with UNIQUE `@RequestMapping` following Maven structure
8. Define or reuse DTOs for data transfer (following Maven structure)
9. Add proper error handling
10. Add logging at key points
11. Document all decisions, extensions, and any deviations in `conversion-notes.md`
12. **Document any issues encountered** - If you face significant challenges, unclear requirements, or make important decisions, document them clearly in `conversion-notes.md` for the project manager to review

## Output Format
- **MUST follow Maven directory structure** as defined in `/common-patterns/maven-project-structure.md`
- Follow Spring Boot best practices
- Use provided templates as reference
- All Java files MUST be in proper package directories (`src/main/java/com/smeup/erp/...`)
- All test files MUST be in test directories (`src/test/java/com/smeup/erp/...`)

## Quality Checklist
- [ ] **CRITICAL: Artifact pre-check completed and documented**
- [ ] **CRITICAL: No duplicate services created for the same entity**
- [ ] **CRITICAL: No @RequestMapping conflicts in controllers**
- [ ] **CRITICAL: All files follow Maven directory structure** (`/common-patterns/maven-project-structure.md`)
- [ ] **CRITICAL: Package declarations match directory paths**
- [ ] **CRITICAL: Existing artifacts copied to current workspace** (not modified in `/final-output/`)
- [ ] Decision to extend vs. create documented in `conversion-notes.md`
- [ ] All RPGLE business logic converted
- [ ] Service layer properly implements logic (new or extended, in Maven structure)
- [ ] Controller exposes appropriate REST endpoints with unique paths (in Maven structure)
- [ ] DTOs properly structure data (reused or new, in Maven structure)
- [ ] Test files in `src/test/java/` with matching package structure
- [ ] Error handling comprehensive
- [ ] Logging added appropriately
- [ ] Code follows Spring conventions
- [ ] Dependencies properly injected
- [ ] Conversion notes document complete with artifact reuse analysis
