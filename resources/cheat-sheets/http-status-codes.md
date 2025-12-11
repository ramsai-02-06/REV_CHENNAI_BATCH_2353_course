# HTTP Status Codes Cheat Sheet

## Overview

HTTP status codes are grouped into five classes:
- **1xx**: Informational
- **2xx**: Success
- **3xx**: Redirection  
- **4xx**: Client Error
- **5xx**: Server Error

## 1xx - Informational

| Code | Status | Description |
|------|--------|-------------|
| 100 | Continue | Request received, client should continue |
| 101 | Switching Protocols | Server is switching protocols |
| 102 | Processing | Server has received and is processing request |

**Common Use:**
- Rarely used in REST APIs
- 100 Continue used for large uploads

## 2xx - Success

| Code | Status | Description | When to Use |
|------|--------|-------------|-------------|
| 200 | OK | Request succeeded | GET, PUT requests |
| 201 | Created | Resource created successfully | POST requests |
| 202 | Accepted | Request accepted, processing pending | Async operations |
| 204 | No Content | Success, but no content to return | DELETE requests |
| 206 | Partial Content | Partial GET request succeeded | Range requests |

### Examples

```java
// Spring Boot examples

// 200 OK
@GetMapping("/products")
public ResponseEntity<List<Product>> getProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
}

// 201 Created
@PostMapping("/products")
public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product created = productService.create(product);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}

// 204 No Content
@DeleteMapping("/products/{id}")
public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
}
```

## 3xx - Redirection

| Code | Status | Description | When to Use |
|------|--------|-------------|-------------|
| 301 | Moved Permanently | Resource moved permanently | Permanent URL changes |
| 302 | Found | Temporary redirect | Temporary redirects |
| 303 | See Other | Response at another URI | After POST, redirect to GET |
| 304 | Not Modified | Resource not modified (caching) | Conditional requests |
| 307 | Temporary Redirect | Same as 302, but method preserved | Temporary redirects |
| 308 | Permanent Redirect | Same as 301, but method preserved | Permanent redirects |

**Common Use:**
- 301: API version deprecation
- 304: Caching with ETag/Last-Modified
- 303: POST-Redirect-GET pattern

## 4xx - Client Errors

| Code | Status | Description | When to Use |
|------|--------|-------------|-------------|
| 400 | Bad Request | Invalid request syntax | Validation errors, malformed JSON |
| 401 | Unauthorized | Authentication required | No/invalid authentication |
| 403 | Forbidden | Authenticated but not authorized | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist | Resource not found |
| 405 | Method Not Allowed | HTTP method not supported | Wrong HTTP method |
| 406 | Not Acceptable | Requested format not available | Accept header mismatch |
| 408 | Request Timeout | Client took too long | Long requests |
| 409 | Conflict | Request conflicts with state | Duplicate resources, version conflicts |
| 410 | Gone | Resource permanently deleted | Deleted resources |
| 411 | Length Required | Content-Length header missing | Missing Content-Length |
| 412 | Precondition Failed | Condition in headers failed | Failed If-Match, If-None-Match |
| 413 | Payload Too Large | Request body too large | File upload limits |
| 415 | Unsupported Media Type | Content-Type not supported | Wrong Content-Type |
| 422 | Unprocessable Entity | Validation error | Semantic validation errors |
| 429 | Too Many Requests | Rate limit exceeded | Rate limiting |

### Common Client Errors

```java
// 400 Bad Request
@PostMapping("/products")
public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
    // Validation annotations trigger 400 automatically
}

// 401 Unauthorized
@GetMapping("/protected")
public ResponseEntity<?> getProtected() {
    if (!isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Authentication required");
    }
    return ResponseEntity.ok(data);
}

// 403 Forbidden
@DeleteMapping("/products/{id}")
public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
    if (!hasPermission()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Insufficient permissions");
    }
    productService.delete(id);
    return ResponseEntity.noContent().build();
}

// 404 Not Found
@GetMapping("/products/{id}")
public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    return productService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}

// 409 Conflict
@PostMapping("/users")
public ResponseEntity<?> createUser(@RequestBody User user) {
    if (userService.existsByEmail(user.getEmail())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("User already exists");
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.create(user));
}

// 422 Unprocessable Entity
@PostMapping("/orders")
public ResponseEntity<?> createOrder(@RequestBody Order order) {
    if (order.getQuantity() > availableStock) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body("Insufficient stock");
    }
    return ResponseEntity.ok(orderService.create(order));
}
```

## 5xx - Server Errors

| Code | Status | Description | When to Use |
|------|--------|-------------|-------------|
| 500 | Internal Server Error | Generic server error | Unexpected errors |
| 501 | Not Implemented | Feature not implemented | Unimplemented endpoints |
| 502 | Bad Gateway | Invalid response from upstream | Proxy/gateway errors |
| 503 | Service Unavailable | Server temporarily unavailable | Maintenance, overload |
| 504 | Gateway Timeout | Upstream server timeout | Slow upstream services |

### Server Error Handling

```java
// Global exception handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An internal error occurred",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    // 503 Service Unavailable
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "Service temporarily unavailable",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}
```

## Quick Decision Guide

### For GET Requests
- ✅ Resource found → **200 OK**
- ❌ Resource not found → **404 Not Found**
- ❌ Not authorized → **401/403**
- ❌ Server error → **500**

### For POST Requests
- ✅ Resource created → **201 Created**
- ❌ Validation error → **400 Bad Request** or **422 Unprocessable Entity**
- ❌ Duplicate resource → **409 Conflict**
- ❌ Not authorized → **401/403**

### For PUT Requests
- ✅ Resource updated → **200 OK**
- ✅ Resource replaced, no content → **204 No Content**
- ❌ Resource not found → **404 Not Found**
- ❌ Validation error → **400 Bad Request**
- ❌ Conflict → **409 Conflict**

### For DELETE Requests
- ✅ Resource deleted → **204 No Content**
- ✅ Resource deleted (return resource) → **200 OK**
- ❌ Resource not found → **404 Not Found**
- ❌ Not authorized → **403 Forbidden**

## Common Patterns

### REST API Status Codes

```
GET /api/products
├─ 200 OK (with product list)
├─ 404 Not Found (collection not found)
└─ 500 Internal Server Error

GET /api/products/123
├─ 200 OK (with product)
├─ 404 Not Found
└─ 401 Unauthorized

POST /api/products
├─ 201 Created (with created product + Location header)
├─ 400 Bad Request (validation error)
├─ 409 Conflict (duplicate)
└─ 422 Unprocessable Entity (business rule violation)

PUT /api/products/123
├─ 200 OK (with updated product)
├─ 204 No Content
├─ 404 Not Found
└─ 409 Conflict (version mismatch)

DELETE /api/products/123
├─ 204 No Content
├─ 200 OK (with deleted product)
└─ 404 Not Found
```

## Best Practices

### 1. Be Consistent
- Use the same status codes for similar situations
- Document your API's status code usage

### 2. Use Appropriate Codes
```java
// ❌ Wrong: Always returning 200
@GetMapping("/products/{id}")
public ResponseEntity<?> getProduct(@PathVariable Long id) {
    Product product = productService.findById(id);
    if (product == null) {
        return ResponseEntity.ok(Map.of("error", "Not found")); // Wrong!
    }
    return ResponseEntity.ok(product);
}

// ✅ Correct: Use proper status codes
@GetMapping("/products/{id}")
public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    return productService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

### 3. Provide Error Details
```java
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private List<String> errors; // For validation errors
}
```

### 4. Use Location Header for 201
```java
@PostMapping("/products")
public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product created = productService.create(product);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(created.getId())
        .toUri();
    return ResponseEntity.created(location).body(created);
}
```

### 5. Handle Validation Properly
```java
// 400 for syntax errors (malformed JSON)
// 422 for semantic errors (invalid data)

@PostMapping("/users")
public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
    // @Valid triggers 400 for basic validation
    
    // Business rule validation → 422
    if (user.getAge() < 18) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body("User must be 18 or older");
    }
    
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
}
```

## Status Code Decision Tree

```
Is the request successful?
├─ Yes → 2xx
│  ├─ Resource created? → 201 Created
│  ├─ Async processing? → 202 Accepted
│  ├─ No content to return? → 204 No Content
│  └─ Otherwise → 200 OK
│
├─ No → Client error or Server error?
   ├─ Client Error → 4xx
   │  ├─ Invalid syntax/format? → 400 Bad Request
   │  ├─ Not authenticated? → 401 Unauthorized
   │  ├─ No permission? → 403 Forbidden
   │  ├─ Resource not found? → 404 Not Found
   │  ├─ Wrong method? → 405 Method Not Allowed
   │  ├─ Conflict? → 409 Conflict
   │  ├─ Validation error? → 422 Unprocessable Entity
   │  └─ Rate limited? → 429 Too Many Requests
   │
   └─ Server Error → 5xx
      ├─ Generic error? → 500 Internal Server Error
      ├─ Not implemented? → 501 Not Implemented
      ├─ Service down? → 503 Service Unavailable
      └─ Timeout? → 504 Gateway Timeout
```

## Quick Reference Card

| Situation | Status Code |
|-----------|-------------|
| Success (GET) | 200 OK |
| Created (POST) | 201 Created |
| Deleted (DELETE) | 204 No Content |
| Validation failed | 400 Bad Request |
| Not logged in | 401 Unauthorized |
| No permission | 403 Forbidden |
| Not found | 404 Not Found |
| Duplicate | 409 Conflict |
| Business rule violated | 422 Unprocessable Entity |
| Server crashed | 500 Internal Server Error |

---

**Remember:**
- 2xx = Success
- 3xx = Redirect
- 4xx = Client's fault
- 5xx = Server's fault
- Always include error details in response body
