/**
 * Application Root Component
 *
 * Main application component with routing configuration.
 * Implements React Router v6 for navigation.
 */

import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { CustomerInquiry } from './features/customers';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/customers" element={<CustomerInquiry />} />
        <Route
          path="/"
          element={
            <div className="min-h-screen bg-gray-50 flex items-center justify-center">
              <div className="text-center">
                <h1 className="text-4xl font-bold text-gray-900 mb-4">
                  RPGLE Transformer
                </h1>
                <p className="text-gray-600 mb-8">
                  Modern transformation of legacy RPGLE applications
                </p>
                <a
                  href="/customers"
                  className="inline-block bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Customer Inquiry
                </a>
              </div>
            </div>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
