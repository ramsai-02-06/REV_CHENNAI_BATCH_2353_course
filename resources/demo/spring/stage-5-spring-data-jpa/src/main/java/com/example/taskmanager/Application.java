package com.example.taskmanager;

import com.example.taskmanager.ui.ConsoleUI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot Application - unchanged from Stage 4.
 *
 * The magic of Spring Boot: we changed from JdbcTemplate to JPA,
 * and only the repository and entity classes changed.
 * The application entry point stays the same!
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ConsoleUI consoleUI) {
        return args -> {
            consoleUI.run();
        };
    }
}
