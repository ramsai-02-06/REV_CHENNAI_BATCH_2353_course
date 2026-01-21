# Spring Boot

## Overview

Spring Boot simplifies Spring application development by providing auto-configuration, embedded servers, and opinionated defaults. It eliminates boilerplate configuration, letting you focus on business logic.

## Learning Objectives

By the end of this module, you will be able to:
- Understand how Spring Boot simplifies Spring development
- Use auto-configuration and starter dependencies effectively
- Configure applications using properties and profiles
- Write tests for Spring Boot applications
- Build production-ready applications

---

## Topics Covered

### 1. [What is Spring Boot?](./topics/01-what-is-spring-boot.md)
Introduction to Spring Boot and its core philosophy. Learn why Spring Boot was created and how it simplifies application development.

- Spring Boot overview and benefits
- Core features (auto-config, starters, embedded servers)
- Spring Boot architecture
- Creating your first Spring Boot application
- @SpringBootApplication annotation

### 2. [Spring Boot vs Spring Framework](./topics/02-spring-boot-vs-spring-framework.md)
Understand the relationship between Spring Boot and Spring Framework. Learn when to use each and how they complement each other.

- Key differences and similarities
- Configuration comparison (XML vs auto-config)
- Dependency management approaches
- Deployment differences (WAR vs JAR)
- Migration considerations

### 3. [Auto-configuration](./topics/03-auto-configuration.md)
Learn how Spring Boot automatically configures your application based on classpath dependencies. Understand the magic that makes Spring Boot work.

- How auto-configuration works
- @EnableAutoConfiguration and @SpringBootApplication
- Conditional annotations (@ConditionalOnClass, @ConditionalOnBean)
- Customizing and excluding auto-configuration
- Debugging with the conditions report

### 4. [Starter Dependencies](./topics/04-starter-dependencies.md)
Master Spring Boot's dependency management system. Learn how starters bundle related dependencies with compatible versions.

- What are starter dependencies
- Common starters (web, data-jpa, security, test)
- Spring Boot parent POM
- BOM import for existing projects
- Excluding and overriding dependencies

### 5. [Spring Boot CLI](./topics/05-spring-boot-cli.md)
Quick introduction to the Spring Boot command-line tool for rapid prototyping and project generation.

- Installing Spring Boot CLI
- Generating projects with `spring init`
- Running Groovy scripts
- When to use CLI vs traditional approach

### 6. [application.properties / .yml](./topics/06-application-properties-yml.md)
Configure your Spring Boot application using external properties. Learn the different configuration formats and property precedence.

- Properties vs YAML formats
- Common configuration properties
- Custom properties with @ConfigurationProperties
- Property precedence rules
- Externalized configuration

### 7. [Spring Profiles](./topics/07-spring-profiles.md)
Manage environment-specific configuration using profiles. Run the same application code in different environments seamlessly.

- What are profiles and why use them
- Profile-specific configuration files
- Activating profiles (properties, CLI, environment)
- @Profile annotation for conditional beans
- Profile groups and expressions

### 8. [Testing Basics](./topics/08-testing-basics.md)
Write effective tests for Spring Boot applications. Learn the testing pyramid and Spring Boot's testing support.

- Testing pyramid (unit, integration, e2e)
- spring-boot-starter-test dependencies
- Unit testing with Mockito
- @SpringBootTest for integration tests
- @WebMvcTest and @DataJpaTest slices
- @MockBean for mocking dependencies

---

## Topic Flow

```
┌─────────────────────┐
│ 1. What is Boot?    │  Overview and benefits
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 2. Boot vs Spring   │  Understanding the relationship
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 3. Auto-config      │  How the magic works
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 4. Starters         │  Dependency management
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 5. CLI              │  Project generation
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 6. Properties       │  Application configuration
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 7. Profiles         │  Environment management
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ 8. Testing          │  Testing Spring Boot apps
└─────────────────────┘
```

---

## Key Concepts Summary

| Concept | Description |
|---------|-------------|
| **Auto-configuration** | Automatic bean configuration based on classpath |
| **Starters** | Pre-configured dependency bundles |
| **Embedded Server** | Tomcat/Jetty included, no external deployment |
| **Properties** | External configuration via .properties or .yml |
| **Profiles** | Environment-specific configuration |
| **Actuator** | Production monitoring and management |

---

## Exercises

See the [exercises](./exercises/) directory for hands-on practice problems.

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Initializr](https://start.spring.io/)
- [Baeldung Spring Boot Tutorials](https://www.baeldung.com/spring-boot)

---

## Next Steps

After completing this module, continue to **Spring MVC** to learn about building web applications and REST APIs with Spring Boot.

---

**Duration:** 4 days | **Difficulty:** Intermediate | **Prerequisites:** Module 14 (Spring Framework)
