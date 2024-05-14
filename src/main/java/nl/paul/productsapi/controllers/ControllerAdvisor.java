package nl.paul.productsapi.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ControllerAdvisor {

    // Developer's note: I consider throwing custom Exceptions, marking them as runtime Exceptions to be an anti-pattern.
    // However, this an ControllerAdvice will catch all my uncaught Exceptions and gracefully fail the request to the
    // consumer. In this case I chose to show the validated messaged within the response entity.

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(final Exception e) {
        return ResponseEntity.internalServerError().body(String.format("Some exception occurred and was not caught [%s].", e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(final ConstraintViolationException e) {
        Optional<ConstraintViolation<?>> constraintViolation = e.getConstraintViolations().stream().findFirst();
        return ResponseEntity.badRequest().body(constraintViolation.isPresent() ? constraintViolation.get().getMessageTemplate() : "Provided object was not valid.");
    }

}