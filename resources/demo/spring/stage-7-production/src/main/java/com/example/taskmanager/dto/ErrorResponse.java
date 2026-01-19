package com.example.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response format.
 *
 * All API errors return this structure for consistency.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String error,
    String message,
    List<FieldError> details,
    LocalDateTime timestamp
) {
    /**
     * Simple error (no field details).
     */
    public ErrorResponse(String error, String message) {
        this(error, message, null, LocalDateTime.now());
    }

    /**
     * Validation error with field details.
     */
    public ErrorResponse(String error, String message, List<FieldError> details) {
        this(error, message, details, LocalDateTime.now());
    }

    /**
     * Field-level validation error.
     */
    public record FieldError(String field, String message) {}
}
