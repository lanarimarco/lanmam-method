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
    queryKey: ['customer', customerId],
    queryFn: async () => {
      if (!customerId) {
        throw new Error('Customer ID is required');
      }
      return getCustomerById(customerId);
    },
    enabled: !!customerId, // Only run query when customerId exists
    retry: 1, // Retry once on failure
    staleTime: 5 * 60 * 1000, // 5 minutes - data stays fresh
  });
}
