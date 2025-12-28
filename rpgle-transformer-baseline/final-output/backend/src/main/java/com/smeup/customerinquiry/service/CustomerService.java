package com.smeup.customerinquiry.service;

import com.smeup.customerinquiry.dto.CustomerDTO;
import com.smeup.customerinquiry.exception.CustomerNotFoundException;
import com.smeup.customerinquiry.model.Customer;
import com.smeup.customerinquiry.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerByNumber(Integer customerNumber) {
        log.info("Fetching customer with number: {}", customerNumber);

        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException(customerNumber));

        return mapToDTO(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");

        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO.getCustomerNumber());

        Customer customer = mapToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        return mapToDTO(savedCustomer);
    }

    @Transactional
    public CustomerDTO updateCustomer(Integer customerNumber, CustomerDTO customerDTO) {
        log.info("Updating customer: {}", customerNumber);

        Customer existingCustomer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException(customerNumber));

        updateEntityFromDTO(existingCustomer, customerDTO);
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return mapToDTO(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Integer customerNumber) {
        log.info("Deleting customer: {}", customerNumber);

        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException(customerNumber));

        customerRepository.delete(customer);
    }

    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerNumber(customer.getCustomerNumber())
                .customerName(customer.getCustomerName())
                .addressLine1(customer.getAddressLine1())
                .city(customer.getCity())
                .state(customer.getState())
                .zipCode(customer.getZipCode())
                .phoneNumber(customer.getPhoneNumber())
                .accountBalance(customer.getAccountBalance())
                .creditLimit(customer.getCreditLimit())
                .lastOrderDate(customer.getLastOrderDate())
                .build();
    }

    private Customer mapToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerNumber(dto.getCustomerNumber());
        updateEntityFromDTO(customer, dto);
        return customer;
    }

    private void updateEntityFromDTO(Customer customer, CustomerDTO dto) {
        customer.setCustomerName(dto.getCustomerName());
        customer.setAddressLine1(dto.getAddressLine1());
        customer.setCity(dto.getCity());
        customer.setState(dto.getState());
        customer.setZipCode(dto.getZipCode());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setAccountBalance(dto.getAccountBalance());
        customer.setCreditLimit(dto.getCreditLimit());
        customer.setLastOrderDate(dto.getLastOrderDate());
    }
}
