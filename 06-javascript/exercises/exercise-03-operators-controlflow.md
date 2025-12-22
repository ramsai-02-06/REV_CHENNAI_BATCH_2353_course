# Exercise: Operators and Control Flow

## Objective
Master JavaScript operators and control flow statements through hands-on practice.

---

## Part 1: Arithmetic Operators

### Exercise 1.1: Basic Calculations
Create a file `calculator.js`:

```javascript
let a = 15;
let b = 4;

// Calculate and log:
// 1. Sum of a and b
// 2. Difference (a - b)
// 3. Product (a * b)
// 4. Division (a / b)
// 5. Remainder (a % b)
// 6. a raised to the power of b

// Bonus: Explain why 15 % 4 = 3
```

### Exercise 1.2: Increment and Decrement
Predict the output before running:

```javascript
let x = 5;

console.log(x++);  // Output: ?
console.log(x);    // Output: ?
console.log(++x);  // Output: ?
console.log(x);    // Output: ?
console.log(x--);  // Output: ?
console.log(--x);  // Output: ?
```

### Exercise 1.3: Compound Assignment
Rewrite using compound operators:

```javascript
let score = 100;

// Rewrite these using +=, -=, *=, /=, %=
score = score + 50;
score = score - 25;
score = score * 2;
score = score / 5;
score = score % 7;
```

---

## Part 2: Comparison Operators

### Exercise 2.1: Equality Challenge
Predict `true` or `false` for each comparison:

```javascript
// Loose equality (==)
console.log(5 == '5');
console.log(0 == false);
console.log(null == undefined);
console.log('' == false);
console.log([] == false);
console.log([] == 0);

// Strict equality (===)
console.log(5 === '5');
console.log(0 === false);
console.log(null === undefined);
console.log(NaN === NaN);

// Write an explanation for each result
```

### Exercise 2.2: Comparison Practice
Write expressions that evaluate to `true`:

```javascript
let age = 25;
let name = "John";
let isStudent = true;
let score = 85.5;

// Write comparisons using: <, >, <=, >=, ===, !==
// Example: age >= 18 evaluates to true

// 1. Check if age is between 20 and 30 (inclusive)
// 2. Check if name is not "Admin"
// 3. Check if score is at least 60
// 4. Check if isStudent is exactly true (not just truthy)
```

---

## Part 3: Logical Operators

### Exercise 3.1: Logical Evaluation
Predict the output:

```javascript
console.log(true && true);
console.log(true && false);
console.log(false || true);
console.log(!true);
console.log(!(5 > 3));

// Short-circuit evaluation
console.log(0 && "Hello");
console.log(1 && "Hello");
console.log(0 || "Default");
console.log("Value" || "Default");

// What value is returned by each short-circuit expression?
```

### Exercise 3.2: Access Control
Complete the function:

```javascript
function canAccess(user) {
    // Return true if:
    // - user is logged in (isLoggedIn: true) AND
    // - user is either an admin (role: 'admin') OR has permission (hasPermission: true)

    // Your code here
}

// Test cases
let user1 = { isLoggedIn: true, role: 'admin', hasPermission: false };
let user2 = { isLoggedIn: true, role: 'user', hasPermission: true };
let user3 = { isLoggedIn: false, role: 'admin', hasPermission: true };
let user4 = { isLoggedIn: true, role: 'user', hasPermission: false };

console.log(canAccess(user1)); // true
console.log(canAccess(user2)); // true
console.log(canAccess(user3)); // false
console.log(canAccess(user4)); // false
```

### Exercise 3.3: Nullish Coalescing vs OR
Explain the difference in outputs:

```javascript
let value1 = 0;
let value2 = '';
let value3 = null;
let value4 = undefined;

console.log(value1 || 'default');
console.log(value1 ?? 'default');

console.log(value2 || 'default');
console.log(value2 ?? 'default');

console.log(value3 || 'default');
console.log(value3 ?? 'default');

// When would you use ?? instead of ||?
```

---

## Part 4: Ternary and Optional Chaining

### Exercise 4.1: Ternary Operator
Convert if-else to ternary:

```javascript
// Convert each if-else to a ternary expression

// 1.
let age = 20;
let status;
if (age >= 18) {
    status = 'adult';
} else {
    status = 'minor';
}

// 2.
let score = 75;
let result;
if (score >= 60) {
    result = 'pass';
} else {
    result = 'fail';
}

// 3. (Nested ternary)
let marks = 85;
let grade;
if (marks >= 90) {
    grade = 'A';
} else if (marks >= 80) {
    grade = 'B';
} else if (marks >= 70) {
    grade = 'C';
} else {
    grade = 'F';
}
```

### Exercise 4.2: Optional Chaining
Safely access nested properties:

```javascript
let company = {
    name: 'TechCorp',
    address: {
        city: 'New York',
        zip: '10001'
    },
    employees: [
        { name: 'John', department: 'Engineering' },
        { name: 'Jane', department: 'Marketing' }
    ]
};

// Use optional chaining to safely access:
// 1. company.address.street (doesn't exist)
// 2. company.ceo.name (doesn't exist)
// 3. First employee's name
// 4. Third employee's name (doesn't exist)
// 5. company.getInfo() (method doesn't exist)
```

---

## Part 5: Control Flow - Conditionals

### Exercise 5.1: Grade Calculator
Create a function:

```javascript
function getLetterGrade(score) {
    // Return:
    // 'A' for 90-100
    // 'B' for 80-89
    // 'C' for 70-79
    // 'D' for 60-69
    // 'F' for below 60
    // 'Invalid' for scores outside 0-100
}

// Test
console.log(getLetterGrade(95));  // 'A'
console.log(getLetterGrade(83));  // 'B'
console.log(getLetterGrade(72));  // 'C'
console.log(getLetterGrade(65));  // 'D'
console.log(getLetterGrade(45));  // 'F'
console.log(getLetterGrade(105)); // 'Invalid'
console.log(getLetterGrade(-5));  // 'Invalid'
```

### Exercise 5.2: Day of Week (Switch)
Create using switch statement:

```javascript
function getDayType(dayNumber) {
    // dayNumber: 1 = Monday, 7 = Sunday
    // Return:
    // 'Weekday' for Monday-Friday
    // 'Weekend' for Saturday-Sunday
    // 'Invalid' for invalid numbers
}

// Test
console.log(getDayType(1)); // 'Weekday'
console.log(getDayType(5)); // 'Weekday'
console.log(getDayType(6)); // 'Weekend'
console.log(getDayType(7)); // 'Weekend'
console.log(getDayType(0)); // 'Invalid'
```

### Exercise 5.3: Leap Year
Determine if a year is a leap year:

```javascript
function isLeapYear(year) {
    // A year is a leap year if:
    // - Divisible by 4 AND
    // - (NOT divisible by 100 OR divisible by 400)
}

// Test
console.log(isLeapYear(2024)); // true
console.log(isLeapYear(2023)); // false
console.log(isLeapYear(2000)); // true
console.log(isLeapYear(1900)); // false
```

---

## Part 6: Control Flow - Loops

### Exercise 6.1: Loop Patterns
Print the following patterns:

```javascript
// Pattern 1: Print numbers 1-10
// Expected: 1 2 3 4 5 6 7 8 9 10

// Pattern 2: Print even numbers from 2-20
// Expected: 2 4 6 8 10 12 14 16 18 20

// Pattern 3: Print countdown from 10 to 1
// Expected: 10 9 8 7 6 5 4 3 2 1

// Pattern 4: Print multiplication table of 5 (1-10)
// Expected:
// 5 x 1 = 5
// 5 x 2 = 10
// ... etc
```

### Exercise 6.2: FizzBuzz
Classic programming challenge:

```javascript
function fizzBuzz(n) {
    // Print numbers from 1 to n
    // For multiples of 3, print "Fizz"
    // For multiples of 5, print "Buzz"
    // For multiples of both 3 and 5, print "FizzBuzz"
}

fizzBuzz(15);
// Expected output:
// 1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz
```

### Exercise 6.3: Array Iteration
Use different loop types:

```javascript
let fruits = ['apple', 'banana', 'cherry', 'date', 'elderberry'];

// 1. Use a traditional for loop to print each fruit with its index
// 2. Use a for...of loop to print each fruit
// 3. Use a while loop to print fruits in reverse order
// 4. Use for...in to print each index
```

### Exercise 6.4: Break and Continue
Complete the exercises:

```javascript
// 1. Find the first number divisible by 7 between 1-100
// Use break when found

// 2. Print all numbers 1-20, but skip multiples of 3
// Use continue

// 3. Nested loop: Print a 5x5 grid of asterisks
// Stop entirely (break outer loop) when row 3, column 3 is reached
```

### Exercise 6.5: Sum and Average
Calculate statistics:

```javascript
let numbers = [23, 45, 67, 12, 89, 34, 56, 78, 90, 11];

// 1. Calculate the sum of all numbers
// 2. Calculate the average
// 3. Find the maximum value
// 4. Find the minimum value
// 5. Count how many numbers are above the average
```

---

## Part 7: Challenge Exercises

### Challenge 1: Number Pyramid
Print a number pyramid:

```javascript
function printPyramid(rows) {
    // For rows = 5, output:
    //     1
    //    121
    //   12321
    //  1234321
    // 123454321
}
```

### Challenge 2: Prime Numbers
Find all prime numbers:

```javascript
function findPrimes(limit) {
    // Return an array of all prime numbers from 2 to limit
}

console.log(findPrimes(30));
// [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
```

### Challenge 3: Validate Password
Create a password validator:

```javascript
function validatePassword(password) {
    // Return an object with:
    // - isValid: boolean
    // - errors: array of error messages

    // Rules:
    // - At least 8 characters
    // - Contains at least one uppercase letter
    // - Contains at least one lowercase letter
    // - Contains at least one number
    // - Contains at least one special character (!@#$%^&*)
}

console.log(validatePassword('Abc123!@'));
// { isValid: true, errors: [] }

console.log(validatePassword('abc'));
// { isValid: false, errors: ['Too short', 'Missing uppercase', 'Missing number', 'Missing special character'] }
```

---

## Expected Learning Outcomes

After completing this exercise, you should be able to:
- Use arithmetic, comparison, and logical operators effectively
- Understand loose vs strict equality
- Apply short-circuit evaluation
- Use ternary operator and optional chaining
- Write conditional statements (if-else, switch)
- Implement various loop patterns (for, while, do-while, for...of, for...in)
- Use break and continue appropriately
