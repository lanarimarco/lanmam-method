package com.smeup.backend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Map;

/**
 * Standard API response wrapper.
 *
 * @param <T> The type of the data being wrapped.
 */
public class ApiResponse<T> {

    private T data;
    private Map<String, Object> meta;

    /**
     * Default constructor.
     */
    public ApiResponse() {
        this.data = null;
        this.meta = Collections.emptyMap();
    }

    /**
     * Constructor for data only.
     *
     * @param data The data to wrap.
     */
    public ApiResponse(T data) {
        this(data, Collections.emptyMap());
    }

    /**
     * Full constructor.
     *
     * @param data The data to wrap.
     * @param meta Metadata.
     */
    public ApiResponse(T data, Map<String, Object> meta) {
        this.data = data;
        this.meta = meta != null ? meta : Collections.emptyMap();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
