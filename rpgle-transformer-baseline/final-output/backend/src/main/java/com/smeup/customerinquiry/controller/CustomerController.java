package com.smeup.customerinquiry.controller;

import com.smeup.customerinquiry.dto.CustomerDTO;
import com.smeup.customerinquiry.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Integer customerNumber) {
        log.info("GET request for customer: {}", customerNumber);
        CustomerDTO customer = customerService.getCustomerByNumber(customerNumber);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("GET request for all customers");
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("POST request to create customer: {}", customerDTO.getCustomerNumber());
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{customerNumber}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Integer customerNumber,
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("PUT request to update customer: {}", customerNumber);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerNumber, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerNumber}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerNumber) {
        log.info("DELETE request for customer: {}", customerNumber);
        customerService.deleteCustomer(customerNumber);
        return ResponseEntity.noContent().build();
    }
}
