package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import com.example.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Task operations.
 *
 * =====================================================
 * THIS REPLACES ConsoleUI FROM STAGE 5
 * =====================================================
 *
 * Instead of:
 *   Console input → TaskService
 *
 * We have:
 *   HTTP Request → TaskController → TaskService → HTTP Response
 *
 * Key Annotations:
 * - @RestController: This is a REST API controller (returns JSON)
 * - @RequestMapping: Base URL path for all endpoints
 * - @GetMapping, @PostMapping, etc.: HTTP method mappings
 * - @PathVariable: Extract value from URL path
 * - @RequestBody: Parse JSON request body
 * - @ResponseStatus: Set HTTP status code
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
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * GET /api/tasks/{id}
     * Returns a specific task by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)                     // 200 OK if found
                .orElse(ResponseEntity.notFound().build());  // 404 Not Found
    }

    /**
     * POST /api/tasks
     * Creates a new task.
     *
     * Request body:
     * {
     *   "title": "Task title",
     *   "description": "Task description"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // 201 Created
    public Task createTask(@RequestBody CreateTaskRequest request) {
        return taskService.createTask(request.title(), request.description());
    }

    /**
     * PUT /api/tasks/{id}
     * Updates an existing task.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request) {
        try {
            Task updated = taskService.updateTask(id, request.title(), request.description());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PATCH /api/tasks/{id}/status
     * Updates only the status of a task.
     *
     * Request body:
     * {
     *   "status": "COMPLETED"
     * }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            TaskStatus status = TaskStatus.valueOf(request.get("status"));
            Task updated = taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/tasks/{id}
     * Deletes a task.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();   // 404 Not Found
        }
    }

    /**
     * GET /api/tasks/status/{status}
     * Returns tasks filtered by status.
     */
    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable TaskStatus status) {
        return taskService.getTasksByStatus(status);
    }

    // =====================================================
    // Request DTOs (Data Transfer Objects)
    // =====================================================
    // Using Java records for simple request objects.
    // Stage 7 will expand on DTOs with validation.

    record CreateTaskRequest(String title, String description) {}

    record UpdateTaskRequest(String title, String description) {}
}
