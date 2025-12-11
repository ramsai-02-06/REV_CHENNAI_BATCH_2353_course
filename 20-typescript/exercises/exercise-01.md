# Exercise: TypeScript Fundamentals - Task Management Library

## Objective
Build a type-safe task management library demonstrating TypeScript features including types, interfaces, generics, and decorators.

## Requirements

### Create a Task Management Module

1. **Type Definitions**
   - Define types for Task, Project, User
   - Use union types for status
   - Use literal types for priority

2. **Interfaces**
   - ITaskRepository interface
   - INotificationService interface
   - Extend interfaces for specialized types

3. **Generics**
   - Generic Repository<T>
   - Generic Result<T, E> for error handling
   - Utility types (Partial, Required, Pick, Omit)

4. **Classes**
   - TaskManager class
   - Task class with decorators
   - Singleton pattern for services

5. **Advanced Types**
   - Discriminated unions
   - Type guards
   - Mapped types
   - Conditional types

### Code Structure
```typescript
src/
├── types/
│   ├── task.types.ts
│   └── common.types.ts
├── interfaces/
│   ├── repository.interface.ts
│   └── service.interface.ts
├── models/
│   ├── Task.ts
│   └── Project.ts
├── services/
│   ├── TaskService.ts
│   └── NotificationService.ts
├── utils/
│   ├── validators.ts
│   └── decorators.ts
└── index.ts
```

### Type Definitions Example
```typescript
// Priority as literal type
type Priority = 'low' | 'medium' | 'high' | 'critical';

// Status as enum-like union
type TaskStatus = 'todo' | 'in_progress' | 'review' | 'done';

// Task interface
interface Task {
  id: string;
  title: string;
  description?: string;
  priority: Priority;
  status: TaskStatus;
  assignee?: User;
  dueDate?: Date;
  tags: string[];
  createdAt: Date;
  updatedAt: Date;
}

// Generic Result type for error handling
type Result<T, E = Error> =
  | { success: true; data: T }
  | { success: false; error: E };
```

## Expected Features

1. Type-safe task CRUD operations
2. Filtering tasks with type guards
3. Generic repository pattern
4. Decorator for logging/validation
5. Compile-time error catching

## Skills Tested
- TypeScript type system
- Interfaces and type aliases
- Generics
- Union and intersection types
- Type guards and narrowing
- Decorators (experimental)
- Module system
