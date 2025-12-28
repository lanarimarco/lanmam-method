import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/customers';

const customerService = {
  getCustomerByNumber: async (customerNumber) => {
    try {
      const response = await axios.get(`${API_BASE_URL}/${customerNumber}`);
      return response.data;
    } catch (error) {
      if (error.response) {
        throw new Error(error.response.data.message || 'Customer not found');
      }
      throw new Error('Network error. Please check if the backend server is running.');
    }
  },

  getAllCustomers: async () => {
    try {
      const response = await axios.get(API_BASE_URL);
      return response.data;
    } catch (error) {
      if (error.response) {
        throw new Error(error.response.data.message || 'Failed to fetch customers');
      }
      throw new Error('Network error. Please check if the backend server is running.');
    }
  },

  createCustomer: async (customerData) => {
    try {
      const response = await axios.post(API_BASE_URL, customerData);
      return response.data;
    } catch (error) {
      if (error.response) {
        throw new Error(error.response.data.message || 'Failed to create customer');
      }
      throw new Error('Network error. Please check if the backend server is running.');
    }
  },

  updateCustomer: async (customerNumber, customerData) => {
    try {
      const response = await axios.put(`${API_BASE_URL}/${customerNumber}`, customerData);
      return response.data;
    } catch (error) {
      if (error.response) {
        throw new Error(error.response.data.message || 'Failed to update customer');
      }
      throw new Error('Network error. Please check if the backend server is running.');
    }
  },

  deleteCustomer: async (customerNumber) => {
    try {
      await axios.delete(`${API_BASE_URL}/${customerNumber}`);
    } catch (error) {
      if (error.response) {
        throw new Error(error.response.data.message || 'Failed to delete customer');
      }
      throw new Error('Network error. Please check if the backend server is running.');
    }
  }
};

export default customerService;
