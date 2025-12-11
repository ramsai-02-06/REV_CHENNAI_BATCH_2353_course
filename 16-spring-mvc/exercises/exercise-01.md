# Exercise: Build a Web Application with Spring MVC

## Objective
Create a web application with server-side rendering using Spring MVC and Thymeleaf, demonstrating controllers, views, form handling, and validation.

## Requirements

### Application: Employee Directory
Build an employee management web application with the following pages:

1. **Home Page** (`/`)
   - Welcome message
   - Navigation to other sections

2. **Employee List** (`/employees`)
   - Table displaying all employees
   - Search/filter functionality
   - Links to view, edit, delete

3. **Add Employee** (`/employees/new`)
   - Form to create new employee
   - Validation with error messages
   - Redirect to list on success

4. **Edit Employee** (`/employees/{id}/edit`)
   - Pre-populated form
   - Update functionality
   - Cancel button

5. **View Employee** (`/employees/{id}`)
   - Display employee details
   - Edit and Delete buttons

### Spring MVC Concepts to Demonstrate

1. **Controllers**
   - `@Controller` vs `@RestController`
   - `@GetMapping`, `@PostMapping`
   - `@PathVariable`, `@RequestParam`
   - `@ModelAttribute`
   - Redirect and Forward

2. **Views (Thymeleaf)**
   - Template layouts
   - Iteration (`th:each`)
   - Conditionals (`th:if`, `th:unless`)
   - Form binding (`th:object`, `th:field`)
   - Fragment reuse

3. **Form Handling**
   - Form submission
   - Model binding
   - Validation errors display
   - PRG pattern (Post-Redirect-Get)

4. **Validation**
   - Bean Validation annotations
   - BindingResult
   - Custom error messages

### Project Structure
```
src/main/java/
├── controller/
│   └── EmployeeController.java
├── model/
│   └── Employee.java
├── service/
│   └── EmployeeService.java
└── repository/
    └── EmployeeRepository.java

src/main/resources/
├── templates/
│   ├── layout/
│   │   └── main.html
│   ├── employee/
│   │   ├── list.html
│   │   ├── form.html
│   │   └── view.html
│   └── index.html
├── static/
│   └── css/
│       └── style.css
└── application.yml
```

## Skills Tested
- Spring MVC request handling
- Thymeleaf templating
- Form processing and validation
- PRG pattern
- Model and View concepts
