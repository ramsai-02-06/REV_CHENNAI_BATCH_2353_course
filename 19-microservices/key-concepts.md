# Microservices Key Concepts for Application Developers

## Overview

This document covers essential microservices architecture concepts that every application developer must master. Microservices represent a modern architectural approach where applications are built as a collection of small, independent services that communicate over networks. This architecture enables scalability, flexibility, and faster development cycles.

---

## 1. Microservices Architecture Fundamentals

### Why It Matters
- Independent deployment and scaling
- Technology diversity per service
- Fault isolation and resilience
- Team autonomy and faster delivery
- Easier to understand and maintain small services

### Key Concepts

| Aspect | Monolith | Microservices |
|--------|----------|---------------|
| Structure | Single deployable unit | Multiple independent services |
| Scaling | Scale entire application | Scale individual services |
| Technology | Single tech stack | Different tech per service |
| Deployment | Deploy everything | Deploy independently |
| Failure Impact | System-wide | Service-specific |
| Team Structure | Cross-functional | Service-oriented |

### Architecture Comparison

**Monolithic Architecture:**
```
┌─────────────────────────────────────┐
│         Single Application          │
│  ┌──────────┐  ┌──────────┐        │
│  │   User   │  │ Product  │        │
│  │ Service  │  │ Service  │        │
│  └──────────┘  └──────────┘        │
│  ┌──────────┐  ┌──────────┐        │
│  │  Order   │  │ Payment  │        │
│  │ Service  │  │ Service  │        │
│  └──────────┘  └──────────┘        │
│                                     │
│      Single Database                │
└─────────────────────────────────────┘
```

**Microservices Architecture:**
```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ User Service │  │Product Service│  │Order Service │
│              │  │               │  │              │
│   ┌────┐     │  │   ┌────┐      │  │   ┌────┐     │
│   │ DB │     │  │   │ DB │      │  │   │ DB │     │
│   └────┘     │  │   └────┘      │  │   └────┘     │
└──────────────┘  └──────────────┘  └──────────────┘
       │                  │                  │
       └──────────────────┼──────────────────┘
                          │
                   ┌──────────────┐
                   │  API Gateway │
                   └──────────────┘
```

### Microservices Characteristics

**1. Single Responsibility:**
```java
// GOOD: Each service has one responsibility
@SpringBootApplication
public class UserService {
    // Handles user management only
    // - User registration
    // - User authentication
    // - User profile management
}

@SpringBootApplication
public class OrderService {
    // Handles orders only
    // - Create orders
    // - Update order status
    // - Order history
}
```

**2. Independent Deployment:**
```bash
# Deploy user service independently
docker build -t user-service:v2 ./user-service
kubectl apply -f user-service-deployment.yaml

# Deploy order service independently
docker build -t order-service:v1 ./order-service
kubectl apply -f order-service-deployment.yaml
```

**3. Decentralized Data:**
```java
// User Service - owns user data
@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String email;
}

// Order Service - owns order data
@Entity
public class Order {
    @Id
    private Long id;
    private Long userId;  // Reference to user, not foreign key
    private BigDecimal total;
}
```

**4. Communication Patterns:**
```java
// Synchronous (REST)
@Service
public class OrderService {
    @Autowired
    private RestTemplate restTemplate;

    public Order createOrder(OrderRequest request) {
        // Call user service to validate user
        User user = restTemplate.getForObject(
            "http://user-service/api/users/" + request.getUserId(),
            User.class
        );

        // Create order
        return orderRepository.save(new Order(user.getId(), request.getItems()));
    }
}

// Asynchronous (Message Queue)
@Service
public class OrderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Order createOrder(OrderRequest request) {
        Order order = orderRepository.save(new Order(request));

        // Publish event
        rabbitTemplate.convertAndSend("order-exchange", "order.created",
            new OrderCreatedEvent(order));

        return order;
    }
}
```

---

## 2. Spring Cloud Ecosystem

### Why It Matters
- Provides microservices infrastructure
- Solves common distributed system problems
- Battle-tested patterns and tools
- Integrates seamlessly with Spring Boot

### Key Concepts

| Component | Purpose | Use Case |
|-----------|---------|----------|
| Spring Cloud Config | Centralized configuration | Manage configs across services |
| Eureka | Service discovery | Find service instances |
| Ribbon | Load balancing | Distribute requests |
| Feign | Declarative REST client | Simplify service calls |
| Gateway | API gateway | Single entry point |
| Circuit Breaker | Fault tolerance | Handle failures gracefully |
| Sleuth/Zipkin | Distributed tracing | Track requests across services |

### Spring Cloud Dependencies

**Parent POM:**
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2023.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

**Service Dependencies:**
```xml
<dependencies>
    <!-- Service Discovery -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>

    <!-- Config Client -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>

    <!-- Circuit Breaker -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
    </dependency>

    <!-- OpenFeign -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
</dependencies>
```

---

## 3. Service Discovery

### Why It Matters
- Services find each other dynamically
- No hardcoded URLs
- Automatic registration/deregistration
- Load balancing support

### Key Concepts

**Eureka Server Setup:**
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**application.yml (Eureka Server):**
```yaml
server:
  port: 8761

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
```

**Eureka Client (Service):**
```java
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

**application.yml (Service):**
```yaml
spring:
  application:
    name: user-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 5
    lease-expiration-duration-in-seconds: 10
```

**Service-to-Service Communication:**
```java
@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // Enable client-side load balancing
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Service
public class OrderService {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    public User getUserById(Long userId) {
        // Uses service name from Eureka, not URL
        return restTemplate.getForObject(
            "http://user-service/api/users/" + userId,
            User.class
        );
    }
}
```

**Using Feign Client:**
```java
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    // ...
}

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users")
    List<User> getAllUsers();

    @PostMapping("/api/users")
    User createUser(@RequestBody UserCreateDTO dto);
}

@Service
public class OrderService {

    @Autowired
    private UserServiceClient userServiceClient;

    public Order createOrder(OrderRequest request) {
        // Simple method call, no RestTemplate
        User user = userServiceClient.getUserById(request.getUserId());

        // Create order
        return orderRepository.save(new Order(user.getId(), request));
    }
}
```

---

## 4. API Gateway

### Why It Matters
- Single entry point for clients
- Routing to microservices
- Cross-cutting concerns (auth, logging, rate limiting)
- Protocol translation
- Response aggregation

### Key Concepts

**Spring Cloud Gateway:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

**Gateway Application:**
```java
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

**Gateway Configuration (application.yml):**
```yaml
spring:
  cloud:
    gateway:
      routes:
        # User Service Routes
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - RewritePath=/api/users/(?<segment>.*), /${segment}

        # Order Service Routes
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
          filters:
            - RewritePath=/api/orders/(?<segment>.*), /${segment}

        # Product Service Routes
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/products/**

      # Global CORS Configuration
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origins: "*"
            allowed-methods:
              - GET
              - POST
              - PUT
              - DELETE
            allowed-headers: "*"

server:
  port: 8080
```

**Programmatic Route Configuration:**
```java
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // User service routes
            .route("user-service", r -> r
                .path("/api/users/**")
                .filters(f -> f
                    .stripPrefix(1)
                    .addRequestHeader("X-Gateway", "API-Gateway")
                    .circuitBreaker(config -> config
                        .setName("userServiceCB")
                        .setFallbackUri("forward:/fallback/users")))
                .uri("lb://user-service"))

            // Order service with rate limiting
            .route("order-service", r -> r
                .path("/api/orders/**")
                .filters(f -> f
                    .stripPrefix(1)
                    .requestRateLimiter(config -> config
                        .setRateLimiter(redisRateLimiter())))
                .uri("lb://order-service"))

            .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(10, 20); // replenishRate, burstCapacity
    }
}
```

**Fallback Controller:**
```java
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "User service is currently unavailable"));
    }

    @GetMapping("/orders")
    public ResponseEntity<Map<String, String>> orderServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "Order service is currently unavailable"));
    }
}
```

---

## 5. Resilience Patterns

### Why It Matters
- Handle failures gracefully
- Prevent cascade failures
- Improve system reliability
- Better user experience during outages

### Key Concepts

| Pattern | Purpose | Use Case |
|---------|---------|----------|
| Circuit Breaker | Stop calling failing service | Prevent cascade failures |
| Retry | Automatically retry failed requests | Transient failures |
| Timeout | Limit request duration | Prevent hanging |
| Bulkhead | Isolate resources | Limit impact of failures |
| Fallback | Provide alternative response | Degraded functionality |

### Circuit Breaker with Resilience4j

**Dependencies:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Configuration:**
```yaml
resilience4j:
  circuitbreaker:
    instances:
      userService:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        wait-duration-in-open-state: 10s
        failure-rate-threshold: 50
        slow-call-rate-threshold: 50
        slow-call-duration-threshold: 2s

  retry:
    instances:
      userService:
        max-attempts: 3
        wait-duration: 1s
        retry-exceptions:
          - org.springframework.web.client.RestClientException

  timelimiter:
    instances:
      userService:
        timeout-duration: 2s

  bulkhead:
    instances:
      userService:
        max-concurrent-calls: 10
        max-wait-duration: 1s

management:
  endpoints:
    web:
      exposure:
        include: health,circuitbreakers,circuitbreakerevents
  health:
    circuitbreakers:
      enabled: true
```

**Service Implementation:**
```java
@Service
public class OrderService {

    @Autowired
    private UserServiceClient userServiceClient;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @CircuitBreaker(name = "userService", fallbackMethod = "createOrderFallback")
    @Retry(name = "userService")
    @TimeLimiter(name = "userService")
    public CompletableFuture<Order> createOrder(OrderRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // Call user service
            User user = userServiceClient.getUserById(request.getUserId());

            // Create order
            Order order = new Order();
            order.setUserId(user.getId());
            order.setItems(request.getItems());
            order.setTotal(calculateTotal(request.getItems()));

            return orderRepository.save(order);
        });
    }

    // Fallback method
    public CompletableFuture<Order> createOrderFallback(OrderRequest request, Exception ex) {
        log.error("User service unavailable, using fallback", ex);

        // Create order without user validation
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setItems(request.getItems());
        order.setTotal(calculateTotal(request.getItems()));
        order.setStatus(OrderStatus.PENDING_VALIDATION);

        return CompletableFuture.completedFuture(orderRepository.save(order));
    }
}
```

**Custom Health Indicator:**
```java
@Component
public class UserServiceHealthIndicator implements HealthIndicator {

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public Health health() {
        try {
            userServiceClient.healthCheck();
            return Health.up()
                .withDetail("userService", "Available")
                .build();
        } catch (Exception ex) {
            return Health.down()
                .withDetail("userService", "Unavailable")
                .withDetail("error", ex.getMessage())
                .build();
        }
    }
}
```

---

## 6. Distributed Configuration

### Why It Matters
- Centralized configuration management
- Dynamic configuration updates
- Environment-specific configs
- Security and encryption

### Key Concepts

**Config Server Setup:**
```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

**application.yml (Config Server):**
```yaml
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/myorg/config-repo
          default-label: main
          search-paths: '{application}'
        # Or use native (file system)
        native:
          search-locations: classpath:/configs
  profiles:
    active: native
```

**Config Repository Structure:**
```
config-repo/
├── application.yml              # Common config for all services
├── user-service.yml            # User service config
├── user-service-dev.yml        # User service dev environment
├── user-service-prod.yml       # User service prod environment
├── order-service.yml           # Order service config
└── order-service-prod.yml      # Order service prod environment
```

**application.yml (Common):**
```yaml
# Common configuration for all services
spring:
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: validate

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics

logging:
  level:
    root: INFO
```

**user-service.yml:**
```yaml
# User service specific configuration
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/userdb
    username: user
    password: '{cipher}encryptedPassword'

# Custom properties
app:
  jwt:
    secret: '{cipher}encryptedSecret'
    expiration: 86400000
```

**Config Client (Service):**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

**bootstrap.yml (Service):**
```yaml
spring:
  application:
    name: user-service
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
  profiles:
    active: dev
```

**Dynamic Configuration Refresh:**
```java
@RefreshScope
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${app.message:Default message}")
    private String message;

    @GetMapping("/message")
    public String getMessage() {
        return message;
    }
}

// Refresh configuration without restart:
// POST http://localhost:8081/actuator/refresh
```

---

## 7. Inter-Service Communication

### Why It Matters
- Services need to communicate
- Choose right pattern for use case
- Handle network failures
- Maintain data consistency

### Key Concepts

**Synchronous Communication (REST):**
```java
// Using RestTemplate
@Service
public class OrderService {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    public Order createOrder(OrderRequest request) {
        // Get user
        User user = restTemplate.getForObject(
            "http://user-service/api/users/" + request.getUserId(),
            User.class
        );

        // Get product
        Product product = restTemplate.getForObject(
            "http://product-service/api/products/" + request.getProductId(),
            Product.class
        );

        // Check inventory
        Boolean inStock = restTemplate.getForObject(
            "http://inventory-service/api/check?productId=" + request.getProductId() +
            "&quantity=" + request.getQuantity(),
            Boolean.class
        );

        if (!inStock) {
            throw new OutOfStockException("Product out of stock");
        }

        // Create order
        return orderRepository.save(new Order(user, product, request.getQuantity()));
    }
}

// Using Feign
@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    User getUser(@PathVariable Long id);
}

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    Product getProduct(@PathVariable Long id);
}

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/check")
    Boolean checkStock(@RequestParam Long productId, @RequestParam Integer quantity);
}
```

**Asynchronous Communication (Messaging):**
```xml
<!-- RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- Kafka -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

**RabbitMQ Configuration:**
```java
@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order-exchange";
    public static final String ORDER_CREATED_QUEUE = "order-created-queue";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder
            .bind(orderCreatedQueue())
            .to(orderExchange())
            .with(ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```

**Event Publishing:**
```java
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Order createOrder(OrderRequest request) {
        Order order = orderRepository.save(new Order(request));

        // Publish event
        OrderCreatedEvent event = new OrderCreatedEvent(
            order.getId(),
            order.getUserId(),
            order.getTotal(),
            order.getCreatedAt()
        );

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE,
            RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
            event
        );

        return order;
    }
}
```

**Event Consuming:**
```java
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);

        // Send notification
        sendOrderConfirmationEmail(event);
    }

    private void sendOrderConfirmationEmail(OrderCreatedEvent event) {
        // Email sending logic
    }
}
```

Kafka follows a similar pattern with `@KafkaListener` for consumers and `KafkaTemplate` for producers.

---

## 8. Distributed Tracing

### Why It Matters
- Track requests across services
- Identify performance bottlenecks
- Debug distributed systems
- Monitor service dependencies

### Key Concepts

**Spring Cloud Sleuth + Zipkin:**
```xml
<dependencies>
    <!-- Sleuth for tracing -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>

    <!-- Zipkin reporter -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-sleuth-zipkin</artifactId>
    </dependency>
</dependencies>
```

**Configuration:**
```yaml
spring:
  sleuth:
    sampler:
      probability: 1.0  # Sample 100% of requests (reduce in production)
  zipkin:
    base-url: http://localhost:9411
    sender:
      type: web

logging:
  pattern:
    level: '%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]'
```

**Automatic Tracing:**
```java
// Sleuth automatically adds trace IDs to:
// - HTTP requests/responses
// - Messaging (RabbitMQ, Kafka)
// - Scheduled tasks
// - Async execution

@Service
public class OrderService {
    // Trace ID automatically propagated
    public Order createOrder(OrderRequest request) {
        // This call will have same trace ID
        User user = userClient.getUser(request.getUserId());

        // This call will also have same trace ID
        Product product = productClient.getProduct(request.getProductId());

        return orderRepository.save(new Order(user, product));
    }
}
```

**Custom Spans:**
```java
@Service
public class OrderService {

    @Autowired
    private Tracer tracer;

    public Order createOrder(OrderRequest request) {
        Span customSpan = tracer.nextSpan().name("validate-order");

        try (Tracer.SpanInScope ws = tracer.withSpan(customSpan.start())) {
            // Custom logic
            validateOrder(request);
        } finally {
            customSpan.end();
        }

        return orderRepository.save(new Order(request));
    }
}
```

---

## 9. Security in Microservices

### Why It Matters
- Authenticate and authorize users
- Secure inter-service communication
- Protect sensitive data
- Prevent security breaches

### Key Concepts

**OAuth2 Resource Server:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

**JWT Security Configuration:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
        return converter;
    }
}
```

**Service-to-Service Security:**
```java
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Get current JWT token
            Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getCredentials() instanceof String) {
                String token = (String) authentication.getCredentials();
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }
}
```

---

## Quick Reference Card

### Microservices Checklist
```
✅ Single Responsibility - One service, one purpose
✅ Independent Deployment - Deploy without affecting others
✅ Decentralized Data - Each service owns its data
✅ API First - Well-defined service contracts
✅ Fault Tolerant - Circuit breakers and fallbacks
✅ Observable - Logging, metrics, tracing
✅ Automated - CI/CD pipelines
✅ Scalable - Horizontal scaling
```

### Common Patterns
```java
// Service Discovery
@EnableDiscoveryClient
@FeignClient(name = "user-service")

// Circuit Breaker
@CircuitBreaker(name = "userService", fallbackMethod = "fallback")

// Configuration
@RefreshScope
@Value("${app.property}")

// API Gateway
spring.cloud.gateway.routes

// Messaging
@RabbitListener(queues = "queue-name")
@KafkaListener(topics = "topic-name")

// Tracing
spring.sleuth.sampler.probability: 1.0
```

### Service Template
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyServiceApplication.class, args);
    }
}
```

---

## Summary Checklist

By the end of this module, developers should be able to:

- [ ] Understand microservices architecture principles
- [ ] Design and decompose monoliths into microservices
- [ ] Implement service discovery with Eureka
- [ ] Build API gateways with Spring Cloud Gateway
- [ ] Apply resilience patterns (circuit breaker, retry, bulkhead)
- [ ] Manage distributed configuration
- [ ] Implement synchronous communication (REST, Feign)
- [ ] Implement asynchronous communication (messaging)
- [ ] Add distributed tracing with Sleuth and Zipkin
- [ ] Secure microservices with OAuth2/JWT
- [ ] Monitor and debug distributed systems
- [ ] Deploy microservices with containers

---

## Next Steps

After mastering these concepts, proceed to:
- [Module 20: TypeScript](../20-typescript/) - Modern frontend development
- Practice building complete microservices applications
