# Exercise: Design and Implement a RESTful API

## Objective
Design and implement a RESTful API following REST best practices, including proper HTTP methods, status codes, versioning, HATEOAS, and documentation.

## Requirements

### API: Task Management System
Design a REST API for managing projects and tasks.

### Resources and Endpoints

#### Projects
| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | /api/v1/projects | List all projects | 200 |
| GET | /api/v1/projects/{id} | Get project by ID | 200, 404 |
| POST | /api/v1/projects | Create project | 201 |
| PUT | /api/v1/projects/{id} | Update project | 200, 404 |
| PATCH | /api/v1/projects/{id} | Partial update | 200, 404 |
| DELETE | /api/v1/projects/{id} | Delete project | 204, 404 |

#### Tasks (nested under projects)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/projects/{projectId}/tasks | List project tasks |
| POST | /api/v1/projects/{projectId}/tasks | Create task |
| GET | /api/v1/tasks/{id} | Get task by ID |
| PUT | /api/v1/tasks/{id} | Update task |
| DELETE | /api/v1/tasks/{id} | Delete task |

### REST Best Practices to Implement

1. **HTTP Methods**
   - GET for retrieval (safe, idempotent)
   - POST for creation
   - PUT for full replacement
   - PATCH for partial update
   - DELETE for removal

2. **Status Codes**
   - 200 OK, 201 Created, 204 No Content
   - 400 Bad Request, 401, 403, 404, 409 Conflict
   - 500 Internal Server Error

3. **Request/Response**
   - JSON format
   - Consistent error response structure
   - Content-Type and Accept headers

4. **Filtering, Sorting, Pagination**
   ```
   GET /api/v1/tasks?status=pending&priority=high
   GET /api/v1/tasks?sort=dueDate,desc
   GET /api/v1/tasks?page=0&size=20
   ```

5. **HATEOAS (Hypermedia)**
   ```json
   {
     "id": 1,
     "name": "Project Alpha",
     "_links": {
       "self": { "href": "/api/v1/projects/1" },
       "tasks": { "href": "/api/v1/projects/1/tasks" }
     }
   }
   ```

6. **API Versioning**
   - URL versioning: `/api/v1/`
   - Header versioning (alternative)

7. **Documentation**
   - OpenAPI/Swagger specification
   - Include examples

## Sample Responses

### Success
```json
{
  "id": 1,
  "name": "Website Redesign",
  "description": "Redesign company website",
  "status": "IN_PROGRESS",
  "createdAt": "2024-12-01T10:00:00Z",
  "_links": {
    "self": { "href": "/api/v1/projects/1" },
    "tasks": { "href": "/api/v1/projects/1/tasks" }
  }
}
```

### Error
```json
{
  "timestamp": "2024-12-11T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Project not found with id: 99",
  "path": "/api/v1/projects/99"
}
```

## Skills Tested
- REST architectural principles
- HTTP semantics
- API design patterns
- HATEOAS implementation
- OpenAPI/Swagger documentation
