/**
 * Base Axios client configuration for API communication.
 *
 * Configured to communicate with Spring Boot backend REST API.
 * Backend: CustomerController.java (GET /api/v1/customers/{customerId})
 *
 * Source: Based on architecture patterns from architecture.md
 */

import axios, { AxiosError, AxiosInstance } from 'axios';

/**
 * Base URL from environment variable (Vite 6)
 * REQUIRED: Must be configured in .env or .env.local
 * Example: VITE_API_BASE_URL=http://localhost:8080/api/v1
 *
 * Per AC#5: "API base URL is configurable via environment variable"
 * No hardcoded fallback allowed per requirements.
 */
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

if (!API_BASE_URL) {
  throw new Error(
    'API base URL is not configured. Set VITE_API_BASE_URL environment variable. ' +
    'Example: VITE_API_BASE_URL=http://localhost:8080/api/v1'
  );
}

/**
 * Axios instance with base configuration
 */
export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 second timeout
});

/**
 * Request interceptor for adding authentication headers if needed in future
 */
apiClient.interceptors.request.use(
  (config) => {
    // Future: Add authentication token here
    // config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

/**
 * Response interceptor for standardized error handling
 * Handles RFC 7807 Problem Details format from backend
 */
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error: AxiosError) => {
    // Log error for debugging
    console.error('API Error:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
    });

    // Transform error to match backend error format if needed
    if (error.response) {
      // Server responded with error status
      const errorData = error.response.data;
      return Promise.reject({
        status: error.response.status,
        message: error.message,
        data: errorData,
      });
    } else if (error.request) {
      // Request made but no response received
      return Promise.reject({
        status: 0,
        message: 'Network error - no response from server',
        data: null,
      });
    } else {
      // Error in request configuration
      return Promise.reject({
        status: 0,
        message: error.message,
        data: null,
      });
    }
  }
);

export default apiClient;
