package com.example.taskmanager.dto;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;

/**
 * Response DTO for task data.
 *
 * Why use a DTO instead of returning Task entity directly?
 * 1. Control what fields are exposed to API
 * 2. Format data differently (e.g., dates)
 * 3. Add computed fields (not stored in DB)
 * 4. Hide internal implementation details
 * 5. Version API independently of database schema
 */
public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDateTime createdAt
) {
    /**
     * Factory method to convert Task entity to TaskResponse DTO.
     */
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreatedAt()
        );
    }
}
