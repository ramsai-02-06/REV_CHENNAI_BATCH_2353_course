# Project Charter Template

## Instructions

Before starting development, each team must complete this charter document. This ensures all team members are aligned on requirements, standards, and conventions.

**Timeline:** Complete during Sprint 0 (Days 1-2)
**Deliverable:** Submit completed charter to instructor before writing any code

---

## Part 1: Project Overview

### 1.1 Project Selection
- **Selected Project:** ____________________
- **Team Name:** ____________________
- **Team Members:**

| Name | Role | Responsibilities |
|------|------|------------------|
| | | |
| | | |
| | | |
| | | |

### 1.2 Project Understanding

Summarize the project in your own words (2-3 sentences):

>

---

## Part 2: Requirements Clarification

### 2.1 Functional Requirements Questions

List questions about WHAT the system should do:

| # | Question | Answer (from instructor) |
|---|----------|--------------------------|
| 1 | | |
| 2 | | |
| 3 | | |
| 4 | | |
| 5 | | |

**Example questions to consider:**
- What happens when [edge case]?
- Is [feature X] required for MVP or can it be added later?
- What are the validation rules for [input field]?
- How should the system handle [error scenario]?
- What are the business rules for [specific functionality]?

### 2.2 Non-Functional Requirements Questions

List questions about HOW the system should perform:

| # | Question | Answer (from instructor) |
|---|----------|--------------------------|
| 1 | | |
| 2 | | |
| 3 | | |

**Consider asking about:**
- Performance expectations (response time, concurrent users)
- Security requirements (authentication, authorization levels)
- Browser/device support requirements
- Data volume expectations

### 2.3 User Stories Clarification

For your top 5 user stories, document acceptance criteria:

**User Story 1:** As a _______, I want to _______ so that _______

Acceptance Criteria:
- [ ]
- [ ]
- [ ]

**User Story 2:** As a _______, I want to _______ so that _______

Acceptance Criteria:
- [ ]
- [ ]
- [ ]

**User Story 3:** As a _______, I want to _______ so that _______

Acceptance Criteria:
- [ ]
- [ ]
- [ ]

---

## Part 3: Technical Standards

### 3.1 Coding Conventions

#### Java/Spring Boot Backend

| Convention | Team Decision |
|------------|---------------|
| Package naming | `com.teamname.projectname` |
| Class naming | PascalCase (e.g., `ProductService`) |
| Method naming | camelCase (e.g., `findAllProducts`) |
| Variable naming | camelCase (e.g., `productList`) |
| Constant naming | UPPER_SNAKE_CASE (e.g., `MAX_PAGE_SIZE`) |
| Indentation | Spaces / Tabs, Size: ___ |
| Max line length | ___ characters |
| Brace style | Same line / New line |

#### Angular/TypeScript Frontend

| Convention | Team Decision |
|------------|---------------|
| Component naming | kebab-case (e.g., `product-list`) |
| Service naming | PascalCase + Service (e.g., `ProductService`) |
| Interface naming | PascalCase (e.g., `Product`) |
| File naming | kebab-case (e.g., `product.service.ts`) |
| CSS class naming | BEM / kebab-case / Other: ___ |
| Indentation | Spaces / Tabs, Size: ___ |

#### SQL/Database

| Convention | Team Decision |
|------------|---------------|
| Table naming | snake_case plural (e.g., `products`) |
| Column naming | snake_case (e.g., `created_at`) |
| Primary key naming | `id` / `table_id` |
| Foreign key naming | `referenced_table_id` |
| Index naming | `idx_table_column` |

### 3.2 Project Structure

#### Backend Structure
```
src/main/java/com/teamname/projectname/
├── controller/
├── service/
├── repository/
├── model/
│   ├── entity/
│   └── dto/
├── exception/
├── config/
└── util/
```

Confirm or modify the structure above:


#### Frontend Structure
```
src/app/
├── components/
│   ├── shared/
│   └── features/
├── services/
├── models/
├── guards/
├── interceptors/
└── pipes/
```

Confirm or modify the structure above:


### 3.3 API Design Standards

| Standard | Team Decision |
|----------|---------------|
| Base URL | `/api/v1` |
| Resource naming | Plural nouns (e.g., `/products`) |
| HTTP methods | GET (read), POST (create), PUT (update), DELETE (delete) |
| Response format | JSON with consistent structure |
| Error response format | `{ "status": 400, "message": "...", "errors": [...] }` |
| Pagination format | `?page=0&size=10&sort=field,asc` |
| Date format | ISO 8601 (`2024-01-15T10:30:00Z`) |

### 3.4 Error Handling Strategy

**Backend:**
- How will you handle validation errors?
- How will you handle not found errors?
- How will you handle server errors?
- Will you use custom exception classes?

**Frontend:**
- How will you display errors to users?
- Will you use toast notifications, inline errors, or both?
- How will you handle network errors?

---

## Part 4: Git Workflow

### 4.1 Branching Strategy

| Branch Type | Naming Convention | Example |
|-------------|-------------------|---------|
| Main/Production | `main` | `main` |
| Development | `develop` | `develop` |
| Feature | `feature/description` | `feature/user-authentication` |
| Bug fix | `bugfix/description` | `bugfix/login-validation` |
| Hotfix | `hotfix/description` | `hotfix/security-patch` |

### 4.2 Commit Message Format

Choose a format:

- [ ] **Conventional Commits:** `type(scope): description`
  - Examples: `feat(auth): add login endpoint`, `fix(ui): resolve button alignment`
  - Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

- [ ] **Simple Format:** `[Type] Description`
  - Examples: `[Feature] Add login endpoint`, `[Fix] Resolve button alignment`

- [ ] **Other:** ____________________

### 4.3 Pull Request Process

- [ ] All code must be reviewed by at least ___ team member(s)
- [ ] PR must pass all tests before merge
- [ ] PR description must include:
  - [ ] Summary of changes
  - [ ] Related user story/task
  - [ ] Screenshots (for UI changes)
  - [ ] Testing steps
- [ ] Branch must be up to date with develop before merge
- [ ] Squash commits on merge: Yes / No

### 4.4 Code Review Guidelines

What will reviewers look for?
- [ ] Code follows team conventions
- [ ] No hardcoded values
- [ ] Proper error handling
- [ ] Clear variable/method names
- [ ] No unnecessary comments
- [ ] Tests included (if applicable)
- [ ] Other: ____________________

---

## Part 5: Definition of Done

A feature/user story is considered DONE when:

### Code Complete
- [ ] Code is written and follows team conventions
- [ ] Code is reviewed and approved
- [ ] Code is merged to develop branch

### Testing
- [ ] Feature works as expected (manual testing)
- [ ] Edge cases are handled
- [ ] Error scenarios are handled
- [ ] API tested with Postman (backend)
- [ ] Tested on multiple screen sizes (frontend)

### Documentation
- [ ] API endpoints documented
- [ ] Complex logic has comments
- [ ] README updated if needed

### Deployment
- [ ] Feature works in development environment
- [ ] No console errors or warnings

---

## Part 6: Communication & Collaboration

### 6.1 Meeting Schedule

| Meeting | Frequency | Time | Duration |
|---------|-----------|------|----------|
| Daily Standup | Daily | | 15 min |
| Sprint Planning | Weekly | | |
| Sprint Review | Weekly | | |
| Code Review Sessions | | | |

### 6.2 Communication Channels

| Purpose | Channel |
|---------|---------|
| Quick questions | |
| Code discussions | |
| Document sharing | |
| Video calls | |

### 6.3 Conflict Resolution

How will the team handle disagreements?

1.
2.
3.

---

## Part 7: Sprint Planning

### 7.1 MVP Features (Must Have)

List the features required for minimum viable product:

1.
2.
3.
4.
5.

### 7.2 Sprint Goals

**Sprint 1 Goal (Week 1):**


**Sprint 2 Goal (Week 2):**


**Sprint 3 Goal (Week 3):**


### 7.3 Risk Assessment

| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| | High/Med/Low | High/Med/Low | |
| | High/Med/Low | High/Med/Low | |
| | High/Med/Low | High/Med/Low | |

---

## Part 8: Pre-Development Checklist

Complete before writing any code:

### Requirements
- [ ] All team members have read the project requirements
- [ ] Clarifying questions submitted and answered
- [ ] User stories defined with acceptance criteria
- [ ] MVP scope agreed upon

### Technical Setup
- [ ] Git repository created
- [ ] Branch protection rules configured
- [ ] All team members have access
- [ ] Project structure created (backend + frontend)
- [ ] Development environment set up on all machines
- [ ] Database schema designed (ERD created)

### Standards & Conventions
- [ ] Coding conventions documented and agreed
- [ ] Git workflow agreed upon
- [ ] Definition of Done established
- [ ] Code review process defined

### Team Agreements
- [ ] Roles and responsibilities assigned
- [ ] Meeting schedule set
- [ ] Communication channels established
- [ ] Conflict resolution process agreed

---

## Signatures

By signing below, all team members agree to follow the conventions, standards, and processes defined in this charter.

| Name | Signature | Date |
|------|-----------|------|
| | | |
| | | |
| | | |
| | | |

---

## Instructor Approval

**Charter Reviewed:** [ ] Approved  [ ] Needs Revision

**Comments:**


**Instructor Signature:** ____________________  **Date:** __________
