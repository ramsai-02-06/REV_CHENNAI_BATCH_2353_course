# Multithreading

## Thread Class

A thread is a lightweight process that allows concurrent execution within a program.

### Creating Threads by Extending Thread Class

```java
// Define thread by extending Thread class
public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            try {
                Thread.sleep(500);  // Sleep for 500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();

        thread1.setName("Thread-1");
        thread2.setName("Thread-2");

        thread1.start();  // Start thread (calls run() internally)
        thread2.start();

        // thread1.run();  // DON'T call run() directly - no concurrency!
    }
}
```

### Thread Methods

```java
public class ThreadMethods extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadMethods thread = new ThreadMethods();

        // Get thread name
        String name = thread.getName();

        // Set thread name
        thread.setName("MyThread");

        // Get thread ID
        long id = thread.getId();

        // Get thread priority (1-10, default 5)
        int priority = thread.getPriority();
        thread.setPriority(Thread.MAX_PRIORITY);  // 10
        thread.setPriority(Thread.MIN_PRIORITY);  // 1
        thread.setPriority(Thread.NORM_PRIORITY); // 5

        // Check if alive
        boolean isAlive = thread.isAlive();

        // Start thread
        thread.start();

        // Wait for thread to complete
        thread.join();  // Current thread waits

        // Wait with timeout
        thread.join(1000);  // Wait max 1 second

        // Check if daemon thread
        boolean isDaemon = thread.isDaemon();
        thread.setDaemon(true);  // Must set before start()

        // Get current thread
        Thread current = Thread.currentThread();

        // Sleep current thread
        Thread.sleep(1000);  // Sleep 1 second

        // Yield (hint to scheduler)
        Thread.yield();

        // Interrupt thread
        thread.interrupt();

        // Check if interrupted
        boolean interrupted = thread.isInterrupted();
    }
}
```

### Daemon Threads

Background threads that don't prevent JVM from exiting.

```java
public class DaemonExample {
    public static void main(String[] args) {
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("Daemon running");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        daemon.setDaemon(true);  // Set as daemon before start
        daemon.start();

        // Main thread sleeps for 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main exits - daemon will stop too");
        // JVM exits, daemon thread stops
    }
}
```

---

## Runnable Interface

Preferred way to create threads (composition over inheritance).

### Creating Threads with Runnable

```java
// Define task by implementing Runnable
public class MyTask implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        MyTask task = new MyTask();

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();
    }
}
```

### Anonymous Runnable

```java
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Anonymous Runnable");
    }
});
thread.start();
```

### Lambda Runnable (Java 8+)

```java
// Runnable is a functional interface
Thread thread = new Thread(() -> {
    System.out.println("Lambda Runnable");
    for (int i = 0; i < 5; i++) {
        System.out.println(i);
    }
});
thread.start();

// One-liner
new Thread(() -> System.out.println("Simple task")).start();
```

### Thread vs Runnable

| Approach | Thread Class | Runnable Interface |
|----------|-------------|--------------------|
| Inheritance | Must extend Thread | Can extend other class |
| Reusability | Less flexible | More flexible |
| OOP | IS-A relationship | HAS-A relationship |
| Multiple threads | New class instance | Same Runnable, multiple threads |
| Best practice | Rarely | Preferred |

```java
// Runnable allows multiple threads to execute same task
Runnable task = () -> {
    System.out.println(Thread.currentThread().getName() + " executing");
};

Thread t1 = new Thread(task, "Worker-1");
Thread t2 = new Thread(task, "Worker-2");
Thread t3 = new Thread(task, "Worker-3");

t1.start();
t2.start();
t3.start();
```

---

## Thread States

A thread can be in one of several states during its lifecycle.

### Thread Lifecycle

```
                  NEW
                   ↓
              start()
                   ↓
               RUNNABLE ←─────┐
                │   ↑         │
      run()     │   │         │
      complete  ↓   │         │
                │   │  notify()│
            TERMINATED│  I/O   │
                      │ complete
                      │        │
             WAITING/TIMED_WAITING/BLOCKED
```

### Thread States Explained

```java
public enum State {
    NEW,           // Created but not started
    RUNNABLE,      // Executing or ready to execute
    BLOCKED,       // Waiting for monitor lock
    WAITING,       // Waiting indefinitely
    TIMED_WAITING, // Waiting for specified time
    TERMINATED     // Completed execution
}
```

### State Examples

```java
public class ThreadStates {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);

                synchronized (ThreadStates.class) {
                    ThreadStates.class.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // NEW state
        System.out.println("State: " + thread.getState());  // NEW

        thread.start();

        // RUNNABLE state
        Thread.sleep(100);
        System.out.println("State: " + thread.getState());  // RUNNABLE

        // TIMED_WAITING state (sleeping)
        Thread.sleep(500);
        System.out.println("State: " + thread.getState());  // TIMED_WAITING

        // WAITING state (wait())
        Thread.sleep(2000);
        System.out.println("State: " + thread.getState());  // WAITING

        // Wake up thread
        synchronized (ThreadStates.class) {
            ThreadStates.class.notify();
        }

        thread.join();

        // TERMINATED state
        System.out.println("State: " + thread.getState());  // TERMINATED
    }
}
```

### State Transitions

```java
// NEW → RUNNABLE
Thread thread = new Thread(() -> {});
thread.start();

// RUNNABLE → TIMED_WAITING
Thread.sleep(1000);
thread.join(1000);

// RUNNABLE → WAITING
Object lock = new Object();
synchronized (lock) {
    lock.wait();
}

// RUNNABLE → BLOCKED (waiting for lock)
synchronized (lock) {
    // Another thread holds lock
}

// ANY → TERMINATED
// When run() completes or exception thrown
```

---

## Concurrency Issues

### Race Conditions

Multiple threads accessing shared data simultaneously, leading to unpredictable results.

```java
public class Counter {
    private int count = 0;

    // Not thread-safe
    public void increment() {
        count++;  // Actually 3 operations: read, increment, write
    }

    public int getCount() {
        return count;
    }
}

public class RaceConditionDemo {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        // Create 1000 threads, each increments counter 1000 times
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        // Wait for all threads
        for (Thread thread : threads) {
            thread.join();
        }

        // Expected: 1,000,000
        // Actual: Less than 1,000,000 (race condition!)
        System.out.println("Count: " + counter.getCount());
    }
}
```

### Thread Interference

```java
class SharedData {
    private int value = 0;

    public void increment() {
        int temp = value;      // Thread 1 reads 0
        // Thread 2 reads 0 here (interference!)
        temp = temp + 1;       // Thread 1: temp = 1
        // Thread 2: temp = 1
        value = temp;          // Thread 1 writes 1
        // Thread 2 writes 1 (overwrites!)
    }
}
```

### Memory Consistency Errors

Different threads have inconsistent views of shared data.

```java
class SharedFlag {
    private boolean flag = false;

    public void setFlag() {
        flag = true;  // Thread 1
    }

    public void checkFlag() {
        while (!flag) {  // Thread 2 might never see flag = true
            // Busy wait
        }
    }
}
```

---

## Synchronization

Ensures thread-safe access to shared resources.

### synchronized Method

```java
public class Counter {
    private int count = 0;

    // synchronized method (instance lock)
    public synchronized void increment() {
        count++;
    }

    public synchronized void decrement() {
        count--;
    }

    public synchronized int getCount() {
        return count;
    }
}

// Only one thread can execute synchronized methods at a time
```

### synchronized Block

```java
public class Counter {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        // Other code (not synchronized)

        synchronized (lock) {
            count++;  // Only this is synchronized
        }

        // More code (not synchronized)
    }

    // Synchronize on this
    public void decrement() {
        synchronized (this) {
            count--;
        }
    }
}
```

### synchronized Static Methods

```java
public class SharedResource {
    private static int count = 0;

    // synchronized on class object
    public static synchronized void increment() {
        count++;
    }

    // Equivalent to:
    public static void incrementAlt() {
        synchronized (SharedResource.class) {
            count++;
        }
    }
}
```

### volatile Keyword

Ensures visibility of changes across threads.

```java
class SharedFlag {
    // Without volatile, thread might cache value
    private volatile boolean flag = false;

    public void setFlag() {
        flag = true;  // Immediately visible to all threads
    }

    public void checkFlag() {
        while (!flag) {
            // Will see flag change
        }
    }
}
```

**volatile vs synchronized:**

| Feature | volatile | synchronized |
|---------|----------|--------------|
| Atomicity | No | Yes |
| Visibility | Yes | Yes |
| Locking | No | Yes |
| Use case | Simple flags | Complex operations |

### Lock Interface (java.util.concurrent)

More flexible than synchronized.

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();  // Always unlock in finally
        }
    }

    // Try lock with timeout
    public void tryIncrement() {
        if (lock.tryLock()) {
            try {
                count++;
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("Could not acquire lock");
        }
    }
}
```

---

## Deadlock

Two or more threads waiting for each other indefinitely.

### Deadlock Example

```java
public class DeadlockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1: Holding lock 1");

                try { Thread.sleep(100); } catch (InterruptedException e) {}

                System.out.println("Thread 1: Waiting for lock 2");
                synchronized (lock2) {
                    System.out.println("Thread 1: Holding lock 1 and 2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2: Holding lock 2");

                try { Thread.sleep(100); } catch (InterruptedException e) {}

                System.out.println("Thread 2: Waiting for lock 1");
                synchronized (lock1) {
                    System.out.println("Thread 2: Holding lock 1 and 2");
                }
            }
        });

        thread1.start();
        thread2.start();

        // Deadlock! Both threads waiting for each other
    }
}
```

### Deadlock Conditions

All four must be true for deadlock:

1. **Mutual Exclusion**: Resources cannot be shared
2. **Hold and Wait**: Thread holds resource while waiting for another
3. **No Preemption**: Resources cannot be forcibly taken
4. **Circular Wait**: Circular chain of threads waiting for resources

### Preventing Deadlock

```java
// Solution: Lock ordering
public class DeadlockPrevention {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            // Always acquire locks in same order
            synchronized (lock1) {
                synchronized (lock2) {
                    System.out.println("Thread 1 done");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            // Same order!
            synchronized (lock1) {
                synchronized (lock2) {
                    System.out.println("Thread 2 done");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}

// Solution: Try lock with timeout
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class DeadlockPreventionWithTimeout {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public void method() throws InterruptedException {
        while (true) {
            boolean lock1Acquired = lock1.tryLock(100, TimeUnit.MILLISECONDS);
            if (lock1Acquired) {
                try {
                    boolean lock2Acquired = lock2.tryLock(100, TimeUnit.MILLISECONDS);
                    if (lock2Acquired) {
                        try {
                            // Do work
                            return;
                        } finally {
                            lock2.unlock();
                        }
                    }
                } finally {
                    lock1.unlock();
                }
            }
            // Failed to acquire both locks, retry
            Thread.sleep(100);
        }
    }
}
```

---

## Livelock

Threads actively responding to each other but making no progress.

### Livelock Example

```java
public class LivelockExample {
    static class Spoon {
        private Diner owner;

        public Spoon(Diner owner) {
            this.owner = owner;
        }

        public synchronized void use() {
            System.out.println(owner.name + " is eating");
        }

        public void setOwner(Diner owner) {
            this.owner = owner;
        }

        public Diner getOwner() {
            return owner;
        }
    }

    static class Diner {
        private String name;
        private boolean isHungry;

        public Diner(String name) {
            this.name = name;
            this.isHungry = true;
        }

        public void eatWith(Spoon spoon, Diner spouse) {
            while (isHungry) {
                // If spouse is hungry, give them the spoon
                if (spoon.getOwner() != this) {
                    try { Thread.sleep(1); } catch (InterruptedException e) {}
                    continue;
                }

                if (spouse.isHungry) {
                    System.out.println(name + ": You eat first, " + spouse.name);
                    spoon.setOwner(spouse);
                    continue;  // Livelock! Both keep giving up spoon
                }

                spoon.use();
                isHungry = false;
                System.out.println(name + ": I'm done eating");
                spoon.setOwner(spouse);
            }
        }
    }

    public static void main(String[] args) {
        Diner husband = new Diner("Husband");
        Diner wife = new Diner("Wife");

        Spoon spoon = new Spoon(husband);

        new Thread(() -> husband.eatWith(spoon, wife)).start();
        new Thread(() -> wife.eatWith(spoon, husband)).start();

        // Livelock! Both keep giving spoon to each other
    }
}
```

### Preventing Livelock

```java
// Solution: Random backoff or priority
if (spouse.isHungry) {
    if (Math.random() < 0.5) {  // Random chance
        spoon.setOwner(spouse);
        continue;
    }
}

// Or use priority system
```

---

## Producer-Consumer Problem

Classic synchronization problem.

### Using wait() and notify()

```java
import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int MAX_SIZE = 5;

    public void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (this) {
                // Wait if queue is full
                while (queue.size() == MAX_SIZE) {
                    wait();
                }

                System.out.println("Produced: " + value);
                queue.add(value++);

                // Notify consumer
                notify();

                Thread.sleep(1000);
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                // Wait if queue is empty
                while (queue.isEmpty()) {
                    wait();
                }

                int value = queue.poll();
                System.out.println("Consumed: " + value);

                // Notify producer
                notify();

                Thread.sleep(1000);
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Thread producer = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();
    }
}
```

### Using BlockingQueue

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumerBlocking {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        // Producer
        Thread producer = new Thread(() -> {
            try {
                int value = 0;
                while (true) {
                    queue.put(value);  // Blocks if full
                    System.out.println("Produced: " + value++);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Consumer
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    int value = queue.take();  // Blocks if empty
                    System.out.println("Consumed: " + value);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();
    }
}
```

---

## Thread Pools (Executor Framework)

Reusing threads instead of creating new ones for each task.

### ExecutorService

```java
import java.util.concurrent.*;

public class ThreadPoolExample {
    public static void main(String[] args) {
        // Fixed thread pool
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " by " +
                    Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();  // No new tasks accepted

        // Wait for all tasks to complete
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();  // Force shutdown
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
```

### Types of Thread Pools

```java
// 1. Fixed Thread Pool (fixed number of threads)
ExecutorService fixed = Executors.newFixedThreadPool(5);

// 2. Cached Thread Pool (creates threads as needed)
ExecutorService cached = Executors.newCachedThreadPool();

// 3. Single Thread Executor (single worker thread)
ExecutorService single = Executors.newSingleThreadExecutor();

// 4. Scheduled Thread Pool (scheduled/periodic tasks)
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(3);

// Schedule task after delay
scheduled.schedule(() -> System.out.println("Task"), 5, TimeUnit.SECONDS);

// Schedule periodic task
scheduled.scheduleAtFixedRate(
    () -> System.out.println("Periodic"),
    0,      // Initial delay
    1,      // Period
    TimeUnit.SECONDS
);
```

### Future and Callable

```java
import java.util.concurrent.*;

public class FutureExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Callable returns a value
        Callable<Integer> task = () -> {
            Thread.sleep(2000);
            return 42;
        };

        // Submit task and get Future
        Future<Integer> future = executor.submit(task);

        System.out.println("Task submitted");

        // Do other work while task executes

        // Get result (blocks until complete)
        Integer result = future.get();
        System.out.println("Result: " + result);

        // With timeout
        try {
            Integer result2 = future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Task timed out");
            future.cancel(true);  // Cancel task
        }

        // Check status
        boolean done = future.isDone();
        boolean cancelled = future.isCancelled();

        executor.shutdown();
    }
}
```

---

## CompletableFuture (Java 8+)

`CompletableFuture` provides a powerful way to write asynchronous, non-blocking code with a fluent API.

### Creating CompletableFuture

```java
import java.util.concurrent.CompletableFuture;

// Run async task with no return value
CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
});

// Run async task with return value
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
    return "Hello from async";
});

// Create completed future
CompletableFuture<String> completed = CompletableFuture.completedFuture("Done");

// Create with custom executor
ExecutorService executor = Executors.newFixedThreadPool(4);
CompletableFuture<String> customExecutor = CompletableFuture.supplyAsync(
    () -> "Custom executor",
    executor
);
```

### Transforming Results

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "hello");

// thenApply: Transform result (like map)
CompletableFuture<String> upper = future.thenApply(s -> s.toUpperCase());
// "HELLO"

// thenApply chain
CompletableFuture<Integer> length = future
    .thenApply(String::toUpperCase)
    .thenApply(String::length);
// 5

// thenCompose: Chain another CompletableFuture (like flatMap)
CompletableFuture<String> composed = future.thenCompose(s ->
    CompletableFuture.supplyAsync(() -> s + " world")
);
// "hello world"

// thenCombine: Combine two futures
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

CompletableFuture<String> combined = future1.thenCombine(future2,
    (s1, s2) -> s1 + " " + s2);
// "Hello World"
```

### Consuming Results

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Result");

// thenAccept: Consume result (no return)
future.thenAccept(result -> System.out.println("Got: " + result));

// thenRun: Run action after completion (no access to result)
future.thenRun(() -> System.out.println("Completed!"));

// Get result (blocking)
String result = future.get();                    // May throw exceptions
String result2 = future.get(5, TimeUnit.SECONDS); // With timeout
String result3 = future.join();                   // Unchecked exception
```

### Error Handling

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (true) throw new RuntimeException("Error!");
    return "Success";
});

// exceptionally: Handle exception and provide fallback
CompletableFuture<String> recovered = future.exceptionally(ex -> {
    System.out.println("Error: " + ex.getMessage());
    return "Fallback value";
});

// handle: Handle both success and failure
CompletableFuture<String> handled = future.handle((result, ex) -> {
    if (ex != null) {
        return "Error: " + ex.getMessage();
    }
    return result.toUpperCase();
});

// whenComplete: Perform action on completion (doesn't transform)
future.whenComplete((result, ex) -> {
    if (ex != null) {
        System.out.println("Failed: " + ex.getMessage());
    } else {
        System.out.println("Success: " + result);
    }
});
```

### Combining Multiple Futures

```java
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Task 1");
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Task 2");
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "Task 3");

// allOf: Wait for all to complete
CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);
all.thenRun(() -> {
    // All completed - get results
    String r1 = f1.join();
    String r2 = f2.join();
    String r3 = f3.join();
    System.out.println(r1 + ", " + r2 + ", " + r3);
});

// Collect all results
CompletableFuture<List<String>> allResults = CompletableFuture.allOf(f1, f2, f3)
    .thenApply(v -> Stream.of(f1, f2, f3)
        .map(CompletableFuture::join)
        .collect(Collectors.toList()));

// anyOf: Complete when any one completes
CompletableFuture<Object> any = CompletableFuture.anyOf(f1, f2, f3);
any.thenAccept(result -> System.out.println("First: " + result));
```

### Async Variants

```java
// Sync version (runs in same thread or calling thread)
future.thenApply(s -> s.toUpperCase());

// Async version (runs in ForkJoinPool.commonPool())
future.thenApplyAsync(s -> s.toUpperCase());

// Async with custom executor
future.thenApplyAsync(s -> s.toUpperCase(), customExecutor);

// All transformation methods have async variants:
// thenApplyAsync, thenAcceptAsync, thenRunAsync,
// thenComposeAsync, thenCombineAsync, handleAsync, etc.
```

### Practical Example: Parallel API Calls

```java
public class AsyncApiExample {
    private final HttpClient client = HttpClient.newHttpClient();

    public CompletableFuture<String> fetchUser(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate API call
            return "{\"id\": " + userId + ", \"name\": \"User" + userId + "\"}";
        });
    }

    public CompletableFuture<String> fetchOrders(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate API call
            return "[{\"orderId\": 1}, {\"orderId\": 2}]";
        });
    }

    public CompletableFuture<String> getUserWithOrders(Long userId) {
        CompletableFuture<String> userFuture = fetchUser(userId);
        CompletableFuture<String> ordersFuture = fetchOrders(userId);

        return userFuture.thenCombine(ordersFuture, (user, orders) -> {
            return "{\"user\": " + user + ", \"orders\": " + orders + "}";
        });
    }

    public static void main(String[] args) {
        AsyncApiExample api = new AsyncApiExample();

        api.getUserWithOrders(1L)
            .thenAccept(System.out::println)
            .exceptionally(ex -> {
                System.err.println("Error: " + ex.getMessage());
                return null;
            })
            .join();  // Wait for completion
    }
}
```

### Timeouts (Java 9+)

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    try {
        Thread.sleep(5000);  // Long running task
    } catch (InterruptedException e) { }
    return "Result";
});

// Complete with timeout
CompletableFuture<String> withTimeout = future
    .orTimeout(2, TimeUnit.SECONDS);  // Throws TimeoutException

// Complete with default on timeout
CompletableFuture<String> withDefault = future
    .completeOnTimeout("Default", 2, TimeUnit.SECONDS);
```

### CompletableFuture Summary

| Method | Description |
|--------|-------------|
| `supplyAsync` | Run task with return value |
| `runAsync` | Run task without return value |
| `thenApply` | Transform result |
| `thenCompose` | Chain another CompletableFuture |
| `thenCombine` | Combine two futures |
| `thenAccept` | Consume result |
| `exceptionally` | Handle exception with fallback |
| `handle` | Handle success or failure |
| `allOf` | Wait for all futures |
| `anyOf` | Wait for any future |
| `orTimeout` | Fail on timeout (Java 9+) |
| `completeOnTimeout` | Default on timeout (Java 9+) |

---

## Virtual Threads (Java 21+)

Virtual threads are lightweight threads that dramatically reduce the cost of creating and managing threads, enabling high-throughput concurrent applications.

### Platform Threads vs Virtual Threads

```java
// Platform thread (traditional) - expensive, OS-managed
Thread platformThread = new Thread(() -> {
    System.out.println("Platform thread");
});

// Virtual thread - lightweight, JVM-managed
Thread virtualThread = Thread.ofVirtual().start(() -> {
    System.out.println("Virtual thread");
});
```

**Key differences:**

| Aspect | Platform Threads | Virtual Threads |
|--------|------------------|-----------------|
| Cost | Heavy (~1MB stack) | Lightweight (~few KB) |
| Count | Limited (thousands) | Millions possible |
| Managed by | Operating System | JVM |
| Blocking | Blocks OS thread | Unmounts from carrier |
| Use case | CPU-intensive work | I/O-intensive work |

### Creating Virtual Threads

```java
// Method 1: Thread.ofVirtual()
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Running in virtual thread");
});

// Method 2: Thread.startVirtualThread()
Thread vThread2 = Thread.startVirtualThread(() -> {
    System.out.println("Another virtual thread");
});

// Method 3: Named virtual thread
Thread namedVThread = Thread.ofVirtual()
    .name("my-virtual-thread")
    .start(() -> {
        System.out.println("Named: " + Thread.currentThread().getName());
    });

// Method 4: Virtual thread factory
ThreadFactory factory = Thread.ofVirtual()
    .name("worker-", 0)  // worker-0, worker-1, etc.
    .factory();

Thread t1 = factory.newThread(() -> System.out.println("Task 1"));
Thread t2 = factory.newThread(() -> System.out.println("Task 2"));
t1.start();
t2.start();
```

### ExecutorService with Virtual Threads

```java
// Virtual thread per task executor - ideal for I/O-bound tasks
try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

    // Submit many tasks - each gets its own virtual thread
    for (int i = 0; i < 10000; i++) {
        int taskId = i;
        executor.submit(() -> {
            // Simulate I/O operation
            Thread.sleep(Duration.ofSeconds(1));
            return "Result " + taskId;
        });
    }

}  // Auto-shutdown with try-with-resources
```

### Practical Example: HTTP Client

```java
import java.net.http.*;
import java.net.URI;

public class VirtualThreadHttpClient {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        List<String> urls = List.of(
            "https://api.example.com/users",
            "https://api.example.com/products",
            "https://api.example.com/orders"
        );

        // Fetch all URLs concurrently with virtual threads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = urls.stream()
                .map(url -> executor.submit(() -> fetchUrl(client, url)))
                .toList();

            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        }
    }

    static String fetchUrl(HttpClient client, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
```

### Structured Concurrency (Preview)

Structured concurrency treats multiple tasks as a single unit of work.

```java
import java.util.concurrent.StructuredTaskScope;

// Note: Preview feature, may require --enable-preview

public class StructuredConcurrencyExample {

    record UserData(String user, String orders) {}

    UserData fetchUserData(int userId) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // Fork subtasks
            Future<String> userFuture = scope.fork(() -> fetchUser(userId));
            Future<String> ordersFuture = scope.fork(() -> fetchOrders(userId));

            // Wait for all tasks
            scope.join();
            scope.throwIfFailed();

            // Combine results
            return new UserData(userFuture.resultNow(), ordersFuture.resultNow());
        }
    }

    String fetchUser(int id) { /* fetch from API */ }
    String fetchOrders(int id) { /* fetch from API */ }
}
```

### When to Use Virtual Threads

```java
// GOOD: I/O-bound operations - virtual threads excel here
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Database queries
    executor.submit(() -> database.query("SELECT * FROM users"));

    // HTTP requests
    executor.submit(() -> httpClient.send(request));

    // File I/O
    executor.submit(() -> Files.readString(path));
}

// NOT IDEAL: CPU-bound operations - use platform threads
ExecutorService cpuExecutor = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors()
);
cpuExecutor.submit(() -> computeIntensiveTask());
```

### Best Practices

```java
// 1. Don't pool virtual threads - create new ones as needed
// BAD
ExecutorService pool = Executors.newFixedThreadPool(100);

// GOOD
ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();


// 2. Avoid thread-local heavy usage (can cause memory issues)
// Be cautious with ThreadLocal in virtual threads


// 3. Use try-with-resources for auto-shutdown
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Submit tasks
}  // Automatically shuts down


// 4. Check if running in virtual thread
if (Thread.currentThread().isVirtual()) {
    System.out.println("Running in virtual thread");
}
```

### Virtual Threads vs Traditional Approaches

| Scenario | Before Java 21 | With Virtual Threads |
|----------|----------------|---------------------|
| 10,000 concurrent requests | Complex async/reactive code | Simple blocking code |
| Thread management | Manual pooling required | Create freely |
| Blocking operations | Avoid or use callbacks | Use naturally |
| Code complexity | High (CompletableFuture chains) | Low (sequential style) |
| Debugging | Difficult (async stack traces) | Easy (normal stack traces) |

```java
// Before: Complex async chain
CompletableFuture.supplyAsync(() -> fetchUser())
    .thenCompose(user -> fetchOrders(user))
    .thenCompose(orders -> processOrders(orders))
    .thenAccept(result -> System.out.println(result));

// With Virtual Threads: Simple sequential code
Thread.startVirtualThread(() -> {
    User user = fetchUser();           // Blocks, but it's fine
    List<Order> orders = fetchOrders(user);
    Result result = processOrders(orders);
    System.out.println(result);
});
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Thread Creation | Extend Thread or implement Runnable (preferred) |
| Thread States | NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED |
| Synchronization | synchronized keyword, Lock interface, volatile |
| Race Conditions | Multiple threads accessing shared data without synchronization |
| Deadlock | Threads waiting for each other indefinitely |
| Livelock | Threads actively responding but making no progress |
| Producer-Consumer | Use wait/notify or BlockingQueue |
| Thread Pools | ExecutorService for reusing threads |
| CompletableFuture (Java 8+) | Async programming with fluent API, chaining, error handling |
| Virtual Threads (Java 21+) | Lightweight threads for high-throughput I/O-bound applications |

## Next Topic

Continue to [JVM Internals and Performance](./10-jvm-internals.md) to learn about JVM architecture, memory management, and garbage collection.
