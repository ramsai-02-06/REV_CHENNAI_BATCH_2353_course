# Testing and Debugging

## Introduction to Testing in Angular

Testing is essential for ensuring code quality, catching bugs early, and maintaining confidence when refactoring. Angular provides robust testing tools and follows best practices for unit testing and end-to-end testing.

### Types of Testing

1. **Unit Testing**: Testing individual components, services, and pipes in isolation
2. **Integration Testing**: Testing how multiple units work together
3. **End-to-End (E2E) Testing**: Testing the entire application flow from a user's perspective

### Angular Testing Tools

- **Jasmine**: Testing framework for writing test specifications
- **Karma**: Test runner that executes tests in real browsers
- **TestBed**: Angular's testing utility for configuring and creating test modules
- **Protractor/Cypress**: E2E testing frameworks (Protractor deprecated, Cypress recommended)

---

## Jasmine

Jasmine is a behavior-driven development (BDD) framework for testing JavaScript code. It provides a clean syntax for writing tests.

### Jasmine Basics

```typescript
// Basic test suite
describe('Calculator', () => {
  // Test case
  it('should add two numbers correctly', () => {
    const result = 2 + 2;
    expect(result).toBe(4);
  });

  it('should subtract two numbers correctly', () => {
    const result = 5 - 3;
    expect(result).toBe(2);
  });

  it('should multiply two numbers correctly', () => {
    const result = 3 * 4;
    expect(result).toBe(12);
  });
});
```

### Jasmine Matchers

```typescript
describe('Jasmine Matchers', () => {
  it('should demonstrate common matchers', () => {
    // Equality
    expect(1 + 1).toBe(2);
    expect({ name: 'John' }).toEqual({ name: 'John' });

    // Truthiness
    expect(true).toBeTruthy();
    expect(false).toBeFalsy();
    expect(null).toBeNull();
    expect(undefined).toBeUndefined();
    expect(42).toBeDefined();

    // Comparisons
    expect(10).toBeGreaterThan(5);
    expect(5).toBeLessThan(10);
    expect(5).toBeGreaterThanOrEqual(5);
    expect(5).toBeLessThanOrEqual(5);

    // Strings
    expect('Hello World').toContain('World');
    expect('test@example.com').toMatch(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/);

    // Arrays
    expect([1, 2, 3]).toContain(2);
    expect([1, 2, 3]).toEqual(jasmine.arrayContaining([2, 3]));

    // Negation
    expect(5).not.toBe(10);

    // Type checking
    expect(typeof 'hello').toBe('string');
    expect(Array.isArray([1, 2, 3])).toBe(true);
  });
});
```

### Setup and Teardown

```typescript
describe('Setup and Teardown', () => {
  let counter: number;

  // Runs once before all tests in this suite
  beforeAll(() => {
    console.log('Before all tests');
  });

  // Runs before each test
  beforeEach(() => {
    counter = 0;
  });

  // Runs after each test
  afterEach(() => {
    counter = 0;
  });

  // Runs once after all tests in this suite
  afterAll(() => {
    console.log('After all tests');
  });

  it('should start with counter at 0', () => {
    expect(counter).toBe(0);
  });

  it('should increment counter', () => {
    counter++;
    expect(counter).toBe(1);
  });
});
```

### Spies

```typescript
describe('Jasmine Spies', () => {
  let calculator: any;

  beforeEach(() => {
    calculator = {
      add: (a: number, b: number) => a + b,
      subtract: (a: number, b: number) => a - b
    };
  });

  it('should spy on a method', () => {
    spyOn(calculator, 'add');
    calculator.add(2, 3);

    expect(calculator.add).toHaveBeenCalled();
    expect(calculator.add).toHaveBeenCalledWith(2, 3);
    expect(calculator.add).toHaveBeenCalledTimes(1);
  });

  it('should spy and return a value', () => {
    spyOn(calculator, 'add').and.returnValue(100);
    const result = calculator.add(2, 3);

    expect(result).toBe(100);
  });

  it('should spy and call through', () => {
    spyOn(calculator, 'add').and.callThrough();
    const result = calculator.add(2, 3);

    expect(result).toBe(5);
    expect(calculator.add).toHaveBeenCalled();
  });

  it('should spy and throw error', () => {
    spyOn(calculator, 'add').and.throwError('Error occurred');

    expect(() => calculator.add(2, 3)).toThrowError('Error occurred');
  });

  it('should create a spy', () => {
    const spy = jasmine.createSpy('spy');
    spy(1, 2, 3);

    expect(spy).toHaveBeenCalledWith(1, 2, 3);
  });
});
```

### Async Testing

```typescript
describe('Async Tests', () => {
  it('should handle async operations with done callback', (done) => {
    setTimeout(() => {
      expect(true).toBe(true);
      done();
    }, 1000);
  });

  it('should handle promises', async () => {
    const promise = Promise.resolve(42);
    const result = await promise;
    expect(result).toBe(42);
  });

  it('should handle async/await', async () => {
    const fetchData = async () => {
      return new Promise(resolve => {
        setTimeout(() => resolve('data'), 100);
      });
    };

    const data = await fetchData();
    expect(data).toBe('data');
  });
});
```

---

## Karma

Karma is a test runner that executes tests in real browsers and provides test results.

### Karma Configuration

```javascript
// karma.conf.js
module.exports = function(config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('karma-coverage'),
      require('@angular-devkit/build-angular/plugins/karma')
    ],
    client: {
      jasmine: {
        // Jasmine configuration
        random: false
      },
      clearContext: false
    },
    jasmineHtmlReporter: {
      suppressAll: true
    },
    coverageReporter: {
      dir: require('path').join(__dirname, './coverage'),
      subdir: '.',
      reporters: [
        { type: 'html' },
        { type: 'text-summary' }
      ]
    },
    reporters: ['progress', 'kjhtml'],
    port: 9876,
    colors: true,
    logLevel: config.LOG_INFO,
    autoWatch: true,
    browsers: ['Chrome'],
    singleRun: false,
    restartOnFileChange: true
  });
};
```

### Running Tests

```bash
# Run tests once
ng test

# Run tests in headless mode
ng test --browsers=ChromeHeadless

# Run tests with code coverage
ng test --code-coverage

# Run specific test file
ng test --include='**/my.component.spec.ts'

# Watch mode (default)
ng test --watch

# Single run
ng test --watch=false
```

---

## Testing with Jasmine and Karma

### Testing Components

```typescript
// counter.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-counter',
  template: `
    <div>
      <h2>Count: {{ count }}</h2>
      <button (click)="increment()">Increment</button>
      <button (click)="decrement()">Decrement</button>
    </div>
  `
})
export class CounterComponent {
  count: number = 0;

  increment(): void {
    this.count++;
  }

  decrement(): void {
    this.count--;
  }
}

// counter.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CounterComponent } from './counter.component';

describe('CounterComponent', () => {
  let component: CounterComponent;
  let fixture: ComponentFixture<CounterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CounterComponent ]
    }).compileComponents();

    fixture = TestBed.createComponent(CounterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should start with count at 0', () => {
    expect(component.count).toBe(0);
  });

  it('should increment count', () => {
    component.increment();
    expect(component.count).toBe(1);
  });

  it('should decrement count', () => {
    component.decrement();
    expect(component.count).toBe(-1);
  });

  it('should display count in template', () => {
    component.count = 5;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('h2').textContent).toContain('Count: 5');
  });

  it('should increment when button is clicked', () => {
    const button = fixture.nativeElement.querySelector('button');
    button.click();
    fixture.detectChanges();

    expect(component.count).toBe(1);
  });
});
```

### Testing Services

```typescript
// user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'https://api.example.com/users';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }
}

// user.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService, User } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();  // Verify no outstanding requests
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch users', () => {
    const mockUsers: User[] = [
      { id: 1, name: 'John', email: 'john@example.com' },
      { id: 2, name: 'Jane', email: 'jane@example.com' }
    ];

    service.getUsers().subscribe(users => {
      expect(users.length).toBe(2);
      expect(users).toEqual(mockUsers);
    });

    const req = httpMock.expectOne('https://api.example.com/users');
    expect(req.request.method).toBe('GET');
    req.flush(mockUsers);
  });

  it('should fetch a single user', () => {
    const mockUser: User = { id: 1, name: 'John', email: 'john@example.com' };

    service.getUser(1).subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('https://api.example.com/users/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should create a user', () => {
    const newUser: User = { id: 3, name: 'Bob', email: 'bob@example.com' };

    service.createUser(newUser).subscribe(user => {
      expect(user).toEqual(newUser);
    });

    const req = httpMock.expectOne('https://api.example.com/users');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newUser);
    req.flush(newUser);
  });

  it('should handle errors', () => {
    service.getUsers().subscribe(
      () => fail('should have failed'),
      (error) => {
        expect(error.status).toBe(500);
      }
    );

    const req = httpMock.expectOne('https://api.example.com/users');
    req.flush('Error', { status: 500, statusText: 'Server Error' });
  });
});
```

### Testing Components with Dependencies

```typescript
// user-list.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService, User } from '../services/user.service';

@Component({
  selector: 'app-user-list',
  template: `
    <div *ngIf="loading">Loading...</div>
    <ul *ngIf="!loading">
      <li *ngFor="let user of users">{{ user.name }}</li>
    </ul>
  `
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  loading: boolean = false;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.getUsers().subscribe(users => {
      this.users = users;
      this.loading = false;
    });
  }
}

// user-list.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { UserListComponent } from './user-list.component';
import { UserService } from '../services/user.service';

describe('UserListComponent', () => {
  let component: UserListComponent;
  let fixture: ComponentFixture<UserListComponent>;
  let userService: jasmine.SpyObj<UserService>;

  beforeEach(async () => {
    const userServiceSpy = jasmine.createSpyObj('UserService', ['getUsers']);

    await TestBed.configureTestingModule({
      declarations: [ UserListComponent ],
      providers: [
        { provide: UserService, useValue: userServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(UserListComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users on init', () => {
    const mockUsers = [
      { id: 1, name: 'John', email: 'john@example.com' },
      { id: 2, name: 'Jane', email: 'jane@example.com' }
    ];

    userService.getUsers.and.returnValue(of(mockUsers));

    component.ngOnInit();

    expect(component.users).toEqual(mockUsers);
    expect(component.loading).toBe(false);
  });

  it('should display loading message', () => {
    component.loading = true;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    expect(compiled.textContent).toContain('Loading...');
  });

  it('should display users', () => {
    component.users = [
      { id: 1, name: 'John', email: 'john@example.com' }
    ];
    component.loading = false;
    fixture.detectChanges();

    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('li').textContent).toContain('John');
  });
});
```

### Testing Directives

```typescript
// highlight.directive.ts
import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appHighlight]'
})
export class HighlightDirective {
  @Input() appHighlight: string = 'yellow';

  constructor(private el: ElementRef) {}

  @HostListener('mouseenter') onMouseEnter(): void {
    this.highlight(this.appHighlight);
  }

  @HostListener('mouseleave') onMouseLeave(): void {
    this.highlight('');
  }

  private highlight(color: string): void {
    this.el.nativeElement.style.backgroundColor = color;
  }
}

// highlight.directive.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { HighlightDirective } from './highlight.directive';

@Component({
  template: '<p appHighlight="yellow">Test</p>'
})
class TestComponent {}

describe('HighlightDirective', () => {
  let fixture: ComponentFixture<TestComponent>;
  let paragraph: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ HighlightDirective, TestComponent ]
    });

    fixture = TestBed.createComponent(TestComponent);
    paragraph = fixture.debugElement.query(By.css('p'));
  });

  it('should highlight on mouseenter', () => {
    paragraph.triggerEventHandler('mouseenter', null);
    fixture.detectChanges();

    expect(paragraph.nativeElement.style.backgroundColor).toBe('yellow');
  });

  it('should remove highlight on mouseleave', () => {
    paragraph.triggerEventHandler('mouseenter', null);
    paragraph.triggerEventHandler('mouseleave', null);
    fixture.detectChanges();

    expect(paragraph.nativeElement.style.backgroundColor).toBe('');
  });
});
```

### Testing Pipes

```typescript
// truncate.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  transform(value: string, limit: number = 50): string {
    if (!value) return '';
    return value.length > limit ? value.substring(0, limit) + '...' : value;
  }
}

// truncate.pipe.spec.ts
import { TruncatePipe } from './truncate.pipe';

describe('TruncatePipe', () => {
  let pipe: TruncatePipe;

  beforeEach(() => {
    pipe = new TruncatePipe();
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return the same string if shorter than limit', () => {
    const result = pipe.transform('Hello', 10);
    expect(result).toBe('Hello');
  });

  it('should truncate string longer than limit', () => {
    const result = pipe.transform('This is a long string', 10);
    expect(result).toBe('This is a ...');
  });

  it('should use default limit of 50', () => {
    const longString = 'a'.repeat(60);
    const result = pipe.transform(longString);
    expect(result.length).toBe(53);  // 50 + '...'
  });

  it('should handle empty string', () => {
    const result = pipe.transform('');
    expect(result).toBe('');
  });
});
```

---

## Debugging Angular Applications

### Chrome DevTools

#### Using Breakpoints

1. Open Chrome DevTools (F12 or Cmd+Option+I)
2. Go to Sources tab
3. Find your TypeScript file (webpack:// folder)
4. Click line number to set breakpoint
5. Reload page or trigger action
6. Inspect variables in Scope panel

#### Console Debugging

```typescript
@Component({
  selector: 'app-debug-demo',
  template: '<button (click)="handleClick()">Click Me</button>'
})
export class DebugDemoComponent {
  data = { name: 'John', age: 30 };

  handleClick(): void {
    console.log('Button clicked');
    console.log('Data:', this.data);
    console.table(this.data);
    console.warn('Warning message');
    console.error('Error message');

    // Group logs
    console.group('User Info');
    console.log('Name:', this.data.name);
    console.log('Age:', this.data.age);
    console.groupEnd();

    // Performance measurement
    console.time('Operation');
    // ... some operation
    console.timeEnd('Operation');

    // Conditional logging
    console.assert(this.data.age > 18, 'User must be adult');

    // Debugger statement
    debugger;  // Pauses execution
  }
}
```

#### Network Tab

Monitor HTTP requests:
1. Open DevTools > Network tab
2. Filter by XHR to see API calls
3. Click request to see headers, payload, response
4. Check timing information

#### Angular DevTools Extension

```bash
# Install Angular DevTools Chrome extension
# Features:
# - Component tree inspector
# - Change detection profiler
# - Dependency injection tree
# - Router inspector
```

### VS Code Debugging

#### launch.json Configuration

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "chrome",
      "request": "launch",
      "name": "Debug Angular App",
      "url": "http://localhost:4200",
      "webRoot": "${workspaceFolder}",
      "sourceMapPathOverrides": {
        "webpack:/*": "${webRoot}/*",
        "/./*": "${webRoot}/*",
        "/src/*": "${webRoot}/*",
        "/*": "*",
        "/./~/*": "${webRoot}/node_modules/*"
      }
    },
    {
      "type": "chrome",
      "request": "attach",
      "name": "Attach to Chrome",
      "port": 9222,
      "webRoot": "${workspaceFolder}"
    }
  ]
}
```

#### Debugging Steps

1. Set breakpoints in VS Code (click left of line number)
2. Press F5 or click Run > Start Debugging
3. Application opens in Chrome
4. Execution pauses at breakpoints
5. Use Debug toolbar to step through code

### Common Debugging Techniques

#### Change Detection Issues

```typescript
import { ChangeDetectorRef } from '@angular/core';

@Component({...})
export class MyComponent {
  constructor(private cdr: ChangeDetectorRef) {}

  someMethod(): void {
    // Manually trigger change detection
    this.cdr.detectChanges();

    // Mark for check (OnPush strategy)
    this.cdr.markForCheck();

    // Detach from change detection
    this.cdr.detach();

    // Reattach to change detection
    this.cdr.reattach();
  }
}
```

#### RxJS Debugging

```typescript
import { tap } from 'rxjs/operators';

this.userService.getUsers().pipe(
  tap(data => console.log('Data received:', data)),
  tap(data => debugger),  // Pause execution
  map(data => data.filter(user => user.active))
).subscribe(users => {
  console.log('Filtered users:', users);
});
```

#### Memory Leaks

```typescript
import { Component, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({...})
export class MyComponent implements OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.dataService.data$
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        // Handle data
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Jasmine | BDD testing framework with matchers and spies |
| Karma | Test runner for executing tests in browsers |
| TestBed | Angular testing utility for component/service tests |
| Chrome DevTools | Browser debugging with breakpoints and console |
| VS Code Debugging | IDE-based debugging with launch configurations |
| Best Practices | Proper setup/teardown, mock dependencies, unsubscribe |

---

**Congratulations!** You've completed all the Angular topics. Continue practicing and building applications to master Angular development.
