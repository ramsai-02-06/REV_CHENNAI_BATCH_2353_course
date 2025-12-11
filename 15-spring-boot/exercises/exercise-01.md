# Exercise: Build a RESTful API with Spring Boot

## Objective
Create a complete RESTful CRUD API for a Product Management System using Spring Boot, demonstrating auto-configuration, starters, and best practices.

## Requirements

### API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/products | Get all products (with pagination) |
| GET | /api/products/{id} | Get product by ID |
| POST | /api/products | Create new product |
| PUT | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product |
| GET | /api/products/search?name={} | Search by name |
| GET | /api/products/category/{category} | Filter by category |

### Product Entity
```java
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Spring Boot Features to Demonstrate

1. **Project Setup**
   - Use Spring Initializr
   - Dependencies: Web, JPA, H2/MySQL, Validation, DevTools

2. **Configuration**
   - application.properties / application.yml
   - Profile-specific configuration (dev, prod)
   - Custom configuration properties with @ConfigurationProperties

3. **Layers**
   - Controller with @RestController
   - Service layer with @Service
   - Repository with Spring Data JPA

4. **Validation**
   - Bean Validation (@NotNull, @Size, @Min)
   - Custom validators
   - Exception handling with @ControllerAdvice

5. **Response Handling**
   - ResponseEntity usage
   - Custom error responses
   - HTTP status codes

### Project Structure
```
src/main/java/com/example/productapi/
├── ProductApiApplication.java
├── controller/
│   └── ProductController.java
├── service/
│   ├── ProductService.java
│   └── ProductServiceImpl.java
├── repository/
│   └── ProductRepository.java
├── model/
│   ├── Product.java
│   └── dto/
│       ├── ProductRequest.java
│       └── ProductResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
└── config/
    └── AppConfig.java

src/main/resources/
├── application.yml
├── application-dev.yml
└── application-prod.yml
```

## Expected Responses

### Success Response
```json
{
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "Electronics",
    "stockQuantity": 50
}
```

### Error Response
```json
{
    "timestamp": "2024-12-11T10:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Product not found with id: 99",
    "path": "/api/products/99"
}
```

## Skills Tested
- Spring Boot auto-configuration
- REST API design
- Spring Data JPA
- Bean validation
- Exception handling
- Configuration management
