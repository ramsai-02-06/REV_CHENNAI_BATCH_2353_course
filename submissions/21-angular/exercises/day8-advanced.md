# Day 8: Advanced Topics

## Learning Goals
- Implement and understand component lifecycle hooks
- Apply change detection strategies for performance
- Create dynamic components programmatically
- Organize code with Feature, Shared, and Core modules

## Prerequisites
- Completed Day 1-7 exercises
- Task Tracker application with forms working

---

## Exercise 8.1: Lifecycle Hooks Demo

### Task
Create a component that demonstrates all lifecycle hooks with console logging.

### Generate Component
```bash
ng g c components/lifecycle-demo
```

### Implementation
```typescript
// lifecycle-demo.component.ts
import {
  Component,
  Input,
  OnInit,
  OnChanges,
  DoCheck,
  AfterContentInit,
  AfterContentChecked,
  AfterViewInit,
  AfterViewChecked,
  OnDestroy,
  SimpleChanges
} from '@angular/core';

@Component({
  selector: 'app-lifecycle-demo',
  template: `
    <div class="lifecycle-demo">
      <h3>Lifecycle Demo: {{ name }}</h3>
      <p>Counter: {{ counter }}</p>
      <ng-content></ng-content>
      <button (click)="increment()">Increment</button>
    </div>
  `,
  styles: [`
    .lifecycle-demo {
      border: 2px solid #1976d2;
      padding: 16px;
      margin: 16px 0;
      border-radius: 8px;
    }
  `]
})
export class LifecycleDemoComponent implements
  OnInit, OnChanges, DoCheck, AfterContentInit,
  AfterContentChecked, AfterViewInit, AfterViewChecked, OnDestroy {

  @Input() name = 'Demo';
  counter = 0;

  constructor() {
    console.log('1. constructor - component instance created');
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('2. ngOnChanges - @Input changed:', changes);
  }

  ngOnInit(): void {
    console.log('3. ngOnInit - component initialized');
  }

  ngDoCheck(): void {
    console.log('4. ngDoCheck - change detection running');
  }

  ngAfterContentInit(): void {
    console.log('5. ngAfterContentInit - projected content ready');
  }

  ngAfterContentChecked(): void {
    console.log('6. ngAfterContentChecked - projected content checked');
  }

  ngAfterViewInit(): void {
    console.log('7. ngAfterViewInit - view and children ready');
  }

  ngAfterViewChecked(): void {
    console.log('8. ngAfterViewChecked - view and children checked');
  }

  ngOnDestroy(): void {
    console.log('9. ngOnDestroy - component being destroyed');
  }

  increment(): void {
    this.counter++;
  }
}
```

### Use in Parent Component
```html
<!-- Add to task-list-page or create a demo page -->
<div class="lifecycle-test">
  <h2>Lifecycle Demo Test</h2>

  <button (click)="showDemo = !showDemo">
    {{ showDemo ? 'Hide' : 'Show' }} Demo
  </button>

  <button (click)="demoName = demoName === 'Demo A' ? 'Demo B' : 'Demo A'">
    Change Name
  </button>

  <app-lifecycle-demo *ngIf="showDemo" [name]="demoName">
    <p>This is projected content!</p>
  </app-lifecycle-demo>
</div>
```

```typescript
// Add to component
showDemo = true;
demoName = 'Demo A';
```

### Questions
1. Which hooks run when the component first loads?
2. Which hooks run when you click "Increment"?
3. Which hook runs when you click "Hide Demo"?
4. Which hooks run when you change the name?

---

## Exercise 8.2: Practical ngOnChanges - Task Card

### Task
Update TaskCard to respond to @Input changes using ngOnChanges.

### Update TaskCard
```typescript
// task-card.component.ts
import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-card',
  templateUrl: './task-card.component.html'
})
export class TaskCardComponent implements OnChanges {
  @Input() task!: Task;
  @Input() highlightPriority = false;

  previousPriority: string = '';
  priorityChanged = false;

  ngOnChanges(changes: SimpleChanges): void {
    // Track when task changes
    if (changes['task']) {
      const prev = changes['task'].previousValue;
      const curr = changes['task'].currentValue;

      if (prev && curr && prev.priority !== curr.priority) {
        this.previousPriority = prev.priority;
        this.priorityChanged = true;

        // Clear highlight after 2 seconds
        setTimeout(() => {
          this.priorityChanged = false;
        }, 2000);
      }
    }

    // Log first change vs subsequent changes
    if (changes['highlightPriority']) {
      if (changes['highlightPriority'].firstChange) {
        console.log('Initial highlightPriority:', changes['highlightPriority'].currentValue);
      } else {
        console.log('highlightPriority changed from',
          changes['highlightPriority'].previousValue,
          'to',
          changes['highlightPriority'].currentValue
        );
      }
    }
  }
}
```

### Update Template
```html
<!-- task-card.component.html -->
<div class="task-card"
     [class.completed]="task.completed"
     [class.priority-changed]="priorityChanged">

  <div *ngIf="priorityChanged" class="change-indicator">
    Priority changed from {{ previousPriority }}!
  </div>

  <!-- rest of template -->
</div>
```

---

## Exercise 8.3: ngOnDestroy - Cleanup Subscriptions

### Task
Add proper cleanup to components with subscriptions.

### Update TaskList with Cleanup
```typescript
// task-list-page.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list-page',
  templateUrl: './task-list-page.component.html'
})
export class TaskListPageComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    // Subscribe with automatic cleanup
    this.taskService.tasks$
      .pipe(takeUntil(this.destroy$))
      .subscribe(tasks => {
        console.log('Tasks updated:', tasks.length);
      });

    // Simulate polling that needs cleanup
    this.startAutoRefresh();
  }

  ngOnDestroy(): void {
    console.log('Component destroying - cleaning up...');
    this.destroy$.next();
    this.destroy$.complete();
  }

  private startAutoRefresh(): void {
    // This would leak without cleanup!
    // interval(30000)
    //   .pipe(takeUntil(this.destroy$))
    //   .subscribe(() => this.refreshTasks());
  }
}
```

### Add Timer Component with Cleanup
```bash
ng g c components/auto-save-indicator
```

```typescript
// auto-save-indicator.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-auto-save-indicator',
  template: `
    <div class="auto-save">
      <span class="dot" [class.saving]="isSaving"></span>
      {{ message }}
    </div>
  `,
  styles: [`
    .auto-save {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 0.875rem;
      color: #666;
    }
    .dot {
      width: 8px;
      height: 8px;
      background: #4caf50;
      border-radius: 50%;
    }
    .dot.saving {
      animation: pulse 1s infinite;
    }
    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.3; }
    }
  `]
})
export class AutoSaveIndicatorComponent implements OnInit, OnDestroy {
  message = 'Auto-saved';
  isSaving = false;
  private subscription?: Subscription;

  ngOnInit(): void {
    // Simulate auto-save every 30 seconds
    this.subscription = interval(30000).subscribe(() => {
      this.performAutoSave();
    });
  }

  ngOnDestroy(): void {
    // IMPORTANT: Unsubscribe to prevent memory leak
    this.subscription?.unsubscribe();
    console.log('AutoSaveIndicator cleaned up');
  }

  private performAutoSave(): void {
    this.isSaving = true;
    this.message = 'Saving...';

    setTimeout(() => {
      this.isSaving = false;
      this.message = `Auto-saved at ${new Date().toLocaleTimeString()}`;
    }, 1000);
  }
}
```

---

## Exercise 8.4: Change Detection - OnPush Strategy

### Task
Optimize TaskCard with OnPush change detection.

### Update TaskCard
```typescript
// task-card.component.ts
import { Component, Input, Output, EventEmitter, ChangeDetectionStrategy } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-card',
  templateUrl: './task-card.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush  // Enable OnPush
})
export class TaskCardComponent {
  @Input() task!: Task;
  @Output() completed = new EventEmitter<number>();
  @Output() deleted = new EventEmitter<number>();

  // With OnPush, component only updates when:
  // 1. @Input reference changes (not mutation)
  // 2. Event from this component or child
  // 3. Async pipe receives value
  // 4. Manual trigger with ChangeDetectorRef
}
```

### Update TaskList to Work with OnPush
```typescript
// task-list-page.component.ts

// WRONG - Mutating won't trigger OnPush child components
completeTaskWrong(id: number): void {
  const task = this.tasks.find(t => t.id === id);
  if (task) task.completed = true;  // Mutation - OnPush won't detect!
}

// CORRECT - Create new reference
completeTaskCorrect(id: number): void {
  this.tasks = this.tasks.map(task =>
    task.id === id ? { ...task, completed: true } : task
  );
  // New array reference triggers OnPush
}

// CORRECT - Create new task object
updateTask(id: number, updates: Partial<Task>): void {
  this.tasks = this.tasks.map(task =>
    task.id === id ? { ...task, ...updates } : task
  );
}
```

### Verify OnPush is Working
```typescript
// Add to TaskCardComponent temporarily
ngDoCheck(): void {
  console.log(`TaskCard ${this.task.id} checked`);
}
```

Open console and observe:
1. With default CD: ALL cards log on ANY change
2. With OnPush: Only affected card logs

---

## Exercise 8.5: Manual Change Detection

### Task
Create a component that uses manual change detection for external data.

### Create WebSocket Simulator
```typescript
// notification-panel.component.ts
import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';

interface Notification {
  id: number;
  message: string;
  timestamp: Date;
}

@Component({
  selector: 'app-notification-panel',
  template: `
    <div class="notification-panel">
      <h4>Notifications ({{ notifications.length }})</h4>
      <div *ngFor="let n of notifications" class="notification">
        <span>{{ n.message }}</span>
        <small>{{ n.timestamp | date:'shortTime' }}</small>
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NotificationPanelComponent implements OnInit, OnDestroy {
  notifications: Notification[] = [];
  private intervalId?: number;
  private nextId = 1;

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    // Simulate WebSocket messages (outside Angular zone)
    this.intervalId = window.setInterval(() => {
      this.notifications = [
        {
          id: this.nextId++,
          message: `New notification #${this.nextId}`,
          timestamp: new Date()
        },
        ...this.notifications.slice(0, 4)  // Keep last 5
      ];

      // Must manually trigger change detection!
      this.cdr.markForCheck();
    }, 5000);
  }

  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
```

### Questions
1. What happens if you remove `markForCheck()`?
2. What's the difference between `markForCheck()` and `detectChanges()`?
3. When would you use `detach()` and `reattach()`?

---

## Exercise 8.6: Dynamic Component - Toast Notifications

### Task
Create a toast notification system using dynamic components.

### Create Toast Component
```bash
ng g c components/toast
```

```typescript
// toast.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-toast',
  template: `
    <div class="toast toast-{{ type }}" [@fadeInOut]>
      <span class="icon">{{ icon }}</span>
      <span class="message">{{ message }}</span>
      <button (click)="close()" class="close-btn">&times;</button>
    </div>
  `,
  styles: [`
    .toast {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 16px;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      margin-bottom: 8px;
      animation: slideIn 0.3s ease;
    }
    .toast-success { background: #4caf50; color: white; }
    .toast-error { background: #f44336; color: white; }
    .toast-warning { background: #ff9800; color: white; }
    .toast-info { background: #2196f3; color: white; }
    .close-btn {
      background: none;
      border: none;
      color: inherit;
      font-size: 1.25rem;
      cursor: pointer;
      opacity: 0.7;
    }
    .close-btn:hover { opacity: 1; }
    @keyframes slideIn {
      from { transform: translateX(100%); opacity: 0; }
      to { transform: translateX(0); opacity: 1; }
    }
  `]
})
export class ToastComponent {
  @Input() message = '';
  @Input() type: 'success' | 'error' | 'warning' | 'info' = 'info';
  @Output() closed = new EventEmitter<void>();

  get icon(): string {
    const icons = { success: '✓', error: '✕', warning: '⚠', info: 'ℹ' };
    return icons[this.type];
  }

  close(): void {
    this.closed.emit();
  }
}
```

### Create Toast Service
```bash
ng g s services/toast
```

```typescript
// toast.service.ts
import {
  Injectable,
  ComponentRef,
  ViewContainerRef,
  ApplicationRef,
  createComponent,
  EnvironmentInjector
} from '@angular/core';
import { ToastComponent } from '../components/toast/toast.component';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private toastContainer?: ViewContainerRef;

  constructor(
    private appRef: ApplicationRef,
    private injector: EnvironmentInjector
  ) {}

  setContainer(container: ViewContainerRef): void {
    this.toastContainer = container;
  }

  show(
    message: string,
    type: 'success' | 'error' | 'warning' | 'info' = 'info',
    duration: number = 3000
  ): void {
    if (!this.toastContainer) {
      console.warn('Toast container not set');
      return;
    }

    // Create component dynamically
    const toastRef = this.toastContainer.createComponent(ToastComponent);

    // Set inputs
    toastRef.instance.message = message;
    toastRef.instance.type = type;

    // Handle close
    toastRef.instance.closed.subscribe(() => {
      toastRef.destroy();
    });

    // Auto-close after duration
    setTimeout(() => {
      if (toastRef.hostView && !toastRef.hostView.destroyed) {
        toastRef.destroy();
      }
    }, duration);
  }

  success(message: string): void {
    this.show(message, 'success');
  }

  error(message: string): void {
    this.show(message, 'error', 5000);
  }

  warning(message: string): void {
    this.show(message, 'warning');
  }

  info(message: string): void {
    this.show(message, 'info');
  }
}
```

### Setup Toast Container in App
```html
<!-- app.component.html -->
<app-header></app-header>

<main class="container">
  <router-outlet></router-outlet>
</main>

<app-footer></app-footer>

<!-- Toast container at bottom-right -->
<div class="toast-container" #toastContainer>
  <ng-container #toastOutlet></ng-container>
</div>
```

```typescript
// app.component.ts
import { Component, ViewChild, ViewContainerRef, AfterViewInit } from '@angular/core';
import { ToastService } from './services/toast.service';

@Component({...})
export class AppComponent implements AfterViewInit {
  @ViewChild('toastOutlet', { read: ViewContainerRef })
  toastOutlet!: ViewContainerRef;

  constructor(private toastService: ToastService) {}

  ngAfterViewInit(): void {
    this.toastService.setContainer(this.toastOutlet);
  }
}
```

```css
/* Add to global styles or app.component.css */
.toast-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}
```

### Use Toast in TaskForm
```typescript
// task-form.component.ts
constructor(
  private taskService: TaskService,
  private toastService: ToastService
) {}

onSubmit(): void {
  if (this.taskForm.invalid) return;

  this.taskService.addTask(this.taskForm.value);
  this.toastService.success('Task created successfully!');
  this.taskForm.reset();
}
```

---

## Exercise 8.7: Module Organization

### Task
Reorganize the Task Tracker into Feature, Shared, and Core modules.

### Create Shared Module
```bash
ng g m shared
```

```typescript
// shared/shared.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Shared Components
import { PriorityBadgeComponent } from './components/priority-badge/priority-badge.component';
import { LoadingSpinnerComponent } from './components/loading-spinner/loading-spinner.component';
import { ToastComponent } from './components/toast/toast.component';

// Shared Pipes
import { TimeAgoPipe } from './pipes/time-ago.pipe';
import { FilterTasksPipe } from './pipes/filter-tasks.pipe';

@NgModule({
  declarations: [
    PriorityBadgeComponent,
    LoadingSpinnerComponent,
    ToastComponent,
    TimeAgoPipe,
    FilterTasksPipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    // Re-export modules for convenience
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    // Export declarations
    PriorityBadgeComponent,
    LoadingSpinnerComponent,
    ToastComponent,
    TimeAgoPipe,
    FilterTasksPipe
  ]
})
export class SharedModule { }
```

### Create Core Module
```bash
ng g m core
```

```typescript
// core/core.module.ts
import { NgModule, Optional, SkipSelf } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { RouterModule } from '@angular/router';

// App-wide components
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';

// Interceptors
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { LoggingInterceptor } from './interceptors/logging.interceptor';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoggingInterceptor, multi: true }
  ]
})
export class CoreModule {
  // Prevent re-importing CoreModule
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('CoreModule is already loaded. Import it only in AppModule.');
    }
  }
}
```

### Create Tasks Feature Module
```bash
ng g m features/tasks --route tasks --module app
```

```typescript
// features/tasks/tasks.module.ts
import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { TasksRoutingModule } from './tasks-routing.module';

import { TaskListPageComponent } from './pages/task-list-page/task-list-page.component';
import { TaskDetailComponent } from './pages/task-detail/task-detail.component';
import { TaskCardComponent } from './components/task-card/task-card.component';
import { TaskFormComponent } from './components/task-form/task-form.component';

@NgModule({
  declarations: [
    TaskListPageComponent,
    TaskDetailComponent,
    TaskCardComponent,
    TaskFormComponent
  ],
  imports: [
    SharedModule,
    TasksRoutingModule
  ]
})
export class TasksModule { }
```

### Update App Module
```typescript
// app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { CoreModule } from './core/core.module';
import { SharedModule } from './shared/shared.module';

import { AppComponent } from './app.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,      // Import once in AppModule
    SharedModule     // Import in feature modules as needed
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

### Module Structure
```
task-tracker/
├── src/app/
│   ├── core/                      # Singleton services, app-wide components
│   │   ├── components/
│   │   │   ├── header/
│   │   │   └── footer/
│   │   ├── interceptors/
│   │   ├── guards/
│   │   └── core.module.ts
│   │
│   ├── shared/                    # Reusable components, pipes, directives
│   │   ├── components/
│   │   │   ├── priority-badge/
│   │   │   ├── loading-spinner/
│   │   │   └── toast/
│   │   ├── pipes/
│   │   └── shared.module.ts
│   │
│   ├── features/                  # Feature modules (lazy loaded)
│   │   └── tasks/
│   │       ├── components/
│   │       ├── pages/
│   │       ├── tasks-routing.module.ts
│   │       └── tasks.module.ts
│   │
│   ├── pages/                     # Non-feature pages
│   │   ├── dashboard/
│   │   └── not-found/
│   │
│   ├── app.component.ts
│   ├── app.module.ts
│   └── app-routing.module.ts
```

---

## Submission Checklist

- [ ] Lifecycle demo component created showing all hooks
- [ ] TaskCard uses ngOnChanges to track priority changes
- [ ] Subscriptions cleaned up in ngOnDestroy with Subject/takeUntil
- [ ] TaskCard uses ChangeDetectionStrategy.OnPush
- [ ] Task mutations use immutable patterns (spread operator)
- [ ] Notification panel uses manual change detection
- [ ] Toast notification system created with dynamic components
- [ ] ToastService creates components programmatically
- [ ] SharedModule created with common components/pipes
- [ ] CoreModule created with singleton services
- [ ] TasksModule created as feature module

## Key Concepts Summary

| Concept | Usage |
|---------|-------|
| `ngOnChanges` | React to @Input changes |
| `ngOnInit` | Initialize component, start subscriptions |
| `ngOnDestroy` | Cleanup subscriptions, timers |
| `OnPush` | Performance optimization |
| `markForCheck()` | Trigger check for OnPush |
| `ViewContainerRef` | Create dynamic components |
| `SharedModule` | Reusable UI components |
| `CoreModule` | Singleton services |
| `Feature Module` | Organize related functionality |

---

## Bonus Challenge

Create a modal service that:
1. Opens any component as a modal dynamically
2. Passes data to the modal component
3. Returns a result when the modal closes
4. Supports keyboard escape to close

```typescript
// Usage example
const result = await this.modalService.open(TaskEditComponent, { task });
if (result) {
  this.taskService.updateTask(result);
}
```
