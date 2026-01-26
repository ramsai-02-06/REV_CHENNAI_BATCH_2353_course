package com.taskmanager.ui;

import com.taskmanager.controller.TaskController;
import com.taskmanager.exception.BusinessException;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.model.TaskInput;
import com.taskmanager.model.TaskView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Console-based user interface for the Task Manager.
 *
 * This version applies OOP principles:
 * - InputHandler: Encapsulates all input operations and creates model objects
 * - OutputRenderer: Encapsulates all output formatting and display
 * - TaskController: Handles business logic interaction
 *
 * The ConsoleUI orchestrates the flow but delegates specific
 * responsibilities to specialized classes.
 *
 * ARCHITECTURE PATTERN:
 *
 *   User Input -> InputHandler -> TaskInput (model)
 *                                      |
 *                                      v
 *                              TaskController
 *                                      |
 *                                      v
 *                               TaskService
 *                                      |
 *                                      v
 *                              TaskView (model) <- OutputRenderer <- Display
 *
 * FUTURE EVOLUTION:
 * This same architecture maps directly to:
 * - Spring MVC: @Controller, @RequestBody, @ResponseBody
 * - CLI frameworks: Picocli, Spring Shell
 * - GUI: JavaFX with FXML controllers
 */
public class ConsoleUI {
    private static final Logger logger = LogManager.getLogger(ConsoleUI.class);

    private final TaskController controller;
    private final InputHandler inputHandler;
    private final OutputRenderer outputRenderer;

    public ConsoleUI(TaskController controller, InputHandler inputHandler, OutputRenderer outputRenderer) {
        this.controller = controller;
        this.inputHandler = inputHandler;
        this.outputRenderer = outputRenderer;
        logger.info("ConsoleUI initialized");
    }

    /**
     * Main application loop.
     */
    public void run() {
        logger.info("Starting ConsoleUI");
        outputRenderer.showHeader();

        boolean running = true;
        while (running) {
            outputRenderer.showMenu();
            String choice = inputHandler.readMenuChoice();

            logger.debug("User selected menu option: {}", choice);

            try {
                switch (choice) {
                    case "1" -> handleCreateTask();
                    case "2" -> handleListAllTasks();
                    case "3" -> handleViewTask();
                    case "4" -> handleUpdateTask();
                    case "5" -> handleChangeStatus();
                    case "6" -> handleDeleteTask();
                    case "7" -> handleFilterByStatus();
                    case "0" -> {
                        running = false;
                        outputRenderer.showGoodbye();
                    }
                    default -> outputRenderer.showError("Invalid option. Please choose 0-7.");
                }
            } catch (ValidationException e) {
                logger.warn("Validation error in operation: {}", e.getMessage());
                outputRenderer.showValidationErrors(e);
            } catch (BusinessException e) {
                logger.warn("Business error in operation: {} - {}", e.getErrorCode(), e.getMessage());
                outputRenderer.showError(e);
            } catch (Exception e) {
                logger.error("Unexpected error in operation", e);
                outputRenderer.showError("An unexpected error occurred: " + e.getMessage());
            }
        }

        inputHandler.close();
        logger.info("ConsoleUI shutdown complete");
    }

    /**
     * Handle create task operation.
     *
     * Flow:
     * 1. InputHandler collects input -> TaskInput
     * 2. Controller validates and creates -> TaskView
     * 3. OutputRenderer displays result
     */
    private void handleCreateTask() {
        logger.info("Handling: Create Task");

        // Collect input as model object
        TaskInput input = inputHandler.collectCreateTaskInput();

        // Controller processes and returns view
        TaskView created = controller.createTask(input);

        // Render the result
        outputRenderer.showCreatedTask(created);
    }

    /**
     * Handle list all tasks operation.
     */
    private void handleListAllTasks() {
        logger.info("Handling: List All Tasks");

        // Controller returns list of views
        List<TaskView> tasks = controller.getAllTasks();

        // Render the list
        outputRenderer.showTaskList(tasks, "ALL TASKS");
    }

    /**
     * Handle view single task operation.
     */
    private void handleViewTask() {
        logger.info("Handling: View Task");

        // Collect task ID
        Long id = inputHandler.collectTaskId("view");

        // Controller returns detailed view
        TaskView task = controller.getTaskById(id);

        // Render details
        outputRenderer.showTaskDetails(task);
    }

    /**
     * Handle update task operation.
     */
    private void handleUpdateTask() {
        logger.info("Handling: Update Task");

        // Collect update input as model
        TaskInput input = inputHandler.collectUpdateTaskInput();

        // Check if anything to update
        if (input.getTitle() == null && input.getDescription() == null) {
            outputRenderer.showInfo("No changes specified. Task not modified.");
            return;
        }

        // Controller processes update
        TaskView updated = controller.updateTask(input);

        // Render result
        outputRenderer.showUpdatedTask(updated);
    }

    /**
     * Handle change status operation.
     */
    private void handleChangeStatus() {
        logger.info("Handling: Change Status");

        // Collect status change input
        TaskInput input = inputHandler.collectStatusChangeInput();

        // Controller processes status change
        TaskView updated = controller.updateTaskStatus(input.getId(), input.getStatus());

        // Render result
        outputRenderer.showSuccess("Status updated to: " + updated.getStatusDisplay());
        outputRenderer.showUpdatedTask(updated);
    }

    /**
     * Handle delete task operation.
     */
    private void handleDeleteTask() {
        logger.info("Handling: Delete Task");

        // Collect task ID
        Long id = inputHandler.collectTaskId("delete");

        // Confirm deletion
        if (!inputHandler.readConfirmation("Are you sure you want to delete this task?")) {
            outputRenderer.showInfo("Delete operation cancelled.");
            return;
        }

        // Controller processes deletion
        String message = controller.deleteTask(id);

        // Render result
        outputRenderer.showSuccess(message);
    }

    /**
     * Handle filter by status operation.
     */
    private void handleFilterByStatus() {
        logger.info("Handling: Filter by Status");

        // Collect status filter
        String status = inputHandler.collectStatusFilter();

        // Controller returns filtered list
        List<TaskView> tasks = controller.getTasksByStatus(status);

        // Render filtered list
        outputRenderer.showTaskList(tasks, "TASKS - " + status);
    }
}
