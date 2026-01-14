# Week 04 - MCQ Answer Key

This document contains answers and explanations for all 80 questions in `mcq.md`.

---

## Answer Distribution

| Option | Count | Percentage |
|--------|-------|------------|
| A | 20 | 25% |
| B | 20 | 25% |
| C | 20 | 25% |
| D | 20 | 25% |

---

## Object-Oriented Programming (Questions 1-20)

### Question 1
**Answer: A**

The four pillars of OOP are **Abstraction, Encapsulation, Polymorphism, and Inheritance**. These are the fundamental principles that define object-oriented programming.

---

### Question 2
**Answer: B**

**Encapsulation** is hiding internal details and providing access through methods. It bundles data and methods that operate on the data, restricting direct access to some components.

---

### Question 3
**Answer: C**

The `extends` keyword is used to inherit from a class in Java. A class can extend only one other class (single inheritance).

---

### Question 4
**Answer: B**

**Polymorphism** means "many forms" - the same interface with different implementations. It allows objects of different types to be treated uniformly.

---

### Question 5
**Answer: D**

The `private` access modifier allows access only within the same class. It provides the highest level of encapsulation.

---

### Question 6
**Answer: B**

**Overloading** is in the same class with different parameters (compile-time polymorphism). **Overriding** is in a subclass with the same signature (runtime polymorphism).

---

### Question 7
**Answer: C**

The `final` keyword prevents a method from being overridden. Final methods cannot be modified by subclasses.

---

### Question 8
**Answer: B**

An **abstract class** cannot be instantiated and may have abstract methods. It can also have concrete methods with implementations.

---

### Question 9
**Answer: C**

A Java class can extend only **one** class. Java doesn't support multiple inheritance of classes to avoid the diamond problem.

---

### Question 10
**Answer: B**

The `super` keyword refers to the **parent class**. It's used to call parent class constructors and methods.

---

### Question 11
**Answer: B**

An interface defines a **contract of methods** that implementing classes must provide. It specifies what a class must do, not how.

---

### Question 12
**Answer: B**

In Java 8+, interfaces can have **default and static methods with implementations**. This allows adding new methods without breaking existing implementations.

---

### Question 13
**Answer: B**

The `==` operator compares **memory addresses (references)** for objects. It checks if two references point to the same object.

---

### Question 14
**Answer: C**

The `hashCode()` method must be overridden together with `equals()`. This is required for proper behavior in hash-based collections.

---

### Question 15
**Answer: B**

A **marker interface** has no methods and marks a capability. Examples include Serializable, Cloneable, and Remote.

---

### Question 16
**Answer: C**

The `protected` access modifier allows visibility in the **same class, package, and subclasses** (even in different packages).

---

### Question 17
**Answer: B**

**Constructor chaining** is calling one constructor from another using `this()` or `super()`. It helps avoid code duplication.

---

### Question 18
**Answer: B**

The `this` keyword is a reference to the **current object instance**. It distinguishes instance variables from parameters.

---

### Question 19
**Answer: B**

Static methods **belong to the class, not instances**. They can be called without creating an object.

---

### Question 20
**Answer: B**

During garbage collection, **unreferenced objects are removed from memory**. The JVM automatically reclaims memory that is no longer in use.

---

## Collections Framework (Questions 21-40)

### Question 21
**Answer: C**

**Iterable** is at the top of the Collections hierarchy. Collection extends Iterable, and List, Set, Queue extend Collection.

---

### Question 22
**Answer: B**

**ArrayList provides O(1) random access**, while **LinkedList provides O(1) insertion at ends**. ArrayList uses an array; LinkedList uses nodes.

---

### Question 23
**Answer: C**

**HashSet** does NOT allow duplicate elements. It uses hashCode() and equals() to determine uniqueness.

---

### Question 24
**Answer: C**

The time complexity of `get()` in ArrayList is **O(1)** because it provides direct array index access.

---

### Question 25
**Answer: C**

**LinkedHashMap** maintains insertion order. It uses a doubly-linked list to track insertion order.

---

### Question 26
**Answer: B**

**Hashtable is synchronized, HashMap is not**. Hashtable is thread-safe but slower; HashMap is faster but not thread-safe.

---

### Question 27
**Answer: C**

**TreeSet** maintains sorted order. It uses a Red-Black tree implementation.

---

### Question 28
**Answer: B**

HashMap uses an **array of buckets with linked lists (or trees)**. In Java 8+, buckets convert to trees when they have more than 8 entries.

---

### Question 29
**Answer: C**

**Queue** should be used for FIFO (First-In-First-Out) processing. Elements are added at the end and removed from the front.

---

### Question 30
**Answer: B**

When you add a duplicate to a HashSet, **the set remains unchanged**. The add() method returns false.

---

### Question 31
**Answer: C**

HashMap does NOT implement **List**. It implements Map, Serializable, and Cloneable.

---

### Question 32
**Answer: B**

The load factor is the **threshold for resizing (default 0.75)**. When the map is 75% full, it doubles in size.

---

### Question 33
**Answer: C**

In TreeMap, **keys are stored in sorted order**. Null keys are not allowed because it uses comparison.

---

### Question 34
**Answer: B**

ArrayList returns a **fail-fast Iterator**. It throws ConcurrentModificationException if the list is modified during iteration.

---

### Question 35
**Answer: B**

`removeFirst()` removes and returns the first element from a LinkedList. `getFirst()` only retrieves without removing.

---

### Question 36
**Answer: B**

ConcurrentHashMap is used for **thread-safe operations without blocking the entire map**. It uses segment-level locking.

---

### Question 37
**Answer: C**

**Vector** is synchronized. It's a legacy class; for modern code, use ArrayList with explicit synchronization if needed.

---

### Question 38
**Answer: B**

**ListIterator can traverse both directions** (forward and backward). Iterator only moves forward.

---

### Question 39
**Answer: B**

`Collections.sort(list)` is used to sort a List. It uses the natural ordering or a provided Comparator.

---

### Question 40
**Answer: B**

`Collections.unmodifiableList()` returns **a read-only view of the list**. Any modification attempt throws UnsupportedOperationException.

---

## Multithreading (Questions 41-55)

### Question 41
**Answer: B**

You create a thread by implementing the **Runnable interface**. Then pass it to a Thread constructor.

---

### Question 42
**Answer: B**

`start()` creates a new thread and calls `run()` in that thread. Calling `run()` directly executes in the current thread.

---

### Question 43
**Answer: C**

The `join()` method makes a thread wait for another thread to complete. It blocks until the target thread terminates.

---

### Question 44
**Answer: B**

The synchronized keyword **ensures only one thread can access the block at a time**. It provides mutual exclusion.

---

### Question 45
**Answer: B**

A **race condition** occurs when multiple threads access shared data leading to unexpected results due to timing issues.

---

### Question 46
**Answer: B**

A **deadlock** is when two or more threads are blocked forever waiting for each other to release locks.

---

### Question 47
**Answer: B**

`wait()` releases the lock and waits for notification. `sleep()` pauses but does **not release** the lock.

---

### Question 48
**Answer: C**

**ExecutorService** provides thread pool functionality. It manages a pool of worker threads for task execution.

---

### Question 49
**Answer: B**

Callable interface returns a **Future object containing the result**. Unlike Runnable, it can return a value and throw exceptions.

---

### Question 50
**Answer: B**

Thread states are: **NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED**. These represent the thread lifecycle.

---

### Question 51
**Answer: B**

The volatile keyword **ensures visibility of changes across threads**. It prevents caching of the variable in thread-local memory.

---

### Question 52
**Answer: B**

`notify()` wakes up **one waiting thread**. `notifyAll()` wakes up all waiting threads.

---

### Question 53
**Answer: B**

`Thread.currentThread()` returns the current thread. It's a static method that returns the Thread executing the code.

---

### Question 54
**Answer: B**

A **daemon thread** is a background thread that doesn't prevent JVM exit. When only daemon threads remain, JVM terminates.

---

### Question 55
**Answer: D**

**Using more threads** is NOT a valid way to prevent deadlock. It may actually increase the chance of deadlock.

---

## Functional Programming (Questions 56-70)

### Question 56
**Answer: B**

A lambda expression is **a concise way to represent an anonymous function**. It can be passed as an argument or stored in a variable.

---

### Question 57
**Answer: B**

A functional interface has **exactly one abstract method**. It can have multiple default and static methods.

---

### Question 58
**Answer: C**

**Predicate** takes a value and returns a boolean. Its method is `boolean test(T t)`.

---

### Question 59
**Answer: B**

**Consumer** takes a value and returns nothing. Its method is `void accept(T t)`.

---

### Question 60
**Answer: B**

**Optional** is used for avoiding NullPointerException by wrapping potentially null values. It forces explicit handling of absent values.

---

### Question 61
**Answer: C**

`Optional.ofNullable()` creates an Optional that may be null. `Optional.of()` throws NPE if the value is null.

---

### Question 62
**Answer: B**

A method reference is **a shorthand for a lambda that calls an existing method**. It uses the `::` operator.

---

### Question 63
**Answer: B**

The syntax for a static method reference is `ClassName::staticMethod`. Example: `Integer::parseInt`.

---

### Question 64
**Answer: B**

The correct lambda syntax for no parameters is `() -> expression`. Empty parentheses are required.

---

### Question 65
**Answer: A**

`Function<T, R>` **takes T and returns R**. It transforms one type to another.

---

### Question 66
**Answer: B**

`orElse()` is used for **providing a default value if empty**. It returns the default when Optional is empty.

---

### Question 67
**Answer: B**

**Supplier** returns a value without taking any input. Its method is `T get()`.

---

### Question 68
**Answer: C**

`@FunctionalInterface` marks a functional interface. It's optional but provides compile-time checking.

---

### Question 69
**Answer: B**

`String::length` is an **instance method reference on arbitrary object**. It's equivalent to `s -> s.length()`.

---

### Question 70
**Answer: B**

**BiFunction** takes two inputs and returns one output. Its method is `R apply(T t, U u)`.

---

## Design Patterns (Questions 71-80)

### Question 71
**Answer: B**

SOLID's 'S' stands for **Single Responsibility**. A class should have only one reason to change.

---

### Question 72
**Answer: B**

The Singleton pattern ensures **a class can have only one instance** and provides global access to it.

---

### Question 73
**Answer: A**

The Factory pattern is used for **creating objects without exposing instantiation logic**. It centralizes object creation.

---

### Question 74
**Answer: B**

The Strategy pattern **defines interchangeable algorithms**. It allows selecting an algorithm at runtime.

---

### Question 75
**Answer: B**

The Observer pattern is where **objects are notified of state changes**. It implements a publish-subscribe mechanism.

---

### Question 76
**Answer: B**

Open/Closed principle means **open for extension, closed for modification**. Add new behavior without changing existing code.

---

### Question 77
**Answer: B**

**Enum** is the best way to implement Singleton in Java. It's thread-safe, prevents reflection attacks, and handles serialization.

---

### Question 78
**Answer: B**

Composition represents a **HAS-A** relationship. A class contains an instance of another class.

---

### Question 79
**Answer: B**

Dependency Inversion principle means **depend on abstractions, not concretions**. High-level modules shouldn't depend on low-level modules.

---

### Question 80
**Answer: B**

**Inheritance is IS-A (tightly coupled), composition is HAS-A (loosely coupled)**. Composition is generally preferred for flexibility.

---

## Summary Table

| Section | Questions | Answer Distribution |
|---------|-----------|---------------------|
| OOP | 1-20 | A:5, B:5, C:5, D:5 |
| Collections | 21-40 | A:5, B:5, C:5, D:5 |
| Multithreading | 41-55 | A:4, B:4, C:4, D:3 |
| Functional | 56-70 | A:4, B:4, C:3, D:4 |
| Design Patterns | 71-80 | A:2, B:2, C:3, D:3 |
| **Total** | **80** | **A:20, B:20, C:20, D:20** |

---

*Use this answer key to review your understanding of Week 04 topics.*
