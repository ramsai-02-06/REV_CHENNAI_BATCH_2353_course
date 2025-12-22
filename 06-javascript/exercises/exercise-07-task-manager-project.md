# Exercise: Build a Task Manager with Local Storage

## Objective
Create a fully functional task manager application using vanilla JavaScript that persists data using localStorage.

## Requirements

Build a task manager (`task-manager.html` and `task-manager.js`) with the following features:

### 1. Core Features

#### Add Tasks
- Input field for task title
- Optional priority selector (Low, Medium, High)
- Optional due date picker
- Add button to create new task

#### Display Tasks
- Show all tasks in a list
- Each task shows: title, priority, due date, created date
- Visual indicator for task priority (color-coded)
- Visual indicator for overdue tasks

#### Task Actions
- Mark task as complete/incomplete (toggle)
- Edit task title inline
- Delete task with confirmation
- Clear all completed tasks

#### Filter & Sort
- Filter by: All, Active, Completed
- Sort by: Date Created, Due Date, Priority

### 2. Data Persistence
- Save all tasks to localStorage
- Load tasks from localStorage on page load
- Update localStorage whenever tasks change

### 3. JavaScript Concepts to Use
- DOM manipulation (querySelector, createElement, etc.)
- Event handling (addEventListener, event delegation)
- Array methods (map, filter, sort, find)
- localStorage API (getItem, setItem, JSON.parse/stringify)
- Date handling
- ES6+ features (arrow functions, template literals, destructuring)

## Sample Task Object Structure
```javascript
{
    id: 'task-1702345678901',
    title: 'Complete JavaScript exercise',
    priority: 'high',
    dueDate: '2024-12-20',
    createdAt: '2024-12-15T10:30:00.000Z',
    completed: false
}
```

## Expected Behavior
1. Page loads → Tasks retrieved from localStorage
2. User adds task → Task appears in list, saved to localStorage
3. User completes task → Strike-through style, saved to localStorage
4. User filters → Only matching tasks displayed
5. Page refreshes → All tasks persist

## Skills Tested
- DOM manipulation and traversal
- Event handling and delegation
- Working with arrays of objects
- localStorage for data persistence
- Date formatting and comparison
- ES6+ JavaScript features

## Bonus Challenges
1. Add drag-and-drop reordering
2. Add task categories/tags
3. Add search functionality
4. Add keyboard shortcuts (Enter to add, Delete to remove)
