package com.taskmanager;

import com.taskmanager.config.ConfigLoader;
import com.taskmanager.config.ConnectionManager;
import com.taskmanager.config.DatabaseConfig;
import com.taskmanager.controller.TaskController;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;
import com.taskmanager.ui.ConsoleUI;
import com.taskmanager.ui.InputHandler;
import com.taskmanager.ui.OutputRenderer;
import com.taskmanager.validation.InputValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Application entry point with dependency wiring.
 *
 * This class demonstrates manual dependency injection that mirrors
 * what Spring does automatically:
 *
 * LAYER ARCHITECTURE:
 * ┌─────────────────────────────────────────────────────────────┐
 * │  ConsoleUI (Presentation)                                   │
 * │    ├── InputHandler  (Input processing)                     │
 * │    └── OutputRenderer (Output formatting)                   │
 * ├─────────────────────────────────────────────────────────────┤
 * │  TaskController (Request/Response handling)                 │
 * ├─────────────────────────────────────────────────────────────┤
 * │  TaskService (Business Logic)                               │
 * ├─────────────────────────────────────────────────────────────┤
 * │  TaskRepository (Data Access)                               │
 * ├─────────────────────────────────────────────────────────────┤
 * │  ConnectionManager / DatabaseConfig (Infrastructure)        │
 * └─────────────────────────────────────────────────────────────┘
 *
 * CROSS-CUTTING CONCERNS (ready for AOP):
 * - Logging: Log4j2 in all layers
 * - Validation: InputValidator
 * - Exception Handling: BusinessException hierarchy
 * - Configuration: External properties file
 *
 * SPRING BOOT EQUIVALENT:
 * In Spring Boot, this entire class is replaced by:
 * 1. @SpringBootApplication annotation
 * 2. Auto-configuration of DataSource, JPA, etc.
 * 3. @Autowired for dependency injection
 *
 * The manual wiring here shows exactly what Spring automates.
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== Task Manager Application Starting ===");

        try {
            // ============================================
            // LAYER 1: CONFIGURATION
            // ============================================
            // Load configuration from external properties file
            // This replaces hardcoded credentials

            logger.info("Loading configuration...");
            ConfigLoader configLoader = new ConfigLoader();
            DatabaseConfig dbConfig = configLoader.createDatabaseConfig();
            logger.info("Configuration loaded: {}", dbConfig);

            // ============================================
            // LAYER 2: INFRASTRUCTURE
            // ============================================
            // Create connection manager
            // In Spring Boot: Auto-configured DataSource

            logger.info("Initializing database connection...");
            ConnectionManager connectionManager = new ConnectionManager(dbConfig);

            // Test connection
            if (!connectionManager.testConnection()) {
                logger.error("Failed to connect to database. Please check your configuration.");
                System.err.println("ERROR: Cannot connect to database. Check resources/application.properties");
                System.exit(1);
            }

            // ============================================
            // LAYER 3: VALIDATION
            // ============================================
            // Create validator with config
            // In Spring Boot: @Validated with Bean Validation

            logger.info("Initializing validation...");
            InputValidator inputValidator = new InputValidator(configLoader);

            // ============================================
            // LAYER 4: DATA ACCESS
            // ============================================
            // Create repository
            // In Spring Boot: @Repository or JpaRepository

            logger.info("Initializing repository layer...");
            TaskRepository taskRepository = new TaskRepository(connectionManager);

            // ============================================
            // LAYER 5: BUSINESS LOGIC
            // ============================================
            // Create service
            // In Spring Boot: @Service

            logger.info("Initializing service layer...");
            TaskService taskService = new TaskService(taskRepository);

            // ============================================
            // LAYER 6: CONTROLLER
            // ============================================
            // Create controller (between UI and Service)
            // In Spring Boot: @Controller or @RestController

            logger.info("Initializing controller layer...");
            TaskController taskController = new TaskController(taskService, inputValidator);

            // ============================================
            // LAYER 7: PRESENTATION
            // ============================================
            // Create UI components
            // In Spring Boot with REST: Not needed (HTTP handles I/O)

            logger.info("Initializing presentation layer...");
            Scanner scanner = new Scanner(System.in);
            InputHandler inputHandler = new InputHandler(scanner, inputValidator);
            OutputRenderer outputRenderer = new OutputRenderer();

            // Wire everything together in ConsoleUI
            ConsoleUI consoleUI = new ConsoleUI(taskController, inputHandler, outputRenderer);

            // ============================================
            // RUN APPLICATION
            // ============================================

            logger.info("=== Application initialized successfully ===");
            logger.info("Starting user interface...");

            consoleUI.run();

            logger.info("=== Application shutdown complete ===");

        } catch (Exception e) {
            logger.fatal("Fatal error during startup", e);
            System.err.println("FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
