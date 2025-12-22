# Exercise: Variables and Data Types

## Objective
Practice declaring variables and working with JavaScript's primitive and reference data types.

---

## Part 1: Variable Declaration (var, let, const)

### Exercise 1.1: Basic Declarations
Create a file `variables.js` and complete the following:

```javascript
// 1. Declare a constant for PI with value 3.14159
// Your code here

// 2. Declare a variable for a user's age that can be reassigned
// Your code here

// 3. Declare a variable for a username without assigning a value
// Your code here

// 4. Try to reassign PI to 3.14 - what happens?
// Your code here (comment the result)
```

### Exercise 1.2: Block Scope vs Function Scope
Predict the output, then run to verify:

```javascript
function scopeTest() {
    var x = 1;
    let y = 2;
    const z = 3;

    if (true) {
        var x = 10;
        let y = 20;
        const z = 30;
        console.log('Inside block:', x, y, z);
    }

    console.log('Outside block:', x, y, z);
}

scopeTest();
```

**Questions:**
1. What values are logged inside the block?
2. What values are logged outside the block?
3. Why is `x` different from `y` and `z` outside the block?

---

## Part 2: Primitive Data Types

### Exercise 2.1: Type Identification
Create a file `types.js`. For each variable, predict the type using `typeof`, then verify:

```javascript
let a = 42;
let b = 'Hello';
let c = true;
let d = null;
let e = undefined;
let f = Symbol('id');
let g = 9007199254740991n;

// For each variable, log: console.log(typeof variableName);
// Write down what you expected vs what you got
```

**Question:** Why does `typeof null` return "object"?

### Exercise 2.2: Type Conversion Challenge
Complete each conversion:

```javascript
// String to Number
let str1 = "123";
let num1 = // Convert str1 to number (3 different ways)

let str2 = "45.67";
let num2 = // Convert str2 to float

let str3 = "abc";
let num3 = // Convert str3 to number - what happens?

// Number to String
let age = 25;
let ageStr = // Convert age to string (2 different ways)

// To Boolean
let val1 = 0;
let val2 = "";
let val3 = "hello";
let val4 = null;

// Convert each to boolean and predict true/false
```

### Exercise 2.3: Truthy and Falsy Values
Create a function that tests if a value is truthy or falsy:

```javascript
function checkTruthy(value) {
    // Return "truthy" if the value is truthy
    // Return "falsy" if the value is falsy
}

// Test with these values and record results:
console.log(checkTruthy(0));
console.log(checkTruthy(""));
console.log(checkTruthy("0"));
console.log(checkTruthy([]));
console.log(checkTruthy({}));
console.log(checkTruthy(null));
console.log(checkTruthy(undefined));
console.log(checkTruthy(NaN));
console.log(checkTruthy(-1));
console.log(checkTruthy(" "));
```

---

## Part 3: Reference Data Types

### Exercise 3.1: Objects
Create and manipulate objects:

```javascript
// 1. Create a person object with name, age, and city properties
let person = // Your code

// 2. Add an email property to the person object
// Your code

// 3. Update the age to 31
// Your code

// 4. Delete the city property
// Your code

// 5. Check if 'email' property exists in person
// Your code

// 6. Get all keys of the person object
// Your code

// 7. Get all values of the person object
// Your code
```

### Exercise 3.2: Arrays
Work with arrays:

```javascript
// 1. Create an array of 5 fruits
let fruits = // Your code

// 2. Access the third fruit
// Your code

// 3. Change the first fruit to "mango"
// Your code

// 4. Add "grape" to the end
// Your code

// 5. Remove the last fruit
// Your code

// 6. Add "kiwi" to the beginning
// Your code

// 7. Find the index of "banana" (if it exists)
// Your code

// 8. Check if fruits is an array (2 ways)
// Your code
```

### Exercise 3.3: Reference vs Value
Predict the output:

```javascript
// Primitive
let a = 10;
let b = a;
a = 20;
console.log('a:', a);
console.log('b:', b);

// Reference
let obj1 = { name: 'John' };
let obj2 = obj1;
obj1.name = 'Jane';
console.log('obj1.name:', obj1.name);
console.log('obj2.name:', obj2.name);

// Array
let arr1 = [1, 2, 3];
let arr2 = arr1;
arr1.push(4);
console.log('arr1:', arr1);
console.log('arr2:', arr2);
```

**Questions:**
1. Why does changing `a` not affect `b`?
2. Why does changing `obj1.name` also change `obj2.name`?
3. How would you create a true copy of an object?

---

## Part 4: Challenge Exercises

### Challenge 1: Variable Swap
Swap two variables without using a third variable:

```javascript
let x = 5;
let y = 10;

// Swap x and y without creating a new variable
// Your code here

console.log(x); // Should be 10
console.log(y); // Should be 5
```

### Challenge 2: Type Checker Function
Create a function that returns the actual type of any value:

```javascript
function getType(value) {
    // Return:
    // - "null" for null
    // - "array" for arrays
    // - "object" for plain objects
    // - "function" for functions
    // - The typeof result for primitives
}

// Tests
console.log(getType(null));        // "null"
console.log(getType([1, 2, 3]));   // "array"
console.log(getType({ a: 1 }));    // "object"
console.log(getType(() => {}));    // "function"
console.log(getType(42));          // "number"
console.log(getType("hello"));     // "string"
```

### Challenge 3: Deep Clone
Create a function to deep clone an object:

```javascript
let original = {
    name: 'John',
    address: {
        city: 'New York',
        zip: '10001'
    },
    hobbies: ['reading', 'gaming']
};

function deepClone(obj) {
    // Your code here
}

let cloned = deepClone(original);
cloned.address.city = 'Boston';
cloned.hobbies.push('swimming');

console.log(original.address.city);  // Should still be 'New York'
console.log(original.hobbies);       // Should still be ['reading', 'gaming']
```

---

## Expected Learning Outcomes

After completing this exercise, you should be able to:
- Explain the differences between `var`, `let`, and `const`
- Identify JavaScript's primitive and reference types
- Convert between different data types
- Understand truthy and falsy values
- Work with objects and arrays
- Explain the difference between value and reference
