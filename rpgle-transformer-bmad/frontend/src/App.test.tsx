/**
 * Unit tests for App component
 *
 * Tests React Router configuration and navigation structure.
 */

import { render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App';
import { describe, it, expect } from 'vitest';

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
