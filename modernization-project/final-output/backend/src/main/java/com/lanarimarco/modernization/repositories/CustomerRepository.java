package com.lanarimarco.modernization.repositories;

import com.lanarimarco.modernization.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Repository for Customer entity
 * Supports operations from RPGLE program: CUST001
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, BigDecimal> {

    /**
     * CHAIN operation equivalent - Find customer by customer number (keyed read)
     * Maps to: CHAIN CUSTNO CUSTMAST operation in CUST001.rpgle
     *
     * @param customerNumber The customer number to search for
     * @return Optional containing the customer if found, empty otherwise
     */
    Optional<Customer> findByCustomerNumber(BigDecimal customerNumber);

    /**
     * Existence check - Validates if a customer exists
     * Useful for validation logic
     *
     * @param customerNumber The customer number to check
     * @return true if customer exists, false otherwise
     */
    boolean existsByCustomerNumber(BigDecimal customerNumber);
}
