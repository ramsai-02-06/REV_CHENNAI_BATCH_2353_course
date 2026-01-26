package com.taskmanager.exception;

/**
 * Exception thrown when a database operation fails.
 *
 * This wraps SQL exceptions and provides a cleaner abstraction
 * for the service layer. It contains information about the
 * original SQL error for logging purposes.
 */
public class DataAccessException extends RuntimeException {
    private final String sqlState;
    private final int errorCode;

    public DataAccessException(String message) {
        super(message);
        this.sqlState = null;
        this.errorCode = 0;
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
        this.sqlState = null;
        this.errorCode = 0;
    }

    public DataAccessException(String message, String sqlState, int errorCode, Throwable cause) {
        super(message, cause);
        this.sqlState = sqlState;
        this.errorCode = errorCode;
    }

    public String getSqlState() {
        return sqlState;
    }

    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Check if this is a unique constraint violation.
     */
    public boolean isUniqueConstraintViolation() {
        // MySQL: 1062, SQLState: 23000
        // PostgreSQL: SQLState: 23505
        // H2: SQLState: 23505
        if (sqlState != null && sqlState.equals("23505")) {
            return true;
        }
        if (sqlState != null && sqlState.equals("23000") && errorCode == 1062) {
            return true;
        }
        return errorCode == 1062;
    }
}
