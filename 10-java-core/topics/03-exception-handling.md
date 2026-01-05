# Exception Handling

## Exceptions Vs Errors

Understanding the difference between exceptions and errors is fundamental to proper error handling in Java.

### The Throwable Hierarchy

```
                    Throwable
                        |
         +--------------+--------------+
         |                             |
      Error                       Exception
         |                             |
   +-----+-----+              +--------+--------+
   |     |     |              |                 |
OutOf   Stack Virtual    RuntimeException   IOException
Memory  Over  Machine    (Unchecked)        (Checked)
Error   flow  Error           |                 |
(Unchecked)  (Unchecked)  +---+---+        +----+----+
                          |   |   |        |    |    |
                        NPE IAE ISE      FNF  IO  SQL
                                         Exception
```

### Errors

Serious problems that applications should not try to handle. Typically external to the application.

```java
// Examples of Errors (usually unrecoverable)

// 1. OutOfMemoryError
List<int[]> list = new ArrayList<>();
while (true) {
    list.add(new int[1000000]);  // Eventually throws OutOfMemoryError
}

// 2. StackOverflowError
public void recursiveMethod() {
    recursiveMethod();  // Infinite recursion
}

// 3. VirtualMachineError
// Internal JVM errors

// 4. NoClassDefFoundError
// Class file not found at runtime
```

**Errors Characteristics:**
- Unchecked (not forced to handle)
- Usually catastrophic
- Application cannot recover
- Should not be caught (generally)

### Exceptions

Problems that can be anticipated and handled by the application.

```java
// Exception examples

// 1. Checked Exception (must handle)
try {
    FileReader reader = new FileReader("file.txt");
} catch (FileNotFoundException e) {
    System.out.println("File not found!");
}

// 2. Unchecked Exception (optional to handle)
String str = null;
str.length();  // NullPointerException

int[] arr = new int[5];
arr[10] = 100;  // ArrayIndexOutOfBoundsException

int result = 10 / 0;  // ArithmeticException
```

### Error vs Exception

| Feature | Error | Exception |
|---------|-------|-----------|
| Recovery | Usually not possible | Can be handled |
| Type | Unchecked | Checked or Unchecked |
| Caused by | System/JVM | Application logic |
| Should catch? | No | Yes (when appropriate) |
| Examples | OutOfMemoryError, StackOverflowError | IOException, SQLException, NullPointerException |

---

## Handling Exceptions

Java provides structured mechanisms to handle exceptions gracefully.

### try-catch Block

```java
try {
    // Code that might throw exception
    int result = 10 / 0;
    System.out.println("Result: " + result);
} catch (ArithmeticException e) {
    // Handle the exception
    System.out.println("Cannot divide by zero!");
}

System.out.println("Program continues...");
```

### Multiple catch Blocks

```java
try {
    String str = null;
    System.out.println(str.length());

    int[] arr = new int[5];
    arr[10] = 100;

    int result = 10 / 0;

} catch (NullPointerException e) {
    System.out.println("Null reference: " + e.getMessage());
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Array index out of bounds: " + e.getMessage());
} catch (ArithmeticException e) {
    System.out.println("Arithmetic error: " + e.getMessage());
}
```

### Multi-catch (Java 7+)

```java
try {
    // Code that might throw multiple exceptions
    performOperation();
} catch (IOException | SQLException e) {
    // Handle both exceptions the same way
    System.out.println("Error: " + e.getMessage());
    e.printStackTrace();
}
```

### finally Block

Always executes, regardless of whether exception occurs.

```java
FileReader reader = null;
try {
    reader = new FileReader("file.txt");
    // Read file
} catch (FileNotFoundException e) {
    System.out.println("File not found");
} finally {
    // Always executes (cleanup code)
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Error closing file");
        }
    }
}
```

### try-with-resources (Java 7+)

Automatically closes resources that implement AutoCloseable.

```java
// Old way (verbose)
BufferedReader reader = null;
try {
    reader = new BufferedReader(new FileReader("file.txt"));
    String line = reader.readLine();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// New way (concise)
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
// reader.close() automatically called

// Multiple resources
try (FileReader fr = new FileReader("input.txt");
     FileWriter fw = new FileWriter("output.txt")) {
    // Use resources
} catch (IOException e) {
    e.printStackTrace();
}
// Both automatically closed in reverse order
```

### throw Keyword

Explicitly throw an exception.

```java
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
    this.age = age;
}

// Usage
try {
    person.setAge(-5);
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

### throws Keyword

Declare that method might throw exceptions (caller must handle).

```java
// Method declares exception
public void readFile(String filename) throws IOException {
    FileReader reader = new FileReader(filename);
    // Read file
    reader.close();
}

// Caller must handle
public void processFile() {
    try {
        readFile("data.txt");
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
}

// Or propagate further
public void processFile() throws IOException {
    readFile("data.txt");
}
```

### Exception Methods

```java
try {
    int result = 10 / 0;
} catch (Exception e) {
    // Get exception message
    String message = e.getMessage();  // "/ by zero"

    // Get exception class name
    String className = e.getClass().getName();  // "java.lang.ArithmeticException"

    // Print stack trace
    e.printStackTrace();

    // Get stack trace as array
    StackTraceElement[] stackTrace = e.getStackTrace();

    // Get cause (if exception was chained)
    Throwable cause = e.getCause();

    // Print formatted
    System.out.println(e.toString());  // Class name + message
}
```

---

## Checked Vs Unchecked Exceptions

Understanding the difference helps write better error handling code.

### Checked Exceptions

Must be handled or declared. Compile-time enforcement.

```java
// Common Checked Exceptions

// 1. IOException
public void readFile() throws IOException {
    FileReader reader = new FileReader("file.txt");
    reader.close();
}

// 2. SQLException
public void queryDatabase() throws SQLException {
    Connection conn = DriverManager.getConnection(url);
    Statement stmt = conn.createStatement();
}

// 3. ClassNotFoundException
public void loadClass() throws ClassNotFoundException {
    Class.forName("com.example.MyClass");
}

// 4. InterruptedException
public void sleepThread() throws InterruptedException {
    Thread.sleep(1000);
}
```

**Must handle:**

```java
// Option 1: Handle with try-catch
public void method1() {
    try {
        FileReader reader = new FileReader("file.txt");
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}

// Option 2: Declare with throws
public void method2() throws FileNotFoundException {
    FileReader reader = new FileReader("file.txt");
}
```

### Unchecked Exceptions

Optional to handle. Runtime exceptions.

```java
// Common Unchecked Exceptions

// 1. NullPointerException
String str = null;
str.length();  // NPE

// 2. ArrayIndexOutOfBoundsException
int[] arr = {1, 2, 3};
int x = arr[10];  // AIOOBE

// 3. ArithmeticException
int result = 10 / 0;  // ArithmeticException

// 4. IllegalArgumentException
public void setAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}

// 5. NumberFormatException
int num = Integer.parseInt("abc");  // NFE

// 6. ClassCastException
Object obj = "Hello";
Integer num = (Integer) obj;  // CCE

// 7. IllegalStateException
// Thrown when method called at illegal/inappropriate time
```

**Optional to handle:**

```java
// Can handle if desired
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero");
}

// Or leave unhandled (will crash if exception occurs)
int result = 10 / 0;  // No compiler error, but crashes at runtime
```

### Checked vs Unchecked Comparison

| Feature | Checked | Unchecked |
|---------|---------|-----------|
| Inheritance | Extends Exception | Extends RuntimeException |
| Compile-time check | Yes | No |
| Must handle? | Yes (catch or throws) | No (optional) |
| When to use | Recoverable conditions | Programming errors |
| Examples | IOException, SQLException | NullPointerException, IllegalArgumentException |

### Hierarchy

```java
Throwable
├── Error (Unchecked)
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception
    ├── RuntimeException (Unchecked)
    │   ├── NullPointerException
    │   ├── ArithmeticException
    │   ├── IllegalArgumentException
    │   └── ArrayIndexOutOfBoundsException
    └── (Other Exceptions are Checked)
        ├── IOException
        │   └── FileNotFoundException
        ├── SQLException
        └── ClassNotFoundException
```

### When to Use Which?

```java
// Use Checked Exceptions for:
// - External factors beyond program control
// - Caller should be forced to handle

public void saveToFile(String filename) throws IOException {
    // File might not exist, disk might be full
    FileWriter writer = new FileWriter(filename);
    writer.write("data");
    writer.close();
}

// Use Unchecked Exceptions for:
// - Programming errors
// - Violations of preconditions
// - Caller should fix their code

public void setAge(int age) {
    if (age < 0) {
        // Programmer error - should not pass negative age
        throw new IllegalArgumentException("Age cannot be negative");
    }
    this.age = age;
}
```

### Best Practices

```java
// 1. Don't catch generic Exception (too broad)
// BAD
try {
    // code
} catch (Exception e) {  // Too generic
    e.printStackTrace();
}

// GOOD
try {
    // code
} catch (IOException e) {  // Specific
    // Handle file error
} catch (SQLException e) {  // Specific
    // Handle database error
}

// 2. Don't ignore exceptions
// BAD
try {
    // code
} catch (Exception e) {
    // Empty - exception silently ignored!
}

// GOOD
try {
    // code
} catch (Exception e) {
    logger.error("Error occurred", e);
    // or throw new RuntimeException("Failed to process", e);
}

// 3. Don't use exceptions for control flow
// BAD
try {
    int i = 0;
    while (true) {
        array[i++];
    }
} catch (ArrayIndexOutOfBoundsException e) {
    // End of array
}

// GOOD
for (int i = 0; i < array.length; i++) {
    // Process array[i]
}
```

---

## Custom Exceptions

Create custom exceptions for application-specific error conditions.

### Creating Custom Checked Exception

```java
// Extend Exception for checked exception
public class InsufficientFundsException extends Exception {
    private double amount;

    public InsufficientFundsException(double amount) {
        super("Insufficient funds: $" + amount);
        this.amount = amount;
    }

    public InsufficientFundsException(String message, double amount) {
        super(message);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

// Usage
public class BankAccount {
    private double balance;

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(amount);
        }
        balance -= amount;
    }
}

// Caller must handle
try {
    account.withdraw(1000);
} catch (InsufficientFundsException e) {
    System.out.println("Error: " + e.getMessage());
    System.out.println("Requested: $" + e.getAmount());
}
```

### Creating Custom Unchecked Exception

```java
// Extend RuntimeException for unchecked exception
public class InvalidUserException extends RuntimeException {
    private String username;

    public InvalidUserException(String username) {
        super("Invalid user: " + username);
        this.username = username;
    }

    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getUsername() {
        return username;
    }
}

// Usage (no need to declare with throws)
public void validateUser(String username) {
    if (username == null || username.isEmpty()) {
        throw new InvalidUserException(username);
    }
}
```

### Exception Chaining

Preserve original exception while throwing new one.

```java
public class DataProcessingException extends Exception {
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Usage
public void processData(String filename) throws DataProcessingException {
    try {
        FileReader reader = new FileReader(filename);
        // Process file
    } catch (FileNotFoundException e) {
        // Wrap original exception
        throw new DataProcessingException("Failed to process file: " + filename, e);
    }
}

// Retrieve original exception
try {
    processData("data.txt");
} catch (DataProcessingException e) {
    System.out.println(e.getMessage());
    Throwable cause = e.getCause();  // Get original FileNotFoundException
    System.out.println("Original cause: " + cause.getMessage());
}
```

### Best Practices for Custom Exceptions

```java
public class UserNotFoundException extends Exception {
    // 1. Provide multiple constructors
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    // 2. Include relevant data
    private Long userId;

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    // 3. Make it serializable if needed
    private static final long serialVersionUID = 1L;
}
```

### Real-World Example

```java
// Domain-specific exceptions
public class OrderException extends Exception {
    public OrderException(String message) {
        super(message);
    }
}

public class OrderNotFoundException extends OrderException {
    private String orderId;

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

public class PaymentFailedException extends OrderException {
    private double amount;

    public PaymentFailedException(double amount, Throwable cause) {
        super("Payment failed for amount: $" + amount, cause);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}

// Service using custom exceptions
public class OrderService {
    public void processOrder(String orderId, double amount) throws OrderException {
        Order order = findOrder(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        try {
            processPayment(amount);
        } catch (PaymentException e) {
            throw new PaymentFailedException(amount, e);
        }
    }
}
```

---

## Reading Stack Traces

Stack traces are essential for debugging exceptions.

### Anatomy of a Stack Trace

```java
public class StackTraceExample {
    public static void main(String[] args) {
        method1();
    }

    public static void method1() {
        method2();
    }

    public static void method2() {
        method3();
    }

    public static void method3() {
        String str = null;
        str.length();  // Throws NullPointerException
    }
}
```

**Output:**

```
Exception in thread "main" java.lang.NullPointerException
    at StackTraceExample.method3(StackTraceExample.java:15)
    at StackTraceExample.method2(StackTraceExample.java:11)
    at StackTraceExample.method1(StackTraceExample.java:7)
    at StackTraceExample.main(StackTraceExample.java:3)
```

### Reading the Stack Trace

```
Exception in thread "main"                    ← Thread name
java.lang.NullPointerException                ← Exception type
    at StackTraceExample.method3              ← Class.method
       (StackTraceExample.java:15)            ← File and line number
    at StackTraceExample.method2              ← Called from method2
       (StackTraceExample.java:11)
    at StackTraceExample.method1              ← Called from method1
       (StackTraceExample.java:7)
    at StackTraceExample.main                 ← Started from main
       (StackTraceExample.java:3)
```

**Reading order:** Bottom to top = execution flow

### Stack Trace with Message

```java
try {
    int age = -5;
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative: " + age);
    }
} catch (IllegalArgumentException e) {
    e.printStackTrace();
}
```

**Output:**

```
java.lang.IllegalArgumentException: Age cannot be negative: -5
    at Example.validateAge(Example.java:10)
    at Example.main(Example.java:5)
```

### Chained Exceptions

```java
try {
    method1();
} catch (Exception e) {
    e.printStackTrace();
}

public void method1() throws Exception {
    try {
        FileReader reader = new FileReader("missing.txt");
    } catch (FileNotFoundException e) {
        throw new Exception("Failed to process file", e);
    }
}
```

**Output:**

```
java.lang.Exception: Failed to process file
    at Example.method1(Example.java:15)
    at Example.main(Example.java:5)
Caused by: java.io.FileNotFoundException: missing.txt
    at java.io.FileInputStream.<init>(FileInputStream.java:146)
    at java.io.FileReader.<init>(FileReader.java:72)
    at Example.method1(Example.java:13)
    ... 1 more
```

### Programmatic Stack Trace Access

```java
try {
    riskyOperation();
} catch (Exception e) {
    // Get stack trace elements
    StackTraceElement[] stackTrace = e.getStackTrace();

    for (StackTraceElement element : stackTrace) {
        String className = element.getClassName();
        String methodName = element.getMethodName();
        String fileName = element.getFileName();
        int lineNumber = element.getLineNumber();

        System.out.println(className + "." + methodName +
                         "(" + fileName + ":" + lineNumber + ")");
    }

    // Get specific element
    StackTraceElement first = stackTrace[0];
    System.out.println("Error at line: " + first.getLineNumber());
}
```

### Common Stack Trace Patterns

#### NullPointerException

```
java.lang.NullPointerException
    at com.example.UserService.getUser(UserService.java:45)
```

**Problem:** Accessing method/field on null object at line 45

#### ArrayIndexOutOfBoundsException

```
java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 3
    at com.example.ArrayProcessor.process(ArrayProcessor.java:20)
```

**Problem:** Accessing array index 5, but array length is 3

#### ClassCastException

```
java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer
    at com.example.TypeConverter.convert(TypeConverter.java:12)
```

**Problem:** Trying to cast String to Integer at line 12

### Debugging Tips

```java
// 1. Start from the top (where exception occurred)
Exception in thread "main" java.lang.NullPointerException
    at Example.method3(Example.java:15)  ← START HERE
    at Example.method2(Example.java:11)
    at Example.method1(Example.java:7)
    at Example.main(Example.java:3)

// 2. Look for your code (not library code)
at java.util.ArrayList.get(ArrayList.java:437)  ← Library
at com.myapp.UserService.getUser(UserService.java:45)  ← YOUR CODE
at com.myapp.Main.main(Main.java:10)  ← YOUR CODE

// 3. Check exception message
IllegalArgumentException: Age cannot be negative: -5
                          ↑ Tells you what went wrong

// 4. Follow the call chain
main() called method1()
method1() called method2()
method2() called method3()
method3() threw exception

// 5. Add logging for context
try {
    processUser(userId);
} catch (Exception e) {
    logger.error("Failed to process user: " + userId, e);
    throw e;
}
```

### Custom Stack Trace Formatting

```java
public class StackTraceFormatter {
    public static String formatStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName()).append(": ")
          .append(e.getMessage()).append("\n");

        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("  at ").append(element.toString()).append("\n");
        }

        Throwable cause = e.getCause();
        if (cause != null) {
            sb.append("Caused by: ").append(formatStackTrace((Exception) cause));
        }

        return sb.toString();
    }
}
```

---

## Exception Handling Best Practices

### 1. Catch Specific Exceptions

```java
// BAD
try {
    // code
} catch (Exception e) {  // Too broad
    e.printStackTrace();
}

// GOOD
try {
    // code
} catch (FileNotFoundException e) {
    // Handle file not found
} catch (IOException e) {
    // Handle other IO errors
}
```

### 2. Don't Swallow Exceptions

```java
// BAD
try {
    // code
} catch (Exception e) {
    // Nothing - exception lost!
}

// GOOD
try {
    // code
} catch (Exception e) {
    logger.error("Error processing request", e);
    throw new RuntimeException("Failed to process", e);
}
```

### 3. Clean Up Resources

```java
// BAD
FileReader reader = new FileReader("file.txt");
// Use reader
reader.close();  // Might not be called if exception occurs

// GOOD
try (FileReader reader = new FileReader("file.txt")) {
    // Use reader
}  // Automatically closed
```

### 4. Provide Context

```java
// BAD
throw new Exception();

// GOOD
throw new Exception("Failed to process order " + orderId +
                    " for user " + userId);
```

### 5. Don't Use Exceptions for Flow Control

```java
// BAD
try {
    while (true) {
        array[i++];
    }
} catch (ArrayIndexOutOfBoundsException e) {
    // Done
}

// GOOD
for (int i = 0; i < array.length; i++) {
    // Process array[i]
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Exceptions vs Errors | Exceptions are recoverable, errors are not |
| Checked Exceptions | Must be handled or declared (IOException, SQLException) |
| Unchecked Exceptions | Optional to handle (NullPointerException, RuntimeException) |
| try-catch-finally | Handle exceptions, cleanup in finally |
| try-with-resources | Automatic resource management (Java 7+) |
| Custom Exceptions | Extend Exception (checked) or RuntimeException (unchecked) |
| Stack Traces | Read top to bottom for error location and call chain |

## Next Topic

Continue to [Design Patterns](./04-design-patterns.md) to learn about common design patterns in Java.
