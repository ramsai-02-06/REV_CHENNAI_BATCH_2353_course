# Angular Routing

## Router Module

The Angular Router enables navigation between views as users perform application tasks. It provides a complete routing library with the ability to have multiple outlets, complex navigation paths, and route guards.

### Setting Up Routing

```bash
# Create app with routing
ng new my-app --routing

# Add routing to existing app
ng generate module app-routing --flat --module=app
```

### Basic Router Module

```typescript
// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: '**', component: NotFoundComponent }  // Wildcard route
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

```html
<!-- app.component.html -->
<nav>
  <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Home</a>
  <a routerLink="/about" routerLinkActive="active">About</a>
  <a routerLink="/contact" routerLinkActive="active">Contact</a>
</nav>

<router-outlet></router-outlet>
```

---

## Routing and Navigation

### Route Configuration

```typescript
const routes: Routes = [
  // Static route
  { path: 'home', component: HomeComponent },

  // Route with parameter
  { path: 'user/:id', component: UserDetailComponent },

  // Route with multiple parameters
  { path: 'product/:category/:id', component: ProductDetailComponent },

  // Route with optional parameters (via query params)
  { path: 'search', component: SearchComponent },

  // Redirect
  { path: '', redirectTo: '/home', pathMatch: 'full' },

  // Wildcard (must be last)
  { path: '**', component: NotFoundComponent }
];
```

### RouterLink Directive

```html
<!-- Basic navigation -->
<a routerLink="/home">Home</a>

<!-- Navigation with parameters -->
<a [routerLink]="['/user', userId]">User Profile</a>

<!-- Navigation with query parameters -->
<a [routerLink]="['/search']"
   [queryParams]="{query: 'angular', page: 1}">
  Search
</a>

<!-- Navigation with fragment -->
<a [routerLink]="['/about']" fragment="team">About Team</a>

<!-- Relative navigation -->
<a [routerLink]="['../sibling']">Sibling Route</a>
<a [routerLink]="['./child']">Child Route</a>
```

### RouterLinkActive

```html
<!-- Add class when route is active -->
<a routerLink="/home"
   routerLinkActive="active">
  Home
</a>

<!-- Multiple classes -->
<a routerLink="/about"
   routerLinkActive="active highlighted">
  About
</a>

<!-- Exact match (for root route) -->
<a routerLink="/"
   routerLinkActive="active"
   [routerLinkActiveOptions]="{exact: true}">
  Home
</a>

<!-- Binding to template variable -->
<a routerLink="/contact"
   routerLinkActive #rla="routerLinkActive"
   [class.active]="rla.isActive">
  Contact
</a>
```

### Programmatic Navigation

```typescript
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-navigation-demo',
  template: `
    <button (click)="goHome()">Go Home</button>
    <button (click)="goToUser(123)">View User 123</button>
    <button (click)="goBack()">Go Back</button>
  `
})
export class NavigationDemoComponent {
  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  goHome(): void {
    this.router.navigate(['/home']);
  }

  goToUser(id: number): void {
    // Absolute navigation
    this.router.navigate(['/user', id]);

    // With query parameters
    this.router.navigate(['/user', id], {
      queryParams: { tab: 'profile' }
    });

    // Relative navigation
    this.router.navigate(['../sibling'], {
      relativeTo: this.route
    });
  }

  goBack(): void {
    // Navigate to previous URL
    window.history.back();
  }
}
```

### Route Parameters

```typescript
// Reading route parameters
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-user-detail',
  template: `
    <div *ngIf="user">
      <h2>{{ user.name }}</h2>
      <p>Email: {{ user.email }}</p>
    </div>
  `
})
export class UserDetailComponent implements OnInit {
  user: User | null = null;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // Method 1: Snapshot (for simple cases)
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.userService.getUser(+id).subscribe(user => {
        this.user = user;
      });
    }

    // Method 2: Observable (recommended)
    this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
        const id = params.get('id');
        return this.userService.getUser(+id!);
      })
    ).subscribe(user => {
      this.user = user;
    });
  }
}
```

### Query Parameters

```typescript
@Component({
  selector: 'app-search',
  template: `
    <div>
      <input [(ngModel)]="searchQuery" (keyup.enter)="search()">
      <button (click)="search()">Search</button>
      <p>Results for: {{ currentQuery }}</p>
    </div>
  `
})
export class SearchComponent implements OnInit {
  searchQuery: string = '';
  currentQuery: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Read query parameters
    this.route.queryParamMap.subscribe(params => {
      this.currentQuery = params.get('query') || '';
      this.searchQuery = this.currentQuery;
    });

    // Or using snapshot
    const query = this.route.snapshot.queryParamMap.get('query');
  }

  search(): void {
    // Update query parameters
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { query: this.searchQuery, page: 1 },
      queryParamsHandling: 'merge'  // Preserve other query params
    });
  }
}
```

---

## Child Routes

```typescript
// app-routing.module.ts
const routes: Routes = [
  {
    path: 'products',
    component: ProductsComponent,
    children: [
      { path: '', component: ProductListComponent },
      { path: ':id', component: ProductDetailComponent },
      { path: ':id/edit', component: ProductEditComponent }
    ]
  }
];
```

```html
<!-- products.component.html -->
<div class="products-layout">
  <aside>
    <nav>
      <a routerLink="/products">All Products</a>
    </nav>
  </aside>
  <main>
    <router-outlet></router-outlet>  <!-- Child routes render here -->
  </main>
</div>
```

---

## Route Guards

Route guards are used to control access to routes based on certain conditions.

### Types of Guards

1. **CanActivate**: Decides if a route can be activated
2. **CanActivateChild**: Decides if child routes can be activated
3. **CanDeactivate**: Decides if a route can be deactivated
4. **Resolve**: Performs data retrieval before route activation
5. **CanLoad**: Decides if a module can be loaded lazily

### CanActivate Guard

```bash
ng generate guard guards/auth
```

```typescript
// auth.guard.ts
import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authService.isLoggedIn()) {
      return true;
    }

    // Redirect to login with return URL
    return this.router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url }
    });
  }
}
```

```typescript
// Using the guard
const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AuthGuard, AdminGuard]  // Multiple guards
  }
];
```

### CanActivateChild Guard

```typescript
// admin.guard.ts
@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivateChild {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    if (this.authService.isAdmin()) {
      return true;
    }

    return this.router.createUrlTree(['/unauthorized']);
  }
}
```

```typescript
const routes: Routes = [
  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivateChild: [AdminGuard],
    children: [
      { path: 'users', component: UserManagementComponent },
      { path: 'settings', component: SettingsComponent }
    ]
  }
];
```

### CanDeactivate Guard

```typescript
// unsaved-changes.guard.ts
export interface CanComponentDeactivate {
  canDeactivate: () => Observable<boolean> | Promise<boolean> | boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UnsavedChangesGuard implements CanDeactivate<CanComponentDeactivate> {
  canDeactivate(
    component: CanComponentDeactivate,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    return component.canDeactivate ? component.canDeactivate() : true;
  }
}
```

```typescript
// product-edit.component.ts
@Component({
  selector: 'app-product-edit',
  template: `
    <form>
      <input [(ngModel)]="productName" name="name">
      <button type="submit">Save</button>
    </form>
  `
})
export class ProductEditComponent implements CanComponentDeactivate {
  productName: string = '';
  private originalName: string = '';
  private saved: boolean = false;

  canDeactivate(): boolean {
    if (!this.saved && this.productName !== this.originalName) {
      return confirm('You have unsaved changes. Do you want to leave?');
    }
    return true;
  }
}
```

```typescript
const routes: Routes = [
  {
    path: 'product/:id/edit',
    component: ProductEditComponent,
    canDeactivate: [UnsavedChangesGuard]
  }
];
```

### Resolve Guard

```typescript
// user-resolver.service.ts
@Injectable({
  providedIn: 'root'
})
export class UserResolver implements Resolve<User> {
  constructor(private userService: UserService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<User> | Promise<User> | User {
    const id = route.paramMap.get('id');
    return this.userService.getUser(+id!);
  }
}
```

```typescript
const routes: Routes = [
  {
    path: 'user/:id',
    component: UserDetailComponent,
    resolve: {
      user: UserResolver  // Data available before component loads
    }
  }
];
```

```typescript
// user-detail.component.ts
@Component({
  selector: 'app-user-detail',
  template: '<div>{{ user.name }}</div>'
})
export class UserDetailComponent implements OnInit {
  user!: User;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    // Data already loaded by resolver
    this.user = this.route.snapshot.data['user'];

    // Or subscribe to data changes
    this.route.data.subscribe(data => {
      this.user = data['user'];
    });
  }
}
```

### CanLoad Guard

```typescript
// auth.guard.ts (extended)
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(/* ... */): boolean | UrlTree {
    // ... existing implementation
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]
  ): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authService.isLoggedIn()) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }
}
```

```typescript
const routes: Routes = [
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
    canLoad: [AuthGuard]  // Prevents module from loading
  }
];
```

---

## Lazy Loading

Lazy loading helps reduce initial bundle size by loading feature modules on demand.

### Configuring Lazy Loading

```typescript
// app-routing.module.ts
const routes: Routes = [
  {
    path: 'products',
    loadChildren: () => import('./features/products/products.module')
      .then(m => m.ProductsModule)
  },
  {
    path: 'users',
    loadChildren: () => import('./features/users/users.module')
      .then(m => m.UsersModule)
  }
];
```

```typescript
// products-routing.module.ts
const routes: Routes = [
  { path: '', component: ProductListComponent },
  { path: ':id', component: ProductDetailComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],  // Use forChild in feature modules
  exports: [RouterModule]
})
export class ProductsRoutingModule { }
```

### Preloading Strategies

```typescript
// app-routing.module.ts
import { PreloadAllModules } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      preloadingStrategy: PreloadAllModules  // Preload all lazy modules
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

### Custom Preloading Strategy

```typescript
// selective-preload.strategy.ts
import { Injectable } from '@angular/core';
import { PreloadingStrategy, Route } from '@angular/router';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SelectivePreloadStrategy implements PreloadingStrategy {
  preload(route: Route, load: () => Observable<any>): Observable<any> {
    if (route.data && route.data['preload']) {
      console.log('Preloading: ' + route.path);
      return load();
    }
    return of(null);
  }
}
```

```typescript
// app-routing.module.ts
const routes: Routes = [
  {
    path: 'products',
    loadChildren: () => import('./products/products.module').then(m => m.ProductsModule),
    data: { preload: true }  // Will be preloaded
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule)
    // Will not be preloaded
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      preloadingStrategy: SelectivePreloadStrategy
    })
  ]
})
export class AppRoutingModule { }
```

---

## Signals (Angular 16+)

Signals provide a new reactive primitive for managing state in Angular applications.

### Basic Signals

```typescript
import { Component, signal, computed } from '@angular/core';

@Component({
  selector: 'app-counter',
  template: `
    <div>
      <p>Count: {{ count() }}</p>
      <p>Double: {{ doubleCount() }}</p>
      <button (click)="increment()">Increment</button>
      <button (click)="decrement()">Decrement</button>
      <button (click)="reset()">Reset</button>
    </div>
  `
})
export class CounterComponent {
  // Writable signal
  count = signal(0);

  // Computed signal (derived state)
  doubleCount = computed(() => this.count() * 2);

  increment(): void {
    this.count.update(value => value + 1);
  }

  decrement(): void {
    this.count.update(value => value - 1);
  }

  reset(): void {
    this.count.set(0);
  }
}
```

### Signals with Effects

```typescript
import { Component, signal, effect } from '@angular/core';

@Component({
  selector: 'app-theme',
  template: `
    <button (click)="toggleTheme()">
      Current theme: {{ theme() }}
    </button>
  `
})
export class ThemeComponent {
  theme = signal<'light' | 'dark'>('light');

  constructor() {
    // Effect runs when signal changes
    effect(() => {
      const theme = this.theme();
      document.body.classList.toggle('dark-theme', theme === 'dark');
      localStorage.setItem('theme', theme);
    });
  }

  toggleTheme(): void {
    this.theme.update(current => current === 'light' ? 'dark' : 'light');
  }
}
```

### Signals with Router

```typescript
import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navigation',
  template: `
    <nav>
      <a [class.active]="isActive('/home')"
         (click)="navigate('/home')">
        Home
      </a>
      <a [class.active]="isActive('/about')"
         (click)="navigate('/about')">
        About
      </a>
    </nav>
    <p>Current path: {{ currentPath() }}</p>
  `
})
export class NavigationComponent {
  currentPath = signal('/home');

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      this.currentPath.set(this.router.url);
    });
  }

  navigate(path: string): void {
    this.router.navigate([path]);
    this.currentPath.set(path);
  }

  isActive(path: string): boolean {
    return this.currentPath() === path;
  }
}
```

---

## Advanced Routing

### Named Router Outlets

```html
<!-- app.component.html -->
<div class="layout">
  <router-outlet></router-outlet>  <!-- Primary outlet -->
  <router-outlet name="sidebar"></router-outlet>  <!-- Named outlet -->
</div>
```

```typescript
const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: SidebarComponent,
        outlet: 'sidebar'
      }
    ]
  }
];

// Navigate to named outlet
this.router.navigate([{
  outlets: {
    primary: ['home'],
    sidebar: ['user-info']
  }
}]);
```

### Route Data

```typescript
const routes: Routes = [
  {
    path: 'about',
    component: AboutComponent,
    data: { title: 'About Us', breadcrumb: 'About' }
  }
];

// Access route data
this.route.data.subscribe(data => {
  this.title = data['title'];
});
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Router Module | Configure routes and navigation |
| RouterLink | Declarative navigation in templates |
| Route Guards | Control access to routes |
| Lazy Loading | Load modules on demand |
| Signals | Reactive state management (Angular 16+) |
| Named Outlets | Multiple router outlets |

## Next Topic

Continue to [RxJS Fundamentals](./07-rxjs-fundamentals.md) to learn reactive programming before working with HTTP.
