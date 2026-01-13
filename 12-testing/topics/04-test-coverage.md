# Test Coverage

## What is Code Coverage?

Code coverage measures how much of your code is executed when tests run. It helps identify untested code.

**Important**: High coverage doesn't guarantee bug-free code - it only shows what code runs during tests.

## Types of Coverage

### Line Coverage

Percentage of code lines executed during testing.

```java
public int getDiscount(boolean isPremium) {
    int discount = 0;              // Line 1
    if (isPremium) {               // Line 2
        discount = 20;             // Line 3
    }
    return discount;               // Line 4
}
```

**Test:**
```java
@Test
void testPremiumDiscount() {
    assertEquals(20, getDiscount(true));
}
```

**Result**: 100% line coverage (all 4 lines executed)

### Branch Coverage

Percentage of decision branches (if/else paths) executed.

Same code, same test:
- `if (isPremium)` → TRUE branch tested ✓
- `if (isPremium)` → FALSE branch NOT tested ✗

**Branch Coverage**: 50% (only 1 of 2 branches)

### Why Branch Coverage Matters

```java
public String getStatus(int score) {
    if (score >= 90) {
        return "A";
    } else if (score >= 80) {
        return "B";
    } else {
        return "C";
    }
}
```

**Insufficient test:**
```java
@Test
void testGradeA() {
    assertEquals("A", getStatus(95));
}
```
- Line coverage: High (most lines run)
- Branch coverage: Low (only 1 of 3 branches)

**Complete tests:**
```java
@Test void testGradeA() { assertEquals("A", getStatus(95)); }
@Test void testGradeB() { assertEquals("B", getStatus(85)); }
@Test void testGradeC() { assertEquals("C", getStatus(70)); }
```

## Line vs Branch Coverage

| Aspect | Line Coverage | Branch Coverage |
|--------|---------------|-----------------|
| Measures | Lines executed | Decision paths taken |
| Thoroughness | Basic | More comprehensive |
| Difficulty | Easier to achieve | Harder to achieve |

## Coverage Example

```java
public double calculateShipping(double weight, boolean express) {
    double cost = 5.0;                    // Base cost

    if (weight > 10) {                    // Branch 1
        cost += 10.0;
    }

    if (express) {                        // Branch 2
        cost *= 2;
    }

    return cost;
}
```

**For 100% branch coverage, test all paths:**

```java
@Test void lightStandard() {
    assertEquals(5.0, calculateShipping(5, false));
}

@Test void lightExpress() {
    assertEquals(10.0, calculateShipping(5, true));
}

@Test void heavyStandard() {
    assertEquals(15.0, calculateShipping(15, false));
}

@Test void heavyExpress() {
    assertEquals(30.0, calculateShipping(15, true));
}
```

## Measuring Coverage with JaCoCo

JaCoCo is the standard Java code coverage tool.

### Maven Setup

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Running Coverage

```bash
mvn clean test
```

View report at: `target/site/jacoco/index.html`

### Reading Reports

| Color | Meaning |
|-------|---------|
| Green | Fully covered |
| Yellow | Partially covered |
| Red | Not covered |

## Coverage Targets

| Coverage Type | Minimum | Good | Excellent |
|---------------|---------|------|-----------|
| Line | 70% | 80% | 90%+ |
| Branch | 60% | 75% | 85%+ |

### Priority by Code Type

| Code Type | Target | Reason |
|-----------|--------|--------|
| Business logic | 90%+ | Critical functionality |
| Service layer | 80%+ | Core application code |
| Utilities | 75%+ | Commonly used |
| DTOs/Models | 60%+ | Simple, less critical |

## Coverage Best Practices

### 1. Focus on Branch Coverage

```java
// Ensure all branches are tested
if (condition) {
    // Test this path
} else {
    // AND this path
}
```

### 2. Don't Chase 100%

Some code is hard to test:
- UI code
- External integrations
- Generated code

Aim for 80-90% on business logic.

### 3. Quality Over Quantity

```java
// Bad - meaningless test for coverage
@Test
void testGetter() {
    User user = new User();
    user.setName("test");
    assertEquals("test", user.getName());  // Testing Java, not your code
}

// Good - meaningful business test
@Test
void testDiscountCalculation() {
    Order order = new Order(customer, items);
    assertEquals(15.0, order.calculateDiscount());
}
```

### 4. Use Coverage as a Guide

Coverage helps find:
- Untested code paths
- Dead code
- Missing edge cases

Don't write tests just to increase the number.

## Summary

| Concept | Key Point |
|---------|-----------|
| Line coverage | % of lines executed |
| Branch coverage | % of decision paths tested |
| JaCoCo | Standard Java coverage tool |
| Target | 80% line, 75% branch |
| Focus | Quality tests over high numbers |

## Next Topic

Continue to [Test Doubles](./05-test-doubles.md) to learn about stubs and fakes for isolating tests.
