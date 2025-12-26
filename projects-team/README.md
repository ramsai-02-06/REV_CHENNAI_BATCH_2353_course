# Team Projects - Full Stack Development

## Overview

This folder contains **10 team project requirements** designed to evolve throughout the **13-week training program**. Each project starts as a **frontend UI application** and progressively transforms into a **full-stack cloud-deployed application** across three major project milestones.

Each project is designed for a **team of 4 members**, following Agile development workflows with Git collaboration, issue tracking, and code reviews.

---

## Program Timeline & Project Milestones

| Week | Focus | Milestone |
|------|-------|-----------|
| 1-2 | HTML, CSS, Bootstrap, Git, Agile | UI Storyboard/Wireframe |
| **2 (End)** | **UI REVIEW/DEMO** | **Wireframe Walkthrough** |
| 3-5 | Java, MySQL, JDBC | Backend Development |
| **6** | **PROJECT 1 PRESENTATION** | **Java + MySQL + JDBC** |
| 7-8 | Spring Boot, Spring MVC, Spring Data JPA | REST API Development |
| **8 (End)** | **REST API REVIEW/DEMO** | **Spring Boot REST API** |
| 9-10 | Angular, TypeScript | Frontend SPA Integration |
| **11** | **PROJECT 2 PRESENTATION** | **Spring Boot + Angular** |
| 12 | Docker, Kubernetes, GCP | Cloud Deployment |
| **13** | **PROJECT 3 / FINAL DEMO** | **Full Stack + Cloud** |

### UI Storyboard/Wireframe Phase (Week 1-2)

The initial UI development serves as a **visual storyboard and wireframe** for the entire project:

- **Purpose:** Design and visualize all application screens before backend development
- **Deliverable:** Fully functional HTML/CSS/Bootstrap pages representing the complete user journey
- **Review:** End of Week 2 demo to validate UI design and user flow before proceeding

**What the UI Storyboard Demonstrates:**
- All pages and navigation flows
- Form layouts with field requirements (ready for backend validation)
- Data display formats (tables, cards, lists)
- User interactions and workflows
- Responsive design across devices

**UI Review Criteria:**
- [ ] All pages/screens completed
- [ ] Navigation between pages works
- [ ] Forms have proper fields and validation attributes
- [ ] Responsive on mobile, tablet, desktop
- [ ] Follows semantic HTML best practices
- [ ] Team demonstrates user journey walkthrough

### REST API Development Phase (Week 7-8)

The Spring REST API serves as the **backend foundation** for the Angular SPA:

- **Purpose:** Build a complete REST API with all CRUD endpoints before Angular development
- **Deliverable:** Fully functional REST API with Swagger documentation
- **Review:** End of Week 8 demo to validate API design and test endpoints

**What the REST API Demo Demonstrates:**
- All REST endpoints documented in Swagger/OpenAPI
- CRUD operations for all entities
- Proper HTTP methods (GET, POST, PUT, DELETE)
- Request/response DTOs
- Exception handling with proper HTTP status codes
- API tested with Postman or similar tool

**REST API Review Criteria:**
- [ ] All entities have CRUD endpoints
- [ ] Swagger UI accessible and complete
- [ ] Proper HTTP status codes returned
- [ ] Input validation with meaningful error messages
- [ ] Service layer separates business logic from controllers
- [ ] Repository layer uses Spring Data JPA
- [ ] Team demonstrates API testing walkthrough (Postman/Swagger)

---

## Project Evolution by Milestone

### Project 1 (Week 6)
**Technology Stack:** Java, MySQL, JDBC

| Component | Technologies |
|-----------|--------------|
| Frontend | HTML, CSS, Bootstrap |
| Backend | Java (Servlets or Console) |
| Database | MySQL |
| Data Access | JDBC |

**User Stories:** 12+ (UI) + 12+ (Backend) = **24+ total**

---

### Project 2 (Week 11)
**Technology Stack:** Java, MySQL, Spring Boot, Spring JPA, Angular

| Component | Technologies |
|-----------|--------------|
| Frontend | Angular, TypeScript |
| Backend | Spring Boot, Spring JPA |
| Database | MySQL |
| API | REST APIs |

**User Stories:** Previous 24+ + 24+ (Spring/Angular) = **48+ total**

---

### Project 3 / Final (Week 13)
**Technology Stack:** Java, MySQL, Spring Boot, Spring Data/JPA, Docker, Kubernetes, GCP

| Component | Technologies |
|-----------|--------------|
| Frontend | Angular |
| Backend | Spring Boot, Spring Data JPA |
| Database | MySQL (Cloud SQL) |
| Containerization | Docker |
| Orchestration | Kubernetes |
| Cloud Platform | GCP |

**User Stories:** Previous 48+ + 8+ (DevOps) = **56+ total**

---

## User Story Distribution by Phase

| Phase | Timeline | Technologies | Min Stories/Member | Min Total |
|-------|----------|--------------|-------------------|-----------|
| **UI Storyboard/Wireframe** | Week 1-2 | HTML, CSS, Bootstrap | 3+ | 12+ |
| *UI Review/Demo* | *Week 2 End* | | | *12+* |
| **Java Backend** | Week 3-5 | Java, MySQL, JDBC | 3+ | 12+ |
| *Project 1* | *Week 6* | | | *24+* |
| **Spring REST API** | Week 7-8 | Spring Boot, MVC, Data JPA | 3+ | 12+ |
| *REST API Review/Demo* | *Week 8 End* | | | *36+* |
| **Angular Frontend** | Week 9-10 | Angular, TypeScript | 3+ | 12+ |
| *Project 2* | *Week 11* | | | *48+* |
| **Cloud Deployment** | Week 12 | Docker, K8s, GCP | 2+ | 8+ |
| *Project 3 / Final* | *Week 13* | | | *56+* |

> **Note:** Each learner should complete a **minimum of 3 features per phase**. Facilitators/Mentors may add more user stories based on project complexity and learner progress. Current document contains **Phase 1: UI Storyboard/Wireframe** stories only.

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

**Project Milestone Labels:**
- `project-1` - Week 6 milestone (Java + JDBC)
- `project-2` - Week 11 milestone (Spring + Angular)
- `project-3` - Week 13 final (Cloud deployment)

**Technology Phase Labels:**
- `ui` - HTML, CSS, Bootstrap (Week 1-2)
- `java-jdbc` - Java backend with JDBC (Week 3-5)
- `rest-api` - Spring Boot REST API (Week 7-8)
- `angular` - Angular SPA (Week 9-10)
- `devops` - Docker, K8s, GCP (Week 12)

**Technology Labels:**
- `html` / `css` / `bootstrap`
- `java` / `jdbc` / `mysql`
- `spring-boot` / `spring-jpa`
- `angular` / `typescript`
- `docker` / `kubernetes` / `gcp`

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

## Technical Requirements by Project

### Project 1 Requirements (Week 6)

#### UI Layer (Week 1-2)
**Technologies:** HTML5, CSS3, Bootstrap 5

- Semantic HTML structure
- Responsive layouts with Bootstrap grid
- Forms with HTML5 validation
- Static content and placeholder data
- All forms use proper `name` attributes (ready for backend)
- Form `action` attributes set to `#` (placeholder for API endpoints)

#### Backend Layer (Week 3-5)
**Technologies:** Java, MySQL, JDBC

**Example User Stories to Add:**
- Database schema design and setup
- JDBC connection and configuration
- DAO (Data Access Object) pattern implementation
- CRUD operations for all entities
- User authentication (login/register)
- Input validation and error handling
- Menu-driven console OR Servlet-based web integration

---

### REST API Interim Milestone (Week 8)

#### Spring REST API (Week 7-8)
**Technologies:** Spring Boot, Spring MVC, Spring Data JPA

**Example User Stories to Add:**
- Migrate JDBC to Spring Data JPA entities
- Create REST controllers for all resources
- Implement DTOs for request/response
- JPA entity relationships and mappings
- Service layer with business logic
- Exception handling with @ControllerAdvice
- Input validation with Bean Validation
- API documentation (Swagger/OpenAPI)
- API testing with Postman collection

**REST API Demo Deliverables:**
- Swagger UI with all endpoints documented
- Postman collection for API testing
- All CRUD operations functional
- Proper HTTP status codes and error responses

---

### Project 2 Requirements (Week 11)

#### Angular Frontend (Week 9-10)
**Technologies:** Angular, TypeScript

**Example User Stories to Add:**
- Convert static HTML to Angular SPA
- Component-based architecture
- Angular routing and navigation
- Services for HTTP API calls (consuming REST API)
- Reactive forms with validation
- State management
- Integration with Spring Boot REST API

---

### Project 3 Requirements (Week 13)

#### Cloud & DevOps (Week 12)
**Technologies:** Docker, Kubernetes, GCP

**Example User Stories to Add:**
- Dockerize frontend application
- Dockerize backend application
- Create docker-compose for local development
- Deploy to GCP (Cloud Run or GKE)
- Configure Cloud SQL for MySQL
- Set up environment variables and secrets
- Implement health checks
- Configure logging and monitoring

---

## Architecture Evolution

### Project 1 Structure
```
project-name/
├── frontend/              # Static HTML/CSS/Bootstrap
│   ├── index.html
│   ├── pages/
│   ├── css/
│   └── images/
├── backend/               # Java application
│   ├── src/
│   │   ├── main/java/
│   │   │   ├── model/
│   │   │   ├── dao/
│   │   │   ├── service/
│   │   │   └── util/
│   │   └── resources/
│   └── pom.xml
├── database/
│   └── schema.sql
└── README.md
```

### Project 2 Structure
```
project-name/
├── frontend/              # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── services/
│   │   │   ├── models/
│   │   │   └── guards/
│   │   └── assets/
│   ├── angular.json
│   └── package.json
├── backend/               # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── entity/
│   │   │   │   └── dto/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
└── README.md
```

### Project 3 Structure (Cloud-Ready)
```
project-name/
├── frontend/
│   ├── src/
│   ├── Dockerfile
│   └── nginx.conf
├── backend/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── k8s/                   # Kubernetes manifests
│   ├── frontend-deployment.yaml
│   ├── backend-deployment.yaml
│   ├── services.yaml
│   └── ingress.yaml
├── docker-compose.yml     # Local development
├── cloudbuild.yaml        # GCP CI/CD
└── README.md
```

### Data Attributes for Angular Integration
All UI elements include data attributes for easy Angular migration:
```html
<div class="product-card" data-product-id="123" data-category="electronics">
<button class="add-to-cart" data-action="add" data-product="123">
<form id="contact-form" data-api-endpoint="/api/contact">
```

---

## Evaluation Criteria

### Project 1 (Week 6): Java + MySQL + JDBC

| Criteria | Weight |
|----------|--------|
| **UI Implementation** - All HTML/CSS features complete | 20% |
| **Database Design** - Proper schema, relationships | 15% |
| **JDBC Implementation** - DAO pattern, CRUD operations | 20% |
| **Business Logic** - Service layer, validation | 15% |
| **Code Quality** - Clean, organized, follows conventions | 10% |
| **Git Workflow** - Branching, PRs, commits, issues | 10% |
| **Collaboration** - Team communication, code reviews | 5% |
| **Presentation** - Demo and explanation | 5% |

### Project 2 (Week 11): Spring Boot + Angular

| Criteria | Weight |
|----------|--------|
| **Angular Frontend** - Components, routing, services | 20% |
| **Spring Boot Backend** - REST APIs, JPA entities | 20% |
| **Integration** - Frontend-backend communication | 15% |
| **Code Quality** - Clean architecture, best practices | 15% |
| **Testing** - Unit tests, API tests | 10% |
| **Git Workflow** - Branching, PRs, code reviews | 10% |
| **Collaboration & Documentation** | 5% |
| **Presentation** - Demo and explanation | 5% |

### Project 3 / Final (Week 13): Full Stack + Cloud

| Criteria | Weight |
|----------|--------|
| **Complete Application** - All features working end-to-end | 20% |
| **Containerization** - Docker setup, docker-compose | 15% |
| **Cloud Deployment** - GCP deployment, Cloud SQL | 20% |
| **DevOps** - CI/CD, environment configuration | 15% |
| **Documentation** - README, API docs, setup guide | 10% |
| **Code Quality** - Production-ready code | 10% |
| **Presentation** - Final demo, architecture explanation | 10% |

---

## Pre-Development Phase (Sprint 0)

Before writing any code, teams must complete the **Project Charter** to ensure alignment on requirements, standards, and conventions.

### Project Charter Template

Use the [Project Charter Template](./project-charter-template.md) to document:

1. **Requirements Clarification**
   - Functional requirements questions
   - Non-functional requirements questions
   - User stories with acceptance criteria

2. **Technical Standards**
   - Coding conventions (Java, Angular, SQL)
   - Project structure agreements
   - API design standards
   - Error handling strategy

3. **Git Workflow**
   - Branching strategy
   - Commit message format
   - Pull request process
   - Code review guidelines

4. **Definition of Done**
   - Code complete criteria
   - Testing requirements
   - Documentation requirements

5. **Team Agreements**
   - Roles and responsibilities
   - Meeting schedule
   - Communication channels
   - Conflict resolution

### Sprint 0 Checklist

Before starting Sprint 1:

- [ ] Team formed and roles assigned
- [ ] Project selected
- [ ] All members read project requirements
- [ ] Clarifying questions documented and answered
- [ ] Coding conventions agreed upon
- [ ] Git workflow established
- [ ] Definition of Done defined
- [ ] Project Charter completed and approved by instructor

---

## Getting Started

### Week 1-2: UI Storyboard/Wireframe Development

1. **Form your team** of 4 members
2. **Choose a project** from the list above
3. **Complete Project Charter** - Use [project-charter-template.md](./project-charter-template.md)
4. **Create a GitHub repository** with proper setup
5. **Set up the project board** with issues for all 12+ UI features
6. **Assign features** to team members (minimum 3 each)
7. **Start Sprint 1** - Begin with setup and core structure
8. **Daily standups** - Communicate progress and blockers
9. **Complete UI Storyboard** - All pages by end of Week 2
10. **UI Review/Demo** - Present wireframe walkthrough to facilitator/mentor

### Week 3-5: Java Backend (Building to Project 1)

1. **Facilitator/Mentor adds** Java/JDBC user stories to project documents
2. **Create new issues** in your repository with `java-jdbc` label
3. **Design database schema** - ERD and SQL scripts
4. **Implement DAO layer** - JDBC operations for all entities
5. **Build service layer** - Business logic and validation
6. **Integrate** - Connect UI forms to backend (Servlets or console)
7. **Test thoroughly** - Prepare for Week 6 presentation

### Week 7-8: Spring REST API (Building to REST API Demo)

1. **Facilitator/Mentor adds** Spring REST API user stories
2. **Migrate to Spring Boot** - Replace JDBC with Spring Data JPA
3. **Build REST Controllers** - Create endpoints for all resources
4. **Implement DTOs** - Request/response data transfer objects
5. **Add Swagger** - Document all API endpoints
6. **Test with Postman** - Create collection for API testing
7. **REST API Review/Demo** - End of Week 8 demo to validate API

### Week 9-10: Angular Frontend (Building to Project 2)

1. **Facilitator/Mentor adds** Angular user stories
2. **Convert to Angular** - Transform static HTML to SPA
3. **Create Services** - HTTP services to consume REST API
4. **Implement Components** - Reusable UI components
5. **Add Routing** - Navigation between views
6. **Integrate** - Connect Angular frontend to Spring backend
7. **Test and refine** - Prepare for Week 11 presentation

### Week 12: Cloud Deployment (Building to Project 3)

1. **Facilitator/Mentor adds** DevOps user stories
2. **Containerize** - Create Dockerfiles for frontend and backend
3. **Deploy to GCP** - Cloud Run or GKE with Cloud SQL
4. **Configure CI/CD** - Automate build and deployment
5. **Final polish** - Prepare for Week 13 final demo

---

## For Facilitators/Mentors: Adding User Stories by Phase

### Before Week 3 (Java/JDBC Phase)
Add to each project file:
```markdown
## Phase 2: Java Backend User Stories (Week 3-5)

### Member A - [2-3 stories related to their UI features]
### Member B - [2-3 stories related to their UI features]
### Member C - [2-3 stories related to their UI features]
### Member D - [2-3 stories related to their UI features]
```

### Before Week 7 (Spring REST API Phase)
Add to each project file:
```markdown
## Phase 3: Spring REST API User Stories (Week 7-8)
```

### Before Week 9 (Angular Phase)
Add to each project file:
```markdown
## Phase 4: Angular User Stories (Week 9-10)
```

### Before Week 12 (DevOps Phase)
Add to each project file:
```markdown
## Phase 5: Cloud Deployment User Stories (Week 12)
```

---

Good luck and happy coding! Remember, this project will grow with you throughout the 13-week training program, culminating in a fully deployed full-stack application.
