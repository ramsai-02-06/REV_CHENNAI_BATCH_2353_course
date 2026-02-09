# HTTP Client

## Introduction to HTTP Client

Angular's `HttpClient` is a powerful service for making HTTP requests to remote servers. It provides a simplified API for HTTP functionality, supports request and response interception, and offers strong typing with TypeScript.

### Setting Up HttpClient

```typescript
// app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    HttpClientModule  // Import HttpClientModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

---

## Making HTTP Requests

### GET Requests

```typescript
// user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
  username: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'https://jsonplaceholder.typicode.com/users';

  constructor(private http: HttpClient) {}

  // Basic GET request
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // GET request with path parameter
  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  // GET request with query parameters
  searchUsers(term: string, page: number = 1): Observable<User[]> {
    const params = new HttpParams()
      .set('q', term)
      .set('page', page.toString())
      .set('limit', '10');

    return this.http.get<User[]>(this.apiUrl, { params });
  }

  // GET request with headers
  getUsersWithAuth(): Observable<User[]> {
    const headers = {
      'Authorization': 'Bearer token123',
      'Content-Type': 'application/json'
    };

    return this.http.get<User[]>(this.apiUrl, { headers });
  }
}
```

### POST Requests

```typescript
// user.service.ts (continued)
export class UserService {
  // Basic POST request
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  // POST with headers
  createUserWithAuth(user: User): Observable<User> {
    const headers = {
      'Authorization': 'Bearer token123',
      'Content-Type': 'application/json'
    };

    return this.http.post<User>(this.apiUrl, user, { headers });
  }

  // POST with full response
  createUserFullResponse(user: User): Observable<any> {
    return this.http.post<User>(this.apiUrl, user, {
      observe: 'response'  // Get full HTTP response
    });
  }
}
```

### PUT Requests

```typescript
// user.service.ts (continued)
export class UserService {
  // Update entire resource
  updateUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  // Partial update
  patchUser(id: number, updates: Partial<User>): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${id}`, updates);
  }
}
```

### DELETE Requests

```typescript
// user.service.ts (continued)
export class UserService {
  // Delete resource
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Delete with options
  deleteUserWithResponse(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      observe: 'response'
    });
  }
}
```

### Using HTTP Requests in Components

```typescript
// user-list.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService, User } from '../services/user.service';

@Component({
  selector: 'app-user-list',
  template: `
    <div *ngIf="loading">Loading...</div>
    <div *ngIf="error" class="error">{{ error }}</div>

    <div *ngIf="!loading && !error">
      <ul>
        <li *ngFor="let user of users">
          {{ user.name }} ({{ user.email }})
          <button (click)="editUser(user)">Edit</button>
          <button (click)="deleteUser(user.id)">Delete</button>
        </li>
      </ul>

      <button (click)="addUser()">Add User</button>
    </div>
  `
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  loading: boolean = false;
  error: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.error = '';

    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load users';
        this.loading = false;
        console.error('Error loading users:', err);
      }
    });
  }

  addUser(): void {
    const newUser: User = {
      id: 0,
      name: 'New User',
      email: 'newuser@example.com',
      username: 'newuser'
    };

    this.userService.createUser(newUser).subscribe({
      next: (user) => {
        this.users.push(user);
      },
      error: (err) => {
        console.error('Error creating user:', err);
      }
    });
  }

  editUser(user: User): void {
    const updated = { ...user, name: user.name + ' (Updated)' };

    this.userService.updateUser(user.id, updated).subscribe({
      next: (updatedUser) => {
        const index = this.users.findIndex(u => u.id === user.id);
        if (index !== -1) {
          this.users[index] = updatedUser;
        }
      },
      error: (err) => {
        console.error('Error updating user:', err);
      }
    });
  }

  deleteUser(id: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.users = this.users.filter(u => u.id !== id);
        },
        error: (err) => {
          console.error('Error deleting user:', err);
        }
      });
    }
  }
}
```

---

## Error Handling

### Basic Error Handling

```typescript
import { catchError, retry } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl).pipe(
      retry(3),  // Retry failed request up to 3 times
      catchError(this.handleError)
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);

    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      console.error('Client-side error:', error.error.message);
    } else {
      // Backend error
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`
      );
    }

    // Return user-friendly error message
    return throwError(() => new Error('Something went wrong. Please try again later.'));
  }
}
```

### Advanced Error Handling

```typescript
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = '';

    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client Error: ${error.error.message}`;
    } else {
      // Server-side error
      switch (error.status) {
        case 0:
          errorMessage = 'No connection. Please check your internet connection.';
          break;
        case 400:
          errorMessage = 'Bad Request. Please check your input.';
          break;
        case 401:
          errorMessage = 'Unauthorized. Please login.';
          break;
        case 403:
          errorMessage = 'Forbidden. You do not have permission.';
          break;
        case 404:
          errorMessage = 'Resource not found.';
          break;
        case 500:
          errorMessage = 'Internal server error. Please try again later.';
          break;
        default:
          errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      }
    }

    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}

// Using the error handler
@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private http: HttpClient,
    private errorHandler: ErrorHandlerService
  ) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl).pipe(
      catchError(this.errorHandler.handleError)
    );
  }
}
```

### Retry Logic

```typescript
import { retry, retryWhen, delay, take, concat } from 'rxjs/operators';
import { throwError, timer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  constructor(private http: HttpClient) {}

  // Simple retry
  getDataWithRetry(): Observable<any> {
    return this.http.get(this.apiUrl).pipe(
      retry(3)  // Retry up to 3 times
    );
  }

  // Retry with delay
  getDataWithDelayedRetry(): Observable<any> {
    return this.http.get(this.apiUrl).pipe(
      retryWhen(errors =>
        errors.pipe(
          delay(1000),  // Wait 1 second between retries
          take(3)       // Maximum 3 retries
        )
      )
    );
  }

  // Exponential backoff retry
  getDataWithBackoff(): Observable<any> {
    return this.http.get(this.apiUrl).pipe(
      retryWhen(errors =>
        errors.pipe(
          concatMap((error, index) => {
            if (index >= 3) {
              return throwError(() => error);
            }
            const delay = Math.pow(2, index) * 1000;  // 1s, 2s, 4s
            console.log(`Retry attempt ${index + 1} after ${delay}ms`);
            return timer(delay);
          })
        )
      )
    );
  }
}
```

---

## HTTP Interceptors

Interceptors allow you to inspect and transform HTTP requests and responses globally.

### Creating an Interceptor

```bash
ng generate interceptor interceptors/auth
```

### Authentication Interceptor

```typescript
// auth.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    // Get authentication token
    const token = this.authService.getToken();

    // Clone request and add authorization header
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    // Pass the modified request to the next handler
    return next.handle(request);
  }
}
```

### Logging Interceptor

```typescript
// logging.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap, finalize } from 'rxjs/operators';

@Injectable()
export class LoggingInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const startTime = Date.now();
    let status: string;

    return next.handle(request).pipe(
      tap({
        next: (event) => {
          if (event instanceof HttpResponse) {
            status = 'succeeded';
          }
        },
        error: (error) => {
          status = 'failed';
        }
      }),
      finalize(() => {
        const elapsedTime = Date.now() - startTime;
        console.log(`${request.method} ${request.urlWithParams} ${status} in ${elapsedTime}ms`);
      })
    );
  }
}
```

### Error Handling Interceptor

```typescript
// error.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Redirect to login on unauthorized
          this.router.navigate(['/login']);
        } else if (error.status === 403) {
          // Handle forbidden
          console.error('Access forbidden');
        }

        return throwError(() => error);
      })
    );
  }
}
```

### Loading Interceptor

```typescript
// loading.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { LoadingService } from '../services/loading.service';

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {
  private activeRequests = 0;

  constructor(private loadingService: LoadingService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    if (this.activeRequests === 0) {
      this.loadingService.show();
    }

    this.activeRequests++;

    return next.handle(request).pipe(
      finalize(() => {
        this.activeRequests--;
        if (this.activeRequests === 0) {
          this.loadingService.hide();
        }
      })
    );
  }
}
```

### Caching Interceptor

```typescript
// cache.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse
} from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class CacheInterceptor implements HttpInterceptor {
  private cache = new Map<string, HttpResponse<any>>();

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    // Only cache GET requests
    if (request.method !== 'GET') {
      return next.handle(request);
    }

    // Check if cached response exists
    const cachedResponse = this.cache.get(request.url);
    if (cachedResponse) {
      console.log('Returning cached response for:', request.url);
      return of(cachedResponse);
    }

    // Make request and cache response
    return next.handle(request).pipe(
      tap(event => {
        if (event instanceof HttpResponse) {
          this.cache.set(request.url, event);
        }
      })
    );
  }
}
```

### Registering Interceptors

```typescript
// app.module.ts
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthInterceptor } from './interceptors/auth.interceptor';
import { LoggingInterceptor } from './interceptors/logging.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { LoadingInterceptor } from './interceptors/loading.interceptor';

@NgModule({
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true  // Allow multiple interceptors
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoggingInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,
      multi: true
    }
  ]
})
export class AppModule { }
```

---

## Best Practices

### 1. Type Safety

```typescript
// Define interfaces for API responses
export interface ApiResponse<T> {
  data: T;
  status: string;
  message?: string;
}

export interface User {
  id: number;
  name: string;
  email: string;
}

// Use strong typing
@Injectable({
  providedIn: 'root'
})
export class UserService {
  getUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users`);
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }
}
```

### 2. Centralized Configuration

```typescript
// environment.ts
export const environment = {
  production: false,
  apiUrl: 'https://api.example.com/v1'
};

// api.service.ts
@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  get<T>(endpoint: string, params?: HttpParams): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}/${endpoint}`, { params });
  }

  post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}/${endpoint}`, data);
  }

  put<T>(endpoint: string, data: any): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}/${endpoint}`, data);
  }

  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(`${this.apiUrl}/${endpoint}`);
  }
}
```

### 3. Unsubscribe from Observables

```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-user-list',
  template: '<div *ngFor="let user of users">{{ user.name }}</div>'
})
export class UserListComponent implements OnInit, OnDestroy {
  users: User[] = [];
  private destroy$ = new Subject<void>();

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getUsers()
      .pipe(takeUntil(this.destroy$))
      .subscribe(users => {
        this.users = users;
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

### 4. Loading States

```typescript
@Component({
  selector: 'app-data-display',
  template: `
    <div *ngIf="loading$ | async" class="loading">
      Loading...
    </div>

    <div *ngIf="error$ | async as error" class="error">
      {{ error }}
    </div>

    <div *ngIf="data$ | async as data">
      <div *ngFor="let item of data">{{ item.name }}</div>
    </div>
  `
})
export class DataDisplayComponent implements OnInit {
  data$!: Observable<any[]>;
  loading$ = new BehaviorSubject<boolean>(false);
  error$ = new BehaviorSubject<string>('');

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading$.next(true);
    this.error$.next('');

    this.data$ = this.dataService.getData().pipe(
      tap(() => this.loading$.next(false)),
      catchError(error => {
        this.loading$.next(false);
        this.error$.next('Failed to load data');
        return throwError(() => error);
      })
    );
  }
}
```

### 5. Request Cancellation

```typescript
import { Subject } from 'rxjs';
import { takeUntil, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-search',
  template: `
    <input [formControl]="searchControl" placeholder="Search...">
    <div *ngFor="let result of results">{{ result }}</div>
  `
})
export class SearchComponent implements OnInit, OnDestroy {
  searchControl = new FormControl('');
  results: any[] = [];
  private destroy$ = new Subject<void>();

  constructor(private searchService: SearchService) {}

  ngOnInit(): void {
    this.searchControl.valueChanges.pipe(
      debounceTime(300),           // Wait 300ms after user stops typing
      distinctUntilChanged(),      // Only if value changed
      switchMap(term => {
        if (term) {
          return this.searchService.search(term);
        }
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe(results => {
      this.results = results;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| HttpClient | Service for making HTTP requests |
| HTTP Methods | GET, POST, PUT, PATCH, DELETE |
| Error Handling | catchError, retry, custom error handlers |
| Interceptors | Global request/response transformation |
| Best Practices | Type safety, unsubscribe, loading states |

## Next Topic

Continue to [Forms](./09-forms.md) to learn about template-driven and reactive forms in Angular.
