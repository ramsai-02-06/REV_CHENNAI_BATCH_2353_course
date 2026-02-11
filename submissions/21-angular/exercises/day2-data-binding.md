# Day 2: Data Binding

## Learning Goals
- Property binding `[property]`
- Event binding `(event)`
- Two-way binding `[(ngModel)]`
- Template reference variables `#ref`

## Prerequisites
- Completed Day 1 exercises
- `task-tracker` project running

---

## Exercise 2.1: Property Binding

### Task
Create a `TaskCard` component that accepts task data and displays it.

### Generate Component
```bash
ng g c components/task-card
```

### Task Interface
Create `src/app/models/task.model.ts`:
```typescript
export interface Task {
  id: number;
  title: string;
  description: string;
  priority: 'low' | 'medium' | 'high';
  completed: boolean;
}
```

### Implementation
```typescript
// task-card.component.ts
import { Component } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-card',
  templateUrl: './task-card.component.html',
  styleUrls: ['./task-card.component.css']
})
export class TaskCardComponent {
  task: Task = {
    id: 1,
    title: 'Learn Angular',
    description: 'Complete the Angular tutorial',
    priority: 'high',
    completed: false
  };
}
```

### Template with Property Binding
```html
<!-- task-card.component.html -->
<div class="task-card" [class.completed]="task.completed">
  <h3>{{ task.title }}</h3>
  <p>{{ task.description }}</p>

  <!-- Property binding for class -->
  <span class="priority" [class.high]="task.priority === 'high'"
                         [class.medium]="task.priority === 'medium'"
                         [class.low]="task.priority === 'low'">
    {{ task.priority }}
  </span>

  <!-- Property binding for disabled -->
  <button [disabled]="task.completed">Mark Complete</button>
</div>
```

### Styling
```css
.task-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin: 8px 0;
  background: white;
}

.task-card.completed {
  opacity: 0.6;
  background: #f5f5f5;
}

.priority {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
  text-transform: uppercase;
}

.priority.high { background: #ffebee; color: #c62828; }
.priority.medium { background: #fff3e0; color: #ef6c00; }
.priority.low { background: #e8f5e9; color: #2e7d32; }
```

---

## Exercise 2.2: Event Binding

### Task
Add interactivity to the TaskCard component.

### Add Methods
```typescript
// Add to task-card.component.ts
toggleComplete(): void {
  this.task.completed = !this.task.completed;
  console.log('Task completed:', this.task.completed);
}

onPriorityChange(newPriority: 'low' | 'medium' | 'high'): void {
  this.task.priority = newPriority;
  console.log('Priority changed to:', newPriority);
}
```

### Update Template
```html
<!-- Add event bindings -->
<div class="task-card" [class.completed]="task.completed">
  <h3>{{ task.title }}</h3>
  <p>{{ task.description }}</p>

  <div class="priority-buttons">
    <button (click)="onPriorityChange('low')">Low</button>
    <button (click)="onPriorityChange('medium')">Medium</button>
    <button (click)="onPriorityChange('high')">High</button>
  </div>

  <span class="priority" [class]="'priority ' + task.priority">
    {{ task.priority }}
  </span>

  <button (click)="toggleComplete()">
    {{ task.completed ? 'Reopen' : 'Complete' }}
  </button>
</div>
```

---

## Exercise 2.3: Template Reference Variables

### Task
Create a quick-add feature using template references.

### Add to TaskCard or New Component
```html
<div class="quick-add">
  <input #titleInput
         type="text"
         placeholder="Enter task title"
         (keyup.enter)="quickAdd(titleInput.value); titleInput.value = ''">

  <button (click)="quickAdd(titleInput.value); titleInput.value = ''">
    Add Task
  </button>

  <button (click)="titleInput.focus()">Focus Input</button>
</div>
```

```typescript
quickAdd(title: string): void {
  if (title.trim()) {
    console.log('Quick adding task:', title);
    // We'll implement actual adding later with services
  }
}
```

---

## Exercise 2.4: Two-Way Binding

### Setup FormsModule
```typescript
// app.module.ts
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule  // Add this!
  ],
  // ...
})
```

### Task
Create a task edit form using two-way binding.

### Generate Component
```bash
ng g c components/task-form
```

### Implementation
```typescript
// task-form.component.ts
import { Component } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css']
})
export class TaskFormComponent {
  task: Task = {
    id: 0,
    title: '',
    description: '',
    priority: 'medium',
    completed: false
  };

  onSubmit(): void {
    console.log('Submitting task:', this.task);
    // Reset form
    this.task = {
      id: 0,
      title: '',
      description: '',
      priority: 'medium',
      completed: false
    };
  }
}
```

### Template
```html
<!-- task-form.component.html -->
<form class="task-form" (ngSubmit)="onSubmit()">
  <h3>Add New Task</h3>

  <div class="form-group">
    <label for="title">Title</label>
    <input id="title"
           type="text"
           [(ngModel)]="task.title"
           name="title"
           placeholder="Enter task title">
  </div>

  <div class="form-group">
    <label for="description">Description</label>
    <textarea id="description"
              [(ngModel)]="task.description"
              name="description"
              rows="3"
              placeholder="Enter description"></textarea>
  </div>

  <div class="form-group">
    <label for="priority">Priority</label>
    <select id="priority" [(ngModel)]="task.priority" name="priority">
      <option value="low">Low</option>
      <option value="medium">Medium</option>
      <option value="high">High</option>
    </select>
  </div>

  <div class="form-group">
    <label>
      <input type="checkbox" [(ngModel)]="task.completed" name="completed">
      Already completed
    </label>
  </div>

  <button type="submit">Add Task</button>

  <!-- Live preview -->
  <div class="preview">
    <h4>Preview:</h4>
    <p><strong>Title:</strong> {{ task.title || '(empty)' }}</p>
    <p><strong>Description:</strong> {{ task.description || '(empty)' }}</p>
    <p><strong>Priority:</strong> {{ task.priority }}</p>
    <p><strong>Completed:</strong> {{ task.completed ? 'Yes' : 'No' }}</p>
  </div>
</form>
```

### Styling
```css
.task-form {
  max-width: 400px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 4px;
  font-weight: 500;
}

.form-group input[type="text"],
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.preview {
  margin-top: 20px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 4px;
}
```

---

## Exercise 2.5: Combine All Bindings

### Task
Create a complete task entry that uses all binding types.

### Update App Component
```html
<!-- app.component.html -->
<app-header></app-header>

<main class="container">
  <div class="two-column">
    <div class="column">
      <app-task-form></app-task-form>
    </div>
    <div class="column">
      <h2>Current Tasks</h2>
      <app-task-card></app-task-card>
    </div>
  </div>

  <app-task-stats></app-task-stats>
</main>

<app-footer></app-footer>
```

---

## Submission Checklist

- [ ] TaskCard uses property binding for classes and disabled state
- [ ] TaskCard uses event binding for button clicks
- [ ] Template reference variable used for input focus/value
- [ ] FormsModule imported in AppModule
- [ ] TaskForm uses two-way binding with [(ngModel)]
- [ ] Live preview updates as user types
- [ ] Form submits and logs data

## Concepts Demonstrated

| Binding Type | Syntax | Example |
|--------------|--------|---------|
| Interpolation | `{{ }}` | `{{ task.title }}` |
| Property | `[prop]` | `[disabled]="isDisabled"` |
| Event | `(event)` | `(click)="onClick()"` |
| Two-way | `[(ngModel)]` | `[(ngModel)]="name"` |
| Template Ref | `#ref` | `#inputRef` |

---

## Bonus Challenge
Add keyboard shortcut: When user presses `Ctrl+Enter` in the description textarea, submit the form automatically.

**Hint**: Use `(keydown.control.enter)="onSubmit()"`
