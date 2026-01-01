/**
 * E2E Test Helper Functions
 *
 * Reusable helper functions for customer inquiry E2E tests.
 * Follows Page Object Model pattern for maintainability.
 */

import { Page, expect } from '@playwright/test';

/**
 * Customer Inquiry Page Object
 * Encapsulates page interactions for customer inquiry workflow
 */
export class CustomerInquiryPage {
  constructor(private page: Page) {}

  /**
   * Navigate to customer inquiry page
   */
  async goto() {
    await this.page.goto('/customers');
    await this.page.waitForLoadState('networkidle');
  }

  /**
   * Search for customer by customer number
   * @param customerNumber - Customer ID to search for
   */
  async searchCustomer(customerNumber: number | string) {
    const input = this.page.getByLabel(/customer number/i);
    await input.clear();
    await input.fill(customerNumber.toString());
    await this.page.getByRole('button', { name: /search/i }).click();
  }

  /**
   * Search for customer using Enter key instead of button
   * @param customerNumber - Customer ID to search for
   */
  async searchCustomerWithEnter(customerNumber: number | string) {
    const input = this.page.getByLabel(/customer number/i);
    await input.clear();
    await input.fill(customerNumber.toString());
    await input.press('Enter');
  }

  /**
   * Wait for customer details to be displayed
   */
  async waitForCustomerDetails() {
    await expect(
      this.page.getByRole('heading', { name: 'Customer Details' })
    ).toBeVisible({ timeout: 5000 });
  }

  /**
   * Wait for error message to appear
   */
  async waitForErrorMessage() {
    await expect(
      this.page.getByText(/error|not found|invalid|failed/i)
    ).toBeVisible({ timeout: 5000 });
  }

  /**
   * Verify customer detail is displayed with expected value
   * @param fieldName - Name of the field (e.g., "Customer Name")
   * @param expectedValue - Expected value to verify
   */
  async verifyCustomerField(fieldName: string, expectedValue: string | number) {
    await expect(this.page.getByText(fieldName, { exact: false })).toBeVisible();
    await expect(this.page.getByText(expectedValue.toString())).toBeVisible();
  }

  /**
   * Verify all standard customer fields are present
   */
  async verifyAllFieldsPresent() {
    const expectedFields = [
      'Customer Number',
      'Customer Name',
      'Address Line 1',
      'City',
      'State',
      'Zip Code',
      'Phone Number',
      'Account Balance',
    ];

    for (const field of expectedFields) {
      await expect(
        this.page.getByText(field, { exact: false })
      ).toBeVisible();
    }
  }

  /**
   * Verify error message is displayed
   * @param errorPattern - Regex pattern or string to match error
   */
  async verifyErrorMessage(errorPattern: RegExp | string) {
    await expect(this.page.getByText(errorPattern)).toBeVisible();
  }

  /**
   * Verify customer details are NOT displayed
   */
  async verifyNoCustomerDetails() {
    await expect(
      this.page.getByRole('heading', { name: 'Customer Details' })
    ).not.toBeVisible();
  }

  /**
   * Verify search button is in loading state
   */
  async verifySearchButtonLoading() {
    const searchButton = this.page.getByRole('button', { name: /search/i });
    await expect(searchButton).toBeDisabled();
  }

  /**
   * Clear the search form
   */
  async clearSearchForm() {
    const input = this.page.getByLabel(/customer number/i);
    await input.clear();
  }

  /**
   * Get the customer number input element
   */
  getCustomerNumberInput() {
    return this.page.getByLabel(/customer number/i);
  }

  /**
   * Get the search button element
   */
  getSearchButton() {
    return this.page.getByRole('button', { name: /search/i });
  }
}

/**
 * Wait for API response with optional timeout
 * @param page - Playwright page object
 * @param urlPattern - URL pattern to match
 * @param timeout - Optional timeout in milliseconds
 */
export async function waitForApiResponse(
  page: Page,
  urlPattern: string | RegExp,
  timeout = 5000
) {
  return page.waitForResponse(
    (response) =>
      (typeof urlPattern === 'string'
        ? response.url().includes(urlPattern)
        : urlPattern.test(response.url())) && response.status() === 200,
    { timeout }
  );
}

/**
 * Wait for any API error response
 * @param page - Playwright page object
 * @param urlPattern - URL pattern to match
 * @param timeout - Optional timeout in milliseconds
 */
export async function waitForApiError(
  page: Page,
  urlPattern: string | RegExp,
  timeout = 5000
) {
  return page.waitForResponse(
    (response) =>
      (typeof urlPattern === 'string'
        ? response.url().includes(urlPattern)
        : urlPattern.test(response.url())) && response.status() >= 400,
    { timeout }
  );
}

/**
 * Take screenshot with custom name
 * @param page - Playwright page object
 * @param name - Screenshot name
 */
export async function takeScreenshot(page: Page, name: string) {
  await page.screenshot({
    path: `e2e/screenshots/${name}.png`,
    fullPage: true,
  });
}
