# Day 4: Services & Component Communication

## Learning Goals
- `@Input()` - Parent to child communication
- `@Output()` with EventEmitter - Child to parent
- `@ViewChild` - Direct child access
- Services and Dependency Injection
- Shared state via services

## Prerequisites
- Completed Day 1-3 exercises
- Task list with filtering working

---

## Exercise 4.1: @Input - Parent to Child

### Task
Refactor TaskCard to receive task via `@Input`.

### Update TaskCard Component
```typescript
// task-card.component.ts
import { Component, Input } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-card',
  templateUrl: './task-card.component.html',
  styleUrls: ['./task-card.component.css']
})
export class TaskCardComponent {
  @Input() task!: Task;
  @Input() showDescription: boolean = true;
}
```

### Template
```html
<!-- task-card.component.html -->
<div class="task-card" [class.completed]="task.completed">
  <div class="task-header">
    <h4>{{ task.title | titlecase }}</h4>
    <app-priority-badge [priority]="task.priority"></app-priority-badge>
  </div>

  <p *ngIf="showDescription && task.description" class="description">
    {{ task.description }}
  </p>

  <div class="task-footer">
    <span class="date">{{ task.createdAt | timeAgo }}</span>
    <span class="status">{{ task.completed ? 'Completed' : 'Pending' }}</span>
  </div>
</div>
```

### Use in TaskList
```html
<!-- task-list.component.html -->
<div class="task-grid">
  <app-task-card
    *ngFor="let task of tasks | filterTasks:searchTerm:showCompleted"
    [task]="task"
    [showDescription]="true">
  </app-task-card>
</div>
```

---

## Exercise 4.2: @Output - Child to Parent

### Task
Add actions to TaskCard that notify the parent.

### Update TaskCard
```typescript
// task-card.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-card',
  templateUrl: './task-card.component.html'
})
export class TaskCardComponent {
  @Input() task!: Task;

  @Output() completed = new EventEmitter<number>();
  @Output() deleted = new EventEmitter<number>();
  @Output() edited = new EventEmitter<Task>();

  onComplete(): void {
    this.completed.emit(this.task.id);
  }

  onDelete(): void {
    if (confirm('Delete this task?')) {
      this.deleted.emit(this.task.id);
    }
  }

  onEdit(): void {
    this.edited.emit(this.task);
  }
}
```

### Add Buttons to Template
```html
<!-- task-card.component.html -->
<div class="task-card">
  <!-- ... existing content ... -->

  <div class="task-actions">
    <button *ngIf="!task.completed"
            (click)="onComplete()"
            class="btn-complete">
      ✓ Complete
    </button>
    <button (click)="onEdit()" class="btn-edit">Edit</button>
    <button (click)="onDelete()" class="btn-delete">Delete</button>
  </div>
</div>
```

### Handle Events in TaskList
```html
<!-- task-list.component.html -->
<app-task-card
  *ngFor="let task of tasks | filterTasks:searchTerm:showCompleted"
  [task]="task"
  (completed)="handleComplete($event)"
  (deleted)="handleDelete($event)"
  (edited)="handleEdit($event)">
</app-task-card>
```

```typescript
// task-list.component.ts
handleComplete(taskId: number): void {
  const task = this.tasks.find(t => t.id === taskId);
  if (task) {
    task.completed = true;
  }
}

handleDelete(taskId: number): void {
  this.tasks = this.tasks.filter(t => t.id !== taskId);
}

handleEdit(task: Task): void {
  console.log('Edit task:', task);
  // We'll implement edit modal later
}
```

---

## Exercise 4.3: Create TaskService

### Task
Move task data and operations to a service.

### Generate Service
```bash
ng g service services/task
```

### Implementation
```typescript
// task.service.ts
import { Injectable } from '@angular/core';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'  // Singleton for entire app
})
export class TaskService {
  private tasks: Task[] = [
    {
      id: 1,
      title: 'Setup Angular project',
      description: 'Initialize with CLI',
      priority: 'high',
      completed: true,
      createdAt: new Date('2024-01-15'),
      estimatedHours: 2
    },
    {
      id: 2,
      title: 'Learn Components',
      description: 'Create reusable components',
      priority: 'high',
      completed: true,
      createdAt: new Date('2024-01-16'),
      estimatedHours: 4
    },
    {
      id: 3,
      title: 'Master Directives',
      description: 'ngIf, ngFor, ngClass',
      priority: 'medium',
      completed: false,
      createdAt: new Date('2024-01-17'),
      estimatedHours: 3
    },
    {
      id: 4,
      title: 'Implement Services',
      description: 'TaskService with DI',
      priority: 'medium',
      completed: false,
      createdAt: new Date(),
      estimatedHours: 2
    }
  ];

  private nextId = 5;

  getTasks(): Task[] {
    return [...this.tasks];  // Return copy to prevent mutation
  }

  getTask(id: number): Task | undefined {
    return this.tasks.find(t => t.id === id);
  }

  addTask(task: Omit<Task, 'id' | 'createdAt'>): Task {
    const newTask: Task = {
      ...task,
      id: this.nextId++,
      createdAt: new Date()
    };
    this.tasks.push(newTask);
    return newTask;
  }

  updateTask(id: number, updates: Partial<Task>): Task | undefined {
    const index = this.tasks.findIndex(t => t.id === id);
    if (index !== -1) {
      this.tasks[index] = { ...this.tasks[index], ...updates };
      return this.tasks[index];
    }
    return undefined;
  }

  deleteTask(id: number): boolean {
    const index = this.tasks.findIndex(t => t.id === id);
    if (index !== -1) {
      this.tasks.splice(index, 1);
      return true;
    }
    return false;
  }

  completeTask(id: number): Task | undefined {
    return this.updateTask(id, { completed: true });
  }

  getTaskStats(): { total: number; completed: number; pending: number } {
    const total = this.tasks.length;
    const completed = this.tasks.filter(t => t.completed).length;
    return { total, completed, pending: total - completed };
  }
}
```

---

## Exercise 4.4: Inject Service into Components

### Update TaskList Component
```typescript
// task-list.component.ts
import { Component, OnInit } from '@angular/core';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  searchTerm = '';
  showCompleted = true;

  // Inject service via constructor
  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.tasks = this.taskService.getTasks();
  }

  handleComplete(taskId: number): void {
    this.taskService.completeTask(taskId);
    this.loadTasks();
  }

  handleDelete(taskId: number): void {
    this.taskService.deleteTask(taskId);
    this.loadTasks();
  }

  handleEdit(task: Task): void {
    // TODO: Open edit modal
    console.log('Edit:', task);
  }

  trackByTaskId(index: number, task: Task): number {
    return task.id;
  }
}
```

### Update TaskStats Component
```typescript
// task-stats.component.ts
import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-stats',
  template: `
    <div class="stats-container">
      <div class="stat-card">
        <span class="stat-value">{{ stats.total }}</span>
        <span class="stat-label">Total Tasks</span>
      </div>
      <div class="stat-card completed">
        <span class="stat-value">{{ stats.completed }}</span>
        <span class="stat-label">Completed</span>
      </div>
      <div class="stat-card pending">
        <span class="stat-value">{{ stats.pending }}</span>
        <span class="stat-label">Pending</span>
      </div>
      <div class="stat-card">
        <span class="stat-value">{{ completionRate }}%</span>
        <span class="stat-label">Completion Rate</span>
      </div>
    </div>
  `,
  styleUrls: ['./task-stats.component.css']
})
export class TaskStatsComponent implements OnInit {
  stats = { total: 0, completed: 0, pending: 0 };

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.stats = this.taskService.getTaskStats();
  }

  get completionRate(): number {
    if (this.stats.total === 0) return 0;
    return Math.round((this.stats.completed / this.stats.total) * 100);
  }
}
```

---

## Exercise 4.5: Connect TaskForm to Service

### Update TaskForm
```typescript
// task-form.component.ts
import { Component, Output, EventEmitter } from '@angular/core';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html'
})
export class TaskFormComponent {
  @Output() taskAdded = new EventEmitter<Task>();

  task: Partial<Task> = {
    title: '',
    description: '',
    priority: 'medium',
    completed: false,
    estimatedHours: 1
  };

  constructor(private taskService: TaskService) {}

  onSubmit(): void {
    if (!this.task.title?.trim()) {
      alert('Title is required');
      return;
    }

    const newTask = this.taskService.addTask(this.task as Omit<Task, 'id' | 'createdAt'>);
    this.taskAdded.emit(newTask);
    this.resetForm();
  }

  resetForm(): void {
    this.task = {
      title: '',
      description: '',
      priority: 'medium',
      completed: false,
      estimatedHours: 1
    };
  }
}
```

### Handle in App Component
```html
<!-- app.component.html -->
<app-header></app-header>

<main class="container">
  <div class="layout">
    <aside>
      <app-task-form (taskAdded)="onTaskAdded()"></app-task-form>
      <app-task-stats></app-task-stats>
    </aside>

    <section>
      <app-task-list></app-task-list>
    </section>
  </div>
</main>

<app-footer></app-footer>
```

```typescript
// app.component.ts
import { Component, ViewChild } from '@angular/core';
import { TaskListComponent } from './components/task-list/task-list.component';
import { TaskStatsComponent } from './components/task-stats/task-stats.component';

@Component({...})
export class AppComponent {
  @ViewChild(TaskListComponent) taskList!: TaskListComponent;
  @ViewChild(TaskStatsComponent) taskStats!: TaskStatsComponent;

  onTaskAdded(): void {
    // Refresh both components
    this.taskList.loadTasks();
    this.taskStats.ngOnInit();
  }
}
```

---

## Exercise 4.6: @ViewChild Example

### Task
Use `@ViewChild` to focus the form input after adding a task.

### Update TaskForm
```html
<!-- task-form.component.html -->
<input #titleInput
       type="text"
       [(ngModel)]="task.title"
       name="title"
       placeholder="Task title">
```

```typescript
// task-form.component.ts
import { ViewChild, ElementRef } from '@angular/core';

export class TaskFormComponent {
  @ViewChild('titleInput') titleInput!: ElementRef<HTMLInputElement>;

  onSubmit(): void {
    // ... existing code ...
    this.resetForm();
    this.focusTitle();
  }

  focusTitle(): void {
    setTimeout(() => {
      this.titleInput.nativeElement.focus();
    });
  }
}
```

---

## Submission Checklist

- [ ] TaskCard receives task via @Input
- [ ] TaskCard emits events via @Output (complete, delete, edit)
- [ ] TaskService created with CRUD operations
- [ ] TaskService injected into TaskList
- [ ] TaskService injected into TaskStats
- [ ] TaskForm uses service to add tasks
- [ ] Stats update when tasks change
- [ ] @ViewChild used for focus management
- [ ] All components communicate via service

## Architecture Overview
```
┌────────────────────────────────────────────────────┐
│                  AppComponent                       │
│  ┌──────────────┐  ┌────────────────────────────┐  │
│  │  TaskForm    │  │       TaskList              │  │
│  │  ↓ addTask   │  │  ┌─────────────────────┐   │  │
│  │              │  │  │    TaskCard         │   │  │
│  └──────────────┘  │  │  @Input task        │   │  │
│         ↓          │  │  @Output completed  │   │  │
│  ┌──────────────┐  │  │  @Output deleted    │   │  │
│  │  TaskStats   │  │  └─────────────────────┘   │  │
│  │              │  └────────────────────────────┘  │
│  └──────────────┘                                  │
└────────────────────────────────────────────────────┘
           ↓               ↓              ↓
      ┌──────────────────────────────────────────┐
      │              TaskService                  │
      │  - tasks[]                               │
      │  - getTasks(), addTask(), deleteTask()   │
      └──────────────────────────────────────────┘
```

---

## Bonus Challenge
Implement an edit modal:
1. When "Edit" is clicked, show a modal with the task form pre-filled
2. Use a separate `EditTaskModalComponent`
3. Pass the task to edit via @Input
4. Emit the updated task via @Output
5. Update the task in the service
