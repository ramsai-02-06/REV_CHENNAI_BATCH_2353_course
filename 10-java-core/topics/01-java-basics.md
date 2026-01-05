# Java Basics

## What Is Java

Java is a high-level, class-based, object-oriented programming language designed to have as few implementation dependencies as possible. It was created by James Gosling at Sun Microsystems (now owned by Oracle) and released in 1995.

### Key Characteristics

- **Platform Independent**: "Write Once, Run Anywhere" (WORA)
- **Object-Oriented**: Everything is an object (except primitives)
- **Secure**: Built-in security features, no explicit pointers
- **Robust**: Strong memory management, exception handling
- **Multithreaded**: Built-in support for concurrent programming
- **High Performance**: JIT compiler optimizes bytecode execution
- **Distributed**: Built-in support for networking and distributed computing

### Java Philosophy

Java was designed with five primary goals:
1. **Simple**: Easy to learn, C++ syntax without complexity
2. **Object-Oriented**: Pure OOP language
3. **Portable**: Platform-independent bytecode
4. **Secure**: Sandbox execution environment
5. **Robust**: Strong type checking, automatic memory management

### Java Evolution

| Version | Year | Key Features |
|---------|------|--------------|
| Java 1.0 | 1996 | Initial release |
| Java 5 | 2004 | [Generics](./02-object-oriented-programming.md#generics-java-5), [Annotations](./02-object-oriented-programming.md#annotations-java-5), enums, autoboxing |
| Java 8 | 2014 | Lambdas, Stream API, default methods |
| Java 11 | 2018 | LTS release, var keyword, HTTP client |
| Java 15 | 2020 | [Text Blocks](#text-blocks-java-15) |
| Java 16 | 2021 | [Records](./02-object-oriented-programming.md#records-java-16) |
| Java 17 | 2021 | LTS release, sealed classes, pattern matching |
| Java 21 | 2023 | LTS release, [Virtual Threads](./09-multithreading.md#virtual-threads-java-21), record patterns |

---

## JVM, JRE, JDK

Understanding the Java platform components is crucial for effective Java development.

### Java Virtual Machine (JVM)

The JVM is an abstract computing machine that enables a computer to run Java programs.

**Key Responsibilities:**
- **Loads** bytecode (.class files)
- **Verifies** bytecode for security
- **Executes** bytecode using interpretation or JIT compilation
- **Manages** memory (garbage collection)
- **Provides** runtime environment

**JVM Architecture:**

```
┌─────────────────────────────────────────────┐
│           Class Loader Subsystem            │
│  (Bootstrap, Extension, Application)        │
├─────────────────────────────────────────────┤
│              Runtime Data Areas             │
│  ┌──────────┬──────────┬──────────────────┐ │
│  │  Method  │   Heap   │  Stack (per      │ │
│  │   Area   │          │   thread)        │ │
│  ├──────────┼──────────┼──────────────────┤ │
│  │    PC    │  Native  │                  │ │
│  │ Register │  Method  │                  │ │
│  │          │  Stack   │                  │ │
│  └──────────┴──────────┴──────────────────┘ │
├─────────────────────────────────────────────┤
│            Execution Engine                 │
│  ┌──────────┬──────────┬──────────────┐    │
│  │ Inter-   │   JIT    │   Garbage    │    │
│  │ preter   │ Compiler │  Collector   │    │
│  └──────────┴──────────┴──────────────┘    │
├─────────────────────────────────────────────┤
│        Native Method Interface (JNI)        │
└─────────────────────────────────────────────┘
```

**Memory Areas:**
- **Heap**: Object storage, shared among threads, GC managed
- **Method Area**: Class metadata, static variables, constant pool
- **Stack**: Method calls, local variables (one per thread)
- **PC Register**: Current instruction address (one per thread)
- **Native Method Stack**: Native method calls

### Java Runtime Environment (JRE)

The JRE provides the libraries, JVM, and other components to run Java applications.

**Components:**
- JVM (Java Virtual Machine)
- Core Libraries (java.lang, java.util, java.io, etc.)
- Supporting Files (property files, configuration)

**Use Case:** Running Java applications (end users)

### Java Development Kit (JDK)

The JDK is a full-featured software development kit for Java, including the JRE plus development tools.

**Components:**
- JRE (Java Runtime Environment)
- Compiler (javac)
- Archiver (jar)
- Documentation Generator (javadoc)
- Debugger (jdb)
- Other development tools

**Use Case:** Developing Java applications (developers)

### Relationship Diagram

```
┌─────────────────────────────────────┐
│              JDK                    │
│  ┌───────────────────────────────┐  │
│  │           JRE                 │  │
│  │  ┌─────────────────────────┐  │  │
│  │  │         JVM             │  │  │
│  │  │                         │  │  │
│  │  └─────────────────────────┘  │  │
│  │  Java Class Libraries         │  │
│  └───────────────────────────────┘  │
│  Development Tools                  │
│  (javac, jar, javadoc, etc.)        │
└─────────────────────────────────────┘
```

### Compilation and Execution Process

```
Source Code (.java)
        ↓
   javac compiler
        ↓
   Bytecode (.class)
        ↓
   Class Loader
        ↓
   Bytecode Verifier
        ↓
   JIT Compiler / Interpreter
        ↓
   Machine Code
        ↓
   Execution
```

### JIT Compiler

The JIT (Just-In-Time) compiler converts frequently executed bytecode to native machine code for better performance.

**How it works:**
1. Initially, bytecode is interpreted (slower but starts immediately)
2. JVM monitors which methods are called frequently ("hot spots")
3. Hot methods are compiled to native machine code
4. Compiled code runs much faster than interpreted bytecode

**Key optimizations:**
- **Method Inlining** - Replaces method calls with actual code
- **Loop Unrolling** - Expands small loops to reduce overhead
- **Dead Code Elimination** - Removes unreachable code
- **Escape Analysis** - Allocates objects on stack when possible

This is why Java applications often get faster the longer they run.

> **Deep Dive:** For detailed JIT examples and JVM internals, see [JVM Internals and Performance](./10-jvm-internals.md).

---

## Setup JDK

Setting up the Java Development Kit is the first step in Java development.

### Installing JDK 21

#### Windows

1. Download JDK 21 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Run the installer (.exe file)
3. Follow installation wizard
4. Set environment variables:
   - `JAVA_HOME`: `C:\Program Files\Java\jdk-21`
   - Add to `Path`: `%JAVA_HOME%\bin`

#### macOS

```bash
# Using Homebrew
brew install openjdk@21

# Add to PATH (add to ~/.zshrc or ~/.bash_profile)
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@21"
```

#### Linux (Ubuntu/Debian)

```bash
# Update package index
sudo apt update

# Install OpenJDK 21
sudo apt install openjdk-21-jdk

# Verify installation
java -version
javac -version
```

### Environment Variables

**JAVA_HOME**: Points to JDK installation directory
```bash
# Linux/macOS (~/.bashrc or ~/.zshrc)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH

# Windows (System Environment Variables)
JAVA_HOME=C:\Program Files\Java\jdk-21
Path=%JAVA_HOME%\bin;%Path%
```

### Verify Installation

```bash
# Check Java version
java -version

# Check compiler version
javac -version

# Check JAVA_HOME
echo $JAVA_HOME    # Linux/macOS
echo %JAVA_HOME%   # Windows
```

Expected output:
```
openjdk version "21.0.1" 2023-10-17
OpenJDK Runtime Environment (build 21.0.1+12-29)
OpenJDK 64-Bit Server VM (build 21.0.1+12-29, mixed mode, sharing)
```

### First Java Program

Create `HelloWorld.java`:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

Compile and run:

```bash
# Compile
javac HelloWorld.java

# Run
java HelloWorld
```

Output:
```
Hello, World!
```

### IDE Setup

#### IntelliJ IDEA (Recommended)

1. Download from [JetBrains](https://www.jetbrains.com/idea/)
2. Install and launch
3. Configure JDK:
   - File > Project Structure > Project SDK
   - Select JDK 21
4. Create new Java project
5. Start coding!

#### Eclipse

1. Download from [Eclipse.org](https://www.eclipse.org/)
2. Install and launch
3. Configure JDK:
   - Window > Preferences > Java > Installed JREs
   - Add JDK 21
4. Create new Java project

#### VS Code

1. Install VS Code
2. Install extensions:
   - Extension Pack for Java
   - Language Support for Java
3. Configure java.home in settings
4. Start developing

---

## Primitive Data Types

Java has 8 primitive data types that hold simple values directly in memory.

### Numeric Types

#### Integer Types

| Type | Size | Range | Default |
|------|------|-------|---------|
| byte | 8-bit | -128 to 127 | 0 |
| short | 16-bit | -32,768 to 32,767 | 0 |
| int | 32-bit | -2³¹ to 2³¹-1 | 0 |
| long | 64-bit | -2⁶³ to 2⁶³-1 | 0L |

```java
byte age = 25;
short year = 2024;
int population = 1000000;
long distance = 9876543210L;  // 'L' suffix for long literals
```

#### Floating-Point Types

| Type | Size | Precision | Default |
|------|------|-----------|---------|
| float | 32-bit | ~7 decimal digits | 0.0f |
| double | 64-bit | ~15 decimal digits | 0.0d |

```java
float price = 19.99f;      // 'f' suffix required
double pi = 3.14159265359;  // Default for decimal literals
double scientific = 1.23e-4;  // Scientific notation
```

### Character Type

| Type | Size | Range | Default |
|------|------|-------|---------|
| char | 16-bit | 0 to 65,535 (Unicode) | '\u0000' |

```java
char grade = 'A';
char unicode = '\u0041';  // Also 'A'
char newline = '\n';       // Escape sequence
char tab = '\t';
```

### Boolean Type

| Type | Size | Values | Default |
|------|------|--------|---------|
| boolean | 1-bit* | true, false | false |

*JVM dependent, typically 1 byte

```java
boolean isJavaFun = true;
boolean isComplete = false;
boolean result = (5 > 3);  // true
```

### Type Ranges Summary

```java
public class PrimitiveRanges {
    public static void main(String[] args) {
        System.out.println("byte: " + Byte.MIN_VALUE + " to " + Byte.MAX_VALUE);
        System.out.println("short: " + Short.MIN_VALUE + " to " + Short.MAX_VALUE);
        System.out.println("int: " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE);
        System.out.println("long: " + Long.MIN_VALUE + " to " + Long.MAX_VALUE);
        System.out.println("float: " + Float.MIN_VALUE + " to " + Float.MAX_VALUE);
        System.out.println("double: " + Double.MIN_VALUE + " to " + Double.MAX_VALUE);
        System.out.println("char: " + (int)Character.MIN_VALUE + " to " + (int)Character.MAX_VALUE);
    }
}
```

### Literals

```java
// Integer literals
int decimal = 100;
int binary = 0b1100100;    // Binary (Java 7+)
int octal = 0144;          // Octal
int hex = 0x64;            // Hexadecimal

// Underscore in numeric literals (Java 7+)
int million = 1_000_000;
long creditCard = 1234_5678_9012_3456L;

// Floating-point literals
double d1 = 123.4;
double d2 = 1.234e2;       // Scientific notation
float f = 123.4f;

// Character literals
char c1 = 'A';
char c2 = '\u0041';        // Unicode
char c3 = 65;              // ASCII value

// Boolean literals
boolean t = true;
boolean f = false;
```

### Type Conversion and Casting

#### Implicit Conversion (Widening)

Automatic conversion when no data loss occurs:

```java
byte b = 10;
int i = b;          // byte → int (widening)
long l = i;         // int → long
float f = l;        // long → float
double d = f;       // float → double

// Widening hierarchy
byte → short → int → long → float → double
       char  ↗
```

#### Explicit Casting (Narrowing)

Manual conversion when data loss may occur:

```java
double d = 100.04;
long l = (long) d;      // 100 (fractional part lost)
int i = (int) l;        // 100
short s = (short) i;    // 100
byte b = (byte) s;      // 100

// Overflow example
int big = 130;
byte small = (byte) big;  // -126 (overflow!)
```

#### Promotion in Expressions

```java
byte b = 10;
byte c = 20;
// byte sum = b + c;     // Compile error!
int sum = b + c;         // OK - promoted to int

// Mixed type expressions
int i = 10;
double d = 3.14;
double result = i + d;   // int promoted to double
```

### Best Practices

1. **Choose appropriate type**: Use `int` for integers unless you need `long`
2. **Use `double` for decimals**: More precise than `float`
3. **Be careful with narrowing**: May lose data or overflow
4. **Use underscores**: Make large numbers readable (`1_000_000`)
5. **Avoid unnecessary casting**: Prefer widening conversions

---

## Reference Variables

Unlike primitives, reference variables hold memory addresses pointing to objects.

### Primitives vs References

| Primitive | Reference |
|-----------|-----------|
| Stores actual value | Stores memory address |
| Fixed size | Variable size |
| On stack | Object on heap, reference on stack |
| Cannot be null | Can be null |
| No methods | Has methods |

```java
// Primitive - stores value directly
int x = 10;

// Reference - stores address of object
String name = "Java";  // Reference to String object
```

### Object Creation

```java
// Using 'new' keyword
String str1 = new String("Hello");

// String literal (special case - uses String pool)
String str2 = "Hello";

// Array (also reference type)
int[] numbers = new int[5];
```

### The String Class

String is the most commonly used reference type in Java.

#### String Creation

```java
// String pool (preferred)
String s1 = "Hello";
String s2 = "Hello";      // Same object as s1

// Heap
String s3 = new String("Hello");  // Different object

// Comparison
System.out.println(s1 == s2);      // true (same reference)
System.out.println(s1 == s3);      // false (different references)
System.out.println(s1.equals(s3)); // true (same content)
```

#### String Immutability

Strings are immutable - cannot be changed after creation.

```java
String s = "Hello";
s = s + " World";    // Creates NEW string object
                     // Original "Hello" unchanged

// This creates 3 String objects!
String result = "a" + "b" + "c";
```

#### Common String Methods

```java
String text = "Hello, World!";

// Length and character access
int len = text.length();              // 13
char ch = text.charAt(0);             // 'H'

// Substring
String sub = text.substring(0, 5);    // "Hello"

// Case conversion
String upper = text.toUpperCase();    // "HELLO, WORLD!"
String lower = text.toLowerCase();    // "hello, world!"

// Searching
int index = text.indexOf("World");    // 7
boolean contains = text.contains("lo"); // true
boolean starts = text.startsWith("Hello"); // true

// Replacement
String replaced = text.replace("World", "Java"); // "Hello, Java!"

// Splitting
String[] words = text.split(", ");    // ["Hello", "World!"]

// Trimming
String padded = "  Java  ";
String trimmed = padded.trim();       // "Java"

// Comparison
boolean equals = text.equals("Hello, World!");  // true
boolean equalsIgnore = text.equalsIgnoreCase("hello, world!"); // true
```

#### StringBuilder and StringBuffer

For mutable strings, use StringBuilder or StringBuffer.

```java
// StringBuilder (not thread-safe, faster)
StringBuilder sb = new StringBuilder("Hello");
sb.append(" World");      // Modifies same object
sb.insert(5, ",");        // "Hello, World"
sb.delete(5, 6);          // "Hello World"
sb.reverse();             // "dlroW olleH"
String result = sb.toString();

// StringBuffer (thread-safe, slower)
StringBuffer sbf = new StringBuffer("Thread-safe");
sbf.append(" string");
```

**When to use:**
- **String**: Immutable text, few modifications
- **StringBuilder**: Single-threaded, many modifications
- **StringBuffer**: Multi-threaded, many modifications

#### Text Blocks (Java 15+)

Text blocks simplify multi-line strings with `"""` delimiters.

```java
// Traditional multi-line string
String html = "<html>\n" +
              "    <body>\n" +
              "        <h1>Hello</h1>\n" +
              "    </body>\n" +
              "</html>";

// Text block - cleaner and preserves formatting
String htmlBlock = """
        <html>
            <body>
                <h1>Hello</h1>
            </body>
        </html>
        """;

// JSON example
String json = """
        {
            "name": "John",
            "age": 30,
            "city": "New York"
        }
        """;

// SQL query
String sql = """
        SELECT id, name, email
        FROM users
        WHERE status = 'active'
        ORDER BY name
        """;
```

**Key features:**
- Opening `"""` must be followed by newline
- Closing `"""` position controls trailing newline
- Incidental whitespace is stripped based on closing delimiter
- Supports escape sequences and string interpolation

```java
// Escape sequences work
String withQuotes = """
        He said "Hello"
        Line with \t tab
        """;

// No trailing newline (closing """ on last line)
String noNewline = """
        No trailing newline""";
```

> **Deep Dive:** For more on Records (Java 16+), see [Object-Oriented Programming](./02-object-oriented-programming.md#records-java-16).

### Wrapper Classes

Wrapper classes provide object representation of primitives.

| Primitive | Wrapper Class |
|-----------|---------------|
| byte | Byte |
| short | Short |
| int | Integer |
| long | Long |
| float | Float |
| double | Double |
| char | Character |
| boolean | Boolean |

#### Autoboxing and Unboxing

Java automatically converts between primitives and wrappers.

```java
// Autoboxing (primitive → wrapper)
Integer i = 10;              // Auto: Integer.valueOf(10)
Double d = 3.14;             // Auto: Double.valueOf(3.14)
Boolean b = true;            // Auto: Boolean.valueOf(true)

// Unboxing (wrapper → primitive)
int x = i;                   // Auto: i.intValue()
double y = d;                // Auto: d.doubleValue()
boolean z = b;               // Auto: b.booleanValue()

// In expressions
Integer a = 10;
Integer b = 20;
Integer sum = a + b;         // Unbox, add, autobox
```

#### Wrapper Class Methods

```java
// Parsing strings to primitives
int num = Integer.parseInt("123");
double price = Double.parseDouble("19.99");
boolean flag = Boolean.parseBoolean("true");

// Converting primitives to strings
String s1 = Integer.toString(100);
String s2 = String.valueOf(100);

// Comparing values
Integer x = 100;
Integer y = 100;
System.out.println(x == y);         // true (cached)

Integer a = 200;
Integer b = 200;
System.out.println(a == b);         // false (not cached)
System.out.println(a.equals(b));    // true (use this!)

// Constants
int max = Integer.MAX_VALUE;        // 2147483647
int min = Integer.MIN_VALUE;        // -2147483648
```

#### Integer Caching

```java
// Cached range: -128 to 127
Integer a = 100;
Integer b = 100;
System.out.println(a == b);  // true (same cached object)

// Outside cache
Integer x = 200;
Integer y = 200;
System.out.println(x == y);  // false (different objects)

// Always use .equals() for wrapper comparison!
System.out.println(x.equals(y));  // true
```

### Null Values

Reference variables can be `null` (no object).

```java
String str = null;
Integer num = null;

// NullPointerException
// System.out.println(str.length());  // Runtime error!

// Null-safe checks
if (str != null) {
    System.out.println(str.length());
}

// Java 8+ Optional (covered later)
String safe = Optional.ofNullable(str).orElse("default");
```

### Best Practices

1. **Use primitives for performance**: When possible
2. **Use String literals**: For string pool efficiency
3. **Use StringBuilder**: For string concatenation in loops
4. **Always use .equals()**: For wrapper and String comparison
5. **Check for null**: Before calling methods on references
6. **Use wrapper constants**: `Integer.MAX_VALUE` instead of magic numbers

---

## Control Flow

Control flow statements determine the order in which code executes.

### Conditional Statements

#### if Statement

```java
int age = 18;

if (age >= 18) {
    System.out.println("Adult");
}
```

#### if-else Statement

```java
int score = 75;

if (score >= 60) {
    System.out.println("Pass");
} else {
    System.out.println("Fail");
}
```

#### if-else-if Ladder

```java
int marks = 85;

if (marks >= 90) {
    System.out.println("Grade: A");
} else if (marks >= 80) {
    System.out.println("Grade: B");
} else if (marks >= 70) {
    System.out.println("Grade: C");
} else if (marks >= 60) {
    System.out.println("Grade: D");
} else {
    System.out.println("Grade: F");
}
```

#### Nested if

```java
int age = 25;
boolean hasLicense = true;

if (age >= 18) {
    if (hasLicense) {
        System.out.println("Can drive");
    } else {
        System.out.println("Need license");
    }
} else {
    System.out.println("Too young");
}
```

#### Ternary Operator

```java
// condition ? valueIfTrue : valueIfFalse
int age = 20;
String status = (age >= 18) ? "Adult" : "Minor";

// Nested ternary (use sparingly!)
int score = 85;
String grade = (score >= 90) ? "A" :
               (score >= 80) ? "B" :
               (score >= 70) ? "C" : "F";
```

### Switch Statements

#### Traditional Switch

```java
int day = 3;
String dayName;

switch (day) {
    case 1:
        dayName = "Monday";
        break;
    case 2:
        dayName = "Tuesday";
        break;
    case 3:
        dayName = "Wednesday";
        break;
    case 4:
        dayName = "Thursday";
        break;
    case 5:
        dayName = "Friday";
        break;
    case 6:
    case 7:
        dayName = "Weekend";
        break;
    default:
        dayName = "Invalid day";
}
```

#### Switch Expressions (Java 14+)

```java
int day = 3;
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    case 4 -> "Thursday";
    case 5 -> "Friday";
    case 6, 7 -> "Weekend";
    default -> "Invalid day";
};
```

#### Switch with yield (Java 14+)

```java
int month = 2;
int year = 2024;

int days = switch (month) {
    case 1, 3, 5, 7, 8, 10, 12 -> 31;
    case 4, 6, 9, 11 -> 30;
    case 2 -> {
        if (year % 4 == 0) {
            yield 29;
        } else {
            yield 28;
        }
    }
    default -> 0;
};
```

#### Pattern Matching for Switch (Java 21+)

```java
Object obj = "Hello";

String result = switch (obj) {
    case String s -> "String of length " + s.length();
    case Integer i -> "Integer: " + i;
    case Double d -> "Double: " + d;
    case null -> "Null value";
    default -> "Unknown type";
};
```

### Loops

#### for Loop

```java
// Basic for loop
for (int i = 0; i < 5; i++) {
    System.out.println("Count: " + i);
}

// Multiple variables
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.println(i + " " + j);
}

// Infinite loop
for (;;) {
    // Loop forever (use break to exit)
}
```

#### Enhanced for Loop (for-each)

```java
int[] numbers = {1, 2, 3, 4, 5};

for (int num : numbers) {
    System.out.println(num);
}

// With collections
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
for (String name : names) {
    System.out.println(name);
}
```

#### while Loop

```java
int count = 0;

while (count < 5) {
    System.out.println("Count: " + count);
    count++;
}

// Infinite loop
while (true) {
    // Loop forever (use break to exit)
}
```

#### do-while Loop

```java
int num = 0;

do {
    System.out.println("Number: " + num);
    num++;
} while (num < 5);

// Executes at least once
int x = 10;
do {
    System.out.println("Executes once");
} while (x < 5);
```

### Loop Control Statements

#### break

```java
// Exit loop early
for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // Exit loop when i is 5
    }
    System.out.println(i);
}
// Output: 0 1 2 3 4

// Labeled break
outer: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) {
            break outer;  // Break from outer loop
        }
        System.out.println(i + "," + j);
    }
}
```

#### continue

```java
// Skip current iteration
for (int i = 0; i < 5; i++) {
    if (i == 2) {
        continue;  // Skip when i is 2
    }
    System.out.println(i);
}
// Output: 0 1 3 4

// Labeled continue
outer: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) {
            continue outer;  // Continue outer loop
        }
        System.out.println(i + "," + j);
    }
}
```

### Common Patterns

```java
// Sum of numbers
int sum = 0;
for (int i = 1; i <= 10; i++) {
    sum += i;
}

// Factorial
int factorial = 1;
int n = 5;
for (int i = 1; i <= n; i++) {
    factorial *= i;
}

// Find element in array
int[] arr = {10, 20, 30, 40, 50};
int target = 30;
boolean found = false;

for (int num : arr) {
    if (num == target) {
        found = true;
        break;
    }
}

// Count occurrences
String text = "hello world";
int count = 0;
for (char c : text.toCharArray()) {
    if (c == 'l') {
        count++;
    }
}
```

---

## Commenting

Comments are non-executable text that explain code.

### Single-Line Comments

```java
// This is a single-line comment
int age = 25;  // Age in years

// Comments can span multiple lines
// by using multiple single-line comments
// like this
```

### Multi-Line Comments

```java
/*
 * This is a multi-line comment.
 * It can span multiple lines.
 * Useful for longer explanations.
 */
int calculate() {
    /* This works inside methods too */
    return 42;
}

/*
Multi-line comments can also
be written without the asterisks,
but it's less common.
*/
```

### Javadoc Comments

Javadoc comments generate HTML documentation.

```java
/**
 * Represents a person with name and age.
 * This class demonstrates proper Javadoc usage.
 *
 * @author John Doe
 * @version 1.0
 * @since 2024-01-01
 */
public class Person {

    /**
     * The person's name.
     */
    private String name;

    /**
     * The person's age in years.
     */
    private int age;

    /**
     * Constructs a new Person with the given name and age.
     *
     * @param name the person's name (must not be null)
     * @param age the person's age (must be positive)
     * @throws IllegalArgumentException if age is negative
     */
    public Person(String name, int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.name = name;
        this.age = age;
    }

    /**
     * Gets the person's name.
     *
     * @return the name of this person
     */
    public String getName() {
        return name;
    }

    /**
     * Calculates if the person is an adult.
     * An adult is someone who is 18 years or older.
     *
     * @return {@code true} if age >= 18, {@code false} otherwise
     */
    public boolean isAdult() {
        return age >= 18;
    }

    /**
     * Compares two Person objects for equality.
     *
     * @param obj the object to compare with
     * @return true if objects are equal, false otherwise
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        // Implementation
        return false;
    }
}
```

### Common Javadoc Tags

| Tag | Description | Usage |
|-----|-------------|-------|
| @author | Author name | Class level |
| @version | Version number | Class level |
| @since | When introduced | Class/method |
| @param | Parameter description | Method |
| @return | Return value | Method |
| @throws | Exception thrown | Method |
| @see | Reference to other elements | Any |
| @deprecated | Mark as deprecated | Any |

### Generating Javadoc

```bash
# Generate documentation
javadoc -d docs *.java

# With custom options
javadoc -d docs -author -version -private *.java
```

### Best Practices

#### DO: Write Clear Comments

```java
// Good: Explains WHY
// Using binary search because array is sorted
int index = binarySearch(arr, target);

// Good: Complex logic explanation
/*
 * We use a two-pointer approach here to avoid nested loops.
 * This reduces time complexity from O(n²) to O(n).
 */
```

#### DON'T: State the Obvious

```java
// Bad: Redundant
int age = 25;  // Set age to 25

// Bad: Obvious
i++;  // Increment i

// Bad: Useless
// This is a variable
int count = 0;
```

#### DO: Update Comments

```java
// Bad: Outdated comment
// Returns age in months
public int getAge() {
    return ageInYears;  // Actually returns years!
}

// Good: Accurate
// Returns age in years
public int getAge() {
    return ageInYears;
}
```

#### DO: Use TODO Comments

```java
// TODO: Implement input validation
// TODO: Optimize this algorithm
// FIXME: This breaks when input is negative
// HACK: Temporary workaround for bug #123
```

### Comment Style Guide

```java
/**
 * Class-level Javadoc comment.
 * Describes the class purpose and usage.
 */
public class Example {

    // Use single-line for brief field comments
    private static final int MAX_SIZE = 100;

    /*
     * Use multi-line for detailed explanations
     * that span multiple lines.
     */
    private void complexMethod() {
        // Single-line comments for inline explanations
        int result = calculate();

        /* Multi-line for code blocks */
        /*
        if (debug) {
            System.out.println("Debug info");
        }
        */
    }

    /**
     * Javadoc for public methods.
     *
     * @param input the input value
     * @return the processed result
     */
    public int publicMethod(int input) {
        return input * 2;
    }
}
```

---

## Packages And Imports

Packages organize classes into namespaces, preventing naming conflicts and improving maintainability.

### What Are Packages?

Packages are containers for classes, similar to folders for files.

**Benefits:**
- **Organization**: Group related classes
- **Namespace**: Avoid naming conflicts
- **Access Control**: Package-private access level
- **Modularity**: Easier to maintain and reuse

### Package Declaration

```java
// First statement in a Java file (before imports)
package com.company.project.module;

public class MyClass {
    // Class code
}
```

### Naming Conventions

```java
// Reverse domain name notation
package com.google.android;
package org.apache.commons;
package edu.mit.ai;

// Lowercase only
package com.mycompany.myproject;  // Good
package com.MyCompany.MyProject;  // Bad

// Hierarchical structure
package com.company.project.models;
package com.company.project.services;
package com.company.project.controllers;
```

### Directory Structure

```
src/
└── com/
    └── company/
        └── project/
            ├── Main.java
            ├── models/
            │   ├── User.java
            │   └── Product.java
            ├── services/
            │   └── UserService.java
            └── utils/
                └── StringUtils.java
```

**File locations:**
- `com.company.project.Main` → `src/com/company/project/Main.java`
- `com.company.project.models.User` → `src/com/company/project/models/User.java`

### Import Statements

Import statements allow you to use classes from other packages.

#### Single Type Import

```java
package com.example.app;

import java.util.ArrayList;
import java.util.HashMap;
import com.company.project.models.User;

public class Example {
    ArrayList<String> list = new ArrayList<>();
    HashMap<String, User> map = new HashMap<>();
}
```

#### Import on Demand (Wildcard)

```java
package com.example.app;

import java.util.*;  // All classes from java.util

public class Example {
    ArrayList<String> list = new ArrayList<>();
    HashMap<String, Integer> map = new HashMap<>();
    // But NOT sub-packages: java.util.concurrent.*
}
```

**Note:** Wildcard imports don't include sub-packages!

```java
import java.util.*;              // Imports java.util classes
// import java.util.concurrent.*;  // Need separate import for sub-package
```

#### Static Imports

Import static members (methods, fields) to use without class name.

```java
// Without static import
import java.lang.Math;

public class Example {
    double result = Math.sqrt(Math.PI);
}

// With static import
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

public class Example {
    double result = sqrt(PI);  // Cleaner!
}

// Wildcard static import
import static java.lang.Math.*;

public class Example {
    double result = sqrt(PI);
    double rounded = round(3.7);
    int max = max(10, 20);
}
```

### Default Imports

Some packages are automatically imported:

```java
// Always imported automatically
java.lang.*  // String, System, Math, etc.

// No need to import:
String s = "Hello";
System.out.println(s);
Math.sqrt(16);
```

### Package Access

```java
package com.example.models;

public class User {
    public String name;        // Accessible everywhere
    protected int age;         // Package + subclasses
    String email;              // Package-private (default)
    private String password;   // Class only
}
```

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| public | ✓ | ✓ | ✓ | ✓ |
| protected | ✓ | ✓ | ✓ | ✗ |
| (default) | ✓ | ✓ | ✗ | ✗ |
| private | ✓ | ✗ | ✗ | ✗ |

### Fully Qualified Names

Using full package path without imports:

```java
// Instead of importing
public class Example {
    java.util.ArrayList<String> list = new java.util.ArrayList<>();
    java.util.Date date = new java.util.Date();
}

// Useful for name conflicts
import java.util.Date;

public class Example {
    java.util.Date utilDate = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
}
```

### Common Java Packages

```java
// Core packages
import java.lang.*;      // Auto-imported (String, System, Math, etc.)
import java.util.*;      // Collections, Date, Random, etc.
import java.io.*;        // Input/Output (File, Stream, etc.)
import java.nio.*;       // New I/O (Path, Files, etc.)

// Networking
import java.net.*;       // URL, Socket, etc.

// Database
import java.sql.*;       // JDBC (Connection, Statement, etc.)

// Time
import java.time.*;      // LocalDate, Duration, etc.

// Concurrency
import java.util.concurrent.*;  // ExecutorService, Future, etc.

// Streams
import java.util.stream.*;      // Stream, Collectors, etc.
```

### Package Examples

#### Example 1: Basic Package Structure

```java
// File: src/com/company/models/Product.java
package com.company.models;

public class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

// File: src/com/company/services/ShoppingCart.java
package com.company.services;

import com.company.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotal() {
        return products.stream()
            .mapToDouble(Product::getPrice)
            .sum();
    }
}

// File: src/com/company/Main.java
package com.company;

import com.company.models.Product;
import com.company.services.ShoppingCart;

public class Main {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("Laptop", 999.99));
        cart.addProduct(new Product("Mouse", 29.99));

        System.out.println("Total: $" + cart.getTotal());
    }
}
```

### Compilation and Execution

```bash
# Compile with package structure
javac -d bin src/com/company/models/Product.java
javac -d bin -cp bin src/com/company/services/ShoppingCart.java
javac -d bin -cp bin src/com/company/Main.java

# Run (use fully qualified class name)
java -cp bin com.company.Main

# Or compile all at once
javac -d bin src/com/company/**/*.java
java -cp bin com.company.Main
```

### Best Practices

1. **One public class per file**: File name must match class name
2. **Package matches directory**: Package structure reflects folder structure
3. **Lowercase package names**: `com.company.project`, not `com.Company.Project`
4. **Meaningful names**: `com.company.ecommerce.models` is better than `com.company.a.b`
5. **Use specific imports**: Prefer `import java.util.ArrayList` over `import java.util.*`
6. **Organize by feature**: Group by functionality, not type
   - Good: `com.company.user`, `com.company.product`
   - Bad: `com.company.models`, `com.company.services` (unless very small project)

---

## Summary

| Concept | Key Points |
|---------|------------|
| Java Platform | JVM (runs bytecode), JRE (runtime), JDK (development) |
| JIT Compiler | Adaptive optimization, method inlining, platform-specific code generation |
| Primitive Types | 8 types: byte, short, int, long, float, double, char, boolean |
| Reference Types | Objects, strings, arrays - store addresses, can be null |
| Control Flow | if/else, switch, for, while, do-while, break, continue |
| Comments | //, /* */, /** */ for Javadoc documentation |
| Packages | Organize classes, prevent conflicts, use reverse domain naming |

## Next Topic

Continue to [Object-Oriented Programming](./02-object-oriented-programming.md) to learn about classes, objects, memory management, and OOP principles.
