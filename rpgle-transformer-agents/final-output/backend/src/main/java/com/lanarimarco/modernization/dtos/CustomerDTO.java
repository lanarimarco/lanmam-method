package com.lanarimarco.modernization.dtos;

import lombok.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Customer Information
 *
 * This DTO is used by the Customer Inquiry program (CUST001) to transfer
 * customer data between the service layer and the presentation layer.
 *
 * Contains only the fields displayed by CUST001 program.
 * Excludes: creditLimit, lastOrderDate (not used by CUST001)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    /**
     * Customer Number
     * Displayed on DETAIL screen
     */
    private Integer customerNumber;

    /**
     * Customer Name
     * Displayed on DETAIL screen
     */
    private String customerName;

    /**
     * Address Line 1
     * Displayed on DETAIL screen
     */
    private String address1;

    /**
     * City
     * Displayed on DETAIL screen
     */
    private String city;

    /**
     * State Code (2 characters)
     * Displayed on DETAIL screen
     */
    private String state;

    /**
     * Zip Code (5 digits)
     * Displayed on DETAIL screen
     */
    private Integer zipCode;

    /**
     * Phone Number
     * Displayed on DETAIL screen
     */
    private String phoneNumber;

    /**
     * Account Balance
     * Displayed on DETAIL screen with currency formatting
     */
    private BigDecimal balance;
}
