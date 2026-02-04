# Monitoring and Observability

## Introduction

In a microservices architecture, monitoring and observability are critical because failures can occur across multiple services, and troubleshooting becomes complex.

### Monitoring vs Observability

**Monitoring:**
- Tracking known metrics and health
- Answering "Is the system working?"
- Alerts based on predefined thresholds

**Observability:**
- Understanding system behavior from outputs
- Answering "Why is the system behaving this way?"
- Exploring unknown issues

### Three Pillars of Observability

```
┌──────────────────────────────────────────┐
│         Observability Pillars            │
├──────────────────────────────────────────┤
│  1. Logs       - Event records           │
│  2. Metrics    - Numerical measurements  │
│  3. Traces     - Request flows           │
└──────────────────────────────────────────┘
```

---

## Distributed Tracing

### The Problem

In microservices, a single user request may traverse multiple services:

```
User Request → API Gateway → Order Service → Payment Service → Email Service
                                    ↓
                              Inventory Service
```

**Challenges:**
- How to track a request across services?
- Where did the failure occur?
- Which service is causing latency?

### The Solution: Distributed Tracing

Distributed tracing tracks requests as they flow through multiple services, providing visibility into the entire request lifecycle.

### Spring Cloud Sleuth

Spring Cloud Sleuth automatically instruments Spring Boot applications to generate and propagate trace information.

#### Key Concepts

**Trace ID:** Unique identifier for entire request flow across all services
**Span ID:** Unique identifier for each unit of work (service call)
**Parent Span ID:** Links spans together

```
Trace ID: abc123 (same for entire request)
│
├─ Span: API Gateway    (Span ID: 001, Parent: -)
│  └─ Span: Order Service   (Span ID: 002, Parent: 001)
│     ├─ Span: Payment Service  (Span ID: 003, Parent: 002)
│     └─ Span: Inventory Service (Span ID: 004, Parent: 002)
```

#### Setup Sleuth

**Dependencies:**
```xml
<dependencies>
    <!-- Spring Cloud Sleuth -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
</dependencies>
```

**That's it! Sleuth auto-configures.**

#### Automatic Instrumentation

Sleuth automatically instruments:
- RestTemplate
- WebClient
- Feign Client
- Spring MVC controllers
- Async methods
- Messaging (RabbitMQ, Kafka)

**Log Output:**
```
2024-01-15 10:30:45.123 INFO [order-service,abc123,002,true] OrderController: Creating order
                              [service-name, trace-id, span-id, exportable]
```

#### Manual Span Creation

```java
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

@Service
public class OrderService {

    private final Tracer tracer;

    public OrderService(Tracer tracer) {
        this.tracer = tracer;
    }

    public Order processOrder(OrderRequest request) {
        // Create custom span
        Span span = tracer.nextSpan().name("process-order").start();
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            log.info("Processing order");

            // Add custom tags
            span.tag("order.id", request.getId().toString());
            span.tag("order.amount", request.getAmount().toString());

            // Business logic
            Order order = createOrder(request);

            return order;
        } finally {
            span.end();
        }
    }

    public void validateOrder(Order order) {
        Span span = tracer.nextSpan().name("validate-order").start();
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            // Validation logic
            if (order.getItems().isEmpty()) {
                span.tag("error", "empty-order");
                throw new ValidationException("Order has no items");
            }
        } finally {
            span.end();
        }
    }
}
```

#### Custom Annotations

```java
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;

@Service
public class PaymentService {

    @NewSpan("process-payment")  // Creates new span
    public Payment processPayment(
            @SpanTag("payment.orderId") Long orderId,  // Add as tag
            @SpanTag("payment.amount") BigDecimal amount) {

        log.info("Processing payment for order: {}", orderId);
        return paymentClient.process(orderId, amount);
    }

    @NewSpan
    public void refundPayment(@SpanTag("refund.paymentId") Long paymentId) {
        log.info("Refunding payment: {}", paymentId);
        paymentClient.refund(paymentId);
    }
}
```

### Zipkin

Zipkin is a distributed tracing system that collects and visualizes trace data.

#### Setup Zipkin Server

**Run with Docker:**
```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

**Access UI:**
```
http://localhost:9411
```

#### Configure Sleuth to Send to Zipkin

**Dependencies:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```

**Configuration:**
```yaml
spring:
  application:
    name: order-service
  sleuth:
    sampler:
      probability: 1.0  # Sample 100% of requests (use 0.1 for 10% in prod)
  zipkin:
    base-url: http://localhost:9411
    sender:
      type: web  # or rabbit, kafka
```

**With RabbitMQ (Recommended for Production):**
```xml
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>
```

```yaml
spring:
  zipkin:
    sender:
      type: rabbit
  rabbitmq:
    host: localhost
    port: 5672
```

#### Zipkin UI Features

1. **Search Traces**: Find traces by service, span name, tags
2. **Trace Timeline**: Visualize request flow across services
3. **Service Dependencies**: View service dependency graph
4. **Latency Analysis**: Identify slow services
5. **Error Tracking**: Find failed requests

**Example Trace Visualization:**
```
API Gateway      |-----------|
Order Service            |------------|
Payment Service                  |------|
Inventory Service               |-----|
Email Service                         |--|

Time: 0ms      50ms     100ms    150ms   200ms
Total Duration: 185ms
```

### Distributed Tracing Best Practices

1. **Sample Appropriately**: 100% in dev, 1-10% in production
2. **Add Meaningful Tags**: Order ID, user ID, business context
3. **Instrument Critical Paths**: Database queries, external API calls
4. **Set Timeouts**: Prevent long-running traces
5. **Monitor Zipkin**: Ensure it's not a bottleneck

---

## Logging Aggregation

### The Problem

```
Order Service Instance 1    → log file 1
Order Service Instance 2    → log file 2
Order Service Instance 3    → log file 3
Payment Service Instance 1  → log file 4
...

How to search across all logs?
```

### Solution: Centralized Logging

```
All Services → Log Aggregator → Elasticsearch → Kibana (UI)
```

### Centralized Logging

For production, aggregate logs from all services using tools like:
- **ELK Stack** (Elasticsearch, Logstash, Kibana)
- **Splunk**
- **Datadog**

#### Structured Logging with MDC

```java
import org.slf4j.MDC;

@Service
public class OrderService {

    public Order createOrder(OrderRequest request) {
        MDC.put("userId", request.getUserId());
        MDC.put("orderId", UUID.randomUUID().toString());

        try {
            log.info("Creating order for user: {}", request.getUserId());
            return processOrder(request);
        } finally {
            MDC.clear();
        }
    }
}
```

#### Log Levels Best Practices

```java
// ERROR - Action required, system functionality affected
log.error("Payment service unavailable", exception);

// WARN - Warning condition, not an error but needs attention
log.warn("Order processing took {} ms, threshold is {} ms", duration, threshold);

// INFO - Important business events
log.info("Order {} created for user {}", orderId, userId);

// DEBUG - Detailed information for debugging
log.debug("Validating order: {}", order);

// TRACE - Very detailed, trace execution flow
log.trace("Entering method processPayment with params: {}", params);
```

### Logging Best Practices

1. **Use Structured Logging**: JSON format for easy parsing
2. **Include Context**: User ID, order ID, trace ID
3. **Don't Log Sensitive Data**: Passwords, credit cards, tokens
4. **Use Appropriate Levels**: ERROR for errors, INFO for business events
5. **Log Correlation IDs**: Link logs across services
6. **Avoid Excessive Logging**: Balance detail with performance
7. **Log Exceptions Properly**: Include stack traces for errors

```java
// Good
log.info("Order created: orderId={}, userId={}, amount={}",
    order.getId(), order.getUserId(), order.getAmount());

// Bad - No context
log.info("Order created");

// Bad - Sensitive data
log.info("User password: {}", password);  // NEVER!

// Good exception logging
try {
    processOrder(order);
} catch (Exception ex) {
    log.error("Failed to process order: orderId={}", order.getId(), ex);
    throw ex;
}

// Bad - No exception details
catch (Exception ex) {
    log.error("Error occurred");  // Missing exception!
}
```

---

## Metrics Collection

### Spring Boot Actuator

Spring Boot Actuator provides production-ready features including metrics, health checks, and monitoring endpoints.

#### Setup

**Dependency:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Configuration:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
      base-path: /actuator
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: ${spring.application.name}
      environment: ${spring.profiles.active}
```

#### Health Checks

```java
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1000)) {
                return Health.up()
                    .withDetail("database", "Available")
                    .withDetail("connection", "Healthy")
                    .build();
            }
        } catch (Exception ex) {
            return Health.down()
                .withDetail("database", "Unavailable")
                .withException(ex)
                .build();
        }
        return Health.down().build();
    }
}
```

**Custom Health Check:**
```java
@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Health health() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                "http://payment-service/actuator/health",
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return Health.up()
                    .withDetail("payment-service", "Available")
                    .build();
            }
        } catch (Exception ex) {
            return Health.down()
                .withDetail("payment-service", "Unavailable")
                .withException(ex)
                .build();
        }
        return Health.down().build();
    }
}
```

**Access health:**
```
GET http://localhost:8081/actuator/health

{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "Available",
        "connection": "Healthy"
      }
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

#### Custom Metrics

Use Micrometer to create custom metrics:

```java
@Service
public class OrderService {

    private final Counter orderCreatedCounter;

    public OrderService(MeterRegistry registry) {
        this.orderCreatedCounter = Counter.builder("orders.created")
            .description("Total number of orders created")
            .register(registry);
    }

    public Order createOrder(OrderRequest request) {
        Order order = processOrder(request);
        orderCreatedCounter.increment();
        return order;
    }
}
```

### Prometheus Integration

Add Prometheus dependency to expose metrics:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Access metrics at: `http://localhost:8081/actuator/prometheus`

Use **Grafana** to visualize Prometheus metrics with pre-built Spring Boot dashboards.

---

## Summary

| Concept | Purpose | Tools |
|---------|---------|-------|
| **Distributed Tracing** | Track requests across services | Sleuth, Zipkin, Jaeger |
| **Logging Aggregation** | Centralize logs from all services | ELK Stack, Splunk |
| **Metrics Collection** | Monitor system health and performance | Actuator, Prometheus, Grafana |
| **Health Checks** | Service availability monitoring | Actuator endpoints |
| **Security** | Protect services and data | OAuth2, mTLS, validation |

## Monitoring Checklist

- [ ] Configure Spring Cloud Sleuth for tracing
- [ ] Set up Zipkin for trace visualization
- [ ] Implement structured logging (JSON format)
- [ ] Configure log aggregation (ELK/Splunk)
- [ ] Enable Spring Boot Actuator
- [ ] Create custom health checks
- [ ] Implement custom metrics
- [ ] Set up Prometheus for metrics collection
- [ ] Create Grafana dashboards
- [ ] Configure alerts for critical metrics
- [ ] Implement security best practices
- [ ] Set up API authentication and authorization
- [ ] Enable rate limiting
- [ ] Validate all inputs
- [ ] Secure sensitive configuration
- [ ] Implement audit logging
- [ ] Regular security scans

## Observability Best Practices

1. **Instrument Everything**: Trace, log, and measure all critical paths
2. **Use Correlation IDs**: Link logs, traces, and metrics
3. **Set Alerts Wisely**: Avoid alert fatigue
4. **Monitor Business Metrics**: Not just technical metrics
5. **Create Dashboards**: Visualize system health
6. **Test Monitoring**: Ensure alerts work
7. **Document Runbooks**: How to respond to alerts
8. **Review Regularly**: Update metrics and dashboards
9. **Secure Monitoring**: Protect monitoring infrastructure
10. **Train Teams**: Everyone should understand monitoring

## Conclusion

Effective monitoring and observability are essential for maintaining healthy microservices. By implementing distributed tracing, centralized logging, and comprehensive metrics collection, you can quickly identify and resolve issues, ensure system reliability, and provide excellent user experience.

---

**Course Complete!** You now have comprehensive knowledge of microservices architecture, from design principles to implementation patterns, security, and observability.
