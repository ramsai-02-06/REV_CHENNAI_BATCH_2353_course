# Exercise: Build a Responsive Dashboard Layout

## Objective
Create a responsive admin dashboard using Bootstrap 5 components and grid system.

## Requirements

Build a dashboard page (`dashboard.html`) using Bootstrap 5 with:

### 1. Navigation
- Responsive navbar with brand logo
- Navigation links that collapse on mobile
- User dropdown menu (Profile, Settings, Logout)
- Search form in navbar

### 2. Sidebar
- Collapsible sidebar navigation
- Icons for each menu item
- Active state indicator
- Categories: Dashboard, Users, Products, Orders, Reports, Settings

### 3. Main Content Area
Using Bootstrap Grid System:

#### Stats Cards Row (4 columns on lg, 2 on md, 1 on sm)
- Total Users card
- Total Revenue card
- New Orders card
- Pending Tasks card

Each card should have:
- Icon
- Title
- Value
- Percentage change indicator

#### Charts Section (2 columns on md+)
- Placeholder for a sales chart
- Placeholder for a traffic chart

#### Recent Activity Table
- Responsive table
- Columns: ID, User, Action, Date, Status
- Status badges with Bootstrap colors
- Pagination

### 4. Bootstrap Components to Use
- Grid system (container, row, col-*)
- Cards
- Tables (table-responsive)
- Buttons and button groups
- Badges
- Navbar
- Dropdowns
- Forms
- Alerts
- Pagination
- Icons (Bootstrap Icons)

### 5. Responsive Requirements
- Sidebar hidden on mobile (toggle button)
- Cards stack vertically on small screens
- Table scrolls horizontally on mobile
- Navigation collapses properly

## Skills Tested
- Bootstrap grid system
- Responsive breakpoints (sm, md, lg, xl)
- Bootstrap components
- Utility classes
- Mobile-first design

## Bonus Challenge
- Add a dark mode toggle using Bootstrap's color modes
- Implement off-canvas sidebar for mobile
