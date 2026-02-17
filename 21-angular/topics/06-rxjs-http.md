# RxJS & HTTP Client

## Introduction to RxJS

RxJS (Reactive Extensions for JavaScript) is a library for composing asynchronous and event-based programs using observable sequences.

```
┌─────────────────────────────────────────────────────────┐
│                      RxJS FLOW                          │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   Data Source ──► Observable ──► Operators ──► Observer │
│                                                         │
│   Examples:                                             │
│   • HTTP responses                                      │
│   • User events (clicks, input)                         │
│   • Timers and intervals                                │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Why RxJS in Angular?

- Angular's HttpClient returns Observables (not Promises)
- Event handling becomes declarative
- Easy to compose multiple async operations
- Built-in operators for common patterns (debounce, retry, etc.)

---

## Observables

An Observable is a stream of data that can emit multiple values over time.

### Promise vs Observable

```
┌─────────────────────────────────────────────────────────┐
│              PROMISE vs OBSERVABLE                      │
├───────────────────────┬─────────────────────────────────┤
│       Promise         │         Observable              │
├───────────────────────┼─────────────────────────────────┤
│ Single value          │ Multiple values over time       │
│ Eager (runs immediately)│ Lazy (runs on subscribe)     │
│ Not cancellable       │ Cancellable (unsubscribe)       │
│ Built-in to JS        │ RxJS library                    │
│ .then() / .catch()    │ .subscribe() / operators        │
└───────────────────────┴─────────────────────────────────┘
```

### Creating Observables

```typescript
import { Observable, of, from, interval } from 'rxjs';

// From static values
const numbers$ = of(1, 2, 3, 4, 5);

// From array
const array$ = from([1, 2, 3, 4, 5]);

// From promise
const promise$ = from(fetch('/api/data'));

// Interval (emits every n milliseconds)
const interval$ = interval(1000);  // 0, 1, 2, 3... every second

// Custom Observable
const custom$ = new Observable(subscriber => {
  subscriber.next('Hello');
  subscriber.next('World');
  subscriber.complete();
});
```

### Subscribing to Observables

```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription, of } from 'rxjs';

@Component({...})
export class DemoComponent implements OnInit, OnDestroy {
  private subscription!: Subscription;

  ngOnInit(): void {
    // Subscribe with observer object
    this.subscription = of(1, 2, 3).subscribe({
      next: value => console.log('Value:', value),
      error: err => console.error('Error:', err),
      complete: () => console.log('Complete')
    });

    // Or shorthand (just next callback)
    of(1, 2, 3).subscribe(value => console.log(value));
  }

  ngOnDestroy(): void {
    // IMPORTANT: Always unsubscribe to prevent memory leaks!
    this.subscription.unsubscribe();
  }
}
```

---

## RxJS Operators

Operators transform, filter, or combine observable streams.

### Using pipe()

```typescript
import { of } from 'rxjs';
import { map, filter, tap } from 'rxjs/operators';

of(1, 2, 3, 4, 5).pipe(
  filter(n => n % 2 === 0),  // Keep even numbers
  map(n => n * 10),          // Multiply by 10
  tap(n => console.log(n))   // Side effect (logging)
).subscribe(result => {
  console.log('Final:', result);
});
// Output: 20, 40
```

### Essential Operators

#### map - Transform Each Value

```typescript
import { map } from 'rxjs/operators';

// Transform user objects to just names
this.http.get<User[]>('/api/users').pipe(
  map(users => users.map(u => u.name))
).subscribe(names => console.log(names));
// ['Alice', 'Bob', 'Charlie']
```

#### filter - Keep Values Matching Condition

```typescript
import { filter } from 'rxjs/operators';

of(1, 2, 3, 4, 5).pipe(
  filter(n => n > 3)
).subscribe(console.log);
// 4, 5
```

#### tap - Side Effects (Logging, Debugging)

```typescript
import { tap } from 'rxjs/operators';

this.http.get('/api/users').pipe(
  tap(users => console.log('Fetched:', users)),  // Log but don't modify
  tap(users => this.cache = users)               // Store in cache
).subscribe();
```

#### switchMap - Switch to New Observable

Used when one observable depends on another (e.g., search as you type).

```typescript
import { switchMap, debounceTime } from 'rxjs/operators';

// Search input: cancels previous request when new input arrives
this.searchInput$.pipe(
  debounceTime(300),  // Wait 300ms after user stops typing
  switchMap(query => this.http.get(`/api/search?q=${query}`))
).subscribe(results => this.results = results);
```

#### catchError - Handle Errors

```typescript
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';

this.http.get('/api/data').pipe(
  catchError(error => {
    console.error('Error:', error);
    return of([]);  // Return fallback value
  })
).subscribe(data => this.data = data);
```

#### take - Take First N Values

```typescript
import { take } from 'rxjs/operators';
import { interval } from 'rxjs';

interval(1000).pipe(
  take(3)
).subscribe(console.log);
// 0, 1, 2, then completes
```

### Operator Summary

| Operator | Purpose | Example Use Case |
|----------|---------|------------------|
| `map` | Transform values | Convert User to UserDTO |
| `filter` | Keep matching values | Show only active users |
| `tap` | Side effects | Logging, caching |
| `switchMap` | Switch to new observable | Search typeahead |
| `catchError` | Handle errors | Show error message |
| `debounceTime` | Wait for pause | Search input |
| `take` | Limit emissions | Take first result |

---

## RxJS Subjects

Subjects are both Observable AND Observer - they can emit and receive values.

### Subject - Basic Multicast

```typescript
import { Subject } from 'rxjs';

const subject = new Subject<string>();

// Subscribe first
subject.subscribe(val => console.log('A:', val));
subject.subscribe(val => console.log('B:', val));

// Then emit
subject.next('Hello');
// Output: A: Hello, B: Hello

subject.next('World');
// Output: A: World, B: World
```

### BehaviorSubject - Has Current Value

BehaviorSubject stores the current value and emits it to new subscribers immediately.

```typescript
import { BehaviorSubject } from 'rxjs';

const counter = new BehaviorSubject<number>(0);  // Initial value: 0

counter.subscribe(val => console.log('A:', val));  // A: 0 (gets current value)

counter.next(1);  // A: 1
counter.next(2);  // A: 2

counter.subscribe(val => console.log('B:', val));  // B: 2 (gets current value)

counter.next(3);  // A: 3, B: 3

// Access current value directly
console.log(counter.value);  // 3
```

### BehaviorSubject for State Management

```typescript
// user-state.service.ts
@Injectable({ providedIn: 'root' })
export class UserStateService {
  private userSubject = new BehaviorSubject<User | null>(null);

  // Expose as Observable (read-only for consumers)
  user$ = this.userSubject.asObservable();

  // Get current value synchronously
  get currentUser(): User | null {
    return this.userSubject.value;
  }

  login(user: User): void {
    this.userSubject.next(user);
  }

  logout(): void {
    this.userSubject.next(null);
  }
}

// Usage in component
@Component({...})
export class NavbarComponent {
  user$ = this.userState.user$;

  constructor(private userState: UserStateService) {}
}

// In template
<div *ngIf="user$ | async as user">
  Welcome, {{ user.name }}!
</div>
```

### Subject vs BehaviorSubject

```
┌─────────────────────────────────────────────────────────┐
│           SUBJECT vs BEHAVIORSUBJECT                    │
├───────────────────────┬─────────────────────────────────┤
│       Subject         │      BehaviorSubject            │
├───────────────────────┼─────────────────────────────────┤
│ No initial value      │ Requires initial value          │
│ Late subscribers miss │ Late subscribers get current    │
│   previous values     │   value immediately             │
│ No .value property    │ .value gives current value      │
│ Use for: events       │ Use for: state management       │
└───────────────────────┴─────────────────────────────────┘
```

---

## Async Pipe

The `async` pipe subscribes to an Observable and automatically unsubscribes when the component is destroyed.

```typescript
@Component({
  selector: 'app-user-list',
  template: `
    <!-- Auto-subscribe and unsubscribe -->
    <div *ngIf="users$ | async as users">
      <div *ngFor="let user of users">
        {{ user.name }}
      </div>
    </div>

    <!-- With loading state -->
    <ng-container *ngIf="data$ | async as data; else loading">
      <p>{{ data.message }}</p>
    </ng-container>
    <ng-template #loading>
      <p>Loading...</p>
    </ng-template>
  `
})
export class UserListComponent {
  users$ = this.http.get<User[]>('/api/users');
  data$ = this.http.get('/api/data');

  constructor(private http: HttpClient) {}
}
```

**Benefits of async pipe:**
- No manual subscription/unsubscription
- No memory leaks
- Cleaner component code
- Works with OnPush change detection

---

## Unsubscribing (Preventing Memory Leaks)

### Method 1: Manual Unsubscribe

```typescript
@Component({...})
export class MyComponent implements OnDestroy {
  private subscription!: Subscription;

  ngOnInit(): void {
    this.subscription = this.service.data$.subscribe(
      data => this.data = data
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
```

### Method 2: Use Async Pipe (Recommended)

```typescript
@Component({
  template: `<div *ngFor="let item of items$ | async">{{ item }}</div>`
})
export class MyComponent {
  items$ = this.service.getItems();  // No manual subscribe needed!
}
```

### When You DON'T Need to Unsubscribe

- HTTP requests (complete after single response)
- Router events (Angular handles it)
- Async pipe (auto-unsubscribes)

---

## HTTP Client Basics

### Setup

```typescript
// app.module.ts
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule  // Add this
  ]
})
export class AppModule { }
```

### Making HTTP Requests

```typescript
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) {}

  // GET - Fetch all
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // GET - Fetch one
  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  // POST - Create
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  // PUT - Update
  updateUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  // DELETE
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

### Using Service in Component

```typescript
@Component({
  selector: 'app-user-list',
  template: `
    <div *ngIf="users$ | async as users; else loading">
      <div *ngFor="let user of users">
        {{ user.name }} - {{ user.email }}
      </div>
    </div>
    <ng-template #loading>Loading...</ng-template>
  `
})
export class UserListComponent implements OnInit {
  users$!: Observable<User[]>;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.users$ = this.userService.getUsers();
  }
}
```

### Error Handling in Service

```typescript
@Injectable({ providedIn: 'root' })
export class UserService {
  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>('/api/users').pipe(
      catchError(error => {
        console.error('Failed to fetch users:', error);
        return of([]);  // Return empty array on error
      })
    );
  }
}
```

---

## Summary

| Concept | Purpose |
|---------|---------|
| Observable | Stream of async data |
| `subscribe()` | Start receiving data |
| `unsubscribe()` | Stop and cleanup |
| `pipe()` | Chain operators |
| `map` | Transform values |
| `filter` | Keep matching values |
| `switchMap` | Switch to new observable |
| `catchError` | Handle errors |
| Subject | Multicast observable |
| BehaviorSubject | Subject with current value |
| `async` pipe | Auto-subscribe in template |
| HttpClient | Make HTTP requests |

## Next Topic

Continue to [Forms & State Management](./07-forms-state.md) to learn about handling user input.
