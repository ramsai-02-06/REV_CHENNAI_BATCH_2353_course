package com.example.taskmanager;

import com.example.taskmanager.config.AppConfig;
import com.example.taskmanager.ui.ConsoleUI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Application entry point.
 *
 * =====================================================
 * COMPARE THIS TO STAGE 2!
 * =====================================================
 *
 * STAGE 2 (Manual Wiring):
 *     ConnectionManager connectionManager = new ConnectionManager();
 *     TaskRepository taskRepository = new TaskRepository(connectionManager);
 *     TaskService taskService = new TaskService(taskRepository);
 *     ConsoleUI ui = new ConsoleUI(taskService);
 *
 * STAGE 3 (Spring DI):
 *     ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
 *     ConsoleUI ui = context.getBean(ConsoleUI.class);
 *
 * Spring creates and wires ALL dependencies automatically!
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Task Manager with Spring...\n");

        /*
         * Create the Spring Application Context.
         *
         * This one line triggers:
         * 1. Reading AppConfig
         * 2. Component scanning
         * 3. Bean creation
         * 4. Dependency injection
         */
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        /*
         * Get the ConsoleUI bean from Spring.
         *
         * Spring has already:
         * - Created ConnectionManager
         * - Injected it into TaskRepository
         * - Created TaskRepository
         * - Injected it into TaskService
         * - Created TaskService
         * - Injected it into ConsoleUI
         * - Created ConsoleUI
         *
         * We just ask for ConsoleUI and everything is ready!
         */
        ConsoleUI ui = context.getBean(ConsoleUI.class);

        // Run the application
        try {
            ui.run();
        } finally {
            // Properly close the context
            ((AnnotationConfigApplicationContext) context).close();
        }
    }
}
