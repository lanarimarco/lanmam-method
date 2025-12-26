/**
 * Customer Inquiry API Service
 * Handles API calls to the backend customer inquiry endpoints
 * Maps to CustomerInquiryController from Phase 3 conversion
 */

import { Customer, ApiError } from '../../types';

/**
 * Base API URL from environment variable
 * Integration Agent will configure this in .env file
 */
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

/**
 * Customer service for API interactions
 */
export const customerService = {
  /**
   * Get customer by customer number
   * Maps to: GET /api/customers/{customerNumber}
   * Controller: CustomerInquiryController.getCustomer()
   * RPGLE: CHAIN operation to CUSTMAST (line 51 in CUST001.rpgle)
   *
   * @param customerNumber - The customer number to search for
   * @returns Promise<Customer> - Customer data
   * @throws ApiError - Validation error (400) or Not Found (404)
   */
  getCustomer: async (customerNumber: number): Promise<Customer> => {
    // Validate customer number (maps to RPGLE lines 43-48)
    if (!customerNumber || customerNumber === 0) {
      throw {
        message: 'Customer number required',
        error: 'VALIDATION_ERROR'
      } as ApiError;
    }

    try {
      const response = await fetch(
        `${API_BASE_URL}/api/customers/${customerNumber}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      );

      // Handle non-OK responses
      if (!response.ok) {
        const errorData: ApiError = await response.json().catch(() => ({
          message: 'An unexpected error occurred',
          error: 'INTERNAL_ERROR'
        }));

        throw errorData;
      }

      // Parse and return customer data
      const customer: Customer = await response.json();
      return customer;

    } catch (error) {
      // Re-throw ApiError objects
      if (error && typeof error === 'object' && 'error' in error) {
        throw error as ApiError;
      }

      // Network or other errors
      throw {
        message: 'Unable to connect to server',
        error: 'NETWORK_ERROR'
      } as ApiError;
    }
  }
};
