# Stage 13: Microservices with JWT Security

This stage builds on Stage 12 by adding **JWT (JSON Web Token) authentication** to the microservices architecture. All requests (except public endpoints) require a valid JWT token.

## Architecture Overview

```
                                    ┌─────────────────┐
                                    │  Config Server  │
                                    │     :8888       │
                                    └────────┬────────┘
                                             │
┌──────────┐     ┌─────────────────────┐    │
│  Client  │────▶│    API Gateway      │◄───┘
│          │     │       :8080         │
└──────────┘     │  ┌───────────────┐  │
                 │  │ JWT Filter    │  │ ← Validates token on all requests
                 │  │ (except /auth)│  │   (except public endpoints)
                 │  └───────────────┘  │
                 └──────────┬──────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
        ▼                   ▼                   ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│ User Service │    │ Task Service │    │ Notification │
│    :8081     │    │    :8082     │    │   Service    │
│              │    │              │    │    :8083     │
│ + AuthController  │ + JWT Header │    │              │
│ + JwtService │    │   Propagation│    │              │
│ + Password   │    │   via Feign  │    │              │
└──────────────┘    └──────────────┘    └──────────────┘
```

## What's New in Stage 13

| Component | Security Feature |
|-----------|------------------|
| **User Service** | Authentication endpoints (`/api/auth/**`), password hashing (BCrypt), JWT token generation |
| **API Gateway** | JWT validation filter - validates all requests except public endpoints |
| **Task Service** | Token propagation via Feign interceptor for service-to-service calls |
| **Config Repo** | Centralized JWT secret configuration |

---

## Security Concepts

### JWT (JSON Web Token)

A JWT is a compact, URL-safe token that contains claims (user info) and is signed to prevent tampering.

**Structure:**
```
header.payload.signature

eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA3MjE4MDAwLCJleHAiOjE3MDczMDQ0MDB9.signature
```

**Why JWT for Microservices?**
- **Stateless**: No session storage needed on server
- **Scalable**: Any service can validate the token
- **Self-contained**: Token carries user info (no database lookup needed)

### Authentication Flow

```
1. Client → POST /api/auth/login (email, password)
2. User Service → Validates credentials → Returns JWT token
3. Client → Stores token (localStorage, sessionStorage)
4. Client → Includes token in all requests: Authorization: Bearer <token>
5. Gateway → Validates token → Forwards to service OR returns 401
```

---

## Default Users

On startup, User Service creates two users:

| Email | Password | Role |
|-------|----------|------|
| `admin@example.com` | `admin123` | ADMIN |
| `user@example.com` | `user123` | USER |

---

## API Endpoints

### Authentication (Public - No Token Required)

```bash
# Register new user
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "password123",
  "department": "Engineering"
}

# Login (get token)
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "user123"
}

# Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "user@example.com",
    "role": "USER"
  }
}

# Refresh token
POST http://localhost:8080/api/auth/refresh
Authorization: Bearer <your-token>
```

### Protected Endpoints (Token Required)

All other endpoints require the `Authorization` header:

```bash
# Get all users (requires token)
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer <your-token>"

# Get all tasks (requires token)
curl http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <your-token>"

# Create task (requires token)
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{"title":"New Task","description":"Description","assignedUserId":1}'
```

---

## Testing Security

### 1. Get a Token

```bash
# Login as regular user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"user123"}'

# Save the token from response
export TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

### 2. Access Protected Endpoint (Success)

```bash
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"

# Response: List of users
```

### 3. Access Without Token (401 Unauthorized)

```bash
curl http://localhost:8080/api/users

# Response:
{
  "error": "Missing Authorization header",
  "status": 401
}
```

### 4. Access With Invalid Token (401 Unauthorized)

```bash
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer invalid-token"

# Response:
{
  "error": "Token validation failed",
  "status": 401
}
```

### 5. Access Public Endpoint (No Token Needed)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"user123"}'

# Works without Authorization header
```

---

## Running the Services

### Prerequisites
- Java 17+
- Maven 3.8+

### Startup Order (Important!)

```bash
# Terminal 1: Config Server (must start first)
cd config-server && mvn spring-boot:run

# Terminal 2: Discovery Server
cd discovery-server && mvn spring-boot:run

# Terminal 3: API Gateway
cd api-gateway && mvn spring-boot:run

# Terminal 4: User Service
cd user-service && mvn spring-boot:run

# Terminal 5: Task Service
cd task-service && mvn spring-boot:run

# Terminal 6: Notification Service
cd notification-service && mvn spring-boot:run
```

### Verify Security is Working

1. **Try without token** - should fail:
   ```bash
   curl http://localhost:8080/api/users
   # Returns 401 Unauthorized
   ```

2. **Login to get token**:
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"user@example.com","password":"user123"}'
   ```

3. **Use token** - should succeed:
   ```bash
   curl http://localhost:8080/api/users \
     -H "Authorization: Bearer <token>"
   ```

---

## Key Security Files

### User Service

| File | Purpose |
|------|---------|
| `model/User.java` | Implements `UserDetails`, has password and role |
| `model/Role.java` | USER, ADMIN enum |
| `service/JwtService.java` | Token generation and validation |
| `service/AuthService.java` | Login, register, refresh logic |
| `controller/AuthController.java` | `/api/auth/**` endpoints |
| `filter/JwtAuthenticationFilter.java` | Validates JWT on requests |
| `config/SecurityConfig.java` | Security filter chain configuration |
| `config/DataInitializer.java` | Creates default users on startup |

### API Gateway

| File | Purpose |
|------|---------|
| `filter/JwtAuthenticationFilter.java` | GlobalFilter that validates JWT |
| `filter/RouteValidator.java` | Determines which paths are public |
| `service/JwtService.java` | Token validation (shared secret) |

### Task Service

| File | Purpose |
|------|---------|
| `config/FeignConfig.java` | Propagates JWT header to Feign calls |

---

## Security Configuration

### JWT Settings (in application.yml or config-repo)

```yaml
jwt:
  secret: dGhpc2lzYXZlcnlsb25nc2VjcmV0a2V5Zm9yand0...  # Base64 encoded
  expiration: 86400000  # 24 hours in milliseconds
```

**Important:** The same secret must be used by both User Service (generates tokens) and API Gateway (validates tokens).

### Public Endpoints (no token required)

Defined in `RouteValidator.java`:
- `/api/auth/**` - Login, register, refresh
- `/actuator/**` - Health checks

---

## Security Best Practices Demonstrated

1. **Password Hashing**: BCrypt with automatic salting
2. **Stateless Authentication**: No server-side sessions
3. **Token Expiration**: 24-hour validity
4. **Gateway-Level Security**: Single point of authentication
5. **Token Propagation**: Service-to-service calls include token

---

## Learning Progression

| Stage | Focus |
|-------|-------|
| 1-3 | Plain Java → Spring Core |
| 4-5 | Spring Boot → Spring Data JPA |
| 6-7 | REST API → Production Patterns |
| 8 | Testing & Profiles |
| 9 | Microservice Decomposition |
| 10 | Service Discovery (Eureka) |
| 11 | Gateway + Config Server |
| 12 | Full Stack + Resilience4j |
| **13** | **JWT Security** |

---

## Common Issues

### 401 on all requests
- Check JWT secret matches between user-service and api-gateway
- Verify token hasn't expired
- Ensure `Authorization: Bearer ` prefix (note the space)

### Token not being validated
- Check `JwtAuthenticationFilter` is registered as a component
- Verify gateway debug logs: `logging.level.com.example.apigateway: DEBUG`

### Feign calls failing with 401
- Check `FeignConfig` is present in task-service
- Verify token is being propagated in request headers
