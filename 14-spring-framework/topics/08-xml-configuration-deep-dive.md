# XML Configuration Deep Dive

## Overview

XML was the original way to configure Spring applications. While modern applications prefer annotations and Java config, understanding XML configuration is valuable for:
- Maintaining legacy applications
- Understanding Spring's core concepts
- External configuration without recompilation

---

## Loading XML Configuration

### ClassPathXmlApplicationContext

The most common way to load XML configuration from the classpath.

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        // Load single file
        ApplicationContext context =
            new ClassPathXmlApplicationContext("applicationContext.xml");

        // Load multiple files
        ApplicationContext context2 = new ClassPathXmlApplicationContext(
            "beans.xml", "services.xml"
        );

        // Get beans
        UserService service = context.getBean("userService", UserService.class);

        // Close when done
        ((ClassPathXmlApplicationContext) context).close();
    }
}
```

### FileSystemXmlApplicationContext

Load XML from file system paths (useful for external configuration).

```java
import org.springframework.context.support.FileSystemXmlApplicationContext;

// Absolute path
ApplicationContext context = new FileSystemXmlApplicationContext(
    "/opt/myapp/config/beans.xml"
);
```

---

## BeanFactory

### What is BeanFactory?

`BeanFactory` is the root interface for accessing the Spring IoC container. It provides the basic functionality for managing beans - creating, configuring, and providing them to the application.

```
┌─────────────────────────────────────┐
│           BeanFactory               │  ← Root interface
│  - getBean()                        │
│  - containsBean()                   │
│  - isSingleton() / isPrototype()    │
└──────────────┬──────────────────────┘
               │ extends
┌──────────────▼──────────────────────┐
│        ApplicationContext           │  ← Adds enterprise features
│  - Event publishing                 │
│  - i18n support                     │
│  - Resource loading                 │
└─────────────────────────────────────┘
```

### BeanFactory Interface Methods

```java
public interface BeanFactory {
    // Get bean by name
    Object getBean(String name);

    // Get bean by name and type
    <T> T getBean(String name, Class<T> requiredType);

    // Get bean by type
    <T> T getBean(Class<T> requiredType);

    // Check if bean exists
    boolean containsBean(String name);

    // Check bean scope
    boolean isSingleton(String name);
    boolean isPrototype(String name);

    // Get bean type
    Class<?> getType(String name);
}
```

### Using BeanFactory

The `XmlBeanFactory` class is **deprecated** since Spring 3.1. Use `DefaultListableBeanFactory` with `XmlBeanDefinitionReader` instead.

**beans.xml:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userRepository" class="com.example.repository.UserRepositoryImpl"/>

    <bean id="userService" class="com.example.service.UserService">
        <constructor-arg ref="userRepository"/>
    </bean>

</beans>
```

**BeanFactory Example:**
```java
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class BeanFactoryExample {
    public static void main(String[] args) {
        // Create the factory
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // Load bean definitions from XML
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("beans.xml"));

        // Beans are NOT created yet (lazy initialization)
        System.out.println("Factory ready. Bean count: " + factory.getBeanDefinitionCount());

        // Bean created on first request
        UserService service = factory.getBean("userService", UserService.class);
        service.createUser("john", "john@example.com");

        // Check bean properties
        System.out.println("Is singleton: " + factory.isSingleton("userService"));
        System.out.println("Contains bean: " + factory.containsBean("userRepository"));
    }
}
```

### Deprecated XmlBeanFactory (for reference only)

```java
// OLD - Deprecated since Spring 3.1
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

BeanFactory factory = new XmlBeanFactory(new ClassPathResource("beans.xml"));
UserService service = (UserService) factory.getBean("userService");
```

---

## BeanFactory vs ApplicationContext

| Feature | BeanFactory | ApplicationContext |
|---------|-------------|-------------------|
| Bean Creation | Lazy (on-demand) | Eager (at startup) |
| Event Publishing | No | Yes |
| i18n Support | No | Yes |
| AOP Integration | Manual | Automatic |
| BeanPostProcessor | Manual registration | Automatic |
| Resource Loading | Basic | Enhanced |
| Usage | Resource-constrained environments | Standard choice (99% of cases) |

### When to Use BeanFactory

- Memory-constrained environments (embedded systems, mobile)
- Need complete control over bean initialization timing
- Simple applications with few beans

### When to Use ApplicationContext

- All standard applications (recommended)
- Need enterprise features (events, i18n)
- Web applications
- Using AOP, transactions, or security

**Recommendation:** Always use `ApplicationContext` unless you have specific constraints.

---

## XML Bean Definition Basics

### Basic Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Simple bean -->
    <bean id="userRepository" class="com.example.repository.UserRepositoryImpl"/>

    <!-- Bean with constructor injection -->
    <bean id="userService" class="com.example.service.UserService">
        <constructor-arg ref="userRepository"/>
    </bean>

    <!-- Bean with property injection -->
    <bean id="emailService" class="com.example.service.EmailService">
        <property name="host" value="smtp.gmail.com"/>
        <property name="port" value="587"/>
    </bean>

</beans>
```

### Constructor Injection

```xml
<!-- Single argument -->
<bean id="userService" class="com.example.UserService">
    <constructor-arg ref="userRepository"/>
</bean>

<!-- Multiple arguments -->
<bean id="orderService" class="com.example.OrderService">
    <constructor-arg ref="orderRepository"/>
    <constructor-arg ref="emailService"/>
    <constructor-arg value="Premium"/>
</bean>

<!-- By index (when types are ambiguous) -->
<bean id="paymentService" class="com.example.PaymentService">
    <constructor-arg index="0" ref="paymentGateway"/>
    <constructor-arg index="1" value="USD"/>
</bean>
```

### Property (Setter) Injection

```xml
<!-- Simple values -->
<bean id="dataSource" class="com.example.DataSource">
    <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
    <property name="username" value="root"/>
    <property name="password" value="secret"/>
</bean>

<!-- Reference to another bean -->
<bean id="userService" class="com.example.UserService">
    <property name="repository" ref="userRepository"/>
</bean>

<!-- Collections -->
<bean id="reportService" class="com.example.ReportService">
    <property name="recipients">
        <list>
            <value>admin@example.com</value>
            <value>manager@example.com</value>
        </list>
    </property>
    <property name="settings">
        <map>
            <entry key="format" value="PDF"/>
            <entry key="compress" value="true"/>
        </map>
    </property>
</bean>
```

### Shorthand with p-namespace and c-namespace

```xml
<beans xmlns:p="http://www.springframework.org/schema/p"
       xmlns:c="http://www.springframework.org/schema/c">

    <!-- p-namespace for properties -->
    <bean id="dataSource" class="com.example.DataSource"
          p:url="jdbc:mysql://localhost/mydb"
          p:username="root"
          p:password="secret"/>

    <!-- c-namespace for constructor -->
    <bean id="userService" class="com.example.UserService"
          c:repository-ref="userRepository"/>

</beans>
```

---

## Bean Inheritance

Share common configuration across multiple beans using the `parent` attribute.

```xml
<!-- Abstract parent bean (cannot be instantiated) -->
<bean id="baseService" abstract="true">
    <property name="timeout" value="30000"/>
    <property name="retryCount" value="3"/>
</bean>

<!-- Child beans inherit from parent -->
<bean id="userService" class="com.example.UserService" parent="baseService">
    <property name="repository" ref="userRepository"/>
</bean>

<bean id="orderService" class="com.example.OrderService" parent="baseService">
    <property name="repository" ref="orderRepository"/>
    <property name="timeout" value="60000"/> <!-- Override parent value -->
</bean>
```

---

## Factory Methods

Create beans through factory methods instead of direct instantiation.

```xml
<!-- Static factory method -->
<bean id="singleton" class="com.example.MySingleton" factory-method="getInstance"/>

<!-- Instance factory method -->
<bean id="connectionFactory" class="com.example.ConnectionFactory"/>

<bean id="connection" factory-bean="connectionFactory" factory-method="createConnection"/>
```

```java
public class MySingleton {
    private static final MySingleton INSTANCE = new MySingleton();

    public static MySingleton getInstance() {
        return INSTANCE;
    }
}

public class ConnectionFactory {
    public Connection createConnection() {
        return new Connection();
    }
}
```

---

## Useful Bean Attributes

```xml
<!-- Lazy initialization - created only when first requested -->
<bean id="heavyService" class="com.example.HeavyService" lazy-init="true"/>

<!-- Initialization order - ensure cacheWarmer runs before this bean -->
<bean id="userService" class="com.example.UserService" depends-on="cacheWarmer"/>

<!-- Lifecycle callbacks -->
<bean id="dbConnection" class="com.example.DbConnection"
      init-method="connect"
      destroy-method="disconnect"/>

<!-- Bean scope -->
<bean id="shoppingCart" class="com.example.ShoppingCart" scope="prototype"/>
```

---

## Multi-File Configuration

### Using import

```xml
<!-- applicationContext.xml -->
<beans>
    <import resource="database-config.xml"/>
    <import resource="service-config.xml"/>

    <!-- Classpath explicit -->
    <import resource="classpath:config/security.xml"/>
</beans>
```

### Property Placeholders

```xml
<beans xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="...
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- Load properties file -->
    <context:property-placeholder location="classpath:application.properties"/>

    <!-- Use placeholders -->
    <bean id="dataSource" class="com.example.DataSource">
        <property name="url" value="${db.url}"/>
        <property name="username" value="${db.username}"/>
        <property name="password" value="${db.password}"/>
        <property name="maxPoolSize" value="${db.pool.size:10}"/> <!-- default value -->
    </bean>

</beans>
```

**application.properties:**
```properties
db.url=jdbc:mysql://localhost:3306/myapp
db.username=app_user
db.password=secret123
```

---

## Complete Example

### Project Structure
```
src/main/
├── java/com/example/
│   ├── Application.java
│   ├── model/User.java
│   ├── repository/UserRepository.java
│   └── service/UserService.java
└── resources/
    ├── applicationContext.xml
    └── application.properties
```

### applicationContext.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:application.properties"/>

    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
        <property name="jdbcUrl" value="${db.url}"/>
        <property name="username" value="${db.username}"/>
        <property name="password" value="${db.password}"/>
    </bean>

    <bean id="userRepository" class="com.example.repository.UserRepositoryImpl">
        <constructor-arg ref="dataSource"/>
    </bean>

    <bean id="userService" class="com.example.service.UserService">
        <constructor-arg ref="userRepository"/>
    </bean>

</beans>
```

### Application.java
```java
package com.example;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml")) {

            UserService userService = context.getBean("userService", UserService.class);

            userService.createUser("john", "john@example.com");
            System.out.println(userService.getAllUsers());
        }
    }
}
```

---

## Summary

| Concept | Usage |
|---------|-------|
| `ClassPathXmlApplicationContext` | Load XML from classpath (most common) |
| `<constructor-arg>` | Constructor injection |
| `<property>` | Setter injection |
| `ref` vs `value` | Bean reference vs literal value |
| `parent` | Bean inheritance |
| `factory-method` | Create via factory |
| `lazy-init` | Delay bean creation |
| `<import>` | Split configuration into multiple files |
| `${property}` | Externalize configuration values |

## Next Topic

Continue to [Dependency Injection](./05-dependency-injection.md) to learn more about DI patterns.
