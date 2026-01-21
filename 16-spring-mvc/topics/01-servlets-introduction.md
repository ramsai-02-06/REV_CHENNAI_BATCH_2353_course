# Servlets Introduction

## What is a Servlet?

A Servlet is a Java class that handles HTTP requests and generates responses. Servlets are the foundation of Java web development and run inside a Servlet container (like Tomcat).

```
┌─────────────────────────────────────────────────────────┐
│                    Servlet Container                     │
│                       (Tomcat)                           │
│  ┌─────────┐    ┌─────────┐    ┌─────────┐             │
│  │ Servlet │    │ Servlet │    │ Servlet │             │
│  │  /users │    │ /orders │    │ /products│             │
│  └─────────┘    └─────────┘    └─────────┘             │
└─────────────────────────────────────────────────────────┘
         ▲                ▲                ▲
         │                │                │
    HTTP Request     HTTP Request     HTTP Request
```

**Key Points:**
- Servlets run on the server (not in browser)
- Each servlet handles specific URL patterns
- Container manages servlet lifecycle
- Spring MVC is built on top of the Servlet API

---

## Servlet Lifecycle

```
┌──────────────┐
│   Loading    │  Container loads servlet class
└──────┬───────┘
       ▼
┌──────────────┐
│    init()    │  Called once when servlet is created
└──────┬───────┘
       ▼
┌──────────────┐
│  service()   │  Called for each request (doGet, doPost)
└──────┬───────┘
       │ (repeats for each request)
       ▼
┌──────────────┐
│  destroy()   │  Called once when container shuts down
└──────────────┘
```

| Method | When Called | Purpose |
|--------|-------------|---------|
| `init()` | Once at startup | Initialize resources |
| `service()` | Every request | Routes to doGet/doPost |
| `destroy()` | Once at shutdown | Cleanup resources |

---

## Basic Servlet Example

### Simple Hello Servlet

```java
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        // Read query parameter: /hello?name=John
        String name = request.getParameter("name");
        if (name == null) {
            name = "World";
        }

        // Set response type
        response.setContentType("text/plain");

        // Write response
        response.getWriter().write("Hello, " + name + "!");
    }
}
```

**Request:** `GET /hello?name=John`
**Response:** `Hello, John!`

---

## Handling Different HTTP Methods

```java
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    // GET /users - List all users
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.getWriter().write("[{\"id\": 1, \"name\": \"John\"}]");
    }

    // POST /users - Create user
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        // Read JSON body manually
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            body.append(line);
        }

        // Would need to parse JSON manually...
        String json = body.toString();

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write("{\"id\": 2, \"name\": \"New User\"}");
    }

    // DELETE /users?id=1
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {

        String id = request.getParameter("id");
        // Delete logic...

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
```

---

## Reading Path Parameters (Manual)

Servlets don't have built-in path variable support. You must parse URLs manually:

```java
@WebServlet("/users/*")
public class UserDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        // URL: /users/123
        // PathInfo: /123
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Extract ID manually
        String id = pathInfo.substring(1);  // Remove leading /

        response.setContentType("application/json");
        response.getWriter().write("{\"id\": " + id + ", \"name\": \"User " + id + "\"}");
    }
}
```

---

## Pain Points with Raw Servlets

| Problem | Servlet Approach |
|---------|------------------|
| **URL Routing** | One servlet per URL or manual parsing |
| **Path Variables** | Manual string parsing |
| **JSON Handling** | Manual serialization/deserialization |
| **Validation** | Manual if/else checks |
| **Error Handling** | Try-catch in every method |
| **Dependency Injection** | None - create objects manually |

### Example: The Boilerplate Problem

```java
@WebServlet("/api/products/*")
public class ProductServlet extends HttpServlet {

    // No dependency injection - must create manually
    private ProductService productService = new ProductServiceImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {

        try {
            String pathInfo = request.getPathInfo();

            response.setContentType("application/json");

            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/products - list all
                List<Product> products = productService.findAll();
                String json = objectMapper.writeValueAsString(products);
                response.getWriter().write(json);
            } else {
                // GET /api/products/123 - get by id
                String id = pathInfo.substring(1);
                Product product = productService.findById(Long.parseLong(id));

                if (product == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Not found\"}");
                    return;
                }

                String json = objectMapper.writeValueAsString(product);
                response.getWriter().write(json);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid ID\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Server error\"}");
        }
    }
}
```

**Problems in this code:**
- Manual URL parsing
- Manual JSON conversion
- Manual error handling
- No dependency injection
- Lots of boilerplate

---

## How Spring MVC Solves This

The same functionality in Spring MVC:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;  // Injected automatically

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();  // Auto-converted to JSON
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

### Comparison

| Aspect | Raw Servlet | Spring MVC |
|--------|-------------|------------|
| **Lines of code** | ~50 | ~15 |
| **URL routing** | Manual | `@GetMapping("/{id}")` |
| **Path variables** | Parse string | `@PathVariable` |
| **JSON** | ObjectMapper | Automatic |
| **Error handling** | Try-catch | `@ExceptionHandler` |
| **DI** | Manual `new` | Constructor injection |

---

## Setup (For Reference)

### Maven Dependencies

```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

### web.xml Configuration (Traditional)

```xml
<web-app>
    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>com.example.HelloServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>
</web-app>
```

Modern approach uses `@WebServlet` annotation instead.

---

## Summary

| Concept | Description |
|---------|-------------|
| **Servlet** | Java class handling HTTP requests |
| **HttpServlet** | Base class with doGet, doPost methods |
| **Container** | Tomcat, Jetty - runs servlets |
| **Lifecycle** | init → service → destroy |
| **Pain Points** | Manual routing, parsing, JSON, error handling |

### Key Takeaway

Servlets provide the low-level foundation. Spring MVC builds on servlets to eliminate boilerplate and provide:
- Declarative routing (`@GetMapping`)
- Automatic JSON conversion
- Dependency injection
- Centralized error handling

## Next Topic

Continue to [Spring MVC Architecture](./02-spring-mvc-architecture.md) to see how Spring MVC simplifies web development.
