# Stage 5: Spring Data JPA

## Goal
Eliminate all SQL and JDBC boilerplate with Spring Data JPA.

## The Mind-Blowing Change

### Stage 4 Repository: ~100 lines of JDBC code
```java
@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public Task save(Task task) {
        String sql = "INSERT INTO tasks (title, description, status, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            // ... more code
        }, keyHolder);
        // ... more code
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        List<Task> results = jdbcTemplate.query(sql, this::mapRowToTask, id);
        // ... more code
    }

    // ... findAll, update, deleteById, findByStatus, mapRowToTask ...
    // ~100 lines total!
}
```

### Stage 5 Repository: 3 lines!
```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
```

**That's it.** Spring Data JPA implements everything else automatically!

## How It Works

### 1. Entity Class (Task.java)
```java
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // ... fields with JPA annotations
}
```

### 2. Repository Interface
```java
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository provides: save, findById, findAll, delete, count, etc.

    // Custom query - Spring generates SQL from method name!
    List<Task> findByStatus(TaskStatus status);

    // More examples:
    // List<Task> findByTitleContaining(String keyword);
    // List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);
    // Optional<Task> findByTitleAndStatus(String title, TaskStatus status);
}
```

### 3. Query Derivation Magic
Spring Data JPA parses method names and generates queries:

| Method Name | Generated SQL |
|-------------|---------------|
| `findByStatus(status)` | `SELECT * FROM tasks WHERE status = ?` |
| `findByTitleContaining(keyword)` | `SELECT * FROM tasks WHERE title LIKE '%keyword%'` |
| `countByStatus(status)` | `SELECT COUNT(*) FROM tasks WHERE status = ?` |
| `deleteByStatus(status)` | `DELETE FROM tasks WHERE status = ?` |

## What JpaRepository Provides Free

```java
// CREATE
Task save(Task task);
List<Task> saveAll(List<Task> tasks);

// READ
Optional<Task> findById(Long id);
List<Task> findAll();
List<Task> findAllById(List<Long> ids);
long count();
boolean existsById(Long id);

// UPDATE
Task save(Task task);  // Same as create - JPA detects if update needed

// DELETE
void deleteById(Long id);
void delete(Task task);
void deleteAll();

// PAGINATION & SORTING
Page<Task> findAll(Pageable pageable);
List<Task> findAll(Sort sort);
```

## Code Comparison

| Aspect | Stage 4 (JdbcTemplate) | Stage 5 (Spring Data JPA) |
|--------|------------------------|---------------------------|
| Repository | ~100 lines | ~3 lines |
| SQL | Manual | Auto-generated |
| Mapping | Manual RowMapper | Automatic (JPA) |
| Schema | Manual schema.sql | Auto from entities |

## How to Run

```bash
mvn spring-boot:run
```

## Configuration

```properties
# JPA/Hibernate settings
spring.jpa.hibernate.ddl-auto=create-drop  # Auto-create schema!
spring.jpa.show-sql=true                    # See generated SQL
spring.jpa.properties.hibernate.format_sql=true
```

## Entity Annotations Explained

```java
@Entity                    // This class maps to a database table
@Table(name = "tasks")     // Table name (optional if same as class)
public class Task {

    @Id                    // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(nullable = false, length = 100)  // Column constraints
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)  // Store enum as string, not ordinal
    private TaskStatus status;

    @Column(name = "created_at")  // Custom column name
    private LocalDateTime createdAt;
}
```

## Custom Queries (When Method Names Aren't Enough)

```java
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Method name query
    List<Task> findByStatus(TaskStatus status);

    // JPQL query
    @Query("SELECT t FROM Task t WHERE t.status = :status ORDER BY t.createdAt DESC")
    List<Task> findTasksByStatusSorted(@Param("status") TaskStatus status);

    // Native SQL (when needed)
    @Query(value = "SELECT * FROM tasks WHERE status = ?1", nativeQuery = true)
    List<Task> findByStatusNative(String status);
}
```

## What's Next?
Stage 6 adds REST API endpoints - expose this functionality over HTTP!
