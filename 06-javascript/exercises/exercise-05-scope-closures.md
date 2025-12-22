# Exercise: Scope and Closures

## Objective
Master JavaScript scope rules and understand how closures work through practical examples.

---

## Part 1: Understanding Scope

### Exercise 1.1: Global vs Local Scope
Predict the output for each scenario:

```javascript
// Scenario 1
let message = 'Global';

function showMessage() {
    console.log(message);
}

showMessage(); // Output: ?

// Scenario 2
let greeting = 'Hello';

function changeGreeting() {
    let greeting = 'Hi';
    console.log(greeting);
}

changeGreeting(); // Output: ?
console.log(greeting); // Output: ?

// Scenario 3
let count = 0;

function increment() {
    count = count + 1;
}

increment();
increment();
console.log(count); // Output: ?
```

### Exercise 1.2: Block Scope
Predict the output:

```javascript
// Scenario 1
let x = 1;

if (true) {
    let x = 2;
    console.log('Inside if:', x);
}

console.log('Outside if:', x);

// Scenario 2
for (let i = 0; i < 3; i++) {
    console.log('Inside loop:', i);
}
// console.log('Outside loop:', i); // What happens?

// Scenario 3
{
    let secret = 'password123';
    const API_KEY = 'abc123';
    var exposed = 'visible';
}

console.log(exposed); // Output: ?
// console.log(secret); // What happens?
```

### Exercise 1.3: var vs let vs const Scope
Predict and explain:

```javascript
// Scenario 1: var in loop
for (var i = 0; i < 3; i++) {
    setTimeout(() => console.log('var:', i), 100);
}

// Scenario 2: let in loop
for (let j = 0; j < 3; j++) {
    setTimeout(() => console.log('let:', j), 100);
}

// Scenario 3: Function scope
function testVar() {
    if (true) {
        var innerVar = 'accessible';
    }
    console.log(innerVar); // Output: ?
}

function testLet() {
    if (true) {
        let innerLet = 'limited';
    }
    // console.log(innerLet); // What happens?
}
```

---

## Part 2: Lexical Scope

### Exercise 2.1: Nested Functions
Trace the variable access:

```javascript
const globalVar = 'I am global';

function outer() {
    const outerVar = 'I am outer';

    function middle() {
        const middleVar = 'I am middle';

        function inner() {
            const innerVar = 'I am inner';

            // Which variables can inner() access?
            console.log(globalVar);  // ?
            console.log(outerVar);   // ?
            console.log(middleVar);  // ?
            console.log(innerVar);   // ?
        }

        inner();

        // Which variables can middle() access?
        // Can it access innerVar?
    }

    middle();
}

outer();
```

### Exercise 2.2: Scope Chain
Fill in what gets logged:

```javascript
let a = 1;

function first() {
    let b = 2;

    function second() {
        let c = 3;

        function third() {
            let d = 4;
            console.log(a + b + c + d); // Output: ?
        }

        third();
        console.log(a + b + c); // Output: ?
    }

    second();
    console.log(a + b); // Output: ?
}

first();
console.log(a); // Output: ?
```

---

## Part 3: Closures Basics

### Exercise 3.1: Simple Closures
Predict the output:

```javascript
// Closure 1
function createCounter() {
    let count = 0;

    return function() {
        count++;
        return count;
    };
}

const counter1 = createCounter();
const counter2 = createCounter();

console.log(counter1()); // ?
console.log(counter1()); // ?
console.log(counter2()); // ?
console.log(counter1()); // ?

// Closure 2
function greetMaker(greeting) {
    return function(name) {
        return `${greeting}, ${name}!`;
    };
}

const sayHello = greetMaker('Hello');
const sayHi = greetMaker('Hi');

console.log(sayHello('Alice')); // ?
console.log(sayHi('Bob'));      // ?
console.log(sayHello('Charlie')); // ?
```

### Exercise 3.2: Create Your Own Closures
Implement these closure-based functions:

```javascript
// 1. Create a function that returns a counter with increment, decrement, and getValue
function createAdvancedCounter(initialValue = 0) {
    // Your code here
    // Return an object with increment(), decrement(), getValue(), reset()
}

const counter = createAdvancedCounter(10);
console.log(counter.getValue()); // 10
counter.increment();
counter.increment();
console.log(counter.getValue()); // 12
counter.decrement();
console.log(counter.getValue()); // 11
counter.reset();
console.log(counter.getValue()); // 10

// 2. Create a function that tracks how many times it's been called
function createCallTracker(fn) {
    // Wrap fn and track call count
    // The returned function should also have a getCallCount() method
}

const trackedAdd = createCallTracker((a, b) => a + b);
console.log(trackedAdd(1, 2));        // 3
console.log(trackedAdd(3, 4));        // 7
console.log(trackedAdd.getCallCount()); // 2

// 3. Create a function that remembers its previous result
function createRememberingFunction(fn) {
    // Return a function that also stores previousResult
}

const rememberingDouble = createRememberingFunction(x => x * 2);
console.log(rememberingDouble(5));            // 10
console.log(rememberingDouble.previousResult); // undefined (first call)
console.log(rememberingDouble(7));            // 14
console.log(rememberingDouble.previousResult); // 10
```

---

## Part 4: Practical Closure Patterns

### Exercise 4.1: Private Variables (Module Pattern)
Create a bank account with private balance:

```javascript
function createBankAccount(initialBalance) {
    // Private variable
    // Return object with:
    // - deposit(amount): adds to balance, returns new balance
    // - withdraw(amount): subtracts from balance if sufficient funds, returns new balance or error message
    // - getBalance(): returns current balance
    // - getTransactionHistory(): returns array of transactions
}

const account = createBankAccount(1000);
console.log(account.getBalance());      // 1000
console.log(account.deposit(500));      // 1500
console.log(account.withdraw(200));     // 1300
console.log(account.withdraw(2000));    // "Insufficient funds"
console.log(account.getBalance());      // 1300
console.log(account.getTransactionHistory());
// [{ type: 'deposit', amount: 500 }, { type: 'withdraw', amount: 200 }]
console.log(account.balance);           // undefined (private)
```

### Exercise 4.2: Function Factory
Create factories using closures:

```javascript
// 1. Create a discount calculator factory
function createDiscountCalculator(discountPercent) {
    // Return a function that calculates discounted price
}

const tenPercentOff = createDiscountCalculator(10);
const twentyPercentOff = createDiscountCalculator(20);

console.log(tenPercentOff(100));    // 90
console.log(twentyPercentOff(100)); // 80

// 2. Create a range checker factory
function createRangeChecker(min, max) {
    // Return a function that checks if a number is in range
}

const isValidAge = createRangeChecker(0, 120);
const isValidScore = createRangeChecker(0, 100);

console.log(isValidAge(25));    // true
console.log(isValidAge(150));   // false
console.log(isValidScore(85));  // true
console.log(isValidScore(105)); // false

// 3. Create a prefix logger factory
function createLogger(prefix) {
    // Return a function that logs messages with the prefix
    // Also track log count
}

const infoLogger = createLogger('[INFO]');
const errorLogger = createLogger('[ERROR]');

infoLogger('Server started');     // "[INFO] Server started"
infoLogger('Processing request'); // "[INFO] Processing request"
errorLogger('Connection failed'); // "[ERROR] Connection failed"
console.log(infoLogger.getLogCount());  // 2
console.log(errorLogger.getLogCount()); // 1
```

### Exercise 4.3: Event Handler Closures
Create event handlers that remember context:

```javascript
// Simulate creating button handlers
function createButtonHandler(buttonId, action) {
    let clickCount = 0;

    return function handleClick() {
        clickCount++;
        console.log(`Button ${buttonId} clicked ${clickCount} times. Action: ${action}`);
    };
}

const saveHandler = createButtonHandler('save-btn', 'Saving document');
const deleteHandler = createButtonHandler('delete-btn', 'Deleting item');

saveHandler(); // "Button save-btn clicked 1 times. Action: Saving document"
saveHandler(); // "Button save-btn clicked 2 times. Action: Saving document"
deleteHandler(); // "Button delete-btn clicked 1 times. Action: Deleting item"
saveHandler(); // "Button save-btn clicked 3 times. Action: Saving document"
```

---

## Part 5: Common Closure Pitfalls

### Exercise 5.1: The Classic Loop Problem
Fix the loop closure issue:

```javascript
// Problem: All logs show 3
function problemLoop() {
    for (var i = 0; i < 3; i++) {
        setTimeout(function() {
            console.log(i);
        }, i * 1000);
    }
}

// Fix 1: Using let
function fixWithLet() {
    // Your solution
}

// Fix 2: Using IIFE
function fixWithIIFE() {
    // Your solution using var but with IIFE
}

// Fix 3: Using a closure helper
function fixWithClosure() {
    function createLogger(index) {
        // Your solution
    }

    for (var i = 0; i < 3; i++) {
        setTimeout(createLogger(i), i * 1000);
    }
}
```

### Exercise 5.2: Preserving this
Understand `this` in closures:

```javascript
const person = {
    name: 'John',
    hobbies: ['reading', 'gaming', 'coding'],

    // Problem: this is undefined in the callback
    showHobbiesProblem: function() {
        this.hobbies.forEach(function(hobby) {
            console.log(this.name + ' likes ' + hobby);
        });
    },

    // Fix 1: Arrow function
    showHobbiesArrow: function() {
        // Your solution
    },

    // Fix 2: Bind this
    showHobbiesBind: function() {
        // Your solution
    },

    // Fix 3: Store this in variable
    showHobbiesVar: function() {
        // Your solution using const self = this
    }
};

// Test each method
```

---

## Part 6: IIFE (Immediately Invoked Function Expression)

### Exercise 6.1: Basic IIFE
Convert to IIFE:

```javascript
// Convert these to IIFEs that execute immediately

// 1. Simple log
function sayHello() {
    console.log('Hello!');
}
sayHello();

// As IIFE:

// 2. Return a value
function getConfig() {
    return { debug: true, version: '1.0' };
}
const config = getConfig();

// As IIFE:

// 3. With parameters
function greet(name) {
    console.log('Hello, ' + name);
}
greet('World');

// As IIFE:
```

### Exercise 6.2: IIFE Module Pattern
Create a module using IIFE:

```javascript
const Calculator = (function() {
    // Private variables
    let result = 0;
    let history = [];

    // Private function
    function addToHistory(operation) {
        history.push(operation);
    }

    // Public API
    return {
        add: function(n) {
            // Your code
        },
        subtract: function(n) {
            // Your code
        },
        multiply: function(n) {
            // Your code
        },
        divide: function(n) {
            // Your code
        },
        getResult: function() {
            // Your code
        },
        getHistory: function() {
            // Your code
        },
        clear: function() {
            // Your code
        }
    };
})();

// Test
Calculator.add(10);
Calculator.multiply(2);
Calculator.subtract(5);
console.log(Calculator.getResult()); // 15
console.log(Calculator.getHistory()); // ["add 10", "multiply 2", "subtract 5"]
Calculator.clear();
console.log(Calculator.getResult()); // 0
```

---

## Part 7: Challenge Exercises

### Challenge 1: Create a Once Function
A function that can only be called once:

```javascript
function once(fn) {
    // Return a function that can only execute fn once
    // Subsequent calls return the first result
}

const initialize = once(() => {
    console.log('Initializing...');
    return 'Initialized';
});

console.log(initialize()); // "Initializing..." then "Initialized"
console.log(initialize()); // Just "Initialized" (no log, cached result)
console.log(initialize()); // Just "Initialized"
```

### Challenge 2: Create a Rate Limiter
Limit function calls:

```javascript
function createRateLimiter(fn, limit, interval) {
    // Return a function that can only be called 'limit' times per 'interval' ms
    // Returns false if rate limit exceeded
}

const limitedFetch = createRateLimiter(
    (url) => console.log('Fetching:', url),
    3,  // 3 calls
    1000 // per second
);

console.log(limitedFetch('/api/1')); // "Fetching: /api/1", true
console.log(limitedFetch('/api/2')); // "Fetching: /api/2", true
console.log(limitedFetch('/api/3')); // "Fetching: /api/3", true
console.log(limitedFetch('/api/4')); // false (rate limited)

// After 1 second, calls work again
```

### Challenge 3: Private Class-like Structure
Create a "class" with private members using closures:

```javascript
function createPerson(name, age) {
    // Private variables and validation

    return {
        getName: function() { /* ... */ },
        getAge: function() { /* ... */ },
        setName: function(newName) { /* validate and set */ },
        setAge: function(newAge) { /* validate: must be 0-150 */ },
        birthday: function() { /* increment age */ },
        introduce: function() { /* return introduction string */ }
    };
}

const person = createPerson('John', 25);
console.log(person.getName());     // "John"
console.log(person.getAge());      // 25
person.birthday();
console.log(person.getAge());      // 26
person.setAge(200);                // Should fail validation
console.log(person.getAge());      // Still 26
person.setAge(30);
console.log(person.getAge());      // 30
console.log(person.introduce());   // "Hi, I'm John and I'm 30 years old."
console.log(person.name);          // undefined (private)
console.log(person.age);           // undefined (private)
```

### Challenge 4: Memoization with Closure
Create a memoization function:

```javascript
function memoize(fn) {
    // Create a cache using closure
    // Return cached result if same arguments were used before
}

let callCount = 0;
const expensiveSquare = memoize((n) => {
    callCount++;
    console.log('Computing square of', n);
    return n * n;
});

console.log(expensiveSquare(5)); // "Computing..." then 25
console.log(expensiveSquare(5)); // Just 25 (cached)
console.log(expensiveSquare(10)); // "Computing..." then 100
console.log(expensiveSquare(5)); // Just 25 (cached)
console.log('Function called', callCount, 'times'); // 2 times
```

---

## Expected Learning Outcomes

After completing this exercise, you should be able to:
- Explain the difference between global, function, and block scope
- Understand how lexical scoping works
- Create and use closures effectively
- Implement the module pattern for data privacy
- Avoid common closure pitfalls (loop issues, `this` binding)
- Use IIFEs to create private scope
- Apply closures in practical patterns like memoization and rate limiting
