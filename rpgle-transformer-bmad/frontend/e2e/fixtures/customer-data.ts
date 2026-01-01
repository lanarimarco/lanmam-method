/**
 * Test fixture data for E2E customer inquiry tests
 *
 * Contains sample customer data used in E2E tests.
 * These customers should exist in the test database.
 *
 * Source: Derived from CUSTMAST DDS Physical File
 * Original: source-rpgle/dds/physical-files/CUSTMAST.dds
 */

export interface CustomerTestData {
  customerId: number;
  customerName: string;
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  zipCode: number;
  phoneNumber: string;
  accountBalance: number;
}

/**
 * Valid test customers that should exist in test database
 */
export const VALID_CUSTOMERS: Record<string, CustomerTestData> = {
  CUSTOMER_1001: {
    customerId: 1001,
    customerName: 'ACME Corporation',
    addressLine1: '123 Main Street',
    addressLine2: 'Suite 100',
    city: 'Springfield',
    state: 'IL',
    zipCode: 62701,
    phoneNumber: '217-555-0100',
    accountBalance: 1500.50,
  },
  CUSTOMER_1002: {
    customerId: 1002,
    customerName: 'Global Industries Inc',
    addressLine1: '456 Oak Avenue',
    city: 'Chicago',
    state: 'IL',
    zipCode: 60601,
    phoneNumber: '312-555-0200',
    accountBalance: 2750.00,
  },
  CUSTOMER_1003: {
    customerId: 1003,
    customerName: 'Tech Solutions LLC',
    addressLine1: '789 Elm Boulevard',
    city: 'Naperville',
    state: 'IL',
    zipCode: 60540,
    phoneNumber: '630-555-0300',
    accountBalance: 500.25,
  },
};

/**
 * Invalid customer numbers for error testing
 */
export const INVALID_CUSTOMER_IDS = {
  NON_EXISTENT: 99999,
  ZERO: 0,
  NEGATIVE: -1,
  OUT_OF_RANGE: 100000,
};

/**
 * Test scenarios with expected outcomes
 */
export const TEST_SCENARIOS = {
  HAPPY_PATH: {
    customerId: VALID_CUSTOMERS.CUSTOMER_1001.customerId,
    expectedName: VALID_CUSTOMERS.CUSTOMER_1001.customerName,
    expectedCity: VALID_CUSTOMERS.CUSTOMER_1001.city,
  },
  NOT_FOUND: {
    customerId: INVALID_CUSTOMER_IDS.NON_EXISTENT,
    expectedError: /customer not found/i,
  },
  VALIDATION_ERROR: {
    invalidInput: 'ABCDE',
    expectedError: /invalid|required/i,
  },
};
