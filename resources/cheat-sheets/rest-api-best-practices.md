# REST API Best Practices Cheat Sheet

## 1. Resource Naming

### Use Nouns, Not Verbs
```
✅ Good:
GET    /products
POST   /products
GET    /products/123
PUT    /products/123
DELETE /products/123

❌ Bad:
GET    /getProducts
POST   /createProduct
GET    /getProduct/123
POST   /updateProduct/123
DELETE /deleteProduct/123
```

### Use Plural Nouns
```
✅ Good:
/products
/users
/orders

❌ Bad:
/product
/user
/order
```

### Use Hierarchical Structure
```
✅ Good:
/users/123/orders
/users/123/orders/456
/products/789/reviews

❌ Bad:
/getUserOrders/123
/orders?userId=123
```

### Use Lowercase and Hyphens
```
✅ Good:
/product-categories
/user-profiles
/order-items

❌ Bad:
/productCategories
/Product_Categories
/PRODUCT-CATEGORIES
```

## 2. HTTP Methods

### CRUD Operations

| Operation | HTTP Method | URL | Request Body | Success Status |
|-----------|-------------|-----|--------------|----------------|
| Create | POST | /products | Product data | 201 Created |
| Read (all) | GET | /products | None | 200 OK |
| Read (one) | GET | /products/123 | None | 200 OK |
| Update (full) | PUT | /products/123 | Full product | 200 OK / 204 No Content |
| Update (partial) | PATCH | /products/123 | Changed fields | 200 OK |
| Delete | DELETE | /products/123 | None | 204 No Content |

### Examples

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // Create
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product created = service.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // Read all
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Update (full replacement)
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(service.update(id, product));
    }
    
    // Update (partial)
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patch(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(service.patch(id, updates));
    }
    
    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

## 3. Status Codes

### Use Appropriate HTTP Status Codes

```java
// Success
200 OK           // GET, PUT
201 Created      // POST
204 No Content   // DELETE

// Client Errors
400 Bad Request              // Invalid input
401 Unauthorized             // Not authenticated
403 Forbidden                // Not authorized
404 Not Found                // Resource not found
409 Conflict                 // Duplicate/conflict
422 Unprocessable Entity     // Validation error

// Server Errors
500 Internal Server Error    // Server error
503 Service Unavailable      // Service down
```

## 4. Filtering, Sorting, Pagination

### Query Parameters

```
# Filtering
GET /products?category=electronics
GET /products?minPrice=100&maxPrice=500
GET /products?inStock=true

# Sorting
GET /products?sort=price
GET /products?sort=price,desc
GET /products?sort=name,asc&sort=price,desc

# Pagination
GET /products?page=0&size=20
GET /products?limit=20&offset=40

# Search
GET /products?search=laptop
GET /products?q=laptop

# Field selection
GET /products?fields=id,name,price
```

### Spring Boot Implementation

```java
@GetMapping
public ResponseEntity<Page<Product>> getProducts(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {
    
    Sort sort = sortDir.equalsIgnoreCase("desc") 
        ? Sort.by(sortBy).descending() 
        : Sort.by(sortBy).ascending();
    
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Product> products = service.findAll(category, minPrice, maxPrice, pageable);
    
    return ResponseEntity.ok(products);
}
```

### Pagination Response Format

```json
{
  "content": [
    { "id": 1, "name": "Product 1" },
    { "id": 2, "name": "Product 2" }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

## 5. Versioning

### URI Versioning (Most Common)
```
GET /api/v1/products
GET /api/v2/products
```

```java
@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller { }

@RestController
@RequestMapping("/api/v2/products")
public class ProductV2Controller { }
```

### Header Versioning
```
GET /api/products
Accept: application/vnd.myapi.v1+json
```

### Query Parameter Versioning
```
GET /api/products?version=1
```

## 6. Error Handling

### Consistent Error Response Format

```java
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<String> errors;
}
```

### Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            404,
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now(),
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
        
        ErrorResponse error = new ErrorResponse(
            400,
            "Validation failed",
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now(),
            errors
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

### Error Response Examples

```json
{
  "status": 404,
  "message": "Product not found with id: 123",
  "path": "/api/products/123",
  "timestamp": "2024-01-15T10:30:00",
  "errors": null
}

{
  "status": 400,
  "message": "Validation failed",
  "path": "/api/products",
  "timestamp": "2024-01-15T10:30:00",
  "errors": [
    "name: must not be blank",
    "price: must be greater than 0"
  ]
}
```

## 7. Security

### Use HTTPS
```
✅ https://api.example.com/products
❌ http://api.example.com/products
```

### Authentication
```java
// JWT Token in header
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...

@GetMapping("/protected")
public ResponseEntity<?> getProtected(@RequestHeader("Authorization") String token) {
    // Validate token
    if (!jwtService.validateToken(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(data);
}
```

### Input Validation
```java
@Entity
public class Product {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100)
    private String name;
    
    @NotNull
    @Positive
    private Double price;
    
    @Email
    private String contactEmail;
}

@PostMapping
public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.create(product));
}
```

### Rate Limiting
```java
@GetMapping
@RateLimit(requests = 100, duration = 3600) // 100 requests per hour
public ResponseEntity<List<Product>> getProducts() {
    return ResponseEntity.ok(service.findAll());
}
```

## 8. CORS

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
```

## 9. Documentation

### Use Swagger/OpenAPI

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.0</version>
</dependency>
```

```java
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {
    
    @Operation(summary = "Get all products", description = "Returns list of all products")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
```

Access at: `http://localhost:8080/swagger-ui.html`

## 10. Response Format

### Consistent JSON Structure

```json
// Single resource
{
  "id": 1,
  "name": "Product Name",
  "price": 99.99,
  "createdAt": "2024-01-15T10:30:00Z"
}

// Collection
{
  "data": [
    { "id": 1, "name": "Product 1" },
    { "id": 2, "name": "Product 2" }
  ],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 100,
    "totalPages": 5
  }
}

// Error
{
  "status": 400,
  "message": "Validation failed",
  "errors": ["Field error 1", "Field error 2"],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Use DTOs

```java
// Don't expose entities directly
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
    Product product = service.findById(id);
    ProductDTO dto = mapper.toDTO(product);
    return ResponseEntity.ok(dto);
}
```

## 11. Idempotency

### Idempotent Methods

| Method | Idempotent | Safe |
|--------|-----------|------|
| GET | Yes | Yes |
| PUT | Yes | No |
| DELETE | Yes | No |
| POST | No | No |
| PATCH | No | No |

### Implementing Idempotency for POST

```java
@PostMapping
public ResponseEntity<Product> create(
        @RequestHeader("Idempotency-Key") String idempotencyKey,
        @RequestBody Product product) {
    
    // Check if request with this key was already processed
    Optional<Product> existing = service.findByIdempotencyKey(idempotencyKey);
    if (existing.isPresent()) {
        return ResponseEntity.ok(existing.get());
    }
    
    Product created = service.create(product, idempotencyKey);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

## 12. Caching

### HTTP Caching Headers

```java
@GetMapping("/{id}")
public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Product product = service.findById(id);
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(3600, TimeUnit.SECONDS))
        .eTag(String.valueOf(product.getVersion()))
        .body(product);
}
```

### Conditional Requests

```java
@GetMapping("/{id}")
public ResponseEntity<Product> getProduct(
        @PathVariable Long id,
        @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
    
    Product product = service.findById(id);
    String etag = String.valueOf(product.getVersion());
    
    if (etag.equals(ifNoneMatch)) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    
    return ResponseEntity.ok()
        .eTag(etag)
        .body(product);
}
```

## Quick Checklist

### API Design
- [ ] Use nouns for resources
- [ ] Use plural nouns
- [ ] Use hierarchical URLs
- [ ] Use kebab-case for URLs
- [ ] Use correct HTTP methods
- [ ] Use appropriate status codes

### Security
- [ ] Use HTTPS
- [ ] Implement authentication
- [ ] Validate all inputs
- [ ] Implement rate limiting
- [ ] Configure CORS properly

### Best Practices
- [ ] Version your API
- [ ] Implement pagination
- [ ] Support filtering and sorting
- [ ] Use DTOs (don't expose entities)
- [ ] Provide clear error messages
- [ ] Document with Swagger/OpenAPI
- [ ] Implement caching
- [ ] Handle errors globally

### Performance
- [ ] Use pagination for large datasets
- [ ] Implement caching
- [ ] Use database indexes
- [ ] Optimize queries (avoid N+1)
- [ ] Use async processing for long tasks

---

**Remember:**
- Be consistent across your API
- Think from the API consumer's perspective
- Document everything
- Test thoroughly
- Monitor and log
