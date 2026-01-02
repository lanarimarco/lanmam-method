# TypeScript/React Documentation Standards

**Version**: 1.0  
**Last Updated**: 2026-01-02  
**Purpose**: Define inline code documentation standards for RPGLE-to-React/TypeScript transformations

## Table of Contents

1. [Overview](#overview)
2. [What to Document](#what-to-document)
3. [What NOT to Document](#what-not-to-document)
4. [JSDoc/TSDoc Standards](#jsdoctstdoc-standards)
5. [Component Documentation](#component-documentation)
6. [Type and Interface Documentation](#type-and-interface-documentation)
7. [Hook Documentation](#hook-documentation)
8. [DDS-to-React Mapping Documentation](#dds-to-react-mapping-documentation)
9. [Examples from CUST001](#examples-from-cust001)

---

## Overview

This document defines documentation standards specifically for RPGLE display file (DSPF) to React/TypeScript transformations. The goal is to maintain clarity about:

- **DDS display field origins** - Where each React component field came from in the original DDS display files
- **Screen format mappings** - How RPGLE display formats translate to React components
- **Form validation rules** - How RPGLE field validations map to React Hook Form + Zod
- **User interaction patterns** - How function keys and indicators map to React events

These standards balance thoroughness with maintainability while preserving critical transformation context.

---

## What to Document

### Always Document

1. **DDS Display File Mappings**
   - Original DDS file name and record format
   - Field names, types, lengths, and edit codes
   - Screen positions (row/column) for layout reference
   - Function key mappings (F3=Exit → ESC key, etc.)

2. **Component Purpose and Origin**
   - RPGLE program reference
   - DDS display file reference
   - Record format name (PROMPT, DETAIL, etc.)
   - User workflow this component supports

3. **Props Interface Rationale**
   - Why each prop exists
   - Connection to parent component state
   - Callback purposes (onSearch, onSubmit, etc.)

4. **Form Validation from DDS**
   - Original DDS validation keywords (VALUES, RANGE, COMP)
   - Translated Zod schema rules
   - Error message mappings

5. **State Management Patterns**
   - React Query usage for RPGLE file operations
   - Loading states (RPGLE hourglass → isLoading)
   - Error indicators (RPGLE *IN90 → error state)

### Consider Documenting

1. **Complex conditional rendering** - Based on RPGLE indicators
2. **Accessibility enhancements** - Screen reader support, keyboard navigation
3. **Styling decisions** - Why Tailwind classes match green-screen layout
4. **Performance optimizations** - Memoization, lazy loading

---

## What NOT to Document

### Avoid Documenting

1. **Obvious prop names**
   ```tsx
   // BAD: Obvious prop documentation
   interface CustomerDetailProps {
       /** The customer object */
       customer: Customer;
   }
   ```

2. **Standard React patterns**
   ```tsx
   // BAD: Unnecessary comment
   // Use state to store customer ID
   const [customerId, setCustomerId] = useState<string | null>(null);
   ```

3. **Self-explanatory JSX**
   ```tsx
   // BAD: Comment adds no value
   {/* Display customer name */}
   <p>{customer.customerName}</p>
   ```

4. **Import statements**
   ```tsx
   // BAD: Obvious import
   import { useState } from 'react'; // React hook for state
   ```

5. **Standard Tailwind utility classes**
   ```tsx
   // BAD: No need to explain standard Tailwind
   <div className="flex flex-col gap-4"> {/* Flexbox column with gap */}
   ```

---

## JSDoc/TSDoc Standards

### File-Level Documentation

**Required for**:
- All component files
- Custom hooks
- Type definition files
- API client modules

**Format**:
```tsx
/**
 * Brief one-sentence description of the file.
 *
 * Longer description explaining purpose, context, and usage.
 * Include transformation-specific details here.
 *
 * DDS Source: path/to/original-file.dspf - Record Format NAME
 * RPGLE Program: PROGNAME - Program Purpose
 *
 * @module path/to/module (optional)
 * @example
 * ```tsx
 * // Usage example
 * <ComponentName prop1="value" />
 * ```
 */
```

**Example**:
```tsx
/**
 * Customer search form component from DDS PROMPT format.
 *
 * Maps to CUSTDSP.dds - Record Format PROMPT (Lines 13-30)
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * RPGLE Program: CUST001 - Customer Inquiry
 *
 * This component recreates the green-screen customer number entry.
 * User enters customer number, submits on Enter, displays errors if any.
 *
 * @example
 * ```tsx
 * <CustomerSearch
 *   onSearch={(customerNumber) => handleSearch(customerNumber)}
 *   errorMessage={apiError}
 *   isLoading={isSearching}
 * />
 * ```
 */
```

### Component Documentation

**Format**:
```tsx
/**
 * Component description.
 *
 * Maps to DDS Display File: FILENAME.dds - Record Format NAME (Lines X-Y)
 * Implements RPGLE program workflow: [brief workflow description]
 *
 * @param props - ComponentProps interface
 * @returns React component JSX
 *
 * @example
 * ```tsx
 * <ComponentName
 *   requiredProp="value"
 *   optionalProp={123}
 * />
 * ```
 */
export function ComponentName(props: ComponentProps): JSX.Element {
    // Implementation
}
```

**Example**:
```tsx
/**
 * CustomerDetail component displays customer information in a read-only format.
 *
 * Maps to CUSTDSP.dds DETAIL format (Lines 33-59) with 8 display fields:
 * - Customer Number (DCUSTNO)
 * - Customer Name (DCUSTNAME)
 * - Address Line 1 (DADDR1)
 * - City (DCITY)
 * - State (DSTATE)
 * - Zip Code (DZIP)
 * - Phone Number (DPHONE)
 * - Account Balance (DBALANCE)
 *
 * @param props - CustomerDetailProps
 * @returns React component displaying customer details or loading/error states
 *
 * @example
 * ```tsx
 * <CustomerDetail
 *   customer={customerData}
 *   isLoading={false}
 *   errorMessage={undefined}
 * />
 * ```
 */
export function CustomerDetail({
  customer,
  isLoading = false,
  errorMessage,
}: CustomerDetailProps): JSX.Element {
    // Implementation
}
```

### Props Interface Documentation

**Format**:
```tsx
/**
 * Props for ComponentName component.
 */
export interface ComponentNameProps {
    /**
     * Brief description of prop.
     * DDS Field: FIELDNAME (if applicable)
     * Additional context about usage or constraints.
     */
    propName: PropType;

    /**
     * Optional prop description.
     * @default defaultValue
     */
    optionalProp?: OptionalType;

    /**
     * Callback function description.
     * @param param - Parameter description
     * @returns Return value description (if any)
     */
    onAction: (param: ParamType) => void;
}
```

**Example**:
```tsx
/**
 * Props for CustomerSearch component.
 */
export interface CustomerSearchProps {
    /**
     * Callback invoked when user submits search form.
     * Passes validated customer number to parent component.
     * @param customerNumber - Customer ID entered by user (1-99999)
     */
    onSearch: (customerNumber: number) => void;

    /**
     * Error message to display below form.
     * Typically from API response or validation error.
     * DDS Equivalent: PMSG field on PROMPT format with *IN90 indicator
     */
    errorMessage?: string;

    /**
     * Loading state indicator.
     * Shows loading spinner while searching.
     * DDS Equivalent: Hourglass cursor or "Processing..." message
     * @default false
     */
    isLoading?: boolean;
}
```

---

## Component Documentation

### Functional Component Header

Include file-level JSDoc comment at the top of the file:

```tsx
/**
 * CustomerInquiry Page Component
 *
 * Complete customer inquiry page integrating search form and detail display.
 * Implements the CUST001 customer inquiry workflow from RPGLE program.
 *
 * Source: source-rpgle/programs/CUST001.rpgle - Customer Inquiry
 * DDS Display File: source-rpgle/dds/display-files/CUSTDSP.dds
 * Implements PROMPT (search) and DETAIL (display) record formats
 *
 * @module features/customers/CustomerInquiry
 */
```

### Inline Comments for RPGLE Logic

Use inline comments to map React code to RPGLE logic:

```tsx
export function CustomerInquiry(): JSX.Element {
  // RPGLE: Variable CUSTNO (5,0) - stores current customer number being searched
  const [customerId, setCustomerId] = useState<string | null>(null);

  // RPGLE: CHAIN operation to CUSTMAST - now handled by React Query hook
  const { data, isLoading, error, isError } = useCustomer(customerId);

  // RPGLE: Subroutine to accept user input and trigger lookup
  const handleSearch = (customerNumber: number) => {
    setCustomerId(customerNumber.toString());
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto p-6">
        {/* RPGLE: Display file title (CUSTDSP.dds Line 1) */}
        <h1 className="text-3xl font-bold text-gray-900 mb-8">
          Customer Inquiry
        </h1>

        {/* RPGLE: PROMPT record format (CUSTDSP.dds Lines 13-30) - Customer number entry */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <CustomerSearch
            onSearch={handleSearch}
            isLoading={isLoading}
            errorMessage={isError ? error?.message : undefined}
          />
        </div>

        {/* RPGLE: DETAIL record format (CUSTDSP.dds Lines 33-59) - Customer data display */}
        <div className="bg-white rounded-lg shadow-sm">
          <CustomerDetail
            customer={data ?? null}
            isLoading={isLoading}
            errorMessage={isError ? error?.message : undefined}
          />
        </div>
      </div>
    </div>
  );
}
```

### JSX Comments for DDS Field Mapping

Comment JSX elements to map to DDS display fields:

```tsx
<dl className="space-y-3 max-w-2xl">
  {/* DDS Line 42-43 - Customer Number (DCUSTNO - 5Y 0) */}
  <div className="flex flex-col sm:flex-row">
    <dt className="w-full sm:w-48 font-medium">Customer Number:</dt>
    <dd className="mt-1 sm:mt-0">{formatCustomerNumber(customer.customerNumber)}</dd>
  </div>

  {/* DDS Line 44-45 - Customer Name (DCUSTNAME - 30A) */}
  <div className="flex flex-col sm:flex-row">
    <dt className="w-full sm:w-48 font-medium">Name:</dt>
    <dd className="mt-1 sm:mt-0">{customer.customerName}</dd>
  </div>

  {/* DDS Line 46-47 - Address Line 1 (DADDR1 - 30A) */}
  <div className="flex flex-col sm:flex-row">
    <dt className="w-full sm:w-48 font-medium">Address:</dt>
    <dd className="mt-1 sm:mt-0">{customer.addressLine1 || 'N/A'}</dd>
  </div>
</dl>
```

---

## Type and Interface Documentation

### TypeScript Type Documentation

**Format**:
```tsx
/**
 * Type description.
 * DDS Source: [file and record format if applicable]
 */
export type TypeName = {
    /**
     * Field description.
     * DDS Field: FIELDNAME (TYPE LENGTH)
     */
    fieldName: FieldType;
};
```

**Example**:
```tsx
/**
 * Customer detail display data.
 * Maps to DDS Display File: CUSTDSP.dds - Record Format DETAIL
 */
export type CustomerDetailDisplay = {
    /**
     * Customer Number.
     * DDS Field: DCUSTNO (5Y 0) - Zero-suppressed output field
     */
    customerNumber: number;

    /**
     * Customer Name.
     * DDS Field: DCUSTNAME (30A) - Alphanumeric output field
     */
    customerName: string;

    /**
     * Account Balance.
     * DDS Field: DBALANCE (9Y 2) - Edited numeric with 2 decimals
     * DDS Edit Code: EDTCDE(J) - Comma, decimal point, no $ sign
     */
    accountBalance: number;
};
```

### Zod Schema Documentation

Document Zod schemas with DDS validation references:

```tsx
/**
 * Validation schema for customer prompt form.
 * 
 * DDS Validation Rules from CUSTDSP.dds PROMPT format:
 * - PCUSTNO (5P 0): Required, numeric, 1-99999 range
 * - Error indicator: *IN90 triggers error message display
 */
export const CustomerPromptFormDataSchema = z.object({
  /**
   * Customer number input.
   * DDS Field: PCUSTNO (5P 0)
   * DDS Validation: COMP(GT 0) - Must be greater than zero
   */
  customerNumber: z
    .number({
      required_error: 'Customer number is required',
      invalid_type_error: 'Customer number must be a number',
    })
    .int('Customer number must be an integer')
    .min(1, 'Customer number must be at least 1')
    .max(99999, 'Customer number cannot exceed 99999'),
});
```

---

## Hook Documentation

### Custom Hook Documentation

**Format**:
```tsx
/**
 * Hook description.
 *
 * RPGLE Operation: [original file operation]
 * Maps to: [REST API endpoint or data operation]
 *
 * @param param - Parameter description
 * @returns Hook return value description
 *
 * @example
 * ```tsx
 * const { data, isLoading, error } = useCustomHook(param);
 * ```
 */
export function useCustomHook(param: ParamType) {
    // Implementation
}
```

**Example**:
```tsx
/**
 * React Query hook for fetching customer data.
 *
 * RPGLE Operation: CHAIN CUSTMAST by CUSTNO
 * Maps to: GET /api/v1/customers/{customerId}
 *
 * Returns customer data with loading and error states.
 * Query is enabled only when customerId is provided (non-null).
 *
 * @param customerId - Customer ID to fetch (null disables query)
 * @returns React Query result with customer data, loading, and error states
 *
 * @example
 * ```tsx
 * const { data, isLoading, error, isError } = useCustomer('1001');
 * 
 * if (isLoading) return <LoadingSpinner />;
 * if (isError) return <ErrorMessage message={error.message} />;
 * return <CustomerDetail customer={data} />;
 * ```
 */
export function useCustomer(customerId: string | null) {
  return useQuery({
    queryKey: ['customer', customerId],
    queryFn: () => getCustomerById(customerId!),
    enabled: !!customerId, // RPGLE: Only CHAIN if customer number entered
    retry: false, // RPGLE: No retry on "Customer not found"
  });
}
```

---

## DDS-to-React Mapping Documentation

### Display Format to Component Mapping

Document how DDS record formats map to React components:

```tsx
/**
 * CustomerInquiry page component.
 *
 * Combines CustomerSearch and CustomerDetail components with React Query
 * for server state management. Implements the complete customer lookup workflow.
 *
 * RPGLE Program Flow (CUST001.rpgle):
 * 1. Display PROMPT screen (customer number entry)
 * 2. Read customer number input
 * 3. Chain to CUSTMAST file to retrieve customer record
 * 4. If found: Display DETAIL screen with customer data
 * 5. If not found: Display error message on PROMPT screen
 * 6. Loop back to PROMPT for next inquiry
 *
 * React Implementation:
 * - PROMPT screen → CustomerSearch component
 * - DETAIL screen → CustomerDetail component
 * - File CHAIN → useCustomer React Query hook (API call)
 * - Program loop → Component re-renders with new state
 */
```

### Function Key Mapping

Document function key mappings:

```tsx
/**
 * Handle keyboard events for function key simulation.
 *
 * DDS Function Keys mapped to browser keyboard:
 * - F3 (Exit) → ESC key
 * - F12 (Cancel) → ESC key
 * - Enter (Submit) → Enter key (handled by form onSubmit)
 *
 * @param event - Keyboard event
 */
const handleKeyDown = (event: KeyboardEvent<HTMLFormElement>) => {
  if (event.key === 'Escape') {
    // F3/F12 equivalent - close or navigate back
    window.history.back();
  }
};
```

### Indicator to State Mapping

Document RPGLE indicator mappings:

```tsx
/**
 * Error state management.
 *
 * DDS Error Indicator: *IN90
 * - *IN90 = *On → Display error message in PMSG field
 * - *IN90 = *Off → Hide error message
 *
 * React Implementation:
 * - errorMessage prop (string | undefined)
 * - Conditional rendering of error div
 */
{errorMessage && (
  <div className="text-red-600 text-sm mt-2" role="alert">
    {errorMessage}
  </div>
)}
```

---

## Examples from CUST001

### Example 1: Component File Documentation

**File**: `frontend/src/features/customers/CustomerSearch.tsx`

```tsx
/**
 * Customer search form component from DDS PROMPT format.
 *
 * Maps to CUSTDSP.dds - Record Format PROMPT (Lines 13-30)
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * RPGLE Program: CUST001 - Customer Inquiry
 *
 * This component recreates the green-screen customer number entry.
 * User enters customer number, submits on Enter, displays errors if any.
 *
 * @example
 * ```tsx
 * <CustomerSearch
 *   onSearch={(customerNumber) => handleSearch(customerNumber)}
 *   errorMessage={apiError}
 *   isLoading={isSearching}
 * />
 * ```
 */

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  CustomerPromptFormDataSchema,
  type CustomerPromptFormData,
} from './customer.types';

/**
 * Props for CustomerSearch component
 */
export interface CustomerSearchProps {
  /**
   * Callback invoked when user submits search form.
   * @param customerNumber - Validated customer number (1-99999)
   */
  onSearch: (customerNumber: number) => void;

  /**
   * Error message to display.
   * DDS Equivalent: PMSG field with *IN90 indicator
   */
  errorMessage?: string;

  /**
   * Loading state indicator.
   * DDS Equivalent: Hourglass cursor during CHAIN operation
   * @default false
   */
  isLoading?: boolean;
}

/**
 * Customer Search Form Component
 *
 * Implements DDS CUSTDSP PROMPT format as React form.
 * Validates customer number input and triggers search callback.
 */
export function CustomerSearch({
  onSearch,
  errorMessage,
  isLoading = false,
}: CustomerSearchProps): JSX.Element {
  // React Hook Form with Zod validation
  // DDS Validation: PCUSTNO field (5P 0) with COMP(GT 0)
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CustomerPromptFormData>({
    resolver: zodResolver(CustomerPromptFormDataSchema),
  });

  // DDS: User presses Enter after entering PCUSTNO
  const onSubmit = (data: CustomerPromptFormData) => {
    onSearch(data.customerNumber);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* DDS Line 20-21 - Customer Number Entry Field (PCUSTNO - 5P 0) */}
      <div>
        <label htmlFor="customerNumber" className="block text-sm font-medium text-gray-700 mb-2">
          Customer Number
        </label>
        <input
          id="customerNumber"
          type="number"
          {...register('customerNumber', { valueAsNumber: true })}
          disabled={isLoading}
          autoFocus
          className="w-full px-3 py-2 border border-gray-300 rounded-md"
        />
        
        {/* DDS: Field-level error from validation */}
        {errors.customerNumber && (
          <p className="text-red-600 text-sm mt-1" role="alert">
            {errors.customerNumber.message}
          </p>
        )}
      </div>

      {/* DDS Line 23-24 - Error Message Field (PMSG - 50A) with *IN90 */}
      {errorMessage && (
        <div className="text-red-600 text-sm" role="alert">
          {errorMessage}
        </div>
      )}

      {/* DDS: Enter key triggers search (CF01='Enter') */}
      <button
        type="submit"
        disabled={isLoading}
        className="w-full bg-blue-600 text-white py-2 px-4 rounded-md"
      >
        {isLoading ? 'Searching...' : 'Search'}
      </button>
    </form>
  );
}
```

### Example 2: Type Definition File

**File**: `frontend/src/features/customers/customer.types.ts`

```tsx
/**
 * TypeScript type definitions for Customer Inquiry (CUST001)
 *
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * Maps DDS record formats to TypeScript types
 */

import { z } from 'zod';

/**
 * Customer prompt form data from PROMPT record format.
 * DDS Record: CUSTDSP.dds - PROMPT (Lines 13-30)
 */
export type CustomerPromptFormData = {
  /**
   * Customer number input field.
   * DDS Field: PCUSTNO (5P 0) - Numeric input, 5 digits
   */
  customerNumber: number;
};

/**
 * Validation schema for customer prompt form.
 *
 * DDS Validation Rules:
 * - PCUSTNO: Required, numeric, 1-99999 range
 * - Error indicator *IN90 triggers if validation fails
 */
export const CustomerPromptFormDataSchema = z.object({
  /**
   * Customer number must be 1-99999.
   * DDS Field: PCUSTNO (5P 0)
   * DDS Validation: COMP(GT 0), implicit max 99999 from 5P 0
   */
  customerNumber: z
    .number({
      required_error: 'Customer number is required',
      invalid_type_error: 'Customer number must be a number',
    })
    .int('Customer number must be an integer')
    .min(1, 'Customer number must be at least 1')
    .max(99999, 'Customer number cannot exceed 99999'),
});

/**
 * Customer detail display data.
 * DDS Record: CUSTDSP.dds - DETAIL (Lines 33-59)
 */
export type CustomerDetailDisplay = {
  /**
   * Customer Number (output field).
   * DDS Field: DCUSTNO (5Y 0) - Zero-suppressed output
   */
  customerNumber: number;

  /**
   * Customer Name (output field).
   * DDS Field: DCUSTNAME (30A)
   */
  customerName: string;

  /**
   * Address Line 1 (output field).
   * DDS Field: DADDR1 (30A)
   */
  addressLine1: string | null;

  /**
   * City (output field).
   * DDS Field: DCITY (20A)
   */
  city: string | null;

  /**
   * State Code (output field).
   * DDS Field: DSTATE (2A)
   */
  state: string | null;

  /**
   * Zip Code (output field).
   * DDS Field: DZIP (5Y 0) - Zero-suppressed output
   */
  zipCode: number | null;

  /**
   * Phone Number (output field).
   * DDS Field: DPHONE (12A)
   */
  phoneNumber: string | null;

  /**
   * Account Balance (output field).
   * DDS Field: DBALANCE (9Y 2) - Edited numeric with 2 decimals
   * DDS Edit Code: EDTCDE(J) - Comma, decimal point, no $ sign
   */
  accountBalance: number;
};

/**
 * Zod schema for CustomerDetailDisplay validation.
 */
export const CustomerDetailDisplaySchema = z.object({
  customerNumber: z.number().int().min(1).max(99999),
  customerName: z.string().min(1).max(30),
  addressLine1: z.string().max(30).nullable(),
  city: z.string().max(20).nullable(),
  state: z.string().length(2).nullable(),
  zipCode: z.number().int().min(0).max(99999).nullable(),
  phoneNumber: z.string().max(12).nullable(),
  accountBalance: z.number(),
});
```

### Example 3: Custom Hook Documentation

**File**: `frontend/src/features/customers/useCustomer.ts`

```tsx
/**
 * React Query hook for customer data fetching.
 *
 * RPGLE Operation: CHAIN CUSTMAST by CUSTNO
 * API Endpoint: GET /api/v1/customers/{customerId}
 *
 * @module features/customers/useCustomer
 */

import { useQuery } from '@tanstack/react-query';
import { getCustomerById } from '../../api/customers';
import type { CustomerDetailDisplay } from './customer.types';

/**
 * Fetch customer by ID using React Query.
 *
 * RPGLE Operation: CHAIN CUSTMAST by CUSTNO
 * - %Found(CUSTMAST) → query success (data returned)
 * - %Found = *Off → query error (404 Not Found)
 *
 * Query behavior:
 * - Only enabled when customerId is non-null
 * - No retry on failure (matches RPGLE - CHAIN once)
 * - Cached by customer ID for performance
 *
 * @param customerId - Customer ID to fetch (null disables query)
 * @returns React Query result with customer data, loading, and error states
 *
 * @example
 * ```tsx
 * function CustomerPage() {
 *   const [customerId, setCustomerId] = useState<string | null>(null);
 *   const { data, isLoading, error, isError } = useCustomer(customerId);
 *
 *   if (isLoading) return <p>Loading...</p>;
 *   if (isError) return <p>Error: {error.message}</p>;
 *   return <CustomerDetail customer={data} />;
 * }
 * ```
 */
export function useCustomer(customerId: string | null) {
  return useQuery<CustomerDetailDisplay>({
    // Query key includes customerId for proper caching
    queryKey: ['customer', customerId],
    
    // API call function - only runs when enabled
    queryFn: () => getCustomerById(customerId!),
    
    // RPGLE: Only CHAIN if customer number entered
    // Prevents unnecessary API call when customerId is null
    enabled: !!customerId,
    
    // RPGLE: No retry on "Customer not found"
    // Single CHAIN operation - either found or not found
    retry: false,
  });
}
```

---

## Summary

### Documentation Checklist

- [ ] File header includes DDS file reference and record format
- [ ] Component documentation includes RPGLE program context
- [ ] Props interfaces document purpose and DDS field origins
- [ ] Type definitions reference DDS field names and types
- [ ] Zod schemas document DDS validation rules
- [ ] Custom hooks explain RPGLE operation equivalence
- [ ] JSX comments map to DDS display fields and line numbers
- [ ] Function key mappings documented
- [ ] Indicator-to-state mappings explained
- [ ] No unnecessary documentation on obvious React patterns
- [ ] All documentation passes ESLint linting

### AI-Friendly Documentation Tips

1. **Be explicit about DDS mappings** - Include field names, types, lengths
2. **Reference line numbers** - Helps AI locate original DDS format definitions
3. **Document RPGLE workflow** - Show how program flow maps to React components
4. **Include usage examples** - Makes it clear how components fit together
5. **Use consistent formatting** - Makes documentation easier to parse

---

**Related Documents**:
- [Java Documentation Standards](java-documentation-standards.md)
- [Business Logic Mapping Template](business-logic-mapping-template.md)
- [Documentation Checklist](documentation-checklist.md)
