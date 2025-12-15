import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './styles.css';

/**
 * {ProgramName} Screen
 * Converted from RPGLE display file: {DISPLAY_FILE}
 * Original program: {PROGRAM_NAME}
 */

interface {ProgramName}Request {
    customerNumber: string;
    f3Pressed?: boolean;
    f12Pressed?: boolean;
}

interface {ProgramName}Response {
    customerNumber: string;
    customerName: string;
    address: string;
    city: string;
    state: string;
    zipCode: string;
    balance: number;
    success: boolean;
    errorMessage?: string;
}

export const {ProgramName}Screen: React.FC = () => {
    // State for form fields
    const [customerNumber, setCustomerNumber] = useState<string>('');
    const [customerData, setCustomerData] = useState<{ProgramName}Response | null>(null);

    // State for UI control
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');

    // Handle form submission (Enter key in RPG)
    const handleSubmit = async (e?: React.FormEvent) => {
        if (e) e.preventDefault();

        setLoading(true);
        setError('');

        try {
            const request: {ProgramName}Request = {
                customerNumber: customerNumber,
            };

            const response = await axios.post<{ProgramName}Response>(
                '/api/{resource}/process',
                request
            );

            if (response.data.success) {
                setCustomerData(response.data);
            } else {
                setError(response.data.errorMessage || 'Unknown error');
            }

        } catch (err: any) {
            setError(err.response?.data?.errorMessage || 'Failed to process request');
        } finally {
            setLoading(false);
        }
    };

    // Handle F3 (Exit)
    const handleExit = () => {
        // Navigate back or close
        window.history.back();
    };

    // Handle F12 (Cancel)
    const handleCancel = () => {
        setCustomerNumber('');
        setCustomerData(null);
        setError('');
    };

    // Keyboard shortcuts
    useEffect(() => {
        const handleKeyPress = (event: KeyboardEvent) => {
            if (event.key === 'F3' || event.key === 'Escape') {
                event.preventDefault();
                handleExit();
            } else if (event.key === 'F12') {
                event.preventDefault();
                handleCancel();
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, []);

    return (
        <div className="screen-container">
            <div className="screen-header">
                <h1>{Program Title}</h1>
            </div>

            <form onSubmit={handleSubmit} className="screen-form">
                {/* Input Section */}
                <div className="form-section">
                    <div className="form-row">
                        <label htmlFor="customerNumber">Customer Number:</label>
                        <input
                            id="customerNumber"
                            type="text"
                            value={customerNumber}
                            onChange={(e) => setCustomerNumber(e.target.value)}
                            maxLength={7}
                            autoFocus
                            disabled={loading}
                        />
                    </div>
                </div>

                {/* Display Section (if data loaded) */}
                {customerData && (
                    <div className="form-section">
                        <div className="form-row">
                            <label>Name:</label>
                            <span className="output-field">{customerData.customerName}</span>
                        </div>
                        <div className="form-row">
                            <label>Address:</label>
                            <span className="output-field">{customerData.address}</span>
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
                            <label>ZIP:</label>
                            <span className="output-field">{customerData.zipCode}</span>
                        </div>
                        <div className="form-row">
                            <label>Balance:</label>
                            <span className="output-field">
                                ${customerData.balance.toFixed(2)}
                            </span>
                        </div>
                    </div>
                )}

                {/* Error Display */}
                {error && (
                    <div className="error-message">
                        {error}
                    </div>
                )}

                {/* Action Buttons */}
                <div className="button-row">
                    <button
                        type="submit"
                        disabled={loading || !customerNumber}
                        className="btn-primary"
                    >
                        {loading ? 'Processing...' : 'Enter'}
                    </button>
                    <button
                        type="button"
                        onClick={handleCancel}
                        className="btn-secondary"
                    >
                        F12-Cancel
                    </button>
                    <button
                        type="button"
                        onClick={handleExit}
                        className="btn-secondary"
                    >
                        F3-Exit
                    </button>
                </div>
            </form>

            {/* Function Key Help */}
            <div className="screen-footer">
                <span>F3=Exit  F12=Cancel  Enter=Submit</span>
            </div>
        </div>
    );
};

export default {ProgramName}Screen;
