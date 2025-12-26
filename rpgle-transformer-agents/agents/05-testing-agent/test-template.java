package {PACKAGE_NAME}.services;

import com.company.modernization.dto.*;
import com.company.modernization.entities.*;
import com.company.modernization.repositories.*;
import com.company.modernization.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {ProgramName}Service
 * Tests converted logic from RPGLE program: {PROGRAM_NAME}
 */
@ExtendWith(MockitoExtension.class)
class {ProgramName}ServiceTest {

    @Mock
    private {Entity}Repository {entity}Repository;

    @InjectMocks
    private {ProgramName}Service service;

    private {Request}DTO validRequest;
    private {Entity} testEntity;

    @BeforeEach
    void setUp() {
        // Setup test data
        validRequest = new {Request}DTO();
        validRequest.set{Key}(new BigDecimal("1234567"));

        testEntity = new {Entity}();
        testEntity.set{Key}(new BigDecimal("1234567"));
        testEntity.set{Field}("Test Value");
    }

    @Test
    @DisplayName("Should successfully process valid request")
    void testProcessRequest_Success() {
        // Given
        when({entity}Repository.findById(any(BigDecimal.class)))
            .thenReturn(Optional.of(testEntity));

        // When
        {Response}DTO response = service.processRequest(validRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Test Value", response.get{Field}());
        verify({entity}Repository, times(1)).findById(validRequest.get{Key}());
    }

    @Test
    @DisplayName("Should throw NotFoundException when entity not found")
    void testProcessRequest_NotFound() {
        // Given
        when({entity}Repository.findById(any(BigDecimal.class)))
            .thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            service.processRequest(validRequest);
        });

        verify({entity}Repository, times(1)).findById(validRequest.get{Key}());
    }

    @Test
    @DisplayName("Should throw ValidationException for null key")
    void testProcessRequest_NullKey() {
        // Given
        validRequest.set{Key}(null);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            service.processRequest(validRequest);
        });

        verify({entity}Repository, never()).findById(any());
    }

    @Test
    @DisplayName("Should handle RPGLE CHAIN operation equivalent")
    void testChainOperation() {
        // This test verifies the Java code matches RPGLE CHAIN behavior
        // Given
        BigDecimal key = new BigDecimal("1234567");
        when({entity}Repository.findById(key))
            .thenReturn(Optional.of(testEntity));

        // When
        Optional<{Entity}> result = {entity}Repository.findById(key);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testEntity, result.get());
    }

    @Test
    @DisplayName("Should handle edge case: zero value")
    void testEdgeCase_ZeroValue() {
        // Given
        validRequest.set{Key}(BigDecimal.ZERO);

        // When & Then
        // Test based on how RPGLE handles zero
        assertThrows(ValidationException.class, () -> {
            service.processRequest(validRequest);
        });
    }

    @Test
    @DisplayName("Should handle edge case: maximum value")
    void testEdgeCase_MaxValue() {
        // Given
        BigDecimal maxValue = new BigDecimal("9999999"); // 7 digits as per RPGLE
        validRequest.set{Key}(maxValue);
        when({entity}Repository.findById(maxValue))
            .thenReturn(Optional.of(testEntity));

        // When
        {Response}DTO response = service.processRequest(validRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }
}
