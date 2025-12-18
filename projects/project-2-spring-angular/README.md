# Project 2: Full-Stack Web Application with Spring Boot and Angular

## Project Overview
**Individual Learner Project 2**

Building on your first project, this is your second individual project where you'll create a comprehensive full-stack web application. This project demonstrates your ability to build modern enterprise applications using Spring Boot for the backend and Angular for the frontend. You'll create RESTful APIs and a responsive single-page application.

## Timeline
**Start:** Week 7 (after Spring Data JPA)
**Duration:** Weeks 7-10
**Completion:** Before Week 11

## Technologies Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring MVC
- MySQL 8.x
- Hibernate
- RESTful APIs
- Spring Boot Actuator (optional)

### Frontend
- Angular 16
- TypeScript
- RxJS
- Bootstrap 5
- HTML5/CSS3

### Tools
- Maven or Gradle
- Git
- Postman (API testing)
- VS Code or IntelliJ IDEA

## Learning Objectives
- Design and implement RESTful APIs
- Build responsive frontend with Angular
- Implement full CRUD operations
- Handle HTTP requests and responses
- Manage application state
- Implement form validation (frontend and backend)
- Handle errors gracefully
- Apply security best practices
- Deploy and test complete application

## Project Ideas

### Option 1: E-Commerce Platform
**Description:** Online shopping application

**Features:**
- User registration and authentication
- Product catalog with categories
- Shopping cart management
- Order placement and tracking
- User profile management
- Product search and filtering
- Reviews and ratings
- Admin panel for product management

**Entities:**
- User, Product, Category, Order, OrderItem, Review, ShoppingCart

### Option 2: Task Management System
**Description:** Project and task tracking application

**Features:**
- User authentication
- Create and manage projects
- Create, assign, and track tasks
- Task status updates (To Do, In Progress, Done)
- Comments on tasks
- File attachments
- Dashboard with statistics
- User roles (Admin, Manager, Member)

**Entities:**
- User, Project, Task, Comment, Attachment, Role

### Option 3: Social Media Platform
**Description:** Social networking application

**Features:**
- User registration and profiles
- Create and share posts
- Like and comment on posts
- Follow/unfollow users
- News feed
- User search
- Notifications
- Profile customization

**Entities:**
- User, Post, Comment, Like, Follow, Notification

### Option 4: Hospital Management System
**Description:** Healthcare facility management

**Features:**
- Patient registration
- Doctor management
- Appointment scheduling
- Medical records
- Prescription management
- Department management
- Search and reporting
- Admin dashboard

**Entities:**
- Patient, Doctor, Appointment, Department, Prescription, MedicalRecord

### Option 5: Learning Management System
**Description:** Online education platform

**Features:**
- Course catalog
- Student enrollment
- Course content management
- Assignment submission
- Grading system
- Discussion forums
- Progress tracking
- Instructor dashboard

**Entities:**
- User, Course, Lesson, Assignment, Enrollment, Grade, Discussion

## Project Architecture

### Backend Structure
```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourname/project/
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── service/         # Business logic
│   │   │       ├── repository/      # Spring Data JPA repositories
│   │   │       ├── model/           # Entity classes
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── exception/       # Custom exceptions
│   │   │       ├── config/          # Configuration classes
│   │   │       └── Application.java # Main class
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml      # Optional
│   └── test/
│       └── java/                    # Test classes
├── pom.xml                          # Maven dependencies
└── README.md
```

### Frontend Structure
```
frontend/
├── src/
│   ├── app/
│   │   ├── components/              # Feature components
│   │   ├── services/                # API services
│   │   ├── models/                  # TypeScript interfaces
│   │   ├── guards/                  # Route guards
│   │   ├── interceptors/            # HTTP interceptors
│   │   ├── pipes/                   # Custom pipes
│   │   ├── app.component.ts
│   │   ├── app.module.ts
│   │   └── app-routing.module.ts
│   ├── assets/                      # Images, styles
│   ├── environments/                # Environment configs
│   └── index.html
├── angular.json
├── package.json
└── README.md
```

## Implementation Phases

### Phase 1: Planning and Design (Week 7, Days 1-2)
1. Choose project idea and define scope
2. Identify all features and user stories
3. Design database schema and ERD
4. Design REST API endpoints
5. Create wireframes for UI
6. Set up project repositories (backend & frontend)

### Phase 2: Backend Development (Week 7-8, Days 3-7)
1. Set up Spring Boot project
2. Configure database connection
3. Create entity classes with JPA annotations
4. Implement repository layer
5. Implement service layer
6. Create REST controllers
7. Implement exception handling
8. Add validation
9. Test APIs with Postman
10. Document APIs

### Phase 3: Frontend Setup (Week 8-9, Days 8-10)
1. Create Angular project
2. Set up routing
3. Create component structure
4. Design layout and navigation
5. Integrate Bootstrap

### Phase 4: Frontend-Backend Integration (Week 9, Days 11-14)
1. Create Angular services for API calls
2. Implement HTTP client
3. Handle API responses
4. Implement error handling
5. Connect components to services
6. Implement forms with validation
7. Display data in tables/lists
8. Implement CRUD operations in UI

### Phase 5: Advanced Features (Week 9-10, Days 15-17)
1. Implement authentication (optional)
2. Add search and filtering
3. Implement pagination
4. Add sorting functionality
5. Implement file upload (if needed)
6. Add confirmation dialogs
7. Implement notifications/toasts

### Phase 6: Testing and Polish (Week 10, Days 18-19)
1. Test all features end-to-end
2. Fix bugs
3. Improve UI/UX
4. Optimize performance
5. Add loading indicators
6. Responsive design testing
7. Cross-browser testing

### Phase 7: Documentation and Presentation (Week 10, Day 20)
1. Write comprehensive README
2. Document API endpoints
3. Create user guide
4. Prepare presentation
5. Create demo video (optional)

## Required Features

### Minimum Requirements (Must Have)
1. At least 4 related entities with relationships
2. Complete CRUD operations for main entities
3. RESTful API design (GET, POST, PUT, DELETE)
4. Angular components for all main features
5. Responsive design using Bootstrap
6. Form validation (frontend and backend)
7. Error handling and display
8. Navigation between views
9. Professional UI/UX
10. Working pagination for lists

### Additional Features (Should Have)
1. Search functionality
2. Sorting and filtering
3. Confirmation dialogs for delete operations
4. Success/error notifications
5. Loading indicators
6. Data validation with proper error messages
7. Relationship management (one-to-many, many-to-many)

### Advanced Features (Nice to Have)
1. User authentication and authorization
2. Role-based access control
3. File upload/download
4. Export data (CSV, PDF)
5. Dashboard with charts
6. Real-time updates
7. Advanced search with multiple criteria
8. Unit and integration tests
9. CI/CD pipeline setup

## Code Examples

### Spring Boot REST Controller
```java
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productDTO));
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
}
```

### Angular Service
```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) { }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

### Angular Component
```typescript
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  loading = false;
  errorMessage = '';

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load products';
        this.loading = false;
        console.error(error);
      }
    });
  }

  deleteProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.products = this.products.filter(p => p.id !== id);
          alert('Product deleted successfully');
        },
        error: (error) => {
          alert('Failed to delete product');
          console.error(error);
        }
      });
    }
  }
}
```

## Evaluation Criteria

### Backend (30%)
- RESTful API design and implementation
- Proper use of Spring Boot and Spring Data JPA
- Service layer implementation
- Exception handling
- Data validation
- Code organization and structure

### Frontend (30%)
- Component design and implementation
- Proper use of Angular features
- Responsive UI with Bootstrap
- Form handling and validation
- Error handling and user feedback
- Code organization and TypeScript usage

### Integration (20%)
- Proper communication between frontend and backend
- HTTP request/response handling
- CORS configuration
- Data flow between layers

### Functionality (10%)
- All features work as expected
- CRUD operations complete
- No critical bugs
- Performance is acceptable

### Code Quality & Documentation (10%)
- Clean, readable code
- Proper naming conventions
- Comments where necessary
- README with setup instructions
- API documentation

## Submission Guidelines

### Deliverables
1. Backend source code (Spring Boot application)
2. Frontend source code (Angular application)
3. Database schema SQL file with sample data
4. README files for both projects
5. API documentation (Postman collection or Swagger)
6. Presentation (10-15 minutes)

### Submission Checklist
- [ ] Both applications run without errors
- [ ] All CRUD operations work end-to-end
- [ ] Responsive design implemented
- [ ] Form validation on both frontend and backend
- [ ] Error handling implemented
- [ ] Database scripts provided
- [ ] README files with setup instructions
- [ ] API documentation provided
- [ ] Code properly commented
- [ ] Presentation prepared

## Setup Instructions Template

Include this in your README:

```markdown
## Prerequisites
- Java 21
- Node.js 18+
- MySQL 8.x
- Maven

## Backend Setup
1. Clone the repository
2. Navigate to backend folder
3. Update application.properties with your MySQL credentials
4. Run: `mvn clean install`
5. Run: `mvn spring-boot:run`
6. Backend will start on http://localhost:8080

## Frontend Setup
1. Navigate to frontend folder
2. Run: `npm install`
3. Run: `ng serve`
4. Frontend will start on http://localhost:4200

## Database Setup
1. Create database: `CREATE DATABASE your_db_name;`
2. Run the provided schema.sql file
3. Run the data.sql file for sample data (optional)
```

## Tips for Success

### Backend
1. Design your REST API before implementation
2. Use DTOs to avoid exposing entities directly
3. Implement global exception handling
4. Use proper HTTP status codes
5. Test APIs with Postman before frontend integration
6. Use Spring Data JPA query methods effectively
7. Implement proper validation using Bean Validation

### Frontend
1. Plan your component hierarchy
2. Use Angular CLI to generate components and services
3. Keep components focused and small
4. Use reactive forms for complex forms
5. Implement error handling in services
6. Use async pipe for observables in templates
7. Keep business logic in services, not components

### General
1. Commit code regularly to Git
2. Write clear commit messages
3. Test features as you build them
4. Start with core features, then add extras
5. Keep UI simple and user-friendly
6. Handle loading states
7. Provide user feedback for all actions
8. Test on different screen sizes

## Common Issues and Solutions

### CORS Errors
- Add `@CrossOrigin` annotation to controllers
- Or configure global CORS in Spring Boot configuration

### 404 Errors
- Verify API endpoint URLs match between frontend and backend
- Check Spring Boot context path configuration

### Data Not Displaying
- Check browser console for errors
- Verify API is returning data (test with Postman)
- Check data binding in Angular templates

### Form Not Submitting
- Check form validation
- Verify API endpoint and request body format
- Check for JavaScript errors in console

## Resources

### Backend Resources
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Building REST APIs with Spring](https://spring.io/guides/tutorials/rest/)

### Frontend Resources
- [Angular Documentation](https://angular.io/docs)
- [Angular HTTP Client](https://angular.io/guide/http)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [RxJS Documentation](https://rxjs.dev/)

### Related Modules
- [Module 14: Spring Framework](../../14-spring-framework/)
- [Module 15: Spring Boot](../../15-spring-boot/)
- [Module 17: Spring Data JPA](../../17-spring-data-jpa/)
- [Module 18: REST APIs](../../18-rest-api/)
- [Module 21: Angular](../../21-angular/)

---

**This second individual project brings together everything you've learned so far. Take your time, plan well, and build something you're proud of!**
