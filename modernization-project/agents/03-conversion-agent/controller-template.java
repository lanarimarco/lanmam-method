package {PACKAGE_NAME}.controllers;

import com.company.modernization.dto.*;
import com.company.modernization.services.*;
import com.company.modernization.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API for {PROGRAM_NAME} functionality
 * Replaces RPGLE display file: {DISPLAY_FILE}
 */
@RestController
@RequestMapping("/api/{resource}")
@CrossOrigin(origins = "*")
public class {ProgramName}Controller {

    private static final Logger log = LoggerFactory.getLogger({ProgramName}Controller.class);

    @Autowired
    private {ProgramName}Service service;

    /**
     * Main endpoint - equivalent to EXFMT display
     * POST used to submit data (like pressing Enter in RPG screen)
     */
    @PostMapping("/process")
    public ResponseEntity<{Response}DTO> process(@RequestBody {Request}DTO request) {
        log.info("API request received: {}", request);

        try {
            {Response}DTO response = service.processRequest(request);
            return ResponseEntity.ok(response);

        } catch (ValidationException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));

        } catch (NotFoundException e) {
            log.warn("Not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(e.getMessage()));

        } catch (Exception e) {
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Internal server error"));
        }
    }

    /**
     * GET endpoint for initial screen load
     * Equivalent to displaying empty screen in RPG
     */
    @GetMapping("/init")
    public ResponseEntity<{Response}DTO> initialize() {
        {Response}DTO response = new {Response}DTO();
        // Set default values
        return ResponseEntity.ok(response);
    }

    private {Response}DTO createErrorResponse(String message) {
        {Response}DTO response = new {Response}DTO();
        response.setErrorMessage(message);
        response.setSuccess(false);
        return response;
    }
}
