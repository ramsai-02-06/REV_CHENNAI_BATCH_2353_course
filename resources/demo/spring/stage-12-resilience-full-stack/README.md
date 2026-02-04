# Stage 12: Microservices with Full Spring Cloud Stack

This stage demonstrates a complete microservices architecture using Spring Boot and Spring Cloud, featuring service discovery, centralized configuration, API gateway, and resilience patterns.

## Architecture Overview

```
                                    ┌─────────────────┐
                                    │  Config Server  │
                                    │     :8888       │
                                    └────────┬────────┘
                                             │ pulls config
                                             ▼
┌──────────┐     ┌─────────────┐    ┌─────────────────┐
│  Client  │────▶│ API Gateway │───▶│ Discovery Server│
│          │     │    :8080    │    │  (Eureka) :8761 │
└──────────┘     └──────┬──────┘    └─────────────────┘
                        │                    ▲
                        │ routes             │ registers
                        ▼                    │
        ┌───────────────┼───────────────┐    │
        │               │               │    │
        ▼               ▼               ▼    │
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ User Service │ │ Task Service │ │Notification  │
│    :8081     │ │    :8082     │ │  Service     │
│              │◀│   (Feign)    │ │    :8083     │
│   H2 DB      │ │   H2 DB      │ │  (In-memory) │
└──────────────┘ └──────────────┘ └──────────────┘
                        │
                 Resilience4j
                 Circuit Breaker
```

## Services Overview

| Service | Port | Description |
|---------|------|-------------|
| Config Server | 8888 | Centralized configuration management |
| Discovery Server | 8761 | Eureka service registry |
| API Gateway | 8080 | Single entry point, request routing |
| User Service | 8081 | User management (CRUD) |
| Task Service | 8082 | Task management with circuit breaker |
| Notification Service | 8083 | Notification handling |

---

## Spring Cloud Concepts Explained

### 1. Service Discovery (Eureka)

**What is it?**
Service Discovery eliminates hardcoded service URLs. Instead of configuring `http://localhost:8081` for user-service, services register themselves with Eureka, and clients look up service locations dynamically.

**Why do we need it?**
- Services can scale horizontally (multiple instances)
- Services can move to different hosts/ports
- Load balancing happens automatically
- No configuration changes needed when services change

**How it works:**
```
1. Service starts → registers with Eureka
2. Client needs service → asks Eureka for location
3. Eureka returns available instances
4. Client calls the service
5. Service sends heartbeats to Eureka
6. If heartbeats stop → Eureka removes service
```

**Key Annotations:**
```java
@EnableEurekaServer    // On discovery server
@EnableDiscoveryClient // On client services
```

**Configuration:**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

---

### 2. API Gateway (Spring Cloud Gateway)

**What is it?**
A single entry point for all client requests. The gateway routes requests to appropriate microservices based on path patterns.

**Why do we need it?**
- **Single entry point**: Clients only know about the gateway
- **Cross-cutting concerns**: Authentication, logging, rate limiting
- **Request routing**: Direct traffic to correct services
- **Load balancing**: Distribute requests across instances

**How it works:**
```
Client → Gateway(:8080) → Routes based on path
         /api/users/**  → user-service
         /api/tasks/**  → task-service
         /api/notifications/** → notification-service
```

**Route Configuration:**
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service    # lb:// = load balanced via Eureka
          predicates:
            - Path=/api/users/**
```

---

### 3. Centralized Configuration (Config Server)

**What is it?**
A central place to manage configuration for all services. Instead of each service having its own `application.yml`, configurations are stored in one location and served by the Config Server.

**Why do we need it?**
- **Single source of truth**: All configs in one place
- **Environment-specific**: Different configs for dev/staging/prod
- **No redeployment**: Change config without rebuilding services
- **Version control**: Configs can be stored in Git

**How it works:**
```
1. Config Server starts, loads configs from config-repo/
2. Services start, connect to Config Server
3. Services fetch their configuration
4. Config changes can be refreshed without restart
```

**Service configuration:**
```yaml
spring:
  config:
    import: optional:configserver:http://localhost:8888
```

---

### 4. Declarative HTTP Clients (Feign)

**What is it?**
Feign is a declarative HTTP client. Instead of writing HTTP request code, you define an interface and Feign generates the implementation.

**Why do we need it?**
- **Less boilerplate**: No RestTemplate code
- **Readable**: Interface clearly shows API contract
- **Integration**: Works with Eureka for service discovery
- **Resilience**: Easy to add circuit breakers

**Without Feign (RestTemplate):**
```java
ResponseEntity<User> response = restTemplate.getForEntity(
    "http://user-service/api/users/" + id,
    User.class
);
return response.getBody();
```

**With Feign:**
```java
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}

// Usage
UserResponse user = userClient.getUserById(1L);
```

---

### 5. Circuit Breaker (Resilience4j)

**What is it?**
A pattern that prevents cascading failures. When a service fails repeatedly, the circuit breaker "opens" and returns a fallback response instead of waiting for timeouts.

**Why do we need it?**
- **Prevent cascading failures**: One service down shouldn't crash everything
- **Fail fast**: Don't wait for timeouts
- **Graceful degradation**: Return fallback responses
- **Self-healing**: Automatically retry after recovery

**Circuit States:**
```
CLOSED (Normal)
    │
    │ failures > threshold
    ▼
  OPEN (Failing) ──────► wait-duration ──────► HALF_OPEN
    │                                              │
    │ fallback responses                           │ test calls
    │                                              │
    └──────────────────────────────────────────────┘
                   success → CLOSED
                   failure → OPEN
```

**Configuration:**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      userService:
        sliding-window-size: 10          # Track last 10 calls
        minimum-number-of-calls: 5       # Min calls before evaluating
        failure-rate-threshold: 50       # Open if 50% fail
        wait-duration-in-open-state: 10s # Wait before testing
        permitted-number-of-calls-in-half-open-state: 3
```

**Usage:**
```java
@CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
public UserResponse getUser(Long userId) {
    return userClient.getUserById(userId);
}

public UserResponse getUserFallback(Long userId, Throwable t) {
    // Return cached/default response
    return new UserResponse(userId, "Unknown User", "unavailable@example.com");
}
```

---

## Running the Services

### Prerequisites
- Java 17+
- Maven 3.8+

### Startup Order (Important!)

Services must be started in this order:

```bash
# Terminal 1: Config Server (must start first)
cd config-server
mvn spring-boot:run

# Terminal 2: Discovery Server (after Config Server is ready)
cd discovery-server
mvn spring-boot:run

# Terminal 3: API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 4: User Service
cd user-service
mvn spring-boot:run

# Terminal 5: Task Service
cd task-service
mvn spring-boot:run

# Terminal 6: Notification Service
cd notification-service
mvn spring-boot:run
```

### Verify Services

1. **Eureka Dashboard**: http://localhost:8761
   - Should show all registered services

2. **Config Server**: http://localhost:8888/user-service/default
   - Should return user-service configuration

3. **Actuator Health**: http://localhost:8082/actuator/health
   - Should show circuit breaker health

---

## API Endpoints

All requests go through the API Gateway at `http://localhost:8080`

### User Service

```bash
# Get all users
GET http://localhost:8080/api/users

# Get user by ID
GET http://localhost:8080/api/users/1

# Create user
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "department": "Engineering"
}

# Update user
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "department": "Engineering"
}

# Delete user
DELETE http://localhost:8080/api/users/1
```

### Task Service

```bash
# Get all tasks
GET http://localhost:8080/api/tasks

# Get tasks by user
GET http://localhost:8080/api/tasks?userId=1

# Get tasks by status
GET http://localhost:8080/api/tasks?status=TODO

# Get task by ID (includes user details via Feign)
GET http://localhost:8080/api/tasks/1

# Create task (validates user via Feign)
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Complete documentation",
  "description": "Write README for microservices demo",
  "assignedUserId": 1,
  "dueDate": "2024-12-31T23:59:59"
}

# Update task status
PATCH http://localhost:8080/api/tasks/1/status
Content-Type: application/json

{
  "status": "COMPLETED"
}

# Delete task
DELETE http://localhost:8080/api/tasks/1
```

### Notification Service

```bash
# Get all notifications
GET http://localhost:8080/api/notifications

# Get notifications by user
GET http://localhost:8080/api/notifications?userId=1
```

---

## Testing Circuit Breaker

### Demo: User Service Failure

1. **Create a user and task (normal operation)**

```bash
# Create user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","department":"IT"}'

# Create task assigned to user
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Task","description":"Testing circuit breaker","assignedUserId":1}'

# Get task (should include user details)
curl http://localhost:8080/api/tasks/1
```

2. **Stop user-service** (Ctrl+C in its terminal)

3. **Try to get the task again**

```bash
curl http://localhost:8080/api/tasks/1
```

**Expected Result:**
- Task is returned (from task-service database)
- `assignedUser` shows fallback: "Unknown User (Service Unavailable)"
- Circuit breaker opens after threshold failures

4. **Check circuit breaker status**

```bash
curl http://localhost:8082/actuator/health
```

Look for:
```json
{
  "circuitBreakers": {
    "userService": {
      "status": "OPEN"
    }
  }
}
```

5. **Restart user-service**

6. **Wait for circuit to half-open** (10 seconds)

7. **Make requests - circuit should close**

```bash
curl http://localhost:8080/api/tasks/1
# Now shows actual user details again
```

---

## Key Files Reference

### Infrastructure

| File | Purpose |
|------|---------|
| `config-server/src/.../ConfigServerApplication.java` | @EnableConfigServer |
| `discovery-server/src/.../DiscoveryServerApplication.java` | @EnableEurekaServer |
| `api-gateway/src/main/resources/application.yml` | Route definitions |

### Task Service (Circuit Breaker Demo)

| File | Purpose |
|------|---------|
| `client/UserClient.java` | Feign client with fallback |
| `fallback/UserClientFallback.java` | Fallback implementation |
| `service/TaskService.java` | @CircuitBreaker annotations |
| `application.yml` | Resilience4j configuration |

---

## Learning Progression

This is Stage 12 in the progressive Spring learning series:

| Stage | Focus |
|-------|-------|
| 1-3 | Plain Java → Spring Core |
| 4-5 | Spring Boot → Spring Data JPA |
| 6-7 | REST API → Production Patterns |
| 8 | Testing & Profiles |
| 9 | Microservice Decomposition |
| 10 | Service Discovery (Eureka) |
| 11 | Gateway + Config Server |
| **12** | **Full Stack + Resilience4j** |

---

## Best Practices Demonstrated

1. **Layered Architecture**: Controller → Service → Repository
2. **DTOs**: Separate request/response objects from entities
3. **Validation**: Bean Validation on request DTOs
4. **Graceful Degradation**: Fallbacks for service failures
5. **Centralized Configuration**: Easy environment management
6. **Service Discovery**: No hardcoded URLs
7. **Single Entry Point**: API Gateway for all requests
8. **Health Monitoring**: Actuator endpoints for observability

---

## Common Issues & Solutions

### Services not registering with Eureka
- Check Eureka is running on 8761
- Verify `eureka.client.service-url.defaultZone` in config

### Config Server not serving configs
- Ensure `config-repo/` directory exists
- Check `spring.profiles.active: native` in config-server

### Circuit breaker not opening
- Check `minimum-number-of-calls` threshold
- Verify `@CircuitBreaker` annotation on correct method
- Enable debug logging: `logging.level.io.github.resilience4j: DEBUG`

### Feign client returning null
- Ensure `@EnableFeignClients` on main application
- Check service name matches Eureka registration
- Verify endpoint path matches target service
