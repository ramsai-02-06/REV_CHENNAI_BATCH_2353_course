# Mock vs Stub vs Spy

## Quick Comparison

| Type | Purpose | Verifiable | Real Logic |
|------|---------|------------|------------|
| **Stub** | Return predefined data | No | No |
| **Mock** | Verify interactions | Yes | No |
| **Spy** | Partial mock of real object | Yes | Yes |

## Stub

**Purpose**: Provide canned responses for queries.

```java
@Mock
private UserRepository repository;

@Test
void testWithStub() {
    // Stub - just return data
    when(repository.findById(1L)).thenReturn(new User("john"));

    User result = service.getUser(1L);

    // Assert on returned data (state verification)
    assertEquals("john", result.getUsername());
    // No verify() needed
}
```

**Use when**: Testing code that **reads** data.

## Mock

**Purpose**: Verify that methods were called correctly.

```java
@Mock
private EmailService emailService;

@Test
void testWithMock() {
    // Act
    service.registerUser(new User("john", "john@example.com"));

    // Mock - verify interaction (behavior verification)
    verify(emailService).sendWelcomeEmail("john@example.com");
    verify(emailService, times(1)).sendWelcomeEmail(anyString());
}
```

**Use when**: Testing code that **writes** or has side effects.

## Spy

**Purpose**: Wrap real object, stub specific methods.

```java
@Test
void testWithSpy() {
    List<String> realList = new ArrayList<>();
    List<String> spyList = spy(realList);

    // Real method works
    spyList.add("one");
    assertEquals(1, spyList.size());  // Real size

    // Stub one method
    doReturn(100).when(spyList).size();
    assertEquals(100, spyList.size());  // Stubbed size
}
```

**Use when**: Need mostly real behavior with some stubbing.

## Decision Guide

```
What do you need?
│
├─ Return specific data?
│  └─> Use as STUB: when().thenReturn()
│
├─ Verify method calls?
│  └─> Use as MOCK: verify()
│
└─ Real object with some stubs?
   └─> Use SPY: spy(realObject)
```

## Example: Using Both Stub and Mock

```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepo;  // Stub for data

    @Mock
    private EmailService emailService;      // Mock for verification

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderAndNotify() {
        // STUB - return data
        when(productRepo.findById(1L))
            .thenReturn(new Product("Laptop", 999.99));

        // Act
        Order order = orderService.createOrder(1L, "customer@example.com");

        // Assert state (stub result)
        assertEquals(999.99, order.getTotal(), 0.01);

        // MOCK - verify behavior
        verify(emailService).sendOrderConfirmation("customer@example.com");
    }
}
```

## Common Mistakes

### 1. Verifying Stubs

```java
// Unnecessary - stub verification
when(repo.findById(1L)).thenReturn(user);
service.getUser(1L);
verify(repo).findById(1L);  // Why? Just assert the result!

// Better - assert result
when(repo.findById(1L)).thenReturn(user);
User result = service.getUser(1L);
assertEquals("john", result.getUsername());
```

### 2. Using Spy When Mock Works

```java
// Overcomplicated
UserService spy = spy(new UserService(mockRepo));

// Simpler - just mock dependencies
@Mock UserRepository mockRepo;
@InjectMocks UserService service;
```

### 3. Stubbing Spies Wrong

```java
List<String> spy = spy(new ArrayList<>());

// WRONG - calls real method first!
when(spy.get(0)).thenReturn("foo");  // Exception!

// CORRECT - use doReturn
doReturn("foo").when(spy).get(0);
```

## Summary Table

| Aspect | Stub | Mock | Spy |
|--------|------|------|-----|
| **Creation** | `@Mock` + `when()` | `@Mock` + `verify()` | `spy(real)` |
| **Returns** | Predefined values | Default values | Real values |
| **Verifies** | No (not intended) | Yes | Yes |
| **Real logic** | No | No | Yes |
| **Use for** | Queries | Commands | Partial mocking |

## Quick Reference

```java
// Stub pattern
when(repo.find(id)).thenReturn(data);
Result result = service.get(id);
assertEquals(expected, result);

// Mock pattern
service.doAction(data);
verify(mock).expectedMethod(args);

// Spy pattern
RealClass spy = spy(new RealClass());
doReturn(value).when(spy).specificMethod();
// Other methods work normally
```

## Next Topic

Continue to [Test Driven Development](./08-test-driven-development.md) to learn about writing tests first.
