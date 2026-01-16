package {PACKAGE_NAME}.repositories;

import {PACKAGE_NAME}.entities.{EntityName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository for {EntityName} entity
 * Supports operations from RPGLE program: {PROGRAM_NAME}
 */
@Repository
public interface {EntityName}Repository extends JpaRepository<{EntityName}, {KeyType}> {

    // ==================== Standard Query Methods ====================
    
    /**
     * Find by primary key field
     * RPGLE equivalent: CHAIN operation
     */
    Optional<{EntityName}> findBy{KeyField}({KeyType} {keyField});

    /**
     * Check if entity exists by key
     * RPGLE equivalent: %FOUND check after CHAIN
     */
    boolean existsBy{KeyField}({KeyType} {keyField});

    // ==================== Range Query Methods ====================
    
    /**
     * Find entities with key greater than or equal to value
     * RPGLE equivalent: SETLL + READ loop
     */
    List<{EntityName}> findBy{KeyField}GreaterThanEqualOrderBy{KeyField}({KeyType} {keyField});

    /**
     * Find entities by field value
     * RPGLE equivalent: READ with filter
     */
    List<{EntityName}> findBy{Field}({Type} {field});

    // ==================== Custom Query Methods ====================
    
    /**
     * Custom query example
     * Purpose: {description}
     */
    @Query("SELECT e FROM {EntityName} e WHERE e.{field} = :value")
    List<{EntityName}> findByCustomCriteria(@Param("value") String value);

    /**
     * Custom query with multiple parameters
     * Purpose: {description}
     */
    @Query("SELECT e FROM {EntityName} e WHERE e.{field1} = :param1 AND e.{field2} = :param2")
    List<{EntityName}> findByMultipleCriteria(
            @Param("param1") String param1,
            @Param("param2") String param2);

    // ==================== Aggregate Methods ====================
    
    /**
     * Count entities by field
     */
    long countBy{Field}({Type} {field});
}
