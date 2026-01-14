# Week 04 - Interview FAQ

This document contains frequently asked interview questions and comprehensive answers for Week 04 topics: Object-Oriented Programming, Collections Framework, Multithreading, Functional Programming, and Design Patterns.

---

## Table of Contents

1. [Object-Oriented Programming](#object-oriented-programming)
2. [Collections Framework](#collections-framework)
3. [Multithreading](#multithreading)
4. [Functional Programming](#functional-programming)
5. [Design Patterns](#design-patterns)

---

## Object-Oriented Programming

### Q1: What are the four pillars of Object-Oriented Programming?

**Answer:**

The four pillars of OOP are:

| Pillar | Definition | Purpose |
|--------|------------|---------|
| **Encapsulation** | Bundling data and methods together, hiding internal details | Data protection, controlled access |
| **Inheritance** | Creating new classes based on existing classes | Code reuse, hierarchical relationships |
| **Polymorphism** | Same interface, different implementations | Flexibility, extensibility |
| **Abstraction** | Hiding complexity, showing only essential features | Simplify usage, reduce complexity |

```java
// Example demonstrating all four pillars
public abstract class Animal {  // Abstraction
    private String name;        // Encapsulation (private field)

    public String getName() {   // Encapsulation (public accessor)
        return name;
    }

    public abstract void makeSound();  // Abstraction
}

public class Dog extends Animal {  // Inheritance
    @Override
    public void makeSound() {      // Polymorphism
        System.out.println("Woof!");
    }
}

public class Cat extends Animal {  // Inheritance
    @Override
    public void makeSound() {      // Polymorphism
        System.out.println("Meow!");
    }
}
```

---

### Q2: What is the difference between an abstract class and an interface?

**Answer:**

| Feature | Abstract Class | Interface |
|---------|---------------|-----------|
| **Instantiation** | Cannot be instantiated | Cannot be instantiated |
| **Methods** | Can have abstract and concrete methods | Only abstract (pre-Java 8), default/static allowed (Java 8+) |
| **Variables** | Can have instance variables | Only public static final (constants) |
| **Constructors** | Can have constructors | Cannot have constructors |
| **Inheritance** | Single inheritance (extends one) | Multiple inheritance (implements many) |
| **Access Modifiers** | Any access modifier | Public only (implicitly) |
| **Use Case** | IS-A relationship with shared code | Capability/contract definition |

```java
// Abstract class - shared behavior with some implementation
public abstract class Vehicle {
    protected String brand;

    public void startEngine() {  // Concrete method
        System.out.println("Engine started");
    }

    public abstract void drive();  // Abstract method
}

// Interface - defines capability
public interface Flyable {
    void fly();  // Implicitly public abstract

    default void land() {  // Default method (Java 8+)
        System.out.println("Landing...");
    }
}

// Class using both
public class FlyingCar extends Vehicle implements Flyable {
    @Override
    public void drive() { /* implementation */ }

    @Override
    public void fly() { /* implementation */ }
}
```

**When to use which:**
- **Abstract class**: When classes share code and have IS-A relationship
- **Interface**: When defining a capability that multiple unrelated classes can have

---

### Q3: Explain method overloading vs method overriding.

**Answer:**

| Aspect | Overloading | Overriding |
|--------|-------------|------------|
| **Definition** | Same method name, different parameters | Same method signature in subclass |
| **Binding** | Compile-time (static) | Runtime (dynamic) |
| **Inheritance** | Not required | Required (parent-child) |
| **Parameters** | Must differ | Must be same |
| **Return Type** | Can differ | Must be same or covariant |
| **Access Modifier** | Can be any | Same or less restrictive |

```java
// OVERLOADING - same class, different parameters
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {  // Different parameter types
        return a + b;
    }

    public int add(int a, int b, int c) {    // Different parameter count
        return a + b + c;
    }
}

// OVERRIDING - child class, same signature
public class Animal {
    public void speak() {
        System.out.println("Some sound");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {  // Same signature
        System.out.println("Woof!");
    }
}
```

**Key rule for overriding:**
- Cannot override `final`, `static`, or `private` methods
- Use `@Override` annotation for compile-time verification

---

### Q4: What is the difference between == and equals() in Java?

**Answer:**

| Operator/Method | Compares | Use For |
|-----------------|----------|---------|
| `==` | Memory address (reference) | Primitives, reference identity |
| `equals()` | Content (if properly overridden) | Object content comparison |

```java
// Primitives - use ==
int a = 5;
int b = 5;
System.out.println(a == b);  // true (values equal)

// Objects - == checks reference
String s1 = new String("hello");
String s2 = new String("hello");
System.out.println(s1 == s2);      // false (different objects)
System.out.println(s1.equals(s2)); // true (same content)

// String pool special case
String s3 = "hello";
String s4 = "hello";
System.out.println(s3 == s4);  // true (same pool object)
```

**Implementing equals() and hashCode():**

```java
public class Person {
    private String name;
    private int age;

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

**Contract:** If `equals()` returns true, `hashCode()` must return the same value. This is critical for HashMap/HashSet.

---

### Q5: What are access modifiers in Java?

**Answer:**

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes | No |
| (default) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

```java
public class Example {
    public int publicVar;       // Accessible everywhere
    protected int protectedVar; // Class, package, subclasses
    int defaultVar;             // Class, package only
    private int privateVar;     // Class only

    // Common pattern: private fields, public getters/setters
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

**Best practice:** Use the most restrictive access level possible. Start with `private` and increase visibility only when needed.

---

## Collections Framework

### Q6: What is the difference between ArrayList and LinkedList?

**Answer:**

| Feature | ArrayList | LinkedList |
|---------|-----------|------------|
| **Structure** | Dynamic array | Doubly-linked list |
| **Access by index** | O(1) - fast | O(n) - slow |
| **Insert at end** | O(1) amortized | O(1) |
| **Insert at beginning** | O(n) - slow | O(1) - fast |
| **Memory** | Less overhead | More (stores node references) |
| **Iteration** | Faster (cache-friendly) | Slower |

```java
// ArrayList - best for random access
List<String> arrayList = new ArrayList<>();
arrayList.add("A");
arrayList.get(0);  // O(1)

// LinkedList - best for frequent insertions
List<String> linkedList = new LinkedList<>();
linkedList.addFirst("A");  // O(1)
linkedList.addLast("B");   // O(1)
```

**When to use:**
- **ArrayList**: Default choice, random access, mostly reading
- **LinkedList**: Frequent insertions/deletions at beginning/middle, queue operations

---

### Q7: What is the difference between HashSet, LinkedHashSet, and TreeSet?

**Answer:**

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| **Ordering** | No order | Insertion order | Sorted order |
| **Null** | One null allowed | One null allowed | No null (throws NPE) |
| **Performance** | O(1) | O(1) | O(log n) |
| **Implementation** | Hash table | Hash table + linked list | Red-black tree |

```java
// HashSet - no order guarantee
Set<String> hashSet = new HashSet<>();
hashSet.add("Banana");
hashSet.add("Apple");
hashSet.add("Cherry");
// Output order unpredictable

// LinkedHashSet - maintains insertion order
Set<String> linkedHashSet = new LinkedHashSet<>();
linkedHashSet.add("Banana");
linkedHashSet.add("Apple");
linkedHashSet.add("Cherry");
// Output: Banana, Apple, Cherry

// TreeSet - sorted order
Set<String> treeSet = new TreeSet<>();
treeSet.add("Banana");
treeSet.add("Apple");
treeSet.add("Cherry");
// Output: Apple, Banana, Cherry
```

---

### Q8: What is the difference between HashMap and Hashtable?

**Answer:**

| Feature | HashMap | Hashtable |
|---------|---------|-----------|
| **Thread-safe** | No | Yes (synchronized) |
| **Null keys** | One null key allowed | No null keys |
| **Null values** | Multiple null values | No null values |
| **Performance** | Faster | Slower (synchronization overhead) |
| **Introduced** | Java 1.2 | Java 1.0 (legacy) |
| **Iterator** | Fail-fast | Fail-fast |

```java
// HashMap - not thread-safe, allows null
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put(null, 100);    // OK
hashMap.put("key", null);  // OK

// Hashtable - thread-safe, no nulls
Map<String, Integer> hashtable = new Hashtable<>();
// hashtable.put(null, 100);  // NullPointerException
// hashtable.put("key", null); // NullPointerException

// For thread-safety, prefer ConcurrentHashMap
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```

**Modern recommendation:** Use `HashMap` for single-threaded, `ConcurrentHashMap` for multi-threaded applications. Avoid `Hashtable`.

---

### Q9: How does HashMap work internally?

**Answer:**

HashMap uses an array of buckets with linked lists (or trees for large buckets in Java 8+).

**Structure:**
```
Bucket Array:
[0] -> null
[1] -> Entry(key1, value1) -> Entry(key2, value2) -> null
[2] -> null
[3] -> Entry(key3, value3) -> null
...
```

**Process:**

1. **put(key, value):**
   - Calculate `hashCode()` of key
   - Compute bucket index: `index = hash & (array.length - 1)`
   - If bucket empty, add new entry
   - If bucket has entries, check for existing key using `equals()`
   - If key exists, update value; otherwise, add to chain

2. **get(key):**
   - Calculate hash, find bucket index
   - Traverse chain, use `equals()` to find matching key
   - Return value or null

```java
// Why equals() and hashCode() contract matters
Map<Person, String> map = new HashMap<>();
Person p1 = new Person("John", 25);
map.put(p1, "Engineer");

Person p2 = new Person("John", 25);  // Same content, different object
System.out.println(map.get(p2));  // Works only if equals/hashCode implemented correctly
```

**Java 8+ optimization:** When bucket has > 8 entries, linked list converts to balanced tree (O(log n) instead of O(n)).

---

## Multithreading

### Q10: What are the ways to create a thread in Java?

**Answer:**

**1. Extend Thread class:**
```java
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + getName());
    }
}

MyThread thread = new MyThread();
thread.start();  // NOT run()!
```

**2. Implement Runnable interface (preferred):**
```java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running");
    }
}

Thread thread = new Thread(new MyRunnable());
thread.start();

// Or with lambda
Thread thread2 = new Thread(() -> System.out.println("Lambda runnable"));
thread2.start();
```

**3. Implement Callable interface (returns value):**
```java
Callable<Integer> task = () -> {
    return 42;
};

ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(task);
Integer result = future.get();  // Blocks until complete
executor.shutdown();
```

**Which to use:**
- **Runnable**: Preferred (allows extending another class)
- **Callable**: When you need a return value
- **Thread class**: Only if you need to override other Thread methods

---

### Q11: What is the difference between start() and run()?

**Answer:**

| Method | Behavior |
|--------|----------|
| `start()` | Creates new thread, calls run() in that thread |
| `run()` | Executes in the current thread (no new thread) |

```java
public class ThreadDemo extends Thread {
    @Override
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        ThreadDemo t = new ThreadDemo();

        // CORRECT - new thread
        t.start();
        // Output: Running in: Thread-0

        // WRONG - same thread, no concurrency
        t.run();
        // Output: Running in: main
    }
}
```

**Key point:** Always use `start()` for concurrent execution. Calling `run()` directly defeats the purpose of threading.

---

### Q12: What is synchronization? Why is it needed?

**Answer:**

**Synchronization** ensures that only one thread can access a critical section at a time, preventing race conditions.

**Problem without synchronization:**
```java
public class Counter {
    private int count = 0;

    public void increment() {
        count++;  // Not atomic: read, increment, write
    }
}

// Two threads incrementing 1000 times each
// Expected: 2000, Actual: unpredictable (race condition)
```

**Solution with synchronization:**
```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;  // Now thread-safe
    }

    // Or use synchronized block
    public void incrementBlock() {
        synchronized(this) {
            count++;
        }
    }
}
```

**Types of synchronization:**

```java
// 1. Synchronized method
public synchronized void method() { }

// 2. Synchronized block (more flexible)
public void method() {
    synchronized(lockObject) {
        // Critical section
    }
}

// 3. Static synchronized (class-level lock)
public static synchronized void staticMethod() { }
```

---

### Q13: What is a deadlock? How can it be prevented?

**Answer:**

**Deadlock** occurs when two or more threads are blocked forever, each waiting for the other to release a lock.

```java
// Deadlock example
Object lock1 = new Object();
Object lock2 = new Object();

// Thread 1
synchronized(lock1) {
    Thread.sleep(100);
    synchronized(lock2) { }  // Waits for Thread 2
}

// Thread 2
synchronized(lock2) {
    Thread.sleep(100);
    synchronized(lock1) { }  // Waits for Thread 1
}
// Both threads wait forever!
```

**Four conditions for deadlock:**
1. **Mutual Exclusion**: Resource can't be shared
2. **Hold and Wait**: Thread holds one resource, waits for another
3. **No Preemption**: Resources can't be forcibly taken
4. **Circular Wait**: Circular chain of waiting threads

**Prevention strategies:**

```java
// 1. Lock ordering - always acquire locks in same order
synchronized(lock1) {  // Always lock1 first
    synchronized(lock2) { }
}

// 2. Lock timeout using tryLock()
Lock lock = new ReentrantLock();
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        // Critical section
    } finally {
        lock.unlock();
    }
} else {
    // Handle timeout
}

// 3. Avoid nested locks when possible
```

---

### Q14: What is the difference between wait() and sleep()?

**Answer:**

| Feature | wait() | sleep() |
|---------|--------|---------|
| **Class** | Object | Thread |
| **Lock** | Releases the lock | Does NOT release lock |
| **Usage** | Must be in synchronized block | Anywhere |
| **Wake up** | notify()/notifyAll() or timeout | Timeout only |
| **Purpose** | Inter-thread communication | Pause execution |

```java
// wait() - releases lock, waits for notification
synchronized(lock) {
    while (!condition) {
        lock.wait();  // Releases lock, waits
    }
}

// sleep() - pauses but holds lock
synchronized(lock) {
    Thread.sleep(1000);  // Still holds lock!
}
```

**Producer-Consumer example:**
```java
synchronized(queue) {
    while (queue.isEmpty()) {
        queue.wait();  // Consumer waits, releases lock
    }
    item = queue.remove();
}

// Producer
synchronized(queue) {
    queue.add(item);
    queue.notify();  // Wake up waiting consumer
}
```

---

## Functional Programming

### Q15: What is a lambda expression?

**Answer:**

A **lambda expression** is a concise way to represent an anonymous function that can be passed around.

**Syntax:**
```java
// No parameters
() -> System.out.println("Hello")

// One parameter (parentheses optional)
x -> x * x
(x) -> x * x

// Multiple parameters
(a, b) -> a + b

// Multiple statements
(a, b) -> {
    int sum = a + b;
    return sum;
}
```

**Examples:**
```java
// Before lambdas
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};

// With lambda
Runnable r2 = () -> System.out.println("Running");

// Sorting with lambda
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
names.sort((s1, s2) -> s1.compareTo(s2));

// With method reference
names.sort(String::compareTo);
```

---

### Q16: What is a functional interface?

**Answer:**

A **functional interface** has exactly one abstract method. It can be used as the target for lambda expressions.

```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);

    // Can have default methods
    default int add(int a, int b) {
        return a + b;
    }

    // Can have static methods
    static void print(int value) {
        System.out.println(value);
    }
}

// Usage with lambda
Calculator multiply = (a, b) -> a * b;
System.out.println(multiply.calculate(5, 3));  // 15
```

**Built-in functional interfaces:**

| Interface | Method | Description |
|-----------|--------|-------------|
| `Predicate<T>` | `boolean test(T t)` | Returns true/false |
| `Function<T,R>` | `R apply(T t)` | Transforms T to R |
| `Consumer<T>` | `void accept(T t)` | Consumes, no return |
| `Supplier<T>` | `T get()` | Provides value |
| `BiFunction<T,U,R>` | `R apply(T t, U u)` | Two inputs, one output |

```java
Predicate<String> isEmpty = s -> s.isEmpty();
Function<String, Integer> length = s -> s.length();
Consumer<String> printer = s -> System.out.println(s);
Supplier<String> greeting = () -> "Hello";
```

---

### Q17: What is Optional and why should we use it?

**Answer:**

**Optional** is a container that may or may not contain a non-null value. It helps avoid NullPointerException.

```java
// Without Optional - NPE risk
public String getUserName(User user) {
    return user.getName().toUpperCase();  // NPE if user or name is null
}

// With Optional
public Optional<String> getUserName(Optional<User> userOpt) {
    return userOpt
        .map(User::getName)
        .map(String::toUpperCase);
}
```

**Creating Optional:**
```java
Optional<String> empty = Optional.empty();
Optional<String> present = Optional.of("Hello");       // Throws if null
Optional<String> nullable = Optional.ofNullable(str);  // Allows null
```

**Using Optional:**
```java
Optional<String> opt = Optional.ofNullable(getValue());

// Check and get
if (opt.isPresent()) {
    System.out.println(opt.get());
}

// Better: ifPresent
opt.ifPresent(System.out::println);

// Default value
String value = opt.orElse("default");
String value2 = opt.orElseGet(() -> computeDefault());

// Throw if empty
String value3 = opt.orElseThrow(() -> new RuntimeException("Not found"));

// Transform
Optional<Integer> length = opt.map(String::length);

// Filter
Optional<String> longString = opt.filter(s -> s.length() > 5);
```

**Best practices:**
- Never call `get()` without checking `isPresent()`
- Don't use Optional for method parameters (use overloading instead)
- Use Optional for return types that might be empty
- Never return `null` from a method that returns Optional

---

### Q18: What is a method reference?

**Answer:**

A **method reference** is a shorthand for a lambda that only calls an existing method.

**Types of method references:**

| Type | Syntax | Lambda Equivalent |
|------|--------|------------------|
| Static method | `ClassName::staticMethod` | `x -> ClassName.staticMethod(x)` |
| Instance method (specific object) | `object::instanceMethod` | `x -> object.instanceMethod(x)` |
| Instance method (arbitrary object) | `ClassName::instanceMethod` | `(obj, x) -> obj.instanceMethod(x)` |
| Constructor | `ClassName::new` | `x -> new ClassName(x)` |

```java
// Static method reference
Function<String, Integer> parseInt = Integer::parseInt;
// Same as: s -> Integer.parseInt(s)

// Instance method reference (specific object)
String str = "hello";
Supplier<Integer> length = str::length;
// Same as: () -> str.length()

// Instance method reference (arbitrary object)
Comparator<String> comp = String::compareToIgnoreCase;
// Same as: (s1, s2) -> s1.compareToIgnoreCase(s2)

// Constructor reference
Supplier<ArrayList<String>> listSupplier = ArrayList::new;
Function<Integer, ArrayList<String>> listWithSize = ArrayList::new;
// Same as: () -> new ArrayList<>()
```

---

## Design Patterns

### Q19: What are the SOLID principles?

**Answer:**

| Principle | Description |
|-----------|-------------|
| **S** - Single Responsibility | A class should have only one reason to change |
| **O** - Open/Closed | Open for extension, closed for modification |
| **L** - Liskov Substitution | Subtypes must be substitutable for base types |
| **I** - Interface Segregation | Many specific interfaces > one general interface |
| **D** - Dependency Inversion | Depend on abstractions, not concretions |

```java
// S - Single Responsibility
// BAD
class User {
    void saveToDatabase() { }
    void sendEmail() { }
    void generateReport() { }
}

// GOOD
class User { /* data only */ }
class UserRepository { void save(User u) { } }
class EmailService { void sendEmail(User u, String msg) { } }

// O - Open/Closed
interface Shape { double area(); }
class Circle implements Shape { public double area() { return Math.PI * r * r; } }
class Rectangle implements Shape { public double area() { return w * h; } }
// Add new shapes without modifying existing code

// L - Liskov Substitution
// If Square extends Rectangle, setWidth/setHeight should work correctly
// (Classic violation: Square can't independently set width and height)

// I - Interface Segregation
// BAD
interface Worker { void work(); void eat(); void sleep(); }

// GOOD
interface Workable { void work(); }
interface Eatable { void eat(); }
interface Sleepable { void sleep(); }

// D - Dependency Inversion
// BAD
class OrderService {
    private MySQLDatabase db = new MySQLDatabase();
}

// GOOD
class OrderService {
    private Database db;  // Depend on abstraction
    public OrderService(Database db) { this.db = db; }
}
```

---

### Q20: Explain the Singleton pattern.

**Answer:**

**Singleton** ensures a class has only one instance and provides global access to it.

```java
// 1. Eager initialization
public class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() { }

    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
}

// 2. Lazy initialization (not thread-safe)
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() { }

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}

// 3. Thread-safe with double-checked locking
public class ThreadSafeSingleton {
    private static volatile ThreadSafeSingleton instance;

    private ThreadSafeSingleton() { }

    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}

// 4. Enum (best approach)
public enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        // Business logic
    }
}
```

**Use cases:** Logging, configuration, connection pools, caches.

---

### Q21: Explain the Factory pattern.

**Answer:**

**Factory** creates objects without exposing instantiation logic, allowing subclasses to decide which class to instantiate.

```java
// Product interface
interface Vehicle {
    void drive();
}

// Concrete products
class Car implements Vehicle {
    public void drive() { System.out.println("Driving a car"); }
}

class Motorcycle implements Vehicle {
    public void drive() { System.out.println("Riding a motorcycle"); }
}

class Truck implements Vehicle {
    public void drive() { System.out.println("Driving a truck"); }
}

// Simple Factory
class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        return switch (type.toLowerCase()) {
            case "car" -> new Car();
            case "motorcycle" -> new Motorcycle();
            case "truck" -> new Truck();
            default -> throw new IllegalArgumentException("Unknown vehicle: " + type);
        };
    }
}

// Usage
Vehicle car = VehicleFactory.createVehicle("car");
car.drive();
```

**Benefits:**
- Decouples client from concrete classes
- Centralizes object creation logic
- Easy to add new types

---

### Q22: Explain the Strategy pattern.

**Answer:**

**Strategy** defines a family of algorithms, encapsulates each one, and makes them interchangeable.

```java
// Strategy interface
interface PaymentStrategy {
    void pay(double amount);
}

// Concrete strategies
class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void pay(double amount) {
        System.out.println("Paid $" + amount + " with credit card: " + cardNumber);
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    public void pay(double amount) {
        System.out.println("Paid $" + amount + " via PayPal: " + email);
    }
}

// Context
class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(double amount) {
        paymentStrategy.pay(amount);
    }
}

// Usage
ShoppingCart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
cart.checkout(100.0);

cart.setPaymentStrategy(new PayPalPayment("user@email.com"));
cart.checkout(50.0);
```

**Use cases:** Payment processing, sorting algorithms, compression strategies, validation rules.

---

### Q23: Explain the Observer pattern.

**Answer:**

**Observer** defines a one-to-many dependency where when one object changes state, all dependents are notified.

```java
import java.util.ArrayList;
import java.util.List;

// Observer interface
interface Observer {
    void update(String message);
}

// Subject interface
interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}

// Concrete subject
class NewsAgency implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String news;

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(news);
        }
    }

    public void setNews(String news) {
        this.news = news;
        notifyObservers();
    }
}

// Concrete observers
class NewsChannel implements Observer {
    private String name;

    public NewsChannel(String name) {
        this.name = name;
    }

    public void update(String news) {
        System.out.println(name + " received: " + news);
    }
}

// Usage
NewsAgency agency = new NewsAgency();
agency.attach(new NewsChannel("CNN"));
agency.attach(new NewsChannel("BBC"));

agency.setNews("Breaking news!");
// Output:
// CNN received: Breaking news!
// BBC received: Breaking news!
```

**Use cases:** Event systems, GUI listeners, pub/sub messaging, real-time notifications.

---

### Q24: What is the difference between Composition and Inheritance?

**Answer:**

| Aspect | Inheritance | Composition |
|--------|-------------|-------------|
| **Relationship** | IS-A | HAS-A |
| **Coupling** | Tight | Loose |
| **Flexibility** | Fixed at compile time | Changeable at runtime |
| **Reuse** | Subclass inherits all | Only uses what's needed |

```java
// INHERITANCE - IS-A (Dog IS-A Animal)
class Animal {
    void eat() { System.out.println("Eating"); }
}

class Dog extends Animal {
    void bark() { System.out.println("Barking"); }
}

// COMPOSITION - HAS-A (Car HAS-A Engine)
class Engine {
    void start() { System.out.println("Engine started"); }
}

class Car {
    private Engine engine;  // Composition

    public Car() {
        this.engine = new Engine();
    }

    void start() {
        engine.start();
        System.out.println("Car started");
    }
}
```

**"Favor composition over inheritance"** because:
- More flexible (can change behavior at runtime)
- Avoids tight coupling
- Avoids fragile base class problem
- Better encapsulation

```java
// Composition example - strategy pattern
interface FlyBehavior { void fly(); }

class FlyWithWings implements FlyBehavior {
    public void fly() { System.out.println("Flying with wings"); }
}

class NoFly implements FlyBehavior {
    public void fly() { System.out.println("Can't fly"); }
}

class Bird {
    private FlyBehavior flyBehavior;  // Composition

    public void setFlyBehavior(FlyBehavior fb) {
        this.flyBehavior = fb;  // Can change at runtime!
    }

    public void performFly() {
        flyBehavior.fly();
    }
}
```

---

## Summary

| Topic | Key Concepts |
|-------|--------------|
| **OOP** | Encapsulation, Inheritance, Polymorphism, Abstraction |
| **Collections** | List, Set, Map, Queue; ArrayList vs LinkedList; HashMap internals |
| **Multithreading** | Thread, Runnable, Callable; synchronized; deadlock; wait/notify |
| **Functional** | Lambda, Functional Interface, Optional, Method Reference |
| **Design Patterns** | SOLID, Singleton, Factory, Strategy, Observer |

---

*Week 04 covers the core Java concepts that form the foundation for enterprise application development.*
