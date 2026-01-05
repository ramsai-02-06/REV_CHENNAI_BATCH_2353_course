# Stream API

## Stream Basics

The Stream API provides a functional approach to processing collections of objects.

### What is a Stream?

A stream is a sequence of elements supporting sequential and parallel aggregate operations.

**Key Characteristics:**
- Not a data structure (doesn't store elements)
- Functional in nature (operations produce result without modifying source)
- Lazy evaluation (intermediate operations not executed until terminal operation)
- Possibly unbounded (infinite streams)
- Consumable (can only be traversed once)

### Creating Streams

```java
import java.util.stream.*;
import java.util.Arrays;
import java.util.List;

// From collection
List<String> list = Arrays.asList("A", "B", "C");
Stream<String> stream1 = list.stream();

// From array
String[] array = {"A", "B", "C"};
Stream<String> stream2 = Arrays.stream(array);
Stream<String> stream3 = Stream.of("A", "B", "C");

// From individual elements
Stream<String> stream4 = Stream.of("A");

// Empty stream
Stream<String> empty = Stream.empty();

// Infinite streams
Stream<Integer> infinite = Stream.iterate(0, n -> n + 1);  // 0, 1, 2, 3, ...
Stream<Double> random = Stream.generate(Math::random);

// From range (IntStream, LongStream, DoubleStream)
IntStream range = IntStream.range(0, 10);        // 0 to 9
IntStream rangeClosed = IntStream.rangeClosed(0, 10);  // 0 to 10

// From String
Stream<String> chars = Stream.of("Hello".split(""));

// From file (Java 8+)
try (Stream<String> lines = Files.lines(Paths.get("file.txt"))) {
    lines.forEach(System.out::println);
}

// Builder pattern
Stream<String> built = Stream.<String>builder()
    .add("A")
    .add("B")
    .add("C")
    .build();
```

### Stream Pipeline

Stream operations form a pipeline: source → intermediate operations → terminal operation

```
Source → filter() → map() → sorted() → collect()
         ↑ Intermediate operations ↑   ↑ Terminal
```

```java
List<String> result = list.stream()         // Source
    .filter(s -> s.length() > 2)            // Intermediate
    .map(String::toUpperCase)               // Intermediate
    .sorted()                                // Intermediate
    .collect(Collectors.toList());          // Terminal

// Nothing happens until terminal operation is called!
Stream<String> stream = list.stream()
    .filter(s -> s.length() > 2)
    .map(String::toUpperCase);
// No processing yet!

long count = stream.count();  // Now processing happens
```

---

## Intermediate Operations

Intermediate operations return a new stream and are lazy (not executed until terminal operation).

### filter()

Keep elements that match predicate.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Filter even numbers
List<Integer> even = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());  // [2, 4, 6, 8, 10]

// Multiple filters
List<Integer> result = numbers.stream()
    .filter(n -> n > 3)
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());  // [4, 6, 8, 10]

// Filter with complex predicate
List<String> words = Arrays.asList("apple", "banana", "cherry", "date");
List<String> filtered = words.stream()
    .filter(w -> w.length() > 5 && w.contains("a"))
    .collect(Collectors.toList());  // [banana, cherry]
```

### map()

Transform elements.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Transform to uppercase
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());  // [ALICE, BOB, CHARLIE]

// Extract length
List<Integer> lengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList());  // [5, 3, 7]

// Complex transformation
class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    String getName() { return name; }
    int getAge() { return age; }
}

List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 30)
);

List<String> names2 = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());  // [Alice, Bob]

List<Integer> ages = people.stream()
    .map(Person::getAge)
    .collect(Collectors.toList());  // [25, 30]
```

### flatMap()

Flatten nested structures.

```java
// List of lists
List<List<Integer>> listOfLists = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5),
    Arrays.asList(6, 7, 8, 9)
);

// Flatten to single list
List<Integer> flattened = listOfLists.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());  // [1, 2, 3, 4, 5, 6, 7, 8, 9]

// Split strings into words
List<String> sentences = Arrays.asList(
    "Hello World",
    "Java Stream API"
);

List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());  // [Hello, World, Java, Stream, API]

// Optional flatMap
List<Optional<String>> optionals = Arrays.asList(
    Optional.of("A"),
    Optional.empty(),
    Optional.of("B")
);

List<String> values = optionals.stream()
    .flatMap(Optional::stream)  // Java 9+
    .collect(Collectors.toList());  // [A, B]
```

### distinct()

Remove duplicates.

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5);

List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());  // [1, 2, 3, 4, 5]

// Works with objects (uses equals/hashCode)
List<String> words = Arrays.asList("apple", "Apple", "APPLE", "banana");
List<String> distinctWords = words.stream()
    .map(String::toLowerCase)
    .distinct()
    .collect(Collectors.toList());  // [apple, banana]
```

### sorted()

Sort elements.

```java
List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);

// Natural order
List<Integer> sorted = numbers.stream()
    .sorted()
    .collect(Collectors.toList());  // [1, 2, 3, 5, 8, 9]

// Reverse order
List<Integer> reversed = numbers.stream()
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());  // [9, 8, 5, 3, 2, 1]

// Custom comparator
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

// By length
List<String> byLength = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());  // [Bob, Alice, Charlie]

// Multiple criteria
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 25),
    new Person("Charlie", 30)
);

List<Person> sorted2 = people.stream()
    .sorted(Comparator.comparing(Person::getAge)
        .thenComparing(Person::getName))
    .collect(Collectors.toList());
```

### peek()

Perform action without modifying stream (for debugging).

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

List<Integer> result = numbers.stream()
    .peek(n -> System.out.println("Original: " + n))
    .map(n -> n * 2)
    .peek(n -> System.out.println("Doubled: " + n))
    .filter(n -> n > 5)
    .peek(n -> System.out.println("Filtered: " + n))
    .collect(Collectors.toList());

// Output shows processing flow:
// Original: 1
// Doubled: 2
// Original: 2
// Doubled: 4
// Original: 3
// Doubled: 6
// Filtered: 6
// ... etc
```

### limit() and skip()

Truncate or skip elements.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Take first 5
List<Integer> first5 = numbers.stream()
    .limit(5)
    .collect(Collectors.toList());  // [1, 2, 3, 4, 5]

// Skip first 5
List<Integer> skip5 = numbers.stream()
    .skip(5)
    .collect(Collectors.toList());  // [6, 7, 8, 9, 10]

// Pagination: Skip first 20, take next 10
List<Integer> page = numbers.stream()
    .skip(20)
    .limit(10)
    .collect(Collectors.toList());

// Infinite stream with limit
List<Integer> first10Even = Stream.iterate(0, n -> n + 2)
    .limit(10)
    .collect(Collectors.toList());  // [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]
```

### takeWhile() and dropWhile() (Java 9+)

Take or drop elements while predicate is true.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1);

// Take while less than 5
List<Integer> taken = numbers.stream()
    .takeWhile(n -> n < 5)
    .collect(Collectors.toList());  // [1, 2, 3, 4]

// Drop while less than 5
List<Integer> dropped = numbers.stream()
    .dropWhile(n -> n < 5)
    .collect(Collectors.toList());  // [5, 4, 3, 2, 1]
```

---

## Terminal Operations

Terminal operations produce a result or side effect and close the stream.

### forEach() and forEachOrdered()

Perform action for each element.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// forEach (order not guaranteed in parallel streams)
names.stream()
    .forEach(System.out::println);

// forEachOrdered (maintains order even in parallel)
names.parallelStream()
    .forEachOrdered(System.out::println);

// With index (using IntStream)
IntStream.range(0, names.size())
    .forEach(i -> System.out.println(i + ": " + names.get(i)));
```

### collect()

Convert stream to collection or other data structure.

See **Collectors** section below for detailed examples.

```java
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());
Map<String, Integer> map = stream.collect(Collectors.toMap(...));
```

### reduce()

Reduce elements to single value.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Sum
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);  // 15

// Using method reference
int sum2 = numbers.stream()
    .reduce(0, Integer::sum);  // 15

// Product
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);  // 120

// Max
Optional<Integer> max = numbers.stream()
    .reduce((a, b) -> a > b ? a : b);  // Optional[5]

// Or using Integer::max
Optional<Integer> max2 = numbers.stream()
    .reduce(Integer::max);  // Optional[5]

// Min
Optional<Integer> min = numbers.stream()
    .reduce(Integer::min);  // Optional[1]

// String concatenation
List<String> words = Arrays.asList("Hello", "World", "!");
String sentence = words.stream()
    .reduce("", (a, b) -> a + " " + b);  // " Hello World !"

// With identity and combiner (for parallel streams)
int sum3 = numbers.parallelStream()
    .reduce(0,              // Identity
            Integer::sum,   // Accumulator
            Integer::sum);  // Combiner
```

### count()

Count elements.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

long count = names.stream()
    .count();  // 3

// Count with filter
long longNames = names.stream()
    .filter(s -> s.length() > 4)
    .count();  // 2
```

### min() and max()

Find minimum or maximum element.

```java
List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);

// Min
Optional<Integer> min = numbers.stream()
    .min(Comparator.naturalOrder());  // Optional[1]

// Max
Optional<Integer> max = numbers.stream()
    .max(Comparator.naturalOrder());  // Optional[9]

// Custom comparator
List<String> words = Arrays.asList("apple", "banana", "cherry");

Optional<String> shortest = words.stream()
    .min(Comparator.comparingInt(String::length));  // Optional[apple]

Optional<String> longest = words.stream()
    .max(Comparator.comparingInt(String::length));  // Optional[banana/cherry]
```

### anyMatch(), allMatch(), noneMatch()

Check if elements match predicate.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// anyMatch: At least one matches
boolean hasEven = numbers.stream()
    .anyMatch(n -> n % 2 == 0);  // true

// allMatch: All match
boolean allPositive = numbers.stream()
    .allMatch(n -> n > 0);  // true

boolean allEven = numbers.stream()
    .allMatch(n -> n % 2 == 0);  // false

// noneMatch: None match
boolean noNegative = numbers.stream()
    .noneMatch(n -> n < 0);  // true

// Short-circuiting (stops as soon as result is determined)
boolean result = Stream.iterate(1, n -> n + 1)
    .anyMatch(n -> n > 100);  // true (stops at 101)
```

### findFirst() and findAny()

Find an element.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// findFirst: First element
Optional<String> first = names.stream()
    .findFirst();  // Optional[Alice]

// findAny: Any element (useful for parallel streams)
Optional<String> any = names.stream()
    .findAny();  // Optional[Alice] (or any element)

// With filter
Optional<String> firstLong = names.stream()
    .filter(s -> s.length() > 5)
    .findFirst();  // Optional[Charlie]

// Short-circuiting
Optional<Integer> firstGreaterThan100 = Stream.iterate(1, n -> n + 1)
    .filter(n -> n > 100)
    .findFirst();  // Optional[101]
```

### toArray()

Convert stream to array.

```java
List<String> list = Arrays.asList("A", "B", "C");

// Object array
Object[] array1 = list.stream()
    .toArray();

// Typed array
String[] array2 = list.stream()
    .toArray(String[]::new);

// With transformation
Integer[] lengths = list.stream()
    .map(String::length)
    .toArray(Integer[]::new);
```

---

## Collectors

Collectors are used with collect() terminal operation to accumulate elements into collections or other results.

### Basic Collectors

```java
import java.util.stream.Collectors;

List<String> list = Arrays.asList("A", "B", "C");

// To List
List<String> toList = list.stream()
    .collect(Collectors.toList());

// To Set
Set<String> toSet = list.stream()
    .collect(Collectors.toSet());

// To specific collection
LinkedList<String> toLinkedList = list.stream()
    .collect(Collectors.toCollection(LinkedList::new));

TreeSet<String> toTreeSet = list.stream()
    .collect(Collectors.toCollection(TreeSet::new));

// To Array (not a Collector, but related)
String[] array = list.stream()
    .toArray(String[]::new);
```

### toMap()

```java
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 30),
    new Person("Charlie", 35)
);

// Map by name
Map<String, Person> byName = people.stream()
    .collect(Collectors.toMap(
        Person::getName,     // Key
        person -> person     // Value
    ));

// Map name to age
Map<String, Integer> nameToAge = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge
    ));

// With duplicate key handling
List<String> words = Arrays.asList("apple", "banana", "apple");
Map<String, Integer> wordLength = words.stream()
    .collect(Collectors.toMap(
        word -> word,           // Key
        String::length,         // Value
        (existing, replacement) -> existing  // Keep existing on duplicate
    ));

// To specific map type
TreeMap<String, Integer> treeMap = people.stream()
    .collect(Collectors.toMap(
        Person::getName,
        Person::getAge,
        (a, b) -> a,
        TreeMap::new
    ));
```

### joining()

Concatenate strings.

```java
List<String> words = Arrays.asList("Hello", "World", "!");

// Simple join
String joined = words.stream()
    .collect(Collectors.joining());  // "HelloWorld!"

// With delimiter
String withComma = words.stream()
    .collect(Collectors.joining(", "));  // "Hello, World, !"

// With delimiter, prefix, suffix
String formatted = words.stream()
    .collect(Collectors.joining(", ", "[", "]"));  // "[Hello, World, !]"

// Common use case
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
String csv = numbers.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(","));  // "1,2,3,4,5"
```

### groupingBy()

Group elements by classifier.

```java
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 25),
    new Person("Charlie", 30),
    new Person("David", 30),
    new Person("Eve", 35)
);

// Group by age
Map<Integer, List<Person>> byAge = people.stream()
    .collect(Collectors.groupingBy(Person::getAge));
// {25=[Alice, Bob], 30=[Charlie, David], 35=[Eve]}

// Group by condition
Map<Boolean, List<Person>> byAgeGroup = people.stream()
    .collect(Collectors.groupingBy(p -> p.getAge() >= 30));
// {false=[Alice, Bob], true=[Charlie, David, Eve]}

// Group and count
Map<Integer, Long> countByAge = people.stream()
    .collect(Collectors.groupingBy(
        Person::getAge,
        Collectors.counting()
    ));
// {25=2, 30=2, 35=1}

// Group and collect names
Map<Integer, List<String>> namesByAge = people.stream()
    .collect(Collectors.groupingBy(
        Person::getAge,
        Collectors.mapping(Person::getName, Collectors.toList())
    ));
// {25=[Alice, Bob], 30=[Charlie, David], 35=[Eve]}

// Group and join names
Map<Integer, String> joinedNamesByAge = people.stream()
    .collect(Collectors.groupingBy(
        Person::getAge,
        Collectors.mapping(
            Person::getName,
            Collectors.joining(", ")
        )
    ));
// {25=Alice, Bob, 30=Charlie, David, 35=Eve}

// Multi-level grouping
List<String> items = Arrays.asList("apple", "apricot", "banana", "berry");

Map<Character, Map<Integer, List<String>>> grouped = items.stream()
    .collect(Collectors.groupingBy(
        s -> s.charAt(0),                    // First level: first character
        Collectors.groupingBy(String::length) // Second level: length
    ));
// {a={5=[apple], 7=[apricot]}, b={6=[banana, berry]}}
```

### partitioningBy()

Partition into two groups based on predicate.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Partition by even/odd
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// {false=[1, 3, 5, 7, 9], true=[2, 4, 6, 8, 10]}

// Partition and count
Map<Boolean, Long> counts = numbers.stream()
    .collect(Collectors.partitioningBy(
        n -> n % 2 == 0,
        Collectors.counting()
    ));
// {false=5, true=5}

List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 30),
    new Person("Charlie", 17)
);

// Partition by adult/minor
Map<Boolean, List<Person>> byAge = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));
```

### Summarizing

Get statistics from numeric stream.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Sum
int sum = numbers.stream()
    .collect(Collectors.summingInt(Integer::intValue));  // 15

// Average
double avg = numbers.stream()
    .collect(Collectors.averagingInt(Integer::intValue));  // 3.0

// Summary statistics (all at once)
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

System.out.println("Count: " + stats.getCount());      // 5
System.out.println("Sum: " + stats.getSum());          // 15
System.out.println("Min: " + stats.getMin());          // 1
System.out.println("Max: " + stats.getMax());          // 5
System.out.println("Average: " + stats.getAverage());  // 3.0

// For objects
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 30),
    new Person("Charlie", 35)
);

IntSummaryStatistics ageStats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));
```

### Custom Collectors

```java
// Collect to immutable list (Java 10+)
List<String> immutable = stream.collect(Collectors.toUnmodifiableList());

// Collect to immutable set
Set<String> immutableSet = stream.collect(Collectors.toUnmodifiableSet());

// Filtering collector (Java 9+)
List<Integer> evenNumbers = numbers.stream()
    .collect(Collectors.filtering(
        n -> n % 2 == 0,
        Collectors.toList()
    ));

// Flat mapping collector (Java 9+)
Map<Integer, List<String>> grouped = people.stream()
    .collect(Collectors.groupingBy(
        Person::getAge,
        Collectors.flatMapping(
            p -> Stream.of(p.getName().split("")),
            Collectors.toList()
        )
    ));
```

---

## Parallel Streams

Process streams in parallel for better performance on multi-core systems.

### Creating Parallel Streams

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// From collection
Stream<Integer> parallel = numbers.parallelStream();

// Convert sequential to parallel
Stream<Integer> parallel2 = numbers.stream().parallel();

// Convert parallel to sequential
Stream<Integer> sequential = parallel.sequential();
```

### Parallel Processing

```java
List<Integer> numbers = IntStream.range(1, 1000000)
    .boxed()
    .collect(Collectors.toList());

// Sequential
long start = System.currentTimeMillis();
long count = numbers.stream()
    .filter(n -> n % 2 == 0)
    .count();
long sequential = System.currentTimeMillis() - start;

// Parallel
start = System.currentTimeMillis();
count = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .count();
long parallel = System.currentTimeMillis() - start;

System.out.println("Sequential: " + sequential + "ms");
System.out.println("Parallel: " + parallel + "ms");
```

### When to Use Parallel Streams

**Use parallel streams when:**
- Large data set (thousands of elements)
- Computationally expensive operations
- Stateless operations
- No shared mutable state

**Avoid parallel streams when:**
- Small data set (overhead > benefit)
- IO-bound operations
- Order matters (use forEachOrdered)
- Shared mutable state (race conditions)

### Thread Safety Issues

```java
// WRONG: Not thread-safe
List<Integer> list = new ArrayList<>();
IntStream.range(0, 1000).parallel()
    .forEach(list::add);  // Race condition!
System.out.println(list.size());  // Less than 1000!

// CORRECT: Thread-safe
List<Integer> list2 = IntStream.range(0, 1000)
    .parallel()
    .boxed()
    .collect(Collectors.toList());  // Thread-safe collector

// CORRECT: Use thread-safe collection
List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
IntStream.range(0, 1000).parallel()
    .forEach(syncList::add);
```

### Performance Considerations

```java
// Bad: Boxing/unboxing overhead
long sum1 = numbers.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();

// Good: Use primitive streams
long sum2 = numbers.parallelStream()
    .mapToInt(i -> i)
    .sum();

// Bad: Stateful operation
numbers.parallelStream()
    .sorted()  // Requires coordination
    .forEach(System.out::println);

// Good: Stateless operations
numbers.parallelStream()
    .filter(n -> n > 100)
    .map(n -> n * 2)
    .collect(Collectors.toList());
```

---

## Primitive Streams

Specialized streams for primitives to avoid boxing/unboxing overhead.

### IntStream, LongStream, DoubleStream

```java
// IntStream
IntStream intStream = IntStream.range(1, 10);  // 1 to 9
IntStream intStreamClosed = IntStream.rangeClosed(1, 10);  // 1 to 10
IntStream fromArray = IntStream.of(1, 2, 3, 4, 5);

// LongStream
LongStream longStream = LongStream.range(1L, 10L);

// DoubleStream
DoubleStream doubleStream = DoubleStream.of(1.0, 2.0, 3.0);

// From collection
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
IntStream intStream2 = numbers.stream()
    .mapToInt(Integer::intValue);
```

### Primitive Stream Operations

```java
IntStream numbers = IntStream.range(1, 10);

// Sum
int sum = numbers.sum();  // 45

// Average
OptionalDouble avg = IntStream.range(1, 10)
    .average();  // OptionalDouble[5.0]

// Max, Min
OptionalInt max = IntStream.range(1, 10).max();  // OptionalInt[9]
OptionalInt min = IntStream.range(1, 10).min();  // OptionalInt[1]

// Summary statistics
IntSummaryStatistics stats = IntStream.range(1, 10)
    .summaryStatistics();

System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
System.out.println("Average: " + stats.getAverage());
```

### Converting Between Streams

```java
// Primitive to Object
Stream<Integer> boxed = IntStream.range(1, 10)
    .boxed();

// Object to Primitive
IntStream ints = Stream.of(1, 2, 3, 4, 5)
    .mapToInt(Integer::intValue);

// Between primitive types
LongStream longs = IntStream.range(1, 10)
    .mapToLong(i -> i);

DoubleStream doubles = IntStream.range(1, 10)
    .mapToDouble(i -> i);
```

---

## Summary

| Operation | Type | Description | Example |
|-----------|------|-------------|---------|
| filter | Intermediate | Keep matching elements | `filter(n -> n > 5)` |
| map | Intermediate | Transform elements | `map(String::length)` |
| flatMap | Intermediate | Flatten nested structures | `flatMap(List::stream)` |
| distinct | Intermediate | Remove duplicates | `distinct()` |
| sorted | Intermediate | Sort elements | `sorted()` |
| limit | Intermediate | Take first n elements | `limit(10)` |
| skip | Intermediate | Skip first n elements | `skip(5)` |
| forEach | Terminal | Perform action | `forEach(System.out::println)` |
| collect | Terminal | Convert to collection | `collect(Collectors.toList())` |
| reduce | Terminal | Reduce to single value | `reduce(0, Integer::sum)` |
| count | Terminal | Count elements | `count()` |
| anyMatch | Terminal | Check if any match | `anyMatch(n -> n > 5)` |
| findFirst | Terminal | Find first element | `findFirst()` |

## Next Topic

Continue to [Multithreading](./08-multithreading.md) to learn about concurrent programming in Java.
