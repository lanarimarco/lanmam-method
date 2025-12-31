/**
 * Customer API client functions.
 *
 * Provides type-safe API calls for customer operations.
 * Backend Controller: CustomerController.java
 * Backend Endpoint: GET /api/v1/customers/{customerId}
 *
 * Source: backend/src/main/java/com/smeup/backend/controller/CustomerController.java
 * RPGLE Program: CUST001 - Customer Inquiry
 */

import { apiClient } from './client';
import type { Customer, ApiResponse, ApiError } from '../types/customer.types';

/**
 * Retrieve customer details by customer ID.
 *
 * Backend Mapping:
 * - Controller: CustomerController.getCustomerById(Long customerId)
 * - Endpoint: GET /api/v1/customers/{customerId}
 * - RPGLE Operation: CHAIN (CUSTID) CUSTMAST
 *
 * @param customerId - The customer ID to retrieve (as string, per AC requirement)
 * @returns Promise resolving to ApiResponse containing Customer data
 * @throws ApiError if request fails
 *
 * @example
 * ```typescript
 * const response = await getCustomerById('12345');
 * console.log(response.data.customerName);
 * ```
 */
export async function getCustomerById(customerId: string): Promise<ApiResponse<Customer>> {
  try {
    const response = await apiClient.get<ApiResponse<Customer>>(`/customers/${customerId}`);
    return response.data;
  } catch (error) {
    // Type-safe error re-throw: preserve error type information
    if (error instanceof Error) {
      const axiosError = error as unknown as { status?: number; data?: unknown };
      throw {
        status: axiosError.status || 0,
        message: error.message,
        data: axiosError.data || null,
      } as ApiError;
    }
    throw error;
  }
}

/**
 * Export all customer API functions and types
 */
export const customerApi = {
  getCustomerById,
};

// Export types for consumers
export type { ApiError } from '../types/customer.types';

export default customerApi;
