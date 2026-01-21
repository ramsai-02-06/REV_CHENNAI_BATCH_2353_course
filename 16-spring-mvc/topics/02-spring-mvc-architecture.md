# Spring MVC Architecture

## What is Spring MVC?

Spring MVC is a web framework built on the Servlet API for building web applications and REST APIs. It implements the Model-View-Controller pattern and is part of the Spring Framework.

**Key Features:**
- Annotation-based configuration (@Controller, @RestController)
- Flexible request mapping
- Built-in JSON/XML support (Jackson)
- Easy integration with Spring ecosystem
- Excellent for REST APIs

---

## MVC Pattern

```
┌─────────────┐     Request      ┌─────────────┐
│   Client    │ ───────────────→ │  Controller │
│  (Browser)  │                  │  (Handler)  │
└─────────────┘                  └──────┬──────┘
       ▲                                │
       │                                │ Calls
       │ Response                       ▼
       │                         ┌─────────────┐
       │                         │   Service   │
       │                         │  (Business  │
       │                         │   Logic)    │
       │                         └──────┬──────┘
       │                                │
       │                                ▼
       │                         ┌─────────────┐
       │    JSON/HTML            │    Model    │
       └─────────────────────────│   (Data)    │
                                 └─────────────┘
```

| Component | Responsibility |
|-----------|----------------|
| **Model** | Data and business logic |
| **View** | Presentation (JSON, HTML, XML) |
| **Controller** | Handles requests, coordinates response |

---

## Spring MVC Request Flow

```
┌────────────────────────────────────────────────────┐
│                    Client Request                   │
└─────────────────────────┬──────────────────────────┘
                          ▼
              ┌───────────────────────┐
              │   DispatcherServlet   │  Front Controller
              │   (receives all)      │
              └───────────┬───────────┘
                          ▼
              ┌───────────────────────┐
              │    HandlerMapping     │  Finds controller
              └───────────┬───────────┘
                          ▼
              ┌───────────────────────┐
              │      Controller       │  Process request
              │   (@RestController)   │  Return data
              └───────────┬───────────┘
                          ▼
              ┌───────────────────────┐
              │  HttpMessageConverter │  Convert to JSON
              │      (Jackson)        │
              └───────────┬───────────┘
                          ▼
              ┌───────────────────────┐
              │    Client Response    │
              └───────────────────────┘
```

**Key Components:**

| Component | Purpose |
|-----------|---------|
| **DispatcherServlet** | Front controller - receives all requests |
| **HandlerMapping** | Maps URLs to controller methods |
| **Controller** | Processes request, returns data |
| **HttpMessageConverter** | Converts objects to JSON/XML |

---

## Spring Boot Setup

With Spring Boot, most configuration is automatic.

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This includes:
- Spring MVC
- Embedded Tomcat
- Jackson (JSON processing)
- Validation

### Application Class

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Simple REST Controller

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

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
}
```

---

## @Controller vs @RestController

| @Controller | @RestController |
|-------------|-----------------|
| Returns view names | Returns data directly |
| Needs @ResponseBody for JSON | @ResponseBody included |
| For MVC with templates | For REST APIs |

```java
// Traditional MVC - returns view name
@Controller
public class WebController {
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Hello");
        return "home";  // View name
    }
}

// REST API - returns data as JSON
@RestController
public class ApiController {
    @GetMapping("/api/message")
    public Map<String, String> getMessage() {
        return Map.of("message", "Hello");  // JSON response
    }
}
```

---

## Project Structure

```
src/main/java/com/example/
├── Application.java              # Main class
├── controller/
│   └── UserController.java       # REST endpoints
├── service/
│   ├── UserService.java          # Business logic interface
│   └── UserServiceImpl.java      # Implementation
├── repository/
│   └── UserRepository.java       # Data access
├── model/
│   └── User.java                 # Entity/DTO
└── exception/
    └── GlobalExceptionHandler.java

src/main/resources/
├── application.properties        # Configuration
└── static/                       # Static files (if any)
```

---

## Configuration Properties

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Jackson (JSON)
spring.jackson.serialization.indent-output=true
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss

# Logging
logging.level.org.springframework.web=DEBUG
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Spring MVC | Web framework on Servlet API |
| DispatcherServlet | Front controller for all requests |
| @RestController | For REST APIs (returns JSON) |
| @Controller | For MVC with views |
| Spring Boot | Auto-configures everything |

## Next Topic

Continue to [Controllers](./03-controllers.md) to learn about building REST endpoints.
