/**
 * Application entry point.
 *
 * ===========================================================
 * THIS IS WHERE YOU FEEL THE PAIN OF MANUAL WIRING
 * ===========================================================
 *
 * Look at how many objects we need to create manually:
 * 1. DatabaseConfig - holds connection settings
 * 2. ConnectionManager - creates connections
 * 3. TaskRepository - needs ConnectionManager
 * 4. TaskService - needs TaskRepository
 * 5. ConsoleUI - needs TaskService
 *
 * The dependency chain is: UI -> Service -> Repository -> ConnectionManager -> Config
 *
 * PROBLEMS WITH THIS APPROACH:
 *
 * 1. ORDER MATTERS
 *    You must create dependencies BEFORE the classes that need them.
 *    Get the order wrong = NullPointerException.
 *
 * 2. CHANGES RIPPLE
 *    If TaskRepository needs a new dependency (e.g., a cache), you must:
 *    - Create the cache here
 *    - Pass it to TaskRepository
 *    - Recompile
 *
 * 3. HARDCODED VALUES
 *    Database credentials are right here in the code.
 *    Different environments (dev/test/prod) need code changes.
 *
 * 4. TESTING IS HARD
 *    How do you test TaskService without a real database?
 *    You'd need to manually create a mock TaskRepository.
 *
 * 5. NO FLEXIBILITY
 *    What if you want two different database configurations?
 *    What if you want to swap MySQL for PostgreSQL?
 *    Every change requires modifying this file.
 *
 * In Stage 3, Spring will handle all of this automatically.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Task Manager...\n");

        // ============================================
        // MANUAL DEPENDENCY WIRING - THE PAIN BEGINS
        // ============================================

        // Step 1: Create configuration
        // PAIN: Credentials hardcoded in Java code
        DatabaseConfig config = new DatabaseConfig(
                "localhost",    // host
                "taskmanager",  // database name
                "root",         // username - CHANGE THIS
                "password"      // password - CHANGE THIS
        );

        // Step 2: Create connection manager (depends on config)
        ConnectionManager connectionManager = new ConnectionManager(config);

        // Step 3: Create repository (depends on connection manager)
        TaskRepository taskRepository = new TaskRepository(connectionManager);

        // Step 4: Create service (depends on repository)
        TaskService taskService = new TaskService(taskRepository);

        // Step 5: Create UI (depends on service)
        ConsoleUI ui = new ConsoleUI(taskService);

        // ============================================
        // FINALLY, we can run the application
        // ============================================

        /*
         * REFLECTION QUESTIONS:
         *
         * 1. What happens if we need to add logging to every repository method?
         *    (Answer: We'd have to modify every method manually)
         *
         * 2. What if we want to add transaction support?
         *    (Answer: More boilerplate in every method)
         *
         * 3. What if we need to switch databases for testing?
         *    (Answer: Change code, recompile)
         *
         * 4. What if we add 50 more classes with dependencies?
         *    (Answer: This main method becomes unmanageable)
         *
         * Spring solves ALL of these problems. Keep going to see how!
         */

        try {
            ui.run();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
