import java.util.List;
import java.util.Optional;

/**
 * Business logic layer for Task operations.
 *
 * This class contains the business rules and orchestrates repository calls.
 * It stays relatively clean across all stages - the service layer pattern
 * doesn't change much with Spring.
 *
 * PAIN POINT: The dependency (TaskRepository) must be passed in via constructor.
 * The caller (Main.java) is responsible for creating the repository first.
 * If TaskRepository needs other dependencies, Main.java must create those too.
 */
public class TaskService {
    private final TaskRepository taskRepository;

    // Constructor injection - we do this manually now
    // Spring will automate this in Stage 3
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Create a new task.
     */
    public Task createTask(String title, String description) {
        // Business validation
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        Task task = new Task(title, description);
        return taskRepository.save(task);
    }

    /**
     * Get all tasks.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Get a task by ID.
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Update task status.
     */
    public Task updateTaskStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        task.setStatus(newStatus);
        return taskRepository.update(task);
    }

    /**
     * Update task details.
     */
    public Task updateTask(Long id, String title, String description) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        if (title != null && !title.trim().isEmpty()) {
            task.setTitle(title);
        }
        if (description != null) {
            task.setDescription(description);
        }

        return taskRepository.update(task);
    }

    /**
     * Delete a task.
     */
    public void deleteTask(Long id) {
        // Verify task exists before deleting
        taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        taskRepository.deleteById(id);
    }

    /**
     * Get tasks by status.
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Mark task as completed.
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
}
