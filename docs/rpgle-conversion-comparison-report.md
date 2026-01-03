# RPGLE to Modern Architecture Conversion Comparison Report

**Program**: CUST001 - Customer Inquiry
**Date**: January 2, 2026
**Analyst**: Claude Code AI Analysis
**Version**: 2.0 - Three-Way Comparison

---

## Executive Summary

This report provides a comprehensive comparison of **three different approaches** for converting the RPGLE program CUST001 (Customer Inquiry) to a modern React/Java architecture.

### Approaches Compared

1. **Baseline Approach** (`rpgle-transformer-baseline/final-output`)
   - Traditional Spring Boot + React conversion
   - Full CRUD capabilities
   - JavaScript frontend

2. **Agents Approach** (`rpgle-transformer-agents/final-output`)
   - Agent-based conversion with extensive documentation
   - Inquiry-only (matches CUST001 scope)
   - TypeScript frontend with comprehensive tests

3. **Bmad Approach** (`rpgle-transformer-bmad`)
   - Modern architecture-focused conversion
   - Feature-based structure with React Query
   - Full production tooling (Docker, Flyway, quality gates)

### Final Scores

| Criterion | Weight | Baseline | Agents | Bmad | Winner |
|-----------|--------|----------|--------|------|--------|
| **Functional Completeness** | 30% | 8.0/10 | 9.0/10 | 9.0/10 | Agents/Bmad |
| **Architecture Quality** | 25% | 7.0/10 | 8.0/10 | 9.5/10 | **Bmad** |
| **Code Quality** | 20% | 7.0/10 | 8.5/10 | 9.0/10 | **Bmad** |
| **UI/UX Translation** | 15% | 8.0/10 | 8.5/10 | 9.0/10 | **Bmad** |
| **Data Model Translation** | 10% | 8.0/10 | 8.5/10 | 9.0/10 | **Bmad** |
| **WEIGHTED TOTAL** | 100% | **7.6/10** | **8.5/10** | **9.1/10** | **Bmad** |

### Winner: **Bmad Approach** (9.1/10)

**Bmad excels at:**
- **Modern Architecture**: Feature-based frontend, React Query, dedicated mapper class, API versioning
- **Production Readiness**: Docker Compose, Flyway migrations, comprehensive testing, quality gates
- **Code Quality**: TypeScript end-to-end, JaCoCo 80% coverage, Checkstyle, PMD, ESLint, Prettier
- **Developer Experience**: Zod validation, React Hook Form, React Query caching, Tailwind CSS

**Agents strengths:**
- **Documentation**: 8 comprehensive markdown files with RPGLE source mapping
- **Testing**: 31+ unit tests with excellent coverage
- **Traceability**: Every method documented with RPGLE line references

**Baseline strengths:**
- **Simplicity**: Straightforward Spring Boot + React implementation
- **CRUD Ready**: Full customer management capabilities
- **Accessibility**: Easy for beginners to understand

---

## Original Program Analysis

### CUST001 - Customer Inquiry Program

**Purpose**: Read-only customer information lookup by customer number

**Key Characteristics**:
- **Program Type**: Interactive 5250 display file application
- **Complexity**: Low (57 lines, simple validation and lookup)
- **Database**: Single table (CUSTMAST) keyed read access
- **Screens**: 2 formats (PROMPT for input, DETAIL for display)
- **Fields Displayed**: 8 of 10 available fields

**Business Logic**:
```rpgle
1. Display PROMPT screen
2. Accept customer number input
3. Validate: customerNumber != 0
4. CHAIN to CUSTMAST by customerNumber
5. If %Found: Display DETAIL screen
6. If NOT %Found: Display "Customer not found" error
7. Loop until F3 (Exit)
```

**Data Structure** (CUSTMAST.dds):
- CUSTNO (5P 0) - Customer Number - **Primary Key** ✓ Used
- CUSTNAME (30A) - Customer Name ✓ Used
- ADDR1 (30A) - Address Line 1 ✓ Used
- CITY (20A) - City ✓ Used
- STATE (2A) - State Code ✓ Used
- ZIP (5P 0) - Zip Code ✓ Used
- PHONE (12A) - Phone Number ✓ Used
- BALANCE (9P 2) - Account Balance ✓ Used
- CREDITLIM (9P 2) - Credit Limit ✗ NOT displayed by CUST001
- LASTORDER (8P 0) - Last Order Date ✗ NOT displayed by CUST001

---

## Baseline Approach Evaluation

### Functional Completeness: 8.0/10

**Strengths**:
- ✅ Core inquiry functionality works correctly
- ✅ Customer lookup by number implemented
- ✅ Error handling for "not found" scenarios
- ✅ Basic validation (zero check on frontend)

**Deviations**:
- ⚠️ **Scope Creep**: Adds full CRUD operations NOT in original
  - POST /api/customers (create)
  - PUT /api/customers/{id} (update)
  - DELETE /api/customers/{id} (delete)
  - GET /api/customers (list all)
- ⚠️ Exposes unused fields (creditLimit, lastOrderDate) in DTO
- ⚠️ No backend validation for negative numbers or upper bound (99999)

### Architecture Quality: 7.0/10

**Strengths**:
- ✅ Clean 3-tier architecture (Controller → Service → Repository)
- ✅ DTO pattern separates API from domain model
- ✅ Spring Data JPA for data access
- ✅ CORS configuration

**Weaknesses**:
- ❌ No API versioning (/api/customers vs /api/v1/customers)
- ❌ Mapper logic embedded in service (violates SRP)
- ⚠️ Over-engineered for inquiry-only scope (5 endpoints for 1 feature)

**File**: `backend/src/main/java/com/smeup/customerinquiry/controller/CustomerController.java:23-58`
- 5 REST endpoints (GET one, GET all, POST, PUT, DELETE)
- Only GET one is needed for CUST001

### Code Quality: 7.0/10

**Strengths**:
- ✅ Clean, readable code with Lombok
- ✅ SLF4J logging
- ✅ Consistent naming conventions

**Critical Gaps**:
- ❌ **No tests** (0 test files) - Major production risk
- ❌ **JavaScript instead of TypeScript** - No compile-time type safety
- ❌ Minimal inline documentation
- ❌ No RPGLE source code mapping

### UI/UX Translation: 8.0/10

**Strengths**:
- ✅ Two-component structure (CustomerSearch, CustomerDetail)
- ✅ PROMPT → DETAIL flow preserved
- ✅ Currency and zip code formatting (formatCurrency, formatZipCode)
- ✅ Keyboard shortcuts (F3, F12/Escape)

**Issues**:
- ⚠️ Displays fields not in original (creditLimit, lastOrderDate conditionally shown)
- ⚠️ Field names differ ("addressLine1" vs "Address", "accountBalance" vs "Balance")

**File**: `frontend/src/components/CustomerDetail.js:84-96`
```javascript
{customer.creditLimit && (
  <div className="detail-row">
    <label>Credit Limit:</label>
    <span>{formatCurrency(customer.creditLimit)}</span>
  </div>
)}
```
^ This field wasn't in CUST001 DETAIL screen

### Data Model Translation: 8.0/10

**Strengths**:
- ✅ Correct JPA entity mapping
- ✅ Appropriate Java types (Integer, String, BigDecimal)

**Issues**:
- ⚠️ Table renamed: `customer_master` instead of `CUSTMAST` (breaks legacy compatibility)
- ⚠️ Columns renamed: `customer_number` vs `CUSTNO`, `address_line1` vs `ADDR1`
- ⚠️ Uses LocalDate for lastOrderDate (RPGLE used Integer YYYYMMDD format)
- ❌ No migration strategy (Hibernate DDL only)

---

## Agents Approach Evaluation

### Functional Completeness: 9.0/10

**Strengths**:
- ✅ Exact RPGLE logic preservation with source code comments
- ✅ All validation rules correctly translated
- ✅ Error messages match RPGLE exactly ("Customer number required", "Customer not found")
- ✅ Enhanced validation: adds negative number check
- ✅ Only 8 fields in DTO (excludes creditLimit, lastOrderDate) - matches CUST001 scope
- ✅ Single GET endpoint (matches inquiry-only nature)

**File**: `backend/src/main/java/com/lanarimarco/modernization/services/CustomerInquiryService.java:74-85`
```java
/**
 * RPGLE: If PCUSTNO = 0
 *    Eval *IN90 = *On
 *    Eval PMSG = 'Customer number required'
 */
private void validateCustomerNumber(Integer customerNumber) {
    if (customerNumber == null || customerNumber == 0) {
        throw new CustomerValidationException("Customer number required");
    }
    if (customerNumber < 0) { // Enhancement beyond RPGLE
        throw new CustomerValidationException("Customer number must be positive");
    }
}
```

### Architecture Quality: 8.0/10

**Strengths**:
- ✅ Clean layered architecture
- ✅ Single GET endpoint (appropriate for inquiry-only)
- ✅ Custom exception hierarchy (CustomerNotFoundException, CustomerValidationException)
- ✅ @Transactional(readOnly = true) - correct for inquiry
- ✅ TypeScript frontend with type safety

**Weaknesses**:
- ⚠️ No API versioning
- ⚠️ Mapper logic in service (not dedicated mapper class)
- ⚠️ Exception handlers in controller (could use global handler)

### Code Quality: 8.5/10

**Strengths**:
- ✅ **Exceptional documentation**: Every method has JavaDoc with RPGLE mapping
- ✅ **Comprehensive tests**: 31+ unit tests (CustomerInquiryServiceTest.java with 267 lines)
- ✅ **TypeScript**: Full type safety in frontend
- ✅ **RPGLE traceability**: Comments link modern code to original source

**Test Coverage** (`backend/src/test/.../CustomerInquiryServiceTest.java`):
- Valid customer retrieval
- Null customer number validation
- Zero customer number validation
- Negative customer number validation
- Customer not found scenario
- DTO mapping validation
- Boundary cases (min=1, max=99999)

### UI/UX Translation: 8.5/10

**Strengths**:
- ✅ Single component (CustomerInquiry.tsx) with conditional rendering (mirrors RPGLE EXFMT logic)
- ✅ Exact field labels from RPGLE
- ✅ TypeScript state management (CustomerSearchState: 'idle' | 'loading' | 'success' | 'error')
- ✅ DDS edit code mapping documented (EDTCDE J, EDTCDE Z)

**File**: `frontend/src/pages/CustomerInquiry/CustomerInquiry.tsx:117-132`
```typescript
/**
 * Format currency for display
 * RPGLE: EDTCDE(J) for balance field
 */
const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(amount);
};
```

### Data Model Translation: 8.5/10

**Strengths**:
- ✅ Preserves RPGLE naming (CUSTMAST, CUSTNO, CUSTNAME)
- ✅ Correct data types with precision (BigDecimal for BALANCE)
- ✅ DB2 compatibility mode for H2 (excellent for AS/400 migration)
- ✅ Production uses actual DB2

**Weaknesses**:
- ❌ No Flyway/Liquibase migrations (relies on Hibernate DDL)

---

## Bmad Approach Evaluation

### Functional Completeness: 9.0/10

**Strengths**:
- ✅ All RPGLE validation correctly implemented
- ✅ Zod schema validation (runtime + compile-time)
- ✅ React Hook Form for declarative validation
- ✅ Validates upper bound: customerId > 99999
- ✅ Single GET endpoint (inquiry-only, correct scope)

**File**: `frontend/src/features/customers/customer.types.ts`
```typescript
const CustomerPromptFormDataSchema = z.object({
  customerNumber: z.number()
    .int('Customer number must be an integer')
    .min(1, 'Customer number must be greater than 0')
    .max(99999, 'Customer number must be at most 99999'),
});
```

### Architecture Quality: 9.5/10 ⭐ **Best-in-Class**

**Exceptional Strengths**:
- ✅ **Feature-based architecture**: `features/customers/` contains all customer-related code
- ✅ **Dedicated CustomerMapper class**: Separate from service (SRP)
- ✅ **API versioning**: `/api/v1/customers`
- ✅ **React Query custom hook** (useCustomer.ts): Eliminates manual state management
- ✅ **RFC 7807 error format**: Industry standard Problem Details
- ✅ **ApiResponse<T> wrapper**: Consistent API responses

**Structure**:
```
features/customers/
├── CustomerInquiry.tsx      # Container
├── CustomerSearch.tsx        # PROMPT screen
├── CustomerDetail.tsx        # DETAIL screen
├── useCustomer.ts           # React Query hook
├── customer.types.ts        # Type definitions
└── __tests__/               # Tests co-located
```

**File**: `backend/src/main/java/com/smeup/backend/controller/CustomerController.java:22-47`
```java
@RestController
@RequestMapping("/api/v1/customers")  // ✓ API versioning
public class CustomerController {
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        CustomerDTO customerDTO = customerMapper.toDTO(customer);  // ✓ Dedicated mapper
        return ResponseEntity.ok(new ApiResponse<>(customerDTO));
    }
}
```

### Code Quality: 9.0/10 ⭐ **Exceptional**

**Strengths**:
- ✅ **TypeScript end-to-end**: Frontend + types for safety
- ✅ **Comprehensive testing**: 21 backend test files + Vitest + Playwright
- ✅ **Quality gates**: Checkstyle (Google), PMD, JaCoCo 80% minimum, ESLint, Prettier
- ✅ **Modern best practices**: Java 21 Records for DTOs, React 19, Vite

**Quality Enforcement**:
- Checkstyle: Google Java Style Guide
- PMD: Static analysis for code smells
- JaCoCo: 80% coverage requirement on service layer
- ESLint + Prettier: Frontend code quality
- Integration tests with Testcontainers

### UI/UX Translation: 9.0/10

**Strengths**:
- ✅ Semantic HTML (dl/dt/dd for customer details)
- ✅ Accessibility: role="alert", role="status", aria-live="polite"
- ✅ **Tailwind CSS**: Responsive design with utility classes
- ✅ **React Query**: Automatic caching (5-minute stale time), retry logic
- ✅ **React Hook Form + Zod**: Declarative validation with helpful error messages

**File**: `frontend/src/features/customers/CustomerDetail.tsx:137-194`
```typescript
return (
  <div className="p-6 space-y-4">
    <h2 className="text-2xl font-bold text-center mb-6">Customer Detail</h2>

    {/* Semantic HTML for accessibility */}
    <dl className="space-y-3 max-w-2xl">
      <div className="flex flex-col sm:flex-row">
        <dt className="w-full sm:w-48 font-medium">Customer Number:</dt>
        <dd className="mt-1 sm:mt-0">{customer.customerNumber}</dd>
      </div>
      ...
    </dl>
  </div>
);
```

### Data Model Translation: 9.0/10

**Strengths**:
- ✅ **Flyway migrations**: V1__create_custmast.sql, V2__insert_test_data.sql
- ✅ PostgreSQL for all environments
- ✅ DDS source documented in migration comments
- ✅ Correct data type mappings

**File**: `backend/src/main/resources/db/migration/V1__create_custmast.sql`
```sql
-- Source: source-rpgle/dds/physical-files/CUSTMAST.dds
CREATE TABLE CUSTMAST (
    CUSTNO      BIGINT PRIMARY KEY,           -- 5P 0
    CUSTNAME    VARCHAR(30) NOT NULL,         -- 30A
    ADDR1       VARCHAR(30),                  -- 30A
    CITY        VARCHAR(20),                  -- 20A
    STATE       CHAR(2),                      -- 2A
    ZIP         INTEGER,                      -- 5P 0
    PHONE       VARCHAR(12),                  -- 12A
    BALANCE     DECIMAL(9,2),                 -- 9P 2
    CREDITLIM   DECIMAL(9,2),                 -- 9P 2
    LASTORDER   INTEGER                       -- 8P 0 (YYYYMMDD)
);
```

---

## Comparative Analysis

### Direct Comparison Matrix

| Criterion | Baseline | Agents | Bmad | Winner | Delta |
|-----------|----------|--------|------|--------|-------|
| **Functional Completeness** (30%) | 8.0 | 9.0 | 9.0 | Agents/Bmad | +1.0 |
| **Architecture Quality** (25%) | 7.0 | 8.0 | 9.5 | **Bmad** | +2.5 |
| **Code Quality** (20%) | 7.0 | 8.5 | 9.0 | **Bmad** | +2.0 |
| **UI/UX Translation** (15%) | 8.0 | 8.5 | 9.0 | **Bmad** | +1.0 |
| **Data Model Translation** (10%) | 8.0 | 8.5 | 9.0 | **Bmad** | +1.0 |
| **WEIGHTED TOTAL** | **7.6** | **8.5** | **9.1** | **Bmad** | **+1.5** |

### Technology Stack Comparison

| Technology | Baseline | Agents | Bmad | Best |
|------------|----------|--------|------|------|
| **Frontend Language** | JavaScript | TypeScript | TypeScript | Agents/Bmad |
| **React Version** | 18.2 | 18.2 | 19.2 | **Bmad** |
| **Spring Boot** | 3.2.1 | 3.2.1 | 4.0.1 | **Bmad** |
| **Java Version** | 17 | 17 | 21 | **Bmad** |
| **State Management** | useState | useState | React Query | **Bmad** |
| **Form Handling** | Manual | Manual | React Hook Form + Zod | **Bmad** |
| **Styling** | CSS | CSS | Tailwind CSS | **Bmad** |
| **Migrations** | None | None | **Flyway** | **Bmad** |
| **Testing (Backend)** | None | JUnit + Mockito | JUnit + Mockito + Testcontainers | **Bmad** |
| **Testing (Frontend)** | None | None | Vitest + Playwright | **Bmad** |
| **Containerization** | None | None | **Docker Compose** | **Bmad** |
| **Quality Gates** | None | None | Checkstyle, PMD, JaCoCo, ESLint | **Bmad** |
| **Error Format** | Spring default | Custom Map | **RFC 7807** | **Bmad** |
| **API Versioning** | ❌ No | ❌ No | ✅ Yes (/v1/) | **Bmad** |

### Strengths & Weaknesses

#### Bmad Strengths
1. **Production-Ready**: Docker, Flyway, quality gates, comprehensive testing
2. **Modern Architecture**: Feature-based, React Query, dedicated mapper, API versioning
3. **Developer Experience**: Zod validation, React Hook Form, Tailwind, TypeScript autocomplete
4. **Standards Compliance**: RFC 7807 errors, REST best practices
5. **Maintainability**: Quality gates enforce standards, comprehensive tests

#### Bmad Weaknesses
1. **Complexity**: Steeper learning curve (React Query, Zod, Flyway)
2. **Over-engineering for Simple Tasks**: CUST001 is 57 lines; Bmad is substantial infrastructure
3. **Dependency Count**: More dependencies to maintain

#### Agents Strengths
1. **Documentation**: 8 MD files with RPGLE source mapping (exceptional traceability)
2. **Testing**: 31+ unit tests with excellent coverage
3. **DB2 Compatibility**: H2 in DB2 mode for smooth AS/400 migration
4. **Scope Discipline**: Inquiry-only, matches CUST001 exactly

#### Agents Weaknesses
1. **No Flyway**: Manual schema management
2. **No Docker**: Manual deployment
3. **No API Versioning**: Breaking changes would affect all clients
4. **Mapper in Service**: Violates SRP

#### Baseline Strengths
1. **Simplicity**: Easy to understand for beginners
2. **CRUD Ready**: Full customer management from day one
3. **Dual Database**: H2 (dev) + PostgreSQL (prod)

#### Baseline Weaknesses
1. **No Tests**: Critical gap for production
2. **JavaScript**: No type safety
3. **Scope Creep**: CRUD beyond inquiry-only CUST001
4. **Table Renaming**: Breaks legacy compatibility (CUSTMAST → customer_master)

---

## Production Readiness Assessment

### Bmad Approach: 8.9/10 ✅ **Production-Ready**

**Deployment**:
```bash
# 1. Set environment variables
export VITE_API_BASE_URL=https://api.example.com
export DB_USERNAME=produser
export DB_PASSWORD=***

# 2. Start with Docker Compose
docker-compose up -d

# 3. Flyway runs migrations automatically
# 4. Application ready!
```

**What's Included**:
- ✅ Docker Compose (PostgreSQL + Backend + Frontend)
- ✅ Flyway migrations (automatic schema versioning)
- ✅ 21 backend test files + integration tests
- ✅ Vitest + Playwright E2E tests
- ✅ JaCoCo 80% coverage enforcement
- ✅ Quality gates (Checkstyle, PMD, ESLint, Prettier)
- ✅ Health checks in Docker Compose
- ✅ Environment variable configuration

**Gaps**:
- ⚠️ No OpenAPI/Swagger documentation
- ⚠️ No monitoring (Prometheus/Grafana)

### Agents Approach: 7.6/10 ⚠️ **Needs Containerization**

**What's Included**:
- ✅ Comprehensive backend tests (31+ test cases)
- ✅ 8 documentation files
- ✅ Multi-environment configs (dev, uat, prod, test)
- ✅ DB2 production database

**Gaps**:
- ❌ No Docker Compose
- ❌ No Flyway migrations
- ❌ No frontend tests
- ❌ No quality gates

### Baseline Approach: 4.8/10 ❌ **Not Production-Ready**

**What's Included**:
- ✅ Basic functionality works
- ✅ CORS configured
- ✅ README documentation

**Critical Gaps**:
- ❌ **Zero tests** - Cannot deploy without tests
- ❌ No Docker
- ❌ No migrations
- ❌ No quality gates
- ❌ Minimal documentation

---

## Recommendations

### Overall Winner: **Bmad Approach**

**Use Bmad when:**
- ✅ Building production applications
- ✅ Scalability and maintainability are priorities
- ✅ Team has modern JavaScript/TypeScript experience
- ✅ Need Docker/container deployment
- ✅ Want industry-standard practices (RFC 7807, Flyway, quality gates)
- ✅ Long-term project (years of maintenance expected)

**Use Agents when:**
- ✅ Migrating from AS/400/iSeries (DB2 compatibility)
- ✅ Documentation and traceability are critical (regulatory, audit)
- ✅ Team needs RPGLE source code mapping
- ✅ Comprehensive testing required but not E2E
- ✅ Medium-complexity application

**Use Baseline when:**
- ✅ Learning React + Spring Boot
- ✅ Rapid prototyping (add tests later)
- ✅ Internal tool with <10 users
- ✅ Timeline is very short (days)
- ✅ Team unfamiliar with modern tools

### Improvement Suggestions

#### For Bmad
1. Add OpenAPI/Swagger documentation
2. Add Prometheus metrics + Grafana dashboards
3. Add more comprehensive seed data
4. Add Redis for distributed caching

#### For Agents
1. **Add Docker Compose** (high priority)
2. **Add Flyway migrations** (high priority)
3. Add frontend tests (Jest/Vitest)
4. Add API versioning (/api/v1/)
5. Extract mapper to dedicated class

#### For Baseline
1. **Add comprehensive tests** (critical - blocks production)
2. **Migrate to TypeScript** (high priority)
3. Add Flyway migrations
4. Add Docker Compose
5. Add RPGLE source code mapping documentation
6. Add quality gates (Checkstyle, ESLint)

### Migration Feasibility

**Easiest to Deploy**: **Bmad**
- One command: `docker-compose up -d`
- Flyway runs migrations automatically
- Health checks ensure proper startup order
- Environment variables for configuration

**Migration Timeline**:
- Bmad: 2-3 weeks (Docker + Flyway ready)
- Agents: 4-6 weeks (need Docker + Flyway)
- Baseline: 6-8 weeks (need tests + Docker + Flyway)

---

## Conclusion

All three approaches successfully convert CUST001 to modern architecture, but with different strengths:

- **Bmad** excels at production readiness and modern architecture (9.1/10)
- **Agents** excels at documentation and traceability (8.5/10)
- **Baseline** excels at simplicity and accessibility (7.6/10)

For production deployment with long-term maintenance requirements, **Bmad is the clear winner**, offering:
- Superior architecture (feature-based, React Query, dedicated mapper)
- Comprehensive testing (backend + frontend E2E)
- Production tooling (Docker, Flyway, quality gates)
- Industry standards (RFC 7807, API versioning)

For organizations migrating from AS/400 systems where documentation is paramount, **Agents is an excellent choice**, providing inline RPGLE mapping and DB2 compatibility.

For learning, prototyping, or simple internal tools, **Baseline provides a solid foundation** with simplicity and CRUD capabilities.

**Recommendation**: Deploy **Bmad** for production systems requiring scalability, maintainability, and modern best practices.

---

**Report Generated**: January 2, 2026
**Analysis Tool**: Claude Code (Sonnet 4.5)
**Total Analysis Time**: Comprehensive multi-phase evaluation

**End of Report**
