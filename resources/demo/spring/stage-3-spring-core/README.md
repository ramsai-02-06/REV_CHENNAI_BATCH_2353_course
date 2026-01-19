# Stage 3: Spring Core - Dependency Injection

## Goal
Eliminate manual dependency wiring using Spring's IoC container.

## The Big Change

### Before (Stage 2 - Manual Wiring)
```java
public static void main(String[] args) {
    // YOU create everything in the right order
    ConnectionManager connectionManager = new ConnectionManager();
    TaskRepository taskRepository = new TaskRepository(connectionManager);
    TaskService taskService = new TaskService(taskRepository);
    ConsoleUI ui = new ConsoleUI(taskService);
    ui.run();
}
```

### After (Stage 3 - Spring DI)
```java
public static void main(String[] args) {
    // SPRING creates and wires everything
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    ConsoleUI ui = context.getBean(ConsoleUI.class);
    ui.run();
}
```

## Key Annotations Introduced

### @Component (and specializations)
Marks a class as a Spring-managed bean:
```java
@Repository  // For data access classes
public class TaskRepository { }

@Service     // For business logic classes
public class TaskService { }

@Component   // Generic component
public class ConsoleUI { }
```

### @Autowired
Tells Spring to inject a dependency:
```java
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired  // Spring injects TaskRepository automatically
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
```

### @Configuration + @ComponentScan
Configures Spring to scan for components:
```java
@Configuration
@ComponentScan("com.example.taskmanager")
public class AppConfig {
    // Spring finds all @Component classes in this package
}
```

## What Changed

| File | Change |
|------|--------|
| `pom.xml` | Added `spring-context` dependency |
| `AppConfig.java` | NEW - Spring configuration class |
| `TaskRepository.java` | Added `@Repository` |
| `TaskService.java` | Added `@Service`, `@Autowired` |
| `ConsoleUI.java` | Added `@Component`, `@Autowired` |
| `Main.java` | Uses `ApplicationContext` |

## How It Works

1. **Startup**: `Main` creates `ApplicationContext` with `AppConfig`
2. **Scanning**: Spring scans `com.example.taskmanager` for `@Component` classes
3. **Instantiation**: Spring creates instances of all found components
4. **Wiring**: Spring looks at constructors with `@Autowired` and injects dependencies
5. **Ready**: All beans are created and wired, application can start

## Dependency Graph (Spring handles this!)
```
ConsoleUI
    └── TaskService
            └── TaskRepository
                    └── ConnectionManager
```

Spring figures out the creation order automatically.

## How to Run

```bash
mvn compile exec:java
```

## Spring Container Lifecycle

```
1. Create ApplicationContext
        ↓
2. Read @Configuration
        ↓
3. @ComponentScan finds:
   - ConnectionManager
   - TaskRepository
   - TaskService
   - ConsoleUI
        ↓
4. Analyze dependencies
        ↓
5. Create beans in correct order
        ↓
6. Inject dependencies
        ↓
7. Application ready!
```

## Benefits Gained

| Problem (Stage 2) | Solution (Stage 3) |
|-------------------|-------------------|
| Manual creation order | Spring figures it out |
| Changes ripple through Main | Just add @Component |
| Hard to test | Inject mock beans easily |
| Tight coupling | Loose coupling via interfaces |

## What's Still Painful

- Manual database configuration
- JDBC boilerplate in repository
- No auto-configuration
- Must manage ApplicationContext lifecycle

## Exercises

1. **Add a new service**: Create `TaskStatisticsService` that depends on `TaskRepository`. Notice you don't change `Main.java`!

2. **Understand scanning**: Remove `@Service` from `TaskService`. What error do you get?

3. **Constructor vs Field injection**: Try `@Autowired` on a field instead of constructor. What are the tradeoffs?

## Key Concepts

### Inversion of Control (IoC)
- Traditional: Your code creates dependencies
- IoC: Framework creates dependencies and gives them to you

### Dependency Injection (DI)
- Constructor Injection (preferred): Dependencies via constructor
- Setter Injection: Dependencies via setter methods
- Field Injection: Dependencies directly into fields (not recommended)

### Bean Scopes
- `@Scope("singleton")` - One instance (default)
- `@Scope("prototype")` - New instance each time

## What's Next?
Stage 4 introduces Spring Boot for auto-configuration and simplified setup.
