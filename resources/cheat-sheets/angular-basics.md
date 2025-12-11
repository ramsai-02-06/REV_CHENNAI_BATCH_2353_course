# Angular Basics Cheat Sheet

## Setup & Installation

```bash
# Install Node.js (required)
# Download from nodejs.org

# Install Angular CLI globally
npm install -g @angular/cli

# Check version
ng version

# Create new project
ng new my-app
ng new my-app --routing --style=scss

# Serve application
cd my-app
ng serve
ng serve --open  # Opens browser automatically
ng serve --port 4300  # Custom port
```

## Project Structure

```
my-app/
├── src/
│   ├── app/
│   │   ├── app.component.ts      # Root component
│   │   ├── app.component.html    # Template
│   │   ├── app.component.css     # Styles
│   │   ├── app.component.spec.ts # Tests
│   │   ├── app.module.ts         # Root module
│   │   └── app-routing.module.ts # Routing
│   ├── assets/                   # Static files
│   ├── environments/             # Environment configs
│   ├── index.html               # Main HTML
│   ├── main.ts                  # Entry point
│   └── styles.css               # Global styles
├── angular.json                 # Angular config
├── package.json                 # Dependencies
└── tsconfig.json               # TypeScript config
```

## CLI Commands

```bash
# Generate components
ng generate component component-name
ng g c component-name  # Shorthand

# Generate service
ng generate service service-name
ng g s service-name

# Generate module
ng generate module module-name
ng g m module-name --routing

# Generate directive
ng generate directive directive-name
ng g d directive-name

# Generate pipe
ng generate pipe pipe-name
ng g p pipe-name

# Generate guard
ng generate guard guard-name
ng g g guard-name

# Generate interface
ng generate interface interface-name
ng g i interface-name

# Build for production
ng build
ng build --prod
ng build --configuration production

# Run tests
ng test

# Run e2e tests
ng e2e

# Lint code
ng lint
```

## Component

### Component TypeScript

```typescript
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  // Properties
  title: string = 'Product List';
  products: Product[] = [];
  selectedProduct?: Product;
  
  // Constructor (Dependency Injection)
  constructor(private productService: ProductService) { }
  
  // Lifecycle hook
  ngOnInit(): void {
    this.loadProducts();
  }
  
  // Methods
  loadProducts(): void {
    this.productService.getProducts().subscribe(
      data => this.products = data
    );
  }
  
  selectProduct(product: Product): void {
    this.selectedProduct = product;
  }
}
```

### Component Template

```html
<!-- Interpolation -->
<h1>{{ title }}</h1>

<!-- Property binding -->
<img [src]="product.imageUrl">
<button [disabled]="!isValid">Submit</button>

<!-- Event binding -->
<button (click)="onClick()">Click me</button>
<input (input)="onInput($event)">

<!-- Two-way binding -->
<input [(ngModel)]="username">

<!-- Structural directives -->
<div *ngIf="isLoggedIn">Welcome!</div>
<div *ngIf="user; else loading">{{ user.name }}</div>
<ng-template #loading>Loading...</ng-template>

<ul>
  <li *ngFor="let product of products; let i = index">
    {{ i + 1 }}. {{ product.name }}
  </li>
</ul>

<div [ngSwitch]="status">
  <p *ngSwitchCase="'pending'">Pending...</p>
  <p *ngSwitchCase="'approved'">Approved!</p>
  <p *ngSwitchDefault>Unknown status</p>
</div>

<!-- Attribute directives -->
<div [ngClass]="{'active': isActive, 'disabled': !isEnabled}">Content</div>
<div [ngStyle]="{'color': textColor, 'font-size': fontSize + 'px'}">Styled</div>

<!-- Pipes -->
<p>{{ today | date }}</p>
<p>{{ price | currency:'USD' }}</p>
<p>{{ name | uppercase }}</p>
<p>{{ description | slice:0:100 }}</p>
```

## Data Binding

```typescript
// Component
export class AppComponent {
  name: string = 'Angular';
  imageUrl: string = 'assets/logo.png';
  isDisabled: boolean = false;
  
  handleClick(): void {
    console.log('Button clicked');
  }
}
```

```html
<!-- One-way: Component to View -->
<h1>{{ name }}</h1>
<img [src]="imageUrl">

<!-- One-way: View to Component -->
<button (click)="handleClick()">Click</button>

<!-- Two-way binding -->
<input [(ngModel)]="name">
```

## Services & Dependency Injection

```typescript
// product.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'  // Singleton service
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products';
  
  constructor(private http: HttpClient) { }
  
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }
  
  getProduct(id: number): Observable<Product> {
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

// Usage in component
export class ProductComponent {
  products: Product[] = [];
  
  constructor(private productService: ProductService) { }
  
  ngOnInit(): void {
    this.productService.getProducts().subscribe(
      data => {
        this.products = data;
      },
      error => {
        console.error('Error:', error);
      }
    );
  }
}
```

## Routing

### app-routing.module.ts

```typescript
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'products', component: ProductListComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: 'about', component: AboutComponent },
  { path: '**', component: NotFoundComponent }  // Wildcard route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

### Navigation

```html
<!-- Using routerLink -->
<a routerLink="/home">Home</a>
<a routerLink="/products">Products</a>
<a [routerLink]="['/products', product.id]">View Product</a>

<!-- Active link styling -->
<a routerLink="/home" routerLinkActive="active">Home</a>

<!-- Router outlet -->
<router-outlet></router-outlet>
```

```typescript
// Programmatic navigation
import { Router } from '@angular/router';

constructor(private router: Router) { }

navigateToProduct(id: number): void {
  this.router.navigate(['/products', id]);
}

// With query params
this.router.navigate(['/products'], { 
  queryParams: { category: 'electronics' }
});

// Get route parameters
import { ActivatedRoute } from '@angular/router';

constructor(private route: ActivatedRoute) { }

ngOnInit(): void {
  this.route.params.subscribe(params => {
    const id = params['id'];
  });
  
  this.route.queryParams.subscribe(params => {
    const category = params['category'];
  });
}
```

## Forms

### Template-Driven Forms

```html
<form #productForm="ngForm" (ngSubmit)="onSubmit(productForm)">
  <input 
    name="name" 
    [(ngModel)]="product.name" 
    required 
    minlength="3"
    #name="ngModel">
  <div *ngIf="name.invalid && name.touched">
    <p *ngIf="name.errors?.['required']">Name is required</p>
    <p *ngIf="name.errors?.['minlength']">Min 3 characters</p>
  </div>
  
  <button [disabled]="productForm.invalid">Submit</button>
</form>
```

### Reactive Forms

```typescript
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

export class ProductComponent implements OnInit {
  productForm!: FormGroup;
  
  constructor(private fb: FormBuilder) { }
  
  ngOnInit(): void {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      price: ['', [Validators.required, Validators.min(0)]],
      description: [''],
      category: ['electronics']
    });
  }
  
  onSubmit(): void {
    if (this.productForm.valid) {
      console.log(this.productForm.value);
    }
  }
  
  get name() {
    return this.productForm.get('name');
  }
}
```

```html
<form [formGroup]="productForm" (ngSubmit)="onSubmit()">
  <input formControlName="name">
  <div *ngIf="name?.invalid && name?.touched">
    <p *ngIf="name?.errors?.['required']">Name is required</p>
  </div>
  
  <input formControlName="price" type="number">
  <textarea formControlName="description"></textarea>
  <select formControlName="category">
    <option value="electronics">Electronics</option>
    <option value="clothing">Clothing</option>
  </select>
  
  <button [disabled]="productForm.invalid">Submit</button>
</form>
```

## HTTP Client

### app.module.ts

```typescript
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule
  ]
})
export class AppModule { }
```

### HTTP Interceptor

```typescript
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const authToken = localStorage.getItem('token');
    
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${authToken}`)
    });
    
    return next.handle(authReq);
  }
}

// Register in app.module.ts
providers: [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
]
```

## Pipes

### Built-in Pipes

```html
<!-- Date -->
{{ today | date:'short' }}
{{ today | date:'dd/MM/yyyy' }}

<!-- Currency -->
{{ price | currency:'USD':'symbol':'1.2-2' }}

<!-- Decimal -->
{{ value | number:'1.2-2' }}

<!-- Percent -->
{{ ratio | percent }}

<!-- Uppercase/Lowercase -->
{{ name | uppercase }}
{{ name | lowercase }}

<!-- Slice -->
{{ text | slice:0:100 }}

<!-- JSON -->
{{ object | json }}
```

### Custom Pipe

```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'reverse'
})
export class ReversePipe implements PipeTransform {
  transform(value: string): string {
    return value.split('').reverse().join('');
  }
}
```

```html
<p>{{ 'Hello' | reverse }}</p>
<!-- Output: olleH -->
```

## Lifecycle Hooks

```typescript
export class AppComponent implements OnInit, OnDestroy, OnChanges {
  constructor() {
    console.log('Constructor called');
  }
  
  ngOnChanges(): void {
    console.log('ngOnChanges - called when input properties change');
  }
  
  ngOnInit(): void {
    console.log('ngOnInit - called once after first ngOnChanges');
  }
  
  ngDoCheck(): void {
    console.log('ngDoCheck - called during every change detection');
  }
  
  ngAfterContentInit(): void {
    console.log('ngAfterContentInit - after content projection');
  }
  
  ngAfterContentChecked(): void {
    console.log('ngAfterContentChecked - after checking projected content');
  }
  
  ngAfterViewInit(): void {
    console.log('ngAfterViewInit - after initializing view');
  }
  
  ngAfterViewChecked(): void {
    console.log('ngAfterViewChecked - after checking view');
  }
  
  ngOnDestroy(): void {
    console.log('ngOnDestroy - cleanup before component destruction');
  }
}
```

## Component Communication

### Input/Output

```typescript
// Child component
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-child',
  template: `
    <p>{{ message }}</p>
    <button (click)="sendData()">Send</button>
  `
})
export class ChildComponent {
  @Input() message!: string;
  @Output() dataEvent = new EventEmitter<string>();
  
  sendData(): void {
    this.dataEvent.emit('Hello from child');
  }
}

// Parent component
@Component({
  template: `
    <app-child 
      [message]="parentMessage" 
      (dataEvent)="receiveData($event)">
    </app-child>
  `
})
export class ParentComponent {
  parentMessage = 'Hello Child';
  
  receiveData(data: string): void {
    console.log(data);
  }
}
```

## Quick Reference

```bash
# CLI commands
ng new <name>           # Create project
ng serve                # Run dev server
ng build                # Build project
ng test                 # Run tests
ng g c <name>           # Generate component
ng g s <name>           # Generate service
ng g m <name>           # Generate module

# Decorators
@Component              # Define component
@Injectable             # Define service
@Input                  # Input property
@Output                 # Output event
@Pipe                   # Define pipe
@Directive              # Define directive

# Common imports
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
```

---

**Best Practices:**
- Use Angular CLI for generation
- Follow naming conventions
- Keep components small and focused
- Use services for business logic
- Implement OnDestroy for cleanup
- Use reactive forms for complex forms
- Lazy load feature modules
- Use async pipe for observables
- Implement error handling
- Write unit tests
