# Week 07 - MCQ Answer Key

This document contains answers and explanations for all 80 questions in `mcq.md`.

---

## Answer Distribution

| Option | Count | Percentage |
|--------|-------|------------|
| A | 20 | 25% |
| B | 20 | 25% |
| C | 20 | 25% |
| D | 20 | 25% |

---

## ORM and Hibernate Fundamentals (Questions 1-30)

### Question 1
**Answer: A**

**ORM** stands for **Object Relational Mapping**. It's a technique for converting data between object-oriented programming languages and relational databases.

---

### Question 2
**Answer: B**

ORM solves the **Object-Relational Impedance Mismatch** - the difference between how data is represented in objects (inheritance, references) versus relational tables (rows, foreign keys).

---

### Question 3
**Answer: B**

**JPA is a specification** (set of interfaces and annotations), while **Hibernate is an implementation** of that specification. Other implementations include EclipseLink and OpenJPA.

---

### Question 4
**Answer: B**

EntityManager **manages entity lifecycle and persistence operations**. It handles persist, merge, remove, find, and query operations for entities.

---

### Question 5
**Answer: B**

The four entity states are **Transient, Managed, Detached, and Removed**. These represent an entity's relationship with the persistence context.

---

### Question 6
**Answer: B**

An entity is **Transient** when it's created with `new` but not yet associated with a persistence context. It has no database representation.

---

### Question 7
**Answer: C**

In **Managed** state, the entity is tracked by the persistence context. **Changes are automatically synchronized** with the database through dirty checking.

---

### Question 8
**Answer: B**

The first-level cache is a **session-scoped cache** that ensures the same entity instance is returned for multiple queries within a transaction, guaranteeing identity.

---

### Question 9
**Answer: B**

The first-level cache is **always enabled and cannot be disabled**. It's fundamental to how Hibernate manages entity identity within a session.

---

### Question 10
**Answer: C**

Dirty checking is Hibernate's mechanism for **automatically detecting and persisting entity changes**. It compares current state with the snapshot taken when the entity was loaded.

---

### Question 11
**Answer: A**

JPQL stands for **Java Persistence Query Language**. It's an object-oriented query language defined by JPA for querying entities.

---

### Question 12
**Answer: B**

JPQL **queries entities and their properties** (e.g., `SELECT u FROM User u`), while SQL queries **tables and columns** (e.g., `SELECT * FROM users`).

---

### Question 13
**Answer: B**

LazyInitializationException occurs when **accessing a lazy-loaded association after the session/persistence context is closed**. The proxy cannot load data without an active session.

---

### Question 14
**Answer: B**

**JOIN FETCH** in the query eagerly loads the association in the same query, avoiding LazyInitializationException by loading data before the session closes.

---

### Question 15
**Answer: B**

The N+1 problem is when Hibernate executes **1 query for the parent + N queries for each child**, instead of a single joined query. This causes severe performance issues.

---

### Question 16
**Answer: B**

To detect N+1, **enable SQL logging** (`spring.jpa.show-sql=true`) and count the queries. If you see repeated similar queries, you likely have N+1.

---

### Question 17
**Answer: B**

`@Entity` marks a class as a JPA entity that should be mapped to a database table. It's required for any class that needs to be persisted.

---

### Question 18
**Answer: B**

`@Id` annotation **marks a field as the primary key** of the entity. Every entity must have exactly one @Id field (or @EmbeddedId for composite keys).

---

### Question 19
**Answer: B**

`@GeneratedValue` **specifies how primary key values are generated**. It defines the strategy like IDENTITY, SEQUENCE, TABLE, or AUTO.

---

### Question 20
**Answer: B**

`GenerationType.IDENTITY` uses the **database's auto-increment column** feature. The database generates the ID when the row is inserted.

---

### Question 21
**Answer: B**

The default fetch type for `@OneToMany` is **LAZY**. This prevents loading all related entities until they're actually accessed.

---

### Question 22
**Answer: A**

The default fetch type for `@ManyToOne` is **EAGER**. Single entity references are loaded immediately since they're typically needed.

---

### Question 23
**Answer: B**

Using @Id for equals/hashCode is problematic because **ID is null before persist**, which breaks HashSet and HashMap behavior when entities change state.

---

### Question 24
**Answer: B**

A business key is a **naturally unique field like email or ISBN** that remains constant throughout the entity's lifecycle, making it safe for equals/hashCode.

---

### Question 25
**Answer: B**

`@Transactional` **manages transaction boundaries automatically**. It starts a transaction before the method and commits/rolls back after.

---

### Question 26
**Answer: B**

Without @Transactional, **dirty checking doesn't work** because there's no active persistence context tracking changes. Modifications may not be persisted.

---

### Question 27
**Answer: B**

Cascade **propagates operations from parent to associated entities**. For example, persisting a parent can automatically persist its children.

---

### Question 28
**Answer: B**

`CascadeType.ALL` includes **PERSIST, MERGE, REMOVE, REFRESH, and DETACH**. All operations on the parent cascade to associated entities.

---

### Question 29
**Answer: B**

`orphanRemoval = true` **deletes child entities when removed from parent's collection**. If a child is no longer referenced, it's deleted from the database.

---

### Question 30
**Answer: B**

`@EntityGraph` solves the **N+1 problem** by specifying which associations to fetch eagerly for a specific query, without changing the entity's default fetch type.

---

## Spring Data JPA (Questions 31-60)

### Question 31
**Answer: B**

Spring Data JPA's main benefit is **eliminating boilerplate repository code**. You define an interface, and Spring generates the implementation automatically.

---

### Question 32
**Answer: C**

**JpaRepository** provides full CRUD + pagination + sorting + JPA-specific operations like flush and batch deletes.

---

### Question 33
**Answer: B**

JpaRepository adds **flush, batch operations (saveAllAndFlush, deleteAllInBatch), and JPA-specific query methods** beyond CrudRepository's basic CRUD.

---

### Question 34
**Answer: B**

A derived query is where **Spring generates the query from the method name**. `findByEmail` automatically creates `WHERE email = ?`.

---

### Question 35
**Answer: B**

`findByEmailAndStatus` generates `SELECT u FROM User u WHERE u.email = ?1 AND u.status = ?2`. Spring parses the method name to build the query.

---

### Question 36
**Answer: B**

This method **finds by last name and orders results by first name ascending**. The `OrderBy` clause comes after the conditions.

---

### Question 37
**Answer: C**

`countByStatus` **returns the count** (Long) of entities matching the status. Count methods use `count` prefix.

---

### Question 38
**Answer: C**

`existsByEmail` returns a **boolean indicating if a record exists** with that email. It's more efficient than counting when you only need existence check.

---

### Question 39
**Answer: B**

`findByFieldIsNull()` finds records where the field **is null**. The `IsNull` keyword is used for null checks.

---

### Question 40
**Answer: B**

`findByAgeBetween` finds records where **age is between start and end (inclusive)**. The Between keyword handles range queries.

---

### Question 41
**Answer: B**

`@Query` is used to **define custom JPQL or native SQL queries** when derived query methods aren't sufficient.

---

### Question 42
**Answer: B**

For native SQL, use `@Query(value = "...", nativeQuery = true)`. The `nativeQuery = true` flag indicates raw SQL.

---

### Question 43
**Answer: B**

`?1` represents the **first method parameter** (positional parameter). `?2` would be the second, and so on.

---

### Question 44
**Answer: B**

Named parameters use `:name` syntax with `@Param("name")` on the method parameter: `@Query("... WHERE u.name = :name")`.

---

### Question 45
**Answer: B**

`@Modifying` indicates the query is an **UPDATE or DELETE that modifies data**, not a SELECT query.

---

### Question 46
**Answer: B**

Modifying queries **require a transaction context** because they change data. Without @Transactional, the update won't be committed.

---

### Question 47
**Answer: B**

`Page` interface represents a page of results with content, pagination info, and total counts.

---

### Question 48
**Answer: B**

Use `PageRequest.of(page, size)` to create a PageRequest. Page numbers are zero-based.

---

### Question 49
**Answer: B**

`getTotalElements()` returns the **total number of elements across all pages**, not just the current page.

---

### Question 50
**Answer: C**

`getTotalPages()` returns the **total number of pages** based on total elements divided by page size.

---

### Question 51
**Answer: B**

Use `Sort.by(Sort.Direction.DESC, "fieldName")` to create a descending sort.

---

### Question 52
**Answer: B**

Combine sorting with pagination using `PageRequest.of(page, size, sort)`. This creates a pageable with both.

---

### Question 53
**Answer: A**

A projection is a way to **select specific fields instead of entire entities**, reducing data transfer and improving performance.

---

### Question 54
**Answer: A**

An interface-based projection is an **interface that defines getter methods** for desired fields. Spring creates a proxy implementing it.

---

### Question 55
**Answer: B**

A class-based projection (DTO) is a **class with constructor matching the selected fields**. JPQL uses `new DTO(...)` syntax.

---

### Question 56
**Answer: B**

A dynamic projection is a **repository method that accepts the projection type as a parameter**, allowing different projections from the same method.

---

### Question 57
**Answer: B**

`@CreatedDate` **automatically sets the creation timestamp** when the entity is first persisted.

---

### Question 58
**Answer: B**

`@EnableJpaAuditing` on a configuration class enables JPA auditing features like @CreatedDate and @LastModifiedDate.

---

### Question 59
**Answer: B**

`@LastModifiedDate` **automatically updates timestamp** whenever the entity is modified.

---

### Question 60
**Answer: B**

`deleteAllInBatch()` **executes a single DELETE query** which is more efficient than `deleteAll()` which loads and deletes entities one by one.

---

## Entity Mapping and Relationships (Questions 61-80)

### Question 61
**Answer: B**

`@Column(nullable = false)` creates a **database NOT NULL constraint**. It's enforced at the database level during schema generation.

---

### Question 62
**Answer: B**

`@Column(unique = true)` creates a **unique constraint in the database**, preventing duplicate values in that column.

---

### Question 63
**Answer: B**

`@Table(name = "users")` **specifies the database table name** for the entity when it differs from the class name.

---

### Question 64
**Answer: B**

`@OneToMany` represents **one entity related to many** other entities, like one Author to many Books.

---

### Question 65
**Answer: B**

In bidirectional relationships, the **@ManyToOne side** (the "many" side) owns the relationship because it contains the foreign key.

---

### Question 66
**Answer: A**

`mappedBy` indicates **the field name on the owning side** that maps this relationship. It's used on the non-owning (inverse) side.

---

### Question 67
**Answer: B**

`@JoinColumn` specifies the **foreign key column** in the owning entity's table that references the related entity.

---

### Question 68
**Answer: B**

`@ManyToMany` means **many entities on both sides**, requiring a join table to store the relationships.

---

### Question 69
**Answer: B**

`@JoinTable` specifies the **intermediate join table** with foreign keys to both sides of a ManyToMany relationship.

---

### Question 70
**Answer: B**

`@Enumerated(EnumType.STRING)` **stores the enum name as a string** in the database, like "ACTIVE" instead of 0.

---

### Question 71
**Answer: B**

STRING is preferred because it **survives enum reordering**. If you add/remove enum values, ORDINAL values would become incorrect.

---

### Question 72
**Answer: B**

`@Temporal` specifies **how to map java.util.Date** to the database: DATE (date only), TIME (time only), or TIMESTAMP (both).

---

### Question 73
**Answer: B**

`@Embedded` **embeds a value object's fields** directly into the entity's table, rather than creating a separate table.

---

### Question 74
**Answer: B**

`@Embeddable` marks a class that can be embedded into entities. Its fields are mapped to the owning entity's table.

---

### Question 75
**Answer: B**

SINGLE_TABLE means **all classes share one table** with a discriminator column to identify the type.

---

### Question 76
**Answer: B**

JOINED strategy means **each class has a table**, and queries join them by primary key to retrieve the complete entity.

---

### Question 77
**Answer: B**

`@Lob` indicates a **Large Object field** (BLOB for binary, CLOB for text), used for large data like files or long text.

---

### Question 78
**Answer: B**

Bean Validation annotations come from `jakarta.validation.constraints` package (formerly javax.validation.constraints).

---

### Question 79
**Answer: B**

`@Version` enables **optimistic locking** to prevent concurrent update conflicts. Hibernate checks the version before updating.

---

### Question 80
**Answer: B**

`@Transient` **excludes a field from persistence**. The field is not mapped to any database column.

---

## Summary

| Topic | Questions | Key Concepts |
|-------|-----------|--------------|
| ORM Fundamentals | 1-30 | Entity states, first-level cache, dirty checking, JPQL, N+1 |
| Spring Data JPA | 31-60 | Repositories, derived queries, @Query, pagination, projections |
| Entity Mapping | 61-80 | Relationships, @Column, @JoinColumn, inheritance, @Version |

---

*For detailed explanations and code examples, refer to the module documentation in `17-spring-data-jpa/`.*
