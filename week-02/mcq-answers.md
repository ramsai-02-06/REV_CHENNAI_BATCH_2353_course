# Week 02 - MCQ Answer Key

This document contains answers and explanations for the Week 02 MCQ.

---


### Question 1

**Correct Answer: A) `const`**


**Explanation:** `const` declares a block-scoped variable that cannot be reassigned after initialization.


**Why other options are incorrect:**
- **B) `let`** - Block-scoped but allows reassignment.
- **C) `var`** - Function-scoped and allows reassignment.
- **D) `static`** - Not a variable declaration keyword in JavaScript.

---


### Question 2

**Correct Answer: B) `container-fluid`**


**Explanation:** `container-fluid` spans the full width of the viewport.


**Why other options are incorrect:**
- **A) `container-wide`** - Not a valid Bootstrap class.
- **C) `container-full`** - Not a valid Bootstrap class.
- **D) `container-100`** - Not a valid Bootstrap class.

---


### Question 3

**Correct Answer: C) The DOM to be fully parsed**


**Explanation:** `$(document).ready()` fires when the DOM structure is complete, but before images and other resources finish loading.


**Why other options are incorrect:**
- **A) All images to load** - That's `window.onload`.
- **B) All external scripts to load** - Not specifically.
- **D) CSS styles to be applied** - Not specifically.

---


### Question 4

**Correct Answer: C) `[]`**


**Explanation:** An empty array `[]` is an object, and all objects are truthy in JavaScript.


**Why other options are incorrect:**
- **A) `0`** - Zero is falsy.
- **B) `""`** - Empty string is falsy.
- **D) `null`** - null is falsy.

---


### Question 5

**Correct Answer: D) container > row > col**


**Explanation:** Bootstrap grid structure requires container wrapping rows, and rows containing columns.


**Why other options are incorrect:**
- All other orders violate the required Bootstrap grid hierarchy.

---


### Question 6

**Correct Answer: C) Value and type**


**Explanation:** The strict equality operator `===` checks both value and type without type coercion.


**Why other options are incorrect:**
- **A) Value only** - That's `==` (loose equality).
- **B) Type only** - Would allow different values of same type.
- **D) Reference only** - Only applies to objects.

---


### Question 7

**Correct Answer: C) `fadeOut()`**


**Explanation:** `fadeOut()` gradually reduces opacity to hide an element with animation.


**Why other options are incorrect:**
- **A) `hide()`** - Can animate but default is instant.
- **B) `remove()`** - Removes from DOM, no animation.
- **D) `display(false)`** - Not a jQuery method.

---


### Question 8

**Correct Answer: B) `md`**


**Explanation:** The `md` breakpoint applies to viewports 768px and wider, typically tablets.


**Why other options are incorrect:**
- **A) `sm`** - Targets 576px and up (large phones).
- **C) `lg`** - Targets 992px and up (desktops).
- **D) `xl`** - Targets 1200px and up (large desktops).

---


### Question 9

**Correct Answer: B) A function with access to its outer scope after the outer function returns**


**Explanation:** Closures allow inner functions to remember and access variables from their outer scope.


**Why other options are incorrect:**
- **A)** - Not related to function completion.
- **C)** - Uses `window.close()`, not closures.
- **D)** - Uses `break`, not closures.

---


### Question 10

**Correct Answer: C) `$(function() {})`**


**Explanation:** Passing a function directly to `$()` is shorthand for `$(document).ready()`.


**Why other options are incorrect:**
- **A)** - Not valid jQuery syntax.
- **B)** - Not the standard shorthand.
- **D)** - Not valid; needs jQuery selector.

---


### Question 11

**Correct Answer: A) `push()`**


**Explanation:** `push()` adds one or more elements to the end of an array and returns the new length.


**Why other options are incorrect:**
- **B) `unshift()`** - Adds to the beginning.
- **C) `append()`** - Not a native array method.
- **D) `add()`** - Not a native array method.

---


### Question 12

**Correct Answer: B) `shadow`**


**Explanation:** Bootstrap provides `shadow`, `shadow-sm`, `shadow-lg`, and `shadow-none` utility classes.


**Why other options are incorrect:**
- All others are not valid Bootstrap utility classes.

---


### Question 13

**Correct Answer: C) Prevents the default browser action**


**Explanation:** `preventDefault()` stops the browser's default action (like form submission or link navigation).


**Why other options are incorrect:**
- **A)** - That's `stopPropagation()`.
- **B)** - Use `removeEventListener()`.
- **D)** - The event still fires; only default is prevented.

---


### Question 14

**Correct Answer: A) `parent()`**


**Explanation:** The `parent()` method returns the direct parent element.


**Why other options are incorrect:**
- **B) `parentElement()`** - Vanilla JS property, not jQuery.
- **C) `up()`** - Not a jQuery method.
- **D) `container()`** - Not a jQuery method.

---


### Question 15

**Correct Answer: C) `text-center`**


**Explanation:** The `text-center` utility class centers inline content.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 16

**Correct Answer: C) "object"**


**Explanation:** This is a historical bug in JavaScript. `typeof null` returns "object" even though null is a primitive.


**Why other options are incorrect:**
- **A) "null"** - Would be logical but isn't the actual result.
- **B) "undefined"** - That's for undefined.
- **D) "boolean"** - null is not boolean.

---


### Question 17

**Correct Answer: D) `html()`**


**Explanation:** The `html()` method gets or sets the HTML content including tags.


**Why other options are incorrect:**
- **A) `content()`** - Not a jQuery method.
- **B) `innerHTML()`** - Vanilla JS property, not jQuery.
- **C) `text()`** - Gets/sets text only, strips HTML.

---


### Question 18

**Correct Answer: A) `d-none d-lg-block`**


**Explanation:** `d-none` hides by default, `d-lg-block` shows on large screens and up.


**Why other options are incorrect:**
- All others are not valid Bootstrap 5 classes.

---


### Question 19

**Correct Answer: B) Combines two arrays into one**


**Explanation:** The spread operator expands array elements, allowing concatenation into a new array.


**Why other options are incorrect:**
- **A)** - Use comparison operators for that.
- **C)** - Creates a new array with copied elements.
- **D)** - Spread doesn't delete anything.

---


### Question 20

**Correct Answer: B) `justify-content-between`**


**Explanation:** `justify-content-between` distributes items with equal space between them.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes (though `gap-*` exists for gaps).

---


### Question 21

**Correct Answer: C) `filter()`**


**Explanation:** `filter()` creates a new array with all elements that pass the provided test function.


**Why other options are incorrect:**
- **A) `find()`** - Returns first matching element, not an array.
- **B) `map()`** - Transforms each element, doesn't filter.
- **D) `reduce()`** - Reduces array to single value.

---


### Question 22

**Correct Answer: C) `$(el).addClass('active')`**


**Explanation:** `addClass()` adds one or more classes to the selected elements.


**Why other options are incorrect:**
- All others are not valid jQuery methods.

---


### Question 23

**Correct Answer: B) Sets gap/gutter between columns**


**Explanation:** `g-*` classes set the gutter (gap) spacing between columns in a row.


**Why other options are incorrect:**
- **A)** - Use `row-cols-3` for that.
- **C)** - Not CSS Grid template.
- **D)** - Not a grouping class.

---


### Question 24

**Correct Answer: B) Attaching event listener to parent for child elements**


**Explanation:** Event delegation uses a single listener on a parent to handle events from its children, leveraging event bubbling.


**Why other options are incorrect:**
- All others don't describe the pattern.

---


### Question 25

**Correct Answer: D) `remove()`**


**Explanation:** `remove()` removes the selected element(s) from the DOM including event handlers and data.


**Why other options are incorrect:**
- **A) `delete()`** - Not a jQuery method.
- **B) `destroy()`** - Not a standard jQuery method.
- **C) `hide()`** - Just hides, doesn't remove from DOM.

---


### Question 26

**Correct Answer: C) `w-100`**


**Explanation:** In Bootstrap 5, `w-100` (width 100%) is used with buttons. Note: `btn-block` was removed in Bootstrap 5.


**Why other options are incorrect:**
- **A)** - Not a Bootstrap class.
- **B)** - Deprecated in Bootstrap 5.
- **D)** - Not a Bootstrap class.

---


### Question 27

**Correct Answer: C) "22"**


**Explanation:** When adding a number and string, JavaScript converts the number to a string and concatenates.


**Why other options are incorrect:**
- **A)** - Would be true if both were numbers.
- **B)** - Close, but it's a string not a number.
- **D)** - NaN occurs with invalid number operations.

---


### Question 28

**Correct Answer: C) The element that triggered the event**


**Explanation:** Inside a jQuery event handler, `this` refers to the DOM element, and `$(this)` wraps it as a jQuery object.


**Why other options are incorrect:**
- All others don't apply in event handler context.

---


### Question 29

**Correct Answer: A) `rounded`**


**Explanation:** `rounded` adds border-radius to all corners. Variations include `rounded-circle`, `rounded-pill`, etc.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 30

**Correct Answer: B) `JSON.parse()`**


**Explanation:** `JSON.parse()` parses a JSON string and returns the corresponding JavaScript object.


**Why other options are incorrect:**
- **A) `JSON.stringify()`** - Does the opposite (object to string).
- **C)** and **D)** - Not real JSON methods.

---


### Question 31

**Correct Answer: B) 12**


**Explanation:** Bootstrap uses a 12-column grid system, allowing flexible layouts.


**Why other options are incorrect:**
- Bootstrap specifically uses 12 columns, not any other number.

---


### Question 32

**Correct Answer: A) true**


**Explanation:** `Array.isArray()` reliably checks if a value is an array, returning true for arrays.


**Why other options are incorrect:**
- An empty array `[]` is still an array, so returns true.

---


### Question 33

**Correct Answer: C) `one()`**


**Explanation:** `one()` attaches a handler that executes at most once per element per event type.


**Why other options are incorrect:**
- All others are not valid jQuery methods for this purpose.

---


### Question 34

**Correct Answer: C) `img-fluid`**


**Explanation:** `img-fluid` applies `max-width: 100%` and `height: auto` for responsive images.


**Why other options are incorrect:**
- **B) `img-responsive`** - Was Bootstrap 3, not Bootstrap 5.
- Others are not valid Bootstrap classes.

---


### Question 35

**Correct Answer: B) To write asynchronous code that looks synchronous**


**Explanation:** `async/await` is syntactic sugar over Promises, making async code easier to read and write.


**Why other options are incorrect:**
- **A)** - Doesn't affect synchronous code performance.
- **C)** - JavaScript is single-threaded.
- **D)** - `await` pauses async function, not all execution.

---


### Question 36

**Correct Answer: A) `$.get(url, callback)`**


**Explanation:** `$.get()` is the shorthand method for making GET requests.


**Why other options are incorrect:**
- All others are not valid jQuery syntax.

---


### Question 37

**Correct Answer: C) `align-items-center`**


**Explanation:** `align-items-center` vertically centers flex items along the cross axis.


**Why other options are incorrect:**
- **B) `align-middle`** - For table cells, not flex.
- Others are not valid Bootstrap classes.

---


### Question 38

**Correct Answer: B) The first element with class "item"**


**Explanation:** `querySelector()` returns the first element matching the CSS selector.


**Why other options are incorrect:**
- **A)** - Use `querySelectorAll()` for all matches.
- **C)** - Returns single element, not array.
- **D)** - Returns first, not last.

---


### Question 39

**Correct Answer: C) `event.stopPropagation()`**


**Explanation:** `stopPropagation()` prevents the event from bubbling up to parent elements.


**Why other options are incorrect:**
- **B) `preventDefault()`** - Prevents default action, not bubbling.
- Others are not valid methods.

---


### Question 40

**Correct Answer: C) `card`**


**Explanation:** `card` is the base class for Bootstrap's card component.


**Why other options are incorrect:**
- **A) `panel`** - Was Bootstrap 3, replaced by cards.
- Others are not valid Bootstrap classes.

---


### Question 41

**Correct Answer: B) Moving declarations to the top of their scope**


**Explanation:** Hoisting is JavaScript's behavior of moving variable and function declarations to the top of their scope during compilation.


**Why other options are incorrect:**
- All others don't describe hoisting behavior.

---


### Question 42

**Correct Answer: B) `find()`**


**Explanation:** `find()` searches all descendants (not just children) for matching elements.


**Why other options are incorrect:**
- **A) `children()`** - Only immediate children.
- Others are not jQuery methods.

---


### Question 43

**Correct Answer: A) Margin-bottom of 1rem**


**Explanation:** `mb-3` adds `margin-bottom: 1rem`. The number 3 corresponds to 1rem in Bootstrap's spacing scale.


**Why other options are incorrect:**
- Bootstrap uses rem units, not px, and 3 = 1rem.

---


### Question 44

**Correct Answer: D) `pop()`**


**Explanation:** `pop()` removes and returns the last element of an array.


**Why other options are incorrect:**
- **A) `remove()`** - Not a native array method.
- **B) `delete()`** - Operator, leaves holes in array.
- **C) `shift()`** - Removes first element.

---


### Question 45

**Correct Answer: B) `btn-primary`**


**Explanation:** `btn-primary` applies the primary color. Note: `btn` base class is also needed but the color class itself is `btn-primary`.


**Why other options are incorrect:**
- **C)** - Both classes are needed but `btn-primary` is the specific color class asked about.
- Others follow wrong naming convention.

---


### Question 46

**Correct Answer: B) false**


**Explanation:** An empty string is a falsy value in JavaScript.


**Why other options are incorrect:**
- Empty string is one of the 8 falsy values.

---


### Question 47

**Correct Answer: A) `css({prop: value, prop: value})`**


**Explanation:** Passing an object to `css()` sets multiple properties at once.


**Why other options are incorrect:**
- All others are not valid jQuery methods.

---


### Question 48

**Correct Answer: C) `fw-bold`**


**Explanation:** `fw-bold` (font-weight-bold) makes text bold in Bootstrap 5.


**Why other options are incorrect:**
- All others are not valid Bootstrap 5 classes.

---


### Question 49

**Correct Answer: C) Array of property names**


**Explanation:** `Object.keys()` returns an array of an object's own enumerable property names.


**Why other options are incorrect:**
- **A)** - Use `Object.values()`.
- **B)** - Use `Object.entries()`.
- **D)** - Use `Object.keys().length`.

---


### Question 50

**Correct Answer: C) `$('#myId')`**


**Explanation:** The `#` prefix selects by ID, following CSS selector syntax.


**Why other options are incorrect:**
- **B)** - `.` selects by class.
- Others are not valid jQuery syntax.

---


### Question 51

**Correct Answer: B) `alert-dismissible`**


**Explanation:** `alert-dismissible` enables the close button functionality on alerts.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 52

**Correct Answer: B) To handle errors gracefully**


**Explanation:** `try...catch` allows you to handle exceptions without crashing the program.


**Why other options are incorrect:**
- All others are not purposes of try...catch.

---


### Question 53

**Correct Answer: A) `click()`**


**Explanation:** Calling `click()` without arguments triggers the click event on the element.


**Why other options are incorrect:**
- All others are not valid jQuery methods.

---


### Question 54

**Correct Answer: A) Sets display: flex**


**Explanation:** `d-flex` applies `display: flex` to the element.


**Why other options are incorrect:**
- All others don't describe `d-flex` behavior.

---


### Question 55

**Correct Answer: C) for...in loop**


**Explanation:** `for...in` iterates over enumerable property names of an object.


**Why other options are incorrect:**
- **D) `for...of`** - Iterates over iterable values (arrays, strings).
- **A)** and **B)** - Require manual property access.

---


### Question 56

**Correct Answer: B) The first matched element**


**Explanation:** `first()` reduces the matched set to the first element.


**Why other options are incorrect:**
- All others don't describe `first()` behavior.

---


### Question 57

**Correct Answer: B) `navbar`**


**Explanation:** `navbar` is the base class for Bootstrap's navigation bar component.


**Why other options are incorrect:**
- **A) `nav`** - For nav component, not full navbar.
- Others are not valid Bootstrap classes.

---


### Question 58

**Correct Answer: C) "undefined"**


**Explanation:** `typeof undefined` correctly returns the string "undefined".


**Why other options are incorrect:**
- All others are not the result of `typeof undefined`.

---


### Question 59

**Correct Answer: C) `slideUp()`**


**Explanation:** `slideUp()` animates the height to hide the element with a sliding motion.


**Why other options are incorrect:**
- **A) `hide()`** - No sliding animation by default.
- Others are not jQuery methods.

---


### Question 60

**Correct Answer: B) `p-3`**


**Explanation:** `p-3` sets padding on all sides. `p` = padding, no side indicator = all sides.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 61

**Correct Answer: B) A new array with transformed elements**


**Explanation:** `map()` creates a new array with the results of calling the provided function on every element.


**Why other options are incorrect:**
- **A)** - `map()` doesn't modify the original.
- **C)** - That's `reduce()`.
- **D)** - Returns an array.

---


### Question 62

**Correct Answer: B) `$.post(url, data)`**


**Explanation:** `$.post()` is the shorthand for making POST requests.


**Why other options are incorrect:**
- **A) `$.get()`** - Makes GET requests.
- Others are not valid jQuery methods.

---


### Question 63

**Correct Answer: C) `justify-content-end`**


**Explanation:** `justify-content-end` aligns flex items to the end of the main axis.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 64

**Correct Answer: B) A tree representation of HTML**


**Explanation:** The Document Object Model (DOM) represents the document as a tree of objects that JavaScript can manipulate.


**Why other options are incorrect:**
- All others don't describe the DOM.

---


### Question 65

**Correct Answer: C) `val()`**


**Explanation:** `val()` gets or sets the value of form elements.


**Why other options are incorrect:**
- **B) `text()`** - Gets text content, not input values.
- Others are not jQuery methods.

---


### Question 66

**Correct Answer: B) Sizes column based on content**


**Explanation:** `col-auto` sizes the column to fit its content width.


**Why other options are incorrect:**
- All others don't describe `col-auto` behavior.

---


### Question 67

**Correct Answer: C) `find()`**


**Explanation:** `find()` returns the first element that satisfies the provided testing function.


**Why other options are incorrect:**
- **A) `filter()`** - Returns all matching elements.
- Others are not array methods.

---


### Question 68

**Correct Answer: C) Removes all child elements**


**Explanation:** `empty()` removes all child nodes from the matched elements.


**Why other options are incorrect:**
- **A)** - Use `.is(':empty')` or check `.html()`.
- Others don't describe `empty()`.

---


### Question 69

**Correct Answer: C) `modal`**


**Explanation:** `modal` is the base class for Bootstrap's modal component.


**Why other options are incorrect:**
- All others are not Bootstrap modal classes.

---


### Question 70

**Correct Answer: B) Not a Number**


**Explanation:** `NaN` represents a value that is Not a Number, resulting from invalid numeric operations.


**Why other options are incorrect:**
- NaN stands for "Not a Number".

---


### Question 71

**Correct Answer: A) `before()`**


**Explanation:** `before()` inserts content before the selected element (as a sibling).


**Why other options are incorrect:**
- **B) `prepend()`** - Inserts inside at the beginning.
- **C) `insertBefore()`** - Works but is reversed (content.insertBefore(target)).

---


### Question 72

**Correct Answer: B) `btn-sm`**


**Explanation:** `btn-sm` creates a smaller button size.


**Why other options are incorrect:**
- **C) `btn-xs`** - Was Bootstrap 3, not in Bootstrap 5.
- Others are not valid Bootstrap classes.

---


### Question 73

**Correct Answer: C) Adds if absent, removes if present**


**Explanation:** `toggle()` adds the class if it doesn't exist, or removes it if it does.


**Why other options are incorrect:**
- **A)** - Use `add()`.
- **B)** - Use `remove()`.
- **D)** - Use `contains()`.

---


### Question 74

**Correct Answer: B) `$('div p')`**


**Explanation:** Space between selectors indicates descendant relationship (all `p` inside `div`).


**Why other options are incorrect:**
- **A)** - Selects all p AND all div.
- **C)** - Only direct children.
- **D)** - Adjacent sibling.

---


### Question 75

**Correct Answer: B) Changes the visual order**


**Explanation:** `order-*` classes change the visual order of flex items without changing the DOM.


**Why other options are incorrect:**
- All others don't describe `order-*` behavior.

---


### Question 76

**Correct Answer: C) They inherit `this` from enclosing scope**


**Explanation:** Arrow functions don't have their own `this`; they inherit it from the surrounding scope.


**Why other options are incorrect:**
- **A)** - They don't have their own `this`.
- **B)** - Cannot use `new` with arrow functions.
- **D)** - Implicit return for single expressions.

---


### Question 77

**Correct Answer: C) Iterates over array elements**


**Explanation:** `$.each()` iterates over arrays or objects, executing the callback for each element.


**Why other options are incorrect:**
- **A)** - Use `$.map()` for that.
- Others don't describe `$.each()`.

---


### Question 78

**Correct Answer: B) `border`**


**Explanation:** `border` adds a border on all sides.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 79

**Correct Answer: A) `...args` in function definition**


**Explanation:** The rest parameter `...args` collects remaining arguments into an array.


**Why other options are incorrect:**
- **B)** - That's spread syntax in function calls.
- Others are not valid JavaScript syntax.

---


### Question 80

**Correct Answer: A) `hasClass()`**


**Explanation:** `hasClass()` returns true if any matched element has the specified class.


**Why other options are incorrect:**
- All others are not jQuery methods.

---


### Question 81

**Correct Answer: B) `table-striped`**


**Explanation:** `table-striped` adds alternating row colors to tables.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 82

**Correct Answer: B) Resolves when all promises resolve**


**Explanation:** `Promise.all()` waits for all promises to resolve, or rejects if any one rejects.


**Why other options are incorrect:**
- **A)** - That's `Promise.any()`.
- **D)** - That's `Promise.race()`.

---


### Question 83

**Correct Answer: C) Directly calling one after another with dots**


**Explanation:** jQuery methods return the jQuery object, allowing chaining: `$(el).addClass('a').removeClass('b')`.


**Why other options are incorrect:**
- All others are not how jQuery chaining works.

---


### Question 84

**Correct Answer: B) `bg-dark`**


**Explanation:** `bg-dark` applies a dark background color.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 85

**Correct Answer: C) `string`**


**Explanation:** Template literals use backticks `` ` `` and allow embedded expressions with `${}`.


**Why other options are incorrect:**
- **A)** and **B)** - Regular string quotes.
- **D)** - Regular expression syntax.

---


### Question 86

**Correct Answer: C) All siblings of matched elements**


**Explanation:** `siblings()` returns all sibling elements, excluding the element itself.


**Why other options are incorrect:**
- **A)** - Use `children()`.
- **B)** - Use `parents()`.
- **D)** - Use `next()`.

---


### Question 87

**Correct Answer: B) `fixed-top`**


**Explanation:** `fixed-top` fixes the navbar to the top of the viewport.


**Why other options are incorrect:**
- **C)** - Valid but `fixed-top` is specific for navbars.
- Others are not the correct Bootstrap classes.

---


### Question 88

**Correct Answer: D) `join()`**


**Explanation:** `join()` creates a string by concatenating all array elements with a separator.


**Why other options are incorrect:**
- **A) `concat()`** - Merges arrays, doesn't create string.
- Others are not array methods.

---


### Question 89

**Correct Answer: C) `$.extend(true, {}, obj)`**


**Explanation:** `$.extend()` with `true` as first argument performs a deep copy.


**Why other options are incorrect:**
- **B) `$.clone()`** - Clones DOM elements, not plain objects.
- Others are not jQuery methods.

---


### Question 90

**Correct Answer: B) Enables wrapping to new lines**


**Explanation:** `flex-wrap` allows flex items to wrap to new lines when they exceed container width.


**Why other options are incorrect:**
- **A)** - Use `flex-nowrap`.
- Others don't describe flex-wrap.

---


### Question 91

**Correct Answer: B) The element that triggered the event**


**Explanation:** `event.target` refers to the element where the event originated.


**Why other options are incorrect:**
- All others are not what `event.target` refers to.

---


### Question 92

**Correct Answer: A) `stop()`**


**Explanation:** `stop()` stops the currently-running animation on matched elements.


**Why other options are incorrect:**
- All others are not jQuery animation methods.

---


### Question 93

**Correct Answer: B) `h-100`**


**Explanation:** `h-100` sets height to 100%, useful for equal height columns in flex containers.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes (though `align-items-stretch` is default for rows).

---


### Question 94

**Correct Answer: B) Creates array from iterable or array-like object**


**Explanation:** `Array.from()` creates a new array from array-like or iterable objects.


**Why other options are incorrect:**
- **A)** - Works with any iterable, not just strings.
- Others don't describe `Array.from()`.

---


### Question 95

**Correct Answer: B) Removes whitespace from both ends**


**Explanation:** `$.trim()` removes leading and trailing whitespace from a string.


**Why other options are incorrect:**
- All others don't describe `$.trim()`.

---


### Question 96

**Correct Answer: C) `overflow-x-auto`**


**Explanation:** `overflow-x-auto` enables horizontal scrolling when content overflows.


**Why other options are incorrect:**
- **B)** - Applies to both axes.
- Others are not valid Bootstrap classes.

---


### Question 97

**Correct Answer: B) `export function fn() {}`**


**Explanation:** ES6 modules use `export` keyword before function declaration for named exports.


**Why other options are incorrect:**
- **A)** and **C)** - CommonJS syntax, not ES modules.
- **D)** - Not module export syntax.

---


### Question 98

**Correct Answer: B) Boolean**


**Explanation:** `is()` checks if any matched element matches the selector, returning true or false.


**Why other options are incorrect:**
- All others are not what `is()` returns.

---


### Question 99

**Correct Answer: C) `d-none`**


**Explanation:** `d-none` sets `display: none`, completely hiding the element.


**Why other options are incorrect:**
- **B) `invisible`** - Hides but still takes up space.
- Others are not valid Bootstrap classes.

---


### Question 100

**Correct Answer: B) Extracting values from arrays/objects into variables**


**Explanation:** Destructuring allows unpacking values from arrays or properties from objects into distinct variables.


**Why other options are incorrect:**
- All others don't describe destructuring.

---


### Question 101

**Correct Answer: A) `focus()`**


**Explanation:** The `focus` event fires when an element receives focus.


**Why other options are incorrect:**
- All others are not the focus event.

---


### Question 102

**Correct Answer: B) `position-absolute`**


**Explanation:** `position-absolute` applies `position: absolute` to the element.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 103

**Correct Answer: C) 42**


**Explanation:** `parseInt()` parses until it hits a non-numeric character, returning 42.


**Why other options are incorrect:**
- **A)** - Only if string starts with non-numeric.
- Others are not numeric return values.

---


### Question 104

**Correct Answer: A) `$(parent).on('click', '.child', handler)`**


**Explanation:** Using `on()` with a selector parameter enables event delegation.


**Why other options are incorrect:**
- **B)** and **C)** - Deprecated methods.
- **D)** - Wrong syntax.

---


### Question 105

**Correct Answer: B) `dropdown-menu`**


**Explanation:** `dropdown-menu` is the class for Bootstrap's dropdown container.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 106

**Correct Answer: C) 6**


**Explanation:** `reduce` accumulates: 0+1=1, 1+2=3, 3+3=6.


**Why other options are incorrect:**
- All others are not the accumulated sum.

---


### Question 107

**Correct Answer: B) Shows if hidden, hides if visible**


**Explanation:** `toggle()` alternates between `show()` and `hide()` states.


**Why other options are incorrect:**
- **A)** - Use `toggleClass()`.
- Others don't describe `toggle()`.

---


### Question 108

**Correct Answer: C) `list-group`**


**Explanation:** `list-group` is the base class for Bootstrap's list group component.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 109

**Correct Answer: C) `sort()`**


**Explanation:** `sort()` sorts array elements in place and returns the sorted array.


**Why other options are incorrect:**
- All others are not array methods.

---


### Question 110

**Correct Answer: C) `load()`**


**Explanation:** `load()` fetches data from server and places the returned HTML into the matched element.


**Why other options are incorrect:**
- Others fetch data but don't directly insert into elements.

---


### Question 111

**Correct Answer: B) `ms-3`**


**Explanation:** In Bootstrap 5, `ms-*` (margin-start) replaces `ml-*` for LTR support.


**Why other options are incorrect:**
- **A) `ml-3`** - Was Bootstrap 4, changed to `ms-*` in Bootstrap 5.
- Others are not valid Bootstrap classes.

---


### Question 112

**Correct Answer: B) Copies properties from source to target objects**


**Explanation:** `Object.assign()` copies enumerable properties from source objects to a target object.


**Why other options are incorrect:**
- All others don't describe `Object.assign()`.

---


### Question 113

**Correct Answer: B) The element's position among siblings**


**Explanation:** `index()` returns the position of the first matched element relative to its siblings.


**Why other options are incorrect:**
- All others don't describe `index()` behavior.

---


### Question 114

**Correct Answer: B) `spinner-border`**


**Explanation:** `spinner-border` creates a spinning loading indicator.


**Why other options are incorrect:**
- All others are not valid Bootstrap classes.

---


### Question 115

**Correct Answer: B) Persistent client-side storage**


**Explanation:** `localStorage` stores data with no expiration, persisting across browser sessions.


**Why other options are incorrect:**
- **A)** - That's `sessionStorage`.
- Others don't describe `localStorage`.

---


### Question 116

**Correct Answer: B) Releases the $ variable**


**Explanation:** `$.noConflict()` relinquishes jQuery's control of `$` for other libraries.


**Why other options are incorrect:**
- All others don't describe `$.noConflict()`.

---


### Question 117

**Correct Answer: C) `toast`**


**Explanation:** `toast` is the base class for Bootstrap's toast notification component.


**Why other options are incorrect:**
- **B) `alert`** - Different component.
- Others are not Bootstrap classes.

---


### Question 118

**Correct Answer: B) Expanding array elements as arguments**


**Explanation:** In function calls, spread expands an array into individual arguments.


**Why other options are incorrect:**
- **A)** - That's rest parameter in definitions.
- Others don't describe spread usage.

---


### Question 119

**Correct Answer: B) `off()`**


**Explanation:** `off()` removes event handlers attached with `on()`.


**Why other options are incorrect:**
- **A) `unbind()`** - Deprecated in favor of `off()`.
- **D) `detach()`** - Removes elements, not handlers.

---


### Question 120

**Correct Answer: B) `accordion`**


**Explanation:** `accordion` is the wrapper class for Bootstrap 5's accordion component.


**Why other options are incorrect:**
- **A) `collapse`** - Used within accordion but not the main class.
- Others are not valid Bootstrap 5 classes.

---


*Answer key for Week 02 curriculum - Revature Training Program*
