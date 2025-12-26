# Job Portal Website

## Project Overview

**Project Name:** CareerConnect  
**Industry:** Recruitment & Employment  
**Type:** Multi-page Job Portal Website (Frontend)

---

## Description

Build a professional job portal website called **CareerConnect** that connects job seekers with employers.  
The platform allows users to browse job listings, apply for jobs, and enables recruiters/admins to manage postings and applications.

---

## Target Audience

- Fresh graduates and job seekers  
- Professionals looking for career growth  
- Recruiters and HR teams  
- Internship seekers  

---

## Pages Required

| Page | Primary Owner | Description |
|-----|--------------|-------------|
| Home | Member A | Landing page with job search, highlights |
| Jobs | Member C | Job listings with filters |
| Job Details | Member C | Detailed view of a single job |
| Apply Job | Member B | Application form for candidates |
| Employer/Admin | Member D | Dashboard UI for job postings |
| About | Member A | Platform overview and mission |
| Contact | Member B | Contact form and support info |

---

## User Stories by Team Member

---

## 👤 Member A – Core Layout & Home

### Feature 1: Responsive Navigation Header

**Issue Title:** `feat: Implement responsive job portal navbar`

**User Story:**  
As a **user**, I want to **navigate easily across pages** so that **I can access jobs, applications, and company info quickly**.

**Acceptance Criteria:**
- Logo links to Home page  
- Navigation links: Home, Jobs, About, Contact, Login  
- Desktop: Horizontal navbar  
- Mobile: Hamburger menu  
- Sticky navigation on scroll  
- Active page highlighting  
- Bootstrap navbar component  

**Technical Notes:**
- Bootstrap `navbar`, `navbar-toggler`  
- Utility classes for spacing and alignment  

---

### Feature 2: Home Page Hero with Job Search

**Issue Title:** `feat: Build home page hero with job search`

**User Story:**  
As a **job seeker**, I want to **search jobs by role and location** so that **I can find relevant opportunities quickly**.

**Acceptance Criteria:**
- Hero section with headline and tagline  
- Job search form (Role, Location)  
- Search button (UI only)  
- CTA buttons: “Browse Jobs”, “Post a Job”  
- Minimum height: 70vh  
- Responsive layout  

**Technical Notes:**
- Bootstrap grid  
- Form controls with placeholders  
- Background image with overlay  

---

### Feature 3: About Page – Platform Overview

**Issue Title:** `feat: Create about page for job portal`

**User Story:**  
As a **visitor**, I want to **understand what CareerConnect does** so that **I can trust and use the platform**.

**Acceptance Criteria:**
- Hero banner  
- “What We Do” section  
- Mission & Vision statements  
- Benefits for Job Seekers & Employers  
- Responsive layout  

**Technical Notes:**
- Semantic sections  
- Icons for features  
- Bootstrap grid  

---

## 👤 Member B – Forms & Communication

### Feature 4: Job Application Form

**Issue Title:** `feat: Build job application form`

**User Story:**  
As a **candidate**, I want to **apply for a job online** so that **I can submit my details easily**.

**Acceptance Criteria:**
- Fields: Name, Email, Phone, Resume upload  
- Experience level dropdown  
- Skills input  
- Cover letter textarea  
- Required field validation  
- Submit & Reset buttons  
- Success message after submit (UI)  

**Technical Notes:**
- HTML5 validation  
- Bootstrap form classes  
- File input (resume)  

---

### Feature 5: Contact Page

**Issue Title:** `feat: Create contact page with support form`

**User Story:**  
As a **user**, I want to **contact support** so that **I can resolve queries or issues**.

**Acceptance Criteria:**
- Contact form: Name, Email, Subject, Message  
- Subject dropdown options  
- Company contact details  
- Support email & phone  
- Responsive layout  

**Technical Notes:**
- Bootstrap grid  
- Card layout for contact info  

---

### Feature 6: Newsletter Signup

**Issue Title:** `feat: Add job alerts newsletter signup`

**User Story:**  
As a **job seeker**, I want to **receive job alerts by email** so that **I don’t miss opportunities**.

**Acceptance Criteria:**
- Email input  
- Subscribe button  
- Checkbox for consent  
- Validation feedback  
- Styled standalone section  

**Technical Notes:**
- Bootstrap input group  
- Alert components for feedback  

---

## 👤 Member C – Jobs & Listings

### Feature 7: Job Listings Page

**Issue Title:** `feat: Build job listings page`

**User Story:**  
As a **job seeker**, I want to **browse job listings** so that **I can find suitable roles**.

**Acceptance Criteria:**
- Job cards with title, company, location  
- Job type (Full-time, Part-time, Internship)  
- Apply button  
- Pagination or load more (UI)  
- Responsive grid layout  

**Technical Notes:**
- Bootstrap cards  
- Grid layout  
- Dummy job data  

---

### Feature 8: Job Filters & Search

**Issue Title:** `feat: Add job filter and search UI`

**User Story:**  
As a **user**, I want to **filter jobs** so that **I can narrow results easily**.

**Acceptance Criteria:**
- Filter by location  
- Filter by job type  
- Filter by experience level  
- Search input  
- Filter sidebar or top bar  

**Technical Notes:**
- Checkbox & select inputs  
- JavaScript DOM filtering (optional)  

---

### Feature 9: Job Details Page

**Issue Title:** `feat: Create job details page`

**User Story:**  
As a **candidate**, I want to **view full job details** so that **I can decide whether to apply**.

**Acceptance Criteria:**
- Job title & company info  
- Job description  
- Responsibilities list  
- Skills required  
- Apply Now button  
- Responsive layout  

**Technical Notes:**
- Semantic HTML (`article`, `section`)  
- Lists for responsibilities  

---

## 👤 Member D – Employer/Admin UI

### Feature 10: Employer Dashboard (UI Only)

**Issue Title:** `feat: Build employer dashboard UI`

**User Story:**  
As an **employer**, I want to **manage job postings** so that **I can hire efficiently**.

**Acceptance Criteria:**
- Dashboard overview cards  
- List of posted jobs  
- Buttons: View, Edit, Delete (UI)  
- Applicant count per job  
- Responsive table or cards  

**Technical Notes:**
- Bootstrap tables/cards  
- Icons for actions  

---

### Feature 11: Post a Job Form

**Issue Title:** `feat: Create post job form`

**User Story:**  
As an **employer**, I want to **post a new job** so that **candidates can apply**.

**Acceptance Criteria:**
- Job title  
- Company name  
- Location  
- Job type  
- Description textarea  
- Skills required  
- Submit button  

**Technical Notes:**
- Bootstrap forms  
- Fieldsets & legends  

---

### Feature 12: Applicants List Page

**Issue Title:** `feat: Build applicants list UI`

**User Story:**  
As an **employer**, I want to **view applicants** so that **I can review candidates**.

**Acceptance Criteria:**
- Table with applicant name, email, job applied  
- Resume view/download button (UI)  
- Status badge (New, Reviewed)  
- Responsive table  

**Technical Notes:**
- Bootstrap table  
- Badges for status  

---

## Technical Requirements Checklist

### HTML Elements
- Semantic layout elements  
- Forms and inputs  
- Tables for listings  
- Lists for job details  
- Media elements  

### Bootstrap Components
- Navbar  
- Grid system  
- Cards  
- Forms  
- Tables  
- Buttons  
- Badges  
- Alerts  

### Responsive Breakpoints
- Mobile (<576px)  
- Tablet (576px–991px)  
- Desktop (992px+)  

---

## Git Issues to Create

- feat: Implement responsive job portal navbar  
- feat: Build home page hero with job search  
- feat: Create about page for job portal  
- feat: Build job application form  
- feat: Create contact page with support form  
- feat: Add job alerts newsletter signup  
- feat: Build job listings page  
- feat: Add job filter and search UI  
- feat: Create job details page  
- feat: Build employer dashboard UI  
- feat: Create post job form  
- feat: Build applicants list UI  

---

## Definition of Done

A feature is considered **DONE** when:
- Code committed to feature branch  
- Pull request created  
- Code reviewed  
- Acceptance criteria met  
- Responsive on all devices  
- HTML validated  
- Merged to develop/main branch  
- Issue closed with PR reference  
