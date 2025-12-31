/**
 * Unit tests for CustomerDetail component
 *
 * Tests display-only component for customer detail information from DDS DETAIL format.
 * Covers all 8 fields, loading states, error states, and null field handling.
 */

import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { CustomerDetail } from '../CustomerDetail';
import { CustomerDetailDisplay } from '../customer.types';

describe('CustomerDetail Component', () => {
  // Sample test data matching DDS DETAIL format
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

  describe('Rendering with valid customer data', () => {
    it('should render the component without errors', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Customer Detail')).toBeInTheDocument();
    });

    it('should display customer number (DCUSTNO)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Customer Number:')).toBeInTheDocument();
      expect(screen.getByText('12345')).toBeInTheDocument();
    });

    it('should display customer name (DCUSTNAME)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Name:')).toBeInTheDocument();
      expect(screen.getByText('ACME Corporation')).toBeInTheDocument();
    });

    it('should display address line 1 (DADDR1)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Address:')).toBeInTheDocument();
      expect(screen.getByText('123 Main Street')).toBeInTheDocument();
    });

    it('should display city (DCITY)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('City:')).toBeInTheDocument();
      expect(screen.getByText('Springfield')).toBeInTheDocument();
    });

    it('should display state (DSTATE)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('State:')).toBeInTheDocument();
      expect(screen.getByText('IL')).toBeInTheDocument();
    });

    it('should display zip code (DZIP)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Zip:')).toBeInTheDocument();
      expect(screen.getByText('62701')).toBeInTheDocument();
    });

    it('should display phone number (DPHONE)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Phone:')).toBeInTheDocument();
      expect(screen.getByText('217-555-0100')).toBeInTheDocument();
    });

    it('should display account balance (DBALANCE)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Balance:')).toBeInTheDocument();
      expect(screen.getByText('1500.50')).toBeInTheDocument();
    });
  });

  describe('Null field handling', () => {
    it('should handle null customer name gracefully', () => {
      const customerWithNullName: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerName: null,
      };
      render(<CustomerDetail customer={customerWithNullName} />);
      expect(screen.getByText('Name:')).toBeInTheDocument();
      // Should display empty string for null values
    });

    it('should handle null address gracefully', () => {
      const customerWithNullAddress: CustomerDetailDisplay = {
        ...mockCustomerData,
        addressLine1: null,
      };
      render(<CustomerDetail customer={customerWithNullAddress} />);
      expect(screen.getByText('Address:')).toBeInTheDocument();
    });

    it('should handle null city gracefully', () => {
      const customerWithNullCity: CustomerDetailDisplay = {
        ...mockCustomerData,
        city: null,
      };
      render(<CustomerDetail customer={customerWithNullCity} />);
      expect(screen.getByText('City:')).toBeInTheDocument();
    });

    it('should handle null state gracefully', () => {
      const customerWithNullState: CustomerDetailDisplay = {
        ...mockCustomerData,
        state: null,
      };
      render(<CustomerDetail customer={customerWithNullState} />);
      expect(screen.getByText('State:')).toBeInTheDocument();
    });

    it('should handle null zip code gracefully', () => {
      const customerWithNullZip: CustomerDetailDisplay = {
        ...mockCustomerData,
        zipCode: null,
      };
      render(<CustomerDetail customer={customerWithNullZip} />);
      expect(screen.getByText('Zip:')).toBeInTheDocument();
      // Should display empty string for null zip
    });

    it('should handle null phone number gracefully', () => {
      const customerWithNullPhone: CustomerDetailDisplay = {
        ...mockCustomerData,
        phoneNumber: null,
      };
      render(<CustomerDetail customer={customerWithNullPhone} />);
      expect(screen.getByText('Phone:')).toBeInTheDocument();
    });

    it('should handle null account balance gracefully', () => {
      const customerWithNullBalance: CustomerDetailDisplay = {
        ...mockCustomerData,
        accountBalance: null,
      };
      render(<CustomerDetail customer={customerWithNullBalance} />);
      expect(screen.getByText('Balance:')).toBeInTheDocument();
      // Should display empty string for null balance
    });

    it('should handle all null fields gracefully', () => {
      const customerWithAllNulls: CustomerDetailDisplay = {
        customerNumber: 99999,
        customerName: null,
        addressLine1: null,
        city: null,
        state: null,
        zipCode: null,
        phoneNumber: null,
        accountBalance: null,
      };
      render(<CustomerDetail customer={customerWithAllNulls} />);
      expect(screen.getByText('Customer Detail')).toBeInTheDocument();
      expect(screen.getByText('99999')).toBeInTheDocument();
    });
  });

  describe('Number formatting', () => {
    it('should format customer number with zero-suppression (DDS EDTCDE(Z))', () => {
      const customerWithSmallNumber: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerNumber: 123, // Should display as "123" not "00123"
      };
      render(<CustomerDetail customer={customerWithSmallNumber} />);
      expect(screen.getByText('123')).toBeInTheDocument();
    });

    it('should format zip code with zero-suppression (DDS EDTCDE(Z))', () => {
      const customerWithSmallZip: CustomerDetailDisplay = {
        ...mockCustomerData,
        zipCode: 1234, // Should display as "1234" not "01234"
      };
      render(<CustomerDetail customer={customerWithSmallZip} />);
      expect(screen.getByText('1234')).toBeInTheDocument();
    });

    it('should format account balance with 2 decimal places (DDS EDTCDE(J))', () => {
      const customerWithWholeBalance: CustomerDetailDisplay = {
        ...mockCustomerData,
        accountBalance: 1000, // Should display as "1000.00"
      };
      render(<CustomerDetail customer={customerWithWholeBalance} />);
      expect(screen.getByText('1000.00')).toBeInTheDocument();
    });

    it('should format account balance with exactly 2 decimals', () => {
      const customerWithDecimal: CustomerDetailDisplay = {
        ...mockCustomerData,
        accountBalance: 1500.5, // Should display as "1500.50" not "1500.5"
      };
      render(<CustomerDetail customer={customerWithDecimal} />);
      expect(screen.getByText('1500.50')).toBeInTheDocument();
    });

    it('should handle negative account balance', () => {
      const customerWithNegativeBalance: CustomerDetailDisplay = {
        ...mockCustomerData,
        accountBalance: -250.75,
      };
      render(<CustomerDetail customer={customerWithNegativeBalance} />);
      expect(screen.getByText('-250.75')).toBeInTheDocument();
    });

    it('should handle zero account balance', () => {
      const customerWithZeroBalance: CustomerDetailDisplay = {
        ...mockCustomerData,
        accountBalance: 0,
      };
      render(<CustomerDetail customer={customerWithZeroBalance} />);
      expect(screen.getByText('0.00')).toBeInTheDocument();
    });
  });

  describe('Loading state', () => {
    it('should display loading message when isLoading is true', () => {
      render(<CustomerDetail customer={null} isLoading={true} />);
      expect(screen.getByText('Loading customer details...')).toBeInTheDocument();
    });

    it('should have role="status" and aria-live="polite" for accessibility', () => {
      render(<CustomerDetail customer={null} isLoading={true} />);
      const loadingDiv = screen.getByRole('status');
      expect(loadingDiv).toHaveAttribute('aria-live', 'polite');
    });

    it('should not display customer data when loading', () => {
      render(<CustomerDetail customer={mockCustomerData} isLoading={true} />);
      expect(screen.queryByText('Customer Detail')).not.toBeInTheDocument();
      expect(screen.queryByText('ACME Corporation')).not.toBeInTheDocument();
    });
  });

  describe('Error state', () => {
    it('should display error message when errorMessage is provided', () => {
      render(<CustomerDetail customer={null} errorMessage="Customer not found" />);
      expect(screen.getByText('Customer not found')).toBeInTheDocument();
    });

    it('should have role="alert" for accessibility', () => {
      render(<CustomerDetail customer={null} errorMessage="An error occurred" />);
      expect(screen.getByRole('alert')).toBeInTheDocument();
    });

    it('should display error message with red styling', () => {
      render(<CustomerDetail customer={null} errorMessage="Error occurred" />);
      const errorElement = screen.getByText('Error occurred');
      expect(errorElement).toHaveClass('text-red-600');
    });

    it('should not display customer data when error is present', () => {
      render(
        <CustomerDetail
          customer={mockCustomerData}
          errorMessage="Invalid customer"
        />
      );
      expect(screen.queryByText('Customer Detail')).not.toBeInTheDocument();
      expect(screen.queryByText('ACME Corporation')).not.toBeInTheDocument();
    });
  });

  describe('No customer state', () => {
    it('should display "No customer selected" when customer is null', () => {
      render(<CustomerDetail customer={null} />);
      expect(screen.getByText('No customer selected')).toBeInTheDocument();
    });

    it('should not display customer detail fields when customer is null', () => {
      render(<CustomerDetail customer={null} />);
      expect(screen.queryByText('Customer Number:')).not.toBeInTheDocument();
      expect(screen.queryByText('Name:')).not.toBeInTheDocument();
    });
  });

  describe('Layout and structure', () => {
    it('should display title at the top (DDS Line 37)', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      const title = screen.getByText('Customer Detail');
      expect(title.tagName).toBe('H2');
    });

    it('should display all 8 field labels in order', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      const labels = [
        'Customer Number:',
        'Name:',
        'Address:',
        'City:',
        'State:',
        'Zip:',
        'Phone:',
        'Balance:',
      ];

      labels.forEach((label) => {
        expect(screen.getByText(label)).toBeInTheDocument();
      });
    });
  });

  describe('Component props', () => {
    it('should use default value false for isLoading when not provided', () => {
      render(<CustomerDetail customer={mockCustomerData} />);
      expect(screen.getByText('Customer Detail')).toBeInTheDocument();
      expect(screen.queryByText('Loading customer details...')).not.toBeInTheDocument();
    });

    it('should handle all props provided', () => {
      render(
        <CustomerDetail
          customer={mockCustomerData}
          isLoading={false}
          errorMessage={undefined}
        />
      );
      expect(screen.getByText('Customer Detail')).toBeInTheDocument();
    });
  });

  describe('Edge cases', () => {
    it('should handle very long customer name (truncation not required per DDS 30A)', () => {
      const customerWithLongName: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerName: 'A'.repeat(30), // Max 30 chars per DDS
      };
      render(<CustomerDetail customer={customerWithLongName} />);
      expect(screen.getByText('A'.repeat(30))).toBeInTheDocument();
    });

    it('should handle minimum customer number (1)', () => {
      const customerWithMinNumber: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerNumber: 1,
      };
      render(<CustomerDetail customer={customerWithMinNumber} />);
      expect(screen.getByText('1')).toBeInTheDocument();
    });

    it('should handle maximum customer number (99999)', () => {
      const customerWithMaxNumber: CustomerDetailDisplay = {
        ...mockCustomerData,
        customerNumber: 99999,
      };
      render(<CustomerDetail customer={customerWithMaxNumber} />);
      expect(screen.getByText('99999')).toBeInTheDocument();
    });

    it('should handle large account balance', () => {
      const customerWithLargeBalance: CustomerDetailDisplay = {
        ...mockCustomerData,
        accountBalance: 9999999.99, // DDS: 9Y 2 = 9 digits total, 2 decimal
      };
      render(<CustomerDetail customer={customerWithLargeBalance} />);
      expect(screen.getByText('9999999.99')).toBeInTheDocument();
    });
  });
});
