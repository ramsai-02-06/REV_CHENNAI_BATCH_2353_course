package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application - Production Ready!
 *
 * This is the culmination of the 7-stage journey:
 *
 * Stage 1: Plain Java - Manual wiring, raw JDBC
 * Stage 2: Maven - Dependency management
 * Stage 3: Spring Core - Dependency injection
 * Stage 4: Spring Boot - Auto-configuration
 * Stage 5: Spring Data JPA - No more SQL
 * Stage 6: REST API - HTTP endpoints
 * Stage 7: Production - Validation, error handling, DTOs
 *
 * The application is now:
 * - Well-structured with clear layers
 * - Validated at the API boundary
 * - Handles errors gracefully
 * - Returns consistent responses
 * - Ready for further production hardening
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("\n============================================");
        System.out.println("  Task Manager API - Production Ready!");
        System.out.println("============================================");
        System.out.println("  API: http://localhost:8080/api/tasks");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("============================================\n");
    }
}
