package com.smeup.erp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for CUST001 - Customer Inquiry
 * Represents data from RPGLE display file: CUSTDSP
 * Combines both PROMPT and DETAIL screen data
 *
 * NOTE: This DTO includes bean validation annotations for input validation.
 * Validation is triggered by @Valid annotation in the controller.
 */
public class CustomerInquiryDTO {

    // Input field from PROMPT screen
    @JsonProperty("customerNumber")
    @NotNull(message = "Customer number is required")
    @Min(value = 1, message = "Customer number must be positive")
    @Max(value = 99999, message = "Customer number exceeds maximum value (99999)")
    private Integer customerNumber;

    // Output fields from DETAIL screen (Customer data)
    @JsonProperty("customerName")
    @Size(max = 30, message = "Customer name exceeds maximum length (30)")
    private String customerName;

    @JsonProperty("address1")
    @Size(max = 30, message = "Address exceeds maximum length (30)")
    private String address1;

    @JsonProperty("city")
    @Size(max = 20, message = "City name exceeds maximum length (20)")
    private String city;

    @JsonProperty("state")
    @Size(max = 2, min = 2, message = "State code must be exactly 2 characters")
    @Pattern(regexp = "^[A-Z]{2}$", message = "State code must be 2 uppercase letters")
    private String state;

    @JsonProperty("zipCode")
    @Min(value = 0, message = "ZIP code must be positive")
    @Max(value = 99999, message = "ZIP code must be 5 digits (max 99999)")
    private Integer zipCode;

    @JsonProperty("phone")
    @Size(max = 12, message = "Phone number exceeds maximum length (12)")
    @Pattern(regexp = "^[0-9\\-\\s()]*$", message = "Phone number contains invalid characters")
    private String phone;

    @JsonProperty("balance")
    @DecimalMin(value = "-999999.99", message = "Balance exceeds minimum allowed value")
    @DecimalMax(value = "9999999.99", message = "Balance exceeds maximum allowed value")
    @Digits(integer = 7, fraction = 2, message = "Balance must have max 7 digits and 2 decimal places")
    private BigDecimal balance;

    @JsonProperty("creditLimit")
    @DecimalMin(value = "0.00", message = "Credit limit cannot be negative")
    @DecimalMax(value = "9999999.99", message = "Credit limit exceeds maximum allowed value")
    @Digits(integer = 7, fraction = 2, message = "Credit limit must have max 7 digits and 2 decimal places")
    private BigDecimal creditLimit;

    @JsonProperty("lastOrderDate")
    @PastOrPresent(message = "Last order date cannot be in the future")
    private LocalDate lastOrderDate;

    // Function key indicators (from RPGLE indicators)
    // NOTE: These are for compatibility with legacy systems
    // New implementations should handle this in the UI layer
    @JsonProperty("f3Pressed")
    private boolean f3Pressed = false;  // Exit key (*IN03)

    @JsonProperty("f12Pressed")
    private boolean f12Pressed = false;  // Cancel/Return key (*IN12)

    // Response metadata
    @JsonProperty("success")
    private boolean success = true;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("errorIndicator")
    private boolean errorIndicator = false;  // *IN90 from RPGLE

    // Constructors
    public CustomerInquiryDTO() {
    }

    public CustomerInquiryDTO(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    // Getters and Setters
    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
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

    public LocalDate getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(LocalDate lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    public boolean isF3Pressed() {
        return f3Pressed;
    }

    public void setF3Pressed(boolean f3Pressed) {
        this.f3Pressed = f3Pressed;
    }

    public boolean isF12Pressed() {
        return f12Pressed;
    }

    public void setF12Pressed(boolean f12Pressed) {
        this.f12Pressed = f12Pressed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isErrorIndicator() {
        return errorIndicator;
    }

    public void setErrorIndicator(boolean errorIndicator) {
        this.errorIndicator = errorIndicator;
    }

    /**
     * toString() implementation that excludes sensitive customer data.
     * Only includes identifiers and status fields for logging purposes.
     * Full customer details should not appear in log files.
     */
    @Override
    public String toString() {
        return "CustomerInquiryDTO{" +
                "customerNumber=" + customerNumber +
                ", success=" + success +
                ", errorIndicator=" + errorIndicator +
                ", hasError=" + (errorMessage != null) +
                '}';
    }
}
