/**
 * Unit tests for CustomerInquiry page component
 *
 * Tests the complete customer inquiry page integrating search form,
 * customer details display, and React Query state management.
 */

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { CustomerInquiry } from '../CustomerInquiry';
import * as customersApi from '../../../api/customers';
import type { CustomerDetailDisplay } from '../customer.types';

// Mock the API module
vi.mock('../../../api/customers');

describe('CustomerInquiry Page', () => {
  // Sample customer data for testing
  const mockCustomerData: CustomerDetailDisplay = {
    customerNumber: 12345,
    customerName: 'ACME Corporation',
    addressLine1: '123 Main Street',
    city: 'Springfield',
    state: 'IL',
    zipCode: 62701,
    phoneNumber: '217-555-0100',
    accountBalance: 1500.50,
  };

  // Create a fresh QueryClient for each test to avoid cache sharing
  let queryClient: QueryClient;

  const createWrapper = () => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: 0, // Disable retry in tests for faster execution
          cacheTime: 0, // Don't cache between tests
          staleTime: 0, // Data is immediately stale
        },
      },
      logger: {
        log: console.log,
        warn: console.warn,
        error: () => {}, // Suppress error logs in tests
      },
    });
    return ({ children }: { children: React.ReactNode }) => (
      <QueryClientProvider client={queryClient}>
        {children}
      </QueryClientProvider>
    );
  };

  const renderComponent = () => {
    const Wrapper = createWrapper();
    return render(
      <Wrapper>
        <CustomerInquiry />
      </Wrapper>
    );
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('Initial page state', () => {
    it('should render page title', () => {
      renderComponent();
      expect(screen.getByRole('heading', { name: 'Customer Inquiry', level: 1 })).toBeInTheDocument();
    });

    it('should render search form initially', () => {
      renderComponent();
      expect(screen.getByLabelText(/customer number/i)).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument();
    });

    it('should render empty customer detail section initially', () => {
      renderComponent();
      // Detail section should show "No customer selected" initially
      expect(screen.getByText(/no customer selected/i)).toBeInTheDocument();
    });

    it('should not fetch data on initial render', () => {
      renderComponent();
      expect(customersApi.getCustomerById).not.toHaveBeenCalled();
    });
  });

  describe('Successful customer search', () => {
    it('should display customer details when search is successful', async () => {
      const user = userEvent.setup();
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      renderComponent();

      // Enter customer number
      const input = screen.getByLabelText(/customer number/i);
      await user.type(input, '12345');

      // Submit search
      const searchButton = screen.getByRole('button', { name: /search/i });
      await user.click(searchButton);

      // Wait for customer details to appear
      await waitFor(() => {
        expect(screen.getByText('ACME Corporation')).toBeInTheDocument();
      });

      // Verify all customer details are displayed
      expect(screen.getByText('123 Main Street')).toBeInTheDocument();
      expect(screen.getByText(/Springfield/)).toBeInTheDocument();
      expect(screen.getByText(/IL/)).toBeInTheDocument();
      expect(screen.getByText('217-555-0100')).toBeInTheDocument();
    });

    it('should call API with correct customer ID as string', async () => {
      const user = userEvent.setup();
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      renderComponent();

      const input = screen.getByLabelText(/customer number/i);
      await user.type(input, '12345');

      const searchButton = screen.getByRole('button', { name: /search/i });
      await user.click(searchButton);

      await waitFor(() => {
        expect(customersApi.getCustomerById).toHaveBeenCalledWith('12345');
      });
      expect(customersApi.getCustomerById).toHaveBeenCalledTimes(1);
    });

    it('should display loading state while fetching', async () => {
      const user = userEvent.setup();

      // Mock slow API response
      vi.mocked(customersApi.getCustomerById).mockImplementation(
        () => new Promise((resolve) => setTimeout(() => resolve(mockCustomerData), 100))
      );

      renderComponent();

      const input = screen.getByLabelText(/customer number/i);
      await user.type(input, '12345');

      const searchButton = screen.getByRole('button', { name: /search/i });
      await user.click(searchButton);

      // Should show loading state
      expect(screen.getByText(/loading customer details/i)).toBeInTheDocument();

      // Wait for completion
      await waitFor(() => {
        expect(screen.getByText('ACME Corporation')).toBeInTheDocument();
      });
    });
  });

  // Note: Error handling is thoroughly tested in useCustomer.test.tsx
  // These page-level tests focus on successful integration scenarios

  describe('Multiple searches', () => {
    it('should allow user to search for different customers', async () => {
      const user = userEvent.setup();

      const customer1: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerNumber: 11111,
        customerName: 'First Customer',
      };

      const customer2: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerNumber: 22222,
        customerName: 'Second Customer',
      };

      vi.mocked(customersApi.getCustomerById)
        .mockResolvedValueOnce(customer1)
        .mockResolvedValueOnce(customer2);

      renderComponent();

      const input = screen.getByLabelText(/customer number/i);
      const searchButton = screen.getByRole('button', { name: /search/i });

      // First search
      await user.type(input, '11111');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByText('First Customer')).toBeInTheDocument();
      });

      // Second search
      await user.clear(input);
      await user.type(input, '22222');
      await user.click(searchButton);

      await waitFor(() => {
        expect(screen.getByText('Second Customer')).toBeInTheDocument();
      });

      // First customer should be replaced
      expect(screen.queryByText('First Customer')).not.toBeInTheDocument();
    });

    it('should make separate API calls for each search', async () => {
      const user = userEvent.setup();
      vi.mocked(customersApi.getCustomerById).mockResolvedValue(mockCustomerData);

      renderComponent();

      const input = screen.getByLabelText(/customer number/i);
      const searchButton = screen.getByRole('button', { name: /search/i });

      // First search
      await user.type(input, '12345');
      await user.click(searchButton);

      await waitFor(() => {
        expect(customersApi.getCustomerById).toHaveBeenCalledWith('12345');
      });

      // Second search
      await user.clear(input);
      await user.type(input, '67890');
      await user.click(searchButton);

      await waitFor(() => {
        expect(customersApi.getCustomerById).toHaveBeenCalledWith('67890');
      });

      expect(customersApi.getCustomerById).toHaveBeenCalledTimes(2);
    });
  });

  describe('Form validation', () => {
    it('should not search with empty customer number', async () => {
      const user = userEvent.setup();
      renderComponent();

      const searchButton = screen.getByRole('button', { name: /search/i });

      // Try to search without entering anything
      await user.click(searchButton);

      // API should not be called
      expect(customersApi.getCustomerById).not.toHaveBeenCalled();

      // Should show validation error for NaN
      await waitFor(() => {
        expect(screen.getByText(/expected number, received nan/i)).toBeInTheDocument();
      });
    });

    // Note: Detailed validation scenarios (max value, negative numbers, etc.)
    // are tested in CustomerSearch.test.tsx
  });

  describe('Responsive layout', () => {
    it('should render with proper container structure', () => {
      renderComponent();

      // Check for responsive container classes
      const container = screen.getByRole('heading', { name: 'Customer Inquiry', level: 1 }).closest('div');
      expect(container).toHaveClass('max-w-4xl');
    });

    it('should separate search and detail sections', () => {
      renderComponent();

      // Both sections should have white backgrounds and rounded corners
      const sections = screen.getAllByRole('generic').filter(
        el => el.className.includes('bg-white') && el.className.includes('rounded-lg')
      );

      expect(sections.length).toBeGreaterThanOrEqual(2);
    });
  });
});
