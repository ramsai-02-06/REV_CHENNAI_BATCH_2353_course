# Angular Exercises: Build a Task Tracker Application

## Overview

These progressive exercises guide you through building a complete **Task Tracker** application over 9 days. Each day introduces new Angular concepts while extending the same application, reinforcing learning through practical implementation.

## Application Preview

By the end of these exercises, you will have built:

```
┌─────────────────────────────────────────────────────────────┐
│  Task Tracker                    Dashboard | Tasks    👤    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │  12 Total   │  │  5 Done     │  │  7 Pending  │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ 🔴 HIGH  Setup Angular project           ✓ Complete │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 🟡 MED   Learn Components                ✓ Complete │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 🟡 MED   Master Directives               ○ Pending  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  [+ Add Task]                              Showing 3 of 12  │
└─────────────────────────────────────────────────────────────┘
```

## Prerequisites

- Node.js v16+ installed
- Angular CLI installed (`npm install -g @angular/cli`)
- Completed TypeScript module
- Basic HTML/CSS knowledge

## Daily Exercises

| Day | Topic | Concepts Covered | Exercise |
|-----|-------|------------------|----------|
| 1 | [Setup & First Components](./exercises/day1-setup-components.md) | CLI, project structure, interpolation | Create project, header, footer, stats components |
| 2 | [Data Binding](./exercises/day2-data-binding.md) | Property, event, two-way binding, template refs | TaskCard, TaskForm with live preview |
| 3 | [Directives & Pipes](./exercises/day3-directives-pipes.md) | *ngIf, *ngFor, ngClass, pipes | TaskList with filtering, custom TimeAgo pipe |
| 4 | [Services & Communication](./exercises/day4-services-communication.md) | @Input, @Output, DI, services | TaskService, component communication |
| 5 | [Routing](./exercises/day5-routing.md) | Routes, guards, lazy loading | Dashboard, task detail pages, navigation |
| 6 | [RxJS & HTTP](./exercises/day6-rxjs-http.md) | Observables, operators, HttpClient | API integration, interceptors, async pipe |
| 7 | [Forms](./exercises/day7-forms.md) | Template-driven, reactive, validation | Task forms with custom validators |
| 8 | [Advanced Topics](./exercises/day8-advanced.md) | Lifecycle hooks, change detection, modules | OnPush, dynamic components, module organization |
| 9 | [Testing & Debugging](./exercises/day9-testing.md) | Jasmine, Karma, TestBed | Unit tests for components, services, pipes |

## Learning Path

```
Day 1: Setup          Day 2: Binding       Day 3: Directives
   │                      │                     │
   ▼                      ▼                     ▼
┌──────────┐         ┌──────────┐         ┌──────────┐
│ Project  │         │ TaskCard │         │ TaskList │
│ Header   │────────►│ TaskForm │────────►│ Filters  │
│ Footer   │         │ Preview  │         │ Pipes    │
└──────────┘         └──────────┘         └──────────┘
                                               │
       ┌───────────────────────────────────────┘
       │
       ▼
Day 4: Services      Day 5: Routing       Day 6: HTTP
   │                      │                     │
   ▼                      ▼                     ▼
┌──────────┐         ┌──────────┐         ┌──────────┐
│ TaskSvc  │         │ Pages    │         │ API Svc  │
│ @Input   │────────►│ Guards   │────────►│ Intercpt │
│ @Output  │         │ Lazy Load│         │ RxJS     │
└──────────┘         └──────────┘         └──────────┘
                                               │
       ┌───────────────────────────────────────┘
       │
       ▼
Day 7: Forms         Day 8: Advanced      Day 9: Testing
   │                      │                     │
   ▼                      ▼                     ▼
┌──────────┐         ┌──────────┐         ┌──────────┐
│ Reactive │         │ Lifecycle│         │ Jasmine  │
│ Validate │────────►│ OnPush   │────────►│ Karma    │
│ Custom   │         │ Modules  │         │ Coverage │
└──────────┘         └──────────┘         └──────────┘
```

## Final Project Structure

After completing all exercises:

```
task-tracker/
├── src/app/
│   ├── core/                      # Singleton services (Day 8)
│   │   ├── components/
│   │   │   ├── header/
│   │   │   └── footer/
│   │   ├── guards/
│   │   │   └── auth.guard.ts
│   │   ├── interceptors/
│   │   │   ├── auth.interceptor.ts
│   │   │   └── logging.interceptor.ts
│   │   └── core.module.ts
│   │
│   ├── shared/                    # Reusable components (Day 8)
│   │   ├── components/
│   │   │   ├── priority-badge/
│   │   │   ├── loading-spinner/
│   │   │   └── toast/
│   │   ├── pipes/
│   │   │   ├── time-ago.pipe.ts
│   │   │   └── filter-tasks.pipe.ts
│   │   └── shared.module.ts
│   │
│   ├── features/                  # Feature modules
│   │   └── tasks/
│   │       ├── components/
│   │       │   ├── task-card/
│   │       │   ├── task-form/
│   │       │   └── task-list/
│   │       ├── pages/
│   │       │   ├── task-list-page/
│   │       │   └── task-detail/
│   │       └── tasks.module.ts
│   │
│   ├── pages/
│   │   ├── dashboard/
│   │   └── not-found/
│   │
│   ├── models/
│   │   └── task.model.ts
│   │
│   ├── services/
│   │   ├── task.service.ts
│   │   ├── api.service.ts
│   │   └── toast.service.ts
│   │
│   ├── validators/
│   │   └── task.validators.ts
│   │
│   ├── app.component.ts
│   ├── app.module.ts
│   └── app-routing.module.ts
│
├── angular.json
├── package.json
└── tsconfig.json
```

## Angular Concepts Covered

| Category | Concepts |
|----------|----------|
| **Components** | @Component, selectors, templates, styles, encapsulation |
| **Data Binding** | Interpolation, property, event, two-way, template refs |
| **Directives** | *ngIf, *ngFor, *ngSwitch, ngClass, ngStyle |
| **Pipes** | Built-in pipes, custom pipes, pure/impure |
| **Services** | @Injectable, providedIn, dependency injection |
| **Communication** | @Input, @Output, EventEmitter, @ViewChild |
| **Routing** | RouterModule, routerLink, route params, guards, lazy loading |
| **RxJS** | Observable, Subject, BehaviorSubject, operators, async pipe |
| **HTTP** | HttpClient, interceptors, error handling |
| **Forms** | Template-driven, reactive, FormBuilder, validators |
| **Lifecycle** | ngOnInit, ngOnChanges, ngOnDestroy, ngAfterViewInit |
| **Performance** | OnPush, trackBy, ChangeDetectorRef |
| **Modules** | NgModule, Feature, Shared, Core modules |
| **Testing** | Jasmine, Karma, TestBed, mocking, coverage |

## Getting Started

```bash
# Start Day 1 exercises
cd exercises
# Follow day1-setup-components.md

# Create project
ng new task-tracker --routing --style=css
cd task-tracker
ng serve --open
```

## Submission Guidelines

Each day's exercise includes a **Submission Checklist**. Ensure all items are checked before moving to the next day.

After completing all exercises:
1. Run `ng test --watch=false` - All tests should pass
2. Run `ng test --code-coverage` - Aim for 80%+ coverage
3. Run `ng build --configuration production` - Should build without errors
4. Verify all features work in the browser

## Tips for Success

1. **Don't skip days** - Each day builds on the previous
2. **Type the code** - Don't copy-paste; typing helps retention
3. **Experiment** - Try variations beyond the exercises
4. **Check console** - Fix errors as they appear
5. **Use DevTools** - Inspect components with Angular DevTools
6. **Read errors** - Angular error messages are helpful
7. **Commit often** - Save progress with git after each exercise

## Additional Resources

- [Angular Official Documentation](https://angular.io/docs)
- [Angular CLI Reference](https://angular.io/cli)
- [RxJS Documentation](https://rxjs.dev/)
- [Angular Style Guide](https://angular.io/guide/styleguide)
