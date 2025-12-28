import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

// Import program pages here as they are integrated
import { CustomerInquiry } from './pages/CustomerInquiry';

const App: React.FC = () => {
  return (
    <Router>
      <div className="app">
        <header className="app-header">
          <h1>ERP Modernization</h1>
          <nav>
            {/* Add navigation links here as programs are integrated */}
            <a href="/customer-inquiry">Customer Inquiry</a>
          </nav>
        </header>

        <main className="app-main">
          <Routes>
            {/* Add routes here as programs are integrated */}
            <Route path="/customer-inquiry" element={<CustomerInquiry />} />

            <Route path="/" element={
              <div className="welcome">
                <h2>Welcome to the Modernized ERP System</h2>
                <p>Select a function from the navigation menu.</p>
              </div>
            } />

            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </main>

        <footer className="app-footer">
          <p>Modernized from RPGLE - {new Date().getFullYear()}</p>
        </footer>
      </div>
    </Router>
  );
};

export default App;
