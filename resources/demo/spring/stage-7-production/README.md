# Stage 7: Production-Ready Features

## Goal
Add production-quality features: validation, error handling, and DTOs.

## What's New

### 1. Data Transfer Objects (DTOs)
Separate API contracts from database entities:

```
Entity (Database)          DTO (API)
─────────────────          ─────────
Task.java                  TaskResponse.java
  - id                       - id
  - title                    - title
  - description              - description
  - status                   - status
  - createdAt                - createdAt (formatted)

                           CreateTaskRequest.java
                             - title (validated)
                             - description
```

**Why DTOs?**
- Hide internal fields (e.g., passwords, internal IDs)
- Different shapes for different operations
- Validation on request objects
- Versioning API without changing database

### 2. Input Validation
```java
public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    String title,

    @Size(max = 1000, message = "Description too long")
    String description
) {}
```

### 3. Global Exception Handling
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(TaskNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
}
```

### 4. Consistent Error Responses
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": [
    {
      "field": "title",
      "message": "Title is required"
    }
  ],
  "timestamp": "2024-01-15T10:30:00"
}
```

## Project Structure
```
stage-7-production/
├── src/main/java/com/example/taskmanager/
│   ├── Application.java
│   ├── model/
│   │   ├── Task.java
│   │   └── TaskStatus.java
│   ├── repository/
│   │   └── TaskRepository.java
│   ├── service/
│   │   └── TaskService.java
│   ├── controller/
│   │   └── TaskController.java
│   ├── dto/                          # NEW!
│   │   ├── CreateTaskRequest.java
│   │   ├── UpdateTaskRequest.java
│   │   ├── UpdateStatusRequest.java
│   │   ├── TaskResponse.java
│   │   └── ErrorResponse.java
│   └── exception/                    # NEW!
│       ├── TaskNotFoundException.java
│       └── GlobalExceptionHandler.java
```

## Validation Annotations

| Annotation | Description |
|------------|-------------|
| `@NotNull` | Cannot be null |
| `@NotBlank` | Cannot be null, empty, or whitespace (for strings) |
| `@NotEmpty` | Cannot be null or empty (for collections) |
| `@Size(min, max)` | Length constraints |
| `@Min`, `@Max` | Numeric constraints |
| `@Email` | Valid email format |
| `@Pattern` | Regex pattern |
| `@Valid` | Trigger validation on nested object |

## How Validation Works

```java
@PostMapping
public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
    // If validation fails, MethodArgumentNotValidException is thrown
    // GlobalExceptionHandler catches it and returns proper error response
}
```

## Error Response Examples

### Validation Error (400 Bad Request)
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": [
    {
      "field": "title",
      "message": "Title is required"
    },
    {
      "field": "description",
      "message": "Description too long"
    }
  ],
  "timestamp": "2024-01-15T10:30:00"
}
```

### Not Found Error (404)
```json
{
  "error": "NOT_FOUND",
  "message": "Task not found with id: 999",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Server Error (500)
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred",
  "timestamp": "2024-01-15T10:30:00"
}
```

## How to Run

```bash
mvn spring-boot:run
```

## Test Validation

```bash
# Missing title - should return 400
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"description":"No title"}'

# Title too long - should return 400
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"This title is way too long and exceeds the 100 character limit that we have set for task titles in our validation rules"}'

# Valid request - should return 201
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Valid Task","description":"This is valid"}'
```

## Production Checklist

This stage implements:
- [x] Input validation
- [x] Global exception handling
- [x] Consistent error responses
- [x] DTOs for API contracts
- [x] Custom exceptions

Still needed for production (beyond this demo):
- [ ] Authentication/Authorization
- [ ] Rate limiting
- [ ] Logging (SLF4J/Logback)
- [ ] API documentation (OpenAPI/Swagger)
- [ ] Health checks (Actuator)
- [ ] Tests (JUnit, MockMvc)

## Summary: The Complete Journey

| Stage | Key Learning |
|-------|--------------|
| 1 | Manual wiring is painful |
| 2 | Maven manages dependencies |
| 3 | Spring DI eliminates manual wiring |
| 4 | Spring Boot auto-configures everything |
| 5 | Spring Data JPA eliminates SQL boilerplate |
| 6 | REST controllers expose HTTP APIs |
| 7 | Validation & error handling for production |

**Congratulations!** You've built a production-quality REST API from scratch!
