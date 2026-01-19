package com.example.taskmanager.dto;

import com.example.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating task status.
 */
public record UpdateStatusRequest(

    @NotNull(message = "Status is required")
    TaskStatus status

) {}
