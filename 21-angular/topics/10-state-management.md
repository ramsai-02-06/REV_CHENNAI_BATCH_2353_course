# State Management

## Understanding Application State

Application state represents data that determines what users see and how the application behaves at any given moment. Managing state effectively is crucial for building maintainable Angular applications.

### Types of State

1. **Local Component State**: Data used only within a single component
2. **Shared State**: Data shared between multiple components
3. **Application State**: Global data accessible throughout the application
4. **Persistent State**: Data that survives page refreshes (localStorage, sessionStorage)
5. **Server State**: Data synchronized with the backend

### State Management Challenges

- Data consistency across components
- Avoiding prop drilling (passing data through multiple levels)
- Handling asynchronous operations
- Maintaining data integrity
- Debugging state changes

---

## State Management Patterns

### 1. Component State (Local State)

The simplest form of state management - data managed within a single component.

```typescript
// counter.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-counter',
  template: `
    <div>
      <h2>Counter: {{ count }}</h2>
      <button (click)="increment()">+</button>
      <button (click)="decrement()">-</button>
      <button (click)="reset()">Reset</button>
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

  reset(): void {
    this.count = 0;
  }
}
```

### 2. Input/Output Pattern

Parent-child communication using @Input and @Output.

```typescript
// parent.component.ts
@Component({
  selector: 'app-parent',
  template: `
    <app-child
      [items]="items"
      (itemSelected)="onItemSelected($event)">
    </app-child>
    <p>Selected: {{ selectedItem }}</p>
  `
})
export class ParentComponent {
  items = ['Item 1', 'Item 2', 'Item 3'];
  selectedItem: string = '';

  onItemSelected(item: string): void {
    this.selectedItem = item;
  }
}

// child.component.ts
@Component({
  selector: 'app-child',
  template: `
    <ul>
      <li *ngFor="let item of items" (click)="select(item)">
        {{ item }}
      </li>
    </ul>
  `
})
export class ChildComponent {
  @Input() items: string[] = [];
  @Output() itemSelected = new EventEmitter<string>();

  select(item: string): void {
    this.itemSelected.emit(item);
  }
}
```

### 3. Template Reference Variables

Share data within templates using template reference variables.

```html
<input #userInput type="text">
<button (click)="processInput(userInput.value)">Submit</button>
<p>You typed: {{ userInput.value }}</p>
```

### 4. ViewChild/ViewChildren

Access child components or DOM elements directly.

```typescript
@Component({
  selector: 'app-parent',
  template: `
    <app-child #childComponent></app-child>
    <button (click)="callChildMethod()">Call Child Method</button>
  `
})
export class ParentComponent {
  @ViewChild('childComponent') child!: ChildComponent;

  callChildMethod(): void {
    this.child.someMethod();
  }
}
```

---

## Service-Based State Management

Services provide a centralized way to manage and share state across components.

### Basic State Service

```typescript
// cart.service.ts
import { Injectable } from '@angular/core';

export interface CartItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private items: CartItem[] = [];

  addItem(item: CartItem): void {
    const existingItem = this.items.find(i => i.id === item.id);

    if (existingItem) {
      existingItem.quantity += item.quantity;
    } else {
      this.items.push({ ...item });
    }
  }

  removeItem(id: number): void {
    this.items = this.items.filter(item => item.id !== id);
  }

  getItems(): CartItem[] {
    return [...this.items];  // Return copy to prevent direct mutation
  }

  getTotal(): number {
    return this.items.reduce((total, item) =>
      total + (item.price * item.quantity), 0
    );
  }

  clearCart(): void {
    this.items = [];
  }

  getItemCount(): number {
    return this.items.reduce((count, item) => count + item.quantity, 0);
  }
}
```

### Using State Service

```typescript
// product-list.component.ts
@Component({
  selector: 'app-product-list',
  template: `
    <div *ngFor="let product of products">
      <h3>{{ product.name }}</h3>
      <p>Price: ${{ product.price }}</p>
      <button (click)="addToCart(product)">Add to Cart</button>
    </div>
  `
})
export class ProductListComponent {
  products = [
    { id: 1, name: 'Laptop', price: 999 },
    { id: 2, name: 'Mouse', price: 25 }
  ];

  constructor(private cartService: CartService) {}

  addToCart(product: any): void {
    this.cartService.addItem({
      id: product.id,
      name: product.name,
      price: product.price,
      quantity: 1
    });
  }
}

// cart-display.component.ts
@Component({
  selector: 'app-cart-display',
  template: `
    <div>
      <h2>Shopping Cart</h2>
      <div *ngFor="let item of items">
        {{ item.name }} - Qty: {{ item.quantity }} - ${{ item.price * item.quantity }}
        <button (click)="remove(item.id)">Remove</button>
      </div>
      <p>Total: ${{ total }}</p>
    </div>
  `
})
export class CartDisplayComponent implements OnInit {
  items: CartItem[] = [];
  total: number = 0;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.updateCart();
  }

  updateCart(): void {
    this.items = this.cartService.getItems();
    this.total = this.cartService.getTotal();
  }

  remove(id: number): void {
    this.cartService.removeItem(id);
    this.updateCart();
  }
}
```

---

## BehaviorSubject Pattern

BehaviorSubject provides reactive state management with observables.

### BehaviorSubject State Service

```typescript
// user-state.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserStateService {
  // Private BehaviorSubject
  private currentUserSubject = new BehaviorSubject<User | null>(null);

  // Public Observable
  public currentUser$: Observable<User | null> =
    this.currentUserSubject.asObservable();

  // Get current value
  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  // Update user
  setUser(user: User | null): void {
    this.currentUserSubject.next(user);
  }

  // Update specific fields
  updateUser(updates: Partial<User>): void {
    const currentUser = this.currentUserValue;
    if (currentUser) {
      this.currentUserSubject.next({ ...currentUser, ...updates });
    }
  }

  // Clear user
  clearUser(): void {
    this.currentUserSubject.next(null);
  }

  // Check if user is logged in
  isLoggedIn(): boolean {
    return this.currentUserValue !== null;
  }
}
```

### Advanced BehaviorSubject State Service

```typescript
// todo-state.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface Todo {
  id: number;
  title: string;
  completed: boolean;
  priority: 'low' | 'medium' | 'high';
}

interface TodoState {
  todos: Todo[];
  filter: 'all' | 'active' | 'completed';
  loading: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TodoStateService {
  private initialState: TodoState = {
    todos: [],
    filter: 'all',
    loading: false
  };

  private stateSubject = new BehaviorSubject<TodoState>(this.initialState);
  public state$: Observable<TodoState> = this.stateSubject.asObservable();

  // Derived observables
  public todos$: Observable<Todo[]> = this.state$.pipe(
    map(state => this.getFilteredTodos(state))
  );

  public loading$: Observable<boolean> = this.state$.pipe(
    map(state => state.loading)
  );

  public activeTodoCount$: Observable<number> = this.state$.pipe(
    map(state => state.todos.filter(t => !t.completed).length)
  );

  private get state(): TodoState {
    return this.stateSubject.value;
  }

  private setState(state: TodoState): void {
    this.stateSubject.next(state);
  }

  private getFilteredTodos(state: TodoState): Todo[] {
    const { todos, filter } = state;

    switch (filter) {
      case 'active':
        return todos.filter(t => !t.completed);
      case 'completed':
        return todos.filter(t => t.completed);
      default:
        return todos;
    }
  }

  // Actions
  addTodo(title: string, priority: 'low' | 'medium' | 'high' = 'medium'): void {
    const newTodo: Todo = {
      id: Date.now(),
      title,
      completed: false,
      priority
    };

    this.setState({
      ...this.state,
      todos: [...this.state.todos, newTodo]
    });
  }

  toggleTodo(id: number): void {
    this.setState({
      ...this.state,
      todos: this.state.todos.map(todo =>
        todo.id === id ? { ...todo, completed: !todo.completed } : todo
      )
    });
  }

  deleteTodo(id: number): void {
    this.setState({
      ...this.state,
      todos: this.state.todos.filter(todo => todo.id !== id)
    });
  }

  updateTodo(id: number, updates: Partial<Todo>): void {
    this.setState({
      ...this.state,
      todos: this.state.todos.map(todo =>
        todo.id === id ? { ...todo, ...updates } : todo
      )
    });
  }

  setFilter(filter: 'all' | 'active' | 'completed'): void {
    this.setState({
      ...this.state,
      filter
    });
  }

  setLoading(loading: boolean): void {
    this.setState({
      ...this.state,
      loading
    });
  }

  clearCompleted(): void {
    this.setState({
      ...this.state,
      todos: this.state.todos.filter(todo => !todo.completed)
    });
  }

  loadTodos(todos: Todo[]): void {
    this.setState({
      ...this.state,
      todos
    });
  }
}
```

### Using BehaviorSubject State Service

```typescript
// todo-list.component.ts
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { TodoStateService, Todo } from './todo-state.service';

@Component({
  selector: 'app-todo-list',
  template: `
    <div>
      <!-- Loading indicator -->
      <div *ngIf="loading$ | async" class="loading">Loading...</div>

      <!-- Add todo form -->
      <div class="add-todo">
        <input [(ngModel)]="newTodoTitle" placeholder="What needs to be done?">
        <select [(ngModel)]="newTodoPriority">
          <option value="low">Low</option>
          <option value="medium">Medium</option>
          <option value="high">High</option>
        </select>
        <button (click)="addTodo()">Add</button>
      </div>

      <!-- Filter buttons -->
      <div class="filters">
        <button (click)="setFilter('all')">All</button>
        <button (click)="setFilter('active')">Active</button>
        <button (click)="setFilter('completed')">Completed</button>
      </div>

      <!-- Todo list -->
      <ul>
        <li *ngFor="let todo of todos$ | async"
            [class.completed]="todo.completed"
            [class.priority-high]="todo.priority === 'high'">
          <input
            type="checkbox"
            [checked]="todo.completed"
            (change)="toggleTodo(todo.id)">
          <span>{{ todo.title }}</span>
          <button (click)="deleteTodo(todo.id)">Delete</button>
        </li>
      </ul>

      <!-- Footer -->
      <div class="footer">
        <span>{{ activeTodoCount$ | async }} items left</span>
        <button (click)="clearCompleted()">Clear completed</button>
      </div>
    </div>
  `
})
export class TodoListComponent implements OnInit {
  todos$!: Observable<Todo[]>;
  loading$!: Observable<boolean>;
  activeTodoCount$!: Observable<number>;

  newTodoTitle: string = '';
  newTodoPriority: 'low' | 'medium' | 'high' = 'medium';

  constructor(private todoState: TodoStateService) {}

  ngOnInit(): void {
    this.todos$ = this.todoState.todos$;
    this.loading$ = this.todoState.loading$;
    this.activeTodoCount$ = this.todoState.activeTodoCount$;
  }

  addTodo(): void {
    if (this.newTodoTitle.trim()) {
      this.todoState.addTodo(this.newTodoTitle, this.newTodoPriority);
      this.newTodoTitle = '';
    }
  }

  toggleTodo(id: number): void {
    this.todoState.toggleTodo(id);
  }

  deleteTodo(id: number): void {
    this.todoState.deleteTodo(id);
  }

  setFilter(filter: 'all' | 'active' | 'completed'): void {
    this.todoState.setFilter(filter);
  }

  clearCompleted(): void {
    this.todoState.clearCompleted();
  }
}
```

---

## Advanced State Management Patterns

### Store Pattern with Actions

```typescript
// state/actions.ts
export enum ActionType {
  ADD_ITEM = '[Cart] Add Item',
  REMOVE_ITEM = '[Cart] Remove Item',
  UPDATE_QUANTITY = '[Cart] Update Quantity',
  CLEAR_CART = '[Cart] Clear'
}

export interface Action {
  type: ActionType;
  payload?: any;
}

// state/cart.store.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface CartItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

interface CartState {
  items: CartItem[];
  total: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartStore {
  private initialState: CartState = {
    items: [],
    total: 0
  };

  private stateSubject = new BehaviorSubject<CartState>(this.initialState);
  public state$: Observable<CartState> = this.stateSubject.asObservable();

  private get state(): CartState {
    return this.stateSubject.value;
  }

  private setState(state: CartState): void {
    this.stateSubject.next(state);
  }

  dispatch(action: Action): void {
    const newState = this.reduce(this.state, action);
    this.setState(newState);
  }

  private reduce(state: CartState, action: Action): CartState {
    switch (action.type) {
      case ActionType.ADD_ITEM:
        return this.addItemReducer(state, action.payload);

      case ActionType.REMOVE_ITEM:
        return this.removeItemReducer(state, action.payload);

      case ActionType.UPDATE_QUANTITY:
        return this.updateQuantityReducer(state, action.payload);

      case ActionType.CLEAR_CART:
        return this.initialState;

      default:
        return state;
    }
  }

  private addItemReducer(state: CartState, item: CartItem): CartState {
    const existingItem = state.items.find(i => i.id === item.id);
    let items: CartItem[];

    if (existingItem) {
      items = state.items.map(i =>
        i.id === item.id
          ? { ...i, quantity: i.quantity + item.quantity }
          : i
      );
    } else {
      items = [...state.items, item];
    }

    return {
      items,
      total: this.calculateTotal(items)
    };
  }

  private removeItemReducer(state: CartState, itemId: number): CartState {
    const items = state.items.filter(i => i.id !== itemId);
    return {
      items,
      total: this.calculateTotal(items)
    };
  }

  private updateQuantityReducer(state: CartState, payload: { id: number, quantity: number }): CartState {
    const items = state.items.map(i =>
      i.id === payload.id ? { ...i, quantity: payload.quantity } : i
    );
    return {
      items,
      total: this.calculateTotal(items)
    };
  }

  private calculateTotal(items: CartItem[]): number {
    return items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }
}

// Usage
@Component({
  selector: 'app-product',
  template: '<button (click)="addToCart()">Add to Cart</button>'
})
export class ProductComponent {
  constructor(private cartStore: CartStore) {}

  addToCart(): void {
    this.cartStore.dispatch({
      type: ActionType.ADD_ITEM,
      payload: {
        id: 1,
        name: 'Product',
        price: 99,
        quantity: 1
      }
    });
  }
}
```

### Persistent State with LocalStorage

```typescript
// local-storage.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  set<T>(key: string, value: T): void {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error('Error saving to localStorage', error);
    }
  }

  get<T>(key: string): T | null {
    try {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    } catch (error) {
      console.error('Error reading from localStorage', error);
      return null;
    }
  }

  remove(key: string): void {
    localStorage.removeItem(key);
  }

  clear(): void {
    localStorage.clear();
  }
}

// persistent-state.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { LocalStorageService } from './local-storage.service';

interface AppSettings {
  theme: 'light' | 'dark';
  language: string;
  notifications: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class SettingsStateService {
  private readonly STORAGE_KEY = 'app_settings';

  private defaultSettings: AppSettings = {
    theme: 'light',
    language: 'en',
    notifications: true
  };

  private settingsSubject: BehaviorSubject<AppSettings>;
  public settings$: Observable<AppSettings>;

  constructor(private storage: LocalStorageService) {
    // Load from localStorage or use defaults
    const saved = this.storage.get<AppSettings>(this.STORAGE_KEY);
    this.settingsSubject = new BehaviorSubject<AppSettings>(
      saved || this.defaultSettings
    );
    this.settings$ = this.settingsSubject.asObservable();

    // Save to localStorage on every change
    this.settings$.subscribe(settings => {
      this.storage.set(this.STORAGE_KEY, settings);
    });
  }

  updateSettings(updates: Partial<AppSettings>): void {
    const current = this.settingsSubject.value;
    this.settingsSubject.next({ ...current, ...updates });
  }

  resetSettings(): void {
    this.settingsSubject.next(this.defaultSettings);
  }
}
```

### State Selectors

```typescript
// state/selectors.ts
import { Observable } from 'rxjs';
import { map, distinctUntilChanged } from 'rxjs/operators';

export function select<T, R>(
  source$: Observable<T>,
  selector: (state: T) => R
): Observable<R> {
  return source$.pipe(
    map(selector),
    distinctUntilChanged()
  );
}

// Usage
@Injectable({
  providedIn: 'root'
})
export class TodoStateService {
  // ... state implementation

  // Selectors
  selectTodos(): Observable<Todo[]> {
    return select(this.state$, state => state.todos);
  }

  selectActiveTodos(): Observable<Todo[]> {
    return select(this.state$, state =>
      state.todos.filter(t => !t.completed)
    );
  }

  selectCompletedTodos(): Observable<Todo[]> {
    return select(this.state$, state =>
      state.todos.filter(t => t.completed)
    );
  }

  selectTodoById(id: number): Observable<Todo | undefined> {
    return select(this.state$, state =>
      state.todos.find(t => t.id === id)
    );
  }
}
```

---

## Best Practices

### 1. Immutable State Updates

```typescript
// Bad - Direct mutation
this.state.items.push(newItem);

// Good - Immutable update
this.setState({
  ...this.state,
  items: [...this.state.items, newItem]
});
```

### 2. Single Source of Truth

```typescript
// Keep one source of truth
private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
public cartItems$ = this.cartItemsSubject.asObservable();

// Derive other values
public itemCount$ = this.cartItems$.pipe(
  map(items => items.reduce((count, item) => count + item.quantity, 0))
);

public total$ = this.cartItems$.pipe(
  map(items => items.reduce((sum, item) => sum + item.price * item.quantity, 0))
);
```

### 3. Unsubscribe from Observables

```typescript
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({...})
export class MyComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.stateService.state$
      .pipe(takeUntil(this.destroy$))
      .subscribe(state => {
        // Handle state
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

### 4. Use Async Pipe

```typescript
// Component
@Component({
  template: `
    <div *ngFor="let item of items$ | async">
      {{ item.name }}
    </div>
  `
})
export class MyComponent {
  items$ = this.stateService.items$;

  constructor(private stateService: StateService) {}
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Application State | Data that determines application behavior |
| Service-Based State | Centralized state management with services |
| BehaviorSubject | Reactive state with observables |
| State Patterns | Actions, reducers, selectors |
| Persistence | LocalStorage for persistent state |
| Best Practices | Immutability, single source of truth, async pipe |

## Next Topic

Continue to [Testing and Debugging](./11-testing-and-debugging.md) to learn about testing Angular applications.
