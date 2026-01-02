# Documentation Checklist for RPGLE Transformations

**Version**: 1.0  
**Last Updated**: 2026-01-02  
**Purpose**: Quality checklist for documentation during RPGLE-to-modern transformations

---

## Overview

This checklist ensures consistent, high-quality documentation across all transformation stories. Use this checklist as part of the **Definition of Done** for each story involving code implementation.

---

## When to Use This Checklist

✅ **Use this checklist when**:
- Implementing a new story that creates backend code
- Implementing a new story that creates frontend code
- Refactoring existing code to improve documentation
- During code review before marking a story as "done"

❌ **Skip this checklist when**:
- Working on pure configuration (no code)
- Writing documentation-only stories
- Quick bug fixes (unless adding new code)

---

## Documentation Checklist

### 1. Backend (Java/Spring Boot) Documentation

#### Entity Classes
- [ ] Class-level JavaDoc includes DDS file reference
- [ ] Class-level JavaDoc lists all DDS field mappings
- [ ] Each field has JavaDoc with DDS field name and type
- [ ] Data type transformations are explained (e.g., Packed Decimal → BigDecimal)
- [ ] Primary key fields explain ID assignment strategy
- [ ] Validation annotations have meaningful messages
- [ ] Special handling (nullable, date formats) is documented
- [ ] No unnecessary JavaDoc on standard getters/setters
- [ ] `toString()`, `equals()`, `hashCode()` do not have JavaDoc (unless custom logic)

#### Repository Interfaces
- [ ] Class-level JavaDoc maps RPGLE operations to Spring Data JPA
- [ ] Each custom query method has JavaDoc with RPGLE operation reference
- [ ] Method JavaDoc explains indicators (`%Found` → `isPresent()`)
- [ ] Return types (Optional, List, etc.) are documented
- [ ] No JavaDoc on standard JpaRepository methods (inherited)

#### Service Classes
- [ ] Class-level JavaDoc explains business purpose
- [ ] Class-level JavaDoc references RPGLE program
- [ ] Each public method has JavaDoc with RPGLE subroutine reference
- [ ] Business logic transformations are explained in JavaDoc
- [ ] Inline comments map complex logic to RPGLE code sections
- [ ] Transaction boundaries (`@Transactional`) have justification comments if non-obvious
- [ ] Validation logic references original RPGLE validation code
- [ ] Error handling maps to RPGLE indicators

#### Controller Classes
- [ ] Class-level JavaDoc explains API purpose and RPGLE program reference
- [ ] Class-level JavaDoc lists all endpoints
- [ ] Each endpoint method has JavaDoc with:
  - [ ] RPGLE workflow description
  - [ ] HTTP method and path
  - [ ] Request/response structure
  - [ ] Status codes and exceptions
  - [ ] Transformation notes (green-screen → REST API)

#### DTOs
- [ ] Class-level JavaDoc explains DTO purpose
- [ ] Class-level JavaDoc references DDS file
- [ ] Each field has JavaDoc with DDS field name and type
- [ ] Record classes (Java 16+) use concise field JavaDoc

#### Exception Classes
- [ ] Class-level JavaDoc explains when exception is thrown
- [ ] Maps to RPGLE error indicator if applicable

### 2. Frontend (React/TypeScript) Documentation

#### Component Files
- [ ] File-level JSDoc comment at top of file
- [ ] File-level comment includes DDS record format reference
- [ ] File-level comment includes RPGLE program reference
- [ ] File-level comment includes usage example
- [ ] Component function has JSDoc with description
- [ ] Component JSDoc explains DDS mapping
- [ ] Component JSDoc includes usage example
- [ ] Props interface is documented
- [ ] Each prop has JSDoc with purpose and DDS field reference (if applicable)
- [ ] Callback props explain what parameters are passed
- [ ] Optional props document default values
- [ ] Inline comments map JSX to DDS display fields
- [ ] Function key mappings are documented (F3 → ESC, etc.)
- [ ] State variables reference RPGLE equivalents in comments
- [ ] No comments on obvious React patterns

#### Type Definition Files
- [ ] File-level JSDoc explains type definitions purpose
- [ ] File-level comment references DDS file
- [ ] Each type has JSDoc with DDS record format reference
- [ ] Each type field has JSDoc with DDS field name and type
- [ ] Zod schemas have JSDoc explaining DDS validation rules
- [ ] Complex types explain transformation rationale

#### Custom Hooks
- [ ] Hook function has JSDoc with description
- [ ] Hook JSDoc maps to RPGLE operation (CHAIN, READ, etc.)
- [ ] Hook JSDoc explains API endpoint used
- [ ] Hook JSDoc includes usage example
- [ ] Hook parameters are documented
- [ ] Return value structure is explained
- [ ] Query behavior (enabled, retry, caching) references RPGLE logic

#### API Client Modules
- [ ] File-level JSDoc explains API client purpose
- [ ] Each API function has JSDoc with:
  - [ ] RPGLE operation reference
  - [ ] HTTP method and endpoint
  - [ ] Request/response types
  - [ ] Error handling
- [ ] Data transformation functions explain DDS-to-API mapping

#### Page Components
- [ ] File-level JSDoc explains complete workflow
- [ ] File-level comment maps RPGLE program flow to React components
- [ ] State management strategy is documented
- [ ] Component integration is explained (how child components interact)
- [ ] RPGLE screen formats mapped to React components

### 3. Business Logic Mapping Documentation

- [ ] Business logic mapping document exists for each RPGLE program
- [ ] Document follows [business-logic-mapping-template.md](business-logic-mapping-template.md)
- [ ] All sections of template are completed
- [ ] Original RPGLE code snippets are included
- [ ] Data transformations table is complete
- [ ] Validation rules are documented (RPGLE → Java → TypeScript)
- [ ] File operations are mapped
- [ ] Display logic is mapped
- [ ] Edge cases and exceptions are documented
- [ ] Known differences are listed
- [ ] Future enhancements are identified

### 4. General Documentation Quality

#### Code Comments
- [ ] Comments explain "why", not "what" (code explains "what")
- [ ] Comments are concise and clear
- [ ] Comments are properly formatted (capitalization, punctuation)
- [ ] No commented-out code (use version control instead)
- [ ] No TODO comments without issue tracking reference
- [ ] No profanity or unprofessional language

#### JavaDoc/JSDoc Quality
- [ ] Uses proper HTML tags (`<p>`, `<ul>`, `<li>`, etc.)
- [ ] Uses `@param`, `@return`, `@throws` appropriately
- [ ] Uses `@example` for complex usage
- [ ] Uses `@see` for related classes/methods
- [ ] Links to related documentation where helpful
- [ ] No typos or grammatical errors
- [ ] Consistent formatting and style

#### AI-Friendly Documentation
- [ ] DDS field mappings are explicit and complete
- [ ] RPGLE line numbers are referenced where helpful
- [ ] Before/after transformation examples are included
- [ ] Edge cases are explicitly documented
- [ ] Validation rules are stated clearly
- [ ] Consistent terminology used throughout

### 5. Linting and Validation

#### Backend (Java)
- [ ] Checkstyle passes (no JavaDoc warnings)
- [ ] No missing JavaDoc on public APIs
- [ ] JavaDoc HTML is well-formed
- [ ] IDE (IntelliJ) shows no documentation warnings

#### Frontend (TypeScript)
- [ ] ESLint passes (no JSDoc warnings)
- [ ] No missing JSDoc on exported functions/components
- [ ] TSDoc is valid and well-formed
- [ ] IDE (VS Code) shows no documentation warnings

### 6. Definition of Done - Documentation

#### Story-Level
- [ ] All acceptance criteria are met
- [ ] All tasks are completed and documented
- [ ] Code passes all quality checks
- [ ] Documentation standards are followed
- [ ] This checklist is completed
- [ ] Code review confirms documentation quality

#### Epic-Level
- [ ] All stories in epic have complete documentation
- [ ] Business logic mapping document is complete
- [ ] Transformation analysis document exists
- [ ] Developer notes capture learnings and challenges
- [ ] Retrospective includes documentation quality feedback

---

## Quick Reference: What to Document

### Always Document
✅ DDS field origins  
✅ RPGLE operation mappings  
✅ Business logic transformations  
✅ Validation rules  
✅ API contracts  
✅ Non-obvious design decisions  
✅ Edge cases and exceptions  

### Never Document
❌ Obvious getters/setters  
❌ Self-explanatory code  
❌ Standard framework patterns  
❌ Obvious variable names  
❌ Standard JPA/React boilerplate  

---

## AI Agent Prompt Template

### When to Use This Checklist

**During Development (dev-story workflow)**:
- Use checklist sections as you implement code
- Apply standards incrementally, not all at once at the end
- Reference specific checklist sections for each component type

**During Code Review (code-review workflow)**:
- Use full checklist to validate completed implementation
- Run linting tools to verify documentation quality
- Check cross-references and examples

### AI Agent Prompt Template

When using this checklist with AI agents during **code review**, use this prompt:

```
I am reviewing [Story Name]. Review the code against the Documentation Checklist at docs/standards/documentation-checklist.md.

Agent Context: code-review workflow (adversarial validation mode)

Specifically check:
1. All backend classes have proper JavaDoc with DDS/RPGLE references
2. All frontend components have JSDoc with DDS record format references
3. Inline comments map to RPGLE logic where applicable
4. No unnecessary documentation on obvious code
5. All public APIs are documented
6. Linting passes with no documentation warnings
7. Cross-references between documentation files are valid
8. Examples match actual code structure

Report any missing or incomplete documentation and suggest improvements.
```

When using during **development (dev-story workflow)**, use this prompt:

```
I am implementing [Story Name]. Apply documentation standards from docs/standards/ as I write code.

For each component I create:
- Backend Entity: Follow java-documentation-standards.md Entity section
- Backend Service: Follow java-documentation-standards.md Service section
- Frontend Component: Follow typescript-documentation-standards.md Component section

Reference the documentation-checklist.md Definition of Done before marking tasks complete.
```

---

## Examples

### Good Documentation Example

```java
/**
 * Customer entity mapped from DDS Physical File CUSTMAST.dds
 * 
 * <p>Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 * <p>Table: CUSTMAST
 * <p>Record Format: CUSTREC
 * 
 * <p>DDS Field Mapping:
 * <ul>
 *   <li>CUSTNO (5P 0) → customerId (Long)</li>
 *   <li>CUSTNAME (30A) → customerName (String)</li>
 * </ul>
 */
@Entity
@Table(name = "CUSTMAST")
public class Customer implements Serializable {

    /**
     * Customer Number - Primary Key
     * <p>DDS Field: CUSTNO (5P 0)
     * <p>Note: No @GeneratedValue - IDs are manually assigned to preserve
     * original AS/400 customer numbers during migration.
     */
    @Id
    @Column(name = "CUSTNO", nullable = false)
    private Long customerId;

    // ... rest of class
}
```

### Bad Documentation Example

```java
// BAD - Missing DDS references, no field mappings
/**
 * Customer class
 */
@Entity
@Table(name = "CUSTMAST")
public class Customer implements Serializable {

    /**
     * Gets the customer ID
     * @return the customer ID
     */
    public Long getCustomerId() {  // BAD - Unnecessary getter documentation
        return customerId;
    }

    // BAD - No documentation on primary key strategy
    @Id
    private Long customerId;
}
```

---

## Related Documents

- [Java Documentation Standards](java-documentation-standards.md)
- [TypeScript Documentation Standards](typescript-documentation-standards.md)
- [Business Logic Mapping Template](business-logic-mapping-template.md)

---

## Checklist Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-01-02 | Initial version |
