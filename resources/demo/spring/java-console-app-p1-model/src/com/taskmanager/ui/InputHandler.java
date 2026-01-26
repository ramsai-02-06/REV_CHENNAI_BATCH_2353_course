package com.taskmanager.ui;

import com.taskmanager.model.TaskInput;
import com.taskmanager.validation.InputValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Handles all user input in an object-oriented way.
 *
 * Responsibilities:
 * - Read raw input from console
 * - Create model objects (TaskInput) from user responses
 * - Provide consistent prompting behavior
 *
 * This separates input concerns from the main UI logic,
 * making the code more testable and maintainable.
 *
 * In a GUI or web application, this would be replaced by
 * form binding or request body parsing.
 */
public class InputHandler {
    private static final Logger logger = LogManager.getLogger(InputHandler.class);

    private final Scanner scanner;
    private final InputValidator validator;

    public InputHandler(Scanner scanner, InputValidator validator) {
        this.scanner = scanner;
        this.validator = validator;
        logger.info("InputHandler initialized");
    }

    /**
     * Read a string with a prompt.
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        logger.debug("Read string input: length={}", input.length());
        return input;
    }

    /**
     * Read a required string (keeps prompting until non-empty).
     */
    public String readRequiredString(String prompt, String fieldName) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                logger.debug("Read required string '{}': length={}", fieldName, input.length());
                return input;
            }

            System.out.println("Error: " + fieldName + " is required. Please try again.");
            logger.debug("Empty input rejected for required field: {}", fieldName);
        }
    }

    /**
     * Read an optional string (empty string allowed).
     */
    public String readOptionalString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input.trim().isEmpty() ? null : input.trim();
    }

    /**
     * Read a task ID with validation.
     */
    public Long readTaskId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                Long id = validator.validateAndParseId(input);
                logger.debug("Read task ID: {}", id);
                return id;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + " Please try again.");
                logger.debug("Invalid ID input: '{}'", input);
            }
        }
    }

    /**
     * Read a menu choice (single character/number).
     */
    public String readMenuChoice() {
        return scanner.nextLine().trim();
    }

    /**
     * Read confirmation (y/n).
     */
    public boolean readConfirmation(String prompt) {
        System.out.print(prompt + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        boolean confirmed = input.equals("y") || input.equals("yes");
        logger.debug("Confirmation response: {} -> {}", input, confirmed);
        return confirmed;
    }

    /**
     * Read a status choice from menu.
     */
    public String readStatusChoice() {
        System.out.println("Select status:");
        System.out.println("  1. PENDING");
        System.out.println("  2. IN_PROGRESS");
        System.out.println("  3. COMPLETED");
        System.out.println("  4. CANCELLED");
        System.out.print("Choice (1-4): ");

        String choice = scanner.nextLine().trim();
        logger.debug("Status choice input: {}", choice);

        return switch (choice) {
            case "1" -> "PENDING";
            case "2" -> "IN_PROGRESS";
            case "3" -> "COMPLETED";
            case "4" -> "CANCELLED";
            default -> throw new IllegalArgumentException("Invalid choice. Please enter 1-4.");
        };
    }

    // ==================== TaskInput Builders ====================

    /**
     * Collect input for creating a new task.
     *
     * @return TaskInput with title and description
     */
    public TaskInput collectCreateTaskInput() {
        logger.info("Collecting input for new task");

        String title = readRequiredString("Enter title: ", "Title");
        String description = readOptionalString("Enter description (optional): ");

        TaskInput input = new TaskInput(title, description);
        logger.debug("Created TaskInput: {}", input);

        return input;
    }

    /**
     * Collect input for updating an existing task.
     *
     * @return TaskInput with id and optional title/description
     */
    public TaskInput collectUpdateTaskInput() {
        logger.info("Collecting input for task update");

        Long id = readTaskId("Enter task ID to update: ");

        System.out.println("(Press Enter to skip a field and keep existing value)");
        String title = readOptionalString("New title: ");
        String description = readOptionalString("New description: ");

        TaskInput input = new TaskInput()
                .withId(id)
                .withTitle(title)
                .withDescription(description);

        logger.debug("Created update TaskInput: {}", input);
        return input;
    }

    /**
     * Collect input for changing task status.
     *
     * @return TaskInput with id and status
     */
    public TaskInput collectStatusChangeInput() {
        logger.info("Collecting input for status change");

        Long id = readTaskId("Enter task ID: ");

        String status;
        while (true) {
            try {
                status = readStatusChoice();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        TaskInput input = new TaskInput()
                .withId(id)
                .withStatus(status);

        logger.debug("Created status change TaskInput: {}", input);
        return input;
    }

    /**
     * Collect task ID for viewing or deleting.
     */
    public Long collectTaskId(String action) {
        logger.info("Collecting task ID for {}", action);
        return readTaskId("Enter task ID to " + action + ": ");
    }

    /**
     * Collect status for filtering.
     */
    public String collectStatusFilter() {
        logger.info("Collecting status filter");
        while (true) {
            try {
                return readStatusChoice();
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Close the scanner when done.
     */
    public void close() {
        scanner.close();
        logger.info("InputHandler closed");
    }
}
