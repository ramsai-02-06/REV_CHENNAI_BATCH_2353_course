package com.taskmanager.model;

/**
 * View model for displaying tasks in the UI.
 * Contains pre-formatted strings ready for display.
 *
 * This separates presentation concerns from the domain model,
 * allowing the Controller to handle all formatting logic.
 */
public class TaskView {
    private final Long id;
    private final String title;
    private final String description;
    private final String status;
    private final String statusDisplay;
    private final String createdAt;
    private final String summary;
    private final String detailedView;

    private TaskView(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.status = builder.status;
        this.statusDisplay = builder.statusDisplay;
        this.createdAt = builder.createdAt;
        this.summary = builder.summary;
        this.detailedView = builder.detailedView;
    }

    // Getters only - immutable view object
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getSummary() {
        return summary;
    }

    public String getDetailedView() {
        return detailedView;
    }

    /**
     * Builder pattern for creating TaskView instances.
     */
    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private String status;
        private String statusDisplay;
        private String createdAt;
        private String summary;
        private String detailedView;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder statusDisplay(String statusDisplay) {
            this.statusDisplay = statusDisplay;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder detailedView(String detailedView) {
            this.detailedView = detailedView;
            return this;
        }

        public TaskView build() {
            return new TaskView(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return summary != null ? summary : String.format("[%d] %s (%s)", id, title, status);
    }
}
