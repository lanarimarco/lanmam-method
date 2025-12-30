package com.smeup.backend.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Customer entity
 * 
 * Tests entity instantiation, getters/setters, and field mappings
 * Uses H2 in-memory database (no Docker required)
 */
class CustomerTest {

    @Test
    void testCustomerEntityInstantiation() {
        // Given
        Customer customer = new Customer();

        // Then
        assertNotNull(customer, "Customer entity should be instantiable");
    }

    @Test
    void testCustomerIdGetterSetter() {
        // Given
        Customer customer = new Customer();
        Long expectedId = 12345L;

        // When
        customer.setCustomerId(expectedId);

        // Then
        assertEquals(expectedId, customer.getCustomerId(),
                "Customer ID should match the set value");
    }

    @Test
    void testCustomerNameGetterSetter() {
        // Given
        Customer customer = new Customer();
        String expectedName = "John Doe";

        // When
        customer.setCustomerName(expectedName);

        // Then
        assertEquals(expectedName, customer.getCustomerName(),
                "Customer name should match the set value");
    }

    @Test
    void testAddressLine1GetterSetter() {
        // Given
        Customer customer = new Customer();
        String expectedAddress = "123 Main Street";

        // When
        customer.setAddressLine1(expectedAddress);

        // Then
        assertEquals(expectedAddress, customer.getAddressLine1(),
                "Address line 1 should match the set value");
    }

    @Test
    void testCityGetterSetter() {
        // Given
        Customer customer = new Customer();
        String expectedCity = "Springfield";

        // When
        customer.setCity(expectedCity);

        // Then
        assertEquals(expectedCity, customer.getCity(),
                "City should match the set value");
    }

    @Test
    void testStateGetterSetter() {
        // Given
        Customer customer = new Customer();
        String expectedState = "IL";

        // When
        customer.setState(expectedState);

        // Then
        assertEquals(expectedState, customer.getState(),
                "State should match the set value");
    }

    @Test
    void testZipCodeGetterSetter() {
        // Given
        Customer customer = new Customer();
        Integer expectedZip = 62701;

        // When
        customer.setZipCode(expectedZip);

        // Then
        assertEquals(expectedZip, customer.getZipCode(),
                "Zip code should match the set value");
    }

    @Test
    void testPhoneNumberGetterSetter() {
        // Given
        Customer customer = new Customer();
        String expectedPhone = "217-555-1234";

        // When
        customer.setPhoneNumber(expectedPhone);

        // Then
        assertEquals(expectedPhone, customer.getPhoneNumber(),
                "Phone number should match the set value");
    }

    @Test
    void testAccountBalanceGetterSetter() {
        // Given
        Customer customer = new Customer();
        BigDecimal expectedBalance = new BigDecimal("1234.56");

        // When
        customer.setAccountBalance(expectedBalance);

        // Then
        assertEquals(expectedBalance, customer.getAccountBalance(),
                "Account balance should match the set value");
    }

    @Test
    void testCreditLimitGetterSetter() {
        // Given
        Customer customer = new Customer();
        BigDecimal expectedLimit = new BigDecimal("5000.00");

        // When
        customer.setCreditLimit(expectedLimit);

        // Then
        assertEquals(expectedLimit, customer.getCreditLimit(),
                "Credit limit should match the set value");
    }

    @Test
    void testLastOrderDateGetterSetter() {
        // Given
        Customer customer = new Customer();
        Integer expectedDate = 20251230; // YYYYMMDD format

        // When
        customer.setLastOrderDate(expectedDate);

        // Then
        assertEquals(expectedDate, customer.getLastOrderDate(),
                "Last order date should match the set value");
    }

    @Test
    void testCustomerWithAllFields() {
        // Given
        Customer customer = new Customer();

        // When
        customer.setCustomerId(12345L);
        customer.setCustomerName("Jane Smith");
        customer.setAddressLine1("456 Oak Avenue");
        customer.setCity("Chicago");
        customer.setState("IL");
        customer.setZipCode(60601);
        customer.setPhoneNumber("312-555-9876");
        customer.setAccountBalance(new BigDecimal("2500.75"));
        customer.setCreditLimit(new BigDecimal("10000.00"));
        customer.setLastOrderDate(20251215);

        // Then
        assertAll("Customer with all fields",
                () -> assertEquals(12345L, customer.getCustomerId()),
                () -> assertEquals("Jane Smith", customer.getCustomerName()),
                () -> assertEquals("456 Oak Avenue", customer.getAddressLine1()),
                () -> assertEquals("Chicago", customer.getCity()),
                () -> assertEquals("IL", customer.getState()),
                () -> assertEquals(60601, customer.getZipCode()),
                () -> assertEquals("312-555-9876", customer.getPhoneNumber()),
                () -> assertEquals(new BigDecimal("2500.75"), customer.getAccountBalance()),
                () -> assertEquals(new BigDecimal("10000.00"), customer.getCreditLimit()),
                () -> assertEquals(20251215, customer.getLastOrderDate()));
    }

    @Test
    void testToString() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(99999L);
        customer.setCustomerName("Test Customer");
        customer.setCity("TestCity");
        customer.setState("TS");

        // When
        String result = customer.toString();

        // Then
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains("99999"), "toString should contain customer ID");
        assertTrue(result.contains("Test Customer"), "toString should contain customer name");
        assertTrue(result.contains("TestCity"), "toString should contain city");
        assertTrue(result.contains("TS"), "toString should contain state");
    }

    @Test
    void testNullValues() {
        // Given
        Customer customer = new Customer();

        // When - set null values (all fields except ID should allow null)
        customer.setCustomerName(null);
        customer.setAddressLine1(null);
        customer.setCity(null);
        customer.setState(null);
        customer.setZipCode(null);
        customer.setPhoneNumber(null);
        customer.setAccountBalance(null);
        customer.setCreditLimit(null);
        customer.setLastOrderDate(null);

        // Then - verify null values are stored correctly
        assertAll("Null values",
                () -> assertNull(customer.getCustomerName()),
                () -> assertNull(customer.getAddressLine1()),
                () -> assertNull(customer.getCity()),
                () -> assertNull(customer.getState()),
                () -> assertNull(customer.getZipCode()),
                () -> assertNull(customer.getPhoneNumber()),
                () -> assertNull(customer.getAccountBalance()),
                () -> assertNull(customer.getCreditLimit()),
                () -> assertNull(customer.getLastOrderDate()));
    }
}
