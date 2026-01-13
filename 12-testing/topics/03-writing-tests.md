# Writing Tests

## The AAA Pattern

Every good test follows the **Arrange-Act-Assert** pattern:

```
┌──────────────────────────────────┐
│  ARRANGE                         │
│  Set up test data                │
├──────────────────────────────────┤
│  ACT                             │
│  Execute the code under test     │
├──────────────────────────────────┤
│  ASSERT                          │
│  Verify the result               │
└──────────────────────────────────┘
```

### Example

```java
@Test
void shouldCalculateOrderTotal() {
    // ARRANGE - Set up test data
    ShoppingCart cart = new ShoppingCart();
    cart.addItem(new Item("Book", 25.00));
    cart.addItem(new Item("Pen", 5.00));

    // ACT - Execute the method being tested
    double total = cart.calculateTotal();

    // ASSERT - Verify the result
    assertEquals(30.00, total, 0.01);
}
```

## Basic Assertions

Import assertions:
```java
import static org.junit.jupiter.api.Assertions.*;
```

### assertEquals

Compare expected and actual values:

```java
@Test
void testEquals() {
    assertEquals(8, calculator.add(5, 3));
    assertEquals("hello", result.toLowerCase());
    assertEquals(10.5, price, 0.01);  // For decimals, use delta
}
```

### assertTrue / assertFalse

Verify boolean conditions:

```java
@Test
void testBoolean() {
    assertTrue(user.isActive());
    assertFalse(list.isEmpty());
    assertTrue(age >= 18, "User must be adult");
}
```

### assertNull / assertNotNull

Check for null values:

```java
@Test
void testNull() {
    assertNull(service.findById(999));     // Not found
    assertNotNull(service.findById(1));    // Found
}
```

### assertThrows

Verify exceptions are thrown:

```java
@Test
void testException() {
    assertThrows(IllegalArgumentException.class, () -> {
        calculator.divide(10, 0);
    });
}

@Test
void testExceptionMessage() {
    Exception ex = assertThrows(IllegalArgumentException.class, () -> {
        service.createUser(null);
    });
    assertEquals("Username cannot be null", ex.getMessage());
}
```

### assertArrayEquals

Compare arrays:

```java
@Test
void testArray() {
    int[] expected = {1, 2, 3};
    int[] actual = {1, 2, 3};
    assertArrayEquals(expected, actual);
}
```

## Assertion Summary

| Assertion | Purpose | Example |
|-----------|---------|---------|
| `assertEquals(exp, act)` | Values equal | `assertEquals(8, result)` |
| `assertNotEquals(exp, act)` | Values not equal | `assertNotEquals(0, count)` |
| `assertTrue(cond)` | Condition is true | `assertTrue(valid)` |
| `assertFalse(cond)` | Condition is false | `assertFalse(empty)` |
| `assertNull(obj)` | Object is null | `assertNull(result)` |
| `assertNotNull(obj)` | Object not null | `assertNotNull(user)` |
| `assertThrows(type, code)` | Throws exception | `assertThrows(NPE.class, ...)` |
| `assertArrayEquals(exp, act)` | Arrays equal | `assertArrayEquals(arr1, arr2)` |

## Adding Messages

Always add descriptive messages for clearer failures:

```java
@Test
void testWithMessages() {
    assertEquals(100, total, "Total should be 100 for premium customer");
    assertTrue(user.isValid(), "User with email should be valid");
    assertNotNull(order.getId(), "Saved order should have an ID");
}
```

## Grouped Assertions with assertAll

Run multiple assertions and see all failures:

```java
@Test
void testUserProperties() {
    User user = new User("john", "john@example.com", 25);

    assertAll("User properties",
        () -> assertEquals("john", user.getUsername()),
        () -> assertEquals("john@example.com", user.getEmail()),
        () -> assertEquals(25, user.getAge())
    );
}
```

**Why use assertAll?**
- Without it: Test stops at first failure
- With it: All assertions run, see all failures at once

## Testing Different Scenarios

### Testing Return Values

```java
@Test
void shouldReturnDiscountForPremiumCustomer() {
    // Arrange
    Customer customer = new Customer("premium");
    DiscountService service = new DiscountService();

    // Act
    double discount = service.getDiscount(customer);

    // Assert
    assertEquals(0.20, discount, 0.01);
}
```

### Testing State Changes

```java
@Test
void shouldAddItemToCart() {
    // Arrange
    ShoppingCart cart = new ShoppingCart();
    Item item = new Item("Book", 29.99);

    // Act
    cart.addItem(item);

    // Assert
    assertEquals(1, cart.getItemCount());
    assertFalse(cart.isEmpty());
}
```

### Testing Exceptions

```java
@Test
void shouldThrowForInvalidInput() {
    // Arrange
    UserService service = new UserService();

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> {
        service.createUser(null, "test@email.com");
    });
}
```

### Testing Collections

```java
@Test
void shouldReturnFilteredProducts() {
    // Arrange
    ProductService service = new ProductService();

    // Act
    List<Product> cheap = service.findByMaxPrice(50.0);

    // Assert
    assertNotNull(cheap);
    assertEquals(3, cheap.size());
    assertTrue(cheap.stream().allMatch(p -> p.getPrice() <= 50.0));
}
```

## Complete Example

```java
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Should create user with valid data")
    void shouldCreateUser() {
        // Arrange
        String username = "john";
        String email = "john@example.com";

        // Act
        User user = userService.createUser(username, email);

        // Assert
        assertAll("Created user",
            () -> assertNotNull(user.getId()),
            () -> assertEquals(username, user.getUsername()),
            () -> assertEquals(email, user.getEmail()),
            () -> assertTrue(user.isActive())
        );
    }

    @Test
    @DisplayName("Should throw exception for null username")
    void shouldRejectNullUsername() {
        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null, "test@email.com");
        });

        assertTrue(ex.getMessage().contains("username"));
    }

    @Test
    @DisplayName("Should find existing user by ID")
    void shouldFindUserById() {
        // Arrange
        User created = userService.createUser("jane", "jane@example.com");

        // Act
        User found = userService.findById(created.getId());

        // Assert
        assertNotNull(found);
        assertEquals("jane", found.getUsername());
    }

    @Test
    @DisplayName("Should return null for non-existent user")
    void shouldReturnNullForMissingUser() {
        // Act
        User result = userService.findById(99999L);

        // Assert
        assertNull(result);
    }
}
```

## Best Practices

### 1. One Concept Per Test

```java
// Good - focused test
@Test
void shouldAddItem() {
    cart.addItem(item);
    assertEquals(1, cart.getItemCount());
}

// Bad - testing multiple things
@Test
void testCartOperations() {
    cart.addItem(item);
    assertEquals(1, cart.getItemCount());
    cart.removeItem(item);
    assertEquals(0, cart.getItemCount());
    cart.clear();
    assertTrue(cart.isEmpty());
}
```

### 2. Use Descriptive Names

```java
// Good
void shouldCalculateTaxForCaliforniaOrders()
void shouldThrowExceptionWhenEmailIsInvalid()

// Bad
void test1()
void testCalculate()
```

### 3. Keep Tests Independent

Each test should:
- Create its own data
- Not depend on other tests
- Clean up after itself (if needed)

### 4. Test Edge Cases

Always test:
- Null inputs
- Empty collections
- Boundary values (0, -1, max)
- Invalid data

```java
@Test void shouldHandleEmptyList() { ... }
@Test void shouldHandleNullInput() { ... }
@Test void shouldHandleMaxValue() { ... }
```

## Summary

| Concept | Key Point |
|---------|-----------|
| AAA Pattern | Arrange → Act → Assert |
| assertEquals | Compare expected and actual |
| assertTrue/False | Verify boolean conditions |
| assertThrows | Verify exceptions |
| assertAll | Group related assertions |
| Messages | Add descriptive failure messages |

## Next Topic

Continue to [Test Coverage](./04-test-coverage.md) to learn about measuring test effectiveness.
