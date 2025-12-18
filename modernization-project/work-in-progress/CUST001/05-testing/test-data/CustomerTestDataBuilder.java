package com.smeup.erp.testdata;

import com.smeup.erp.entities.Customer;
import com.smeup.erp.dtos.CustomerInquiryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Test Data Builder for Customer entities and DTOs
 * Provides fluent API for creating test data in unit tests
 *
 * Usage:
 * <pre>
 * Customer customer = CustomerTestDataBuilder.aCustomer()
 *     .withCustomerNumber(12345)
 *     .withCustomerName("ACME Corp")
 *     .withBalance(new BigDecimal("1500.00"))
 *     .build();
 * </pre>
 */
public class CustomerTestDataBuilder {

    private Integer customerNumber;
    private String customerName;
    private String address1;
    private String city;
    private String state;
    private Integer zipCode;
    private String phone;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private LocalDate lastOrderDate;

    private CustomerTestDataBuilder() {
        // Default test values
        this.customerNumber = 12345;
        this.customerName = "Test Customer";
        this.address1 = "123 Test Street";
        this.city = "Test City";
        this.state = "NY";
        this.zipCode = 10001;
        this.phone = "555-555-5555";
        this.balance = new BigDecimal("1000.00");
        this.creditLimit = new BigDecimal("5000.00");
        this.lastOrderDate = LocalDate.now();
    }

    /**
     * Start building a Customer entity
     */
    public static CustomerTestDataBuilder aCustomer() {
        return new CustomerTestDataBuilder();
    }

    /**
     * Start building a Customer with default ACME Corporation data
     */
    public static CustomerTestDataBuilder anAcmeCustomer() {
        return new CustomerTestDataBuilder()
            .withCustomerNumber(12345)
            .withCustomerName("ACME Corporation")
            .withAddress1("123 Main Street")
            .withCity("New York")
            .withState("NY")
            .withZipCode(10001)
            .withPhone("212-555-1234")
            .withBalance(new BigDecimal("1500.50"))
            .withCreditLimit(new BigDecimal("5000.00"))
            .withLastOrderDate(LocalDate.of(2025, 12, 15));
    }

    /**
     * Create minimal customer (minimum valid data)
     */
    public static CustomerTestDataBuilder aMinimalCustomer() {
        return new CustomerTestDataBuilder()
            .withCustomerNumber(1)
            .withCustomerName("Min Customer")
            .withBalance(BigDecimal.ZERO)
            .withCreditLimit(BigDecimal.ZERO)
            .withLastOrderDate(null);
    }

    /**
     * Create customer with maximum values
     */
    public static CustomerTestDataBuilder aMaximalCustomer() {
        return new CustomerTestDataBuilder()
            .withCustomerNumber(99999)
            .withCustomerName("ABCDEFGHIJKLMNOPQRSTUVWXYZ12") // 30 chars max
            .withAddress1("ABCDEFGHIJKLMNOPQRSTUVWXYZ12") // 30 chars max
            .withCity("ABCDEFGHIJKLMNOPQR") // 20 chars max
            .withState("CA")
            .withZipCode(99999)
            .withPhone("999-999-9999")
            .withBalance(new BigDecimal("9999999.99")) // 9P 2
            .withCreditLimit(new BigDecimal("9999999.99"))
            .withLastOrderDate(LocalDate.of(2025, 12, 31));
    }

    /**
     * Create customer with negative balance
     */
    public static CustomerTestDataBuilder anOverdrawnCustomer() {
        return new CustomerTestDataBuilder()
            .withCustomerNumber(10004)
            .withCustomerName("Overdrawn Customer")
            .withBalance(new BigDecimal("-500.00"))
            .withCreditLimit(new BigDecimal("5000.00"));
    }

    // Fluent builder methods

    public CustomerTestDataBuilder withCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
        return this;
    }

    public CustomerTestDataBuilder withCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public CustomerTestDataBuilder withAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public CustomerTestDataBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public CustomerTestDataBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public CustomerTestDataBuilder withZipCode(Integer zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public CustomerTestDataBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CustomerTestDataBuilder withBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public CustomerTestDataBuilder withCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
        return this;
    }

    public CustomerTestDataBuilder withLastOrderDate(LocalDate lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
        return this;
    }

    /**
     * Build Customer entity
     */
    public Customer build() {
        Customer customer = new Customer();
        customer.setCustomerNumber(customerNumber);
        customer.setCustomerName(customerName);
        customer.setAddress1(address1);
        customer.setCity(city);
        customer.setState(state);
        customer.setZipCode(zipCode);
        customer.setPhone(phone);
        customer.setBalance(balance);
        customer.setCreditLimit(creditLimit);
        customer.setLastOrderDate(lastOrderDate);
        return customer;
    }

    /**
     * Build CustomerInquiryDTO from current customer data
     */
    public CustomerInquiryDTO buildDTO() {
        CustomerInquiryDTO dto = new CustomerInquiryDTO();
        dto.setCustomerNumber(customerNumber);
        dto.setCustomerName(customerName);
        dto.setAddress1(address1);
        dto.setCity(city);
        dto.setState(state);
        dto.setZipCode(zipCode);
        dto.setPhone(phone);
        dto.setBalance(balance);
        dto.setCreditLimit(creditLimit);
        dto.setLastOrderDate(lastOrderDate);
        dto.setSuccess(true);
        dto.setErrorIndicator(false);
        return dto;
    }

    /**
     * Build CustomerInquiryDTO with only customer number (for request)
     */
    public CustomerInquiryDTO buildRequestDTO() {
        CustomerInquiryDTO dto = new CustomerInquiryDTO();
        dto.setCustomerNumber(customerNumber);
        return dto;
    }
}
