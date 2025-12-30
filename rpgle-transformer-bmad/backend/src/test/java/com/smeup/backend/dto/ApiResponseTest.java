package com.smeup.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ApiResponse.
 */
class ApiResponseTest {

    @Test
    void shouldCreateApiResponseWithData() {
        String data = "test data";
        ApiResponse<String> response = new ApiResponse<>(data);

        assertNotNull(response);
        assertEquals(data, response.getData());
        assertNotNull(response.getMeta());
    }

    @Test
    void shouldCreateApiResponseWithDataAndMeta() {
        String data = "test data";
        Map<String, Object> meta = Collections.singletonMap("timestamp", "2023-01-01T00:00:00Z");
        ApiResponse<String> response = new ApiResponse<>(data, meta);

        assertEquals(data, response.getData());
        assertEquals(meta, response.getMeta());
    }
}
