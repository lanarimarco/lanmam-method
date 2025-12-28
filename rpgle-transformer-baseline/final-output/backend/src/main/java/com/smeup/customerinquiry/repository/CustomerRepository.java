package com.smeup.customerinquiry.repository;

import com.smeup.customerinquiry.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByCustomerNumber(Integer customerNumber);

    boolean existsByCustomerNumber(Integer customerNumber);
}
