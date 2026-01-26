package com.taskmanager.controller;

import com.taskmanager.exception.BusinessException;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskInput;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.model.TaskView;
import com.taskmanager.service.TaskService;
import com.taskmanager.validation.InputValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller layer that sits between UI and Service.
 *
 * Responsibilities:
 * 1. Convert raw input (TaskInput) to domain model (Task)
 * 2. Validate input using InputValidator
 * 3. Call service methods
 * 4. Convert domain model (Task) to view model (TaskView)
 * 5. Format responses for the UI
 *
 * This pattern mirrors Spring MVC @RestController behavior:
 * - @RequestBody -> TaskInput
 * - Service call -> TaskService methods
 * - Return value conversion -> TaskView
 *
 * FUTURE AOP ENHANCEMENT:
 * Controller could be enhanced with aspects for:
 * - @ExceptionHandler equivalent for unified error responses
 * - Request/response logging
 * - Input sanitization
 * - Rate limiting
 *
 * Example:
 * @Around("execution(* com.taskmanager.controller.*.*(..))")
 * Would automatically:
 * - Log request/response
 * - Handle exceptions uniformly
 * - Measure response time
 */
public class TaskController {
    private static final Logger logger = LogManager.getLogger(TaskController.class);

    private final TaskService taskService;
    private final InputValidator inputValidator;

    public TaskController(TaskService taskService, InputValidator inputValidator) {
        this.taskService = taskService;
        this.inputValidator = inputValidator;
        logger.info("TaskController initialized");
    }

    /**
     * Create a new task from input.
     *
     * Flow: TaskInput -> validate -> Task -> service.create -> Task -> TaskView
     *
     * @param input Raw input from UI
     * @return Formatted view model for display
     * @throws ValidationException if input is invalid
     * @throws BusinessException if business rules are violated
     */
    public TaskView createTask(TaskInput input) {
        logger.info("Controller: createTask called with input: {}", input);

        // Step 1: Validate input
        inputValidator.validateForCreate(input);
        logger.debug("Input validation passed");

        // Step 2: Convert input to domain model
        Task task = convertInputToTask(input);
        logger.debug("Converted to domain model: {}", task);

        // Step 3: Call service
        Task createdTask = taskService.createTask(task);
        logger.debug("Service returned: {}", createdTask);

        // Step 4: Convert to view model and return
        TaskView view = convertTaskToView(createdTask);
        logger.info("Controller: createTask completed - id={}", view.getId());

        return view;
    }

    /**
     * Get all tasks as formatted views.
     *
     * @return List of task views
     */
    public List<TaskView> getAllTasks() {
        logger.info("Controller: getAllTasks called");

        List<Task> tasks = taskService.getAllTasks();

        List<TaskView> views = tasks.stream()
                .map(this::convertTaskToView)
                .collect(Collectors.toList());

        logger.info("Controller: getAllTasks returning {} items", views.size());
        return views;
    }

    /**
     * Get a single task by ID.
     *
     * @param id Task ID (already validated)
     * @return Task view
     */
    public TaskView getTaskById(Long id) {
        logger.info("Controller: getTaskById called with id={}", id);

        Task task = taskService.getTaskById(id);
        TaskView view = convertTaskToDetailedView(task);

        logger.info("Controller: getTaskById returning task: {}", view.getTitle());
        return view;
    }

    /**
     * Update a task from input.
     *
     * @param input Input containing id and fields to update
     * @return Updated task view
     */
    public TaskView updateTask(TaskInput input) {
        logger.info("Controller: updateTask called with input: {}", input);

        // Validate for update (id required, other fields optional)
        inputValidator.validateForUpdate(input);

        // Get title and description (may be null)
        String title = input.getTitle();
        String description = input.getDescription();

        // Clean empty strings to null
        if (title != null && title.trim().isEmpty()) {
            title = null;
        }
        if (description != null && description.trim().isEmpty()) {
            description = null;
        }

        Task updatedTask = taskService.updateTask(input.getId(), title, description);
        TaskView view = convertTaskToView(updatedTask);

        logger.info("Controller: updateTask completed for id={}", input.getId());
        return view;
    }

    /**
     * Update task status.
     *
     * @param id Task ID
     * @param statusString Status as string
     * @return Updated task view
     */
    public TaskView updateTaskStatus(Long id, String statusString) {
        logger.info("Controller: updateTaskStatus called - id={}, status={}", id, statusString);

        // Parse and validate status
        TaskStatus status;
        try {
            status = TaskStatus.fromString(statusString);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("status", e.getMessage());
        }

        Task updatedTask = taskService.updateTaskStatus(id, status);
        TaskView view = convertTaskToView(updatedTask);

        logger.info("Controller: updateTaskStatus completed - id={}, newStatus={}", id, status);
        return view;
    }

    /**
     * Delete a task by ID.
     *
     * @param id Task ID
     * @return Confirmation message
     */
    public String deleteTask(Long id) {
        logger.info("Controller: deleteTask called with id={}", id);

        // Get task info before deleting (for confirmation message)
        Task task = taskService.getTaskById(id);
        String title = task.getTitle();

        taskService.deleteTask(id);

        String message = String.format("Task '%s' (ID: %d) has been deleted.", title, id);
        logger.info("Controller: deleteTask completed - {}", message);

        return message;
    }

    /**
     * Get tasks filtered by status.
     *
     * @param statusString Status to filter by
     * @return List of matching task views
     */
    public List<TaskView> getTasksByStatus(String statusString) {
        logger.info("Controller: getTasksByStatus called with status={}", statusString);

        TaskStatus status;
        try {
            status = TaskStatus.fromString(statusString);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("status", e.getMessage());
        }

        List<Task> tasks = taskService.getTasksByStatus(status);

        List<TaskView> views = tasks.stream()
                .map(this::convertTaskToView)
                .collect(Collectors.toList());

        logger.info("Controller: getTasksByStatus returning {} items", views.size());
        return views;
    }

    /**
     * Mark task as started.
     */
    public TaskView startTask(Long id) {
        logger.info("Controller: startTask called with id={}", id);
        Task task = taskService.startTask(id);
        return convertTaskToView(task);
    }

    /**
     * Mark task as completed.
     */
    public TaskView completeTask(Long id) {
        logger.info("Controller: completeTask called with id={}", id);
        Task task = taskService.completeTask(id);
        return convertTaskToView(task);
    }

    // ==================== Conversion Methods ====================

    /**
     * Convert TaskInput to Task domain model.
     */
    private Task convertInputToTask(TaskInput input) {
        Task task = new Task();

        if (input.getId() != null) {
            task.setId(input.getId());
        }

        if (input.getTitle() != null) {
            task.setTitle(inputValidator.sanitize(input.getTitle()));
        }

        if (input.getDescription() != null) {
            task.setDescription(inputValidator.sanitize(input.getDescription()));
        }

        if (input.getStatus() != null) {
            task.setStatus(TaskStatus.fromString(input.getStatus()));
        }

        return task;
    }

    /**
     * Convert Task to TaskView for list display (summary format).
     */
    private TaskView convertTaskToView(Task task) {
        String summary = formatSummary(task);

        return TaskView.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .statusDisplay(task.getStatus().getDisplayName())
                .createdAt(task.getFormattedCreatedAt())
                .summary(summary)
                .detailedView(null)  // Not needed for list view
                .build();
    }

    /**
     * Convert Task to TaskView for detailed display.
     */
    private TaskView convertTaskToDetailedView(Task task) {
        String summary = formatSummary(task);
        String detailed = formatDetailedView(task);

        return TaskView.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .statusDisplay(task.getStatus().getDisplayName())
                .createdAt(task.getFormattedCreatedAt())
                .summary(summary)
                .detailedView(detailed)
                .build();
    }

    // ==================== Formatting Methods ====================

    /**
     * Format task as a one-line summary.
     */
    private String formatSummary(Task task) {
        return String.format("[%d] %s - %s (%s)",
                task.getId(),
                task.getTitle(),
                truncate(task.getDescription(), 30),
                task.getStatus().getDisplayName());
    }

    /**
     * Format task as detailed multi-line view.
     */
    private String formatDetailedView(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║  Task #%-47d  ║%n", task.getId()));
        sb.append("╠══════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Title:       %-40s ║%n", truncate(task.getTitle(), 40)));
        sb.append(String.format("║  Status:      %-40s ║%n", task.getStatus().getDisplayName()));
        sb.append(String.format("║  Created:     %-40s ║%n", task.getFormattedCreatedAt()));
        sb.append("╠══════════════════════════════════════════════════════╣\n");
        sb.append("║  Description:                                        ║\n");

        // Word-wrap description
        String desc = task.getDescription() != null ? task.getDescription() : "(No description)";
        for (String line : wordWrap(desc, 52)) {
            sb.append(String.format("║  %-52s  ║%n", line));
        }

        sb.append("╚══════════════════════════════════════════════════════╝");
        return sb.toString();
    }

    /**
     * Format a list of task views as a table.
     */
    public String formatTaskList(List<TaskView> tasks, String header) {
        if (tasks.isEmpty()) {
            return "No tasks found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(header).append("\n");
        sb.append("─".repeat(60)).append("\n");
        sb.append(String.format("%-5s | %-30s | %-12s | %s%n",
                "ID", "Title", "Status", "Created"));
        sb.append("─".repeat(60)).append("\n");

        for (TaskView task : tasks) {
            sb.append(String.format("%-5d | %-30s | %-12s | %s%n",
                    task.getId(),
                    truncate(task.getTitle(), 30),
                    task.getStatusDisplay(),
                    truncate(task.getCreatedAt(), 16)));
        }

        sb.append("─".repeat(60)).append("\n");
        sb.append("Total: ").append(tasks.size()).append(" task(s)\n");

        return sb.toString();
    }

    // ==================== Utility Methods ====================

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    private String[] wordWrap(String text, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return new String[]{""};
        }

        java.util.List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
                // Handle words longer than maxWidth
                while (word.length() > maxWidth) {
                    lines.add(word.substring(0, maxWidth));
                    word = word.substring(maxWidth);
                }
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }
}
