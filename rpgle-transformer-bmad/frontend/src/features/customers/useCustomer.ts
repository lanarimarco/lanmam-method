/**
 * useCustomer React Query Hook
 *
 * Custom hook for fetching customer data from the API using React Query v5.
 * Integrates with the CUST001 customer inquiry workflow from RPGLE program.
 *
 * Source: source-rpgle/programs/CUST001.rpgle - Customer Inquiry
 * DDS Display File: source-rpgle/dds/display-files/CUSTDSP.dds
 *
 * @module features/customers/useCustomer
 */

import { useQuery } from '@tanstack/react-query';
import { getCustomerById } from '../../api/customers';
import type { CustomerDetailDisplay } from './customer.types';

/**
 * React Query hook for fetching customer details by customer ID.
 *
 * RPGLE Operation Mapping (CUST001.rpgle):
 * - RPGLE CHAIN operation to CUSTMAST file → React Query useQuery with API call
 * - RPGLE %FOUND indicator check → React Query isSuccess/isError states
 * - RPGLE error indicator 90 → React Query error state
 * - RPGLE file positioning → React Query cache with queryKey
 *
 * Original RPGLE Logic:
 * ```rpgle
 * CHAIN CUSTNO CUSTMAST;           // Retrieve customer record by number
 * IF %FOUND(CUSTMAST);              // Check if record found
 *   EXFMT DETAIL;                   // Display detail screen
 * ELSE;
 *   *IN90 = *ON;                    // Set error indicator
 *   PMSG = 'Customer not found';   // Error message
 *   EXFMT PROMPT;                   // Re-display prompt with error
 * ENDIF;
 * ```
 *
 * React Query Implementation:
 * - queryKey: Identifies unique query (like file positioning)
 * - queryFn: Executes API call (replaces CHAIN operation)
 * - enabled: Controls when query runs (replaces conditional CHAIN)
 * - isSuccess/isError: Replaces %FOUND indicator logic
 * - retry: Automatic retry on transient failures (improvement over RPGLE)
 * - staleTime: Cache duration (performance improvement)
 *
 * Features:
 * - Only fetches when customerId is provided (enabled option)
 * - Caches results for 5 minutes (staleTime)
 * - Retries once on failure
 * - Returns loading, error, and data states
 *
 * @param customerId - The customer number to fetch (null to disable query)
 * @returns React Query result with customer data and query states
 *
 * @example
 * ```typescript
 * function CustomerPage() {
 *   const [customerId, setCustomerId] = useState<string | null>(null);
 *   const { data, isLoading, error, isError } = useCustomer(customerId);
 *
 *   if (isLoading) return <div>Loading...</div>;
 *   if (isError) return <div>Error: {error.message}</div>;
 *   if (data) return <CustomerDetail customer={data} />;
 *   return <div>Enter a customer number</div>;
 * }
 * ```
 */
export function useCustomer(customerId: string | null) {
  return useQuery<CustomerDetailDisplay, Error>({
    // RPGLE: File positioning by key (CUSTNO) → React Query cache key
    queryKey: ['customer', customerId],

    // RPGLE: CHAIN operation to CUSTMAST → API GET /customers/{id}
    queryFn: async () => {
      if (!customerId) {
        throw new Error('Customer ID is required');
      }
      return getCustomerById(customerId);
    },

    // RPGLE: Conditional CHAIN (only when CUSTNO provided) → enabled option
    enabled: !!customerId,

    // Improvement over RPGLE: Automatic retry on transient failures
    retry: 1,

    // Improvement over RPGLE: Client-side caching reduces server calls
    staleTime: 5 * 60 * 1000, // 5 minutes - data stays fresh
  });
}
