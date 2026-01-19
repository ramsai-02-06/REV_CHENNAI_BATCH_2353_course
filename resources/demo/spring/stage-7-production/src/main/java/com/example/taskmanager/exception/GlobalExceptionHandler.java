package com.example.taskmanager.exception;

import com.example.taskmanager.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Global Exception Handler.
 *
 * @RestControllerAdvice makes this apply to ALL controllers.
 * Each @ExceptionHandler method handles a specific exception type.
 *
 * Benefits:
 * - Centralized error handling
 * - Consistent error response format
 * - Separation of concerns (controllers don't handle errors)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle task not found errors.
     * Returns 404 Not Found.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTaskNotFound(TaskNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }

    /**
     * Handle validation errors (from @Valid).
     * Returns 400 Bad Request with field-level details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return new ErrorResponse("VALIDATION_ERROR", "Validation failed", fieldErrors);
    }

    /**
     * Handle malformed JSON or invalid enum values.
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(HttpMessageNotReadableException ex) {
        String message = "Invalid request body";

        // Check for enum parsing errors
        if (ex.getMessage() != null && ex.getMessage().contains("TaskStatus")) {
            message = "Invalid status value. Allowed: PENDING, IN_PROGRESS, COMPLETED, CANCELLED";
        }

        return new ErrorResponse("BAD_REQUEST", message);
    }

    /**
     * Handle illegal argument exceptions.
     * Returns 400 Bad Request.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage());
    }

    /**
     * Handle all other unexpected errors.
     * Returns 500 Internal Server Error.
     *
     * In production, you'd log the full error but return a generic message.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericError(Exception ex) {
        // In production: log.error("Unexpected error", ex);
        System.err.println("Unexpected error: " + ex.getMessage());
        return new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred");
    }
}
