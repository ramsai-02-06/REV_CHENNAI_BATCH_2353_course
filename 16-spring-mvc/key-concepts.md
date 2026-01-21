# Spring MVC Key Concepts

## Overview

Spring MVC is a web framework for building REST APIs and web applications. Built on the Servlet API, it provides annotation-based controllers, declarative request mapping, and automatic JSON handling.

---

## 1. Servlet Foundation

### What is a Servlet?

A Servlet is a Java class that handles HTTP requests. Spring MVC is built on top of the Servlet API.

```
Client Request → Servlet Container (Tomcat) → Servlet → Response
```

### Basic Servlet

```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Hello, " + name + "\"}");
    }
}
```

### Servlet Lifecycle

| Method | When Called |
|--------|-------------|
| `init()` | Once at startup |
| `service()` | Every request (routes to doGet/doPost) |
| `destroy()` | Once at shutdown |

### Why Spring MVC?

Servlets require manual URL parsing, JSON handling, and have no dependency injection. Spring MVC solves all of this.

---

## 2. Spring MVC Architecture

### Request Flow

```
Request → DispatcherServlet → HandlerMapping → Controller → Response
```

| Component | Purpose |
|-----------|---------|
| **DispatcherServlet** | Front controller - receives all requests |
| **HandlerMapping** | Maps URLs to controller methods |
| **Controller** | Processes request, returns data |
| **HttpMessageConverter** | Converts objects to JSON |

### @Controller vs @RestController

| @Controller | @RestController |
|-------------|-----------------|
| Returns view names | Returns data (JSON) |
| Needs @ResponseBody | @ResponseBody included |
| For MVC with templates | For REST APIs |

```java
// REST API - returns JSON
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

---

## 3. Controllers and Request Mapping

### HTTP Method Annotations

| Annotation | HTTP Method | Use Case |
|------------|-------------|----------|
| `@GetMapping` | GET | Retrieve data |
| `@PostMapping` | POST | Create resource |
| `@PutMapping` | PUT | Update resource |
| `@DeleteMapping` | DELETE | Delete resource |
| `@PatchMapping` | PATCH | Partial update |

### Handler Method Parameters

**@PathVariable** - URL path parameters:
```java
// GET /api/users/123
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

**@RequestParam** - Query parameters:
```java
// GET /api/users?page=0&size=10
@GetMapping
public Page<User> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return userService.findAll(page, size);
}
```

**@RequestBody** - JSON request body:
```java
// POST /api/users with JSON body
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.save(user);
}
```

### ResponseEntity

Full control over HTTP response:

```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}

@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
    User saved = userService.save(user);
    URI location = URI.create("/api/users/" + saved.getId());
    return ResponseEntity.created(location).body(saved);
}
```

| Method | Status | Use Case |
|--------|--------|----------|
| `ok(body)` | 200 | Success |
| `created(uri)` | 201 | Resource created |
| `noContent()` | 204 | Success, no body |
| `badRequest()` | 400 | Invalid request |
| `notFound()` | 404 | Resource not found |

---

## 4. Validation

### Setup

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### DTO with Validation

```java
public class UserRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
```

### Using @Valid

```java
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
    // If validation fails, MethodArgumentNotValidException is thrown
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.create(request));
}
```

### Common Validation Annotations

| Annotation | Purpose |
|------------|---------|
| `@NotNull` | Not null |
| `@NotBlank` | Not null, not empty, not whitespace |
| `@Size(min, max)` | String/collection size |
| `@Email` | Valid email format |
| `@Min`, `@Max` | Numeric range |
| `@Pattern` | Regex match |

---

## 5. Exception Handling

### Custom Exceptions

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}
```

### Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            message,
            request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

### Error Response DTO

```java
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String message;
    private String path;

    // Constructor, getters
}
```

---

## 6. Spring WebFlux (Introduction)

### What is WebFlux?

Reactive, non-blocking alternative to Spring MVC for high-concurrency scenarios.

| Spring MVC | Spring WebFlux |
|------------|----------------|
| Blocking, thread-per-request | Non-blocking, event loop |
| Servlet containers (Tomcat) | Netty, Undertow |
| `Object` return types | `Mono<T>`, `Flux<T>` |
| JDBC, JPA | R2DBC, Reactive Mongo |

### Reactive Types

| Type | Description |
|------|-------------|
| `Mono<T>` | 0 or 1 element (like Optional) |
| `Flux<T>` | 0 to N elements (like Stream) |

### WebFlux Controller

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public Mono<User> getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.findAll();
    }
}
```

### When to Use

- **WebFlux**: High concurrency, streaming, microservices with many external calls
- **Spring MVC**: Traditional CRUD, blocking dependencies (JDBC), simpler debugging

---

## Quick Reference

### Essential Annotations

```java
@RestController                     // REST API controller
@RequestMapping("/api/path")        // Base URL

@GetMapping("/{id}")                // GET request
@PostMapping                        // POST request
@PutMapping("/{id}")                // PUT request
@DeleteMapping("/{id}")             // DELETE request

@PathVariable Long id               // URL path parameter
@RequestParam String name           // Query parameter
@RequestBody User user              // JSON body
@Valid                              // Trigger validation

@RestControllerAdvice               // Global exception handler
@ExceptionHandler(Exception.class)  // Handle specific exception
```

### Complete REST Controller

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id,
                                          @Valid @RequestBody Product product) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## Summary Checklist

By the end of this module, you should be able to:

- [ ] Understand Servlet basics and why Spring MVC improves on them
- [ ] Create REST controllers using `@RestController`
- [ ] Map HTTP requests using `@GetMapping`, `@PostMapping`, etc.
- [ ] Extract path variables and request parameters
- [ ] Handle JSON request/response bodies
- [ ] Use `ResponseEntity` for HTTP response control
- [ ] Validate request data with `@Valid` and JSR-303 annotations
- [ ] Implement global exception handling with `@RestControllerAdvice`
- [ ] Understand when to use Spring WebFlux vs Spring MVC
