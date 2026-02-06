package com.example.notificationservice.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Notification {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private String type;
    private String message;
    private Long userId;
    private Long taskId;
    private LocalDateTime createdAt;
    private boolean sent;

    public Notification() {
        this.id = ID_GENERATOR.getAndIncrement();
        this.createdAt = LocalDateTime.now();
        this.sent = false;
    }

    public Notification(String type, String message, Long userId, Long taskId) {
        this();
        this.type = type;
        this.message = message;
        this.userId = userId;
        this.taskId = taskId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", createdAt=" + createdAt +
                ", sent=" + sent +
                '}';
    }
}
