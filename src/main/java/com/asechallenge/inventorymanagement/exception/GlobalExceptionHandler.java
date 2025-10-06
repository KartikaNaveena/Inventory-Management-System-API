package com.asechallenge.inventorymanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import com.asechallenge.inventorymanagement.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Duplicate Product
    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateProduct(
            DuplicateProductException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
    }

    // Invalid Stock Value
   @ExceptionHandler(InvalidStockValueException.class)
   public ResponseEntity<ErrorResponseDTO> handleInvalidStockValue(
                InvalidStockValueException ex,
                HttpServletRequest request) {

        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "Invalid stock value provided.";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI()));
    }

    // Product Not Found
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
    }

    // Malformed JSON / Unrecognized Property
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        Throwable cause = ex.getCause();
        String message;

        if (cause instanceof UnrecognizedPropertyException unrecognizedPropertyEx) {
            message = String.format("Unknown property: %s", unrecognizedPropertyEx.getPropertyName());
        } else {
            message = "Malformed JSON request";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI()));
    }

    // Wrong Endpoint
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request) {

        String message = "Wrong API call: The requested endpoint does not exist.";

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, message, request.getRequestURI()));
    }

    // Illegal Argument
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "Invalid input provided.";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI()));
    }

    // Missing Request Parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingParams(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI()));
    }

    // Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> String.format("%s: %s", err.getField(), err.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, errors, request.getRequestURI()));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidParams(
                ServletRequestBindingException ex,
                HttpServletRequest request) {
        String message = "Request parameters are invalid or do not match the required parameters";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI()));
    }

    // Fallback for any other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(
            Exception ex,
            HttpServletRequest request) {

        String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "An unexpected error occurred.";

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, message, request.getRequestURI()));
    }

    // Helper method to build consistent error response
    private ErrorResponseDTO buildError(HttpStatus status, String message, String path) {
        return new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }
}
