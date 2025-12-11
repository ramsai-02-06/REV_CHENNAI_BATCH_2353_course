# Core Java (Java 21)

## Overview
This comprehensive module covers Core Java from fundamentals to advanced concepts. You'll learn object-oriented programming, collections, streams, multithreading, functional programming, and design patterns using Java 21.

## Learning Objectives
By the end of this module, you will be able to:
- Write Java programs using OOP principles
- Work with Java collections and generics
- Utilize functional programming features
- Implement multithreaded applications
- Apply common design patterns
- Use Java Stream API for data processing
- Handle exceptions effectively

## Module Structure

This module is organized into subdirectories:
- [Basics](./basics/) - Java fundamentals and syntax
- [OOP](./oop/) - Object-Oriented Programming
- [Collections](./collections/) - Collections Framework
- [Streams](./streams/) - Stream API and functional operations
- [Multithreading](./multithreading/) - Concurrent programming
- [Functional Programming](./functional-programming/) - Lambdas and functional interfaces
- [Design Patterns](./design-patterns/) - Common design patterns
- [Exercises](./exercises/) - Practice problems

## Topics Covered

### Week 3: Java Basics
#### What Is Java
- Java platform overview
- JVM, JRE, JDK
- Java features and advantages
- Java versions and evolution to Java 21

#### JVM JRE JDK
- Java Virtual Machine architecture
- Java Runtime Environment
- Java Development Kit components
- Compilation and execution process

#### Setup JDK
- Installing JDK 21
- Setting up PATH and JAVA_HOME
- IDE setup (IntelliJ IDEA, Eclipse)
- First Java program

#### Primitive Data Types
- byte, short, int, long
- float, double
- char, boolean
- Type conversion and casting

#### Reference Variables
- Objects and references
- String class
- Wrapper classes
- Autoboxing and unboxing

#### Control Flow Conditional Statements
- if, if-else, if-else-if
- switch statements (including new switch expressions)
- Ternary operator

#### Control Flow Loops
- for loop
- while loop
- do-while loop
- Enhanced for loop
- break and continue statements

#### Commenting
- Single-line comments
- Multi-line comments
- Javadoc comments
- Documentation best practices

#### Packages And Imports
- Creating packages
- Package naming conventions
- Import statements
- Static imports
- Access across packages

### Week 4: Advanced Java Concepts

#### OOPs (Object-Oriented Programming)
**Introduction To Oop**
- Classes and objects
- Encapsulation
- Inheritance
- Polymorphism
- Abstraction

**Classes Vs Objects**
- Class definition
- Object instantiation
- Instance vs class members

**Classes Members**
- Fields/attributes
- Methods
- Constructors
- Initializer blocks

**Static Members**
- Static variables
- Static methods
- Static blocks
- When to use static

**Oop Inheritance**
- extends keyword
- super keyword
- Method overriding
- Inheritance hierarchies

**Interfaces And Abstract Class**
- Abstract classes
- Interfaces
- Multiple inheritance through interfaces
- Default and static methods in interfaces
- When to use which

**Non Access Modifiers**
- final
- abstract
- static
- synchronized
- volatile
- transient

**Equality Hashcode Equals**
- == vs equals()
- Contract between equals() and hashCode()
- Implementing equals() and hashCode()
- Objects.equals() and Objects.hash()

**Garbage Collection**
- Automatic memory management
- How GC works
- finalize() method
- Memory leaks in Java

#### Exceptions
**Exceptions Vs Errors And Hierarchy**
- Throwable hierarchy
- Checked vs unchecked exceptions
- Error vs Exception
- Common exception types

**Handling Exceptions**
- try-catch blocks
- Multiple catch blocks
- finally block
- try-with-resources

**Checked Vs Unchecked Exceptions**
- When to use checked exceptions
- Runtime exceptions
- Best practices

**Creating Custom Exceptions**
- Extending Exception class
- Custom error messages
- Exception chaining

**Reading The Stack Trace**
- Understanding stack traces
- Debugging with stack traces
- Root cause analysis

#### Collections
**Overview Of Collections Hierarchy**
- Collection interface
- List, Set, Queue, Map interfaces
- Implementation classes

**List Interface & Implementation Classes**
- ArrayList
- LinkedList
- Vector
- When to use each

**Set Interface & Implementation Classes**
- HashSet
- LinkedHashSet
- TreeSet
- Set operations

**Queue Interface & Implementation Classes**
- PriorityQueue
- Deque
- ArrayDeque
- LinkedList as Queue

**Iterators**
- Iterator interface
- ListIterator
- Enhanced for loop
- forEach method

**Map Interface & Implementation Classes**
- HashMap
- LinkedHashMap
- TreeMap
- Hashtable
- Map operations

#### Multithreading
**thread-class**
- Thread class overview
- Creating threads by extending Thread

**runnable-interface**
- Runnable interface
- Lambda expressions with Runnable
- Thread vs Runnable

**states-of-a-thread**
- New, Runnable, Running, Blocked, Waiting, Terminated
- Thread lifecycle

**multithreading**
- Race conditions
- Thread interference
- Memory consistency errors

**Concurrency**
- synchronized keyword
- Locks and monitors
- volatile keyword

**synchronization**
- Method synchronization
- Block synchronization
- Static synchronization

**deadlock**
- What is deadlock?
- Deadlock conditions
- Preventing deadlock

**livelock**
- Livelock vs deadlock
- Livelock scenarios

**producer-consumer-problem**
- wait() and notify()
- Producer-consumer implementation
- BlockingQueue solution

#### Functional Programming
**functional-interfaces**
- @FunctionalInterface annotation
- Predicate, Function, Consumer, Supplier
- BiFunction, BiPredicate, etc.

**lambdas**
- Lambda syntax
- Type inference
- Method references
- Constructor references

**method-reference-syntax**
- Static method references
- Instance method references
- Constructor references

**Optional-class**
- Creating Optional objects
- isPresent(), ifPresent()
- orElse(), orElseGet(), orElseThrow()
- map() and flatMap()

#### File Handling and I/O
**reading-and-writing-to-files**
- File class
- FileReader and FileWriter
- BufferedReader and BufferedWriter
- Try-with-resources

### Week 5: Advanced Topics

#### Stream API
**stream-api**
- What are streams?
- Stream pipeline
- Intermediate vs terminal operations

**Date and Time API**
- LocalDate, LocalTime, LocalDateTime
- ZonedDateTime
- Period and Duration
- Formatting and parsing

**reflection-api**
- Class object
- Inspecting classes, methods, fields
- Dynamic invocation
- Annotations at runtime

**Intermediate Operations**
- filter, map, flatMap
- distinct, sorted
- peek, limit, skip

**Terminal Operations**
- forEach
- collect (Collectors)
- reduce
- count, min, max
- anyMatch, allMatch, noneMatch

**Collectors**
- toList, toSet, toMap
- joining
- groupingBy, partitioningBy
- Custom collectors

**Parallel Streams**
- parallelStream()
- Performance considerations
- Thread safety

#### Design Patterns
**SOLID Principles**
- Single Responsibility
- Open/Closed
- Liskov Substitution
- Interface Segregation
- Dependency Inversion

**Singleton Pattern**
- Implementation approaches
- Thread-safe singleton
- Enum singleton

**Factory Pattern**
- Simple Factory
- Factory Method
- Abstract Factory

**Behavioral Pattern**
- Strategy Pattern
- Observer Pattern
- Command Pattern

**Structural Pattern**
- Adapter Pattern
- Decorator Pattern
- Facade Pattern

## Java 17 & 21 Features

### Text Blocks, Records, Virtual Threads
**Sealed Classes, Pattern Matching**
- Sealed classes and interfaces
- Pattern matching for switch
- Record patterns

**JDBC**
- Database connectivity (covered in separate module)

## Key Concepts

### Object-Oriented Principles
- **Encapsulation**: Hide internal details, expose through methods
- **Inheritance**: IS-A relationship, code reuse
- **Polymorphism**: One interface, multiple implementations
- **Abstraction**: Hide complex implementation, show only necessary details

### SOLID Principles
- **S**: Single Responsibility - One class, one purpose
- **O**: Open/Closed - Open for extension, closed for modification
- **L**: Liskov Substitution - Subtypes must be substitutable for base types
- **I**: Interface Segregation - Many specific interfaces better than one general
- **D**: Dependency Inversion - Depend on abstractions, not concretions

## Code Examples

### Basic Class Structure
```java
public class Person {
    // Fields
    private String name;
    private int age;

    // Constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Methods
    public void introduce() {
        System.out.println("Hi, I'm " + name + " and I'm " + age + " years old.");
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

### Stream API Example
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Filter and transform
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());

// Grouping
Map<Integer, List<String>> groupedByLength = names.stream()
    .collect(Collectors.groupingBy(String::length));
```

### Multithreading Example
```java
// Using Runnable with lambda
Thread thread = new Thread(() -> {
    for (int i = 0; i < 5; i++) {
        System.out.println("Count: " + i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
});
thread.start();
```

## Exercises

See the [exercises](./exercises/) directory for:
- Basic Java syntax problems
- OOP design exercises
- Collection manipulation tasks
- Stream API challenges
- Multithreading problems
- Design pattern implementations

## Additional Resources

### Official Documentation
- [Java SE 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [JDK 21 Release Notes](https://www.oracle.com/java/technologies/javase/21-relnotes.html)

### Books
- "Effective Java" by Joshua Bloch
- "Java: The Complete Reference" by Herbert Schildt
- "Head First Design Patterns" by Freeman & Freeman
- "Java Concurrency in Practice" by Brian Goetz

### Online Resources
- [Baeldung Java Tutorials](https://www.baeldung.com/)
- [Java Code Geeks](https://www.javacodegeeks.com/)
- [Oracle Java Magazine](https://blogs.oracle.com/javamagazine/)

## Assessment

You should be comfortable with:
- [ ] Writing Java classes with proper OOP principles
- [ ] Working with all collection types effectively
- [ ] Creating and managing threads
- [ ] Using Stream API for data processing
- [ ] Implementing common design patterns
- [ ] Handling exceptions appropriately
- [ ] Understanding Java memory management
- [ ] Using Java 21 features

## Projects Using This Module
- [Project 1: Java MySQL JDBC Application](../../projects/project-1-java-mysql-jdbc/)

## Next Steps

After mastering Core Java, proceed to:
- [Module 11: JDBC](../11-jdbc/)
- [Module 12: Testing (JUnit & Mockito)](../12-testing/)

---

**Time Estimate:** 2.5 weeks
**Difficulty:** Intermediate to Advanced
**Prerequisites:** Basic programming knowledge
