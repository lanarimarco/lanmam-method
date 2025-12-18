package com.company.modernization.repositories;

import com.company.modernization.entities.{EntityName};
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
public interface {EntityName}Repository extends JpaRepository<{EntityName}, BigDecimal> {

    // CHAIN operation equivalent (keyed read)
    Optional<{EntityName}> findBy{KeyField}(BigDecimal {keyField});

    // SETLL + READ equivalent (range read)
    List<{EntityName}> findBy{Field}GreaterThanEqualOrderBy{Field}(BigDecimal value);

    // Custom query if needed
    @Query("SELECT e FROM {EntityName} e WHERE e.{field} = :value")
    List<{EntityName}> findByCustomCriteria(@Param("value") String value);

    // Exists check (for validation)
    boolean existsBy{KeyField}(BigDecimal {keyField});
}
