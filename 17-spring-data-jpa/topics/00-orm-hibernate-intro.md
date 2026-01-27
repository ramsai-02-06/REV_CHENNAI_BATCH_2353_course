# ORM and Hibernate Introduction

## The Problem: Object-Relational Impedance Mismatch

When you've worked with JDBC, you've experienced this pain:

```java
// JDBC: Manual mapping everywhere
ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = ?");
if (rs.next()) {
    User user = new User();
    user.setId(rs.getLong("id"));
    user.setUsername(rs.getString("username"));
    user.setEmail(rs.getString("email"));
    // What about relationships? More queries...
}
```

**The fundamental mismatch:**

| Java Objects | Relational Tables |
|--------------|-------------------|
| Inheritance (`extends`) | No direct equivalent |
| References (`user.getAddress()`) | Foreign keys (joins) |
| Collections (`List<Order>`) | Separate table + FK |
| Identity (`==` vs `.equals()`) | Primary key |
| Navigation (`order.getCustomer().getName()`) | Requires JOIN |

This mismatch means you spend more time writing SQL and mapping code than actual business logic.

---

## What is ORM?

**Object-Relational Mapping (ORM)** is a technique that lets you work with database records as Java objects.

```
┌─────────────────────────────────────────────────────────┐
│                    Your Application                     │
│                                                         │
│   User user = new User("john", "john@email.com");      │
│   entityManager.persist(user);  // INSERT happens      │
│   user.setEmail("new@email.com");                      │
│   // UPDATE happens automatically!                      │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    ORM Framework                        │
│         (Hibernate, EclipseLink, OpenJPA)              │
│                                                         │
│   - Generates SQL from your objects                     │
│   - Maps ResultSets to objects                          │
│   - Tracks changes and syncs to database                │
│   - Manages relationships and lazy loading              │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                      Database                           │
│                                                         │
│   INSERT INTO users (username, email) VALUES (?, ?)     │
│   UPDATE users SET email = ? WHERE id = ?               │
└─────────────────────────────────────────────────────────┘
```

### ORM Benefits

| Benefit | Description |
|---------|-------------|
| **Less boilerplate** | No manual ResultSet mapping |
| **Database portable** | Switch MySQL → PostgreSQL with config change |
| **Object-oriented** | Work with objects, not SQL strings |
| **Automatic SQL** | Framework generates optimized queries |
| **Change tracking** | Modified objects sync to database |

---

## JPA vs Hibernate

**JPA (Java Persistence API)** is a specification - it defines interfaces and annotations but no implementation.

**Hibernate** is an implementation of JPA (the most popular one).

```
┌─────────────────────────────────────┐
│          JPA Specification          │  ← Standard API
│  (javax.persistence / jakarta.persistence)
├──────────┬──────────┬───────────────┤
│ Hibernate│EclipseLink│   OpenJPA    │  ← Implementations
└──────────┴──────────┴───────────────┘
```

**Analogy:** JPA is like JDBC (specification), Hibernate is like MySQL Connector (implementation).

In Spring Boot, Hibernate is the default JPA provider - you use JPA annotations and Hibernate does the work.

---

## Core Concepts

### Entity

An entity is a Java class mapped to a database table:

```java
import jakarta.persistence.*;

@Entity                          // Marks as JPA entity
@Table(name = "users")           // Optional: specify table name
public class User {

    @Id                          // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")  // Optional: specify column name
    private String username;

    private String email;        // Maps to "email" column by default

    // Default constructor required by JPA
    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Getters and setters...
}
```

### EntityManager

The `EntityManager` is your gateway to the database:

```java
@PersistenceContext
private EntityManager entityManager;

// Create
User user = new User("john", "john@email.com");
entityManager.persist(user);  // INSERT

// Read
User found = entityManager.find(User.class, 1L);  // SELECT by PK

// Update (automatic!)
found.setEmail("new@email.com");
// No explicit call needed - Hibernate tracks changes

// Delete
entityManager.remove(found);  // DELETE
```

---

## Entity Lifecycle States

Understanding entity states is crucial for working with Hibernate:

```
    ┌─────────────────────────────────────────────────────────┐
    │                                                         │
    │   new User()          persist()         Database        │
    │       │                  │                  │           │
    │       ▼                  ▼                  ▼           │
    │   ┌────────┐         ┌────────┐         ┌────────┐     │
    │   │TRANSIENT│───────▶│MANAGED │◀───────▶│DATABASE│     │
    │   └────────┘         └────────┘         └────────┘     │
    │                          │                              │
    │                    detach() / close()                   │
    │                          │                              │
    │                          ▼                              │
    │                     ┌────────┐                          │
    │                     │DETACHED│                          │
    │                     └────────┘                          │
    │                          │                              │
    │                      merge()                            │
    │                          │                              │
    │                          ▼                              │
    │                     ┌────────┐                          │
    │                     │MANAGED │  (again)                 │
    │                     └────────┘                          │
    │                                                         │
    └─────────────────────────────────────────────────────────┘
```

| State | Description | Example |
|-------|-------------|---------|
| **Transient** | New object, not tracked by Hibernate | `new User()` |
| **Managed** | Tracked by Hibernate, changes auto-sync | After `persist()` or `find()` |
| **Detached** | Was managed, now disconnected | After `close()` or `detach()` |
| **Removed** | Scheduled for deletion | After `remove()` |

### Why This Matters

```java
// Managed entity - changes are tracked
User user = entityManager.find(User.class, 1L);  // MANAGED
user.setEmail("new@email.com");
// No save() call needed! Hibernate detects the change
// and generates UPDATE at transaction commit

// Detached entity - changes are NOT tracked
entityManager.detach(user);  // DETACHED
user.setEmail("another@email.com");
// This change is LOST unless you call merge()

entityManager.merge(user);  // Re-attach and sync changes
```

---

## Simple Hibernate Example (Without Spring)

To appreciate Spring Data JPA, see what pure Hibernate looks like:

```java
// Configuration (hibernate.cfg.xml or programmatic)
EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit");
EntityManager em = emf.createEntityManager();

try {
    // Start transaction
    em.getTransaction().begin();

    // Create
    User user = new User("john", "john@email.com");
    em.persist(user);

    // Read
    User found = em.find(User.class, user.getId());

    // Update
    found.setEmail("updated@email.com");

    // Query
    List<User> users = em.createQuery(
        "SELECT u FROM User u WHERE u.email LIKE :domain", User.class)
        .setParameter("domain", "%@email.com")
        .getResultList();

    // Commit transaction
    em.getTransaction().commit();

} catch (Exception e) {
    em.getTransaction().rollback();
    throw e;
} finally {
    em.close();
    emf.close();
}
```

**Problems with this approach:**
- Manual transaction management
- Resource cleanup (`close()` everywhere)
- Repetitive DAO code for every entity
- No automatic repository implementation

**Spring Data JPA solves all of this!**

---

## JPQL: Java Persistence Query Language

JPQL is SQL for entities - you query objects, not tables:

```java
// SQL (tables and columns)
SELECT * FROM users WHERE email LIKE '%@gmail.com'

// JPQL (entities and fields)
SELECT u FROM User u WHERE u.email LIKE '%@gmail.com'
```

| Feature | SQL | JPQL |
|---------|-----|------|
| Query target | Tables | Entity classes |
| Field names | Column names | Java field names |
| Case sensitive | Usually no | Class/field names: yes |
| Joins | Explicit | Can use navigation |

```java
// JPQL with relationship navigation
SELECT o FROM Order o WHERE o.customer.email = :email

// Equivalent SQL requires explicit JOIN
SELECT o.* FROM orders o
JOIN customers c ON o.customer_id = c.id
WHERE c.email = ?
```

---

## Summary

| Concept | Description |
|---------|-------------|
| **Impedance Mismatch** | Objects and tables don't naturally align |
| **ORM** | Maps objects to tables automatically |
| **JPA** | Standard specification for Java ORM |
| **Hibernate** | Most popular JPA implementation |
| **Entity** | Java class mapped to database table |
| **EntityManager** | API for CRUD operations |
| **Entity States** | Transient → Managed → Detached |
| **JPQL** | Object-oriented query language |

---

## What's Next

Now that you understand ORM fundamentals, you're ready to see how **Spring Data JPA** eliminates the remaining boilerplate:

- No manual `EntityManager` handling
- No transaction management code
- Repositories auto-implemented from interfaces
- Queries generated from method names

Continue to [JPA Introduction](./01-jpa-introduction.md) to see Spring Data JPA in action.
