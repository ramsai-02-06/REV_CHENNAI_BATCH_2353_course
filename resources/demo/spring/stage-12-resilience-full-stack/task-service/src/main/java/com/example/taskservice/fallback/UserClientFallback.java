package com.example.taskservice.fallback;

import com.example.taskservice.client.UserClient;
import com.example.taskservice.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Fallback implementation for UserClient.
 *
 * This class provides fallback responses when the user-service is unavailable.
 * The circuit breaker pattern ensures that:
 * 1. The system remains responsive even when dependencies fail
 * 2. Failed services get time to recover (circuit opens)
 * 3. Users get meaningful fallback responses instead of errors
 */
@Component
public class UserClientFallback implements UserClient {

    private static final Logger logger = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public UserResponse getUserById(Long id) {
        logger.warn("FALLBACK: user-service unavailable. Returning fallback user for id: {}", id);

        // Return a fallback user with minimal information
        UserResponse fallbackUser = new UserResponse();
        fallbackUser.setId(id);
        fallbackUser.setName("Unknown User (Service Unavailable)");
        fallbackUser.setEmail("unavailable@fallback.local");
        fallbackUser.setDepartment("N/A");

        return fallbackUser;
    }

    @Override
    public Map<String, Boolean> checkUserExists(Long id) {
        logger.warn("FALLBACK: user-service unavailable. Assuming user {} exists.", id);

        // In fallback mode, we assume the user exists to allow task operations
        // This is a business decision - you might want to block instead
        return Map.of("exists", true);
    }
}
