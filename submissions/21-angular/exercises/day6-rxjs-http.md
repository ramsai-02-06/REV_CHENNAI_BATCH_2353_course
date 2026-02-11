# Day 6: RxJS & HTTP Client

## Learning Goals
- Observables and subscriptions
- RxJS operators (map, filter, switchMap, catchError)
- Subjects and BehaviorSubject
- HttpClient for API calls
- Error handling
- HTTP interceptors

## Prerequisites
- Completed Day 1-5 exercises
- Routes and navigation working

---

## Exercise 6.1: Convert TaskService to Observable-Based

### Task
Refactor TaskService to use Observables instead of direct array access.

### Update TaskService
```typescript
// task.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private tasks: Task[] = [
    // ... initial tasks
  ];

  private tasksSubject = new BehaviorSubject<Task[]>(this.tasks);
  private nextId = 5;

  // Expose as Observable (read-only)
  tasks$: Observable<Task[]> = this.tasksSubject.asObservable();

  // Derived observables
  completedTasks$ = this.tasks$.pipe(
    map(tasks => tasks.filter(t => t.completed))
  );

  pendingTasks$ = this.tasks$.pipe(
    map(tasks => tasks.filter(t => !t.completed))
  );

  taskStats$ = this.tasks$.pipe(
    map(tasks => ({
      total: tasks.length,
      completed: tasks.filter(t => t.completed).length,
      pending: tasks.filter(t => !t.completed).length
    }))
  );

  getTask(id: number): Observable<Task | undefined> {
    return this.tasks$.pipe(
      map(tasks => tasks.find(t => t.id === id))
    );
  }

  addTask(task: Omit<Task, 'id' | 'createdAt'>): void {
    const newTask: Task = {
      ...task,
      id: this.nextId++,
      createdAt: new Date()
    };
    this.tasks = [...this.tasks, newTask];
    this.tasksSubject.next(this.tasks);
  }

  updateTask(id: number, updates: Partial<Task>): void {
    this.tasks = this.tasks.map(t =>
      t.id === id ? { ...t, ...updates } : t
    );
    this.tasksSubject.next(this.tasks);
  }

  deleteTask(id: number): void {
    this.tasks = this.tasks.filter(t => t.id !== id);
    this.tasksSubject.next(this.tasks);
  }

  completeTask(id: number): void {
    this.updateTask(id, { completed: true });
  }
}
```

---

## Exercise 6.2: Use Async Pipe in Components

### Update TaskList to Use Observable
```typescript
// task-list-page.component.ts
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list-page',
  templateUrl: './task-list-page.component.html'
})
export class TaskListPageComponent {
  tasks$: Observable<Task[]>;
  searchTerm = '';

  constructor(private taskService: TaskService) {
    this.tasks$ = this.taskService.tasks$;
  }
}
```

### Template with Async Pipe
```html
<!-- task-list-page.component.html -->
<div class="task-list-page">
  <h2>All Tasks</h2>

  <div class="filters">
    <input [(ngModel)]="searchTerm" placeholder="Search tasks...">
  </div>

  <!-- Using async pipe - no manual subscribe needed! -->
  <div *ngIf="tasks$ | async as tasks; else loading">
    <div *ngIf="tasks.length === 0" class="empty">
      No tasks found.
    </div>

    <div *ngFor="let task of tasks | filterTasks:searchTerm:true"
         class="task-item">
      <app-task-card
        [task]="task"
        (completed)="onComplete($event)"
        (deleted)="onDelete($event)">
      </app-task-card>
    </div>

    <p class="count">Showing {{ (tasks | filterTasks:searchTerm:true).length }} tasks</p>
  </div>

  <ng-template #loading>
    <p>Loading tasks...</p>
  </ng-template>
</div>
```

### Update TaskStats
```typescript
// task-stats.component.ts
import { Component } from '@angular/core';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-stats',
  template: `
    <div class="stats" *ngIf="taskService.taskStats$ | async as stats">
      <div class="stat">Total: {{ stats.total }}</div>
      <div class="stat">Completed: {{ stats.completed }}</div>
      <div class="stat">Pending: {{ stats.pending }}</div>
    </div>
  `
})
export class TaskStatsComponent {
  constructor(public taskService: TaskService) {}
}
```

---

## Exercise 6.3: Setup HTTP Client

### Configure HttpClientModule
```typescript
// app.module.ts
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule  // Add this
  ],
  // ...
})
export class AppModule { }
```

### Create API Service
```typescript
// api.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  // Using JSONPlaceholder for demo (todos endpoint)
  private apiUrl = 'https://jsonplaceholder.typicode.com/todos';

  constructor(private http: HttpClient) {}

  getTasks(): Observable<Task[]> {
    return this.http.get<any[]>(this.apiUrl).pipe(
      map(todos => todos.slice(0, 10).map(this.mapTodoToTask)),
      tap(tasks => console.log('Fetched tasks:', tasks)),
      catchError(this.handleError)
    );
  }

  getTask(id: number): Observable<Task> {
    return this.http.get<any>(`${this.apiUrl}/${id}`).pipe(
      map(this.mapTodoToTask),
      catchError(this.handleError)
    );
  }

  createTask(task: Partial<Task>): Observable<Task> {
    return this.http.post<any>(this.apiUrl, {
      title: task.title,
      completed: task.completed || false,
      userId: 1
    }).pipe(
      map(this.mapTodoToTask),
      catchError(this.handleError)
    );
  }

  updateTask(id: number, updates: Partial<Task>): Observable<Task> {
    return this.http.patch<any>(`${this.apiUrl}/${id}`, updates).pipe(
      map(this.mapTodoToTask),
      catchError(this.handleError)
    );
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // Map JSONPlaceholder todo to our Task model
  private mapTodoToTask(todo: any): Task {
    return {
      id: todo.id,
      title: todo.title,
      description: '',
      priority: 'medium',
      completed: todo.completed,
      createdAt: new Date(),
      estimatedHours: 1
    };
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';

    if (error.status === 0) {
      errorMessage = 'Unable to connect to server';
    } else if (error.status === 404) {
      errorMessage = 'Resource not found';
    } else if (error.status >= 500) {
      errorMessage = 'Server error';
    }

    console.error('API Error:', error);
    return throwError(() => new Error(errorMessage));
  }
}
```

---

## Exercise 6.4: Use HTTP in Component

### Create API-Connected Task List
```typescript
// api-task-list.component.ts
import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { Task } from '../../models/task.model';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-api-task-list',
  templateUrl: './api-task-list.component.html'
})
export class ApiTaskListComponent implements OnInit {
  tasks$!: Observable<Task[]>;
  loading = false;
  error = '';

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.loading = true;
    this.error = '';

    this.tasks$ = this.apiService.getTasks().pipe(
      catchError(err => {
        this.error = err.message;
        return of([]);  // Return empty array on error
      }),
      finalize(() => this.loading = false)
    );
  }

  onDelete(id: number): void {
    this.apiService.deleteTask(id).subscribe({
      next: () => {
        console.log('Task deleted');
        this.loadTasks();  // Refresh list
      },
      error: err => {
        this.error = 'Failed to delete task';
      }
    });
  }

  onComplete(id: number): void {
    this.apiService.updateTask(id, { completed: true }).subscribe({
      next: () => {
        console.log('Task completed');
        this.loadTasks();
      },
      error: err => {
        this.error = 'Failed to update task';
      }
    });
  }
}
```

### Template
```html
<!-- api-task-list.component.html -->
<div class="api-task-list">
  <h2>Tasks from API</h2>

  <div *ngIf="loading" class="loading">
    <div class="spinner"></div>
    Loading tasks...
  </div>

  <div *ngIf="error" class="error-message">
    {{ error }}
    <button (click)="loadTasks()">Retry</button>
  </div>

  <div *ngIf="tasks$ | async as tasks">
    <div *ngFor="let task of tasks" class="task-item">
      <span [class.completed]="task.completed">{{ task.title }}</span>
      <button *ngIf="!task.completed" (click)="onComplete(task.id)">✓</button>
      <button (click)="onDelete(task.id)">🗑</button>
    </div>
  </div>
</div>
```

---

## Exercise 6.5: HTTP Interceptor

### Create Auth Interceptor
```bash
ng g interceptor interceptors/auth
```

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

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    // Clone request and add auth header
    const authReq = request.clone({
      setHeaders: {
        Authorization: 'Bearer fake-jwt-token',
        'Content-Type': 'application/json'
      }
    });

    console.log('Intercepted request:', authReq.url);
    return next.handle(authReq);
  }
}
```

### Create Logging Interceptor
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
    const started = Date.now();
    let status = '';

    return next.handle(request).pipe(
      tap({
        next: event => {
          if (event instanceof HttpResponse) {
            status = 'succeeded';
          }
        },
        error: error => {
          status = 'failed';
        }
      }),
      finalize(() => {
        const elapsed = Date.now() - started;
        console.log(`${request.method} ${request.url} ${status} in ${elapsed}ms`);
      })
    );
  }
}
```

### Register Interceptors
```typescript
// app.module.ts
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { LoggingInterceptor } from './interceptors/logging.interceptor';

@NgModule({
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoggingInterceptor, multi: true }
  ]
})
export class AppModule { }
```

---

## Exercise 6.6: RxJS Operators Practice

### Create Search with Debounce
```typescript
// search.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap, catchError } from 'rxjs/operators';
import { ApiService } from '../../services/api.service';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-search',
  template: `
    <div class="search-container">
      <input
        type="text"
        placeholder="Search tasks..."
        (input)="onSearch($event)">

      <div *ngIf="loading" class="loading">Searching...</div>

      <div class="results">
        <div *ngFor="let task of results" class="result-item">
          {{ task.title }}
        </div>
      </div>
    </div>
  `
})
export class SearchComponent implements OnInit, OnDestroy {
  private searchSubject = new Subject<string>();
  private subscription!: Subscription;

  results: Task[] = [];
  loading = false;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.subscription = this.searchSubject.pipe(
      debounceTime(300),           // Wait 300ms after user stops typing
      distinctUntilChanged(),       // Only if value changed
      switchMap(term => {           // Cancel previous request
        this.loading = true;
        return this.apiService.getTasks().pipe(
          catchError(() => [])
        );
      })
    ).subscribe(tasks => {
      this.loading = false;
      this.results = tasks;
    });
  }

  onSearch(event: Event): void {
    const term = (event.target as HTMLInputElement).value;
    this.searchSubject.next(term);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
```

---

## Submission Checklist

- [ ] TaskService converted to use BehaviorSubject
- [ ] Components use async pipe for subscriptions
- [ ] HttpClientModule imported
- [ ] ApiService makes HTTP requests
- [ ] Error handling with catchError
- [ ] Loading states implemented
- [ ] Auth interceptor adds headers
- [ ] Logging interceptor tracks requests
- [ ] Search with debounce working

## Key RxJS Operators Used

| Operator | Purpose |
|----------|---------|
| `map` | Transform values |
| `filter` | Filter values |
| `tap` | Side effects (logging) |
| `catchError` | Handle errors |
| `switchMap` | Cancel previous, switch to new |
| `debounceTime` | Wait for pause |
| `distinctUntilChanged` | Only emit if changed |
| `finalize` | Run on complete/error |

---

## Bonus Challenge
1. Implement retry logic with `retry(3)` operator
2. Add a loading spinner component that shows during HTTP requests
3. Cache HTTP responses using `shareReplay(1)`
