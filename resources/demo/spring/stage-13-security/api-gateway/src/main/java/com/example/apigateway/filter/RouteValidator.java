package com.example.apigateway.filter;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

    // Public endpoints that don't require authentication
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/",
            "/actuator/",
            "/eureka/"
    );

    public boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(path::startsWith);
    }
}
