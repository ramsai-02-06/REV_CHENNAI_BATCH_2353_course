# Project 3: Single Page Application with Angular

## Project Overview
**Individual Learner Project 3**

Building on your Spring Boot API from Project 2, this project focuses on creating a responsive single-page application (SPA) using Angular. You'll connect your frontend to the REST API you built previously, implementing a complete user interface with data binding, routing, and state management.

## Timeline
**Start:** Week 9 (after Angular fundamentals)
**Duration:** Weeks 9-10
**Completion:** End of Week 10

## Technology Stack

- Angular 16+
- TypeScript
- RxJS
- Bootstrap 5
- HTML5/CSS3
- Node.js 18+ (runtime)
- VS Code (recommended IDE)

## Learning Objectives

- Build responsive single-page applications
- Implement component-based architecture
- Handle HTTP requests with HttpClient
- Work with Observables and RxJS
- Implement client-side routing
- Create reactive forms with validation
- Manage application state
- Apply responsive design principles

## Project Requirement

**Important:** This project must connect to the Spring Boot API you created in Project 2. You will build the frontend interface for the same domain (e-commerce, task management, blog, etc.).

## Project Architecture

```
project-name/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── shared/              # Shared components (header, footer, etc.)
│   │   │   ├── product/             # Feature components
│   │   │   │   ├── product-list/
│   │   │   │   ├── product-detail/
│   │   │   │   ├── product-form/
│   │   │   │   └── product.module.ts
│   │   │   └── ...
│   │   ├── services/                # API services
│   │   ├── models/                  # TypeScript interfaces
│   │   ├── guards/                  # Route guards
│   │   ├── interceptors/            # HTTP interceptors
│   │   ├── pipes/                   # Custom pipes
│   │   ├── app.component.ts
│   │   ├── app.module.ts
│   │   └── app-routing.module.ts
│   ├── assets/                      # Images, icons
│   ├── environments/                # Environment configs
│   ├── styles.css                   # Global styles
│   └── index.html
├── angular.json
├── package.json
├── tsconfig.json
└── README.md
```

## Implementation Phases

### Phase 1: Project Setup (Days 1-2)
1. Create Angular project using Angular CLI
2. Set up project structure (components, services, models folders)
3. Install Bootstrap and configure in angular.json
4. Create environment configuration for API URL
5. Set up basic routing structure
6. Create shared layout components (header, footer, navbar)

### Phase 2: Models and Services (Days 3-4)
1. Create TypeScript interfaces matching API DTOs
2. Create services for each resource (ProductService, CategoryService, etc.)
3. Implement HttpClient for API communication
4. Add error handling in services
5. Create HTTP interceptor for common headers/error handling

### Phase 3: List Components (Days 5-6)
1. Create list components for main entities
2. Implement data fetching using services
3. Display data in tables with Bootstrap styling
4. Add loading indicators
5. Implement error display
6. Add pagination controls

### Phase 4: Detail and Form Components (Days 7-8)
1. Create detail view components
2. Create form components for create/edit operations
3. Implement reactive forms with validation
4. Add form error messages
5. Handle form submission
6. Implement navigation between views

### Phase 5: Advanced Features (Days 9-10)
1. Implement search functionality
2. Add sorting controls
3. Implement filtering
4. Add confirmation dialogs for delete
5. Implement toast notifications
6. Add loading states throughout

### Phase 6: Polish and Testing (Days 11-12)
1. Responsive design testing
2. Cross-browser testing
3. Fix UI/UX issues
4. Optimize performance
5. Add finishing touches

### Phase 7: Documentation (Days 13-14)
1. Write comprehensive README
2. Document component structure
3. Create user guide
4. Prepare presentation

## Required Features

### Minimum Requirements (Must Have)
1. List view with data from API
2. Detail view for individual items
3. Create form with validation
4. Edit form with pre-populated data
5. Delete functionality with confirmation
6. Navigation between views
7. Responsive design (mobile, tablet, desktop)
8. Loading indicators
9. Error handling and display
10. Professional UI with Bootstrap

### Additional Features (Should Have)
1. Search functionality
2. Pagination controls
3. Sorting by columns
4. Filter by category/status
5. Toast notifications for actions
6. Form validation messages
7. Empty state displays

### Advanced Features (Nice to Have)
1. Route guards for protected routes
2. HTTP interceptor for error handling
3. Lazy loading for feature modules
4. Custom pipes for data formatting
5. Animations and transitions
6. Dark mode toggle
7. Export functionality

## Code Examples

### Model Interface
```typescript
export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  categoryId: number;
  categoryName?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
```

### Service
```typescript
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Product, PageResponse } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) { }

  getProducts(page: number = 0, size: number = 10): Observable<PageResponse<Product>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Product>>(this.apiUrl, { params });
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

  searchProducts(name: string): Observable<Product[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<Product[]>(`${this.apiUrl}/search`, { params });
  }
}
```

### List Component
```typescript
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product, PageResponse } from '../../models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  loading = false;
  error = '';

  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.error = '';

    this.productService.getProducts(this.currentPage, this.pageSize).subscribe({
      next: (response: PageResponse<Product>) => {
        this.products = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load products. Please try again.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadProducts();
  }

  deleteProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.products = this.products.filter(p => p.id !== id);
          // Show success notification
        },
        error: (err) => {
          this.error = 'Failed to delete product.';
          console.error(err);
        }
      });
    }
  }
}
```

### List Component Template
```html
<div class="container mt-4">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Products</h2>
    <a routerLink="/products/new" class="btn btn-primary">
      Add Product
    </a>
  </div>

  <!-- Loading -->
  <div *ngIf="loading" class="text-center py-5">
    <div class="spinner-border" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>

  <!-- Error -->
  <div *ngIf="error" class="alert alert-danger">
    {{ error }}
    <button class="btn btn-sm btn-outline-danger ms-2" (click)="loadProducts()">
      Retry
    </button>
  </div>

  <!-- Empty State -->
  <div *ngIf="!loading && !error && products.length === 0" class="text-center py-5">
    <p class="text-muted">No products found.</p>
    <a routerLink="/products/new" class="btn btn-primary">Add your first product</a>
  </div>

  <!-- Product Table -->
  <div *ngIf="!loading && products.length > 0" class="table-responsive">
    <table class="table table-striped table-hover">
      <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Category</th>
          <th>Price</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let product of products">
          <td>{{ product.id }}</td>
          <td>
            <a [routerLink]="['/products', product.id]">{{ product.name }}</a>
          </td>
          <td>{{ product.categoryName }}</td>
          <td>{{ product.price | currency }}</td>
          <td>
            <a [routerLink]="['/products', product.id, 'edit']" class="btn btn-sm btn-outline-primary me-1">
              Edit
            </a>
            <button class="btn btn-sm btn-outline-danger" (click)="deleteProduct(product.id)">
              Delete
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Pagination -->
    <nav *ngIf="totalPages > 1">
      <ul class="pagination justify-content-center">
        <li class="page-item" [class.disabled]="currentPage === 0">
          <a class="page-link" (click)="onPageChange(currentPage - 1)">Previous</a>
        </li>
        <li class="page-item" *ngFor="let page of [].constructor(totalPages); let i = index"
            [class.active]="currentPage === i">
          <a class="page-link" (click)="onPageChange(i)">{{ i + 1 }}</a>
        </li>
        <li class="page-item" [class.disabled]="currentPage === totalPages - 1">
          <a class="page-link" (click)="onPageChange(currentPage + 1)">Next</a>
        </li>
      </ul>
    </nav>
  </div>
</div>
```

### Form Component with Reactive Forms
```typescript
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html'
})
export class ProductFormComponent implements OnInit {
  productForm: FormGroup;
  isEditMode = false;
  productId: number | null = null;
  loading = false;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.maxLength(500)]],
      price: ['', [Validators.required, Validators.min(0.01)]],
      categoryId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.productId = +id;
      this.loadProduct();
    }
  }

  loadProduct(): void {
    if (!this.productId) return;

    this.loading = true;
    this.productService.getProductById(this.productId).subscribe({
      next: (product) => {
        this.productForm.patchValue(product);
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    const product = this.productForm.value;

    const request = this.isEditMode
      ? this.productService.updateProduct(this.productId!, product)
      : this.productService.createProduct(product);

    request.subscribe({
      next: () => {
        this.router.navigate(['/products']);
      },
      error: (err) => {
        console.error(err);
        this.submitting = false;
      }
    });
  }

  get f() {
    return this.productForm.controls;
  }
}
```

### Routing Module
```typescript
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { ProductDetailComponent } from './components/product/product-detail/product-detail.component';
import { ProductFormComponent } from './components/product/product-form/product-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductListComponent },
  { path: 'products/new', component: ProductFormComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: 'products/:id/edit', component: ProductFormComponent },
  { path: '**', redirectTo: '/products' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

## Evaluation Criteria

### Component Design (25%)
- Proper component structure
- Separation of concerns
- Reusable components
- Component communication

### API Integration (25%)
- Service implementation
- HTTP client usage
- Error handling
- Loading states

### User Interface (25%)
- Responsive design
- Bootstrap usage
- Professional appearance
- User experience

### Forms and Validation (15%)
- Reactive forms implementation
- Validation rules
- Error message display
- Form submission handling

### Code Quality (10%)
- TypeScript usage
- Clean code
- Naming conventions
- Project organization

## Submission Guidelines

### Deliverables
1. Angular application source code
2. README with setup instructions
3. Screenshots of all major views
4. Presentation (10 minutes)

### Submission Checklist
- [ ] Application runs without errors
- [ ] Connected to Spring Boot API
- [ ] All CRUD operations work
- [ ] Responsive on mobile and desktop
- [ ] Forms have validation
- [ ] Loading and error states handled
- [ ] Navigation works correctly
- [ ] README complete

## Setup Instructions Template

```markdown
## Prerequisites
- Node.js 18+
- npm 9+
- Angular CLI 16+

## Installation
1. Clone the repository
2. Navigate to project folder
3. Run: `npm install`

## Configuration
1. Update environment.ts with your API URL:
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/api'
   };
   ```

## Running the Application
1. Ensure Spring Boot API is running on port 8080
2. Run: `ng serve`
3. Open http://localhost:4200

## Building for Production
Run: `ng build --configuration production`
```

## Resources

### Documentation
- [Angular Documentation](https://angular.io/docs)
- [Angular CLI](https://angular.io/cli)
- [RxJS Documentation](https://rxjs.dev/)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)

### Related Modules
- [Module 20: TypeScript](../../20-typescript/)
- [Module 21: Angular](../../21-angular/)
- [Module 22: RxJS](../../22-rxjs/)
- [Module 07: Bootstrap](../../07-bootstrap/)

---

**This project completes your full-stack journey! Focus on creating a polished, user-friendly interface that showcases your API work.**
