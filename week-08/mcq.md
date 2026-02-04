# Week 08 - Multiple Choice Questions

This document contains 80 multiple choice questions covering the key concepts from Week 08 topics: REST API Development and Microservices Fundamentals.

**Topic Distribution:**
- REST API Fundamentals and HTTP: 25 questions
- Spring REST Controllers and DTOs: 25 questions
- Microservices Architecture: 30 questions

---

**Note:** Answers and explanations are in `mcq-answers.md`

---

## REST API Fundamentals and HTTP

### Question 1
**[REST Basics]**

What does REST stand for?

- A) Remote Execution State Transfer
- B) Representational State Transfer
- C) Resource Execution Service Transport
- D) Remote State Transaction

---

### Question 2
**[REST Basics]**

Which is NOT a REST constraint?

- A) Stateless
- B) Client-Server
- C) Cached Sessions
- D) Uniform Interface

---

### Question 3
**[REST Basics]**

What does "stateless" mean in REST?

- A) Server maintains no client session between requests
- B) Client has no state
- C) Resources cannot change state
- D) No HTTP status codes

---

### Question 4
**[HTTP Methods]**

Which HTTP method is used to retrieve a resource?

- A) POST
- B) GET
- C) PUT
- D) PATCH

---

### Question 5
**[HTTP Methods]**

Which HTTP method creates a new resource?

- A) GET
- B) PUT
- C) POST
- D) DELETE

---

### Question 6
**[HTTP Methods]**

What is the difference between PUT and PATCH?

- A) PUT is faster than PATCH
- B) PUT replaces entire resource, PATCH updates partially
- C) PATCH creates, PUT updates
- D) No difference

---

### Question 7
**[HTTP Methods]**

Which HTTP method is idempotent?

- A) POST
- B) GET
- C) CONNECT
- D) None of the above

---

### Question 8
**[HTTP Status Codes]**

What does HTTP status code 200 indicate?

- A) Resource created
- B) Request successful (OK)
- C) No content
- D) Redirect

---

### Question 9
**[HTTP Status Codes]**

Which status code indicates a resource was created?

- A) 200 OK
- B) 201 Created
- C) 204 No Content
- D) 202 Accepted

---

### Question 10
**[HTTP Status Codes]**

What does HTTP status 404 mean?

- A) Server error
- B) Unauthorized
- C) Resource not found
- D) Bad request

---

### Question 11
**[HTTP Status Codes]**

Which status code range indicates client errors?

- A) 1xx
- B) 2xx
- C) 4xx
- D) 5xx

---

### Question 12
**[HTTP Status Codes]**

What does HTTP status 401 indicate?

- A) Forbidden (no permission)
- B) Unauthorized (authentication required)
- C) Not found
- D) Bad request

---

### Question 13
**[HTTP Status Codes]**

What is the difference between 401 and 403?

- A) No difference
- B) 401 = not authenticated, 403 = authenticated but not authorized
- C) 401 = forbidden, 403 = unauthorized
- D) 401 is for POST, 403 is for GET

---

### Question 14
**[REST Design]**

Which URL follows REST conventions?

- A) /getUsers
- B) /users/getById/1
- C) /users/1
- D) /api?action=getUser&id=1

---

### Question 15
**[REST Design]**

What should a REST API return after creating a resource?

- A) Just 200 OK
- B) 201 Created with Location header and resource body
- C) Only the ID
- D) Just 204 No Content

---

### Question 16
**[HTTP Headers]**

What header specifies the format of the request body?

- A) Accept
- B) Content-Type
- C) Authorization
- D) Content-Length

---

### Question 17
**[HTTP Headers]**

What header specifies the desired response format?

- A) Content-Type
- B) Accept
- C) Response-Type
- D) Format

---

### Question 18
**[REST Design]**

Which HTTP method should be used to delete a resource?

- A) POST with action=delete
- B) GET /users/1/delete
- C) DELETE /users/1
- D) PUT with deleted=true

---

### Question 19
**[REST Design]**

How should query parameters be used in REST APIs?

- A) For resource identification
- B) For filtering, sorting, and pagination
- C) For authentication only
- D) Never used in REST

---

### Question 20
**[HTTP Methods]**

Which HTTP methods are safe (don't modify resources)?

- A) GET and HEAD
- B) POST and PUT
- C) DELETE and PATCH
- D) All methods are safe

---

### Question 21
**[REST Basics]**

What does HATEOAS stand for?

- A) Hypertext As The Engine Of Application State
- B) HTTP As Transfer Engine Of API Services
- C) Hyperlink Activated Transfer Engine Of API State
- D) HTTP Application Transfer Engine And Services

---

### Question 22
**[HTTP Status Codes]**

What status code should be returned for a successful DELETE?

- A) 200 OK with deleted resource
- B) 201 Created
- C) 204 No Content
- D) Both A and C are acceptable

---

### Question 23
**[REST Design]**

What is the correct way to version a REST API?

- A) /api/v1/users (URL versioning)
- B) Accept: application/vnd.api.v1+json (header)
- C) ?version=1 (query parameter)
- D) All are valid approaches

---

### Question 24
**[HTTP Methods]**

What does idempotent mean for HTTP methods?

- A) The method is very fast
- B) Multiple identical requests have the same effect as one
- C) The method cannot fail
- D) The method changes state

---

### Question 25
**[REST Basics]**

What is a resource in REST?

- A) A database table
- B) Any information that can be named (entity, collection, etc.)
- C) Only JSON objects
- D) HTTP endpoints

---

## Spring REST Controllers and DTOs

### Question 26
**[Spring REST]**

What annotation creates a REST API controller?

- A) @Controller
- B) @WebController
- C) @RestController
- D) @APIController

---

### Question 27
**[Spring REST]**

What does @RestController combine?

- A) @Component + @ResponseBody
- B) @Controller + @ResponseBody
- C) @Service + @Controller
- D) @Bean + @Controller

---

### Question 28
**[Spring REST]**

Which annotation maps a method to handle GET requests?

- A) @Get
- B) @GetMapping
- C) @RequestMapping(GET)
- D) @HttpGet

---

### Question 29
**[Spring REST]**

What does @PathVariable extract?

- A) Query parameters
- B) Values from URL path segments
- C) HTTP headers
- D) Request body

---

### Question 30
**[Spring REST]**

What does @RequestParam extract?

- A) URL path variables
- B) Query string parameters
- C) Request headers
- D) Cookie values

---

### Question 31
**[Spring REST]**

What does @RequestBody do?

- A) Extracts URL parameters
- B) Binds HTTP request body to a Java object
- C) Returns response body
- D) Validates request format

---

### Question 32
**[Spring REST]**

What class provides full control over HTTP response?

- A) HttpResponse
- B) ResponseEntity
- C) RestResponse
- D) HttpEntity

---

### Question 33
**[Spring REST]**

How do you return 201 Created with location header?

- A) return ResponseEntity.created(uri).body(entity)
- B) return ResponseEntity.ok(entity)
- C) return new Response(201, entity)
- D) return entity with @Created annotation

---

### Question 34
**[DTOs]**

What is a DTO (Data Transfer Object)?

- A) A database entity
- B) An object used to transfer data between layers
- C) A service class
- D) A controller annotation

---

### Question 35
**[DTOs]**

Why use DTOs instead of exposing entities directly?

- A) DTOs are faster
- B) Decouple API from database schema, control exposed data
- C) DTOs use less memory
- D) JPA requires DTOs

---

### Question 36
**[Spring REST]**

Which annotation maps POST requests?

- A) @Post
- B) @PostMapping
- C) @CreateMapping
- D) @AddMapping

---

### Question 37
**[Validation]**

Which annotation triggers validation on request body?

- A) @Validate
- B) @Valid
- C) @Check
- D) @Verify

---

### Question 38
**[Validation]**

What annotation ensures a string is not blank (not null, not empty, not whitespace)?

- A) @NotNull
- B) @NotEmpty
- C) @NotBlank
- D) @Required

---

### Question 39
**[Validation]**

What exception is thrown when @Valid validation fails?

- A) ValidationException
- B) InvalidRequestException
- C) MethodArgumentNotValidException
- D) ConstraintViolationException

---

### Question 40
**[Exception Handling]**

Which annotation creates a global exception handler?

- A) @ExceptionHandler
- B) @ControllerAdvice
- C) @GlobalHandler
- D) @ErrorController

---

### Question 41
**[Exception Handling]**

What does @ExceptionHandler do?

- A) Throws exceptions
- B) Handles specific exception types in controller/advice
- C) Logs exceptions
- D) Prevents exceptions

---

### Question 42
**[Spring REST]**

What is @RestControllerAdvice?

- A) Advice for REST performance
- B) @ControllerAdvice + @ResponseBody for REST exception handling
- C) REST API documentation
- D) REST client configuration

---

### Question 43
**[Spring REST]**

What does @RequestMapping at class level do?

- A) Maps a single endpoint
- B) Sets base URL for all methods in the controller
- C) Enables request logging
- D) Configures request timeout

---

### Question 44
**[Spring REST]**

How do you make @RequestParam optional?

- A) @RequestParam(optional = true)
- B) @RequestParam(required = false)
- C) @Optional @RequestParam
- D) @RequestParam(mandatory = false)

---

### Question 45
**[DTOs]**

What library is commonly used for entity-to-DTO mapping?

- A) Gson
- B) ModelMapper or MapStruct
- C) Jackson
- D) Lombok

---

### Question 46
**[Spring REST]**

What does consumes attribute in @PostMapping specify?

- A) Response content type
- B) Request content type the method accepts
- C) Data consumption rate
- D) Memory consumption limit

---

### Question 47
**[Spring REST]**

What does produces attribute in @GetMapping specify?

- A) Request content type
- B) Response content type the method returns
- C) Production environment flag
- D) Performance metric

---

### Question 48
**[Validation]**

What annotation validates email format?

- A) @EmailFormat
- B) @Email
- C) @ValidEmail
- D) @MailAddress

---

### Question 49
**[Validation]**

What annotation sets minimum and maximum for a number?

- A) @Range
- B) @MinMax
- C) @Min and @Max
- D) @Between

---

### Question 50
**[Spring REST]**

How do you return 404 Not Found with ResponseEntity?

- A) ResponseEntity.notFound().build()
- B) ResponseEntity.status(404).build()
- C) new ResponseEntity(HttpStatus.NOT_FOUND)
- D) All of the above

---

## Microservices Architecture

### Question 51
**[Microservices]**

What are microservices?

- A) Very small applications
- B) Independently deployable services organized around business capabilities
- C) Services that only use REST
- D) Services with micro-sized databases

---

### Question 52
**[Microservices]**

What is the main advantage of microservices over monolith?

- A) Lower memory usage
- B) Independent deployment and scaling of services
- C) Simpler to develop
- D) No network calls needed

---

### Question 53
**[Microservices]**

What is a challenge of microservices architecture?

- A) Too much simplicity
- B) Distributed system complexity (network, data consistency)
- C) Cannot use databases
- D) Only works with Java

---

### Question 54
**[Microservices]**

What is Service Discovery?

- A) Finding bugs in services
- B) Mechanism for services to find network locations of other services
- C) Discovering new service features
- D) Service documentation

---

### Question 55
**[Spring Cloud]**

What is Netflix Eureka used for?

- A) Load balancing
- B) Service discovery and registration
- C) API gateway
- D) Circuit breaking

---

### Question 56
**[Spring Cloud]**

What annotation registers a service with Eureka?

- A) @EurekaService
- B) @EnableDiscoveryClient
- C) @RegisterService
- D) @ServiceRegistry

---

### Question 57
**[Spring Cloud]**

What is an API Gateway?

- A) A gateway to the database
- B) Single entry point that routes requests to appropriate services
- C) A monitoring tool
- D) A testing framework

---

### Question 58
**[Spring Cloud]**

What is Spring Cloud Gateway used for?

- A) Database gateway
- B) API Gateway with routing, filtering, and load balancing
- C) Security gateway only
- D) Message gateway

---

### Question 59
**[Microservices]**

What is the Circuit Breaker pattern?

- A) A physical circuit component
- B) Pattern that prevents cascading failures by failing fast
- C) A network security pattern
- D) A database connection pattern

---

### Question 60
**[Spring Cloud]**

What library implements Circuit Breaker in Spring?

- A) Spring Security
- B) Resilience4j
- C) Spring Retry
- D) Spring Batch

---

### Question 61
**[Circuit Breaker]**

What are the three states of a Circuit Breaker?

- A) On, Off, Standby
- B) Closed, Open, Half-Open
- C) Active, Passive, Neutral
- D) Start, Running, Stopped

---

### Question 62
**[Microservices]**

What does @LoadBalanced annotation enable?

- A) Database load balancing
- B) Client-side load balancing for REST clients
- C) CPU load balancing
- D) Memory load balancing

---

### Question 63
**[Microservices]**

What is a Feign Client?

- A) A fake client for testing
- B) Declarative REST client that simplifies HTTP calls
- C) A security client
- D) A database client

---

### Question 64
**[Spring Cloud]**

What annotation creates a Feign client interface?

- A) @RestClient
- B) @FeignClient
- C) @HttpClient
- D) @ServiceClient

---

### Question 65
**[Microservices]**

What is Spring Cloud Config Server used for?

- A) Database configuration
- B) Centralized external configuration for all services
- C) Security configuration
- D) Logging configuration

---

### Question 66
**[Microservices]**

What annotation refreshes configuration without restart?

- A) @Refresh
- B) @RefreshScope
- C) @ConfigRefresh
- D) @DynamicConfig

---

### Question 67
**[Microservices]**

What is distributed tracing?

- A) Tracing application bugs
- B) Tracking requests across multiple services
- C) Tracing database queries
- D) Tracing memory leaks

---

### Question 68
**[Spring Cloud]**

What tools provide distributed tracing in Spring Cloud?

- A) Log4j and SLF4J
- B) Spring Cloud Sleuth and Zipkin
- C) JUnit and Mockito
- D) Maven and Gradle

---

### Question 69
**[Microservices]**

What is the 12-Factor App methodology?

- A) A testing methodology
- B) Best practices for building cloud-native applications
- C) A security framework
- D) A database design pattern

---

### Question 70
**[12-Factor App]**

Which is a 12-Factor App principle?

- A) Store config in the environment
- B) Use only SQL databases
- C) Single deployment per year
- D) Monolithic architecture

---

### Question 71
**[Microservices]**

What is a Bounded Context in Domain-Driven Design?

- A) A database boundary
- B) A boundary within which a domain model is defined and applicable
- C) A security boundary
- D) A network boundary

---

### Question 72
**[Microservices]**

What communication style is preferred between microservices?

- A) Shared database
- B) Direct database calls
- C) REST APIs or async messaging
- D) File sharing

---

### Question 73
**[Security]**

What is OAuth2 used for in microservices?

- A) Database access
- B) Authorization framework for secure API access
- C) Service discovery
- D) Load balancing

---

### Question 74
**[Security]**

What is a Resource Server in OAuth2?

- A) A server that stores tokens
- B) A server that hosts protected resources and validates access tokens
- C) A server that issues tokens
- D) A server that manages users

---

### Question 75
**[Security]**

What is JWT (JSON Web Token)?

- A) A database format
- B) A compact, URL-safe token for transmitting claims
- C) A JavaScript framework
- D) A testing tool

---

### Question 76
**[Security]**

What annotation configures a Spring app as OAuth2 Resource Server?

- A) @EnableOAuth2
- B) @EnableResourceServer
- C) Configure via SecurityFilterChain with oauth2ResourceServer()
- D) @ResourceServer

---

### Question 77
**[Microservices]**

What is the Saga pattern?

- A) A logging pattern
- B) Pattern for managing distributed transactions across services
- C) A testing pattern
- D) A deployment pattern

---

### Question 78
**[Microservices]**

What does "database per service" mean?

- A) Each service has read-only database access
- B) Each microservice owns and manages its own database
- C) All services share one database
- D) Services don't use databases

---

### Question 79
**[Resilience]**

What does a fallback method provide in Circuit Breaker pattern?

- A) Faster performance
- B) Alternative response when main call fails
- C) Database backup
- D) Load balancing

---

### Question 80
**[Microservices]**

What is the Retry pattern used for?

- A) Retrying database connections only
- B) Automatically retrying failed operations with configurable attempts
- C) Retrying user logins
- D) Retrying deployments

---

## End of Questions

**Total: 80 Questions**
- REST API Fundamentals and HTTP: 25
- Spring REST Controllers and DTOs: 25
- Microservices Architecture: 30

---

*Proceed to `mcq-answers.md` for answers and explanations.*
