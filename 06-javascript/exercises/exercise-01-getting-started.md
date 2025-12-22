# Exercise: Getting Started with JavaScript

## Objective
Set up your JavaScript development environment and write your first JavaScript programs.

---

## Part 1: Setting Up

### Exercise 1.1: Create Your First HTML with JavaScript
Create a file called `hello.html`:

```html
<!DOCTYPE html>
<html>
<head>
    <title>My First JavaScript</title>
</head>
<body>
    <h1>JavaScript Practice</h1>

    <script>
        // Your JavaScript code goes here
        console.log('Hello, World!');
    </script>
</body>
</html>
```

**Tasks:**
1. Open this file in a web browser
2. Open the browser's Developer Tools (F12 or Right-click > Inspect)
3. Go to the Console tab
4. Verify you see "Hello, World!" in the console

### Exercise 1.2: External JavaScript File
Create two files:

**index.html:**
```html
<!DOCTYPE html>
<html>
<head>
    <title>External JS</title>
</head>
<body>
    <h1>External JavaScript</h1>
    <script src="script.js"></script>
</body>
</html>
```

**script.js:**
```javascript
console.log('Hello from external file!');
```

**Tasks:**
1. Verify the message appears in the console
2. Why is the script tag placed at the end of the body?

---

## Part 2: Console Methods

### Exercise 2.1: Different Console Methods
In your `script.js`, practice different console methods:

```javascript
// 1. Regular log
console.log('This is a log message');

// 2. Warning
console.warn('This is a warning');

// 3. Error
console.error('This is an error');

// 4. Info
console.info('This is info');

// 5. Log multiple values
console.log('Name:', 'John', 'Age:', 25);

// 6. Log an object
console.log({ name: 'John', age: 25 });

// 7. Table format
console.table([
    { name: 'John', age: 25 },
    { name: 'Jane', age: 30 }
]);
```

**Tasks:**
1. Run each console method and observe the differences in the console
2. Notice how different types of messages are styled

### Exercise 2.2: String Formatting
Practice string formatting in console:

```javascript
// Template literal
let name = 'Alice';
let age = 28;
console.log(`${name} is ${age} years old`);

// String concatenation
console.log(name + ' is ' + age + ' years old');

// Format specifiers
console.log('Name: %s, Age: %d', name, age);
```

---

## Part 3: Basic Input and Output

### Exercise 3.1: Alert, Prompt, and Confirm
Try these browser dialog methods:

```javascript
// Alert - displays a message
alert('Welcome to JavaScript!');

// Prompt - gets input from user
let userName = prompt('What is your name?');
console.log('User entered:', userName);

// Confirm - yes/no question
let isConfirmed = confirm('Do you want to continue?');
console.log('User confirmed:', isConfirmed);
```

**Tasks:**
1. Run each dialog and observe the behavior
2. What happens if the user clicks Cancel on prompt?
3. What type of value does confirm return?

### Exercise 3.2: Interactive Greeting
Create an interactive greeting:

```javascript
// 1. Ask for the user's name
let name = prompt('What is your name?');

// 2. Ask for their favorite color
let color = prompt('What is your favorite color?');

// 3. Display a personalized greeting using alert
// Hint: Use template literals

// 4. Log the info to console as well
```

---

## Part 4: Comments and Code Organization

### Exercise 4.1: Writing Comments
Practice different comment styles:

```javascript
// This is a single-line comment

/*
   This is a
   multi-line comment
*/

/**
 * This is a documentation comment (JSDoc style)
 * @param {string} name - The user's name
 * @returns {string} A greeting message
 */
function greet(name) {
    return 'Hello, ' + name;
}

// TODO: Add feature to remember user's name
// FIXME: Handle empty name input
```

### Exercise 4.2: Organize Your Code
Create a well-organized script:

```javascript
// ===========================
// Configuration
// ===========================

const APP_NAME = 'My JavaScript App';
const VERSION = '1.0.0';

// ===========================
// Helper Functions
// ===========================

// Add your helper functions here

// ===========================
// Main Code
// ===========================

console.log(`${APP_NAME} v${VERSION}`);

// Your main code here
```

---

## Part 5: Basic Math and Strings

### Exercise 5.1: Simple Calculator
Create a simple calculator:

```javascript
// 1. Ask for two numbers
let num1 = prompt('Enter first number:');
let num2 = prompt('Enter second number:');

// 2. Convert to numbers (prompt returns strings!)
num1 = Number(num1);
num2 = Number(num2);

// 3. Perform calculations
let sum = num1 + num2;
let difference = num1 - num2;
let product = num1 * num2;
let quotient = num1 / num2;

// 4. Display results
console.log('Sum:', sum);
console.log('Difference:', difference);
console.log('Product:', product);
console.log('Quotient:', quotient);
```

**Tasks:**
1. Run the calculator
2. What happens if you don't convert to numbers?
3. What happens if you divide by zero?

### Exercise 5.2: String Operations
Practice basic string operations:

```javascript
let message = 'Hello, JavaScript!';

// 1. Get the length
console.log('Length:', message.length);

// 2. Convert to uppercase
console.log('Uppercase:', message.toUpperCase());

// 3. Convert to lowercase
console.log('Lowercase:', message.toLowerCase());

// 4. Get first character
console.log('First char:', message[0]);

// 5. Get last character
console.log('Last char:', message[message.length - 1]);

// 6. Check if it includes a word
console.log('Has "Java":', message.includes('Java'));

// 7. Replace a word
console.log('Replaced:', message.replace('JavaScript', 'World'));
```

---

## Part 6: Your First Program Challenges

### Challenge 1: Mad Libs Game
Create a Mad Libs-style story generator:

```javascript
// Ask the user for:
// - A name
// - An adjective
// - A noun
// - A verb
// - A place

// Create a story using their inputs
// Example output:
// "One day, John found a shiny unicorn that could dance in the kitchen."
```

### Challenge 2: Temperature Converter
Create a temperature converter:

```javascript
// Ask user to choose conversion type:
// 1. Celsius to Fahrenheit
// 2. Fahrenheit to Celsius

// Ask for the temperature value

// Perform conversion and display result
// Formulas:
// F = (C × 9/5) + 32
// C = (F − 32) × 5/9
```

### Challenge 3: Basic Quiz
Create a simple quiz:

```javascript
// Create a 3-question quiz
// Keep track of the score
// Display final score at the end

// Example questions:
// 1. What is 5 + 3?
// 2. What is the capital of France?
// 3. What color is the sky?
```

### Challenge 4: BMI Calculator
Create a BMI calculator:

```javascript
// Ask for weight in kg
// Ask for height in meters
// Calculate BMI: weight / (height * height)
// Display BMI and category:
// - Underweight: < 18.5
// - Normal: 18.5 - 24.9
// - Overweight: 25 - 29.9
// - Obese: >= 30
```

---

## Debugging Tips

### Using console.log for Debugging

```javascript
let x = 5;
let y = 10;

console.log('Before calculation');
console.log('x =', x);
console.log('y =', y);

let result = x + y;

console.log('After calculation');
console.log('result =', result);
```

### Reading Error Messages

Common errors you might see:
- `Uncaught SyntaxError` - You have a typo in your code
- `Uncaught ReferenceError` - You're using a variable that doesn't exist
- `Uncaught TypeError` - You're trying to do something with the wrong type

---

## Expected Learning Outcomes

After completing this exercise, you should be able to:
- Create HTML files with embedded JavaScript
- Use external JavaScript files
- Use console methods for debugging
- Get user input with prompt and confirm
- Display output with alert and console.log
- Write and organize comments in your code
- Perform basic math and string operations
