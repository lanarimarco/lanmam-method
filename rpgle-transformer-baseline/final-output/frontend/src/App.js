import React, { useState } from 'react';
import './App.css';
import CustomerSearch from './components/CustomerSearch';
import CustomerDetail from './components/CustomerDetail';

function App() {
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [showDetail, setShowDetail] = useState(false);

  const handleCustomerFound = (customer) => {
    setSelectedCustomer(customer);
    setShowDetail(true);
  };

  const handleBack = () => {
    setShowDetail(false);
    setSelectedCustomer(null);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>CUST001 - Customer Inquiry</h1>
        <div className="header-info">
          <span>{new Date().toLocaleDateString()}</span>
          <span>{new Date().toLocaleTimeString()}</span>
        </div>
      </header>

      <main className="App-main">
        {!showDetail ? (
          <CustomerSearch onCustomerFound={handleCustomerFound} />
        ) : (
          <CustomerDetail customer={selectedCustomer} onBack={handleBack} />
        )}
      </main>

      <footer className="App-footer">
        <span>F3=Exit</span>
        {showDetail && <span>F12=Return</span>}
      </footer>
    </div>
  );
}

export default App;
