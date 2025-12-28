package com.lanarimarco.modernization.repositories;

import com.lanarimarco.modernization.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Customer entity
 *
 * Provides data access methods for Customer Master File (CUSTMAST)
 * Supports the CUST001 Customer Inquiry program
 *
 * Spring Data JPA automatically implements this interface at runtime
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Find customer by customer number
     *
     * This method maps to the RPGLE CHAIN operation:
     * C     PCUSTNO       Chain     CUSTMAST
     *
     * @param customerNumber the customer number to search for
     * @return Optional containing the customer if found, empty otherwise
     */
    Optional<Customer> findByCustomerNumber(Integer customerNumber);

    /**
     * Check if a customer exists by customer number
     *
     * Useful for validation without loading the full entity
     *
     * @param customerNumber the customer number to check
     * @return true if customer exists, false otherwise
     */
    boolean existsByCustomerNumber(Integer customerNumber);
}
