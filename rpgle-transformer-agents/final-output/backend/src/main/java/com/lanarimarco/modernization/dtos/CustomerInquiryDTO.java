package com.lanarimarco.modernization.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Customer Inquiry
 * Converted from RPGLE program: CUST001
 *
 * Maps to DETAIL screen fields in CUSTDSP.dds (lines 33-59)
 *
 * REFACTORING NOTES (R4):
 * - Added Jakarta Bean Validation annotations to all fields
 * - Validation constraints match RPGLE field definitions
 * - Enables declarative validation when used with @Valid annotation
 */
public class CustomerInquiryDTO {

    /**
     * Customer Number - 5 digit numeric
     * RPGLE: DCUSTNO (5Y 0) - line 43 in CUSTDSP.dds
     */
    @NotNull(message = "Customer number is required")
    @DecimalMin(value = "1", message = "Customer number must be positive")
    @DecimalMax(value = "99999", message = "Customer number must be 5 digits or less")
    @Digits(integer = 5, fraction = 0, message = "Customer number must be a 5-digit integer")
    private BigDecimal customerNumber;

    /**
     * Customer Name - 30 characters
     * RPGLE: DCUSTNAME (30A) - line 45 in CUSTDSP.dds
     */
    @NotBlank(message = "Customer name is required")
    @Size(max = 30, message = "Customer name must not exceed 30 characters")
    private String customerName;

    /**
     * Address Line 1 - 30 characters
     * RPGLE: DADDR1 (30A) - line 47 in CUSTDSP.dds
     */
    @Size(max = 30, message = "Address must not exceed 30 characters")
    private String address;

    /**
     * City - 20 characters
     * RPGLE: DCITY (20A) - line 49 in CUSTDSP.dds
     */
    @Size(max = 20, message = "City must not exceed 20 characters")
    private String city;

    /**
     * State - 2 characters
     * RPGLE: DSTATE (2A) - line 51 in CUSTDSP.dds
     */
    @Size(max = 2, message = "State must be 2 characters")
    private String state;

    /**
     * Zip Code - 5 digit numeric
     * RPGLE: DZIP (5Y 0) - line 53 in CUSTDSP.dds
     */
    @DecimalMin(value = "0", message = "Zip code cannot be negative")
    @DecimalMax(value = "99999", message = "Zip code must be 5 digits or less")
    @Digits(integer = 5, fraction = 0, message = "Zip code must be a 5-digit integer")
    private BigDecimal zipCode;

    /**
     * Phone Number - 12 characters
     * RPGLE: DPHONE (12A) - line 55 in CUSTDSP.dds
     */
    @Size(max = 12, message = "Phone must not exceed 12 characters")
    private String phone;

    /**
     * Account Balance - 9 digits with 2 decimal places
     * RPGLE: DBALANCE (9Y 2) with EDTCDE(J) - line 57 in CUSTDSP.dds
     * Edit code J provides decimal editing with commas
     */
    @NotNull(message = "Balance is required")
    @Digits(integer = 9, fraction = 2, message = "Balance must have max 9 digits and 2 decimal places")
    private BigDecimal balance;

    /**
     * Credit Limit - 9 digits with 2 decimal places
     * Not displayed in RPGLE DETAIL screen but available in entity
     */
    @Digits(integer = 9, fraction = 2, message = "Credit limit must have max 9 digits and 2 decimal places")
    private BigDecimal creditLimit;

    /**
     * Last Order Date - YYYYMMDD format
     * Not displayed in RPGLE DETAIL screen but available in entity
     */
    @Pattern(regexp = "\\d{8}", message = "Last order date must be in YYYYMMDD format")
    private String lastOrderDate;

    // Constructors
    public CustomerInquiryDTO() {
    }

    // Getters and Setters
    public BigDecimal getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(BigDecimal customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public BigDecimal getZipCode() {
        return zipCode;
    }

    public void setZipCode(BigDecimal zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(String lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    @Override
    public String toString() {
        return "CustomerInquiryDTO{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", balance=" + balance +
                '}';
    }
}
