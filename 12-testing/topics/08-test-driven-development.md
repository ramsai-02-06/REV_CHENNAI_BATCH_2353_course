# Test Driven Development (TDD)

## What is TDD?

Test Driven Development is a practice where you write tests **before** writing the actual code.

```
Traditional:     Code → Test → Fix
TDD:             Test → Code → Refactor
```

## The Red-Green-Refactor Cycle

```
    ┌─────────────────────────────────────┐
    │                                     │
    ▼                                     │
┌───────┐      ┌───────┐      ┌──────────┐│
│  RED  │ ───► │ GREEN │ ───► │ REFACTOR ││
└───────┘      └───────┘      └──────────┘│
  Write          Write          Improve   │
  failing        minimal        code      │
  test           code           quality   │
                                     │    │
                                     └────┘
```

### Step 1: RED - Write a Failing Test

Write a test for functionality that doesn't exist yet.

```java
@Test
void shouldAddNumbers() {
    Calculator calc = new Calculator();
    assertEquals(8, calc.add(5, 3));  // Fails - add() doesn't exist!
}
```

### Step 2: GREEN - Make It Pass

Write the minimum code to make the test pass.

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;  // Simplest implementation
    }
}
```

### Step 3: REFACTOR - Improve

Clean up the code while keeping tests green.

## TDD Example: Email Validator

### Iteration 1: Basic Validation

**RED** - Write failing test:

```java
@Test
void shouldRejectNullEmail() {
    EmailValidator validator = new EmailValidator();
    assertFalse(validator.isValid(null));
}
```

**GREEN** - Make it pass:

```java
public class EmailValidator {
    public boolean isValid(String email) {
        return email != null;
    }
}
```

### Iteration 2: Check @ Symbol

**RED** - Add another failing test:

```java
@Test
void shouldRejectEmailWithoutAtSymbol() {
    EmailValidator validator = new EmailValidator();
    assertFalse(validator.isValid("invalidemail"));
}
```

**GREEN** - Expand implementation:

```java
public boolean isValid(String email) {
    return email != null && email.contains("@");
}
```

### Iteration 3: Valid Email

**RED**:

```java
@Test
void shouldAcceptValidEmail() {
    EmailValidator validator = new EmailValidator();
    assertTrue(validator.isValid("user@example.com"));
}
```

**GREEN**: Already passes! Move to next test or refactor.

### Complete Test Class

```java
class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
    }

    @Test
    void shouldRejectNull() {
        assertFalse(validator.isValid(null));
    }

    @Test
    void shouldRejectEmpty() {
        assertFalse(validator.isValid(""));
    }

    @Test
    void shouldRejectWithoutAtSymbol() {
        assertFalse(validator.isValid("invalidemail"));
    }

    @Test
    void shouldRejectWithoutDomain() {
        assertFalse(validator.isValid("user@"));
    }

    @Test
    void shouldAcceptValidEmail() {
        assertTrue(validator.isValid("user@example.com"));
    }
}
```

### Final Implementation

```java
public class EmailValidator {

    public boolean isValid(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        int atIndex = email.indexOf("@");
        if (atIndex < 1) {
            return false;
        }

        String domain = email.substring(atIndex + 1);
        return domain.contains(".");
    }
}
```

## TDD Benefits

| Benefit | Description |
|---------|-------------|
| **Design** | Forces you to think about API before implementation |
| **Documentation** | Tests describe expected behavior |
| **Confidence** | Change code knowing tests catch regressions |
| **Focus** | Write only what's needed |

## When to Use TDD

**Good for:**
- New features with clear requirements
- Bug fixes (write test to reproduce bug first)
- API design

**Less suitable for:**
- Exploratory coding
- UI development
- Prototypes

## Common Mistakes

### 1. Writing Too Much Test at Once

```java
// Bad - too complex for first test
@Test
void testCompleteRegistration() {
    // Tests validation, database, email, logging...
}

// Good - start simple
@Test
void shouldRejectEmptyUsername() {
    assertFalse(validator.isValidUsername(""));
}
```

### 2. Making Test Pass with Shortcuts

```java
// Bad - hardcoded to pass
public int add(int a, int b) {
    return 8;  // Works for add(5, 3) but nothing else!
}

// Good - general solution
public int add(int a, int b) {
    return a + b;
}
```

### 3. Skipping Refactor Step

Always clean up after getting green:
- Remove duplication
- Improve names
- Simplify logic

## TDD Workflow Summary

```
1. Think: What's the next small behavior?
2. RED: Write a test for it (should fail)
3. GREEN: Write minimal code to pass
4. REFACTOR: Clean up code
5. Repeat
```

## Quick Reference

| Phase | Action | Test Status |
|-------|--------|-------------|
| RED | Write test | Failing |
| GREEN | Write code | Passing |
| REFACTOR | Improve | Still passing |

```java
// TDD Cycle Pattern
@Test void step1_writeFailingTest() { }   // RED
// implement minimal code                  // GREEN
// clean up code                           // REFACTOR
// repeat with next test
```

## Summary

| Concept | Key Point |
|---------|-----------|
| TDD | Write tests before code |
| Red-Green-Refactor | Fail → Pass → Improve |
| Small steps | One behavior at a time |
| Benefits | Better design, confidence, documentation |

## Module Complete

You have completed the Testing module. Key concepts covered:

1. Introduction to Testing - Why test, testing pyramid
2. JUnit Fundamentals - Setup, annotations, lifecycle
3. Writing Tests - AAA pattern, assertions
4. Test Coverage - Line vs branch coverage
5. Test Doubles - Stubs, fakes, dummies
6. Mockito - Mocking framework
7. Mock vs Stub vs Spy - Comparison
8. Test Driven Development - TDD methodology
