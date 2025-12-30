package com.smeup.backend.mapper;

import com.smeup.backend.dto.CustomerDTO;
import com.smeup.backend.entity.Customer;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between MyCustomer Entity and CustomerDTO.
 */
@Component
public class CustomerMapper {

    /**
     * Converts a Customer entity to a CustomerDTO.
     *
     * @param customer The entity to convert.
     * @return The corresponding DTO, or null if input is null.
     */
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getAddressLine1(),
                customer.getCity(),
                customer.getState(),
                customer.getZipCode(),
                customer.getPhoneNumber(),
                customer.getAccountBalance(),
                customer.getCreditLimit(),
                customer.getLastOrderDate());
    }

    /**
     * Converts a CustomerDTO to a Customer entity.
     *
     * @param dto The DTO to convert.
     * @return The corresponding Entity, or null if input is null.
     */
    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setCustomerId(dto.customerId());
        customer.setCustomerName(dto.customerName());
        customer.setAddressLine1(dto.addressLine1());
        customer.setCity(dto.city());
        customer.setState(dto.state());
        customer.setZipCode(dto.zipCode());
        customer.setPhoneNumber(dto.phoneNumber());
        customer.setAccountBalance(dto.accountBalance());
        customer.setCreditLimit(dto.creditLimit());
        customer.setLastOrderDate(dto.lastOrderDate());

        return customer;
    }
}
