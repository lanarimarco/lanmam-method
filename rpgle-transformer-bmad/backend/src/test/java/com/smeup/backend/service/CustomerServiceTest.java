package com.smeup.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smeup.backend.entity.Customer;
import com.smeup.backend.exception.CustomerNotFoundException;
import com.smeup.backend.exception.InvalidCustomerIdException;
import com.smeup.backend.repository.CustomerRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("findCustomerById should return customer when found (RPGLE: CHAIN %FOUND)")
    void shouldReturnCustomerWhenFound() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(12345L);
        customer.setCustomerName("Test Customer");
        when(customerRepository.findByCustomerId(12345L)).thenReturn(Optional.of(customer));

        // When
        Customer result = customerService.findCustomerById(12345L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("Test Customer");
        verify(customerRepository).findByCustomerId(12345L);
    }

    @Test
    @DisplayName("findCustomerById should throw CustomerNotFoundException when not found (RPGLE: CHAIN NOT %FOUND)")
    void shouldThrowWhenNotFound() {
        // Given - RPGLE: CHAIN returns %FOUND = *OFF
        when(customerRepository.findByCustomerId(99999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> customerService.findCustomerById(99999L))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found with ID: 99999");
        verify(customerRepository).findByCustomerId(99999L);
    }

    @Test
    @DisplayName("findCustomerById should throw InvalidCustomerIdException when ID is null (RPGLE: CUSTNO IFEQ *ZEROS)")
    void shouldThrowWhenIdIsNull() {
        // RPGLE: CUSTNO IFEQ *ZEROS check
        assertThatThrownBy(() -> customerService.findCustomerById(null))
                .isInstanceOf(InvalidCustomerIdException.class)
                .hasMessageContaining("Customer ID must be a positive number");
    }

    @Test
    @DisplayName("findCustomerById should throw InvalidCustomerIdException when ID is zero or negative (RPGLE: CUSTNO IFEQ *ZEROS)")
    void shouldThrowWhenIdIsZeroOrNegative() {
        // RPGLE: CUSTNO IFEQ *ZEROS check
        assertThatThrownBy(() -> customerService.findCustomerById(0L))
                .isInstanceOf(InvalidCustomerIdException.class)
                .hasMessageContaining("Customer ID must be a positive number");

        assertThatThrownBy(() -> customerService.findCustomerById(-1L))
                .isInstanceOf(InvalidCustomerIdException.class)
                .hasMessageContaining("Customer ID must be a positive number");
    }
}
