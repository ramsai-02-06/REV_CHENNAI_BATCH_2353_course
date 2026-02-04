# Inter-Service Communication

## Overview

In a microservices architecture, services need to communicate with each other to fulfill business requirements. There are two main communication patterns: synchronous and asynchronous.

### Communication Patterns

```
Synchronous (Request-Response):
Client → Request → Service → Response → Client
(Client waits for response)

Asynchronous (Event-Driven):
Service A → Event → Message Broker → Service B
(Service A doesn't wait)
```

### Choosing Communication Type

| Use Case | Synchronous | Asynchronous |
|----------|-------------|--------------|
| Real-time data needed | ✓ | |
| Can tolerate latency | | ✓ |
| High coupling acceptable | ✓ | |
| Loose coupling required | | ✓ |
| Simple request-response | ✓ | |
| Fire-and-forget | | ✓ |
| Multiple consumers | | ✓ |

---

## REST Template

### What is RestTemplate?

RestTemplate is a synchronous HTTP client provided by Spring for making REST API calls. It simplifies communication with HTTP servers and enforces RESTful principles.

**Note:** RestTemplate is in maintenance mode. For new projects, use WebClient.

### Basic Setup

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### HTTP Methods

#### GET Request

```java
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    // 1. Using getForObject - returns body directly
    public Product getProduct(Long productId) {
        String url = "http://localhost:8081/api/products/" + productId;
        return restTemplate.getForObject(url, Product.class);
    }

    // 2. Using getForEntity - returns ResponseEntity
    public ResponseEntity<Product> getProductWithHeaders(Long productId) {
        String url = "http://localhost:8081/api/products/" + productId;
        ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);

        HttpHeaders headers = response.getHeaders();
        HttpStatus statusCode = response.getStatusCode();
        Product product = response.getBody();

        return response;
    }

    // 3. Using exchange - most flexible
    public Product getProductWithExchange(Long productId) {
        String url = "http://localhost:8081/api/products/" + productId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Product> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Product.class
        );

        return response.getBody();
    }
}
```

#### POST Request

```java
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    // 1. Using postForObject
    public Order createOrder(OrderRequest request) {
        String url = "http://localhost:8082/api/orders";
        return restTemplate.postForObject(url, request, Order.class);
    }

    // 2. Using postForEntity
    public ResponseEntity<Order> createOrderWithResponse(OrderRequest request) {
        String url = "http://localhost:8082/api/orders";
        ResponseEntity<Order> response = restTemplate.postForEntity(
            url,
            request,
            Order.class
        );
        return response;
    }

    // 3. Using postForLocation
    public URI createOrderAndGetLocation(OrderRequest request) {
        String url = "http://localhost:8082/api/orders";
        return restTemplate.postForLocation(url, request);
    }

    // 4. Using exchange with headers
    public Order createOrderWithHeaders(OrderRequest request, String token) {
        String url = "http://localhost:8082/api/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<OrderRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Order> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Order.class
        );

        return response.getBody();
    }
}
```

#### PUT Request

```java
public void updateProduct(Long productId, ProductRequest request) {
    String url = "http://localhost:8081/api/products/" + productId;
    restTemplate.put(url, request);
}

// With exchange
public Product updateProductWithResponse(Long productId, ProductRequest request) {
    String url = "http://localhost:8081/api/products/" + productId;

    HttpEntity<ProductRequest> entity = new HttpEntity<>(request);

    ResponseEntity<Product> response = restTemplate.exchange(
        url,
        HttpMethod.PUT,
        entity,
        Product.class
    );

    return response.getBody();
}
```

#### DELETE Request

```java
public void deleteProduct(Long productId) {
    String url = "http://localhost:8081/api/products/" + productId;
    restTemplate.delete(url);
}

// With exchange for response
public ResponseEntity<Void> deleteProductWithResponse(Long productId) {
    String url = "http://localhost:8081/api/products/" + productId;

    return restTemplate.exchange(
        url,
        HttpMethod.DELETE,
        null,
        Void.class
    );
}
```

### URL Parameters

```java
// 1. Using URI variables
public Product getProduct(Long productId) {
    String url = "http://localhost:8081/api/products/{id}";
    return restTemplate.getForObject(url, Product.class, productId);
}

// 2. Using Map for multiple parameters
public List<Product> searchProducts(String category, BigDecimal minPrice) {
    String url = "http://localhost:8081/api/products/search?category={category}&minPrice={minPrice}";

    Map<String, Object> params = new HashMap<>();
    params.put("category", category);
    params.put("minPrice", minPrice);

    return restTemplate.getForObject(url, List.class, params);
}

// 3. Using UriComponentsBuilder
public List<Product> searchProductsWithBuilder(String category, BigDecimal minPrice) {
    String url = UriComponentsBuilder
        .fromHttpUrl("http://localhost:8081/api/products/search")
        .queryParam("category", category)
        .queryParam("minPrice", minPrice)
        .toUriString();

    return restTemplate.getForObject(url, List.class);
}
```

### Load Balanced RestTemplate

```java
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced  // Enable load balancing with Eureka
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public Product getProduct(Long productId) {
        // Use service name instead of host:port
        String url = "http://product-service/api/products/" + productId;
        return restTemplate.getForObject(url, Product.class);
    }
}
```

### Error Handling

```java
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public Product getProduct(Long productId) {
        try {
            String url = "http://product-service/api/products/" + productId;
            return restTemplate.getForObject(url, Product.class);

        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Product not found: {}", productId);
            throw new ProductNotFoundException(productId);

        } catch (HttpClientErrorException ex) {
            log.error("Client error: {}", ex.getStatusCode());
            throw new ServiceException("Invalid request");

        } catch (HttpServerErrorException ex) {
            log.error("Server error: {}", ex.getStatusCode());
            throw new ServiceException("Product service unavailable");

        } catch (RestClientException ex) {
            log.error("Network error", ex);
            throw new ServiceException("Unable to connect to product service");
        }
    }
}
```

### Custom RestTemplate Configuration

```java
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(5))
            .interceptors(loggingInterceptor())
            .errorHandler(new CustomResponseErrorHandler())
            .build();
    }

    @Bean
    public ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            log.info("Request: {} {}", request.getMethod(), request.getURI());
            ClientHttpResponse response = execution.execute(request, body);
            log.info("Response: {}", response.getStatusCode());
            return response;
        };
    }
}
```

---

## WebClient (Brief Overview)

WebClient is a modern, reactive HTTP client introduced in Spring WebFlux. It supports both synchronous and asynchronous operations and is the recommended replacement for RestTemplate for new projects.

```java
@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}

@Service
public class ProductService {

    private final WebClient webClient;

    public ProductService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Product getProduct(Long productId) {
        return webClient.get()
            .uri("http://product-service/api/products/{id}", productId)
            .retrieve()
            .bodyToMono(Product.class)
            .block();  // Blocks until response received
    }
}
```

For reactive applications, use `Mono` and `Flux` return types instead of `.block()`.

---

## Feign Client

### What is Feign?

Feign is a declarative HTTP client that makes writing web service clients easier. You create an interface and annotate it, and Feign generates the implementation.

### Setup

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

```java
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients  // Enable Feign clients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

### Creating Feign Client

```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// Basic Feign client with service name (uses Eureka)
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    Product getProduct(@PathVariable("id") Long productId);

    @GetMapping("/api/products")
    List<Product> getAllProducts();

    @PostMapping("/api/products")
    Product createProduct(@RequestBody ProductRequest request);

    @PutMapping("/api/products/{id}")
    Product updateProduct(@PathVariable("id") Long id,
                         @RequestBody ProductRequest request);

    @DeleteMapping("/api/products/{id}")
    void deleteProduct(@PathVariable("id") Long id);
}

// With URL (without service discovery)
@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {
    // Same methods
}
```

### Using Feign Client

```java
@Service
public class OrderService {

    @Autowired
    private ProductClient productClient;

    public Order createOrder(OrderRequest request) {
        // Simple method call - Feign handles HTTP
        Product product = productClient.getProduct(request.getProductId());

        if (product.getStock() < request.getQuantity()) {
            throw new InsufficientStockException();
        }

        // Create order
        return orderRepository.save(new Order(request));
    }

    public List<Product> getAvailableProducts() {
        return productClient.getAllProducts();
    }
}
```

### Request Parameters

```java
@FeignClient(name = "product-service")
public interface ProductClient {

    // Query parameters
    @GetMapping("/api/products/search")
    List<Product> searchProducts(
        @RequestParam("category") String category,
        @RequestParam("minPrice") BigDecimal minPrice,
        @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice
    );

    // Multiple path variables
    @GetMapping("/api/categories/{categoryId}/products/{productId}")
    Product getProductInCategory(
        @PathVariable("categoryId") Long categoryId,
        @PathVariable("productId") Long productId
    );

    // Request headers
    @GetMapping("/api/products/{id}")
    Product getProduct(
        @PathVariable("id") Long id,
        @RequestHeader("Authorization") String authToken
    );
}
```

### Configuration

```java
import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    // Logging level
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;  // NONE, BASIC, HEADERS, FULL
    }

    // Timeouts
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            5000,  // Connect timeout (ms)
            10000  // Read timeout (ms)
        );
    }

    // Retry configuration
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
            100,   // Initial interval (ms)
            1000,  // Max interval (ms)
            3      // Max attempts
        );
    }
}
```

**Apply configuration to client:**
```java
@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {
    // Methods
}
```

**Or globally in application.yml:**
```yaml
feign:
  client:
    config:
      default:  # Applied to all clients
        connectTimeout: 5000
        readTimeout: 10000
        loggerLevel: FULL
      product-service:  # Specific client configuration
        connectTimeout: 3000
        readTimeout: 5000
```

### Error Handling

```java
import feign.FeignException;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new BadRequestException();
            case 404:
                return new ProductNotFoundException();
            case 503:
                return new ServiceUnavailableException();
            default:
                return new FeignException.FeignClientException(
                    response.status(),
                    "Error calling service"
                );
        }
    }
}
```

### Resilience with Feign

```java
// Using Resilience4j with Feign
@FeignClient(
    name = "product-service",
    fallback = ProductClientFallback.class
)
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    Product getProduct(@PathVariable("id") Long id);
}

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public Product getProduct(Long id) {
        return Product.builder()
            .id(id)
            .name("Product Unavailable")
            .available(false)
            .build();
    }
}
```

```yaml
# Enable circuit breaker for Feign
feign:
  circuitbreaker:
    enabled: true
```

---

## Asynchronous Communication

For loosely coupled services, asynchronous messaging using message brokers (RabbitMQ, Kafka) is preferred. This decouples services and enables fire-and-forget patterns.

Key technologies:
- **Spring AMQP** for RabbitMQ integration
- **Spring Kafka** for Apache Kafka integration
- **Spring Cloud Stream** for abstracted messaging

Asynchronous communication is covered in detail in messaging-specific modules.

---

## Summary

| Method | Type | Use Case | Pros | Cons |
|--------|------|----------|------|------|
| **RestTemplate** | Sync | Legacy, simple REST calls | Simple, widely known | Blocking, deprecated |
| **WebClient** | Async/Sync | Modern REST calls | Non-blocking, reactive | Learning curve |
| **Feign** | Sync | Declarative clients | Clean, easy to use | Limited async support |
| **Messaging** | Async | Event-driven, decoupling | Loose coupling, scalable | Complex, eventual consistency |

## Best Practices

1. **Use WebClient for New Projects**: Modern, reactive, performant
2. **Feign for Simple Synchronous Calls**: Clean, declarative interfaces
3. **Async for Decoupling**: Use messaging when immediate response not needed
4. **Add Resilience**: Circuit breakers, retries, timeouts
5. **Handle Errors Gracefully**: Proper error handling and fallbacks
6. **Load Balancing**: Use with service discovery
7. **Timeouts**: Always configure connection and read timeouts
8. **Logging**: Log requests and responses for debugging
9. **Monitor**: Track latency, errors, throughput
10. **Version APIs**: Maintain backward compatibility

## Next Topic

Continue to [Security](./06-security.md) to learn about securing microservices with OAuth2, SSO, and preventing common security vulnerabilities.
