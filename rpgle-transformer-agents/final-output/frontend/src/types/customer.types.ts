/**
 * Customer Inquiry Type Definitions
 * Converted from RPGLE program CUST001 and CustomerInquiryDTO.java
 */

/**
 * Customer data model
 * Maps to CustomerInquiryDTO from Phase 3 conversion
 */
export interface Customer {
  /**
   * Customer Number - 5 digit numeric
   * Maps to CUSTNO in CUSTMAST and DCUSTNO in CUSTDSP
   */
  customerNumber: number;

  /**
   * Customer Name - 30 characters
   * Maps to CUSTNAME in CUSTMAST and DCUSTNAME in CUSTDSP
   */
  customerName: string;

  /**
   * Address Line 1 - 30 characters
   * Maps to ADDR1 in CUSTMAST and DADDR1 in CUSTDSP
   */
  address: string;

  /**
   * City - 20 characters
   * Maps to CITY in CUSTMAST and DCITY in CUSTDSP
   */
  city: string;

  /**
   * State - 2 characters
   * Maps to STATE in CUSTMAST and DSTATE in CUSTDSP
   */
  state: string;

  /**
   * Zip Code - 5 digit numeric
   * Maps to ZIP in CUSTMAST and DZIP in CUSTDSP
   */
  zipCode: number;

  /**
   * Phone Number - 12 characters
   * Maps to PHONE in CUSTMAST and DPHONE in CUSTDSP
   */
  phone: string;

  /**
   * Account Balance - numeric with 2 decimal places
   * Maps to BALANCE in CUSTMAST and DBALANCE in CUSTDSP
   * Displayed with EDTCDE(J) - decimal editing with commas
   */
  balance: number;

  /**
   * Credit Limit - numeric with 2 decimal places
   * Available in entity but not displayed in RPGLE DETAIL screen
   */
  creditLimit?: number;

  /**
   * Last Order Date - YYYYMMDD format string
   * Available in entity but not displayed in RPGLE DETAIL screen
   */
  lastOrderDate?: string;
}

/**
 * API Error Response
 * Maps to ErrorResponse in CustomerInquiryController
 */
export interface ApiError {
  /**
   * Error message
   */
  message: string;

  /**
   * Error type code
   * Possible values: VALIDATION_ERROR, NOT_FOUND, INTERNAL_ERROR
   */
  error: string;
}

/**
 * Form state for customer inquiry
 */
export interface CustomerInquiryFormState {
  /**
   * Customer number input value
   */
  customerNumber: string;

  /**
   * Current view mode
   */
  viewMode: 'entry' | 'detail';

  /**
   * Loading state during API call
   */
  loading: boolean;

  /**
   * Error message to display
   */
  error: string | null;

  /**
   * Retrieved customer data
   */
  customerData: Customer | null;
}
