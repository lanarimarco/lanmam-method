package com.smeup.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.smeup.backend.AbstractIntegrationTest;
import com.smeup.backend.dto.ApiResponse;
import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.entity.Customer;
import com.smeup.backend.repository.CustomerRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Integration Test for CustomerController.
 * Uses standard RestTemplate to verify the full stack.
 */
class CustomerControllerTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        restTemplate = new RestTemplate();
        // Configure RestTemplate to NOT throw exceptions on 4xx/5xx
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/customers";
    }

    @Test
    void shouldReturnCustomerWhenIdIsValidAndExists() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Test Customer");
        customer.setAccountBalance(new BigDecimal("100.00"));
        customer.setCreditLimit(new BigDecimal("5000.00"));
        customerRepository.save(customer);

        // When
        ResponseEntity<ApiResponse<CustomerDTO>> response = restTemplate.exchange(
                getBaseUrl() + "/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<CustomerDTO>>() {
                });

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Validate "data" wrapper with strict types
        CustomerDTO dto = response.getBody().getData();
        assertThat(dto).isNotNull();
        assertThat(dto.customerId()).isEqualTo(1L);
        assertThat(dto.customerName()).isEqualTo("Test Customer");

        // Validate "meta" wrapper exists
        assertThat(response.getBody().getMeta()).isNotNull();
    }

    @Test
    void shouldReturn404WhenCustomerDoesNotExist() {
        // When
        ResponseEntity<ProblemDetail> response = restTemplate.getForEntity(
                getBaseUrl() + "/999", ProblemDetail.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Customer Not Found");
        assertThat(response.getBody().getDetail()).isEqualTo("Customer not found with ID: 999");
    }

    @Test
    void shouldReturn400WhenCustomerIdIsInvalid() {
        // When
        ResponseEntity<ProblemDetail> response = restTemplate.getForEntity(
                getBaseUrl() + "/-1", ProblemDetail.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Invalid Customer ID");
        assertThat(response.getBody().getDetail()).isEqualTo("Customer ID must be a positive number");
    }
}
