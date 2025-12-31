package com.smeup.backend.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Customer.
 * Represents the API view of the Customer entity.
 */
public record CustomerDTO(
        /**
         * Customer Number.
         * DDS Field: CUSTNO (5P 0)
         */
        Long customerId,

        /**
         * Customer Name.
         * DDS Field: CUSTNAME (30A)
         */
        String customerName,

        /**
         * Address Line 1.
         * DDS Field: ADDR1 (30A)
         */
        String addressLine1,

        /**
         * City.
         * DDS Field: CITY (20A)
         */
        String city,

        /**
         * State Code.
         * DDS Field: STATE (2A)
         */
        String state,

        /**
         * Zip Code.
         * DDS Field: ZIP (5P 0)
         */
        Integer zipCode,

        /**
         * Phone Number.
         * DDS Field: PHONE (12A)
         */
        String phoneNumber,

        /**
         * Account Balance.
         * DDS Field: BALANCE (9P 2)
         */
        BigDecimal accountBalance,

        /**
         * Credit Limit.
         * DDS Field: CREDITLIM (9P 2)
         */
        BigDecimal creditLimit,

        /**
         * Last Order Date (YYYYMMDD format).
         * DDS Field: LASTORDER (8P 0)
         */
        Integer lastOrderDate) {
}
