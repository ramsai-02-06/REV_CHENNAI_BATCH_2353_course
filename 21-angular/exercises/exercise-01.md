# Exercise: Build a Task Board Application with Angular

## Objective
Create a Kanban-style task board application demonstrating Angular components, services, routing, forms, and HTTP communication.

## Requirements

### Application Features

1. **Task Board View**
   - Display tasks in columns (To Do, In Progress, Done)
   - Drag and drop between columns
   - Task cards with title, assignee, priority

2. **Task Management**
   - Create new tasks (modal form)
   - Edit existing tasks
   - Delete tasks with confirmation
   - Filter tasks by assignee/priority

3. **Navigation**
   - Dashboard (board view)
   - Task list (table view)
   - Task detail page
   - Settings

### Angular Concepts to Demonstrate

1. **Components**
   - Smart (container) vs Presentational components
   - Component communication (@Input, @Output)
   - Lifecycle hooks
   - Change detection

2. **Services & DI**
   - TaskService for API calls
   - State management service
   - Dependency injection

3. **Routing**
   - Route configuration
   - Route guards
   - Lazy loading modules
   - Route parameters

4. **Forms**
   - Reactive forms
   - Form validation
   - Custom validators
   - Dynamic form fields

5. **HTTP**
   - HttpClient usage
   - Interceptors
   - Error handling
   - Loading states

6. **RxJS**
   - Observables
   - Subjects for state
   - Operators (map, filter, switchMap)

### Project Structure
```
src/app/
├── core/
│   ├── services/
│   │   ├── task.service.ts
│   │   └── auth.service.ts
│   ├── guards/
│   │   └── auth.guard.ts
│   └── interceptors/
│       └── http-error.interceptor.ts
├── shared/
│   ├── components/
│   │   ├── task-card/
│   │   └── confirm-dialog/
│   └── pipes/
│       └── priority.pipe.ts
├── features/
│   ├── board/
│   │   ├── board.component.ts
│   │   └── column/
│   ├── task-list/
│   └── task-detail/
├── models/
│   └── task.model.ts
└── app.routes.ts
```

## Expected Components

### Task Card Component
```typescript
@Component({
  selector: 'app-task-card',
  template: `
    <div class="task-card" [class.high-priority]="task.priority === 'high'">
      <h4>{{ task.title }}</h4>
      <span class="badge">{{ task.priority }}</span>
      <span class="assignee">{{ task.assignee?.name }}</span>
    </div>
  `
})
export class TaskCardComponent {
  @Input() task!: Task;
  @Output() edit = new EventEmitter<Task>();
  @Output() delete = new EventEmitter<string>();
}
```

## Skills Tested
- Angular component architecture
- Services and dependency injection
- Routing and navigation
- Reactive forms
- HTTP client
- RxJS operators
- State management patterns
