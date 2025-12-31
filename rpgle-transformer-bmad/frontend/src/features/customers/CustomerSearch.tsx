/**
 * Customer search form component from DDS PROMPT format.
 *
 * Maps to CUSTDSP.dds - Record Format PROMPT (Lines 13-30)
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * RPGLE Program: CUST001 - Customer Inquiry
 *
 * This component recreates the green-screen customer number entry.
 * User enters customer number, submits on Enter, displays errors if any.
 *
 * @example
 * ```tsx
 * <CustomerSearch
 *   onSearch={(customerNumber) => handleSearch(customerNumber)}
 *   errorMessage={apiError}
 *   isLoading={isSearching}
 * />
 * ```
 */

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  CustomerPromptFormDataSchema,
  type CustomerPromptFormData,
} from './customer.types';

/**
 * Props for CustomerSearch component
 */
export interface CustomerSearchProps {
  /**
   * Callback when user submits valid customer number
   * @param customerNumber - Validated customer number to search
   */
  onSearch: (customerNumber: number) => void;

  /**
   * Optional error message to display (from API call failure)
   * Maps to DDS PMSG field (50A) - Line 27
   */
  errorMessage?: string;

  /**
   * Optional loading state during search
   */
  isLoading?: boolean;
}

/**
 * Customer Search Form Component
 *
 * Implements DDS CUSTDSP PROMPT format as React form.
 * Features:
 * - Customer number input (DDS PCUSTNO field)
 * - Zod schema validation
 * - Enter key submission
 * - Error message display (DDS PMSG field)
 * - Form reset after submission
 */
export function CustomerSearch({
  onSearch,
  errorMessage,
  isLoading = false,
}: CustomerSearchProps) {
  // React Hook Form setup with Zod validation
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<CustomerPromptFormData>({
    resolver: zodResolver(CustomerPromptFormDataSchema),
  });

  /**
   * Form submission handler
   * Calls parent callback with validated customer number
   * Clears form after submission (matching green-screen behavior)
   */
  const onSubmit = (data: CustomerPromptFormData) => {
    onSearch(data.customerNumber);
    reset(); // Clear form like green-screen does after lookup
  };

  return (
    <div className="customer-search">
      {/* Customer Inquiry Title - DDS Line 1, Col 30 */}
      <h2 className="text-xl font-bold mb-4">Customer Inquiry</h2>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        {/* Customer Number Input Field - DDS PCUSTNO (5Y 0) - Line 23 */}
        <div>
          <label
            htmlFor="customerNumber"
            className="block text-sm font-medium mb-1"
          >
            Customer Number:
          </label>
          <input
            id="customerNumber"
            type="number"
            max={99999}
            {...register('customerNumber', { valueAsNumber: true })}
            disabled={isLoading}
            className="border border-gray-300 rounded px-3 py-2 w-full max-w-xs focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
            placeholder="Enter customer number"
            autoFocus
          />
          {/* Validation error from Zod schema */}
          {errors.customerNumber && (
            <p className="text-red-600 text-sm mt-1">
              {errors.customerNumber.message}
            </p>
          )}
        </div>

        {/* Error Message Display - DDS PMSG (50A) - Line 27, Indicator 90 */}
        {errorMessage && (
          <div className="text-red-600 font-semibold">
            <span className="font-bold">Error: </span>
            {errorMessage}
          </div>
        )}

        {/* Submit button (optional - Enter key also submits) */}
        <button
          type="submit"
          disabled={isLoading}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
        >
          {isLoading ? 'Searching...' : 'Search'}
        </button>

        {/* Function key help - DDS Line 23: F3=Exit */}
        <p className="text-sm text-blue-600 mt-2">
          Press Enter to search • F3=Exit (handled by parent)
        </p>
      </form>
    </div>
  );
}
