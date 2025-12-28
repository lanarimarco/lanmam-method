/**
 * TypeScript type definitions for Customer Inquiry (CUST001)
 *
 * Maps to CustomerDTO.java from backend
 */

/**
 * Customer interface
 * Represents customer information returned from the API
 */
export interface Customer {
  customerNumber: number;
  customerName: string;
  address1: string;
  city: string;
  state: string;
  zipCode: number;
  phoneNumber: string;
  balance: number;
}

/**
 * API Error Response
 * Represents error responses from the customer API
 */
export interface CustomerErrorResponse {
  error: string;
  message: string;
  customerNumber?: number;
}

/**
 * Customer Search State
 * Tracks the current state of the customer search operation
 */
export type CustomerSearchState = 'idle' | 'loading' | 'success' | 'error';
