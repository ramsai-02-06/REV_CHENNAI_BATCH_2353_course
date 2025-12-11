# Exercise: Library Management System

## Objective
Build a console-based Library Management System that demonstrates Core Java concepts including OOP, Collections, Streams, Exception Handling, and File I/O.

## Requirements

### Domain Classes

#### Book
- ISBN (unique identifier)
- Title
- Author
- Genre (enum: FICTION, NON_FICTION, SCIENCE, HISTORY, TECHNOLOGY)
- PublishedYear
- Available (boolean)

#### Member
- MemberId (auto-generated)
- Name
- Email
- MembershipDate
- BorrowedBooks (List of Book)

#### BorrowRecord
- RecordId
- Member
- Book
- BorrowDate
- DueDate
- ReturnDate (nullable)

### Functionality

1. **Book Management**
   - Add new books
   - Search books by title, author, or genre
   - List all available books
   - Remove books from library

2. **Member Management**
   - Register new members
   - Search members by name or email
   - View member's borrowed books

3. **Borrowing System**
   - Borrow a book (max 3 books per member)
   - Return a book
   - Check overdue books
   - Calculate late fees ($0.50 per day)

4. **Reports (Using Streams)**
   - Most popular books (most borrowed)
   - Members with overdue books
   - Books by genre statistics
   - Revenue from late fees

### Java Concepts to Demonstrate

- **OOP**: Encapsulation, Inheritance, Polymorphism
- **Collections**: ArrayList, HashMap, HashSet
- **Generics**: Custom generic repository class
- **Streams**: filter, map, collect, groupingBy, reduce
- **Exceptions**: Custom LibraryException hierarchy
- **Optional**: For nullable return values
- **File I/O**: Save/load data to JSON or serialization
- **Java 21 Features**: Records, Pattern Matching, Text Blocks

## Class Structure

```
src/
├── model/
│   ├── Book.java
│   ├── Member.java
│   ├── BorrowRecord.java
│   └── Genre.java (enum)
├── repository/
│   ├── Repository.java (generic interface)
│   ├── BookRepository.java
│   └── MemberRepository.java
├── service/
│   ├── LibraryService.java
│   └── ReportService.java
├── exception/
│   ├── LibraryException.java
│   ├── BookNotAvailableException.java
│   └── MemberLimitExceededException.java
└── Main.java
```

## Sample Output
```
=== Library Management System ===
1. Manage Books
2. Manage Members
3. Borrow/Return Books
4. Reports
5. Exit

Choice: 4

=== Reports ===
1. Most Popular Books
2. Overdue Books
3. Genre Statistics
4. Late Fee Revenue

Choice: 3

Genre Statistics:
TECHNOLOGY: 45 books (12 borrowed)
FICTION: 38 books (20 borrowed)
SCIENCE: 25 books (8 borrowed)
...
```

## Skills Tested
- Object-Oriented Design
- Java Collections Framework
- Stream API operations
- Exception handling best practices
- File I/O operations
- Java 21 features
