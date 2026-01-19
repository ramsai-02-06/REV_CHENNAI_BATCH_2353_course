package com.example.taskmanager;

import com.example.taskmanager.repository.ConnectionManager;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.ui.ConsoleUI;

/**
 * Application entry point.
 *
 * WHAT IMPROVED (vs Stage 1):
 * - Proper package structure
 * - Dependencies managed by Maven
 * - Configuration in external properties file
 *
 * WHAT'S STILL PAINFUL:
 * - Manual wiring of all dependencies
 * - Order of creation matters
 * - Hard to test (can't inject mocks)
 *
 * Stage 3 (Spring Core) will eliminate the manual wiring.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Task Manager...\n");

        // ========================================
        // STILL MANUAL WIRING - Spring fixes this
        // ========================================

        // Config now loaded from db.properties (improvement!)
        ConnectionManager connectionManager = new ConnectionManager();

        // But we still create everything manually...
        TaskRepository taskRepository = new TaskRepository(connectionManager);
        TaskService taskService = new TaskService(taskRepository);
        ConsoleUI ui = new ConsoleUI(taskService);

        // Run
        try {
            ui.run();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
