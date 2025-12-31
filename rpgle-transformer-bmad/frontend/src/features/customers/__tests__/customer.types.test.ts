/**
 * Unit tests for customer feature types.
 *
 * Tests verify:
 * - Type guards work correctly
 * - Type exports are available
 * - Type structures match DDS display file
 */

import { describe, it, expect } from 'vitest';
import {
  type CustomerPromptFormData,
  type CustomerDetailDisplay,
  isCustomerPromptFormData,
  isCustomerDetailDisplay,
  CustomerPromptFormDataSchema,
  CustomerDetailDisplaySchema,
  apiCustomerToDisplay,
} from '../customer.types';

describe('Customer Types', () => {
  describe('CustomerPromptFormData', () => {
    it('should have correct shape for valid prompt data', () => {
      const validData: CustomerPromptFormData = {
        customerNumber: 12345,
      };

      expect(validData.customerNumber).toBe(12345);
      expect(validData.errorMessage).toBeUndefined();
    });

    it('should allow optional error message', () => {
      const dataWithError: CustomerPromptFormData = {
        customerNumber: 12345,
        errorMessage: 'Customer not found',
      };

      expect(dataWithError.errorMessage).toBe('Customer not found');
    });

    it('should validate with type guard for valid data', () => {
      const validData = {
        customerNumber: 12345,
      };

      expect(isCustomerPromptFormData(validData)).toBe(true);
    });

    it('should reject invalid data with type guard', () => {
      const invalidData = {
        customerNumber: '12345', // string instead of number
      };

      expect(isCustomerPromptFormData(invalidData)).toBe(false);
    });

    it('should reject null and undefined', () => {
      expect(isCustomerPromptFormData(null)).toBe(false);
      expect(isCustomerPromptFormData(undefined)).toBe(false);
    });
  });

  describe('CustomerDetailDisplay', () => {
    it('should have correct shape for complete customer data', () => {
      const validData: CustomerDetailDisplay = {
        customerNumber: 12345,
        customerName: 'ACME Corporation',
        addressLine1: '123 Main Street',
        city: 'Springfield',
        state: 'IL',
        zipCode: 62701,
        phoneNumber: '217-555-0100',
        accountBalance: 1500.50,
      };

      expect(validData.customerNumber).toBe(12345);
      expect(validData.customerName).toBe('ACME Corporation');
      expect(validData.accountBalance).toBe(1500.50);
    });

    it('should allow null for all fields except customerNumber', () => {
      const minimalData: CustomerDetailDisplay = {
        customerNumber: 12345,
        customerName: null,
        addressLine1: null,
        city: null,
        state: null,
        zipCode: null,
        phoneNumber: null,
        accountBalance: null,
      };

      expect(minimalData.customerNumber).toBe(12345);
      expect(minimalData.customerName).toBeNull();
    });

    it('should validate with type guard for valid data', () => {
      const validData = {
        customerNumber: 12345,
        customerName: 'Test Customer',
        addressLine1: '123 Main St',
        city: 'Springfield',
        state: 'IL',
        zipCode: 62701,
        phoneNumber: '217-555-0100',
        accountBalance: 1500.50,
      };

      expect(isCustomerDetailDisplay(validData)).toBe(true);
    });

    it('should validate with type guard for data with nulls', () => {
      const dataWithNulls = {
        customerNumber: 12345,
        customerName: null,
        addressLine1: null,
        city: null,
        state: null,
        zipCode: null,
        phoneNumber: null,
        accountBalance: null,
      };

      expect(isCustomerDetailDisplay(dataWithNulls)).toBe(true);
    });

    it('should reject invalid data with type guard', () => {
      const invalidData = {
        customerNumber: '12345', // string instead of number
        customerName: 'Test',
      };

      expect(isCustomerDetailDisplay(invalidData)).toBe(false);
    });

    it('should reject missing required field', () => {
      const missingRequired = {
        customerName: 'Test Customer',
      };

      expect(isCustomerDetailDisplay(missingRequired)).toBe(false);
    });
  });

  describe('Type Structure Validation', () => {
    it('should have all required PROMPT format fields', () => {
      const promptData: CustomerPromptFormData = {
        customerNumber: 12345,
        errorMessage: 'Test error',
      };

      // PROMPT format has 2 fields: PCUSTNO, PMSG
      expect(promptData).toHaveProperty('customerNumber');
      expect(promptData).toHaveProperty('errorMessage');
      expect(typeof promptData.customerNumber).toBe('number');
      expect(typeof promptData.errorMessage).toBe('string');
    });

    it('should have all required DETAIL format fields', () => {
      const detailData: CustomerDetailDisplay = {
        customerNumber: 12345,
        customerName: 'Test',
        addressLine1: 'Address',
        city: 'City',
        state: 'ST',
        zipCode: 12345,
        phoneNumber: '555-1234',
        accountBalance: 100.00,
      };

      // DETAIL format has 8 display fields from CUSTDSP.dds
      expect(detailData).toHaveProperty('customerNumber');
      expect(detailData).toHaveProperty('customerName');
      expect(detailData).toHaveProperty('addressLine1');
      expect(detailData).toHaveProperty('city');
      expect(detailData).toHaveProperty('state');
      expect(detailData).toHaveProperty('zipCode');
      expect(detailData).toHaveProperty('phoneNumber');
      expect(detailData).toHaveProperty('accountBalance');
    });
  });

  describe('Zod Schema Validation', () => {
    describe('CustomerPromptFormDataSchema', () => {
      it('should validate correct prompt data', () => {
        const validData = {
          customerNumber: 12345,
          errorMessage: 'Test error',
        };

        const result = CustomerPromptFormDataSchema.safeParse(validData);
        expect(result.success).toBe(true);
      });

      it('should reject invalid customer number type', () => {
        const invalidData = {
          customerNumber: '12345', // string instead of number
        };

        const result = CustomerPromptFormDataSchema.safeParse(invalidData);
        expect(result.success).toBe(false);
      });

      it('should reject negative customer number', () => {
        const invalidData = {
          customerNumber: -12345,
        };

        const result = CustomerPromptFormDataSchema.safeParse(invalidData);
        expect(result.success).toBe(false);
      });

      it('should allow missing optional errorMessage', () => {
        const validData = {
          customerNumber: 12345,
        };

        const result = CustomerPromptFormDataSchema.safeParse(validData);
        expect(result.success).toBe(true);
      });
    });

    describe('CustomerDetailDisplaySchema', () => {
      it('should validate complete customer data', () => {
        const validData = {
          customerNumber: 12345,
          customerName: 'ACME Corporation',
          addressLine1: '123 Main Street',
          city: 'Springfield',
          state: 'IL',
          zipCode: 62701,
          phoneNumber: '217-555-0100',
          accountBalance: 1500.50,
        };

        const result = CustomerDetailDisplaySchema.safeParse(validData);
        expect(result.success).toBe(true);
      });

      it('should validate data with nullable fields as null', () => {
        const validData = {
          customerNumber: 12345,
          customerName: null,
          addressLine1: null,
          city: null,
          state: null,
          zipCode: null,
          phoneNumber: null,
          accountBalance: null,
        };

        const result = CustomerDetailDisplaySchema.safeParse(validData);
        expect(result.success).toBe(true);
      });

      it('should reject invalid state length', () => {
        const invalidData = {
          customerNumber: 12345,
          customerName: 'Test',
          addressLine1: 'Address',
          city: 'City',
          state: 'Illinois', // Too long - should be 2 chars
          zipCode: 12345,
          phoneNumber: '555-1234',
          accountBalance: 100.00,
        };

        const result = CustomerDetailDisplaySchema.safeParse(invalidData);
        expect(result.success).toBe(false);
      });

      it('should reject missing required customerNumber', () => {
        const invalidData = {
          customerName: 'Test',
        };

        const result = CustomerDetailDisplaySchema.safeParse(invalidData);
        expect(result.success).toBe(false);
      });
    });
  });

  describe('API to Display Mapper', () => {
    it('should correctly map API Customer to CustomerDetailDisplay', () => {
      const apiCustomer = {
        customerId: 12345,
        customerName: 'ACME Corporation',
        addressLine1: '123 Main Street',
        city: 'Springfield',
        state: 'IL',
        zipCode: 62701,
        phoneNumber: '217-555-0100',
        accountBalance: 1500.50,
      };

      const displayData = apiCustomerToDisplay(apiCustomer);

      expect(displayData.customerNumber).toBe(12345);
      expect(displayData.customerName).toBe('ACME Corporation');
      expect(displayData.addressLine1).toBe('123 Main Street');
      expect(displayData.city).toBe('Springfield');
      expect(displayData.state).toBe('IL');
      expect(displayData.zipCode).toBe(62701);
      expect(displayData.phoneNumber).toBe('217-555-0100');
      expect(displayData.accountBalance).toBe(1500.50);
    });

    it('should handle null values correctly', () => {
      const apiCustomer = {
        customerId: 12345,
        customerName: null,
        addressLine1: null,
        city: null,
        state: null,
        zipCode: null,
        phoneNumber: null,
        accountBalance: null,
      };

      const displayData = apiCustomerToDisplay(apiCustomer);

      expect(displayData.customerNumber).toBe(12345);
      expect(displayData.customerName).toBeNull();
      expect(displayData.addressLine1).toBeNull();
      expect(displayData.city).toBeNull();
      expect(displayData.state).toBeNull();
      expect(displayData.zipCode).toBeNull();
      expect(displayData.phoneNumber).toBeNull();
      expect(displayData.accountBalance).toBeNull();
    });
  });
});
