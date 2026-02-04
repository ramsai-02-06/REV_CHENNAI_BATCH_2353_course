# Week 08 - Interview FAQ

This document contains frequently asked interview questions and comprehensive answers for Week 08 topics: REST API Development and Microservices Fundamentals.

---

## Table of Contents

1. [REST API Fundamentals](#rest-api-fundamentals)
2. [HTTP Methods and Status Codes](#http-methods-and-status-codes)
3. [Spring REST Controllers](#spring-rest-controllers)
4. [DTOs and Validation](#dtos-and-validation)
5. [Exception Handling](#exception-handling)
6. [Microservices Architecture](#microservices-architecture)
7. [Spring Cloud Components](#spring-cloud-components)
8. [Security in Microservices](#security-in-microservices)

---

## REST API Fundamentals

### Q1: What is REST? What are the REST constraints?

**Answer:**

**REST (Representational State Transfer)** is an architectural style for designing networked applications. It relies on stateless, client-server communication using HTTP.

**Six REST Constraints:**

| Constraint | Description |
|------------|-------------|
| **Client-Server** | Separation of concerns; client handles UI, server handles data |
| **Stateless** | Each request contains all needed information; no session state |
| **Cacheable** | Responses must define themselves as cacheable or not |
| **Uniform Interface** | Consistent way to interact with resources |
| **Layered System** | Client can't tell if connected directly to server |
| **Code on Demand** | (Optional) Server can send executable code |

**Uniform Interface sub-constraints:**
1. **Resource identification** - URIs identify resources
2. **Manipulation through representations** - JSON/XML represents resources
3. **Self-descriptive messages** - Headers describe how to process
4. **HATEOAS** - Responses contain links to related actions

```
REST Architecture:

Client ──HTTP Request──> Server
       <──HTTP Response──

       No session state stored
       Self-contained requests
       Standard HTTP methods
```

**Key point:** REST is not a protocol or standard - it's a set of architectural constraints.

---

### Q2: What are the HTTP methods and when should each be used?

**Answer:**

| Method | Purpose | Idempotent | Safe | Request Body |
|--------|---------|------------|------|--------------|
| **GET** | Retrieve resource | Yes | Yes | No |
| **POST** | Create resource | No | No | Yes |
| **PUT** | Replace entire resource | Yes | No | Yes |
| **PATCH** | Partial update | No* | No | Yes |
| **DELETE** | Remove resource | Yes | No | Optional |
| **HEAD** | Get headers only | Yes | Yes | No |
| **OPTIONS** | Get allowed methods | Yes | Yes | No |

**Idempotent** = Multiple identical requests have same effect as one
**Safe** = Doesn't modify server state

**Usage examples:**

```
GET    /users          - List all users
GET    /users/123      - Get user 123
POST   /users          - Create new user
PUT    /users/123      - Replace user 123 completely
PATCH  /users/123      - Update specific fields of user 123
DELETE /users/123      - Delete user 123
```

**PUT vs PATCH:**
```java
// PUT - Replace entire resource
PUT /users/123
{
  "name": "John",
  "email": "john@example.com",
  "phone": "555-1234"  // Must include all fields
}

// PATCH - Update specific fields
PATCH /users/123
{
  "phone": "555-9999"  // Only the field being changed
}
```

---

### Q3: Explain HTTP status codes and when to use them.

**Answer:**

**Status code ranges:**

| Range | Category | Description |
|-------|----------|-------------|
| 1xx | Informational | Request received, continuing |
| 2xx | Success | Request successfully processed |
| 3xx | Redirection | Further action needed |
| 4xx | Client Error | Bad request from client |
| 5xx | Server Error | Server failed to process |

**Common status codes:**

```
2xx SUCCESS
├── 200 OK              - Request succeeded
├── 201 Created         - Resource created (POST)
├── 204 No Content      - Success, no body (DELETE)
└── 202 Accepted        - Accepted for async processing

4xx CLIENT ERRORS
├── 400 Bad Request     - Malformed request syntax
├── 401 Unauthorized    - Authentication required
├── 403 Forbidden       - Authenticated but not authorized
├── 404 Not Found       - Resource doesn't exist
├── 405 Method Not Allowed - HTTP method not supported
├── 409 Conflict        - Resource state conflict
├── 422 Unprocessable   - Validation errors
└── 429 Too Many Requests - Rate limit exceeded

5xx SERVER ERRORS
├── 500 Internal Server Error - Generic server error
├── 502 Bad Gateway     - Invalid response from upstream
├── 503 Service Unavailable - Server temporarily down
└── 504 Gateway Timeout - Upstream server timeout
```

**When to use each:**

```java
// 200 OK - Successful GET or PUT
return ResponseEntity.ok(user);

// 201 Created - Successful POST
URI location = URI.create("/users/" + user.getId());
return ResponseEntity.created(location).body(user);

// 204 No Content - Successful DELETE
return ResponseEntity.noContent().build();

// 400 Bad Request - Invalid input format
// 404 Not Found - Resource doesn't exist
// 409 Conflict - e.g., duplicate email
// 422 Unprocessable Entity - Validation errors
```

---

## HTTP Methods and Status Codes

### Q4: What is the difference between 401 and 403?

**Answer:**

| Code | Name | Meaning |
|------|------|---------|
| **401** | Unauthorized | Not authenticated (who are you?) |
| **403** | Forbidden | Authenticated but not authorized (you can't do this) |

**401 Unauthorized:**
- User hasn't provided credentials
- Credentials are invalid
- Token expired
- Response should include `WWW-Authenticate` header

**403 Forbidden:**
- User is authenticated (we know who they are)
- User doesn't have permission for this resource
- Re-authenticating won't help

**Example scenarios:**

```
Scenario: Access admin endpoint /admin/users

Anonymous user → 401 Unauthorized (need to log in)
Regular user   → 403 Forbidden (logged in, but not admin)
Admin user     → 200 OK (logged in and authorized)
```

```java
@GetMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public List<User> getAdminData() {
    // Returns 401 if not authenticated
    // Returns 403 if authenticated but not admin
    return userService.findAll();
}
```

---

### Q5: What makes a good REST API URL design?

**Answer:**

**Best practices:**

1. **Use nouns, not verbs** (resources, not actions)
2. **Use plural nouns** for collections
3. **Use lowercase and hyphens**
4. **Nest resources for relationships**
5. **Keep URLs simple and intuitive**

**Good vs Bad examples:**

| Bad | Good | Reason |
|-----|------|--------|
| /getUsers | /users | Use HTTP methods, not URL verbs |
| /user | /users | Plural for collections |
| /Users | /users | Lowercase |
| /get_user_by_id | /users/{id} | Path variables for IDs |
| /users/123/getOrders | /users/123/orders | Nested resources |

**URL hierarchy:**

```
/users                     - All users
/users/{id}               - Single user
/users/{id}/orders        - User's orders
/users/{id}/orders/{orderId} - Specific order

Query parameters for filtering/sorting:
/users?status=active
/users?sort=name&order=asc
/users?page=2&size=10
```

**Resource naming conventions:**

```
GET    /articles                - List articles
POST   /articles                - Create article
GET    /articles/{id}           - Get article
PUT    /articles/{id}           - Update article
DELETE /articles/{id}           - Delete article
GET    /articles/{id}/comments  - Get article's comments
POST   /articles/{id}/comments  - Add comment to article
```

---

## Spring REST Controllers

### Q6: Explain @RestController and how to build REST endpoints.

**Answer:**

`@RestController` = `@Controller` + `@ResponseBody`

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    // GET /api/users/123
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserDTO created = userService.create(request);
        URI location = URI.create("/api/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    // PUT /api/users/123
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/users/123
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/users?status=active&page=0&size=10
    @GetMapping(params = "status")
    public Page<UserDTO> getUsersByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.findByStatus(status, PageRequest.of(page, size));
    }
}
```

**Key annotations:**

| Annotation | Purpose |
|------------|---------|
| `@RestController` | Marks class as REST controller |
| `@RequestMapping` | Base URL for all methods |
| `@GetMapping` | Handle GET requests |
| `@PostMapping` | Handle POST requests |
| `@PutMapping` | Handle PUT requests |
| `@DeleteMapping` | Handle DELETE requests |
| `@PathVariable` | Extract URL path variables |
| `@RequestParam` | Extract query parameters |
| `@RequestBody` | Bind JSON body to object |
| `@Valid` | Trigger validation |

---

### Q7: What is ResponseEntity and when should you use it?

**Answer:**

`ResponseEntity` represents the complete HTTP response: status code, headers, and body.

**When to use:**
- Need specific status codes (201, 204, 404)
- Need custom headers
- Conditional responses
- Following REST conventions precisely

**Basic usage:**

```java
// 200 OK with body
return ResponseEntity.ok(user);

// 201 Created with location
URI location = URI.create("/users/" + user.getId());
return ResponseEntity.created(location).body(user);

// 204 No Content
return ResponseEntity.noContent().build();

// 404 Not Found
return ResponseEntity.notFound().build();

// 400 Bad Request
return ResponseEntity.badRequest().body(errors);
```

**Advanced usage:**

```java
// Custom status code
return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);

// Custom headers
return ResponseEntity.ok()
    .header("X-Custom-Header", "value")
    .header("Cache-Control", "no-cache")
    .body(data);

// Content type
return ResponseEntity.ok()
    .contentType(MediaType.APPLICATION_JSON)
    .body(data);

// ETag for caching
return ResponseEntity.ok()
    .eTag(resource.getVersion().toString())
    .body(resource);
```

**Complete controller example:**

```java
@GetMapping("/{id}")
public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
    Optional<UserDTO> user = userService.findById(id);

    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());  // 200 + body
    } else {
        return ResponseEntity.notFound().build();  // 404
    }

    // Or using functional style:
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

---

## DTOs and Validation

### Q8: What are DTOs? Why use them instead of entities?

**Answer:**

**DTO (Data Transfer Object)** is an object that carries data between processes/layers. In REST APIs, DTOs define the request/response format.

**Why use DTOs:**

| Concern | Without DTOs | With DTOs |
|---------|--------------|-----------|
| API changes | Break database schema | Independent from entity |
| Security | May expose sensitive fields | Control what's exposed |
| Flexibility | Entity structure dictates API | Shape responses as needed |
| Validation | Mixed entity/API validation | Separate validation rules |

**Example entity vs DTOs:**

```java
// Entity - database model
@Entity
public class User {
    @Id
    private Long id;
    private String email;
    private String passwordHash;  // Sensitive!
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean active;

    @OneToMany
    private List<Order> orders;  // May cause N+1
}

// Response DTO - what API returns
public class UserDTO {
    private Long id;
    private String email;
    private String role;
    private boolean active;
    // No passwordHash, no orders
}

// Create Request DTO - what API receives
public class CreateUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}

// Update Request DTO - different validation
public class UpdateUserRequest {
    @Email
    private String email;  // Optional for update

    @Size(min = 8)
    private String password;  // Optional for update
}
```

**Mapping between entity and DTO:**

```java
@Service
public class UserService {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    // Or use MapStruct for automatic mapping
    @Mapper(componentModel = "spring")
    public interface UserMapper {
        UserDTO toDTO(User user);
        User toEntity(CreateUserRequest request);
    }
}
```

---

### Q9: How do you validate request data in Spring?

**Answer:**

Use Bean Validation (JSR-380) with `@Valid` annotation.

**Setup:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**Request DTO with validation:**

```java
public class CreateUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    @Pattern(regexp = ".*[A-Z].*", message = "Must contain uppercase")
    @Pattern(regexp = ".*[0-9].*", message = "Must contain digit")
    private String password;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Must be at least 18")
    @Max(value = 120, message = "Invalid age")
    private Integer age;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
}
```

**Controller with validation:**

```java
@PostMapping
public ResponseEntity<UserDTO> createUser(
        @Valid @RequestBody CreateUserRequest request) {
    // Only reached if validation passes
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.create(request));
}
```

**Common validation annotations:**

| Annotation | Purpose |
|------------|---------|
| `@NotNull` | Not null |
| `@NotEmpty` | Not null or empty (strings, collections) |
| `@NotBlank` | Not null, not empty, not whitespace |
| `@Size(min, max)` | String/collection size |
| `@Min`, `@Max` | Numeric range |
| `@Email` | Valid email format |
| `@Pattern` | Regex match |
| `@Past`, `@Future` | Date validation |
| `@Positive`, `@Negative` | Number sign |

---

## Exception Handling

### Q10: How do you handle exceptions in REST APIs?

**Answer:**

Use `@RestControllerAdvice` for global exception handling.

**Error response DTO:**

```java
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;

    // Constructor, getters, setters

    public static class FieldError {
        private String field;
        private String message;
    }
}
```

**Global exception handler:**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Not Found");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fe -> new ErrorResponse.FieldError(
                fe.getField(),
                fe.getDefaultMessage()
            ))
            .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("Validation Failed");
        error.setMessage("Invalid request data");
        error.setPath(request.getRequestURI());
        error.setFieldErrors(fieldErrors);

        return ResponseEntity.badRequest().body(error);
    }

    // Handle business logic exceptions
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        error.setError("Business Rule Violation");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(error);
    }

    // Catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error", ex);

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError("Internal Server Error");
        error.setMessage("An unexpected error occurred");
        error.setPath(request.getRequestURI());

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
```

---

## Microservices Architecture

### Q11: What are microservices? What are the benefits and challenges?

**Answer:**

**Microservices** are small, independently deployable services that work together to form a complete application. Each service focuses on a specific business capability.

**Monolith vs Microservices:**

```
MONOLITH                          MICROSERVICES
┌─────────────────────┐           ┌──────┐ ┌──────┐ ┌──────┐
│    All Features     │           │User  │ │Order │ │Pay   │
│  - User Management  │    →      │Svc   │ │Svc   │ │Svc   │
│  - Orders           │           └──────┘ └──────┘ └──────┘
│  - Payments         │              ↓         ↓        ↓
│  - Inventory        │           ┌──────┐ ┌──────┐ ┌──────┐
│                     │           │User  │ │Order │ │Pay   │
│  Single Database    │           │DB    │ │DB    │ │DB    │
└─────────────────────┘           └──────┘ └──────┘ └──────┘
```

**Benefits:**

| Benefit | Description |
|---------|-------------|
| **Independent Deployment** | Deploy services without affecting others |
| **Technology Flexibility** | Each service can use different tech stack |
| **Scalability** | Scale individual services based on demand |
| **Team Autonomy** | Small teams own entire services |
| **Fault Isolation** | Failure in one service doesn't crash all |
| **Easier to Understand** | Smaller codebases are manageable |

**Challenges:**

| Challenge | Description |
|-----------|-------------|
| **Distributed Complexity** | Network latency, partial failures |
| **Data Consistency** | No shared transactions |
| **Service Communication** | API contracts, versioning |
| **Operational Overhead** | More services to deploy/monitor |
| **Testing Complexity** | Integration testing across services |
| **Debugging** | Tracing requests across services |

**Key principles:**
1. Single Responsibility - one business capability per service
2. Database per Service - no shared databases
3. API Communication - REST or messaging
4. Decentralized Governance - teams choose their tools
5. Design for Failure - expect services to fail

---

### Q12: What is the 12-Factor App methodology?

**Answer:**

The **12-Factor App** is a methodology for building cloud-native, scalable applications.

| Factor | Description |
|--------|-------------|
| **1. Codebase** | One codebase in version control, many deploys |
| **2. Dependencies** | Explicitly declare and isolate dependencies |
| **3. Config** | Store config in environment variables |
| **4. Backing Services** | Treat databases, queues as attached resources |
| **5. Build, Release, Run** | Strictly separate build and run stages |
| **6. Processes** | Execute app as stateless processes |
| **7. Port Binding** | Export services via port binding |
| **8. Concurrency** | Scale out via the process model |
| **9. Disposability** | Fast startup and graceful shutdown |
| **10. Dev/Prod Parity** | Keep development and production similar |
| **11. Logs** | Treat logs as event streams |
| **12. Admin Processes** | Run admin tasks as one-off processes |

**Spring Boot implementation:**

```yaml
# Factor 3: Config in environment
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}

# Factor 4: Backing services
spring:
  redis:
    host: ${REDIS_HOST}
  rabbitmq:
    host: ${RABBITMQ_HOST}

# Factor 7: Port binding
server:
  port: ${PORT:8080}

# Factor 11: Logs as streams
logging:
  pattern:
    console: "%d{ISO8601} [%t] %-5level %logger{36} - %msg%n"
```

---

## Spring Cloud Components

### Q13: Explain Service Discovery with Eureka.

**Answer:**

**Service Discovery** allows services to find each other dynamically without hardcoded URLs.

**How it works:**

```
┌───────────────────────────────────────────┐
│            Eureka Server                   │
│  ┌─────────────────────────────────────┐  │
│  │ Service Registry                    │  │
│  │ - user-service: 192.168.1.10:8081  │  │
│  │ - user-service: 192.168.1.11:8081  │  │
│  │ - order-service: 192.168.1.20:8082 │  │
│  └─────────────────────────────────────┘  │
└───────────────────────────────────────────┘
        ↑ register        ↓ discover
   ┌────────────┐    ┌────────────┐
   │ User       │    │ Order      │
   │ Service    │    │ Service    │
   └────────────┘    └────────────┘
```

**Eureka Server setup:**

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

```yaml
# eureka-server application.yml
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

**Client service registration:**

```java
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

```yaml
# user-service application.yml
spring:
  application:
    name: user-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

**Calling other services:**

```java
@Service
public class OrderService {

    @Autowired
    @LoadBalanced  // Enable client-side load balancing
    private RestTemplate restTemplate;

    public User getUser(Long userId) {
        // Use service name instead of URL
        return restTemplate.getForObject(
            "http://user-service/api/users/" + userId,
            User.class
        );
    }
}
```

---

### Q14: What is the Circuit Breaker pattern? How does Resilience4j implement it?

**Answer:**

**Circuit Breaker** prevents cascading failures by failing fast when a service is unavailable.

**States:**

```
        success                    failure threshold reached
     ┌──────────┐                      ┌──────────┐
     │          │                      │          │
     │  CLOSED  │─────────────────────>│   OPEN   │
     │ (normal) │                      │(fail fast)│
     │          │                      │          │
     └──────────┘                      └──────────┘
          ↑                                  │
          │                          timeout │
          │                                  ↓
          │         success            ┌──────────┐
          └────────────────────────────│HALF-OPEN │
                                       │ (testing)│
                  failure              └──────────┘
              ┌───────────────────────────────┘
              ↓
         back to OPEN
```

**Resilience4j configuration:**

```yaml
resilience4j:
  circuitbreaker:
    instances:
      userService:
        failure-rate-threshold: 50        # Open when 50% fail
        slow-call-rate-threshold: 50      # Open when 50% are slow
        slow-call-duration-threshold: 2s  # What's "slow"
        wait-duration-in-open-state: 30s  # Time in OPEN state
        permitted-calls-in-half-open-state: 10  # Test calls
        sliding-window-size: 100          # Calls to evaluate
```

**Usage:**

```java
@Service
public class OrderService {

    private final UserServiceClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
    public User getUser(Long userId) {
        return userClient.getUser(userId);
    }

    // Fallback when circuit is open or call fails
    public User getUserFallback(Long userId, Exception ex) {
        log.warn("User service unavailable, returning default", ex);
        return User.builder()
            .id(userId)
            .name("Unknown User")
            .build();
    }
}
```

**Combining with Retry:**

```java
@CircuitBreaker(name = "userService", fallbackMethod = "fallback")
@Retry(name = "userService")
public User getUser(Long userId) {
    return userClient.getUser(userId);
}
```

---

### Q15: What is an API Gateway? What does Spring Cloud Gateway provide?

**Answer:**

**API Gateway** is a single entry point for all client requests, handling cross-cutting concerns.

**Responsibilities:**

```
┌──────────────────────────────────────────────────────┐
│                    API Gateway                        │
│                                                       │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────┐  │
│  │   Routing   │  │ Auth/Security│  │Rate Limiting│  │
│  └─────────────┘  └──────────────┘  └────────────┘  │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────┐  │
│  │Load Balance │  │   Logging    │  │   Caching  │  │
│  └─────────────┘  └──────────────┘  └────────────┘  │
└──────────────────────────────────────────────────────┘
           │              │              │
           ↓              ↓              ↓
     ┌──────────┐  ┌──────────┐  ┌──────────┐
     │ User Svc │  │Order Svc │  │ Pay Svc  │
     └──────────┘  └──────────┘  └──────────┘
```

**Spring Cloud Gateway configuration:**

```yaml
spring:
  cloud:
    gateway:
      routes:
        # Route to user service
        - id: user-service
          uri: lb://user-service  # Load balanced
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1

        # Route to order service
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
            - Method=GET,POST
          filters:
            - AddRequestHeader=X-Request-Source, gateway
            - RewritePath=/api/orders/(?<path>.*), /orders/$\{path}

        # Route with authentication
        - id: admin-service
          uri: lb://admin-service
          predicates:
            - Path=/api/admin/**
            - Header=X-Admin-Key, secret.*
```

**Custom filter example:**

```java
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest()
            .getHeaders()
            .getFirst("Authorization");

        if (token == null || !isValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Run early
    }
}
```

---

## Security in Microservices

### Q16: How do you secure microservices with OAuth2 and JWT?

**Answer:**

**OAuth2 flow in microservices:**

```
┌────────┐    ┌──────────────────┐    ┌────────────────┐
│ Client │───>│ Authorization    │───>│ Resource       │
│        │    │ Server (Keycloak)│    │ Server (API)   │
└────────┘    └──────────────────┘    └────────────────┘

1. Client authenticates with Auth Server
2. Auth Server returns JWT access token
3. Client sends JWT with API requests
4. Resource Server validates JWT
```

**Resource Server configuration:**

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://keycloak:8080/realms/myapp
```

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter()))
            );
        return http.build();
    }

    private JwtAuthenticationConverter jwtConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("roles");
        converter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }
}
```

**Accessing JWT claims in controller:**

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public UserDTO getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        String email = jwt.getClaim("email");
        List<String> roles = jwt.getClaim("roles");

        return userService.findById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }
}
```

**Service-to-service authentication:**

```java
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return template -> {
            Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
            if (auth != null && auth.getCredentials() instanceof Jwt) {
                Jwt jwt = (Jwt) auth.getCredentials();
                template.header("Authorization", "Bearer " + jwt.getTokenValue());
            }
        };
    }
}
```

---

## Summary

| Topic | Key Concepts |
|-------|--------------|
| **REST Fundamentals** | HTTP methods, status codes, URL design, statelessness |
| **Spring REST** | @RestController, ResponseEntity, @PathVariable, @RequestBody |
| **DTOs** | Separate API from entities, control exposed data |
| **Validation** | @Valid, Bean Validation annotations |
| **Exception Handling** | @RestControllerAdvice, @ExceptionHandler |
| **Microservices** | Independent services, bounded contexts, 12-Factor |
| **Service Discovery** | Eureka, @EnableDiscoveryClient, @LoadBalanced |
| **Resilience** | Circuit Breaker, Retry, Fallback |
| **API Gateway** | Routing, filtering, cross-cutting concerns |
| **Security** | OAuth2, JWT, Resource Server |

---

*Week 08 covers REST API development and microservices fundamentals essential for building distributed systems.*
