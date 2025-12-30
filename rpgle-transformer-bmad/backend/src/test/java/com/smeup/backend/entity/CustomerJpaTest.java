package com.smeup.backend.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Additional JPA-focused tests for Customer entity
 * 
 * Tests equals/hashCode, BigDecimal precision, and entity behavior
 * Complements CustomerTest.java with JPA-specific validations
 */
class CustomerJpaTest {

    @Test
    void testCustomerEquality() {
        // Given
        Customer customer1 = new Customer();
        customer1.setCustomerId(100L);
        customer1.setCustomerName("Test Customer");

        Customer customer2 = new Customer();
        customer2.setCustomerId(100L);
        customer2.setCustomerName("Different Name");

        Customer customer3 = new Customer();
        customer3.setCustomerId(200L);
        customer3.setCustomerName("Test Customer");

        // Then
        assertEquals(customer1, customer2, "Customers with same ID should be equal");
        assertNotEquals(customer1, customer3, "Customers with different IDs should not be equal");
        assertEquals(customer1.hashCode(), customer2.hashCode(),
                "Customers with same ID should have same hashCode");
    }

    @Test
    void testEqualsWithNull() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        // Then
        assertNotEquals(null, customer, "Customer should not equal null");
    }

    @Test
    void testEqualsWithSameInstance() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        // Then
        assertEquals(customer, customer, "Customer should equal itself");
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        String notACustomer = "Not a customer";

        // Then
        assertNotEquals(customer, notACustomer, "Customer should not equal different class");
    }

    @Test
    void testHashCodeConsistency() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(42L);
        int firstHashCode = customer.hashCode();

        // When - modify non-ID fields
        customer.setCustomerName("Changed Name");
        customer.setCity("Changed City");
        int secondHashCode = customer.hashCode();

        // Then
        assertEquals(firstHashCode, secondHashCode,
                "hashCode should remain consistent when non-ID fields change");
    }

    @Test
    void testBigDecimalPrecisionHandling() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCustomerName("Precision Test");

        // Test values within precision (9 digits total, 2 decimal places = 7 integer
        // digits)
        BigDecimal maxBalance = new BigDecimal("9999999.99"); // Max valid value
        BigDecimal typicalBalance = new BigDecimal("1234567.89");

        // When
        customer.setAccountBalance(maxBalance);
        customer.setCreditLimit(typicalBalance);

        // Then
        assertEquals(maxBalance, customer.getAccountBalance());
        assertEquals(typicalBalance, customer.getCreditLimit());
        assertEquals(2, customer.getAccountBalance().scale(),
                "Balance should maintain 2 decimal places");
        assertEquals(2, customer.getCreditLimit().scale(),
                "Credit limit should maintain 2 decimal places");
    }

    @Test
    void testBigDecimalScalePreservation() {
        // Given
        Customer customer = new Customer();
        BigDecimal valueWithScale = new BigDecimal("100.50");

        // When
        customer.setAccountBalance(valueWithScale);

        // Then
        assertEquals(valueWithScale, customer.getAccountBalance());
        assertEquals(2, customer.getAccountBalance().scale());
    }

    @Test
    void testToStringIncludesAllFields() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(999L);
        customer.setCustomerName("Full Test");
        customer.setAddressLine1("123 Test St");
        customer.setCity("TestCity");
        customer.setState("TS");
        customer.setZipCode(12345);
        customer.setPhoneNumber("555-1234");
        customer.setAccountBalance(new BigDecimal("500.00"));
        customer.setCreditLimit(new BigDecimal("2000.00"));
        customer.setLastOrderDate(20251230);

        // When
        String result = customer.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("999"), "Should contain customer ID");
        assertTrue(result.contains("Full Test"), "Should contain customer name");
        assertTrue(result.contains("123 Test St"), "Should contain address");
        assertTrue(result.contains("TestCity"), "Should contain city");
        assertTrue(result.contains("TS"), "Should contain state");
        assertTrue(result.contains("12345"), "Should contain ZIP");
        assertTrue(result.contains("555-1234"), "Should contain phone");
        assertTrue(result.contains("500.00"), "Should contain balance");
        assertTrue(result.contains("2000.00"), "Should contain credit limit");
        assertTrue(result.contains("20251230"), "Should contain last order date");
    }

    @Test
    void testSerializableInterface() {
        // Given
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        // Then
        assertTrue(customer instanceof java.io.Serializable,
                "Customer should implement Serializable for JPA");
    }
}
