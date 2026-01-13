# Mockito

## What is Mockito?

Mockito is a mocking framework that simplifies creating test doubles. Instead of writing stub classes manually, Mockito creates them automatically.

## Setup

### Maven

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>
```

### Gradle

```gradle
testImplementation 'org.mockito:mockito-junit-jupiter:5.5.0'
```

## Creating Mocks

### Using Annotations (Recommended)

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSomething() {
        // mocks are ready to use
    }
}
```

**Key annotations:**
- `@ExtendWith(MockitoExtension.class)` - Enables Mockito
- `@Mock` - Creates a mock object
- `@InjectMocks` - Injects mocks into the class under test

## Stubbing with when().thenReturn()

Define what a mock should return:

```java
@Test
void shouldFindUser() {
    // Arrange - define mock behavior
    User mockUser = new User(1L, "john");
    when(userRepository.findById(1L)).thenReturn(mockUser);

    // Act
    User result = userService.getUser(1L);

    // Assert
    assertEquals("john", result.getUsername());
}
```

### Multiple Return Values

```java
@Test
void shouldReturnDifferentUsers() {
    when(userRepository.findById(1L)).thenReturn(new User(1L, "john"));
    when(userRepository.findById(2L)).thenReturn(new User(2L, "jane"));

    assertEquals("john", userService.getUser(1L).getUsername());
    assertEquals("jane", userService.getUser(2L).getUsername());
}
```

## Argument Matchers

Match any argument instead of specific values:

```java
import static org.mockito.ArgumentMatchers.*;

@Test
void shouldFindAnyUser() {
    User user = new User(1L, "test");

    // Match any Long value
    when(userRepository.findById(anyLong())).thenReturn(user);

    assertNotNull(userService.getUser(1L));
    assertNotNull(userService.getUser(999L));
}
```

### Common Matchers

| Matcher | Description |
|---------|-------------|
| `any()` | Any object |
| `anyInt()` | Any int |
| `anyLong()` | Any long |
| `anyString()` | Any string |
| `eq(value)` | Specific value |
| `isNull()` | Null value |

## Stubbing Exceptions

```java
@Test
void shouldThrowException() {
    when(userRepository.findById(999L))
        .thenThrow(new UserNotFoundException("Not found"));

    assertThrows(UserNotFoundException.class, () -> {
        userService.getUser(999L);
    });
}
```

## Verifying Method Calls

Check that methods were called:

```java
@Test
void shouldCallRepository() {
    when(userRepository.findById(1L)).thenReturn(new User(1L, "john"));

    userService.getUser(1L);

    // Verify findById was called with argument 1L
    verify(userRepository).findById(1L);
}
```

### Verification Modes

```java
// Verify called exactly once (default)
verify(mock).method();
verify(mock, times(1)).method();

// Verify called multiple times
verify(mock, times(3)).method();

// Verify never called
verify(mock, never()).method();

// Verify called at least once
verify(mock, atLeastOnce()).method();
```

## Complete Example

### Service Class

```java
public class OrderService {

    private OrderRepository orderRepository;
    private EmailService emailService;

    public OrderService(OrderRepository orderRepo, EmailService emailService) {
        this.orderRepository = orderRepo;
        this.emailService = emailService;
    }

    public Order createOrder(String customerId, double amount) {
        Order order = new Order(customerId, amount);
        Order saved = orderRepository.save(order);
        emailService.sendConfirmation(customerId, saved.getId());
        return saved;
    }
}
```

### Test Class

```java
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderAndSendEmail() {
        // Arrange
        Order savedOrder = new Order("CUST1", 100.0);
        savedOrder.setId("ORD123");

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = orderService.createOrder("CUST1", 100.0);

        // Assert - verify result
        assertEquals("ORD123", result.getId());

        // Assert - verify interactions
        verify(orderRepository).save(any(Order.class));
        verify(emailService).sendConfirmation("CUST1", "ORD123");
    }

    @Test
    void shouldNotSendEmailWhenSaveFails() {
        // Arrange
        when(orderRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder("CUST1", 100.0);
        });

        // Verify email was never sent
        verify(emailService, never()).sendConfirmation(anyString(), anyString());
    }
}
```

## Argument Captors

Capture arguments passed to mocks for inspection:

```java
@Test
void shouldCaptureOrderDetails() {
    when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    orderService.createOrder("CUST1", 150.0);

    // Capture the Order passed to save()
    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(captor.capture());

    // Inspect captured order
    Order captured = captor.getValue();
    assertEquals("CUST1", captured.getCustomerId());
    assertEquals(150.0, captured.getAmount(), 0.01);
}
```

## Spies

Wrap real objects to partially mock:

```java
@Test
void testSpy() {
    List<String> realList = new ArrayList<>();
    List<String> spyList = spy(realList);

    // Real method called
    spyList.add("one");
    assertEquals(1, spyList.size());

    // Stub specific method
    when(spyList.size()).thenReturn(100);
    assertEquals(100, spyList.size());
}
```

## Best Practices

### 1. Only Mock Dependencies

```java
// Good - mock dependencies
@Mock private UserRepository repository;

// Bad - don't mock value objects
@Mock private User user;  // Just create: new User("john")
```

### 2. Use @InjectMocks

```java
// Good - automatic injection
@InjectMocks
private UserService userService;

// Avoid - manual setup
@BeforeEach
void setUp() {
    userService = new UserService(mockRepo);  // Unnecessary
}
```

### 3. Verify Important Interactions

```java
@Test
void testOrderCreation() {
    // ... create order ...

    // Verify critical operations
    verify(paymentService).charge(anyDouble());
    verify(inventoryService).reserve(anyLong());

    // Don't verify everything
}
```

### 4. Use Specific Matchers When Possible

```java
// Better - specific values
verify(repo).findById(1L);

// Less precise - any value
verify(repo).findById(anyLong());
```

## Common Patterns

### Void Methods

```java
// Stub void method to do nothing (default)
doNothing().when(emailService).send(anyString());

// Stub void method to throw exception
doThrow(new RuntimeException()).when(emailService).send("invalid");
```

### Consecutive Calls

```java
when(repository.findById(1L))
    .thenReturn(new User("first"))
    .thenReturn(new User("second"));

assertEquals("first", service.getUser(1L).getUsername());
assertEquals("second", service.getUser(1L).getUsername());
```

## Summary

| Concept | Key Point |
|---------|-----------|
| `@Mock` | Create mock object |
| `@InjectMocks` | Inject mocks into class |
| `when().thenReturn()` | Define mock behavior |
| `verify()` | Check method was called |
| Argument matchers | Match any/specific arguments |
| ArgumentCaptor | Capture arguments for inspection |

## Quick Reference

```java
// Setup
@ExtendWith(MockitoExtension.class)

// Create mocks
@Mock private Repository repo;
@InjectMocks private Service service;

// Stubbing
when(repo.find(1L)).thenReturn(object);
when(repo.find(anyLong())).thenReturn(object);
when(repo.find(999L)).thenThrow(new Exception());

// Verification
verify(repo).save(any());
verify(repo, times(2)).find(anyLong());
verify(repo, never()).delete(anyLong());
```

## Next Topic

Continue to [Mock vs Stub vs Spy](./07-mock-stub-spy.md) for a comparison of test double types.
