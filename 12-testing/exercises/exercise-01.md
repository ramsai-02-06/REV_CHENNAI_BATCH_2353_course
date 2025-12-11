# Exercise: Test Suite for Calculator Service

## Objective
Create a comprehensive test suite using JUnit 5 and Mockito that demonstrates unit testing, mocking, test lifecycle, and various assertion techniques.

## Requirements

### Calculator Service to Test

```java
public class CalculatorService {
    private final AuditLogger auditLogger;
    private final MathValidator validator;

    // Basic operations
    public double add(double a, double b);
    public double subtract(double a, double b);
    public double multiply(double a, double b);
    public double divide(double a, double b) throws ArithmeticException;

    // Advanced operations
    public double power(double base, int exponent);
    public double squareRoot(double number) throws IllegalArgumentException;
    public double percentage(double value, double percent);

    // Operations with validation
    public double calculateDiscount(double price, double discountPercent);
    public double calculateTax(double amount, String state);

    // Batch operations
    public double sum(double... numbers);
    public double average(double... numbers);
}
```

### Test Requirements

1. **Basic Unit Tests**
   - Test all arithmetic operations
   - Test edge cases (zero, negative, large numbers)
   - Test exception scenarios

2. **Parameterized Tests**
   - Test multiple input/output combinations
   - Use @ValueSource, @CsvSource, @MethodSource

3. **Mocking with Mockito**
   - Mock AuditLogger dependency
   - Mock MathValidator for validation tests
   - Verify method invocations
   - Use argument captors

4. **Test Lifecycle**
   - Use @BeforeEach, @AfterEach
   - Use @BeforeAll, @AfterAll
   - Demonstrate test ordering

5. **Advanced Assertions**
   - assertAll for grouped assertions
   - assertThrows for exceptions
   - assertTimeout for performance
   - Use Hamcrest matchers (optional)

### Test Structure

```
src/test/java/
├── service/
│   ├── CalculatorServiceTest.java
│   ├── CalculatorServiceParameterizedTest.java
│   └── CalculatorServiceMockTest.java
└── integration/
    └── CalculatorIntegrationTest.java
```

## Expected Test Examples

```java
@Test
@DisplayName("Division by zero should throw ArithmeticException")
void testDivideByZero() {
    assertThrows(ArithmeticException.class,
        () -> calculator.divide(10, 0));
}

@ParameterizedTest
@CsvSource({"10, 2, 12", "0, 0, 0", "-5, 5, 0"})
void testAddition(double a, double b, double expected) {
    assertEquals(expected, calculator.add(a, b));
}

@Test
void testWithMockedLogger() {
    calculator.add(5, 3);
    verify(auditLogger, times(1)).log(anyString());
}
```

## Skills Tested
- JUnit 5 annotations and lifecycle
- Parameterized tests
- Mockito mocking and verification
- Exception testing
- Test organization and naming
- Assertion techniques

## Bonus Challenges
1. Achieve 100% code coverage
2. Add integration tests with real dependencies
3. Use TestContainers for database tests
4. Implement custom test extensions
