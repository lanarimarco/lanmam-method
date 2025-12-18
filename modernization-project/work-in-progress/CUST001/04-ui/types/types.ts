/**
 * Type definitions for CUST001 - Customer Inquiry
 * Converted from RPGLE display file: CUSTDSP
 * Maps to DTO: CustomerInquiryDTO.java
 */

/**
 * Request payload for customer inquiry
 * Maps to input fields from PROMPT screen
 */
export interface CustomerInquiryRequest {
    customerNumber: number;
    f3Pressed?: boolean;
    f12Pressed?: boolean;
}

/**
 * Response payload from customer inquiry API
 * Maps to CustomerInquiryDTO from backend
 */
export interface CustomerInquiryResponse {
    // Input field
    customerNumber: number;

    // Customer data fields (from DETAIL screen)
    customerName?: string;
    address1?: string;
    city?: string;
    state?: string;
    zipCode?: number;
    phone?: string;
    balance?: number;
    creditLimit?: number;
    lastOrderDate?: string; // ISO date string

    // Control fields
    success: boolean;
    errorMessage?: string;
    errorIndicator: boolean;
    f3Pressed?: boolean;
    f12Pressed?: boolean;
}

/**
 * Customer data display fields
 * Used for the DETAIL screen display
 */
export interface CustomerData {
    customerNumber: number;
    customerName: string;
    address1: string;
    city: string;
    state: string;
    zipCode: number;
    phone: string;
    balance: number;
    creditLimit?: number;
    lastOrderDate?: string;
}

/**
 * Screen state enumeration
 * Represents the two screen formats from DDS: PROMPT and DETAIL
 */
export enum ScreenState {
    PROMPT = 'PROMPT',   // Customer number entry screen
    DETAIL = 'DETAIL'    // Customer detail display screen
}

/**
 * Component props for CustomerInquiryScreen
 */
export interface CustomerInquiryScreenProps {
    apiBaseUrl?: string;
    onExit?: () => void;
}

/**
 * Field validation errors
 */
export interface ValidationErrors {
    customerNumber?: string;
}

/**
 * Format options for numeric fields
 */
export interface FormatOptions {
    /** Format as currency (balance) */
    currency?: boolean;
    /** Format as phone number */
    phone?: boolean;
    /** Format as zip code */
    zipCode?: boolean;
}
