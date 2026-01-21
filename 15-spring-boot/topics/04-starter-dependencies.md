# Starter Dependencies

## Introduction

Spring Boot Starter Dependencies are pre-configured dependency descriptors that simplify your project setup. They bring in all libraries needed for specific functionality, with versions tested to work together.

**Key Benefits:**
- Simplified dependency management
- Version compatibility guaranteed
- No manual version hunting
- Quick project setup

---

## How Starters Work

### Traditional vs Spring Boot Approach

```xml
<!-- Traditional: Manage many dependencies manually -->
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
        <version>5.3.20</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.3.20</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.13.3</version>
    </dependency>
    <!-- ... many more -->
</dependencies>

<!-- Spring Boot: Single starter brings everything -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <!-- No version needed - managed by parent -->
    </dependency>
</dependencies>
```

---

## Common Starters Reference

### Web Starters

| Starter | Purpose |
|---------|---------|
| `spring-boot-starter-web` | Web applications with Spring MVC, Tomcat, Jackson |
| `spring-boot-starter-webflux` | Reactive web applications with Netty |
| `spring-boot-starter-thymeleaf` | Server-side HTML with Thymeleaf |

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Data Starters

| Starter | Purpose |
|---------|---------|
| `spring-boot-starter-data-jpa` | JPA with Hibernate, HikariCP |
| `spring-boot-starter-jdbc` | JDBC with connection pooling |
| `spring-boot-starter-data-mongodb` | MongoDB support |
| `spring-boot-starter-data-redis` | Redis support |

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Security Starter

| Starter | Purpose |
|---------|---------|
| `spring-boot-starter-security` | Spring Security with defaults |
| `spring-boot-starter-oauth2-client` | OAuth 2.0 client |

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Testing Starter

| Starter | Purpose |
|---------|---------|
| `spring-boot-starter-test` | JUnit 5, Mockito, AssertJ, Spring Test |

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Production Starters

| Starter | Purpose |
|---------|---------|
| `spring-boot-starter-actuator` | Health checks, metrics, monitoring |
| `spring-boot-starter-validation` | Bean validation (JSR-380) |

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## Spring Boot Parent POM

The `spring-boot-starter-parent` provides:
- Dependency version management
- Plugin configuration
- Default properties (Java version, encoding)

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>

<properties>
    <java.version>17</java.version>
</properties>

<dependencies>
    <!-- No versions needed - managed by parent -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### Without Parent POM (BOM Import)

If you already have a corporate parent POM:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>3.2.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## Common Tasks

### Switching Embedded Server

```xml
<!-- Exclude Tomcat, use Jetty -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

### Override Dependency Version

```xml
<properties>
    <jackson.version>2.15.0</jackson.version>
</properties>
```

### View Dependency Tree

```bash
# See all dependencies and their versions
mvn dependency:tree
```

---

## Typical Project Dependencies

### REST API Project

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Secure Web Application

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

---

## Summary

| Concept | Key Points |
|---------|------------|
| What are Starters | Pre-configured dependency bundles |
| Benefits | No version management, tested compatibility |
| Parent POM | Provides version management for all starters |
| Common Starters | web, data-jpa, security, test, actuator |
| Customization | Exclude dependencies, override versions |

## Next Topic

Continue to [Spring Boot CLI](./05-spring-boot-cli.md) to learn about project generation from command line.
