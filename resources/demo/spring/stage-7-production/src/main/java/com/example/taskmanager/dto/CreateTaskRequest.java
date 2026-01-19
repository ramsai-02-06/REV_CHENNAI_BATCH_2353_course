package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new task.
 *
 * Validation annotations ensure:
 * - Title is required and not empty
 * - Title doesn't exceed 100 characters
 * - Description is optional but limited to 1000 characters
 */
public record CreateTaskRequest(

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    String title,

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description

) {}
