# Stage 4: Spring Boot

## Goal
Experience Spring Boot's auto-configuration magic.

## The Big Changes

### 1. No More Manual Configuration
```java
// Stage 3: Manual configuration class
@Configuration
@ComponentScan("com.example.taskmanager")
@PropertySource("classpath:db.properties")
public class AppConfig { }

// Stage 4: One annotation does it all!
@SpringBootApplication
public class Application { }
```

### 2. No Database Setup Required!
```properties
# H2 embedded database - auto-configured
spring.datasource.url=jdbc:h2:mem:taskdb
# Spring Boot figures out the rest!
```

### 3. JdbcTemplate Instead of Raw JDBC
```java
// Stage 3: Manual connection management
Connection conn = connectionManager.getConnection();
PreparedStatement ps = conn.prepareStatement(sql);
// ... lots of boilerplate

// Stage 4: JdbcTemplate handles it
jdbcTemplate.update(sql, title, description, status);
```

### 4. Simplified Main Class
```java
// Stage 3
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
ConsoleUI ui = context.getBean(ConsoleUI.class);

// Stage 4
SpringApplication.run(Application.class, args);
// Spring Boot handles everything!
```

## What Spring Boot Auto-Configures

When it sees H2 on classpath:
- Creates DataSource with connection pool (HikariCP)
- Configures connection URL, username, password
- Sets up transaction management

When it sees spring-boot-starter-jdbc:
- Creates JdbcTemplate bean
- Configures exception translation

## Project Structure
```
stage-4-spring-boot/
├── pom.xml
├── src/main/java/com/example/taskmanager/
│   ├── Application.java          # @SpringBootApplication
│   ├── model/
│   │   ├── Task.java
│   │   └── TaskStatus.java
│   ├── repository/
│   │   └── TaskRepository.java   # Uses JdbcTemplate
│   ├── service/
│   │   └── TaskService.java
│   └── ui/
│       └── ConsoleUI.java
└── src/main/resources/
    ├── application.properties    # All config here
    └── schema.sql                # Auto-executed on startup!
```

## How to Run

```bash
# Run directly (no build needed)
mvn spring-boot:run

# Or build and run JAR
mvn package
java -jar target/task-manager-1.0-SNAPSHOT.jar
```

## Configuration Comparison

| Aspect | Stage 3 | Stage 4 |
|--------|---------|---------|
| Config file | db.properties | application.properties |
| DB setup | MySQL required | H2 embedded (auto) |
| Connection pool | None | HikariCP (auto) |
| JDBC code | Raw JDBC | JdbcTemplate |
| Main class | Manual context | SpringApplication.run() |

## Key Spring Boot Features Used

### @SpringBootApplication
Combines three annotations:
- `@Configuration` - This is a configuration class
- `@EnableAutoConfiguration` - Enable auto-configuration
- `@ComponentScan` - Scan for components in this package

### application.properties
Central configuration file:
```properties
# Database
spring.datasource.url=jdbc:h2:mem:taskdb

# H2 Console (for debugging)
spring.h2.console.enabled=true

# SQL initialization
spring.sql.init.mode=always
```

### schema.sql
Automatically executed on startup:
```sql
CREATE TABLE IF NOT EXISTS tasks (...);
```

### JdbcTemplate
Eliminates JDBC boilerplate:
```java
// Query with row mapper
List<Task> tasks = jdbcTemplate.query(sql, this::mapRowToTask);

// Update with parameters
jdbcTemplate.update(sql, param1, param2);
```

## What's Still Manual

- Writing SQL queries
- Mapping ResultSet to objects
- Repository method implementations

Stage 5 (Spring Data JPA) eliminates all of this!

## Exercises

1. **Access H2 Console**: Visit http://localhost:8080/h2-console (if running as web app)

2. **Switch to MySQL**: Change application.properties:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
   spring.datasource.username=root
   spring.datasource.password=password
   ```

3. **Add a property**: Create custom property and inject with `@Value`

## What's Next?
Stage 5 introduces Spring Data JPA - write interfaces, Spring writes implementations!
