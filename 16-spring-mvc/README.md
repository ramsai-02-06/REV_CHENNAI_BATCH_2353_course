# Spring MVC

## Overview

Spring MVC is a web framework for building REST APIs and web applications. Built on the Servlet API, it provides annotation-based controllers, request mapping, and seamless JSON handling.

## Learning Objectives

By the end of this module, you will be able to:
- Understand the Servlet foundation that Spring MVC builds upon
- Build RESTful APIs using @RestController
- Handle request parameters, path variables, and request bodies
- Implement proper exception handling
- Understand reactive programming basics with Spring WebFlux

---

## Topics Covered

### 1. [Servlets Introduction](./topics/01-servlets-introduction.md)
Understand the foundation that Spring MVC is built upon.

- What is a Servlet
- Servlet lifecycle (init, service, destroy)
- HttpServlet and handling GET/POST
- Pain points with raw servlets
- How Spring MVC solves these problems

### 2. [Spring MVC Architecture](./topics/02-spring-mvc-architecture.md)
Introduction to Spring MVC and how it processes HTTP requests.

- What is Spring MVC
- MVC pattern in Spring
- DispatcherServlet and request flow
- @Controller vs @RestController
- Spring Boot setup and project structure

### 3. [Controllers and REST Endpoints](./topics/03-controllers.md)
Build REST APIs with controllers, request mapping, and response handling.

- @RestController and @RequestMapping
- HTTP method annotations (@GetMapping, @PostMapping, etc.)
- @PathVariable for URL parameters
- @RequestParam for query parameters
- @RequestBody for JSON payloads
- ResponseEntity for HTTP response control
- Request validation with @Valid

### 4. [Exception Handling](./topics/04-exception-handling.md)
Implement consistent error responses across your API.

- Custom exception classes
- Error response DTOs
- @RestControllerAdvice for global handling
- @ExceptionHandler for specific exceptions
- Validation error handling

### 5. [Spring WebFlux Introduction](./topics/05-spring-webflux.md)
Introduction to reactive web programming as an alternative to Spring MVC.

- What is Spring WebFlux
- When to use WebFlux vs MVC
- Mono and Flux reactive types
- Reactive controllers
- WebClient for HTTP calls
- Spring MVC vs WebFlux comparison

---

## Topic Flow

```
┌─────────────────────┐
│ 1. Servlets Intro   │  The foundation (feel the pain)
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 2. MVC Architecture │  How Spring MVC simplifies
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 3. Controllers      │  Building REST APIs
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 4. Exception        │  Error handling
│    Handling         │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 5. WebFlux Intro    │  Reactive alternative
└─────────────────────┘
```

---

## Key Concepts Summary

| Concept | Description |
|---------|-------------|
| **Servlet** | Java class that handles HTTP requests |
| **DispatcherServlet** | Front controller that routes all requests |
| **@RestController** | Controller that returns data as JSON |
| **@RequestMapping** | Maps URLs to handler methods |
| **ResponseEntity** | Full control over HTTP response |
| **@RestControllerAdvice** | Global exception handling |
| **WebFlux** | Reactive, non-blocking web framework |

---

## Exercises

See the [exercises](./exercises/) directory for hands-on practice problems.

## Additional Resources

- [Jakarta Servlet Specification](https://jakarta.ee/specifications/servlet/)
- [Spring MVC Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring WebFlux Documentation](https://docs.spring.io/spring-framework/reference/web/webflux.html)

---

## Next Steps

After completing this module, continue to **Spring Data JPA** to learn about database access and persistence.

---

**Duration:** 3 days | **Difficulty:** Intermediate | **Prerequisites:** Module 15 (Spring Boot)
