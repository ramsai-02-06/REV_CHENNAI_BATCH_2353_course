# Angular Forms

## Introduction to Forms

Angular provides two approaches to handling user input through forms:

1. **Template-Driven Forms**: Uses directives in the template to create and manipulate the form model
2. **Reactive Forms**: Provides a model-driven approach to handling form inputs with explicit and immutable data flow

### Choosing Between Template-Driven and Reactive Forms

| Feature | Template-Driven | Reactive |
|---------|----------------|----------|
| Form model setup | Implicit, created by directives | Explicit, created in component |
| Data model | Mutable | Immutable |
| Form validation | Directives | Functions |
| Testability | Difficult | Easy |
| Best for | Simple forms | Complex forms |

---

## Template-Driven Forms

Template-driven forms rely on directives in the template to create and manipulate the underlying form model.

### Setting Up Template-Driven Forms

```typescript
// app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';  // Import FormsModule

@NgModule({
  imports: [
    BrowserModule,
    FormsModule  // Add to imports
  ]
})
export class AppModule { }
```

### Basic Template-Driven Form

```typescript
// login.component.ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  template: `
    <form #loginForm="ngForm" (ngSubmit)="onSubmit(loginForm)">
      <div>
        <label for="username">Username:</label>
        <input
          type="text"
          id="username"
          name="username"
          [(ngModel)]="model.username"
          required
          #username="ngModel">
        <div *ngIf="username.invalid && username.touched" class="error">
          Username is required
        </div>
      </div>

      <div>
        <label for="password">Password:</label>
        <input
          type="password"
          id="password"
          name="password"
          [(ngModel)]="model.password"
          required
          minlength="6"
          #password="ngModel">
        <div *ngIf="password.invalid && password.touched" class="error">
          <div *ngIf="password.errors?.['required']">Password is required</div>
          <div *ngIf="password.errors?.['minlength']">
            Password must be at least 6 characters
          </div>
        </div>
      </div>

      <button type="submit" [disabled]="loginForm.invalid">Login</button>
    </form>

    <div>Form Valid: {{ loginForm.valid }}</div>
    <div>Form Value: {{ loginForm.value | json }}</div>
  `
})
export class LoginComponent {
  model = {
    username: '',
    password: ''
  };

  onSubmit(form: any): void {
    if (form.valid) {
      console.log('Form submitted:', this.model);
    }
  }
}
```

### Template-Driven Form with Validation

```typescript
// user-form.component.ts
import { Component } from '@angular/core';

interface User {
  firstName: string;
  lastName: string;
  email: string;
  age: number;
  country: string;
}

@Component({
  selector: 'app-user-form',
  template: `
    <form #userForm="ngForm" (ngSubmit)="onSubmit()">
      <!-- First Name -->
      <div class="form-group">
        <label for="firstName">First Name</label>
        <input
          type="text"
          id="firstName"
          name="firstName"
          [(ngModel)]="user.firstName"
          required
          minlength="2"
          #firstName="ngModel"
          class="form-control"
          [class.is-invalid]="firstName.invalid && firstName.touched">
        <div *ngIf="firstName.invalid && firstName.touched" class="error">
          <small *ngIf="firstName.errors?.['required']">First name is required</small>
          <small *ngIf="firstName.errors?.['minlength']">
            First name must be at least 2 characters
          </small>
        </div>
      </div>

      <!-- Email -->
      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          name="email"
          [(ngModel)]="user.email"
          required
          email
          #email="ngModel"
          class="form-control"
          [class.is-invalid]="email.invalid && email.touched">
        <div *ngIf="email.invalid && email.touched" class="error">
          <small *ngIf="email.errors?.['required']">Email is required</small>
          <small *ngIf="email.errors?.['email']">Email is invalid</small>
        </div>
      </div>

      <!-- Age -->
      <div class="form-group">
        <label for="age">Age</label>
        <input
          type="number"
          id="age"
          name="age"
          [(ngModel)]="user.age"
          required
          min="18"
          max="100"
          #age="ngModel">
        <div *ngIf="age.invalid && age.touched" class="error">
          <small *ngIf="age.errors?.['required']">Age is required</small>
          <small *ngIf="age.errors?.['min']">Age must be at least 18</small>
          <small *ngIf="age.errors?.['max']">Age must be less than 100</small>
        </div>
      </div>

      <!-- Country (Select) -->
      <div class="form-group">
        <label for="country">Country</label>
        <select
          id="country"
          name="country"
          [(ngModel)]="user.country"
          required
          #country="ngModel">
          <option value="">Select a country</option>
          <option value="USA">United States</option>
          <option value="UK">United Kingdom</option>
          <option value="Canada">Canada</option>
        </select>
      </div>

      <button type="submit" [disabled]="userForm.invalid">Submit</button>
      <button type="button" (click)="userForm.reset()">Reset</button>
    </form>

    <pre>{{ user | json }}</pre>
  `,
  styles: [`
    .error { color: red; }
    .is-invalid { border-color: red; }
    .form-group { margin-bottom: 1rem; }
  `]
})
export class UserFormComponent {
  user: User = {
    firstName: '',
    lastName: '',
    email: '',
    age: 0,
    country: ''
  };

  onSubmit(): void {
    console.log('User submitted:', this.user);
  }
}
```

---

## Reactive Forms

Reactive forms provide a model-driven approach to handling form inputs with explicit form control objects in the component class.

### Setting Up Reactive Forms

```typescript
// app.module.ts
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule  // Import ReactiveFormsModule
  ]
})
export class AppModule { }
```

### Basic Reactive Form

```typescript
// login.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  template: `
    <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
      <div>
        <label for="username">Username:</label>
        <input
          type="text"
          id="username"
          formControlName="username">
        <div *ngIf="username?.invalid && username?.touched" class="error">
          <div *ngIf="username?.errors?.['required']">Username is required</div>
        </div>
      </div>

      <div>
        <label for="password">Password:</label>
        <input
          type="password"
          id="password"
          formControlName="password">
        <div *ngIf="password?.invalid && password?.touched" class="error">
          <div *ngIf="password?.errors?.['required']">Password is required</div>
          <div *ngIf="password?.errors?.['minlength']">
            Password must be at least 6 characters
          </div>
        </div>
      </div>

      <button type="submit" [disabled]="loginForm.invalid">Login</button>
    </form>

    <div>Form Valid: {{ loginForm.valid }}</div>
    <div>Form Value: {{ loginForm.value | json }}</div>
  `
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // Getter methods for easy access
  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      console.log('Form submitted:', this.loginForm.value);
    }
  }
}
```

### Advanced Reactive Form

```typescript
// user-profile.component.ts
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
  FormArray
} from '@angular/forms';

@Component({
  selector: 'app-user-profile',
  template: `
    <form [formGroup]="profileForm" (ngSubmit)="onSubmit()">
      <!-- Personal Information -->
      <div formGroupName="personalInfo">
        <h3>Personal Information</h3>

        <div>
          <label>First Name:</label>
          <input formControlName="firstName">
          <div *ngIf="getControl('personalInfo.firstName')?.invalid &&
                      getControl('personalInfo.firstName')?.touched"
               class="error">
            First name is required
          </div>
        </div>

        <div>
          <label>Last Name:</label>
          <input formControlName="lastName">
        </div>

        <div>
          <label>Email:</label>
          <input type="email" formControlName="email">
          <div *ngIf="getControl('personalInfo.email')?.invalid &&
                      getControl('personalInfo.email')?.touched"
               class="error">
            <div *ngIf="getControl('personalInfo.email')?.errors?.['required']">
              Email is required
            </div>
            <div *ngIf="getControl('personalInfo.email')?.errors?.['email']">
              Email is invalid
            </div>
          </div>
        </div>
      </div>

      <!-- Address -->
      <div formGroupName="address">
        <h3>Address</h3>

        <div>
          <label>Street:</label>
          <input formControlName="street">
        </div>

        <div>
          <label>City:</label>
          <input formControlName="city">
        </div>

        <div>
          <label>State:</label>
          <input formControlName="state">
        </div>

        <div>
          <label>Zip Code:</label>
          <input formControlName="zipCode">
        </div>
      </div>

      <!-- Phone Numbers (FormArray) -->
      <div>
        <h3>Phone Numbers</h3>
        <div formArrayName="phoneNumbers">
          <div *ngFor="let phone of phoneNumbers.controls; let i = index"
               [formGroupName]="i">
            <label>Type:</label>
            <select formControlName="type">
              <option value="home">Home</option>
              <option value="work">Work</option>
              <option value="mobile">Mobile</option>
            </select>

            <label>Number:</label>
            <input formControlName="number">

            <button type="button" (click)="removePhone(i)">Remove</button>
          </div>
        </div>
        <button type="button" (click)="addPhone()">Add Phone</button>
      </div>

      <button type="submit" [disabled]="profileForm.invalid">Submit</button>
      <button type="button" (click)="resetForm()">Reset</button>
    </form>

    <pre>{{ profileForm.value | json }}</pre>
  `
})
export class UserProfileComponent implements OnInit {
  profileForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      personalInfo: this.fb.group({
        firstName: ['', Validators.required],
        lastName: [''],
        email: ['', [Validators.required, Validators.email]]
      }),
      address: this.fb.group({
        street: [''],
        city: [''],
        state: [''],
        zipCode: ['']
      }),
      phoneNumbers: this.fb.array([
        this.createPhoneGroup()
      ])
    });
  }

  get phoneNumbers(): FormArray {
    return this.profileForm.get('phoneNumbers') as FormArray;
  }

  createPhoneGroup(): FormGroup {
    return this.fb.group({
      type: ['mobile'],
      number: ['', Validators.required]
    });
  }

  addPhone(): void {
    this.phoneNumbers.push(this.createPhoneGroup());
  }

  removePhone(index: number): void {
    this.phoneNumbers.removeAt(index);
  }

  getControl(path: string) {
    return this.profileForm.get(path);
  }

  onSubmit(): void {
    if (this.profileForm.valid) {
      console.log('Profile submitted:', this.profileForm.value);
    }
  }

  resetForm(): void {
    this.profileForm.reset();
  }
}
```

---

## Form Validation

### Built-in Validators

```typescript
import { Validators } from '@angular/forms';

this.form = this.fb.group({
  // Required field
  username: ['', Validators.required],

  // Multiple validators
  email: ['', [Validators.required, Validators.email]],

  // Min/Max length
  password: ['', [
    Validators.required,
    Validators.minLength(8),
    Validators.maxLength(20)
  ]],

  // Min/Max value (for numbers)
  age: ['', [
    Validators.required,
    Validators.min(18),
    Validators.max(100)
  ]],

  // Pattern (regex)
  phone: ['', [
    Validators.required,
    Validators.pattern(/^[0-9]{10}$/)
  ]],

  // Email validator
  contactEmail: ['', Validators.email],

  // Composed validators
  username: ['', Validators.compose([
    Validators.required,
    Validators.minLength(3),
    Validators.pattern(/^[a-zA-Z0-9]+$/)
  ])]
});
```

### Custom Validators

```typescript
// custom-validators.ts
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class CustomValidators {
  // Validator function
  static noWhitespace(control: AbstractControl): ValidationErrors | null {
    const isWhitespace = (control.value || '').trim().length === 0;
    return isWhitespace ? { whitespace: true } : null;
  }

  // Validator factory (with parameters)
  static minDate(minDate: Date): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const inputDate = new Date(control.value);
      return inputDate < minDate ? { minDate: { minDate, actual: inputDate } } : null;
    };
  }

  // Password strength validator
  static passwordStrength(control: AbstractControl): ValidationErrors | null {
    const value = control.value || '';

    if (!value) {
      return null;
    }

    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);

    const passwordValid = hasUpperCase && hasLowerCase && hasNumeric && hasSpecialChar;

    return !passwordValid ? {
      passwordStrength: {
        hasUpperCase,
        hasLowerCase,
        hasNumeric,
        hasSpecialChar
      }
    } : null;
  }

  // Match fields validator
  static matchFields(field1: string, field2: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value1 = control.get(field1)?.value;
      const value2 = control.get(field2)?.value;

      return value1 === value2 ? null : { fieldsMismatch: true };
    };
  }
}

// Usage
this.form = this.fb.group({
  username: ['', [Validators.required, CustomValidators.noWhitespace]],
  birthDate: ['', CustomValidators.minDate(new Date('1900-01-01'))],
  password: ['', [Validators.required, CustomValidators.passwordStrength]],
  confirmPassword: ['', Validators.required]
}, {
  validators: CustomValidators.matchFields('password', 'confirmPassword')
});
```

### Async Validators

```typescript
// async-validators.ts
import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map, catchError, debounceTime, switchMap } from 'rxjs/operators';

export class AsyncValidators {
  // Check if username is available
  static usernameAvailable(userService: UserService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value) {
        return of(null);
      }

      return userService.checkUsername(control.value).pipe(
        debounceTime(500),  // Wait 500ms after user stops typing
        map(isAvailable => isAvailable ? null : { usernameTaken: true }),
        catchError(() => of(null))
      );
    };
  }

  // Check if email exists
  static emailExists(userService: UserService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value) {
        return of(null);
      }

      return userService.checkEmail(control.value).pipe(
        debounceTime(500),
        map(exists => exists ? { emailExists: true } : null),
        catchError(() => of(null))
      );
    };
  }
}

// Usage
this.form = this.fb.group({
  username: [
    '',
    [Validators.required],
    [AsyncValidators.usernameAvailable(this.userService)]
  ],
  email: [
    '',
    [Validators.required, Validators.email],
    [AsyncValidators.emailExists(this.userService)]
  ]
});
```

### Displaying Validation Errors

```typescript
@Component({
  selector: 'app-registration',
  template: `
    <form [formGroup]="registrationForm" (ngSubmit)="onSubmit()">
      <div>
        <label>Username:</label>
        <input formControlName="username">

        <!-- Show validation errors -->
        <div *ngIf="username?.invalid && (username?.dirty || username?.touched)"
             class="error">
          <div *ngIf="username?.errors?.['required']">
            Username is required
          </div>
          <div *ngIf="username?.errors?.['minlength']">
            Username must be at least
            {{ username?.errors?.['minlength'].requiredLength }} characters
          </div>
          <div *ngIf="username?.errors?.['usernameTaken']">
            Username is already taken
          </div>
        </div>

        <!-- Show async validation pending -->
        <div *ngIf="username?.pending" class="info">
          Checking username availability...
        </div>
      </div>

      <div>
        <label>Password:</label>
        <input type="password" formControlName="password">

        <div *ngIf="password?.invalid && password?.touched" class="error">
          <div *ngIf="password?.errors?.['required']">
            Password is required
          </div>
          <div *ngIf="password?.errors?.['passwordStrength']">
            Password must contain:
            <ul>
              <li [class.valid]="password?.errors?.['passwordStrength'].hasUpperCase">
                Uppercase letter
              </li>
              <li [class.valid]="password?.errors?.['passwordStrength'].hasLowerCase">
                Lowercase letter
              </li>
              <li [class.valid]="password?.errors?.['passwordStrength'].hasNumeric">
                Number
              </li>
              <li [class.valid]="password?.errors?.['passwordStrength'].hasSpecialChar">
                Special character
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div>
        <label>Confirm Password:</label>
        <input type="password" formControlName="confirmPassword">

        <div *ngIf="registrationForm.errors?.['fieldsMismatch'] &&
                    confirmPassword?.touched"
             class="error">
          Passwords do not match
        </div>
      </div>

      <button type="submit" [disabled]="registrationForm.invalid">Register</button>
    </form>
  `
})
export class RegistrationComponent implements OnInit {
  registrationForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.registrationForm = this.fb.group({
      username: [
        '',
        [Validators.required, Validators.minLength(3)],
        [AsyncValidators.usernameAvailable(this.userService)]
      ],
      password: ['', [Validators.required, CustomValidators.passwordStrength]],
      confirmPassword: ['', Validators.required]
    }, {
      validators: CustomValidators.matchFields('password', 'confirmPassword')
    });
  }

  get username() {
    return this.registrationForm.get('username');
  }

  get password() {
    return this.registrationForm.get('password');
  }

  get confirmPassword() {
    return this.registrationForm.get('confirmPassword');
  }

  onSubmit(): void {
    if (this.registrationForm.valid) {
      console.log('Registration:', this.registrationForm.value);
    }
  }
}
```

---

## Dynamic Forms

Dynamic forms are generated programmatically based on metadata.

### Dynamic Form Configuration

```typescript
// form-field.model.ts
export interface FormFieldConfig {
  type: 'text' | 'email' | 'number' | 'select' | 'checkbox' | 'textarea';
  name: string;
  label: string;
  value?: any;
  placeholder?: string;
  required?: boolean;
  validators?: any[];
  options?: { value: any; label: string }[];  // For select fields
}

// form-config.ts
export const userFormConfig: FormFieldConfig[] = [
  {
    type: 'text',
    name: 'firstName',
    label: 'First Name',
    required: true,
    validators: [Validators.required, Validators.minLength(2)]
  },
  {
    type: 'text',
    name: 'lastName',
    label: 'Last Name',
    required: true
  },
  {
    type: 'email',
    name: 'email',
    label: 'Email',
    required: true,
    validators: [Validators.required, Validators.email]
  },
  {
    type: 'number',
    name: 'age',
    label: 'Age',
    required: true,
    validators: [Validators.required, Validators.min(18)]
  },
  {
    type: 'select',
    name: 'country',
    label: 'Country',
    required: true,
    options: [
      { value: 'usa', label: 'United States' },
      { value: 'uk', label: 'United Kingdom' },
      { value: 'canada', label: 'Canada' }
    ]
  },
  {
    type: 'checkbox',
    name: 'subscribe',
    label: 'Subscribe to newsletter',
    value: false
  }
];
```

### Dynamic Form Component

```typescript
// dynamic-form.component.ts
import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { FormFieldConfig } from './form-field.model';

@Component({
  selector: 'app-dynamic-form',
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div *ngFor="let field of config" class="form-field">
        <!-- Text Input -->
        <div *ngIf="field.type === 'text' || field.type === 'email'">
          <label [for]="field.name">
            {{ field.label }}
            <span *ngIf="field.required">*</span>
          </label>
          <input
            [type]="field.type"
            [id]="field.name"
            [formControlName]="field.name"
            [placeholder]="field.placeholder || ''">
          <div *ngIf="form.get(field.name)?.invalid &&
                      form.get(field.name)?.touched"
               class="error">
            {{ getErrorMessage(field.name) }}
          </div>
        </div>

        <!-- Number Input -->
        <div *ngIf="field.type === 'number'">
          <label [for]="field.name">
            {{ field.label }}
            <span *ngIf="field.required">*</span>
          </label>
          <input
            type="number"
            [id]="field.name"
            [formControlName]="field.name">
          <div *ngIf="form.get(field.name)?.invalid &&
                      form.get(field.name)?.touched"
               class="error">
            {{ getErrorMessage(field.name) }}
          </div>
        </div>

        <!-- Select -->
        <div *ngIf="field.type === 'select'">
          <label [for]="field.name">
            {{ field.label }}
            <span *ngIf="field.required">*</span>
          </label>
          <select [id]="field.name" [formControlName]="field.name">
            <option value="">Select {{ field.label }}</option>
            <option *ngFor="let option of field.options"
                    [value]="option.value">
              {{ option.label }}
            </option>
          </select>
        </div>

        <!-- Checkbox -->
        <div *ngIf="field.type === 'checkbox'">
          <label>
            <input
              type="checkbox"
              [formControlName]="field.name">
            {{ field.label }}
          </label>
        </div>

        <!-- Textarea -->
        <div *ngIf="field.type === 'textarea'">
          <label [for]="field.name">
            {{ field.label }}
            <span *ngIf="field.required">*</span>
          </label>
          <textarea
            [id]="field.name"
            [formControlName]="field.name"
            [placeholder]="field.placeholder || ''">
          </textarea>
        </div>
      </div>

      <button type="submit" [disabled]="form.invalid">Submit</button>
    </form>

    <pre>{{ form.value | json }}</pre>
  `
})
export class DynamicFormComponent implements OnInit {
  @Input() config: FormFieldConfig[] = [];
  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.createFormGroup();
  }

  createFormGroup(): FormGroup {
    const group: any = {};

    this.config.forEach(field => {
      const validators = field.validators || [];
      group[field.name] = [field.value || '', validators];
    });

    return this.fb.group(group);
  }

  getErrorMessage(fieldName: string): string {
    const control = this.form.get(fieldName);
    if (!control || !control.errors) {
      return '';
    }

    const errors = control.errors;

    if (errors['required']) {
      return 'This field is required';
    }
    if (errors['email']) {
      return 'Invalid email format';
    }
    if (errors['minlength']) {
      return `Minimum length is ${errors['minlength'].requiredLength}`;
    }
    if (errors['min']) {
      return `Minimum value is ${errors['min'].min}`;
    }

    return 'Invalid value';
  }

  onSubmit(): void {
    if (this.form.valid) {
      console.log('Form submitted:', this.form.value);
    }
  }
}

// Usage
@Component({
  selector: 'app-user-registration',
  template: '<app-dynamic-form [config]="formConfig"></app-dynamic-form>'
})
export class UserRegistrationComponent {
  formConfig = userFormConfig;
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Template-Driven | Uses directives, good for simple forms |
| Reactive Forms | Model-driven, explicit, testable |
| Validation | Built-in, custom, and async validators |
| FormBuilder | Simplified form creation |
| FormArray | Dynamic form fields |
| Dynamic Forms | Metadata-driven form generation |

## Next Topic

Continue to [State Management](./10-state-management.md) to learn about managing application state in Angular.
