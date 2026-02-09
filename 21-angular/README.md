# Angular (v16)

## Overview
Angular is a comprehensive framework for building single-page applications with TypeScript.

## Learning Objectives
By the end of this module, you will understand and be able to apply the key concepts and practices of Angular (v16).

## Topics Covered

### 1. [Angular Introduction](./topics/01-angular-introduction.md)
- What is Angular?
- Setup and Installation
- Angular CLI
- Angular Project Structure
- Single Page Applications (SPA)
- Webpack
- Node.js and npm

### 2. [Components](./topics/02-components.md)
- Component Lifecycle
- @Component Decorator
- Component Styles
- Change Detection
- Dynamic Components
- Event Emitters
- Sharing Data Between Components

### 3. [Modules](./topics/03-modules.md)
- NgModule Decorator
- Root Module
- Feature Modules
- Shared Modules

### 4. [Directives and Pipes](./topics/04-directives-and-pipes.md)
- Structural Directives (ngIf, ngFor, ngSwitch)
- Attribute Directives (ngClass, ngStyle, ngModel)
- Built-in Pipes
- Custom Pipes

### 5. [Dependency Injection](./topics/05-dependency-injection.md)
- DI in Angular
- Injector Hierarchy
- DI Providers
- Services
- Creating and Injecting Services
- Service Communication

### 6. [Routing](./topics/06-routing.md)
- Router Module
- Route Guards (CanActivate, CanDeactivate, Resolve)
- Routing and Navigation
- Lazy Loading
- Signals (Angular 16+)

### 7. [RxJS Fundamentals](./topics/07-rxjs-fundamentals.md)
- Observables and Subscriptions
- Pipe and Operators (map, filter, catchError)
- Subjects and BehaviorSubject
- Unsubscribing and Memory Leaks
- Common Patterns (async pipe, takeUntil)

### 8. [HTTP Client](./topics/08-http-client.md)
- Making HTTP Requests (GET, POST, PUT, DELETE)
- Error Handling
- HTTP Interceptors
- Best Practices

### 9. [Forms](./topics/09-forms.md)
- Template-Driven Forms
- Reactive Forms
- Form Validation
- Custom Validators
- Dynamic Forms

### 10. [State Management](./topics/10-state-management.md)
- Application State
- State Management Patterns
- Service-based State
- BehaviorSubject Pattern

### 11. [Testing and Debugging](./topics/11-testing-and-debugging.md)
- Jasmine Framework
- Karma Test Runner
- Testing Components, Services, and Pipes
- Debugging with Chrome DevTools
- Debugging with VS Code

## Key Concepts
For detailed explanations and comprehensive code examples, see the individual topic files linked above.

## Exercises
See the [exercises](./exercises/) directory for hands-on practice problems and solutions.

## Quick Reference

### Essential Commands
```bash
# Create new Angular app
ng new my-app

# Serve application
ng serve

# Generate component
ng g c component-name

# Generate service
ng g s service-name

# Build for production
ng build --configuration production

# Run tests
ng test

# Run linting
ng lint
```

### Key Concepts Summary

| Concept | Description |
|---------|-------------|
| Components | Building blocks with templates, styles, and logic |
| Modules | Containers for organizing related functionality |
| Services | Reusable business logic and data management |
| Routing | Navigation between views |
| HTTP Client | Communication with backend APIs |
| Forms | User input handling (Template-driven & Reactive) |
| Pipes | Data transformation in templates |
| Directives | DOM manipulation and behavior |

## Additional Resources

### Official Documentation
- [Angular Official Documentation](https://angular.io/docs)
- [Angular CLI Documentation](https://angular.io/cli)
- [Angular API Reference](https://angular.io/api)
- [RxJS Documentation](https://rxjs.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)

### Learning Resources
- [Angular University](https://angular-university.io/)
- [Angular Official Tutorial - Tour of Heroes](https://angular.io/tutorial)
- [Angular Official Style Guide](https://angular.io/guide/styleguide)

### Community
- [Angular Blog](https://blog.angular.io/)
- [Stack Overflow - Angular Tag](https://stackoverflow.com/questions/tagged/angular)
- [Angular Discord Community](https://discord.gg/angular)
- [Angular GitHub Repository](https://github.com/angular/angular)

## Assessment
Make sure you are comfortable with all topics listed above before proceeding to the next module.

## Next Steps
Continue to the next module in the curriculum sequence.

---
**Time Estimate:** 2 weeks | **Difficulty:** Intermediate | **Prerequisites:** Previous modules
