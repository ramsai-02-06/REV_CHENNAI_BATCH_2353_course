package com.taskmanager.service;

import com.taskmanager.exception.BusinessException;
import com.taskmanager.exception.DataAccessException;
import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Business logic layer for Task operations.
 *
 * This service layer:
 * 1. Contains business rules and validation logic
 * 2. Translates repository exceptions to business exceptions
 * 3. Logs business operations for audit trails
 *
 * EXCEPTION TRANSLATION:
 * - DataAccessException with unique constraint violation -> DuplicateTaskException
 * - Task not found in repository -> TaskNotFoundException
 *
 * FUTURE AOP ENHANCEMENT:
 * This service could be enhanced with aspects for:
 * - @Transactional behavior (automatic commit/rollback)
 * - @Cacheable for frequently accessed tasks
 * - @Secured for role-based access control
 * - Automatic exception translation
 *
 * Example AOP for exception translation:
 * @AfterThrowing(pointcut = "execution(* com.taskmanager.service.*.*(..))",
 *                throwing = "ex")
 * public void translateException(DataAccessException ex) {
 *     if (ex.isUniqueConstraintViolation()) {
 *         throw new DuplicateTaskException(...);
 *     }
 * }
 */
public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        logger.info("TaskService initialized");
    }

    /**
     * Create a new task.
     *
     * @param task The task to create (id should be null)
     * @return The created task with generated id
     * @throws DuplicateTaskException if a task with the same title exists
     * @throws BusinessException for other business rule violations
     */
    public Task createTask(Task task) {
        logger.info("Creating task with title: '{}'", task.getTitle());

        // Business rule: title must be unique
        // Note: This is also enforced at DB level, but checking here
        // provides a better error message before hitting the database
        if (taskRepository.existsByTitle(task.getTitle())) {
            logger.warn("Duplicate task title detected: '{}'", task.getTitle());
            throw new DuplicateTaskException(task.getTitle());
        }

        try {
            Task savedTask = taskRepository.save(task);
            logger.info("Task created successfully: id={}, title='{}'",
                    savedTask.getId(), savedTask.getTitle());
            return savedTask;

        } catch (DataAccessException e) {
            // Translate SQL constraint violation to business exception
            if (e.isUniqueConstraintViolation()) {
                logger.warn("SQL unique constraint violation for title: '{}'", task.getTitle());
                throw new DuplicateTaskException(task.getTitle(), e);
            }
            // Re-throw other data access errors
            logger.error("Failed to create task: {}", e.getMessage());
            throw new BusinessException("Failed to create task", e);
        }
    }

    /**
     * Get all tasks.
     *
     * @return List of all tasks, ordered by creation date (newest first)
     */
    public List<Task> getAllTasks() {
        logger.debug("Retrieving all tasks");

        try {
            List<Task> tasks = taskRepository.findAll();
            logger.info("Retrieved {} tasks", tasks.size());
            return tasks;

        } catch (DataAccessException e) {
            logger.error("Failed to retrieve tasks: {}", e.getMessage());
            throw new BusinessException("Failed to retrieve tasks", e);
        }
    }

    /**
     * Get a task by ID.
     *
     * @param id The task ID
     * @return The task
     * @throws TaskNotFoundException if task doesn't exist
     */
    public Task getTaskById(Long id) {
        logger.debug("Retrieving task with id: {}", id);

        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Task not found: id={}", id);
                        return new TaskNotFoundException(id);
                    });

            logger.debug("Found task: id={}, title='{}'", task.getId(), task.getTitle());
            return task;

        } catch (TaskNotFoundException e) {
            throw e;  // Re-throw business exceptions as-is
        } catch (DataAccessException e) {
            logger.error("Failed to retrieve task {}: {}", id, e.getMessage());
            throw new BusinessException("Failed to retrieve task", e);
        }
    }

    /**
     * Update an existing task.
     *
     * @param id The task ID
     * @param title New title (or null to keep existing)
     * @param description New description (or null to keep existing)
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     * @throws DuplicateTaskException if new title conflicts with existing task
     */
    public Task updateTask(Long id, String title, String description) {
        logger.info("Updating task id={}: title='{}', description length={}",
                id, title, description != null ? description.length() : 0);

        // First, find the existing task
        Task task = getTaskById(id);

        // If changing title, check for duplicates
        if (title != null && !title.equals(task.getTitle())) {
            if (taskRepository.existsByTitle(title)) {
                logger.warn("Cannot update: duplicate title '{}'", title);
                throw new DuplicateTaskException(title);
            }
            task.setTitle(title);
        }

        if (description != null) {
            task.setDescription(description);
        }

        try {
            Task updatedTask = taskRepository.update(task);
            logger.info("Task updated successfully: id={}", id);
            return updatedTask;

        } catch (DataAccessException e) {
            if (e.isUniqueConstraintViolation()) {
                throw new DuplicateTaskException(title, e);
            }
            logger.error("Failed to update task {}: {}", id, e.getMessage());
            throw new BusinessException("Failed to update task", e);
        }
    }

    /**
     * Update task status.
     *
     * @param id The task ID
     * @param newStatus The new status
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        logger.info("Updating task {} status to {}", id, newStatus);

        Task task = getTaskById(id);
        TaskStatus oldStatus = task.getStatus();

        // Business rule: validate status transitions (example)
        validateStatusTransition(oldStatus, newStatus);

        task.setStatus(newStatus);

        try {
            Task updatedTask = taskRepository.update(task);
            logger.info("Task {} status changed: {} -> {}", id, oldStatus, newStatus);
            return updatedTask;

        } catch (DataAccessException e) {
            logger.error("Failed to update task {} status: {}", id, e.getMessage());
            throw new BusinessException("Failed to update task status", e);
        }
    }

    /**
     * Delete a task.
     *
     * @param id The task ID to delete
     * @throws TaskNotFoundException if task doesn't exist
     */
    public void deleteTask(Long id) {
        logger.info("Deleting task with id: {}", id);

        // Verify task exists before deleting
        Task task = getTaskById(id);

        try {
            boolean deleted = taskRepository.deleteById(id);
            if (deleted) {
                logger.info("Task deleted: id={}, title='{}'", id, task.getTitle());
            } else {
                logger.warn("Task {} was not deleted (may have been already removed)", id);
            }

        } catch (DataAccessException e) {
            logger.error("Failed to delete task {}: {}", id, e.getMessage());
            throw new BusinessException("Failed to delete task", e);
        }
    }

    /**
     * Get tasks by status.
     *
     * @param status The status to filter by
     * @return List of tasks with the given status
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        logger.debug("Retrieving tasks with status: {}", status);

        try {
            List<Task> tasks = taskRepository.findByStatus(status);
            logger.info("Retrieved {} tasks with status {}", tasks.size(), status);
            return tasks;

        } catch (DataAccessException e) {
            logger.error("Failed to retrieve tasks by status {}: {}", status, e.getMessage());
            throw new BusinessException("Failed to retrieve tasks by status", e);
        }
    }

    /**
     * Mark a task as completed.
     */
    public Task completeTask(Long id) {
        return updateTaskStatus(id, TaskStatus.COMPLETED);
    }

    /**
     * Start working on a task.
     */
    public Task startTask(Long id) {
        return updateTaskStatus(id, TaskStatus.IN_PROGRESS);
    }

    /**
     * Validate status transitions.
     *
     * Business rule example: COMPLETED tasks cannot be set back to PENDING
     */
    private void validateStatusTransition(TaskStatus from, TaskStatus to) {
        // Example business rule: completed tasks cannot go back to pending
        if (from == TaskStatus.COMPLETED && to == TaskStatus.PENDING) {
            logger.warn("Invalid status transition: {} -> {}", from, to);
            throw new BusinessException("INVALID_TRANSITION",
                    "Cannot change a completed task back to pending");
        }

        // Example: cancelled tasks can only be reopened as pending
        if (from == TaskStatus.CANCELLED && to != TaskStatus.PENDING && to != TaskStatus.CANCELLED) {
            logger.warn("Invalid status transition: {} -> {}", from, to);
            throw new BusinessException("INVALID_TRANSITION",
                    "Cancelled tasks can only be reopened as pending");
        }

        logger.debug("Status transition validated: {} -> {}", from, to);
    }
}
