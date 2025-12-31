/**
 * Unit tests for CustomerSearch component.
 *
 * Tests verify:
 * - Component rendering with correct labels and inputs
 * - Form validation using Zod schema
 * - User interactions (typing, Enter key submission)
 * - Error message display (both validation and API errors)
 * - Loading state behavior
 * - Form reset after submission
 */

import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { CustomerSearch } from '../CustomerSearch';

describe('CustomerSearch', () => {
  describe('Component Rendering', () => {
    it('should render input field with correct label', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      // Check for "Customer Number:" label
      expect(screen.getByText(/Customer Number:/i)).toBeInTheDocument();

      // Check for input field
      const input = screen.getByPlaceholderText(/Enter customer number/i);
      expect(input).toBeInTheDocument();
      expect(input).toHaveAttribute('type', 'number');
    });

    it('should render title "Customer Inquiry"', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      expect(screen.getByText('Customer Inquiry')).toBeInTheDocument();
    });

    it('should render without error message initially', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      // Error message should not be present
      expect(screen.queryByText(/Error:/i)).not.toBeInTheDocument();
    });

    it('should render with error message when provided', () => {
      const mockOnSearch = vi.fn();
      const errorMsg = 'Customer not found';
      render(<CustomerSearch onSearch={mockOnSearch} errorMessage={errorMsg} />);

      // Error message should be displayed in red
      expect(screen.getByText(/Error:/i)).toBeInTheDocument();
      expect(screen.getByText(errorMsg)).toBeInTheDocument();
    });

    it('should render submit button', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const submitButton = screen.getByRole('button', { name: /Search/i });
      expect(submitButton).toBeInTheDocument();
      expect(submitButton).not.toBeDisabled();
    });

    it('should display function key help text', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      expect(screen.getByText(/Press Enter to search/i)).toBeInTheDocument();
      expect(screen.getByText(/F3=Exit/i)).toBeInTheDocument();
    });
  });

  describe('Form Validation', () => {
    it('should prevent submission with empty input', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const submitButton = screen.getByRole('button', { name: /Search/i });
      await user.click(submitButton);

      // onSearch should not be called with invalid data
      expect(mockOnSearch).not.toHaveBeenCalled();
    });

    it('should prevent submission with negative number', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      await user.type(input, '-123');

      const submitButton = screen.getByRole('button', { name: /Search/i });
      await user.click(submitButton);

      // onSearch should not be called with negative number
      expect(mockOnSearch).not.toHaveBeenCalled();
    });

    it('should prevent submission with zero', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      await user.type(input, '0');

      const submitButton = screen.getByRole('button', { name: /Search/i });
      await user.click(submitButton);

      // onSearch should not be called with zero (Zod schema requires positive)
      expect(mockOnSearch).not.toHaveBeenCalled();
    });

    it('should allow submission with valid positive integer', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      await user.type(input, '12345');

      const submitButton = screen.getByRole('button', { name: /Search/i });
      await user.click(submitButton);

      // onSearch should be called with valid customer number
      expect(mockOnSearch).toHaveBeenCalledWith(12345);
    });

    it('should display validation errors from Zod schema', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const submitButton = screen.getByRole('button', { name: /Search/i });

      // Try to submit without entering anything
      await user.click(submitButton);

      // Wait for validation error to appear (error message: "Expected number, received nan")
      await screen.findByText(/Expected number, received nan/i);
      expect(mockOnSearch).not.toHaveBeenCalled();
    });
  });

  describe('User Interactions', () => {
    it('should update input value when typing', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(
        /Enter customer number/i
      ) as HTMLInputElement;
      await user.type(input, '54321');

      expect(input.value).toBe('54321');
    });

    it('should submit on Enter key press', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      await user.type(input, '99999{Enter}');

      // Form should submit via Enter key
      expect(mockOnSearch).toHaveBeenCalledWith(99999);
    });

    it('should call onSearch callback with validated number', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      await user.type(input, '77777');

      const submitButton = screen.getByRole('button', { name: /Search/i });
      await user.click(submitButton);

      expect(mockOnSearch).toHaveBeenCalledTimes(1);
      expect(mockOnSearch).toHaveBeenCalledWith(77777);
    });

    it('should clear form after successful submission', async () => {
      const user = userEvent.setup();
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(
        /Enter customer number/i
      ) as HTMLInputElement;
      await user.type(input, '12345');

      const submitButton = screen.getByRole('button', { name: /Search/i });
      await user.click(submitButton);

      // Form should be cleared after submission
      expect(input.value).toBe('');
    });
  });

  describe('Error Handling', () => {
    it('should display API error message in red', () => {
      const mockOnSearch = vi.fn();
      const errorMsg = 'Database connection failed';
      render(<CustomerSearch onSearch={mockOnSearch} errorMessage={errorMsg} />);

      const errorElement = screen.getByText(errorMsg);
      expect(errorElement).toBeInTheDocument();
      expect(errorElement.className).toContain('text-red-600');
    });

    it('should display error label "Error:" before message', () => {
      const mockOnSearch = vi.fn();
      const errorMsg = 'Invalid customer ID';
      render(<CustomerSearch onSearch={mockOnSearch} errorMessage={errorMsg} />);

      expect(screen.getByText(/Error:/i)).toBeInTheDocument();
      expect(screen.getByText(errorMsg)).toBeInTheDocument();
    });

    it('should hide error message when errorMessage prop is undefined', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      expect(screen.queryByText(/Error:/i)).not.toBeInTheDocument();
    });

    it('should update error message when errorMessage prop changes', () => {
      const mockOnSearch = vi.fn();
      const { rerender } = render(<CustomerSearch onSearch={mockOnSearch} />);

      // Initially no error
      expect(screen.queryByText(/Error:/i)).not.toBeInTheDocument();

      // Rerender with error
      rerender(
        <CustomerSearch onSearch={mockOnSearch} errorMessage="Server error" />
      );
      expect(screen.getByText('Server error')).toBeInTheDocument();
    });
  });

  describe('Loading State', () => {
    it('should disable input during loading', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} isLoading={true} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      expect(input).toBeDisabled();
    });

    it('should disable submit button during loading', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} isLoading={true} />);

      const submitButton = screen.getByRole('button', { name: /Searching.../i });
      expect(submitButton).toBeDisabled();
    });

    it('should change button text to "Searching..." when loading', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} isLoading={true} />);

      expect(screen.getByText('Searching...')).toBeInTheDocument();
      expect(screen.queryByText('Search')).not.toBeInTheDocument();
    });

    it('should enable input when not loading', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} isLoading={false} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      expect(input).not.toBeDisabled();
    });

    it('should enable submit button when not loading', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} isLoading={false} />);

      const submitButton = screen.getByRole('button', { name: /Search/i });
      expect(submitButton).not.toBeDisabled();
    });
  });

  describe('Accessibility', () => {
    it('should have label associated with input via htmlFor', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const label = screen.getByText(/Customer Number:/i);
      const input = screen.getByPlaceholderText(/Enter customer number/i);

      expect(label).toHaveAttribute('for', 'customerNumber');
      expect(input).toHaveAttribute('id', 'customerNumber');
    });

    it('should autofocus on customer number input', () => {
      const mockOnSearch = vi.fn();
      render(<CustomerSearch onSearch={mockOnSearch} />);

      const input = screen.getByPlaceholderText(/Enter customer number/i);
      // Check for autofocus attribute (React uses lowercase in DOM)
      expect(input).toHaveProperty('autofocus');
    });
  });
});
