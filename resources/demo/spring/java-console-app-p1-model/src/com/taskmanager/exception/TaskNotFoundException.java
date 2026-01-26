package com.taskmanager.exception;

/**
 * Exception thrown when a requested task is not found.
 */
public class TaskNotFoundException extends BusinessException {
    private static final String ERROR_CODE = "TASK_NOT_FOUND";
    private final Long taskId;

    public TaskNotFoundException(Long taskId) {
        super(ERROR_CODE,
              "Task with ID " + taskId + " not found",
              "The requested task does not exist. It may have been deleted.");
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }
}
