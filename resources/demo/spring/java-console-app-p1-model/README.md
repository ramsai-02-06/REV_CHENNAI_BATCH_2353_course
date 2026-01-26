# Java Console App - P1 Model Architecture

A well-structured Java console application demonstrating enterprise patterns that evolve naturally into Spring concepts.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│  PRESENTATION LAYER                                                 │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────────┐              │
│  │  ConsoleUI  │──│ InputHandler │──│ OutputRenderer │              │
│  └─────────────┘  └──────────────┘  └────────────────┘              │
│         │                                                           │
│         │  TaskInput (model)           TaskView (model)             │
│         ▼                                                           │
├─────────────────────────────────────────────────────────────────────┤
│  CONTROLLER LAYER                                                   │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  TaskController                                                │ │
│  │  - Input validation (delegates to InputValidator)              │ │
│  │  - Convert TaskInput → Task (domain model)                     │ │
│  │  - Call service methods                                        │ │
│  │  - Convert Task → TaskView (formatted for display)             │ │
│  └────────────────────────────────────────────────────────────────┘ │
│         │                                                           │
├─────────────────────────────────────────────────────────────────────┤
│  SERVICE LAYER                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  TaskService                                                   │ │
│  │  - Business logic and rules                                    │ │
│  │  - Exception translation (DataAccessException → BusinessException) │
│  │  - Orchestrates repository calls                               │ │
│  └────────────────────────────────────────────────────────────────┘ │
│         │                                                           │
├─────────────────────────────────────────────────────────────────────┤
│  REPOSITORY LAYER                                                   │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  TaskRepository                                                │ │
│  │  - JDBC operations with logging                                │ │
│  │  - SQL error code detection for constraint violations          │ │
│  │  - Returns DataAccessException with context                    │ │
│  └────────────────────────────────────────────────────────────────┘ │
│         │                                                           │
├─────────────────────────────────────────────────────────────────────┤
│  INFRASTRUCTURE LAYER                                               │
│  ┌──────────────────┐  ┌────────────────┐  ┌──────────────────┐    │
│  │  ConnectionManager│──│  DatabaseConfig │──│   ConfigLoader   │    │
│  └──────────────────┘  └────────────────┘  └──────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘

CROSS-CUTTING CONCERNS:
┌─────────────────────────────────────────────────────────────────────┐
│  InputValidator   │  Log4j2 Logging   │  BusinessException Hierarchy │
│  (REGEX patterns) │  (all layers)     │  (consistent error handling) │
└─────────────────────────────────────────────────────────────────────┘
```

## Key Features

### 1. Business Exception from SQL Constraint Violation

```java
// Repository detects SQL error and wraps it
throw new DataAccessException(message, e.getSQLState(), e.getErrorCode(), e);

// Service translates to business exception
if (e.isUniqueConstraintViolation()) {
    throw new DuplicateTaskException(title, e);
}
```

### 2. Controller Layer (Input → Model → Output)

```java
// Input model from UI
public TaskView createTask(TaskInput input) {
    inputValidator.validateForCreate(input);      // Validate
    Task task = convertInputToTask(input);        // Convert to domain
    Task created = taskService.createTask(task);  // Business logic
    return convertTaskToView(created);            // Format for display
}
```

### 3. Log4j2 Logging in All Layers

```java
// Repository logging pattern (ready for AOP)
logger.debug("Entering save() with task: title='{}'", task.getTitle());
// ... execute ...
logger.info("Exiting save() - created task id={} in {}ms", task.getId(), duration);
```

### 4. REGEX Input Validation

```java
// Title pattern: must start with alphanumeric, allows common punctuation
private static final String DEFAULT_TITLE_PATTERN =
    "^[a-zA-Z0-9][a-zA-Z0-9\\s\\-_:,.!?()]*$";

// ID pattern: positive integer only
private static final String ID_PATTERN = "^[1-9]\\d*$";
```

### 5. External Configuration

```properties
# resources/application.properties
db.host=localhost
db.port=3306
db.name=taskmanager
db.username=root
db.password=password

validation.title.minLength=3
validation.title.maxLength=100
```

### 6. OOP-Based Console UI

```java
// ConsoleUI orchestrates, delegates to specialized classes
public void run() {
    outputRenderer.showMenu();                    // Display
    TaskInput input = inputHandler.collectCreateTaskInput();  // Input as model
    TaskView view = controller.createTask(input);             // Process
    outputRenderer.showCreatedTask(view);                     // Output view
}
```

## Project Structure

```
java-console-app-p1-model/
├── src/
│   └── com/taskmanager/
│       ├── Main.java                    # Entry point, DI wiring
│       ├── model/
│       │   ├── Task.java                # Domain model
│       │   ├── TaskStatus.java          # Status enum
│       │   ├── TaskInput.java           # Input DTO
│       │   └── TaskView.java            # Output DTO
│       ├── exception/
│       │   ├── BusinessException.java   # Base business exception
│       │   ├── DuplicateTaskException.java
│       │   ├── TaskNotFoundException.java
│       │   ├── ValidationException.java
│       │   └── DataAccessException.java
│       ├── config/
│       │   ├── ConfigLoader.java        # Properties file loading
│       │   ├── DatabaseConfig.java
│       │   └── ConnectionManager.java
│       ├── validation/
│       │   └── InputValidator.java      # REGEX validation
│       ├── repository/
│       │   └── TaskRepository.java      # JDBC with logging
│       ├── service/
│       │   └── TaskService.java         # Business logic
│       ├── controller/
│       │   └── TaskController.java      # Request/Response handling
│       └── ui/
│           ├── ConsoleUI.java           # Main UI orchestrator
│           ├── InputHandler.java        # Input collection
│           └── OutputRenderer.java      # Display formatting
├── resources/
│   ├── application.properties           # External configuration
│   └── log4j2.xml                       # Logging configuration
├── lib/                                 # JAR dependencies
└── README.md
```

## Prerequisites

1. JDK 17+
2. MySQL Server running
3. Required JARs in `lib/`:
   - `mysql-connector-j-8.x.x.jar`
   - `log4j-api-2.x.x.jar`
   - `log4j-core-2.x.x.jar`

## Database Setup

```sql
CREATE DATABASE taskmanager;
USE taskmanager;

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL UNIQUE,  -- UNIQUE constraint for demo
    description TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## How to Run

### 1. Download Dependencies

```bash
# Create lib directory
mkdir -p lib

# Download MySQL Connector
curl -L -o lib/mysql-connector-j-8.0.33.jar \
  https://repo1.maven.org/maven2/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar

# Download Log4j
curl -L -o lib/log4j-api-2.20.0.jar \
  https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar

curl -L -o lib/log4j-core-2.20.0.jar \
  https://repo1.maven.org/maven2/org/apache/logging/log4j/log4j-core/2.20.0/log4j-core-2.20.0.jar
```

### 2. Configure Database

Edit `resources/application.properties`:
```properties
db.host=localhost
db.port=3306
db.name=taskmanager
db.username=your_username
db.password=your_password
```

### 3. Compile

```bash
# From project root
javac -cp "lib/*" -d out $(find src -name "*.java")
```

### 4. Run

```bash
java -cp "out:lib/*:resources" com.taskmanager.Main
```

## Future AOP Evolution

This architecture is designed to evolve into AOP patterns:

### Current Manual Logging
```java
logger.debug("Entering save() with task: {}", task);
// ... method body ...
logger.info("Exiting save() in {}ms", duration);
```

### Future AOP Aspect
```java
@Aspect
public class LoggingAspect {
    @Around("execution(* com.taskmanager.repository.*.*(..))")
    public Object logMethod(ProceedingJoinPoint pjp) {
        logger.debug("Entering {} with {}", pjp.getSignature(), pjp.getArgs());
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        logger.info("Exiting {} in {}ms", pjp.getSignature(),
                    System.currentTimeMillis() - start);
        return result;
    }
}
```

### Other AOP Candidates
- **Security**: Check permissions before sensitive operations
- **Caching**: Cache frequently accessed data
- **Transaction**: Automatic commit/rollback
- **Retry**: Automatic retry on transient failures

## Learning Objectives

After studying this project, you should understand:

1. **Layered Architecture**: Clear separation of concerns
2. **Model Objects**: TaskInput for input, Task for domain, TaskView for display
3. **Exception Hierarchy**: Business exceptions vs. technical exceptions
4. **Validation Patterns**: REGEX-based input validation
5. **External Configuration**: Properties file for environment-specific settings
6. **Logging Strategy**: What to log at each layer
7. **Manual DI**: Explicit dependency wiring (what Spring automates)

## Comparison with Spring Boot

| This Project | Spring Boot Equivalent |
|--------------|------------------------|
| `Main.java` (manual wiring) | `@SpringBootApplication` |
| `ConfigLoader` | `@ConfigurationProperties` |
| `InputValidator` | `@Valid` + Bean Validation |
| `TaskRepository` | `@Repository` / JpaRepository |
| `TaskService` | `@Service` + `@Transactional` |
| `TaskController` | `@RestController` |
| Manual logging | Spring AOP + `@Aspect` |
| `DataAccessException` | Spring's `DataAccessException` |
