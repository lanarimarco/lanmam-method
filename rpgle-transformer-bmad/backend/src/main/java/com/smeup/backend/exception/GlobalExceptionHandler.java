package com.smeup.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TITLE_NOT_FOUND = "Customer Not Found";
    private static final String TITLE_INVALID_ID = "Invalid Customer ID";

    /**
     * Handles CustomerNotFoundException.
     *
     * @param ex The exception.
     * @return ProblemDetail response with 404 status.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle(TITLE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * Handles InvalidCustomerIdException.
     *
     * @param ex The exception.
     * @return ProblemDetail response with 400 status.
     */
    @ExceptionHandler(InvalidCustomerIdException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCustomerIdException(
            InvalidCustomerIdException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle(TITLE_INVALID_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
