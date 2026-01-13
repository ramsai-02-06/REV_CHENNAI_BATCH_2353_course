# Introduction to Software Testing

## What is Software Testing?

Software testing is the process of evaluating an application to find defects and verify it meets requirements. Testing ensures software works correctly before it reaches users.

## Why Testing Matters

| Benefit | Description |
|---------|-------------|
| **Bug Detection** | Find issues before production |
| **Quality Assurance** | Ensure software meets requirements |
| **Confidence** | Safe to make changes and refactor |
| **Documentation** | Tests show how code should work |
| **Cost Savings** | Fixing bugs early is cheaper |

## The Testing Pyramid

The testing pyramid shows the ideal distribution of test types:

```
        /\
       /  \
      / UI \         ← Few (slow, expensive)
     /──────\
    /        \
   /Integration\     ← Some (moderate speed)
  /──────────────\
 /                \
/    Unit Tests    \ ← Many (fast, cheap)
────────────────────
```

**Key insight**: Most tests should be unit tests - they're fast, reliable, and catch issues early.

## Types of Tests

### 1. Unit Tests

Test individual methods or classes in isolation.

```java
@Test
void testAddition() {
    Calculator calc = new Calculator();
    assertEquals(8, calc.add(5, 3));
}
```

**Characteristics:**
- Fast (milliseconds)
- Test one thing
- No external dependencies
- Run frequently

### 2. Integration Tests

Test how multiple components work together.

```java
@Test
void testUserServiceWithDatabase() {
    UserService service = new UserService(realDatabase);
    User user = service.createUser("john", "john@example.com");

    User retrieved = service.findById(user.getId());
    assertEquals("john", retrieved.getUsername());
}
```

**Characteristics:**
- Slower (seconds)
- Test component interactions
- May use real databases/services
- Run less frequently

### 3. End-to-End (E2E) Tests

Test the complete application flow from user perspective.

**Characteristics:**
- Slowest (minutes)
- Test full user workflows
- Simulate real user actions
- Run before releases

## Comparison Table

| Aspect | Unit Tests | Integration Tests | E2E Tests |
|--------|------------|-------------------|-----------|
| **Speed** | Very fast | Moderate | Slow |
| **Scope** | Single unit | Multiple components | Full system |
| **Dependencies** | Mocked | Real or partial | Real |
| **Quantity** | Many | Some | Few |
| **Run frequency** | Every change | Daily/PR | Before release |

## What Makes a Good Test?

A good test is:

1. **Fast** - Runs in milliseconds
2. **Independent** - Doesn't depend on other tests
3. **Repeatable** - Same result every time
4. **Self-validating** - Clearly passes or fails
5. **Timely** - Written close to the code

## Unit Testing Focus

This module focuses on **unit testing** because:

- Foundation of all testing
- Catches most bugs early
- Provides fast feedback
- Enables confident refactoring
- Required skill for developers

## Tools We'll Use

| Tool | Purpose |
|------|---------|
| **JUnit 5** | Testing framework |
| **Assertions** | Verify expected outcomes |
| **Mockito** | Create mock objects |

## Summary

| Concept | Key Point |
|---------|-----------|
| Testing purpose | Find bugs, ensure quality |
| Testing pyramid | More unit tests, fewer E2E tests |
| Unit tests | Fast, isolated, frequent |
| Good tests | Fast, independent, repeatable |

## Next Topic

Continue to [JUnit Fundamentals](./02-junit-fundamentals.md) to learn the JUnit 5 testing framework.
