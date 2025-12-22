# Exercise: Array Methods with Callbacks

## Objective
Master JavaScript's powerful array methods that use callbacks: `forEach`, `map`, `filter`, `find`, `reduce`, `some`, `every`, and `sort`.

---

## Sample Data

Use this data throughout the exercises:

```javascript
const products = [
    { id: 1, name: 'Laptop', price: 999, category: 'Electronics', inStock: true, rating: 4.5 },
    { id: 2, name: 'Headphones', price: 199, category: 'Electronics', inStock: true, rating: 4.2 },
    { id: 3, name: 'Coffee Maker', price: 79, category: 'Kitchen', inStock: false, rating: 4.0 },
    { id: 4, name: 'Desk Chair', price: 299, category: 'Furniture', inStock: true, rating: 4.7 },
    { id: 5, name: 'Monitor', price: 349, category: 'Electronics', inStock: true, rating: 4.3 },
    { id: 6, name: 'Keyboard', price: 129, category: 'Electronics', inStock: false, rating: 4.6 },
    { id: 7, name: 'Blender', price: 49, category: 'Kitchen', inStock: true, rating: 3.8 },
    { id: 8, name: 'Bookshelf', price: 159, category: 'Furniture', inStock: true, rating: 4.1 }
];

const numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

const users = [
    { id: 1, name: 'Alice', age: 28, city: 'New York', isActive: true },
    { id: 2, name: 'Bob', age: 35, city: 'San Francisco', isActive: false },
    { id: 3, name: 'Charlie', age: 22, city: 'New York', isActive: true },
    { id: 4, name: 'Diana', age: 31, city: 'Chicago', isActive: true },
    { id: 5, name: 'Eve', age: 45, city: 'San Francisco', isActive: false }
];
```

---

## Part 1: forEach

### Exercise 1.1: Basic Iteration
Use `forEach` to:

```javascript
// 1. Log each product name
products.forEach(/* your code */);

// 2. Log each number squared
numbers.forEach(/* your code */);

// 3. Log each user's name and city
users.forEach(/* your code */);
// Expected: "Alice lives in New York", etc.

// 4. Calculate total price of all products (using forEach and a variable)
let total = 0;
products.forEach(/* your code */);
console.log('Total:', total);
```

### Exercise 1.2: forEach with Index
Use forEach with index parameter:

```javascript
// 1. Log each product with its position
// Expected: "1. Laptop", "2. Headphones", etc.

// 2. Log only products at even indices

// 3. Create a numbered list of user names
```

---

## Part 2: map

### Exercise 2.1: Transformations
Use `map` to create new arrays:

```javascript
// 1. Get array of product names
const productNames = products.map(/* your code */);
// Expected: ['Laptop', 'Headphones', ...]

// 2. Get array of prices with 10% discount
const discountedPrices = products.map(/* your code */);
// Expected: [899.1, 179.1, ...]

// 3. Double each number
const doubled = numbers.map(/* your code */);

// 4. Create array of user display strings
const userDisplays = users.map(/* your code */);
// Expected: ['Alice (28)', 'Bob (35)', ...]

// 5. Create simplified product objects
const simplified = products.map(/* your code */);
// Expected: [{ name: 'Laptop', price: 999 }, ...]
```

### Exercise 2.2: Complex Transformations
Create more complex transformations:

```javascript
// 1. Add a 'displayPrice' property formatted as currency
const withDisplayPrice = products.map(product => ({
    ...product,
    displayPrice: /* format as "$999.00" */
}));

// 2. Create product cards HTML
const productCards = products.map(product => {
    // Return HTML string like:
    // <div class="product">
    //   <h3>Laptop</h3>
    //   <p>$999</p>
    // </div>
});

// 3. Calculate age in 10 years for all users
const usersInFuture = users.map(/* your code */);
// Add property: ageIn10Years
```

---

## Part 3: filter

### Exercise 3.1: Basic Filtering
Use `filter` to select items:

```javascript
// 1. Get products in stock
const inStock = products.filter(/* your code */);

// 2. Get products under $200
const affordable = products.filter(/* your code */);

// 3. Get electronics
const electronics = products.filter(/* your code */);

// 4. Get even numbers
const evens = numbers.filter(/* your code */);

// 5. Get active users
const activeUsers = users.filter(/* your code */);

// 6. Get users from New York
const newYorkers = users.filter(/* your code */);
```

### Exercise 3.2: Complex Filtering
Combine multiple conditions:

```javascript
// 1. Get electronics that are in stock
const availableElectronics = products.filter(/* your code */);

// 2. Get products with rating >= 4.5 and in stock
const topRatedAvailable = products.filter(/* your code */);

// 3. Get products between $100 and $500
const midRange = products.filter(/* your code */);

// 4. Get active users over 30
const activeOver30 = users.filter(/* your code */);

// 5. Get products NOT in Electronics category
const nonElectronics = products.filter(/* your code */);
```

---

## Part 4: find and findIndex

### Exercise 4.1: Finding Single Items
Use `find` and `findIndex`:

```javascript
// 1. Find the product named "Laptop"
const laptop = products.find(/* your code */);

// 2. Find the first product over $500
const expensive = products.find(/* your code */);

// 3. Find user named "Charlie"
const charlie = users.find(/* your code */);

// 4. Find index of the Keyboard product
const keyboardIndex = products.findIndex(/* your code */);

// 5. Find first number greater than 5
const firstOver5 = numbers.find(/* your code */);
```

### Exercise 4.2: Find with Default
Handle cases where item might not exist:

```javascript
// 1. Find product by ID (with default)
function findProductById(id) {
    const product = products.find(/* your code */);
    return product || { name: 'Product not found', price: 0 };
}

console.log(findProductById(1));  // Laptop
console.log(findProductById(99)); // Default object

// 2. Find user by name, return null if not found
function findUserByName(name) {
    // Your code
}
```

---

## Part 5: reduce

### Exercise 5.1: Basic Reductions
Use `reduce` for aggregations:

```javascript
// 1. Sum all numbers
const sum = numbers.reduce(/* your code */, 0);

// 2. Sum all product prices
const totalPrice = products.reduce(/* your code */, 0);

// 3. Find maximum number
const max = numbers.reduce(/* your code */);

// 4. Find minimum price
const minPrice = products.reduce(/* your code */);

// 5. Count products in stock
const stockCount = products.reduce(/* your code */, 0);

// 6. Calculate average rating
const avgRating = products.reduce(/* your code */) / products.length;
```

### Exercise 5.2: Reduce to Objects
Transform arrays to objects:

```javascript
// 1. Create object mapping product id to name
const idToName = products.reduce((acc, product) => {
    // Your code
    return acc;
}, {});
// Expected: { 1: 'Laptop', 2: 'Headphones', ... }

// 2. Group products by category
const byCategory = products.reduce((acc, product) => {
    // Your code
    return acc;
}, {});
// Expected: { Electronics: [...], Kitchen: [...], Furniture: [...] }

// 3. Count products per category
const categoryCount = products.reduce((acc, product) => {
    // Your code
    return acc;
}, {});
// Expected: { Electronics: 4, Kitchen: 2, Furniture: 2 }

// 4. Create user lookup by id
const userLookup = users.reduce((acc, user) => {
    // Your code
    return acc;
}, {});
// Expected: { 1: { name: 'Alice', ... }, 2: { name: 'Bob', ... }, ... }
```

### Exercise 5.3: Advanced Reduce
More complex reductions:

```javascript
// 1. Calculate total value of in-stock items only
const inStockValue = products.reduce(/* your code */, 0);

// 2. Find the product with highest rating
const bestProduct = products.reduce(/* your code */);

// 3. Create a summary object
const summary = products.reduce((acc, product) => {
    return {
        totalProducts: acc.totalProducts + 1,
        totalValue: acc.totalValue + product.price,
        inStockCount: acc.inStockCount + (product.inStock ? 1 : 0),
        avgRating: /* calculate running average */
    };
}, { totalProducts: 0, totalValue: 0, inStockCount: 0, avgRating: 0 });
```

---

## Part 6: some and every

### Exercise 6.1: Testing Conditions
Use `some` and `every`:

```javascript
// some - returns true if at least one element matches

// 1. Check if any product is out of stock
const hasOutOfStock = products.some(/* your code */);

// 2. Check if any user is from Chicago
const hasChicagoUser = users.some(/* your code */);

// 3. Check if any product costs over $1000
const hasExpensive = products.some(/* your code */);

// every - returns true if all elements match

// 4. Check if all products have rating >= 3.5
const allGoodRating = products.every(/* your code */);

// 5. Check if all numbers are positive
const allPositive = numbers.every(/* your code */);

// 6. Check if all users have names
const allHaveNames = users.every(/* your code */);
```

### Exercise 6.2: Validation Functions
Create validation using some/every:

```javascript
// 1. Check if cart has any item (non-empty)
function isCartValid(cart) {
    return cart.some(item => item.quantity > 0);
}

// 2. Check if all required fields are filled
function isFormComplete(formData) {
    const requiredFields = ['name', 'email', 'phone'];
    return requiredFields.every(field => formData[field] && formData[field].trim() !== '');
}

// Test
console.log(isFormComplete({ name: 'John', email: 'john@example.com', phone: '555-1234' })); // true
console.log(isFormComplete({ name: 'John', email: '', phone: '555-1234' })); // false
```

---

## Part 7: sort

### Exercise 7.1: Basic Sorting
Sort arrays:

```javascript
// 1. Sort products by price (ascending)
const byPriceAsc = [...products].sort(/* your code */);

// 2. Sort products by price (descending)
const byPriceDesc = [...products].sort(/* your code */);

// 3. Sort products alphabetically by name
const byName = [...products].sort(/* your code */);

// 4. Sort users by age
const byAge = [...users].sort(/* your code */);

// 5. Sort numbers in descending order
const numbersDesc = [...numbers].sort(/* your code */);
```

### Exercise 7.2: Complex Sorting
Multi-criteria sorting:

```javascript
// 1. Sort products by category, then by price within category
const sortedProducts = [...products].sort((a, b) => {
    // First compare by category
    // If same category, compare by price
});

// 2. Sort users: active users first, then by name
const sortedUsers = [...users].sort((a, b) => {
    // Active users come first
    // Then sort by name
});

// 3. Sort products by: in stock first, then by rating (desc)
const prioritySorted = [...products].sort(/* your code */);
```

---

## Part 8: Method Chaining

### Exercise 8.1: Combine Methods
Chain multiple array methods:

```javascript
// 1. Get names of in-stock electronics, sorted alphabetically
const result1 = products
    .filter(/* your code */)
    .filter(/* your code */)
    .map(/* your code */)
    .sort(/* your code */);

// 2. Get total price of all in-stock items with rating >= 4
const result2 = products
    .filter(/* your code */)
    .filter(/* your code */)
    .reduce(/* your code */, 0);

// 3. Get top 3 most expensive products (names only)
const result3 = [...products]
    .sort(/* your code */)
    .slice(0, 3)
    .map(/* your code */);

// 4. Get active users from NY, sorted by age, return their names
const result4 = users
    .filter(/* your code */)
    .filter(/* your code */)
    .sort(/* your code */)
    .map(/* your code */);

// 5. Calculate average price of electronics
const result5 = products
    .filter(/* your code */)
    /* calculate average using reduce */;
```

### Exercise 8.2: Real-World Scenarios
Solve practical problems:

```javascript
// 1. E-commerce: Get cart summary
const cart = [
    { productId: 1, quantity: 2 },
    { productId: 3, quantity: 1 },
    { productId: 5, quantity: 3 }
];

const cartSummary = cart
    .map(item => {
        const product = products.find(p => p.id === item.productId);
        return {
            ...item,
            name: product.name,
            unitPrice: product.price,
            total: product.price * item.quantity
        };
    })
    .reduce((summary, item) => ({
        items: [...summary.items, item],
        totalQuantity: summary.totalQuantity + item.quantity,
        totalPrice: summary.totalPrice + item.total
    }), { items: [], totalQuantity: 0, totalPrice: 0 });

// 2. Analytics: Get category statistics
const categoryStats = products.reduce((stats, product) => {
    // For each category, calculate:
    // - count
    // - totalValue
    // - avgRating
    // - inStockPercentage
}, {});

// 3. Dashboard: Get user engagement report
const engagementReport = users
    // Filter to active users
    // Group by city
    // Calculate count and average age per city
```

---

## Part 9: Challenge Exercises

### Challenge 1: Implement Your Own Methods
Recreate array methods without using them:

```javascript
// Implement myMap
function myMap(arr, callback) {
    // Your implementation
}

// Implement myFilter
function myFilter(arr, callback) {
    // Your implementation
}

// Implement myReduce
function myReduce(arr, callback, initialValue) {
    // Your implementation
}

// Test
console.log(myMap([1, 2, 3], x => x * 2)); // [2, 4, 6]
console.log(myFilter([1, 2, 3, 4], x => x > 2)); // [3, 4]
console.log(myReduce([1, 2, 3, 4], (acc, x) => acc + x, 0)); // 10
```

### Challenge 2: Data Pipeline
Create a reusable data pipeline:

```javascript
function createPipeline(...operations) {
    return function(data) {
        return operations.reduce((result, operation) => operation(result), data);
    };
}

// Create operations
const filterInStock = products => products.filter(p => p.inStock);
const sortByPrice = products => [...products].sort((a, b) => a.price - b.price);
const getNames = products => products.map(p => p.name);
const takeFirst = n => items => items.slice(0, n);

// Create pipeline
const getTopCheapestInStock = createPipeline(
    filterInStock,
    sortByPrice,
    takeFirst(3),
    getNames
);

console.log(getTopCheapestInStock(products));
// Expected: ['Blender', 'Headphones', 'Bookshelf'] (3 cheapest in-stock items)
```

### Challenge 3: Complex Aggregation
Build a complete report:

```javascript
function generateProductReport(products) {
    return {
        totalProducts: /* count */,
        totalValue: /* sum of prices */,
        averagePrice: /* average */,
        averageRating: /* average */,

        byCategory: /* { category: { count, totalValue, products: [...] } } */,

        stockStatus: {
            inStock: /* count */,
            outOfStock: /* count */,
            inStockPercentage: /* percentage */
        },

        priceRanges: {
            budget: /* count of items under $100 */,
            midRange: /* count of items $100-$500 */,
            premium: /* count of items over $500 */
        },

        topRated: /* top 3 products by rating */,

        recommendations: /* products with rating >= 4.5 and in stock */
    };
}

console.log(generateProductReport(products));
```

---

## Expected Learning Outcomes

After completing this exercise, you should be able to:
- Use `forEach` for iteration without creating new arrays
- Transform arrays with `map`
- Filter arrays based on conditions
- Find single elements with `find` and `findIndex`
- Aggregate data with `reduce`
- Test conditions with `some` and `every`
- Sort arrays with custom comparators
- Chain multiple array methods effectively
- Choose the right array method for each use case
