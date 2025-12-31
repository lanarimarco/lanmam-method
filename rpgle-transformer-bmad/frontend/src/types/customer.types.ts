/**
 * TypeScript types for Customer domain.
 *
 * Types match backend DTOs from Spring Boot application.
 * Backend Source: backend/src/main/java/com/smeup/backend/dto/CustomerDTO.java
 *
 * DDS Source: CUSTMAST physical file (AS/400)
 */

/**
 * Customer interface matching backend CustomerDTO
 *
 * Maps to DDS Physical File: CUSTMAST
 * Backend DTO: CustomerDTO.java
 */
export interface Customer {
  /**
   * Customer Number (Required)
   * Backend: customerId (Long)
   * DDS Field: CUSTNO (5P 0)
   */
  customerId: number;

  /**
   * Customer Name
   * Backend: customerName (String)
   * DDS Field: CUSTNAME (30A)
   */
  customerName: string | null;

  /**
   * Address Line 1
   * Backend: addressLine1 (String)
   * DDS Field: ADDR1 (30A)
   */
  addressLine1: string | null;

  /**
   * City
   * Backend: city (String)
   * DDS Field: CITY (20A)
   */
  city: string | null;

  /**
   * State Code
   * Backend: state (String)
   * DDS Field: STATE (2A)
   */
  state: string | null;

  /**
   * Zip Code
   * Backend: zipCode (Integer)
   * DDS Field: ZIP (5P 0)
   */
  zipCode: number | null;

  /**
   * Phone Number
   * Backend: phoneNumber (String)
   * DDS Field: PHONE (12A)
   */
  phoneNumber: string | null;

  /**
   * Account Balance
   * Backend: accountBalance (BigDecimal)
   * DDS Field: BALANCE (9P 2)
   * Note: Using number for TypeScript, represents decimal values
   */
  accountBalance: number | null;

  /**
   * Credit Limit
   * Backend: creditLimit (BigDecimal)
   * DDS Field: CREDITLIM (9P 2)
   * Note: Using number for TypeScript, represents decimal values
   */
  creditLimit: number | null;

  /**
   * Last Order Date (YYYYMMDD format)
   * Backend: lastOrderDate (Integer)
   * DDS Field: LASTORDER (8P 0)
   * Note: Stored as integer in YYYYMMDD format, not a Date object
   */
  lastOrderDate: number | null;
}

/**
 * Generic API Response wrapper matching backend ApiResponse<T>
 *
 * Backend Source: backend/src/main/java/com/smeup/backend/dto/ApiResponse.java
 *
 * Structure:
 * {
 *   "data": T,
 *   "meta": { }
 * }
 */
export interface ApiResponse<T> {
  /**
   * Response data payload
   */
  data: T;

  /**
   * Response metadata
   */
  meta: Record<string, unknown>;
}

/**
 * API Error response matching RFC 7807 Problem Details format
 *
 * Backend uses RFC 7807 for error responses
 * Source: architecture.md - API Communication Pattern
 */
export interface ApiError {
  /**
   * HTTP status code
   */
  status: number;

  /**
   * Error message
   */
  message: string;

  /**
   * Additional error data (RFC 7807 fields)
   */
  data: {
    type?: string;
    title?: string;
    detail?: string;
    instance?: string;
  } | null;
}
