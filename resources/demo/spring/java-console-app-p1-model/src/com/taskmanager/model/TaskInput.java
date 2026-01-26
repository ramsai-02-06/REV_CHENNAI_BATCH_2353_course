package com.taskmanager.model;

/**
 * Input model for creating or updating tasks.
 * Used to transfer data from UI layer to Controller layer.
 *
 * This separates the raw input from the domain model, allowing
 * validation and transformation in the controller before passing
 * to the service layer.
 */
public class TaskInput {
    private Long id;  // Used for updates
    private String title;
    private String description;
    private String status;  // String to allow flexible input parsing

    public TaskInput() {}

    public TaskInput(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public TaskInput(Long id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Builder-style setters for fluent API
    public TaskInput withId(Long id) {
        this.id = id;
        return this;
    }

    public TaskInput withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskInput withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskInput withStatus(String status) {
        this.status = status;
        return this;
    }

    // Standard Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("TaskInput{id=%d, title='%s', description='%s', status='%s'}",
                id, title, description, status);
    }
}
