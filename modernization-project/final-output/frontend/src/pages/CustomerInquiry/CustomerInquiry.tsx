/**
 * Customer Inquiry Page Component
 * Converted from RPGLE program CUST001 with display file CUSTDSP
 *
 * Implements two-screen flow:
 * 1. PROMPT screen: Customer number entry (lines 13-29 in CUSTDSP.dds)
 * 2. DETAIL screen: Customer information display (lines 33-59 in CUSTDSP.dds)
 */

import React, { useState, useEffect } from 'react';
import { Customer, ApiError } from '../../types';
import { customerService } from '../../services/api';
import './CustomerInquiry.css';

/**
 * CustomerInquiry component
 * Main page component for customer inquiry functionality
 */
export const CustomerInquiry: React.FC = () => {
  // State management (maps to RPGLE variables and screen modes)
  const [viewMode, setViewMode] = useState<'entry' | 'detail'>('entry');
  const [customerNumber, setCustomerNumber] = useState<string>('');
  const [customerData, setCustomerData] = useState<Customer | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  /**
   * Handle keyboard shortcuts
   * Maps to function keys in CUSTDSP.dds:
   * - F3 (Exit): Close/Navigate away (line 8, 28, 58)
   * - F12 (Cancel): Return to entry screen (line 9, 58)
   * - Enter: Submit form
   */
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      // F3 or Escape - Exit
      if (event.key === 'F3' || event.key === 'Escape') {
        event.preventDefault();
        handleExit();
      }
      // F12 - Return to entry screen (only in detail view)
      else if (event.key === 'F12' && viewMode === 'detail') {
        event.preventDefault();
        handleReturn();
      }
      // Enter - Submit form (only in entry view)
      else if (event.key === 'Enter' && viewMode === 'entry') {
        event.preventDefault();
        handleSubmit();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [viewMode, customerNumber]);

  /**
   * Handle form submission
   * Maps to RPGLE lines 43-72:
   * - Validate customer number
   * - CHAIN to CUSTMAST
   * - Display results or error
   */
  const handleSubmit = async (event?: React.FormEvent) => {
    if (event) {
      event.preventDefault();
    }

    // Clear previous error (maps to RPGLE lines 38-40)
    setError(null);

    // Client-side validation (maps to RPGLE lines 43-48)
    const custNo = parseInt(customerNumber, 10);
    if (!customerNumber || isNaN(custNo) || custNo === 0) {
      setError('Customer number required');
      return;
    }

    // API call (maps to RPGLE lines 51-70: CHAIN operation)
    setLoading(true);
    try {
      const customer = await customerService.getCustomer(custNo);

      // Customer found (maps to RPGLE lines 53-64)
      setCustomerData(customer);
      setViewMode('detail');
      setError(null);
    } catch (err) {
      // Customer not found or validation error (maps to RPGLE lines 66-70)
      const apiError = err as ApiError;
      setError(apiError.message || 'An unexpected error occurred');
      setCustomerData(null);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle return to entry screen
   * Maps to F12 function key (line 58 in CUSTDSP.dds)
   */
  const handleReturn = () => {
    setViewMode('entry');
    setCustomerNumber('');
    setCustomerData(null);
    setError(null);
  };

  /**
   * Handle exit
   * Maps to F3 function key (lines 8, 28, 58 in CUSTDSP.dds)
   * In a real application, this would navigate away or close the window
   */
  const handleExit = () => {
    // Integration Agent will configure this based on routing setup
    // For now, just return to entry screen and clear state
    setViewMode('entry');
    setCustomerNumber('');
    setCustomerData(null);
    setError(null);

    // Could also use: window.history.back() or navigate('/') with React Router
  };

  /**
   * Format currency values
   * Maps to EDTCDE(J) in CUSTDSP.dds (line 57)
   * Provides decimal editing with commas
   */
  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(value);
  };

  /**
   * Format phone number
   * Maps to DPHONE field (12A) in CUSTDSP.dds (line 55)
   */
  const formatPhone = (phone: string): string => {
    // Simple formatting: XXX-XXX-XXXX
    const cleaned = phone.replace(/\D/g, '');
    const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
    if (match) {
      return `${match[1]}-${match[2]}-${match[3]}`;
    }
    return phone;
  };

  /**
   * Format zip code
   * Maps to DZIP field with EDTCDE(Z) in CUSTDSP.dds (line 53)
   */
  const formatZip = (zip: number): string => {
    return zip.toString().padStart(5, '0');
  };

  /**
   * Get current date and time for header
   * Maps to DATE and TIME fields in CUSTDSP.dds (lines 19-21, 39-41)
   */
  const getCurrentDateTime = () => {
    const now = new Date();
    const time = now.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    });
    const date = now.toLocaleDateString('en-US', {
      month: '2-digit',
      day: '2-digit',
      year: '2-digit'
    });
    return { time, date };
  };

  const { time, date } = getCurrentDateTime();

  // Render PROMPT screen (entry mode)
  // Maps to CUSTDSP.dds lines 13-29
  if (viewMode === 'entry') {
    return (
      <div className="customer-inquiry">
        <div className="screen-container">
          {/* Header - maps to lines 15-21 in CUSTDSP.dds */}
          <header className="screen-header">
            <div className="header-left">
              <span className="program-name">CUST001</span>
              <span className="screen-title">Customer Inquiry</span>
            </div>
            <div className="header-right">
              <span className="header-time">{time}</span>
              <span className="header-date">{date}</span>
            </div>
          </header>

          {/* Main content - maps to lines 22-27 in CUSTDSP.dds */}
          <main className="screen-content">
            <form onSubmit={handleSubmit} className="inquiry-form">
              <div className="form-row">
                <label htmlFor="customerNumber" className="form-label">
                  Customer Number:
                </label>
                <input
                  id="customerNumber"
                  type="text"
                  className="form-input"
                  value={customerNumber}
                  onChange={(e) => setCustomerNumber(e.target.value)}
                  maxLength={5}
                  disabled={loading}
                  autoFocus
                  aria-label="Customer Number"
                  aria-required="true"
                  aria-invalid={error ? 'true' : 'false'}
                  aria-describedby={error ? 'error-message' : undefined}
                />
              </div>

              {/* Error message - maps to lines 24-27 in CUSTDSP.dds (indicator 90) */}
              {error && (
                <div className="error-message" id="error-message" role="alert">
                  <span className="error-label">Error:</span>
                  <span className="error-text">{error}</span>
                </div>
              )}

              {/* Submit button (hidden, form submits on Enter) */}
              <button type="submit" style={{ display: 'none' }} aria-hidden="true">
                Submit
              </button>
            </form>
          </main>

          {/* Footer - maps to line 28 in CUSTDSP.dds */}
          <footer className="screen-footer">
            <div className="function-keys">
              <button
                type="button"
                className="function-key"
                onClick={handleExit}
                disabled={loading}
              >
                F3=Exit
              </button>
            </div>
          </footer>

          {/* Loading indicator */}
          {loading && (
            <div className="loading-overlay">
              <div className="loading-spinner" role="status" aria-live="polite">
                <span className="sr-only">Loading customer data...</span>
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }

  // Render DETAIL screen (detail mode)
  // Maps to CUSTDSP.dds lines 33-59
  return (
    <div className="customer-inquiry">
      <div className="screen-container">
        {/* Header - maps to lines 35-41 in CUSTDSP.dds */}
        <header className="screen-header">
          <div className="header-left">
            <span className="program-name">CUST001</span>
            <span className="screen-title">Customer Detail</span>
          </div>
          <div className="header-right">
            <span className="header-time">{time}</span>
            <span className="header-date">{date}</span>
          </div>
        </header>

        {/* Main content - maps to lines 42-57 in CUSTDSP.dds */}
        <main className="screen-content">
          {customerData && (
            <div className="customer-detail">
              {/* Customer Number - line 42-43 */}
              <div className="detail-row">
                <span className="detail-label">Customer Number:</span>
                <span className="detail-value">{customerData.customerNumber}</span>
              </div>

              {/* Customer Name - line 44-45 */}
              <div className="detail-row">
                <span className="detail-label">Name:</span>
                <span className="detail-value">{customerData.customerName}</span>
              </div>

              {/* Address - line 46-47 */}
              <div className="detail-row">
                <span className="detail-label">Address:</span>
                <span className="detail-value">{customerData.address}</span>
              </div>

              {/* City - line 48-49 */}
              <div className="detail-row">
                <span className="detail-label">City:</span>
                <span className="detail-value">{customerData.city}</span>
              </div>

              {/* State - line 50-51 */}
              <div className="detail-row">
                <span className="detail-label">State:</span>
                <span className="detail-value">{customerData.state}</span>
              </div>

              {/* Zip - line 52-53 */}
              <div className="detail-row">
                <span className="detail-label">Zip:</span>
                <span className="detail-value">{formatZip(customerData.zipCode)}</span>
              </div>

              {/* Phone - line 54-55 */}
              <div className="detail-row">
                <span className="detail-label">Phone:</span>
                <span className="detail-value">{formatPhone(customerData.phone)}</span>
              </div>

              {/* Balance - line 56-57 */}
              <div className="detail-row">
                <span className="detail-label">Balance:</span>
                <span className="detail-value detail-value-currency">
                  {formatCurrency(customerData.balance)}
                </span>
              </div>
            </div>
          )}
        </main>

        {/* Footer - maps to line 58 in CUSTDSP.dds */}
        <footer className="screen-footer">
          <div className="function-keys">
            <button
              type="button"
              className="function-key"
              onClick={handleExit}
            >
              F3=Exit
            </button>
            <button
              type="button"
              className="function-key"
              onClick={handleReturn}
            >
              F12=Return
            </button>
          </div>
        </footer>
      </div>
    </div>
  );
};
