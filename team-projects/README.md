# Team Projects - Full Stack Development

## Overview

This folder contains **10 team project requirements** designed to evolve throughout the training program. Each project starts as a **frontend UI application** and progressively transforms into a **full-stack application** as learners acquire new technologies.

Each project is designed for a **team of 4 members**, following Agile development workflows with Git collaboration, issue tracking, and code reviews.

---

## Project Evolution Roadmap

These projects are designed to grow with your learning journey. As you master new technologies, you'll expand the same project with additional user stories and functionality.

| Phase | Technologies | Focus | User Stories |
|-------|--------------|-------|--------------|
| **Phase 1** | HTML, CSS, Bootstrap | Static UI, Responsive Design, Semantic HTML | 12 stories (3/member) |
| **Phase 2** | JavaScript, DOM, ES6+ | Interactivity, Form Validation, Dynamic Content | +8 stories (2/member) |
| **Phase 3** | TypeScript, Angular | SPA Architecture, Components, Services, Routing | +12 stories (3/member) |
| **Phase 4** | Java, JDBC, SQL | Backend API, Database Design, Data Persistence | +12 stories (3/member) |
| **Phase 5** | Spring Boot, Spring Data | REST APIs, Security, Production-Ready Backend | +8 stories (2/member) |
| **Phase 6** | Cloud, DevOps | Deployment, CI/CD, Monitoring | +4 stories (1/member) |

### Total Project Growth
- **Phase 1 (UI):** 12 user stories
- **Phase 1-2 (Interactive UI):** 20 user stories
- **Phase 1-3 (Angular SPA):** 32 user stories
- **Phase 1-4 (Full Stack with Java):** 44 user stories
- **Phase 1-5 (Spring Boot):** 52 user stories
- **Phase 1-6 (Deployed Application):** 56 user stories

> **Note:** User stories for Phases 2-6 will be added to each project document as the curriculum progresses.

---

## Project List

| # | Project | Description | Difficulty |
|---|---------|-------------|------------|
| 1 | [Restaurant Website](./project-01-restaurant.md) | Fine dining restaurant with menu, reservations | Medium |
| 2 | [E-Commerce Store](./project-02-ecommerce.md) | Online shopping platform with products, cart | Medium-High |
| 3 | [Tech Startup Landing](./project-03-tech-startup.md) | SaaS product landing with pricing, features | Medium |
| 4 | [Hotel/Resort Website](./project-04-hotel-resort.md) | Hospitality site with rooms, booking | Medium |
| 5 | [Fitness Center](./project-05-fitness-center.md) | Gym website with classes, memberships | Medium |
| 6 | [Online Magazine](./project-06-online-magazine.md) | News/blog platform with articles, categories | Medium-High |
| 7 | [Real Estate Listing](./project-07-real-estate.md) | Property listings with search, details | Medium-High |
| 8 | [Event/Conference](./project-08-event-conference.md) | Conference site with schedule, registration | Medium |
| 9 | [Non-Profit Organization](./project-09-nonprofit.md) | Charity site with donations, programs | Medium |
| 10 | [Educational Portal](./project-10-educational.md) | School/course site with catalog, admissions | Medium |

---

## Team Structure

Each team consists of **4 members** with distinct responsibilities:

| Role | Primary Focus | Features |
|------|---------------|----------|
| **Member A** | Core Structure & Navigation | Home page, Header/Nav, Footer |
| **Member B** | Forms & User Interaction | Contact, Booking/Registration, Newsletter |
| **Member C** | Data Display & Tables | Listings, Comparison tables, Pricing |
| **Member D** | Content & Media | About, Gallery, Team/Testimonials |

---

## Git Workflow Requirements

### Repository Setup

1. **One team member** creates the repository and adds others as collaborators
2. Initialize with a `README.md` and `.gitignore`
3. Create a `main` branch (protected - no direct commits)
4. Create a `develop` branch for integration

### Branch Strategy

```
main (protected)
  └── develop (integration branch)
        ├── feature/home-page
        ├── feature/header-nav
        ├── feature/footer
        ├── feature/menu-page
        └── ... (one branch per feature)
```

### Branch Naming Convention

```
feature/<feature-name>     # New features
bugfix/<bug-description>   # Bug fixes
hotfix/<issue-number>      # Urgent fixes
```

### Workflow Steps

1. **Create Issue** - Before starting work, create a GitHub issue
2. **Create Branch** - Branch from `develop` using naming convention
3. **Commit Often** - Small, meaningful commits with clear messages
4. **Push & Create PR** - Push branch and create Pull Request to `develop`
5. **Code Review** - At least 1 team member must review and approve
6. **Merge** - Squash and merge after approval
7. **Close Issue** - Link PR to issue for automatic closure

### Commit Message Format

```
<type>: <short description>

[optional body]

[optional footer - issue reference]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `style`: CSS/styling changes
- `refactor`: Code restructuring
- `docs`: Documentation

**Examples:**
```
feat: add responsive navigation menu

- Implement hamburger menu for mobile
- Add dropdown for sub-navigation
- Style active nav states

Closes #12
```

---

## GitHub Issues & Project Board

### Issue Templates

Each feature should have an issue with:

```markdown
## User Story
As a [user type], I want to [action] so that [benefit].

## Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2
- [ ] Criterion 3

## Technical Notes
- Bootstrap components to use
- CSS considerations
- Accessibility requirements

## Assigned To
@team-member-username

## Labels
- `feature`, `frontend`, `priority:high`
```

### Project Board Columns

Set up a GitHub Project board with:

| Column | Description |
|--------|-------------|
| **Backlog** | All user stories not yet started |
| **Sprint** | Current sprint items |
| **In Progress** | Actively being worked on |
| **In Review** | PR created, awaiting review |
| **Done** | Merged and completed |

### Labels to Create

**Type Labels:**
- `feature` - New functionality
- `bug` - Something isn't working
- `enhancement` - Improvement to existing feature
- `documentation` - Documentation updates

**Priority Labels:**
- `priority:high` / `priority:medium` / `priority:low`

**Assignment Labels:**
- `member-a` / `member-b` / `member-c` / `member-d`

**Phase Labels (add as curriculum progresses):**
- `phase-1:ui` - HTML, CSS, Bootstrap
- `phase-2:javascript` - JavaScript interactivity
- `phase-3:angular` - Angular SPA
- `phase-4:java` - Java backend
- `phase-5:spring` - Spring Boot
- `phase-6:devops` - Deployment

**Technology Labels:**
- `html` / `css` / `bootstrap`
- `javascript` / `typescript`
- `angular` / `java` / `spring`
- `database` / `api` / `security`

---

## Sprint Planning

### Suggested Sprint Structure

| Sprint | Focus | Duration |
|--------|-------|----------|
| **Sprint 1** | Project setup, Header, Footer, Home page structure | 2-3 days |
| **Sprint 2** | Core pages (About, Contact, main content pages) | 2-3 days |
| **Sprint 3** | Forms, Tables, Interactive components | 2-3 days |
| **Sprint 4** | Polish, Responsive testing, Final integration | 2-3 days |

### Daily Standup Questions

Each team member should answer:
1. What did I complete yesterday?
2. What will I work on today?
3. Do I have any blockers?

---

## Technical Requirements

### HTML Standards
- Valid HTML5 with proper DOCTYPE
- Semantic elements (`header`, `nav`, `main`, `section`, `article`, `footer`)
- Accessible forms with labels and ARIA attributes
- Proper heading hierarchy (h1 > h2 > h3)
- Alt text for all images

### CSS Standards
- External stylesheets (no inline styles except for Bootstrap utilities)
- Mobile-first responsive design
- Consistent naming convention (BEM recommended)
- CSS custom properties for theming (colors, fonts)

### Bootstrap Requirements
- Use Bootstrap 5.x via CDN
- Utilize grid system for layouts
- Use Bootstrap components (navbar, cards, forms, tables, modals)
- Customize with your own CSS (don't rely solely on Bootstrap defaults)

### File Structure

```
project-name/
├── index.html
├── pages/
│   ├── about.html
│   ├── contact.html
│   └── [other pages].html
├── css/
│   └── styles.css
├── images/
│   └── [image files]
├── js/
│   └── script.js (optional for interactivity)
└── README.md
```

---

## Code Review Checklist

Before approving a PR, reviewers should verify:

- [ ] HTML validates without errors
- [ ] Semantic HTML elements used appropriately
- [ ] All images have alt text
- [ ] Forms have proper labels
- [ ] Page is responsive (mobile, tablet, desktop)
- [ ] Bootstrap grid used correctly
- [ ] CSS follows project conventions
- [ ] No broken links or missing images
- [ ] Accessibility considerations addressed
- [ ] Code is properly indented and formatted

---

## Phase-wise Technical Evolution

These projects are architected to evolve seamlessly as new technologies are introduced.

### Phase 1: UI Foundation (Current)
**Technologies:** HTML5, CSS3, Bootstrap 5

- Semantic HTML structure
- Responsive layouts with Bootstrap grid
- Forms with HTML5 validation
- Static content and placeholder data
- All forms use proper `name` attributes (ready for backend)
- Form `action` attributes set to `#` (placeholder for API endpoints)

### Phase 2: JavaScript Interactivity
**Technologies:** JavaScript ES6+, DOM Manipulation

**Example User Stories to Add:**
- Form validation with real-time feedback
- Dynamic content filtering and sorting
- Local storage for cart/preferences
- Interactive image galleries
- Modal dialogs and popups
- Fetch API for mock data

### Phase 3: Angular SPA
**Technologies:** TypeScript, Angular, RxJS

**Example User Stories to Add:**
- Convert to single-page application
- Component-based architecture
- Angular routing and navigation
- Services for data management
- Reactive forms with validation
- HTTP client for API calls

### Phase 4: Java Backend
**Technologies:** Java, JDBC, PostgreSQL/MySQL

**Example User Stories to Add:**
- RESTful API endpoints
- Database schema design
- CRUD operations
- User authentication
- Data validation on server
- Connection pooling

### Phase 5: Spring Boot
**Technologies:** Spring Boot, Spring Data JPA, Spring Security

**Example User Stories to Add:**
- Spring Boot REST controllers
- JPA entity relationships
- Spring Security authentication
- JWT token management
- Exception handling
- API documentation (Swagger)

### Phase 6: Cloud Deployment
**Technologies:** AWS/Azure, Docker, CI/CD

**Example User Stories to Add:**
- Containerize application
- Deploy to cloud platform
- Set up CI/CD pipeline
- Configure environment variables
- Implement logging and monitoring

---

## Architecture Ready for Growth

### Frontend Structure (Phase 1)
```
project-name/
├── index.html
├── pages/
├── css/
├── images/
└── js/
```

### Full Stack Structure (Phase 4+)
```
project-name/
├── frontend/          # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── services/
│   │   │   └── models/
│   │   └── assets/
│   └── package.json
├── backend/           # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   └── model/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
└── README.md
```

### Data Attributes for Future JavaScript
All UI elements include data attributes for easy JavaScript integration:
```html
<div class="product-card" data-product-id="123" data-category="electronics">
<button class="add-to-cart" data-action="add" data-product="123">
<form id="contact-form" data-api-endpoint="/api/contact">
```

---

## Evaluation Criteria

### Phase 1: UI Development

| Criteria | Weight |
|----------|--------|
| **Functionality** - All features implemented | 25% |
| **Code Quality** - Clean, semantic, valid HTML/CSS | 20% |
| **Responsiveness** - Works on all device sizes | 20% |
| **Git Workflow** - Proper branching, PRs, commits | 20% |
| **Collaboration** - Issues, reviews, team communication | 15% |

### Phase 2-3: Frontend (JavaScript/Angular)

| Criteria | Weight |
|----------|--------|
| **Functionality** - All features working correctly | 25% |
| **Code Quality** - Clean, modular, well-structured code | 20% |
| **User Experience** - Smooth interactions, validation | 15% |
| **Testing** - Unit tests, component tests | 15% |
| **Git Workflow** - Branching, PRs, code reviews | 15% |
| **Collaboration** - Team communication, documentation | 10% |

### Phase 4-5: Full Stack (Java/Spring)

| Criteria | Weight |
|----------|--------|
| **Functionality** - Frontend + Backend integration | 20% |
| **API Design** - RESTful principles, proper endpoints | 15% |
| **Database Design** - Schema, relationships, queries | 15% |
| **Security** - Authentication, authorization, validation | 15% |
| **Testing** - Unit, integration, API tests | 15% |
| **Code Quality** - Clean architecture, SOLID principles | 10% |
| **Git Workflow & Collaboration** | 10% |

### Phase 6: Deployment

| Criteria | Weight |
|----------|--------|
| **Deployment** - Successfully deployed and accessible | 30% |
| **CI/CD Pipeline** - Automated build and deploy | 25% |
| **Documentation** - README, API docs, setup guide | 20% |
| **Monitoring** - Logging, error tracking | 15% |
| **Presentation** - Demo and explanation | 10% |

---

## Getting Started

### Phase 1 (Current)

1. **Form your team** of 4 members
2. **Choose a project** from the list above
3. **Create a GitHub repository** with proper setup
4. **Set up the project board** with issues for all 12 Phase 1 features
5. **Assign features** to team members (3 each)
6. **Start Sprint 1** - Begin with setup and core structure
7. **Daily standups** - Communicate progress and blockers
8. **Submit** - Final PR to main when all Phase 1 features complete

### Future Phases

As you learn new technologies:

1. **Product Owner (Trainer)** will add new user stories to each project document
2. **Create new issues** in your repository for the new phase
3. **Label issues** with the appropriate phase tag
4. **Continue iterating** on the same codebase
5. **Maintain git history** - Your project grows organically with commits

---

## For Trainers: Adding New Phases

When introducing a new technology phase:

1. Add a new section to each project file: `## Phase X: [Technology] User Stories`
2. Include 8-12 new user stories per project appropriate to the technology
3. Update this README with any new technical requirements
4. Announce the new phase to teams and have them create issues

---

Good luck and happy coding! Remember, this project will grow with you throughout the training program.
