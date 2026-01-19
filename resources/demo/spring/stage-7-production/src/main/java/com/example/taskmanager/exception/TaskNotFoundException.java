package com.example.taskmanager.exception;

/**
 * Custom exception for when a task is not found.
 *
 * Using custom exceptions allows:
 * - More specific error handling
 * - Better error messages
 * - Cleaner service code
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
