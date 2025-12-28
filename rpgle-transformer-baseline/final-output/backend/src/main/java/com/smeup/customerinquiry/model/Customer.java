package com.smeup.customerinquiry.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customer_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customer_number", nullable = false)
    @NotNull(message = "Customer number is required")
    @Min(value = 1, message = "Customer number must be positive")
    @Max(value = 99999, message = "Customer number must be 5 digits or less")
    private Integer customerNumber;

    @Column(name = "customer_name", length = 30, nullable = false)
    @NotBlank(message = "Customer name is required")
    @Size(max = 30, message = "Customer name must not exceed 30 characters")
    private String customerName;

    @Column(name = "address_line1", length = 30)
    @Size(max = 30, message = "Address must not exceed 30 characters")
    private String addressLine1;

    @Column(name = "city", length = 20)
    @Size(max = 20, message = "City must not exceed 20 characters")
    private String city;

    @Column(name = "state", length = 2)
    @Size(max = 2, message = "State must be 2 characters")
    private String state;

    @Column(name = "zip_code")
    @Min(value = 0, message = "Zip code must be positive")
    @Max(value = 99999, message = "Zip code must be 5 digits")
    private Integer zipCode;

    @Column(name = "phone_number", length = 12)
    @Size(max = 12, message = "Phone number must not exceed 12 characters")
    private String phoneNumber;

    @Column(name = "account_balance", precision = 9, scale = 2)
    private BigDecimal accountBalance;

    @Column(name = "credit_limit", precision = 9, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "last_order_date")
    private LocalDate lastOrderDate;
}
