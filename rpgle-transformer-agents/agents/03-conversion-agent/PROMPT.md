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
Create in `/work-in-progress/{PROGRAM}/03-conversion/` by following `/common-patterns/maven-project-structure.md`:
1. Service class (e.g., `CustomerInquiryService.java`)
2. Controller class (e.g., `CustomerInquiryController.java`)
3. DTO classes (e.g., `CustomerInquiryDTO.java`)
4. Exception classes if needed
5. `conversion-notes.md` - Explain all decisions
6. `README.md` - Overview, setup, usage instructions for review and integration agent



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
