# Week 07 - Multiple Choice Questions

This document contains 80 multiple choice questions covering the key concepts from Week 07 topics: ORM/Hibernate Fundamentals and Spring Data JPA.

**Topic Distribution:**
- ORM and Hibernate Fundamentals: 30 questions
- Spring Data JPA (Repositories, Queries): 30 questions
- Entity Mapping and Relationships: 20 questions

---

**Note:** Answers and explanations are in `mcq-answers.md`

---

## ORM and Hibernate Fundamentals

### Question 1
**[ORM Basics]**

What does ORM stand for?

- A) Object Relational Mapping
- B) Object Resource Management
- C) Operational Record Mapping
- D) Object Runtime Module

---

### Question 2
**[ORM Basics]**

What problem does ORM solve?

- A) Network latency issues
- B) Object-Relational Impedance Mismatch
- C) Memory management
- D) Code compilation

---

### Question 3
**[ORM Basics]**

Which statement about JPA is correct?

- A) JPA is an implementation of Hibernate
- B) JPA is a specification, Hibernate is an implementation
- C) JPA and Hibernate are the same thing
- D) JPA replaced Hibernate completely

---

### Question 4
**[Hibernate]**

What is the primary function of EntityManager?

- A) Manage database connections only
- B) Manage entity lifecycle and persistence operations
- C) Generate SQL queries only
- D) Handle HTTP requests

---

### Question 5
**[Entity Lifecycle]**

What are the four entity states in JPA?

- A) New, Active, Inactive, Deleted
- B) Transient, Managed, Detached, Removed
- C) Created, Updated, Saved, Archived
- D) Open, Closed, Pending, Complete

---

### Question 6
**[Entity Lifecycle]**

When is an entity in the "Transient" state?

- A) After being retrieved from database
- B) When created with new but not yet persisted
- C) After the transaction commits
- D) When the EntityManager is closed

---

### Question 7
**[Entity Lifecycle]**

What happens when an entity is in the "Managed" state?

- A) Changes are ignored by Hibernate
- B) The entity cannot be modified
- C) Changes are automatically synchronized with the database
- D) The entity is deleted from the database

---

### Question 8
**[Hibernate]**

What is the first-level cache in Hibernate?

- A) A distributed cache shared across applications
- B) A session-scoped cache that ensures identity within a transaction
- C) A database query cache
- D) A file-based cache on disk

---

### Question 9
**[Hibernate]**

Which is true about the first-level cache?

- A) It must be explicitly enabled
- B) It is always enabled and cannot be disabled
- C) It only works with read operations
- D) It is shared across all sessions

---

### Question 10
**[Hibernate]**

What is dirty checking in Hibernate?

- A) Validating entity field values
- B) Checking for SQL injection
- C) Automatically detecting and persisting entity changes
- D) Verifying database connections

---

### Question 11
**[JPQL]**

What does JPQL stand for?

- A) Java Persistence Query Language
- B) Java Programming Query Library
- C) JPA Query Logic
- D) Java Protocol Query Language

---

### Question 12
**[JPQL]**

How does JPQL differ from SQL?

- A) JPQL is faster than SQL
- B) JPQL queries entities and properties, SQL queries tables and columns
- C) JPQL only supports SELECT operations
- D) JPQL doesn't support WHERE clauses

---

### Question 13
**[Hibernate]**

What causes LazyInitializationException?

- A) Using eager fetching
- B) Accessing a lazy-loaded association after the session is closed
- C) Too many database connections
- D) Invalid JPQL syntax

---

### Question 14
**[Hibernate]**

Which is a solution for LazyInitializationException?

- A) Use FetchType.LAZY
- B) Use JOIN FETCH in the query
- C) Remove all associations
- D) Disable transactions

---

### Question 15
**[Hibernate]**

What is the N+1 query problem?

- A) Having more than N queries in a transaction
- B) 1 query for parent + N queries for each child, instead of a single join
- C) Queries taking more than N+1 seconds
- D) Having N+1 database connections

---

### Question 16
**[Hibernate]**

How do you detect the N+1 problem?

- A) Check application memory usage
- B) Enable SQL logging and count the queries
- C) Monitor CPU utilization
- D) Check network latency

---

### Question 17
**[Hibernate]**

Which annotation marks a class as a JPA entity?

- A) @Table
- B) @Entity
- C) @Persistent
- D) @Model

---

### Question 18
**[Hibernate]**

What is the purpose of @Id annotation?

- A) Generate unique identifiers automatically
- B) Mark a field as the primary key
- C) Create a database index
- D) Define a foreign key

---

### Question 19
**[Hibernate]**

What does @GeneratedValue do?

- A) Validates the ID value
- B) Specifies how primary key values are generated
- C) Creates a unique constraint
- D) Generates entity instances

---

### Question 20
**[Hibernate]**

What is GenerationType.IDENTITY?

- A) Uses a database sequence
- B) Uses database auto-increment column
- C) Uses a UUID generator
- D) Uses application-provided values

---

### Question 21
**[Hibernate]**

What is the default fetch type for @OneToMany?

- A) EAGER
- B) LAZY
- C) NONE
- D) AUTO

---

### Question 22
**[Hibernate]**

What is the default fetch type for @ManyToOne?

- A) EAGER
- B) LAZY
- C) NONE
- D) AUTO

---

### Question 23
**[Hibernate]**

Why is using @Id for equals/hashCode problematic?

- A) It's too slow
- B) ID is null before persist, breaking Set/Map behavior
- C) @Id cannot be used in methods
- D) It causes compilation errors

---

### Question 24
**[Hibernate]**

What is a business key for equals/hashCode?

- A) The database primary key
- B) A naturally unique field like email or ISBN
- C) A random UUID
- D) The entity class name

---

### Question 25
**[Transactions]**

What does @Transactional annotation do?

- A) Logs all database operations
- B) Manages transaction boundaries automatically
- C) Validates entity data
- D) Encrypts database connections

---

### Question 26
**[Transactions]**

What happens if @Transactional is missing on a service method that modifies entities?

- A) Changes are always saved
- B) Dirty checking doesn't work, changes may not persist
- C) The application crashes
- D) Queries return null

---

### Question 27
**[Hibernate]**

What is cascade in JPA?

- A) Deleting all data in a table
- B) Propagating operations from parent to associated entities
- C) Ordering query results
- D) Joining multiple tables

---

### Question 28
**[Hibernate]**

What does CascadeType.ALL include?

- A) Only PERSIST and MERGE
- B) PERSIST, MERGE, REMOVE, REFRESH, DETACH
- C) Only DELETE operations
- D) Only SELECT operations

---

### Question 29
**[Hibernate]**

What is orphanRemoval = true?

- A) Removes orphan database records on startup
- B) Deletes child entities when removed from parent's collection
- C) Clears the cache periodically
- D) Removes unused database tables

---

### Question 30
**[Hibernate]**

What does @EntityGraph solve?

- A) Entity validation errors
- B) N+1 query problem by specifying which associations to fetch eagerly
- C) Database connection issues
- D) Primary key generation

---

## Spring Data JPA

### Question 31
**[Spring Data JPA]**

What is the main benefit of Spring Data JPA?

- A) Faster database connections
- B) Eliminates boilerplate repository code
- C) Automatic UI generation
- D) Built-in security

---

### Question 32
**[Spring Data JPA]**

Which interface should repositories extend for full CRUD + pagination?

- A) CrudRepository
- B) Repository
- C) JpaRepository
- D) PagingRepository

---

### Question 33
**[Spring Data JPA]**

What does JpaRepository provide that CrudRepository doesn't?

- A) Save and delete methods
- B) Flush, batch operations, and JPA-specific queries
- C) FindById method
- D) Transaction support

---

### Question 34
**[Derived Queries]**

What is a derived query method in Spring Data JPA?

- A) A method that uses raw SQL
- B) A method where Spring generates the query from the method name
- C) A method that calls stored procedures
- D) A method that returns derived types

---

### Question 35
**[Derived Queries]**

What query does `findByEmailAndStatus(String email, Status status)` generate?

- A) SELECT * FROM user
- B) SELECT u FROM User u WHERE u.email = ?1 AND u.status = ?2
- C) SELECT email, status FROM user
- D) DELETE FROM user WHERE email = ?

---

### Question 36
**[Derived Queries]**

What does `findByLastNameOrderByFirstNameAsc` do?

- A) Finds by first name, orders by last name
- B) Finds by last name, orders by first name ascending
- C) Finds all records in ascending order
- D) Throws an exception

---

### Question 37
**[Derived Queries]**

What does `countByStatus(Status status)` return?

- A) A list of entities
- B) A single entity
- C) The count of entities with that status
- D) A boolean

---

### Question 38
**[Derived Queries]**

What does `existsByEmail(String email)` return?

- A) The entity with that email
- B) A count of matching records
- C) Boolean indicating if a record exists
- D) The email string

---

### Question 39
**[Derived Queries]**

Which keyword finds records where a field is null?

- A) findByFieldEquals(null)
- B) findByFieldIsNull()
- C) findByFieldNull()
- D) findByFieldNullValue()

---

### Question 40
**[Derived Queries]**

What does `findByAgeBetween(int start, int end)` do?

- A) Finds age equal to start or end
- B) Finds records where age is between start and end (inclusive)
- C) Finds records outside the range
- D) Throws IllegalArgumentException

---

### Question 41
**[@Query Annotation]**

What is the @Query annotation used for?

- A) To disable query generation
- B) To define custom JPQL or native SQL queries
- C) To log all queries
- D) To validate query parameters

---

### Question 42
**[@Query Annotation]**

How do you write a native SQL query with @Query?

- A) @Query(value = "...", native = true)
- B) @Query(value = "...", nativeQuery = true)
- C) @NativeQuery("...")
- D) @SQL("...")

---

### Question 43
**[@Query Annotation]**

What does `?1` represent in a @Query?

- A) A literal value 1
- B) The first method parameter (positional parameter)
- C) The primary key
- D) A pagination parameter

---

### Question 44
**[@Query Annotation]**

How do you use named parameters in @Query?

- A) @Query("... WHERE u.name = ?name")
- B) @Query("... WHERE u.name = :name") with @Param("name")
- C) @Query("... WHERE u.name = #{name}")
- D) @Query("... WHERE u.name = $name")

---

### Question 45
**[@Query Annotation]**

What does @Modifying annotation indicate?

- A) The query returns modified entities
- B) The query is an UPDATE or DELETE that modifies data
- C) The query can be modified at runtime
- D) The query result is modifiable

---

### Question 46
**[@Query Annotation]**

Why is @Transactional often needed with @Modifying?

- A) For logging purposes
- B) Because modifying queries require a transaction context
- C) To improve performance
- D) To enable caching

---

### Question 47
**[Pagination]**

Which interface represents a page of results?

- A) PageResult
- B) Page
- C) PagedList
- D) ResultPage

---

### Question 48
**[Pagination]**

How do you create a PageRequest?

- A) new PageRequest(page, size)
- B) PageRequest.of(page, size)
- C) Page.request(page, size)
- D) Pageable.create(page, size)

---

### Question 49
**[Pagination]**

What does Page.getTotalElements() return?

- A) Number of elements in current page
- B) Total number of elements across all pages
- C) Page size
- D) Number of pages

---

### Question 50
**[Pagination]**

What does Page.getTotalPages() return?

- A) Current page number
- B) Number of elements per page
- C) Total number of pages
- D) Total elements

---

### Question 51
**[Sorting]**

How do you create a Sort object for descending order?

- A) Sort.desc("fieldName")
- B) Sort.by(Sort.Direction.DESC, "fieldName")
- C) new Sort("fieldName", DESC)
- D) Sort.descending("fieldName")

---

### Question 52
**[Sorting]**

How do you combine sorting with pagination?

- A) PageRequest.of(page, size).sort(sort)
- B) PageRequest.of(page, size, sort)
- C) new PageSort(page, size, sort)
- D) Pageable.sorted(page, size, sort)

---

### Question 53
**[Projections]**

What is a projection in Spring Data JPA?

- A) A way to select specific fields instead of entire entities
- B) A method to project data to different databases
- C) A way to display data on screen
- D) A data validation technique

---

### Question 54
**[Projections]**

What is an interface-based projection?

- A) An interface that defines getter methods for desired fields
- B) An interface that extends JpaRepository
- C) An interface for database connections
- D) An interface for entity validation

---

### Question 55
**[Projections]**

What is a class-based projection (DTO projection)?

- A) A class that extends the entity
- B) A class with constructor matching the selected fields
- C) A class that implements Repository
- D) A class for database configuration

---

### Question 56
**[Projections]**

What is a dynamic projection?

- A) A projection that changes at runtime
- B) A repository method that accepts the projection type as a parameter
- C) A projection with dynamic field names
- D) A projection that auto-updates

---

### Question 57
**[Auditing]**

What does @CreatedDate annotation do?

- A) Validates date format
- B) Automatically sets the creation timestamp
- C) Creates a date column
- D) Parses date strings

---

### Question 58
**[Auditing]**

What annotation enables JPA auditing in Spring Boot?

- A) @EnableAuditing
- B) @EnableJpaAuditing
- C) @AuditingEnabled
- D) @JpaAudit

---

### Question 59
**[Auditing]**

What does @LastModifiedDate do?

- A) Retrieves the last modification date
- B) Automatically updates timestamp on entity modification
- C) Sets a deadline date
- D) Logs modification dates

---

### Question 60
**[Spring Data JPA]**

What does `deleteAllInBatch()` do differently than `deleteAll()`?

- A) Deletes one by one with events
- B) Executes a single DELETE query (more efficient)
- C) Only marks as deleted
- D) Deletes from cache only

---

## Entity Mapping and Relationships

### Question 61
**[Entity Mapping]**

What does @Column(nullable = false) enforce?

- A) Java null check only
- B) Database NOT NULL constraint
- C) Both Java and database null check
- D) No enforcement

---

### Question 62
**[Entity Mapping]**

What does @Column(unique = true) create?

- A) A primary key
- B) A unique constraint in the database
- C) A unique index only
- D) A foreign key

---

### Question 63
**[Entity Mapping]**

What is @Table(name = "users") used for?

- A) Creating a users variable
- B) Specifying the database table name for the entity
- C) Joining multiple tables
- D) Defining table relationships

---

### Question 64
**[Relationships]**

What does @OneToMany represent?

- A) One entity related to one other entity
- B) One entity related to many other entities
- C) Many entities related to one entity
- D) Many entities related to many entities

---

### Question 65
**[Relationships]**

In a bidirectional @OneToMany, which side owns the relationship?

- A) The @OneToMany side
- B) The @ManyToOne side (the "many" side)
- C) Both sides equally
- D) Neither side

---

### Question 66
**[Relationships]**

What does mappedBy attribute indicate?

- A) The field name on the owning side that maps this relationship
- B) The database column name
- C) The table name
- D) The primary key field

---

### Question 67
**[Relationships]**

What does @JoinColumn specify?

- A) The column used for joining in queries only
- B) The foreign key column in the owning entity's table
- C) A composite key
- D) An index column

---

### Question 68
**[Relationships]**

What is a @ManyToMany relationship?

- A) One entity to one entity
- B) Many entities on both sides, requiring a join table
- C) Many entities to one entity
- D) Self-referencing relationship

---

### Question 69
**[Relationships]**

What does @JoinTable specify in @ManyToMany?

- A) The entity table
- B) The intermediate join table with foreign keys to both sides
- C) A temporary table
- D) A view

---

### Question 70
**[Entity Mapping]**

What does @Enumerated(EnumType.STRING) do?

- A) Stores enum ordinal as integer
- B) Stores enum name as string in database
- C) Converts string to enum
- D) Validates enum values

---

### Question 71
**[Entity Mapping]**

Why is EnumType.STRING preferred over EnumType.ORDINAL?

- A) It's faster
- B) It survives enum reordering without data corruption
- C) It uses less storage
- D) It's required by JPA specification

---

### Question 72
**[Entity Mapping]**

What does @Temporal(TemporalType.TIMESTAMP) specify?

- A) Time zone configuration
- B) How to map java.util.Date to database (date, time, or timestamp)
- C) Temporal table support
- D) Time-based queries

---

### Question 73
**[Entity Mapping]**

What is @Embedded used for?

- A) Embedding SQL in Java
- B) Embedding a value object's fields into the entity's table
- C) Embedding entities in JSON
- D) Embedding images in database

---

### Question 74
**[Entity Mapping]**

What annotation marks a class as embeddable?

- A) @Embedded
- B) @Embeddable
- C) @ValueObject
- D) @Component

---

### Question 75
**[Inheritance]**

What does @Inheritance(strategy = InheritanceType.SINGLE_TABLE) mean?

- A) Each subclass has its own table
- B) All classes in hierarchy share one table with discriminator
- C) Only the parent has a table
- D) Tables are joined

---

### Question 76
**[Inheritance]**

What is InheritanceType.JOINED strategy?

- A) Single table for all entities
- B) Each class has a table, joined by primary key for queries
- C) Table per concrete class
- D) No inheritance support

---

### Question 77
**[Entity Mapping]**

What does @Lob annotation indicate?

- A) A logging field
- B) A Large Object (BLOB/CLOB) field
- C) A lazy-loaded field
- D) A locked field

---

### Question 78
**[Validation]**

Which package provides @NotNull, @Size, @Email annotations?

- A) javax.persistence
- B) jakarta.validation.constraints
- C) org.springframework.validation
- D) java.validation

---

### Question 79
**[Entity Mapping]**

What is @Version used for?

- A) API versioning
- B) Optimistic locking to prevent concurrent update conflicts
- C) Database schema versioning
- D) Entity version history

---

### Question 80
**[Entity Mapping]**

What does @Transient annotation do?

- A) Makes field nullable
- B) Excludes field from persistence (not mapped to database)
- C) Makes field temporary
- D) Logs field changes

---

## End of Questions

**Total: 80 Questions**
- ORM and Hibernate Fundamentals: 30
- Spring Data JPA (Repositories, Queries): 30
- Entity Mapping and Relationships: 20

---

*Proceed to `mcq-answers.md` for answers and explanations.*
