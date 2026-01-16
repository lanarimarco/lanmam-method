package {PACKAGE_NAME}.repositories;

import {PACKAGE_NAME}.entities.{EntityName};
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for {EntityName}Repository
 */
@DataJpaTest
class {EntityName}RepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private {EntityName}Repository repository;

    // ==================== Test Data Setup ====================

    private {EntityName} createTestEntity() {
        {EntityName} entity = new {EntityName}();
        entity.set{KeyField}(new BigDecimal("12345"));
        entity.set{Field}("Test Value");
        // Set other required fields
        return entity;
    }

    // ==================== Find By Key Tests ====================

    @Test
    void findBy{KeyField}_WhenExists_ReturnsEntity() {
        // Given
        {EntityName} entity = createTestEntity();
        entityManager.persist(entity);
        entityManager.flush();

        // When
        Optional<{EntityName}> found = repository.findBy{KeyField}(entity.get{KeyField}());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().get{KeyField}()).isEqualTo(entity.get{KeyField}());
    }

    @Test
    void findBy{KeyField}_WhenNotExists_ReturnsEmpty() {
        // When
        Optional<{EntityName}> found = repository.findBy{KeyField}(new BigDecimal("99999"));

        // Then
        assertThat(found).isEmpty();
    }

    // ==================== Exists Tests ====================

    @Test
    void existsBy{KeyField}_WhenExists_ReturnsTrue() {
        // Given
        {EntityName} entity = createTestEntity();
        entityManager.persist(entity);
        entityManager.flush();

        // When
        boolean exists = repository.existsBy{KeyField}(entity.get{KeyField}());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsBy{KeyField}_WhenNotExists_ReturnsFalse() {
        // When
        boolean exists = repository.existsBy{KeyField}(new BigDecimal("99999"));

        // Then
        assertThat(exists).isFalse();
    }

    // ==================== Save Tests ====================

    @Test
    void save_ValidEntity_SavesSuccessfully() {
        // Given
        {EntityName} entity = createTestEntity();

        // When
        {EntityName} saved = repository.save(entity);

        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.get{KeyField}()).isEqualTo(entity.get{KeyField}());

        // Verify persistence
        {EntityName} found = entityManager.find({EntityName}.class, saved.get{KeyField}());
        assertThat(found).isNotNull();
    }

    // ==================== Delete Tests ====================

    @Test
    void deleteById_WhenExists_DeletesSuccessfully() {
        // Given
        {EntityName} entity = createTestEntity();
        entityManager.persist(entity);
        entityManager.flush();
        BigDecimal id = entity.get{KeyField}();

        // When
        repository.deleteById(id);
        entityManager.flush();

        // Then
        {EntityName} found = entityManager.find({EntityName}.class, id);
        assertThat(found).isNull();
    }

    // ==================== Range Query Tests ====================

    @Test
    void findBy{KeyField}GreaterThanEqual_ReturnsMatchingEntities() {
        // Given
        {EntityName} entity1 = createTestEntity();
        entity1.set{KeyField}(new BigDecimal("100"));
        entityManager.persist(entity1);

        {EntityName} entity2 = createTestEntity();
        entity2.set{KeyField}(new BigDecimal("200"));
        entityManager.persist(entity2);

        {EntityName} entity3 = createTestEntity();
        entity3.set{KeyField}(new BigDecimal("300"));
        entityManager.persist(entity3);

        entityManager.flush();

        // When
        List<{EntityName}> found = repository.findBy{KeyField}GreaterThanEqualOrderBy{KeyField}(
                new BigDecimal("200"));

        // Then
        assertThat(found).hasSize(2);
        assertThat(found.get(0).get{KeyField}()).isEqualTo(new BigDecimal("200"));
        assertThat(found.get(1).get{KeyField}()).isEqualTo(new BigDecimal("300"));
    }

    // ==================== Custom Query Tests ====================

    @Test
    void findByCustomCriteria_ReturnsMatchingEntities() {
        // Given
        {EntityName} entity = createTestEntity();
        entity.set{Field}("SearchValue");
        entityManager.persist(entity);
        entityManager.flush();

        // When
        List<{EntityName}> found = repository.findByCustomCriteria("SearchValue");

        // Then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).get{Field}()).isEqualTo("SearchValue");
    }
}
