package com.smeup.erp.repositories;

import com.smeup.erp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Customer entity
 * Supports operations from RPGLE program: CUST001
 *
 * Original RPGLE operations:
 * - CHAIN CUSTMAST (keyed read by customer number)
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * CHAIN operation equivalent - keyed read by customer number
     * Returns Optional to handle "not found" scenario (%Found in RPGLE)
     *
     * @param customerNumber the customer number to search for
     * @return Optional containing Customer if found, empty otherwise
     */
    Optional<Customer> findByCustomerNumber(Integer customerNumber);

    /**
     * Exists check - useful for validation
     *
     * @param customerNumber the customer number to check
     * @return true if customer exists, false otherwise
     */
    boolean existsByCustomerNumber(Integer customerNumber);
}
