package com.example.taskservice.service;

import com.example.taskservice.client.NotificationClient;
import com.example.taskservice.client.UserClient;
import com.example.taskservice.dto.TaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.dto.UserResponse;
import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.repository.TaskRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    public TaskService(TaskRepository taskRepository,
                       UserClient userClient,
                       NotificationClient notificationClient) {
        this.taskRepository = taskRepository;
        this.userClient = userClient;
        this.notificationClient = notificationClient;
    }

    public List<TaskResponse> getAllTasks() {
        logger.debug("Fetching all tasks");
        return taskRepository.findAll().stream()
                .map(this::mapToResponseWithUser)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponse> getTaskById(Long id) {
        logger.debug("Fetching task with id: {}", id);
        return taskRepository.findById(id)
                .map(this::mapToResponseWithUser);
    }

    public List<TaskResponse> getTasksByUserId(Long userId) {
        logger.debug("Fetching tasks for user: {}", userId);
        return taskRepository.findByAssignedUserId(userId).stream()
                .map(this::mapToResponseWithUser)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        logger.debug("Fetching tasks with status: {}", status);
        return taskRepository.findByStatus(status).stream()
                .map(this::mapToResponseWithUser)
                .collect(Collectors.toList());
    }

    public TaskResponse createTask(TaskRequest request) {
        logger.info("Creating new task: {}", request.getTitle());

        // Validate assigned user exists (with circuit breaker)
        if (request.getAssignedUserId() != null) {
            validateUserExists(request.getAssignedUserId());
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);
        task.setAssignedUserId(request.getAssignedUserId());
        task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);
        logger.info("Created task with id: {}", savedTask.getId());

        // Send notification (fire-and-forget, with circuit breaker)
        sendTaskNotification("TASK_CREATED",
                "New task created: " + savedTask.getTitle(),
                savedTask.getAssignedUserId(),
                savedTask.getId());

        return mapToResponseWithUser(savedTask);
    }

    public Optional<TaskResponse> updateTask(Long id, TaskRequest request) {
        logger.info("Updating task with id: {}", id);

        return taskRepository.findById(id)
                .map(existingTask -> {
                    // Validate new assigned user if changed
                    if (request.getAssignedUserId() != null
                            && !request.getAssignedUserId().equals(existingTask.getAssignedUserId())) {
                        validateUserExists(request.getAssignedUserId());
                    }

                    TaskStatus oldStatus = existingTask.getStatus();

                    existingTask.setTitle(request.getTitle());
                    existingTask.setDescription(request.getDescription());
                    if (request.getStatus() != null) {
                        existingTask.setStatus(request.getStatus());
                    }
                    existingTask.setAssignedUserId(request.getAssignedUserId());
                    existingTask.setDueDate(request.getDueDate());

                    Task updatedTask = taskRepository.save(existingTask);
                    logger.info("Updated task with id: {}", updatedTask.getId());

                    // Send notification if status changed to COMPLETED
                    if (updatedTask.getStatus() == TaskStatus.COMPLETED
                            && oldStatus != TaskStatus.COMPLETED) {
                        sendTaskNotification("TASK_COMPLETED",
                                "Task completed: " + updatedTask.getTitle(),
                                updatedTask.getAssignedUserId(),
                                updatedTask.getId());
                    }

                    return mapToResponseWithUser(updatedTask);
                });
    }

    public Optional<TaskResponse> updateTaskStatus(Long id, TaskStatus newStatus) {
        logger.info("Updating status for task {}: {}", id, newStatus);

        return taskRepository.findById(id)
                .map(task -> {
                    TaskStatus oldStatus = task.getStatus();
                    task.setStatus(newStatus);
                    Task updatedTask = taskRepository.save(task);

                    // Send notification if completed
                    if (newStatus == TaskStatus.COMPLETED && oldStatus != TaskStatus.COMPLETED) {
                        sendTaskNotification("TASK_COMPLETED",
                                "Task completed: " + updatedTask.getTitle(),
                                updatedTask.getAssignedUserId(),
                                updatedTask.getId());
                    }

                    return mapToResponseWithUser(updatedTask);
                });
    }

    public boolean deleteTask(Long id) {
        logger.info("Deleting task with id: {}", id);

        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            logger.info("Deleted task with id: {}", id);
            return true;
        }

        logger.warn("Task with id {} not found for deletion", id);
        return false;
    }

    // ==================== Circuit Breaker Methods ====================

    @CircuitBreaker(name = "userService", fallbackMethod = "validateUserExistsFallback")
    private void validateUserExists(Long userId) {
        logger.debug("Validating user exists: {}", userId);
        Map<String, Boolean> result = userClient.checkUserExists(userId);
        if (!Boolean.TRUE.equals(result.get("exists"))) {
            throw new IllegalArgumentException("User with id " + userId + " does not exist");
        }
    }

    private void validateUserExistsFallback(Long userId, Throwable t) {
        logger.warn("FALLBACK: Could not validate user {}. Proceeding anyway. Error: {}",
                userId, t.getMessage());
        // In fallback, we allow the operation to proceed
        // This is a business decision - you might want to block instead
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
    private UserResponse getUser(Long userId) {
        logger.debug("Fetching user details for: {}", userId);
        return userClient.getUserById(userId);
    }

    private UserResponse getUserFallback(Long userId, Throwable t) {
        logger.warn("FALLBACK: Could not fetch user {}. Error: {}", userId, t.getMessage());
        UserResponse fallback = new UserResponse();
        fallback.setId(userId);
        fallback.setName("Unknown User (Service Unavailable)");
        fallback.setEmail("unavailable@fallback.local");
        fallback.setDepartment("N/A");
        return fallback;
    }

    @CircuitBreaker(name = "notificationService", fallbackMethod = "sendNotificationFallback")
    private void sendTaskNotification(String type, String message, Long userId, Long taskId) {
        logger.info("Sending notification - type: {}, userId: {}, taskId: {}", type, userId, taskId);

        try {
            notificationClient.sendNotification(Map.of(
                    "type", type,
                    "message", message,
                    "userId", userId != null ? userId : 0,
                    "taskId", taskId
            ));
            logger.info("Notification sent successfully");
        } catch (Exception e) {
            logger.error("Failed to send notification: {}", e.getMessage());
            // Don't throw - notifications are fire-and-forget
        }
    }

    private void sendNotificationFallback(String type, String message, Long userId, Long taskId, Throwable t) {
        logger.warn("FALLBACK: Notification service unavailable. Type: {}, TaskId: {}. Error: {}",
                type, taskId, t.getMessage());
        // Log the notification that couldn't be sent
        // In production, you might queue this for retry
    }

    // ==================== Helper Methods ====================

    private TaskResponse mapToResponseWithUser(Task task) {
        TaskResponse response = TaskResponse.fromEntity(task);

        if (task.getAssignedUserId() != null) {
            try {
                UserResponse user = getUser(task.getAssignedUserId());
                response.setAssignedUser(user);
            } catch (Exception e) {
                logger.warn("Could not fetch user for task {}: {}", task.getId(), e.getMessage());
            }
        }

        return response;
    }
}
