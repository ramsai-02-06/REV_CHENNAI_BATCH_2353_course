# Spring WebFlux Introduction

## What is Spring WebFlux?

Spring WebFlux is a reactive web framework that provides non-blocking, asynchronous request handling. It's an alternative to Spring MVC for building reactive applications.

```
┌─────────────────────────────────────────────────────────────┐
│                    Spring Web Stack                          │
├─────────────────────────────┬───────────────────────────────┤
│       Spring MVC            │       Spring WebFlux           │
│   (Servlet, Blocking)       │   (Reactive, Non-blocking)     │
├─────────────────────────────┼───────────────────────────────┤
│   Servlet Container         │   Netty / Undertow             │
│   (Tomcat, Jetty)           │   (Event Loop)                 │
├─────────────────────────────┼───────────────────────────────┤
│   Thread-per-request        │   Event-driven                 │
│   Synchronous               │   Asynchronous                 │
└─────────────────────────────┴───────────────────────────────┘
```

---

## When to Use WebFlux

### Use WebFlux When:
- High concurrency with limited resources
- Streaming data (real-time feeds, SSE)
- Microservices with many external calls
- Non-blocking I/O requirements

### Use Spring MVC When:
- Traditional CRUD applications
- Blocking dependencies (JDBC)
- Team is familiar with imperative programming
- Simpler debugging requirements

---

## Reactive Types

WebFlux uses Project Reactor's types:

| Type | Description | Analogous To |
|------|-------------|--------------|
| `Mono<T>` | 0 or 1 element | `Optional<T>` or single async result |
| `Flux<T>` | 0 to N elements | `Stream<T>` or async collection |

```java
// Mono - single value (or empty)
Mono<User> findById(Long id);

// Flux - multiple values (stream)
Flux<User> findAll();
```

---

## Setup

### Dependencies

```xml
<!-- Spring WebFlux (instead of spring-boot-starter-web) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- Reactive database (R2DBC for SQL) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<dependency>
    <groupId>io.r2dbc</groupId>
    <artifactId>r2dbc-h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Note:** Don't mix `spring-boot-starter-web` and `spring-boot-starter-webflux` - choose one.

---

## Reactive Controllers

### Basic Controller

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Return single user
    @GetMapping("/{id}")
    public Mono<User> getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    // Return multiple users
    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.findAll();
    }

    // Create user
    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userService.save(user);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteById(id);
    }
}
```

### With ResponseEntity

```java
@GetMapping("/{id}")
public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
}

@PostMapping
public Mono<ResponseEntity<User>> createUser(@RequestBody User user) {
    return userService.save(user)
        .map(saved -> ResponseEntity
            .created(URI.create("/api/users/" + saved.getId()))
            .body(saved));
}
```

---

## Reactive Repository

```java
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Flux<User> findByLastName(String lastName);

    Mono<User> findByEmail(String email);
}
```

---

## Service Layer

```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<Void> deleteById(Long id) {
        return userRepository.deleteById(id);
    }
}
```

---

## Chaining Operations

```java
@GetMapping("/{id}/orders")
public Flux<Order> getUserOrders(@PathVariable Long id) {
    return userService.findById(id)
        .flatMapMany(user -> orderService.findByUserId(user.getId()));
}

@PostMapping
public Mono<User> createUser(@RequestBody User user) {
    return userService.findByEmail(user.getEmail())
        .flatMap(existing -> Mono.error(new BadRequestException("Email exists")))
        .switchIfEmpty(userService.save(user))
        .cast(User.class);
}
```

---

## Error Handling

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }
}

// In controller
@GetMapping("/{id}")
public Mono<User> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("User", id)));
}
```

---

## WebClient (Reactive HTTP Client)

```java
@Service
public class ExternalApiService {

    private final WebClient webClient;

    public ExternalApiService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.example.com").build();
    }

    public Mono<Product> getProduct(Long id) {
        return webClient.get()
            .uri("/products/{id}", id)
            .retrieve()
            .bodyToMono(Product.class);
    }

    public Flux<Product> getAllProducts() {
        return webClient.get()
            .uri("/products")
            .retrieve()
            .bodyToFlux(Product.class);
    }

    public Mono<Product> createProduct(Product product) {
        return webClient.post()
            .uri("/products")
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product.class);
    }
}
```

---

## Spring MVC vs WebFlux Comparison

| Aspect | Spring MVC | Spring WebFlux |
|--------|------------|----------------|
| **Model** | Blocking, thread-per-request | Non-blocking, event loop |
| **Server** | Tomcat, Jetty (Servlet) | Netty, Undertow |
| **Return Types** | Object, ResponseEntity | Mono, Flux |
| **Database** | JDBC, JPA | R2DBC, Reactive Mongo |
| **HTTP Client** | RestTemplate | WebClient |
| **Concurrency** | Thread pool | Small thread count |
| **Debugging** | Easier | More complex |
| **Learning Curve** | Lower | Higher |

### Code Comparison

```java
// Spring MVC (blocking)
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException());
}

// Spring WebFlux (reactive)
@GetMapping("/{id}")
public Mono<User> getUser(@PathVariable Long id) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException()));
}
```

---

## Configuration

```properties
# Server (Netty by default with WebFlux)
server.port=8080

# R2DBC Database
spring.r2dbc.url=r2dbc:h2:mem:///testdb
spring.r2dbc.username=sa
spring.r2dbc.password=
```

---

## Summary

| Concept | Description |
|---------|-------------|
| **WebFlux** | Reactive, non-blocking web framework |
| **Mono** | 0 or 1 async result |
| **Flux** | 0 to N async results (stream) |
| **WebClient** | Reactive HTTP client |
| **R2DBC** | Reactive database connectivity |
| **Use Case** | High concurrency, streaming, microservices |

### When to Choose

```
High Concurrency + Streaming → WebFlux
Traditional CRUD + Team Familiar → Spring MVC
```

## Additional Resources

- [Project Reactor Documentation](https://projectreactor.io/docs)
- [Spring WebFlux Reference](https://docs.spring.io/spring-framework/reference/web/webflux.html)
