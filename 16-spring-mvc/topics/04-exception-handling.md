# Exception Handling

## Overview

Proper exception handling in REST APIs ensures consistent error responses and improves API usability.

---

## Custom Exceptions

```java
// Base exception
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

// Specific exceptions
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
```

---

## Error Response DTO

```java
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    // Getters
}
```

---

## Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle custom API exceptions
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            ex.getStatus(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            message,
            request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    // Handle missing parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Missing parameter: " + ex.getParameterName(),
            request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

## Using Exceptions in Controllers

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @PostMapping
    public User createUser(@Valid @RequestBody UserRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        return userService.create(request);
    }
}
```

---

## Error Response Examples

### 404 Not Found
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "User not found with id: 123",
    "path": "/api/users/123"
}
```

### 400 Bad Request (Validation)
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Bad Request",
    "message": "email: must be a valid email, name: must not be blank",
    "path": "/api/users"
}
```

---

## Summary

| Component | Purpose |
|-----------|---------|
| Custom exceptions | Domain-specific errors |
| `@RestControllerAdvice` | Global exception handling |
| `@ExceptionHandler` | Handle specific exception types |
| ErrorResponse DTO | Consistent error format |

## Next Topic

Continue to [Spring WebFlux](./05-spring-webflux.md) to learn about reactive web programming.
