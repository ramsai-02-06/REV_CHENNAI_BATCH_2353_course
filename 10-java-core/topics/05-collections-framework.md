# Collections Framework

## Overview of Collections Hierarchy

The Java Collections Framework provides a unified architecture for storing and manipulating groups of objects.

### Collections Hierarchy

```
                    Iterable<E>
                        |
                  Collection<E>
                        |
         +--------------+--------------+
         |              |              |
      List<E>        Set<E>        Queue<E>
         |              |              |
    +----+----+    +----+----+    +----+----+
    |    |    |    |    |    |    |    |    |
ArrayList  Vector HashSet TreeSet PriorityQueue
LinkedList Stack LinkedHashSet  ArrayDeque
                SortedSet     Deque

                    Map<E>
                      |
         +------------+------------+
         |            |            |
      HashMap    TreeMap    LinkedHashMap
      Hashtable  SortedMap
```

### Core Interfaces

| Interface | Description | Ordered | Duplicates | Null |
|-----------|-------------|---------|------------|------|
| List | Ordered collection | Yes | Yes | Yes |
| Set | Unique elements | No | No | Once |
| Queue | FIFO processing | Yes | Yes | No* |
| Map | Key-value pairs | No | No (keys) | Once (key) |

*Most Queue implementations don't allow null

### When to Use Which?

```java
// Use List when:
// - Order matters
// - Duplicates allowed
// - Access by index needed
List<String> names = new ArrayList<>();

// Use Set when:
// - Uniqueness required
// - Order doesn't matter
// - Fast lookup needed
Set<String> uniqueIds = new HashSet<>();

// Use Queue when:
// - FIFO processing
// - Priority-based processing
// - Thread-safe operations
Queue<Task> taskQueue = new LinkedList<>();

// Use Map when:
// - Key-value associations
// - Fast lookup by key
// - No duplicate keys
Map<String, User> userMap = new HashMap<>();
```

---

## List Interface

Ordered collection that allows duplicates and indexed access.

### ArrayList

Dynamic array implementation. Best for random access.

```java
// Creating ArrayList
List<String> list = new ArrayList<>();
List<String> list2 = new ArrayList<>(100);  // Initial capacity
List<String> list3 = new ArrayList<>(Arrays.asList("A", "B", "C"));

// Adding elements
list.add("Apple");           // [Apple]
list.add("Banana");          // [Apple, Banana]
list.add(0, "Apricot");      // [Apricot, Apple, Banana]
list.addAll(Arrays.asList("Cherry", "Date"));  // [Apricot, Apple, Banana, Cherry, Date]

// Accessing elements
String first = list.get(0);  // Apricot
String last = list.get(list.size() - 1);  // Date

// Modifying elements
list.set(1, "Avocado");      // [Apricot, Avocado, Banana, Cherry, Date]

// Removing elements
list.remove(0);              // [Avocado, Banana, Cherry, Date]
list.remove("Banana");       // [Avocado, Cherry, Date]

// Searching
boolean contains = list.contains("Cherry");  // true
int index = list.indexOf("Cherry");          // 1

// Size
int size = list.size();      // 3
boolean isEmpty = list.isEmpty();  // false

// Iteration
for (String fruit : list) {
    System.out.println(fruit);
}

// Clear all
list.clear();
```

**ArrayList Characteristics:**
- **Time Complexity:**
  - get(i): O(1)
  - add(element): O(1) amortized
  - add(i, element): O(n)
  - remove(i): O(n)
  - contains(element): O(n)
- **Space:** O(n)
- **Thread-safe:** No

### LinkedList

Doubly-linked list implementation. Best for frequent insertions/deletions.

```java
List<String> list = new LinkedList<>();

// All List operations supported
list.add("First");
list.add("Second");
list.add("Third");

// Additional LinkedList-specific operations
LinkedList<String> linkedList = new LinkedList<>();
linkedList.addFirst("Start");    // Add at beginning
linkedList.addLast("End");       // Add at end
linkedList.removeFirst();        // Remove from beginning
linkedList.removeLast();         // Remove from end
linkedList.getFirst();           // Get first element
linkedList.getLast();            // Get last element

// As a Queue
Queue<String> queue = new LinkedList<>();
queue.offer("Task1");            // Add to queue
queue.offer("Task2");
String task = queue.poll();      // Remove from queue

// As a Deque (double-ended queue)
Deque<String> deque = new LinkedList<>();
deque.offerFirst("Front");
deque.offerLast("Back");
```

**LinkedList Characteristics:**
- **Time Complexity:**
  - get(i): O(n)
  - add(element): O(1)
  - add(i, element): O(n)
  - remove(i): O(n)
  - addFirst/addLast: O(1)
  - removeFirst/removeLast: O(1)
- **Space:** O(n) (more overhead than ArrayList)
- **Thread-safe:** No

### Vector

Synchronized version of ArrayList (legacy, prefer ArrayList).

```java
List<String> vector = new Vector<>();
vector.add("Element");

// Thread-safe but slower
// Better alternatives: Collections.synchronizedList() or CopyOnWriteArrayList
```

### ArrayList vs LinkedList

| Operation | ArrayList | LinkedList |
|-----------|-----------|------------|
| Random Access | O(1) - Fast | O(n) - Slow |
| Add/Remove at end | O(1) - Fast | O(1) - Fast |
| Add/Remove at beginning | O(n) - Slow | O(1) - Fast |
| Add/Remove in middle | O(n) - Slow | O(n) - Slow |
| Memory | Less | More (node overhead) |
| Use case | Random access, less modification | Frequent insertions/deletions |

### List Operations Examples

```java
List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));

// Sorting
Collections.sort(numbers);  // [1, 2, 3, 5, 8, 9]
numbers.sort(Comparator.reverseOrder());  // [9, 8, 5, 3, 2, 1]

// Searching (binary search on sorted list)
Collections.sort(numbers);
int index = Collections.binarySearch(numbers, 5);  // 3

// Reversing
Collections.reverse(numbers);

// Shuffling
Collections.shuffle(numbers);

// Min/Max
int min = Collections.min(numbers);
int max = Collections.max(numbers);

// Frequency
List<String> words = Arrays.asList("a", "b", "a", "c", "a");
int freq = Collections.frequency(words, "a");  // 3

// Sublist
List<Integer> sublist = numbers.subList(1, 4);  // View, not copy

// Converting to array
Integer[] array = numbers.toArray(new Integer[0]);
```

---

## Set Interface

Collection that contains no duplicate elements.

### HashSet

Hash table implementation. No ordering guaranteed.

```java
Set<String> set = new HashSet<>();

// Adding elements
set.add("Apple");     // true
set.add("Banana");    // true
set.add("Apple");     // false (duplicate)

// Size
System.out.println(set.size());  // 2

// Contains
System.out.println(set.contains("Apple"));  // true

// Removing
set.remove("Banana");

// Iteration (order not guaranteed)
for (String fruit : set) {
    System.out.println(fruit);
}

// Set operations
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
Set<Integer> set2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));

// Union
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2);  // [1, 2, 3, 4, 5, 6, 7, 8]

// Intersection
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2);  // [4, 5]

// Difference
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2);  // [1, 2, 3]
```

**HashSet Characteristics:**
- **Time Complexity:**
  - add/remove/contains: O(1) average
- **Space:** O(n)
- **Ordering:** No
- **Null:** One null allowed
- **Thread-safe:** No

### LinkedHashSet

HashSet with predictable iteration order (insertion order).

```java
Set<String> set = new LinkedHashSet<>();
set.add("Zebra");
set.add("Apple");
set.add("Mango");

// Iteration maintains insertion order
for (String fruit : set) {
    System.out.println(fruit);  // Zebra, Apple, Mango
}
```

**LinkedHashSet Characteristics:**
- Same as HashSet but maintains insertion order
- Slightly slower than HashSet due to maintaining linked list

### TreeSet

NavigableSet implementation based on TreeMap. Sorted order.

```java
Set<Integer> set = new TreeSet<>();
set.add(5);
set.add(2);
set.add(8);
set.add(1);

// Iteration in sorted order
for (int num : set) {
    System.out.println(num);  // 1, 2, 5, 8
}

// NavigableSet operations
TreeSet<Integer> treeSet = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));

// Accessing elements
int first = treeSet.first();     // 1
int last = treeSet.last();       // 9
int lower = treeSet.lower(5);    // 3 (< 5)
int floor = treeSet.floor(5);    // 5 (<= 5)
int ceiling = treeSet.ceiling(6); // 7 (>= 6)
int higher = treeSet.higher(5);  // 7 (> 5)

// Subsets
SortedSet<Integer> headSet = treeSet.headSet(5);    // [1, 3]
SortedSet<Integer> tailSet = treeSet.tailSet(5);    // [5, 7, 9]
SortedSet<Integer> subSet = treeSet.subSet(3, 8);   // [3, 5, 7]

// Descending
NavigableSet<Integer> descending = treeSet.descendingSet();  // [9, 7, 5, 3, 1]

// Custom comparator
TreeSet<String> custom = new TreeSet<>(Comparator.reverseOrder());
custom.add("A");
custom.add("C");
custom.add("B");
// Iteration: C, B, A
```

**TreeSet Characteristics:**
- **Time Complexity:**
  - add/remove/contains: O(log n)
- **Space:** O(n)
- **Ordering:** Sorted (natural or comparator)
- **Null:** Not allowed
- **Thread-safe:** No

### HashSet vs LinkedHashSet vs TreeSet

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| Order | None | Insertion | Sorted |
| Performance | Fastest | Medium | Slowest |
| Null | Yes | Yes | No |
| Use case | Unique elements | Unique + order | Unique + sorted |

---

## Queue Interface

Collection for holding elements prior to processing (FIFO typically).

### PriorityQueue

Heap-based priority queue. Elements ordered by natural ordering or comparator.

```java
// Natural ordering (min heap)
Queue<Integer> pq = new PriorityQueue<>();
pq.offer(5);
pq.offer(2);
pq.offer(8);
pq.offer(1);

System.out.println(pq.poll());  // 1
System.out.println(pq.poll());  // 2
System.out.println(pq.poll());  // 5
System.out.println(pq.poll());  // 8

// Custom comparator (max heap)
Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
maxHeap.offer(5);
maxHeap.offer(2);
maxHeap.offer(8);

System.out.println(maxHeap.poll());  // 8
System.out.println(maxHeap.poll());  // 5
System.out.println(maxHeap.poll());  // 2

// Complex objects
class Task {
    String name;
    int priority;

    Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
}

Queue<Task> tasks = new PriorityQueue<>(Comparator.comparingInt(t -> t.priority));
tasks.offer(new Task("Low", 3));
tasks.offer(new Task("High", 1));
tasks.offer(new Task("Medium", 2));

// Processes in priority order: High, Medium, Low
while (!tasks.isEmpty()) {
    System.out.println(tasks.poll().name);
}
```

### Deque (Double-Ended Queue)

Queue that allows insertion and removal at both ends.

```java
Deque<String> deque = new ArrayDeque<>();

// Add to front
deque.offerFirst("First");
deque.addFirst("VeryFirst");

// Add to back
deque.offerLast("Last");
deque.addLast("VeryLast");

// Remove from front
String first = deque.pollFirst();
String alsoFirst = deque.removeFirst();

// Remove from back
String last = deque.pollLast();
String alsoLast = deque.removeLast();

// Peek
String peekFirst = deque.peekFirst();
String peekLast = deque.peekLast();

// As Stack
Deque<String> stack = new ArrayDeque<>();
stack.push("Item1");     // Add to front
stack.push("Item2");
String top = stack.pop(); // Remove from front (LIFO)
```

### Queue Methods

```java
Queue<String> queue = new LinkedList<>();

// Adding (throws exception if fails)
queue.add("Element1");

// Adding (returns false if fails)
queue.offer("Element2");

// Removing (throws exception if empty)
String element = queue.remove();

// Removing (returns null if empty)
String element2 = queue.poll();

// Examining (throws exception if empty)
String head = queue.element();

// Examining (returns null if empty)
String head2 = queue.peek();

// Size and checking
int size = queue.size();
boolean isEmpty = queue.isEmpty();
```

---

## Iterators

Iterators provide a way to traverse collections.

### Iterator Interface

```java
List<String> list = Arrays.asList("A", "B", "C", "D");

// Using Iterator
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    System.out.println(element);

    // Remove while iterating (only with Iterator)
    if (element.equals("B")) {
        iterator.remove();  // Safe removal
    }
}

// ConcurrentModificationException if modifying collection directly
List<String> list2 = new ArrayList<>(Arrays.asList("A", "B", "C"));
for (String s : list2) {
    // list2.remove(s);  // THROWS ConcurrentModificationException
}

// Use Iterator for safe removal
Iterator<String> it = list2.iterator();
while (it.hasNext()) {
    if (it.next().equals("B")) {
        it.remove();  // OK
    }
}
```

### ListIterator

Bidirectional iterator for lists.

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
ListIterator<String> iterator = list.listIterator();

// Forward iteration
while (iterator.hasNext()) {
    int index = iterator.nextIndex();
    String element = iterator.next();
    System.out.println(index + ": " + element);

    // Modify during iteration
    if (element.equals("B")) {
        iterator.set("BB");   // Replace
        iterator.add("B2");   // Insert after
    }
}

// Backward iteration
while (iterator.hasPrevious()) {
    String element = iterator.previous();
    System.out.println(element);
}

// Start from specific position
ListIterator<String> it2 = list.listIterator(2);  // Start from index 2
```

### forEach Method (Java 8+)

```java
List<String> list = Arrays.asList("A", "B", "C");

// Using forEach
list.forEach(element -> System.out.println(element));

// Method reference
list.forEach(System.out::println);

// With index (using streams)
IntStream.range(0, list.size())
    .forEach(i -> System.out.println(i + ": " + list.get(i)));
```

### Spliterator (Java 8+)

Advanced iterator for parallel processing.

```java
List<String> list = Arrays.asList("A", "B", "C", "D");
Spliterator<String> spliterator = list.spliterator();

// Try to split for parallel processing
Spliterator<String> split = spliterator.trySplit();

// Process elements
spliterator.forEachRemaining(System.out::println);
if (split != null) {
    split.forEachRemaining(System.out::println);
}
```

---

## Map Interface

Key-value pairs, no duplicate keys.

### HashMap

Hash table implementation. No ordering.

```java
Map<String, Integer> map = new HashMap<>();

// Adding entries
map.put("Alice", 25);
map.put("Bob", 30);
map.put("Charlie", 35);

// Getting values
int age = map.get("Alice");  // 25
int defaultAge = map.getOrDefault("David", 0);  // 0

// Checking
boolean hasKey = map.containsKey("Alice");    // true
boolean hasValue = map.containsValue(30);     // true

// Removing
map.remove("Bob");
map.remove("Charlie", 35);  // Remove only if value matches

// Size
int size = map.size();
boolean isEmpty = map.isEmpty();

// Updating
map.put("Alice", 26);  // Update existing
map.putIfAbsent("Alice", 27);  // Only if key doesn't exist

// Java 8+ operations
map.computeIfAbsent("David", k -> 40);  // Add if missing
map.computeIfPresent("Alice", (k, v) -> v + 1);  // Update if present
map.merge("Alice", 1, Integer::sum);  // Merge with existing value

// Iterating over keys
for (String key : map.keySet()) {
    System.out.println(key);
}

// Iterating over values
for (Integer value : map.values()) {
    System.out.println(value);
}

// Iterating over entries
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// forEach (Java 8+)
map.forEach((key, value) -> System.out.println(key + ": " + value));
```

**HashMap Characteristics:**
- **Time Complexity:**
  - get/put/remove: O(1) average
- **Space:** O(n)
- **Ordering:** No
- **Null:** One null key, multiple null values
- **Thread-safe:** No

### LinkedHashMap

HashMap with predictable iteration order (insertion order or access order).

```java
// Insertion order
Map<String, Integer> map = new LinkedHashMap<>();
map.put("C", 3);
map.put("A", 1);
map.put("B", 2);

// Iteration in insertion order
map.forEach((k, v) -> System.out.println(k));  // C, A, B

// Access order (LRU cache)
Map<String, Integer> lruMap = new LinkedHashMap<>(16, 0.75f, true);
lruMap.put("A", 1);
lruMap.put("B", 2);
lruMap.put("C", 3);
lruMap.get("A");  // Access A

// Iteration in access order
lruMap.forEach((k, v) -> System.out.println(k));  // B, C, A
```

### TreeMap

NavigableMap implementation based on Red-Black tree. Sorted by keys.

```java
Map<String, Integer> map = new TreeMap<>();
map.put("Charlie", 35);
map.put("Alice", 25);
map.put("Bob", 30);

// Iteration in sorted key order
map.forEach((k, v) -> System.out.println(k));  // Alice, Bob, Charlie

// NavigableMap operations
TreeMap<Integer, String> treeMap = new TreeMap<>();
treeMap.put(3, "Three");
treeMap.put(1, "One");
treeMap.put(5, "Five");
treeMap.put(2, "Two");

// First and last
Map.Entry<Integer, String> first = treeMap.firstEntry();  // 1=One
Map.Entry<Integer, String> last = treeMap.lastEntry();    // 5=Five

// Lower, floor, ceiling, higher
Integer lowerKey = treeMap.lowerKey(3);    // 2
Integer floorKey = treeMap.floorKey(3);    // 3
Integer ceilKey = treeMap.ceilingKey(4);   // 5
Integer higherKey = treeMap.higherKey(3);  // 5

// Submaps
SortedMap<Integer, String> headMap = treeMap.headMap(3);    // {1=One, 2=Two}
SortedMap<Integer, String> tailMap = treeMap.tailMap(3);    // {3=Three, 5=Five}
SortedMap<Integer, String> subMap = treeMap.subMap(2, 5);   // {2=Two, 3=Three}

// Descending
NavigableMap<Integer, String> descending = treeMap.descendingMap();
```

**TreeMap Characteristics:**
- **Time Complexity:**
  - get/put/remove: O(log n)
- **Space:** O(n)
- **Ordering:** Sorted by keys
- **Null:** No null keys (values can be null)
- **Thread-safe:** No

### Hashtable

Synchronized version of HashMap (legacy, prefer HashMap or ConcurrentHashMap).

```java
Map<String, Integer> table = new Hashtable<>();
table.put("Key", 1);

// Thread-safe but slower
// Better: Collections.synchronizedMap() or ConcurrentHashMap
```

### HashMap vs LinkedHashMap vs TreeMap

| Feature | HashMap | LinkedHashMap | TreeMap |
|---------|---------|---------------|---------|
| Order | None | Insertion/Access | Sorted |
| Performance | Fastest | Medium | Slowest |
| Null keys | Yes | Yes | No |
| Use case | General purpose | Preserve order | Sorted keys |

### Common Map Patterns

```java
// Counting occurrences
List<String> words = Arrays.asList("a", "b", "a", "c", "b", "a");
Map<String, Integer> counts = new HashMap<>();

for (String word : words) {
    counts.put(word, counts.getOrDefault(word, 0) + 1);
}
// Or with Java 8+
for (String word : words) {
    counts.merge(word, 1, Integer::sum);
}

// Grouping
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 25),
    new Person("Charlie", 30)
);

Map<Integer, List<Person>> byAge = new HashMap<>();
for (Person person : people) {
    byAge.computeIfAbsent(person.getAge(), k -> new ArrayList<>())
         .add(person);
}

// Inverting map
Map<String, Integer> original = Map.of("A", 1, "B", 2);
Map<Integer, String> inverted = new HashMap<>();
original.forEach((k, v) -> inverted.put(v, k));
```

---

## Collections Utility Class

Static methods for operating on collections.

### Sorting

```java
List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));

// Natural order
Collections.sort(list);  // [1, 2, 5, 8, 9]

// Reverse order
Collections.sort(list, Collections.reverseOrder());  // [9, 8, 5, 2, 1]

// Custom comparator
Collections.sort(list, Comparator.comparingInt(i -> -i));
```

### Searching

```java
List<Integer> list = Arrays.asList(1, 2, 5, 8, 9);

// Binary search (list must be sorted)
int index = Collections.binarySearch(list, 5);  // 2

// Not found returns negative (insertion point)
int index2 = Collections.binarySearch(list, 3);  // -3
```

### Reversing, Shuffling

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// Reverse
Collections.reverse(list);  // [5, 4, 3, 2, 1]

// Shuffle
Collections.shuffle(list);  // Random order

// Rotate
Collections.rotate(list, 2);  // Shift right by 2

// Swap
Collections.swap(list, 0, 4);  // Swap elements at index 0 and 4
```

### Min, Max, Frequency

```java
List<Integer> list = Arrays.asList(5, 2, 8, 1, 9, 2);

int min = Collections.min(list);  // 1
int max = Collections.max(list);  // 9
int freq = Collections.frequency(list, 2);  // 2
```

### Immutable Collections

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// Unmodifiable view
List<String> unmodifiable = Collections.unmodifiableList(list);
// unmodifiable.add("D");  // UnsupportedOperationException

// Changes to original affect unmodifiable
list.add("D");
System.out.println(unmodifiable);  // [A, B, C, D]

// Truly immutable (Java 9+)
List<String> immutable = List.of("A", "B", "C");
// immutable.add("D");  // UnsupportedOperationException

// Immutable collections (Java 9+)
List<String> immutableList = List.of("A", "B", "C");
Set<String> immutableSet = Set.of("A", "B", "C");
Map<String, Integer> immutableMap = Map.of("A", 1, "B", 2);
```

### Synchronized Collections

```java
List<String> list = new ArrayList<>();
List<String> syncList = Collections.synchronizedList(list);

Set<String> set = new HashSet<>();
Set<String> syncSet = Collections.synchronizedSet(set);

Map<String, Integer> map = new HashMap<>();
Map<String, Integer> syncMap = Collections.synchronizedMap(map);

// Must synchronize on collection when iterating
synchronized (syncList) {
    for (String s : syncList) {
        System.out.println(s);
    }
}
```

---

## Summary

| Collection | Implementation | Ordered | Duplicates | Null | Performance |
|------------|----------------|---------|------------|------|-------------|
| ArrayList | Dynamic array | Yes | Yes | Yes | O(1) get, O(n) add/remove |
| LinkedList | Doubly-linked list | Yes | Yes | Yes | O(n) get, O(1) add/remove at ends |
| HashSet | Hash table | No | No | Yes (once) | O(1) add/remove/contains |
| LinkedHashSet | Hash table + linked list | Insertion | No | Yes (once) | O(1) add/remove/contains |
| TreeSet | Red-Black tree | Sorted | No | No | O(log n) add/remove/contains |
| PriorityQueue | Heap | Priority | Yes | No | O(log n) add/remove |
| HashMap | Hash table | No | No (keys) | Yes (once) | O(1) get/put |
| LinkedHashMap | Hash table + linked list | Insertion/Access | No (keys) | Yes (once) | O(1) get/put |
| TreeMap | Red-Black tree | Sorted | No (keys) | No (keys) | O(log n) get/put |

## Next Topic

Continue to [Functional Programming](./06-functional-programming.md) to learn about functional interfaces, lambdas, and method references.
