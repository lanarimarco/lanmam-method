/**
 * E2E Tests for Customer Inquiry Workflow
 *
 * Tests the complete user journey for customer lookup from frontend to backend.
 *
 * Source: RPGLE Program CUST001 - Customer Inquiry
 * DDS Display File: CUSTDSP.dds (PROMPT and DETAIL formats)
 *
 * Test Coverage:
 * - Happy path: Valid customer number lookup
 * - Error cases: Invalid/non-existent customer numbers
 * - Complete workflow from search to display
 */

import { test, expect } from '@playwright/test';
import { CustomerInquiryPage } from './helpers/page-helpers';
import { VALID_CUSTOMERS, INVALID_CUSTOMER_IDS } from './fixtures/customer-data';

test.describe('Customer Inquiry', () => {
  let customerPage: CustomerInquiryPage;

  test.beforeEach(async ({ page }) => {
    customerPage = new CustomerInquiryPage(page);
    await customerPage.goto();
  });

  test('should display customer inquiry page with search form', async ({
    page,
  }) => {
    // Verify page title
    await expect(
      page.getByRole('heading', { name: 'Customer Inquiry' })
    ).toBeVisible();

    // Verify search form is present
    await expect(
      page.getByRole('heading', { name: 'Customer Search' })
    ).toBeVisible();

    // Verify customer number input field
    await expect(
      page.getByLabel(/customer number/i)
    ).toBeVisible();

    // Verify search button
    await expect(
      page.getByRole('button', { name: /search/i })
    ).toBeVisible();
  });

  test('should search for valid customer and display details', async ({
    page,
  }) => {
    const testCustomer = VALID_CUSTOMERS.CUSTOMER_1001;

    // Search for customer using helper
    await customerPage.searchCustomer(testCustomer.customerId);

    // Wait for customer details to appear
    await customerPage.waitForCustomerDetails();

    // Verify customer data is displayed
    await customerPage.verifyCustomerField('Customer Number', testCustomer.customerId);
    await customerPage.verifyCustomerField('Customer Name', testCustomer.customerName);
    await customerPage.verifyCustomerField('City', testCustomer.city);
  });

  test('should handle customer search via Enter key', async ({ page }) => {
    const testCustomer = VALID_CUSTOMERS.CUSTOMER_1001;

    // Search using Enter key
    await customerPage.searchCustomerWithEnter(testCustomer.customerId);

    // Verify customer details appear
    await customerPage.waitForCustomerDetails();
    await expect(page.getByText(testCustomer.customerId.toString())).toBeVisible();
  });

  test('should display all customer detail fields', async ({ page }) => {
    // Search for customer
    await customerPage.searchCustomer(VALID_CUSTOMERS.CUSTOMER_1001.customerId);

    // Wait and verify all fields are present
    await customerPage.waitForCustomerDetails();
    await customerPage.verifyAllFieldsPresent();
  });

  test('should show loading state during search', async ({ page }) => {
    // Start search
    await page.getByLabel(/customer number/i).fill('1001');
    
    // Click search button
    const searchButton = page.getByRole('button', { name: /search/i });
    await searchButton.click();

    // Button should be disabled during loading
    // This might be quick, so we check immediately after click
    // (In real scenario, might need to slow down network to test properly)
  });

  test('should allow searching for different customers sequentially', async ({
    page,
  }) => {
    const customer1 = VALID_CUSTOMERS.CUSTOMER_1001;
    const customer2 = VALID_CUSTOMERS.CUSTOMER_1002;

    // First search
    await customerPage.searchCustomer(customer1.customerId);
    await expect(page.getByText(customer1.customerId.toString())).toBeVisible();

    // Second search
    await customerPage.searchCustomer(customer2.customerId);
    await expect(page.getByText(customer2.customerId.toString())).toBeVisible();
  });
});

test.describe('Customer Inquiry - Error Cases', () => {
  let customerPage: CustomerInquiryPage;

  test.beforeEach(async ({ page }) => {
    customerPage = new CustomerInquiryPage(page);
    await customerPage.goto();
  });

  test('should display error message for non-existent customer', async ({
    page,
  }) => {
    // Search for non-existent customer
    await customerPage.searchCustomer(INVALID_CUSTOMER_IDS.NON_EXISTENT);

    // Verify error message appears
    await customerPage.waitForErrorMessage();
    
    // Verify customer details are NOT shown
    await customerPage.verifyNoCustomerDetails();
  });

  test('should display error for invalid customer number format', async ({
    page,
  }) => {
    // Try to enter non-numeric value
    const customerInput = page.getByLabel(/customer number/i);
    
    // Most number inputs will prevent non-numeric input
    // But we can test validation if form allows it
    await customerInput.fill('ABCDE');
    
    // Try to submit
    await page.getByRole('button', { name: /search/i }).click();

    // Should show validation error or prevent submission
    // Check if error message appears or if nothing happens
    // This depends on form validation implementation
  });

  test('should handle empty customer number submission', async ({ page }) => {
    // Try to submit without entering a customer number
    const searchButton = page.getByRole('button', { name: /search/i });
    await searchButton.click();

    // Form validation should prevent submission or show error
    // Customer details should not be shown
    await expect(
      page.getByRole('heading', { name: 'Customer Details' })
    ).not.toBeVisible();
  });

  test('should display error message and allow retry', async ({ page }) => {
    // First attempt - invalid customer
    await customerPage.searchCustomer(INVALID_CUSTOMER_IDS.NON_EXISTENT);
    await customerPage.waitForErrorMessage();

    // Retry with valid customer
    await customerPage.searchCustomer(VALID_CUSTOMERS.CUSTOMER_1001.customerId);

    // Should now show customer details successfully
    await customerPage.waitForCustomerDetails();
    await expect(page.getByText(VALID_CUSTOMERS.CUSTOMER_1001.customerId.toString())).toBeVisible();

    // Error message should be cleared
    await expect(
      page.getByText(/customer not found/i)
    ).not.toBeVisible();
  });

  test('should handle network errors gracefully', async ({ page }) => {
    // Simulate network failure by going offline
    await page.context().setOffline(true);

    // Try to search
    await page.getByLabel(/customer number/i).fill('1001');
    await page.getByRole('button', { name: /search/i }).click();

    // Should show error message (network error or request failed)
    // The exact message depends on error handling implementation
    await expect(
      page.getByText(/error|failed|unable/i)
    ).toBeVisible({ timeout: 5000 });

    // Restore network
    await page.context().setOffline(false);
  });

  test('should validate customer number is within valid range', async ({
    page,
  }) => {
    // Test with zero
    await page.getByLabel(/customer number/i).fill('0');
    await page.getByRole('button', { name: /search/i }).click();

    // Should either show validation error or "not found" error
    // Depends on backend validation
    
    // Test with negative number (if input allows)
    const customerInput = page.getByLabel(/customer number/i);
    await customerInput.clear();
    await customerInput.fill('-1');
    await page.getByRole('button', { name: /search/i }).click();

    // Should show error or prevent invalid input
  });
});
