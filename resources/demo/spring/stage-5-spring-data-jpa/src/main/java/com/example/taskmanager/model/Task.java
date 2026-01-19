package com.example.taskmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity - maps to database table.
 *
 * CHANGES FROM STAGE 4:
 * - Added @Entity annotation
 * - Added @Table to specify table name
 * - Added @Id and @GeneratedValue for primary key
 * - Added @Column for column mappings
 * - Added @Enumerated for enum handling
 *
 * JPA/Hibernate will:
 * - Create the table automatically (ddl-auto=create)
 * - Map fields to columns
 * - Handle all SQL generation
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)  // Store as 'PENDING', not 0
    @Column(nullable = false)
    private TaskStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // JPA requires a no-arg constructor
    public Task() {
        this.status = TaskStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Task(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%s)", id, title, description, status);
    }
}
