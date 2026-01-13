# JUnit Fundamentals

## What is JUnit?

JUnit is the standard testing framework for Java. JUnit 5 is the current version with modern features and better architecture.

## Setting Up JUnit 5

### Maven

Add to `pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
```

### Gradle

Add to `build.gradle`:

```gradle
testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'

test {
    useJUnitPlatform()
}
```

## Project Structure

```
my-project/
├── src/
│   ├── main/java/
│   │   └── com/example/
│   │       └── Calculator.java
│   └── test/java/
│       └── com/example/
│           └── CalculatorTest.java
└── pom.xml
```

**Convention**: Test class name = Class name + "Test"

## Your First Test

### Class Under Test

```java
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
}
```

### Test Class

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void shouldAddTwoNumbers() {
        Calculator calc = new Calculator();

        int result = calc.add(5, 3);

        assertEquals(8, result);
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        Calculator calc = new Calculator();

        assertThrows(IllegalArgumentException.class, () -> {
            calc.divide(10, 0);
        });
    }
}
```

## Core Annotations

| Annotation | Purpose |
|------------|---------|
| `@Test` | Marks a test method |
| `@BeforeEach` | Runs before each test |
| `@AfterEach` | Runs after each test |
| `@BeforeAll` | Runs once before all tests (static) |
| `@AfterAll` | Runs once after all tests (static) |
| `@DisplayName` | Custom test name |
| `@Disabled` | Skip this test |

## Test Lifecycle

```java
class LifecycleTest {

    @BeforeAll
    static void setupAll() {
        System.out.println("Before all tests");
    }

    @BeforeEach
    void setup() {
        System.out.println("Before each test");
    }

    @Test
    void testOne() {
        System.out.println("Test 1");
    }

    @Test
    void testTwo() {
        System.out.println("Test 2");
    }

    @AfterEach
    void teardown() {
        System.out.println("After each test");
    }

    @AfterAll
    static void teardownAll() {
        System.out.println("After all tests");
    }
}
```

**Output:**
```
Before all tests
Before each test
Test 1
After each test
Before each test
Test 2
After each test
After all tests
```

## Using @BeforeEach

Use `@BeforeEach` to create fresh objects for each test:

```java
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAdd() {
        assertEquals(8, calculator.add(5, 3));
    }

    @Test
    void testSubtract() {
        assertEquals(2, calculator.subtract(5, 3));
    }
}
```

## DisplayName for Readable Tests

```java
@DisplayName("Calculator Tests")
class CalculatorTest {

    @Test
    @DisplayName("Should add two positive numbers")
    void testAddition() {
        Calculator calc = new Calculator();
        assertEquals(8, calc.add(5, 3));
    }

    @Test
    @DisplayName("Should throw exception for division by zero")
    void testDivisionByZero() {
        Calculator calc = new Calculator();
        assertThrows(IllegalArgumentException.class, () -> calc.divide(10, 0));
    }
}
```

## Disabling Tests

```java
@Test
@Disabled("Bug #123 - fix in progress")
void testBrokenFeature() {
    // This test will be skipped
}
```

## Nested Tests

Group related tests using `@Nested`:

```java
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Addition tests")
    class AdditionTests {

        @Test
        void addPositiveNumbers() {
            assertEquals(8, calculator.add(5, 3));
        }

        @Test
        void addNegativeNumbers() {
            assertEquals(-8, calculator.add(-5, -3));
        }
    }

    @Nested
    @DisplayName("Division tests")
    class DivisionTests {

        @Test
        void dividePositiveNumbers() {
            assertEquals(2, calculator.divide(10, 5));
        }

        @Test
        void divideByZeroThrowsException() {
            assertThrows(IllegalArgumentException.class,
                () -> calculator.divide(10, 0));
        }
    }
}
```

## Running Tests

### From IDE

- Right-click test class → Run
- Click green arrow next to test method
- Use keyboard shortcut (Ctrl+Shift+F10 in IntelliJ)

### From Command Line

**Maven:**
```bash
mvn test                           # Run all tests
mvn test -Dtest=CalculatorTest     # Run specific class
mvn test -Dtest=CalculatorTest#testAdd  # Run specific method
```

**Gradle:**
```bash
gradle test                        # Run all tests
gradle test --tests CalculatorTest # Run specific class
```

## Test Results

| Color | Meaning |
|-------|---------|
| Green | Test passed |
| Red | Test failed |
| Yellow | Test skipped/disabled |

## Naming Conventions

### Test Method Names

Good names describe the expected behavior:

```java
// Good names
void shouldReturnSumWhenAddingTwoNumbers()
void shouldThrowExceptionForNullInput()
void emptyCartShouldHaveZeroTotal()

// Poor names
void test1()
void testAdd()
void myTest()
```

### Patterns

1. **Should pattern**: `shouldDoSomethingWhenCondition`
2. **Given-When-Then**: `givenInput_whenAction_thenResult`
3. **Simple description**: `addTwoNumbers`

## Summary

| Concept | Key Point |
|---------|-----------|
| `@Test` | Marks test methods |
| `@BeforeEach` | Setup before each test |
| `@AfterEach` | Cleanup after each test |
| `@DisplayName` | Readable test names |
| `@Nested` | Group related tests |
| Test location | `src/test/java` |
| Naming | ClassNameTest |

## Next Topic

Continue to [Writing Tests](./03-writing-tests.md) to learn about structuring tests and using assertions.
