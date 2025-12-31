/**
 * Customer feature module exports.
 *
 * Barrel file for convenient imports from the customers feature.
 * Re-exports all types, schemas, utilities, and components.
 *
 * @example
 * ```typescript
 * // Instead of:
 * import { CustomerPromptFormData } from './features/customers/customer.types';
 * import { CustomerSearch } from './features/customers/CustomerSearch';
 *
 * // Use:
 * import { CustomerPromptFormData, CustomerSearch } from './features/customers';
 * ```
 */

export type {
  CustomerPromptFormData,
  CustomerDetailDisplay,
} from './customer.types';

export {
  apiCustomerToDisplay,
  isCustomerPromptFormData,
  isCustomerDetailDisplay,
  CustomerPromptFormDataSchema,
  CustomerDetailDisplaySchema,
} from './customer.types';

export { CustomerSearch } from './CustomerSearch';
export type { CustomerSearchProps } from './CustomerSearch';
