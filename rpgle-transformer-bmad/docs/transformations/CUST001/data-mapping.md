# CUST001 Data Mapping: DDS to Modern Stack

**Program:** CUST001 - Customer Inquiry  
**Created:** 2026-01-02  
**Status:** Documentation Complete

## Table of Contents

1. [Overview](#overview)
2. [DDS Concepts for Modern Developers](#dds-concepts-for-modern-developers)
3. [CUSTMAST Physical File Mapping](#custmast-physical-file-mapping)
4. [CUSTDSP Display File Mapping](#custdsp-display-file-mapping)
5. [Full-Stack Data Flow](#full-stack-data-flow)
6. [Data Type Transformation Rules](#data-type-transformation-rules)
7. [Field-Level Notes and Special Cases](#field-level-notes-and-special-cases)

## Overview

This document maps data structures from IBM AS/400 DDS (Data Description Specifications) files to their modern equivalents across the full technology stack.

**Data Flow Path:**
```
DDS Physical File (CUSTMAST.dds)
    ↓
SQL Table (CUSTMAST)
    ↓
JPA Entity (Customer.java)
    ↓
DTO (CustomerDTO.java)
    ↓
API Response (JSON)
    ↓
TypeScript Types (customer.types.ts)
    ↓
React Components (CustomerDetail.tsx, CustomerSearch.tsx)
```

**Key Principles:**
- SQL table/column names preserve DDS names for traceability
- Java/TypeScript use readable field names
- All layers include DDS field references in comments
- Type safety enforced end-to-end with validation

**Source Files:**
- DDS Physical: [source-rpgle/dds/physical-files/CUSTMAST.dds](../../../source-rpgle/dds/physical-files/CUSTMAST.dds)
- DDS Display: [source-rpgle/dds/display-files/CUSTDSP.dds](../../../source-rpgle/dds/display-files/CUSTDSP.dds)
- SQL Schema: [backend/src/main/resources/db/migration/V1__create_custmast.sql](../../../backend/src/main/resources/db/migration/V1__create_custmast.sql)
- JPA Entity: [backend/src/main/java/com/smeup/backend/entity/Customer.java](../../../backend/src/main/java/com/smeup/backend/entity/Customer.java)
- TypeScript Types: [frontend/src/features/customers/customer.types.ts](../../../frontend/src/features/customers/customer.types.ts)

## DDS Concepts for Modern Developers

### What is DDS?

DDS (Data Description Specifications) is IBM's declarative language for defining data structures on AS/400 systems. It predates modern ORMs and schemas by decades.

**DDS File Types:**
- **Physical Files**: Database tables (like SQL DDL)
- **Display Files**: Screen layouts (like HTML forms)
- **Logical Files**: Views/indexes (like SQL views)

### DDS Data Type Notation

| DDS Type | Meaning | Example | Modern Equivalent |
|----------|---------|---------|-------------------|
| `5P 0` | Packed Decimal, 5 digits, 0 decimals | `CUSTNO 5P 0` | `BIGINT` (SQL), `Long` (Java) |
| `9P 2` | Packed Decimal, 9 digits, 2 decimals | `BALANCE 9P 2` | `DECIMAL(9,2)` (SQL), `BigDecimal` (Java) |
| `30A` | Alphanumeric, 30 characters | `CUSTNAME 30A` | `VARCHAR(30)` (SQL), `String` (Java) |
| `8P 0` | Packed Decimal, 8 digits | `LASTORDER 8P 0` | `INTEGER` (SQL), `Integer` (Java) |
| `5Y 0` | Zoned Decimal (display) | `PCUSTNO 5Y 0` | `number` (TypeScript) |

### DDS Field Attributes

| Attribute | Purpose | Example |
|-----------|---------|---------|
| `COLHDG('text')` | Column heading for reports | Field label |
| `TEXT('desc')` | Field description | Documentation |
| `B` (Both) | Input/Output field | User can type |
| `O` (Output) | Output-only field | Display only |
| `EDTCDE(code)` | Edit code for formatting | Date/number formatting |

### Key Record Format

```
A          K CUSTNO
```

The `K` indicates the primary key field for the file.

## CUSTMAST Physical File Mapping

### DDS Source Definition

```cobol
     A*===============================================================
     A* File: CUSTMAST - Customer Master File
     A* Description: Physical file containing customer master data
     A*===============================================================
     A          R CUSTREC
     A            CUSTNO         5P 0       COLHDG('Customer' 'Number')
     A                                      TEXT('Customer Number')
     A            CUSTNAME      30A         COLHDG('Customer' 'Name')
     A                                      TEXT('Customer Name')
     A            ADDR1         30A         COLHDG('Address' 'Line 1')
     A                                      TEXT('Address Line 1')
     A            CITY          20A         COLHDG('City')
     A                                      TEXT('City')
     A            STATE          2A         COLHDG('State')
     A                                      TEXT('State Code')
     A            ZIP            5P 0       COLHDG('Zip' 'Code')
     A                                      TEXT('Zip Code')
     A            PHONE         12A         COLHDG('Phone' 'Number')
     A                                      TEXT('Phone Number')
     A            BALANCE        9P 2       COLHDG('Account' 'Balance')
     A                                      TEXT('Account Balance')
     A            CREDITLIM      9P 2       COLHDG('Credit' 'Limit')
     A                                      TEXT('Credit Limit')
     A            LASTORDER      8P 0       COLHDG('Last' 'Order Date')
     A                                      TEXT('Last Order Date YYYYMMDD')
     A          K CUSTNO
```

### Field-by-Field Mapping

| DDS Field | DDS Type | SQL Column | SQL Type | Java Field | Java Type | TypeScript Field | TS Type |
|-----------|----------|------------|----------|------------|-----------|------------------|---------|
| `CUSTNO` | `5P 0` | `CUSTNO` | `BIGINT NOT NULL` | `customerId` | `Long` | `customerNumber` | `number` |
| `CUSTNAME` | `30A` | `CUSTNAME` | `VARCHAR(30) NOT NULL` | `customerName` | `String` | `customerName` | `string \| null` |
| `ADDR1` | `30A` | `ADDR1` | `VARCHAR(30)` | `addressLine1` | `String` | `addressLine1` | `string \| null` |
| `CITY` | `20A` | `CITY` | `VARCHAR(20)` | `city` | `String` | `city` | `string \| null` |
| `STATE` | `2A` | `STATE` | `CHAR(2)` | `state` | `String` | `state` | `string \| null` |
| `ZIP` | `5P 0` | `ZIP` | `INTEGER` | `zipCode` | `Integer` | `zipCode` | `number \| null` |
| `PHONE` | `12A` | `PHONE` | `VARCHAR(12)` | `phoneNumber` | `String` | `phoneNumber` | `string \| null` |
| `BALANCE` | `9P 2` | `BALANCE` | `DECIMAL(9,2)` | `accountBalance` | `BigDecimal` | `accountBalance` | `number \| null` |
| `CREDITLIM` | `9P 2` | `CREDITLIM` | `DECIMAL(9,2)` | `creditLimit` | `BigDecimal` | `creditLimit` | `number \| null` |
| `LASTORDER` | `8P 0` | `LASTORDER` | `INTEGER` | `lastOrderDate` | `Integer` | `lastOrderDate` | `number \| null` |

### SQL Table Schema

```sql
CREATE TABLE CUSTMAST (
    CUSTNO BIGINT NOT NULL PRIMARY KEY,
    CUSTNAME VARCHAR(30) NOT NULL,
    ADDR1 VARCHAR(30),
    CITY VARCHAR(20),
    STATE CHAR(2),
    ZIP INTEGER,
    PHONE VARCHAR(12),
    BALANCE DECIMAL(9,2),
    CREDITLIM DECIMAL(9,2),
    LASTORDER INTEGER
);

CREATE INDEX IDX_CUSTMAST_NAME ON CUSTMAST(CUSTNAME);
```

**Note:** See `backend/src/main/resources/db/migration/V1__create_custmast.sql` for the full migration with detailed DDS field comments for traceability.

### JPA Entity Annotations

```java
@Entity
@Table(name = "CUSTMAST") // Preserves DDS file name

@Id
@Column(name = "CUSTNO", nullable = false)
private Long customerId;

@Column(name = "CUSTNAME", length = 30, nullable = false)
@NotBlank(message = "Customer name is required")
@Size(max = 30)
private String customerName;

@Column(name = "BALANCE", precision = 9, scale = 2)
private BigDecimal accountBalance;
```

**Key Annotations:**
- `@Table(name = "CUSTMAST")` - Uses DDS physical file name
- `@Column(name = "CUSTNO")` - Uses DDS field name
- `@NotBlank`, `@Size`, `@Pattern` - Jakarta Bean Validation constraints
- No `@GeneratedValue` on ID - preserves AS/400 customer numbers

## CUSTDSP Display File Mapping

### DDS Source Definition

```cobol
     A*===============================================================
     A* File: CUSTDSP - Customer Inquiry Display File
     A* Description: Display file for customer inquiry program
     A*===============================================================
     A                                      DSPSIZ(24 80 *DS3)
     A*---------------------------------------------------------------
     A* Record Format: PROMPT - Customer Number Entry Screen
     A*---------------------------------------------------------------
     A          R PROMPT
     A            PCUSTNO        5Y 0B  4 20EDTCDE(Z)
     A  90        PMSG          50A  O  5  9COLOR(RED)
     
     A*---------------------------------------------------------------
     A* Record Format: DETAIL - Customer Detail Display Screen
     A*---------------------------------------------------------------
     A          R DETAIL
     A            DCUSTNO        5Y 0O  4 20EDTCDE(Z)
     A            DCUSTNAME     30A  O  5 20
     A            DADDR1        30A  O  7 20
     A            DCITY         20A  O  8 20
     A            DSTATE         2A  O  9 20
     A            DZIP           5Y 0O 10 20EDTCDE(Z)
     A            DPHONE        12A  O 12 20
     A            DBALANCE       9Y 2O 14 20EDTCDE(J)
```

### Display Field Mapping to React Components

#### PROMPT Record Format → CustomerSearch Component

| DDS Field | Type | Usage | React Component | Prop/Field | TypeScript Type | Component File |
|-----------|------|-------|-----------------|------------|-----------------|----------------|
| `PCUSTNO` | `5Y 0` | Input (B) | `CustomerSearch` | `customerNumber` (form field) | `number` | CustomerSearch.tsx |
| `PMSG` | `50A` | Output (O) | `CustomerSearch` | `errorMessage` (state) | `string \| undefined` | CustomerSearch.tsx |

**TypeScript Interface:**
```typescript
export interface CustomerPromptFormData {
  customerNumber: number;
  errorMessage?: string;
}
```

**React Hook Form Usage:**
```typescript
const { register, handleSubmit } = useForm<CustomerPromptFormData>();
```

#### DETAIL Record Format → CustomerDetail Component

| DDS Field | Type | Usage | React Component | Prop Name | TypeScript Type | Component File |
|-----------|------|-------|-----------------|-----------|-----------------|----------------|
| `DCUSTNO` | `5Y 0` | Output (O) | `CustomerDetail` | `customerNumber` | `number` | CustomerDetail.tsx |
| `DCUSTNAME` | `30A` | Output (O) | `CustomerDetail` | `customerName` | `string \| null` | CustomerDetail.tsx |
| `DADDR1` | `30A` | Output (O) | `CustomerDetail` | `addressLine1` | `string \| null` | CustomerDetail.tsx |
| `DCITY` | `20A` | Output (O) | `CustomerDetail` | `city` | `string \| null` | CustomerDetail.tsx |
| `DSTATE` | `2A` | Output (O) | `CustomerDetail` | `state` | `string \| null` | CustomerDetail.tsx |
| `DZIP` | `5Y 0` | Output (O) | `CustomerDetail` | `zipCode` | `number \| null` | CustomerDetail.tsx |
| `DPHONE` | `12A` | Output (O) | `CustomerDetail` | `phoneNumber` | `string \| null` | CustomerDetail.tsx |
| `DBALANCE` | `9Y 2` | Output (O) | `CustomerDetail` | `accountBalance` | `number \| null` | CustomerDetail.tsx |

**TypeScript Interface:**
```typescript
export interface CustomerDetailDisplay {
  customerNumber: number;
  customerName: string | null;
  addressLine1: string | null;
  city: string | null;
  state: string | null;
  zipCode: number | null;
  phoneNumber: string | null;
  accountBalance: number | null;
  // Note: creditLimit and lastOrderDate are NOT displayed in DDS DETAIL format.
  // This is an intentional design decision from the original RPGLE program CUST001.
  // The display file only shows customer identification and balance information.
  // Credit limit and last order date exist in CUSTMAST database but are not
  // surfaced in the customer inquiry screen. This preserves functional equivalence
  // with the legacy AS/400 green screen interface.
}
```

### Display Field Attributes

| Attribute | DDS Meaning | React Equivalent |
|-----------|-------------|------------------|
| `B` (Both) | Input/Output field | `<input>` element with `onChange` handler |
| `O` (Output) | Display-only | Read-only `<span>` or `<div>` |
| `EDTCDE(Z)` | Zero suppress | Formatting in display logic |
| `EDTCDE(J)` | Comma edit | Number formatting with `.toLocaleString()` |
| `COLOR(RED)` | Red text | Tailwind CSS `text-red-600` |
| `DSPATR(HI)` | High intensity | Tailwind CSS `font-bold` |

## Full-Stack Data Flow

### Complete End-to-End Mapping Table

| DDS Field | SQL Column | SQL Type | Java Field | Java Type | DTO Field | API JSON | TypeScript Field | TS Type | Notes |
|-----------|------------|----------|------------|-----------|-----------|----------|------------------|---------|-------|
| `CUSTNO` | `CUSTNO` | `BIGINT` | `customerId` | `Long` | `customerId` | `customerId` | `customerNumber` | `number` | Primary key, not generated |
| `CUSTNAME` | `CUSTNAME` | `VARCHAR(30)` | `customerName` | `String` | `customerName` | `customerName` | `customerName` | `string \| null` | Required in DB and entity |
| `ADDR1` | `ADDR1` | `VARCHAR(30)` | `addressLine1` | `String` | `addressLine1` | `addressLine1` | `addressLine1` | `string \| null` | Optional field |
| `CITY` | `CITY` | `VARCHAR(20)` | `city` | `String` | `city` | `city` | `city` | `string \| null` | Optional field |
| `STATE` | `STATE` | `CHAR(2)` | `state` | `String` | `state` | `state` | `state` | `string \| null` | 2-char uppercase validation |
| `ZIP` | `ZIP` | `INTEGER` | `zipCode` | `Integer` | `zipCode` | `zipCode` | `zipCode` | `number \| null` | 0-99999 range validation |
| `PHONE` | `PHONE` | `VARCHAR(12)` | `phoneNumber` | `String` | `phoneNumber` | `phoneNumber` | `phoneNumber` | `string \| null` | Optional field |
| `BALANCE` | `BALANCE` | `DECIMAL(9,2)` | `accountBalance` | `BigDecimal` | `accountBalance` | `accountBalance` | `accountBalance` | `number \| null` | Currency value, 2 decimals |
| `CREDITLIM` | `CREDITLIM` | `DECIMAL(9,2)` | `creditLimit` | `BigDecimal` | `creditLimit` | `creditLimit` | `creditLimit` | `number \| null` | Currency value, 2 decimals |
| `LASTORDER` | `LASTORDER` | `INTEGER` | `lastOrderDate` | `Integer` | `lastOrderDate` | `lastOrderDate` | `lastOrderDate` | `number \| null` | YYYYMMDD format (e.g., 20261225) |

### API Request/Response Flow

**GET /api/customers/{customerId} Request:**
```
Path Parameter: customerId (number)
    ↓
CustomerController.getCustomer(Long id)
    ↓
CustomerService.findById(Long id)
    ↓
CustomerRepository.findById(Long id)
    ↓
JPA Query: SELECT * FROM CUSTMAST WHERE CUSTNO = ?
    ↓
Customer Entity populated from ResultSet
    ↓
CustomerMapper.toDTO(Customer entity)
    ↓
CustomerDTO returned
    ↓
JSON Response
```

**Response JSON Example:**
```json
{
  "data": {
    "customerId": 12345,
    "customerName": "ACME Corporation",
    "addressLine1": "123 Main Street",
    "city": "Springfield",
    "state": "IL",
    "zipCode": 62701,
    "phoneNumber": "217-555-0100",
    "accountBalance": 1500.50,
    "creditLimit": 5000.00,
    "lastOrderDate": 20251215
  }
}
```

**TypeScript API Client:**
```typescript
const response = await fetch(`/api/customers/${customerNumber}`);
const data: ApiResponse<Customer> = await response.json();
// data.data.customerName is typed as string | null
```

## Data Type Transformation Rules

### RPGLE Packed Decimal → SQL → Java → TypeScript

#### Integer Types (0 decimal places)

| DDS Type | SQL Type | Java Type | TypeScript | Constraints | Example |
|----------|----------|-----------|------------|-------------|---------|
| `5P 0` | `BIGINT` | `Long` | `number` | 0 to 99,999 | Customer ID |
| `8P 0` | `INTEGER` | `Integer` | `number` | YYYYMMDD (e.g., 20261225) | Date fields |

**Type Conversion Chain:**
```
RPGLE: 12345 (5P 0)
  → SQL: 12345 (BIGINT)
  → Java: 12345L (Long)
  → JSON: 12345 (number)
  → TypeScript: 12345 (number)
```

#### Decimal Types (with decimal places)

| DDS Type | SQL Type | Java Type | TypeScript | Precision | Example |
|----------|----------|-----------|------------|-----------|---------|
| `9P 2` | `DECIMAL(9,2)` | `BigDecimal` | `number` | 2 decimal places | Currency values |

**Type Conversion Chain:**
```
RPGLE: 1500.50 (9P 2)
  → SQL: 1500.50 (DECIMAL(9,2))
  → Java: new BigDecimal("1500.50")
  → JSON: 1500.50 (number)
  → TypeScript: 1500.50 (number)
```

**Important:** Java uses `BigDecimal` for precision in financial calculations, but JSON/TypeScript use native `number`.

### RPGLE Character → SQL → Java → TypeScript

| DDS Type | SQL Type | Java Type | TypeScript | Max Length | Example |
|----------|----------|-----------|------------|------------|---------|
| `30A` | `VARCHAR(30)` | `String` | `string` | 30 chars | Customer name |
| `2A` | `CHAR(2)` | `String` | `string` | 2 chars | State code |
| `12A` | `VARCHAR(12)` | `String` | `string` | 12 chars | Phone number |

**Type Conversion Chain:**
```
RPGLE: "ACME Corporation" (30A)
  → SQL: "ACME Corporation" (VARCHAR(30))
  → Java: "ACME Corporation" (String)
  → JSON: "ACME Corporation" (string)
  → TypeScript: "ACME Corporation" (string)
```

**Special Case - CHAR vs VARCHAR:**
- `CHAR(2)` (STATE) - Fixed width, always 2 characters
- `VARCHAR(30)` (CUSTNAME) - Variable width, up to 30 characters

### Date Handling: RPGLE → Modern Stack

| DDS Field | DDS Type | Format | SQL Type | Java Type | TypeScript | Display Format |
|-----------|----------|--------|----------|-----------|------------|----------------|
| `LASTORDER` | `8P 0` | YYYYMMDD | `INTEGER` | `Integer` | `number` | Format in UI layer |

**Date Transformation Strategy:**
- **Storage:** Integer in YYYYMMDD format (e.g., 20261225)
- **Database:** `INTEGER` column (LASTORDER)
- **Java:** `Integer` type (no conversion in entity)
- **API:** JSON number (20261225)
- **TypeScript:** `number` type
- **Display:** Format to locale string in React component

**Example Conversion:**
```typescript
// In React component
const formatDate = (yyyymmdd: number | null): string => {
  if (!yyyymmdd) return 'N/A';
  const year = Math.floor(yyyymmdd / 10000);
  const month = Math.floor((yyyymmdd % 10000) / 100);
  const day = yyyymmdd % 100;
  return new Date(year, month - 1, day).toLocaleDateString();
};

// Usage
formatDate(20261225) // → "12/25/2026" (US locale)
```

### Null Handling Across Layers

| Layer | Required Fields | Optional Fields | Validation |
|-------|----------------|-----------------|------------|
| **DDS** | No explicit nulls | Defaults to blanks/zeros | - |
| **SQL** | `NOT NULL` constraint | Nullable columns | Database enforced |
| **JPA Entity** | `@NotNull`, `@NotBlank` | Java `null` allowed | Jakarta Bean Validation |
| **DTO** | Java record (non-null) | Java `null` allowed | - |
| **API** | JSON field present | JSON `null` allowed | - |
| **TypeScript** | Required in interface | `string \| null`, `number \| null` | Compile-time checking |
| **React** | Props validation | Optional chaining `?.` | Runtime safety |

**Key Differences:**
- **DDS:** No concept of `null` - uses blank strings or zero values
- **Modern Stack:** Explicit `null` handling at every layer
- **Migration Note:** Empty DDS fields converted to SQL `NULL`

### Precision and Format Changes

#### Numeric Precision

| Field | DDS | SQL | Java | TypeScript | Precision Change |
|-------|-----|-----|------|------------|------------------|
| `BALANCE` | `9P 2` | `DECIMAL(9,2)` | `BigDecimal` | `number` | Java maintains precision, TS uses floating point |
| `CUSTNO` | `5P 0` | `BIGINT` | `Long` | `number` | Widened from 5 digits to 64-bit |
| `ZIP` | `5P 0` | `INTEGER` | `Integer` | `number` | Maintains 32-bit integer range |

**Important:** `BigDecimal` in Java prevents floating-point errors in currency calculations. TypeScript receives formatted number from JSON.

#### String Length Constraints

| Field | DDS Max | SQL Max | Java Validation | TypeScript | Enforcement |
|-------|---------|---------|-----------------|------------|-------------|
| `CUSTNAME` | 30 | 30 | `@Size(max = 30)` | `string` | Bean Validation + DB constraint |
| `PHONE` | 12 | 12 | `@Size(max = 12)` | `string` | Bean Validation + DB constraint |
| `STATE` | 2 | 2 | `@Size(min = 2, max = 2)` | `string` | Bean Validation + DB constraint |

**Validation Layers:**
1. **Database:** Length constraint on `VARCHAR(n)`
2. **Java Entity:** `@Size` annotation (pre-insert/update)
3. **TypeScript:** Zod schema validation (pre-API call)
4. **React Form:** Input maxLength attribute

## Field-Level Notes and Special Cases

### Fields with Special Handling

#### Customer ID (CUSTNO → customerId)
- **No Auto-Generation:** Unlike typical JPA entities, ID is NOT auto-generated
- **Reason:** Preserves original AS/400 customer numbers during migration
- **Validation:** Must be manually assigned before persistence
- **Range:** 0 to 99,999 (5 digits)

#### State Code (STATE → state)
- **Constraints:** Exactly 2 uppercase letters
- **Validation:**
  - SQL: `CHAR(2)` (fixed width)
  - Java: `@Pattern(regexp = "[A-Z]{2}")`
  - TypeScript: Zod schema with regex
- **Example:** "IL", "NY", "CA"

#### Account Balance (BALANCE → accountBalance)
- **Currency Field:** Uses `BigDecimal` for precision
- **Decimal Places:** Always 2 (enforced by `DECIMAL(9,2)`)
- **Display:** Format with currency symbol in UI
- **Validation:** Business logic ensures balance ≤ creditLimit

#### Last Order Date (LASTORDER → lastOrderDate)
- **Format:** YYYYMMDD integer (e.g., 20261225)
- **Type:** `Integer` (not Java `LocalDate`)
- **Reason:** Preserves exact DDS format for traceability
- **Display:** Convert to locale date string in UI layer

### Fields Added in Modern Stack (Not in DDS)

**None.** This transformation maintains exact field parity with legacy system.

### DDS Fields Not Implemented in Modern Stack

**None.** All CUSTMAST physical file fields are fully implemented across all layers.

**Important:** While `CREDITLIM` and `LASTORDER` exist in the CUSTMAST database table and JPA entity, they are NOT displayed in the original CUSTDSP.dds DETAIL record format. The React UI maintains functional equivalence by only displaying the 8 fields that appear in the green screen: customer number, name, address, city, state, zip, phone, and balance. This is an intentional design decision from the original RPGLE program, not an implementation gap.

### Default Values and Null Handling

| Field | DDS Default | SQL Default | Java Default | Behavior |
|-------|-------------|-------------|--------------|----------|
| `CUSTNO` | (required) | No default | No default | Must be provided |
| `CUSTNAME` | Blank | No default | No default | Must be provided |
| `ADDR1` | Blank | `NULL` | `null` | Optional |
| `CITY` | Blank | `NULL` | `null` | Optional |
| `STATE` | Blank | `NULL` | `null` | Optional |
| `ZIP` | Zero | `NULL` | `null` | Optional (null = not set) |
| `PHONE` | Blank | `NULL` | `null` | Optional |
| `BALANCE` | Zero | `NULL` | `null` | Optional (null = unknown) |
| `CREDITLIM` | Zero | `NULL` | `null` | Optional (null = no limit) |
| `LASTORDER` | Zero | `NULL` | `null` | Optional (null = never ordered) |

**Migration Note:** 
- DDS blank strings → SQL `NULL`
- DDS zero values → SQL `NULL` (except where zero is meaningful)
- Modern stack prefers explicit `null` over "magic values"

### Performance Considerations

#### Database Indexes

```sql
-- Primary key index (automatic)
PRIMARY KEY (CUSTNO)

-- Search optimization
CREATE INDEX IDX_CUSTMAST_NAME ON CUSTMAST(CUSTNAME);
```

**Index Usage:**
- `CUSTNO` (PK): Lookup by ID - O(log n)
- `CUSTNAME`: Search by name - O(log n)
- Full table scan avoided for common queries

#### JPA Fetch Strategies

```java
// Default EAGER fetch for all fields (simple entity, no relationships)
// No lazy loading needed - all fields loaded in single query
```

### Cross-References

#### Business Logic Validation

See [business-logic-mapping.md](business-logic-mapping.md) for:
- Balance ≤ Credit Limit validation (Rule: BALANCE_CHECK)
- State code validation against valid state table
- Customer number format validation
- Phone number format validation

#### Transformation Analysis

See [analysis.md](analysis.md) for:
- Program flow overview
- RPGLE to Java architectural mapping
- Display file to React UI mapping
- Complete transformation strategy

### Field Usage Patterns

#### Read-Only Fields (Output Only in DDS)

All fields in `DETAIL` record format are output-only (display-only):
- Customer data is retrieved, not edited
- Fields are rendered as text, not input elements
- React components use `<span>` or `<div>`, not `<input>`

#### Input Fields (Both I/O in DDS)

Only `PCUSTNO` in `PROMPT` record format accepts user input:
- User enters customer number to search
- React uses `<input type="number">` with form validation
- Submit triggers API call to fetch customer data

### Backward Compatibility

**Principle:** Modern stack maintains compatibility with AS/400 data

**Preserved Elements:**
- Customer ID values (no renumbering)
- Date format (YYYYMMDD integers)
- Field lengths and precision
- Data validation rules

**Breaking Changes:**
- None - full backward compatibility maintained

---

## Summary

This data mapping provides complete traceability from DDS source files through every layer of the modern stack:

1. **DDS Physical File (CUSTMAST.dds)** → SQL table schema
2. **SQL Schema** → JPA entity annotations
3. **JPA Entity** → DTO record
4. **DTO** → JSON API response
5. **API Response** → TypeScript types
6. **TypeScript Types** → React component props

**Key Takeaways:**
- SQL preserves DDS names for traceability
- Java/TypeScript use readable names
- Every layer includes DDS field comments
- Type safety enforced end-to-end
- Null handling explicit in modern stack
- Full parity with legacy data structures

**For Developers:**
- Reference this document when adding new fields
- Maintain naming conventions: DDS names in SQL, readable names in code
- Always include DDS field references in comments
- Follow type transformation patterns for consistency
