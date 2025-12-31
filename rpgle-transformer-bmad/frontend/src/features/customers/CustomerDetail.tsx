/**
 * CustomerDetail Component
 *
 * Generated from DDS Display File: CUSTDSP.dds - Record Format DETAIL (Lines 33-59)
 * DDS Source: source-rpgle/dds/display-files/CUSTDSP.dds
 * RPGLE Program: CUST001 - Customer Inquiry
 *
 * This component displays customer detail information in a read-only format,
 * matching the original green-screen DETAIL display layout.
 *
 * Purpose: Display complete customer information after successful lookup
 */

import { CustomerDetailDisplay } from './customer.types';

/**
 * Props interface for CustomerDetail component
 */
export interface CustomerDetailProps {
  /**
   * Customer data to display (null if no customer loaded)
   */
  customer: CustomerDetailDisplay | null;

  /**
   * Loading state indicator
   */
  isLoading?: boolean;

  /**
   * Error message to display (if any)
   */
  errorMessage?: string;
}

/**
 * Format customer number with zero-suppression (DDS EDTCDE(Z))
 * @param num - Customer number to format
 * @returns Formatted customer number string
 */
const formatCustomerNumber = (num: number): string => {
  return num.toString();
};

/**
 * Format zip code with zero-suppression (DDS EDTCDE(Z))
 * @param zip - Zip code to format (nullable)
 * @returns Formatted zip code string or empty string if null
 */
const formatZipCode = (zip: number | null): string => {
  return zip !== null ? zip.toString() : '';
};

/**
 * Format account balance with 2 decimal places and thousand separators (DDS EDTCDE(J))
 * @param balance - Account balance to format (nullable)
 * @returns Formatted currency string or empty string if null
 */
const formatAccountBalance = (balance: number | null): string => {
  if (balance === null) return '';

  // Use Intl.NumberFormat for proper currency formatting with thousand separators
  const formatter = new Intl.NumberFormat('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

  return formatter.format(balance);
};

/**
 * Format nullable string field - returns empty string for null
 * @param value - String value (nullable)
 * @returns Value or empty string if null
 */
const formatStringField = (value: string | null): string => {
  return value ?? '';
};

/**
 * CustomerDetail component displays customer information in a read-only format.
 *
 * Maps to CUSTDSP.dds DETAIL format (Lines 33-59) with 8 display fields:
 * - Customer Number (DCUSTNO)
 * - Customer Name (DCUSTNAME)
 * - Address Line 1 (DADDR1)
 * - City (DCITY)
 * - State (DSTATE)
 * - Zip Code (DZIP)
 * - Phone Number (DPHONE)
 * - Account Balance (DBALANCE)
 *
 * @param props - CustomerDetailProps
 * @returns React component displaying customer details or loading/error states
 *
 * @example
 * ```tsx
 * <CustomerDetail
 *   customer={customerData}
 *   isLoading={false}
 *   errorMessage={undefined}
 * />
 * ```
 */
export function CustomerDetail({
  customer,
  isLoading = false,
  errorMessage,
}: CustomerDetailProps): JSX.Element {
  // Error state takes priority over loading state
  if (errorMessage) {
    return (
      <div className="p-6" role="alert">
        <p className="text-red-600 font-semibold">{errorMessage}</p>
      </div>
    );
  }

  // Loading state
  if (isLoading) {
    return (
      <div className="p-6" role="status" aria-live="polite">
        <p className="text-gray-600">Loading customer details...</p>
      </div>
    );
  }

  // No customer data
  if (!customer) {
    return (
      <div className="p-6">
        <p className="text-gray-500">No customer selected</p>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-4">
      {/* DDS Line 37 - Title */}
      <h2 className="text-2xl font-bold text-center mb-6">Customer Detail</h2>

      {/* Customer detail fields using semantic HTML for accessibility */}
      {/* Definition list provides proper semantic structure for screen readers */}
      <dl className="space-y-3 max-w-2xl">
        {/* DDS Line 42-43 - Customer Number (DCUSTNO - 5Y 0) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">Customer Number:</dt>
          <dd className="mt-1 sm:mt-0">{formatCustomerNumber(customer.customerNumber)}</dd>
        </div>

        {/* DDS Line 44-45 - Customer Name (DCUSTNAME - 30A) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">Name:</dt>
          <dd className="mt-1 sm:mt-0">{formatStringField(customer.customerName)}</dd>
        </div>

        {/* DDS Line 46-47 - Address Line 1 (DADDR1 - 30A) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">Address:</dt>
          <dd className="mt-1 sm:mt-0">{formatStringField(customer.addressLine1)}</dd>
        </div>

        {/* DDS Line 48-49 - City (DCITY - 20A) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">City:</dt>
          <dd className="mt-1 sm:mt-0">{formatStringField(customer.city)}</dd>
        </div>

        {/* DDS Line 50-51 - State (DSTATE - 2A) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">State:</dt>
          <dd className="mt-1 sm:mt-0">{formatStringField(customer.state)}</dd>
        </div>

        {/* DDS Line 52-53 - Zip Code (DZIP - 5Y 0) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">Zip:</dt>
          <dd className="mt-1 sm:mt-0">{formatZipCode(customer.zipCode)}</dd>
        </div>

        {/* DDS Line 54-55 - Phone Number (DPHONE - 12A) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">Phone:</dt>
          <dd className="mt-1 sm:mt-0">{formatStringField(customer.phoneNumber)}</dd>
        </div>

        {/* DDS Line 56-57 - Account Balance (DBALANCE - 9Y 2) */}
        <div className="flex flex-col sm:flex-row">
          <dt className="w-full sm:w-48 font-medium">Balance:</dt>
          <dd className="mt-1 sm:mt-0">{formatAccountBalance(customer.accountBalance)}</dd>
        </div>
      </dl>
    </div>
  );
}
