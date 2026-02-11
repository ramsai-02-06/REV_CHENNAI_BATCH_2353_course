# Day 9: Testing & Debugging

## Learning Goals
- Write unit tests with Jasmine
- Run tests with Karma
- Test components (inputs, outputs, DOM)
- Test services with mock dependencies
- Test pipes
- Debug Angular applications effectively

## Prerequisites
- Completed Day 1-8 exercises
- Task Tracker application complete

---

## Exercise 9.1: Jasmine Basics - Test a Utility Function

### Task
Create and test a utility function for task validation.

### Create Utility
```typescript
// utils/task-validators.ts
export function isValidTaskTitle(title: string): boolean {
  if (!title || typeof title !== 'string') return false;
  const trimmed = title.trim();
  return trimmed.length >= 3 && trimmed.length <= 100;
}

export function calculateCompletionPercentage(
  completed: number,
  total: number
): number {
  if (total === 0) return 0;
  return Math.round((completed / total) * 100);
}

export function sortTasksByPriority<T extends { priority: string }>(
  tasks: T[]
): T[] {
  const priorityOrder = { high: 1, medium: 2, low: 3 };
  return [...tasks].sort((a, b) => {
    return (priorityOrder[a.priority as keyof typeof priorityOrder] || 4) -
           (priorityOrder[b.priority as keyof typeof priorityOrder] || 4);
  });
}
```

### Write Tests
```typescript
// utils/task-validators.spec.ts
import {
  isValidTaskTitle,
  calculateCompletionPercentage,
  sortTasksByPriority
} from './task-validators';

describe('Task Validators', () => {

  describe('isValidTaskTitle', () => {
    it('should return true for valid titles', () => {
      expect(isValidTaskTitle('Buy groceries')).toBeTrue();
      expect(isValidTaskTitle('abc')).toBeTrue();  // Min length
    });

    it('should return false for empty strings', () => {
      expect(isValidTaskTitle('')).toBeFalse();
    });

    it('should return false for whitespace-only strings', () => {
      expect(isValidTaskTitle('   ')).toBeFalse();
    });

    it('should return false for too short titles', () => {
      expect(isValidTaskTitle('ab')).toBeFalse();
    });

    it('should return false for too long titles', () => {
      const longTitle = 'x'.repeat(101);
      expect(isValidTaskTitle(longTitle)).toBeFalse();
    });

    it('should return false for null/undefined', () => {
      expect(isValidTaskTitle(null as any)).toBeFalse();
      expect(isValidTaskTitle(undefined as any)).toBeFalse();
    });

    it('should trim whitespace before validation', () => {
      expect(isValidTaskTitle('  abc  ')).toBeTrue();
    });
  });

  describe('calculateCompletionPercentage', () => {
    it('should calculate correct percentage', () => {
      expect(calculateCompletionPercentage(5, 10)).toBe(50);
      expect(calculateCompletionPercentage(3, 4)).toBe(75);
    });

    it('should return 0 when total is 0', () => {
      expect(calculateCompletionPercentage(0, 0)).toBe(0);
    });

    it('should round to nearest integer', () => {
      expect(calculateCompletionPercentage(1, 3)).toBe(33);
      expect(calculateCompletionPercentage(2, 3)).toBe(67);
    });

    it('should return 100 when all completed', () => {
      expect(calculateCompletionPercentage(10, 10)).toBe(100);
    });
  });

  describe('sortTasksByPriority', () => {
    it('should sort tasks by priority (high first)', () => {
      const tasks = [
        { id: 1, priority: 'low' },
        { id: 2, priority: 'high' },
        { id: 3, priority: 'medium' }
      ];

      const sorted = sortTasksByPriority(tasks);

      expect(sorted[0].priority).toBe('high');
      expect(sorted[1].priority).toBe('medium');
      expect(sorted[2].priority).toBe('low');
    });

    it('should not mutate original array', () => {
      const tasks = [
        { id: 1, priority: 'low' },
        { id: 2, priority: 'high' }
      ];

      const sorted = sortTasksByPriority(tasks);

      expect(tasks[0].priority).toBe('low');  // Original unchanged
      expect(sorted).not.toBe(tasks);  // Different array reference
    });

    it('should handle empty array', () => {
      expect(sortTasksByPriority([])).toEqual([]);
    });
  });
});
```

### Run Tests
```bash
ng test

# Run specific file
ng test --include=**/task-validators.spec.ts
```

---

## Exercise 9.2: Test a Pipe

### Task
Write comprehensive tests for the TimeAgo pipe.

### Time Ago Pipe
```typescript
// pipes/time-ago.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'timeAgo' })
export class TimeAgoPipe implements PipeTransform {
  transform(value: Date | string | null): string {
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

### Write Pipe Tests
```typescript
// pipes/time-ago.pipe.spec.ts
import { TimeAgoPipe } from './time-ago.pipe';

describe('TimeAgoPipe', () => {
  let pipe: TimeAgoPipe;

  beforeEach(() => {
    pipe = new TimeAgoPipe();
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return empty string for null', () => {
    expect(pipe.transform(null)).toBe('');
  });

  it('should return empty string for undefined', () => {
    expect(pipe.transform(undefined as any)).toBe('');
  });

  it('should return "just now" for recent times', () => {
    const now = new Date();
    expect(pipe.transform(now)).toBe('just now');

    const thirtySecondsAgo = new Date(now.getTime() - 30000);
    expect(pipe.transform(thirtySecondsAgo)).toBe('just now');
  });

  it('should return minutes ago', () => {
    const now = new Date();
    const fiveMinutesAgo = new Date(now.getTime() - 5 * 60 * 1000);
    expect(pipe.transform(fiveMinutesAgo)).toBe('5 minutes ago');
  });

  it('should use singular for 1 minute', () => {
    const now = new Date();
    const oneMinuteAgo = new Date(now.getTime() - 60 * 1000);
    expect(pipe.transform(oneMinuteAgo)).toBe('1 minute ago');
  });

  it('should return hours ago', () => {
    const now = new Date();
    const threeHoursAgo = new Date(now.getTime() - 3 * 60 * 60 * 1000);
    expect(pipe.transform(threeHoursAgo)).toBe('3 hours ago');
  });

  it('should use singular for 1 hour', () => {
    const now = new Date();
    const oneHourAgo = new Date(now.getTime() - 60 * 60 * 1000);
    expect(pipe.transform(oneHourAgo)).toBe('1 hour ago');
  });

  it('should return days ago', () => {
    const now = new Date();
    const fiveDaysAgo = new Date(now.getTime() - 5 * 24 * 60 * 60 * 1000);
    expect(pipe.transform(fiveDaysAgo)).toBe('5 days ago');
  });

  it('should return formatted date for old dates', () => {
    const oldDate = new Date('2020-01-15');
    const result = pipe.transform(oldDate);
    // Result will be locale-dependent, just verify it's not empty
    expect(result).toBeTruthy();
    expect(result).not.toContain('ago');
  });

  it('should handle string date input', () => {
    const now = new Date();
    const fiveMinutesAgo = new Date(now.getTime() - 5 * 60 * 1000);
    expect(pipe.transform(fiveMinutesAgo.toISOString())).toBe('5 minutes ago');
  });
});
```

---

## Exercise 9.3: Test a Component

### Task
Write tests for the TaskCard component.

### TaskCard Component Tests
```typescript
// components/task-card/task-card.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { TaskCardComponent } from './task-card.component';
import { Task } from '../../models/task.model';

describe('TaskCardComponent', () => {
  let component: TaskCardComponent;
  let fixture: ComponentFixture<TaskCardComponent>;

  const mockTask: Task = {
    id: 1,
    title: 'Test Task',
    description: 'Test description',
    priority: 'high',
    completed: false,
    createdAt: new Date(),
    estimatedHours: 2
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TaskCardComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskCardComponent);
    component = fixture.componentInstance;
    component.task = { ...mockTask };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Rendering', () => {
    it('should display task title', () => {
      const titleElement = fixture.nativeElement.querySelector('h4, .task-title');
      expect(titleElement.textContent).toContain('Test Task');
    });

    it('should display task description', () => {
      const descElement = fixture.nativeElement.querySelector('.description, p');
      expect(descElement.textContent).toContain('Test description');
    });

    it('should apply completed class when task is completed', () => {
      component.task = { ...mockTask, completed: true };
      fixture.detectChanges();

      const cardElement = fixture.nativeElement.querySelector('.task-card');
      expect(cardElement.classList.contains('completed')).toBeTrue();
    });

    it('should not show complete button for completed tasks', () => {
      component.task = { ...mockTask, completed: true };
      fixture.detectChanges();

      const completeBtn = fixture.nativeElement.querySelector('.btn-complete');
      expect(completeBtn).toBeFalsy();
    });
  });

  describe('Events', () => {
    it('should emit completed event with task id', () => {
      spyOn(component.completed, 'emit');

      const completeBtn = fixture.nativeElement.querySelector('.btn-complete');
      if (completeBtn) {
        completeBtn.click();
        expect(component.completed.emit).toHaveBeenCalledWith(1);
      }
    });

    it('should emit deleted event with task id', () => {
      spyOn(component.deleted, 'emit');
      spyOn(window, 'confirm').and.returnValue(true);

      const deleteBtn = fixture.nativeElement.querySelector('.btn-delete');
      if (deleteBtn) {
        deleteBtn.click();
        expect(component.deleted.emit).toHaveBeenCalledWith(1);
      }
    });

    it('should not emit deleted if confirm is cancelled', () => {
      spyOn(component.deleted, 'emit');
      spyOn(window, 'confirm').and.returnValue(false);

      const deleteBtn = fixture.nativeElement.querySelector('.btn-delete');
      if (deleteBtn) {
        deleteBtn.click();
        expect(component.deleted.emit).not.toHaveBeenCalled();
      }
    });
  });

  describe('Input changes', () => {
    it('should update display when task input changes', () => {
      component.task = { ...mockTask, title: 'Updated Title' };
      fixture.detectChanges();

      const titleElement = fixture.nativeElement.querySelector('h4, .task-title');
      expect(titleElement.textContent).toContain('Updated Title');
    });
  });
});
```

---

## Exercise 9.4: Test a Component with Dependencies

### Task
Test TaskListPage component that depends on TaskService.

### Component with Service
```typescript
// pages/task-list-page/task-list-page.component.spec.ts
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { of, Subject } from 'rxjs';
import { TaskListPageComponent } from './task-list-page.component';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';

describe('TaskListPageComponent', () => {
  let component: TaskListPageComponent;
  let fixture: ComponentFixture<TaskListPageComponent>;
  let taskServiceSpy: jasmine.SpyObj<TaskService>;

  const mockTasks: Task[] = [
    {
      id: 1,
      title: 'Task 1',
      description: 'Description 1',
      priority: 'high',
      completed: false,
      createdAt: new Date(),
      estimatedHours: 2
    },
    {
      id: 2,
      title: 'Task 2',
      description: 'Description 2',
      priority: 'low',
      completed: true,
      createdAt: new Date(),
      estimatedHours: 1
    }
  ];

  beforeEach(async () => {
    // Create spy object for TaskService
    taskServiceSpy = jasmine.createSpyObj('TaskService', [
      'completeTask',
      'deleteTask'
    ], {
      tasks$: of(mockTasks)  // Property spy
    });

    await TestBed.configureTestingModule({
      declarations: [TaskListPageComponent],
      providers: [
        { provide: TaskService, useValue: taskServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskListPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load tasks on init', () => {
    component.tasks$.subscribe(tasks => {
      expect(tasks.length).toBe(2);
      expect(tasks[0].title).toBe('Task 1');
    });
  });

  it('should call completeTask when handling complete', () => {
    component.onComplete(1);
    expect(taskServiceSpy.completeTask).toHaveBeenCalledWith(1);
  });

  it('should call deleteTask when handling delete', () => {
    component.onDelete(1);
    expect(taskServiceSpy.deleteTask).toHaveBeenCalledWith(1);
  });

  describe('Search/Filter', () => {
    it('should update searchTerm', () => {
      component.searchTerm = 'test';
      expect(component.searchTerm).toBe('test');
    });
  });
});
```

---

## Exercise 9.5: Test a Service with HTTP

### Task
Test the ApiService that makes HTTP requests.

### Service Tests with HttpTestingController
```typescript
// services/api.service.spec.ts
import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { ApiService } from './api.service';
import { Task } from '../models/task.model';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApiService]
    });

    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify no outstanding HTTP requests
    httpMock.verify();
  });

  describe('getTasks', () => {
    it('should fetch tasks from API', () => {
      const mockResponse = [
        { id: 1, title: 'Task 1', completed: false, userId: 1 },
        { id: 2, title: 'Task 2', completed: true, userId: 1 }
      ];

      service.getTasks().subscribe(tasks => {
        expect(tasks.length).toBe(2);
        expect(tasks[0].title).toBe('Task 1');
      });

      const req = httpMock.expectOne((r) => r.url.includes('/todos'));
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle HTTP error', () => {
      service.getTasks().subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.message).toContain('error');
        }
      });

      const req = httpMock.expectOne((r) => r.url.includes('/todos'));
      req.flush('Error', { status: 500, statusText: 'Server Error' });
    });
  });

  describe('getTask', () => {
    it('should fetch single task by id', () => {
      const mockTask = { id: 1, title: 'Task 1', completed: false, userId: 1 };

      service.getTask(1).subscribe(task => {
        expect(task.id).toBe(1);
        expect(task.title).toBe('Task 1');
      });

      const req = httpMock.expectOne((r) => r.url.includes('/todos/1'));
      expect(req.request.method).toBe('GET');
      req.flush(mockTask);
    });
  });

  describe('createTask', () => {
    it('should POST new task', () => {
      const newTask = { title: 'New Task', completed: false };
      const createdTask = { id: 101, ...newTask, userId: 1 };

      service.createTask(newTask).subscribe(task => {
        expect(task.id).toBe(101);
      });

      const req = httpMock.expectOne((r) => r.url.includes('/todos'));
      expect(req.request.method).toBe('POST');
      expect(req.request.body.title).toBe('New Task');
      req.flush(createdTask);
    });
  });

  describe('updateTask', () => {
    it('should PATCH task', () => {
      const updates = { completed: true };

      service.updateTask(1, updates).subscribe(task => {
        expect(task.completed).toBeTrue();
      });

      const req = httpMock.expectOne((r) => r.url.includes('/todos/1'));
      expect(req.request.method).toBe('PATCH');
      req.flush({ id: 1, title: 'Task 1', completed: true, userId: 1 });
    });
  });

  describe('deleteTask', () => {
    it('should DELETE task', () => {
      service.deleteTask(1).subscribe(() => {
        // Success
      });

      const req = httpMock.expectOne((r) => r.url.includes('/todos/1'));
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });
});
```

---

## Exercise 9.6: Test TaskService (BehaviorSubject)

### Task
Test the TaskService that uses BehaviorSubject for state management.

### Service Tests
```typescript
// services/task.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { TaskService } from './task.service';
import { Task } from '../models/task.model';

describe('TaskService', () => {
  let service: TaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TaskService]
    });
    service = TestBed.inject(TaskService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('tasks$', () => {
    it('should emit initial tasks', (done) => {
      service.tasks$.subscribe(tasks => {
        expect(tasks.length).toBeGreaterThan(0);
        done();
      });
    });
  });

  describe('addTask', () => {
    it('should add new task to the list', () => {
      const newTask = {
        title: 'New Task',
        description: 'Test',
        priority: 'high' as const,
        completed: false,
        estimatedHours: 1
      };

      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const initialCount = tasks.length;
      service.addTask(newTask);

      expect(tasks.length).toBe(initialCount + 1);
      expect(tasks[tasks.length - 1].title).toBe('New Task');
    });

    it('should assign unique id to new task', () => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const ids = new Set(tasks.map(t => t.id));

      service.addTask({
        title: 'Task A',
        description: '',
        priority: 'low',
        completed: false,
        estimatedHours: 1
      });

      service.addTask({
        title: 'Task B',
        description: '',
        priority: 'low',
        completed: false,
        estimatedHours: 1
      });

      const newIds = tasks.map(t => t.id);
      const uniqueIds = new Set(newIds);
      expect(uniqueIds.size).toBe(newIds.length);
    });

    it('should set createdAt date', () => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      service.addTask({
        title: 'Task',
        description: '',
        priority: 'low',
        completed: false,
        estimatedHours: 1
      });

      const newTask = tasks[tasks.length - 1];
      expect(newTask.createdAt).toBeTruthy();
      expect(newTask.createdAt instanceof Date).toBeTrue();
    });
  });

  describe('updateTask', () => {
    it('should update existing task', () => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const firstTaskId = tasks[0].id;
      service.updateTask(firstTaskId, { title: 'Updated Title' });

      const updatedTask = tasks.find(t => t.id === firstTaskId);
      expect(updatedTask?.title).toBe('Updated Title');
    });

    it('should not affect other tasks', () => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const firstTaskId = tasks[0].id;
      const secondTaskTitle = tasks[1]?.title;

      service.updateTask(firstTaskId, { title: 'Updated' });

      expect(tasks[1]?.title).toBe(secondTaskTitle);
    });
  });

  describe('deleteTask', () => {
    it('should remove task from list', () => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const initialCount = tasks.length;
      const firstTaskId = tasks[0].id;

      service.deleteTask(firstTaskId);

      expect(tasks.length).toBe(initialCount - 1);
      expect(tasks.find(t => t.id === firstTaskId)).toBeUndefined();
    });
  });

  describe('completeTask', () => {
    it('should mark task as completed', () => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const incompleteTask = tasks.find(t => !t.completed);
      if (incompleteTask) {
        service.completeTask(incompleteTask.id);

        const task = tasks.find(t => t.id === incompleteTask.id);
        expect(task?.completed).toBeTrue();
      }
    });
  });

  describe('taskStats$', () => {
    it('should emit correct stats', (done) => {
      service.taskStats$.subscribe(stats => {
        expect(stats.total).toBeGreaterThanOrEqual(0);
        expect(stats.completed).toBeGreaterThanOrEqual(0);
        expect(stats.pending).toBe(stats.total - stats.completed);
        done();
      });
    });
  });

  describe('getTask', () => {
    it('should return task by id', (done) => {
      let tasks: Task[] = [];
      service.tasks$.subscribe(t => tasks = t);

      const firstTaskId = tasks[0].id;

      service.getTask(firstTaskId).subscribe(task => {
        expect(task?.id).toBe(firstTaskId);
        done();
      });
    });

    it('should return undefined for non-existent id', (done) => {
      service.getTask(99999).subscribe(task => {
        expect(task).toBeUndefined();
        done();
      });
    });
  });
});
```

---

## Exercise 9.7: Test Component with Forms

### Task
Test the reactive TaskForm component.

### Form Component Tests
```typescript
// components/task-form/task-form.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { TaskFormComponent } from './task-form.component';
import { TaskService } from '../../services/task.service';

describe('TaskFormComponent', () => {
  let component: TaskFormComponent;
  let fixture: ComponentFixture<TaskFormComponent>;
  let taskServiceSpy: jasmine.SpyObj<TaskService>;

  beforeEach(async () => {
    taskServiceSpy = jasmine.createSpyObj('TaskService', ['addTask']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [TaskFormComponent],
      providers: [
        { provide: TaskService, useValue: taskServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Form Validation', () => {
    it('should be invalid when empty', () => {
      expect(component.taskForm.valid).toBeFalse();
    });

    it('should be valid with required fields', () => {
      component.taskForm.patchValue({
        title: 'Test Task',
        priority: 'medium'
      });
      expect(component.taskForm.valid).toBeTrue();
    });

    it('should require title', () => {
      const titleControl = component.taskForm.get('title');
      expect(titleControl?.valid).toBeFalse();

      titleControl?.setValue('Test');
      expect(titleControl?.valid).toBeTrue();
    });

    it('should require minimum title length', () => {
      const titleControl = component.taskForm.get('title');

      titleControl?.setValue('ab');
      expect(titleControl?.hasError('minlength')).toBeTrue();

      titleControl?.setValue('abc');
      expect(titleControl?.hasError('minlength')).toBeFalse();
    });

    it('should validate estimatedHours range', () => {
      const hoursControl = component.taskForm.get('estimatedHours');

      hoursControl?.setValue(0.1);
      expect(hoursControl?.hasError('min')).toBeTrue();

      hoursControl?.setValue(1);
      expect(hoursControl?.valid).toBeTrue();

      hoursControl?.setValue(101);
      expect(hoursControl?.hasError('max')).toBeTrue();
    });
  });

  describe('Form Submission', () => {
    it('should not submit invalid form', () => {
      component.onSubmit();
      expect(taskServiceSpy.addTask).not.toHaveBeenCalled();
    });

    it('should submit valid form', () => {
      component.taskForm.patchValue({
        title: 'Test Task',
        description: 'Test description',
        priority: 'high',
        estimatedHours: 2
      });

      component.onSubmit();

      expect(taskServiceSpy.addTask).toHaveBeenCalledWith(jasmine.objectContaining({
        title: 'Test Task',
        priority: 'high'
      }));
    });

    it('should reset form after submission', () => {
      component.taskForm.patchValue({
        title: 'Test Task',
        priority: 'high'
      });

      component.onSubmit();

      expect(component.taskForm.get('title')?.value).toBeFalsy();
    });

    it('should emit taskAdded event on submit', () => {
      spyOn(component.taskAdded, 'emit');

      component.taskForm.patchValue({
        title: 'Test Task',
        priority: 'medium'
      });

      component.onSubmit();

      expect(component.taskAdded.emit).toHaveBeenCalled();
    });
  });

  describe('Form Controls Accessibility', () => {
    it('should have title getter', () => {
      expect(component.title).toBe(component.taskForm.get('title'));
    });

    it('should have priority getter', () => {
      expect(component.priority).toBe(component.taskForm.get('priority'));
    });
  });
});
```

---

## Exercise 9.8: Debugging Techniques

### Task
Practice debugging Angular applications.

### Add Debug Helpers
```typescript
// Add to any component for debugging
@Component({...})
export class DebugComponent {
  // 1. Template debugging
  // Add this to template to see object state:
  // <pre>{{ someObject | json }}</pre>

  // 2. Lifecycle logging
  ngOnInit() {
    console.log('Component initialized');
    console.log('Initial state:', this);
  }

  ngOnChanges(changes: SimpleChanges) {
    console.log('Changes detected:', changes);
  }

  // 3. Method debugging
  someMethod(data: any) {
    console.log('Method called with:', data);
    console.trace();  // Shows call stack
    debugger;  // Breakpoint
    return data;
  }

  // 4. HTTP debugging
  fetchData() {
    this.http.get('/api/data').subscribe({
      next: data => console.log('Success:', data),
      error: err => console.error('Error:', err)
    });
  }
}
```

### Chrome DevTools Tips
```javascript
// In Chrome Console, after selecting element in Elements tab:

// Get component instance
ng.getComponent($0)

// Get component context
ng.getContext($0)

// Profile change detection
ng.profiler.timeChangeDetection()
```

### VS Code launch.json
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "chrome",
      "request": "launch",
      "name": "Debug Angular",
      "url": "http://localhost:4200",
      "webRoot": "${workspaceFolder}",
      "sourceMapPathOverrides": {
        "webpack:/*": "${webRoot}/*"
      }
    }
  ]
}
```

### Debug Steps
1. Run `ng serve`
2. Open VS Code Debug panel (Ctrl+Shift+D)
3. Select "Debug Angular" configuration
4. Press F5 to start
5. Set breakpoints in .ts files
6. Interact with app to hit breakpoints

---

## Exercise 9.9: Code Coverage

### Task
Generate and analyze test coverage report.

### Generate Coverage
```bash
# Run tests with coverage
ng test --code-coverage

# Single run with coverage
ng test --code-coverage --watch=false
```

### View Report
Open `coverage/index.html` in browser to see:
- Overall coverage percentage
- Per-file coverage
- Uncovered lines highlighted

### Coverage Targets
```json
// karma.conf.js - Add coverage thresholds
coverageReporter: {
  check: {
    global: {
      statements: 80,
      branches: 80,
      functions: 80,
      lines: 80
    }
  }
}
```

### Improve Coverage
Identify and test:
1. Uncovered branches (if/else paths)
2. Error handling paths
3. Edge cases
4. All public methods

---

## Submission Checklist

- [ ] Utility function tests written and passing
- [ ] TimeAgo pipe fully tested
- [ ] TaskCard component tested (rendering, events)
- [ ] TaskListPage tested with mocked service
- [ ] ApiService tested with HttpTestingController
- [ ] TaskService (BehaviorSubject) tested
- [ ] TaskForm validated and submission tested
- [ ] Debug techniques practiced
- [ ] Code coverage generated (target: 80%+)
- [ ] All tests passing: `ng test --watch=false`

## Testing Commands Quick Reference

```bash
# Run all tests
ng test

# Single run (CI mode)
ng test --watch=false

# With coverage
ng test --code-coverage

# Specific file
ng test --include=**/task.service.spec.ts

# Headless (for CI)
ng test --watch=false --browsers=ChromeHeadless

# Run with verbose output
ng test --reporters=verbose
```

## Testing Best Practices

1. **AAA Pattern**: Arrange, Act, Assert
2. **One assertion per test** (when practical)
3. **Test behavior, not implementation**
4. **Use descriptive test names**
5. **Mock external dependencies**
6. **Test edge cases and error scenarios**
7. **Keep tests independent**
8. **Run tests before every commit**

---

## Bonus Challenge

Write end-to-end (E2E) tests using Cypress or Playwright:

```typescript
// e2e/task-flow.spec.ts
describe('Task Management Flow', () => {
  it('should create, complete, and delete a task', () => {
    cy.visit('/');
    cy.get('[data-testid="add-task-btn"]').click();
    cy.get('[data-testid="title-input"]').type('E2E Test Task');
    cy.get('[data-testid="submit-btn"]').click();

    cy.contains('E2E Test Task').should('exist');

    cy.get('[data-testid="complete-btn"]').first().click();
    cy.get('.task-card.completed').should('exist');

    cy.get('[data-testid="delete-btn"]').first().click();
    cy.contains('E2E Test Task').should('not.exist');
  });
});
```
