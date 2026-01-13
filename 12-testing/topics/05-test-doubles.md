# Test Doubles

## Why Test Doubles?

When testing a class, you often need to isolate it from its dependencies:

```
Without Test Doubles:          With Test Doubles:
┌─────────────┐               ┌─────────────┐
│ UserService │               │ UserService │
└──────┬──────┘               └──────┬──────┘
       │                             │
       ↓                             ↓
┌─────────────┐               ┌─────────────┐
│  Database   │               │    Stub     │
└─────────────┘               └─────────────┘
  Slow, complex                Fast, simple
```

**Benefits:**
- Tests run faster
- No external dependencies
- Predictable results
- Test edge cases easily

## Types of Test Doubles

| Type | Purpose | Has Logic |
|------|---------|-----------|
| **Dummy** | Fill parameters | No |
| **Stub** | Return predefined data | Minimal |
| **Fake** | Working simplified implementation | Yes |
| **Mock** | Verify interactions | No |
| **Spy** | Wrap real object | Yes (real) |

## Dummy Objects

Objects passed but never used:

```java
@Test
void testSomething() {
    Logger dummyLogger = null;  // Never actually called
    Calculator calc = new Calculator(dummyLogger);

    assertEquals(8, calc.add(5, 3));
}
```

## Stubs

Return predefined responses without real logic:

### Interface

```java
public interface UserRepository {
    User findById(Long id);
    User save(User user);
}
```

### Stub Implementation

```java
public class UserRepositoryStub implements UserRepository {

    @Override
    public User findById(Long id) {
        // Always returns the same user
        return new User(1L, "john", "john@example.com");
    }

    @Override
    public User save(User user) {
        user.setId(1L);
        return user;
    }
}
```

### Using the Stub

```java
class UserServiceTest {

    @Test
    void shouldFindUser() {
        // Arrange - use stub instead of real database
        UserRepository stub = new UserRepositoryStub();
        UserService service = new UserService(stub);

        // Act
        User user = service.findById(1L);

        // Assert
        assertNotNull(user);
        assertEquals("john", user.getUsername());
    }
}
```

## Configurable Stubs

Stubs that can be configured for different scenarios:

```java
public class PaymentGatewayStub implements PaymentGateway {

    private boolean shouldSucceed = true;

    public void setShouldSucceed(boolean value) {
        this.shouldSucceed = value;
    }

    @Override
    public PaymentResult process(double amount) {
        if (shouldSucceed) {
            return new PaymentResult(true, "TXN123");
        } else {
            return new PaymentResult(false, null);
        }
    }
}
```

### Usage

```java
@Test
void shouldHandlePaymentSuccess() {
    PaymentGatewayStub stub = new PaymentGatewayStub();
    stub.setShouldSucceed(true);

    OrderService service = new OrderService(stub);
    Order result = service.placeOrder(order);

    assertEquals("CONFIRMED", result.getStatus());
}

@Test
void shouldHandlePaymentFailure() {
    PaymentGatewayStub stub = new PaymentGatewayStub();
    stub.setShouldSucceed(false);

    OrderService service = new OrderService(stub);

    assertThrows(PaymentException.class, () -> {
        service.placeOrder(order);
    });
}
```

## Fakes

Working implementations with shortcuts (e.g., in-memory instead of database):

```java
public class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> storage = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public User findById(Long id) {
        return storage.get(id);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}
```

**Fake vs Stub:**
- Stub: Returns fixed values
- Fake: Actually works, just simpler than real thing

## When to Use Each

| Scenario | Use |
|----------|-----|
| Parameter not used | Dummy |
| Need specific return value | Stub |
| Need working data layer | Fake |
| Verify method was called | Mock |
| Test real code + verify | Spy |

## Example: Testing with Stubs

### Service Under Test

```java
public class OrderService {

    private ProductRepository productRepo;
    private PaymentGateway paymentGateway;

    public OrderService(ProductRepository productRepo, PaymentGateway paymentGateway) {
        this.productRepo = productRepo;
        this.paymentGateway = paymentGateway;
    }

    public Order createOrder(Long productId, int quantity) {
        Product product = productRepo.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product not found");
        }

        double total = product.getPrice() * quantity;
        PaymentResult payment = paymentGateway.process(total);

        if (!payment.isSuccess()) {
            throw new PaymentException("Payment failed");
        }

        return new Order(product, quantity, payment.getTransactionId());
    }
}
```

### Tests with Stubs

```java
class OrderServiceTest {

    private ProductRepositoryStub productRepo;
    private PaymentGatewayStub paymentGateway;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productRepo = new ProductRepositoryStub();
        paymentGateway = new PaymentGatewayStub();
        orderService = new OrderService(productRepo, paymentGateway);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        productRepo.setProduct(new Product(1L, "Laptop", 999.99));
        paymentGateway.setShouldSucceed(true);

        // Act
        Order order = orderService.createOrder(1L, 2);

        // Assert
        assertNotNull(order);
        assertEquals(1999.98, order.getTotal(), 0.01);
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        // Arrange
        productRepo.setProduct(null);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            orderService.createOrder(1L, 1);
        });
    }

    @Test
    void shouldThrowWhenPaymentFails() {
        // Arrange
        productRepo.setProduct(new Product(1L, "Laptop", 999.99));
        paymentGateway.setShouldSucceed(false);

        // Act & Assert
        assertThrows(PaymentException.class, () -> {
            orderService.createOrder(1L, 1);
        });
    }
}
```

## Limitations of Manual Test Doubles

- Lots of code to write and maintain
- Hard to verify method calls
- No flexible argument matching

**Solution**: Use a mocking framework like **Mockito**.

## Summary

| Concept | Key Point |
|---------|-----------|
| Test doubles | Stand-ins for real dependencies |
| Dummy | Fills parameters, never used |
| Stub | Returns predefined values |
| Fake | Working simplified implementation |
| Purpose | Isolate code, fast tests, test edge cases |

## Next Topic

Continue to [Mockito](./06-mockito.md) to learn about the mocking framework that simplifies test doubles.
