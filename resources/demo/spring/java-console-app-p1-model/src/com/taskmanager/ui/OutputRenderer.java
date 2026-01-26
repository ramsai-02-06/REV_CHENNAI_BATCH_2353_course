package com.taskmanager.ui;

import com.taskmanager.exception.BusinessException;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.model.TaskView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Handles all output rendering in an object-oriented way.
 *
 * Responsibilities:
 * - Display task views in various formats
 * - Show success/error messages
 * - Render menus and prompts
 * - Format tables and lists
 *
 * This separates output concerns from business logic,
 * making it easy to:
 * - Change display format
 * - Add colors/styling
 * - Support different output targets (console, file, etc.)
 *
 * In a GUI, this would be replaced by view templates or components.
 * In a REST API, this would be replaced by JSON serialization.
 */
public class OutputRenderer {
    private static final Logger logger = LogManager.getLogger(OutputRenderer.class);

    // Box drawing characters for prettier output
    private static final String HORIZONTAL = "─";
    private static final String VERTICAL = "│";
    private static final String CORNER_TL = "┌";
    private static final String CORNER_TR = "┐";
    private static final String CORNER_BL = "└";
    private static final String CORNER_BR = "┘";
    private static final String TEE_LEFT = "├";
    private static final String TEE_RIGHT = "┤";

    public OutputRenderer() {
        logger.info("OutputRenderer initialized");
    }

    /**
     * Display the application header.
     */
    public void showHeader() {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║         TASK MANAGER - Console Application            ║");
        System.out.println("║                   (P1 Model Version)                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Display the main menu.
     */
    public void showMenu() {
        System.out.println();
        System.out.println(CORNER_TL + HORIZONTAL.repeat(25) + CORNER_TR);
        System.out.println(VERTICAL + "       MAIN MENU         " + VERTICAL);
        System.out.println(TEE_LEFT + HORIZONTAL.repeat(25) + TEE_RIGHT);
        System.out.println(VERTICAL + " 1. Create Task          " + VERTICAL);
        System.out.println(VERTICAL + " 2. List All Tasks       " + VERTICAL);
        System.out.println(VERTICAL + " 3. View Task Details    " + VERTICAL);
        System.out.println(VERTICAL + " 4. Update Task          " + VERTICAL);
        System.out.println(VERTICAL + " 5. Change Status        " + VERTICAL);
        System.out.println(VERTICAL + " 6. Delete Task          " + VERTICAL);
        System.out.println(VERTICAL + " 7. Filter by Status     " + VERTICAL);
        System.out.println(TEE_LEFT + HORIZONTAL.repeat(25) + TEE_RIGHT);
        System.out.println(VERTICAL + " 0. Exit                 " + VERTICAL);
        System.out.println(CORNER_BL + HORIZONTAL.repeat(25) + CORNER_BR);
        System.out.print("Choose option: ");
    }

    /**
     * Display a list of tasks in table format.
     */
    public void showTaskList(List<TaskView> tasks, String title) {
        logger.debug("Rendering task list: {} items", tasks.size());

        System.out.println();
        if (tasks.isEmpty()) {
            showInfo("No tasks found.");
            return;
        }

        int width = 70;
        System.out.println(CORNER_TL + HORIZONTAL.repeat(width) + CORNER_TR);
        System.out.println(VERTICAL + centerText(title, width) + VERTICAL);
        System.out.println(TEE_LEFT + HORIZONTAL.repeat(width) + TEE_RIGHT);

        // Header row
        System.out.printf(VERTICAL + " %-5s " + VERTICAL + " %-30s " + VERTICAL + " %-12s " + VERTICAL + " %-15s " + VERTICAL + "%n",
                "ID", "Title", "Status", "Created");
        System.out.println(TEE_LEFT + HORIZONTAL.repeat(width) + TEE_RIGHT);

        // Data rows
        for (TaskView task : tasks) {
            System.out.printf(VERTICAL + " %-5d " + VERTICAL + " %-30s " + VERTICAL + " %-12s " + VERTICAL + " %-15s " + VERTICAL + "%n",
                    task.getId(),
                    truncate(task.getTitle(), 30),
                    task.getStatusDisplay(),
                    truncate(task.getCreatedAt(), 15));
        }

        System.out.println(CORNER_BL + HORIZONTAL.repeat(width) + CORNER_BR);
        System.out.println("Total: " + tasks.size() + " task(s)");
    }

    /**
     * Display a single task with full details.
     */
    public void showTaskDetails(TaskView task) {
        logger.debug("Rendering task details: id={}", task.getId());

        if (task.getDetailedView() != null) {
            System.out.println(task.getDetailedView());
        } else {
            showTaskCard(task);
        }
    }

    /**
     * Display task as a card format.
     */
    private void showTaskCard(TaskView task) {
        int width = 56;
        System.out.println();
        System.out.println(CORNER_TL + HORIZONTAL.repeat(width) + CORNER_TR);
        System.out.println(VERTICAL + centerText("TASK #" + task.getId(), width) + VERTICAL);
        System.out.println(TEE_LEFT + HORIZONTAL.repeat(width) + TEE_RIGHT);
        System.out.printf(VERTICAL + " Title:       %-43s " + VERTICAL + "%n", truncate(task.getTitle(), 43));
        System.out.printf(VERTICAL + " Status:      %-43s " + VERTICAL + "%n", task.getStatusDisplay());
        System.out.printf(VERTICAL + " Created:     %-43s " + VERTICAL + "%n", task.getCreatedAt());
        System.out.println(TEE_LEFT + HORIZONTAL.repeat(width) + TEE_RIGHT);
        System.out.println(VERTICAL + " Description:                                          " + VERTICAL);

        String desc = task.getDescription() != null ? task.getDescription() : "(No description)";
        for (String line : wordWrap(desc, 54)) {
            System.out.printf(VERTICAL + " %-54s " + VERTICAL + "%n", line);
        }

        System.out.println(CORNER_BL + HORIZONTAL.repeat(width) + CORNER_BR);
    }

    /**
     * Display a success message.
     */
    public void showSuccess(String message) {
        logger.debug("Showing success: {}", message);
        System.out.println();
        System.out.println("[SUCCESS] " + message);
    }

    /**
     * Display an informational message.
     */
    public void showInfo(String message) {
        logger.debug("Showing info: {}", message);
        System.out.println();
        System.out.println("[INFO] " + message);
    }

    /**
     * Display an error from a BusinessException.
     */
    public void showError(BusinessException e) {
        logger.debug("Showing business error: {} - {}", e.getErrorCode(), e.getUserMessage());
        System.out.println();
        System.out.println("[ERROR] " + e.getUserMessage());
    }

    /**
     * Display validation errors.
     */
    public void showValidationErrors(ValidationException e) {
        logger.debug("Showing validation errors: {} error(s)", e.getErrors().size());
        System.out.println();
        System.out.println("[VALIDATION ERROR]");
        for (ValidationException.ValidationError error : e.getErrors()) {
            System.out.println("  - " + error.getField() + ": " + error.getMessage());
        }
    }

    /**
     * Display a generic error message.
     */
    public void showError(String message) {
        logger.debug("Showing error: {}", message);
        System.out.println();
        System.out.println("[ERROR] " + message);
    }

    /**
     * Display a newly created task.
     */
    public void showCreatedTask(TaskView task) {
        showSuccess("Task created successfully!");
        System.out.println("  ID: " + task.getId());
        System.out.println("  Title: " + task.getTitle());
        System.out.println("  Status: " + task.getStatusDisplay());
    }

    /**
     * Display an updated task.
     */
    public void showUpdatedTask(TaskView task) {
        showSuccess("Task updated successfully!");
        System.out.println("  " + task.getSummary());
    }

    /**
     * Display goodbye message.
     */
    public void showGoodbye() {
        System.out.println();
        System.out.println("Thank you for using Task Manager. Goodbye!");
        System.out.println();
    }

    // ==================== Utility Methods ====================

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
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
