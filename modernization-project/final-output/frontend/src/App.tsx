import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { CustomerInquiry } from './pages/CustomerInquiry';

const App: React.FC = () => {
  return (
    <Router>
      <div className="app">
        <header className="app-header">
          <h1>Modernization Application</h1>
          <nav>
            <ul className="nav-menu">
              <li>
                <Link to="/">Home</Link>
              </li>
              <li>
                <Link to="/customer-inquiry">Customer Inquiry</Link>
              </li>
            </ul>
          </nav>
        </header>

        <main className="app-main">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/customer-inquiry" element={<CustomerInquiry />} />
          </Routes>
        </main>

        <footer className="app-footer">
          <p>&copy; 2025 Modernization Project</p>
        </footer>
      </div>
    </Router>
  );
};

const HomePage: React.FC = () => {
  return (
    <div className="home-page">
      <h2>Welcome to the Modernization Application</h2>
      <p>Select a program from the navigation menu above.</p>
      <div className="program-list">
        <h3>Available Programs:</h3>
        <ul>
          <li>
            <Link to="/customer-inquiry">Customer Inquiry (CUST001)</Link>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default App;
