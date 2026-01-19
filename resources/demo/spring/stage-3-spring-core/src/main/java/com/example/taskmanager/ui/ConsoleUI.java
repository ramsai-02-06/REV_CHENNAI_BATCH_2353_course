package com.example.taskmanager.ui;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console-based user interface.
 *
 * NEW IN STAGE 3:
 * - @Component marks this as a Spring-managed bean
 * - @Autowired injects TaskService automatically
 */
@Component
public class ConsoleUI {

    private final TaskService taskService;
    private final Scanner scanner;

    @Autowired
    public ConsoleUI(TaskService taskService) {
        this.taskService = taskService;
        this.scanner = new Scanner(System.in);
        System.out.println("ConsoleUI initialized by Spring");
    }

    public void run() {
        System.out.println("\n=================================");
        System.out.println("   Task Manager - Spring Core");
        System.out.println("=================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createTask();
                    case "2" -> listAllTasks();
                    case "3" -> viewTask();
                    case "4" -> updateTask();
                    case "5" -> changeStatus();
                    case "6" -> deleteTask();
                    case "7" -> listByStatus();
                    case "0" -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Create Task");
        System.out.println("2. List All Tasks");
        System.out.println("3. View Task");
        System.out.println("4. Update Task");
        System.out.println("5. Change Status");
        System.out.println("6. Delete Task");
        System.out.println("7. List by Status");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    private void createTask() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        Task task = taskService.createTask(title, description);
        System.out.println("Task created: " + task);
    }

    private void listAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println("\n--- All Tasks ---");
        tasks.forEach(System.out::println);
    }

    private void viewTask() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<Task> task = taskService.getTaskById(id);
        task.ifPresentOrElse(
                t -> {
                    System.out.println("\n--- Task Details ---");
                    System.out.println("ID: " + t.getId());
                    System.out.println("Title: " + t.getTitle());
                    System.out.println("Description: " + t.getDescription());
                    System.out.println("Status: " + t.getStatus());
                    System.out.println("Created: " + t.getCreatedAt());
                },
                () -> System.out.println("Task not found.")
        );
    }

    private void updateTask() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("New title (Enter to skip): ");
        String title = scanner.nextLine();
        System.out.print("New description (Enter to skip): ");
        String description = scanner.nextLine();

        Task task = taskService.updateTask(id,
                title.isEmpty() ? null : title,
                description.isEmpty() ? null : description);
        System.out.println("Task updated: " + task);
    }

    private void changeStatus() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.println("1. PENDING  2. IN_PROGRESS  3. COMPLETED  4. CANCELLED");
        System.out.print("Choice: ");

        TaskStatus status = switch (scanner.nextLine()) {
            case "1" -> TaskStatus.PENDING;
            case "2" -> TaskStatus.IN_PROGRESS;
            case "3" -> TaskStatus.COMPLETED;
            case "4" -> TaskStatus.CANCELLED;
            default -> throw new IllegalArgumentException("Invalid choice");
        };

        Task task = taskService.updateTaskStatus(id, status);
        System.out.println("Status updated: " + task);
    }

    private void deleteTask() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Confirm delete? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            taskService.deleteTask(id);
            System.out.println("Task deleted.");
        }
    }

    private void listByStatus() {
        System.out.println("1. PENDING  2. IN_PROGRESS  3. COMPLETED  4. CANCELLED");
        System.out.print("Choice: ");

        TaskStatus status = switch (scanner.nextLine()) {
            case "1" -> TaskStatus.PENDING;
            case "2" -> TaskStatus.IN_PROGRESS;
            case "3" -> TaskStatus.COMPLETED;
            case "4" -> TaskStatus.CANCELLED;
            default -> throw new IllegalArgumentException("Invalid choice");
        };

        List<Task> tasks = taskService.getTasksByStatus(status);
        if (tasks.isEmpty()) {
            System.out.println("No tasks with status: " + status);
        } else {
            tasks.forEach(System.out::println);
        }
    }
}
