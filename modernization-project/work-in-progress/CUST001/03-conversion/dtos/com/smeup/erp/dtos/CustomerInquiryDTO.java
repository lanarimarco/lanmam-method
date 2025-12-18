package com.smeup.erp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for CUST001 - Customer Inquiry
 * Represents data from RPGLE display file: CUSTDSP
 * Combines both PROMPT and DETAIL screen data
 */
public class CustomerInquiryDTO {

    // Input field from PROMPT screen
    @JsonProperty("customerNumber")
    private Integer customerNumber;

    // Output fields from DETAIL screen (Customer data)
    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("address1")
    private String address1;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zipCode")
    private Integer zipCode;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("creditLimit")
    private BigDecimal creditLimit;

    @JsonProperty("lastOrderDate")
    private LocalDate lastOrderDate;

    // Function key indicators (from RPGLE indicators)
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

    @Override
    public String toString() {
        return "CustomerInquiryDTO{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", balance=" + balance +
                ", success=" + success +
                ", errorIndicator=" + errorIndicator +
                '}';
    }
}
