# Java Essentials Cheat Sheet

## Basic Syntax

```java
// Single-line comment
/* Multi-line comment */
/** Javadoc comment */

// Package declaration (must be first)
package com.example.project;

// Import statements
import java.util.ArrayList;
import java.util.*;  // Import all from package

// Main class
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

## Data Types

### Primitive Types

```java
// Integer types
byte b = 127;           // 8-bit (-128 to 127)
short s = 32767;        // 16-bit
int i = 2147483647;     // 32-bit (most common)
long l = 9223372036854775807L;  // 64-bit

// Floating-point types
float f = 3.14f;        // 32-bit (need 'f' suffix)
double d = 3.14159;     // 64-bit (default for decimals)

// Other types
boolean bool = true;    // true or false
char c = 'A';          // 16-bit Unicode character

// Type casting
int x = (int) 3.14;    // Explicit cast: 3
double y = 5;          // Implicit cast: 5.0
```

### Reference Types

```java
// Strings
String str = "Hello";
String str2 = new String("World");

// Arrays
int[] arr = {1, 2, 3, 4, 5};
int[] arr2 = new int[10];
String[] names = {"Alice", "Bob", "Charlie"};

// Multi-dimensional arrays
int[][] matrix = {{1, 2}, {3, 4}};
int[][] grid = new int[5][5];
```

## Operators

```java
// Arithmetic
int sum = 5 + 3;        // 8
int diff = 5 - 3;       // 2
int product = 5 * 3;    // 15
int quotient = 5 / 3;   // 1 (integer division)
int remainder = 5 % 3;  // 2 (modulo)

// Increment/Decrement
int x = 5;
x++;    // Post-increment: x = 6
++x;    // Pre-increment: x = 7
x--;    // Post-decrement: x = 6
--x;    // Pre-decrement: x = 5

// Assignment
int a = 10;
a += 5;     // a = a + 5 (15)
a -= 3;     // a = a - 3 (12)
a *= 2;     // a = a * 2 (24)
a /= 4;     // a = a / 4 (6)
a %= 4;     // a = a % 4 (2)

// Comparison
5 == 5      // true (equals)
5 != 3      // true (not equals)
5 > 3       // true
5 < 3       // false
5 >= 5      // true
5 <= 3      // false

// Logical
true && false   // false (AND)
true || false   // true (OR)
!true           // false (NOT)

// Ternary
int max = (a > b) ? a : b;  // If a > b, max = a, else max = b
```

## Control Flow

### If-Else

```java
int score = 85;

if (score >= 90) {
    System.out.println("A");
} else if (score >= 80) {
    System.out.println("B");
} else if (score >= 70) {
    System.out.println("C");
} else {
    System.out.println("F");
}
```

### Switch

```java
// Traditional switch
int day = 3;
switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    default:
        System.out.println("Other day");
}

// Switch expression (Java 14+)
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    default -> "Other day";
};
```

### Loops

```java
// For loop
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}

// Enhanced for loop (for-each)
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}

// While loop
int i = 0;
while (i < 10) {
    System.out.println(i);
    i++;
}

// Do-while loop (executes at least once)
int j = 0;
do {
    System.out.println(j);
    j++;
} while (j < 10);

// Break and continue
for (int k = 0; k < 10; k++) {
    if (k == 5) break;      // Exit loop
    if (k % 2 == 0) continue;  // Skip to next iteration
    System.out.println(k);
}
```

## Classes and Objects

```java
// Class definition
public class Person {
    // Fields (instance variables)
    private String name;
    private int age;
    
    // Static field (class variable)
    private static int population = 0;
    
    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        population++;
    }
    
    // Default constructor
    public Person() {
        this("Unknown", 0);
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }
    }
    
    // Instance method
    public void introduce() {
        System.out.println("Hi, I'm " + name + ", " + age + " years old.");
    }
    
    // Static method
    public static int getPopulation() {
        return population;
    }
    
    // toString method
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

// Using the class
Person person1 = new Person("Alice", 25);
person1.introduce();
System.out.println(Person.getPopulation());
```

## Inheritance

```java
// Parent class
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void makeSound() {
        System.out.println("Some sound");
    }
}

// Child class
public class Dog extends Animal {
    private String breed;
    
    public Dog(String name, String breed) {
        super(name);  // Call parent constructor
        this.breed = breed;
    }
    
    @Override  // Method overriding
    public void makeSound() {
        System.out.println("Woof!");
    }
    
    public void fetch() {
        System.out.println(name + " is fetching!");
    }
}

// Usage
Dog dog = new Dog("Buddy", "Golden Retriever");
dog.makeSound();  // Woof!
dog.fetch();
```

## Interfaces

```java
// Interface definition
public interface Drawable {
    // Abstract method (implicit public abstract)
    void draw();
    
    // Default method (Java 8+)
    default void display() {
        System.out.println("Displaying...");
    }
    
    // Static method (Java 8+)
    static void info() {
        System.out.println("Drawable interface");
    }
}

// Implementing interface
public class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}

// Multiple interfaces
public class Rectangle implements Drawable, Comparable<Rectangle> {
    private int width, height;
    
    @Override
    public void draw() {
        System.out.println("Drawing rectangle");
    }
    
    @Override
    public int compareTo(Rectangle other) {
        return this.width * this.height - other.width * other.height;
    }
}
```

## Abstract Classes

```java
public abstract class Shape {
    protected String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    // Abstract method (must be implemented by subclass)
    public abstract double getArea();
    
    // Concrete method
    public void displayColor() {
        System.out.println("Color: " + color);
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}
```

## Exception Handling

```java
// Try-catch-finally
try {
    int result = 10 / 0;  // ArithmeticException
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero");
    e.printStackTrace();
} finally {
    System.out.println("This always executes");
}

// Multiple catch blocks
try {
    String str = null;
    System.out.println(str.length());  // NullPointerException
} catch (NullPointerException e) {
    System.out.println("Null pointer!");
} catch (Exception e) {
    System.out.println("General exception");
}

// Try-with-resources (auto-close)
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
}

// Throwing exceptions
public void validateAge(int age) throws IllegalArgumentException {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative");
    }
}

// Custom exception
public class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}
```

## Collections Framework

### List

```java
// ArrayList
List<String> list = new ArrayList<>();
list.add("Apple");
list.add("Banana");
list.add(1, "Cherry");  // Insert at index
list.remove("Banana");
list.remove(0);  // Remove by index
String fruit = list.get(0);
int size = list.size();
boolean contains = list.contains("Apple");
list.clear();

// LinkedList
List<Integer> linkedList = new LinkedList<>();
linkedList.add(1);
linkedList.add(2);

// Iteration
for (String item : list) {
    System.out.println(item);
}
```

### Set

```java
// HashSet (unordered, no duplicates)
Set<String> set = new HashSet<>();
set.add("Apple");
set.add("Banana");
set.add("Apple");  // Won't be added (duplicate)
boolean added = set.add("Cherry");

// TreeSet (sorted, no duplicates)
Set<Integer> treeSet = new TreeSet<>();
treeSet.add(5);
treeSet.add(1);
treeSet.add(3);  // Stored in sorted order: 1, 3, 5

// LinkedHashSet (insertion order, no duplicates)
Set<String> linkedSet = new LinkedHashSet<>();
```

### Map

```java
// HashMap
Map<String, Integer> map = new HashMap<>();
map.put("Apple", 1);
map.put("Banana", 2);
map.put("Cherry", 3);
int value = map.get("Apple");
boolean hasKey = map.containsKey("Banana");
boolean hasValue = map.containsValue(2);
map.remove("Cherry");

// Iteration
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// TreeMap (sorted by key)
Map<String, Integer> treeMap = new TreeMap<>();

// LinkedHashMap (insertion order)
Map<String, Integer> linkedMap = new LinkedHashMap<>();
```

### Queue

```java
// LinkedList as Queue
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
String head = queue.peek();  // Get without removing
String removed = queue.poll();  // Get and remove

// PriorityQueue (natural ordering)
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(5);
pq.offer(1);
pq.offer(3);
System.out.println(pq.poll());  // 1 (smallest)
```

## Streams (Java 8+)

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Filter
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());

// Map
List<Integer> squared = numbers.stream()
    .map(n -> n * n)
    .collect(Collectors.toList());

// Reduce
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);

// forEach
numbers.stream()
    .forEach(System.out::println);

// Sorted
List<Integer> sorted = numbers.stream()
    .sorted()
    .collect(Collectors.toList());

// Count
long count = numbers.stream()
    .filter(n -> n > 5)
    .count();

// anyMatch, allMatch, noneMatch
boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
boolean allPositive = numbers.stream().allMatch(n -> n > 0);
boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
```

## Lambda Expressions

```java
// Syntax: (parameters) -> expression or statement

// No parameters
Runnable r = () -> System.out.println("Hello");

// One parameter
Consumer<String> printer = s -> System.out.println(s);
printer.accept("Hello");

// Multiple parameters
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
int result = add.apply(5, 3);

// Block body
Predicate<Integer> isEven = (n) -> {
    return n % 2 == 0;
};

// Method reference
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.forEach(System.out::println);  // Method reference
```

## String Methods

```java
String str = "Hello World";

// Length
int len = str.length();  // 11

// Character at index
char ch = str.charAt(0);  // 'H'

// Substring
String sub = str.substring(0, 5);  // "Hello"
String sub2 = str.substring(6);     // "World"

// Contains
boolean contains = str.contains("World");  // true

// Equals
boolean equals = str.equals("Hello World");  // true
boolean equalsIgnoreCase = str.equalsIgnoreCase("hello world");  // true

// StartsWith / EndsWith
boolean starts = str.startsWith("Hello");  // true
boolean ends = str.endsWith("World");      // true

// Index
int index = str.indexOf("o");      // 4 (first occurrence)
int lastIndex = str.lastIndexOf("o");  // 7

// Replace
String replaced = str.replace("World", "Java");  // "Hello Java"

// Split
String[] words = str.split(" ");  // ["Hello", "World"]

// Trim
String trimmed = "  hello  ".trim();  // "hello"

// Upper/Lower case
String upper = str.toUpperCase();  // "HELLO WORLD"
String lower = str.toLowerCase();  // "hello world"

// Empty check
boolean isEmpty = str.isEmpty();  // false
boolean isBlank = str.isBlank();  // false (Java 11+)

// Join
String joined = String.join(", ", "Apple", "Banana", "Cherry");
// "Apple, Banana, Cherry"

// Format
String formatted = String.format("Name: %s, Age: %d", "Alice", 25);
```

## File I/O

```java
// Reading file
try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}

// Writing file
try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    bw.write("Hello, World!");
    bw.newLine();
    bw.write("Second line");
} catch (IOException e) {
    e.printStackTrace();
}

// Java NIO (Java 7+)
try {
    List<String> lines = Files.readAllLines(Paths.get("input.txt"));
    Files.write(Paths.get("output.txt"), lines);
} catch (IOException e) {
    e.printStackTrace();
}
```

## Common Patterns

```java
// Singleton pattern
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

// Builder pattern
public class Person {
    private String name;
    private int age;
    
    private Person(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }
    
    public static class Builder {
        private String name;
        private int age;
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        
        public Person build() {
            return new Person(this);
        }
    }
}

// Usage
Person person = new Person.Builder()
    .name("Alice")
    .age(25)
    .build();
```

## Quick Reference

```java
// Data types: byte, short, int, long, float, double, boolean, char
// Reference: String, arrays, objects

// Loops: for, while, do-while, for-each
// Conditions: if-else, switch
// Exception: try-catch-finally, throw, throws

// Collections: List, Set, Map, Queue
// ArrayList, HashSet, HashMap, LinkedList

// Access modifiers: public, private, protected, default
// Keywords: static, final, abstract, interface, extends, implements
```

---

**Tips:**
- Use meaningful variable names
- Follow naming conventions (camelCase for variables/methods, PascalCase for classes)
- Always close resources (use try-with-resources)
- Prefer interfaces over concrete classes
- Use collections over arrays when possible
- Handle exceptions properly
