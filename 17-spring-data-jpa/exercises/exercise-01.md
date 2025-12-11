# Exercise: Spring Data JPA Repository Implementation

## Objective
Build a data access layer using Spring Data JPA demonstrating repository interfaces, custom queries, relationships, and pagination.

## Requirements

### Domain Model: Blog System
Create entities for a blogging platform:

1. **User** (author)
2. **Post** (blog post)
3. **Comment** (on posts)
4. **Tag** (categorization)

### Entity Relationships
- User → Posts (One-to-Many)
- Post → Comments (One-to-Many)
- Post ↔ Tags (Many-to-Many)

### Repository Features to Implement

1. **Basic CRUD** (inherited from JpaRepository)

2. **Query Methods (Derived Queries)**
   - `findByTitle(String title)`
   - `findByAuthorUsername(String username)`
   - `findByPublishedTrue()`
   - `findByCreatedAtAfter(LocalDateTime date)`
   - `findByTitleContainingIgnoreCase(String keyword)`

3. **Custom JPQL Queries**
   - Find posts with comment count
   - Find most active authors
   - Search posts by multiple criteria

4. **Native Queries**
   - Complex reporting query
   - Performance-optimized query

5. **Pagination and Sorting**
   - Paginated post listing
   - Sorted results

6. **Specifications (Dynamic Queries)**
   - Build queries based on search criteria

### Project Structure
```
src/main/java/
├── entity/
│   ├── User.java
│   ├── Post.java
│   ├── Comment.java
│   └── Tag.java
├── repository/
│   ├── UserRepository.java
│   ├── PostRepository.java
│   ├── CommentRepository.java
│   └── TagRepository.java
├── specification/
│   └── PostSpecification.java
└── service/
    └── BlogService.java
```

### Example Queries
```java
// Derived query
List<Post> findByAuthorUsernameAndPublishedTrue(String username);

// JPQL query
@Query("SELECT p FROM Post p WHERE p.author.id = :authorId ORDER BY p.createdAt DESC")
List<Post> findPostsByAuthor(@Param("authorId") Long authorId);

// Native query
@Query(value = "SELECT * FROM posts WHERE MATCH(title, content) AGAINST(:search)",
       nativeQuery = true)
List<Post> fullTextSearch(@Param("search") String search);

// Pagination
Page<Post> findByPublishedTrue(Pageable pageable);
```

## Skills Tested
- Spring Data JPA repository interfaces
- Derived query methods
- JPQL and native queries
- Entity relationships
- Pagination and sorting
- JPA Specifications
