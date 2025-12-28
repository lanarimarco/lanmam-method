package com.lanarimarco.modernization.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * JPA Entity for Customer Master File (CUSTMAST)
 *
 * This entity represents the customer master data including contact information
 * and account details.
 *
 * Source: CUSTMAST.dds physical file
 * Program: CUST001 - Customer Inquiry
 */
@Entity
@Table(name = "CUSTMAST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer Number - Primary Key
     * Source: CUSTNO (5P 0)
     */
    @Id
    @Column(name = "CUSTNO", precision = 5, scale = 0, nullable = false)
    @EqualsAndHashCode.Include
    private Integer customerNumber;

    /**
     * Customer Name
     * Source: CUSTNAME (30A)
     */
    @Column(name = "CUSTNAME", length = 30)
    private String customerName;

    /**
     * Address Line 1
     * Source: ADDR1 (30A)
     */
    @Column(name = "ADDR1", length = 30)
    private String address1;

    /**
     * City
     * Source: CITY (20A)
     */
    @Column(name = "CITY", length = 20)
    private String city;

    /**
     * State Code
     * Source: STATE (2A)
     */
    @Column(name = "STATE", length = 2)
    private String state;

    /**
     * Zip Code
     * Source: ZIP (5P 0)
     */
    @Column(name = "ZIP", precision = 5, scale = 0)
    private Integer zipCode;

    /**
     * Phone Number
     * Source: PHONE (12A)
     */
    @Column(name = "PHONE", length = 12)
    private String phoneNumber;

    /**
     * Account Balance
     * Source: BALANCE (9P 2)
     */
    @Column(name = "BALANCE", precision = 9, scale = 2)
    private BigDecimal balance;

    /**
     * Credit Limit
     * Source: CREDITLIM (9P 2)
     * Note: Not used by CUST001 program but included for database completeness
     */
    @Column(name = "CREDITLIM", precision = 9, scale = 2)
    private BigDecimal creditLimit;

    /**
     * Last Order Date in YYYYMMDD format
     * Source: LASTORDER (8P 0)
     * Note: Not used by CUST001 program but included for database completeness
     * Stored as numeric format YYYYMMDD (e.g., 20251228)
     */
    @Column(name = "LASTORDER", precision = 8, scale = 0)
    private Integer lastOrderDate;
}
