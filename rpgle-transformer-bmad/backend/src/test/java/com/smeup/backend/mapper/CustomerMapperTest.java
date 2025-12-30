package com.smeup.backend.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.entity.Customer;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void shouldMapEntityToDTO() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(12345L);
        customer.setCustomerName("Acme Corp");
        customer.setAddressLine1("123 Main St");
        customer.setCity("Metropolis");
        customer.setState("NY");
        customer.setZipCode(10001);
        customer.setPhoneNumber("555-1234");
        customer.setAccountBalance(new BigDecimal("1000.00"));
        customer.setCreditLimit(new BigDecimal("5000.00"));
        customer.setLastOrderDate(20230101);

        // When
        CustomerDTO dto = customerMapper.toDTO(customer);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.customerId()).isEqualTo(12345L);
        assertThat(dto.customerName()).isEqualTo("Acme Corp");
        assertThat(dto.addressLine1()).isEqualTo("123 Main St");
        assertThat(dto.city()).isEqualTo("Metropolis");
        assertThat(dto.state()).isEqualTo("NY");
        assertThat(dto.zipCode()).isEqualTo(10001);
        assertThat(dto.phoneNumber()).isEqualTo("555-1234");
        assertThat(dto.accountBalance()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(dto.creditLimit()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(dto.lastOrderDate()).isEqualTo(20230101);
    }

    @Test
    void shouldMapDTOToEntity() {
        // Given
        CustomerDTO dto = new CustomerDTO(
                12345L,
                "Acme Corp",
                "123 Main St",
                "Metropolis",
                "NY",
                10001,
                "555-1234",
                new BigDecimal("1000.00"),
                new BigDecimal("5000.00"),
                20230101);

        // When
        Customer customer = customerMapper.toEntity(dto);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerId()).isEqualTo(12345L);
        assertThat(customer.getCustomerName()).isEqualTo("Acme Corp");
        assertThat(customer.getAddressLine1()).isEqualTo("123 Main St");
        assertThat(customer.getCity()).isEqualTo("Metropolis");
        assertThat(customer.getState()).isEqualTo("NY");
        assertThat(customer.getZipCode()).isEqualTo(10001);
        assertThat(customer.getPhoneNumber()).isEqualTo("555-1234");
        assertThat(customer.getAccountBalance()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(customer.getCreditLimit()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(customer.getLastOrderDate()).isEqualTo(20230101);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertThat(customerMapper.toDTO(null)).isNull();
        assertThat(customerMapper.toEntity(null)).isNull();
    }
}
