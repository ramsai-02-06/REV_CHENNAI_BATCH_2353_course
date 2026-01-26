package com.taskmanager.exception;

/**
 * Exception thrown when attempting to create a task with a duplicate title.
 *
 * This exception is generated when a SQL UNIQUE constraint violation occurs
 * on the task title column. The repository layer catches the SQLException
 * and the service layer translates it to this business exception.
 *
 * SQL Error Codes that trigger this exception:
 * - MySQL: 1062 (Duplicate entry for key)
 * - PostgreSQL: 23505 (unique_violation)
 * - H2: 23505 (Unique index or primary key violation)
 */
public class DuplicateTaskException extends BusinessException {
    private static final String ERROR_CODE = "DUPLICATE_TASK";
    private final String duplicateTitle;

    public DuplicateTaskException(String title) {
        super(ERROR_CODE,
              "A task with title '" + title + "' already exists",
              "Cannot create task: A task with this title already exists. Please use a different title.");
        this.duplicateTitle = title;
    }

    public DuplicateTaskException(String title, Throwable cause) {
        super(ERROR_CODE,
              "A task with title '" + title + "' already exists",
              "Cannot create task: A task with this title already exists. Please use a different title.",
              cause);
        this.duplicateTitle = title;
    }

    public String getDuplicateTitle() {
        return duplicateTitle;
    }
}
