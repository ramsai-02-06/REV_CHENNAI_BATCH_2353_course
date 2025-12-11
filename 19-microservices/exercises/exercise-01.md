# Exercise: Build a Microservices Architecture

## Objective
Design and implement a simple microservices system demonstrating service decomposition, inter-service communication, and common patterns.

## Requirements

### System: E-Commerce Platform
Build three microservices:

1. **Product Service** (port 8081)
   - Manage product catalog
   - CRUD operations for products

2. **Order Service** (port 8082)
   - Create and manage orders
   - Calls Product Service to validate products

3. **API Gateway** (port 8080)
   - Single entry point
   - Route requests to services

### Microservices Concepts to Implement

1. **Service Discovery**
   - Use Eureka or Consul
   - Services register on startup
   - Dynamic service lookup

2. **Inter-Service Communication**
   - REST with RestTemplate/WebClient
   - Feign Client (declarative)

3. **API Gateway**
   - Spring Cloud Gateway
   - Route configuration
   - Load balancing

4. **Circuit Breaker**
   - Resilience4j
   - Fallback methods
   - Timeout handling

5. **Configuration Management**
   - Externalized configuration
   - Spring Cloud Config (optional)

### Project Structure
```
microservices-demo/
├── product-service/
│   ├── src/main/java/
│   │   └── com.example.product/
│   │       ├── ProductServiceApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       └── model/
│   └── pom.xml
│
├── order-service/
│   ├── src/main/java/
│   │   └── com.example.order/
│   │       ├── OrderServiceApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       ├── client/  (Feign client)
│   │       └── model/
│   └── pom.xml
│
├── api-gateway/
│   ├── src/main/java/
│   │   └── com.example.gateway/
│   └── application.yml
│
└── discovery-server/  (Eureka)
```

### Configuration Examples

#### API Gateway (application.yml)
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: lb://PRODUCT-SERVICE
          predicates:
            - Path=/api/products/**
        - id: order-service
          uri: lb://ORDER-SERVICE
          predicates:
            - Path=/api/orders/**
```

#### Feign Client
```java
@FeignClient(name = "product-service", fallback = ProductClientFallback.class)
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    ProductDTO getProduct(@PathVariable Long id);
}
```

## Skills Tested
- Microservices architecture
- Service discovery (Eureka)
- API Gateway pattern
- Inter-service communication
- Circuit breaker pattern
- Distributed systems concepts
