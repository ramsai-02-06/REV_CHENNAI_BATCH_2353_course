# Day 3: Directives & Pipes

## Learning Goals
- Structural directives (`*ngIf`, `*ngFor`, `*ngSwitch`)
- Attribute directives (`ngClass`, `ngStyle`)
- Built-in pipes
- Custom pipes

## Prerequisites
- Completed Day 1-2 exercises
- Task model and components created

---

## Exercise 3.1: Display Task List with *ngFor

### Task
Display a list of tasks using `*ngFor`.

### Create TaskList Component
```bash
ng g c components/task-list
```

### Sample Data
```typescript
// task-list.component.ts
import { Component } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent {
  tasks: Task[] = [
    { id: 1, title: 'Setup Angular project', description: 'Initialize with CLI', priority: 'high', completed: true },
    { id: 2, title: 'Create components', description: 'Header, footer, task card', priority: 'high', completed: true },
    { id: 3, title: 'Learn directives', description: 'ngIf, ngFor, ngClass', priority: 'medium', completed: false },
    { id: 4, title: 'Implement pipes', description: 'Built-in and custom', priority: 'medium', completed: false },
    { id: 5, title: 'Add services', description: 'TaskService with DI', priority: 'low', completed: false },
    { id: 6, title: 'Setup routing', description: 'Multiple pages', priority: 'low', completed: false },
  ];
}
```

### Template with *ngFor
```html
<!-- task-list.component.html -->
<div class="task-list">
  <h2>My Tasks ({{ tasks.length }})</h2>

  <div class="task-item"
       *ngFor="let task of tasks; let i = index; let first = first; let last = last; let even = even"
       [class.first-item]="first"
       [class.last-item]="last"
       [class.even-row]="even">

    <span class="task-number">{{ i + 1 }}.</span>
    <span class="task-title">{{ task.title }}</span>
    <span class="task-priority">{{ task.priority }}</span>
    <span class="task-status">{{ task.completed ? '✓' : '○' }}</span>
  </div>
</div>
```

### Add trackBy for Performance
```html
<div *ngFor="let task of tasks; trackBy: trackByTaskId">
```

```typescript
trackByTaskId(index: number, task: Task): number {
  return task.id;
}
```

---

## Exercise 3.2: Conditional Rendering with *ngIf

### Task
Add conditional elements based on task state.

### Update Template
```html
<!-- task-list.component.html -->
<div class="task-list">
  <h2>My Tasks</h2>

  <!-- Show message if no tasks -->
  <div *ngIf="tasks.length === 0" class="empty-state">
    <p>No tasks yet. Add your first task!</p>
  </div>

  <!-- Show list if tasks exist -->
  <ng-container *ngIf="tasks.length > 0">
    <p class="summary">
      {{ getCompletedCount() }} of {{ tasks.length }} completed
    </p>

    <div class="task-item" *ngFor="let task of tasks; trackBy: trackByTaskId">
      <span class="task-title" [class.completed]="task.completed">
        {{ task.title }}
      </span>

      <!-- Show description only if exists -->
      <p *ngIf="task.description" class="task-description">
        {{ task.description }}
      </p>

      <!-- Conditional button text -->
      <button *ngIf="!task.completed; else completedTpl" (click)="complete(task)">
        Mark Complete
      </button>
      <ng-template #completedTpl>
        <button (click)="reopen(task)" class="btn-secondary">Reopen</button>
      </ng-template>
    </div>
  </ng-container>
</div>
```

### Add Methods
```typescript
getCompletedCount(): number {
  return this.tasks.filter(t => t.completed).length;
}

complete(task: Task): void {
  task.completed = true;
}

reopen(task: Task): void {
  task.completed = false;
}
```

---

## Exercise 3.3: *ngSwitch for Priority Badge

### Task
Use `*ngSwitch` to display different badge styles.

### Add Priority Badge Component
```bash
ng g c components/priority-badge
```

```typescript
// priority-badge.component.ts
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-priority-badge',
  template: `
    <span class="badge" [ngSwitch]="priority">
      <span *ngSwitchCase="'high'" class="badge-high">
        🔴 High Priority
      </span>
      <span *ngSwitchCase="'medium'" class="badge-medium">
        🟡 Medium
      </span>
      <span *ngSwitchCase="'low'" class="badge-low">
        🟢 Low
      </span>
      <span *ngSwitchDefault class="badge-default">
        ⚪ Unknown
      </span>
    </span>
  `,
  styles: [`
    .badge span {
      padding: 4px 8px;
      border-radius: 4px;
      font-size: 0.75rem;
    }
    .badge-high { background: #ffebee; }
    .badge-medium { background: #fff3e0; }
    .badge-low { background: #e8f5e9; }
  `]
})
export class PriorityBadgeComponent {
  @Input() priority: string = '';
}
```

### Use in Task List
```html
<app-priority-badge [priority]="task.priority"></app-priority-badge>
```

---

## Exercise 3.4: ngClass and ngStyle

### Task
Add dynamic styling based on task properties.

### Update Task Item
```html
<div class="task-item"
     *ngFor="let task of tasks"
     [ngClass]="{
       'completed': task.completed,
       'high-priority': task.priority === 'high',
       'overdue': isOverdue(task)
     }"
     [ngStyle]="{
       'border-left-color': getPriorityColor(task.priority),
       'opacity': task.completed ? 0.7 : 1
     }">
  <!-- content -->
</div>
```

### Add Helper Methods
```typescript
getPriorityColor(priority: string): string {
  const colors: Record<string, string> = {
    high: '#c62828',
    medium: '#ef6c00',
    low: '#2e7d32'
  };
  return colors[priority] || '#757575';
}

isOverdue(task: Task): boolean {
  // Mock implementation - would check due date
  return false;
}
```

### Styling
```css
.task-item {
  border-left: 4px solid transparent;
  padding: 12px;
  margin: 8px 0;
  background: white;
  border-radius: 4px;
  transition: all 0.2s;
}

.task-item.completed {
  text-decoration: line-through;
  color: #999;
}

.task-item.high-priority {
  background: #fff8f8;
}

.task-item:hover {
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
```

---

## Exercise 3.5: Built-in Pipes

### Task
Use built-in pipes to format task data.

### Update Task Model
```typescript
// Add to task.model.ts
export interface Task {
  id: number;
  title: string;
  description: string;
  priority: 'low' | 'medium' | 'high';
  completed: boolean;
  createdAt: Date;      // Add
  estimatedHours: number; // Add
}
```

### Update Sample Data
```typescript
tasks: Task[] = [
  {
    id: 1,
    title: 'setup angular project',
    description: 'Initialize with CLI',
    priority: 'high',
    completed: true,
    createdAt: new Date('2024-01-15'),
    estimatedHours: 2.5
  },
  // ... more tasks with dates
];
```

### Use Pipes in Template
```html
<div class="task-item" *ngFor="let task of tasks">
  <!-- Titlecase pipe -->
  <h4>{{ task.title | titlecase }}</h4>

  <!-- Date pipe -->
  <span class="date">Created: {{ task.createdAt | date:'mediumDate' }}</span>

  <!-- Uppercase for priority -->
  <span class="priority">{{ task.priority | uppercase }}</span>

  <!-- Number pipe for hours -->
  <span class="hours">Est: {{ task.estimatedHours | number:'1.1-1' }}h</span>

  <!-- Slice pipe for long descriptions -->
  <p class="description">
    {{ task.description | slice:0:50 }}{{ task.description.length > 50 ? '...' : '' }}
  </p>

  <!-- JSON pipe for debugging -->
  <!-- <pre>{{ task | json }}</pre> -->
</div>
```

---

## Exercise 3.6: Create Custom Pipe

### Task
Create a `timeAgo` pipe that displays relative time.

### Generate Pipe
```bash
ng g pipe pipes/time-ago
```

### Implementation
```typescript
// time-ago.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeAgo'
})
export class TimeAgoPipe implements PipeTransform {
  transform(value: Date | string): string {
    if (!value) return '';

    const date = new Date(value);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffSecs = Math.floor(diffMs / 1000);
    const diffMins = Math.floor(diffSecs / 60);
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffSecs < 60) return 'just now';
    if (diffMins < 60) return `${diffMins} minute${diffMins > 1 ? 's' : ''} ago`;
    if (diffHours < 24) return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    if (diffDays < 30) return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`;

    return date.toLocaleDateString();
  }
}
```

### Use Custom Pipe
```html
<span class="created">{{ task.createdAt | timeAgo }}</span>
```

---

## Exercise 3.7: Filter Pipe

### Task
Create a filter pipe to search tasks.

### Generate Pipe
```bash
ng g pipe pipes/filter-tasks
```

### Implementation
```typescript
// filter-tasks.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import { Task } from '../models/task.model';

@Pipe({
  name: 'filterTasks'
})
export class FilterTasksPipe implements PipeTransform {
  transform(tasks: Task[], searchTerm: string, showCompleted: boolean = true): Task[] {
    if (!tasks) return [];

    let filtered = tasks;

    // Filter by search term
    if (searchTerm) {
      searchTerm = searchTerm.toLowerCase();
      filtered = filtered.filter(task =>
        task.title.toLowerCase().includes(searchTerm) ||
        task.description.toLowerCase().includes(searchTerm)
      );
    }

    // Filter by completion status
    if (!showCompleted) {
      filtered = filtered.filter(task => !task.completed);
    }

    return filtered;
  }
}
```

### Use in Template
```typescript
// task-list.component.ts
searchTerm = '';
showCompleted = true;
```

```html
<!-- task-list.component.html -->
<div class="filters">
  <input [(ngModel)]="searchTerm" placeholder="Search tasks...">
  <label>
    <input type="checkbox" [(ngModel)]="showCompleted">
    Show completed
  </label>
</div>

<div *ngFor="let task of tasks | filterTasks:searchTerm:showCompleted">
  <!-- task content -->
</div>

<p *ngIf="(tasks | filterTasks:searchTerm:showCompleted).length === 0">
  No tasks match your criteria.
</p>
```

---

## Submission Checklist

- [ ] Task list displays with `*ngFor` and trackBy
- [ ] Empty state shows when no tasks
- [ ] `*ngIf`/`else` used for conditional rendering
- [ ] `*ngSwitch` used for priority badge
- [ ] `ngClass` applies dynamic classes
- [ ] `ngStyle` applies dynamic styles
- [ ] Built-in pipes used (date, titlecase, number, slice)
- [ ] Custom `timeAgo` pipe created and working
- [ ] Custom filter pipe with search functionality
- [ ] Filter controls work correctly

## Project Structure After Day 3
```
task-tracker/
├── src/app/
│   ├── components/
│   │   ├── header/
│   │   ├── footer/
│   │   ├── task-stats/
│   │   ├── task-card/
│   │   ├── task-form/
│   │   ├── task-list/
│   │   └── priority-badge/
│   ├── pipes/
│   │   ├── time-ago.pipe.ts
│   │   └── filter-tasks.pipe.ts
│   └── models/
│       └── task.model.ts
```

---

## Bonus Challenge
Create a `sortTasks` pipe that sorts tasks by:
- Priority (high → medium → low)
- Creation date (newest first)
- Completion status (incomplete first)

Allow the sort field to be passed as a parameter.
