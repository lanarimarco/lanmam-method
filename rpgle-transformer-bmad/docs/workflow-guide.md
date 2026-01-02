# RPGLE Transformation Workflow Guide

**Version:** 1.0  
**Last Updated:** 2026-01-02  
**Target Audience:** Java/Spring/React developers with ZERO RPGLE knowledge

---

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Transformation Phases](#transformation-phases)
   - [Phase 1: Preparation](#phase-1-preparation)
   - [Phase 2: Semantic Analysis](#phase-2-semantic-analysis-ai-assisted)
   - [Phase 3: Backend Transformation](#phase-3-backend-transformation)
   - [Phase 4: Frontend Transformation](#phase-4-frontend-transformation)
   - [Phase 5: Testing](#phase-5-testing)
   - [Phase 6: Validation](#phase-6-validation)
   - [Phase 7: Documentation & Integration](#phase-7-documentation--integration)
4. [Troubleshooting](#troubleshooting)
5. [CUST001 Reference Examples](#cust001-reference-examples)

---

## Overview

### Purpose

This guide enables developers to transform legacy RPGLE programs into modern Java/Spring Boot backend and React/TypeScript frontend applications **without needing to understand RPGLE**.

You will leverage AI tools (Claude, GitHub Copilot) to analyze the semantic meaning of RPGLE code and follow proven patterns from the CUST001 reference implementation.

### Key Principles

1. **AI-Assisted Transformation**: Use AI to analyze RPGLE semantics - you don't need to read or understand RPGLE syntax
2. **Pattern-Based Implementation**: Follow CUST001 patterns for consistent, predictable results
3. **Validation-First**: Test-driven approach ensures functional equivalence with original RPGLE
4. **Documentation-Driven**: Comprehensive inline docs enable long-term maintenance without RPGLE knowledge

### Success Criteria

✅ Transformation produces functionally equivalent results to RPGLE original  
✅ All tests pass (unit, integration, E2E)  
✅ Code coverage ≥ 80%  
✅ All linters pass with zero violations  
✅ Complete documentation with RPGLE traceability

---

## Prerequisites

Before starting a transformation, ensure you have:

### Development Environment

- ✅ Java 21 LTS installed
- ✅ Maven 3.9+ installed
- ✅ Node.js 20+ and npm installed
- ✅ PostgreSQL 16.x running locally
- ✅ IDE configured (IntelliJ IDEA or VS Code recommended)
- ✅ Git configured with repository access

### Project Setup

- ✅ Backend project initialized (Spring Boot 4.0.1)
- ✅ Frontend project initialized (React 19 + Vite)
- ✅ Testing infrastructure configured (JUnit, Testcontainers, Vitest, Playwright)
- ✅ Code quality tools configured (Checkstyle, ESLint, Prettier)

### AI Tools Access

- ✅ Access to Claude or GitHub Copilot for semantic analysis
- ✅ Familiarity with prompting AI for code generation

### Documentation Available

- ✅ `docs/standards/` - Documentation standards
- ✅ `docs/transformations/CUST001/` - Reference transformation
- ✅ `project-context.md` - Project-wide coding rules

---

## Transformation Phases

<!-- To be filled in subsequent tasks -->

---

## Phase 1: Preparation

### 1.1 Identify the RPGLE Program to Transform

**Goal:** Select the next RPGLE program from your backlog and understand its scope.

**Steps:**

1. **Check Sprint Status**: Review `_bmad-output/implementation-artifacts/sprint-status.yaml` for the next backlog story
2. **Locate RPGLE Source**: Find the program file in `source-rpgle/programs/`
3. **Identify Program Name**: Note the program identifier (e.g., `CUST001` for customer inquiry)

**Example:**
```bash
# Next program to transform
Program: CUST001
Location: source-rpgle/programs/CUST001.rpgle
Purpose: Customer inquiry program
```

### 1.2 Locate All Required Input Files

**Goal:** Gather all DDS (Data Description Specifications) files needed for transformation.

You'll need three types of files (you don't need to understand their contents):

1. **DDS Physical Files** - Define database tables (like SQL CREATE TABLE)
2. **DDS Display Files** - Define screen layouts (like HTML forms)
3. **RPGLE Program** - Contains business logic

**How to Find Files:**

```bash
# Navigate to source RPGLE directory
cd source-rpgle/

# Look for DDS physical files (database definitions)
ls dds/physical-files/

# Look for DDS display files (screen definitions)  
ls dds/display-files/

# Look for the RPGLE program
ls programs/
```

**CUST001 Example:**

| File Type | File Name | Location | Purpose |
|-----------|-----------|----------|---------|
| RPGLE Program | `CUST001.rpgle` | `source-rpgle/programs/` | Business logic |
| DDS Physical | `CUSTMAST.dds` | `source-rpgle/dds/physical-files/` | Customer table definition |
| DDS Display | `CUSTDSP.dds` | `source-rpgle/dds/display-files/` | Screen layout |

**Checklist:**

- [ ] RPGLE program file exists (`.rpgle` extension)
- [ ] All referenced DDS physical files exist (check program comments or ask AI to identify)
- [ ] Display file exists (usually same name as program with `.dspf` extension)
- [ ] Files are readable and not corrupted

**If Files Are Missing:**

1. Check alternate locations (sometimes files are in subdirectories)
2. Look for similar named files (e.g., `CUSTMST` vs `CUSTMAST`)
3. Check with team lead or consult legacy system documentation
4. **HALT:** Cannot proceed without required input files

### 1.4 Review Similar Transformations (CUST001 Reference)

**Goal:** Understand the transformation pattern you'll follow.

Before starting, review the CUST001 reference implementation:

**Backend Structure:**
```bash
backend/src/main/java/com/smeup/backend/
├── controller/
│   └── CustomerController.java       # REST endpoints
├── service/
│   └── CustomerService.java          # Business logic
├── repository/
│   └── CustomerRepository.java       # Data access
├── entity/
│   └── Customer.java                 # JPA entity (maps to CUSTMAST table)
├── dto/
│   └── CustomerDTO.java              # API response format
└── mapper/
    └── CustomerMapper.java           # Entity/DTO mapping
```

**Frontend Structure:**
```bash
frontend/src/features/customers/
├── CustomerInquiry.tsx               # Main page component
├── CustomerSearch.tsx                # Search form
├── CustomerDetail.tsx                # Detail view
├── customer.types.ts                 # TypeScript types
├── useCustomer.ts                    # React Query hooks
└── index.ts                          # Barrel export
```

**Database Migration:**
```bash
backend/src/main/resources/db/migration/
└── V1__Create_Customer_Table.sql     # Flyway migration
```

**Documentation:**
```bash
docs/transformations/CUST001/
├── analysis.md                       # Transformation analysis
├── business-logic-mapping.md         # Logic mapping
└── data-mapping.md                   # Field mapping
```

**Key Observation:**  
Notice the pattern: **Entity → Repository → Service → Controller** for backend, and **Types → API Client → Hooks → Components** for frontend. You'll follow this exact pattern.

### 1.5 Prepare Your Workspace

**Goal:** Set up environment for efficient transformation work.

```bash
# Ensure you're on a clean branch
git checkout -b feature/transform-<PROGRAM_NAME>

# Pull latest changes
git pull origin master

# Verify backend builds
cd backend
mvn clean install
cd ..

# Verify frontend builds  
cd frontend
npm install
npm run build
cd ..

# Run existing tests to ensure baseline passes
cd backend && mvn test && cd ..
cd frontend && npm test && cd ..
```

**Checkpoint:**

✅ RPGLE program identified  
✅ All DDS files located and verified  
✅ CUST001 reference reviewed  
✅ Clean workspace prepared  
✅ Baseline tests passing

**Next:** Proceed to Phase 2 - Semantic Analysis

---

**💡 Pro Tip:** Create a checklist for each transformation in a markdown file to track progress. See CUST001 story file in `_bmad-output/implementation-artifacts/` for example structure.

---

## Phase 2: Semantic Analysis (AI-Assisted)

### 2.1 Why AI Analysis?

**You don't need to understand RPGLE syntax.** Instead, you'll use AI to extract the semantic meaning - what the program does, not how it's written.

AI tools (Claude, GitHub Copilot) are trained on RPGLE and can:
- Explain business logic in plain English
- Identify data flows and validations
- Extract business rules
- Map screen interactions to REST APIs

### 2.2 Analyze the RPGLE Program

**Goal:** Get AI to explain what the program does in plain English.

**Prompt Template for AI:**

```
I need help understanding this RPGLE program. I don't know RPGLE syntax, 
so please explain in plain English:

1. What is the main purpose of this program?
2. What database tables does it read/write?
3. What are the key business rules and validations?
4. What user interactions does it support?
5. What are the input fields and output fields?
6. Are there any calculations or data transformations?

Here's the RPGLE program:
[paste CUST001.rpgle contents here]

Here are the DDS files it references:
[paste CUSTMAST.dds and CUSTDSP.dds contents here]

Please provide a structured explanation suitable for a Java/React developer.
```

**Expected AI Response (CUST001 Example):**

```markdown
**Program Purpose:** Customer inquiry program - allows users to search 
and view customer details.

**Database Tables:**
- CUSTMAST: Customer master table with fields CUSTID, CUSTNM, CUSTAD1, 
  CUSTAD2, CUSTCY, CUSTST, CUSTPC

**Business Logic:**
1. Search by customer ID or partial name match
2. Validate customer ID is numeric
3. Display customer details if found
4. Show error message if customer not found

**User Interactions:**
- Screen 1: Search form (customer ID or name input)
- Screen 2: Customer detail display (read-only view)

**Key Fields:**
- Input: CUSTID (search), CUSTNM (search)
- Output: All customer fields for display

**Validations:**
- Customer ID must be numeric
- Search requires at least one field populated
```

**Save this analysis** to `docs/transformations/<PROGRAM>/analysis.md` - you'll reference it throughout transformation.

### 2.3 Create Business Logic Mapping

**Goal:** Map RPGLE business rules to Java service methods.

Use the template from `docs/standards/business-logic-mapping-template.md`.

**CUST001 Example:** See `docs/transformations/CUST001/business-logic-mapping.md`

Key mapping:
```
RPGLE: Search subroutine with validation
  ↓
Java: CustomerService.searchCustomers(CustomerSearchDTO) 
  - Validates search criteria
  - Delegates to repository
  - Returns CustomerDTO list
```

### 2.4 Create Data Mapping

**Goal:** Map DDS field names to Java entity fields and TypeScript types.

**CUST001 Example:**

| DDS Field | DDS Type | Java Entity Field | Java Type | TypeScript Type |
|-----------|----------|-------------------|-----------|-----------------|
| CUSTID | Decimal(9,0) | customerId | Long | number |
| CUSTNM | Char(50) | customerName | String | string |
| CUSTAD1 | Char(50) | addressLine1 | String | string |
| CUSTAD2 | Char(50) | addressLine2 | String | string |
| CUSTCY | Char(30) | city | String | string |
| CUSTST | Char(2) | state | String | string |
| CUSTPC | Char(10) | postalCode | String | string |

**Rule:** Table name stays DDS (`CUSTMAST`), field names stay DDS in `@Column`, but Java/TypeScript uses readable names.

See full example: `docs/transformations/CUST001/data-mapping.md`

### 2.5 Document Transformation Approach

Create `docs/transformations/<PROGRAM>/analysis.md` with:

1. **Program Overview** (from AI analysis)
2. **Transformation Strategy** (which patterns you'll use)
3. **Technical Decisions** (libraries, approaches, edge cases)
4. **Risks & Mitigations**

**Checkpoint:**

✅ AI analysis complete  
✅ Business logic mapped  
✅ Data mapping complete  
✅ Transformation approach documented

**Next:** Proceed to Phase 3 - Backend Transformation

---

## Phase 3: Backend Transformation

**Pattern:** Entity → Migration → Repository → Service → Controller → DTO

### 3.1 Create JPA Entity from DDS Physical File

**Goal:** Generate Java entity representing database table.

**Steps:**

1. Use AI to convert DDS physical file to JPA entity
2. Apply project naming conventions
3. Add RPGLE traceability comments

**AI Prompt Template:**

```
Convert this DDS physical file to a JPA entity following these rules:

1. @Entity annotation
2. @Table(name = "CUSTMAST") - use DDS file name
3. @Column(name = "CUSTID") - use DDS field names
4. Java field names: camelCase readable names (customerId, customerName)
5. Map DDS types to Java types correctly (Decimal → Long, Char → String)
6. Add @Id and @GeneratedValue where appropriate
7. Include JavaDoc with RPGLE traceability

Here's the DDS physical file:
[paste CUSTMAST.dds]

Use this data mapping:
[paste data mapping table from Phase 2]
```

**Generated Entity Example:**

```java
package com.smeup.backend.entity;

import jakarta.persistence.*;

/**
 * Customer entity mapped from DDS Physical File: CUSTMAST
 * Original fields: CUSTID, CUSTNM, CUSTAD1, CUSTAD2, CUSTCY, CUSTST, CUSTPC
 * Source: source-rpgle/dds/physical-files/CUSTMAST.dds
 * RPGLE Program: CUST001.rpgle
 */
@Entity
@Table(name = "CUSTMAST")
public class Customer {

    @Id
    @Column(name = "CUSTID")
    private Long customerId;

    @Column(name = "CUSTNM", length = 50)
    private String customerName;

    @Column(name = "CUSTAD1", length = 50)
    private String addressLine1;

    @Column(name = "CUSTAD2", length = 50)
    private String addressLine2;

    @Column(name = "CUSTCY", length = 30)
    private String city;

    @Column(name = "CUSTST", length = 2)
    private String state;

    @Column(name = "CUSTPC", length = 10)
    private String postalCode;

    // Constructors, getters, setters (generate with IDE)
}
```

**Location:** `backend/src/main/java/com/smeup/backend/entity/<Entity>.java`

**See CUST001 example:** `backend/src/main/java/com/smeup/backend/entity/Customer.java`

### 3.2 Create Flyway Migration

**Goal:** Generate SQL to create database table.

**AI Prompt:**

```
Create a Flyway migration SQL file to create the CUSTMAST table based on this JPA entity.
Use PostgreSQL syntax.
Include:
- Primary key on CUSTID
- Appropriate data types
- NOT NULL constraints where needed
- Descriptive comments

[paste Customer.java entity]
```

**Migration File:**

```sql
-- Migration: V2__Create_Customer_Table.sql
-- Purpose: Create CUSTMAST customer master table
-- Source: DDS Physical File CUSTMAST.dds
-- Transformed from RPGLE program CUST001

CREATE TABLE CUSTMAST (
    CUSTID BIGINT PRIMARY KEY,
    CUSTNM VARCHAR(50) NOT NULL,
    CUSTAD1 VARCHAR(50),
    CUSTAD2 VARCHAR(50),
    CUSTCY VARCHAR(30),
    CUSTST VARCHAR(2),
    CUSTPC VARCHAR(10)
);

CREATE INDEX idx_custmast_name ON CUSTMAST(CUSTNM);

COMMENT ON TABLE CUSTMAST IS 'Customer master table (from DDS CUSTMAST)';
```

**Location:** `backend/src/main/resources/db/migration/V<n>__<Description>.sql`

**See CUST001 example:** `backend/src/main/resources/db/migration/V1__Create_Customer_Table.sql`

### 3.3 Create Spring Data JPA Repository

**Goal:** Create data access layer.

**Template:**

```java
package com.smeup.backend.repository;

import com.smeup.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Customer entity (CUSTMAST table)
 * Provides data access methods for CUST001 transformation
 * Source: source-rpgle/programs/CUST001.rpgle
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Custom query methods based on RPGLE search logic
    List<Customer> findByCustomerNameContainingIgnoreCase(String name);
    
    // Add other query methods as needed from business logic mapping
}
```

**Location:** `backend/src/main/java/com/smeup/backend/repository/<Entity>Repository.java`

**See CUST001 example:** `backend/src/main/java/com/smeup/backend/repository/CustomerRepository.java`

### 3.4 Create Service Layer with Business Logic

**Goal:** Implement business rules identified in semantic analysis.

**AI Prompt:**

```
Create a Spring service class that implements this business logic:

[paste business logic from analysis.md]

Use this repository:
[paste CustomerRepository.java]

Follow these rules:
- Use @Service annotation
- Use constructor injection
- Add @Transactional for write operations
- Validate inputs as per RPGLE validations
- Return DTOs, not entities
- Include JavaDoc with RPGLE traceability
```

**Service Example:**

```java
package com.smeup.backend.service;

import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.dto.CustomerSearchDTO;
import com.smeup.backend.entity.Customer;
import com.smeup.backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Customer service implementing business logic from RPGLE program CUST001
 * Source: source-rpgle/programs/CUST001.rpgle
 * Business Logic Mapping: docs/transformations/CUST001/business-logic-mapping.md
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Search customers by ID or name
     * Original RPGLE: Search subroutine
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO) {
        // Validation from RPGLE
        if (searchDTO.getCustomerId() == null && 
            (searchDTO.getCustomerName() == null || searchDTO.getCustomerName().isBlank())) {
            throw new IllegalArgumentException("At least one search field required");
        }

        List<Customer> customers;
        
        if (searchDTO.getCustomerId() != null) {
            // Search by ID (exact match)
            customers = customerRepository.findById(searchDTO.getCustomerId())
                .map(List::of)
                .orElse(List.of());
        } else {
            // Search by name (partial match, case-insensitive)
            customers = customerRepository.findByCustomerNameContainingIgnoreCase(
                searchDTO.getCustomerName()
            );
        }

        return customers.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private CustomerDTO convertToDTO(Customer customer) {
        // Mapper logic (or use MapStruct)
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setAddressLine1(customer.getAddressLine1());
        dto.setAddressLine2(customer.getAddressLine2());
        dto.setCity(customer.getCity());
        dto.setState(customer.getState());
        dto.setPostalCode(customer.getPostalCode());
        return dto;
    }
}
```

**Location:** `backend/src/main/java/com/smeup/backend/service/<Entity>Service.java`

**See CUST001 example:** `backend/src/main/java/com/smeup/backend/service/CustomerService.java`

### 3.5 Create DTOs (Data Transfer Objects)

**Goal:** Define API request/response formats.

```java
package com.smeup.backend.dto;

/**
 * Customer search request DTO
 * Maps to CUST001 display file search screen inputs
 */
public class CustomerSearchDTO {
    private Long customerId;
    private String customerName;
    // Getters, setters
}

/**
 * Customer response DTO
 * Maps to CUST001 display file detail screen outputs
 */
public class CustomerDTO {
    private Long customerId;
    private String customerName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    // Getters, setters
}
```

**Location:** `backend/src/main/java/com/smeup/backend/dto/`

### 3.6 Create REST Controller

**Goal:** Expose HTTP endpoints following API conventions.

```java
package com.smeup.backend.controller;

import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.dto.CustomerSearchDTO;
import com.smeup.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Customer REST controller
 * Exposes customer inquiry operations from RPGLE CUST001
 * Source: source-rpgle/programs/CUST001.rpgle
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Search customers by ID or name
     * Original RPGLE: CUST001 search function
     */
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(
        @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) String customerName
    ) {
        CustomerSearchDTO searchDTO = new CustomerSearchDTO();
        searchDTO.setCustomerId(customerId);
        searchDTO.setCustomerName(customerName);
        
        List<CustomerDTO> results = customerService.searchCustomers(searchDTO);
        return ResponseEntity.ok(results);
    }
}
```

**Location:** `backend/src/main/java/com/smeup/backend/controller/<Entity>Controller.java`

**See CUST001 example:** `backend/src/main/java/com/smeup/backend/controller/CustomerController.java`

### 3.7 Follow Documentation Standards

**Critical:** Add inline documentation per `docs/standards/java-documentation-standards.md`

Every class must include:
- Purpose and RPGLE source reference
- Business logic mapping reference
- Key assumptions and validations

**Checkpoint:**

✅ Entity created and annotated  
✅ Migration created  
✅ Repository created  
✅ Service with business logic implemented  
✅ DTOs created  
✅ Controller with REST endpoints created  
✅ Inline documentation added

**Next:** Proceed to Phase 4 - Frontend Transformation

---

## Phase 4: Frontend Transformation

**Pattern:** Types → API Client → Hooks → Components

### 4.1 Create TypeScript Types from DDS Display File

**Goal:** Generate TypeScript interfaces for API communication.

**AI Prompt:**

```
Create TypeScript interfaces for the customer domain based on:
1. This JPA entity [paste Customer.java]
2. This DTO [paste CustomerDTO.java and CustomerSearchDTO.java]

Follow naming: Customer, CustomerSearchParams, CustomerDetailResponse
Use strict types (no 'any')
```

**Generated Types:**

```typescript
/**
 * Customer types generated from RPGLE program CUST001
 * Display file: CUSTDSP.dds
 * Source: source-rpgle/programs/CUST001.rpgle
 */

export interface Customer {
  customerId: number;
  customerName: string;
  addressLine1: string;
  addressLine2: string;
  city: string;
  state: string;
  postalCode: string;
}

export interface CustomerSearchParams {
  customerId?: number;
  customerName?: string;
}

export interface CustomerListResponse {
  data: Customer[];
}
```

**Location:** `frontend/src/features/<module>/<domain>.types.ts`

**See CUST001 example:** `frontend/src/features/customers/customer.types.ts`

### 4.2 Create API Client

**Goal:** Type-safe API client using fetch/axios.

```typescript
/**
 * Customer API client
 * Communicates with backend CustomerController
 * Implements CUST001 customer inquiry operations
 */

import { Customer, CustomerSearchParams } from '../types/customer.types';

const API_BASE = '/api/customers';

export const customerApi = {
  /**
   * Search customers by ID or name
   * Maps to: CustomerController.searchCustomers()
   */
  searchCustomers: async (params: CustomerSearchParams): Promise<Customer[]> => {
    const queryParams = new URLSearchParams();
    if (params.customerId) queryParams.append('customerId', params.customerId.toString());
    if (params.customerName) queryParams.append('customerName', params.customerName);

    const response = await fetch(`${API_BASE}/search?${queryParams}`);
    if (!response.ok) {
      throw new Error(`Search failed: ${response.statusText}`);
    }
    return response.json();
  },
};
```

**Location:** `frontend/src/features/<module>/<domain>Api.ts` (or in shared `api/` folder)

**See CUST001 example:** `frontend/src/api/customerApi.ts` (note: may be in shared api folder)

### 4.3 Create React Query Hooks

**Goal:** Manage server state with React Query.

```typescript
/**
 * Customer hooks using React Query
 * Provides data fetching for CUST001 customer inquiry
 */

import { useQuery } from '@tanstack/react-query';
import { customerApi } from '../api/customerApi';
import { CustomerSearchParams } from '../types/customer.types';

export const useCustomerSearch = (searchParams: CustomerSearchParams) => {
  return useQuery({
    queryKey: ['customers', 'search', searchParams],
    queryFn: () => customerApi.searchCustomers(searchParams),
    enabled: !!(searchParams.customerId || searchParams.customerName),
  });
};
```

**Location:** `frontend/src/features/<module>/use<Domain>.ts`

**See CUST001 example:** `frontend/src/features/customers/useCustomer.ts`

### 4.4 Create Search Form Component

**Goal:** Build form component matching RPGLE display file search screen.

**AI Prompt:**

```
Create a React component for customer search form using:
- React Hook Form for form state
- Zod for validation
- TypeScript strict mode
- Tailwind CSS for styling

Form fields (from CUSTDSP.dds):
- Customer ID (number, optional)
- Customer Name (text, optional)

Validation: At least one field required

Reference: CUST001 display file search screen
```

**Component Example:**

```tsx
/**
 * Customer search form component
 * Maps to CUSTDSP.dds search screen
 * Source: source-rpgle/dds/display-files/CUSTDSP.dds
 */

import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { CustomerSearchParams } from '../types/customer.types';

const searchSchema = z.object({
  customerId: z.number().optional(),
  customerName: z.string().optional(),
}).refine(data => data.customerId || data.customerName, {
  message: "At least one search field is required"
});

interface CustomerSearchProps {
  onSearch: (params: CustomerSearchParams) => void;
}

export const CustomerSearch: React.FC<CustomerSearchProps> = ({ onSearch }) => {
  const { register, handleSubmit, formState: { errors } } = useForm<CustomerSearchParams>({
    resolver: zodResolver(searchSchema),
  });

  return (
    <form onSubmit={handleSubmit(onSearch)} className="space-y-4">
      <div>
        <label className="block text-sm font-medium">Customer ID</label>
        <input
          type="number"
          {...register('customerId', { valueAsNumber: true })}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm"
        />
      </div>
      
      <div>
        <label className="block text-sm font-medium">Customer Name</label>
        <input
          type="text"
          {...register('customerName')}
          className="mt-1 block w-full rounded-md border-gray-300 shadow-sm"
        />
      </div>

      {errors.root && (
        <p className="text-red-600 text-sm">{errors.root.message}</p>
      )}

      <button type="submit" className="btn btn-primary">
        Search
      </button>
    </form>
  );
};
```

**Location:** `frontend/src/features/<module>/<Domain>Search.tsx`

**See CUST001 example:** `frontend/src/features/customers/CustomerSearch.tsx`

### 4.5 Create Detail Display Component

**Goal:** Display customer details matching RPGLE display file detail screen.

```tsx
/**
 * Customer detail display component
 * Maps to CUSTDSP.dds detail screen
 * Source: source-rpgle/dds/display-files/CUSTDSP.dds
 */

import React from 'react';
import { Customer } from '../types/customer.types';

interface CustomerDetailProps {
  customer: Customer;
}

export const CustomerDetail: React.FC<CustomerDetailProps> = ({ customer }) => {
  return (
    <div className="bg-white shadow rounded-lg p-6">
      <h2 className="text-xl font-semibold mb-4">Customer Details</h2>
      <dl className="grid grid-cols-1 gap-4">
        <div>
          <dt className="text-sm font-medium text-gray-500">Customer ID</dt>
          <dd className="mt-1 text-sm text-gray-900">{customer.customerId}</dd>
        </div>
        <div>
          <dt className="text-sm font-medium text-gray-500">Name</dt>
          <dd className="mt-1 text-sm text-gray-900">{customer.customerName}</dd>
        </div>
        <div>
          <dt className="text-sm font-medium text-gray-500">Address</dt>
          <dd className="mt-1 text-sm text-gray-900">
            {customer.addressLine1}<br />
            {customer.addressLine2 && <>{customer.addressLine2}<br /></>}
            {customer.city}, {customer.state} {customer.postalCode}
          </dd>
        </div>
      </dl>
    </div>
  );
};
```

**Location:** `frontend/src/features/<module>/<Domain>Detail.tsx`

**See CUST001 example:** `frontend/src/features/customers/CustomerDetail.tsx`

### 4.6 Create Main Page Component

**Goal:** Compose search + results into full page.

```tsx
/**
 * Customer inquiry page
 * Full page implementation of CUST001 program flow
 * Source: source-rpgle/programs/CUST001.rpgle
 */

import React, { useState } from 'react';
import { CustomerSearch } from './CustomerSearch';
import { CustomerDetail } from './CustomerDetail';
import { useCustomerSearch } from './useCustomer';
import { CustomerSearchParams } from './customer.types';

export const CustomerInquiry: React.FC = () => {
  const [searchParams, setSearchParams] = useState<CustomerSearchParams>({});
  const { data: customers, isLoading, error } = useCustomerSearch(searchParams);

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Customer Inquiry</h1>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-1">
          <CustomerSearch onSearch={setSearchParams} />
        </div>
        
        <div className="lg:col-span-2">
          {isLoading && <p>Loading...</p>}
          {error && <p className="text-red-600">Error: {error.message}</p>}
          {customers && customers.length === 0 && <p>No customers found</p>}
          {customers && customers.map(customer => (
            <CustomerDetail key={customer.customerId} customer={customer} />
          ))}
        </div>
      </div>
    </div>
  );
};
```

**Location:** `frontend/src/features/<module>/<Domain>Inquiry.tsx` or `<Domain>Page.tsx`

**See CUST001 example:** `frontend/src/features/customers/CustomerInquiry.tsx`

### 4.7 Follow Documentation Standards

Add inline documentation per `docs/standards/typescript-documentation-standards.md`

**Checkpoint:**

✅ TypeScript types created  
✅ API client created  
✅ React Query hooks created  
✅ Search form component created  
✅ Detail display component created  
✅ Main page component created  
✅ Tailwind CSS styling applied  
✅ Inline documentation added

**Next:** Proceed to Phase 5 - Testing

---

## Phase 5: Testing

**Goal:** Write comprehensive tests ensuring functional equivalence with original RPGLE.

### 5.1 Backend Unit Tests

**Goal:** Test business logic in isolation.

**Framework:** JUnit 5 + Mockito

**Test Structure:**

```java
package com.smeup.backend.service;

import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.dto.CustomerSearchDTO;
import com.smeup.backend.entity.Customer;
import com.smeup.backend.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerService
 * Tests business logic from RPGLE CUST001
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void searchByCustomerId_WhenExists_ReturnsCustomer() {
        // Given
        Long customerId = 12345L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("ACME Corp");

        when(customerRepository.findById(customerId))
            .thenReturn(Optional.of(customer));

        CustomerSearchDTO searchDTO = new CustomerSearchDTO();
        searchDTO.setCustomerId(customerId);

        // When
        List<CustomerDTO> results = customerService.searchCustomers(searchDTO);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCustomerId()).isEqualTo(customerId);
        assertThat(results.get(0).getCustomerName()).isEqualTo("ACME Corp");
    }

    @Test
    void searchCustomers_WhenNoSearchCriteria_ThrowsException() {
        // Given
        CustomerSearchDTO searchDTO = new CustomerSearchDTO();

        // When/Then
        assertThatThrownBy(() -> customerService.searchCustomers(searchDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("At least one search field required");
    }

    @Test
    void searchByName_WhenPartialMatch_ReturnsMultipleResults() {
        // Given
        String searchName = "ACME";
        List<Customer> customers = List.of(
            createCustomer(1L, "ACME Corp"),
            createCustomer(2L, "ACME Industries")
        );

        when(customerRepository.findByCustomerNameContainingIgnoreCase(searchName))
            .thenReturn(customers);

        CustomerSearchDTO searchDTO = new CustomerSearchDTO();
        searchDTO.setCustomerName(searchName);

        // When
        List<CustomerDTO> results = customerService.searchCustomers(searchDTO);

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).extracting(CustomerDTO::getCustomerName)
            .containsExactly("ACME Corp", "ACME Industries");
    }

    private Customer createCustomer(Long id, String name) {
        Customer c = new Customer();
        c.setCustomerId(id);
        c.setCustomerName(name);
        return c;
    }
}
```

**Location:** `backend/src/test/java/com/smeup/backend/service/<Entity>ServiceTest.java`

**See CUST001 example:** `backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java`

### 5.2 Backend Integration Tests with Testcontainers

**Goal:** Test against real PostgreSQL database.

**Framework:** JUnit 5 + Testcontainers

```java
package com.smeup.backend.repository;

import com.smeup.backend.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for CustomerRepository with real database
 * Uses Testcontainers PostgreSQL for isolation
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByCustomerNameContainingIgnoreCase_WithPartialMatch_ReturnsMatches() {
        // Given
        Customer customer1 = new Customer();
        customer1.setCustomerId(1L);
        customer1.setCustomerName("ACME Corporation");
        customerRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setCustomerId(2L);
        customer2.setCustomerName("Acme Industries");
        customerRepository.save(customer2);

        Customer customer3 = new Customer();
        customer3.setCustomerId(3L);
        customer3.setCustomerName("XYZ Company");
        customerRepository.save(customer3);

        // When
        List<Customer> results = customerRepository
            .findByCustomerNameContainingIgnoreCase("acme");

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Customer::getCustomerName)
            .containsExactlyInAnyOrder("ACME Corporation", "Acme Industries");
    }
}
```

**Location:** `backend/src/test/java/com/smeup/backend/repository/<Entity>RepositoryIntegrationTest.java`

**See CUST001 example:** `backend/src/test/java/com/smeup/backend/repository/CustomerRepositoryIT.java`

### 5.3 Backend Controller Tests

**Goal:** Test REST endpoints with MockMvc.

```java
package com.smeup.backend.controller;

import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for CustomerController REST endpoints
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void searchCustomers_WithValidId_ReturnsOk() throws Exception {
        // Given
        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerId(12345L);
        customer.setCustomerName("ACME Corp");

        when(customerService.searchCustomers(any()))
            .thenReturn(List.of(customer));

        // When/Then
        mockMvc.perform(get("/api/customers/search")
                .param("customerId", "12345"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].customerId").value(12345))
            .andExpect(jsonPath("$[0].customerName").value("ACME Corp"));
    }
}
```

**Location:** `backend/src/test/java/com/smeup/lanmam/<module>/controller/<Entity>ControllerTest.java`

**Run all backend tests:**
```bash
cd backend
mvn test
```

### 5.4 Frontend Component Tests with Vitest

**Goal:** Test React components in isolation.

**Framework:** Vitest + React Testing Library

```typescript
/**
 * Tests for CustomerSearch component
 */

import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { CustomerSearch } from '../CustomerSearch';

describe('CustomerSearch', () => {
  it('renders search fields', () => {
    const onSearch = vi.fn();
    render(<CustomerSearch onSearch={onSearch} />);

    expect(screen.getByLabelText(/customer id/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/customer name/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument();
  });

  it('calls onSearch with customerId when submitted', async () => {
    const onSearch = vi.fn();
    render(<CustomerSearch onSearch={onSearch} />);

    const idInput = screen.getByLabelText(/customer id/i);
    const submitButton = screen.getByRole('button', { name: /search/i });

    fireEvent.change(idInput, { target: { value: '12345' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(onSearch).toHaveBeenCalledWith({
        customerId: 12345,
        customerName: undefined,
      });
    });
  });

  it('shows validation error when both fields are empty', async () => {
    const onSearch = vi.fn();
    render(<CustomerSearch onSearch={onSearch} />);

    const submitButton = screen.getByRole('button', { name: /search/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText(/at least one search field/i)).toBeInTheDocument();
    });

    expect(onSearch).not.toHaveBeenCalled();
  });
});
```

**Location:** `frontend/src/features/<module>/__tests__/<Component>.test.tsx`

**See CUST001 example:** `frontend/src/features/customers/__tests__/CustomerSearch.test.tsx`

**Run frontend tests:**
```bash
cd frontend
npm test
```

### 5.5 End-to-End Tests with Playwright

**Goal:** Test complete user flows matching RPGLE program behavior.

**Framework:** Playwright

```typescript
/**
 * E2E tests for CUST001 customer inquiry workflow
 * Tests functional equivalence with original RPGLE program
 */

import { test, expect } from '@playwright/test';

test.describe('Customer Inquiry (CUST001)', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/customers');
  });

  test('search by customer ID returns customer details', async ({ page }) => {
    // Fill search form
    await page.fill('input[name="customerId"]', '12345');
    await page.click('button:has-text("Search")');

    // Verify results
    await expect(page.locator('text=ACME Corporation')).toBeVisible();
    await expect(page.locator('text=123 Main St')).toBeVisible();
  });

  test('search by partial name returns multiple results', async ({ page }) => {
    // Fill search form
    await page.fill('input[name="customerName"]', 'ACME');
    await page.click('button:has-text("Search")');

    // Verify multiple results
    await expect(page.locator('text=ACME Corporation')).toBeVisible();
    await expect(page.locator('text=ACME Industries')).toBeVisible();
  });

  test('empty search shows validation error', async ({ page }) => {
    // Submit without filling fields
    await page.click('button:has-text("Search")');

    // Verify error message
    await expect(page.locator('text=At least one search field')).toBeVisible();
  });

  test('search with no results shows appropriate message', async ({ page }) => {
    // Search for non-existent customer
    await page.fill('input[name="customerId"]', '99999');
    await page.click('button:has-text("Search")');

    // Verify no results message
    await expect(page.locator('text=No customers found')).toBeVisible();
  });
});
```

**Location:** `frontend/e2e/<domain>.spec.ts`

**See CUST001 example:** `frontend/e2e/customer-inquiry.spec.ts`

**Run E2E tests:**
```bash
cd frontend
npx playwright test
```

### 5.6 Functional Equivalence Tests

**Goal:** Verify transformation produces identical results to RPGLE.

**Approach:**
1. Run test cases through original RPGLE program (or use saved test data)
2. Run same test cases through new Java/React implementation
3. Compare results byte-by-byte

**Test Data Structure:**

```json
{
  "testCases": [
    {
      "scenario": "Search by valid customer ID",
      "input": {
        "customerId": 12345
      },
      "expectedOutput": {
        "customerId": 12345,
        "customerName": "ACME Corporation",
        "addressLine1": "123 Main St",
        "city": "Chicago",
        "state": "IL",
        "postalCode": "60601"
      }
    },
    {
      "scenario": "Search by partial name",
      "input": {
        "customerName": "ACME"
      },
      "expectedOutput": [
        { "customerId": 12345, "customerName": "ACME Corporation" },
        { "customerId": 67890, "customerName": "ACME Industries" }
      ]
    }
  ]
}
```

**Validation:**
```bash
# Run functional equivalence test suite
cd backend
mvn test -Dtest=FunctionalEquivalenceTest

cd ../frontend
npm run test:equivalence
```

**See CUST001 example:** `backend/src/test/java/com/smeup/lanmam/customer/FunctionalEquivalenceTest.java`

### 5.7 Code Coverage Requirements

**Goal:** Achieve ≥80% code coverage.

**Backend Coverage:**
```bash
cd backend
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html
```

**Frontend Coverage:**
```bash
cd frontend
npm test -- --coverage

# View report
open coverage/index.html
```

**Minimum Coverage Requirements:**
- **Unit Tests:** 80% line coverage
- **Integration Tests:** Key user flows covered
- **E2E Tests:** Critical business scenarios covered

**Checkpoint:**

✅ Unit tests written (backend + frontend)  
✅ Integration tests written with Testcontainers  
✅ Controller tests written  
✅ Component tests written  
✅ E2E tests written with Playwright  
✅ Functional equivalence tests pass  
✅ Code coverage ≥80%

**Next:** Proceed to Phase 6 - Validation

---

## Phase 6: Validation

**Goal:** Verify transformation quality before marking complete.

### 6.1 Run All Tests

**Backend:**
```bash
cd backend
mvn clean test

# Expected output:
# Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
```

**Frontend:**
```bash
cd frontend
npm test

# Expected output:
# Test Suites: 12 passed, 12 total
# Tests:       48 passed, 48 total
```

**E2E:**
```bash
cd frontend
npx playwright test

# Expected output:
# 8 passed (12s)
```

**✅ Requirement:** All tests must pass with ZERO failures.

### 6.2 Run Linters

**Backend (Checkstyle + PMD):**
```bash
cd backend
mvn checkstyle:check
mvn pmd:check

# Expected output:
# BUILD SUCCESS
# No violations found
```

**Frontend (ESLint + Prettier):**
```bash
cd frontend
npm run lint
npm run format:check

# Expected output:
# ✔ No linting errors
# ✔ All files formatted correctly
```

**✅ Requirement:** Zero critical violations. Warnings are acceptable if justified in comments.

### 6.3 Verify Functional Equivalence

**Manual Testing Checklist:**

- [ ] Search by customer ID returns correct customer
- [ ] Search by customer name (partial match) returns all matching customers
- [ ] Search with no criteria shows validation error
- [ ] Search with non-existent customer shows "not found" message
- [ ] Customer detail display shows all fields correctly
- [ ] Address formatting matches RPGLE output
- [ ] Error messages match RPGLE behavior

**Automated Validation:**
```bash
cd backend
mvn test -Dtest=FunctionalEquivalenceTest

# Verify all scenarios pass
```

**✅ Requirement:** 100% functional equivalence with RPGLE original.

### 6.4 Verify Code Coverage

```bash
# Backend
cd backend
mvn clean test jacoco:report
# Check target/site/jacoco/index.html - must be ≥80%

# Frontend
cd frontend
npm test -- --coverage
# Check coverage/index.html - must be ≥80%
```

**✅ Requirement:** Line coverage ≥80% for all modules.

### 6.5 Validate Inline Documentation

**Check that every file includes:**

- [ ] **Entities:** RPGLE/DDS source reference in JavaDoc
- [ ] **Services:** Business logic mapping reference
- [ ] **Controllers:** Original RPGLE program reference
- [ ] **Components:** Display file reference

**Example Validation:**

```bash
# Search for missing RPGLE references
grep -r "Source: source-rpgle" backend/src/main/java/
# Should return results for all transformed files

grep -r "Source: source-rpgle" frontend/src/features/
# Should return results for all transformed files
```

**✅ Requirement:** All generated code must include RPGLE traceability comments.

### 6.6 Run Full Build

**Backend:**
```bash
cd backend
mvn clean install

# Expected output:
# BUILD SUCCESS
# Total time:  01:23 min
```

**Frontend:**
```bash
cd frontend
npm run build

# Expected output:
# ✓ built in 4.52s
# dist/index.html  0.45 kB │ gzip: 0.30 kB
```

**✅ Requirement:** Clean build with no errors or warnings.

### 6.7 Transformation Validation Checklist

**Purpose:** This checklist serves as your **Definition of Done** before marking a transformation complete. It ensures functional equivalence with RPGLE, comprehensive testing, quality standards, and proper documentation.

**When to Use:**
- Before marking story/transformation as "done"
- Before creating a Pull Request
- During code review (reviewer validates all items)
- As final quality gate before merging

**Estimated Time:** 15-30 minutes to complete full checklist validation

---

#### **Category 1: Compilation & Build Validation**

Verify all code compiles without errors or warnings.

- [ ] **Backend compiles successfully**
  ```bash
  cd backend
  mvn clean compile
  # Expected: BUILD SUCCESS, 0 errors
  ```

- [ ] **Frontend compiles successfully**
  ```bash
  cd frontend
  npm run build
  # Expected: ✓ built in <n>s, 0 errors
  ```

- [ ] **No compilation errors or warnings**
  - Backend: Check Maven output for `[WARNING]` or `[ERROR]`
  - Frontend: Check Vite/TypeScript output for type errors

- [ ] **All dependencies resolved**
  - Backend: No Maven dependency resolution failures
  - Frontend: `npm install` completes without errors

**Command Summary:**
```bash
# Quick validation
cd backend && mvn clean compile && cd ..
cd frontend && npm run build && cd ..
```

---

#### **Category 2: Testing Validation**

Ensure comprehensive test coverage and all tests passing.

- [ ] **All backend unit tests pass**
  ```bash
  cd backend
  mvn test
  # Expected: Tests run: N, Failures: 0, Errors: 0, Skipped: 0
  ```

- [ ] **All frontend unit tests pass**
  ```bash
  cd frontend
  npm test
  # Expected: Test Files: N passed (N), Tests: N passed (N)
  ```

- [ ] **All backend integration tests pass** (Testcontainers)
  ```bash
  cd backend
  mvn verify -Pintegration-tests
  # Or: mvn test -Dtest=*IT
  # Expected: All integration tests pass
  ```

- [ ] **All E2E tests pass** (Playwright)
  ```bash
  cd frontend
  npx playwright test
  # Expected: All tests passed (N passed)
  ```

- [ ] **Code coverage ≥ 80%** (backend and frontend)
  ```bash
  # Backend (JaCoCo)
  cd backend
  mvn clean test jacoco:report
  # Check: target/site/jacoco/index.html
  
  # Frontend (Vitest)
  cd frontend
  npm test -- --coverage
  # Check: coverage/index.html
  ```

- [ ] **No flaky tests detected**
  - Run test suite 2-3 times to verify consistency
  - All tests produce same results on each run

**Test Coverage Requirements:**
- Line coverage ≥ 80% for all modules
- Critical business logic: 100% coverage
- New code must have accompanying tests

**Command Summary:**
```bash
# Full test suite validation
cd backend && mvn clean verify && cd ..
cd frontend && npm test && npx playwright test && cd ..
```

---

#### **Category 3: Code Quality & Linting Validation**

Ensure code meets quality standards and style guidelines.

- [ ] **Backend passes Checkstyle**
  ```bash
  cd backend
  mvn checkstyle:check
  # Expected: BUILD SUCCESS, 0 violations
  ```

- [ ] **Backend passes PMD static analysis**
  ```bash
  cd backend
  mvn pmd:check
  # Expected: BUILD SUCCESS, 0 critical violations
  ```

- [ ] **Frontend passes ESLint**
  ```bash
  cd frontend
  npm run lint
  # Expected: ✔ No linting errors
  ```

- [ ] **Frontend passes Prettier formatting**
  ```bash
  cd frontend
  npm run format:check
  # Expected: All files formatted correctly
  ```

- [ ] **No TypeScript `any` types in new code**
  - Search: `grep -r ": any" frontend/src/features/<module>/`
  - Should return 0 results for new transformation code

- [ ] **Cyclomatic complexity ≤ 10 for all methods**
  - PMD checks this automatically
  - Refactor complex methods into smaller units

**Zero Critical Violations Required:**
- Warnings are acceptable if justified in code comments
- Critical violations must be fixed before proceeding

**Command Summary:**
```bash
# Quick quality check
cd backend && mvn checkstyle:check pmd:check && cd ..
cd frontend && npm run lint && npm run format:check && cd ..
```

---

#### **Category 4: Documentation Validation**

Verify comprehensive inline documentation with RPGLE traceability.

- [ ] **All Java classes have JavaDoc** (per [java-documentation-standards.md](../standards/java-documentation-standards.md))
  - Entities: Include DDS physical file reference
  - Services: Include RPGLE program reference and business logic mapping
  - Controllers: Include original RPGLE program reference
  - DTOs: Include DDS display file or purpose

- [ ] **All React components have JSDoc** (per [typescript-documentation-standards.md](../standards/typescript-documentation-standards.md))
  - Components: Include DDS display file and record format reference
  - Hooks: Include original RPGLE interaction pattern
  - Types: Include DDS field mapping

- [ ] **Inline comments reference original DDS/RPGLE sources**
  ```bash
  # Verify RPGLE references exist
  grep -r "Source: source-rpgle" backend/src/main/java/
  grep -r "Source: source-rpgle" frontend/src/features/
  # Should return results for all transformed files
  ```

- [ ] **Business logic transformations are documented**
  - Service methods include comment explaining RPGLE equivalent
  - Complex validations reference original RPGLE logic

- [ ] **Transformation analysis document created**
  - Location: `docs/transformations/<PROGRAM>/analysis.md`
  - Contains: Program overview, transformation approach, technical decisions

- [ ] **Business logic mapping document created**
  - Location: `docs/transformations/<PROGRAM>/business-logic-mapping.md`
  - Contains: RPGLE subroutine to Java method mappings

- [ ] **Data mapping document created** (DDS → JPA → React)
  - Location: `docs/transformations/<PROGRAM>/data-mapping.md`
  - Contains: Complete field-by-field mapping table

**Documentation Standard Compliance:**
- Reference Story 5.4 for documentation requirements
- Use CUST001 docs as template: `docs/transformations/CUST001/`

---

#### **Category 5: Functional Equivalence Validation** ⚠️ **CRITICAL - NFR1**

Ensure 100% functional equivalence with original RPGLE behavior.

- [ ] **Functional equivalence tests pass with test data**
  ```bash
  cd backend
  mvn test -Dtest=FunctionalEquivalenceTest
  # All scenarios must pass
  ```

- [ ] **Manual testing of key workflows completed**
  - Test each user workflow end-to-end
  - Compare output with RPGLE program output (if available)
  - Document test results

- [ ] **Edge cases identified and tested**
  - Empty inputs
  - Maximum length inputs
  - Boundary values (min/max numbers)
  - Special characters in text fields
  - Null/missing data scenarios

- [ ] **Error handling matches original RPGLE behavior**
  - Error messages match RPGLE messages (or are more helpful)
  - Error codes/formats consistent
  - Graceful degradation for expected failures

- [ ] **Validation rules match original DDS/RPGLE constraints**
  - Field length limits enforced
  - Required field validation
  - Data type validation (numeric, date formats)
  - Range checks (min/max values)
  - Cross-field validation logic

- [ ] **Data transformations preserve original semantics**
  - Calculations produce same results
  - Decimal precision matches RPGLE
  - Date/time conversions preserve meaning
  - String operations (trim, case) match behavior

- [ ] **UI behavior matches original green-screen intent**
  - Display fields show same data
  - Input fields accept same formats
  - Screen flow matches RPGLE program flow
  - Function key equivalents work correctly

**NFR1 Requirement:** 100% functional equivalence is mandatory.  
Reference: `_bmad-output/planning-artifacts/prd.md` - NFR1

---

#### **Category 6: Architectural Compliance Validation**

Ensure code follows project architectural patterns and conventions.

- [ ] **Code follows project naming conventions** (per [architecture.md](../../_bmad-output/planning-artifacts/architecture.md))
  - Packages: `com.smeup.backend.<module>`
  - Classes: PascalCase (CustomerService, CustomerController)
  - Methods: camelCase (searchCustomers, validateInput)
  - Files: kebab-case for React (customer-search.tsx)

- [ ] **Database schema uses DDS file names for tables**
  - `@Table(name = "CUSTMAST")` - preserves DDS physical file name
  - Verified in Flyway migration SQL

- [ ] **JPA entities use `@Table(name="DDS_FILE")` pattern**
  ```java
  @Entity
  @Table(name = "CUSTMAST")  // DDS physical file name
  public class Customer { ... }
  ```

- [ ] **JPA fields use `@Column(name="DDS_FIELD")` pattern**
  ```java
  @Column(name = "CUSTID")  // DDS field name
  private Long customerId;  // Java readable name
  ```

- [ ] **API endpoints follow REST conventions**
  - Plural resource names: `/api/customers`
  - HTTP methods: GET (read), POST (create), PUT (update), DELETE
  - Query params for search: `/api/customers/search?customerId=123`
  - Path params for specific resource: `/api/customers/{id}`

- [ ] **React components follow feature module structure**
  ```
  frontend/src/features/<module>/
  ├── <Domain>Search.tsx
  ├── <Domain>Detail.tsx
  ├── <Domain>Inquiry.tsx (or <Domain>Page.tsx)
  ├── use<Domain>.ts
  └── <domain>.types.ts
  ```

- [ ] **Error responses use RFC 7807 Problem Details format**
  - Backend controller exception handlers return RFC 7807 format
  - Frontend API client handles problem detail responses

**Architectural Patterns Reference:**
- Backend patterns: `_bmad-output/planning-artifacts/architecture.md` - Implementation Patterns
- Frontend patterns: `_bmad-output/planning-artifacts/architecture.md` - React patterns

---

#### **Category 7: Traceability & Project Cleanliness**

Ensure code is traceable to original sources and repository is clean.

- [ ] **All generated files reference original RPGLE/DDS sources**
  ```bash
  # Every transformed file should have source reference
  # Example: "Source: source-rpgle/programs/CUST001.rpgle"
  find backend/src/main/java -name "*.java" -exec grep -l "Source: source-rpgle" {} \;
  find frontend/src/features -name "*.tsx" -exec grep -l "Source: source-rpgle" {} \;
  ```

- [ ] **Commit messages reference story number** (e.g., "5-6")
  ```bash
  git log --oneline -5
  # Should show commits like: "feat(5-6): Add validation checklist"
  ```

- [ ] **Git history is clean**
  - No merge conflicts remaining
  - No debug commits (console.log, System.out.println)
  - No "WIP" or "temp" commits in final history
  - Consider: `git rebase -i` to clean up before merging

- [ ] **Files are in correct locations per project structure**
  - Backend: Entities in `entity/`, Services in `service/`, etc.
  - Frontend: Components in `features/<module>/`, types in same folder
  - Tests: Co-located with source or in parallel `test/` structure

- [ ] **No leftover TODO or FIXME comments without issues**
  ```bash
  # Search for unresolved TODOs
  grep -rn "TODO\|FIXME" backend/src/main/java/ frontend/src/features/
  # Each TODO should have corresponding GitHub issue or be resolved
  ```

**Clean Repository Standards:**
- All code is production-ready
- No commented-out code blocks (unless with explanation)
- No unused imports or variables

---

#### **Category 8: PR Review & Integration Readiness**

Validate story is ready for code review and integration.

- [ ] **All tasks/subtasks in story file marked complete** `[x]`
  - Review story file: `_bmad-output/implementation-artifacts/<story-key>.md`
  - Every `[ ]` checkbox should be `[x]`

- [ ] **Story file updated with completion details**
  - Dev Agent Record → Implementation Notes filled
  - File List → All modified files listed
  - Change Log → Summary of changes added

- [ ] **Sprint status updated** (if using sprint tracking)
  - `_bmad-output/implementation-artifacts/sprint-status.yaml`
  - Story status: `in-progress` → `review` (done by workflow automatically)

- [ ] **All acceptance criteria satisfied**
  - Review story acceptance criteria section
  - Verify each "Given/When/Then" is implemented and tested

- [ ] **Full build succeeds (backend + frontend)**
  ```bash
  cd backend && mvn clean install && cd ..
  cd frontend && npm run build && cd ..
  # Expected: Both succeed with no errors
  ```

**Ready for Review Criteria:**
- All checklist items above are checked ✅
- Implementation matches story requirements exactly
- No known bugs or incomplete features

---

### **Validation Summary Template**

Use this summary when marking transformation complete:

```markdown
## Transformation Validation Summary

**Story:** <story-key> - <story-title>
**Program:** <RPGLE-program-name>
**Validation Date:** <date>
**Validated By:** <your-name>

### Validation Results

- ✅ **Compilation & Build:** All code compiles, no errors
- ✅ **Testing:** All tests pass (Unit: N, Integration: N, E2E: N)
- ✅ **Code Coverage:** Backend: X%, Frontend: Y% (both ≥80%)
- ✅ **Code Quality:** Checkstyle, PMD, ESLint pass with 0 critical violations
- ✅ **Documentation:** All inline docs complete with RPGLE traceability
- ✅ **Functional Equivalence:** 100% match with RPGLE behavior verified
- ✅ **Architectural Compliance:** Follows all project patterns and conventions
- ✅ **Traceability:** All files reference original sources, git history clean
- ✅ **PR Readiness:** Story complete, sprint status updated

### Manual Test Results

- [ ] Search by customer ID: ✅ Pass
- [ ] Search by customer name: ✅ Pass
- [ ] Empty search validation: ✅ Pass
- [ ] Not found scenario: ✅ Pass
- [ ] Detail display: ✅ Pass

### Files Modified

**Backend:**
- `backend/src/main/java/.../Customer.java` (entity)
- `backend/src/main/java/.../CustomerRepository.java` (repository)
- `backend/src/main/java/.../CustomerService.java` (service)
- `backend/src/main/java/.../CustomerController.java` (controller)
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

### Reviewer Sign-Off

- [ ] Code Review Complete
- [ ] All Checklist Items Verified
- [ ] Approved for Merge

**Reviewer:** _______________  
**Date:** _______________
```

---

**If ANY checklist item is unchecked:** Address the issue before proceeding to Phase 7 or marking story done.

**CUST001 Validation Example:** See `docs/transformations/CUST001/` for reference of fully validated transformation.

**Next Steps After Validation:**
1. Complete Phase 7: Documentation & Integration
2. Create Pull Request using validation summary
3. Run code review workflow (recommended: fresh context, different LLM)
4. Merge after approval

**Checkpoint:**

✅ All tests passing  
✅ All linters passing  
✅ Functional equivalence verified  
✅ Code coverage ≥80%  
✅ Documentation complete  
✅ Full build successful

**Next:** Proceed to Phase 7 - Documentation & Integration

---

## Phase 7: Documentation & Integration

**Goal:** Complete transformation documentation and integrate changes.

### 7.1 Create Transformation Analysis Document

**Purpose:** Provide comprehensive analysis of the transformation for future maintainers.

**Template Location:** `docs/standards/business-logic-mapping-template.md`

**Create:** `docs/transformations/<PROGRAM>/analysis.md`

**Structure:**

```markdown
# <PROGRAM> Transformation Analysis

**Original RPGLE Program:** source-rpgle/programs/<PROGRAM>.rpgle  
**Transformation Date:** <DATE>  
**Developer:** <YOUR_NAME>

## Program Overview

[Plain English description from AI semantic analysis]

## Files Referenced

### DDS Physical Files
- `CUSTMAST.dds` → `CUSTMAST` table → `Customer.java` entity

### DDS Display Files
- `CUSTDSP.dds` → React components (CustomerSearch, CustomerDetail)

### RPGLE Program
- `CUST001.rpgle` → CustomerService business logic

## Transformation Approach

### Backend Architecture
- **Entity:** Customer.java (maps to CUSTMAST table)
- **Repository:** CustomerRepository.java (Spring Data JPA)
- **Service:** CustomerService.java (business logic from RPGLE)
- **Controller:** CustomerController.java (REST endpoints)
- **DTOs:** CustomerDTO, CustomerSearchDTO

### Frontend Architecture
- **Types:** customer.types.ts (TypeScript interfaces)
- **API Client:** customerApi.ts (fetch wrapper)
- **Hooks:** useCustomer.ts (React Query)
- **Components:** 
  - CustomerSearch.tsx (search screen)
  - CustomerDetail.tsx (detail screen)
  - CustomerInquiry.tsx (main page)

## Technical Decisions

### Database Mapping
- Table name: `CUSTMAST` (preserved DDS name)
- Field names: Preserved DDS field names in @Column
- Java fields: Readable camelCase names

### API Design
- Endpoint: `GET /api/customers/search`
- Query params: `customerId`, `customerName`
- Response: JSON array of CustomerDTO

### Validation Strategy
- Backend: Spring Validation annotations
- Frontend: Zod schema validation
- Business rule: At least one search field required

## Risks & Mitigations

| Risk | Mitigation |
|------|------------|
| Partial name search performance | Indexed CUSTNM field |
| Case-sensitive search mismatch | Used `IgnoreCase` query method |
| Missing validation | Comprehensive unit tests |

## Test Strategy

- **Unit Tests:** 12 tests covering all business logic paths
- **Integration Tests:** 5 tests with Testcontainers PostgreSQL
- **E2E Tests:** 4 scenarios covering full user flows
- **Functional Equivalence:** 100% match with RPGLE behavior

## Code Coverage

- **Backend:** 87% line coverage
- **Frontend:** 84% line coverage

## Lessons Learned

- AI analysis was accurate for 95% of business logic
- Display file field positions not relevant for React (use semantic layout)
- RPGLE validation rules need explicit implementation (not automatic)
```

**CUST001 Example:** `docs/transformations/CUST001/analysis.md`

### 7.2 Create Business Logic Mapping Document

**Purpose:** Map RPGLE subroutines/procedures to Java methods.

**Create:** `docs/transformations/<PROGRAM>/business-logic-mapping.md`

**Structure:**

```markdown
# <PROGRAM> Business Logic Mapping

## Overview

Maps RPGLE business logic from `<PROGRAM>.rpgle` to Java service methods.

## Subroutine Mappings

### RPGLE: Search Subroutine

**Original RPGLE Logic:**
```
C     EXSR      SEARCH
C     SEARCH    BEGSR
C                 IF        CUSTID <> *ZERO
C                   CHAIN     CUSTID    CUSTMAST
C                 ELSE
C                   SETLL     CUSTNM    CUSTMAST
C                   READ      CUSTMAST
C                 ENDIF
C     SEARCH    ENDSR
```

**Java Implementation:**
```java
@Transactional(readOnly = true)
public List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO) {
    if (searchDTO.getCustomerId() != null) {
        // CHAIN operation → findById
        return customerRepository.findById(searchDTO.getCustomerId())
            .map(List::of)
            .orElse(List.of());
    } else {
        // SETLL + READ operation → findByNameContaining
        return customerRepository.findByCustomerNameContainingIgnoreCase(
            searchDTO.getCustomerName()
        );
    }
}
```

**Validation Rules:**
- At least one search field required (RPGLE validation)
- Customer ID must be numeric (enforced by type system)
- Name search is case-insensitive (RPGLE behavior)

### RPGLE: Display Subroutine

**Original RPGLE Logic:**
```
C     EXSR      DISPLAY
C     DISPLAY   BEGSR
C                 EXFMT     CUSTDTL
C     DISPLAY   ENDSR
```

**React Implementation:**
```tsx
<CustomerDetail customer={customer} />
```

**Mapping:**
- `CUSTDTL` format → `CustomerDetail` component
- Field-by-field mapping via TypeScript types
```

**CUST001 Example:** `docs/transformations/CUST001/business-logic-mapping.md`

### 7.3 Create Data Mapping Document

**Purpose:** Document DDS field to Java/TypeScript mappings.

**Create:** `docs/transformations/<PROGRAM>/data-mapping.md`

**Structure:**

```markdown
# <PROGRAM> Data Mapping

## DDS Physical File: CUSTMAST

### Field Mappings

| DDS Field | DDS Type | Length | Java Entity Field | Java Type | TypeScript Type | Notes |
|-----------|----------|--------|-------------------|-----------|-----------------|-------|
| CUSTID | Decimal | 9,0 | customerId | Long | number | Primary key |
| CUSTNM | Char | 50 | customerName | String | string | Indexed for search |
| CUSTAD1 | Char | 50 | addressLine1 | String | string | Optional |
| CUSTAD2 | Char | 50 | addressLine2 | String | string | Optional |
| CUSTCY | Char | 30 | city | String | string | Optional |
| CUSTST | Char | 2 | state | String | string | 2-letter code |
| CUSTPC | Char | 10 | postalCode | String | string | Optional |

### Table Configuration

- **Table Name:** `CUSTMAST` (preserved from DDS)
- **Primary Key:** `CUSTID`
- **Indexes:** 
  - `idx_custmast_name` on `CUSTNM` (for search performance)

## DDS Display File: CUSTDSP.dds

### Screen Format: CUSTSRCH (Search Screen)

| DDS Field | Field Type | Java DTO Field | React Component | HTML Input Type |
|-----------|------------|----------------|-----------------|-----------------|  
| SCUSTID | Input | customerId | CustomerSearch | number |
| SCUSTNM | Input | customerName | CustomerSearch | text |

### Screen Format: CUSTDTL (Detail Screen)

| DDS Field | Field Type | Java DTO Field | React Component | Display Format |
|-----------|------------|----------------|-----------------|----------------|
| DCUSTID | Output | customerId | CustomerDetail | number |
| DCUSTNM | Output | customerName | CustomerDetail | text |
| DCUSTAD1 | Output | addressLine1 | CustomerDetail | text |
| DCUSTAD2 | Output | addressLine2 | CustomerDetail | text |
| DCUSTCY | Output | city | CustomerDetail | text |
| DCUSTST | Output | state | CustomerDetail | text |
| DCUSTPC | Output | postalCode | CustomerDetail | text |
```

**CUST001 Example:** `docs/transformations/CUST001/data-mapping.md`

### 7.4 Update Sprint Status

**Goal:** Mark story as complete in sprint tracking.

**File:** `_bmad-output/implementation-artifacts/sprint-status.yaml`

**Update status:**
```yaml
development_status:
  # ...
  <story-key>: done  # Change from 'in-progress' to 'done'
```

**Example:**
```bash
# Edit sprint-status.yaml
vim _bmad-output/implementation-artifacts/sprint-status.yaml

# Find your story (e.g., 2-1-create-customer-entity)
# Change: 2-1-create-customer-entity: in-progress
# To:     2-1-create-customer-entity: done
```

### 7.5 Create Pull Request (Optional)

**If using Git workflow:**

```bash
# Ensure all changes are committed
git add .
git commit -m "feat: Transform <PROGRAM> - Backend and Frontend"

# Push feature branch
git push origin feature/transform-<PROGRAM>

# Create PR on GitHub/GitLab
# Title: "Transform <PROGRAM>: Customer Inquiry Program"
# Description:
# - Transformed CUST001.rpgle to Spring Boot + React
# - Implemented customer search and detail display
# - All tests passing (45 backend, 48 frontend, 8 E2E)
# - Code coverage: 87% backend, 84% frontend
# - Functional equivalence: 100%
# - Documentation: analysis.md, business-logic-mapping.md, data-mapping.md
```

**PR Checklist:**

- [ ] All tests pass
- [ ] Code coverage ≥80%
- [ ] Linters pass with zero critical violations
- [ ] Inline documentation includes RPGLE references
- [ ] Transformation documentation complete
- [ ] Sprint status updated
- [ ] Manual testing completed

### 7.6 Code Review Process

**Recommended Approach:**

1. **Self-Review:** Use the validation checklist (Phase 6.7)
2. **Peer Review:** Have another developer review the PR
3. **AI Review:** Run code through AI for additional feedback
4. **Integration Testing:** Verify in staging environment

**Code Review Checklist:**

- [ ] Business logic correctly implements RPGLE behavior
- [ ] All edge cases handled
- [ ] Error messages match RPGLE (or are more helpful)
- [ ] Performance is acceptable
- [ ] Security best practices followed
- [ ] Code follows project standards
- [ ] Tests are comprehensive
- [ ] Documentation is clear and accurate

### 7.7 Merge and Deploy

**After approval:**

```bash
# Merge PR
git checkout master
git pull origin master
git merge feature/transform-<PROGRAM>
git push origin master

# Tag release (optional)
git tag -a transform-<PROGRAM>-v1.0 -m "CUST001 transformation complete"
git push origin transform-<PROGRAM>-v1.0

# Deploy to staging
# [Follow your project's deployment process]

# Verify in staging
# Run smoke tests to confirm deployment
```

**Checkpoint:**

✅ Transformation analysis document created  
✅ Business logic mapping document created  
✅ Data mapping document created  
✅ Sprint status updated to "done"  
✅ Pull request created (if using Git workflow)  
✅ Code review completed  
✅ Changes merged and deployed

**🎉 Transformation Complete!**

---

## Troubleshooting

### Common Issue #1: Backend Tests Failing

**Symptom:**
```
[ERROR] Tests run: 45, Failures: 3, Errors: 0, Skipped: 0
[ERROR] CustomerServiceTest.searchByCustomerId_WhenExists_ReturnsCustomer FAILED
```

**Possible Causes & Solutions:**

#### Cause 1: Mock not configured correctly

**Solution:**
```java
// Verify you're using the correct Mockito syntax
when(customerRepository.findById(customerId))
    .thenReturn(Optional.of(customer));  // Not Optional.empty()

// For methods with complex parameters, use ArgumentMatchers
when(customerRepository.findByCustomerNameContainingIgnoreCase(anyString()))
    .thenReturn(customers);
```

#### Cause 2: Test data doesn't match expectations

**Solution:**
```java
// Log actual vs expected
System.out.println("Expected: " + expectedCustomer);
System.out.println("Actual: " + actualCustomer);

// Use AssertJ for better error messages
assertThat(actualCustomer)
    .usingRecursiveComparison()
    .isEqualTo(expectedCustomer);
```

#### Cause 3: Testcontainers database not starting

**Solution:**
```bash
# Ensure Docker is running
docker ps

# Check Testcontainers logs
# Add to test class:
@Slf4j
public class CustomerRepositoryIntegrationTest {
    static {
        postgres.start();
        log.info("PostgreSQL started: {}", postgres.getJdbcUrl());
    }
}

# Verify Flyway migrations ran
# Check logs for "Flyway Migration to version 1 - Create Customer Table"
```

**See also:** `backend/src/test/java/com/smeup/lanmam/customer/` for working examples

---

### Common Issue #2: Frontend Tests Failing

**Symptom:**
```
FAIL  src/features/customers/__tests__/CustomerSearch.test.tsx
  ● CustomerSearch › calls onSearch with customerId when submitted
    Expected number of calls: 1
    Received number of calls: 0
```

**Possible Causes & Solutions:**

#### Cause 1: Async form submission not awaited

**Solution:**
```typescript
// Use waitFor for async operations
import { waitFor } from '@testing-library/react';

await waitFor(() => {
  expect(onSearch).toHaveBeenCalledWith({
    customerId: 12345,
  });
});
```

#### Cause 2: React Query provider missing

**Solution:**
```typescript
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { retry: false },  // Don't retry in tests
  },
});

render(
  <QueryClientProvider client={queryClient}>
    <CustomerInquiry />
  </QueryClientProvider>
);
```

#### Cause 3: Form validation preventing submission

**Solution:**
```typescript
// Check for validation errors in test output
screen.debug();  // Prints current DOM

// Verify form fields are being filled correctly
const input = screen.getByLabelText(/customer id/i);
fireEvent.change(input, { target: { value: '12345' } });
expect(input).toHaveValue(12345);  // Verify value is set
```

**See also:** `frontend/src/features/customers/__tests__/` for working examples

---

### Common Issue #3: Linting Errors

**Symptom:**
```
backend/src/main/java/com/smeup/backend/service/CustomerService.java:42:
[Checkstyle] Line is longer than 120 characters (found 135).

frontend/src/features/customers/CustomerSearch.tsx:15:
[@typescript-eslint/no-explicit-any] Unexpected any. Specify a different type.
```

**Solutions:**

#### Backend (Checkstyle)

**Line too long:**
```java
// Before (135 chars)
List<Customer> customers = customerRepository.findByCustomerNameContainingIgnoreCaseAndCityContainingIgnoreCase(name, city);

// After (break into multiple lines)
List<Customer> customers = customerRepository
    .findByCustomerNameContainingIgnoreCaseAndCityContainingIgnoreCase(
        name, 
        city
    );
```

**Missing JavaDoc:**
```java
/**
 * Search customers by ID or name
 * Original RPGLE: CUST001 search subroutine
 *
 * @param searchDTO search criteria
 * @return list of matching customers
 */
@Transactional(readOnly = true)
public List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO) {
    // ...
}
```

#### Frontend (ESLint + TypeScript)

**No 'any' types:**
```typescript
// Before (wrong)
const handleSearch = (data: any) => {
  onSearch(data);
};

// After (correct)
const handleSearch = (data: CustomerSearchParams) => {
  onSearch(data);
};
```

**Unused variables:**
```typescript
// Before
import { useState, useEffect } from 'react';  // useEffect unused

// After
import { useState } from 'react';
```

**Run auto-fix:**
```bash
# Backend
cd backend
mvn spotless:apply  # Auto-format code

# Frontend
cd frontend
npm run lint:fix    # Auto-fix ESLint issues
npm run format      # Auto-format with Prettier
```

---

### Common Issue #4: Functional Equivalence Mismatch

**Symptom:**
```
FunctionalEquivalenceTest.testSearchByCustomerId FAILED
Expected: Customer[id=12345, name=ACME Corporation]
Actual:   Customer[id=12345, name=ACME CORPORATION]
```

**Possible Causes & Solutions:**

#### Cause 1: Case sensitivity mismatch

**RPGLE behavior:** Returns data as stored in database  
**Java behavior:** Might uppercase/lowercase during processing

**Solution:**
```java
// Preserve exact case from database
@Column(name = "CUSTNM")
private String customerName;  // Don't call toUpperCase() or toLowerCase()

// In DTO mapper
dto.setCustomerName(entity.getCustomerName());  // Preserve as-is
```

#### Cause 2: Whitespace handling difference

**RPGLE behavior:** Pads char fields with spaces  
**Java behavior:** Strings don't have fixed length

**Solution:**
```java
// Trim whitespace if RPGLE comparison expects it
public String getCustomerName() {
    return customerName != null ? customerName.trim() : null;
}
```

#### Cause 3: Decimal precision mismatch

**RPGLE:** Decimal(9,2) = 1234567.89  
**Java:** BigDecimal might have different scale

**Solution:**
```java
// Use BigDecimal with explicit scale for financial data
@Column(name = "AMOUNT", precision = 9, scale = 2)
private BigDecimal amount;

// Set scale in DTO
dto.setAmount(entity.getAmount().setScale(2, RoundingMode.HALF_UP));
```

**Debug approach:**
```java
@Test
void debugFunctionalEquivalence() {
    CustomerDTO expected = loadExpectedFromRPGLE();  // Test data from RPGLE run
    CustomerDTO actual = customerService.searchCustomers(searchDTO).get(0);
    
    // Field-by-field comparison
    assertEquals(expected.getCustomerId(), actual.getCustomerId(), "Customer ID mismatch");
    assertEquals(expected.getCustomerName(), actual.getCustomerName(), "Customer Name mismatch");
    // ... compare all fields individually
}
```

---

### Common Issue #5: Missing DDS Files

**Symptom:**
```
FileNotFoundException: source-rpgle/dds/physical-files/CUSTMAST.dds not found
```

**Solutions:**

#### Solution 1: Check alternate locations

```bash
# Search entire source-rpgle directory
find source-rpgle/ -name "CUSTMAST.dds" -o -name "CUSTMST.dds"

# Check for case variations
find source-rpgle/ -iname "custmast*"

# Check subdirectories
ls -R source-rpgle/dds/
```

#### Solution 2: Check for compressed files

```bash
# Look for .zip or .tar.gz files
find source-rpgle/ -name "*.zip" -o -name "*.tar.gz"

# Extract if found
unzip source-rpgle/dds/physical-files.zip
```

#### Solution 3: Reconstruct from existing table

**If you have the PostgreSQL table but not the DDS:**

```bash
# Generate DDS-like documentation from existing table
psql -U postgres -d lanmam -c "\d+ CUSTMAST" > CUSTMAST-schema.txt

# Use AI to create entity from table schema
# Prompt: "Create JPA entity from this PostgreSQL table schema: [paste schema]"
```

#### Solution 4: HALT and request missing files

**If files truly don't exist:**
```
HALT: Cannot proceed without DDS files for <PROGRAM>.
Required files:
- source-rpgle/dds/physical-files/<TABLE>.dds
- source-rpgle/dds/display-files/<PROGRAM>.dspf

Please obtain these files before continuing transformation.
```

---

### Common Issue #6: Database Migration Errors

**Symptom:**
```
FlywayException: Migration V2__Create_Customer_Table.sql failed
ERROR: relation "CUSTMAST" already exists
```

**Solutions:**

#### Solution 1: Clean and re-run migrations

```bash
# Drop all tables and re-run Flyway
cd backend
mvn flyway:clean flyway:migrate

# Or use Spring Boot
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.flyway.clean-on-validation-error=true"
```

#### Solution 2: Fix migration version conflicts

```bash
# Check existing migrations
ls backend/src/main/resources/db/migration/

# Ensure no version conflicts:
# V1__Create_Customer_Table.sql (OK)
# V2__Add_Indexes.sql (OK)
# V2__Create_Order_Table.sql (CONFLICT - two V2!)

# Rename conflicting migration
mv V2__Create_Order_Table.sql V3__Create_Order_Table.sql
```

#### Solution 3: Repair Flyway schema history

```bash
# Check schema_version table
psql -U postgres -d lanmam -c "SELECT * FROM flyway_schema_history;"

# Repair failed migration
cd backend
mvn flyway:repair

# Re-run migrations
mvn flyway:migrate
```

#### Solution 4: Use H2 for testing

**If PostgreSQL setup is problematic:**

```yaml
# backend/src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

---

### Getting Help

**When you're stuck:**

1. **Check CUST001 Reference Implementation:**
   - Backend: `backend/src/main/java/com/smeup/backend/`
   - Frontend: `frontend/src/features/customers/`
   - Tests: `backend/src/test/java/` and `frontend/src/__tests__/`
   - Docs: `docs/transformations/CUST001/`

2. **Review Standards Documentation:**
   - Java docs: `docs/standards/java-documentation-standards.md`
   - TypeScript docs: `docs/standards/typescript-documentation-standards.md`
   - Project rules: `_bmad-output/project-context.md`

3. **Use AI for Assistance:**
   ```
   Prompt: "I'm transforming an RPGLE program to Spring Boot + React. 
   I'm getting this error: [paste error]. The CUST001 reference 
   implementation handles this differently: [paste CUST001 code]. 
   How should I fix this?"
   ```

4. **Check Project Documentation:**
   - Architecture: `_bmad-output/planning-artifacts/architecture.md`
   - PRD: `_bmad-output/planning-artifacts/prd.md`
   - Epic context: `_bmad-output/planning-artifacts/epics.md`

5. **Run Example Tests:**
   ```bash
   # See CUST001 tests passing
   cd backend
   mvn test -Dtest=CustomerServiceTest
   
   cd ../frontend
   npm test CustomerSearchForm.test.tsx
   ```

6. **Ask Your Team:**
   - Tag team lead in PR comments
   - Post in project Slack/Teams channel
   - Schedule pairing session for complex issues

**💡 Pro Tip:** 90% of transformation issues have already been solved in CUST001. Always check the reference implementation first!

---

## CUST001 Reference Examples

**The CUST001 (Customer Inquiry) transformation is your complete reference implementation.** Use it as a template for all future transformations.

### Source Files

**RPGLE and DDS Sources:**
- **RPGLE Program:** `source-rpgle/programs/CUST001.rpgle` - Customer inquiry business logic
- **DDS Physical File:** `source-rpgle/dds/physical-files/CUSTMAST.dds` - Customer master table definition
- **DDS Display File:** `source-rpgle/dds/display-files/CUSTDSP.dds` - Screen layout definitions

### Backend Implementation

**Entity Layer:**
```
backend/src/main/java/com/smeup/lanmam/customer/entity/Customer.java
```
- Maps DDS Physical File `CUSTMAST.dds` to JPA entity
- Uses `@Table(name = "CUSTMAST")` to preserve DDS table name
- Uses `@Column(name = "CUSTID")` for DDS field names
- Java fields use readable camelCase names
- Includes RPGLE traceability comments

**Repository Layer:**
```
backend/src/main/java/com/smeup/lanmam/customer/repository/CustomerRepository.java
```
- Extends `JpaRepository<Customer, Long>`
- Custom query methods: `findByCustomerNameContainingIgnoreCase()`
- Implements RPGLE search patterns

**Service Layer:**
```
backend/src/main/java/com/smeup/lanmam/customer/service/CustomerService.java
```
- Implements business logic from `CUST001.rpgle`
- `searchCustomers()` method maps to RPGLE search subroutine
- Validates inputs per RPGLE validation rules
- Returns DTOs, not entities
- Uses constructor injection

**Controller Layer:**
```
backend/src/main/java/com/smeup/lanmam/customer/controller/CustomerController.java
```
- REST endpoint: `GET /api/customers/search`
- Query params: `customerId`, `customerName`
- Returns JSON array of `CustomerDTO`
- Follows API conventions (plural endpoints, kebab-case)

**DTOs:**
```
backend/src/main/java/com/smeup/lanmam/customer/dto/CustomerDTO.java
backend/src/main/java/com/smeup/lanmam/customer/dto/CustomerSearchDTO.java
```
- `CustomerSearchDTO`: Request format (search inputs)
- `CustomerDTO`: Response format (customer details)

**Database Migration:**
```
backend/src/main/resources/db/migration/V1__Create_Customer_Table.sql
```
- Creates `CUSTMAST` table with PostgreSQL syntax
- Preserves DDS field names in SQL
- Adds indexes for performance
- Includes descriptive comments

### Frontend Implementation

**TypeScript Types:**
```
frontend/src/features/customers/customer.types.ts
```
- `Customer` interface matches `CustomerDTO`
- `CustomerSearchParams` matches search query params
- No 'any' types - fully typed
- Includes JSDoc with RPGLE reference

**API Client:**
```
frontend/src/api/customerApi.ts (or in features/customers/)
```
- `searchCustomers()` function calls `GET /api/customers/search`
- Type-safe with TypeScript
- Error handling included

**React Query Hooks:**
```
frontend/src/features/customers/useCustomer.ts
```
- `useCustomerSearch()` hook manages server state
- Uses React Query for caching and refetching
- Enabled only when search criteria present

**Components:**
```
frontend/src/features/customers/CustomerSearch.tsx
```
- Maps to `CUSTDSP.dds` search screen (CUSTSRCH format)
- Uses React Hook Form for state management
- Zod schema for validation
- Tailwind CSS for styling
- Includes RPGLE traceability comments

```
frontend/src/features/customers/CustomerDetail.tsx
```
- Maps to `CUSTDSP.dds` detail screen (CUSTDTL format)
- Displays customer fields in semantic layout
- Responsive design with Tailwind CSS

```
frontend/src/features/customers/CustomerInquiry.tsx
```
- Main page combining search + results
- Implements full RPGLE program flow
- Error handling and loading states

### Test Examples

**Backend Unit Tests:**
```
backend/src/test/java/com/smeup/backend/service/CustomerServiceTest.java
```
**Example tests:**
- `searchByCustomerId_WhenExists_ReturnsCustomer()` - Happy path
- `searchCustomers_WhenNoSearchCriteria_ThrowsException()` - Validation
- `searchByName_WhenPartialMatch_ReturnsMultipleResults()` - Search logic

**Backend Integration Tests:**
```
backend/src/test/java/com/smeup/backend/repository/CustomerRepositoryIT.java
```
- Uses Testcontainers PostgreSQL
- Tests real database queries
- Verifies case-insensitive search

**Backend Controller Tests:**
```
backend/src/test/java/com/smeup/backend/controller/CustomerControllerTest.java
```
- Uses MockMvc for REST endpoint testing
- Tests query parameter handling
- Verifies JSON response format

**Frontend Component Tests:**
```
frontend/src/features/customers/__tests__/CustomerSearch.test.tsx
frontend/src/features/customers/__tests__/CustomerDetail.test.tsx
frontend/src/features/customers/__tests__/CustomerInquiry.test.tsx
```
- Vitest + React Testing Library
- Tests user interactions
- Validates form submission
- Checks component rendering

**E2E Tests:**
```
frontend/e2e/customer-inquiry.spec.ts
```
- Playwright end-to-end tests
- Tests full user workflows
- Validates functional equivalence with RPGLE

### Documentation Examples

**Transformation Analysis:**
```
docs/transformations/CUST001/analysis.md
```
- Program overview from AI analysis
- Transformation approach
- Technical decisions
- Risks and mitigations
- Test strategy
- Lessons learned

**Business Logic Mapping:**
```
docs/transformations/CUST001/business-logic-mapping.md
```
- Maps RPGLE subroutines to Java methods
- Side-by-side code comparison
- Validation rules documentation
- Screen format mappings

**Data Mapping:**
```
docs/transformations/CUST001/data-mapping.md
```
- Complete DDS-to-Java-to-TypeScript field mapping
- Data type conversions
- Index documentation
- Screen field mappings

### Side-by-Side Comparison

#### RPGLE Search Logic vs Java

**RPGLE (CUST001.rpgle):**
```rpg
C     EXSR      SEARCH
C     SEARCH    BEGSR
C                 IF        CUSTID <> *ZERO
C                   CHAIN     CUSTID    CUSTMAST
C                 ELSE
C                   SETLL     CUSTNM    CUSTMAST
C                   READ      CUSTMAST
C                 ENDIF
C     SEARCH    ENDSR
```

**Java (CustomerService.java):**
```java
@Transactional(readOnly = true)
public List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO) {
    if (searchDTO.getCustomerId() != null) {
        // CHAIN operation → findById
        return customerRepository.findById(searchDTO.getCustomerId())
            .map(List::of)
            .orElse(List.of());
    } else {
        // SETLL + READ operation → findByNameContaining
        return customerRepository.findByCustomerNameContainingIgnoreCase(
            searchDTO.getCustomerName()
        );
    }
}
```

**Key Observations:**
- RPGLE `CHAIN` → JPA `findById()`
- RPGLE `SETLL + READ` → JPA query method
- RPGLE `IF` validation → Java null check
- RPGLE returns single record → Java returns `List` (more flexible)

#### DDS Display File vs React Component

**DDS (CUST001.dspf - Search Screen):**
```
A          R CUSTSRCH
A            SCUSTID        9S 0B  5 10TEXT('Customer ID')
A            SCUSTNM       50A  B  7 10TEXT('Customer Name')
A                                  3  2'Customer Search'
A                                  5  2'Customer ID......:'
A                                  7  2'Customer Name....:'
```

**React (CustomerSearchForm.tsx):**
```tsx
<form onSubmit={handleSubmit(onSearch)} className="space-y-4">
  <h2 className="text-xl font-semibold">Customer Search</h2>
  
  <div>
    <label className="block text-sm font-medium">Customer ID</label>
    <input
      type="number"
      {...register('customerId', { valueAsNumber: true })}
      className="mt-1 block w-full rounded-md border-gray-300"
    />
  </div>
  
  <div>
    <label className="block text-sm font-medium">Customer Name</label>
    <input
      type="text"
      {...register('customerName')}
      className="mt-1 block w-full rounded-md border-gray-300"
    />
  </div>

  <button type="submit" className="btn btn-primary">Search</button>
</form>
```

**Key Observations:**
- DDS fixed positions (5,10) → React semantic layout (flexbox/grid)
- DDS field attributes (9S 0B) → HTML input types (number, text)
- DDS function keys → React button click handlers
- DDS screen flow → React component composition

### Quick Reference Checklist

When starting a new transformation, reference CUST001 for:

**Phase 1 - Preparation:**
- ✅ Example of locating DDS files in `source-rpgle/`
- ✅ Example sprint-status.yaml entry

**Phase 2 - Semantic Analysis:**
- ✅ Example AI analysis output in `docs/transformations/CUST001/analysis.md`

**Phase 3 - Backend:**
- ✅ Entity annotation pattern → `Customer.java`
- ✅ Repository query methods → `CustomerRepository.java`
- ✅ Service business logic → `CustomerService.java`
- ✅ Controller endpoint structure → `CustomerController.java`
- ✅ DTO design → `CustomerDTO.java`, `ApiResponse.java`
- ✅ Migration SQL → `V1__Create_Customer_Table.sql`

**Phase 4 - Frontend:**
- ✅ TypeScript types → `customer.types.ts`
- ✅ API client pattern → `customerApi.ts`
- ✅ React Query hooks → `useCustomer.ts`
- ✅ Form component → `CustomerSearch.tsx`
- ✅ Display component → `CustomerDetail.tsx`
- ✅ Page composition → `CustomerInquiry.tsx`

**Phase 5 - Testing:**
- ✅ Unit test structure → `CustomerServiceTest.java`
- ✅ Integration test with Testcontainers → `CustomerRepositoryIT.java`
- ✅ Controller test with MockMvc → `CustomerControllerTest.java`
- ✅ Component test → `CustomerSearch.test.tsx`
- ✅ E2E test → `customer-inquiry.spec.ts`

**Phase 6 - Validation:**
- ✅ Example test execution output
- ✅ Example linter configuration
- ✅ Example code coverage reports

**Phase 7 - Documentation:**
- ✅ Analysis document structure → `analysis.md`
- ✅ Business logic mapping → `business-logic-mapping.md`
- ✅ Data mapping → `data-mapping.md`

### Using CUST001 as a Template

**Copy-Paste Approach for New Transformations:**

```bash
# 1. Copy CUST001 structure for new program (e.g., ORD001)
cp -r backend/src/main/java/com/smeup/lanmam/customer \
      backend/src/main/java/com/smeup/lanmam/order

cp -r frontend/src/features/customer \
      frontend/src/features/order

# 2. Find and replace class/component names
# Use IDE refactoring or:
find backend/src/main/java/com/smeup/lanmam/order -type f \
  -exec sed -i 's/Customer/Order/g' {} \;
  -exec sed -i 's/customer/order/g' {} \;

find frontend/src/features/order -type f \
  -exec sed -i 's/Customer/Order/g' {} \;
  -exec sed -i 's/customer/order/g' {} \;

# 3. Update DDS references
# Change @Table(name = "CUSTMAST") → @Table(name = "ORDMAST")
# Change source comments to reference ORD001.rpgle

# 4. Implement business logic using AI semantic analysis
# Keep the structure, replace the logic

# 5. Update tests to match new business rules
```

**This copy-paste approach saves 50%+ of transformation time!**

---

**💡 Final Pro Tip:** Whenever you're unsure how to implement something, check CUST001 first. It's your comprehensive, working reference for RPGLE transformation patterns.

---

**Note:** This guide is a living document. Update it as transformation patterns evolve.
