# Auto-configuration

## Introduction

Auto-configuration is one of Spring Boot's most powerful features. It automatically configures your Spring application based on the dependencies present in your classpath, eliminating the need for extensive manual configuration.

### What is Auto-Configuration?

Auto-configuration is Spring Boot's way of automatically setting up beans and configurations based on:
- Dependencies in your classpath
- Properties you've defined
- Beans you've already configured

**Key Principle**: Convention over Configuration

---

## How Auto-Configuration Works

### The Process

```
Application Startup
       ↓
@SpringBootApplication
       ↓
@EnableAutoConfiguration
       ↓
Scans META-INF/spring.factories
       ↓
Loads Auto-Configuration Classes
       ↓
Evaluates @Conditional Annotations
       ↓
Configures Beans (if conditions met)
       ↓
Application Ready
```

### The @SpringBootApplication Annotation

```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}

// @SpringBootApplication is equivalent to:
@Configuration           // Allows bean definitions
@EnableAutoConfiguration // Enables auto-configuration
@ComponentScan          // Scans for components
```

---

## Conditional Annotations

Auto-configuration uses conditional annotations to determine whether to apply configurations.

### Common @Conditional Annotations

| Annotation | Condition |
|------------|-----------|
| `@ConditionalOnClass` | Class is present in classpath |
| `@ConditionalOnMissingClass` | Class is not in classpath |
| `@ConditionalOnBean` | Bean exists in context |
| `@ConditionalOnMissingBean` | Bean doesn't exist in context |
| `@ConditionalOnProperty` | Property has specific value |
| `@ConditionalOnWebApplication` | Application is a web app |

### How It Works

```java
// Spring Boot's DataSource auto-configuration (simplified)
@Configuration
@ConditionalOnClass(DataSource.class)  // Only if DataSource class exists
public class DataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean  // Only if user hasn't defined their own
    public DataSource dataSource() {
        // Create default DataSource
    }
}
```

**Key insight:** If you define your own bean, auto-configuration backs off (`@ConditionalOnMissingBean`).

---

## Real-World Examples

### Example 1: Adding JPA

```xml
<!-- Just add the dependency -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>
```

**Spring Boot automatically configures:**
- DataSource (H2 in-memory)
- EntityManagerFactory
- TransactionManager
- JPA repositories

```java
// You can immediately use JPA:
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastName(String lastName);
}
```

### Example 2: Adding Web

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**Spring Boot automatically configures:**
- Embedded Tomcat server
- DispatcherServlet
- Jackson for JSON
- Error handling

```java
// You can immediately create REST endpoints:
@RestController
public class UserController {
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}
```

### Example 3: Adding Security

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

**Spring Boot automatically configures:**
- Basic authentication
- Default user (password logged at startup)
- CSRF protection
- Security headers

---

## Customizing Auto-Configuration

### 1. Using Properties (Preferred)

```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=secret

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

server.port=9090
```

### 2. Providing Your Own Beans

```java
@Configuration
public class CustomConfig {

    // This replaces the auto-configured DataSource
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        config.setMaximumPoolSize(20);
        return new HikariDataSource(config);
    }
}
```

### 3. Excluding Auto-Configuration

```java
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Or via properties:
// spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

---

## Debugging Auto-Configuration

### Enable Debug Report

```properties
# application.properties
debug=true
```

### Sample Output

```
============================
CONDITIONS EVALUATION REPORT
============================

Positive matches:
-----------------
   DataSourceAutoConfiguration matched:
      - @ConditionalOnClass found required classes 'javax.sql.DataSource'

Negative matches:
-----------------
   MongoAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.mongodb.client.MongoClient'
```

### Common Issues

| Problem | Solution |
|---------|----------|
| Bean not found | Check if dependency is in classpath |
| Auto-config not applied | Check conditions with `debug=true` |
| Multiple beans conflict | Use `@Primary` or `@Qualifier` |
| Unwanted auto-config | Use `exclude` attribute |

---

## Best Practices

### 1. Let Auto-Configuration Work

```java
// Good - let Spring Boot configure
@SpringBootApplication
public class Application { }

// Bad - unnecessary manual configuration
@Bean
public ObjectMapper objectMapper() { }  // Already auto-configured!
```

### 2. Override via Properties First

```properties
# Good - customize via properties
spring.datasource.hikari.maximum-pool-size=20

# Only create custom beans when properties aren't enough
```

### 3. Use Conditional Beans for Features

```java
@Bean
@ConditionalOnProperty("app.cache.enabled")
public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| What it is | Automatic bean configuration based on classpath |
| How it works | @EnableAutoConfiguration + @Conditional annotations |
| Customization | Properties → Custom beans → Exclusions |
| Debugging | Use `debug=true` for conditions report |
| Best practice | Let auto-config work, override only when needed |

## Next Topic

Continue to [Starter Dependencies](./04-starter-dependencies.md) to learn about Spring Boot's dependency management.
