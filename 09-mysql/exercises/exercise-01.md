# Exercise: E-Commerce Database Design and Queries

## Objective
Design and implement a relational database for an e-commerce application, then write SQL queries to manage and analyze the data.

## Part 1: Database Design

### Requirements
Create a MySQL database called `ecommerce_db` with the following tables:

#### 1. customers
- customer_id (Primary Key, Auto Increment)
- first_name, last_name
- email (unique)
- phone
- created_at (timestamp)

#### 2. categories
- category_id (Primary Key)
- category_name
- description

#### 3. products
- product_id (Primary Key)
- product_name
- description
- price (decimal)
- stock_quantity
- category_id (Foreign Key)
- created_at

#### 4. orders
- order_id (Primary Key)
- customer_id (Foreign Key)
- order_date
- status (enum: 'pending', 'processing', 'shipped', 'delivered', 'cancelled')
- total_amount
- shipping_address

#### 5. order_items
- order_item_id (Primary Key)
- order_id (Foreign Key)
- product_id (Foreign Key)
- quantity
- unit_price

## Part 2: SQL Queries

Write SQL queries for the following tasks:

### Basic Queries
1. Insert sample data (at least 5 records per table)
2. Select all products with their category names
3. Find all orders for a specific customer
4. Update product stock after an order
5. Delete cancelled orders older than 30 days

### Intermediate Queries
6. Find the top 5 best-selling products
7. Calculate total revenue per category
8. List customers who haven't ordered in the last 6 months
9. Find products that are low in stock (quantity < 10)

### Advanced Queries
10. Create a view for order summary with customer and product details
11. Write a query using subquery to find customers who ordered above-average amounts
12. Use GROUP BY and HAVING to find categories with more than $1000 in sales
13. Write a transaction to process an order (insert order, order_items, update stock)

## Expected Deliverables
1. `schema.sql` - Database and table creation scripts
2. `seed_data.sql` - INSERT statements for sample data
3. `queries.sql` - All query solutions

## Skills Tested
- DDL: CREATE DATABASE, CREATE TABLE
- DML: INSERT, UPDATE, DELETE, SELECT
- Constraints: PRIMARY KEY, FOREIGN KEY, UNIQUE, NOT NULL
- Joins: INNER JOIN, LEFT JOIN
- Aggregations: COUNT, SUM, AVG, GROUP BY, HAVING
- Subqueries and Views
- Transactions

## Bonus Challenge
- Implement a stored procedure for processing orders
- Create triggers to automatically update stock and log order history
