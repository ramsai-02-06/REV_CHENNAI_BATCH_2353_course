package com.example.taskmanager.controller;

import com.example.taskmanager.dto.*;
import com.example.taskmanager.model.TaskStatus;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller with production-ready features.
 *
 * CHANGES FROM STAGE 6:
 * - Uses DTOs instead of entity/maps
 * - @Valid triggers validation
 * - Returns TaskResponse instead of Task
 * - No try-catch - GlobalExceptionHandler handles errors
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks
     * Returns all tasks.
     */
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks()
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    /**
     * GET /api/tasks/{id}
     * Returns a specific task.
     * Throws TaskNotFoundException if not found (handled by GlobalExceptionHandler).
     */
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return TaskResponse.fromEntity(taskService.getTaskById(id));
    }

    /**
     * POST /api/tasks
     * Creates a new task.
     *
     * @Valid triggers validation on CreateTaskRequest.
     * If validation fails, MethodArgumentNotValidException is thrown
     * and handled by GlobalExceptionHandler.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return TaskResponse.fromEntity(
                taskService.createTask(request.title(), request.description())
        );
    }

    /**
     * PUT /api/tasks/{id}
     * Updates task title and/or description.
     */
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        return TaskResponse.fromEntity(
                taskService.updateTask(id, request.title(), request.description())
        );
    }

    /**
     * PATCH /api/tasks/{id}/status
     * Updates only the task status.
     */
    @PatchMapping("/{id}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        return TaskResponse.fromEntity(
                taskService.updateTaskStatus(id, request.status())
        );
    }

    /**
     * DELETE /api/tasks/{id}
     * Deletes a task.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /**
     * GET /api/tasks/status/{status}
     * Returns tasks filtered by status.
     */
    @GetMapping("/status/{status}")
    public List<TaskResponse> getTasksByStatus(@PathVariable TaskStatus status) {
        return taskService.getTasksByStatus(status)
                .stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }
}
