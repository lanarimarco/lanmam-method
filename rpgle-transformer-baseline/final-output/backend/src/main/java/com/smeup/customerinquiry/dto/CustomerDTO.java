package com.smeup.customerinquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Integer customerNumber;
    private String customerName;
    private String addressLine1;
    private String city;
    private String state;
    private Integer zipCode;
    private String phoneNumber;
    private BigDecimal accountBalance;
    private BigDecimal creditLimit;
    private LocalDate lastOrderDate;
}
