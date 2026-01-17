# Common Pitfalls and Troubleshooting

## Introduction

When learning Spring Framework, beginners often encounter recurring errors and misconceptions. This guide covers the most common pitfalls, explains why they occur, and provides solutions to help you debug issues quickly.

---

## Bean-Related Errors

### 1. NoSuchBeanDefinitionException

This is the most common error for Spring beginners. It occurs when Spring cannot find a bean to inject.

**Error Message:**
```
org.springframework.beans.factory.NoSuchBeanDefinitionException:
No qualifying bean of type 'com.example.service.UserService' available
```

**Common Causes and Solutions:**

#### Cause 1: Missing Component Annotation

```java
// WRONG - No annotation, not detected by Spring
public class UserService {
    public User getUser(Long id) {
        // ...
    }
}

// CORRECT - Annotated as a component
@Service
public class UserService {
    public User getUser(Long id) {
        // ...
    }
}
```

#### Cause 2: Package Not Scanned

```java
// Main class in com.example
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Service in com.other.service - NOT scanned!
@Service
public class UserService { }
```

**Solution:** Ensure components are in the same package or sub-packages as your main class, or explicitly configure component scanning:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.other"})
public class Application { }
```

#### Cause 3: Missing @EnableAutoConfiguration

In non-Spring Boot applications:

```java
// WRONG - Missing annotation
@Configuration
@ComponentScan("com.example")
public class AppConfig { }

// CORRECT - Includes @Configuration
@Configuration
@ComponentScan("com.example")
@EnableAutoConfiguration  // Or use @SpringBootApplication
public class AppConfig { }
```

#### Cause 4: Interface Without Implementation

```java
// Interface defined
public interface UserRepository {
    User findById(Long id);
}

// But no implementation exists!
// Spring cannot create a bean for an interface without:
// 1. A concrete implementation with @Repository
// 2. Or Spring Data JPA (which generates implementation)
```

**Debugging Tip:** Enable debug logging to see what beans Spring creates:

```properties
# application.properties
logging.level.org.springframework=DEBUG
```

---

### 2. NoUniqueBeanDefinitionException

Occurs when multiple beans of the same type exist and Spring cannot determine which one to inject.

**Error Message:**
```
org.springframework.beans.factory.NoUniqueBeanDefinitionException:
No qualifying bean of type 'com.example.MessageService' available:
expected single matching bean but found 2: emailService,smsService
```

**The Problem:**

```java
public interface MessageService {
    void send(String message);
}

@Service
public class EmailService implements MessageService { }

@Service
public class SmsService implements MessageService { }

@Service
public class NotificationService {
    @Autowired
    private MessageService messageService;  // Which one?
}
```

**Solutions:**

#### Solution 1: Use @Qualifier

```java
@Service
public class NotificationService {
    @Autowired
    @Qualifier("emailService")  // Specify bean name
    private MessageService messageService;
}
```

#### Solution 2: Use @Primary

```java
@Service
@Primary  // Default choice when multiple beans exist
public class EmailService implements MessageService { }

@Service
public class SmsService implements MessageService { }
```

#### Solution 3: Use Constructor Injection with @Qualifier

```java
@Service
public class NotificationService {
    private final MessageService emailService;
    private final MessageService smsService;

    @Autowired
    public NotificationService(
            @Qualifier("emailService") MessageService emailService,
            @Qualifier("smsService") MessageService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }
}
```

---

### 3. BeanCurrentlyInCreationException (Circular Dependency)

Occurs when two or more beans depend on each other.

**Error Message:**
```
org.springframework.beans.factory.BeanCurrentlyInCreationException:
Error creating bean with name 'serviceA': Requested bean is currently in creation
```

**The Problem:**

```java
@Service
public class ServiceA {
    @Autowired
    private ServiceB serviceB;  // ServiceA needs ServiceB
}

@Service
public class ServiceB {
    @Autowired
    private ServiceA serviceA;  // ServiceB needs ServiceA - Circular!
}
```

**Visual Representation:**
```
ServiceA ──depends on──→ ServiceB
    ↑                        │
    └────depends on──────────┘
```

**Solutions:**

#### Solution 1: Redesign (Best Approach)

Extract shared logic into a third service:

```java
@Service
public class SharedLogicService {
    // Common functionality
}

@Service
public class ServiceA {
    @Autowired
    private SharedLogicService sharedService;
}

@Service
public class ServiceB {
    @Autowired
    private SharedLogicService sharedService;
}
```

#### Solution 2: Use @Lazy

```java
@Service
public class ServiceA {
    @Autowired
    @Lazy  // Creates a proxy, resolves dependency lazily
    private ServiceB serviceB;
}
```

#### Solution 3: Use Setter Injection

```java
@Service
public class ServiceA {
    private ServiceB serviceB;

    @Autowired
    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
```

**Best Practice:** Circular dependencies usually indicate a design problem. Try to refactor your code to eliminate them.

---

### 4. UnsatisfiedDependencyException

Occurs when Spring cannot satisfy a dependency for various reasons.

**Error Message:**
```
org.springframework.beans.factory.UnsatisfiedDependencyException:
Error creating bean with name 'userController': Unsatisfied dependency expressed
through field 'userService'; nested exception is NoSuchBeanDefinitionException
```

**Common Causes:**

1. The dependency bean doesn't exist
2. The dependency is in a package not being scanned
3. The dependency has its own unsatisfied dependencies
4. The dependency failed to initialize

**Debugging Approach:**

```java
// Check if the bean exists
@Autowired
private ApplicationContext context;

public void debug() {
    // List all beans
    String[] beanNames = context.getBeanDefinitionNames();
    for (String name : beanNames) {
        System.out.println(name);
    }

    // Check specific bean
    boolean exists = context.containsBean("userService");
    System.out.println("UserService exists: " + exists);
}
```

---

## Configuration Errors

### 5. Misconfigured Component Scan

**Problem:** Beans not found because they're outside the scan path.

**Default Behavior:**
- `@SpringBootApplication` scans the package it's in and all sub-packages
- Classes in other packages are NOT scanned

**Example Structure:**
```
com.example/
├── Application.java        ← @SpringBootApplication here
├── controller/
│   └── UserController.java ← Scanned
└── service/
    └── UserService.java    ← Scanned

com.other/
└── util/
    └── Helper.java         ← NOT scanned!
```

**Solution:**

```java
// Option 1: Move to correct package (recommended)

// Option 2: Explicit component scan
@SpringBootApplication
@ComponentScan(basePackages = {"com.example", "com.other"})
public class Application { }

// Option 3: Scan specific classes
@SpringBootApplication
@ComponentScan(basePackageClasses = {Application.class, Helper.class})
public class Application { }
```

---

### 6. Missing @Configuration on Config Classes

**Problem:** @Bean methods don't work without @Configuration.

```java
// WRONG - @Bean methods won't be processed
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}

// CORRECT
@Configuration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
```

**Why This Matters:**
- `@Configuration` enables CGLIB proxying
- Bean methods called internally return the same singleton
- Without it, each call creates a new instance

```java
@Configuration
public class AppConfig {
    @Bean
    public ServiceA serviceA() {
        return new ServiceA(commonDependency());  // Returns singleton
    }

    @Bean
    public ServiceB serviceB() {
        return new ServiceB(commonDependency());  // Same singleton
    }

    @Bean
    public CommonDependency commonDependency() {
        return new CommonDependency();
    }
}
```

---

### 7. Bean Scope Misunderstanding

**Problem:** Expecting new instances but getting singletons.

```java
@Service  // Default scope is SINGLETON
public class UserService {
    private User currentUser;  // Shared across all requests!

    public void setCurrentUser(User user) {
        this.currentUser = user;  // DANGEROUS in web apps
    }
}
```

**Solution:** Use appropriate scopes:

```java
// For request-specific data
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedService {
    private User currentUser;  // Now safe per request
}

// Or use prototype scope
@Service
@Scope("prototype")  // New instance each time
public class PrototypeService { }
```

**Scope Reference:**

| Scope | Description | Use Case |
|-------|-------------|----------|
| singleton | Single instance (default) | Stateless services |
| prototype | New instance each time | Stateful objects |
| request | One per HTTP request | Request-specific data |
| session | One per HTTP session | Session data |
| application | One per ServletContext | Global state |

---

## Injection Errors

### 8. Field Injection with Final Fields

**Problem:** Cannot use field injection with final fields.

```java
@Service
public class UserService {
    @Autowired
    private final UserRepository repository;  // ERROR: Cannot inject final field
}
```

**Solution:** Use constructor injection:

```java
@Service
public class UserService {
    private final UserRepository repository;  // Works!

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

// With Lombok (even cleaner)
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
}
```

---

### 9. Injecting Primitive Values

**Problem:** Cannot directly inject primitive values without @Value.

```java
@Service
public class EmailService {
    @Autowired
    private String smtpHost;  // ERROR: No bean of type String

    @Autowired
    private int smtpPort;     // ERROR: No bean of type int
}
```

**Solution:** Use @Value annotation:

```java
@Service
public class EmailService {
    @Value("${smtp.host}")
    private String smtpHost;

    @Value("${smtp.port}")
    private int smtpPort;

    @Value("${smtp.enabled:true}")  // With default value
    private boolean enabled;
}
```

**In application.properties:**
```properties
smtp.host=smtp.gmail.com
smtp.port=587
```

---

### 10. @Autowired on Static Fields

**Problem:** @Autowired doesn't work on static fields.

```java
@Service
public class UtilityService {
    @Autowired
    private static UserRepository repository;  // Will be null!

    public static User getUser(Long id) {
        return repository.findById(id);  // NullPointerException
    }
}
```

**Why:** Static fields belong to the class, not instances. Spring injects into instances.

**Solutions:**

#### Solution 1: Use Instance Methods

```java
@Service
public class UtilityService {
    @Autowired
    private UserRepository repository;

    public User getUser(Long id) {  // Non-static
        return repository.findById(id);
    }
}
```

#### Solution 2: Setter Injection Workaround (Not Recommended)

```java
@Service
public class UtilityService {
    private static UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repo) {
        UtilityService.repository = repo;  // Workaround
    }
}
```

---

## Lifecycle Errors

### 11. NullPointerException in Constructor

**Problem:** Dependencies are null in constructor when using field injection.

```java
@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserService() {
        repository.doSomething();  // NullPointerException!
    }
}
```

**Why:** Field injection happens AFTER constructor execution.

**Lifecycle Order:**
```
1. Constructor called     ← repository is null
2. Dependencies injected  ← repository set
3. @PostConstruct called  ← repository available
```

**Solution:** Use constructor injection or @PostConstruct:

```java
// Option 1: Constructor injection
@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
        repository.doSomething();  // Works!
    }
}

// Option 2: @PostConstruct
@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @PostConstruct
    public void init() {
        repository.doSomething();  // Works!
    }
}
```

---

### 12. Using @Transactional Incorrectly

**Problem:** @Transactional not working as expected.

#### Issue 1: Self-Invocation

```java
@Service
public class OrderService {

    public void processOrder(Order order) {
        // This calls the internal method directly, bypassing proxy
        updateInventory(order);  // @Transactional ignored!
    }

    @Transactional
    public void updateInventory(Order order) {
        // Transaction not created when called from processOrder
    }
}
```

**Solution:** Inject self or use separate service:

```java
@Service
public class OrderService {
    @Autowired
    private InventoryService inventoryService;

    public void processOrder(Order order) {
        inventoryService.updateInventory(order);  // Transaction works
    }
}

@Service
public class InventoryService {
    @Transactional
    public void updateInventory(Order order) {
        // Transaction properly created
    }
}
```

#### Issue 2: Private Methods

```java
@Service
public class UserService {

    @Transactional  // Ignored on private methods!
    private void saveUser(User user) {
        // Not transactional
    }
}
```

**Solution:** Use public methods:

```java
@Service
public class UserService {

    @Transactional
    public void saveUser(User user) {  // Now works
        // Transactional
    }
}
```

---

## Debugging Tips

### Enable Debug Logging

```properties
# application.properties

# Show all auto-configuration decisions
debug=true

# Show bean creation
logging.level.org.springframework.beans=DEBUG

# Show component scanning
logging.level.org.springframework.context=DEBUG

# Show web requests
logging.level.org.springframework.web=DEBUG
```

### Use Actuator for Bean Inspection

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
management.endpoints.web.exposure.include=beans,conditions
```

Access:
- `http://localhost:8080/actuator/beans` - List all beans
- `http://localhost:8080/actuator/conditions` - Auto-configuration report

### Check Auto-Configuration Report

Run with `--debug` flag:
```bash
java -jar myapp.jar --debug
```

This shows:
- Positive matches (what was configured)
- Negative matches (what wasn't configured and why)
- Exclusions
- Unconditional classes

---

## Common Error Quick Reference

| Error | Likely Cause | Quick Fix |
|-------|--------------|-----------|
| `NoSuchBeanDefinitionException` | Missing annotation or not scanned | Add @Service/@Repository, check package |
| `NoUniqueBeanDefinitionException` | Multiple beans of same type | Use @Qualifier or @Primary |
| `BeanCurrentlyInCreationException` | Circular dependency | Redesign, use @Lazy, or setter injection |
| `UnsatisfiedDependencyException` | Dependency chain issue | Check nested bean errors |
| `NullPointerException` in constructor | Field injection timing | Use constructor injection |
| `@Transactional` not working | Self-invocation or private method | Use separate service, public method |
| Bean not found | Wrong package | Check @ComponentScan configuration |

---

## Best Practices to Avoid Pitfalls

1. **Use Constructor Injection**: Prevents most injection-related issues
2. **Keep Main Class in Root Package**: Ensures all sub-packages are scanned
3. **Avoid Circular Dependencies**: Design services with clear responsibilities
4. **Use @Qualifier Proactively**: When creating multiple implementations
5. **Prefer @Service/@Repository**: Over generic @Component for clarity
6. **Enable Debug Logging Early**: When troubleshooting bean issues
7. **Test Bean Configuration**: Write integration tests that load context

---

## Summary

| Category | Common Issues |
|----------|---------------|
| Bean Creation | NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException |
| Dependencies | Circular dependencies, UnsatisfiedDependencyException |
| Configuration | Missing @Configuration, wrong component scan |
| Injection | Static fields, final fields, primitive values |
| Lifecycle | Constructor NPE, @Transactional self-invocation |

## Next Steps

Now that you understand common pitfalls, continue to [Spring Boot](../15-spring-boot/topics/01-what-is-spring-boot.md) to learn how it simplifies Spring development.
