# Progressive Spring Learning: Task Manager Application

A hands-on project that evolves through 7 stages, demonstrating why each Spring concept exists by first experiencing the problem it solves.

## The Learning Philosophy

> "You can't appreciate the solution until you've felt the problem."

Each stage builds on the previous one. **Do not skip stages** - the learning comes from experiencing the evolution.

---

## Project Overview

**Application:** Simple Task Manager
- Create, read, update, delete tasks
- Each task has: id, title, description, status, created date

**What stays the same across all stages:**
- The business logic (TaskService)
- The data model (Task)
- The functionality

**What changes:**
- How components are wired together
- How data is accessed
- How the application is exposed (console → REST)

---

## Stage Progression

```
Stage 1: Plain Java + JDBC
    │    "Feel the pain of manual wiring and raw JDBC"
    │
    ▼
Stage 2: Maven
    │    "Proper project structure and dependency management"
    │
    ▼
Stage 3: Spring Core
    │    "Let Spring wire dependencies for you"
    │
    ▼
Stage 4: Spring Boot
    │    "Auto-configuration removes boilerplate"
    │
    ▼
Stage 5: Spring Data JPA
    │    "Write interfaces, Spring writes SQL"
    │
    ▼
Stage 6: REST API
    │    "Expose functionality over HTTP"
    │
    ▼
Stage 7: Production Touches
         "Validation, error handling, DTOs"
```

---

## Stage Details

### Stage 1: Plain Java + JDBC (Days 1-2)
**Location:** `stage-1-plain-java/`

Build the app with:
- Manual object creation and wiring in `main()`
- Raw JDBC with manual connection management
- Hand-written SQL statements
- Console-based user interface

**Pain Points You'll Experience:**
- Creating every object manually
- Passing dependencies through constructors everywhere
- Writing try-catch-finally for every DB operation
- Duplicating connection code
- Changing one dependency requires changing multiple files

**Run:** `javac *.java && java Main`

---

### Stage 2: Maven (Day 3)
**Location:** `stage-2-maven/`

Convert to Maven project:
- Standard directory structure (`src/main/java`, `src/main/resources`)
- `pom.xml` for dependency management
- External libraries (MySQL connector) managed properly

**What Improves:**
- No more manual JAR management
- Consistent project structure
- Easy to add dependencies

**Run:** `mvn compile exec:java`

---

### Stage 3: Spring Core (Days 4-5)
**Location:** `stage-3-spring-core/`

Introduce Spring Dependency Injection:
- `@Component`, `@Service`, `@Repository` annotations
- `@Autowired` for dependency injection
- `ApplicationContext` manages object lifecycle

**What Improves:**
- No manual wiring in `main()`
- Easy to swap implementations
- Testable code (inject mocks)

**Code change example:**
```java
// BEFORE (Stage 2)
DataSource ds = new DataSource(url, user, pass);
TaskRepository repo = new TaskRepository(ds);
TaskService service = new TaskService(repo);

// AFTER (Stage 3)
ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
TaskService service = ctx.getBean(TaskService.class);
// Spring created and wired everything!
```

**Run:** `mvn compile exec:java`

---

### Stage 4: Spring Boot (Day 6)
**Location:** `stage-4-spring-boot/`

Add Spring Boot:
- `@SpringBootApplication` replaces manual configuration
- `application.properties` for configuration
- Embedded H2 database (no MySQL setup needed)
- Auto-configuration "just works"

**What Improves:**
- No XML configuration
- Database connection auto-configured
- Sensible defaults for everything

**Run:** `mvn spring-boot:run`

---

### Stage 5: Spring Data JPA (Days 7-8)
**Location:** `stage-5-spring-data-jpa/`

Replace JDBC with JPA:
- `@Entity` for domain objects
- `JpaRepository` interface (no implementation needed!)
- Derived query methods

**Code change example:**
```java
// BEFORE (Stage 4) - 50 lines of JDBC
public Task findById(Long id) {
    String sql = "SELECT * FROM tasks WHERE id = ?";
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        // ... map result to Task object
    }
}

// AFTER (Stage 5) - 1 line
public interface TaskRepository extends JpaRepository<Task, Long> {
    // findById() is FREE - Spring implements it!
}
```

**Run:** `mvn spring-boot:run`

---

### Stage 6: REST API (Days 9-10)
**Location:** `stage-6-rest-api/`

Expose as HTTP API:
- `@RestController` for endpoints
- `@GetMapping`, `@PostMapping`, etc.
- JSON request/response with Jackson
- Same service layer, different interface

**What Improves:**
- Accessible from any client (web, mobile, Postman)
- Stateless, scalable
- Industry standard

**Endpoints:**
```
GET    /api/tasks       - List all tasks
GET    /api/tasks/{id}  - Get single task
POST   /api/tasks       - Create task
PUT    /api/tasks/{id}  - Update task
DELETE /api/tasks/{id}  - Delete task
```

**Run:** `mvn spring-boot:run`
**Test:** `curl http://localhost:8080/api/tasks`

---

### Stage 7: Production Touches (Days 11-12)
**Location:** `stage-7-production/`

Add production-ready features:
- Input validation (`@Valid`, `@NotBlank`)
- Global exception handling (`@ControllerAdvice`)
- DTOs (separate API model from domain model)
- Proper error responses
- Logging

**What Improves:**
- Meaningful error messages
- Input validation
- Clean separation of concerns
- Ready for real deployment

---

## How to Use This Project

### For Self-Learning

1. **Start with Stage 1** - Type the code yourself, don't copy-paste
2. **Run it, break it** - Understand what each part does
3. **Move to Stage 2** - Notice what improves, what stays the same
4. **Continue through all stages** - Don't skip!

### For Teaching

1. **Live code Stage 1** - Let students feel the boilerplate
2. **Ask:** "What's annoying about this code?"
3. **Introduce next stage** - Show how it solves exactly that problem
4. **Repeat for each stage**

---

## Database Setup

### Stages 1-3: MySQL Required
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

### Stages 4-7: H2 Embedded (No Setup!)
Spring Boot auto-configures H2 in-memory database.

---

## Quick Reference: What Changes Between Stages

| Aspect | Stage 1-2 | Stage 3 | Stage 4 | Stage 5 | Stage 6-7 |
|--------|-----------|---------|---------|---------|-----------|
| Wiring | Manual | Spring DI | Boot Auto | Boot Auto | Boot Auto |
| Database | Raw JDBC | Raw JDBC | Raw JDBC | JPA | JPA |
| Config | Hardcoded | Java Config | Properties | Properties | Properties |
| Interface | Console | Console | Console | Console | REST API |

---

## Files in Each Stage

```
stage-X/
├── pom.xml                 # Maven config (stage 2+)
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/taskmanager/
│       │       ├── Main.java / Application.java
│       │       ├── model/Task.java
│       │       ├── repository/TaskRepository.java
│       │       ├── service/TaskService.java
│       │       └── controller/ (stage 6+)
│       └── resources/
│           └── application.properties (stage 4+)
└── README.md               # Stage-specific instructions
```

---

## Learning Checkpoints

After each stage, you should be able to answer:

- **Stage 1:** Why is manual wiring problematic?
- **Stage 2:** What does Maven's dependency management solve?
- **Stage 3:** What is Inversion of Control? Why is it useful?
- **Stage 4:** What does Spring Boot auto-configure for you?
- **Stage 5:** Why is JpaRepository better than raw JDBC?
- **Stage 6:** What makes an API "RESTful"?
- **Stage 7:** Why separate DTOs from entities?

---

## Estimated Time

| Stage | Time | Focus |
|-------|------|-------|
| 1 | 4-6 hours | Understanding the baseline |
| 2 | 2-3 hours | Maven structure |
| 3 | 4-6 hours | Core Spring concepts |
| 4 | 3-4 hours | Spring Boot magic |
| 5 | 4-6 hours | JPA and repositories |
| 6 | 4-6 hours | REST principles |
| 7 | 3-4 hours | Production patterns |

**Total: ~25-35 hours** (1.5-2 weeks of focused learning)

---

## Next Steps After Completion

Once you've completed all 7 stages, you're ready for:
- Spring Security (authentication/authorization)
- Spring Profiles (environment-specific config)
- AOP (cross-cutting concerns like logging)
- Microservices architecture
