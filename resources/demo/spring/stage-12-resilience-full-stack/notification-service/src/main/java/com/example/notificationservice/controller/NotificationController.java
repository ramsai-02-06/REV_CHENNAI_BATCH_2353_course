package com.example.notificationservice.controller;

import com.example.notificationservice.model.Notification;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long taskId) {
        logger.debug("GET /api/notifications - userId: {}, taskId: {}", userId, taskId);

        List<Notification> notifications;

        if (userId != null) {
            notifications = notificationService.getNotificationsByUserId(userId);
        } else if (taskId != null) {
            notifications = notificationService.getNotificationsByTaskId(taskId);
        } else {
            notifications = notificationService.getAllNotifications();
        }

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        logger.debug("GET /api/notifications/{}", id);

        return notificationService.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Map<String, Object> request) {
        logger.debug("POST /api/notifications - request: {}", request);

        String type = (String) request.get("type");
        String message = (String) request.get("message");
        Long userId = request.get("userId") != null
                ? ((Number) request.get("userId")).longValue()
                : null;
        Long taskId = request.get("taskId") != null
                ? ((Number) request.get("taskId")).longValue()
                : null;

        if (type == null || message == null) {
            return ResponseEntity.badRequest().build();
        }

        Notification notification = notificationService.createNotification(
                type, message, userId, taskId);

        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        logger.debug("DELETE /api/notifications/{}", id);

        if (notificationService.deleteNotification(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearAllNotifications() {
        logger.warn("DELETE /api/notifications - clearing all");

        notificationService.clearAllNotifications();
        return ResponseEntity.noContent().build();
    }
}
