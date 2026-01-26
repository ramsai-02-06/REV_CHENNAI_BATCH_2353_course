package com.taskmanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Domain model representing a Task entity.
 * This is the core business object used across all layers.
 */
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor
    public Task() {
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for creating new tasks
    public Task(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    // Full constructor (used when loading from DB)
    public Task(Long id, String title, String description, TaskStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.format(FORMATTER) : "N/A";
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, title='%s', status=%s, createdAt=%s}",
                id, title, status, getFormattedCreatedAt());
    }
}
