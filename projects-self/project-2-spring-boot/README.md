# Project 2: RESTful API with Spring Boot

## Project Overview
**Individual Learner Project 2**

Building on your first project, this is your second individual project where you'll create a comprehensive RESTful API backend using Spring Boot. This project demonstrates your ability to build enterprise-grade backend applications with proper layered architecture, data persistence, and API design.

## Timeline
**Start:** Week 7 (after Spring Data JPA)
**Duration:** Weeks 7-8
**Completion:** Week 8 (REST API Interim Milestone)

## Technology Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring MVC
- MySQL 8.x
- Hibernate ORM
- Maven or Gradle
- Postman (API testing)

## Learning Objectives

- Design and implement RESTful APIs
- Apply layered architecture (Controller, Service, Repository)
- Implement full CRUD operations
- Handle HTTP requests and responses with proper status codes
- Implement request validation using Bean Validation
- Handle exceptions globally
- Use DTOs to decouple API from entities
- Document APIs effectively

## Project Ideas

### Option 1: E-Commerce Product API
**Description:** Backend API for an online shopping platform

**Features:**
- Product management (CRUD)
- Category management
- Product search and filtering
- Pagination and sorting
- Inventory tracking

**Entities:**
- Product, Category, Inventory

### Option 2: Task Management API
**Description:** Backend API for project and task tracking

**Features:**
- Project management (CRUD)
- Task management with status tracking
- Task assignment
- Comments on tasks
- Task filtering by status/project

**Entities:**
- Project, Task, Comment

### Option 3: Blog API
**Description:** Backend API for a blogging platform

**Features:**
- Post management (CRUD)
- Category/tag management
- Comments system
- Post search
- Author management

**Entities:**
- Post, Category, Tag, Comment, Author

### Option 4: Hospital Management API
**Description:** Backend API for healthcare facility management

**Features:**
- Patient registration
- Doctor management
- Appointment scheduling
- Department management
- Search and filtering

**Entities:**
- Patient, Doctor, Appointment, Department

### Option 5: Library Management API
**Description:** Backend API for library operations

**Features:**
- Book catalog management
- Member management
- Book borrowing and returns
- Fine calculation
- Search functionality

**Entities:**
- Book, Member, BorrowRecord, Category

## Project Architecture

```
project-name/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourname/project/
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── service/         # Business logic
│   │   │       ├── repository/      # Spring Data JPA repositories
│   │   │       ├── model/           # Entity classes
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── exception/       # Custom exceptions & handlers
│   │   │       ├── config/          # Configuration classes
│   │   │       └── Application.java # Main class
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql             # Sample data (optional)
│   └── test/
│       └── java/                    # Test classes
├── pom.xml                          # Maven dependencies
└── README.md
```

## Implementation Phases

### Phase 1: Planning and Design (Days 1-2)
1. Choose project idea and define scope
2. Identify all features and user stories
3. Design database schema and ERD
4. Design REST API endpoints (resource naming, HTTP methods)
5. Set up project repository

### Phase 2: Project Setup (Day 3)
1. Create Spring Boot project using Spring Initializr
2. Configure database connection in application.properties
3. Add required dependencies (Spring Web, Spring Data JPA, MySQL Driver, Validation)
4. Verify application starts successfully

### Phase 3: Entity Layer (Days 4-5)
1. Create entity classes with JPA annotations
2. Define relationships (@OneToMany, @ManyToOne, @ManyToMany)
3. Add validation constraints (@NotNull, @Size, etc.)
4. Create database tables (auto-generation or manual scripts)

### Phase 4: Repository Layer (Day 6)
1. Create repository interfaces extending JpaRepository
2. Add custom query methods using method naming convention
3. Add @Query for complex queries if needed

### Phase 5: Service Layer (Days 7-8)
1. Create service interfaces
2. Implement service classes with business logic
3. Handle entity-DTO conversions
4. Implement validation logic

### Phase 6: Controller Layer (Days 9-10)
1. Create REST controllers
2. Implement CRUD endpoints
3. Add request validation (@Valid)
4. Return proper HTTP status codes
5. Implement search/filter endpoints

### Phase 7: Exception Handling (Day 11)
1. Create custom exception classes
2. Implement @ControllerAdvice for global exception handling
3. Return consistent error responses

### Phase 8: Testing and Documentation (Days 12-14)
1. Test all APIs with Postman
2. Create Postman collection
3. Write comprehensive README
4. Add API documentation

## Required Features

### Minimum Requirements (Must Have)
1. At least 3 related entities with relationships
2. Complete CRUD operations for all main entities
3. RESTful API design (proper resource naming, HTTP methods)
4. Proper HTTP status codes (200, 201, 204, 400, 404, 500)
5. Request validation with error messages
6. Global exception handling
7. DTO pattern for API responses
8. Pagination for list endpoints
9. At least one search/filter endpoint

### Additional Features (Should Have)
1. Sorting functionality
2. Multiple search criteria
3. Custom validation
4. Proper logging
5. Database constraints (unique, foreign keys)

### Advanced Features (Nice to Have)
1. Spring Boot Actuator for monitoring
2. API documentation with Swagger/OpenAPI
3. Unit tests for service layer
4. Integration tests for controllers
5. Database versioning with Flyway/Liquibase

## Code Examples

### Entity Class
```java
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Constructors, getters, setters
}
```

### Repository Interface
```java
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findByPriceRange(@Param("min") BigDecimal min,
                                   @Param("max") BigDecimal max);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}
```

### Service Class
```java
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(this::mapToDTO);
    }

    // Other methods and mapping logic
}
```

### REST Controller
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO created = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }
}
```

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors,
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

## Evaluation Criteria

### API Design (25%)
- RESTful resource naming
- Proper HTTP methods usage
- Correct status codes
- Consistent response format

### Code Quality (25%)
- Layered architecture
- Separation of concerns
- Clean, readable code
- Proper naming conventions

### Data Persistence (20%)
- Entity relationships
- JPA annotations
- Repository methods
- Database design

### Error Handling (15%)
- Global exception handling
- Meaningful error messages
- Validation implementation

### Documentation (15%)
- README with setup instructions
- API documentation
- Postman collection

## Submission Guidelines

### Deliverables
1. Spring Boot application source code
2. Database schema SQL file
3. Sample data SQL file
4. Postman collection with all endpoints
5. README with setup instructions
6. Presentation (10 minutes)

### Submission Checklist
- [ ] Application runs without errors
- [ ] All CRUD operations work
- [ ] Pagination implemented
- [ ] Search/filter endpoint works
- [ ] Validation working with error messages
- [ ] Exception handling implemented
- [ ] Database scripts provided
- [ ] Postman collection included
- [ ] README complete
- [ ] Code properly organized

## Setup Instructions Template

Include this in your README:

```markdown
## Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8.x

## Database Setup
1. Create database: `CREATE DATABASE your_db_name;`
2. Run schema.sql to create tables
3. Run data.sql for sample data (optional)

## Application Setup
1. Clone the repository
2. Update application.properties:
   - spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
   - spring.datasource.username=your_username
   - spring.datasource.password=your_password
3. Run: `mvn clean install`
4. Run: `mvn spring-boot:run`
5. API will be available at http://localhost:8080

## Testing with Postman
1. Import the provided Postman collection
2. Test endpoints in order: Create -> Read -> Update -> Delete
```

## Resources

### Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Building REST Services with Spring](https://spring.io/guides/tutorials/rest/)

### Related Modules
- [Module 14: Spring Framework](../../14-spring-framework/)
- [Module 15: Spring Boot](../../15-spring-boot/)
- [Module 16: Spring MVC](../../16-spring-mvc/)
- [Module 17: Spring Data JPA](../../17-spring-data-jpa/)
- [Module 18: REST API](../../18-rest-api/)

---

**This project is your first step into enterprise backend development. Focus on clean architecture and proper API design!**
