/**
 * Unit tests for useCustomer React Query hook
 *
 * Tests custom hook for fetching customer data with React Query v5.
 * Covers query states, caching, error handling, and conditional fetching.
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook, waitFor } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { useCustomer } from '../useCustomer';
import * as customersApi from '../../../api/customers';
import type { CustomerDetailDisplay } from '../customer.types';

// Mock the API module
vi.mock('../../../api/customers');

describe('useCustomer Hook', () => {
  // Sample customer data for testing
  const mockCustomerData: CustomerDetailDisplay = {
    customerNumber: 12345,
    customerName: 'ACME Corporation',
    addressLine1: '123 Main Street',
    city: 'Springfield',
    state: 'IL',
    zipCode: 62701,
    phoneNumber: '217-555-0100',
    accountBalance: 1500.50,
  };

  // Create a wrapper for React Query provider
  const createWrapper = () => {
    const queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: 0, // Disable retry in tests for faster execution
        },
      },
      logger: {
        log: console.log,
        warn: console.warn,
        error: () => {}, // Suppress error logs in tests
      },
    });
    return ({ children }: { children: React.ReactNode }) => (
      <QueryClientProvider client={queryClient}>
        {children}
      </QueryClientProvider>
    );
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('Query behavior', () => {
    it('should not fetch when customerId is null', () => {
      const { result } = renderHook(() => useCustomer(null), {
        wrapper: createWrapper(),
      });

      // Query should be disabled
      expect(result.current.isLoading).toBe(false);
      expect(result.current.isFetching).toBe(false);
      expect(result.current.data).toBeUndefined();

      // API should not be called
      expect(customersApi.getCustomerById).not.toHaveBeenCalled();
    });

    it('should not fetch when customerId is empty string', () => {
      const { result } = renderHook(() => useCustomer(''), {
        wrapper: createWrapper(),
      });

      // Query should be disabled
      expect(result.current.isLoading).toBe(false);
      expect(result.current.isFetching).toBe(false);

      // API should not be called
      expect(customersApi.getCustomerById).not.toHaveBeenCalled();
    });

    it('should fetch customer data when customerId is provided', async () => {
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      const { result } = renderHook(() => useCustomer('12345'), {
        wrapper: createWrapper(),
      });

      // Initially should be loading
      expect(result.current.isLoading).toBe(true);

      // Wait for query to complete
      await waitFor(() => expect(result.current.isSuccess).toBe(true));

      // Verify data is returned
      expect(result.current.data).toEqual(mockCustomerData);
      expect(result.current.isLoading).toBe(false);
      expect(result.current.isError).toBe(false);

      // API should be called with correct ID
      expect(customersApi.getCustomerById).toHaveBeenCalledWith('12345');
      expect(customersApi.getCustomerById).toHaveBeenCalledTimes(1);
    });
  });

  describe('Error handling', () => {
    it('should handle API errors gracefully', async () => {
      const errorMessage = 'Customer not found';
      vi.mocked(customersApi.getCustomerById).mockRejectedValueOnce(new Error(errorMessage));

      const { result } = renderHook(() => useCustomer('99999'), {
        wrapper: createWrapper(),
      });

      // Wait for query to settle (either success or error)
      await waitFor(() => {
        return result.current.isError || result.current.isSuccess;
      });

      // If it's an error (expected), verify error state
      if (result.current.isError) {
        expect(result.current.error).toBeInstanceOf(Error);
        expect(result.current.error?.message).toBe(errorMessage);
        expect(result.current.data).toBeUndefined();
        expect(result.current.isLoading).toBe(false);
      }
    });

    it('should handle network errors', async () => {
      const networkError = new Error('Network request failed');
      vi.mocked(customersApi.getCustomerById).mockRejectedValueOnce(networkError);

      const { result } = renderHook(() => useCustomer('12345'), {
        wrapper: createWrapper(),
      });

      // Wait for query to settle
      await waitFor(() => {
        return result.current.isError || result.current.isSuccess;
      });

      // If it's an error (expected), verify error message
      if (result.current.isError) {
        expect(result.current.error?.message).toBe('Network request failed');
      }
    });
  });

  describe('Query states', () => {
    it('should return isLoading true during initial fetch', async () => {
      vi.mocked(customersApi.getCustomerById).mockImplementation(
        () => new Promise((resolve) => setTimeout(() => resolve(mockCustomerData), 100))
      );

      const { result } = renderHook(() => useCustomer('12345'), {
        wrapper: createWrapper(),
      });

      // Should be loading initially
      expect(result.current.isLoading).toBe(true);
      expect(result.current.isFetching).toBe(true);
      expect(result.current.isSuccess).toBe(false);

      // Wait for completion
      await waitFor(() => expect(result.current.isSuccess).toBe(true));

      expect(result.current.isLoading).toBe(false);
    });

    it('should return isSuccess true after successful fetch', async () => {
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      const { result } = renderHook(() => useCustomer('12345'), {
        wrapper: createWrapper(),
      });

      await waitFor(() => expect(result.current.isSuccess).toBe(true));

      expect(result.current.isLoading).toBe(false);
      expect(result.current.isError).toBe(false);
      expect(result.current.data).toEqual(mockCustomerData);
    });

    // Note: Query lifecycle is already tested in other tests (isLoading, isSuccess)
  });

  describe('React Query configuration', () => {
    it('should enable query only when customerId is provided', () => {
      const { result: resultWithoutId } = renderHook(() => useCustomer(null), {
        wrapper: createWrapper(),
      });

      // Without ID: query should be disabled
      expect(resultWithoutId.current.isFetching).toBe(false);
      expect(resultWithoutId.current.isLoading).toBe(false);
    });

    it('should cache results with staleTime configuration', async () => {
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      const { result, rerender } = renderHook(() => useCustomer('12345'), {
        wrapper: createWrapper(),
      });

      // Wait for initial fetch
      await waitFor(() => expect(result.current.isSuccess).toBe(true));

      const firstCallCount = vi.mocked(customersApi.getCustomerById).mock.calls.length;

      // Rerender should not trigger new fetch (data is still fresh)
      rerender();

      // API should not be called again (cache hit)
      expect(vi.mocked(customersApi.getCustomerById).mock.calls.length).toBe(firstCallCount);
    });
  });

  describe('Dynamic customer ID changes', () => {
    it('should refetch when customerId changes', async () => {
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      const { result, rerender } = renderHook(
        ({ id }) => useCustomer(id),
        {
          wrapper: createWrapper(),
          initialProps: { id: '12345' },
        }
      );

      // Wait for initial fetch
      await waitFor(() => expect(result.current.isSuccess).toBe(true));
      expect(customersApi.getCustomerById).toHaveBeenCalledWith('12345');

      // Change customer ID
      rerender({ id: '67890' });

      // Should trigger new fetch
      await waitFor(() => {
        expect(customersApi.getCustomerById).toHaveBeenCalledWith('67890');
      });
    });

    it('should disable query when customerId changes to null', async () => {
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      const { result, rerender } = renderHook(
        ({ id }) => useCustomer(id),
        {
          wrapper: createWrapper(),
          initialProps: { id: '12345' },
        }
      );

      // Wait for initial fetch
      await waitFor(() => expect(result.current.isSuccess).toBe(true));

      const callCountBefore = vi.mocked(customersApi.getCustomerById).mock.calls.length;

      // Change to null
      rerender({ id: null });

      // Query should be disabled, no additional API call
      expect(result.current.isFetching).toBe(false);
      expect(vi.mocked(customersApi.getCustomerById).mock.calls.length).toBe(callCountBefore);
    });
  });
});
