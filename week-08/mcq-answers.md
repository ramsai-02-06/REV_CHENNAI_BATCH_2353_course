# Week 08 - MCQ Answer Key

This document contains answers and explanations for all 80 questions in `mcq.md`.

---

## Answer Distribution

| Option | Count | Percentage |
|--------|-------|------------|
| A | 20 | 25% |
| B | 20 | 25% |
| C | 20 | 25% |
| D | 20 | 25% |

---

## REST API Fundamentals and HTTP (Questions 1-25)

### Question 1
**Answer: B**

REST stands for **Representational State Transfer**. It's an architectural style for designing networked applications, defined by Roy Fielding in 2000.

---

### Question 2
**Answer: C**

**Cached Sessions** is NOT a REST constraint. The actual constraints are: Client-Server, Stateless, Cacheable (not sessions), Uniform Interface, Layered System, and Code on Demand (optional).

---

### Question 3
**Answer: A**

**Stateless** means the **server maintains no client session between requests**. Each request must contain all information needed to process it.

---

### Question 4
**Answer: B**

**GET** is used to retrieve a resource. It's a safe and idempotent method that should not modify server state.

---

### Question 5
**Answer: C**

**POST** is used to create a new resource. It's neither safe nor idempotent.

---

### Question 6
**Answer: B**

**PUT replaces the entire resource** with the new representation, while **PATCH updates only specific fields**. PUT requires the complete entity.

---

### Question 7
**Answer: B**

**GET** is idempotent (along with PUT, DELETE, HEAD). Multiple identical GET requests have the same effect as a single request.

---

### Question 8
**Answer: B**

HTTP 200 means **OK - Request successful**. The request was received, understood, and processed successfully.

---

### Question 9
**Answer: B**

**201 Created** indicates a resource was successfully created. It's typically returned for POST requests that create new resources.

---

### Question 10
**Answer: C**

HTTP 404 means **Resource not found**. The server cannot find the requested resource.

---

### Question 11
**Answer: C**

**4xx** status codes indicate **client errors**. These include 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found.

---

### Question 12
**Answer: B**

HTTP 401 means **Unauthorized - authentication required**. The request lacks valid authentication credentials.

---

### Question 13
**Answer: B**

**401 = not authenticated** (need to log in), **403 = authenticated but not authorized** (logged in but no permission).

---

### Question 14
**Answer: C**

`/users/1` follows REST conventions. REST uses nouns (resources) in URLs, not verbs (actions). The ID is a path variable.

---

### Question 15
**Answer: B**

After creating a resource, return **201 Created with Location header** (URL of new resource) **and the created resource body**.

---

### Question 16
**Answer: B**

**Content-Type** header specifies the format of the request body (e.g., `application/json`).

---

### Question 17
**Answer: B**

**Accept** header specifies the desired response format. The client tells the server what content types it can handle.

---

### Question 18
**Answer: C**

Use **DELETE /users/1** to delete a resource. REST uses HTTP methods for actions, not URL verbs.

---

### Question 19
**Answer: B**

Query parameters are used for **filtering, sorting, and pagination** (e.g., `/users?status=active&sort=name&page=1`).

---

### Question 20
**Answer: A**

**GET and HEAD** are safe methods - they only retrieve data and don't modify server state.

---

### Question 21
**Answer: A**

HATEOAS stands for **Hypertext As The Engine Of Application State**. It's a REST constraint where responses include links to related actions/resources.

---

### Question 22
**Answer: D**

Both **200 OK** (with the deleted resource) and **204 No Content** (with no body) are acceptable for successful DELETE.

---

### Question 23
**Answer: D**

**All approaches are valid** for API versioning: URL path (/v1/), header (Accept), or query parameter. URL versioning is most common.

---

### Question 24
**Answer: B**

Idempotent means **multiple identical requests have the same effect as one**. GET, PUT, DELETE are idempotent; POST is not.

---

### Question 25
**Answer: B**

A resource is **any information that can be named** - entities, collections, computed values, etc. It's the fundamental concept in REST.

---

## Spring REST Controllers and DTOs (Questions 26-50)

### Question 26
**Answer: C**

`@RestController` creates a REST API controller. It combines @Controller and @ResponseBody.

---

### Question 27
**Answer: B**

`@RestController` combines `@Controller + @ResponseBody`. All methods return data directly (JSON) instead of view names.

---

### Question 28
**Answer: B**

`@GetMapping` maps a method to handle GET requests. It's a shortcut for `@RequestMapping(method = RequestMethod.GET)`.

---

### Question 29
**Answer: B**

`@PathVariable` extracts **values from URL path segments** (e.g., `/users/{id}` → `@PathVariable Long id`).

---

### Question 30
**Answer: B**

`@RequestParam` extracts **query string parameters** (e.g., `/users?page=1` → `@RequestParam int page`).

---

### Question 31
**Answer: B**

`@RequestBody` **binds HTTP request body to a Java object**. Spring uses Jackson to deserialize JSON to objects.

---

### Question 32
**Answer: B**

`ResponseEntity` provides **full control over HTTP response** - status code, headers, and body.

---

### Question 33
**Answer: A**

Use `ResponseEntity.created(uri).body(entity)` to return 201 Created with Location header and response body.

---

### Question 34
**Answer: B**

A DTO is **an object used to transfer data between layers**. It decouples the API from internal domain models.

---

### Question 35
**Answer: B**

DTOs **decouple API from database schema and control exposed data**. You can hide sensitive fields and shape responses independently.

---

### Question 36
**Answer: B**

`@PostMapping` maps POST requests. It's a shortcut for `@RequestMapping(method = RequestMethod.POST)`.

---

### Question 37
**Answer: B**

`@Valid` triggers validation on the request body using Bean Validation annotations.

---

### Question 38
**Answer: C**

`@NotBlank` ensures a string is **not null, not empty, and not just whitespace**. It's stricter than @NotNull or @NotEmpty.

---

### Question 39
**Answer: C**

`MethodArgumentNotValidException` is thrown when `@Valid` validation fails on a @RequestBody parameter.

---

### Question 40
**Answer: B**

`@ControllerAdvice` creates a **global exception handler** that handles exceptions across all controllers.

---

### Question 41
**Answer: B**

`@ExceptionHandler` **handles specific exception types** in a controller or controller advice.

---

### Question 42
**Answer: B**

`@RestControllerAdvice` is `@ControllerAdvice + @ResponseBody` for REST API exception handling, returning JSON responses.

---

### Question 43
**Answer: B**

`@RequestMapping` at class level **sets the base URL** for all methods in that controller.

---

### Question 44
**Answer: B**

Use `@RequestParam(required = false)` to make a query parameter optional.

---

### Question 45
**Answer: B**

**ModelMapper or MapStruct** are commonly used for entity-to-DTO mapping. MapStruct generates code at compile time.

---

### Question 46
**Answer: B**

`consumes` specifies the **request content type** the method accepts (e.g., `consumes = "application/json"`).

---

### Question 47
**Answer: B**

`produces` specifies the **response content type** the method returns (e.g., `produces = "application/json"`).

---

### Question 48
**Answer: B**

`@Email` validates that a string is a valid email format.

---

### Question 49
**Answer: C**

Use `@Min` and `@Max` separately to set minimum and maximum constraints for numeric values.

---

### Question 50
**Answer: D**

All three approaches return 404: `ResponseEntity.notFound().build()`, `ResponseEntity.status(404).build()`, and `new ResponseEntity(HttpStatus.NOT_FOUND)`.

---

## Microservices Architecture (Questions 51-80)

### Question 51
**Answer: B**

Microservices are **independently deployable services organized around business capabilities**. Each service is small and focused.

---

### Question 52
**Answer: B**

The main advantage is **independent deployment and scaling** of services. Teams can work, deploy, and scale services independently.

---

### Question 53
**Answer: B**

**Distributed system complexity** is a key challenge - network latency, partial failures, data consistency, debugging across services.

---

### Question 54
**Answer: B**

Service Discovery is a **mechanism for services to find network locations of other services** dynamically.

---

### Question 55
**Answer: B**

Netflix Eureka is used for **service discovery and registration**. Services register themselves and discover others through Eureka.

---

### Question 56
**Answer: B**

`@EnableDiscoveryClient` registers a service with the discovery server (Eureka or other implementations).

---

### Question 57
**Answer: B**

An API Gateway is a **single entry point that routes requests** to appropriate microservices, handling cross-cutting concerns.

---

### Question 58
**Answer: B**

Spring Cloud Gateway provides **API Gateway capabilities** including routing, filtering, and load balancing.

---

### Question 59
**Answer: B**

Circuit Breaker **prevents cascading failures by failing fast** when a service is unavailable, rather than waiting.

---

### Question 60
**Answer: B**

**Resilience4j** implements Circuit Breaker pattern in Spring. It replaced Netflix Hystrix which is in maintenance mode.

---

### Question 61
**Answer: B**

The three states are **Closed** (normal), **Open** (failing fast), and **Half-Open** (testing if service recovered).

---

### Question 62
**Answer: B**

`@LoadBalanced` enables **client-side load balancing** for REST clients like RestTemplate or WebClient.

---

### Question 63
**Answer: B**

Feign Client is a **declarative REST client** that simplifies HTTP calls using interface definitions.

---

### Question 64
**Answer: B**

`@FeignClient` annotation creates a Feign client interface for calling other services.

---

### Question 65
**Answer: B**

Spring Cloud Config Server provides **centralized external configuration** for all microservices.

---

### Question 66
**Answer: B**

`@RefreshScope` allows beans to refresh their configuration without restart when config changes.

---

### Question 67
**Answer: B**

Distributed tracing is **tracking requests across multiple services** to understand the flow and diagnose issues.

---

### Question 68
**Answer: B**

**Spring Cloud Sleuth and Zipkin** provide distributed tracing. Sleuth adds trace IDs, Zipkin visualizes traces.

---

### Question 69
**Answer: B**

12-Factor App is **best practices for building cloud-native applications** that are portable and scalable.

---

### Question 70
**Answer: A**

**"Store config in the environment"** is a 12-Factor principle. Configuration should be separate from code.

---

### Question 71
**Answer: B**

A Bounded Context is **a boundary within which a domain model is defined**. Each microservice typically represents one bounded context.

---

### Question 72
**Answer: C**

**REST APIs or async messaging** are preferred for inter-service communication. Avoid shared databases.

---

### Question 73
**Answer: B**

OAuth2 is an **authorization framework** for secure API access. It enables delegated authorization.

---

### Question 74
**Answer: B**

A Resource Server **hosts protected resources and validates access tokens**. It's where your API endpoints live.

---

### Question 75
**Answer: B**

JWT is a **compact, URL-safe token for transmitting claims**. It's commonly used for authentication in microservices.

---

### Question 76
**Answer: C**

Configure Resource Server via **SecurityFilterChain with oauth2ResourceServer()** in Spring Security 6+.

---

### Question 77
**Answer: B**

Saga pattern **manages distributed transactions across services** using a sequence of local transactions.

---

### Question 78
**Answer: B**

Database per service means **each microservice owns and manages its own database**, ensuring loose coupling.

---

### Question 79
**Answer: B**

A fallback method provides an **alternative response when the main call fails**, ensuring graceful degradation.

---

### Question 80
**Answer: B**

Retry pattern **automatically retries failed operations** with configurable attempts, delays, and conditions.

---

## Summary

| Topic | Questions | Key Concepts |
|-------|-----------|--------------|
| REST Fundamentals | 1-25 | HTTP methods, status codes, REST constraints, URL design |
| Spring REST | 26-50 | @RestController, DTOs, validation, exception handling |
| Microservices | 51-80 | Service discovery, API Gateway, Circuit Breaker, OAuth2 |

---

*For detailed explanations and code examples, refer to the module documentation in `18-rest-api/` and `19-microservices/`.*
