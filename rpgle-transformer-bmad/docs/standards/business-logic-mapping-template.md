# Business Logic Mapping Documentation Template

**Version**: 1.0  
**Last Updated**: 2026-01-02  
**Purpose**: Template for documenting RPGLE-to-modern transformation business logic mappings

## Template Overview

This template is used to document how RPGLE business logic transforms to modern Java/Spring Boot and React/TypeScript implementations. Use this template when creating transformation documentation for each RPGLE program.

---

## Template Structure

```markdown
# [PROGRAM_NAME] Business Logic Mapping

**RPGLE Program**: [program-name.rpgle]  
**Program Purpose**: [Brief description of program purpose]  
**Transformation Date**: [YYYY-MM-DD]  
**Transformed By**: [Agent/Team Name]

## Table of Contents

1. [Overview](#overview)
2. [Original RPGLE Logic](#original-rpgle-logic)
3. [Modern Implementation](#modern-implementation)
4. [Data Transformations](#data-transformations)
5. [Validation Rules](#validation-rules)
6. [File Operations](#file-operations)
7. [Display Logic](#display-logic)
8. [Edge Cases and Exceptions](#edge-cases-and-exceptions)
9. [Known Differences](#known-differences)
10. [Future Enhancements](#future-enhancements)

---

## Overview

### Business Purpose

[Describe what business function this program performs]

### User Workflow

**Original RPGLE Workflow:**
1. [Step 1]
2. [Step 2]
3. [etc.]

**Modern Workflow:**
1. [Step 1]
2. [Step 2]
3. [etc.]

### Architecture Components

| Component | RPGLE | Modern Implementation |
|-----------|-------|----------------------|
| **Data Source** | [Physical File(s)] | [Database Table(s) / API] |
| **Display** | [Display File(s)] | [React Component(s)] |
| **Business Logic** | [RPGLE Program] | [Java Service Class] |
| **Data Access** | [File I/O Operations] | [Repository / API Client] |

---

## Original RPGLE Logic

### Program Structure

**File**: `source-rpgle/programs/[PROGRAM_NAME].rpgle`

```rpgle
[Include key sections of original RPGLE code]
```

### Key Subroutines

| Subroutine | Purpose | Lines |
|------------|---------|-------|
| [SUBR_NAME] | [Description] | [Line range] |

### Indicators

| Indicator | Purpose | Usage |
|-----------|---------|-------|
| *IN03 | [Description] | [Where used] |
| *IN90 | [Description] | [Where used] |

---

## Modern Implementation

### Backend (Java/Spring Boot)

**Service Class**: `[package/path/ServiceClass.java]`

#### Method Mapping

| RPGLE Operation | Java Method | Notes |
|----------------|-------------|-------|
| [CHAIN FILENAME] | [repository.findById()] | [Any special handling] |
| [WRITE FILENAME] | [repository.save()] | [Any special handling] |

#### Code Example

```java
/**
 * [Method description]
 *
 * RPGLE Subroutine: [SUBR_NAME]
 * Original Logic: [Brief description]
 */
@Transactional(readOnly = true)
public CustomerDTO getCustomerByNumber(Long customerNumber) {
    // RPGLE: CHAIN CUSTMAST
    Customer customer = customerRepository.findByCustomerId(customerNumber)
        .orElseThrow(() -> new CustomerNotFoundException(
            "Customer not found: " + customerNumber)); // RPGLE: *IN90 = *On
    
    return mapToDTO(customer);
}
```

### Frontend (React/TypeScript)

**Component**: `[path/to/ComponentName.tsx]`

#### Screen Mapping

| DDS Record Format | React Component | Notes |
|-------------------|-----------------|-------|
| [PROMPT] | [ComponentName] | [Purpose] |
| [DETAIL] | [ComponentName] | [Purpose] |

#### Code Example

```tsx
/**
 * [Component description]
 *
 * DDS Record Format: [FORMAT_NAME]
 * RPGLE Program Flow: [Description]
 */
export function ComponentName() {
  // RPGLE: Variable CUSTNO - stores customer number
  const [customerId, setCustomerId] = useState<string | null>(null);

  // RPGLE: CHAIN operation to CUSTMAST
  const { data, isLoading, error } = useCustomer(customerId);

  // Implementation...
}
```

---

## Data Transformations

### DDS Field Mappings

| DDS Field | Type | Java Field | Java Type | Notes |
|-----------|------|------------|-----------|-------|
| CUSTNO | 5P 0 | customerId | Long | Primary key, manually assigned |
| CUSTNAME | 30A | customerName | String | Max length 30 |
| BALANCE | 9P 2 | accountBalance | BigDecimal | Financial precision required |
| LASTORDER | 8P 0 | lastOrderDate | Integer | YYYYMMDD format preserved |

### Type Conversion Rules

1. **Packed Decimal (P) → BigDecimal**
   - RPGLE: `BALANCE (9P 2)` → Java: `BigDecimal(precision=9, scale=2)`
   - Reason: Exact decimal arithmetic for financial data

2. **Date Fields (Numeric YYYYMMDD) → Integer**
   - RPGLE: `LASTORDER (8P 0)` → Java: `Integer`
   - Reason: Preserve original format for compatibility
   - Future Enhancement: Could convert to LocalDate

3. **Alphanumeric (A) → String**
   - RPGLE: `CUSTNAME (30A)` → Java: `String (max length 30)`
   - Validation: `@Size(max = 30)`

---

## Validation Rules

### Original RPGLE Validation

```rpgle
[Include RPGLE validation code]
```

### Modern Validation

#### Backend Validation (Java)

```java
/**
 * Validate customer number input.
 *
 * RPGLE Validation (lines X-Y):
 * IF PCUSTNO = 0
 *   *IN90 = *On
 *   PMSG = 'Customer number required'
 * ENDIF
 */
@NotNull(message = "Customer ID is required")
@Min(value = 1, message = "Customer number must be at least 1")
@Max(value = 99999, message = "Customer number cannot exceed 99999")
private Long customerId;
```

#### Frontend Validation (TypeScript/Zod)

```tsx
/**
 * Validation schema for customer number input.
 * DDS Validation: COMP(GT 0), implicit max 99999 from 5P 0
 */
export const CustomerNumberSchema = z
  .number()
  .int('Customer number must be an integer')
  .min(1, 'Customer number must be at least 1')
  .max(99999, 'Customer number cannot exceed 99999');
```

---

## File Operations

### RPGLE File Operations

| Operation | File | Key | Purpose |
|-----------|------|-----|---------|
| CHAIN | CUSTMAST | CUSTNO | Look up customer by ID |
| READ | CUSTMAST | - | Sequential read |
| WRITE | CUSTMAST | - | Insert new customer |
| UPDATE | CUSTMAST | - | Update existing customer |

### Modern Equivalents

#### Backend (Spring Data JPA)

```java
/**
 * Repository for Customer entity operations.
 *
 * RPGLE Operations:
 * - CHAIN CUSTMAST → findByCustomerId()
 * - READ CUSTMAST → findAll()
 * - WRITE CUSTMAST → save()
 * - UPDATE CUSTMAST → save()
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(Long customerId);
}
```

#### Frontend (API Client)

```tsx
/**
 * Fetch customer by ID.
 * RPGLE Operation: CHAIN CUSTMAST
 * API Endpoint: GET /api/v1/customers/{id}
 */
export async function getCustomerById(customerId: string): Promise<CustomerDetailDisplay> {
  const response = await axios.get(`${API_BASE_URL}/customers/${customerId}`);
  return apiCustomerToDisplay(response.data);
}
```

---

## Display Logic

### DDS Display File Mapping

**Display File**: `source-rpgle/dds/display-files/[FILENAME].dspf`

| Record Format | Purpose | Lines | React Component |
|---------------|---------|-------|-----------------|
| PROMPT | Customer number entry | 13-30 | CustomerSearch |
| DETAIL | Display customer info | 33-59 | CustomerDetail |

### Field Positioning

| DDS Field | Row | Col | React Element | Styling |
|-----------|-----|-----|---------------|---------|
| PCUSTNO | 10 | 20 | `<input type="number">` | Tailwind input |
| DCUSTNAME | 5 | 15 | `<dd>{customerName}</dd>` | Tailwind dd |

### Function Keys

| Function Key | RPGLE Indicator | Modern Equivalent | Implementation |
|--------------|----------------|-------------------|----------------|
| F3 (Exit) | *IN03 | ESC key / Close button | `window.history.back()` |
| F12 (Cancel) | *IN12 | ESC key / Cancel button | Reset form state |
| Enter (Submit) | CF01 | Enter key / Submit button | Form `onSubmit` |

---

## Edge Cases and Exceptions

### RPGLE Edge Case Handling

```rpgle
[Include RPGLE code for edge cases]
```

### Modern Edge Case Handling

#### Backend

```java
/**
 * Handle customer not found.
 * RPGLE: %Found(CUSTMAST) = *Off → *IN90 = *On, PMSG = 'Customer not found'
 */
if (!customerRepository.findByCustomerId(customerId).isPresent()) {
    throw new CustomerNotFoundException("Customer not found: " + customerId);
}
```

#### Frontend

```tsx
/**
 * Display error message.
 * DDS: PMSG field with *IN90 indicator
 */
{errorMessage && (
  <div className="text-red-600" role="alert">
    {errorMessage}
  </div>
)}
```

### Known Edge Cases

1. **Null/Missing Data**
   - RPGLE: Blank fields (`*Blanks`)
   - Modern: `null` or empty string `""`
   - Handling: Display "N/A" in UI for null values

2. **Zero Values**
   - RPGLE: Zero-suppression with EDTCDE(Z)
   - Modern: Display as-is or format with JavaScript
   - Handling: Custom formatting functions

---

## Known Differences

### Intentional Differences

1. **Error Handling**
   - RPGLE: Error indicators (*IN90, etc.) and message fields
   - Modern: HTTP status codes (404, 400) and exception handling
   - Reason: RESTful API best practices

2. **Data Types**
   - RPGLE: Numeric dates (YYYYMMDD)
   - Modern: Integer preserved (backward compatibility)
   - Future: Could migrate to LocalDate/ISO 8601

3. **User Interface**
   - RPGLE: Green-screen 5250 terminal
   - Modern: Responsive web UI with Tailwind CSS
   - Reason: Modern UX expectations

### Limitations

1. **[Limitation 1]**
   - Description: [What doesn't work exactly like RPGLE]
   - Workaround: [How it's handled]
   - Impact: [Low/Medium/High]

2. **[Limitation 2]**
   - Description: [What doesn't work exactly like RPGLE]
   - Workaround: [How it's handled]
   - Impact: [Low/Medium/High]

---

## Future Enhancements

### Short-term (Next Sprint)

- [ ] [Enhancement 1]
- [ ] [Enhancement 2]

### Long-term (Future Releases)

- [ ] Convert YYYYMMDD integer dates to LocalDate
- [ ] Add audit logging (created/modified timestamps)
- [ ] Implement soft delete instead of hard delete
- [ ] Add batch processing capabilities

---

## Validation and Testing

### Test Coverage

| Test Type | Coverage | Status |
|-----------|----------|--------|
| Backend Unit Tests | [XX%] | ✅ Pass |
| Backend Integration Tests | [XX%] | ✅ Pass |
| Frontend Component Tests | [XX%] | ✅ Pass |
| E2E Tests | [XX%] | ✅ Pass |
| Functional Equivalence | [Pass/Fail] | ✅ Pass |

### Test Cases

| Test Case | RPGLE Scenario | Modern Implementation | Result |
|-----------|----------------|----------------------|--------|
| Valid customer lookup | CHAIN finds record | GET returns 200 | ✅ Pass |
| Invalid customer number | CHAIN not found | GET returns 404 | ✅ Pass |
| Zero customer number | Validation error | 400 Bad Request | ✅ Pass |

---

## References

### Source Files

- **RPGLE Program**: `source-rpgle/programs/[PROGRAM_NAME].rpgle`
- **DDS Physical Files**: `source-rpgle/dds/physical-files/*.pf`
- **DDS Display Files**: `source-rpgle/dds/display-files/*.dspf`

### Implementation Files

- **Backend Entity**: `backend/src/main/java/[package]/entity/[ClassName].java`
- **Backend Service**: `backend/src/main/java/[package]/service/[ServiceName].java`
- **Backend Controller**: `backend/src/main/java/[package]/controller/[ControllerName].java`
- **Frontend Component**: `frontend/src/features/[feature]/[ComponentName].tsx`
- **Frontend Types**: `frontend/src/features/[feature]/[feature].types.ts`

### Documentation

- [Architecture Document](../../_bmad-output/planning-artifacts/architecture.md)
- [PRD](../../_bmad-output/planning-artifacts/prd.md)
- [Epic Documentation](../../_bmad-output/planning-artifacts/epics.md)
- [Story Implementation](../../_bmad-output/implementation-artifacts/[story-file].md)

---

## Appendix

### RPGLE Code Snippets

```rpgle
[Include full relevant RPGLE code sections]
```

### DDS File Definitions

```
[Include full DDS file definitions]
```

### Glossary

| Term | Definition |
|------|------------|
| CHAIN | RPGLE operation to read a record by key |
| DDS | Data Description Specifications |
| DSPF | Display File |
| PF | Physical File |
| RPGLE | RPG IV (ILE RPG) programming language |
```

---

## End of Template

---

## Example: CUST001 Business Logic Mapping

See [docs/transformations/CUST001/business-logic-mapping.md](../../transformations/CUST001/business-logic-mapping.md) for a complete example of this template in use.
