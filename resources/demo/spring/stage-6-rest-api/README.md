# Stage 6: REST API

## Goal
Expose task management functionality over HTTP as a RESTful API.

## The Transformation

### Stage 5: Console-based
```
User → Console Menu → TaskService → TaskRepository → Database
```

### Stage 6: HTTP-based
```
HTTP Client → REST Controller → TaskService → TaskRepository → Database
     ↓
  (Postman, curl, browser, frontend app)
```

## REST Endpoints

| Method | URL | Description | Request Body | Response |
|--------|-----|-------------|--------------|----------|
| GET | `/api/tasks` | List all tasks | - | `[Task, Task, ...]` |
| GET | `/api/tasks/{id}` | Get task by ID | - | `Task` |
| POST | `/api/tasks` | Create new task | `{title, description}` | `Task` |
| PUT | `/api/tasks/{id}` | Update task | `{title, description}` | `Task` |
| PATCH | `/api/tasks/{id}/status` | Update status | `{status}` | `Task` |
| DELETE | `/api/tasks/{id}` | Delete task | - | - |
| GET | `/api/tasks/status/{status}` | Filter by status | - | `[Task, ...]` |

## Key Annotations

### @RestController
Combines `@Controller` + `@ResponseBody`:
```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    // All methods return JSON automatically
}
```

### HTTP Method Mappings
```java
@GetMapping           // GET request
@PostMapping          // POST request
@PutMapping           // PUT request
@PatchMapping         // PATCH request
@DeleteMapping        // DELETE request
```

### Path Variables & Request Body
```java
@GetMapping("/{id}")
public Task getTask(@PathVariable Long id) { }

@PostMapping
public Task createTask(@RequestBody CreateTaskRequest request) { }
```

### Response Status
```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)  // Returns 201 instead of 200
public Task createTask(...) { }
```

## How to Run

```bash
mvn spring-boot:run
```

Server starts at `http://localhost:8080`

## Test with curl

```bash
# List all tasks
curl http://localhost:8080/api/tasks

# Get specific task
curl http://localhost:8080/api/tasks/1

# Create task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn REST","description":"Build REST APIs"}'

# Update task
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated Title","description":"Updated description"}'

# Update status only
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"COMPLETED"}'

# Delete task
curl -X DELETE http://localhost:8080/api/tasks/1

# Filter by status
curl http://localhost:8080/api/tasks/status/PENDING
```

## JSON Response Examples

### Single Task
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Complete the tutorial",
  "status": "IN_PROGRESS",
  "createdAt": "2024-01-15T10:30:00"
}
```

### Task List
```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "description": "Complete the tutorial",
    "status": "IN_PROGRESS",
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "title": "Build REST API",
    "description": "Create endpoints",
    "status": "PENDING",
    "createdAt": "2024-01-15T11:00:00"
  }
]
```

## Code Changes

| Stage 5 | Stage 6 |
|---------|---------|
| `ui/ConsoleUI.java` | `controller/TaskController.java` |
| Scanner input | HTTP requests |
| System.out output | JSON responses |
| Console menu | REST endpoints |

## What Spring Boot Auto-Configures

With `spring-boot-starter-web`:
- Embedded Tomcat server (port 8080)
- Jackson JSON serializer
- Spring MVC DispatcherServlet
- Error handling
- Content negotiation

## Project Structure
```
stage-6-rest-api/
├── pom.xml
├── src/main/java/com/example/taskmanager/
│   ├── Application.java
│   ├── model/
│   │   ├── Task.java
│   │   └── TaskStatus.java
│   ├── repository/
│   │   └── TaskRepository.java
│   ├── service/
│   │   └── TaskService.java
│   └── controller/              # NEW!
│       └── TaskController.java
└── src/main/resources/
    └── application.properties
```

## REST Best Practices Applied

1. **Resource-based URLs**: `/api/tasks`, not `/api/getTasks`
2. **HTTP methods for actions**: GET=read, POST=create, PUT=update, DELETE=delete
3. **Plural nouns**: `/tasks`, not `/task`
4. **Status codes**: 200 OK, 201 Created, 204 No Content, 404 Not Found
5. **JSON format**: Standard data interchange format

## What's Next?
Stage 7 adds production touches: validation, error handling, and DTOs!
