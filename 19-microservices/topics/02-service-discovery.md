# Service Discovery

## Spring Cloud Overview

Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems, including configuration management, service discovery, circuit breakers, intelligent routing, and more.

### What is Spring Cloud?

Spring Cloud is a framework built on top of Spring Boot that provides libraries and tools for building cloud-native applications and implementing microservices patterns.

### Key Components

```
┌─────────────────────────────────────────┐
│         Spring Cloud Ecosystem          │
├─────────────────────────────────────────┤
│ • Spring Cloud Netflix (Eureka)         │
│ • Spring Cloud Gateway                  │
│ • Spring Cloud Config                   │
│ • Spring Cloud LoadBalancer             │
│ • Spring Cloud Circuit Breaker          │
│ • Spring Cloud OpenFeign                │
│ • Spring Cloud Sleuth                   │
└─────────────────────────────────────────┘
```

### Spring Cloud vs Spring Boot

| Spring Boot | Spring Cloud |
|-------------|--------------|
| Framework for building microservices | Framework for connecting microservices |
| Stand-alone applications | Distributed system patterns |
| Auto-configuration | Cloud patterns and tools |
| Embedded server | Service discovery, config, etc. |

### Maven Dependencies

```xml
<properties>
    <spring-cloud.version>2023.0.0</spring-cloud.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## Service Discovery with Eureka

### What is Service Discovery?

Service discovery is the automatic detection of devices and services on a network. In microservices, it allows services to find and communicate with each other without hardcoding hostname and port.

### Why Service Discovery?

**Problem Without Service Discovery:**
```java
// Hardcoded URLs - problematic!
@Value("http://localhost:8081")
private String orderServiceUrl;

@Value("http://localhost:8082")
private String paymentServiceUrl;
```

**Issues:**
- Hardcoded URLs
- Manual configuration updates
- No dynamic scaling
- Port conflicts
- Environment-specific configuration

**Solution With Service Discovery:**
```java
// Dynamic discovery
@FeignClient(name = "order-service")
public interface OrderClient {
    // Eureka resolves "order-service" to actual instances
}
```

### Service Discovery Architecture

```
┌─────────────────────────────────────────┐
│        Eureka Server (Registry)         │
│   Maintains registry of all services    │
└─────────────────┬───────────────────────┘
                  │
        ┌─────────┴─────────┐
        │                   │
        ▼                   ▼
┌──────────────┐    ┌──────────────┐
│   Service A  │    │   Service B  │
│  (Client)    │───▶│  (Client)    │
│              │    │              │
│ 1. Register  │    │ 1. Register  │
│ 2. Heartbeat │    │ 2. Discover  │
│ 3. Discover  │    │ 3. Call      │
└──────────────┘    └──────────────┘
```

### Setting Up Eureka Server

#### 1. Create Eureka Server Application

**pom.xml:**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```

**Application Class:**
```java
package com.example.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  // Enable Eureka Server
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**application.yml:**
```yaml
spring:
  application:
    name: eureka-server

server:
  port: 8761  # Default Eureka port

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false  # Don't register itself
    fetchRegistry: false       # Don't fetch registry
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    enable-self-preservation: false  # Disable in dev (enable in prod)
```

**Access Eureka Dashboard:**
```
http://localhost:8761
```

### Setting Up Eureka Client

#### 1. Add Dependencies

**pom.xml:**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

#### 2. Enable Discovery Client

**Application Class:**
```java
package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // Enable service discovery
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

#### 3. Configure Client

**application.yml:**
```yaml
spring:
  application:
    name: order-service  # Service name for discovery

server:
  port: 8081  # Can be dynamic: ${PORT:0}

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/  # Eureka server URL
    registerWithEureka: true
    fetchRegistry: true
  instance:
    preferIpAddress: true
    instance-id: ${spring.application.name}:${server.port}
    lease-renewal-interval-in-seconds: 30  # Heartbeat interval
    lease-expiration-duration-in-seconds: 90  # Expiration time
```

### Service Registration Flow

```
1. Service Startup
   ↓
2. Register with Eureka (POST /eureka/apps/{appName})
   ↓
3. Send Heartbeat every 30s (PUT /eureka/apps/{appName}/{instanceId})
   ↓
4. Renew Registration
   ↓
5. On Shutdown: De-register (DELETE /eureka/apps/{appName}/{instanceId})
```

### Discovery and Communication

**Using DiscoveryClient:**
```java
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class OrderController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/discover")
    public List<ServiceInstance> getInstances() {
        return discoveryClient.getInstances("payment-service");
    }
}
```

**Using RestTemplate with @LoadBalanced:**
```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

    public Payment processPayment(Long orderId) {
        // Use service name instead of URL
        String url = "http://payment-service/api/payments";
        return restTemplate.postForObject(url, paymentRequest, Payment.class);
    }
}
```

---

## Load Balancing

### What is Load Balancing?

Load balancing distributes network traffic across multiple servers to ensure no single server bears too much load, improving responsiveness and availability.

### Types of Load Balancing

#### 1. Server-Side Load Balancing

Traditional approach using hardware or software load balancers.

```
Clients → Load Balancer → Service Instances
```

**Examples:**
- NGINX
- HAProxy
- AWS ELB/ALB

#### 2. Client-Side Load Balancing

Client contains load balancing logic and chooses which service instance to call.

```
Client (with LB logic) → Service Instance 1
                      → Service Instance 2
                      → Service Instance 3
```

### Spring Cloud LoadBalancer

Spring Cloud LoadBalancer is a client-side load balancer that replaced Netflix Ribbon.

#### Setup

**pom.xml:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

#### Using with RestTemplate

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LoadBalancerConfig {

    @Bean
    @LoadBalanced  // Enable load balancing
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**Usage:**
```java
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public PaymentResponse processPayment(PaymentRequest request) {
        // Load balancer automatically selects an instance
        String url = "http://payment-service/api/payments";
        return restTemplate.postForObject(url, request, PaymentResponse.class);
    }
}
```

#### Using with WebClient

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
```

**Usage:**
```java
@Service
public class OrderService {

    private final WebClient webClient;

    public OrderService(@LoadBalanced WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Mono<PaymentResponse> processPayment(PaymentRequest request) {
        return webClient.post()
            .uri("http://payment-service/api/payments")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(PaymentResponse.class);
    }
}
```

### Load Balancing Algorithms

#### 1. Round Robin (Default)

Distributes requests sequentially across all instances.

```
Request 1 → Instance 1
Request 2 → Instance 2
Request 3 → Instance 3
Request 4 → Instance 1 (cycle repeats)
```

**Configuration:**
```java
@Configuration
public class LoadBalancerConfiguration {

    @Bean
    public ServiceInstanceListSupplier serviceInstanceListSupplier(
            ConfigurableApplicationContext context) {
        return ServiceInstanceListSupplier.builder()
                .withDiscoveryClient()
                .withHealthChecks()
                .build(context);
    }
}
```

#### 2. Random

Randomly selects an instance for each request.

#### 3. Weighted Response Time

Assigns weights based on response times; faster instances get more requests.

---

## Summary

| Concept | Key Points |
|---------|------------|
| **Spring Cloud** | Framework for distributed system patterns |
| **Service Discovery** | Automatic detection and registration of services |
| **Eureka Server** | Service registry that maintains service instances |
| **Eureka Client** | Services that register with and discover from Eureka |
| **Load Balancing** | Distribute traffic across multiple instances |
| **Client-Side LB** | Load balancing logic in the client |
| **Algorithms** | Round Robin (default), Random, Weighted, Custom |

## Best Practices

1. **Use Service Names**: Never hardcode URLs; use service names
2. **Enable Health Checks**: Ensure only healthy instances receive traffic
3. **High Availability**: Run multiple Eureka servers in production
4. **Self-Preservation**: Enable in production, disable in development
5. **Proper Timeouts**: Configure appropriate heartbeat and expiration times
6. **Instance IDs**: Use unique instance IDs for multiple instances
7. **Monitor**: Track service registrations, heartbeats, and load distribution
8. **Graceful Shutdown**: Ensure services de-register properly

## Common Issues and Solutions

**Issue 1: Services not discovered**
```yaml
# Solution: Check Eureka client configuration
eureka:
  client:
    registerWithEureka: true  # Must be true
    fetchRegistry: true       # Must be true
```

**Issue 2: Self-preservation mode warnings**
```yaml
# Solution: Disable in development
eureka:
  server:
    enable-self-preservation: false
```

**Issue 3: Instances not removed after shutdown**
```yaml
# Solution: Adjust eviction settings
eureka:
  server:
    eviction-interval-timer-in-ms: 5000  # Check every 5 seconds
```

## Next Topic

Continue to [API Gateway](./03-api-gateway.md) to learn about routing, filtering, and managing API requests in a microservices architecture.
