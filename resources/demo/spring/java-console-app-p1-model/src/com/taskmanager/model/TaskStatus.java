package com.taskmanager.model;

/**
 * Enum representing the possible states of a Task.
 */
public enum TaskStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Parse status from string (case-insensitive).
     */
    public static TaskStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        String normalized = value.trim().toUpperCase().replace(" ", "_");
        try {
            return TaskStatus.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + value +
                ". Valid values are: PENDING, IN_PROGRESS, COMPLETED, CANCELLED");
        }
    }
}
