# Exercise: Functions

## Objective
Practice writing functions using different declaration styles and understand parameters, return values, and function behavior.

---

## Part 1: Function Declarations

### Exercise 1.1: Basic Functions
Create a file `functions.js`:

```javascript
// 1. Write a function that takes a name and returns a greeting
function greet(name) {
    // Return "Hello, [name]!"
}

// 2. Write a function that calculates the area of a rectangle
function calculateArea(length, width) {
    // Return the area
}

// 3. Write a function that converts Celsius to Fahrenheit
// Formula: (C × 9/5) + 32
function celsiusToFahrenheit(celsius) {
    // Your code
}

// 4. Write a function that checks if a number is even
function isEven(number) {
    // Return true or false
}

// Test your functions
console.log(greet('Alice'));           // "Hello, Alice!"
console.log(calculateArea(5, 3));      // 15
console.log(celsiusToFahrenheit(0));   // 32
console.log(celsiusToFahrenheit(100)); // 212
console.log(isEven(4));                // true
console.log(isEven(7));                // false
```

### Exercise 1.2: Multiple Parameters
Create functions with multiple parameters:

```javascript
// 1. Calculate the volume of a box
function calculateVolume(length, width, height) {
    // Your code
}

// 2. Create a full name from parts
function createFullName(firstName, middleName, lastName) {
    // Return "FirstName MiddleName LastName"
}

// 3. Calculate total price with tax
function calculateTotal(price, quantity, taxRate) {
    // Return total price including tax
}

// Test
console.log(calculateVolume(2, 3, 4));              // 24
console.log(createFullName('John', 'William', 'Doe')); // "John William Doe"
console.log(calculateTotal(10, 3, 0.1));            // 33 (30 + 10% tax)
```

### Exercise 1.3: Default Parameters
Implement functions with default values:

```javascript
// 1. Greet with default name
function greetUser(name = 'Guest') {
    // Your code
}

// 2. Power function with default exponent of 2
function power(base, exponent = 2) {
    // Your code
}

// 3. Create user with defaults
function createUser(username, role = 'user', isActive = true) {
    // Return an object with these properties
}

// Test
console.log(greetUser());          // "Hello, Guest!"
console.log(greetUser('Alice'));   // "Hello, Alice!"
console.log(power(3));             // 9
console.log(power(3, 3));          // 27
console.log(createUser('john'));   // { username: 'john', role: 'user', isActive: true }
console.log(createUser('admin', 'admin', true)); // { username: 'admin', role: 'admin', isActive: true }
```

---

## Part 2: Function Expressions

### Exercise 2.1: Anonymous Functions
Convert declarations to expressions:

```javascript
// Convert these function declarations to function expressions

// Declaration:
function add(a, b) {
    return a + b;
}

// Expression:
const add = // Your code

// Declaration:
function multiply(a, b) {
    return a * b;
}

// Expression:
const multiply = // Your code

// Declaration:
function isPositive(num) {
    return num > 0;
}

// Expression:
const isPositive = // Your code
```

### Exercise 2.2: Hoisting Difference
Predict and explain the output:

```javascript
// Scenario 1
console.log(funcDeclaration());
function funcDeclaration() {
    return 'Declaration works!';
}

// Scenario 2
console.log(funcExpression());
const funcExpression = function() {
    return 'Expression works!';
};

// Why does one work and the other doesn't?
```

---

## Part 3: Arrow Functions

### Exercise 3.1: Convert to Arrow Functions
Rewrite as arrow functions:

```javascript
// 1. Traditional
const double = function(n) {
    return n * 2;
};
// Arrow:

// 2. Traditional
const greet = function(name) {
    return 'Hello, ' + name;
};
// Arrow:

// 3. Traditional
const sum = function(a, b) {
    return a + b;
};
// Arrow:

// 4. Traditional with no parameters
const getTimestamp = function() {
    return Date.now();
};
// Arrow:

// 5. Traditional with object return
const createPair = function(a, b) {
    return { first: a, second: b };
};
// Arrow:
```

### Exercise 3.2: Arrow Function Variations
Write these arrow functions in the shortest form possible:

```javascript
// 1. Square a number
const square = // Your code (implicit return)

// 2. Check if number is negative
const isNegative = // Your code

// 3. Get the length of a string
const getLength = // Your code

// 4. Return a random number
const getRandom = // Your code

// 5. Triple a number and add 10
const tripleAndAdd = // Your code
```

### Exercise 3.3: Multi-line Arrow Functions
Write arrow functions that require curly braces:

```javascript
// 1. Get the max of three numbers
const maxOfThree = (a, b, c) => {
    // Your code - need to compare all three
};

// 2. Describe a person
const describePerson = (name, age, city) => {
    // Return a multi-line description string
    // "[name] is [age] years old and lives in [city]."
};

// 3. Calculate factorial
const factorial = (n) => {
    // Calculate n! using a loop
};

// Test
console.log(maxOfThree(5, 9, 3));     // 9
console.log(describePerson('John', 25, 'NYC')); // "John is 25 years old and lives in NYC."
console.log(factorial(5));            // 120
```

---

## Part 4: Rest Parameters and Spread

### Exercise 4.1: Rest Parameters
Create functions using rest parameters:

```javascript
// 1. Sum any number of arguments
const sumAll = (...numbers) => {
    // Return the sum of all numbers
};

// 2. Find the average
const average = (...numbers) => {
    // Return the average
};

// 3. First element and rest
const firstAndRest = (first, ...rest) => {
    // Return { first: first, rest: rest }
};

// 4. Log with prefix
const logWithPrefix = (prefix, ...messages) => {
    // Log each message with the prefix
    // e.g., "[INFO] message1", "[INFO] message2"
};

// Test
console.log(sumAll(1, 2, 3, 4, 5));   // 15
console.log(average(10, 20, 30));     // 20
console.log(firstAndRest(1, 2, 3, 4)); // { first: 1, rest: [2, 3, 4] }
logWithPrefix('[INFO]', 'Starting', 'Processing', 'Done');
```

---

## Part 5: Return Values

### Exercise 5.1: Early Returns
Refactor using early return pattern:

```javascript
// Refactor this nested function using early returns
function processUser(user) {
    if (user) {
        if (user.isActive) {
            if (user.hasPermission) {
                return 'Access granted';
            } else {
                return 'No permission';
            }
        } else {
            return 'User inactive';
        }
    } else {
        return 'No user provided';
    }
}

// Refactored version:
function processUserRefactored(user) {
    // Use early returns to flatten the code
}
```

### Exercise 5.2: Returning Objects
Create functions that return objects:

```javascript
// 1. Create a point
const createPoint = (x, y) => {
    // Return { x, y }
};

// 2. Parse a full name
const parseName = (fullName) => {
    // Split "John Doe" into { firstName: 'John', lastName: 'Doe' }
};

// 3. Get array stats
const getStats = (numbers) => {
    // Return { min, max, sum, average, count }
};

// Test
console.log(createPoint(5, 10));    // { x: 5, y: 10 }
console.log(parseName('John Doe')); // { firstName: 'John', lastName: 'Doe' }
console.log(getStats([1, 2, 3, 4, 5]));
// { min: 1, max: 5, sum: 15, average: 3, count: 5 }
```

### Exercise 5.3: Returning Functions
Create functions that return functions:

```javascript
// 1. Create a greeter for a specific greeting
const createGreeter = (greeting) => {
    // Return a function that takes a name
};

const sayHello = createGreeter('Hello');
const sayHi = createGreeter('Hi');

console.log(sayHello('John'));  // "Hello, John!"
console.log(sayHi('Jane'));     // "Hi, Jane!"

// 2. Create a multiplier
const createMultiplier = (factor) => {
    // Return a function that multiplies by factor
};

const double = createMultiplier(2);
const triple = createMultiplier(3);

console.log(double(5));   // 10
console.log(triple(5));   // 15

// 3. Create a tax calculator
const createTaxCalculator = (taxRate) => {
    // Return a function that calculates price + tax
};

const calcWithGST = createTaxCalculator(0.18);
const calcWithVAT = createTaxCalculator(0.20);

console.log(calcWithGST(100));  // 118
console.log(calcWithVAT(100));  // 120
```

---

## Part 6: Practical Function Exercises

### Exercise 6.1: String Utilities
Create a string utility module:

```javascript
// 1. Capitalize first letter
const capitalize = (str) => {
    // "hello" -> "Hello"
};

// 2. Reverse a string
const reverse = (str) => {
    // "hello" -> "olleh"
};

// 3. Count vowels
const countVowels = (str) => {
    // "hello" -> 2
};

// 4. Truncate with ellipsis
const truncate = (str, maxLength) => {
    // If str.length > maxLength, truncate and add "..."
    // "Hello World", 8 -> "Hello..."
};

// 5. Convert to camelCase
const toCamelCase = (str) => {
    // "hello world" -> "helloWorld"
    // "Hello World" -> "helloWorld"
};

// Test all functions
console.log(capitalize('hello'));          // "Hello"
console.log(reverse('hello'));             // "olleh"
console.log(countVowels('hello world'));   // 3
console.log(truncate('Hello World', 8));   // "Hello..."
console.log(toCamelCase('hello world'));   // "helloWorld"
```

### Exercise 6.2: Number Utilities
Create number utility functions:

```javascript
// 1. Clamp a number between min and max
const clamp = (num, min, max) => {
    // clamp(15, 0, 10) -> 10
    // clamp(-5, 0, 10) -> 0
    // clamp(5, 0, 10) -> 5
};

// 2. Check if a number is in range
const inRange = (num, min, max) => {
    // Return true if min <= num <= max
};

// 3. Generate random integer in range
const randomInt = (min, max) => {
    // Return random integer between min and max (inclusive)
};

// 4. Round to decimal places
const roundTo = (num, decimals) => {
    // roundTo(3.14159, 2) -> 3.14
};

// 5. Check if number is prime
const isPrime = (num) => {
    // Return true if num is prime
};

// Test
console.log(clamp(15, 0, 10));  // 10
console.log(inRange(5, 0, 10)); // true
console.log(randomInt(1, 10)); // Random number 1-10
console.log(roundTo(3.14159, 2)); // 3.14
console.log(isPrime(17));       // true
console.log(isPrime(4));        // false
```

---

## Part 7: Challenge Exercises

### Challenge 1: Function Composition
Create a compose function:

```javascript
// Create a function that composes multiple functions
const compose = (...functions) => {
    // Return a function that applies all functions right-to-left
};

const addOne = x => x + 1;
const double = x => x * 2;
const square = x => x * x;

const composed = compose(addOne, double, square);
console.log(composed(3)); // addOne(double(square(3))) = addOne(double(9)) = addOne(18) = 19
```

### Challenge 2: Curry Function
Implement currying:

```javascript
// Create a curry function that allows partial application
const curry = (fn) => {
    // Return a curried version of fn
};

const add = (a, b, c) => a + b + c;
const curriedAdd = curry(add);

console.log(curriedAdd(1)(2)(3));    // 6
console.log(curriedAdd(1, 2)(3));    // 6
console.log(curriedAdd(1)(2, 3));    // 6
console.log(curriedAdd(1, 2, 3));    // 6
```

### Challenge 3: Memoization
Create a memoize function:

```javascript
const memoize = (fn) => {
    // Return a memoized version that caches results
};

// Test with an expensive function
const expensiveOperation = (n) => {
    console.log('Computing...');
    let result = 0;
    for (let i = 0; i < n * 1000000; i++) {
        result += i;
    }
    return result;
};

const memoizedOperation = memoize(expensiveOperation);

console.log(memoizedOperation(5)); // "Computing..." then result
console.log(memoizedOperation(5)); // Just result (cached, no "Computing...")
console.log(memoizedOperation(10)); // "Computing..." then result
console.log(memoizedOperation(10)); // Just result (cached)
```

### Challenge 4: Debounce Function
Implement debounce:

```javascript
const debounce = (fn, delay) => {
    // Return a debounced version of fn
    // Only executes after delay ms of inactivity
};

// Usage example (for browser):
// const handleSearch = debounce((query) => {
//     console.log('Searching for:', query);
// }, 300);
```

---

## Expected Learning Outcomes

After completing this exercise, you should be able to:
- Write function declarations, expressions, and arrow functions
- Use default parameters and rest parameters
- Understand function hoisting
- Return values, objects, and functions from functions
- Apply the early return pattern
- Create higher-order functions
- Understand function composition and currying concepts
