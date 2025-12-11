# Exercise: Dependency Injection with Spring Framework

## Objective
Build a simple application demonstrating Spring Core concepts including IoC container, dependency injection, bean scopes, and AOP.

## Requirements

### Application: Notification Service
Create a notification system that can send messages via different channels (Email, SMS, Push).

### Components to Create

1. **Interfaces**
   - `NotificationService` - main service interface
   - `MessageFormatter` - formats messages
   - `NotificationSender` - sends notifications

2. **Implementations**
   - `EmailSender`, `SmsSender`, `PushNotificationSender`
   - `HtmlFormatter`, `PlainTextFormatter`
   - `NotificationServiceImpl` - orchestrates sending

3. **Configuration**
   - Java-based configuration (`@Configuration`)
   - Component scanning (`@ComponentScan`)
   - Qualifier for multiple implementations

### Spring Concepts to Demonstrate

1. **Dependency Injection**
   - Constructor injection (preferred)
   - Setter injection
   - Field injection with `@Autowired`

2. **Bean Configuration**
   - `@Component`, `@Service`, `@Repository`
   - `@Bean` methods in configuration class
   - `@Qualifier` for disambiguation

3. **Bean Scopes**
   - Singleton (default)
   - Prototype
   - Request/Session (mention for web)

4. **AOP (Aspect-Oriented Programming)**
   - Create logging aspect
   - Performance monitoring aspect
   - Use `@Before`, `@After`, `@Around`

### Project Structure
```
src/main/java/
├── config/
│   └── AppConfig.java
├── model/
│   └── Notification.java
├── service/
│   ├── NotificationService.java
│   ├── NotificationServiceImpl.java
│   ├── sender/
│   │   ├── NotificationSender.java
│   │   ├── EmailSender.java
│   │   └── SmsSender.java
│   └── formatter/
│       ├── MessageFormatter.java
│       └── HtmlFormatter.java
├── aspect/
│   └── LoggingAspect.java
└── Application.java
```

## Expected Output
```
INFO: Sending EMAIL notification to: user@example.com
INFO: [LoggingAspect] Method send() executed in 45ms
INFO: Notification sent successfully!
```

## Skills Tested
- Spring IoC container
- Dependency injection types
- Bean lifecycle and scopes
- Spring AOP basics
- Java-based configuration
