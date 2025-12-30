package com.smeup.backend.repository;

import com.smeup.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Customer entity operations.
 *
 * <p>Original RPGLE Operations mapped to Spring Data JPA:
 * <ul>
 *   <li>CHAIN CUSTMAST - Record lookup by key → findByCustomerId()</li>
 *   <li>READ CUSTMAST - Sequential read → findAll()</li>
 *   <li>WRITE CUSTMAST - Insert record → save()</li>
 *   <li>UPDATE CUSTMAST - Update record → save()</li>
 *   <li>DELETE CUSTMAST - Delete record → deleteById()</li>
 * </ul>
 *
 * <p>Source: RPGLE programs use CHAIN opcode to retrieve customer records
 * from CUSTMAST physical file by customer number (CUSTNO).
 *
 * <p>DDS Physical File: source-rpgle/dds/physical-files/CUSTMAST.dds
 *
 * @see Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  /**
   * Find customer by customer ID.
   *
   * <p>RPGLE Equivalent: CHAIN CUSTMAST
   * <pre>
   * C     CUSTNO    CHAIN     CUSTMAST
   * C                   IF        %FOUND(CUSTMAST)
   * C                   ...       (record found processing)
   * C                   ENDIF
   * </pre>
   *
   * <p>Retrieves a single record by key (CUSTNO field).
   * Returns empty Optional if not found (equivalent to %FOUND = *OFF).
   *
   * @param customerId the customer number (CUSTNO field from DDS)
   * @return Optional containing the customer if found, empty otherwise
   */
  Optional<Customer> findByCustomerId(Long customerId);

  /**
   * Find customers by name containing the given string (case-insensitive).
   *
   * <p>RPGLE Equivalent: SETLL/READE with partial key match
   * This provides search functionality for customer lookup screens.
   *
   * @param customerName partial or full customer name to search
   * @return List of customers matching the name pattern
   */
  List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);

}
