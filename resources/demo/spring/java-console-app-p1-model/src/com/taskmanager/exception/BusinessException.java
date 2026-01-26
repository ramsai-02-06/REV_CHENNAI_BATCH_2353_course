package com.taskmanager.exception;

/**
 * Base class for all business exceptions.
 *
 * Business exceptions represent violations of business rules or constraints.
 * They are thrown by the service layer and should be handled by the controller
 * or UI layer to provide user-friendly error messages.
 *
 * This class serves as the foundation for AOP-based exception handling,
 * where a single aspect can catch all BusinessException subclasses.
 */
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final String userMessage;

    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
        this.userMessage = message;
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = message;
    }

    public BusinessException(String errorCode, String message, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
        this.userMessage = message;
    }

    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = message;
    }

    public BusinessException(String errorCode, String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Get a user-friendly message suitable for display.
     */
    public String getUserMessage() {
        return userMessage;
    }
}
