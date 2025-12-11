# Project 1: Java Console Application with MySQL and JDBC

## Project Overview
This is your first major project where you'll build a complete Java console application that interacts with a MySQL database using JDBC. This project demonstrates your understanding of Core Java, database concepts, and JDBC connectivity.

## Timeline
**Start:** Week 3 (after MySQL and Java basics)
**Duration:** Ongoing through Week 5
**Technologies:** Java 21, MySQL, JDBC

## Learning Objectives
- Apply Core Java concepts in a real application
- Design and implement a database schema
- Perform CRUD operations using JDBC
- Handle exceptions and edge cases
- Follow coding best practices
- Document and present your work

## Technical Requirements

### Required Technologies
- Java 21 (JDK)
- MySQL 8.x
- JDBC Driver (MySQL Connector/J)
- Any Java IDE (IntelliJ IDEA recommended)

### Core Concepts to Apply
- Object-Oriented Programming
- Collections Framework
- Exception Handling
- File I/O (optional for data import/export)
- JDBC API
- SQL (DDL, DML, Queries)

## Project Ideas

### Option 1: Library Management System
**Description:** Manage books, members, and borrowing transactions

**Features:**
- Add, update, delete, and search books
- Register and manage library members
- Issue and return books
- View borrowing history
- Generate reports (overdue books, popular books, etc.)

**Database Tables:**
- Books (book_id, title, author, isbn, category, quantity)
- Members (member_id, name, email, phone, membership_date)
- Transactions (transaction_id, book_id, member_id, issue_date, return_date, status)

### Option 2: Banking Application
**Description:** Simple banking system for account management

**Features:**
- Create and manage customer accounts
- Deposit and withdraw money
- Transfer funds between accounts
- View transaction history
- Generate account statements

**Database Tables:**
- Customers (customer_id, name, email, phone, address)
- Accounts (account_id, customer_id, account_type, balance, created_date)
- Transactions (transaction_id, account_id, transaction_type, amount, timestamp, description)

### Option 3: Employee Management System
**Description:** HR system for employee records and payroll

**Features:**
- Add, update, delete employees
- Manage departments
- Track attendance
- Calculate salaries
- Generate employee reports

**Database Tables:**
- Employees (employee_id, name, email, phone, department_id, position, salary, hire_date)
- Departments (department_id, department_name, manager_id)
- Attendance (attendance_id, employee_id, date, status)
- Payroll (payroll_id, employee_id, month, year, salary, deductions, net_pay)

### Option 4: Inventory Management System
**Description:** Track products and inventory levels

**Features:**
- Add and manage products
- Track stock levels
- Record sales and purchases
- Low stock alerts
- Sales reports

**Database Tables:**
- Products (product_id, name, description, category, price, stock_quantity)
- Suppliers (supplier_id, name, contact, email, phone)
- Sales (sale_id, product_id, quantity, sale_date, total_amount)
- Purchases (purchase_id, product_id, supplier_id, quantity, purchase_date, cost)

## Project Structure

```
project-1/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/yourname/project/
│   │   │   │   ├── model/          # Entity classes
│   │   │   │   ├── dao/            # Data Access Objects
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── util/           # Utility classes (DB connection)
│   │   │   │   └── Main.java       # Entry point
│   │   └── resources/
│   │       ├── db.properties       # Database configuration
│   │       └── schema.sql          # Database schema
│   └── test/
│       └── java/                   # Test classes (optional)
├── lib/                            # JDBC driver JAR
├── docs/                           # Documentation
│   ├── ERD.png                     # Entity-Relationship Diagram
│   └── UserGuide.md                # How to use the application
└── README.md                       # Project documentation
```

## Implementation Guidelines

### Phase 1: Planning and Design (Day 1)
1. Choose your project idea or propose your own
2. Identify key features (minimum 5 CRUD operations)
3. Design database schema
4. Create Entity-Relationship Diagram
5. Plan class structure

### Phase 2: Database Setup (Day 1-2)
1. Install MySQL if not already installed
2. Create database
3. Write SQL scripts to create tables
4. Insert sample data for testing
5. Test queries in MySQL Workbench

### Phase 3: Java Project Setup (Day 2)
1. Create Java project in IDE
2. Add MySQL JDBC driver to classpath
3. Create package structure
4. Set up database connection utility class
5. Test database connectivity

### Phase 4: Implementation (Day 3-5)
1. Create model classes (POJOs)
2. Implement DAO layer with JDBC
   - Insert operations
   - Select/Read operations
   - Update operations
   - Delete operations
3. Implement service layer (business logic)
4. Create console-based user interface
5. Implement menu system

### Phase 5: Testing and Refinement (Day 5-7)
1. Test all CRUD operations
2. Handle edge cases
3. Implement input validation
4. Add exception handling
5. Optimize queries
6. Code cleanup and refactoring

### Phase 6: Documentation (Day 7)
1. Write README with setup instructions
2. Create user guide
3. Document code with Javadoc comments
4. Prepare presentation

## Required Features

### Minimum Requirements (Must Have)
1. At least 3 database tables with relationships
2. Complete CRUD operations for main entities
3. Menu-driven console interface
4. Proper exception handling
5. Input validation
6. Database connection management (proper opening/closing)
7. At least one complex query (JOIN, GROUP BY, or subquery)
8. Code organized in proper packages
9. Basic documentation

### Additional Features (Should Have)
1. Search and filter functionality
2. Data validation (email format, phone number, etc.)
3. Transaction management
4. Export data to file (CSV or text)
5. Import data from file
6. Reporting features

### Advanced Features (Nice to Have)
1. Connection pooling
2. Prepared statements for all queries
3. Logging using a logging framework
4. Configuration file for database credentials
5. Unit tests for DAO layer
6. Pagination for large datasets

## Code Examples

### Database Connection Utility
```java
package com.yourname.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
            Properties props = new Properties();
            InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties");
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### Sample DAO Pattern
```java
package com.yourname.project.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.getQuantity());
            stmt.executeUpdate();
        }
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
        }
        return null;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        }
        return books;
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, quantity=? WHERE book_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setInt(4, book.getQuantity());
            stmt.setInt(5, book.getBookId());
            stmt.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setQuantity(rs.getInt("quantity"));
        return book;
    }
}
```

## Evaluation Criteria

### Functionality (40%)
- Application runs without errors
- All CRUD operations work correctly
- Features match requirements
- Proper data validation

### Code Quality (30%)
- Well-organized package structure
- Proper naming conventions
- DRY principle followed
- Comments and documentation
- Exception handling
- Resource management (connections closed properly)

### Database Design (15%)
- Normalized schema
- Appropriate data types
- Primary and foreign keys
- Indexes where appropriate
- Sample data provided

### Documentation (10%)
- Clear README with setup instructions
- Code comments (Javadoc)
- ERD diagram
- User guide

### Presentation (5%)
- Clear demonstration of features
- Explanation of technical choices
- Discussion of challenges faced
- Q&A handling

## Submission Guidelines

### Deliverables
1. Complete source code (organized in proper structure)
2. Database schema SQL file
3. README.md with setup and run instructions
4. ERD diagram
5. Brief presentation (5-10 minutes)

### Submission Checklist
- [ ] All features implemented and tested
- [ ] Code compiles and runs without errors
- [ ] Database scripts provided
- [ ] Documentation complete
- [ ] Code commented appropriately
- [ ] README with clear instructions
- [ ] No hardcoded credentials (use properties file)
- [ ] Presentation prepared

## Common Pitfalls to Avoid

1. **Not closing resources**: Always close Connection, Statement, ResultSet
2. **SQL Injection**: Use PreparedStatement, not Statement
3. **Poor exception handling**: Don't just print stack trace, handle appropriately
4. **Hardcoded values**: Use configuration files
5. **No input validation**: Validate user input before database operations
6. **Magic numbers**: Use constants for repeated values
7. **God classes**: Keep classes focused with single responsibility
8. **No documentation**: Document your code and provide setup instructions

## Tips for Success

1. Start early and work incrementally
2. Test each feature as you implement it
3. Commit to Git regularly
4. Keep your database connection logic separate
5. Use PreparedStatement to prevent SQL injection
6. Handle exceptions gracefully with user-friendly messages
7. Validate all user inputs
8. Keep your menu system simple and intuitive
9. Write clean, readable code
10. Ask for help when stuck

## Resources

### JDBC Documentation
- [Oracle JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [MySQL JDBC Driver](https://dev.mysql.com/downloads/connector/j/)

### Related Modules
- [Module 9: MySQL](../../09-mysql/)
- [Module 10: Core Java](../../10-java-core/)
- [Module 11: JDBC](../../11-jdbc/)

## Support

- Ask questions during office hours
- Use discussion forums
- Collaborate with peers (but write your own code)
- Consult module README files for reference

---

**Good luck with your first project! This is where your Java and database skills come together.** 🚀
