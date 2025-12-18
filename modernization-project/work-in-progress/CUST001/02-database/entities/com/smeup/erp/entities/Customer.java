package com.smeup.erp.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity representing Customer Master data
 * Mapped to DB2 table: CUSTMAST
 * Original RPGLE file: CUSTMAST.dds
 */
@Entity
@Table(name = "CUSTMAST")
public class Customer {

    @Id
    @Column(name = "CUSTNO", precision = 5, scale = 0)
    private Integer customerNumber;

    @Column(name = "CUSTNAME", length = 30)
    private String customerName;

    @Column(name = "ADDR1", length = 30)
    private String address1;

    @Column(name = "CITY", length = 20)
    private String city;

    @Column(name = "STATE", length = 2)
    private String state;

    @Column(name = "ZIP", precision = 5, scale = 0)
    private Integer zipCode;

    @Column(name = "PHONE", length = 12)
    private String phone;

    @Column(name = "BALANCE", precision = 9, scale = 2)
    private BigDecimal balance;

    @Column(name = "CREDITLIM", precision = 9, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "LASTORDER")
    private LocalDate lastOrderDate;

    // Constructors
    public Customer() {
    }

    public Customer(Integer customerNumber) {
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

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return customerNumber.equals(customer.customerNumber);
    }

    @Override
    public int hashCode() {
        return customerNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerNumber=" + customerNumber +
                ", customerName='" + customerName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", balance=" + balance +
                '}';
    }
}
