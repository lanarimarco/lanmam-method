package com.smeup.backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/**
 * Unit tests for GlobalExceptionHandler.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleCustomerNotFoundException() {
        CustomerNotFoundException ex = new CustomerNotFoundException(123L);
        ResponseEntity<ProblemDetail> response = handler.handleCustomerNotFoundException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Customer not found with ID: 123", response.getBody().getDetail());
    }

    @Test
    void shouldHandleInvalidCustomerIdException() {
        InvalidCustomerIdException ex = new InvalidCustomerIdException("Invalid ID");
        ResponseEntity<ProblemDetail> response = handler.handleInvalidCustomerIdException(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody().getDetail());
    }
}
