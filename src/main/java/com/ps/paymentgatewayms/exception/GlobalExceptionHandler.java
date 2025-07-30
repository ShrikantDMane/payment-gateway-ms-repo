package com.ps.paymentgatewayms.exception;

import com.ps.paymentgatewayms.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    // Example: Handle a custom exception, e.g., PaymentProcessingException
    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<String> handlePaymentProcessingException(PaymentProcessingException ex) {
        logger.error("Error occurred: {}",ex.getMessage());

        // Return a response entity with appropriate HTTP status code
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error processing payment: " + ex.getMessage());
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error("Error occurred: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + ex.getMessage());
    }
}

