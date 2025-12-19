package com.smeup.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application for Customer Inquiry (CUST001)
 *
 * This application provides REST API endpoints for customer inquiry operations,
 * modernized from the original RPGLE program CUST001.
 *
 * @author Modernization Team
 * @version 1.0.0
 */
@SpringBootApplication
public class CustomerInquiryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerInquiryApplication.class, args);
    }
}
