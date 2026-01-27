# Spring Data JPA Key Concepts

## Overview

Spring Data JPA provides repository abstractions over JPA, eliminating boilerplate for common data access operations.

---

## 1. ORM and Hibernate Fundamentals

### Object-Relational Impedance Mismatch

| Java Objects | Relational Tables |
|--------------|-------------------|
| Inheritance | No direct equivalent |
| Object references | Foreign keys |
| Collections | Separate table + FK |
| Identity (== vs .equals()) | Primary key |

### JPA vs Hibernate

- **JPA**: Specification (interfaces + annotations)
- **Hibernate**: Implementation (default in Spring Boot)

### Entity Lifecycle States

| State | Description |
|-------|-------------|
| **Transient** | New object, not tracked |
| **Managed** | Tracked, changes auto-sync |
| **Detached** | Was managed, now disconnected |
| **Removed** | Scheduled for deletion |

### First-Level Cache

```java
User u1 = em.find(User.class, 1L);  // SQL executed
User u2 = em.find(User.class, 1L);  // Cache hit - no SQL
u1 == u2;  // true - same instance
```

- Always on, transaction-scoped
- Guarantees identity within session
- Enables dirty checking

### Entity equals() and hashCode()

**Problem:** Using @Id in equals/hashCode breaks collections when ID changes after persist.

**Solution:** Use business key (email, ISBN) or UUID set at creation:

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return email != null && email.equals(user.email);
}

@Override
public int hashCode() {
    return email != null ? email.hashCode() : 0;
}
```

### Common Pitfalls

| Pitfall | Cause | Solution |
|---------|-------|----------|
| **LazyInitializationException** | Accessing lazy collection outside transaction | JOIN FETCH, @EntityGraph, or DTO |
| **N+1 Problem** | Loop triggers separate query per entity | JOIN FETCH or batch fetching |
| **Changes not persisting** | Missing @Transactional | Add @Transactional to service methods |

```java
// N+1 Problem
List<User> users = repo.findAll();       // 1 query
for (User u : users) u.getOrders();      // N queries!

// Solution: JOIN FETCH
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();          // 1 query
```

---

## 2. JPA Introduction

### What is JPA?

JPA (Java Persistence API) is an ORM specification that maps Java objects to database tables.

```
Java Objects  ←→  JPA/Hibernate  ←→  Database Tables
```

### Spring Data JPA Benefits

| Without Spring Data JPA | With Spring Data JPA |
|------------------------|---------------------|
| Write EntityManager code | Interface only |
| Implement CRUD manually | Auto-implemented |
| Write JPQL for queries | Method names generate queries |

```java
// All you need
public interface UserRepository extends JpaRepository<User, Long> {
    // CRUD methods inherited
}
```

---

## 3. Entity Mapping

### Basic Entity

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(unique = true)
    private String email;

    // Getters, setters
}
```

### Relationships

```java
// Many-to-One (most common)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;

// One-to-Many (inverse)
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
private List<Employee> employees;
```

### Key Annotations

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Mark class as JPA entity |
| `@Table` | Customize table name |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-generate ID |
| `@Column` | Customize column |
| `@ManyToOne` | Many-to-one relationship |
| `@OneToMany` | One-to-many relationship |
| `@JoinColumn` | Foreign key column |

---

## 4. Repository Interface

### JpaRepository

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Inherited methods:
    // save(), findById(), findAll(), delete(), count(), existsById()
}
```

### Derived Query Methods

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Find
    User findByEmail(String email);
    List<User> findByLastName(String lastName);
    List<User> findByAgeGreaterThan(int age);

    // String matching
    List<User> findByEmailContaining(String part);
    List<User> findByUsernameStartingWith(String prefix);

    // Boolean/Null
    List<User> findByActiveTrue();
    List<User> findByPhoneIsNull();

    // Count/Exists
    long countByActive(boolean active);
    boolean existsByEmail(String email);

    // Order
    List<User> findByActiveOrderByCreatedAtDesc(boolean active);
}
```

### @Query (Custom JPQL)

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmailQuery(@Param("email") String email);

@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role")
List<User> findByRole(@Param("role") String role);

@Modifying
@Transactional
@Query("UPDATE User u SET u.active = :active WHERE u.id = :id")
int updateStatus(@Param("id") Long id, @Param("active") boolean active);
```

---

## 5. CRUD Operations

### Service Pattern

```java
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public User create(User user) {
        return userRepository.save(user);
    }

    // READ
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    // UPDATE
    public User update(Long id, UserDTO dto) {
        User user = findById(id);
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    // DELETE
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
```

### save() Behavior

- `id == null` → INSERT
- `id != null` → UPDATE

---

## 6. Pagination and Sorting

### Pageable

```java
// Create Pageable
Pageable pageable = PageRequest.of(0, 10);  // page 0, size 10

// With sorting
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
```

### Repository Methods

```java
Page<Product> findAll(Pageable pageable);
Page<Product> findByCategory(String category, Pageable pageable);
```

### Page Object

```java
Page<Product> page = repository.findAll(PageRequest.of(0, 10));

List<Product> content = page.getContent();
int currentPage = page.getNumber();
long totalElements = page.getTotalElements();
int totalPages = page.getTotalPages();
boolean hasNext = page.hasNext();
```

### REST Controller

```java
@GetMapping
public Page<Product> getProducts(Pageable pageable) {
    return productService.findAll(pageable);
}
// Request: GET /products?page=0&size=20&sort=name,asc
```

---

## Quick Reference

### Entity Template

```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Constructors, getters, setters
}
```

### Repository Template

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(Double min, Double max);
    Page<Product> findByCategory(String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    List<Product> search(@Param("keyword") String keyword);
}
```

### Service Template

```java
@Service
@Transactional
public class ProductService {
    private final ProductRepository repository;

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
```

---

## Checklist

By the end of this module, you should be able to:

- [ ] Create JPA entities with proper annotations
- [ ] Define relationships (@ManyToOne, @OneToMany)
- [ ] Use JpaRepository for CRUD operations
- [ ] Write derived query methods
- [ ] Use @Query for custom JPQL
- [ ] Implement pagination with Pageable and Page
- [ ] Build REST APIs with paginated responses
