# Week 07 - Interview FAQ

This document contains frequently asked interview questions and comprehensive answers for Week 07 topics: ORM/Hibernate Fundamentals and Spring Data JPA.

---

## Table of Contents

1. [ORM Fundamentals](#orm-fundamentals)
2. [JPA and Hibernate Basics](#jpa-and-hibernate-basics)
3. [Entity Lifecycle and Caching](#entity-lifecycle-and-caching)
4. [Entity Mapping](#entity-mapping)
5. [Relationships](#relationships)
6. [Spring Data JPA Repositories](#spring-data-jpa-repositories)
7. [Query Methods](#query-methods)
8. [Pagination and Sorting](#pagination-and-sorting)
9. [Common Pitfalls](#common-pitfalls)

---

## ORM Fundamentals

### Q1: What is ORM? Why do we need it?

**Answer:**

**ORM (Object-Relational Mapping)** is a technique that maps objects to relational database tables, bridging the gap between object-oriented programming and relational databases.

**The Problem: Object-Relational Impedance Mismatch**

| Object World | Relational World |
|--------------|------------------|
| Classes and inheritance | Tables (no inheritance) |
| Object references | Foreign keys |
| Collections | Join tables |
| Identity (==) | Primary keys |
| Encapsulation | All columns exposed |

**Example of the mismatch:**
```java
// Object world - has inheritance
public class Employee extends Person {
    private Department department;  // Reference
    private List<Project> projects; // Collection
}

// Relational world - flat tables
// PERSON table, EMPLOYEE table (joined),
// DEPARTMENT table (foreign key),
// EMPLOYEE_PROJECT join table
```

**Why ORM:**
1. **Productivity**: Write less boilerplate JDBC code
2. **Maintainability**: Object-oriented code is easier to understand
3. **Portability**: Database-independent (mostly)
4. **Performance**: Built-in caching and optimization

**Key point:** ORM lets you work with objects while it handles the SQL translation behind the scenes.

---

### Q2: What is the difference between JPA and Hibernate?

**Answer:**

**JPA (Java Persistence API)** is a specification that defines how Java objects should be persisted to relational databases.

**Hibernate** is the most popular implementation of the JPA specification.

```
┌─────────────────────────────────────────┐
│             Your Application            │
├─────────────────────────────────────────┤
│        JPA API (Specification)          │
│   EntityManager, @Entity, @Id, etc.     │
├─────────────────────────────────────────┤
│       Hibernate (Implementation)        │
│   Session, SessionFactory, HQL, etc.    │
├─────────────────────────────────────────┤
│              JDBC Driver                │
├─────────────────────────────────────────┤
│              Database                   │
└─────────────────────────────────────────┘
```

**Comparison:**

| Aspect | JPA | Hibernate |
|--------|-----|-----------|
| Type | Specification | Implementation |
| Package | jakarta.persistence | org.hibernate |
| Query Language | JPQL | HQL (superset of JPQL) |
| Main Interface | EntityManager | Session |
| Portability | Can switch implementations | Hibernate-specific |

**Best practice:** Code to JPA interfaces, use Hibernate as the implementation. This keeps your code portable.

```java
// Good - uses JPA
@PersistenceContext
private EntityManager entityManager;

// Avoid - Hibernate-specific
private SessionFactory sessionFactory;
```

---

## JPA and Hibernate Basics

### Q3: Explain the JPA Entity Lifecycle states.

**Answer:**

JPA entities have four lifecycle states:

```
    ┌───────────────────────────────────────────────────────┐
    │                                                       │
    ▼                                                       │
┌────────┐  persist()  ┌─────────┐  close()/clear()  ┌──────────┐
│Transient│───────────>│ Managed │─────────────────>│ Detached │
└────────┘             └─────────┘                   └──────────┘
                           │                              │
                      remove()                        merge()
                           │                              │
                           ▼                              │
                      ┌─────────┐                         │
                      │ Removed │<────────────────────────┘
                      └─────────┘
```

**1. Transient (New)**
```java
User user = new User();  // Transient
user.setName("John");    // Still transient
// Not associated with any persistence context
// No database row exists
```

**2. Managed (Persistent)**
```java
entityManager.persist(user);  // Now managed
user.setName("Jane");         // Change tracked!
// Associated with persistence context
// Changes automatically synced to database
```

**3. Detached**
```java
entityManager.detach(user);  // Now detached
// Or when EntityManager is closed
user.setName("Bob");         // NOT tracked
// Must merge() to re-attach
```

**4. Removed**
```java
entityManager.remove(user);  // Scheduled for deletion
// Still managed until flush/commit
// Database row deleted on flush
```

**Key point:** Only **Managed** entities have automatic dirty checking. Changes to Transient or Detached entities are not persisted automatically.

---

### Q4: What is the First-Level Cache? How does it work?

**Answer:**

The **First-Level Cache** is a session-scoped cache that stores entities within a persistence context (transaction).

**Key characteristics:**
- **Always enabled** - cannot be disabled
- **Session scoped** - each EntityManager/Session has its own
- **Identity guarantee** - same entity returned for same ID within transaction
- **Automatic** - no configuration needed

**How it works:**

```java
@Transactional
public void demonstrateCache() {
    // First query - hits database, caches entity
    User user1 = entityManager.find(User.class, 1L);  // SQL executed

    // Second query - returns cached entity, NO SQL
    User user2 = entityManager.find(User.class, 1L);  // No SQL!

    // Same instance
    System.out.println(user1 == user2);  // true
}
```

**Benefits:**
1. **Performance**: Avoids repeated database queries
2. **Identity guarantee**: `user1 == user2` within same transaction
3. **Dirty checking**: Cache holds original state for comparison

**Limitations:**
- Cache cleared when session closes
- Not shared across sessions/transactions
- Can cause memory issues with large result sets

```java
// Clearing cache manually
entityManager.clear();  // Clears entire cache
entityManager.detach(user);  // Removes single entity
```

---

### Q5: What is Dirty Checking in Hibernate?

**Answer:**

**Dirty Checking** is Hibernate's automatic detection of entity changes. When a transaction commits, Hibernate compares the current state with the snapshot taken when the entity was loaded.

```java
@Transactional
public void updateUser(Long id) {
    User user = entityManager.find(User.class, id);  // Snapshot taken

    user.setName("New Name");  // Entity is "dirty"

    // No explicit save/update needed!
    // Hibernate detects change and generates UPDATE
}
// Transaction commits → UPDATE executed automatically
```

**How it works:**

```
1. Entity loaded → Snapshot stored in cache
2. Entity modified → Current state differs from snapshot
3. Transaction commits → Hibernate compares states
4. If different → Generate and execute UPDATE
```

**When dirty checking occurs:**
- At transaction commit
- When `flush()` is called
- Before executing queries (auto-flush)

**Important:** Dirty checking only works for **Managed** entities:

```java
@Transactional
public void updateUser(Long id) {
    User user = new User();       // Transient - no dirty checking
    user.setId(id);
    user.setName("New Name");
    // Nothing happens! Entity not managed

    entityManager.merge(user);    // Now changes will persist
}
```

---

### Q6: What is the N+1 Query Problem? How do you solve it?

**Answer:**

The **N+1 Problem** occurs when Hibernate executes 1 query for parent entities + N additional queries for their associations.

**Example:**
```java
@Entity
public class Author {
    @Id
    private Long id;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;
}

// This causes N+1:
List<Author> authors = authorRepository.findAll();  // 1 query
for (Author author : authors) {
    author.getBooks().size();  // N queries (one per author)
}

// If 100 authors: 1 + 100 = 101 queries!
```

**Solutions:**

**1. JOIN FETCH (JPQL)**
```java
@Query("SELECT a FROM Author a JOIN FETCH a.books")
List<Author> findAllWithBooks();
// Single query with JOIN
```

**2. @EntityGraph**
```java
@EntityGraph(attributePaths = {"books"})
List<Author> findAll();
// Adds LEFT JOIN automatically
```

**3. Batch Fetching**
```java
@OneToMany(mappedBy = "author")
@BatchSize(size = 25)
private List<Book> books;
// Fetches 25 authors' books per query
// 100 authors = 1 + 4 = 5 queries
```

**Detection:**
```properties
# Enable SQL logging
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

**Key point:** Always check for N+1 when accessing collections in loops. Use JOIN FETCH or @EntityGraph for eager loading when needed.

---

## Entity Mapping

### Q7: Explain the different relationship mappings in JPA.

**Answer:**

JPA supports four relationship types:

**1. @OneToOne**
```java
@Entity
public class User {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
```

**2. @OneToMany / @ManyToOne (Bidirectional)**
```java
@Entity
public class Author {
    @Id
    private Long id;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
}

@Entity
public class Book {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;  // Owning side (has FK)
}
```

**3. @ManyToMany**
```java
@Entity
public class Student {
    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}

@Entity
public class Course {
    @Id
    private Long id;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
```

**Key concepts:**

| Concept | Description |
|---------|-------------|
| **Owning side** | Has @JoinColumn, contains foreign key |
| **Inverse side** | Has mappedBy, doesn't own relationship |
| **mappedBy** | Points to field name on owning side |
| **cascade** | Propagates operations to related entities |

**Default fetch types:**

| Relationship | Default Fetch |
|--------------|---------------|
| @OneToOne | EAGER |
| @ManyToOne | EAGER |
| @OneToMany | LAZY |
| @ManyToMany | LAZY |

---

### Q8: What is the difference between CascadeType and orphanRemoval?

**Answer:**

Both affect how operations propagate to related entities, but they work differently:

**CascadeType** - Propagates operations from parent to child:

```java
@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
private List<Book> books;

// When you persist author, books are also persisted
entityManager.persist(author);  // Also persists author.books

// When you remove author, books are also removed
entityManager.remove(author);   // Also removes author.books
```

**Cascade types:**
- `PERSIST` - Cascade persist operations
- `MERGE` - Cascade merge operations
- `REMOVE` - Cascade remove operations
- `REFRESH` - Cascade refresh operations
- `DETACH` - Cascade detach operations
- `ALL` - All of the above

**orphanRemoval** - Removes child when disconnected from parent:

```java
@OneToMany(mappedBy = "author", orphanRemoval = true)
private List<Book> books;

// Removing from collection deletes the entity
author.getBooks().remove(book);  // Book deleted from database!

// Without orphanRemoval, book would just have null author
```

**Comparison:**

| Scenario | CascadeType.REMOVE | orphanRemoval = true |
|----------|-------------------|---------------------|
| Delete parent | Children deleted | Children deleted |
| Remove child from collection | Child NOT deleted | Child deleted |
| Set collection to null | Children NOT deleted | Children deleted |

**Key point:** Use `orphanRemoval` when a child entity has no meaning without its parent (composition). Use `CascadeType.REMOVE` when you only want deletion to cascade when parent is explicitly removed.

---

## Spring Data JPA Repositories

### Q9: What is Spring Data JPA? What interfaces does it provide?

**Answer:**

**Spring Data JPA** simplifies data access by providing repository abstractions that eliminate boilerplate code.

**Repository hierarchy:**

```
Repository (marker interface)
    │
    ▼
CrudRepository (basic CRUD)
    │
    ▼
PagingAndSortingRepository (+ pagination/sorting)
    │
    ▼
JpaRepository (+ JPA specific operations)
```

**Interface comparison:**

| Interface | Key Methods |
|-----------|-------------|
| `Repository` | Marker interface only |
| `CrudRepository` | save, findById, findAll, delete, count, existsById |
| `PagingAndSortingRepository` | + findAll(Pageable), findAll(Sort) |
| `JpaRepository` | + flush, saveAndFlush, deleteInBatch, getOne |

**Usage:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Inherits 20+ methods automatically
    // Add custom methods as needed
}

@Service
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
```

**Benefits:**
1. No implementation needed - Spring generates it
2. Derived queries from method names
3. Custom queries with @Query
4. Pagination and sorting built-in
5. Auditing support (@CreatedDate, @LastModifiedDate)

---

### Q10: Explain derived query methods in Spring Data JPA.

**Answer:**

**Derived queries** are repository methods where Spring generates the query from the method name.

**Syntax:**
```
find|read|get|query|count|exists + By + Property + Condition
```

**Examples:**

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Simple property match
    List<User> findByEmail(String email);
    // SELECT u FROM User u WHERE u.email = ?1

    // Multiple conditions
    List<User> findByFirstNameAndLastName(String first, String last);
    // WHERE u.firstName = ?1 AND u.lastName = ?2

    // Or condition
    List<User> findByFirstNameOrLastName(String first, String last);
    // WHERE u.firstName = ?1 OR u.lastName = ?2

    // Comparison operators
    List<User> findByAgeLessThan(int age);
    List<User> findByAgeGreaterThanEqual(int age);
    List<User> findByAgeBetween(int start, int end);

    // String matching
    List<User> findByEmailContaining(String text);
    List<User> findByEmailStartingWith(String prefix);
    List<User> findByEmailEndingWith(String suffix);
    List<User> findByEmailLike(String pattern);  // Use % wildcards

    // Null checks
    List<User> findByMiddleNameIsNull();
    List<User> findByMiddleNameIsNotNull();

    // Boolean
    List<User> findByActiveTrue();
    List<User> findByActiveFalse();

    // Ordering
    List<User> findByLastNameOrderByFirstNameAsc(String lastName);
    List<User> findByLastNameOrderByFirstNameDesc(String lastName);

    // Limiting results
    User findFirstByOrderByCreatedAtDesc();
    List<User> findTop10ByOrderByScoreDesc();

    // Count and exists
    long countByStatus(Status status);
    boolean existsByEmail(String email);

    // Delete
    void deleteByEmail(String email);
    long deleteByStatus(Status status);  // Returns count

    // Nested properties
    List<User> findByAddressCity(String city);
    // WHERE u.address.city = ?1
}
```

**Keywords:**

| Keyword | Sample | JPQL |
|---------|--------|------|
| And | findByFirstNameAndLastName | where x.firstName = ?1 and x.lastName = ?2 |
| Or | findByFirstNameOrLastName | where x.firstName = ?1 or x.lastName = ?2 |
| Between | findByAgeBetween | where x.age between ?1 and ?2 |
| LessThan | findByAgeLessThan | where x.age < ?1 |
| GreaterThan | findByAgeGreaterThan | where x.age > ?1 |
| IsNull | findByAgeIsNull | where x.age is null |
| Like | findByFirstNameLike | where x.firstName like ?1 |
| Containing | findByFirstNameContaining | where x.firstName like %?1% |
| OrderBy | findByAgeOrderByLastNameDesc | where x.age = ?1 order by x.lastName desc |

---

### Q11: How do you write custom queries with @Query?

**Answer:**

When derived queries aren't sufficient, use `@Query` for custom JPQL or native SQL.

**JPQL Queries:**
```java
public interface UserRepository extends JpaRepository<User, Long> {

    // Basic JPQL
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();

    // Positional parameters
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.status = ?2")
    List<User> findByEmailAndStatus(String email, Status status);

    // Named parameters (preferred)
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = :status")
    List<User> findByEmailAndStatus(
        @Param("email") String email,
        @Param("status") Status status
    );

    // JOIN FETCH for eager loading
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    // Projection to DTO
    @Query("SELECT new com.example.dto.UserSummary(u.id, u.name, u.email) FROM User u")
    List<UserSummary> findUserSummaries();

    // Aggregation
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    long countByStatus(@Param("status") Status status);

    @Query("SELECT u.department, COUNT(u) FROM User u GROUP BY u.department")
    List<Object[]> countByDepartment();
}
```

**Native SQL Queries:**
```java
@Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
User findByEmailNative(String email);

@Query(value = "SELECT * FROM users u WHERE u.created_at > :date",
       nativeQuery = true)
List<User> findUsersCreatedAfter(@Param("date") LocalDate date);
```

**Modifying Queries:**
```java
@Modifying
@Transactional
@Query("UPDATE User u SET u.status = :status WHERE u.lastLogin < :date")
int deactivateInactiveUsers(
    @Param("status") Status status,
    @Param("date") LocalDate date
);

@Modifying
@Transactional
@Query("DELETE FROM User u WHERE u.status = 'DELETED'")
void purgeDeletedUsers();
```

**Important notes:**
- JPQL uses entity/field names, not table/column names
- @Modifying is required for UPDATE/DELETE queries
- @Transactional is needed for modifying queries
- Native queries use actual SQL and database names

---

## Pagination and Sorting

### Q12: How do you implement pagination in Spring Data JPA?

**Answer:**

Spring Data JPA provides built-in pagination through `Pageable` and `Page` interfaces.

**Basic pagination:**
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByStatus(Status status, Pageable pageable);
}

@Service
public class UserService {

    public Page<User> getActiveUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByStatus(Status.ACTIVE, pageable);
    }
}
```

**Pagination with sorting:**
```java
public Page<User> getActiveUsersSorted(int page, int size) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    Pageable pageable = PageRequest.of(page, size, sort);
    return userRepository.findByStatus(Status.ACTIVE, pageable);
}

// Multiple sort fields
public Page<User> getUsers(int page, int size) {
    Sort sort = Sort.by(
        Sort.Order.asc("lastName"),
        Sort.Order.desc("firstName")
    );
    Pageable pageable = PageRequest.of(page, size, sort);
    return userRepository.findAll(pageable);
}
```

**Page interface methods:**
```java
Page<User> page = userRepository.findAll(pageable);

// Content
List<User> users = page.getContent();

// Pagination info
int currentPage = page.getNumber();        // 0-based
int pageSize = page.getSize();
long totalElements = page.getTotalElements();
int totalPages = page.getTotalPages();

// Navigation
boolean hasNext = page.hasNext();
boolean hasPrevious = page.hasPrevious();
boolean isFirst = page.isFirst();
boolean isLast = page.isLast();
```

**Controller example:**
```java
@GetMapping("/users")
public Page<User> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {

    Sort sort = direction.equalsIgnoreCase("asc")
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    return userRepository.findAll(pageable);
}
```

**Slice vs Page:**
- `Page` - includes total count (extra COUNT query)
- `Slice` - no total count (more efficient for infinite scroll)

```java
Slice<User> findByStatus(Status status, Pageable pageable);
```

---

## Common Pitfalls

### Q13: What is LazyInitializationException and how do you fix it?

**Answer:**

`LazyInitializationException` occurs when you access a lazy-loaded association after the persistence context (session) is closed.

**Cause:**
```java
@Service
public class UserService {

    @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }  // Transaction ends, session closes
}

@Controller
public class UserController {

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userService.getUser(id);

        // Session is closed!
        user.getRoles().size();  // LazyInitializationException!

        return "user";
    }
}
```

**Solutions:**

**1. JOIN FETCH in query:**
```java
@Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id")
Optional<User> findByIdWithRoles(@Param("id") Long id);
```

**2. @EntityGraph:**
```java
@EntityGraph(attributePaths = {"roles"})
Optional<User> findById(Long id);
```

**3. Initialize within transaction:**
```java
@Transactional
public User getUserWithRoles(Long id) {
    User user = userRepository.findById(id).orElseThrow();
    user.getRoles().size();  // Force initialization
    return user;
}
```

**4. Use DTOs (best for APIs):**
```java
@Transactional(readOnly = true)
public UserDTO getUserDTO(Long id) {
    User user = userRepository.findById(id).orElseThrow();
    return new UserDTO(user);  // Copy needed data
}
```

**5. Open Session in View (not recommended):**
```properties
spring.jpa.open-in-view=true  # Default, but has drawbacks
```

**Best practice:** Use JOIN FETCH or DTOs rather than keeping session open.

---

### Q14: How do you implement equals() and hashCode() for JPA entities?

**Answer:**

Implementing equals/hashCode for entities is tricky because the ID is null before persist.

**Bad approach - using @Id:**
```java
// DON'T DO THIS
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);  // Problem: id is null before persist
}
```

**Problem scenario:**
```java
Set<User> users = new HashSet<>();
User user = new User();  // id is null
users.add(user);         // Stored with hashCode based on null id

userRepository.save(user);  // id is now 1

users.contains(user);  // FALSE! hashCode changed
```

**Solution 1: Business Key**
```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;  // Natural business key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return email != null && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
```

**Solution 2: UUID (when no natural key)**
```java
@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid = UUID.randomUUID();  // Set on creation

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return uuid.equals(order.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
```

**Rules:**
1. Use immutable business key if available (email, ISBN)
2. Use UUID if no natural business key
3. Never use @Id alone for equals/hashCode
4. Always implement both equals and hashCode together

---

### Q15: What is @Transactional and where should you place it?

**Answer:**

`@Transactional` manages transaction boundaries, ensuring operations are atomic.

**Placement:**
```java
// On service class (common)
@Service
@Transactional
public class UserService {
    // All methods are transactional
}

// On specific methods (fine-grained control)
@Service
public class UserService {

    @Transactional
    public User createUser(UserDTO dto) {
        // Transactional
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        // Read-only transaction (performance optimization)
    }
}
```

**Important attributes:**

```java
@Transactional(
    readOnly = true,                    // Optimization for reads
    propagation = Propagation.REQUIRED, // Default
    isolation = Isolation.DEFAULT,      // Database default
    timeout = 30,                       // Seconds
    rollbackFor = Exception.class       // Rollback on this exception
)
```

**Propagation types:**

| Propagation | Behavior |
|-------------|----------|
| REQUIRED (default) | Join existing or create new |
| REQUIRES_NEW | Always create new (suspend existing) |
| SUPPORTS | Join if exists, else non-transactional |
| MANDATORY | Must have existing, else exception |
| NEVER | Must NOT have existing, else exception |

**Common mistakes:**

```java
// 1. Self-invocation doesn't work
@Service
public class UserService {

    public void processUsers() {
        for (User user : users) {
            updateUser(user);  // @Transactional ignored! Same class
        }
    }

    @Transactional
    public void updateUser(User user) {
        // Won't be in separate transaction
    }
}

// 2. Private methods don't work
@Transactional  // IGNORED!
private void helper() {
    // Spring AOP can't intercept private methods
}

// 3. Not on interface implementations
// Place @Transactional on implementation, not interface
```

**Best practices:**
1. Place on service layer, not repository or controller
2. Use `readOnly = true` for queries (performance)
3. Keep transactions short
4. Don't call @Transactional methods from same class

---

## Summary

| Topic | Key Concepts |
|-------|--------------|
| **ORM** | Object-Relational Impedance Mismatch, JPA vs Hibernate |
| **Entity Lifecycle** | Transient, Managed, Detached, Removed |
| **Caching** | First-level cache (session), dirty checking |
| **Relationships** | @OneToMany, @ManyToOne, @ManyToMany, owning side |
| **Cascade** | CascadeType.ALL, orphanRemoval |
| **Spring Data JPA** | JpaRepository, derived queries, @Query |
| **Pagination** | Pageable, Page, PageRequest, Sort |
| **Pitfalls** | N+1, LazyInitializationException, equals/hashCode |

---

*Week 07 covers persistence fundamentals essential for building data-driven Spring applications.*
