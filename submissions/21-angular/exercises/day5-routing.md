# Day 5: Routing

## Learning Goals
- Configure routes
- Use `routerLink` for navigation
- Read route parameters
- Implement route guards
- Lazy load feature modules

## Prerequisites
- Completed Day 1-4 exercises
- TaskService with CRUD operations

---

## Exercise 5.1: Basic Route Configuration

### Task
Set up routes for the task tracker application.

### Update App Routing Module
```typescript
// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Import components
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { TaskListPageComponent } from './pages/task-list-page/task-list-page.component';
import { TaskDetailComponent } from './pages/task-detail/task-detail.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'tasks', component: TaskListPageComponent },
  { path: 'tasks/:id', component: TaskDetailComponent },
  { path: '**', component: NotFoundComponent }  // Wildcard - must be last
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

### Generate Page Components
```bash
ng g c pages/dashboard
ng g c pages/task-list-page
ng g c pages/task-detail
ng g c pages/not-found
```

---

## Exercise 5.2: Create Navigation

### Update Header Component
```html
<!-- header.component.html -->
<header class="app-header">
  <div class="logo">
    <h1>{{ appTitle }}</h1>
  </div>

  <nav class="main-nav">
    <a routerLink="/dashboard"
       routerLinkActive="active"
       [routerLinkActiveOptions]="{ exact: true }">
      Dashboard
    </a>
    <a routerLink="/tasks"
       routerLinkActive="active">
      Tasks
    </a>
  </nav>

  <div class="user-menu">
    <span>Welcome, User</span>
  </div>
</header>
```

### Add Styles
```css
/* header.component.css */
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  background: #1976d2;
  color: white;
  height: 64px;
}

.main-nav {
  display: flex;
  gap: 24px;
}

.main-nav a {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.2s;
}

.main-nav a:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.main-nav a.active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  font-weight: 500;
}
```

### Update App Component
```html
<!-- app.component.html -->
<app-header></app-header>

<main class="container">
  <router-outlet></router-outlet>
</main>

<app-footer></app-footer>
```

---

## Exercise 5.3: Dashboard Page

### Implementation
```typescript
// dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats = { total: 0, completed: 0, pending: 0 };
  recentTasks: any[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.stats = this.taskService.getTaskStats();
    this.recentTasks = this.taskService.getTasks()
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      .slice(0, 5);
  }
}
```

```html
<!-- dashboard.component.html -->
<div class="dashboard">
  <h2>Dashboard</h2>

  <div class="stats-row">
    <div class="stat-card">
      <span class="value">{{ stats.total }}</span>
      <span class="label">Total Tasks</span>
    </div>
    <div class="stat-card success">
      <span class="value">{{ stats.completed }}</span>
      <span class="label">Completed</span>
    </div>
    <div class="stat-card warning">
      <span class="value">{{ stats.pending }}</span>
      <span class="label">Pending</span>
    </div>
  </div>

  <section class="recent-tasks">
    <h3>Recent Tasks</h3>
    <div class="task-list">
      <a *ngFor="let task of recentTasks"
         [routerLink]="['/tasks', task.id]"
         class="task-link">
        <span class="title">{{ task.title }}</span>
        <span class="status" [class.completed]="task.completed">
          {{ task.completed ? '✓' : '○' }}
        </span>
      </a>
    </div>
    <a routerLink="/tasks" class="view-all">View All Tasks →</a>
  </section>
</div>
```

---

## Exercise 5.4: Route Parameters

### Task Detail Component
```typescript
// task-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-detail',
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.css']
})
export class TaskDetailComponent implements OnInit {
  task: Task | undefined;
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    // Method 1: Snapshot (one-time read)
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTask(id);

    // Method 2: Observable (for when params change without reload)
    // this.route.paramMap.subscribe(params => {
    //   const id = Number(params.get('id'));
    //   this.loadTask(id);
    // });
  }

  loadTask(id: number): void {
    this.loading = true;
    this.task = this.taskService.getTask(id);
    this.loading = false;

    if (!this.task) {
      // Task not found - redirect to 404
      this.router.navigate(['/not-found']);
    }
  }

  toggleComplete(): void {
    if (this.task) {
      this.taskService.updateTask(this.task.id, {
        completed: !this.task.completed
      });
      this.task = this.taskService.getTask(this.task.id);
    }
  }

  deleteTask(): void {
    if (this.task && confirm('Delete this task?')) {
      this.taskService.deleteTask(this.task.id);
      this.router.navigate(['/tasks']);
    }
  }

  goBack(): void {
    this.router.navigate(['/tasks']);
  }
}
```

```html
<!-- task-detail.component.html -->
<div class="task-detail" *ngIf="task; else notFound">
  <div class="detail-header">
    <button (click)="goBack()" class="btn-back">← Back to Tasks</button>
    <h2>{{ task.title }}</h2>
  </div>

  <div class="detail-card">
    <div class="detail-row">
      <label>Status</label>
      <span [class.completed]="task.completed">
        {{ task.completed ? 'Completed' : 'Pending' }}
      </span>
    </div>

    <div class="detail-row">
      <label>Priority</label>
      <app-priority-badge [priority]="task.priority"></app-priority-badge>
    </div>

    <div class="detail-row">
      <label>Description</label>
      <p>{{ task.description || 'No description' }}</p>
    </div>

    <div class="detail-row">
      <label>Created</label>
      <span>{{ task.createdAt | date:'medium' }}</span>
    </div>

    <div class="detail-row">
      <label>Estimated Hours</label>
      <span>{{ task.estimatedHours }}h</span>
    </div>
  </div>

  <div class="detail-actions">
    <button (click)="toggleComplete()" class="btn-primary">
      {{ task.completed ? 'Reopen Task' : 'Mark Complete' }}
    </button>
    <button [routerLink]="['/tasks', task.id, 'edit']" class="btn-secondary">
      Edit
    </button>
    <button (click)="deleteTask()" class="btn-danger">
      Delete
    </button>
  </div>
</div>

<ng-template #notFound>
  <div class="not-found">
    <h2>Task Not Found</h2>
    <p>The task you're looking for doesn't exist.</p>
    <a routerLink="/tasks">Back to Tasks</a>
  </div>
</ng-template>
```

---

## Exercise 5.5: Programmatic Navigation

### Update Task List Page
```typescript
// task-list-page.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list-page',
  templateUrl: './task-list-page.component.html'
})
export class TaskListPageComponent implements OnInit {
  tasks: Task[] = [];
  searchTerm = '';
  filterPriority = '';

  constructor(
    private taskService: TaskService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.tasks = this.taskService.getTasks();
  }

  viewTask(task: Task): void {
    // Programmatic navigation
    this.router.navigate(['/tasks', task.id]);
  }

  createTask(): void {
    this.router.navigate(['/tasks', 'new']);
  }

  // Navigate with query params
  filterByPriority(priority: string): void {
    this.router.navigate(['/tasks'], {
      queryParams: { priority }
    });
  }
}
```

---

## Exercise 5.6: Route Guard (CanActivate)

### Create Auth Service
```bash
ng g service services/auth
```

```typescript
// auth.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticated = true;  // Simulated

  isLoggedIn(): boolean {
    return this.isAuthenticated;
  }

  login(): void {
    this.isAuthenticated = true;
  }

  logout(): void {
    this.isAuthenticated = false;
  }
}
```

### Create Auth Guard
```bash
ng g guard guards/auth
```

```typescript
// auth.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  // Redirect to login with return URL
  return router.createUrlTree(['/login'], {
    queryParams: { returnUrl: state.url }
  });
};
```

### Apply Guard to Routes
```typescript
// app-routing.module.ts
const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tasks',
    component: TaskListPageComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tasks/:id',
    component: TaskDetailComponent,
    canActivate: [authGuard]
  },
  { path: '**', component: NotFoundComponent }
];
```

---

## Exercise 5.7: Lazy Loading (Optional Advanced)

### Create Tasks Feature Module
```bash
ng g module features/tasks --route tasks --module app.module
```

This creates:
- `features/tasks/tasks.module.ts`
- `features/tasks/tasks-routing.module.ts`
- `features/tasks/tasks.component.ts`

### Update App Routing for Lazy Load
```typescript
// app-routing.module.ts
const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  {
    path: 'tasks',
    loadChildren: () => import('./features/tasks/tasks.module')
      .then(m => m.TasksModule)
  },
  { path: '**', component: NotFoundComponent }
];
```

### Tasks Feature Routing
```typescript
// features/tasks/tasks-routing.module.ts
const routes: Routes = [
  { path: '', component: TaskListPageComponent },
  { path: ':id', component: TaskDetailComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],  // forChild for feature modules
  exports: [RouterModule]
})
export class TasksRoutingModule { }
```

---

## Submission Checklist

- [ ] Routes configured for dashboard, tasks, task detail
- [ ] Navigation with `routerLink` working
- [ ] `routerLinkActive` highlights current route
- [ ] Route parameters read in TaskDetailComponent
- [ ] Programmatic navigation with `Router.navigate()`
- [ ] 404 page for unknown routes
- [ ] Auth guard protects routes
- [ ] (Bonus) Lazy loading implemented

## Route Structure
```
/                   → Redirects to /dashboard
/dashboard          → DashboardComponent
/tasks              → TaskListPageComponent
/tasks/:id          → TaskDetailComponent
/tasks/new          → (Future) CreateTaskComponent
/login              → LoginComponent
/**                 → NotFoundComponent
```

---

## Bonus Challenge
1. Add a "Back" button that uses `Location.back()` from `@angular/common`
2. Implement breadcrumbs using route data
3. Add route transitions/animations
