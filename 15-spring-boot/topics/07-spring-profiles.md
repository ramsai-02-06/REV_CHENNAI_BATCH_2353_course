# Spring Profiles

## What are Spring Profiles?

Spring Profiles provide a way to segregate parts of your application configuration and make it available only in certain environments. This allows you to have different configurations for development, testing, staging, and production environments without changing code.

### Why Use Profiles?

Different environments require different configurations:

| Environment | Database | Logging | Debug | External Services |
|-------------|----------|---------|-------|-------------------|
| Development | H2 in-memory | DEBUG | Enabled | Mock services |
| Testing | H2/TestContainers | INFO | Enabled | Mock services |
| Staging | MySQL (staging) | INFO | Disabled | Real services |
| Production | MySQL (prod) | WARN | Disabled | Real services |

### Profile Benefits

1. **Environment Isolation**: Different settings per environment
2. **Security**: Keep production secrets separate
3. **Flexibility**: Switch configurations without code changes
4. **Testing**: Use lighter configurations for tests
5. **Local Development**: Developer-friendly settings

---

## Creating Profile-Specific Configuration

### Method 1: Separate Properties Files

Create files named `application-{profile}.properties` or `application-{profile}.yml`:

```
src/main/resources/
├── application.properties           # Common settings (all profiles)
├── application-dev.properties       # Development settings
├── application-test.properties      # Testing settings
├── application-staging.properties   # Staging settings
└── application-prod.properties      # Production settings
```

**application.properties (Common settings):**
```properties
# Common to all profiles
spring.application.name=my-app
server.port=8080

# Default profile if none specified
spring.profiles.active=dev
```

**application-dev.properties:**
```properties
# Development Database - H2 in-memory
spring.datasource.url=jdbc:h2:mem:devdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# Show SQL queries
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create-drop

# Development logging
logging.level.root=INFO
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG

# Enable H2 console
spring.h2-console.enabled=true

# Development features
app.feature.debug-mode=true
app.feature.mock-external-services=true
```

**application-prod.properties:**
```properties
# Production Database - MySQL
spring.datasource.url=jdbc:mysql://prod-db-server:3306/myapp
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Don't show SQL in production
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate

# Production logging
logging.level.root=WARN
logging.level.com.example=INFO

# Disable H2 console
spring.h2-console.enabled=false

# Production features
app.feature.debug-mode=false
app.feature.mock-external-services=false
```

### Method 2: YAML with Profile Sections

Use a single `application.yml` with profile separators:

```yaml
# application.yml

# Common settings
spring:
  application:
    name: my-app

server:
  port: 8080

---
# Development Profile
spring:
  config:
    activate:
      on-profile: dev

  datasource:
    url: jdbc:h2:mem:devdb
    username: sa
    password: ""

  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop

  h2:
    console:
      enabled: true

logging:
  level:
    root: INFO
    com.example: DEBUG

app:
  feature:
    debug-mode: true

---
# Production Profile
spring:
  config:
    activate:
      on-profile: prod

  datasource:
    url: jdbc:mysql://prod-db-server:3306/myapp
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    root: WARN
    com.example: INFO

app:
  feature:
    debug-mode: false
```

---

## Activating Profiles

### Method 1: In application.properties

```properties
# Set default active profile
spring.profiles.active=dev
```

### Method 2: Command Line Argument

```bash
# Run with specific profile
java -jar myapp.jar --spring.profiles.active=prod

# Multiple profiles
java -jar myapp.jar --spring.profiles.active=prod,metrics
```

### Method 3: Environment Variable

```bash
# Set environment variable
export SPRING_PROFILES_ACTIVE=prod

# Then run the application
java -jar myapp.jar
```

### Method 4: JVM System Property

```bash
java -Dspring.profiles.active=prod -jar myapp.jar
```

### Method 5: In IDE

**IntelliJ IDEA:**
1. Run → Edit Configurations
2. Select your application
3. Add to VM options: `-Dspring.profiles.active=dev`
4. Or add to Environment variables: `SPRING_PROFILES_ACTIVE=dev`

**Eclipse/STS:**
1. Run → Run Configurations
2. Arguments tab
3. VM arguments: `-Dspring.profiles.active=dev`

### Method 6: Programmatically

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setAdditionalProfiles("dev");  // Set profile programmatically
        app.run(args);
    }
}
```

---

## Profile Precedence

When multiple configuration sources exist, Spring Boot uses this precedence (highest to lowest):

1. Command line arguments (`--spring.profiles.active=prod`)
2. JVM system properties (`-Dspring.profiles.active=prod`)
3. Environment variables (`SPRING_PROFILES_ACTIVE=prod`)
4. `application.properties` in the working directory
5. `application.properties` in the classpath

**Property Resolution Order:**
```
1. application-{profile}.properties (profile-specific)
2. application.properties (common)
```

Profile-specific properties always override common properties.

---

## Profile-Specific Beans

Use `@Profile` annotation to create beans only for specific profiles.

### Basic Profile-Specific Beans

```java
// Interface
public interface NotificationService {
    void sendNotification(String message);
}

// Development implementation - logs to console
@Service
@Profile("dev")
public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message) {
        System.out.println("[DEV] Notification: " + message);
    }
}

// Production implementation - sends real emails
@Service
@Profile("prod")
public class EmailNotificationService implements NotificationService {

    @Autowired
    private EmailClient emailClient;

    @Override
    public void sendNotification(String message) {
        emailClient.send("admin@company.com", message);
    }
}
```

### Using the Beans

```java
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;  // Correct one injected based on profile

    @PostMapping("/notify")
    public String notify(@RequestBody String message) {
        notificationService.sendNotification(message);
        return "Notification sent";
    }
}
```

### Profile Expressions

```java
// Active in dev OR test profile
@Service
@Profile({"dev", "test"})
public class MockPaymentService implements PaymentService { }

// Active when NOT in prod profile
@Service
@Profile("!prod")
public class DebugService { }

// Active in prod AND secure profiles (both must be active)
@Configuration
@Profile("prod & secure")
public class SecureProductionConfig { }

// Active in dev OR (prod AND us-east)
@Configuration
@Profile("dev | (prod & us-east)")
public class RegionalConfig { }
```

### Profile-Specific Configuration Classes

```java
@Configuration
@Profile("dev")
public class DevConfig {

    @Bean
    public DataSource dataSource() {
        // H2 in-memory database for development
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("schema.sql")
            .addScript("test-data.sql")
            .build();
    }

    @Bean
    public CacheManager cacheManager() {
        // Simple cache for development
        return new ConcurrentMapCacheManager();
    }
}

@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public DataSource dataSource() {
        // HikariCP connection pool for production
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(System.getenv("DB_URL"));
        config.setUsername(System.getenv("DB_USERNAME"));
        config.setPassword(System.getenv("DB_PASSWORD"));
        config.setMaximumPoolSize(20);
        return new HikariDataSource(config);
    }

    @Bean
    public CacheManager cacheManager() {
        // Redis cache for production
        return new RedisCacheManager(redisConnectionFactory());
    }
}
```

---

## Default Profile

Spring Boot allows you to define a default profile that activates when no profile is explicitly set.

### Setting Default Profile

```properties
# application.properties
spring.profiles.default=dev
```

Or programmatically:

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(
            Collections.singletonMap("spring.profiles.default", "dev")
        );
        app.run(args);
    }
}
```

### Default Profile Beans

```java
// This bean is created only when no profile matches
@Service
@Profile("default")
public class DefaultNotificationService implements NotificationService {
    // Fallback implementation
}
```

---

## Multiple Active Profiles

You can activate multiple profiles simultaneously:

```properties
# Multiple profiles
spring.profiles.active=prod,metrics,us-east
```

**Use Case Example:**

```
Profiles:
- prod          → Production database, logging
- metrics       → Enables metrics collection
- us-east       → US East region specific settings
- secure        → Enhanced security settings
```

```java
@Configuration
@Profile("metrics")
public class MetricsConfig {
    @Bean
    public MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry();
    }
}

@Configuration
@Profile("us-east")
public class UsEastConfig {
    @Value("${aws.region:us-east-1}")
    private String region;

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .build();
    }
}
```

---

## Profile Groups (Spring Boot 2.4+)

Group multiple profiles together for easier activation:

```properties
# application.properties

# Define profile groups
spring.profiles.group.production=prod,metrics,secure
spring.profiles.group.development=dev,local-db,debug
```

Now activating `production` automatically activates `prod`, `metrics`, and `secure`:

```bash
java -jar myapp.jar --spring.profiles.active=production
# Activates: prod, metrics, secure
```

---

## Checking Active Profiles

### In Code

```java
@Service
public class ProfileAwareService {

    @Autowired
    private Environment environment;

    public void checkProfiles() {
        // Get active profiles
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println("Active profiles: " + Arrays.toString(activeProfiles));

        // Check specific profile
        if (Arrays.asList(activeProfiles).contains("prod")) {
            System.out.println("Running in production mode");
        }

        // Using acceptsProfiles
        if (environment.acceptsProfiles(Profiles.of("dev", "test"))) {
            System.out.println("Running in development or test mode");
        }
    }
}
```

### At Startup

Spring Boot logs active profiles at startup:

```
2024-01-15 10:30:00.000  INFO --- [main] c.e.Application:
The following profiles are active: dev, local
```

---

## Practical Examples

### Example 1: Database Configuration by Profile

```java
@Configuration
public class DatabaseConfig {

    @Bean
    @Profile("dev")
    public DataSource h2DataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:devdb")
            .username("sa")
            .password("")
            .build();
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:testdb;MODE=MySQL")
            .username("sa")
            .password("")
            .build();
    }

    @Bean
    @Profile("prod")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create().build();
    }
}
```

### Example 2: External Service Configuration

```java
// Mock service for development
@Service
@Profile("dev")
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // Simulate successful payment
        return new PaymentResult("MOCK-" + UUID.randomUUID(), "SUCCESS");
    }
}

// Real service for production
@Service
@Profile("prod")
public class StripePaymentGateway implements PaymentGateway {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        // Real Stripe integration
        Stripe.apiKey = apiKey;
        // ... actual payment processing
    }
}
```

### Example 3: Feature Flags with Profiles

```java
@Configuration
public class FeatureConfig {

    @Bean
    @Profile("dev")
    public FeatureFlags devFeatures() {
        return FeatureFlags.builder()
            .enableBetaFeatures(true)
            .enableDebugEndpoints(true)
            .enableMockResponses(true)
            .build();
    }

    @Bean
    @Profile("prod")
    public FeatureFlags prodFeatures() {
        return FeatureFlags.builder()
            .enableBetaFeatures(false)
            .enableDebugEndpoints(false)
            .enableMockResponses(false)
            .build();
    }
}
```

---

## Best Practices

### 1. Keep Sensitive Data Out of Properties Files

```properties
# application-prod.properties

# WRONG - Don't commit secrets!
spring.datasource.password=MySecretPassword123

# CORRECT - Use environment variables
spring.datasource.password=${DB_PASSWORD}
```

### 2. Use Profiles for Environment Differences Only

```java
// WRONG - Using profile for feature toggle
@Service
@Profile("newCheckoutEnabled")
public class NewCheckoutService { }

// CORRECT - Use feature flags instead
@Service
@ConditionalOnProperty(name = "feature.new-checkout", havingValue = "true")
public class NewCheckoutService { }
```

### 3. Always Have a Sensible Default

```properties
# application.properties
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}
```

### 4. Document Your Profiles

```yaml
# application.yml

# Profile documentation:
# - dev: Local development with H2 database
# - test: Automated testing configuration
# - staging: Pre-production with real database
# - prod: Production environment

spring:
  profiles:
    active: dev
```

### 5. Use Profile Groups for Complex Setups

```properties
# Instead of: --spring.profiles.active=prod,aws,us-east,secure,logging
spring.profiles.group.production-us=prod,aws,us-east,secure,logging
# Now just: --spring.profiles.active=production-us
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| Profiles | Segregate configuration by environment |
| Activation | Properties file, command line, environment variable, programmatic |
| Profile Files | `application-{profile}.properties/yml` |
| @Profile | Create beans for specific profiles |
| Profile Expressions | Combine with `!`, `&`, `\|` |
| Profile Groups | Group multiple profiles together |
| Best Practices | Don't commit secrets, use for environments only |

## Next Topic

Continue to [Testing Basics](./08-testing-basics.md) to learn how to test your Spring Boot applications.
