# RxJS Fundamentals

## What is RxJS?

RxJS (Reactive Extensions for JavaScript) is a library for composing asynchronous programs using observable sequences. Angular uses RxJS extensively for handling async operations like HTTP requests, user events, and state management.

### Why RxJS in Angular?

```
Traditional Async          →    RxJS Approach
─────────────────────────────────────────────
Callbacks (callback hell)  →    Observable streams
Promises (single value)    →    Observable (multiple values)
Event listeners           →    fromEvent() observables
setInterval/setTimeout    →    interval()/timer()
```

---

## Observables

An Observable is a stream of data that arrives over time. Unlike Promises (which resolve once), Observables can emit multiple values.

### Creating Observables

```typescript
import { Observable, of, from } from 'rxjs';

// Create from values
const numbers$ = of(1, 2, 3, 4, 5);

// Create from array
const fruits$ = from(['apple', 'banana', 'cherry']);

// Create custom observable
const custom$ = new Observable<number>(subscriber => {
    subscriber.next(1);
    subscriber.next(2);
    subscriber.next(3);
    subscriber.complete();
});
```

### Subscribing to Observables

Observables are lazy - they don't execute until subscribed:

```typescript
import { of } from 'rxjs';

const data$ = of('Hello', 'World');

// Subscribe with observer object
data$.subscribe({
    next: (value) => console.log('Received:', value),
    error: (err) => console.error('Error:', err),
    complete: () => console.log('Done!')
});

// Output:
// Received: Hello
// Received: World
// Done!

// Simplified: just the next handler
data$.subscribe(value => console.log(value));
```

### Observable vs Promise

```typescript
// Promise: Single value, eager execution
const promise = new Promise(resolve => {
    console.log('Promise executing');  // Runs immediately
    resolve('done');
});

// Observable: Multiple values, lazy execution
const observable$ = new Observable(subscriber => {
    console.log('Observable executing');  // Only runs when subscribed
    subscriber.next('value 1');
    subscriber.next('value 2');
    subscriber.complete();
});

// Nothing happens until we subscribe
observable$.subscribe(value => console.log(value));
```

---

## The Pipe Operator

`pipe()` chains operators to transform observable data:

```typescript
import { of } from 'rxjs';
import { map, filter } from 'rxjs/operators';

const numbers$ = of(1, 2, 3, 4, 5);

numbers$.pipe(
    filter(n => n % 2 === 0),     // Keep even numbers
    map(n => n * 10)              // Multiply by 10
).subscribe(result => console.log(result));

// Output: 20, 40
```

### Common Operators

#### Transformation Operators

```typescript
import { of } from 'rxjs';
import { map, tap } from 'rxjs/operators';

// map: Transform each value
of(1, 2, 3).pipe(
    map(x => x * 2)
).subscribe(console.log);  // 2, 4, 6

// tap: Side effects without changing values (for logging/debugging)
of(1, 2, 3).pipe(
    tap(x => console.log('Before:', x)),
    map(x => x * 2),
    tap(x => console.log('After:', x))
).subscribe();
```

#### Filtering Operators

```typescript
import { of, interval } from 'rxjs';
import { filter, take, first, distinctUntilChanged } from 'rxjs/operators';

// filter: Keep values matching condition
of(1, 2, 3, 4, 5).pipe(
    filter(x => x > 2)
).subscribe(console.log);  // 3, 4, 5

// take: Take first N values then complete
interval(1000).pipe(
    take(3)
).subscribe(console.log);  // 0, 1, 2 (then completes)

// first: Take only first value
of(1, 2, 3).pipe(
    first()
).subscribe(console.log);  // 1

// distinctUntilChanged: Emit only when value changes
of(1, 1, 2, 2, 3, 3).pipe(
    distinctUntilChanged()
).subscribe(console.log);  // 1, 2, 3
```

#### Error Handling Operators

```typescript
import { of, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

// catchError: Handle errors gracefully
const risky$ = throwError(() => new Error('Something went wrong'));

risky$.pipe(
    catchError(error => {
        console.error('Caught:', error.message);
        return of('Fallback value');  // Return fallback observable
    })
).subscribe(console.log);  // 'Fallback value'

// retry: Retry failed observable N times
httpRequest$.pipe(
    retry(3),  // Retry up to 3 times
    catchError(error => of('Failed after 3 retries'))
).subscribe();
```

#### Combination Operators

```typescript
import { of, forkJoin, combineLatest } from 'rxjs';
import { switchMap, mergeMap, concatMap } from 'rxjs/operators';

// switchMap: Cancel previous, switch to new (use for search/autocomplete)
searchInput$.pipe(
    switchMap(term => this.searchService.search(term))
).subscribe(results => console.log(results));

// mergeMap: Run all in parallel (use when order doesn't matter)
ids$.pipe(
    mergeMap(id => this.http.get(`/api/users/${id}`))
).subscribe();

// concatMap: Run sequentially (use when order matters)
ids$.pipe(
    concatMap(id => this.http.get(`/api/users/${id}`))
).subscribe();

// forkJoin: Wait for all to complete (like Promise.all)
forkJoin({
    users: this.http.get('/api/users'),
    products: this.http.get('/api/products')
}).subscribe(({ users, products }) => {
    console.log('Both loaded:', users, products);
});
```

---

## Subjects

Subjects are both Observable AND Observer - they can emit values and be subscribed to.

### Subject

```typescript
import { Subject } from 'rxjs';

const subject = new Subject<string>();

// Subscribe before emitting
subject.subscribe(value => console.log('A:', value));
subject.subscribe(value => console.log('B:', value));

// Emit values
subject.next('Hello');
subject.next('World');

// Output:
// A: Hello
// B: Hello
// A: World
// B: World
```

### BehaviorSubject

BehaviorSubject stores the current value - new subscribers get the last emitted value immediately:

```typescript
import { BehaviorSubject } from 'rxjs';

// Must provide initial value
const state$ = new BehaviorSubject<string>('initial');

// Get current value synchronously
console.log(state$.getValue());  // 'initial'

state$.next('updated');

// New subscriber gets last value immediately
state$.subscribe(value => console.log('Got:', value));
// Output: Got: updated
```

### Using Subjects in Angular Services

```typescript
// state.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

interface AppState {
    user: string | null;
    isLoggedIn: boolean;
}

@Injectable({ providedIn: 'root' })
export class StateService {
    private state$ = new BehaviorSubject<AppState>({
        user: null,
        isLoggedIn: false
    });

    // Expose as Observable (read-only)
    getState(): Observable<AppState> {
        return this.state$.asObservable();
    }

    // Update state
    login(username: string): void {
        this.state$.next({
            user: username,
            isLoggedIn: true
        });
    }

    logout(): void {
        this.state$.next({
            user: null,
            isLoggedIn: false
        });
    }
}
```

---

## Unsubscribing

Always unsubscribe to prevent memory leaks:

### Manual Unsubscribe

```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({ ... })
export class MyComponent implements OnInit, OnDestroy {
    private subscription!: Subscription;

    ngOnInit(): void {
        this.subscription = this.dataService.getData()
            .subscribe(data => console.log(data));
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }
}
```

### Using takeUntil (Recommended)

```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({ ... })
export class MyComponent implements OnInit, OnDestroy {
    private destroy$ = new Subject<void>();

    ngOnInit(): void {
        // All subscriptions automatically cleaned up
        this.dataService.getData()
            .pipe(takeUntil(this.destroy$))
            .subscribe(data => console.log(data));

        this.userService.getUser()
            .pipe(takeUntil(this.destroy$))
            .subscribe(user => console.log(user));
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
```

### Using Async Pipe (Best for Templates)

The async pipe subscribes and unsubscribes automatically:

```typescript
@Component({
    template: `
        <div *ngIf="users$ | async as users">
            <div *ngFor="let user of users">{{ user.name }}</div>
        </div>
    `
})
export class UserListComponent {
    users$ = this.userService.getUsers();

    constructor(private userService: UserService) {}
}
```

---

## Common Patterns in Angular

### HTTP Request with Loading State

```typescript
import { Component } from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, catchError, finalize } from 'rxjs/operators';

@Component({
    template: `
        <div *ngIf="loading$ | async">Loading...</div>
        <div *ngIf="error$ | async as error" class="error">{{ error }}</div>
        <div *ngFor="let user of users$ | async">{{ user.name }}</div>
    `
})
export class UsersComponent {
    users$!: Observable<User[]>;
    loading$ = new BehaviorSubject<boolean>(false);
    error$ = new BehaviorSubject<string>('');

    constructor(private http: HttpClient) {
        this.loadUsers();
    }

    loadUsers(): void {
        this.loading$.next(true);
        this.error$.next('');

        this.users$ = this.http.get<User[]>('/api/users').pipe(
            catchError(err => {
                this.error$.next('Failed to load users');
                return of([]);
            }),
            finalize(() => this.loading$.next(false))
        );
    }
}
```

### Debounced Search

```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs/operators';

@Component({
    template: `
        <input [formControl]="searchControl" placeholder="Search...">
        <div *ngFor="let result of results">{{ result.name }}</div>
    `
})
export class SearchComponent implements OnInit, OnDestroy {
    searchControl = new FormControl('');
    results: any[] = [];
    private destroy$ = new Subject<void>();

    constructor(private searchService: SearchService) {}

    ngOnInit(): void {
        this.searchControl.valueChanges.pipe(
            debounceTime(300),        // Wait 300ms after typing stops
            distinctUntilChanged(),   // Only if value changed
            switchMap(term =>         // Cancel previous request
                term ? this.searchService.search(term) : of([])
            ),
            takeUntil(this.destroy$)
        ).subscribe(results => this.results = results);
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}
```

---

## Summary

| Concept | Purpose | Example |
|---------|---------|---------|
| Observable | Stream of async data | `http.get()` returns Observable |
| subscribe() | Execute observable | `.subscribe(data => ...)` |
| pipe() | Chain operators | `.pipe(map(), filter())` |
| map | Transform values | `map(x => x * 2)` |
| filter | Keep matching values | `filter(x => x > 0)` |
| catchError | Handle errors | `catchError(err => of([]))` |
| switchMap | Cancel & switch | Search autocomplete |
| takeUntil | Auto unsubscribe | `takeUntil(destroy$)` |
| Subject | Emit & subscribe | Event bus |
| BehaviorSubject | Has current value | State management |
| async pipe | Auto subscribe in template | `users$ \| async` |

## Next Topic

Continue to [HTTP Client](./08-http-client.md) to make API requests using RxJS observables.
