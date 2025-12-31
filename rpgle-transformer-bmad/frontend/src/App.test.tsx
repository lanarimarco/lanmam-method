/**
 * Unit tests for App component
 *
 * Tests React Router configuration and navigation structure.
 */

import { render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App';
import { describe, it, expect, vi } from 'vitest';

// Mock the API to prevent actual network calls
vi.mock('./api/customers', () => ({
  getCustomerById: vi.fn(),
}));

describe('App Component', () => {
  const renderApp = () => {
    const queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: 0,
        },
      },
    });

    return render(
      <QueryClientProvider client={queryClient}>
        <App />
      </QueryClientProvider>
    );
  };

  describe('Home page', () => {
    it('renders home page with title', () => {
      renderApp();
      expect(screen.getByRole('heading', { name: /rpgle transformer/i })).toBeInTheDocument();
    });

    it('renders link to customer inquiry', () => {
      renderApp();
      const customerLink = screen.getByRole('link', { name: /customer inquiry/i });
      expect(customerLink).toBeInTheDocument();
      expect(customerLink).toHaveAttribute('href', '/customers');
    });

    it('renders tagline', () => {
      renderApp();
      expect(screen.getByText(/modern transformation of legacy rpgle applications/i)).toBeInTheDocument();
    });
  });

  describe('Navigation', () => {
    it('renders customer inquiry link with correct href', () => {
      renderApp();

      // Verify link exists and points to correct route
      const customerLink = screen.getByRole('link', { name: /customer inquiry/i });
      expect(customerLink).toBeInTheDocument();
      expect(customerLink).toHaveAttribute('href', '/customers');

      // Note: Actual navigation testing requires E2E tests (Playwright)
      // jsdom doesn't support full browser navigation behavior
    });
  });

  describe('Route configuration', () => {
    it('mounts CustomerInquiry component at /customers route', () => {
      // Manually navigate to /customers by updating window.history
      window.history.pushState({}, 'Customer Inquiry', '/customers');

      renderApp();

      // Verify CustomerInquiry component is rendered
      expect(screen.getByRole('heading', { name: /customer inquiry/i, level: 1 })).toBeInTheDocument();
      expect(screen.getByLabelText(/customer number/i)).toBeInTheDocument();
    });

    it('renders home page at root route', () => {
      // Ensure we're at root
      window.history.pushState({}, 'Home', '/');

      renderApp();

      // Verify home page content
      expect(screen.getByRole('heading', { name: /rpgle transformer/i })).toBeInTheDocument();
      expect(screen.getByText(/modern transformation/i)).toBeInTheDocument();
    });
  });
});
