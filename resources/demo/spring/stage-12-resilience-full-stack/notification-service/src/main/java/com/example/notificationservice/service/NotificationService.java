package com.example.notificationservice.service;

import com.example.notificationservice.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final Map<Long, Notification> notificationStore = new ConcurrentHashMap<>();

    public List<Notification> getAllNotifications() {
        logger.debug("Fetching all notifications, count: {}", notificationStore.size());
        return new ArrayList<>(notificationStore.values());
    }

    public Optional<Notification> getNotificationById(Long id) {
        logger.debug("Fetching notification with id: {}", id);
        return Optional.ofNullable(notificationStore.get(id));
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        logger.debug("Fetching notifications for user: {}", userId);
        return notificationStore.values().stream()
                .filter(n -> userId.equals(n.getUserId()))
                .collect(Collectors.toList());
    }

    public List<Notification> getNotificationsByTaskId(Long taskId) {
        logger.debug("Fetching notifications for task: {}", taskId);
        return notificationStore.values().stream()
                .filter(n -> taskId.equals(n.getTaskId()))
                .collect(Collectors.toList());
    }

    public Notification createNotification(String type, String message, Long userId, Long taskId) {
        logger.info("Creating notification - type: {}, userId: {}, taskId: {}", type, userId, taskId);

        Notification notification = new Notification(type, message, userId, taskId);

        // Simulate sending notification
        sendNotification(notification);

        notificationStore.put(notification.getId(), notification);
        logger.info("Created notification with id: {}", notification.getId());

        return notification;
    }

    private void sendNotification(Notification notification) {
        // Simulate notification sending (email, SMS, push, etc.)
        logger.info("SENDING NOTIFICATION: {}", notification);
        logger.info("=========================================");
        logger.info("To User ID: {}", notification.getUserId());
        logger.info("Type: {}", notification.getType());
        logger.info("Message: {}", notification.getMessage());
        logger.info("=========================================");

        notification.setSent(true);
    }

    public boolean deleteNotification(Long id) {
        logger.info("Deleting notification with id: {}", id);
        return notificationStore.remove(id) != null;
    }

    public void clearAllNotifications() {
        logger.warn("Clearing all notifications");
        notificationStore.clear();
    }
}
