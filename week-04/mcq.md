# Week 04 - Multiple Choice Questions

This document contains 80 multiple choice questions covering the key concepts from Week 04 topics: Object-Oriented Programming, Collections Framework, Multithreading, Functional Programming, and Design Patterns.

**Topic Distribution:**
- Object-Oriented Programming: 20 questions
- Collections Framework: 20 questions
- Multithreading: 15 questions
- Functional Programming: 15 questions
- Design Patterns: 10 questions

---

**Note:** Answers and explanations are in `mcq-answers.md`

---

## Object-Oriented Programming

### Question 1
**[OOP]**

What are the four pillars of Object-Oriented Programming?

- A) Abstraction, Encapsulation, Polymorphism, Inheritance
- B) Classes, Objects, Methods, Variables
- C) Public, Private, Protected, Default
- D) Compilation, Execution, Debugging, Testing

---

### Question 2
**[OOP]**

What is encapsulation?

- A) Creating multiple copies of an object
- B) Hiding internal details and providing access through methods
- C) Inheriting properties from a parent class
- D) Converting one data type to another

---

### Question 3
**[OOP]**

What keyword is used to inherit from a class in Java?

- A) implements
- B) inherits
- C) extends
- D) super

---

### Question 4
**[OOP]**

What is polymorphism?

- A) Having multiple constructors
- B) Same interface, different implementations
- C) Hiding internal details
- D) Creating abstract classes

---

### Question 5
**[OOP]**

Which access modifier allows access only within the same class?

- A) public
- B) protected
- C) default
- D) private

---

### Question 6
**[OOP]**

What is the difference between method overloading and overriding?

- A) Overloading changes return type, overriding changes parameters
- B) Overloading is in same class with different parameters, overriding is in subclass with same signature
- C) Overloading requires inheritance, overriding doesn't
- D) There is no difference

---

### Question 7
**[OOP]**

Which keyword prevents a method from being overridden?

- A) static
- B) abstract
- C) final
- D) private

---

### Question 8
**[OOP]**

What is an abstract class?

- A) A class that cannot have methods
- B) A class that cannot be instantiated and may have abstract methods
- C) A class that can only have static methods
- D) A class that can only have one instance

---

### Question 9
**[OOP]**

How many classes can a Java class extend?

- A) Unlimited
- B) Two
- C) One
- D) Zero

---

### Question 10
**[OOP]**

What does the 'super' keyword refer to?

- A) The current class
- B) The parent class
- C) The child class
- D) A static class

---

### Question 11
**[OOP]**

What is the purpose of an interface in Java?

- A) To provide implementation for all methods
- B) To define a contract of methods that implementing classes must provide
- C) To create objects directly
- D) To store data

---

### Question 12
**[OOP]**

Which is true about interfaces in Java 8+?

- A) They can only have abstract methods
- B) They can have default and static methods with implementations
- C) They can have instance variables
- D) They can have constructors

---

### Question 13
**[OOP]**

What does == compare for objects?

- A) Content/values
- B) Memory addresses (references)
- C) Hash codes
- D) Class types

---

### Question 14
**[OOP]**

Which method must be overridden together with equals()?

- A) toString()
- B) clone()
- C) hashCode()
- D) finalize()

---

### Question 15
**[OOP]**

What is a marker interface?

- A) An interface with many methods
- B) An interface with no methods that marks a capability
- C) An interface that marks errors
- D) An interface for logging

---

### Question 16
**[OOP]**

What is the protected access modifier visibility?

- A) Same class only
- B) Same class and package
- C) Same class, package, and subclasses
- D) Everywhere

---

### Question 17
**[OOP]**

What is constructor chaining?

- A) Creating multiple objects
- B) Calling one constructor from another
- C) Inheriting constructors
- D) Static constructor calls

---

### Question 18
**[OOP]**

What is the purpose of the 'this' keyword?

- A) Reference to parent class
- B) Reference to current object instance
- C) Reference to static context
- D) Reference to child class

---

### Question 19
**[OOP]**

Which statement about static methods is true?

- A) They can access instance variables directly
- B) They belong to the class, not instances
- C) They can be overridden
- D) They must be called on objects

---

### Question 20
**[OOP]**

What happens during garbage collection?

- A) Variables are initialized
- B) Unreferenced objects are removed from memory
- C) Classes are loaded
- D) Methods are compiled

---

## Collections Framework

### Question 21
**[Collections]**

Which interface is at the top of the Collections hierarchy?

- A) List
- B) Collection
- C) Iterable
- D) Set

---

### Question 22
**[Collections]**

What is the main difference between ArrayList and LinkedList?

- A) ArrayList uses linked nodes, LinkedList uses array
- B) ArrayList provides O(1) random access, LinkedList provides O(1) insertion at ends
- C) ArrayList is thread-safe, LinkedList is not
- D) ArrayList stores unique elements only

---

### Question 23
**[Collections]**

Which collection does NOT allow duplicate elements?

- A) ArrayList
- B) LinkedList
- C) HashSet
- D) Vector

---

### Question 24
**[Collections]**

What is the time complexity of get() in ArrayList?

- A) O(n)
- B) O(log n)
- C) O(1)
- D) O(n log n)

---

### Question 25
**[Collections]**

Which Map implementation maintains insertion order?

- A) HashMap
- B) TreeMap
- C) LinkedHashMap
- D) Hashtable

---

### Question 26
**[Collections]**

What is the difference between HashMap and Hashtable?

- A) HashMap is synchronized, Hashtable is not
- B) Hashtable is synchronized, HashMap is not
- C) HashMap doesn't allow null, Hashtable does
- D) There is no difference

---

### Question 27
**[Collections]**

Which Set implementation maintains sorted order?

- A) HashSet
- B) LinkedHashSet
- C) TreeSet
- D) EnumSet

---

### Question 28
**[Collections]**

What does HashMap use internally to store entries?

- A) Linked list only
- B) Array of buckets with linked lists (or trees)
- C) Single array
- D) Stack

---

### Question 29
**[Collections]**

Which collection should be used for FIFO processing?

- A) Stack
- B) ArrayList
- C) Queue
- D) Set

---

### Question 30
**[Collections]**

What happens when you add a duplicate to a HashSet?

- A) Exception is thrown
- B) The set remains unchanged
- C) Both elements are kept
- D) The old element is replaced

---

### Question 31
**[Collections]**

Which interface does HashMap NOT implement?

- A) Map
- B) Serializable
- C) List
- D) Cloneable

---

### Question 32
**[Collections]**

What is the load factor in HashMap?

- A) Maximum number of elements
- B) Threshold for resizing (default 0.75)
- C) Initial capacity
- D) Number of buckets

---

### Question 33
**[Collections]**

Which is true about TreeMap?

- A) Elements are in random order
- B) Null keys are allowed
- C) Keys are stored in sorted order
- D) It is synchronized

---

### Question 34
**[Collections]**

What type of Iterator does ArrayList return?

- A) Enumeration
- B) Fail-fast Iterator
- C) Fail-safe Iterator
- D) Static Iterator

---

### Question 35
**[Collections]**

Which method removes and returns the first element from a LinkedList?

- A) getFirst()
- B) removeFirst()
- C) popFirst()
- D) deleteFirst()

---

### Question 36
**[Collections]**

What is ConcurrentHashMap used for?

- A) Single-threaded applications
- B) Thread-safe operations without blocking entire map
- C) Storing primitive types only
- D) Maintaining sorted order

---

### Question 37
**[Collections]**

Which collection class is synchronized?

- A) ArrayList
- B) HashMap
- C) Vector
- D) HashSet

---

### Question 38
**[Collections]**

What is the difference between Iterator and ListIterator?

- A) No difference
- B) ListIterator can traverse both directions
- C) Iterator is faster
- D) ListIterator works with all collections

---

### Question 39
**[Collections]**

Which method is used to sort a List?

- A) list.order()
- B) Collections.sort(list)
- C) list.arrange()
- D) Arrays.sort(list)

---

### Question 40
**[Collections]**

What does Collections.unmodifiableList() return?

- A) A copy of the list
- B) A read-only view of the list
- C) A synchronized list
- D) A sorted list

---

## Multithreading

### Question 41
**[Multithreading]**

How do you create a thread by implementing an interface?

- A) Extend Thread class
- B) Implement Runnable interface
- C) Implement Threadable interface
- D) Extend Runnable class

---

### Question 42
**[Multithreading]**

What is the difference between start() and run()?

- A) No difference
- B) start() creates new thread and calls run(), run() executes in current thread
- C) run() creates new thread, start() doesn't
- D) start() is deprecated

---

### Question 43
**[Multithreading]**

Which method makes a thread wait for another thread to complete?

- A) wait()
- B) sleep()
- C) join()
- D) stop()

---

### Question 44
**[Multithreading]**

What does the synchronized keyword do?

- A) Makes a method faster
- B) Ensures only one thread can access the block at a time
- C) Creates a new thread
- D) Stops a thread

---

### Question 45
**[Multithreading]**

What is a race condition?

- A) Two threads racing to complete first
- B) Multiple threads accessing shared data leading to unexpected results
- C) A thread running faster than expected
- D) A thread timing out

---

### Question 46
**[Multithreading]**

What is a deadlock?

- A) A thread that has stopped
- B) Two or more threads blocked forever waiting for each other
- C) A thread that runs too fast
- D) A thread without a lock

---

### Question 47
**[Multithreading]**

What is the difference between wait() and sleep()?

- A) No difference
- B) wait() releases the lock, sleep() does not
- C) sleep() releases the lock, wait() does not
- D) Both release the lock

---

### Question 48
**[Multithreading]**

Which class provides thread pool functionality?

- A) Thread
- B) Runnable
- C) ExecutorService
- D) Callable

---

### Question 49
**[Multithreading]**

What does Callable interface return?

- A) void
- B) A Future object containing the result
- C) Boolean only
- D) Thread object

---

### Question 50
**[Multithreading]**

What are the thread states in Java?

- A) START, RUN, STOP
- B) NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
- C) BEGIN, MIDDLE, END
- D) CREATED, RUNNING, COMPLETED

---

### Question 51
**[Multithreading]**

What does volatile keyword do?

- A) Makes a variable constant
- B) Ensures visibility of changes across threads
- C) Creates a lock
- D) Stops a thread

---

### Question 52
**[Multithreading]**

What is the purpose of notify()?

- A) Stop all threads
- B) Wake up one waiting thread
- C) Create a new thread
- D) Pause a thread

---

### Question 53
**[Multithreading]**

Which method returns the current thread?

- A) Thread.getThread()
- B) Thread.currentThread()
- C) Thread.this()
- D) Thread.active()

---

### Question 54
**[Multithreading]**

What is a daemon thread?

- A) A high-priority thread
- B) A background thread that doesn't prevent JVM exit
- C) A thread that never stops
- D) A synchronized thread

---

### Question 55
**[Multithreading]**

Which is NOT a valid way to prevent deadlock?

- A) Lock ordering
- B) Lock timeout
- C) Avoiding nested locks
- D) Using more threads

---

## Functional Programming

### Question 56
**[Functional]**

What is a lambda expression?

- A) A regular method
- B) A concise way to represent an anonymous function
- C) A type of loop
- D) A class definition

---

### Question 57
**[Functional]**

What is a functional interface?

- A) Any interface
- B) An interface with exactly one abstract method
- C) An interface with no methods
- D) An interface with only static methods

---

### Question 58
**[Functional]**

Which functional interface takes a value and returns a boolean?

- A) Function
- B) Consumer
- C) Predicate
- D) Supplier

---

### Question 59
**[Functional]**

What does Consumer functional interface do?

- A) Returns a value
- B) Takes a value and returns nothing
- C) Takes nothing and returns a value
- D) Takes two values and returns one

---

### Question 60
**[Functional]**

What is Optional used for?

- A) Making code optional
- B) Avoiding NullPointerException by wrapping potentially null values
- C) Creating optional methods
- D) Optional imports

---

### Question 61
**[Functional]**

Which method creates an Optional that may be null?

- A) Optional.of()
- B) Optional.empty()
- C) Optional.ofNullable()
- D) Optional.create()

---

### Question 62
**[Functional]**

What is a method reference?

- A) A reference to a class
- B) A shorthand for a lambda that calls an existing method
- C) A pointer to memory
- D) A variable reference

---

### Question 63
**[Functional]**

What is the syntax for a static method reference?

- A) object::method
- B) ClassName::staticMethod
- C) method::static
- D) ::method

---

### Question 64
**[Functional]**

Which is the correct lambda syntax for no parameters?

- A) -> expression
- B) () -> expression
- C) {} -> expression
- D) [] -> expression

---

### Question 65
**[Functional]**

What does Function<T, R> interface represent?

- A) Takes T, returns R
- B) Takes nothing, returns R
- C) Takes T, returns nothing
- D) Takes R, returns T

---

### Question 66
**[Functional]**

What is orElse() used for with Optional?

- A) Throwing an exception
- B) Providing a default value if empty
- C) Checking if present
- D) Mapping to another type

---

### Question 67
**[Functional]**

What does Supplier functional interface do?

- A) Takes a value
- B) Returns a value without taking any input
- C) Modifies a value
- D) Compares values

---

### Question 68
**[Functional]**

Which annotation marks a functional interface?

- A) @Function
- B) @Lambda
- C) @FunctionalInterface
- D) @Interface

---

### Question 69
**[Functional]**

What type of method reference is String::length?

- A) Static method reference
- B) Instance method reference on arbitrary object
- C) Constructor reference
- D) Bound method reference

---

### Question 70
**[Functional]**

What does BiFunction take and return?

- A) One input, one output
- B) Two inputs, one output
- C) No inputs, two outputs
- D) Two inputs, no output

---

## Design Patterns

### Question 71
**[Design Patterns]**

What does SOLID's 'S' stand for?

- A) Simple Responsibility
- B) Single Responsibility
- C) Structured Responsibility
- D) Shared Responsibility

---

### Question 72
**[Design Patterns]**

What is the Singleton pattern?

- A) A class with one method
- B) A class that can have only one instance
- C) A class with a single parent
- D) A class with no constructor

---

### Question 73
**[Design Patterns]**

What is the Factory pattern used for?

- A) Creating objects without exposing instantiation logic
- B) Making copies of objects
- C) Destroying objects
- D) Sorting objects

---

### Question 74
**[Design Patterns]**

What is the Strategy pattern?

- A) A pattern for creating objects
- B) A pattern that defines interchangeable algorithms
- C) A pattern for logging
- D) A pattern for database access

---

### Question 75
**[Design Patterns]**

What is the Observer pattern?

- A) A pattern for watching code
- B) A pattern where objects are notified of state changes
- C) A pattern for debugging
- D) A pattern for security

---

### Question 76
**[Design Patterns]**

What does Open/Closed principle mean?

- A) Classes should be open or closed
- B) Open for extension, closed for modification
- C) Methods should be public
- D) Files should be open

---

### Question 77
**[Design Patterns]**

Which is the best way to implement Singleton in Java?

- A) Public constructor
- B) Enum
- C) Static method only
- D) Abstract class

---

### Question 78
**[Design Patterns]**

What relationship does composition represent?

- A) IS-A
- B) HAS-A
- C) USES-A
- D) LIKE-A

---

### Question 79
**[Design Patterns]**

What is Dependency Inversion principle?

- A) Dependencies should be inverted in order
- B) Depend on abstractions, not concretions
- C) Invert method calls
- D) Reverse inheritance

---

### Question 80
**[Design Patterns]**

What is the difference between inheritance and composition?

- A) No difference
- B) Inheritance is IS-A (tightly coupled), composition is HAS-A (loosely coupled)
- C) Composition is IS-A, inheritance is HAS-A
- D) Inheritance is for interfaces only

---

## End of Questions

**Total: 80 Questions**
- Object-Oriented Programming: 20
- Collections Framework: 20
- Multithreading: 15
- Functional Programming: 15
- Design Patterns: 10

---

*Proceed to `mcq-answers.md` for answers and explanations.*
