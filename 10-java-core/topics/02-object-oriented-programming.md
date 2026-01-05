# Object-Oriented Programming

Object-Oriented Programming (OOP) is a programming paradigm based on the concept of "objects" that contain data and code. Java is a pure OOP language where everything (except primitives) is an object.

## Classes And Objects

Understanding the distinction between classes and objects is fundamental to OOP.

### Class

A class is a blueprint or template for creating objects. It defines:
- **State** (fields/attributes)
- **Behavior** (methods)
- **Identity** (name)

```java
public class Car {
    // State (fields)
    String brand;
    String model;
    int year;
    String color;

    // Behavior (methods)
    void start() {
        System.out.println("Car started");
    }

    void stop() {
        System.out.println("Car stopped");
    }

    void accelerate() {
        System.out.println("Car accelerating");
    }
}
```

### Object

An object is an instance of a class. It has:
- **Actual values** for the attributes
- **Can invoke** methods
- **Exists in memory**

```java
// Creating objects (instances)
Car car1 = new Car();
car1.brand = "Toyota";
car1.model = "Camry";
car1.year = 2024;
car1.color = "Blue";

Car car2 = new Car();
car2.brand = "Honda";
car2.model = "Accord";
car2.year = 2023;
car2.color = "Red";

// Different objects, same class
car1.start();  // car1 starts
car2.start();  // car2 starts
```

### Visualization

```
Class (Blueprint)
┌─────────────────┐
│      Car        │
├─────────────────┤
│ - brand         │
│ - model         │
│ - year          │
│ - color         │
├─────────────────┤
│ + start()       │
│ + stop()        │
│ + accelerate()  │
└─────────────────┘
        │
        │ creates
        ↓
Objects (Instances)
┌─────────────────┐    ┌─────────────────┐
│     car1        │    │     car2        │
├─────────────────┤    ├─────────────────┤
│ Toyota          │    │ Honda           │
│ Camry           │    │ Accord          │
│ 2024            │    │ 2023            │
│ Blue            │    │ Red             │
└─────────────────┘    └─────────────────┘
```

### Key Differences

| Class | Object |
|-------|--------|
| Blueprint/Template | Instance of class |
| Defined once | Can create many |
| No memory at definition | Allocated memory when created |
| Logical entity | Physical entity |
| Contains no data | Contains actual data |

---

## Class Members

Class members define the structure and behavior of objects.

### Fields (Attributes)

Variables that hold object state.

```java
public class Person {
    // Instance fields (each object has its own copy)
    String name;
    int age;
    String email;

    // Different access levels
    public String publicField;
    private String privateField;
    protected String protectedField;
    String packageField;  // default (package-private)
}
```

### Methods

Functions that define object behavior.

```java
public class Calculator {
    // Method with no parameters, returns int
    public int getConstant() {
        return 42;
    }

    // Method with parameters, returns int
    public int add(int a, int b) {
        return a + b;
    }

    // Method with parameters, returns void
    public void printSum(int a, int b) {
        System.out.println("Sum: " + (a + b));
    }

    // Method with multiple parameters
    public double average(int a, int b, int c) {
        return (a + b + c) / 3.0;
    }

    // Varargs method (variable arguments)
    public int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }
}

// Usage
Calculator calc = new Calculator();
int result1 = calc.add(5, 3);           // 8
calc.printSum(10, 20);                  // Sum: 30
double avg = calc.average(10, 20, 30);  // 20.0
int sum = calc.sum(1, 2, 3, 4, 5);      // 15
```

### Constructors

Special methods for initializing objects.

```java
public class Person {
    private String name;
    private int age;

    // No-argument constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }

    // Parameterized constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Constructor overloading
    public Person(String name) {
        this.name = name;
        this.age = 0;
    }

    // Constructor chaining
    public Person(String name, int age, String email) {
        this(name, age);  // Call other constructor
        // Additional initialization
    }
}

// Usage
Person p1 = new Person();              // No-arg constructor
Person p2 = new Person("Alice", 25);   // Parameterized constructor
Person p3 = new Person("Bob");         // Single parameter
```

**Constructor Rules:**
- Same name as class
- No return type (not even void)
- Called automatically when object is created
- Can be overloaded
- If no constructor defined, Java provides default no-arg constructor

### this Keyword

Reference to current object.

```java
public class Person {
    private String name;
    private int age;

    // 'this' to distinguish fields from parameters
    public Person(String name, int age) {
        this.name = name;  // this.name refers to field
        this.age = age;    // age refers to parameter
    }

    // 'this' to call another constructor
    public Person(String name) {
        this(name, 0);  // Call other constructor
    }

    // 'this' to return current object
    public Person setName(String name) {
        this.name = name;
        return this;  // Enable method chaining
    }

    public Person setAge(int age) {
        this.age = age;
        return this;
    }
}

// Method chaining
Person person = new Person()
    .setName("Alice")
    .setAge(25);
```

### Initializer Blocks

Code blocks that initialize objects.

```java
public class Example {
    private int value;
    private static int staticValue;

    // Instance initializer block (runs before constructor)
    {
        System.out.println("Instance initializer");
        value = 10;
    }

    // Static initializer block (runs once when class is loaded)
    static {
        System.out.println("Static initializer");
        staticValue = 100;
    }

    // Constructor
    public Example() {
        System.out.println("Constructor");
    }
}

// Execution order when creating object:
// 1. Static initializer (only once)
// 2. Instance initializer
// 3. Constructor
```

### Getters and Setters

Methods to access and modify private fields.

```java
public class Person {
    private String name;
    private int age;

    // Getter
    public String getName() {
        return name;
    }

    // Setter with validation
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    // Getter
    public int getAge() {
        return age;
    }

    // Setter with validation
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;
        }
    }

    // Derived property (no setter)
    public boolean isAdult() {
        return age >= 18;
    }
}
```

---

## Static Members

Static members belong to the class itself, not to any specific object.

### Static Fields

Shared among all instances of the class.

```java
public class Counter {
    // Instance field - each object has its own
    private int instanceCount;

    // Static field - shared by all objects
    private static int totalCount = 0;

    public Counter() {
        instanceCount++;
        totalCount++;
    }

    public void display() {
        System.out.println("Instance: " + instanceCount);
        System.out.println("Total: " + totalCount);
    }
}

// Usage
Counter c1 = new Counter();
c1.display();  // Instance: 1, Total: 1

Counter c2 = new Counter();
c2.display();  // Instance: 1, Total: 2

Counter c3 = new Counter();
c3.display();  // Instance: 1, Total: 3
```

### Static Methods

Belong to class, can be called without creating object.

```java
public class MathUtils {
    // Static constant
    public static final double PI = 3.14159;

    // Static method
    public static int add(int a, int b) {
        return a + b;
    }

    public static int square(int n) {
        return n * n;
    }

    // Static method cannot access instance members
    private int instanceVar = 10;

    public static void staticMethod() {
        // System.out.println(instanceVar);  // ERROR!
        // Must use static members only
        System.out.println(PI);  // OK
    }
}

// Usage - no object needed
int sum = MathUtils.add(5, 3);
int sq = MathUtils.square(4);
double pi = MathUtils.PI;
```

### Static vs Instance

```java
public class Example {
    // Static members
    private static int staticVar = 100;

    public static void staticMethod() {
        System.out.println("Static: " + staticVar);
        // Can only access static members
    }

    // Instance members
    private int instanceVar = 200;

    public void instanceMethod() {
        System.out.println("Instance: " + instanceVar);
        System.out.println("Static: " + staticVar);
        // Can access both static and instance members
    }
}

// Usage
Example.staticMethod();  // Call on class

Example obj = new Example();
obj.instanceMethod();    // Call on object
```

| Feature | Static | Instance |
|---------|--------|----------|
| Belongs to | Class | Object |
| Memory | Single copy | Copy per object |
| Access | Class name or object | Object only |
| Can access | Static members only | Static and instance |
| Use case | Utilities, constants, counters | Object-specific data |

### Static Import

```java
// Without static import
import java.lang.Math;

public class Example {
    double x = Math.sqrt(16);
    double y = Math.PI;
}

// With static import
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

public class Example {
    double x = sqrt(16);  // Cleaner!
    double y = PI;
}
```

### Common Use Cases

```java
// Constants
public class Constants {
    public static final String APP_NAME = "MyApp";
    public static final int MAX_USERS = 1000;
}

// Utility methods
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

// Factory methods
public class User {
    private String name;

    private User(String name) {
        this.name = name;
    }

    public static User createAdmin(String name) {
        return new User(name);
    }

    public static User createGuest() {
        return new User("Guest");
    }
}

// Singleton pattern
public class Database {
    private static Database instance;

    private Database() { }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}
```

---

## JVM Memory Architecture

Now that we understand classes, objects, and static members, let's examine where they live in memory.

### Memory Areas Overview

```
┌───────────────────────────────────────────────────────────────────┐
│                        JVM Memory Structure                        │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                         HEAP MEMORY                          │  │
│  │  (Shared among all threads - Objects and Instance Variables) │  │
│  │                                                               │  │
│  │  ┌─────────────────┐  ┌─────────────────────────────────┐   │  │
│  │  │   Young Gen     │  │         Old Generation          │   │  │
│  │  │  ┌─────────┐    │  │                                 │   │  │
│  │  │  │  Eden   │    │  │   Long-lived objects            │   │  │
│  │  │  │  Space  │    │  │                                 │   │  │
│  │  │  ├─────────┤    │  │                                 │   │  │
│  │  │  │   S0    │    │  │                                 │   │  │
│  │  │  ├─────────┤    │  │                                 │   │  │
│  │  │  │   S1    │    │  │                                 │   │  │
│  │  │  └─────────┘    │  └─────────────────────────────────┘   │  │
│  │  └─────────────────┘                                         │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                        STACK MEMORY                          │  │
│  │        (Thread-private - Local Variables and References)     │  │
│  │                                                               │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │  │
│  │  │   Thread 1   │  │   Thread 2   │  │   Thread 3   │        │  │
│  │  │  ┌────────┐  │  │  ┌────────┐  │  │  ┌────────┐  │        │  │
│  │  │  │ Frame  │  │  │  │ Frame  │  │  │  │ Frame  │  │        │  │
│  │  │  │ Frame  │  │  │  │ Frame  │  │  │  │ Frame  │  │        │  │
│  │  │  │ Frame  │  │  │  └────────┘  │  │  │ Frame  │  │        │  │
│  │  │  └────────┘  │  │              │  │  │ Frame  │  │        │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘        │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                       METASPACE                              │  │
│  │   (Class metadata, static variables, method bytecode)        │  │
│  └─────────────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────────┘
```

### Stack Memory

The stack is a region of memory used for storing local variables and method call information. Each thread has its own private stack.

| Property | Description |
|----------|-------------|
| **Thread-private** | Each thread has its own stack |
| **LIFO structure** | Last In, First Out (push/pop operations) |
| **Fixed size** | Size determined at thread creation |
| **Fast access** | Direct memory access, no GC overhead |
| **Stores** | Primitive values, object references, method frames |

```java
public class StackExample {
    public void methodA() {
        int x = 10;              // Primitive stored in stack
        String name = "Hello";   // Reference stored in stack
                                 // (String object in heap)
        methodB(x);              // New stack frame created
    }

    public void methodB(int param) {
        double result = param * 2.5;  // Stored in stack
        int[] arr = new int[5];       // Reference in stack, array in heap
    }
}
```

### Heap Memory

The heap is a shared memory region where all objects and their instance variables are stored.

| Property | Description |
|----------|-------------|
| **Shared** | All threads share the same heap |
| **Dynamic size** | Can grow/shrink at runtime |
| **GC managed** | Garbage collector reclaims unused memory |
| **Stores** | Objects, instance variables, arrays |

```java
public class HeapExample {
    private String name;           // Instance variable - stored in heap with object
    private int[] scores;

    public HeapExample(String name) {
        this.name = name;                    // String object in heap
        this.scores = new int[]{90, 85, 88}; // Array object in heap
    }
}
```

### Stack vs Heap Comparison

| Aspect | Stack | Heap |
|--------|-------|------|
| **Storage** | Primitives, references | Objects, instance variables |
| **Scope** | Thread-private | Shared among threads |
| **Size** | Fixed, smaller | Dynamic, larger |
| **Speed** | Very fast | Slower (GC overhead) |
| **Management** | Automatic (LIFO) | Garbage collected |
| **Error** | StackOverflowError | OutOfMemoryError |

### Memory Allocation Example

```java
public class MemoryDemo {
    private int instanceVar = 10;        // Heap (with object)
    private static int staticVar = 20;   // Metaspace

    public void process() {
        int localVar = 30;               // Stack
        String localRef = new String("Hello");  // Reference: Stack, Object: Heap
        calculate(localVar);             // New stack frame
    }

    public int calculate(int param) {    // param: Stack
        int result = param * 2;          // result: Stack
        return result;
    }
}
```

```
STACK (Thread-1)                    HEAP
┌─────────────────────┐            ┌─────────────────────────────┐
│ process()           │            │   ┌─────────────────────┐   │
│  ├─ this (ref) ────────────────► │   │   MemoryDemo object │   │
│  ├─ localVar = 30   │            │   │  instanceVar = 10   │   │
│  ├─ localRef (ref) ────────────► │   └─────────────────────┘   │
├─────────────────────┤            │   String "Hello"            │
│ calculate()         │            └─────────────────────────────┘
│  ├─ param = 30      │
│  ├─ result = 60     │            METASPACE
└─────────────────────┘            ┌─────────────────────────────┐
                                   │  staticVar = 20             │
                                   │  Class metadata             │
                                   └─────────────────────────────┘
```

---

## Method Execution on the Stack

When a method is called, a new stack frame is created. When the method returns, the frame is destroyed.

### Stack Frame Lifecycle

```java
public class MethodStackDemo {
    public static void main(String[] args) {
        int result = methodA(5);
    }

    public static int methodA(int x) {
        int y = methodB(x + 10);
        return y * 2;
    }

    public static int methodB(int a) {
        return a + 5;
    }
}
```

**Stack evolution:**

```
Step 1: main() called          Step 2: methodA() called
┌──────────────────┐           ┌──────────────────┐
│      main()      │           │    methodA()     │ ← Top
│  args, result    │           │  x=5, y=?        │
└──────────────────┘           ├──────────────────┤
                               │      main()      │
                               └──────────────────┘

Step 3: methodB() called       Step 4: After returns
┌──────────────────┐           ┌──────────────────┐
│    methodB()     │ ← Top     │      main()      │ ← Top
│  a=15            │           │  args, result=40 │
├──────────────────┤           └──────────────────┘
│    methodA()     │
├──────────────────┤
│      main()      │
└──────────────────┘
```

### Recursion and Stack Frames

```java
public static int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}
```

**Stack during factorial(5):**

```
┌────────────────────────┐
│ factorial(1) → returns 1│
├────────────────────────┤
│ factorial(2) → 2 * 1 = 2│
├────────────────────────┤
│ factorial(3) → 3 * 2 = 6│
├────────────────────────┤
│ factorial(4) → 4 * 6 = 24│
├────────────────────────┤
│ factorial(5) → 5 * 24 = 120│
├────────────────────────┤
│ main()                  │
└────────────────────────┘
```

---

## OOP Principles Overview

Now that we understand the building blocks (classes, objects, static members, and memory), let's explore the four pillars of OOP.

```
┌─────────────────────────────────────┐
│         OOP Principles              │
├─────────────────────────────────────┤
│  1. Encapsulation                   │
│     Hide internal details           │
│                                     │
│  2. Inheritance                     │
│     Reuse and extend functionality  │
│                                     │
│  3. Polymorphism                    │
│     One interface, many forms       │
│                                     │
│  4. Abstraction                     │
│     Hide complexity, show essentials│
└─────────────────────────────────────┘
```

---

## Encapsulation

Encapsulation bundles data and methods that operate on that data within a single unit (class), hiding internal details from the outside world.

### Implementing Encapsulation

```java
public class BankAccount {
    // Private data - hidden from outside
    private double balance;
    private String accountNumber;

    // Public methods - controlled access
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }
}
```

### Benefits of Encapsulation

| Benefit | Description |
|---------|-------------|
| **Data Protection** | Internal state cannot be directly modified |
| **Flexibility** | Can change implementation without affecting users |
| **Validation** | Control how data is accessed and modified |
| **Maintainability** | Easier to modify and debug |

---

## Inheritance

Inheritance allows a class to inherit properties and methods from another class.

### Basic Inheritance

```java
// Parent class (superclass, base class)
public class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " is eating");
    }

    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Child class (subclass, derived class)
public class Dog extends Animal {
    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);  // Call parent constructor
        this.breed = breed;
    }

    // Inherited: eat(), sleep()

    // New method
    public void bark() {
        System.out.println(name + " is barking");
    }
}

// Usage
Dog dog = new Dog("Buddy", 3, "Golden Retriever");
dog.eat();    // Inherited from Animal
dog.sleep();  // Inherited from Animal
dog.bark();   // Defined in Dog
```

### super Keyword

Reference to parent class.

```java
public class Vehicle {
    protected String brand;

    public Vehicle(String brand) {
        this.brand = brand;
    }

    public void start() {
        System.out.println("Vehicle starting");
    }
}

public class Car extends Vehicle {
    private String model;

    public Car(String brand, String model) {
        super(brand);  // Call parent constructor
        this.model = model;
    }

    @Override
    public void start() {
        super.start();  // Call parent method
        System.out.println("Car starting: " + brand + " " + model);
    }

    public void display() {
        System.out.println("Brand: " + super.brand);  // Access parent field
    }
}
```

### Method Overriding

Child class provides specific implementation of parent method.

```java
public class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
}

public class Dog extends Animal {
    @Override  // Annotation (recommended)
    public void makeSound() {
        System.out.println("Bark!");
    }
}

public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Meow!");
    }
}

// Usage
Animal animal = new Animal();
animal.makeSound();  // Some sound

Dog dog = new Dog();
dog.makeSound();     // Bark!

Cat cat = new Cat();
cat.makeSound();     // Meow!

// Polymorphism
Animal pet = new Dog();
pet.makeSound();     // Bark! (runtime polymorphism)
```

**Override Rules:**
- Same method signature (name, parameters)
- Return type same or subtype (covariant return)
- Cannot reduce visibility (can increase)
- Cannot override final, static, or private methods
- Can add @Override annotation for compile-time checking

### Types of Inheritance

#### Single Inheritance

```java
class A { }
class B extends A { }
```

#### Multilevel Inheritance

```java
class A { }
class B extends A { }
class C extends B { }
```

#### Hierarchical Inheritance

```java
class A { }
class B extends A { }
class C extends A { }
class D extends A { }
```

**Note:** Java does NOT support multiple inheritance (class extending multiple classes) to avoid the Diamond Problem. Use interfaces instead.

### IS-A vs HAS-A

```java
// IS-A relationship (Inheritance)
public class Dog extends Animal {
    // Dog IS-A Animal
}

// HAS-A relationship (Composition)
public class Car {
    private Engine engine;  // Car HAS-A Engine

    public Car() {
        this.engine = new Engine();
    }
}
```

### Constructor Chaining in Inheritance

```java
public class Animal {
    public Animal() {
        System.out.println("Animal constructor");
    }
}

public class Dog extends Animal {
    public Dog() {
        super();  // Implicit if not specified
        System.out.println("Dog constructor");
    }
}

public class Puppy extends Dog {
    public Puppy() {
        super();  // Implicit if not specified
        System.out.println("Puppy constructor");
    }
}

// Creating Puppy object prints:
// Animal constructor
// Dog constructor
// Puppy constructor
```

---

## Polymorphism

Polymorphism means "many forms" - the ability of objects to take different forms. It allows the same interface to be used for different underlying implementations.

### Method Overloading (Compile-time Polymorphism)

Same method name with different parameters in the same class.

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }
}

// Usage
Calculator calc = new Calculator();
calc.add(5, 3);        // Calls int version
calc.add(5.0, 3.0);    // Calls double version
calc.add(1, 2, 3);     // Calls three-parameter version
```

### Method Overriding (Runtime Polymorphism)

Child class provides specific implementation of parent's method.

```java
public class Animal {
    public void makeSound() {
        System.out.println("Some sound");
    }
}

public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Bark!");
    }
}

public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Meow!");
    }
}

// Runtime polymorphism in action
Animal animal1 = new Dog();
Animal animal2 = new Cat();
animal1.makeSound();  // Bark! (actual type determines behavior)
animal2.makeSound();  // Meow!
```

### Override Rules

| Rule | Description |
|------|-------------|
| **Same signature** | Method name and parameters must match |
| **Return type** | Same or subtype (covariant return) |
| **Access** | Cannot reduce visibility (can increase) |
| **Cannot override** | final, static, or private methods |

---

## Abstraction

Abstraction hides complex implementation details, showing only essential features to the user.

### Abstract Classes

Cannot be instantiated, may contain abstract and concrete methods.

```java
public abstract class Shape {
    protected String color;

    // Abstract method - no implementation
    public abstract double calculateArea();

    // Concrete method
    public void display() {
        System.out.println("Color: " + color);
        System.out.println("Area: " + calculateArea());
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        this.color = color;
        this.radius = radius;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}
```

---

## Interfaces

Interfaces define a contract that implementing classes must fulfill.

### Basic Interface

```java
public interface Drawable {
    void draw();  // Implicitly public abstract
    void resize(int width, int height);
}

public class Rectangle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing rectangle");
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("Resizing to " + width + "x" + height);
    }
}
```

### Multiple Interface Implementation

```java
public interface Flyable {
    void fly();
}

public interface Swimmable {
    void swim();
}

public class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck flying");
    }

    @Override
    public void swim() {
        System.out.println("Duck swimming");
    }
}
```

### Interface Features (Java 8+)

```java
public interface ModernInterface {
    // Constants (implicitly public static final)
    int MAX_SIZE = 100;

    // Abstract method
    void abstractMethod();

    // Default method (Java 8+)
    default void defaultMethod() {
        System.out.println("Default implementation");
    }

    // Static method (Java 8+)
    static void staticMethod() {
        System.out.println("Static method in interface");
    }

    // Private method (Java 9+)
    private void privateHelper() {
        System.out.println("Private helper method");
    }
}
```

### Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| Methods | Abstract and concrete | Abstract, default, static (Java 8+) |
| Fields | Any | public static final only |
| Constructor | Yes | No |
| Multiple Inheritance | No | Yes |
| When to use | IS-A relationship | CAN-DO capability |

---

## Marker Interfaces

A marker interface is an empty interface (no methods) used to signal a capability or characteristic to the JVM or other code.

### Common Marker Interfaces

```java
// java.io.Serializable - marks objects that can be serialized
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
}

// java.lang.Cloneable - marks objects that can be cloned
public class Document implements Cloneable {
    private String content;

    @Override
    public Document clone() throws CloneNotSupportedException {
        return (Document) super.clone();
    }
}
```

### Purpose of Marker Interfaces

| Interface | Purpose |
|-----------|---------|
| **Serializable** | Object can be converted to byte stream |
| **Cloneable** | Object can be cloned using Object.clone() |
| **Remote** | Object can be accessed remotely (RMI) |
| **RandomAccess** | List supports fast random access |

### Marker Interfaces vs Annotations

Modern Java often uses annotations instead of marker interfaces:

```java
// Marker interface approach
public class OldStyle implements Serializable { }

// Annotation approach (modern)
@Entity
public class ModernStyle { }
```

**When to use marker interfaces:**
- When you need to define a type that a class can implement
- When you want to use `instanceof` checks
- When the marker represents a permanent characteristic

---

## Non-Access Modifiers

Non-access modifiers provide additional properties to classes, methods, and fields beyond access control. They define behavior, state characteristics, and compile-time constraints.

### Overview of Non-Access Modifiers

| Modifier | Applies To | Purpose |
|----------|-----------|---------|
| `final` | Class, Method, Variable | Prevents modification/extension |
| `abstract` | Class, Method | Declares incomplete implementation |
| `static` | Variable, Method, Block, Class | Belongs to class, not instance |
| `synchronized` | Method, Block | Thread-safe access |
| `volatile` | Variable | Thread visibility guarantee |
| `transient` | Variable | Exclude from serialization |
| `native` | Method | Implemented in native code |
| `strictfp` | Class, Method | Strict floating-point |
| `sealed` | Class, Interface | Restricts inheritance (Java 17+) |

---

### final Modifier

The `final` keyword prevents modification and provides immutability guarantees.

#### final Variables

Cannot be reassigned after initialization.

```java
public class FinalVariables {
    // 1. Final instance variable - must be initialized once
    private final int ID;

    // 2. Final static variable (constant)
    public static final double PI = 3.14159;
    public static final String APP_NAME = "MyApp";

    // 3. Blank final - initialized in constructor
    private final String createdAt;

    public FinalVariables(int id) {
        this.ID = id;  // First assignment - OK
        // this.ID = 100;  // ERROR: Cannot reassign
        this.createdAt = java.time.Instant.now().toString();
    }

    public void method() {
        // 4. Local final variable
        final int localVar = 10;
        // localVar = 20;  // ERROR: Cannot reassign

        // 5. Effectively final (Java 8+) - for lambdas
        int effectivelyFinal = 5;  // Never reassigned
        Runnable r = () -> System.out.println(effectivelyFinal);  // OK
    }
}
```

**Important: Final doesn't mean immutable for objects!**

```java
public class FinalObjectReference {
    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();

        // Reference cannot change
        // list = new ArrayList<>();  // ERROR

        // But object content CAN change!
        list.add("Hello");    // OK
        list.add("World");    // OK
        list.clear();         // OK

        // For true immutability, use immutable collections
        final List<String> immutable = List.of("A", "B", "C");
        // immutable.add("D");  // Runtime error: UnsupportedOperationException
    }
}
```

#### final Methods

Cannot be overridden by subclasses.

```java
public class Parent {
    // Cannot be overridden
    public final void criticalMethod() {
        System.out.println("Critical logic - don't change!");
    }

    // Can be overridden
    public void normalMethod() {
        System.out.println("Can be customized");
    }
}

public class Child extends Parent {
    // @Override
    // public void criticalMethod() { }  // ERROR: Cannot override final

    @Override
    public void normalMethod() {
        System.out.println("Customized behavior");
    }
}
```

**Use cases for final methods:**
- Security-critical operations
- Template method pattern (invariant parts)
- Performance optimization (JVM can inline)

#### final Classes

Cannot be extended (no subclasses).

```java
// Cannot be extended
public final class ImmutablePoint {
    private final int x;
    private final int y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

// ERROR: Cannot extend final class
// public class ColoredPoint extends ImmutablePoint { }

// Examples from Java API:
// String, Integer, Double, etc. are all final classes
```

**Benefits of final classes:**
- Prevents breaking changes through inheritance
- Ensures immutability (combined with final fields)
- Allows JVM optimizations

#### final Parameters

```java
public class FinalParameters {
    // Parameter cannot be reassigned
    public void process(final String input) {
        // input = "modified";  // ERROR
        System.out.println(input.toUpperCase());  // OK to use
    }

    // Useful in lambdas and anonymous classes
    public Runnable createTask(final String message) {
        return () -> System.out.println(message);  // Captures final/effectively final
    }
}
```

---

### abstract Modifier

Declares incomplete implementations that must be completed by subclasses.

#### abstract Methods

No implementation body - subclasses must provide it.

```java
public abstract class Shape {
    protected String color;

    // Abstract methods - no body
    public abstract double calculateArea();
    public abstract double calculatePerimeter();

    // Concrete methods allowed
    public void setColor(String color) {
        this.color = color;
    }

    public void display() {
        System.out.println("Color: " + color);
        System.out.println("Area: " + calculateArea());
    }
}

public class Rectangle extends Shape {
    private double width, height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }
}
```

#### abstract Classes

Cannot be instantiated directly.

```java
public abstract class Animal {
    protected String name;

    // Constructor allowed (called by subclass)
    public Animal(String name) {
        this.name = name;
    }

    // Abstract method
    public abstract void makeSound();

    // Concrete method
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Usage
// Animal animal = new Animal("Generic");  // ERROR: Cannot instantiate

Animal dog = new Animal("Buddy") {  // Anonymous subclass
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
};
```

#### Abstract Class Rules

```java
// 1. Can have constructor
public abstract class Base {
    public Base() { }
}

// 2. Can have all concrete methods
public abstract class ConcreteAbstract {
    public void method1() { }
    public void method2() { }
    // Just prevents instantiation
}

// 3. If class has abstract method, class MUST be abstract
public abstract class MustBeAbstract {
    public abstract void mustImplement();  // Forces class to be abstract
}

// 4. First concrete subclass must implement all abstract methods
public abstract class A {
    public abstract void m1();
    public abstract void m2();
}

public abstract class B extends A {
    @Override
    public void m1() { }  // Partial implementation OK
    // m2() still abstract
}

public class C extends B {
    @Override
    public void m2() { }  // Must implement remaining
}
```

---

### static Modifier

Static members belong to the class, not instances.

#### static Fields

```java
public class Counter {
    // Static field - shared by all instances
    private static int totalCount = 0;

    // Instance field - unique per object
    private int instanceId;

    public Counter() {
        totalCount++;
        this.instanceId = totalCount;
    }

    public static int getTotalCount() {
        return totalCount;
    }

    public int getInstanceId() {
        return instanceId;
    }
}

// Usage
Counter c1 = new Counter();  // totalCount = 1, instanceId = 1
Counter c2 = new Counter();  // totalCount = 2, instanceId = 2
Counter c3 = new Counter();  // totalCount = 3, instanceId = 3

System.out.println(Counter.getTotalCount());  // 3
```

#### static Methods

```java
public class MathUtils {
    // Static method - called on class, not instance
    public static int add(int a, int b) {
        return a + b;
    }

    public static int max(int... numbers) {
        int result = Integer.MIN_VALUE;
        for (int n : numbers) {
            if (n > result) result = n;
        }
        return result;
    }

    // Static methods CANNOT:
    // - Access instance variables
    // - Use 'this' keyword
    // - Be overridden (can be hidden)

    private int instanceVar = 10;

    public static void staticMethod() {
        // System.out.println(instanceVar);  // ERROR
        // System.out.println(this);         // ERROR
    }
}

// Usage - no object needed
int sum = MathUtils.add(5, 3);
int maximum = MathUtils.max(1, 5, 3, 9, 2);
```

#### static Blocks (Initializers)

```java
public class Configuration {
    private static Map<String, String> config;

    // Static block - runs once when class is loaded
    static {
        System.out.println("Loading configuration...");
        config = new HashMap<>();
        config.put("db.host", "localhost");
        config.put("db.port", "5432");

        // Can have multiple static blocks - execute in order
    }

    static {
        config.put("app.name", "MyApp");
    }

    public static String get(String key) {
        return config.get(key);
    }
}

// Class loading triggers static blocks
String host = Configuration.get("db.host");
```

#### static Nested Classes

```java
public class Outer {
    private static int staticVar = 10;
    private int instanceVar = 20;

    // Static nested class
    public static class StaticNested {
        public void display() {
            System.out.println(staticVar);    // OK - static
            // System.out.println(instanceVar);  // ERROR - instance
        }
    }

    // Non-static inner class (for comparison)
    public class Inner {
        public void display() {
            System.out.println(staticVar);    // OK
            System.out.println(instanceVar);  // OK
        }
    }
}

// Usage
Outer.StaticNested nested = new Outer.StaticNested();  // No Outer instance needed
Outer.Inner inner = new Outer().new Inner();           // Outer instance required
```

---

### synchronized Modifier

Ensures thread-safe access to shared resources.

#### synchronized Methods

```java
public class BankAccount {
    private double balance;

    // Synchronized instance method - locks on 'this'
    public synchronized void deposit(double amount) {
        double newBalance = balance + amount;
        // Simulate processing time
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        balance = newBalance;
    }

    public synchronized void withdraw(double amount) {
        if (balance >= amount) {
            double newBalance = balance - amount;
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            balance = newBalance;
        }
    }

    public synchronized double getBalance() {
        return balance;
    }
}
```

#### synchronized Blocks

```java
public class Counter {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        // Synchronized on specific lock object
        synchronized(lock) {
            count++;
        }
    }

    // Synchronized on this
    public void decrement() {
        synchronized(this) {
            count--;
        }
    }

    // Synchronized on class object (for static context)
    public static void staticMethod() {
        synchronized(Counter.class) {
            // Critical section
        }
    }
}
```

#### synchronized Static Methods

```java
public class SharedResource {
    private static int sharedValue = 0;

    // Locks on SharedResource.class object
    public static synchronized void updateShared(int value) {
        sharedValue = value;
    }

    // Equivalent block version
    public static void updateSharedAlternative(int value) {
        synchronized(SharedResource.class) {
            sharedValue = value;
        }
    }
}
```

---

### volatile Modifier

Guarantees visibility of variable changes across threads.

```java
public class VolatileExample {
    // Without volatile - threads may cache value
    private boolean running = true;

    // With volatile - changes immediately visible to all threads
    private volatile boolean flag = false;

    public void stop() {
        flag = true;  // Change immediately visible
    }

    public void process() {
        while (!flag) {  // Reads latest value
            // Do work
        }
        System.out.println("Stopped!");
    }
}
```

**When to use volatile:**

```java
// 1. Simple flags
private volatile boolean shutdown = false;

// 2. Status indicators
private volatile int status = INITIALIZING;

// 3. Double-checked locking (with synchronized)
public class Singleton {
    private static volatile Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// volatile does NOT make compound operations atomic!
// For: count++, use AtomicInteger instead
```

---

### transient Modifier

Excludes fields from serialization.

```java
import java.io.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private transient String password;      // Not serialized
    private transient Connection dbConn;     // Not serialized

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // After deserialization, transient fields are:
    // - null for objects
    // - 0 for numbers
    // - false for booleans

    // Re-initialize transient fields if needed
    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.password = "";  // Set default value
    }
}
```

**Use transient for:**
- Sensitive data (passwords, keys)
- Non-serializable fields (connections, streams)
- Cached/computed values
- Logger instances

---

### native Modifier

Indicates method is implemented in native code (C/C++/assembly).

```java
public class NativeExample {
    // Declaration only - no body
    public native void nativeMethod();
    public native int computeNative(int[] data);

    // Load native library
    static {
        System.loadLibrary("mylib");  // Loads libmylib.so or mylib.dll
    }
}

// Use cases:
// - JNI (Java Native Interface) calls
// - Performance-critical operations
// - System-level operations
// - Existing C/C++ libraries
```

---

### strictfp Modifier

Ensures consistent floating-point calculations across platforms.

```java
// Applied to class - all methods use strict FP
public strictfp class StrictMath {
    public double calculate() {
        return 1.0 / 3.0;  // Same result on all platforms
    }
}

// Applied to method only
public class MixedPrecision {
    public strictfp double preciseCalc() {
        return Math.PI * Math.E;  // Strict mode
    }

    public double normalCalc() {
        return Math.PI * Math.E;  // Platform-dependent
    }
}

// Note: Since Java 17, strictfp is default and keyword is redundant
```

---

### sealed Modifier (Java 17+)

Restricts which classes can extend or implement.

#### sealed Classes

```java
// Only specified classes can extend
public sealed class Shape
    permits Circle, Rectangle, Triangle {

    public abstract double area();
}

// Permitted subclasses must be final, sealed, or non-sealed
public final class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public final class Rectangle extends Shape {
    private double width, height;

    public Rectangle(double w, double h) {
        this.width = w;
        this.height = h;
    }

    @Override
    public double area() {
        return width * height;
    }
}

// Can be further extended if non-sealed
public non-sealed class Triangle extends Shape {
    @Override
    public double area() {
        return 0;  // Simplified
    }
}

// Anyone can extend non-sealed Triangle
public class RightTriangle extends Triangle { }
```

#### sealed Interfaces

```java
public sealed interface Expression
    permits Constant, Variable, BinaryOp {
}

public final class Constant implements Expression {
    private final int value;
    public Constant(int value) { this.value = value; }
}

public final class Variable implements Expression {
    private final String name;
    public Variable(String name) { this.name = name; }
}

public sealed class BinaryOp implements Expression
    permits Add, Subtract, Multiply {
}

public final class Add extends BinaryOp { }
public final class Subtract extends BinaryOp { }
public final class Multiply extends BinaryOp { }
```

#### Pattern Matching with sealed Classes

```java
// Compiler knows all subtypes - exhaustive switch
public static double calculate(Shape shape) {
    return switch (shape) {
        case Circle c -> c.area();
        case Rectangle r -> r.area();
        case Triangle t -> t.area();
        // No default needed - compiler knows it's exhaustive
    };
}
```

---

### Non-Access Modifiers Summary

| Modifier | Variables | Methods | Classes | Key Behavior |
|----------|-----------|---------|---------|--------------|
| `final` | Cannot reassign | Cannot override | Cannot extend | Immutability |
| `abstract` | N/A | No body | Cannot instantiate | Incomplete |
| `static` | Class-level | Class-level | Nested only | Shared |
| `synchronized` | N/A | Thread-safe | N/A | Locking |
| `volatile` | Thread-visible | N/A | N/A | No caching |
| `transient` | Skip serialize | N/A | N/A | Exclusion |
| `native` | N/A | Native code | N/A | JNI |
| `strictfp` | N/A | Strict FP | Strict FP | Consistent |
| `sealed` | N/A | N/A | Limit inheritance | Control |

---

## Equality, hashCode, and equals

Understanding object equality is crucial for collections and comparisons.

### == vs equals()

```java
String s1 = new String("Hello");
String s2 = new String("Hello");
String s3 = s1;

// == compares references
System.out.println(s1 == s2);     // false (different objects)
System.out.println(s1 == s3);     // true (same reference)

// equals() compares content
System.out.println(s1.equals(s2)); // true (same content)
System.out.println(s1.equals(s3)); // true

// String pool special case
String s4 = "Hello";
String s5 = "Hello";
System.out.println(s4 == s5);     // true (same string pool object)
```

### equals() Contract

When overriding equals(), must satisfy:

1. **Reflexive**: `x.equals(x)` must be true
2. **Symmetric**: If `x.equals(y)` then `y.equals(x)`
3. **Transitive**: If `x.equals(y)` and `y.equals(z)` then `x.equals(z)`
4. **Consistent**: Multiple calls return same result
5. **Null**: `x.equals(null)` must be false

### Implementing equals()

```java
public class Person {
    private String name;
    private int age;
    private String email;

    // Constructor, getters, setters...

    @Override
    public boolean equals(Object obj) {
        // 1. Check reference equality
        if (this == obj) {
            return true;
        }

        // 2. Check null and type
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // 3. Cast and compare fields
        Person other = (Person) obj;
        return age == other.age &&
               Objects.equals(name, other.name) &&
               Objects.equals(email, other.email);
    }
}
```

### hashCode() Contract

When overriding equals(), must also override hashCode():

1. **Consistent**: Multiple calls on same object return same value
2. **Equal objects**: If `x.equals(y)` then `x.hashCode() == y.hashCode()`
3. **Unequal objects**: Not required, but different hash codes improve performance

### Implementing hashCode()

```java
public class Person {
    private String name;
    private int age;
    private String email;

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }

    // Or manually
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
```

### Complete Example

```java
import java.util.Objects;

public class Person {
    private final String name;
    private final int age;
    private final String email;

    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Person person = (Person) obj;
        return age == person.age &&
               Objects.equals(name, person.name) &&
               Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email);
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }
}

// Usage in collections
Set<Person> people = new HashSet<>();
people.add(new Person("Alice", 25, "alice@example.com"));
people.add(new Person("Alice", 25, "alice@example.com"));  // Duplicate, won't be added

System.out.println(people.size());  // 1
```

### Why hashCode() Matters

```java
Person p1 = new Person("Alice", 25, "alice@example.com");
Person p2 = new Person("Alice", 25, "alice@example.com");

// Without proper hashCode(), HashMap won't work correctly
Map<Person, String> map = new HashMap<>();
map.put(p1, "Value1");

// If hashCode() not overridden, this might return null!
System.out.println(map.get(p2));  // Should return "Value1"
```

---

## Garbage Collection

Automatic memory management is one of Java's most powerful features. The Garbage Collector (GC) automatically reclaims memory occupied by objects that are no longer in use.

### How Garbage Collection Works

The GC identifies and removes objects that are no longer reachable from any live reference in the application.

```java
public class GCExample {
    public static void main(String[] args) {
        // Object created - referenced by p1
        Person p1 = new Person("Alice");

        // Another reference to same object
        Person p2 = p1;

        // p1 no longer references object, but p2 still does
        p1 = null;  // Object NOT eligible for GC

        // Now no references exist
        p2 = null;  // Object eligible for GC
    }
}
```

### Object Reachability

An object is considered reachable if it can be accessed through a chain of references starting from a **GC Root**.

**GC Roots include:**
- Local variables in currently executing methods
- Active Java threads
- Static variables
- JNI references

```
GC Roots                    Heap Objects
┌─────────────┐
│ Thread      │──────────► Object A ──────► Object C
│ Stack       │                │
└─────────────┘                ▼
                           Object B ──────► Object D
┌─────────────┐
│ Static      │──────────► Object E
│ Variables   │
└─────────────┘

Object F  ◄─────── Object G    (Unreachable - eligible for GC)
     │
     ▼
Object H
```

### Making Objects Eligible for GC

```java
// 1. Nulling references
Person p = new Person("Alice");
p = null;  // Eligible

// 2. Reassigning reference
Person p1 = new Person("Alice");
p1 = new Person("Bob");  // "Alice" object eligible

// 3. Object out of scope
public void method() {
    Person local = new Person("Alice");
}  // "Alice" object eligible when method ends

// 4. Anonymous objects
new Person("Alice").display();  // Eligible immediately after use

// 5. Island of Isolation
public class Node {
    Node next;
}

Node a = new Node();
Node b = new Node();
a.next = b;
b.next = a;  // Circular reference
a = null;
b = null;   // Both eligible despite circular reference!
```

### Generational Garbage Collection

The JVM uses a generational approach based on the observation that most objects die young.

```
┌─────────────────────────────────────────────────────────────────────┐
│                           HEAP MEMORY                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                    YOUNG GENERATION                             │ │
│  │                                                                  │ │
│  │  ┌──────────────────────────────────────────────────────────┐  │ │
│  │  │                     EDEN SPACE                            │  │ │
│  │  │                                                           │  │ │
│  │  │  • New objects allocated here                             │  │ │
│  │  │  • Minor GC triggered when full                           │  │ │
│  │  │  • Most objects die here (never leave Eden)               │  │ │
│  │  │                                                           │  │ │
│  │  └──────────────────────────────────────────────────────────┘  │ │
│  │                                                                  │ │
│  │  ┌───────────────────┐       ┌───────────────────┐             │ │
│  │  │   SURVIVOR S0     │       │   SURVIVOR S1     │             │ │
│  │  │   (From Space)    │       │   (To Space)      │             │ │
│  │  │                   │       │                   │             │ │
│  │  │  Objects that     │  ◄──► │  Objects copied   │             │ │
│  │  │  survived GC      │       │  back and forth   │             │ │
│  │  │                   │       │                   │             │ │
│  │  └───────────────────┘       └───────────────────┘             │ │
│  │                                                                  │ │
│  │  After N survivals (threshold), objects promoted to Old Gen     │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                              ↓ Promotion                             │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                     OLD GENERATION (Tenured)                    │ │
│  │                                                                  │ │
│  │  • Long-lived objects                                           │ │
│  │  • Major GC (Full GC) when full - more expensive                │ │
│  │  • Large objects may be allocated directly here                 │ │
│  │                                                                  │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                         METASPACE                                    │
│                    (Native Memory, not Heap)                         │
│                                                                      │
│  • Class metadata                                                    │
│  • Method bytecode                                                   │
│  • Constant pool                                                     │
│  • Grows automatically (limited by system memory)                    │
└─────────────────────────────────────────────────────────────────────┘
```

### GC Types

#### Minor GC (Young Generation GC)

```java
// Triggered when Eden space is full
// Fast - only scans young generation
// Uses copying algorithm

// Objects flow:
// Eden → Survivor S0/S1 → Old Generation

// Example scenario:
public void createManyObjects() {
    for (int i = 0; i < 1000000; i++) {
        String temp = new String("Object " + i);  // Short-lived
        // Object dies after loop iteration
    }
    // Most objects collected by Minor GC
}
```

#### Major GC (Old Generation GC)

```java
// Triggered when Old Generation is full
// Slower - scans entire old generation
// May cause longer pause times

// Objects that survive many Minor GCs are promoted here
public class LongLivedCache {
    private static Map<String, Object> cache = new HashMap<>();

    public static void store(String key, Object value) {
        cache.put(key, value);  // Long-lived, goes to Old Gen
    }
}
```

#### Full GC

```java
// Cleans entire heap (Young + Old + Metaspace)
// Most expensive - longest pause times
// Triggered by:
//   - System.gc() call
//   - Old generation full
//   - Metaspace full
//   - Promotion failure
```

### GC Algorithms

#### 1. Serial GC

```bash
# Single-threaded, simple, stop-the-world
java -XX:+UseSerialGC MyApp

# Best for:
# - Small applications
# - Single-core machines
# - Limited memory environments
```

#### 2. Parallel GC (Throughput Collector)

```bash
# Multi-threaded, optimized for throughput
java -XX:+UseParallelGC MyApp

# Configure threads
java -XX:ParallelGCThreads=4 MyApp

# Best for:
# - Batch processing
# - Applications where pause time is less critical
# - Multi-core systems
```

#### 3. G1 GC (Garbage First) - Default since Java 9

```bash
# Balanced throughput and latency
java -XX:+UseG1GC MyApp

# Set pause time goal (milliseconds)
java -XX:MaxGCPauseMillis=200 MyApp

# Best for:
# - Large heaps (> 4GB)
# - Applications requiring predictable pause times
# - General-purpose use
```

**G1 GC Heap Structure:**

```
┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐
│  E  │  E  │  S  │  O  │  O  │  E  │  H  │  O  │
├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
│  O  │  E  │  O  │  O  │  E  │  S  │  O  │  E  │
├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
│  E  │  O  │  E  │  H  │  H  │  O  │  E  │  O  │
└─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘

E = Eden Region       S = Survivor Region
O = Old Region        H = Humongous (large objects)

• Heap divided into equal-sized regions
• GC can choose which regions to collect
• Prioritizes regions with most garbage ("Garbage First")
```

#### 4. ZGC (Z Garbage Collector) - Java 11+

```bash
# Ultra-low latency (< 10ms pauses)
java -XX:+UseZGC MyApp

# Best for:
# - Very large heaps (up to 16TB)
# - Latency-sensitive applications
# - Pause times must be minimal
```

#### 5. Shenandoah GC - Java 12+

```bash
# Low-latency, concurrent
java -XX:+UseShenandoahGC MyApp

# Similar to ZGC but different implementation
# Concurrent compaction
```

### GC Algorithm Comparison

| Algorithm | Pause Time | Throughput | Heap Size | Use Case |
|-----------|-----------|------------|-----------|----------|
| Serial | Long | Low | Small | Single-core, small apps |
| Parallel | Medium | High | Medium | Batch processing |
| G1 | Predictable | Medium | Large | General purpose |
| ZGC | Very short | Medium | Very large | Low-latency critical |
| Shenandoah | Very short | Medium | Large | Low-latency |

### Monitoring and Tuning GC

#### GC Logging

```bash
# Enable GC logging (Java 9+)
java -Xlog:gc*:file=gc.log:time,uptime,level,tags MyApp

# Verbose GC output
java -Xlog:gc*=debug:file=gc.log MyApp

# Example output:
# [0.015s][info][gc] Using G1
# [0.234s][info][gc] GC(0) Pause Young (Normal) 24M->8M(256M) 5.123ms
# [1.567s][info][gc] GC(1) Pause Young (Normal) 32M->12M(256M) 7.456ms
```

#### Heap Configuration

```bash
# Initial and maximum heap size
java -Xms512m -Xmx2g MyApp

# Young generation size
java -Xmn256m MyApp

# Survivor ratio (Eden:Survivor)
java -XX:SurvivorRatio=8 MyApp  # Eden is 8x each Survivor

# New/Old generation ratio
java -XX:NewRatio=2 MyApp  # Old is 2x Young

# Metaspace size
java -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m MyApp
```

#### Analyzing GC Logs

```
[GC (Allocation Failure) [PSYoungGen: 65536K->10752K(76288K)]
    65536K->15840K(251392K), 0.0156712 secs]
    [Times: user=0.05 sys=0.01, real=0.02 secs]

Explanation:
- PSYoungGen: Parallel Scavenge Young Generation
- 65536K->10752K: Young gen before -> after
- (76288K): Total young gen capacity
- 65536K->15840K: Total heap before -> after
- (251392K): Total heap capacity
- 0.0156712 secs: GC pause time
```

### finalize() Method (Deprecated)

```java
// Deprecated since Java 9 - DO NOT USE
public class Resource {
    @Override
    protected void finalize() throws Throwable {
        try {
            // Cleanup code (unreliable!)
            System.out.println("Finalize called");
        } finally {
            super.finalize();
        }
    }
}

// Problems with finalize():
// 1. No guarantee when (or if) it will be called
// 2. Can resurrect objects
// 3. Adds GC overhead
// 4. Not thread-safe
// 5. May cause memory leaks
```

### Modern Resource Management

```java
// Better: Use try-with-resources (Java 7+)
public class Resource implements AutoCloseable {
    @Override
    public void close() {
        System.out.println("Resource closed");
    }
}

// Usage
try (Resource r = new Resource()) {
    // Use resource
}  // close() automatically called

// Multiple resources
try (FileInputStream fis = new FileInputStream("input.txt");
     FileOutputStream fos = new FileOutputStream("output.txt")) {
    // Use both streams
}  // Both closed automatically (in reverse order)
```

### Memory Leaks in Java

Despite GC, memory leaks can still occur:

```java
// 1. Static collections that grow indefinitely
public class Cache {
    private static Map<String, Object> cache = new HashMap<>();

    public void add(String key, Object value) {
        cache.put(key, value);  // Never removed!
    }

    // Fix: Use bounded cache or weak references
    private static Map<String, Object> boundedCache =
        new LinkedHashMap<>(100, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > 100;  // Remove oldest when > 100
            }
        };
}

// 2. Unclosed resources
public void readFile() {
    FileReader reader = new FileReader("file.txt");
    // If exception occurs, reader never closed
}

// Fix: try-with-resources
public void readFile() throws IOException {
    try (FileReader reader = new FileReader("file.txt")) {
        // Use reader
    }
}

// 3. Listeners not removed
public class Observable {
    private List<Listener> listeners = new ArrayList<>();

    public void addListener(Listener l) {
        listeners.add(l);
    }

    // Missing: removeListener method or WeakReference
}

// 4. Inner class holding reference to outer class
public class Outer {
    private byte[] largeData = new byte[1000000];

    public Runnable getTask() {
        return new Runnable() {  // Holds reference to Outer
            public void run() {
                System.out.println("Task");
            }
        };
    }

    // Fix: Use static inner class or lambda
    public Runnable getTaskFixed() {
        return () -> System.out.println("Task");  // No outer reference
    }
}

// 5. String.intern() abuse
public void processStrings(List<String> strings) {
    for (String s : strings) {
        String interned = s.intern();  // Added to string pool forever
    }
}
```

### Weak References

Use weak references for caches and listeners:

```java
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

// WeakReference - collected when no strong references exist
WeakReference<ExpensiveObject> weakRef =
    new WeakReference<>(new ExpensiveObject());

ExpensiveObject obj = weakRef.get();  // May return null
if (obj != null) {
    // Use object
}

// WeakHashMap - entries removed when keys are weakly reachable
Map<Key, Value> cache = new WeakHashMap<>();
cache.put(new Key("temp"), new Value());
// Entry automatically removed when Key has no strong references
```

### Reference Types Comparison

```java
// 1. Strong Reference (default)
Object strong = new Object();  // Never collected while referenced

// 2. Weak Reference
WeakReference<Object> weak = new WeakReference<>(new Object());
// Collected at next GC if no strong references

// 3. Soft Reference
SoftReference<Object> soft = new SoftReference<>(new Object());
// Collected only when memory is low

// 4. Phantom Reference
PhantomReference<Object> phantom =
    new PhantomReference<>(new Object(), referenceQueue);
// Used for cleanup, object already finalized

// Reachability order:
// Strong > Soft > Weak > Phantom > Unreachable
```

### Best Practices

1. **Let GC do its job**: Don't call `System.gc()` - JVM knows best
2. **Size heap appropriately**: Not too small (frequent GC), not too large (long pauses)
3. **Use try-with-resources**: For all closeable resources
4. **Avoid finalize()**: Use `Cleaner` (Java 9+) if needed
5. **Profile your application**: Use tools like VisualVM, JConsole, or async-profiler
6. **Choose the right GC**: Match GC algorithm to your requirements
7. **Monitor GC logs**: Watch for frequent Full GCs or long pauses
8. **Avoid memory leaks**: Clear caches, remove listeners, close resources
9. **Use weak references for caches**: Let GC manage cache eviction
10. **Minimize object creation**: Reuse objects when possible

### GC Diagnostic Commands

```bash
# Print GC details before termination
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps MyApp

# Heap dump on OutOfMemoryError
java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heap.hprof MyApp

# Enable NMT (Native Memory Tracking)
java -XX:NativeMemoryTracking=summary MyApp

# JConsole for monitoring
jconsole

# VisualVM for profiling
jvisualvm

# Get heap histogram
jmap -histo <pid>

# Force heap dump
jmap -dump:format=b,file=heap.hprof <pid>
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Classes and Objects | Class is blueprint, Object is instance with actual data |
| Class Members | Fields, methods, constructors, 'this' keyword, getters/setters |
| JVM Memory | Stack (primitives, references), Heap (objects), Metaspace (class data) |
| Static Members | Belong to class, shared among instances |
| OOP Principles | Encapsulation, Inheritance, Polymorphism, Abstraction |
| Inheritance | Code reuse, IS-A relationship, 'extends' keyword |
| Polymorphism | Overloading (compile-time), Overriding (runtime) |
| Abstraction | Abstract classes and interfaces hide complexity |
| Interfaces | Contract, multiple inheritance, default/static methods (Java 8+) |
| Marker Interfaces | Empty interfaces for type signaling (Serializable, Cloneable) |
| Non-Access Modifiers | final, abstract, static, synchronized, volatile, transient, sealed |
| equals/hashCode | Override together, important for collections |
| Garbage Collection | Generational GC, G1/ZGC algorithms, memory management |

## Next Topic

Continue to [Exception Handling](./03-exception-handling.md) to learn about handling errors and exceptions in Java.
