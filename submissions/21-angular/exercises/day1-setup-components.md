# Day 1: Setup & First Components

## Learning Goals
- Set up an Angular project using CLI
- Understand project structure
- Create basic components
- Use interpolation `{{ }}`

---

## Exercise 1.1: Project Setup

### Task
Create a new Angular project called `task-tracker`.

### Steps
```bash
# Create new project with routing
ng new task-tracker --routing --style=css

# Navigate to project
cd task-tracker

# Start development server
ng serve --open
```

### Verify
- Browser opens at `http://localhost:4200`
- Default Angular page displays

---

## Exercise 1.2: Explore Project Structure

### Task
Identify and understand key files:

| File | Purpose |
|------|---------|
| `src/main.ts` | ? |
| `src/app/app.module.ts` | ? |
| `src/app/app.component.ts` | ? |
| `angular.json` | ? |
| `package.json` | ? |

### Questions
1. What is the root component selector?
2. Where is the root module bootstrapped?
3. What command would build for production?

---

## Exercise 1.3: Create Header Component

### Task
Create a header component that displays the app title.

### Steps
```bash
ng generate component components/header
# or shorthand
ng g c components/header
```

### Implementation
```typescript
// header.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  appTitle = 'Task Tracker';
  version = '1.0.0';
}
```

```html
<!-- header.component.html -->
<header class="app-header">
  <h1>{{ appTitle }}</h1>
  <span class="version">v{{ version }}</span>
</header>
```

```css
/* header.component.css */
.app-header {
  background: #1976d2;
  color: white;
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-header h1 {
  margin: 0;
  font-size: 1.5rem;
}

.version {
  font-size: 0.875rem;
  opacity: 0.8;
}
```

### Use in App Component
```html
<!-- app.component.html -->
<app-header></app-header>
<main class="container">
  <p>Welcome to Task Tracker!</p>
</main>
```

---

## Exercise 1.4: Create Footer Component

### Task
Create a footer component showing:
- Copyright with current year
- Author name (your name)

### Requirements
- Use interpolation for dynamic year
- Use `new Date().getFullYear()` in component

### Expected Output
```
© 2024 Your Name | Task Tracker
```

---

## Exercise 1.5: Create Task Stats Component

### Task
Create a component that displays task statistics using interpolation.

### Component Data
```typescript
export class TaskStatsComponent {
  totalTasks = 12;
  completedTasks = 5;
  pendingTasks = 7;

  get completionRate(): number {
    return Math.round((this.completedTasks / this.totalTasks) * 100);
  }
}
```

### Template Requirements
Display:
- Total tasks: 12
- Completed: 5
- Pending: 7
- Completion rate: 42%

### Styling
Create cards for each stat with a colored top border.

---

## Submission Checklist

- [ ] Project created with `ng new task-tracker --routing`
- [ ] Header component created and displays title
- [ ] Footer component created with dynamic year
- [ ] TaskStats component displays hardcoded stats
- [ ] All components render in app.component.html
- [ ] No console errors

## Project Structure After Day 1
```
task-tracker/
├── src/app/
│   ├── components/
│   │   ├── header/
│   │   ├── footer/
│   │   └── task-stats/
│   ├── app.component.ts
│   ├── app.component.html
│   └── app.module.ts
```

---

## Bonus Challenge
Create a `CurrentTime` component that displays the current time using interpolation.

**Hint**: Use `setInterval` in `ngOnInit` to update time every second (we'll learn proper cleanup later).
