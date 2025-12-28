/**
 * Customer Inquiry Component (CUST001)
 *
 * React component that replaces the 5250 display file interface
 * Maps to CUSTDSP.dds display file with PROMPT and DETAIL record formats
 */

import React, { useState, FormEvent, KeyboardEvent } from 'react';
import { Customer, CustomerSearchState } from '../../types';
import { getCustomerByNumber } from '../../services/api';
import './CustomerInquiry.css';

/**
 * CustomerInquiry Page Component
 *
 * Implements the customer inquiry workflow:
 * 1. PROMPT screen: User enters customer number
 * 2. DETAIL screen: Display customer information (if found)
 * 3. Error handling: Display validation and not-found errors
 */
export const CustomerInquiry: React.FC = () => {
  // Form state
  const [customerNumber, setCustomerNumber] = useState<string>('');

  // Search state
  const [searchState, setSearchState] = useState<CustomerSearchState>('idle');
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [errorMessage, setErrorMessage] = useState<string>('');

  /**
   * Handle form submission
   * Maps to: User pressing Enter on PROMPT screen
   */
  const handleSearch = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await performSearch();
  };

  /**
   * Perform the customer search
   * Maps to RPGLE logic:
   * - Validate customer number
   * - CHAIN to CUSTMAST
   * - Check %Found()
   * - Display result or error
   */
  const performSearch = async () => {
    // Clear previous results and errors
    setErrorMessage('');
    setCustomer(null);

    // Validate customer number
    // RPGLE: If PCUSTNO = 0
    const custNo = parseInt(customerNumber, 10);
    if (!customerNumber || isNaN(custNo) || custNo === 0) {
      setErrorMessage('Customer number required');
      setSearchState('error');
      return;
    }

    // Additional validation
    if (custNo < 0) {
      setErrorMessage('Customer number must be positive');
      setSearchState('error');
      return;
    }

    // Set loading state
    setSearchState('loading');

    try {
      // Call API: GET /api/customers/{customerNumber}
      // RPGLE: C     PCUSTNO       Chain     CUSTMAST
      const data = await getCustomerByNumber(custNo);

      // Customer found
      // RPGLE: If %Found(CUSTMAST)
      setCustomer(data);
      setSearchState('success');
    } catch (error) {
      // Customer not found or error
      // RPGLE: Else (not found) -> Set *IN90, display error message
      setErrorMessage(error instanceof Error ? error.message : 'An error occurred');
      setSearchState('error');
    }
  };

  /**
   * Handle new search
   * Maps to: F12 (Return) key to go back to PROMPT screen
   */
  const handleNewSearch = () => {
    setCustomerNumber('');
    setCustomer(null);
    setErrorMessage('');
    setSearchState('idle');
  };

  /**
   * Handle keyboard shortcuts
   * Maps to function keys:
   * - F3 = Exit (handled by navigation)
   * - F12 = Return to search
   * - Enter = Submit
   */
  const handleKeyDown = (e: KeyboardEvent) => {
    if (e.key === 'F12' || (e.key === 'Escape' && customer)) {
      e.preventDefault();
      handleNewSearch();
    }
  };

  /**
   * Format currency for display
   * RPGLE: EDTCDE(J) for balance field
   */
  const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(amount);
  };

  /**
   * Format zip code with leading zeros
   * RPGLE: EDTCDE(Z) for zip code field
   */
  const formatZipCode = (zip: number): string => {
    return zip.toString().padStart(5, '0');
  };

  return (
    <div className="customer-inquiry" onKeyDown={handleKeyDown}>
      {/* Header - Maps to line 1 of both PROMPT and DETAIL screens */}
      <div className="page-header">
        <h1 className="program-name">CUST001</h1>
        <h2 className="page-title">
          {customer ? 'Customer Detail' : 'Customer Inquiry'}
        </h2>
      </div>

      {/* PROMPT Screen - Customer Number Entry */}
      {!customer && (
        <div className="prompt-section">
          <form onSubmit={handleSearch} className="search-form">
            <div className="form-group">
              <label htmlFor="customerNumber" className="form-label">
                Customer Number:
              </label>
              <input
                id="customerNumber"
                type="number"
                className="form-input"
                value={customerNumber}
                onChange={(e) => setCustomerNumber(e.target.value)}
                placeholder="Enter customer number"
                autoFocus
                disabled={searchState === 'loading'}
                min="1"
                max="99999"
              />
            </div>

            {/* Error Message Display */}
            {/* RPGLE: PMSG field with *IN90 indicator */}
            {errorMessage && (
              <div className="error-message" role="alert">
                <strong>Error:</strong> {errorMessage}
              </div>
            )}

            {/* Action Buttons */}
            <div className="button-group">
              <button
                type="submit"
                className="btn btn-primary"
                disabled={searchState === 'loading'}
              >
                {searchState === 'loading' ? 'Searching...' : 'Search'}
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={handleNewSearch}
                disabled={searchState === 'loading'}
              >
                Clear
              </button>
            </div>

            {/* Function Key Help */}
            <div className="function-keys">
              F3=Exit
            </div>
          </form>
        </div>
      )}

      {/* DETAIL Screen - Customer Information Display */}
      {customer && (
        <div className="detail-section">
          <div className="customer-info">
            {/* Customer Number */}
            <div className="info-row">
              <span className="info-label">Customer Number:</span>
              <span className="info-value">{customer.customerNumber}</span>
            </div>

            {/* Customer Name */}
            <div className="info-row">
              <span className="info-label">Name:</span>
              <span className="info-value">{customer.customerName}</span>
            </div>

            {/* Address */}
            <div className="info-row">
              <span className="info-label">Address:</span>
              <span className="info-value">{customer.address1}</span>
            </div>

            {/* City */}
            <div className="info-row">
              <span className="info-label">City:</span>
              <span className="info-value">{customer.city}</span>
            </div>

            {/* State */}
            <div className="info-row">
              <span className="info-label">State:</span>
              <span className="info-value">{customer.state}</span>
            </div>

            {/* Zip Code */}
            <div className="info-row">
              <span className="info-label">Zip:</span>
              <span className="info-value">{formatZipCode(customer.zipCode)}</span>
            </div>

            {/* Phone Number */}
            <div className="info-row">
              <span className="info-label">Phone:</span>
              <span className="info-value">{customer.phoneNumber}</span>
            </div>

            {/* Balance */}
            <div className="info-row">
              <span className="info-label">Balance:</span>
              <span className="info-value balance">{formatCurrency(customer.balance)}</span>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="button-group">
            <button
              type="button"
              className="btn btn-primary"
              onClick={handleNewSearch}
              autoFocus
            >
              New Search
            </button>
          </div>

          {/* Function Key Help */}
          <div className="function-keys">
            F3=Exit  F12=Return
          </div>
        </div>
      )}
    </div>
  );
};
