/**
 * Customer API Service
 *
 * Handles API calls to the customer inquiry backend
 * Maps to CustomerInquiryController.java endpoints
 */

import { Customer, CustomerErrorResponse } from '../../types';

// Get API base URL from environment variable
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

/**
 * Get customer by customer number
 *
 * Calls: GET /api/customers/{customerNumber}
 *
 * @param customerNumber The customer number to look up
 * @returns Promise<Customer> The customer data
 * @throws Error if customer not found or validation fails
 */
export const getCustomerByNumber = async (customerNumber: number): Promise<Customer> => {
  // Construct the full URL
  const url = `${API_BASE_URL}/api/customers/${customerNumber}`;

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    });

    // Handle successful response
    if (response.ok) {
      const data: Customer = await response.json();
      return data;
    }

    // Handle error responses
    const errorData: CustomerErrorResponse = await response.json();

    // Create meaningful error messages based on status code
    if (response.status === 404) {
      throw new Error(errorData.message || 'Customer not found');
    } else if (response.status === 400) {
      throw new Error(errorData.message || 'Invalid customer number');
    } else {
      throw new Error(errorData.message || 'An error occurred while fetching customer data');
    }
  } catch (error) {
    // Handle network errors or JSON parsing errors
    if (error instanceof Error) {
      throw error;
    }
    throw new Error('Network error: Unable to connect to the server');
  }
};

/**
 * Customer service object
 * Provides all customer-related API calls
 */
export const customerService = {
  getCustomerByNumber,
};
