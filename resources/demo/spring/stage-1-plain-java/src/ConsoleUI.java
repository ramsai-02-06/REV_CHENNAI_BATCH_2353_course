import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console-based user interface for the Task Manager.
 *
 * This provides a simple text menu to interact with tasks.
 * In Stage 6, this will be replaced by REST endpoints.
 */
public class ConsoleUI {
    private final TaskService taskService;
    private final Scanner scanner;

    public ConsoleUI(TaskService taskService) {
        this.taskService = taskService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Main application loop.
     */
    public void run() {
        System.out.println("=================================");
        System.out.println("   Task Manager - Plain Java");
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
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
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
        System.out.println("Task created successfully: " + task);
    }

    private void listAllTasks() {
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\n--- All Tasks ---");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    private void viewTask() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            System.out.println("\n--- Task Details ---");
            System.out.println("ID: " + task.get().getId());
            System.out.println("Title: " + task.get().getTitle());
            System.out.println("Description: " + task.get().getDescription());
            System.out.println("Status: " + task.get().getStatus());
            System.out.println("Created: " + task.get().getCreatedAt());
        } else {
            System.out.println("Task not found.");
        }
    }

    private void updateTask() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Enter new title (or press Enter to skip): ");
        String title = scanner.nextLine();

        System.out.print("Enter new description (or press Enter to skip): ");
        String description = scanner.nextLine();

        Task task = taskService.updateTask(id,
                title.isEmpty() ? null : title,
                description.isEmpty() ? null : description);

        System.out.println("Task updated: " + task);
    }

    private void changeStatus() {
        System.out.print("Enter task ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.println("Select new status:");
        System.out.println("1. PENDING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        TaskStatus status = switch (choice) {
            case "1" -> TaskStatus.PENDING;
            case "2" -> TaskStatus.IN_PROGRESS;
            case "3" -> TaskStatus.COMPLETED;
            case "4" -> TaskStatus.CANCELLED;
            default -> throw new IllegalArgumentException("Invalid status choice");
        };

        Task task = taskService.updateTaskStatus(id, status);
        System.out.println("Status updated: " + task);
    }

    private void deleteTask() {
        System.out.print("Enter task ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Are you sure? (y/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            taskService.deleteTask(id);
            System.out.println("Task deleted.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private void listByStatus() {
        System.out.println("Select status to filter:");
        System.out.println("1. PENDING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.println("4. CANCELLED");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        TaskStatus status = switch (choice) {
            case "1" -> TaskStatus.PENDING;
            case "2" -> TaskStatus.IN_PROGRESS;
            case "3" -> TaskStatus.COMPLETED;
            case "4" -> TaskStatus.CANCELLED;
            default -> throw new IllegalArgumentException("Invalid status choice");
        };

        List<Task> tasks = taskService.getTasksByStatus(status);

        if (tasks.isEmpty()) {
            System.out.println("No tasks with status: " + status);
            return;
        }

        System.out.println("\n--- Tasks with status: " + status + " ---");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
