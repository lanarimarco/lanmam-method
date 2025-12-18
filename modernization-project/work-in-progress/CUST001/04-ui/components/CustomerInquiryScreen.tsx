import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import {
    CustomerInquiryRequest,
    CustomerInquiryResponse,
    CustomerData,
    ScreenState,
    CustomerInquiryScreenProps
} from '../types/types';
import '../styles/CustomerInquiry.css';

/**
 * Customer Inquiry Screen (CUST001)
 * Converted from RPGLE display file: CUSTDSP.dds
 * Original program: CUST001
 *
 * This component implements a two-screen workflow:
 * - PROMPT: Customer number entry
 * - DETAIL: Customer information display
 */
export const CustomerInquiryScreen: React.FC<CustomerInquiryScreenProps> = ({
    apiBaseUrl = '/api/customer-inquiry',
    onExit
}) => {
    // Screen state management (PROMPT or DETAIL)
    const [screenState, setScreenState] = useState<ScreenState>(ScreenState.PROMPT);

    // Form input state
    const [customerNumber, setCustomerNumber] = useState<string>('');

    // Customer data from API
    const [customerData, setCustomerData] = useState<CustomerData | null>(null);

    // UI control state
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const [currentTime, setCurrentTime] = useState<string>('');
    const [currentDate, setCurrentDate] = useState<string>('');

    // Update time and date (simulates DDS TIME and DATE keywords)
    useEffect(() => {
        const updateDateTime = () => {
            const now = new Date();
            setCurrentTime(now.toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                hour12: false
            }));
            setCurrentDate(now.toLocaleDateString('en-US', {
                year: '2-digit',
                month: '2-digit',
                day: '2-digit'
            }));
        };

        updateDateTime();
        const interval = setInterval(updateDateTime, 1000);
        return () => clearInterval(interval);
    }, []);

    /**
     * Validate customer number input
     * Maps to RPG validation logic (lines 44-47 in original program)
     */
    const validateInput = (): boolean => {
        const custNo = parseInt(customerNumber);

        if (!customerNumber || custNo === 0 || isNaN(custNo)) {
            setError('Customer number is required');
            return false;
        }

        if (custNo < 0 || custNo > 99999) {
            setError('Customer number must be between 1 and 99999');
            return false;
        }

        return true;
    };

    /**
     * Handle form submission (Enter key)
     * Maps to EXFMT PROMPT and subsequent CHAIN operation
     */
    const handleSubmit = async (e?: React.FormEvent) => {
        if (e) e.preventDefault();

        // Clear previous errors (maps to *IN90 = *OFF)
        setError('');

        // Validate input
        if (!validateInput()) {
            return;
        }

        setLoading(true);

        try {
            const request: CustomerInquiryRequest = {
                customerNumber: parseInt(customerNumber),
            };

            const response = await axios.post<CustomerInquiryResponse>(
                `${apiBaseUrl}/process`,
                request
            );

            if (response.data.success && response.data.customerName) {
                // Customer found - display DETAIL screen
                setCustomerData({
                    customerNumber: response.data.customerNumber,
                    customerName: response.data.customerName || '',
                    address1: response.data.address1 || '',
                    city: response.data.city || '',
                    state: response.data.state || '',
                    zipCode: response.data.zipCode || 0,
                    phone: response.data.phone || '',
                    balance: response.data.balance || 0,
                    creditLimit: response.data.creditLimit,
                    lastOrderDate: response.data.lastOrderDate
                });
                setScreenState(ScreenState.DETAIL);
            } else {
                // Customer not found (maps to NOT %FOUND condition)
                setError(response.data.errorMessage || 'Customer not found');
            }

        } catch (err: any) {
            // Handle API errors
            setError(
                err.response?.data?.errorMessage ||
                'Failed to retrieve customer information. Please try again.'
            );
        } finally {
            setLoading(false);
        }
    };

    /**
     * Handle F3 (Exit) - Maps to CA03 and *IN03
     * Exits the program entirely
     */
    const handleExit = useCallback(() => {
        if (onExit) {
            onExit();
        } else {
            // Default behavior: navigate back
            window.history.back();
        }
    }, [onExit]);

    /**
     * Handle F12 (Cancel/Return) - Maps to CA12 and *IN12
     * Returns to PROMPT screen from DETAIL screen
     */
    const handleCancel = useCallback(() => {
        if (screenState === ScreenState.DETAIL) {
            // Return to PROMPT screen
            setScreenState(ScreenState.PROMPT);
            setCustomerData(null);
            setError('');
        } else {
            // If already on PROMPT, treat as Exit
            handleExit();
        }
    }, [screenState, handleExit]);

    /**
     * Keyboard shortcuts for function keys
     * Maps DDS function key definitions
     */
    useEffect(() => {
        const handleKeyPress = (event: KeyboardEvent) => {
            // F3 or Escape = Exit
            if (event.key === 'F3' || event.key === 'Escape') {
                event.preventDefault();
                handleExit();
            }
            // F12 = Cancel/Return
            else if (event.key === 'F12') {
                event.preventDefault();
                handleCancel();
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [handleExit, handleCancel]);

    /**
     * Format phone number for display
     * Maps to DDS field DPHONE (12A)
     */
    const formatPhone = (phone: string): string => {
        if (!phone) return '';
        const cleaned = phone.replace(/\D/g, '');
        const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
        if (match) {
            return `(${match[1]}) ${match[2]}-${match[3]}`;
        }
        return phone;
    };

    /**
     * Format currency for display
     * Maps to DDS EDTCDE(J) - decimal editing with commas
     */
    const formatCurrency = (amount: number): string => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(amount);
    };

    /**
     * Format zip code for display
     * Maps to DDS EDTCDE(Z) - zero suppression
     */
    const formatZipCode = (zip: number): string => {
        if (!zip || zip === 0) return '';
        return zip.toString().padStart(5, '0');
    };

    /**
     * Render PROMPT screen (Customer Number Entry)
     * Maps to DDS record format PROMPT (lines 13-29)
     */
    const renderPromptScreen = () => (
        <div className="screen-container">
            {/* Header - Maps to DDS lines 15-20 */}
            <div className="screen-header">
                <div className="header-row">
                    <span className="program-id">CUST001</span>
                    <h1 className="screen-title">Customer Inquiry</h1>
                    <span className="header-time">{currentTime}</span>
                </div>
                <div className="header-row">
                    <span className="header-date">{currentDate}</span>
                </div>
            </div>

            {/* Main Form - Maps to DDS lines 22-27 */}
            <form onSubmit={handleSubmit} className="screen-form">
                <div className="form-section">
                    <div className="form-row">
                        <label htmlFor="customerNumber">Customer Number:</label>
                        <input
                            id="customerNumber"
                            type="number"
                            value={customerNumber}
                            onChange={(e) => setCustomerNumber(e.target.value)}
                            min="1"
                            max="99999"
                            autoFocus
                            disabled={loading}
                            className="input-field"
                            aria-label="Customer Number"
                            aria-required="true"
                            aria-invalid={!!error}
                        />
                    </div>
                </div>

                {/* Error Display - Maps to DDS lines 24-27 (indicator 90) */}
                {error && (
                    <div className="error-message" role="alert">
                        <span className="error-label">Error:</span>
                        <span className="error-text">{error}</span>
                    </div>
                )}

                {/* Action Buttons */}
                <div className="button-row">
                    <button
                        type="submit"
                        disabled={loading || !customerNumber}
                        className="btn-primary"
                        aria-label="Submit customer inquiry"
                    >
                        {loading ? 'Processing...' : 'Enter'}
                    </button>
                </div>
            </form>

            {/* Footer - Function Key Help - Maps to DDS line 28-29 */}
            <div className="screen-footer">
                <button
                    type="button"
                    onClick={handleExit}
                    className="btn-function-key"
                    aria-label="Exit application"
                >
                    F3=Exit
                </button>
            </div>
        </div>
    );

    /**
     * Render DETAIL screen (Customer Detail Display)
     * Maps to DDS record format DETAIL (lines 33-59)
     */
    const renderDetailScreen = () => (
        <div className="screen-container">
            {/* Header - Maps to DDS lines 35-40 */}
            <div className="screen-header">
                <div className="header-row">
                    <span className="program-id">CUST001</span>
                    <h1 className="screen-title">Customer Detail</h1>
                    <span className="header-time">{currentTime}</span>
                </div>
                <div className="header-row">
                    <span className="header-date">{currentDate}</span>
                </div>
            </div>

            {/* Customer Detail Display - Maps to DDS lines 42-57 */}
            {customerData && (
                <div className="detail-section">
                    <div className="form-row">
                        <label>Customer Number:</label>
                        <span className="output-field">{customerData.customerNumber}</span>
                    </div>

                    <div className="form-row">
                        <label>Name:</label>
                        <span className="output-field">{customerData.customerName}</span>
                    </div>

                    <div className="form-row">
                        <label>Address:</label>
                        <span className="output-field">{customerData.address1}</span>
                    </div>

                    <div className="form-row">
                        <label>City:</label>
                        <span className="output-field">{customerData.city}</span>
                    </div>

                    <div className="form-row">
                        <label>State:</label>
                        <span className="output-field">{customerData.state}</span>
                    </div>

                    <div className="form-row">
                        <label>Zip:</label>
                        <span className="output-field">{formatZipCode(customerData.zipCode)}</span>
                    </div>

                    <div className="form-row">
                        <label>Phone:</label>
                        <span className="output-field">{formatPhone(customerData.phone)}</span>
                    </div>

                    <div className="form-row">
                        <label>Balance:</label>
                        <span className="output-field balance">
                            {formatCurrency(customerData.balance)}
                        </span>
                    </div>
                </div>
            )}

            {/* Footer - Function Key Help - Maps to DDS lines 58-59 */}
            <div className="screen-footer">
                <button
                    type="button"
                    onClick={handleExit}
                    className="btn-function-key"
                    aria-label="Exit application"
                >
                    F3=Exit
                </button>
                <button
                    type="button"
                    onClick={handleCancel}
                    className="btn-function-key"
                    aria-label="Return to search"
                >
                    F12=Return
                </button>
            </div>
        </div>
    );

    // Render appropriate screen based on state
    return screenState === ScreenState.PROMPT
        ? renderPromptScreen()
        : renderDetailScreen();
};

export default CustomerInquiryScreen;
