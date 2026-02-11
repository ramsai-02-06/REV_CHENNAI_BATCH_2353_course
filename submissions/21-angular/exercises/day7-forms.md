# Day 7: Forms

## Learning Goals
- Template-driven forms
- Reactive forms (FormGroup, FormControl, FormBuilder)
- Built-in and custom validators
- Form state and error display
- Dynamic forms

## Prerequisites
- Completed Day 1-6 exercises
- HTTP service working

---

## Exercise 7.1: Template-Driven Form Review

### Task
Enhance the existing task form with validation.

### Setup
Ensure FormsModule is imported:
```typescript
// app.module.ts
import { FormsModule } from '@angular/forms';
```

### Template-Driven Task Form
```html
<!-- template-task-form.component.html -->
<form #taskForm="ngForm" (ngSubmit)="onSubmit(taskForm)" class="task-form">
  <h3>Add New Task</h3>

  <!-- Title Field -->
  <div class="form-group">
    <label for="title">Title *</label>
    <input
      id="title"
      name="title"
      type="text"
      [(ngModel)]="task.title"
      required
      minlength="3"
      maxlength="100"
      #titleInput="ngModel">

    <div class="error" *ngIf="titleInput.invalid && titleInput.touched">
      <span *ngIf="titleInput.errors?.['required']">Title is required</span>
      <span *ngIf="titleInput.errors?.['minlength']">
        Minimum {{ titleInput.errors?.['minlength'].requiredLength }} characters
      </span>
    </div>
  </div>

  <!-- Description Field -->
  <div class="form-group">
    <label for="description">Description</label>
    <textarea
      id="description"
      name="description"
      [(ngModel)]="task.description"
      maxlength="500"
      rows="3"
      #descInput="ngModel">
    </textarea>
    <small>{{ task.description?.length || 0 }}/500</small>
  </div>

  <!-- Priority Field -->
  <div class="form-group">
    <label for="priority">Priority *</label>
    <select
      id="priority"
      name="priority"
      [(ngModel)]="task.priority"
      required
      #priorityInput="ngModel">
      <option value="">Select priority</option>
      <option value="low">Low</option>
      <option value="medium">Medium</option>
      <option value="high">High</option>
    </select>

    <div class="error" *ngIf="priorityInput.invalid && priorityInput.touched">
      <span *ngIf="priorityInput.errors?.['required']">Priority is required</span>
    </div>
  </div>

  <!-- Estimated Hours -->
  <div class="form-group">
    <label for="hours">Estimated Hours</label>
    <input
      id="hours"
      name="estimatedHours"
      type="number"
      [(ngModel)]="task.estimatedHours"
      min="0.5"
      max="100"
      step="0.5"
      #hoursInput="ngModel">

    <div class="error" *ngIf="hoursInput.invalid && hoursInput.touched">
      <span *ngIf="hoursInput.errors?.['min']">Minimum 0.5 hours</span>
      <span *ngIf="hoursInput.errors?.['max']">Maximum 100 hours</span>
    </div>
  </div>

  <!-- Submit Button -->
  <button type="submit" [disabled]="taskForm.invalid || submitting">
    {{ submitting ? 'Adding...' : 'Add Task' }}
  </button>

  <!-- Form Debug -->
  <div class="debug" *ngIf="false">
    <p>Form Valid: {{ taskForm.valid }}</p>
    <p>Form Value: {{ taskForm.value | json }}</p>
  </div>
</form>
```

```typescript
// template-task-form.component.ts
import { Component, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-template-task-form',
  templateUrl: './template-task-form.component.html',
  styleUrls: ['./template-task-form.component.css']
})
export class TemplateTaskFormComponent {
  @Output() taskAdded = new EventEmitter<Task>();

  task: Partial<Task> = {
    title: '',
    description: '',
    priority: 'medium',
    estimatedHours: 1,
    completed: false
  };

  submitting = false;

  constructor(private taskService: TaskService) {}

  onSubmit(form: NgForm): void {
    if (form.invalid) return;

    this.submitting = true;

    // Simulate API call
    setTimeout(() => {
      this.taskService.addTask(this.task as Omit<Task, 'id' | 'createdAt'>);
      this.taskAdded.emit();
      this.resetForm(form);
      this.submitting = false;
    }, 500);
  }

  resetForm(form: NgForm): void {
    form.resetForm();
    this.task = {
      title: '',
      description: '',
      priority: 'medium',
      estimatedHours: 1,
      completed: false
    };
  }
}
```

---

## Exercise 7.2: Reactive Form Basics

### Setup
```typescript
// app.module.ts
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    // ...
    ReactiveFormsModule
  ]
})
```

### Create Reactive Task Form
```typescript
// reactive-task-form.component.ts
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-reactive-task-form',
  templateUrl: './reactive-task-form.component.html',
  styleUrls: ['./reactive-task-form.component.css']
})
export class ReactiveTaskFormComponent implements OnInit {
  @Output() taskAdded = new EventEmitter<void>();

  taskForm!: FormGroup;
  submitting = false;

  ngOnInit(): void {
    this.taskForm = new FormGroup({
      title: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]),
      description: new FormControl('', [
        Validators.maxLength(500)
      ]),
      priority: new FormControl('medium', [
        Validators.required
      ]),
      estimatedHours: new FormControl(1, [
        Validators.min(0.5),
        Validators.max(100)
      ])
    });
  }

  // Getters for easy access
  get title() { return this.taskForm.get('title'); }
  get description() { return this.taskForm.get('description'); }
  get priority() { return this.taskForm.get('priority'); }
  get estimatedHours() { return this.taskForm.get('estimatedHours'); }

  constructor(private taskService: TaskService) {}

  onSubmit(): void {
    if (this.taskForm.invalid) {
      // Mark all fields as touched to show errors
      this.taskForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    const formValue = this.taskForm.value;
    this.taskService.addTask({
      ...formValue,
      completed: false
    });

    this.taskAdded.emit();
    this.taskForm.reset({ priority: 'medium', estimatedHours: 1 });
    this.submitting = false;
  }
}
```

### Template
```html
<!-- reactive-task-form.component.html -->
<form [formGroup]="taskForm" (ngSubmit)="onSubmit()" class="task-form">
  <h3>Add New Task (Reactive)</h3>

  <!-- Title -->
  <div class="form-group">
    <label for="title">Title *</label>
    <input id="title" type="text" formControlName="title">

    <div class="error" *ngIf="title?.invalid && title?.touched">
      <span *ngIf="title?.errors?.['required']">Title is required</span>
      <span *ngIf="title?.errors?.['minlength']">
        Minimum {{ title?.errors?.['minlength'].requiredLength }} characters
      </span>
      <span *ngIf="title?.errors?.['maxlength']">
        Maximum {{ title?.errors?.['maxlength'].requiredLength }} characters
      </span>
    </div>
  </div>

  <!-- Description -->
  <div class="form-group">
    <label for="description">Description</label>
    <textarea id="description" formControlName="description" rows="3"></textarea>
    <small>{{ description?.value?.length || 0 }}/500</small>
  </div>

  <!-- Priority -->
  <div class="form-group">
    <label for="priority">Priority *</label>
    <select id="priority" formControlName="priority">
      <option value="low">Low</option>
      <option value="medium">Medium</option>
      <option value="high">High</option>
    </select>
  </div>

  <!-- Estimated Hours -->
  <div class="form-group">
    <label for="hours">Estimated Hours</label>
    <input id="hours" type="number" formControlName="estimatedHours" step="0.5">
  </div>

  <button type="submit" [disabled]="submitting">
    {{ submitting ? 'Adding...' : 'Add Task' }}
  </button>
</form>
```

---

## Exercise 7.3: FormBuilder (Cleaner Syntax)

### Refactor with FormBuilder
```typescript
// reactive-task-form.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({...})
export class ReactiveTaskFormComponent implements OnInit {
  taskForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: ['', Validators.maxLength(500)],
      priority: ['medium', Validators.required],
      estimatedHours: [1, [Validators.min(0.5), Validators.max(100)]]
    });
  }

  // ... rest of component
}
```

---

## Exercise 7.4: Custom Validators

### Create Custom Validators
```typescript
// validators/task.validators.ts
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// No whitespace-only values
export function noWhitespace(control: AbstractControl): ValidationErrors | null {
  if (control.value && control.value.trim().length === 0) {
    return { noWhitespace: true };
  }
  return null;
}

// No forbidden words
export function forbiddenWords(words: string[]): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) return null;

    const value = control.value.toLowerCase();
    const found = words.find(word => value.includes(word.toLowerCase()));

    if (found) {
      return { forbiddenWord: { word: found } };
    }
    return null;
  };
}

// Minimum estimated hours based on priority
export function minHoursForPriority(priorityControl: AbstractControl): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const priority = priorityControl.value;
    const hours = control.value;

    if (priority === 'high' && hours < 2) {
      return { minHoursForPriority: { required: 2, actual: hours } };
    }
    return null;
  };
}
```

### Use Custom Validators
```typescript
// In component
import { noWhitespace, forbiddenWords } from '../../validators/task.validators';

this.taskForm = this.fb.group({
  title: ['', [
    Validators.required,
    Validators.minLength(3),
    noWhitespace,
    forbiddenWords(['test', 'temp', 'todo'])
  ]],
  // ...
});
```

### Display Custom Errors
```html
<div class="error" *ngIf="title?.invalid && title?.touched">
  <span *ngIf="title?.errors?.['required']">Title is required</span>
  <span *ngIf="title?.errors?.['noWhitespace']">Title cannot be only whitespace</span>
  <span *ngIf="title?.errors?.['forbiddenWord']">
    Cannot use "{{ title?.errors?.['forbiddenWord'].word }}"
  </span>
</div>
```

---

## Exercise 7.5: Cross-Field Validation

### Task
Validate that high-priority tasks have at least 2 estimated hours.

### Form Group Validator
```typescript
// validators/task.validators.ts
export function priorityHoursValidator(group: AbstractControl): ValidationErrors | null {
  const priority = group.get('priority')?.value;
  const hours = group.get('estimatedHours')?.value;

  if (priority === 'high' && hours < 2) {
    return { priorityHoursMismatch: { priority, hours, requiredHours: 2 } };
  }

  return null;
}
```

### Apply to Form Group
```typescript
this.taskForm = this.fb.group({
  title: ['', [Validators.required]],
  priority: ['medium'],
  estimatedHours: [1]
}, {
  validators: priorityHoursValidator  // Apply to entire form group
});
```

### Display Form-Level Error
```html
<div class="form-error" *ngIf="taskForm.errors?.['priorityHoursMismatch']">
  High priority tasks require at least
  {{ taskForm.errors?.['priorityHoursMismatch'].requiredHours }} hours.
  Current: {{ taskForm.errors?.['priorityHoursMismatch'].hours }}h
</div>
```

---

## Exercise 7.6: Edit Task Form

### Create Edit Form with Pre-populated Values
```typescript
// task-edit-form.component.ts
import { Component, Input, Output, EventEmitter, OnInit, OnChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-edit-form',
  templateUrl: './task-edit-form.component.html'
})
export class TaskEditFormComponent implements OnInit, OnChanges {
  @Input() task!: Task;
  @Output() saved = new EventEmitter<Partial<Task>>();
  @Output() cancelled = new EventEmitter<void>();

  editForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initForm();
  }

  ngOnChanges(): void {
    if (this.editForm && this.task) {
      this.editForm.patchValue(this.task);
    }
  }

  initForm(): void {
    this.editForm = this.fb.group({
      title: [this.task?.title || '', [Validators.required, Validators.minLength(3)]],
      description: [this.task?.description || ''],
      priority: [this.task?.priority || 'medium', Validators.required],
      estimatedHours: [this.task?.estimatedHours || 1, [Validators.min(0.5)]],
      completed: [this.task?.completed || false]
    });
  }

  onSubmit(): void {
    if (this.editForm.valid) {
      this.saved.emit(this.editForm.value);
    }
  }

  onCancel(): void {
    this.cancelled.emit();
  }
}
```

### Template
```html
<!-- task-edit-form.component.html -->
<form [formGroup]="editForm" (ngSubmit)="onSubmit()" class="edit-form">
  <h3>Edit Task</h3>

  <div class="form-group">
    <label>Title</label>
    <input formControlName="title">
  </div>

  <div class="form-group">
    <label>Description</label>
    <textarea formControlName="description"></textarea>
  </div>

  <div class="form-group">
    <label>Priority</label>
    <select formControlName="priority">
      <option value="low">Low</option>
      <option value="medium">Medium</option>
      <option value="high">High</option>
    </select>
  </div>

  <div class="form-group">
    <label>Estimated Hours</label>
    <input type="number" formControlName="estimatedHours" step="0.5">
  </div>

  <div class="form-group">
    <label>
      <input type="checkbox" formControlName="completed">
      Completed
    </label>
  </div>

  <div class="form-actions">
    <button type="button" (click)="onCancel()">Cancel</button>
    <button type="submit" [disabled]="editForm.invalid">Save Changes</button>
  </div>
</form>
```

---

## Exercise 7.7: Dynamic Form (Bonus)

### Create Form from Configuration
```typescript
// dynamic-form.component.ts
interface FieldConfig {
  name: string;
  label: string;
  type: 'text' | 'textarea' | 'select' | 'number' | 'checkbox';
  options?: { value: string; label: string }[];
  validators?: any[];
}

@Component({
  selector: 'app-dynamic-form',
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div *ngFor="let field of config" class="form-group">
        <label>{{ field.label }}</label>

        <ng-container [ngSwitch]="field.type">
          <input *ngSwitchCase="'text'"
                 [formControlName]="field.name"
                 type="text">

          <textarea *ngSwitchCase="'textarea'"
                    [formControlName]="field.name"></textarea>

          <select *ngSwitchCase="'select'" [formControlName]="field.name">
            <option *ngFor="let opt of field.options" [value]="opt.value">
              {{ opt.label }}
            </option>
          </select>

          <input *ngSwitchCase="'number'"
                 [formControlName]="field.name"
                 type="number">

          <input *ngSwitchCase="'checkbox'"
                 [formControlName]="field.name"
                 type="checkbox">
        </ng-container>
      </div>

      <button type="submit">Submit</button>
    </form>
  `
})
export class DynamicFormComponent implements OnInit {
  @Input() config: FieldConfig[] = [];
  @Output() submitted = new EventEmitter<any>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    const group: any = {};
    this.config.forEach(field => {
      group[field.name] = ['', field.validators || []];
    });
    this.form = this.fb.group(group);
  }

  onSubmit(): void {
    this.submitted.emit(this.form.value);
  }
}
```

---

## Submission Checklist

- [ ] Template-driven form with validation
- [ ] Reactive form using FormGroup/FormControl
- [ ] FormBuilder used for cleaner syntax
- [ ] Built-in validators (required, minLength, etc.)
- [ ] Custom validator created and used
- [ ] Cross-field validation working
- [ ] Edit form pre-populates with existing task
- [ ] Error messages display correctly
- [ ] Form states (touched, dirty) handled

## Comparison: Template-Driven vs Reactive

| Feature | Template-Driven | Reactive |
|---------|-----------------|----------|
| Setup | FormsModule | ReactiveFormsModule |
| Form Definition | In template | In component |
| Validation | Attributes | Validators class |
| Data Flow | Async | Sync |
| Testing | Harder | Easier |
| Dynamic Forms | Difficult | Easy |
| Use Case | Simple forms | Complex forms |

---

## Final Project Structure
```
task-tracker/
├── src/app/
│   ├── components/
│   │   ├── header/
│   │   ├── footer/
│   │   ├── task-card/
│   │   ├── task-stats/
│   │   ├── priority-badge/
│   │   └── search/
│   ├── pages/
│   │   ├── dashboard/
│   │   ├── task-list-page/
│   │   ├── task-detail/
│   │   └── not-found/
│   ├── forms/
│   │   ├── template-task-form/
│   │   ├── reactive-task-form/
│   │   └── task-edit-form/
│   ├── services/
│   │   ├── task.service.ts
│   │   ├── api.service.ts
│   │   └── auth.service.ts
│   ├── guards/
│   │   └── auth.guard.ts
│   ├── interceptors/
│   │   ├── auth.interceptor.ts
│   │   └── logging.interceptor.ts
│   ├── pipes/
│   │   ├── time-ago.pipe.ts
│   │   └── filter-tasks.pipe.ts
│   ├── validators/
│   │   └── task.validators.ts
│   └── models/
│       └── task.model.ts
```

---

## Congratulations!

You've completed the Angular Task Tracker exercise covering:
- Components & data binding
- Directives & pipes
- Services & dependency injection
- Routing & navigation
- RxJS & HTTP
- Forms & validation

The application demonstrates all core Angular concepts in a real-world context.
