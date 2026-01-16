package {PACKAGE_NAME}.controllers;

import {PACKAGE_NAME}.dtos.*;
import {PACKAGE_NAME}.services.*;
import {PACKAGE_NAME}.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST API for {PROGRAM_NAME} functionality
 * Replaces RPGLE display file: {DISPLAY_FILE}
 */
@RestController
@RequestMapping("/api/{resource}")
@CrossOrigin(origins = "*")
public class {ProgramName}Controller {

    private static final Logger log = LoggerFactory.getLogger({ProgramName}Controller.class);

    private final {ProgramName}Service service;

    @Autowired
    public {ProgramName}Controller({ProgramName}Service service) {
        this.service = service;
    }

    // ==================== GET Endpoints ====================

    /**
     * Get all entities
     * 
     * @return list of entities
     */
    @GetMapping
    public ResponseEntity<List<{Response}DTO>> getAll() {
        log.info("GET /api/{resource} - Getting all entities");

        List<{Response}DTO> result = service.getAll();
        return ResponseEntity.ok(result);
    }

    /**
     * Get entity by ID
     * 
     * @param id the entity ID
     * @return the entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<{Response}DTO> getById(@PathVariable {KeyType} id) {
        log.info("GET /api/{resource}/{} - Getting entity by ID", id);

        try {
            {Response}DTO result = service.getById(id);
            return ResponseEntity.ok(result);
        } catch ({Entity}NotFoundException e) {
            log.warn("Entity not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Initialize screen - equivalent to displaying empty RPG screen
     * 
     * @return empty response with default values
     */
    @GetMapping("/init")
    public ResponseEntity<{Response}DTO> initialize() {
        log.info("GET /api/{resource}/init - Initializing");

        {Response}DTO response = new {Response}DTO();
        // Set default values
        return ResponseEntity.ok(response);
    }

    // ==================== POST Endpoints ====================

    /**
     * Create new entity
     * 
     * @param request the creation request
     * @return the created entity
     */
    @PostMapping
    public ResponseEntity<{Response}DTO> create(@Valid @RequestBody {Request}DTO request) {
        log.info("POST /api/{resource} - Creating entity");

        try {
            {Response}DTO result = service.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ValidationException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Process request - main business operation
     * Equivalent to EXFMT display interaction
     * 
     * @param request the request data
     * @return the processed response
     */
    @PostMapping("/process")
    public ResponseEntity<{Response}DTO> process(@Valid @RequestBody {Request}DTO request) {
        log.info("POST /api/{resource}/process - Processing request");

        try {
            // Handle function key equivalents
            if (request.isF3Pressed()) {
                log.info("F3 (Exit) pressed");
                return ResponseEntity.ok(createExitResponse());
            }

            if (request.isF12Pressed()) {
                log.info("F12 (Cancel) pressed");
                return ResponseEntity.ok(createCancelResponse());
            }

            {Response}DTO response = service.processRequest(request);
            return ResponseEntity.ok(response);

        } catch (ValidationException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));

        } catch ({Entity}NotFoundException e) {
            log.warn("Not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));

        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal server error"));
        }
    }

    // ==================== PUT Endpoints ====================

    /**
     * Update existing entity
     * 
     * @param id the entity ID
     * @param request the update request
     * @return the updated entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<{Response}DTO> update(
            @PathVariable {KeyType} id,
            @Valid @RequestBody {Request}DTO request) {
        log.info("PUT /api/{resource}/{} - Updating entity", id);

        try {
            {Response}DTO result = service.update(id, request);
            return ResponseEntity.ok(result);
        } catch ({Entity}NotFoundException e) {
            log.warn("Entity not found: {}", id);
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== DELETE Endpoints ====================

    /**
     * Delete entity
     * 
     * @param id the entity ID
     * @return no content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable {KeyType} id) {
        log.info("DELETE /api/{resource}/{} - Deleting entity", id);

        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch ({Entity}NotFoundException e) {
            log.warn("Entity not found: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Helper Methods ====================

    private {Response}DTO createErrorResponse(String message) {
        {Response}DTO response = new {Response}DTO();
        response.setSuccess(false);
        response.setErrorMessage(message);
        return response;
    }

    private {Response}DTO createExitResponse() {
        {Response}DTO response = new {Response}DTO();
        response.setSuccess(true);
        response.setExited(true);
        return response;
    }

    private {Response}DTO createCancelResponse() {
        {Response}DTO response = new {Response}DTO();
        response.setSuccess(true);
        response.setCancelled(true);
        return response;
    }
}
