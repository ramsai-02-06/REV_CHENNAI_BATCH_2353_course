# SQL Queries Cheat Sheet

## Database Operations (DDL)

```sql
-- Create Database
CREATE DATABASE database_name;
CREATE DATABASE IF NOT EXISTS database_name;

-- Use Database
USE database_name;

-- Drop Database
DROP DATABASE database_name;
DROP DATABASE IF EXISTS database_name;

-- Show Databases
SHOW DATABASES;
```

## Table Operations (DDL)

```sql
-- Create Table
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    hire_date DATE,
    salary DECIMAL(10, 2),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- Create Table from Another Table
CREATE TABLE new_table AS
SELECT * FROM existing_table WHERE condition;

-- Show Tables
SHOW TABLES;

-- Describe Table Structure
DESC table_name;
DESCRIBE table_name;
SHOW COLUMNS FROM table_name;

-- Drop Table
DROP TABLE table_name;
DROP TABLE IF EXISTS table_name;

-- Truncate Table (delete all data, keep structure)
TRUNCATE TABLE table_name;
```

## Altering Tables

```sql
-- Add Column
ALTER TABLE table_name
ADD COLUMN column_name datatype;

ALTER TABLE employees
ADD COLUMN phone VARCHAR(15);

-- Drop Column
ALTER TABLE table_name
DROP COLUMN column_name;

-- Modify Column
ALTER TABLE table_name
MODIFY COLUMN column_name new_datatype;

ALTER TABLE employees
MODIFY COLUMN salary DECIMAL(12, 2);

-- Rename Column
ALTER TABLE table_name
CHANGE old_column_name new_column_name datatype;

ALTER TABLE table_name
RENAME COLUMN old_name TO new_name;

-- Add Primary Key
ALTER TABLE table_name
ADD PRIMARY KEY (column_name);

-- Add Foreign Key
ALTER TABLE table_name
ADD FOREIGN KEY (column_name)
REFERENCES other_table(column_name);

-- Drop Foreign Key
ALTER TABLE table_name
DROP FOREIGN KEY constraint_name;

-- Rename Table
RENAME TABLE old_name TO new_name;
ALTER TABLE old_name RENAME TO new_name;
```

## Constraints

```sql
-- NOT NULL
CREATE TABLE users (
    user_id INT NOT NULL,
    username VARCHAR(50) NOT NULL
);

-- UNIQUE
CREATE TABLE users (
    email VARCHAR(100) UNIQUE
);

-- PRIMARY KEY
CREATE TABLE users (
    user_id INT PRIMARY KEY
);

-- FOREIGN KEY
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- CHECK
CREATE TABLE employees (
    employee_id INT,
    salary DECIMAL(10, 2),
    CHECK (salary > 0)
);

-- DEFAULT
CREATE TABLE products (
    product_id INT,
    stock_quantity INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- AUTO_INCREMENT
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY
);
```

## Data Manipulation (DML)

### INSERT

```sql
-- Insert Single Row
INSERT INTO employees (first_name, last_name, email, hire_date, salary)
VALUES ('John', 'Doe', 'john.doe@example.com', '2024-01-15', 50000.00);

-- Insert Multiple Rows
INSERT INTO employees (first_name, last_name, email, hire_date, salary)
VALUES
    ('Jane', 'Smith', 'jane.smith@example.com', '2024-01-16', 55000.00),
    ('Bob', 'Johnson', 'bob.johnson@example.com', '2024-01-17', 52000.00);

-- Insert from Another Table
INSERT INTO archived_employees
SELECT * FROM employees WHERE hire_date < '2020-01-01';

-- Insert with Specific Columns
INSERT INTO employees (first_name, last_name)
VALUES ('Alice', 'Williams');
```

### SELECT

```sql
-- Select All Columns
SELECT * FROM employees;

-- Select Specific Columns
SELECT first_name, last_name, salary FROM employees;

-- Select Distinct
SELECT DISTINCT department_id FROM employees;

-- Select with Alias
SELECT first_name AS fname, last_name AS lname FROM employees;

-- Select with Calculation
SELECT first_name, salary, salary * 1.1 AS increased_salary FROM employees;

-- Limit Results
SELECT * FROM employees LIMIT 10;
SELECT * FROM employees LIMIT 5 OFFSET 10;  -- Skip first 10, get next 5
```

### UPDATE

```sql
-- Update Single Row
UPDATE employees
SET salary = 60000.00
WHERE employee_id = 1;

-- Update Multiple Columns
UPDATE employees
SET salary = 55000.00, department_id = 2
WHERE employee_id = 1;

-- Update Multiple Rows
UPDATE employees
SET salary = salary * 1.1
WHERE department_id = 1;

-- Update All Rows (Be Careful!)
UPDATE employees
SET status = 'active';
```

### DELETE

```sql
-- Delete Specific Rows
DELETE FROM employees
WHERE employee_id = 1;

-- Delete with Condition
DELETE FROM employees
WHERE hire_date < '2020-01-01';

-- Delete All Rows (Be Careful!)
DELETE FROM employees;
```

## WHERE Clause

```sql
-- Basic Conditions
SELECT * FROM employees WHERE salary > 50000;
SELECT * FROM employees WHERE department_id = 1;
SELECT * FROM employees WHERE first_name = 'John';

-- AND, OR, NOT
SELECT * FROM employees WHERE salary > 50000 AND department_id = 1;
SELECT * FROM employees WHERE department_id = 1 OR department_id = 2;
SELECT * FROM employees WHERE NOT department_id = 1;

-- BETWEEN
SELECT * FROM employees WHERE salary BETWEEN 40000 AND 60000;
SELECT * FROM employees WHERE hire_date BETWEEN '2024-01-01' AND '2024-12-31';

-- IN
SELECT * FROM employees WHERE department_id IN (1, 2, 3);
SELECT * FROM employees WHERE first_name IN ('John', 'Jane', 'Bob');

-- LIKE (Pattern Matching)
SELECT * FROM employees WHERE first_name LIKE 'J%';     -- Starts with J
SELECT * FROM employees WHERE email LIKE '%@gmail.com'; -- Ends with @gmail.com
SELECT * FROM employees WHERE first_name LIKE '%oh%';   -- Contains 'oh'
SELECT * FROM employees WHERE first_name LIKE '_ohn';   -- Second char onwards is 'ohn'

-- IS NULL / IS NOT NULL
SELECT * FROM employees WHERE email IS NULL;
SELECT * FROM employees WHERE email IS NOT NULL;
```

## ORDER BY

```sql
-- Sort Ascending (default)
SELECT * FROM employees ORDER BY salary;
SELECT * FROM employees ORDER BY salary ASC;

-- Sort Descending
SELECT * FROM employees ORDER BY salary DESC;

-- Sort by Multiple Columns
SELECT * FROM employees ORDER BY department_id ASC, salary DESC;

-- Sort by Column Position
SELECT first_name, salary FROM employees ORDER BY 2 DESC;
```

## GROUP BY

```sql
-- Basic Grouping
SELECT department_id, COUNT(*) as employee_count
FROM employees
GROUP BY department_id;

-- Group with Multiple Columns
SELECT department_id, job_title, COUNT(*) as count
FROM employees
GROUP BY department_id, job_title;

-- Group with Aggregate Functions
SELECT department_id,
       COUNT(*) as total_employees,
       AVG(salary) as avg_salary,
       MAX(salary) as max_salary,
       MIN(salary) as min_salary
FROM employees
GROUP BY department_id;

-- HAVING Clause (filter groups)
SELECT department_id, AVG(salary) as avg_salary
FROM employees
GROUP BY department_id
HAVING AVG(salary) > 50000;

-- WHERE vs HAVING
SELECT department_id, COUNT(*) as emp_count
FROM employees
WHERE salary > 40000              -- Filter rows before grouping
GROUP BY department_id
HAVING COUNT(*) > 5;              -- Filter groups after grouping
```

## Aggregate Functions

```sql
-- COUNT
SELECT COUNT(*) FROM employees;                    -- Total rows
SELECT COUNT(email) FROM employees;                -- Non-null emails
SELECT COUNT(DISTINCT department_id) FROM employees; -- Unique departments

-- SUM
SELECT SUM(salary) FROM employees;
SELECT SUM(salary) as total_salary FROM employees WHERE department_id = 1;

-- AVG
SELECT AVG(salary) FROM employees;
SELECT AVG(salary) as average_salary FROM employees;

-- MAX
SELECT MAX(salary) FROM employees;
SELECT MAX(hire_date) FROM employees;

-- MIN
SELECT MIN(salary) FROM employees;
SELECT MIN(hire_date) FROM employees;

-- Combining Aggregates
SELECT
    COUNT(*) as total_employees,
    AVG(salary) as avg_salary,
    MAX(salary) as highest_salary,
    MIN(salary) as lowest_salary,
    SUM(salary) as total_payroll
FROM employees;
```

## JOINS

### INNER JOIN

```sql
-- Basic Inner Join
SELECT e.first_name, e.last_name, d.department_name
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id;

-- Join Multiple Tables
SELECT e.first_name, d.department_name, l.city
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id
INNER JOIN locations l ON d.location_id = l.location_id;

-- Alternative Syntax
SELECT e.first_name, d.department_name
FROM employees e, departments d
WHERE e.department_id = d.department_id;
```

### LEFT JOIN (LEFT OUTER JOIN)

```sql
-- Get all employees, even without department
SELECT e.first_name, e.last_name, d.department_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id;

-- Find employees without department
SELECT e.first_name, e.last_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id
WHERE d.department_id IS NULL;
```

### RIGHT JOIN (RIGHT OUTER JOIN)

```sql
-- Get all departments, even without employees
SELECT e.first_name, d.department_name
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.department_id;
```

### FULL OUTER JOIN

```sql
-- MySQL doesn't support FULL OUTER JOIN directly
-- Simulate with UNION
SELECT e.first_name, d.department_name
FROM employees e
LEFT JOIN departments d ON e.department_id = d.department_id
UNION
SELECT e.first_name, d.department_name
FROM employees e
RIGHT JOIN departments d ON e.department_id = d.department_id;
```

### CROSS JOIN

```sql
-- Cartesian Product (every combination)
SELECT e.first_name, d.department_name
FROM employees e
CROSS JOIN departments d;
```

### SELF JOIN

```sql
-- Find employees with same manager
SELECT e1.first_name as employee1, e2.first_name as employee2, e1.manager_id
FROM employees e1
JOIN employees e2 ON e1.manager_id = e2.manager_id
WHERE e1.employee_id < e2.employee_id;

-- Employee and Manager names
SELECT e.first_name as employee, m.first_name as manager
FROM employees e
LEFT JOIN employees m ON e.manager_id = m.employee_id;
```

## Subqueries

### Subquery in WHERE

```sql
-- Employees with salary above average
SELECT first_name, salary
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);

-- Employees in specific departments
SELECT first_name
FROM employees
WHERE department_id IN (
    SELECT department_id FROM departments WHERE location_id = 1700
);
```

### Subquery in FROM

```sql
-- Derived table
SELECT avg_sal.department_id, avg_sal.average_salary
FROM (
    SELECT department_id, AVG(salary) as average_salary
    FROM employees
    GROUP BY department_id
) avg_sal
WHERE avg_sal.average_salary > 50000;
```

### Subquery in SELECT

```sql
-- Scalar subquery
SELECT first_name, salary,
    (SELECT AVG(salary) FROM employees) as avg_salary,
    salary - (SELECT AVG(salary) FROM employees) as difference
FROM employees;
```

### EXISTS

```sql
-- Departments with employees
SELECT department_name
FROM departments d
WHERE EXISTS (
    SELECT 1 FROM employees e WHERE e.department_id = d.department_id
);

-- NOT EXISTS
SELECT department_name
FROM departments d
WHERE NOT EXISTS (
    SELECT 1 FROM employees e WHERE e.department_id = d.department_id
);
```

## String Functions

```sql
-- CONCAT
SELECT CONCAT(first_name, ' ', last_name) as full_name FROM employees;

-- UPPER / LOWER
SELECT UPPER(first_name), LOWER(last_name) FROM employees;

-- LENGTH / CHAR_LENGTH
SELECT first_name, LENGTH(first_name) as length FROM employees;

-- SUBSTRING
SELECT SUBSTRING(first_name, 1, 3) FROM employees;  -- First 3 characters

-- TRIM
SELECT TRIM('  hello  ') as trimmed;
SELECT LTRIM('  hello') as left_trimmed;
SELECT RTRIM('hello  ') as right_trimmed;

-- REPLACE
SELECT REPLACE(email, '@oldcompany.com', '@newcompany.com') FROM employees;

-- LEFT / RIGHT
SELECT LEFT(first_name, 3), RIGHT(last_name, 3) FROM employees;
```

## Date Functions

```sql
-- Current Date/Time
SELECT NOW();              -- Current datetime
SELECT CURDATE();          -- Current date
SELECT CURTIME();          -- Current time

-- Date Extraction
SELECT YEAR(hire_date), MONTH(hire_date), DAY(hire_date) FROM employees;
SELECT HOUR(order_time), MINUTE(order_time), SECOND(order_time) FROM orders;

-- Date Formatting
SELECT DATE_FORMAT(hire_date, '%Y-%m-%d') FROM employees;
SELECT DATE_FORMAT(hire_date, '%M %d, %Y') FROM employees;
-- %Y=year, %m=month(num), %M=month(name), %d=day, %W=weekday

-- Date Arithmetic
SELECT hire_date, DATE_ADD(hire_date, INTERVAL 1 YEAR) as anniversary FROM employees;
SELECT DATE_SUB(NOW(), INTERVAL 30 DAY) as thirty_days_ago;
SELECT DATEDIFF(NOW(), hire_date) as days_employed FROM employees;

-- Extract Parts
SELECT EXTRACT(YEAR FROM hire_date) as year FROM employees;
SELECT EXTRACT(MONTH FROM hire_date) as month FROM employees;
```

## Numeric Functions

```sql
-- ROUND
SELECT salary, ROUND(salary, 0) as rounded FROM employees;
SELECT ROUND(1234.5678, 2);  -- 1234.57

-- CEIL / FLOOR
SELECT CEIL(3.14);   -- 4
SELECT FLOOR(3.99);  -- 3

-- ABS (Absolute value)
SELECT ABS(-10);     -- 10

-- MOD (Modulo)
SELECT MOD(10, 3);   -- 1

-- POWER
SELECT POWER(2, 3);  -- 8

-- SQRT
SELECT SQRT(16);     -- 4

-- RAND (Random number)
SELECT RAND();       -- Random between 0 and 1
SELECT FLOOR(RAND() * 100);  -- Random between 0 and 99
```

## Conditional Expressions

### CASE

```sql
-- Simple CASE
SELECT first_name, salary,
    CASE
        WHEN salary < 40000 THEN 'Low'
        WHEN salary BETWEEN 40000 AND 60000 THEN 'Medium'
        WHEN salary > 60000 THEN 'High'
        ELSE 'Unknown'
    END as salary_grade
FROM employees;

-- CASE in Aggregation
SELECT department_id,
    COUNT(CASE WHEN salary > 50000 THEN 1 END) as high_earners,
    COUNT(CASE WHEN salary <= 50000 THEN 1 END) as regular_earners
FROM employees
GROUP BY department_id;
```

### IF

```sql
SELECT first_name, salary,
    IF(salary > 50000, 'High', 'Low') as salary_level
FROM employees;
```

### IFNULL / COALESCE

```sql
-- IFNULL
SELECT first_name, IFNULL(email, 'No email') as email FROM employees;

-- COALESCE (first non-null value)
SELECT first_name, COALESCE(email, phone, 'No contact') as contact FROM employees;
```

## UNION

```sql
-- Combine results (remove duplicates)
SELECT first_name FROM employees WHERE department_id = 1
UNION
SELECT first_name FROM employees WHERE salary > 50000;

-- Include duplicates
SELECT first_name FROM employees WHERE department_id = 1
UNION ALL
SELECT first_name FROM employees WHERE salary > 50000;

-- Union with ORDER BY
SELECT first_name, 'Employee' as type FROM employees
UNION
SELECT first_name, 'Manager' as type FROM managers
ORDER BY first_name;
```

## Indexes

```sql
-- Create Index
CREATE INDEX idx_last_name ON employees(last_name);
CREATE INDEX idx_dept_sal ON employees(department_id, salary);

-- Unique Index
CREATE UNIQUE INDEX idx_email ON employees(email);

-- Show Indexes
SHOW INDEXES FROM employees;

-- Drop Index
DROP INDEX idx_last_name ON employees;
```

## Views

```sql
-- Create View
CREATE VIEW employee_details AS
SELECT e.first_name, e.last_name, d.department_name, e.salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id;

-- Use View
SELECT * FROM employee_details WHERE salary > 50000;

-- Update View
CREATE OR REPLACE VIEW employee_details AS
SELECT e.first_name, e.last_name, d.department_name, e.salary, l.city
FROM employees e
JOIN departments d ON e.department_id = d.department_id
JOIN locations l ON d.location_id = l.location_id;

-- Drop View
DROP VIEW employee_details;
```

## Transactions

```sql
-- Start Transaction
START TRANSACTION;
-- or
BEGIN;

-- Perform operations
UPDATE accounts SET balance = balance - 100 WHERE account_id = 1;
UPDATE accounts SET balance = balance + 100 WHERE account_id = 2;

-- Commit (save changes)
COMMIT;

-- Rollback (undo changes)
ROLLBACK;

-- Savepoint
START TRANSACTION;
UPDATE accounts SET balance = balance - 100 WHERE account_id = 1;
SAVEPOINT sp1;
UPDATE accounts SET balance = balance + 100 WHERE account_id = 2;
ROLLBACK TO sp1;  -- Undo only after savepoint
COMMIT;
```

## Common Table Expressions (CTE)

```sql
-- Basic CTE
WITH high_earners AS (
    SELECT * FROM employees WHERE salary > 60000
)
SELECT first_name, last_name, salary
FROM high_earners;

-- Multiple CTEs
WITH
dept_avg AS (
    SELECT department_id, AVG(salary) as avg_salary
    FROM employees
    GROUP BY department_id
),
emp_info AS (
    SELECT e.*, d.department_name
    FROM employees e
    JOIN departments d ON e.department_id = d.department_id
)
SELECT ei.first_name, ei.department_name, ei.salary, da.avg_salary
FROM emp_info ei
JOIN dept_avg da ON ei.department_id = da.department_id;

-- Recursive CTE (organizational hierarchy)
WITH RECURSIVE employee_hierarchy AS (
    -- Anchor: top-level employees
    SELECT employee_id, first_name, manager_id, 1 as level
    FROM employees
    WHERE manager_id IS NULL

    UNION ALL

    -- Recursive: subordinates
    SELECT e.employee_id, e.first_name, e.manager_id, eh.level + 1
    FROM employees e
    JOIN employee_hierarchy eh ON e.manager_id = eh.employee_id
)
SELECT * FROM employee_hierarchy;
```

## Window Functions

```sql
-- ROW_NUMBER
SELECT first_name, salary,
    ROW_NUMBER() OVER (ORDER BY salary DESC) as row_num
FROM employees;

-- RANK (gaps in ranking)
SELECT first_name, salary,
    RANK() OVER (ORDER BY salary DESC) as rank
FROM employees;

-- DENSE_RANK (no gaps)
SELECT first_name, salary,
    DENSE_RANK() OVER (ORDER BY salary DESC) as dense_rank
FROM employees;

-- PARTITION BY
SELECT first_name, department_id, salary,
    ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) as dept_rank
FROM employees;

-- LAG / LEAD (previous/next row)
SELECT first_name, salary,
    LAG(salary) OVER (ORDER BY salary) as prev_salary,
    LEAD(salary) OVER (ORDER BY salary) as next_salary
FROM employees;

-- FIRST_VALUE / LAST_VALUE
SELECT first_name, department_id, salary,
    FIRST_VALUE(salary) OVER (PARTITION BY department_id ORDER BY salary) as lowest_in_dept,
    LAST_VALUE(salary) OVER (PARTITION BY department_id ORDER BY salary ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) as highest_in_dept
FROM employees;
```

## Performance Tips

```sql
-- Use EXPLAIN to analyze query performance
EXPLAIN SELECT * FROM employees WHERE salary > 50000;

-- Use indexes on frequently queried columns
CREATE INDEX idx_salary ON employees(salary);

-- Avoid SELECT *
SELECT first_name, last_name FROM employees;  -- Better

-- Use LIMIT for large datasets
SELECT * FROM employees LIMIT 100;

-- Use EXISTS instead of IN for subqueries
SELECT * FROM departments d
WHERE EXISTS (SELECT 1 FROM employees e WHERE e.department_id = d.department_id);

-- Avoid functions on indexed columns in WHERE
-- Bad:
SELECT * FROM employees WHERE YEAR(hire_date) = 2024;
-- Good:
SELECT * FROM employees WHERE hire_date BETWEEN '2024-01-01' AND '2024-12-31';
```

## Quick Reference

```sql
-- CRUD Operations
INSERT INTO table VALUES (...);
SELECT * FROM table WHERE condition;
UPDATE table SET column = value WHERE condition;
DELETE FROM table WHERE condition;

-- Joins
INNER JOIN    -- Only matching rows
LEFT JOIN     -- All from left + matching from right
RIGHT JOIN    -- All from right + matching from left
CROSS JOIN    -- Cartesian product

-- Aggregates
COUNT(*), SUM(), AVG(), MAX(), MIN()

-- Grouping
GROUP BY column HAVING condition

-- Sorting
ORDER BY column ASC/DESC

-- Filtering
WHERE, HAVING, IN, BETWEEN, LIKE, IS NULL
```

---

**Remember:**
- Always use WHERE with UPDATE and DELETE!
- Test queries with SELECT first before UPDATE/DELETE
- Use transactions for important operations
- Back up data before making major changes
- Use EXPLAIN to optimize slow queries
