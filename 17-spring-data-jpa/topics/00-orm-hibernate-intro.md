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

## First-Level Cache (Session Cache)

Hibernate automatically caches entities within a session/transaction. This is the **first-level cache** - it's always on and cannot be disabled.

```java
// Within the same transaction
User user1 = entityManager.find(User.class, 1L);  // SQL SELECT executed
User user2 = entityManager.find(User.class, 1L);  // NO SQL - returns cached object

System.out.println(user1 == user2);  // true - same object instance!
```

### How It Works

```
┌─────────────────────────────────────────────────────────────┐
│                     EntityManager                           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │            First-Level Cache (Session)                │  │
│  │                                                       │  │
│  │   Key: (User.class, 1L)  →  Value: User@abc123       │  │
│  │   Key: (User.class, 2L)  →  Value: User@def456       │  │
│  │   Key: (Order.class, 5L) →  Value: Order@ghi789      │  │
│  │                                                       │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
              find(User.class, 1L) - Cache HIT (no SQL)
              find(User.class, 3L) - Cache MISS (SQL executed)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        Database                             │
└─────────────────────────────────────────────────────────────┘
```

### Why First-Level Cache Matters

| Benefit | Description |
|---------|-------------|
| **Performance** | Avoids redundant database queries |
| **Identity guarantee** | Same ID always returns same object instance |
| **Dirty checking** | Hibernate compares cached vs current state |
| **Write-behind** | Changes batched until transaction commit |

```java
// Dirty checking in action
User user = entityManager.find(User.class, 1L);  // Cached with original state
user.setEmail("new@email.com");                   // Object modified

// At commit, Hibernate compares:
// - Cached original state: email = "old@email.com"
// - Current state: email = "new@email.com"
// → Generates UPDATE statement automatically
```

### Cache Scope

The first-level cache is **transaction-scoped**:

```java
// Transaction 1
User user1 = em.find(User.class, 1L);  // Cached in Transaction 1

// Transaction 2 (different EntityManager)
User user2 = em.find(User.class, 1L);  // Cache MISS - new query

System.out.println(user1 == user2);  // false - different transactions
```

> **Note:** Second-level cache (shared across transactions) is an advanced topic requiring additional configuration (EhCache, Infinispan). It's optional and used for performance optimization.

---

## Entity equals() and hashCode()

Hibernate relies on `equals()` and `hashCode()` for collection management, cache lookups, and dirty checking. Getting these wrong causes subtle bugs.

### The Problem

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    // No equals/hashCode overridden - uses Object defaults
}

// This breaks!
Set<User> users = new HashSet<>();
User user = new User();
user.setEmail("john@email.com");
users.add(user);                    // Added with id = null

entityManager.persist(user);         // Now id = 1

users.contains(user);               // FALSE! HashCode changed after persist
```

### Why It Breaks

```
Before persist():
┌─────────────┐
│ User        │
│ id = null   │  hashCode() based on id → some value X
│ email = ... │
└─────────────┘
     ↓ persist()
┌─────────────┐
│ User        │
│ id = 1      │  hashCode() based on id → different value Y!
│ email = ... │
└─────────────┘

HashSet can't find the object because it's looking in bucket X,
but the object's hashCode now points to bucket Y.
```

### The Solution: Use Business Key

Use a **natural/business key** that doesn't change, not the database ID:

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;  // Business key - unique and immutable

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
}
```

### Rules for Entity equals/hashCode

| Rule | Reason |
|------|--------|
| **Don't use @Id alone** | ID is null before persist, changes after |
| **Use business key** | Unique field that's set at creation (email, ISBN, SSN) |
| **Be consistent** | Same fields in both equals() and hashCode() |
| **Handle null** | Check for null values |
| **Use getClass()** | Hibernate proxies extend your class |

### When No Natural Key Exists

If there's no natural business key, use a UUID assigned at object creation:

```java
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String uuid = UUID.randomUUID().toString();  // Set at creation

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return uuid.equals(order.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
```

### Impact on Collections

```java
// With proper equals/hashCode
Set<User> users = new HashSet<>();
User user = new User("john@email.com");
users.add(user);

entityManager.persist(user);  // id changes, but email stays same

users.contains(user);  // TRUE - hashCode based on email, unchanged
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

## Common Pitfalls

These are the most common issues beginners encounter. Understanding them now saves hours of debugging later.

### 1. LazyInitializationException

**The #1 Hibernate error for beginners.**

```java
@Entity
public class User {
    @Id
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;  // LAZY by default
}
```

```java
// In service layer (transaction active)
User user = userRepository.findById(1L).get();
// Transaction ends here...

// In controller or view (no transaction)
user.getOrders().size();  // BOOM! LazyInitializationException
```

**Why it happens:**

```
┌─────────────────────────────────────────────────────────────┐
│  @Transactional Service Method                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ User user = repo.findById(1L);                         │ │
│  │ // orders NOT loaded yet (lazy)                        │ │
│  │ return user;                                           │ │
│  └────────────────────────────────────────────────────────┘ │
│  Transaction COMMITS, EntityManager CLOSES                  │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  Controller (no transaction)                                │
│                                                             │
│  user.getOrders()  →  Hibernate tries to fetch...          │
│                    →  But session is CLOSED!                │
│                    →  LazyInitializationException!          │
└─────────────────────────────────────────────────────────────┘
```

**Quick fixes:**

| Approach | How |
|----------|-----|
| **Fetch eagerly** | `@OneToMany(fetch = FetchType.EAGER)` - use sparingly! |
| **JOIN FETCH** | `SELECT u FROM User u JOIN FETCH u.orders` |
| **DTO projection** | Return only needed fields, not entities |
| **Initialize before returning** | `user.getOrders().size()` inside transaction |

> **Rule of thumb:** Lazy is the default for collections (`@OneToMany`, `@ManyToMany`). Eager is the default for single associations (`@ManyToOne`, `@OneToOne`).

### 2. N+1 Query Problem

**Silent performance killer** - your app works but makes way too many queries.

```java
// Fetch all users
List<User> users = userRepository.findAll();  // 1 query

// Access each user's orders
for (User user : users) {
    System.out.println(user.getOrders().size());  // N queries (one per user!)
}
// Total: 1 + N queries = N+1 problem
```

**What you expect:**
```sql
SELECT * FROM users;  -- 1 query for everything
```

**What actually happens:**
```sql
SELECT * FROM users;                      -- 1 query
SELECT * FROM orders WHERE user_id = 1;   -- query 2
SELECT * FROM orders WHERE user_id = 2;   -- query 3
SELECT * FROM orders WHERE user_id = 3;   -- query 4
-- ... N more queries
```

**With 1000 users = 1001 database queries!**

**Solutions:**

```java
// Solution 1: JOIN FETCH in JPQL
@Query("SELECT u FROM User u JOIN FETCH u.orders")
List<User> findAllWithOrders();  // 1 query!

// Solution 2: @EntityGraph
@EntityGraph(attributePaths = {"orders"})
List<User> findAll();  // 1 query!

// Solution 3: Batch fetching (hibernate setting)
// application.properties
spring.jpa.properties.hibernate.default_batch_fetch_size=25
```

**How to detect:**

```properties
# Enable SQL logging
spring.jpa.show-sql=true

# Pretty print (easier to count queries)
spring.jpa.properties.hibernate.format_sql=true
```

Watch your console - if you see repeated similar queries, you have N+1.

### 3. Forgetting @Transactional

Changes to managed entities auto-sync **only within a transaction**.

```java
// WITHOUT @Transactional - changes may NOT persist!
public void updateEmail(Long id, String newEmail) {
    User user = userRepository.findById(id).get();
    user.setEmail(newEmail);
    // No explicit save... did it persist? Maybe, maybe not!
}

// WITH @Transactional - changes auto-persist at commit
@Transactional
public void updateEmail(Long id, String newEmail) {
    User user = userRepository.findById(id).get();
    user.setEmail(newEmail);
    // Dirty checking detects change → UPDATE at commit
}
```

> **Tip:** Spring Data JPA repository methods are transactional by default. Your service methods that modify entities should be `@Transactional`.

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
| **First-Level Cache** | Session-scoped cache, always on, ensures identity |
| **equals/hashCode** | Use business key, not @Id, for collections |
| **JPQL** | Object-oriented query language |
| **LazyInitializationException** | Accessing lazy data outside transaction |
| **N+1 Problem** | 1 query + N queries for related data |

---

## What's Next

Now that you understand ORM fundamentals, you're ready to see how **Spring Data JPA** eliminates the remaining boilerplate:

- No manual `EntityManager` handling
- No transaction management code
- Repositories auto-implemented from interfaces
- Queries generated from method names

Continue to [JPA Introduction](./01-jpa-introduction.md) to see Spring Data JPA in action.
