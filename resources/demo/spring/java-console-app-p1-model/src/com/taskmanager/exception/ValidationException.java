package com.taskmanager.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exception thrown when input validation fails.
 *
 * This exception can hold multiple validation errors, allowing
 * all validation issues to be reported at once rather than
 * one at a time.
 *
 * Used by the validation layer to report REGEX pattern failures
 * and other input validation issues.
 */
public class ValidationException extends BusinessException {
    private static final String ERROR_CODE = "VALIDATION_ERROR";
    private final List<ValidationError> errors;

    public ValidationException(String message) {
        super(ERROR_CODE, message);
        this.errors = new ArrayList<>();
        this.errors.add(new ValidationError("input", message));
    }

    public ValidationException(String field, String message) {
        super(ERROR_CODE, message);
        this.errors = new ArrayList<>();
        this.errors.add(new ValidationError(field, message));
    }

    public ValidationException(List<ValidationError> errors) {
        super(ERROR_CODE, formatErrorMessage(errors));
        this.errors = new ArrayList<>(errors);
    }

    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public void addError(String field, String message) {
        errors.add(new ValidationError(field, message));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private static String formatErrorMessage(List<ValidationError> errors) {
        if (errors == null || errors.isEmpty()) {
            return "Validation failed";
        }
        if (errors.size() == 1) {
            return errors.get(0).getMessage();
        }
        StringBuilder sb = new StringBuilder("Validation failed:\n");
        for (ValidationError error : errors) {
            sb.append("  - ").append(error.getField()).append(": ")
              .append(error.getMessage()).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Represents a single validation error for a specific field.
     */
    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return field + ": " + message;
        }
    }
}
