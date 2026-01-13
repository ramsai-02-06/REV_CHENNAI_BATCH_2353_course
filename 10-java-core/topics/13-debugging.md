# Software Debugging

## What is Debugging?

Debugging is the systematic process of identifying, analyzing, and removing errors (bugs) from computer programs. The term originates from the early days of computing when actual insects (bugs) would occasionally cause hardware malfunctions. Today, debugging refers to the methodical investigation and correction of software defects that cause unexpected behavior.

### Definition

**Debugging** is the art and science of locating and fixing errors in code. It involves:
- Reproducing the problem consistently
- Isolating the root cause
- Understanding the expected vs. actual behavior
- Implementing a fix without introducing new bugs
- Verifying the solution works correctly

### Why Debugging Matters

| Aspect | Impact |
|--------|--------|
| **Software Quality** | Ensures reliable, predictable application behavior |
| **User Experience** | Prevents crashes, data loss, and frustration |
| **Development Efficiency** | Reduces time spent on recurring issues |
| **Maintenance Cost** | Early bug detection saves exponential costs later |
| **Professional Growth** | Sharpens problem-solving and analytical skills |

---

## Purpose of Debugging

### Primary Objectives

1. **Error Identification**: Locate the exact source of unexpected behavior
2. **Root Cause Analysis**: Understand why the error occurs, not just where
3. **Code Correction**: Fix the issue without breaking existing functionality
4. **Prevention**: Learn patterns to avoid similar bugs in the future
5. **Validation**: Confirm the fix resolves the problem completely

### Types of Bugs

```
┌─────────────────────────────────────────────────────────────────────┐
│                        BUG CLASSIFICATION                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  SYNTAX ERRORS          RUNTIME ERRORS         LOGIC ERRORS         │
│  ─────────────          ──────────────         ────────────         │
│  • Missing semicolons   • NullPointerException • Incorrect output   │
│  • Unclosed brackets    • ArrayIndexOutOfBounds• Wrong algorithm    │
│  • Typos in keywords    • ClassCastException   • Off-by-one errors  │
│  • Type mismatches      • StackOverflowError   • Incorrect conditions│
│                                                                     │
│  SEMANTIC ERRORS        CONCURRENCY BUGS       PERFORMANCE BUGS     │
│  ───────────────        ────────────────       ─────────────────    │
│  • Wrong variable used  • Race conditions      • Memory leaks       │
│  • Incorrect operator   • Deadlocks            • Infinite loops     │
│  • Scope issues         • Thread starvation    • N+1 query problems │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Bug Severity Levels

| Level | Description | Example |
|-------|-------------|---------|
| **Critical** | System crash, data loss, security breach | NullPointerException in payment processing |
| **High** | Major feature broken, no workaround | Login fails for all users |
| **Medium** | Feature partially broken, workaround exists | Search returns wrong order |
| **Low** | Minor issue, cosmetic problems | Typo in UI label |

---

## The Debugging Process

### Scientific Method for Debugging

```
┌──────────────────────────────────────────────────────────────────────┐
│                    DEBUGGING WORKFLOW                                │
│                                                                      │
│   ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐      │
│   │ REPRODUCE│───▶│ ISOLATE  │───▶│ IDENTIFY │───▶│   FIX    │      │
│   │  THE BUG │    │ THE AREA │    │THE CAUSE │    │ THE BUG  │      │
│   └──────────┘    └──────────┘    └──────────┘    └──────────┘      │
│        │                                               │             │
│        │              ┌──────────┐                     │             │
│        └──────────────│  VERIFY  │◀────────────────────┘             │
│                       │ THE FIX  │                                   │
│                       └──────────┘                                   │
└──────────────────────────────────────────────────────────────────────┘
```

### Step 1: Reproduce the Bug

Before fixing anything, you must reliably reproduce the issue:

```java
// Example: Bug report says "calculateDiscount returns wrong value"

public class DiscountCalculator {
    public double calculateDiscount(double price, int quantity) {
        // What inputs cause the bug?
        // Document the steps to reproduce
        if (quantity > 10) {
            return price * 0.15;  // 15% discount
        } else if (quantity > 5) {
            return price * 0.10;  // 10% discount
        }
        return 0;
    }
}

// Reproduction steps:
// 1. Call calculateDiscount(100.0, 10)
// 2. Expected: 10.0 (10% of 100)
// 3. Actual: 0.0 (no discount applied)
// 4. Bug: condition uses > instead of >=
```

### Step 2: Isolate the Problem Area

Narrow down where the bug occurs:

```java
public class OrderProcessor {

    public void processOrder(Order order) {
        // Add checkpoints to isolate the issue
        System.out.println("Step 1: Starting order processing");

        validateOrder(order);
        System.out.println("Step 2: Validation complete");

        calculateTotal(order);
        System.out.println("Step 3: Total calculated: " + order.getTotal());

        applyDiscount(order);
        System.out.println("Step 4: Discount applied: " + order.getDiscount());

        finalizeOrder(order);
        System.out.println("Step 5: Order finalized");
    }
}
```

### Step 3: Identify the Root Cause

Use debugging tools to examine state:

```java
public void applyDiscount(Order order) {
    // Set a breakpoint here to inspect:
    // - order.getItems() - are items correct?
    // - order.getCustomer() - is customer loaded?
    // - order.getTotal() - is total calculated?

    Customer customer = order.getCustomer();

    // Bug found: customer is null!
    // Root cause: validateOrder() doesn't check customer
    double discount = customer.getLoyaltyDiscount();  // NullPointerException

    order.setDiscount(discount);
}
```

### Step 4: Fix the Bug

Implement a targeted fix:

```java
public void applyDiscount(Order order) {
    Customer customer = order.getCustomer();

    // Fix: Add null check with appropriate handling
    if (customer == null) {
        order.setDiscount(0);
        return;
    }

    double discount = customer.getLoyaltyDiscount();
    order.setDiscount(discount);
}
```

### Step 5: Verify the Fix

Ensure the fix works and doesn't break anything else:

```java
@Test
public void testApplyDiscount_WithCustomer() {
    Order order = createOrderWithCustomer();
    processor.applyDiscount(order);
    assertEquals(10.0, order.getDiscount(), 0.01);
}

@Test
public void testApplyDiscount_WithoutCustomer() {
    Order order = createOrderWithoutCustomer();
    processor.applyDiscount(order);  // Should not throw
    assertEquals(0.0, order.getDiscount(), 0.01);
}
```

---

## Debugging Techniques

### 1. Print Statement Debugging

The simplest debugging technique using output statements:

```java
public int binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        // Debug output
        System.out.println("left=" + left + ", right=" + right + ", mid=" + mid);
        System.out.println("arr[mid]=" + arr[mid] + ", target=" + target);

        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}
```

**Pros**: Simple, works everywhere
**Cons**: Clutters code, must be removed, no state inspection

### 2. Logging for Debugging

Using a logging framework for structured debugging:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentResult processPayment(PaymentRequest request) {
        logger.debug("Processing payment: {}", request);

        try {
            logger.trace("Validating payment details");
            validatePayment(request);

            logger.trace("Connecting to payment gateway");
            PaymentGateway gateway = getGateway(request.getMethod());

            logger.debug("Submitting to gateway: {}", gateway.getName());
            PaymentResult result = gateway.submit(request);

            logger.info("Payment processed successfully: {}", result.getTransactionId());
            return result;

        } catch (PaymentException e) {
            logger.error("Payment failed for request {}: {}", request.getId(), e.getMessage());
            throw e;
        }
    }
}
```

### 3. Rubber Duck Debugging

Explain the code line-by-line to find the bug:

```java
// Talk through the code:
public String formatName(String firstName, String lastName) {
    // "I'm getting the first character of firstName..."
    String initial = firstName.substring(0, 1);

    // "Then uppercasing it..."
    initial = initial.toUpperCase();

    // "Wait - what if firstName is empty?"
    // Bug found through explanation!

    return initial + ". " + lastName;
}

// Fixed version:
public String formatName(String firstName, String lastName) {
    if (firstName == null || firstName.isEmpty()) {
        return lastName;
    }
    String initial = firstName.substring(0, 1).toUpperCase();
    return initial + ". " + lastName;
}
```

### 4. Divide and Conquer

Systematically narrow down the problem area:

```java
public void processLargeDataSet(List<Data> items) {
    // Bug: Something fails with large datasets

    // Test with first half
    List<Data> firstHalf = items.subList(0, items.size() / 2);
    processItems(firstHalf);  // Works

    // Test with second half
    List<Data> secondHalf = items.subList(items.size() / 2, items.size());
    processItems(secondHalf);  // Fails!

    // Continue dividing second half until problem item found
}
```

### 5. Code Review and Diff Analysis

Compare working vs. non-working versions:

```bash
# Git commands for debugging
git log --oneline -20           # Find recent commits
git bisect start                # Binary search through commits
git diff HEAD~5 HEAD -- src/    # Compare changes
git blame src/MyClass.java      # See who changed each line
```

---

## Debugging with IntelliJ IDEA

IntelliJ IDEA provides powerful debugging tools that make finding and fixing bugs efficient.

### Starting the Debugger

#### Debug Mode vs. Run Mode

```
┌─────────────────────────────────────────────────────────────────────┐
│                    DEBUG MODE vs RUN MODE                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   RUN MODE (Shift+F10)          DEBUG MODE (Shift+F9)               │
│   ───────────────────           ─────────────────────               │
│   • Normal execution            • Enables breakpoints               │
│   • No pause points             • Allows stepping through code      │
│   • Faster execution            • Variable inspection               │
│   • Production-like             • Expression evaluation             │
│                                                                     │
│   Use for: Testing,             Use for: Finding bugs,              │
│   running application           understanding code flow             │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

#### Ways to Start Debugging

1. **Toolbar**: Click the bug icon (Debug) or press `Shift+F9`
2. **Right-click**: Right-click on a file and select "Debug"
3. **Run menu**: Select Run > Debug
4. **Gutter**: Click the green arrow next to `main()` and select Debug

### Breakpoints

Breakpoints pause program execution at specific lines.

#### Types of Breakpoints

```
┌─────────────────────────────────────────────────────────────────────┐
│                     BREAKPOINT TYPES                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   LINE BREAKPOINT (Red Circle)                                      │
│   • Pauses at specific line                                         │
│   • Most common type                                                │
│   • Click in gutter to set                                          │
│                                                                     │
│   METHOD BREAKPOINT (Red Diamond)                                   │
│   • Pauses when method is entered/exited                            │
│   • Useful for interface implementations                            │
│   • Set on method signature                                         │
│                                                                     │
│   FIELD WATCHPOINT (Red Eye)                                        │
│   • Pauses when field is read/modified                              │
│   • Great for tracking state changes                                │
│   • Set on field declaration                                        │
│                                                                     │
│   EXCEPTION BREAKPOINT (Red Lightning)                              │
│   • Pauses when specific exception is thrown                        │
│   • Find where exceptions originate                                 │
│   • Set via Run > View Breakpoints                                  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

#### Setting Breakpoints

```java
public class Calculator {
    private int result = 0;  // Field watchpoint: track changes to result

    // Method breakpoint: pause on entry
    public int calculate(int a, int b, String operation) {

        // Line breakpoint: pause before switch
        switch (operation) {
            case "add":
                result = a + b;  // Line breakpoint
                break;
            case "subtract":
                result = a - b;
                break;
            case "multiply":
                result = a * b;
                break;
            case "divide":
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = a / b;
                break;
        }
        return result;
    }
}
```

**Keyboard Shortcuts**:
- `Ctrl+F8` (Windows/Linux) / `Cmd+F8` (Mac): Toggle line breakpoint
- `Ctrl+Shift+F8`: View all breakpoints

#### Conditional Breakpoints

Right-click on a breakpoint to add conditions:

```java
public void processItems(List<Item> items) {
    for (Item item : items) {
        // Set breakpoint with condition: item.getId() == 42
        // Only pauses when processing item with ID 42
        processItem(item);
    }
}
```

**Condition Examples**:
- `item.getPrice() > 100` - Pause for expensive items
- `counter % 100 == 0` - Pause every 100 iterations
- `name.contains("error")` - Pause when name contains "error"

#### Breakpoint Options

| Option | Description |
|--------|-------------|
| **Suspend** | All threads vs. Current thread |
| **Condition** | Boolean expression to evaluate |
| **Log message** | Print message instead of pausing |
| **Log stack trace** | Print stack trace when hit |
| **Remove once hit** | One-time breakpoint |
| **Disable until breakpoint hit** | Chain breakpoints |

### Debug Window Panels

```
┌─────────────────────────────────────────────────────────────────────┐
│ IntelliJ Debug Window                                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌────────────────┐  ┌─────────────────────────────────────────────┐│
│  │   FRAMES       │  │              VARIABLES                      ││
│  ├────────────────┤  ├─────────────────────────────────────────────┤│
│  │ processOrder   │  │ this = OrderProcessor@1234                  ││
│  │ validateOrder  │  │ order = Order@5678                          ││
│  │ main           │  │   ├─ id = 42                                ││
│  │                │  │   ├─ items = ArrayList (size=3)             ││
│  │                │  │   └─ total = 150.0                          ││
│  └────────────────┘  │ discount = 15.0                             ││
│                      └─────────────────────────────────────────────┘│
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                      WATCHES                                   │ │
│  ├────────────────────────────────────────────────────────────────┤ │
│  │ order.getTotal() = 150.0                                       │ │
│  │ items.size() = 3                                               │ │
│  │ customer.getName() = "John Doe"                                │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

#### Frames Panel

Shows the call stack (method calls that led to current location):
- Click on any frame to see its local variables
- Navigate up/down the stack to understand execution flow
- Grayed frames are library/framework code

#### Variables Panel

Displays all variables in current scope:
- **Primitive values**: Shown directly (int, double, boolean)
- **Objects**: Expandable tree to view fields
- **Collections**: Show size and elements
- **Right-click**: Set value, copy, add to watches

#### Watches Panel

Track specific expressions throughout debugging:
- Add custom expressions to evaluate
- Watches persist across debug sessions
- Use for complex expressions or calculated values

### Stepping Through Code

```
┌─────────────────────────────────────────────────────────────────────┐
│                    STEPPING ACTIONS                                 │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   STEP OVER (F8)                 STEP INTO (F7)                     │
│   ──────────────                 ─────────────                      │
│   • Execute current line         • Enter the called method          │
│   • Don't enter methods          • Debug inside the method          │
│   • Stay at same level           • Go deeper into code              │
│                                                                     │
│   STEP OUT (Shift+F8)            RUN TO CURSOR (Alt+F9)             │
│   ─────────────────              ─────────────────────              │
│   • Complete current method      • Run until cursor position        │
│   • Return to caller             • Skip intermediate code           │
│   • Go up one level              • Quick navigation                 │
│                                                                     │
│   FORCE STEP INTO (Alt+Shift+F7) SMART STEP INTO (Shift+F7)         │
│   ──────────────────────────     ───────────────────────            │
│   • Enter even library methods   • Choose which method to enter     │
│   • Debug third-party code       • For chained method calls         │
│                                                                     │
│   RESUME (F9)                    DROP FRAME (Reset)                 │
│   ───────────                    ─────────────                      │
│   • Continue to next breakpoint  • Re-execute current method        │
│   • Normal execution resumes     • Reset to method start            │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

#### Practical Stepping Example

```java
public void placeOrder(Order order) {
    // Breakpoint here, then:

    validate(order);        // F8 - Step Over (skip validation details)

    calculateShipping(order);  // F7 - Step Into (see calculation)

    processPayment(order);  // F7 - Step Into, then Shift+F8 Step Out

    sendConfirmation(order);  // Alt+F9 - Run to Cursor (skip to here)
}
```

### Evaluate Expression

The most powerful debugging feature - evaluate any Java expression during debugging.

**Access**: `Alt+F8` or right-click > Evaluate Expression

```java
// While paused at a breakpoint, evaluate:

// Simple expressions
order.getTotal()                          // Returns: 150.0
order.getItems().size()                   // Returns: 3

// Complex expressions
order.getItems().stream()
    .filter(i -> i.getPrice() > 50)
    .count()                              // Returns: 2

// Method calls (may have side effects!)
orderService.recalculateTotal(order)      // Modifies order

// Object creation
new BigDecimal("100.50")                  // Creates new instance

// Modify state during debugging
order.setDiscount(25.0)                   // Changes discount value
```

**Use Cases**:
- Test hypotheses about bug causes
- Try potential fixes before coding
- Explore object state
- Call methods to understand behavior

### Watches and Variable Modification

#### Adding Watches

1. Right-click variable > Add to Watches
2. Click + in Watches panel
3. Type expression and press Enter

```java
// Useful watch expressions:
order.getItems().stream().mapToDouble(Item::getPrice).sum()
System.currentTimeMillis() - startTime
Thread.currentThread().getName()
```

#### Modifying Variables

Change variable values during debugging:
1. Right-click variable in Variables panel
2. Select "Set Value"
3. Enter new value

```java
// Example: Testing different scenarios
// Original: discount = 0.10
// Set Value to: 0.25
// Continue debugging to see effect of 25% discount
```

### Debug Toolbar Actions

| Button | Shortcut | Description |
|--------|----------|-------------|
| Resume | F9 | Continue to next breakpoint |
| Pause | - | Pause running program |
| Stop | Ctrl+F2 | Terminate debug session |
| View Breakpoints | Ctrl+Shift+F8 | Manage all breakpoints |
| Mute Breakpoints | - | Temporarily disable all breakpoints |
| Step Over | F8 | Execute line without entering methods |
| Step Into | F7 | Enter called method |
| Force Step Into | Alt+Shift+F7 | Enter any method, including libraries |
| Step Out | Shift+F8 | Exit current method |
| Run to Cursor | Alt+F9 | Continue to cursor location |
| Evaluate Expression | Alt+F8 | Open evaluation dialog |

### Advanced IntelliJ Debugging Features

#### Stream Debugger

Debug Java Streams with visual representation:

```java
List<String> result = names.stream()
    .filter(n -> n.length() > 3)        // See filtered elements
    .map(String::toUpperCase)           // See mapped results
    .sorted()                           // See sorted order
    .collect(Collectors.toList());      // See final result

// Click "Trace Current Stream Chain" in debug window
```

#### Memory View

Analyze heap memory and object instances:
1. Open via Run > Debug Windows > Memory
2. See all loaded classes and instance counts
3. Double-click class to view all instances
4. Track memory leaks and object creation

#### Thread Debugging

Debug multithreaded applications:

```java
// In Frames panel, switch between threads
// Each thread shows its own call stack
// Variables panel updates for selected thread

public void concurrentTask() {
    // Set breakpoint with "Suspend: All" to pause all threads
    // Or "Suspend: Thread" to pause only current thread
    processItem(item);
}
```

#### Remote Debugging

Debug applications running on remote servers:

1. Start remote JVM with debug options:
```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar app.jar
```

2. In IntelliJ: Run > Edit Configurations > + > Remote JVM Debug
3. Set host and port (5005)
4. Click Debug to connect

### Debugging Best Practices in IntelliJ

```
┌─────────────────────────────────────────────────────────────────────┐
│                  INTELLIJ DEBUGGING TIPS                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  DO:                               DON'T:                           │
│  ───                               ──────                           │
│  ✓ Use conditional breakpoints     ✗ Set breakpoints everywhere     │
│  ✓ Add meaningful watches          ✗ Rely only on print statements  │
│  ✓ Evaluate expressions to test    ✗ Modify variables carelessly    │
│  ✓ Use Step Over for known code    ✗ Step Into every method         │
│  ✓ Check call stack when confused  ✗ Ignore the Frames panel        │
│  ✓ Use Exception breakpoints       ✗ Only catch exceptions in code  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Common Debugging Scenarios

### Scenario 1: NullPointerException

```java
public class UserService {
    public String getUserDisplayName(Long userId) {
        User user = userRepository.findById(userId);  // Breakpoint here
        // user might be null!

        // Debug steps:
        // 1. Set breakpoint on line above
        // 2. Check if user is null in Variables panel
        // 3. If null, check userId value
        // 4. Query database directly to verify data exists

        return user.getFirstName() + " " + user.getLastName();
    }
}

// Fix:
public String getUserDisplayName(Long userId) {
    User user = userRepository.findById(userId);
    if (user == null) {
        return "Unknown User";
    }
    return user.getFirstName() + " " + user.getLastName();
}
```

### Scenario 2: Infinite Loop

```java
public void processQueue(Queue<Task> tasks) {
    // Set breakpoint with condition: counter > 1000
    int counter = 0;

    while (!tasks.isEmpty()) {
        Task task = tasks.peek();  // Bug: should be poll()!
        processTask(task);
        counter++;

        // Debug: After many iterations, check counter and tasks.size()
        // Realize tasks.size() never decreases
    }
}

// Fix:
while (!tasks.isEmpty()) {
    Task task = tasks.poll();  // Remove from queue
    processTask(task);
}
```

### Scenario 3: Wrong Calculation

```java
public double calculateTax(double amount, String state) {
    // Set breakpoint, add watches:
    // - amount
    // - state
    // - taxRate
    // - amount * taxRate

    double taxRate;
    switch (state) {
        case "CA":
            taxRate = 0.0725;
            break;
        case "TX":
            taxRate = 0.0625;
            break;
        default:
            taxRate = 0.05;
    }

    // Bug: Integer division if taxRate were int
    return amount * taxRate;
}
```

### Scenario 4: Concurrent Modification

```java
public void removeInactiveUsers(List<User> users) {
    // This will throw ConcurrentModificationException
    for (User user : users) {
        if (!user.isActive()) {
            users.remove(user);  // Bug!
        }
    }
}

// Debug with exception breakpoint on ConcurrentModificationException
// See exactly where it's thrown

// Fix using Iterator:
public void removeInactiveUsers(List<User> users) {
    Iterator<User> iterator = users.iterator();
    while (iterator.hasNext()) {
        User user = iterator.next();
        if (!user.isActive()) {
            iterator.remove();  // Safe removal
        }
    }
}

// Or using removeIf:
users.removeIf(user -> !user.isActive());
```

---

## Debugging Strategy Summary

### The IDEAL Debugging Process

| Step | Action | IntelliJ Tool |
|------|--------|---------------|
| **I**dentify | Confirm the bug exists and understand symptoms | Console output, Logs |
| **D**etermine | Narrow down where the bug occurs | Breakpoints, Call stack |
| **E**xamine | Inspect variables and program state | Variables, Watches, Evaluate |
| **A**nalyze | Understand root cause | Step through code, Trace |
| **L**earn | Fix and prevent future occurrences | Tests, Documentation |

### Quick Reference Card

```
┌─────────────────────────────────────────────────────────────────────┐
│                 INTELLIJ DEBUG SHORTCUTS                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  START/STOP                        BREAKPOINTS                      │
│  Shift+F9      Debug               Ctrl+F8       Toggle             │
│  Ctrl+F2       Stop                Ctrl+Shift+F8 View All           │
│                                                                     │
│  STEPPING                          INSPECTION                       │
│  F8            Step Over           Alt+F8        Evaluate           │
│  F7            Step Into           Ctrl+Click    Quick Evaluate     │
│  Shift+F8      Step Out                                             │
│  Alt+F9        Run to Cursor                                        │
│  F9            Resume                                               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Summary

| Concept | Description |
|---------|-------------|
| **Debugging** | Systematic process of finding and fixing code errors |
| **Bug Types** | Syntax, Runtime, Logic, Semantic, Concurrency, Performance |
| **Breakpoints** | Pause execution at specific locations |
| **Stepping** | Control execution flow line by line |
| **Variables/Watches** | Inspect and track program state |
| **Evaluate Expression** | Test expressions during debugging |
| **Call Stack** | Trace execution path through methods |

**Key Takeaways**:
1. Always reproduce the bug before attempting to fix it
2. Use breakpoints strategically, not everywhere
3. Conditional breakpoints save time with large datasets
4. The Evaluate Expression feature is invaluable for testing hypotheses
5. Check the call stack to understand how you got to the current state
6. Debugging is a skill that improves with practice
