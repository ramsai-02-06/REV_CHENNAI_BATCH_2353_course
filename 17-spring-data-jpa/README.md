# Spring Data JPA

## Overview

Spring Data JPA simplifies database access by providing repository abstractions over JPA. It eliminates boilerplate code for common data access operations.

## Learning Objectives

By the end of this module, you will be able to:
- Understand JPA and Spring Data JPA benefits
- Map Java entities to database tables
- Use JpaRepository for data access
- Write derived queries and custom @Query methods
- Implement pagination and sorting

---

## Topics Covered

### 0. [ORM and Hibernate Introduction](./topics/00-orm-hibernate-intro.md)
Foundation concepts before Spring Data JPA.

- Object-Relational Impedance Mismatch
- What ORM solves
- JPA vs Hibernate
- EntityManager and entity lifecycle states
- JPQL basics

### 1. [JPA Introduction](./topics/01-jpa-introduction.md)
Introduction to JPA and Spring Data JPA.

- What is JPA and why use it
- Spring Data JPA benefits
- Project setup and configuration
- Quick start example

### 2. [Entity Mapping](./topics/02-entity-mapping.md)
Map Java classes to database tables.

- @Entity, @Table, @Column annotations
- Primary keys and generation strategies
- Relationships: @ManyToOne, @OneToMany, @ManyToMany
- Fetch types and cascade options

### 3. [Repositories](./topics/03-repositories.md)
Use repository interfaces for data access.

- JpaRepository interface
- Derived query methods (findBy, countBy, existsBy)
- @Query annotation for custom JPQL
- Native SQL queries

### 4. [CRUD Operations](./topics/04-crud-operations.md)
Perform create, read, update, delete operations.

- Save and update with save()
- Find methods (findById, findAll)
- Delete operations
- Service and controller patterns

### 5. [Pagination and Sorting](./topics/05-pagination-sorting.md)
Handle large datasets efficiently.

- Pageable and PageRequest
- Sort object for ordering
- Page object and metadata
- REST API pagination patterns

---

## Topic Flow

```
┌─────────────────────┐
│ 0. ORM & Hibernate  │  Why ORM exists
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 1. JPA Introduction │  Spring Data JPA
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 2. Entity Mapping   │  Classes to tables
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 3. Repositories     │  Query methods
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 4. CRUD Operations  │  Practical usage
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 5. Pagination       │  Large datasets
└─────────────────────┘
```

---

## Key Concepts Summary

| Concept | Description |
|---------|-------------|
| **ORM** | Object-Relational Mapping - bridges objects and tables |
| **Impedance Mismatch** | Objects and relational tables don't naturally align |
| **JPA** | Java Persistence API - ORM specification |
| **Hibernate** | JPA implementation (default in Spring) |
| **EntityManager** | JPA interface for CRUD operations |
| **Entity States** | Transient, Managed, Detached, Removed |
| **Entity** | Java class mapped to database table |
| **Repository** | Interface for data access operations |
| **Derived Query** | Query generated from method name |
| **@Query** | Custom JPQL or native SQL |
| **Pageable** | Pagination and sorting parameters |

---

## Exercises

See the [exercises](./exercises/) directory for hands-on practice problems.

## Additional Resources

- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [JPA Query Methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)

---

## Next Steps

After completing this module, continue to **Spring Security** to learn about authentication and authorization.

---

**Duration:** 5 days | **Difficulty:** Intermediate | **Prerequisites:** Module 16 (Spring MVC)
