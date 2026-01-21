# Controllers and REST Endpoints

## What are Controllers?

Controllers handle incoming HTTP requests, process them, and return responses. In Spring, controllers are annotated classes that map URLs to handler methods.

---

## @RestController

For REST APIs, use `@RestController` which combines `@Controller` and `@ResponseBody`.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

---

## Request Mapping Annotations

| Annotation | HTTP Method | Example |
|------------|-------------|---------|
| `@GetMapping` | GET | Retrieve data |
| `@PostMapping` | POST | Create resource |
| `@PutMapping` | PUT | Update resource |
| `@DeleteMapping` | DELETE | Delete resource |
| `@PatchMapping` | PATCH | Partial update |
| `@RequestMapping` | Any | Generic mapping |

```java
// Class-level base path
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping                    // GET /api/products
    @GetMapping("/{id}")           // GET /api/products/123
    @GetMapping("/search")         // GET /api/products/search
    @PostMapping                   // POST /api/products
    @PutMapping("/{id}")           // PUT /api/products/123
    @DeleteMapping("/{id}")        // DELETE /api/products/123
}
```

---

## Handler Method Parameters

### @PathVariable - URL Path Parameters

```java
// GET /api/users/123
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);
}

// GET /api/users/123/orders/456
@GetMapping("/{userId}/orders/{orderId}")
public Order getOrder(@PathVariable Long userId,
                      @PathVariable Long orderId) {
    return orderService.findByUserAndId(userId, orderId);
}
```

### @RequestParam - Query Parameters

```java
// GET /api/users?page=0&size=10&sort=name
@GetMapping
public Page<User> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String sort) {
    return userService.findAll(page, size, sort);
}

// GET /api/products/search?name=phone&minPrice=100
@GetMapping("/search")
public List<Product> search(
        @RequestParam String name,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice) {
    return productService.search(name, minPrice, maxPrice);
}
```

### @RequestBody - JSON Request Body

```java
// POST /api/users
// Body: {"name": "John", "email": "john@example.com"}
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.save(user);
}

// With validation
@PostMapping
public User createUser(@Valid @RequestBody User user) {
    return userService.save(user);
}
```

### @RequestHeader - HTTP Headers

```java
@GetMapping
public String getData(@RequestHeader("Authorization") String authToken,
                      @RequestHeader(value = "X-Custom", required = false) String custom) {
    // Use headers
}
```

---

## Response Types

### Return Object (Automatic JSON)

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id);  // Converted to JSON
}
```

### ResponseEntity (Full Control)

```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
}

@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
    User created = userService.save(user);
    URI location = URI.create("/api/users/" + created.getId());
    return ResponseEntity.created(location).body(created);
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
}
```

### Common ResponseEntity Methods

| Method | Status Code | Use Case |
|--------|-------------|----------|
| `ok(body)` | 200 | Success with data |
| `created(uri)` | 201 | Resource created |
| `noContent()` | 204 | Success, no body |
| `badRequest()` | 400 | Invalid request |
| `notFound()` | 404 | Resource not found |
| `status(code)` | Custom | Any status |

---

## Validation

### Add Validation Dependency

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

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Invalid age")
    private Integer age;

    // Getters and setters
}
```

### Validate in Controller

```java
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
    // If validation fails, MethodArgumentNotValidException is thrown
    User user = userService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
}
```

---

## Exception Handling

### @ExceptionHandler (Controller-Level)

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```

### @ControllerAdvice (Global)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .toList();
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", errors.toString());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

// Error response DTO
public record ErrorResponse(String code, String message) {}
```

---

## Complete CRUD Example

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET /api/products
    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    // GET /api/products/123
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/products/123
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id,
                                          @Valid @RequestBody Product product) {
        if (!productService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        product.setId(id);
        return ResponseEntity.ok(productService.save(product));
    }

    // DELETE /api/products/123
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

## Summary

| Concept | Usage |
|---------|-------|
| `@RestController` | REST API controller (returns JSON) |
| `@RequestMapping` | Base URL path |
| `@GetMapping`, `@PostMapping`, etc. | HTTP method mapping |
| `@PathVariable` | URL path parameters |
| `@RequestParam` | Query string parameters |
| `@RequestBody` | JSON request body |
| `ResponseEntity` | Full HTTP response control |
| `@Valid` | Enable validation |
| `@ControllerAdvice` | Global exception handling |

## Next Topic

Continue to [Exception Handling](./04-exception-handling.md) for advanced error handling patterns.
