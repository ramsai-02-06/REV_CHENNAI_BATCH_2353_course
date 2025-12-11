# Exercise: Employee Database Application with JDBC

## Objective
Build a console application that demonstrates JDBC concepts including connection management, CRUD operations, prepared statements, transactions, and the DAO pattern.

## Requirements

### Database Setup
Create a MySQL database `employee_db` with tables:

```sql
CREATE TABLE departments (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    salary DECIMAL(10, 2),
    hire_date DATE,
    dept_id INT,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);
```

### Application Features

1. **Connection Management**
   - Use a properties file for database configuration
   - Implement connection pooling (HikariCP optional)
   - Proper resource management with try-with-resources

2. **CRUD Operations**
   - Create, Read, Update, Delete for employees and departments
   - Use PreparedStatement for all queries (prevent SQL injection)
   - Handle ResultSet mapping to Java objects

3. **Advanced Queries**
   - Find employees by department
   - Search employees by name (partial match)
   - Get employees with salary in a range
   - Count employees per department

4. **Transaction Management**
   - Transfer employee between departments (transaction)
   - Bulk salary update with rollback on failure
   - Demonstrate commit/rollback

5. **DAO Pattern Implementation**
   - EmployeeDAO interface and implementation
   - DepartmentDAO interface and implementation
   - Generic DAO pattern (bonus)

### Project Structure
```
src/
├── config/
│   └── DatabaseConfig.java
├── model/
│   ├── Employee.java
│   └── Department.java
├── dao/
│   ├── DAO.java (generic interface)
│   ├── EmployeeDAO.java
│   ├── EmployeeDAOImpl.java
│   ├── DepartmentDAO.java
│   └── DepartmentDAOImpl.java
├── service/
│   └── EmployeeService.java
├── exception/
│   └── DataAccessException.java
└── Main.java

resources/
└── db.properties
```

### db.properties
```properties
db.url=jdbc:mysql://localhost:3306/employee_db
db.username=root
db.password=password
db.driver=com.mysql.cj.jdbc.Driver
```

## Expected Functionality

```
=== Employee Management System ===
1. Add Employee
2. View All Employees
3. Search Employee
4. Update Employee
5. Delete Employee
6. Transfer Department
7. Bulk Salary Update
8. Department Reports
9. Exit

Choice: 6
Enter Employee ID: 101
Enter New Department ID: 3

Transaction started...
Removing from current department...
Adding to new department...
Updating records...
Transaction committed successfully!

Employee John Doe transferred to Engineering department.
```

## Skills Tested
- JDBC Connection management
- PreparedStatement usage
- ResultSet handling
- Transaction management (commit/rollback)
- DAO design pattern
- Exception handling in database operations
- Resource management (try-with-resources)

## Bonus Challenges
1. Implement connection pooling with HikariCP
2. Add batch processing for bulk inserts
3. Implement pagination for large result sets
4. Add stored procedure calls
