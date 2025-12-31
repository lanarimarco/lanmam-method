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
  const [customerId, setCustomerId] = useState<string | null>(null);
  const { data, isLoading, error, isError } = useCustomer(customerId);

  const handleSearch = (customerNumber: number) => {
    setCustomerId(customerNumber.toString());
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-4xl mx-auto p-6">
        {/* Page Title */}
        <h1 className="text-3xl font-bold text-gray-900 mb-8">
          Customer Inquiry
        </h1>

        {/* Search Form */}
        <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
          <CustomerSearch
            onSearch={handleSearch}
            isLoading={isLoading}
            errorMessage={isError ? error?.message : undefined}
          />
        </div>

        {/* Detail Display */}
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
