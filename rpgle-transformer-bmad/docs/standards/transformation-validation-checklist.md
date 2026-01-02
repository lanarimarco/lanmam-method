# Transformation Validation Checklist

**Version:** 1.0  
**Last Updated:** 2026-01-02  
**Purpose:** Definition of Done checklist for RPGLE transformations - ensures quality, functional equivalence, and completeness

---

## 📋 How to Use This Checklist

**When to Use:**
- ✅ Before marking transformation story as "done"
- ✅ Before creating a Pull Request
- ✅ During code review (reviewer validates all items)
- ✅ As final quality gate before merging

**Estimated Time:** 15-30 minutes for full validation

**Instructions:**
1. Copy this checklist into your PR description or story file
2. Work through each category systematically
3. Check `[x]` only when item is fully validated
4. If ANY item fails, fix before proceeding
5. Include validation summary in PR

---

## ✅ Validation Checklist

### **Category 1: Compilation & Build Validation**

Verify all code compiles without errors or warnings.

- [ ] **Backend compiles successfully**
  ```bash
  cd backend && mvn clean compile
  # Expected: BUILD SUCCESS, 0 errors
  ```

- [ ] **Frontend compiles successfully**
  ```bash
  cd frontend && npm run build
  # Expected: ✓ built in <n>s, 0 errors
  ```

- [ ] **No compilation errors or warnings**

- [ ] **All dependencies resolved**

**Quick Test:**
```bash
cd backend && mvn clean compile && cd ..
cd frontend && npm run build && cd ..
```

---

### **Category 2: Testing Validation**

Ensure comprehensive test coverage and all tests passing.

- [ ] **All backend unit tests pass**
  ```bash
  cd backend && mvn test
  # Expected: Tests run: N, Failures: 0, Errors: 0
  ```

- [ ] **All frontend unit tests pass**
  ```bash
  cd frontend && npm test
  # Expected: Test Files: N passed (N)
  ```

- [ ] **All backend integration tests pass** (Testcontainers)
  ```bash
  cd backend && mvn verify -Pintegration-tests
  ```

- [ ] **All E2E tests pass** (Playwright)
  ```bash
  cd frontend && npx playwright test
  ```

- [ ] **Code coverage ≥ 80%** (backend and frontend)
  ```bash
  # Backend: mvn clean test jacoco:report
  # Frontend: npm test -- --coverage
  ```

- [ ] **No flaky tests** (run suite 2-3 times to verify)

**Quick Test:**
```bash
cd backend && mvn clean verify && cd ..
cd frontend && npm test && npx playwright test && cd ..
```

---

### **Category 3: Code Quality & Linting Validation**

Ensure code meets quality standards and style guidelines.

- [ ] **Backend passes Checkstyle**
  ```bash
  cd backend && mvn checkstyle:check
  ```

- [ ] **Backend passes PMD static analysis**
  ```bash
  cd backend && mvn pmd:check
  ```

- [ ] **Frontend passes ESLint**
  ```bash
  cd frontend && npm run lint
  ```

- [ ] **Frontend passes Prettier formatting**
  ```bash
  cd frontend && npm run format:check
  ```

- [ ] **No TypeScript `any` types in new code**
  ```bash
  grep -r ": any" frontend/src/features/<module>/
  # Should return 0 results
  ```

- [ ] **Cyclomatic complexity ≤ 10 for all methods**

**Zero Critical Violations Required**

**Quick Test:**
```bash
cd backend && mvn checkstyle:check pmd:check && cd ..
cd frontend && npm run lint && npm run format:check && cd ..
```

---

### **Category 4: Documentation Validation**

Verify comprehensive inline documentation with RPGLE traceability.

- [ ] **All Java classes have JavaDoc** (per [java-documentation-standards.md](java-documentation-standards.md))
  - Entities: DDS physical file reference
  - Services: RPGLE program + business logic mapping
  - Controllers: Original RPGLE program reference
  - DTOs: DDS display file or purpose

- [ ] **All React components have JSDoc** (per [typescript-documentation-standards.md](typescript-documentation-standards.md))
  - Components: DDS display file + record format
  - Hooks: Original RPGLE interaction pattern
  - Types: DDS field mapping

- [ ] **Inline comments reference original DDS/RPGLE sources**
  ```bash
  grep -r "Source: source-rpgle" backend/src/main/java/
  grep -r "Source: source-rpgle" frontend/src/features/
  ```

- [ ] **Business logic transformations documented**

- [ ] **Transformation analysis document created**
  - Location: `docs/transformations/<PROGRAM>/analysis.md`

- [ ] **Business logic mapping document created**
  - Location: `docs/transformations/<PROGRAM>/business-logic-mapping.md`

- [ ] **Data mapping document created** (DDS → JPA → React)
  - Location: `docs/transformations/<PROGRAM>/data-mapping.md`

**Reference:** CUST001 docs at `docs/transformations/CUST001/`

---

### **Category 5: Functional Equivalence Validation** ⚠️ **CRITICAL - NFR1**

Ensure 100% functional equivalence with original RPGLE behavior.

- [ ] **Functional equivalence tests pass with test data**
  ```bash
  cd backend && mvn test -Dtest=FunctionalEquivalenceTest
  ```

- [ ] **Manual testing of key workflows completed**
  - Document test results and compare with RPGLE output

- [ ] **Edge cases identified and tested**
  - Empty inputs
  - Maximum length inputs
  - Boundary values
  - Special characters
  - Null/missing data

- [ ] **Error handling matches original RPGLE behavior**

- [ ] **Validation rules match original DDS/RPGLE constraints**
  - Field length limits
  - Required field validation
  - Data type validation
  - Range checks
  - Cross-field validation

- [ ] **Data transformations preserve original semantics**
  - Calculations produce same results
  - Decimal precision matches
  - Date/time conversions correct

- [ ] **UI behavior matches original green-screen intent**

**NFR1 Requirement:** 100% functional equivalence is mandatory

---

### **Category 6: Architectural Compliance Validation**

Ensure code follows project architectural patterns and conventions.

- [ ] **Code follows project naming conventions**
  - Packages: `com.smeup.backend.<module>`
  - Classes: PascalCase
  - Methods: camelCase
  - Files: kebab-case for React

- [ ] **Database schema uses DDS file names for tables**
  - Example: `@Table(name = "CUSTMAST")`

- [ ] **JPA entities use `@Table(name="DDS_FILE")` pattern**
  ```java
  @Entity
  @Table(name = "CUSTMAST")
  public class Customer { ... }
  ```

- [ ] **JPA fields use `@Column(name="DDS_FIELD")` pattern**
  ```java
  @Column(name = "CUSTID")
  private Long customerId;
  ```

- [ ] **API endpoints follow REST conventions**
  - Plural resource names: `/api/customers`
  - Proper HTTP methods
  - Query params for search

- [ ] **React components follow feature module structure**
  ```
  frontend/src/features/<module>/
  ├── <Domain>Search.tsx
  ├── <Domain>Detail.tsx
  ├── <Domain>Inquiry.tsx
  ├── use<Domain>.ts
  └── <domain>.types.ts
  ```

- [ ] **Error responses use RFC 7807 Problem Details format**

**Reference:** `_bmad-output/planning-artifacts/architecture.md`

---

### **Category 7: Traceability & Project Cleanliness**

Ensure code is traceable to original sources and repository is clean.

- [ ] **All generated files reference original RPGLE/DDS sources**
  ```bash
  find backend/src/main/java -name "*.java" -exec grep -l "Source: source-rpgle" {} \;
  find frontend/src/features -name "*.tsx" -exec grep -l "Source: source-rpgle" {} \;
  ```

- [ ] **Commit messages reference story number** (e.g., "5-6")

- [ ] **Git history is clean**
  - No merge conflicts
  - No debug commits
  - No "WIP" or "temp" commits

- [ ] **Files are in correct locations per project structure**

- [ ] **No leftover TODO or FIXME comments without issues**
  ```bash
  grep -rn "TODO\|FIXME" backend/src/main/java/ frontend/src/features/
  ```

---

### **Category 8: PR Review & Integration Readiness**

Validate story is ready for code review and integration.

- [ ] **All tasks/subtasks in story file marked complete** `[x]`

- [ ] **Story file updated with completion details**
  - Dev Agent Record → Implementation Notes
  - File List → All modified files
  - Change Log → Summary of changes

- [ ] **Sprint status updated** (if using sprint tracking)

- [ ] **All acceptance criteria satisfied**

- [ ] **Full build succeeds (backend + frontend)**
  ```bash
  cd backend && mvn clean install && cd ..
  cd frontend && npm run build && cd ..
  ```

---

## 📊 Validation Summary Template

**Copy this template into your PR description:**

```markdown
## Transformation Validation Summary

**Story:** <story-key> - <story-title>  
**Program:** <RPGLE-program-name>  
**Validation Date:** <date>  
**Validated By:** <your-name>

### ✅ Validation Results

- ✅ **Compilation & Build:** All code compiles, no errors
- ✅ **Testing:** All tests pass (Unit: N, Integration: N, E2E: N)
- ✅ **Code Coverage:** Backend: X%, Frontend: Y% (both ≥80%)
- ✅ **Code Quality:** Checkstyle, PMD, ESLint pass (0 critical violations)
- ✅ **Documentation:** All inline docs complete with RPGLE traceability
- ✅ **Functional Equivalence:** 100% match with RPGLE behavior verified
- ✅ **Architectural Compliance:** Follows all project patterns
- ✅ **Traceability:** All files reference sources, git history clean
- ✅ **PR Readiness:** Story complete, sprint status updated

### 🧪 Manual Test Results

| Test Scenario | Result |
|---------------|--------|
| Search by customer ID | ✅ Pass |
| Search by customer name | ✅ Pass |
| Empty search validation | ✅ Pass |
| Not found scenario | ✅ Pass |
| Detail display | ✅ Pass |

### 📝 Files Modified

**Backend:**
- `backend/src/main/java/.../Customer.java`
- `backend/src/main/java/.../CustomerRepository.java`
- `backend/src/main/java/.../CustomerService.java`
- `backend/src/main/java/.../CustomerController.java`
- `backend/src/main/resources/db/migration/V<n>__Create_Customer_Table.sql`

**Frontend:**
- `frontend/src/features/customers/customer.types.ts`
- `frontend/src/features/customers/CustomerSearch.tsx`
- `frontend/src/features/customers/CustomerDetail.tsx`
- `frontend/src/features/customers/CustomerInquiry.tsx`
- `frontend/src/features/customers/useCustomer.ts`

**Documentation:**
- `docs/transformations/<PROGRAM>/analysis.md`
- `docs/transformations/<PROGRAM>/business-logic-mapping.md`
- `docs/transformations/<PROGRAM>/data-mapping.md`

### 👥 Reviewer Sign-Off

- [ ] Code Review Complete
- [ ] All Checklist Items Verified
- [ ] Approved for Merge

**Reviewer:** _______________  
**Date:** _______________
```

---

## 🎯 CUST001 Validation Example

See `docs/transformations/CUST001/` for a complete example of a fully validated transformation:

- **Analysis Document:** Shows comprehensive program understanding
- **Business Logic Mapping:** Complete RPGLE-to-Java mappings
- **Data Mapping:** Full field-by-field DDS mappings
- **Code Examples:** Properly documented with RPGLE references
- **Test Coverage:** 87% backend, 84% frontend (both above 80% threshold)

---

## 📚 Additional Resources

- **Workflow Guide:** `docs/workflow-guide.md` - Full transformation process
- **Java Standards:** `docs/standards/java-documentation-standards.md`
- **TypeScript Standards:** `docs/standards/typescript-documentation-standards.md`
- **Architecture:** `_bmad-output/planning-artifacts/architecture.md`
- **PRD (NFRs):** `_bmad-output/planning-artifacts/prd.md`

---

## ⚠️ Important Notes

**Before marking transformation complete:**
- Every checkbox must be `[x]`
- All validation commands must show success
- 100% functional equivalence is non-negotiable (NFR1)
- Documentation must include RPGLE traceability

**If ANY item fails:**
- Stop and fix the issue
- Do not proceed to merge
- Update story with findings
- Re-validate after fixes

**Quality over speed:** Take the time to validate thoroughly. A complete validation prevents technical debt and ensures maintainable code.

---

**Last Updated:** 2026-01-02  
**Maintained By:** Development Team  
**Questions?** See `docs/workflow-guide.md` or consult CUST001 reference implementation
