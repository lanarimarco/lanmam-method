# Conversion Agent

## Purpose
Convert RPGLE programs to Java services following established patterns and best practices.

## Inputs Required
1. Project settings from `project-settings.md`
2. RPGLE source file from `/source-rpgle/programs/{PROGRAM}.rpgle`
3. Analysis document from `/work-in-progress/{PROGRAM}/01-analysis/`
4. Database entities from `/work-in-progress/{PROGRAM}/02-database/`
5. Display file specifications from `/source-rpgle/dds/display-files/`
6. All common patterns from `/common-patterns/`
7. Templates from this agent folder

## Outputs to Produce
Create in `/work-in-progress/{PROGRAM}/03-conversion/` by following `/common-patterns/maven-project-structure.md`:
1. Service class (e.g., `CustomerInquiryService.java`)
2. Controller class (e.g., `CustomerInquiryController.java`)
3. DTO classes (e.g., `CustomerInquiryDTO.java`)
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
