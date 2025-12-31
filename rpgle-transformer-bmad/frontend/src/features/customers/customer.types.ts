/**
 * Customer feature TypeScript type definitions.
 *
 * Types generated from DDS Display File: CUSTDSP.dds
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * RPGLE Program: CUST001 - Customer Inquiry
 *
 * This file contains display-specific types for customer UI components.
 * For API integration types, see src/types/customer.types.ts
 */

import { z } from 'zod';

/**
 * Customer number entry form data from DDS PROMPT format.
 *
 * Maps to CUSTDSP.dds - Record Format PROMPT (Lines 13-30)
 * Purpose: User input for customer number lookup
 *
 * @example
 * ```typescript
 * const formData: CustomerPromptFormData = {
 *   customerNumber: 12345,
 * };
 * ```
 */
export interface CustomerPromptFormData {
  /**
   * Customer Number for lookup
   * DDS Field: PCUSTNO (5Y 0) - Line 23
   * Input field (B = Both)
   */
  customerNumber: number;

  /**
   * Error message display (optional)
   * DDS Field: PMSG (50A) - Line 27
   * Conditional display with indicator 90
   */
  errorMessage?: string;
}

/**
 * Customer detail display data from DDS DETAIL format.
 *
 * Maps to CUSTDSP.dds - Record Format DETAIL (Lines 33-59)
 * Purpose: Display complete customer information
 *
 * @example
 * ```typescript
 * const customerDisplay: CustomerDetailDisplay = {
 *   customerNumber: 12345,
 *   customerName: 'ACME Corporation',
 *   addressLine1: '123 Main Street',
 *   city: 'Springfield',
 *   state: 'IL',
 *   zipCode: 62701,
 *   phoneNumber: '217-555-0100',
 *   accountBalance: 1500.50,
 * };
 * ```
 */
export interface CustomerDetailDisplay {
  /**
   * Customer Number
   * DDS Field: DCUSTNO (5Y 0) - Line 43
   * Output field (O = Output only)
   */
  customerNumber: number;

  /**
   * Customer Name
   * DDS Field: DCUSTNAME (30A) - Line 45
   * Output field - may be null if customer not found
   */
  customerName: string | null;

  /**
   * Address Line 1
   * DDS Field: DADDR1 (30A) - Line 47
   * Output field - may be null
   */
  addressLine1: string | null;

  /**
   * City
   * DDS Field: DCITY (20A) - Line 49
   * Output field - may be null
   */
  city: string | null;

  /**
   * State Code (2-character)
   * DDS Field: DSTATE (2A) - Line 51
   * Output field - may be null
   */
  state: string | null;

  /**
   * Zip Code
   * DDS Field: DZIP (5Y 0) - Line 53
   * Output field - may be null
   */
  zipCode: number | null;

  /**
   * Phone Number
   * DDS Field: DPHONE (12A) - Line 55
   * Output field - may be null
   */
  phoneNumber: string | null;

  /**
   * Account Balance
   * DDS Field: DBALANCE (9Y 2) - Line 57
   * Output field - may be null
   * Note: Represents decimal values (9 digits, 2 decimal places)
   */
  accountBalance: number | null;
}

/**
 * Zod schema for CustomerPromptFormData runtime validation.
 *
 * Provides runtime type checking and validation for customer number entry form.
 * More robust than type guards - validates all fields and their constraints.
 *
 * @example
 * ```typescript
 * const result = CustomerPromptFormDataSchema.safeParse(userInput);
 * if (result.success) {
 *   const formData = result.data; // Guaranteed valid
 * } else {
 *   console.error(result.error); // Validation errors
 * }
 * ```
 */
export const CustomerPromptFormDataSchema = z.object({
  /**
   * Customer Number - must be positive integer, max 5 digits
   * DDS Field: PCUSTNO (5Y 0) - numeric 5 digits
   */
  customerNumber: z.number().int().positive().max(99999).describe('Customer Number for lookup'),

  /**
   * Error message - optional string
   * DDS Field: PMSG (50A) - max 50 characters
   */
  errorMessage: z.string().max(50).optional().describe('Error message display'),
});

/**
 * Zod schema for CustomerDetailDisplay runtime validation.
 *
 * Provides runtime type checking and validation for customer detail display.
 * Validates all fields including nullable fields and data constraints.
 *
 * Note: This schema validates 8 display fields from CUSTDSP.dds DETAIL format.
 * It does NOT include creditLimit or lastOrderDate which exist in the CUSTMAST
 * database table but are not displayed in the DDS DETAIL screen format.
 *
 * @example
 * ```typescript
 * const result = CustomerDetailDisplaySchema.safeParse(apiResponse);
 * if (result.success) {
 *   const displayData = result.data; // Type-safe and validated
 * }
 * ```
 */
export const CustomerDetailDisplaySchema = z.object({
  /**
   * Customer Number - must be positive integer
   * DDS Field: DCUSTNO (5Y 0)
   */
  customerNumber: z.number().int().positive().describe('Customer Number'),

  /**
   * Customer Name - nullable string up to 30 characters
   * DDS Field: DCUSTNAME (30A)
   */
  customerName: z.string().max(30).nullable().describe('Customer Name'),

  /**
   * Address Line 1 - nullable string up to 30 characters
   * DDS Field: DADDR1 (30A)
   */
  addressLine1: z.string().max(30).nullable().describe('Address Line 1'),

  /**
   * City - nullable string up to 20 characters
   * DDS Field: DCITY (20A)
   */
  city: z.string().max(20).nullable().describe('City'),

  /**
   * State Code - nullable 2-character string
   * DDS Field: DSTATE (2A)
   */
  state: z.string().length(2).nullable().describe('State Code (2-character)'),

  /**
   * Zip Code - nullable positive integer
   * DDS Field: DZIP (5Y 0)
   */
  zipCode: z.number().int().positive().nullable().describe('Zip Code'),

  /**
   * Phone Number - nullable string up to 12 characters
   * DDS Field: DPHONE (12A)
   */
  phoneNumber: z.string().max(12).nullable().describe('Phone Number'),

  /**
   * Account Balance - nullable number with decimals
   * DDS Field: DBALANCE (9Y 2)
   */
  accountBalance: z.number().nullable().describe('Account Balance'),
});

/**
 * Maps API Customer response to CustomerDetailDisplay for UI rendering.
 *
 * Converts between API field names (customerId) and display field names (customerNumber).
 * This mapper bridges the gap between backend DTOs and DDS display file types.
 *
 * @param customer - Customer data from API (Story 3.1 types)
 * @returns CustomerDetailDisplay formatted for UI components
 *
 * @example
 * ```typescript
 * const apiCustomer: Customer = await fetchCustomer(12345);
 * const displayData = apiCustomerToDisplay(apiCustomer);
 * // Now use displayData in your UI component
 * ```
 */
export function apiCustomerToDisplay(customer: { customerId: number; customerName: string | null; addressLine1: string | null; city: string | null; state: string | null; zipCode: number | null; phoneNumber: string | null; accountBalance: number | null }): CustomerDetailDisplay {
  return {
    customerNumber: customer.customerId,
    customerName: customer.customerName,
    addressLine1: customer.addressLine1,
    city: customer.city,
    state: customer.state,
    zipCode: customer.zipCode,
    phoneNumber: customer.phoneNumber,
    accountBalance: customer.accountBalance,
  };
}

/**
 * Type guard to check if a value is CustomerPromptFormData
 *
 * WARNING: This is a shallow check. For runtime validation, use Zod schema instead.
 * Only validates customerNumber exists and is a number.
 *
 * @param value - The value to check
 * @returns true if value has customerNumber field as number
 */
export function isCustomerPromptFormData(value: unknown): value is CustomerPromptFormData {
  return (
    typeof value === 'object' &&
    value !== null &&
    'customerNumber' in value &&
    typeof (value as CustomerPromptFormData).customerNumber === 'number'
  );
}

/**
 * Type guard to check if a value is CustomerDetailDisplay
 *
 * WARNING: This is a shallow check. For runtime validation, use Zod schema instead.
 * Only validates customerNumber exists and is a number.
 *
 * @param value - The value to check
 * @returns true if value has customerNumber field as number
 */
export function isCustomerDetailDisplay(value: unknown): value is CustomerDetailDisplay {
  return (
    typeof value === 'object' &&
    value !== null &&
    'customerNumber' in value &&
    typeof (value as CustomerDetailDisplay).customerNumber === 'number'
  );
}
