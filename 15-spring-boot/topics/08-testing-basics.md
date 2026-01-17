# Testing Basics in Spring Boot

## Introduction

Spring Boot provides excellent testing support out of the box. Testing is a crucial skill for developers, and Spring's testing framework makes it easy to write both unit tests and integration tests. This guide introduces the fundamentals of testing Spring Boot applications.

### Why Test Spring Applications?

1. **Verify Business Logic**: Ensure your code does what it's supposed to do
2. **Catch Bugs Early**: Find issues before they reach production
3. **Enable Refactoring**: Change code confidently with tests as a safety net
4. **Documentation**: Tests show how code is intended to be used
5. **Design Feedback**: Hard-to-test code often indicates design problems

### Types of Tests

```
┌─────────────────────────────────────────────────────────┐
│                    Testing Pyramid                       │
├─────────────────────────────────────────────────────────┤
│                                                          │
│                    /\                                    │
│                   /  \     E2E Tests                     │
│                  /    \    (Few, Slow, Expensive)        │
│                 /──────\                                 │
│                /        \   Integration Tests            │
│               /          \  (Some, Medium Speed)         │
│              /────────────\                              │
│             /              \  Unit Tests                 │
│            /________________\ (Many, Fast, Cheap)        │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

| Type | Scope | Speed | Dependencies |
|------|-------|-------|--------------|
| Unit Tests | Single class/method | Very fast | Mocked |
| Integration Tests | Multiple components | Medium | Real or embedded |
| End-to-End Tests | Full application | Slow | Real |

---

## Setting Up Testing

### Dependencies

Spring Boot Starter Test includes everything you need:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Included Libraries:**
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework
- **AssertJ**: Fluent assertions
- **Hamcrest**: Matcher library
- **Spring Test**: Spring testing utilities
- **JSONassert**: JSON assertions
- **JsonPath**: JSON path expressions

### Test Directory Structure

```
src/
├── main/
│   └── java/
│       └── com/example/
│           ├── controller/
│           │   └── UserController.java
│           ├── service/
│           │   └── UserService.java
│           └── repository/
│               └── UserRepository.java
└── test/
    └── java/
        └── com/example/
            ├── controller/
            │   └── UserControllerTest.java
            ├── service/
            │   └── UserServiceTest.java
            └── repository/
                └── UserRepositoryTest.java
```

---

## Unit Testing

Unit tests verify individual components in isolation, with dependencies mocked.

### Testing a Service with Mockito

**Service to Test:**
```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
        return userRepository.save(user);
    }
}
```

**Unit Test:**
```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Enable Mockito
class UserServiceTest {

    @Mock  // Create a mock of the repository
    private UserRepository userRepository;

    @InjectMocks  // Inject mocks into the service
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John Doe", "john@example.com");
    }

    @Test
    void getUserById_WhenUserExists_ReturnsUser() {
        // Arrange - Set up mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act - Call the method
        User result = userService.getUserById(1L);

        // Assert - Verify the result
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");

        // Verify mock was called
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(99L))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User not found: 99");
    }

    @Test
    void createUser_WhenEmailUnique_SavesUser() {
        // Arrange
        User newUser = new User(null, "Jane Doe", "jane@example.com");
        User savedUser = new User(2L, "Jane Doe", "jane@example.com");

        when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(savedUser);

        // Act
        User result = userService.createUser(newUser);

        // Assert
        assertThat(result.getId()).isEqualTo(2L);
        verify(userRepository).existsByEmail("jane@example.com");
        verify(userRepository).save(newUser);
    }

    @Test
    void createUser_WhenEmailExists_ThrowsException() {
        // Arrange
        User newUser = new User(null, "Duplicate", "existing@example.com");
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(newUser))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessage("Email already exists");

        // Verify save was never called
        verify(userRepository, never()).save(any());
    }
}
```

### Key Mockito Annotations and Methods

| Annotation/Method | Purpose |
|-------------------|---------|
| `@Mock` | Creates a mock object |
| `@InjectMocks` | Injects mocks into the test subject |
| `@Spy` | Creates a partial mock (real methods unless stubbed) |
| `when().thenReturn()` | Define mock behavior |
| `verify()` | Verify mock interactions |
| `times(n)` | Verify exact number of calls |
| `never()` | Verify method was never called |
| `any()` | Match any argument |

---

## Integration Testing with @SpringBootTest

Integration tests load the Spring context and test multiple components together.

### Basic Integration Test

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest  // Loads full application context
class ApplicationIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
        assertThat(userService).isNotNull();
        assertThat(userRepository).isNotNull();
    }
}
```

### Testing with a Real Database (H2)

```java
@SpringBootTest
@ActiveProfiles("test")  // Use test profile
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createAndRetrieveUser() {
        // Create user
        User newUser = new User(null, "Integration Test", "test@example.com");
        User saved = userService.createUser(newUser);

        // Retrieve user
        User retrieved = userService.getUserById(saved.getId());

        // Verify
        assertThat(retrieved.getName()).isEqualTo("Integration Test");
        assertThat(retrieved.getEmail()).isEqualTo("test@example.com");
    }
}
```

**application-test.properties:**
```properties
# Use H2 for tests
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## Testing REST Controllers

### Using MockMvc

MockMvc allows testing controllers without starting a full HTTP server.

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)  // Test only the controller layer
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // Mock the service layer
    private UserService userService;

    @Test
    void getUser_ReturnsUser() throws Exception {
        // Arrange
        User user = new User(1L, "John Doe", "john@example.com");
        when(userService.getUserById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void getUser_NotFound_Returns404() throws Exception {
        // Arrange
        when(userService.getUserById(99L))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ValidInput_ReturnsCreated() throws Exception {
        // Arrange
        User newUser = new User(null, "Jane Doe", "jane@example.com");
        User savedUser = new User(1L, "Jane Doe", "jane@example.com");
        when(userService.createUser(any(User.class))).thenReturn(savedUser);

        String requestBody = """
            {
                "name": "Jane Doe",
                "email": "jane@example.com"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void createUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        String requestBody = """
            {
                "name": "Jane Doe",
                "email": "invalid-email"
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }
}
```

### Testing with Full Server (TestRestTemplate)

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getUsers_ReturnsUserList() {
        ResponseEntity<User[]> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/users",
            User[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void createUser_ReturnsCreatedUser() {
        User newUser = new User(null, "Test User", "test@example.com");

        ResponseEntity<User> response = restTemplate.postForEntity(
            "http://localhost:" + port + "/api/users",
            newUser,
            User.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
    }
}
```

---

## Testing Data Access Layer

### Testing with @DataJpaTest

`@DataJpaTest` configures only JPA components, using an embedded database.

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest  // Configures JPA tests with embedded database
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;  // Helper for test data setup

    @Autowired
    private UserRepository userRepository;

    @Test
    void findById_WhenUserExists_ReturnsUser() {
        // Arrange - Use TestEntityManager to set up data
        User user = new User(null, "John Doe", "john@example.com");
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> found = userRepository.findById(user.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void findByEmail_ReturnsUser() {
        // Arrange
        User user = new User(null, "Jane Doe", "jane@example.com");
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> found = userRepository.findByEmail("jane@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void findByEmail_WhenNotExists_ReturnsEmpty() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    void findAllByNameContaining_ReturnsMatchingUsers() {
        // Arrange
        entityManager.persistAndFlush(new User(null, "John Smith", "john@example.com"));
        entityManager.persistAndFlush(new User(null, "Jane Johnson", "jane@example.com"));
        entityManager.persistAndFlush(new User(null, "Bob Williams", "bob@example.com"));

        // Act
        List<User> results = userRepository.findAllByNameContaining("John");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(User::getName)
            .containsExactlyInAnyOrder("John Smith", "Jane Johnson");
    }
}
```

---

## Testing Slices

Spring Boot provides test slices for testing specific layers:

| Annotation | Purpose | Loaded Components |
|------------|---------|-------------------|
| `@WebMvcTest` | Web layer | Controllers, filters, advice |
| `@DataJpaTest` | JPA layer | Repositories, entities, embedded DB |
| `@JsonTest` | JSON serialization | ObjectMapper, JSON components |
| `@RestClientTest` | REST clients | RestTemplate, WebClient |

### @JsonTest Example

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.*;

@JsonTest
class UserJsonTest {

    @Autowired
    private JacksonTester<User> json;

    @Test
    void serializeUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");

        assertThat(json.write(user))
            .hasJsonPathNumberValue("$.id")
            .hasJsonPathStringValue("$.name")
            .extractingJsonPathStringValue("$.email")
            .isEqualTo("john@example.com");
    }

    @Test
    void deserializeUser() throws Exception {
        String content = """
            {
                "id": 1,
                "name": "John Doe",
                "email": "john@example.com"
            }
            """;

        assertThat(json.parse(content))
            .usingRecursiveComparison()
            .isEqualTo(new User(1L, "John Doe", "john@example.com"));
    }
}
```

---

## Test Configuration

### Using @TestConfiguration

Create test-specific beans:

```java
@TestConfiguration
public class TestConfig {

    @Bean
    public EmailService emailService() {
        // Return a mock or test implementation
        return new MockEmailService();
    }
}

@SpringBootTest
@Import(TestConfig.class)  // Import test configuration
class ServiceTest {
    // Uses MockEmailService instead of real one
}
```

### Using @MockBean

Replace beans with mocks in the Spring context:

```java
@SpringBootTest
class NotificationServiceTest {

    @MockBean  // Replaces real bean with mock
    private EmailClient emailClient;

    @Autowired
    private NotificationService notificationService;

    @Test
    void sendNotification_CallsEmailClient() {
        notificationService.notify("test@example.com", "Hello");

        verify(emailClient).send(eq("test@example.com"), anyString());
    }
}
```

---

## AssertJ Assertions

AssertJ provides fluent, readable assertions:

```java
import static org.assertj.core.api.Assertions.*;

// Basic assertions
assertThat(result).isNotNull();
assertThat(result).isEqualTo(expected);
assertThat(value).isTrue();
assertThat(value).isFalse();

// String assertions
assertThat(name).startsWith("John");
assertThat(name).endsWith("Doe");
assertThat(name).contains("ohn");
assertThat(name).matches("John.*");

// Number assertions
assertThat(age).isGreaterThan(18);
assertThat(age).isBetween(18, 65);
assertThat(price).isCloseTo(19.99, within(0.01));

// Collection assertions
assertThat(users).hasSize(3);
assertThat(users).isEmpty();
assertThat(users).isNotEmpty();
assertThat(users).contains(user1, user2);
assertThat(users).extracting(User::getName)
    .containsExactly("Alice", "Bob", "Charlie");

// Exception assertions
assertThatThrownBy(() -> service.findUser(-1L))
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("Invalid ID");

assertThatCode(() -> service.findUser(1L))
    .doesNotThrowAnyException();

// Object assertions
assertThat(actual)
    .usingRecursiveComparison()
    .ignoringFields("id", "createdAt")
    .isEqualTo(expected);
```

---

## Running Tests

### Maven Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run specific test method
mvn test -Dtest=UserServiceTest#getUserById_WhenUserExists_ReturnsUser

# Run with profile
mvn test -Dspring.profiles.active=test

# Skip tests
mvn package -DskipTests
```

### IDE Test Runners

- **IntelliJ**: Right-click on test class/method → Run
- **Eclipse**: Right-click → Run As → JUnit Test
- **VS Code**: Click on the play button next to test methods

---

## Best Practices

### 1. Follow AAA Pattern

```java
@Test
void methodName_StateUnderTest_ExpectedBehavior() {
    // Arrange - Set up test data and mocks

    // Act - Execute the method under test

    // Assert - Verify the results
}
```

### 2. Test One Thing Per Test

```java
// WRONG - Testing multiple things
@Test
void userService_Works() {
    User created = userService.create(user);
    assertThat(created).isNotNull();

    User retrieved = userService.getById(created.getId());
    assertThat(retrieved).isEqualTo(created);

    userService.delete(created.getId());
    assertThatThrownBy(() -> userService.getById(created.getId()));
}

// CORRECT - Separate tests
@Test
void create_ValidUser_ReturnsCreatedUser() { }

@Test
void getById_ExistingUser_ReturnsUser() { }

@Test
void delete_ExistingUser_RemovesUser() { }
```

### 3. Use Descriptive Test Names

```java
// Good names
void getUserById_WhenUserExists_ReturnsUser()
void createUser_WithDuplicateEmail_ThrowsException()
void calculateDiscount_ForPremiumCustomer_Returns20Percent()

// Bad names
void test1()
void testGetUser()
void itWorks()
```

### 4. Don't Test Framework Code

```java
// WRONG - Testing Spring's functionality
@Test
void repositorySaveWorks() {
    User user = new User("John");
    User saved = repository.save(user);
    assertThat(saved.getId()).isNotNull();  // Testing JPA, not your code
}

// CORRECT - Test your business logic
@Test
void createUser_AssignsDefaultRole() {
    User user = userService.create(new User("John"));
    assertThat(user.getRole()).isEqualTo(Role.USER);  // Testing YOUR logic
}
```

### 5. Keep Tests Independent

```java
@BeforeEach
void setUp() {
    // Reset state before each test
    userRepository.deleteAll();
}
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Unit Tests | Test single components with mocked dependencies |
| @SpringBootTest | Full integration test with application context |
| @WebMvcTest | Test web layer only with MockMvc |
| @DataJpaTest | Test JPA repositories with embedded database |
| @MockBean | Replace beans with mocks in Spring context |
| Mockito | Mock dependencies: when/thenReturn, verify |
| AssertJ | Fluent assertions for readable tests |
| Best Practices | AAA pattern, one thing per test, descriptive names |

## Next Steps

Now that you understand Spring Boot testing basics, continue to [Spring MVC](../../16-spring-mvc/topics/01-spring-mvc-architecture.md) to learn about building web applications.
