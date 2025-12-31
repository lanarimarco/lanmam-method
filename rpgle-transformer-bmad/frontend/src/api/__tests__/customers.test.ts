/**
 * Unit tests for Customer API client.
 *
 * Tests verify:
 * - Type-safe API calls
 * - Proper error handling
 * - Integration with backend CustomerController
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Customer, ApiResponse } from '../../types/customer.types';

// Mock the client module before importing customers
vi.mock('../client', () => ({
  apiClient: {
    get: vi.fn(),
  },
}));

// Import after mocking
import { getCustomerById } from '../customers';
import { apiClient } from '../client';

describe('Customer API Client', () => {
  // Sample customer data matching backend CustomerDTO structure
  const mockCustomer: Customer = {
    customerId: 12345,
    customerName: 'Test Customer',
    addressLine1: '123 Main St',
    city: 'Springfield',
    state: 'IL',
    zipCode: 62701,
    phoneNumber: '217-555-0100',
    accountBalance: 1500.50,
    creditLimit: 5000.00,
    lastOrderDate: 20251230,
  };

  const mockApiResponse: ApiResponse<Customer> = {
    data: mockCustomer,
    meta: {},
  };

  beforeEach(() => {
    // Reset all mocks before each test
    vi.clearAllMocks();
  });

  describe('getCustomerById', () => {
    it('should fetch customer successfully with correct types', async () => {
      // Arrange
      const customerId = '12345'; // AC requires string parameter type
      const mockAxiosResponse = {
        data: mockApiResponse,
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {},
      };

      vi.mocked(apiClient.get).mockResolvedValue(mockAxiosResponse);

      // Act
      const response = await getCustomerById(customerId);

      // Assert
      expect(response).toEqual(mockApiResponse);
      expect(response.data.customerId).toBe(12345);
      expect(response.data.customerName).toBe('Test Customer');
      expect(apiClient.get).toHaveBeenCalledWith(`/customers/${customerId}`);
    });

    it('should handle 404 error when customer not found', async () => {
      // Arrange
      const customerId = '99999'; // AC requires string parameter type
      const mockError = {
        response: {
          status: 404,
          data: {
            type: 'https://api.smeup.com/errors/not-found',
            title: 'Not Found',
            status: 404,
            detail: 'Customer not found',
            instance: `/api/v1/customers/${customerId}`,
          },
        },
        message: 'Request failed with status code 404',
      };

      vi.mocked(apiClient.get).mockRejectedValue(mockError);

      // Act & Assert
      await expect(getCustomerById(customerId)).rejects.toMatchObject({
        response: {
          status: 404,
        },
      });
    });

    it('should handle network errors properly', async () => {
      // Arrange
      const customerId = '12345'; // AC requires string parameter type
      const mockNetworkError = {
        message: 'Network error - no response from server',
        request: {},
      };

      vi.mocked(apiClient.get).mockRejectedValue(mockNetworkError);

      // Act & Assert
      await expect(getCustomerById(customerId)).rejects.toMatchObject({
        message: 'Network error - no response from server',
      });
    });

    it('should call correct endpoint with customer ID', async () => {
      // Arrange
      const customerId = '54321'; // AC requires string parameter type
      const mockAxiosResponse = {
        data: mockApiResponse,
      };

      vi.mocked(apiClient.get).mockResolvedValue(mockAxiosResponse);

      // Act
      await getCustomerById(customerId);

      // Assert
      expect(apiClient.get).toHaveBeenCalledWith(`/customers/${customerId}`);
      expect(apiClient.get).toHaveBeenCalledTimes(1);
    });

    it('should return data matching CustomerDTO structure', async () => {
      // Arrange
      const mockAxiosResponse = {
        data: mockApiResponse,
      };

      vi.mocked(apiClient.get).mockResolvedValue(mockAxiosResponse);

      // Act
      const response = await getCustomerById('12345');

      // Assert - Verify all CustomerDTO fields are present
      expect(response.data).toHaveProperty('customerId');
      expect(response.data).toHaveProperty('customerName');
      expect(response.data).toHaveProperty('addressLine1');
      expect(response.data).toHaveProperty('city');
      expect(response.data).toHaveProperty('state');
      expect(response.data).toHaveProperty('zipCode');
      expect(response.data).toHaveProperty('phoneNumber');
      expect(response.data).toHaveProperty('accountBalance');
      expect(response.data).toHaveProperty('creditLimit');
      expect(response.data).toHaveProperty('lastOrderDate');
    });

    it('should return ApiResponse wrapper format', async () => {
      // Arrange
      const mockAxiosResponse = {
        data: mockApiResponse,
      };

      vi.mocked(apiClient.get).mockResolvedValue(mockAxiosResponse);

      // Act
      const response = await getCustomerById('12345');

      // Assert - Verify ApiResponse structure
      expect(response).toHaveProperty('data');
      expect(response).toHaveProperty('meta');
      expect(typeof response.meta).toBe('object');
    });
  });
});
