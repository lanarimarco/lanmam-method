package com.smeup.backend.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Customer.
 * Represents the API view of the Customer entity.
 */
public record CustomerDTO(
        Long customerId,
        String customerName,
        String addressLine1,
        String city,
        String state,
        Integer zipCode,
        String phoneNumber,
        BigDecimal accountBalance,
        BigDecimal creditLimit,
        Integer lastOrderDate) {
}
