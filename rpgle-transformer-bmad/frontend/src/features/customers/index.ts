/**
 * Customer feature module exports.
 *
 * Barrel file for convenient imports from the customers feature.
 * Re-exports all types, schemas, and utilities.
 *
 * @example
 * ```typescript
 * // Instead of:
 * import { CustomerPromptFormData } from './features/customers/customer.types';
 *
 * // Use:
 * import { CustomerPromptFormData } from './features/customers';
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
