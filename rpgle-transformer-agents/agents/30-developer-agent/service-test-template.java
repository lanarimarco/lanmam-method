package {PACKAGE_NAME}.services;

import {PACKAGE_NAME}.dtos.*;
import {PACKAGE_NAME}.entities.*;
import {PACKAGE_NAME}.repositories.*;
import {PACKAGE_NAME}.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Service tests for {ProgramName}Service
 */
@ExtendWith(MockitoExtension.class)
class {ProgramName}ServiceTest {

    @Mock
    private {Entity}Repository {entity}Repository;

    @InjectMocks
    private {ProgramName}Service service;

    private {Entity} testEntity;
    private {Request}DTO testRequest;

    @BeforeEach
    void setUp() {
        // Set up test entity
        testEntity = new {Entity}();
        testEntity.set{KeyField}(new BigDecimal("12345"));
        testEntity.set{Field}("Test Value");

        // Set up test request
        testRequest = new {Request}DTO();
        testRequest.set{KeyField}(new BigDecimal("12345"));
    }

    // ==================== GetById Tests ====================

    @Test
    void getById_WhenExists_ReturnsDto() {
        // Given
        when({entity}Repository.findById(any())).thenReturn(Optional.of(testEntity));

        // When
        {Response}DTO result = service.getById(new BigDecimal("12345"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get{KeyField}()).isEqualTo(testEntity.get{KeyField}());
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void getById_WhenNotExists_ThrowsNotFoundException() {
        // Given
        when({entity}Repository.findById(any())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.getById(new BigDecimal("99999")))
                .isInstanceOf({Entity}NotFoundException.class)
                .hasMessageContaining("not found");
    }

    // ==================== GetAll Tests ====================

    @Test
    void getAll_ReturnsAllEntities() {
        // Given
        {Entity} entity2 = new {Entity}();
        entity2.set{KeyField}(new BigDecimal("67890"));
        when({entity}Repository.findAll()).thenReturn(Arrays.asList(testEntity, entity2));

        // When
        List<{Response}DTO> result = service.getAll();

        // Then
        assertThat(result).hasSize(2);
    }

    // ==================== Create Tests ====================

    @Test
    void create_ValidRequest_CreatesEntity() {
        // Given
        when({entity}Repository.save(any())).thenReturn(testEntity);

        // When
        {Response}DTO result = service.create(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        verify({entity}Repository).save(any());
    }

    @Test
    void create_NullRequest_ThrowsValidationException() {
        // When/Then
        assertThatThrownBy(() -> service.create(null))
                .isInstanceOf(ValidationException.class);
    }

    // ==================== Update Tests ====================

    @Test
    void update_WhenExists_UpdatesEntity() {
        // Given
        when({entity}Repository.findById(any())).thenReturn(Optional.of(testEntity));
        when({entity}Repository.save(any())).thenReturn(testEntity);

        // When
        {Response}DTO result = service.update(new BigDecimal("12345"), testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        verify({entity}Repository).save(any());
    }

    @Test
    void update_WhenNotExists_ThrowsNotFoundException() {
        // Given
        when({entity}Repository.findById(any())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.update(new BigDecimal("99999"), testRequest))
                .isInstanceOf({Entity}NotFoundException.class);
    }

    // ==================== Delete Tests ====================

    @Test
    void delete_WhenExists_DeletesEntity() {
        // Given
        when({entity}Repository.existsById(any())).thenReturn(true);
        doNothing().when({entity}Repository).deleteById(any());

        // When
        service.delete(new BigDecimal("12345"));

        // Then
        verify({entity}Repository).deleteById(any());
    }

    @Test
    void delete_WhenNotExists_ThrowsNotFoundException() {
        // Given
        when({entity}Repository.existsById(any())).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> service.delete(new BigDecimal("99999")))
                .isInstanceOf({Entity}NotFoundException.class);
    }

    // ==================== ProcessRequest Tests ====================

    @Test
    void processRequest_ValidRequest_ReturnsResponse() {
        // Given
        when({entity}Repository.findById(any())).thenReturn(Optional.of(testEntity));

        // When
        {Response}DTO result = service.processRequest(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void processRequest_NullRequest_ThrowsValidationException() {
        // When/Then
        assertThatThrownBy(() -> service.processRequest(null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void processRequest_EntityNotFound_ThrowsNotFoundException() {
        // Given
        when({entity}Repository.findById(any())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.processRequest(testRequest))
                .isInstanceOf({Entity}NotFoundException.class);
    }
}
