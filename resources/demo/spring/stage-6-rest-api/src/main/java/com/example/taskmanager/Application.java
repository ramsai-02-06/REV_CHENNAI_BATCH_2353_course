package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application.
 *
 * CHANGE FROM STAGE 5:
 * - No more CommandLineRunner!
 * - Spring Boot starts an embedded Tomcat server
 * - The REST API is automatically available at http://localhost:8080
 *
 * That's the power of spring-boot-starter-web:
 * Just add @RestController and the server is ready!
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("\n==========================================");
        System.out.println("  Task Manager REST API is running!");
        System.out.println("  Base URL: http://localhost:8080/api/tasks");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("==========================================\n");
    }
}
