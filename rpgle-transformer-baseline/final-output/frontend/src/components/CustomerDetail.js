import React, { useEffect } from 'react';
import './CustomerDetail.css';

function CustomerDetail({ customer, onBack }) {
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.key === 'F12' || (e.key === 'Escape')) {
        onBack();
      } else if (e.key === 'F3') {
        window.close();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [onBack]);

  const formatCurrency = (value) => {
    if (value === null || value === undefined) return '$0.00';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(value);
  };

  const formatZipCode = (zip) => {
    if (!zip) return '';
    return String(zip).padStart(5, '0');
  };

  const formatDate = (date) => {
    if (!date) return '';
    return new Date(date).toLocaleDateString('en-US');
  };

  return (
    <div className="customer-detail">
      <div className="detail-container">
        <h2 className="detail-title">Customer Detail</h2>

        <div className="detail-grid">
          <div className="detail-row">
            <label>Customer Number:</label>
            <span className="detail-value">{customer.customerNumber}</span>
          </div>

          <div className="detail-row">
            <label>Name:</label>
            <span className="detail-value">{customer.customerName}</span>
          </div>

          <div className="detail-row">
            <label>Address:</label>
            <span className="detail-value">{customer.addressLine1 || 'N/A'}</span>
          </div>

          <div className="detail-row">
            <label>City:</label>
            <span className="detail-value">{customer.city || 'N/A'}</span>
          </div>

          <div className="detail-row">
            <label>State:</label>
            <span className="detail-value">{customer.state || 'N/A'}</span>
          </div>

          <div className="detail-row">
            <label>Zip:</label>
            <span className="detail-value">{formatZipCode(customer.zipCode)}</span>
          </div>

          <div className="detail-row">
            <label>Phone:</label>
            <span className="detail-value">{customer.phoneNumber || 'N/A'}</span>
          </div>

          <div className="detail-row highlight">
            <label>Balance:</label>
            <span className="detail-value balance">
              {formatCurrency(customer.accountBalance)}
            </span>
          </div>

          {customer.creditLimit && (
            <div className="detail-row">
              <label>Credit Limit:</label>
              <span className="detail-value">{formatCurrency(customer.creditLimit)}</span>
            </div>
          )}

          {customer.lastOrderDate && (
            <div className="detail-row">
              <label>Last Order Date:</label>
              <span className="detail-value">{formatDate(customer.lastOrderDate)}</span>
            </div>
          )}
        </div>

        <div className="button-container">
          <button onClick={onBack} className="back-button">
            Return to Search
          </button>
        </div>
      </div>
    </div>
  );
}

export default CustomerDetail;
