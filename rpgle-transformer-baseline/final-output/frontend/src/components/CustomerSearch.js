import React, { useState } from 'react';
import customerService from '../services/customerService';
import './CustomerSearch.css';

function CustomerSearch({ onCustomerFound }) {
  const [customerNumber, setCustomerNumber] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!customerNumber || customerNumber === '0') {
      setError('Customer number required');
      return;
    }

    setLoading(true);

    try {
      const customer = await customerService.getCustomerByNumber(parseInt(customerNumber));
      onCustomerFound(customer);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'F3' || (e.key === 'Escape')) {
      window.close();
    }
  };

  return (
    <div className="customer-search" onKeyDown={handleKeyDown}>
      <form onSubmit={handleSubmit} className="search-form">
        <div className="form-group">
          <label htmlFor="customerNumber">Customer Number:</label>
          <input
            type="number"
            id="customerNumber"
            value={customerNumber}
            onChange={(e) => setCustomerNumber(e.target.value)}
            className="input-field"
            autoFocus
            disabled={loading}
            min="1"
            max="99999"
          />
        </div>

        {error && (
          <div className="error-message">
            <span className="error-label">Error: </span>
            <span>{error}</span>
          </div>
        )}

        <button type="submit" className="submit-button" disabled={loading}>
          {loading ? 'Searching...' : 'Search'}
        </button>
      </form>
    </div>
  );
}

export default CustomerSearch;
