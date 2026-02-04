# Microservices Introduction

## Introduction to Microservices Architecture (MSA)

Microservices Architecture (MSA) is an architectural style that structures an application as a collection of small, independent, and loosely coupled services. Each service is self-contained, implements a specific business capability, and can be developed, deployed, and scaled independently.

### What Are Microservices?

Microservices are an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often HTTP-based APIs.

**Key Principles:**
- **Single Responsibility**: Each microservice focuses on one business capability
- **Independence**: Services can be developed, deployed, and scaled independently
- **Decentralization**: Decentralized governance and data management
- **Resilience**: Designed to handle failures gracefully
- **Technology Diversity**: Different services can use different technologies

---

## Monolith vs Microservices

Understanding the differences between monolithic and microservices architectures is crucial for making informed architectural decisions.

### Monolithic Architecture

A monolithic application is built as a single, unified unit where all components are interconnected and interdependent.

**Structure:**
```
┌─────────────────────────────────────┐
│      Monolithic Application         │
│                                     │
│  ┌──────────┐  ┌──────────┐       │
│  │   UI     │  │ Business │       │
│  │  Layer   │←→│  Logic   │       │
│  └──────────┘  └──────────┘       │
│       ↓              ↓             │
│  ┌─────────────────────────┐      │
│  │   Single Database       │      │
│  └─────────────────────────┘      │
└─────────────────────────────────────┘
```

**Advantages:**
- Simple to develop initially
- Easy to test end-to-end
- Simple deployment (single unit)
- Straightforward debugging
- No network latency between components

**Disadvantages:**
- Tight coupling between components
- Difficult to scale specific features
- Long-term maintenance complexity
- Technology stack lock-in
- Deployment of small changes requires redeploying entire application
- Reliability issues (one bug can crash entire system)

### Microservices Architecture

Application is decomposed into small, independent services organized around business capabilities.

**Structure:**
```
┌──────────┐  ┌──────────┐  ┌──────────┐
│ Service  │  │ Service  │  │ Service  │
│    A     │  │    B     │  │    C     │
├──────────┤  ├──────────┤  ├──────────┤
│   DB-A   │  │   DB-B   │  │   DB-C   │
└──────────┘  └──────────┘  └──────────┘
      ↕              ↕              ↕
    ┌────────────────────────────────┐
    │        API Gateway             │
    └────────────────────────────────┘
```

**Advantages:**
- Independent deployment
- Technology diversity
- Fault isolation
- Scalability (scale specific services)
- Team autonomy
- Easier to understand (smaller codebases)

**Disadvantages:**
- Increased complexity in distributed systems
- Network latency and reliability
- Data consistency challenges
- Testing complexity
- Deployment coordination
- Operational overhead

### Comparison Table

| Aspect | Monolith | Microservices |
|--------|----------|---------------|
| **Size** | Single large codebase | Multiple small codebases |
| **Deployment** | All-or-nothing | Independent per service |
| **Scaling** | Scale entire application | Scale individual services |
| **Technology** | Single tech stack | Multiple tech stacks possible |
| **Database** | Shared database | Database per service |
| **Development** | Single team or coordinated teams | Autonomous teams |
| **Testing** | Simpler integration testing | Complex distributed testing |
| **Failure Impact** | Can bring down entire system | Isolated to specific service |
| **Initial Complexity** | Lower | Higher |
| **Long-term Maintenance** | Harder as it grows | Easier with proper boundaries |

---

## Microservices Characteristics

### 1. Componentization via Services

Services are independently deployable components that communicate through well-defined APIs.

```java
// Each service is independently deployable
@SpringBootApplication
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

### 2. Organized Around Business Capabilities

Services are structured around business domains, not technical layers.

```
❌ Technical Layering:        ✅ Business Capabilities:
- UI Services                - Order Service
- Business Logic Services    - Payment Service
- Data Access Services       - Inventory Service
                            - Customer Service
```

### 3. Products not Projects

Teams own services end-to-end throughout their lifetime ("you build it, you run it").

### 4. Smart Endpoints and Dumb Pipes

- Services own their domain logic
- Communication channels are simple (REST, messaging)
- No complex ESB or orchestration layer

### 5. Decentralized Governance

Teams can choose technologies best suited for their service.

```
Order Service    → Java/Spring Boot
Payment Service  → Node.js/Express
Analytics Service → Python/Flask
```

### 6. Decentralized Data Management

Each service manages its own database (database per service pattern).

```java
// Order Service - owns its data
@Entity
public class Order {
    @Id
    private Long id;
    private Long customerId; // Reference, not foreign key
    // ...
}
```

### 7. Infrastructure Automation

Heavy reliance on CI/CD, automated testing, and infrastructure as code.

### 8. Design for Failure

Services must be designed to handle failures gracefully.

```java
@CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
public PaymentResponse processPayment(PaymentRequest request) {
    return paymentClient.process(request);
}

public PaymentResponse paymentFallback(PaymentRequest request, Exception ex) {
    return PaymentResponse.pending();
}
```

### 9. Evolutionary Design

Services can be replaced or upgraded independently.

---

## Benefits and Challenges

### Benefits

#### 1. **Independent Deployability**
```
Old Service V1 → Deploy V2 → Only Service X updates
Other services remain unchanged
```

#### 2. **Technology Flexibility**
```java
// Order Service - Spring Boot
@RestController
public class OrderController {
    // Java implementation
}
```

```javascript
// Notification Service - Node.js
app.post('/notifications', (req, res) => {
    // JavaScript implementation
});
```

#### 3. **Fault Isolation**
If Payment Service fails, Order Service can still accept orders (with degraded functionality).

#### 4. **Scalability**
```
High traffic on Catalog Service?
→ Scale only Catalog Service instances
→ Other services remain unchanged
```

#### 5. **Team Autonomy**
- Small, focused teams
- Independent decision-making
- Faster development cycles

#### 6. **Better Resource Utilization**
Scale services based on their specific needs, not entire application.

### Challenges

#### 1. **Distributed System Complexity**
- Network failures
- Latency issues
- Partial failures
- Service discovery

#### 2. **Data Consistency**
```
Problem: How to maintain consistency across services?
Solutions:
- Saga pattern
- Event sourcing
- CQRS
- Eventual consistency
```

#### 3. **Testing Complexity**
- Unit testing is simpler
- Integration testing is complex
- End-to-end testing requires all services

#### 4. **Operational Overhead**
- Multiple deployments
- Monitoring many services
- Log aggregation
- Distributed tracing

#### 5. **Network Latency**
```
Monolith: Method call (nanoseconds)
Microservices: HTTP call (milliseconds)
```

#### 6. **Versioning and Backward Compatibility**
Need to maintain API contracts while evolving services.

---

## Domain-Driven Design (DDD)

Domain-Driven Design helps identify microservice boundaries based on business domains.

### Key DDD Concepts for Microservices

#### 1. **Bounded Context**
A boundary within which a particular domain model is defined. Each bounded context typically maps to one microservice.

```
E-Commerce System:

┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  Order Context  │  │ Payment Context │  │Shipping Context │
│                 │  │                 │  │                 │
│  Order          │  │  Payment        │  │  Shipment       │
│  OrderItem      │  │  Transaction    │  │  Address        │
│  Customer       │  │  Customer       │  │  Customer       │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

Note: "Customer" means different things in each context.

#### 2. **Ubiquitous Language**
A common language shared by developers and domain experts within a bounded context.

### Applying DDD to Microservices

**Step 1: Identify Bounded Contexts**
```
E-Commerce Domain:
- Sales Context → Order Service
- Payment Context → Payment Service
- Inventory Context → Inventory Service
```

**Step 2: Define Context Boundaries**
```java
// Order Service owns Order data
public class Order {
    private Long orderId;
    private Long customerId; // Reference to Customer Service (not FK)
    private List<OrderItem> items;
}

// Customer Service owns Customer data
public class Customer {
    private Long customerId;
    private String name;
}
```

---

## 12 Factor App Methodology

The 12 Factor App is a methodology for building software-as-a-service applications that are portable, scalable, and maintainable.

### I. Codebase
**One codebase tracked in version control, many deploys**

```
✅ One Git repository per microservice
✅ Same codebase → dev, staging, production

❌ Multiple repositories for same service
❌ Different code for different environments
```

### II. Dependencies
**Explicitly declare and isolate dependencies**

```xml
<!-- Maven: Explicit dependencies -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>3.2.0</version>
    </dependency>
</dependencies>
```

### III. Config
**Store config in the environment**

```yaml
# application.yml - NO hardcoded values
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
```

```bash
# Environment variables
export DATABASE_URL=jdbc:postgresql://localhost:5432/orderdb
export DATABASE_USER=admin
export DATABASE_PASSWORD=secret123
```

### IV. Backing Services
**Treat backing services as attached resources**

```yaml
# Can swap databases by changing config
spring:
  datasource:
    url: ${DATABASE_URL} # Local, staging, or production DB
```

### V. Build, Release, Run
**Strictly separate build and run stages**

```
Build:  code → executable bundle (JAR/WAR)
Release: bundle + config → release
Run:    execute release in environment

# CI/CD Pipeline
Build → Create artifact → Deploy to env → Run
```

### VI. Processes
**Execute the app as one or more stateless processes**

```java
// ❌ Storing state in memory
public class OrderController {
    private List<Order> orders = new ArrayList<>(); // BAD!
}

// ✅ Stateless - use external storage
@RestController
public class OrderController {
    @Autowired
    private OrderRepository orderRepository; // GOOD!
}
```

### VII. Port Binding
**Export services via port binding**

```yaml
# application.yml
server:
  port: ${PORT:8080} # Configurable port
```

```java
@SpringBootApplication
public class OrderServiceApplication {
    // Self-contained, exports HTTP service on port
}
```

### VIII. Concurrency
**Scale out via the process model**

```
Scale horizontally by running multiple instances:

Instance 1 (Port 8081) ─┐
Instance 2 (Port 8082) ─┤→ Load Balancer → Clients
Instance 3 (Port 8083) ─┘
```

### IX. Disposability
**Maximize robustness with fast startup and graceful shutdown**

```java
@Configuration
public class GracefulShutdownConfig {
    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat() {
        return new GracefulShutdownTomcat();
    }
}
```

```yaml
# Fast startup
spring:
  jmx:
    enabled: false # Disable if not needed
```

### X. Dev/Prod Parity
**Keep development, staging, and production as similar as possible**

```
Development   → PostgreSQL
Staging       → PostgreSQL (same version)
Production    → PostgreSQL (same version)

❌ SQLite (dev) → MySQL (staging) → PostgreSQL (prod)
```

### XI. Logs
**Treat logs as event streams**

```java
// Don't manage log files
logger.info("Order created: {}", orderId);

// Let environment handle log routing:
// - Development: Console
// - Production: Log aggregation service
```

### XII. Admin Processes
**Run admin/management tasks as one-off processes**

```bash
# Database migration as separate process
java -jar flyway.jar migrate

# Data cleanup job
java -jar app.jar --spring.profiles.active=admin cleanup
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| **MSA** | Collection of small, independent, loosely coupled services |
| **Monolith vs Microservices** | Trade-offs between simplicity and scalability/flexibility |
| **Characteristics** | Componentization, business-focused, decentralized, resilient |
| **Benefits** | Independent deployment, scalability, fault isolation, team autonomy |
| **Challenges** | Distributed complexity, data consistency, testing, operations |
| **DDD** | Bounded contexts map to service boundaries |
| **12 Factor** | Methodology for portable, scalable, maintainable services |

## Best Practices

1. **Start with a Monolith**: Don't start with microservices for new projects
2. **Identify Bounded Contexts**: Use DDD to find natural service boundaries
3. **One Service, One Database**: Each service owns its data
4. **API Contracts**: Define and version APIs carefully
5. **Embrace Automation**: CI/CD, testing, deployment, monitoring
6. **Design for Failure**: Circuit breakers, retries, fallbacks
7. **Follow 12 Factor**: Portable and maintainable services
8. **Monitor Everything**: Distributed tracing, centralized logging, metrics

## Next Topic

Continue to [Service Discovery](./02-service-discovery.md) to learn how services find and communicate with each other in a microservices ecosystem.
