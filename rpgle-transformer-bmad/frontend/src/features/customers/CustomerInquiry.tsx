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

import { useState } from 'react';
import { CustomerSearch } from './CustomerSearch';
import { CustomerDetail } from './CustomerDetail';
import { useCustomer } from './useCustomer';

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
 *
 * Features:
 * - Customer number search form
 * - Real-time customer data fetching with React Query
 * - Loading and error states
 * - Responsive layout
 *
 * @example
 * ```tsx
 * // In App.tsx routing:
 * <Route path="/customers" element={<CustomerInquiry />} />
 * ```
 */
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
