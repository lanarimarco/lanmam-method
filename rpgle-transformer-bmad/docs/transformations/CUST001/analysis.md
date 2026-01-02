# CUST001 Transformation Analysis

> **Target Audience**: Developers with Java/Spring Boot and React experience but **ZERO RPGLE knowledge**  
> **Purpose**: Understand the CUST001 customer inquiry transformation without reading RPGLE code

---

## Table of Contents

1. [What is CUST001?](#what-is-cust001)
2. [Business Functionality](#business-functionality)
3. [Input/Output Behavior](#inputoutput-behavior)
4. [Original RPGLE Architecture](#original-rpgle-architecture)
5. [Modern Architecture](#modern-architecture)
6. [Transformation Mapping](#transformation-mapping)
7. [Key Design Decisions](#key-design-decisions)
8. [References](#references)

---

## What is CUST001?

**CUST001** is a customer inquiry program originally written in RPGLE that runs on IBM AS/400 (iSeries) systems. Think of it as a simple "lookup tool" where users can:

1. Enter a customer number
2. View that customer's complete information (name, address, phone, account balance)
3. See an error if the customer doesn't exist
4. Search for another customer (repeat the process)

**Business Context**: This is a read-only lookup tool used by customer service representatives, sales teams, or support staff who need quick access to customer information without modifying data.

**Why Transform It?**: The AS/400 platform is legacy technology. This transformation moves the functionality to a modern web-based stack (Spring Boot + React) that can run on any platform, integrate with modern systems, and provide a better user experience.

---

## Business Functionality

### What Does CUST001 Do?

CUST001 provides a **customer inquiry workflow**:

- **Primary Function**: Look up customer details by customer number
- **Read-Only**: No data modification, strictly inquiry/viewing
- **Single Record**: Displays one customer at a time (not a list or search)
- **Simple Validation**: Ensures customer number is provided and valid
- **User Feedback**: Shows clear error messages when customer is not found

### Who Uses It?

- Customer service representatives answering customer calls
- Sales teams verifying customer account information
- Support staff checking customer details before providing assistance
- Any employee who needs quick access to customer master data

### What Business Value Does It Provide?

- **Fast customer lookups** without navigating complex systems
- **Accurate information** from the authoritative customer master database
- **Simple interface** that requires minimal training
- **Consistent experience** across all users

---

## Input/Output Behavior

### User Interaction Flow

```
1. User sees a search screen with one field: "Customer Number"
   ├─ User enters a customer number (e.g., 1001)
   └─ User presses Enter or clicks Submit

2. System validates the input
   ├─ If customer number is blank/zero → Show error: "Customer number required"
   └─ If customer number is invalid format → Show error: "Customer ID must be a positive number"

3. System searches for the customer in the database
   ├─ If found → Display customer details screen
   └─ If not found → Show error: "Customer not found"

4. User can:
   ├─ Press F3 to exit the program
   ├─ Press F12 to return to the search screen (from detail screen)
   └─ Enter a new customer number to search again
```

### Input Specifications

**Customer Number Field**:
- **Type**: Numeric (5 digits maximum)
- **Range**: 1 to 99999
- **Required**: Yes
- **Format**: Zero-padded display (e.g., 00001, 01001)
- **Validation**: Must be positive integer, non-zero

### Output Specifications

**Customer Detail Display** (when customer is found):

| Field Name       | Type                  | Description                        | Example             |
|------------------|-----------------------|------------------------------------|---------------------|
| Customer Number  | 5-digit integer       | Unique customer identifier         | 1001                |
| Name             | Text (30 characters)  | Customer's full name               | John Smith          |
| Address          | Text (30 characters)  | Street address line 1              | 123 Main Street     |
| City             | Text (20 characters)  | City name                          | Springfield         |
| State            | 2-letter code         | US state abbreviation              | IL                  |
| Zip Code         | 5-digit integer       | US ZIP code                        | 62701               |
| Phone Number     | Text (12 characters)  | Phone number                       | 555-123-4567        |
| Account Balance  | Currency (9 digits)   | Current account balance (2 decimals)| $1,250.50          |

**Error Messages**:
- `"Customer number required"` - When customer number is blank or zero
- `"Customer ID must be a positive number"` - When customer number is invalid format
- `"Customer not found"` - When customer number doesn't exist in the database

---

## Original RPGLE Architecture

### Technology Stack

The original CUST001 uses IBM AS/400 (iSeries) technology:

| Component Type    | Technology Used       | Purpose                                  |
|-------------------|-----------------------|------------------------------------------|
| Programming       | RPGLE                 | Business logic and program flow          |
| Database          | DB2/400               | Relational database on AS/400            |
| Data Definition   | DDS (Physical Files)  | Define database table structure          |
| User Interface    | DDS (Display Files)   | Define green-screen terminal layouts     |
| Runtime           | AS/400 OS             | Operating system and runtime environment |

### Source Files

The CUST001 program consists of three source files:

1. **CUST001.rpgle** (Program Logic)
   - Location: `source-rpgle/programs/CUST001.rpgle`
   - Purpose: Main program containing business logic and workflow
   - Lines of Code: ~60 lines

2. **CUSTMAST.dds** (Database Structure)
   - Location: `source-rpgle/dds/physical-files/CUSTMAST.dds`
   - Purpose: Defines the CUSTMAST table structure (customer master data)
   - Record Format: CUSTREC (10 fields)

3. **CUSTDSP.dds** (User Interface)
   - Location: `source-rpgle/dds/display-files/CUSTDSP.dds`
   - Purpose: Defines two 24x80 green-screen layouts
   - Record Formats: PROMPT (search screen), DETAIL (display screen)

### Program Flow (RPGLE)

```
START PROGRAM
  │
  ├─ Open Files (CUSTMAST database, CUSTDSP display)
  │
  ├─ Display PROMPT screen (customer number entry)
  │
  └─ Loop until F3 (Exit) pressed:
      │
      ├─ Read user input from PROMPT screen
      │
      ├─ Validate customer number
      │   ├─ If zero/blank → Display error, loop back
      │   └─ If valid → Continue
      │
      ├─ CHAIN to CUSTMAST (database lookup by customer number)
      │
      ├─ Check if record found
      │   ├─ If FOUND → Populate DETAIL screen fields
      │   │             Display DETAIL screen
      │   │             Wait for F12 (Return)
      │   └─ If NOT FOUND → Display "Customer not found" error
      │
      └─ Display PROMPT screen again (for next search)
      
END PROGRAM
```

### Key RPGLE Concepts (Explained for Java Developers)

| RPGLE Concept           | What It Means                              | Java/Spring Equivalent          |
|-------------------------|--------------------------------------------|---------------------------------|
| File Declaration (F-spec) | Declare which files program uses         | `@Autowired` dependencies       |
| CHAIN operation         | Database lookup by primary key             | `repository.findById()`         |
| %FOUND function         | Check if CHAIN found a record              | `Optional.isPresent()`          |
| ExFmt operation         | Display screen and wait for user input     | HTTP Request/Response cycle     |
| Indicator (*IN03, *IN90)| Boolean flags for program control          | Boolean variables               |
| DoW loop                | Do While loop                              | `while` loop or event handlers  |

---

## Modern Architecture

### Technology Stack

The transformed CUST001 uses a modern three-tier web architecture:

| Layer              | Technology                    | Purpose                                  |
|--------------------|-------------------------------|------------------------------------------|
| **Backend API**    | Java 21 + Spring Boot 3.4.1   | Business logic and REST API              |
| **Database**       | PostgreSQL                    | Relational database (CUSTMAST table)     |
| **Frontend UI**    | React 19.2.1 + TypeScript     | Modern web-based user interface          |
| **HTTP Client**    | Fetch API + React Query       | API communication and caching            |
| **Styling**        | Tailwind CSS                  | Responsive, modern UI styling            |

### Project Structure

```
backend/
├── src/main/java/com/smeup/backend/
│   ├── entity/Customer.java              # Database entity (CUSTMAST record)
│   ├── repository/CustomerRepository.java # JPA repository (database access)
│   ├── service/CustomerService.java      # Business logic (CHAIN operation)
│   ├── controller/CustomerController.java # REST API endpoint
│   ├── dto/CustomerDTO.java              # API response format
│   ├── mapper/CustomerMapper.java        # Entity ↔ DTO conversion
│   └── exception/
│       ├── CustomerNotFoundException.java # "Customer not found" error
│       └── InvalidCustomerIdException.java # Validation error
│
└── src/main/resources/db/migration/
    ├── V1__create_custmast_table.sql     # Database schema (from CUSTMAST.dds)
    └── V2__insert_test_data.sql          # Sample customer data

frontend/
└── src/
    ├── api/customerApi.ts                # API client (HTTP requests)
    ├── types/Customer.ts                 # TypeScript types (DDS field mapping)
    └── features/customers/
        ├── CustomerInquiry.tsx           # Main page (orchestrates workflow)
        ├── CustomerSearch.tsx            # Search form (PROMPT screen)
        ├── CustomerDetail.tsx            # Detail display (DETAIL screen)
        └── useCustomer.ts                # React Query hook (CHAIN operation)
```

### Application Flow (Modern Stack)

```
USER INTERACTION
  │
  ├─ User opens browser → http://localhost:5173/customers
  │
  └─ CustomerInquiry page loads
      │
      ├─ Renders CustomerSearch component (search form)
      │
      ├─ User enters customer number → clicks "Search"
      │
      ├─ useCustomer hook triggers API call
      │   │
      │   └─ HTTP GET → http://localhost:8080/api/v1/customers/{id}
      │       │
      │       └─ Backend Flow:
      │           │
      │           ├─ CustomerController receives request
      │           │
      │           ├─ CustomerService.findCustomerById()
      │           │   ├─ Validates customer ID (RPGLE: If CUSTNO = 0)
      │           │   └─ Calls CustomerRepository.findById()
      │           │       │
      │           │       └─ JPA/Hibernate queries PostgreSQL (RPGLE: CHAIN CUSTMAST)
      │           │
      │           ├─ If found:
      │           │   └─ Map Customer → CustomerDTO → Return JSON (HTTP 200)
      │           │
      │           └─ If not found:
      │               └─ Throw CustomerNotFoundException → Return error (HTTP 404)
      │
      ├─ React Query processes response
      │   ├─ Success → Update state with customer data
      │   └─ Error → Update state with error message
      │
      └─ CustomerDetail component renders
          ├─ Shows customer details (if found)
          └─ Shows error message (if not found)
```

---

## Transformation Mapping

### RPGLE → Spring Boot Mapping

| RPGLE Component                      | Modern Component                          | Purpose                          |
|--------------------------------------|-------------------------------------------|----------------------------------|
| CUSTMAST.dds (Physical File)         | `Customer.java` (JPA Entity)              | Database table/record structure  |
| CHAIN CUSTMAST operation             | `repository.findById()`                   | Database lookup by primary key   |
| %FOUND(CUSTMAST)                     | `Optional.isPresent()`                    | Check if record exists           |
| `If CUSTNO = 0` validation           | `validateCustomerId()` method             | Input validation                 |
| Error indicator *IN90                | Exception throwing                        | Error handling                   |
| PMSG field (error message)           | Exception message in HTTP response        | Error message to user            |

### DDS Display File → React Component Mapping

| DDS Component                        | React Component                           | Purpose                          |
|--------------------------------------|-------------------------------------------|----------------------------------|
| CUSTDSP.dds (Display File)           | `CustomerInquiry.tsx` page                | Overall user interface           |
| PROMPT record format                 | `CustomerSearch.tsx` component            | Customer number entry screen     |
| DETAIL record format                 | `CustomerDetail.tsx` component            | Customer detail display screen   |
| PCUSTNO input field                  | `<input type="number">` in search form    | Customer number input            |
| D* output fields                     | Customer detail display divs              | Read-only customer data          |
| F3=Exit function key                 | Browser back button / navigation          | Exit functionality               |
| F12=Return function key              | "Search Again" button                     | Return to search                 |

### Data Field Mapping (CUSTMAST → Customer Entity)

| DDS Field Name | DDS Type | Java Field Name   | Java Type      | Notes                          |
|----------------|----------|-------------------|----------------|--------------------------------|
| CUSTNO         | 5P 0     | customerId        | Long           | Primary Key (not auto-generated)|
| CUSTNAME       | 30A      | customerName      | String         | Max length: 30                 |
| ADDR1          | 30A      | addressLine1      | String         | Max length: 30                 |
| CITY           | 20A      | city              | String         | Max length: 20                 |
| STATE          | 2A       | state             | String         | Exactly 2 characters, uppercase|
| ZIP            | 5P 0     | zipCode           | Integer        | 5 digits (0-99999)             |
| PHONE          | 12A      | phoneNumber       | String         | Max length: 12                 |
| BALANCE        | 9P 2     | accountBalance    | BigDecimal     | Currency, 2 decimal places     |
| CREDITLIM      | 9P 2     | creditLimit       | BigDecimal     | Currency, 2 decimal places     |
| LASTORDER      | 8P 0     | lastOrderDate     | Integer        | Date in YYYYMMDD format        |

**Type Conversion Notes**:
- DDS `P` (Packed Decimal) → Java `Long` or `Integer` for whole numbers, `BigDecimal` for currency
- DDS `A` (Alphanumeric) → Java `String`
- Field names converted to camelCase following Java conventions
- CUSTNO mapped to `customerId` for clarity (more descriptive than just `custno`)

---

## Key Design Decisions

### 1. RESTful API Design

**Decision**: Use REST instead of RPC-style endpoints  
**Rationale**: 
- Industry standard for web APIs
- Resource-oriented (customers are resources)
- HTTP verbs clearly express intent (GET for read-only inquiry)
- Easy to extend (POST for create, PUT for update in future)

**Endpoint**: `GET /api/v1/customers/{customerId}`

### 2. Database Migration Strategy

**Decision**: Use Flyway for schema versioning  
**Rationale**:
- Track database schema changes in version control
- Repeatable deployments across environments
- Database state is code, not manual scripts
- Easy rollback if needed

**Files**:
- `V1__create_custmast_table.sql` - Creates CUSTMAST table from DDS structure
- `V2__insert_test_data.sql` - Loads sample customer records

### 3. Exception Handling Strategy

**Decision**: Use specific exception classes instead of error codes  
**Rationale**:
- More idiomatic in Java/Spring Boot
- Automatically maps to HTTP status codes (404 for not found, 400 for validation)
- Clearer intent than RPGLE indicators (*IN90)
- Easier to unit test

**Custom Exceptions**:
- `CustomerNotFoundException` (404) - Customer ID doesn't exist
- `InvalidCustomerIdException` (400) - Customer ID validation failed

### 4. Frontend State Management

**Decision**: Use React Query instead of local useState for API data  
**Rationale**:
- Handles loading, error, and success states automatically
- Built-in caching (same customer lookup doesn't hit API twice)
- Automatic background refetching keeps data fresh
- Reduces boilerplate compared to manual fetch + useState

### 5. Type Safety with TypeScript

**Decision**: Define `Customer` interface matching backend DTO  
**Rationale**:
- Catch type errors at compile time, not runtime
- IntelliSense/autocomplete in IDE
- Enforces contract between frontend and backend
- Easier refactoring

### 6. Preserve Original Customer IDs

**Decision**: No `@GeneratedValue` on Customer.customerId  
**Rationale**:
- Preserve original AS/400 customer numbers during migration
- Existing customers expect to use same customer numbers
- External systems may reference these IDs
- Manual assignment matches RPGLE behavior (IDs assigned by business process, not database)

### 7. Styling with Tailwind CSS

**Decision**: Use Tailwind utility classes instead of custom CSS  
**Rationale**:
- Rapid development with pre-built utilities
- Responsive design built-in
- No naming conflicts (no global CSS class names)
- Modern, professional look replaces green-screen aesthetic

---

## References

### Source Files (Original RPGLE)

- **Program**: [`source-rpgle/programs/CUST001.rpgle`](../../../source-rpgle/programs/CUST001.rpgle)
- **Physical File**: [`source-rpgle/dds/physical-files/CUSTMAST.dds`](../../../source-rpgle/dds/physical-files/CUSTMAST.dds)
- **Display File**: [`source-rpgle/dds/display-files/CUSTDSP.dds`](../../../source-rpgle/dds/display-files/CUSTDSP.dds)

### Generated Backend Code

- **Entity**: [`backend/src/main/java/com/smeup/backend/entity/Customer.java`](../../../backend/src/main/java/com/smeup/backend/entity/Customer.java)
- **Repository**: [`backend/src/main/java/com/smeup/backend/repository/CustomerRepository.java`](../../../backend/src/main/java/com/smeup/backend/repository/CustomerRepository.java)
- **Service**: [`backend/src/main/java/com/smeup/backend/service/CustomerService.java`](../../../backend/src/main/java/com/smeup/backend/service/CustomerService.java)
- **Controller**: [`backend/src/main/java/com/smeup/backend/controller/CustomerController.java`](../../../backend/src/main/java/com/smeup/backend/controller/CustomerController.java)
- **DTO**: [`backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java`](../../../backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java)
- **Mapper**: [`backend/src/main/java/com/smeup/backend/mapper/CustomerMapper.java`](../../../backend/src/main/java/com/smeup/backend/mapper/CustomerMapper.java)

### Generated Frontend Code

- **Page**: [`frontend/src/features/customers/CustomerInquiry.tsx`](../../../frontend/src/features/customers/CustomerInquiry.tsx)
- **Search Component**: [`frontend/src/features/customers/CustomerSearch.tsx`](../../../frontend/src/features/customers/CustomerSearch.tsx)
- **Detail Component**: [`frontend/src/features/customers/CustomerDetail.tsx`](../../../frontend/src/features/customers/CustomerDetail.tsx)
- **React Query Hook**: [`frontend/src/features/customers/useCustomer.ts`](../../../frontend/src/features/customers/useCustomer.ts)
- **API Client**: [`frontend/src/api/customerApi.ts`](../../../frontend/src/api/customerApi.ts)
- **TypeScript Types**: [`frontend/src/types/Customer.ts`](../../../frontend/src/types/Customer.ts)

### Database Migrations

- **Schema**: [`backend/src/main/resources/db/migration/V1__create_custmast_table.sql`](../../../backend/src/main/resources/db/migration/V1__create_custmast_table.sql)
- **Test Data**: [`backend/src/main/resources/db/migration/V2__insert_test_data.sql`](../../../backend/src/main/resources/db/migration/V2__insert_test_data.sql)

### Testing

- **Backend Unit Tests**: [`backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java`](../../../backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java)
- **Backend Integration Tests**: [`backend/src/test/java/com/smeup/backend/integration/CustomerIntegrationIT.java`](../../../backend/src/test/java/com/smeup/backend/integration/CustomerIntegrationIT.java)
- **Frontend Component Tests**: [`frontend/src/features/customers/__tests__/CustomerInquiry.test.tsx`](../../../frontend/src/features/customers/__tests__/CustomerInquiry.test.tsx)
- **Functional Equivalence Tests**: [`backend/src/test/java/com/smeup/backend/equivalence/CustomerEquivalenceTest.java`](../../../backend/src/test/java/com/smeup/backend/equivalence/CustomerEquivalenceTest.java)

### Planning Documents

- **Epic Definition**: [`_bmad-output/planning-artifacts/epics.md`](../../../_bmad-output/planning-artifacts/epics.md) - Epic 2 (Backend), Epic 3 (Frontend)
- **PRD**: [`_bmad-output/planning-artifacts/prd.md`](../../../_bmad-output/planning-artifacts/prd.md) - FR1-FR19 (Customer Inquiry Requirements)
- **Architecture**: [`_bmad-output/planning-artifacts/architecture.md`](../../../_bmad-output/planning-artifacts/architecture.md)

---

## Summary

The CUST001 transformation successfully converts a legacy AS/400 customer inquiry program into a modern, web-based application while preserving 100% functional equivalence. The transformation:

✅ **Maintains Business Logic**: All validation rules and error handling preserved  
✅ **Improves User Experience**: Green-screen → modern, responsive web UI  
✅ **Enables Integration**: REST API allows other systems to query customer data  
✅ **Ensures Quality**: Comprehensive test suites validate functional equivalence  
✅ **Documents Traceability**: Every component includes RPGLE/DDS references  

Developers maintaining this code can understand the business functionality and make changes confidently **without any RPGLE knowledge**, relying on this documentation and the inline comments in the generated code.
