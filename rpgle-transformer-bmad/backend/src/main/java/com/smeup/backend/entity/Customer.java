package com.smeup.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Customer entity mapped from DDS Physical File CUSTMAST.dds
 *
 * <p>Original DDS File: source-rpgle/dds/physical-files/CUSTMAST.dds
 * <p>Table: CUSTMAST
 * <p>Record Format: CUSTREC
 *
 * <p>This entity preserves the original AS/400 data structure while providing
 * a modern Java interface for the application.
 *
 * <p>DDS Field Mapping:
 * <ul>
 *   <li>CUSTNO (5P 0) → customerId (Long)</li>
 *   <li>CUSTNAME (30A) → customerName (String)</li>
 *   <li>ADDR1 (30A) → addressLine1 (String)</li>
 *   <li>CITY (20A) → city (String)</li>
 *   <li>STATE (2A) → state (String)</li>
 *   <li>ZIP (5P 0) → zipCode (Integer)</li>
 *   <li>PHONE (12A) → phoneNumber (String)</li>
 *   <li>BALANCE (9P 2) → accountBalance (BigDecimal)</li>
 *   <li>CREDITLIM (9P 2) → creditLimit (BigDecimal)</li>
 *   <li>LASTORDER (8P 0) → lastOrderDate (Integer, YYYYMMDD format)</li>
 * </ul>
 */
@Entity
@Table(name = "CUSTMAST") // DDS Physical File name
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer Number - Primary Key
     * <p>DDS Field: CUSTNO (5P 0)
     * <p>Note: No @GeneratedValue - IDs are manually assigned to preserve
     * original AS/400 customer numbers during migration.
     */
    @Id
    @Column(name = "CUSTNO", nullable = false)
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    /**
     * Customer Name
     * <p>DDS Field: CUSTNAME (30A)
     */
    @Column(name = "CUSTNAME", length = 30, nullable = false)
    @NotBlank(message = "Customer name is required")
    @Size(max = 30, message = "Customer name must not exceed 30 characters")
    private String customerName;

    /**
     * Address Line 1
     * <p>DDS Field: ADDR1 (30A)
     */
    @Column(name = "ADDR1", length = 30)
    private String addressLine1;

    /**
     * City
     * <p>DDS Field: CITY (20A)
     */
    @Column(name = "CITY", length = 20)
    private String city;

    /**
     * State Code
     * <p>DDS Field: STATE (2A)
     * <p>Validation: Must be 2 uppercase letters (e.g., "IL", "CA")
     */
    @Column(name = "STATE", length = 2)
    @Size(min = 2, max = 2, message = "State code must be exactly 2 characters")
    @Pattern(regexp = "[A-Z]{2}", message = "State code must be 2 uppercase letters")
    private String state;

    /**
     * Zip Code
     * <p>DDS Field: ZIP (5P 0)
     */
    @Column(name = "ZIP")
    @Min(value = 0, message = "ZIP code must be positive")
    @Max(value = 99999, message = "ZIP code must be 5 digits or less")
    private Integer zipCode;

    /**
     * Phone Number
     * <p>DDS Field: PHONE (12A)
     */
    @Column(name = "PHONE", length = 12)
    @Size(max = 12, message = "Phone number must not exceed 12 characters")
    private String phoneNumber;

    /**
     * Account Balance
     * <p>DDS Field: BALANCE (9P 2) - Packed Decimal with 2 decimal places
     * <p>Java Type: BigDecimal - Exact decimal arithmetic for financial data
     */
    @Column(name = "BALANCE", precision = 9, scale = 2)
    private BigDecimal accountBalance;

    /**
     * Credit Limit
     * <p>DDS Field: CREDITLIM (9P 2) - Packed Decimal with 2 decimal places
     * <p>Java Type: BigDecimal - Exact decimal arithmetic for financial data
     */
    @Column(name = "CREDITLIM", precision = 9, scale = 2)
    private BigDecimal creditLimit;

    /**
     * Last Order Date (YYYYMMDD format)
     * <p>DDS Field: LASTORDER (8P 0)
     * <p>Format: Integer in YYYYMMDD format (e.g., 20251228)
     * <p>Note: Preserves original AS/400 date format for compatibility
     */
    @Column(name = "LASTORDER")
    private Integer lastOrderDate;

    // Default constructor required by JPA
    public Customer() {
    }

    // Getters and Setters

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Integer getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(Integer lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    @Override
    public String toString() {
        return "Customer{"
                + "customerId=" + customerId
                + ", customerName='" + customerName + '\''
                + ", addressLine1='" + addressLine1 + '\''
                + ", city='" + city + '\''
                + ", state='" + state + '\''
                + ", zipCode=" + zipCode
                + ", phoneNumber='" + phoneNumber + '\''
                + ", accountBalance=" + accountBalance
                + ", creditLimit=" + creditLimit
                + ", lastOrderDate=" + lastOrderDate
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
