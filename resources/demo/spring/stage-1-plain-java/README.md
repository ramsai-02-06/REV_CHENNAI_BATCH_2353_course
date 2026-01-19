# Stage 1: Plain Java + JDBC

## Goal
Experience the pain of manual dependency wiring and raw JDBC code.

## What You'll Notice
- Every object is created manually in `Main.java`
- Changing database credentials requires code changes
- JDBC boilerplate is repeated in every repository method
- Connection management is error-prone
- Testing is difficult (can't mock dependencies easily)

## Project Structure
```
src/
├── Main.java                 # Entry point - ALL wiring happens here
├── Task.java                 # Domain model
├── TaskStatus.java           # Enum for status
├── DatabaseConfig.java       # Database connection settings
├── ConnectionManager.java    # Creates database connections
├── TaskRepository.java       # Raw JDBC data access
├── TaskService.java          # Business logic
└── ConsoleUI.java            # User interface
```

## Prerequisites
1. MySQL installed and running
2. JDK 17+
3. MySQL Connector/J JAR file

## Database Setup
```sql
CREATE DATABASE taskmanager;
USE taskmanager;

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## How to Run

### Option 1: Command Line
```bash
# Download MySQL connector JAR first
# https://dev.mysql.com/downloads/connector/j/

# Compile
javac -cp ".:mysql-connector-j-8.0.33.jar" src/*.java -d out

# Run
java -cp "out:mysql-connector-j-8.0.33.jar" Main
```

### Option 2: IDE
1. Create a new Java project
2. Add MySQL Connector/J to classpath
3. Copy all files from `src/` to your source folder
4. Run `Main.java`

## Pain Points to Observe

### 1. Manual Wiring (Main.java)
```java
// You have to create and connect everything manually
DatabaseConfig config = new DatabaseConfig("localhost", "taskmanager", "root", "password");
ConnectionManager connectionManager = new ConnectionManager(config);
TaskRepository repository = new TaskRepository(connectionManager);
TaskService service = new TaskService(repository);
ConsoleUI ui = new ConsoleUI(service);
```

**Problem:** Imagine 50 classes. Every dependency change ripples through.

### 2. JDBC Boilerplate (TaskRepository.java)
```java
public Task findById(Long id) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        conn = connectionManager.getConnection();
        ps = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?");
        ps.setLong(1, id);
        rs = ps.executeQuery();
        // ... more code to map results
    } catch (SQLException e) {
        // handle error
    } finally {
        // close rs, ps, conn - in reverse order!
    }
}
```

**Problem:** This pattern repeats for EVERY database method.

### 3. Hardcoded Configuration (DatabaseConfig.java)
```java
// Credentials are in code!
new DatabaseConfig("localhost", "taskmanager", "root", "password");
```

**Problem:** Can't change without recompiling.

## Exercises

1. **Add a new field** - Add `priority` to Task. Notice how many files change.
2. **Add a new method** - Add `findByStatus()`. Count the JDBC boilerplate lines.
3. **Try to write a test** - How would you test `TaskService` without a real database?

## What's Next?
Stage 2 introduces Maven to manage dependencies properly.

---

## Key Learning
After completing this stage, you should feel:
- "There must be a better way to wire objects"
- "This JDBC code is repetitive"
- "Hardcoding config is bad"

**That's the point.** Spring exists to solve these exact problems.
